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
