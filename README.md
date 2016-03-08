## SpyFluxってなに
Androidアプリを作るためのArchitectureです。

## 関連知識
### Flux
### Event Bus

## SpyFluxの詳細
### SpyFluxフレームワークの構造
```
├── action
│   ├── SpyAction.java
│   └── SpyActionCreator.java
├── dispatcher
│   └── SpyDispatcher.java
├── store
│   └── SpyStore.java
├── ui
│   └── SpyView.java
└── util
    └── SpyLog.java
```
### SpyAction

### SpyDispatcher
シングルートンクラスです。一つのアプリ内で一つのSpyDispatcherしかないです。
中枢的なもので、すべてのStore, Viewを監視し、Action（データ）のデータフローをコントロールします。
主な役割は：
- ActionCreatorはActionを生成したことをすべてのStoreに通知し、生成されたActionをStoreに送る
- StoreはActionを処理したことをViewに通知し、処理したActionも同時にViewに送る

### SpyStore

### SpyView

### その他

## SpyFluxの使い方
1. 画面定義
2. 画面で使われるデータを抽出し、Modelクラスにします
3. ActionCreatorの定義(singletonにする必要があり)
4. Storeの定義(singletonにする必要があります)
5. Viewの実装(Activity, Fragmentなど)
6. その他



## 難しいところ・悩むところ
### View, Action, Storeどちら先に定義する？
- まずUIの定義：UIさえ分かれば、どんなデータが必要、データに対するどんな操作が必要ってこともわかる（ActionCreatorのメソッド）
- 次はデータの定義(Action);
- 続いてはデータにめぐる操作(Biz Logic)の定義(Store);
- 
### 非同期methodの実装部分
1. network通信部分
2. DBからデータを読み出す部分
3. 他に
これらのメソッドの実装は、utilというパッケージ内でやります。

### リスドの中に結構数のあるitemがロードされる場合, どうやってOOMを防ぐか

### Storeのregisterタイミング
StoreはViewと合わせて、使わないと意味がなくなりますので、
各ViewにStoreを持ち、Viewがregisterタイミングで、Storeもregisterする

### ViewとViewの間のInteraction
例えば、FragmentのListViewのあるItemをクリックし、そのItem詳細画面を開くという操作：
ViewとViewの間に渡すのはクリックしたItemの番号だけ、Itemそのもの渡さない。
Item詳細画面Viewはその番号を使って、Storeの中から取得すべき。
REMEMBER!!!Storeはデータの倉庫

## メリッド
1. アプリの構造はより分かりやすく、maintain cost下がる、
2. Activityはdataを管理する必要はなくなる: onSaveInstanceState(), onCreate()などの関数で、Data一時保存、データの読み出すはいらなくなる.
全てのデータはStoreの中にある。（Storeはsingletonですので、データなくなる配慮はいならい）


## レビュー観点
1. biz logic
2. 変数: newすべきかどうか、typeあってるかどうか、
3. メソッド: 