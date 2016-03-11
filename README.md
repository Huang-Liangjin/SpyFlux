## SpyFluxってなに
Androidアプリ向けのソフトウェアアーキテクチャです。
ライブラリではなく、フレームワークでもない、デザインパターン（ルール）みたいなものです。
新規アプリを開発する際、このアーキテクチャが定めたルールに従えば、アプリの各部分は機能ことに綺麗に分けることができ、
コードも綺麗に組織することができて、後期アプリのメンテナンスコストも下りられます。

## SpyFluxが解決したい問題
現在、Androidアプリの開発には、幾つか共通の問題があります:
- Activityの膨大化  
MVCの視点から見ると、ActivityはViewであり、Controllerでもあります。
ActivityはユーザとインタラクションするUI部分の描画、ユーザイベントの監視、ユーザイベントのリスポンスの反映などのUI仕事をやる一方で、
ユーザイベントに対する処理（ビジネスロジック）も負担しています。
- ユニットテストの難しさ  
ビジネスロジックとUIがくっつけているせいで、ユニットテストがしづらいです。
（例えば、あるメソッドは、データを処理して、処理結果を直接あるTextViewのsetText()関数を使ってUIに反映する）
- メンテナンスコストの高さ  
初期で綺麗なコード組織できてないため、新規機能追加、Debug、人員変動によりプロジェクトの後継など発生する場合、それを対応するコストが高まります。

これらの問題を解決するために、SpyFluxというAndroid向けのアプリ構築アーキテクチャが作られました。

## 予備知識
### [Flux](http://facebook.github.io/flux/)
Flux Architectureは、Facebookが作られた、クライアント側のウェブアプリケーションを構築するためのアーキテクチャです。
![Flux Architecture](http://facebook.github.io/flux/img/flux-simple-f8-diagram-explained-1300w.png)

Fluxはアプリケーションを四つの部分に分けます：
#### View
ユーザとやり取りするUI部分です。  
主な役目は
- UIの各要素・レイアウトを構成する
- ユーザの操作を受け、その操作にたいする処理リクエストを生成する
- リクエスト処理モジュールの処理結果を表示する

#### Dispatcher
Dispatcherはプロジェクトの中の位置付けはCenter Hubです。
Viewと処理モジュール（Store）の間の通信は、全てDispatcher経由です。  
主な役目は
- Viewに生成されたリクエストを処理モジュールに渡す
- 処理モジュールの処理結果をViewに渡す

#### Store
プロジェクトの全てのビジネスロジックはStoreの中にあります。
Storeは、データ倉庫であり、データに対する操作の集合でもあります。  
主な役目は：
- Dispatcherからユーザのリクエストを受け、それを解析して、どんな処理を行うのを判断する
- そのリクエストに対する適切な処理を行う
- 処理結果を生成して、Dispatcherに渡す

#### Action
アプリ内流されたデータはActionという形として存在しています。
ActionはType域とData域を含めます。
Typeはビジネスロジックの種類であり、StoreとViewをペアリングするためのkeyでもあります。
一つのプロジェクトの中に、必ず複数のStoreとViewがあって、
ViewのリクエストはどのStoreに送るか、
Storeの処理結果はどのViewに渡すべきか、
全部ActionのTypeに決められます。

### [Event Bus](https://github.com/greenrobot/EventBus)
Android向けのPub/Subのライブラリです。
今回SpyDispatcherの実装は、Event Busを借りて、作ってきました。

## SpyFlux アーキテクチャの詳細
SpyFluxはFlux Architectureの思想を吸収した上で作られたAndorid向けのアプリアーキテクチャです。
Fluxの仕組みを理解すれば、SpyFluxもほぼ把握済みです。

ただ、SpyFluxとFluxの唯一違うところは、ViewからStoreにリクエストを送る際、Dispatcherに経由しないことです。
理由としては、Storeはシングルートンとして実装されるPOJOですので、Storeを使いたいとき、Store.getInstance()を呼べば、
いつでも、どこでも使えます。わざわざdispatcher経由する必要はありません。さらに、Storeの中にあるメソッドは全て同期メソッド
（実行コストが高くない、UI-threadで呼ばれてもUI-threadに負担かからない）ですので、UIパフォーマンスに影響しません。

SpyFluxを導入しやすいため、そのルールを具体化して、AndroidStudioのsubmoduleとして作られました。
そのmoduleの構成は：
```
├── action
│   ├── SpyActionCreator.java
│   ├── SpyStoreAction.java
│   └── SpyViewAction.java
├── dispatcher
│   └── SpyDispatcher.java
├── store
│   └── SpyStore.java
├── ui
│   └── SpyView.java
└── util
    └── SpyLog.java
```
となります。

### ui.SpyView
プロジェクトのActivity, Fragmentなど、Storeとやり取りする必要があるViewは、このInterfaceをimplementすべきです。
Viewは、onReceiveStoreMsg(SpyStoreAction action)関数の中で、StoreにDispatcher経由で送られたActionのtypeをチェックして、データを取り出してUIに反映します。

### store.SpyStore
全てのStoreは、このinterfaceをimplementすべきです。
Storeは、onProcessViewReqeust(SpyViewAction)関数で、
ViewからもらったSpyViewActionのタイプを解析し、それに合わせる処理メソッドを呼びます。
処理結果を新たなSpyStoreActionとしてカプセルして、Dispatcher経由でViewに渡します。  
UIからStoreにリクエスト送る時に、Store.getInstance().onProcessViewReqeust(SpyViewAction)を使ってください。  
Storeは、static シングルートンとして実装すべきです。

### action.SpyActionCreator
Storeは、Viewのリクエストを処理する時に、
場合（network経由の操作、DBのアクセスなど時間かかる操作など）によって、非同期にする必要があります。
Storeの同期性を保つため、
これらの非同期操作は、Storeではなく、Storeに所属しているXXXActionCreatorに譲るべきです。
ActionCreatorは、データの生成操作はもちろん、一部Storeの重いビジネスロジックメソッドも実装します。  
全てのActionCreatorは、この抽象クラスをextendすべきです。  
ActionCreator objectを作る時に、このActionCreatorが所属するStoreをパラメータとする必要があります。  

### dispatcher.SpyDispatcher
シングルートンオブジェクトです。アプリ起動時点で作られます。
Finalタイプにして拡張できないです。
主な役割は、Viewをsubscriberとしての登録と解除と、Storeで生成したStroeActionをViewに送ることです。

### action.SpyStoreAction
Store側生成して、Viewに渡すActionです。
その中に、このビジネスロジックの名前(Type)とUI側の必要なデータが内包しています。Storeは、Dispatcher経由で、このSpyStoreActionをViewに渡す

### action.SpyViewAction
View側生成して、Storeに渡すActionです。
その中にユーザのリクエストデータ（ユーザのログイン情報、ユーザが検査したいkeyword、クリックしたイテムのID, ボタンの番号など）と、リクエストの名前(TYpe)が入ってます。
ViewはこのSpyViewActionを、dispatcher経由せずに、直接Storeに渡します。


## SpyFluxの使い方
[SpyFluxExample](https://github.com/Huang-Liangjin/SpyFluxExample)の方を参照してください。

## 参考資料
[Flux](http://facebook.github.io/flux/)  
[Flux Architecture on Android](http://lgvalle.xyz/2015/08/04/flux-architecture/)  
[RxFlux Android Architecture](https://medium.com/swlh/rxflux-android-architecture-94f77c857aa2#.lgdqvvjpu)  

