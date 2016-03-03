## SpyFluxってなに
Androidアプリを作るためのArchitectureです。

## 関連知識
### Flux
### Event Bus

## SpyFluxの詳細
### SpyFluxフレームワークの構造
```
├── action
│   └── SpyAction.java
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
