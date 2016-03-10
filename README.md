## SpyFluxってなに
Androidアプリ向けのソフトウェアアーキテクチャです。
ライブラリではなく、フレームワークでもない、デザインパターン（ルール）みたいなものです。

## SpyFluxが解決したい問題
現在、Androidアプリの開発には、幾つか共通の問題があります:
- Activityの膨大化
MVCの視点から見ると、ActivityはViewであり、Controllerでもあります。
ActivityはユーザとインタラクションするUIの描画、ユーザイベントの監視、それに対するリスポンスなどのUI仕事をやる一方で、
ユーザリクエストに対する処理（ビジネスロジック）も負担しています。
- ユニットテストの難しさ
ビジネスロジックとUIがくっつけているせいで、ユニットテストがしづらいです。
（例えば、あるメソッドは、データを処理して、処理結果を直接あるTextViewのsetText()関数を使ってUIに反映する）
- メンテナンスコストの高さ
初期で綺麗なコード組織できてないため、新規機能追加、Debug、人員変動によりプロジェクトの後継などのコストが高くなります。

これらの問題を解決するために、SpyFluxというAndroid向けのアプリ構築アーキテクチャが作られました。

## 予備知識
### [Flux](http://facebook.github.io/flux/)
Flux Architectureは、Facebookが作られた、クライアント側のウェブアプリケーションを構築するためのアーキテクチャです。
![Flux Architecture](http://facebook.github.io/flux/img/flux-simple-f8-diagram-explained-1300w.png)

上の図から見ると、Fluxはアプリケーションを四つの部分として分けています：
#### View
ユーザとやり取りする部分です。主な役目は
- ユーザに見せるUI部分のレイアウト
- ユーザの操作を受け、その操作にたいする処理リクエストを生成し、アプリのリクエスト処理モジュール(Store)に渡す
- リクエスト処理モジュールの処理結果を表示する

#### Dispatcher
Dispatcherは、一つのプロジェクトの中に一つしかないです。
位置付けはCenter Hubです。
Viewと処理モジュール（Store）の間の通信は、全てDispatcher経由です。

#### Store
プロジェクトの全てのビジネスロジックはStoreの中にあります。
Storeは、データ倉庫であり、データに対する操作の集合でもあります。
主な役目は：
- Dispatcherからユーザのリクエストを受ける
- そのリクエストに対する適切な処理を行う
- 処理した結果をDispatcher経由でViewに渡す。

#### Action
データフローで流されたデータはActionという形として定義されています。
ActionはType域とData域を含めます。
TypeはStoreとViewをペアリングするためのkeyです。
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

### action.SpyActionCreator
XXXStoreで処理するデータは、非同期でネットやDBから取得する必要がある場合、または、
XXXStoreのある処理自体は重くて、非同期でやる必要がある場合、
その操作は、XXXStoreではなく、XXXStoreに所属しているXXXActionCreatorに譲るべきです。

### action.SpyStoreAction
Store側はSpyStoreActionを生成して、その中に、UI側が必要となるデータが内包しています。Storeは、Dispatcher経由で、このSpyStoreActionをViewに渡す

### action.SpyViewAction
View側はSpyViewActionを生成します。その中にユーザのリクエストデータ（クリックしたイテムのID, ボタンの番号など）が入ってます。ViewはこのSpyViewActionを、dispatcher経由せずに、直接Storeに渡します。

### dispatcher.SpyDispatcher
シングルートンクラスです。一つのアプリ内で一つのSpyDispatcherしかないです。
中枢的なもので、すべてのViewを監視し、Action（データ）のデータフローをコントロールします。
主な役割は、Viewの登録と解除と、Storeで生成したStroeActionをViewに送ることです。

### store.SpyStore
全てのStoreは、このSpyStore interfaceをimplementすべきです。
UIからStoreにリクエスト送る時に、Store.getInstance().onProcessViewReqeust(SpyViewAction)を使ってください。
Storeは、static シングルートンとして実装すべきです。

### ui.SpyView
プロジェクトのActivity, Fragmentなど、Storeとやり取りする必要があるViewは、このInterfaceをimplementすべきです。
Viewは、onReceiveStoreMsg()関数の中で、Storeから送られたActionのtypeを判断して、データを取り出してUIに反映します。

## SpyFluxの使い方
[SpyFluxExample](https://github.com/Huang-Liangjin/SpyFluxExample)の方を参照してください。

## 参考資料
[Flux](http://facebook.github.io/flux/)
[Flux Architecture on Android](http://lgvalle.xyz/2015/08/04/flux-architecture/)
[RxFlux Android Architecture](https://medium.com/swlh/rxflux-android-architecture-94f77c857aa2#.lgdqvvjpu)

