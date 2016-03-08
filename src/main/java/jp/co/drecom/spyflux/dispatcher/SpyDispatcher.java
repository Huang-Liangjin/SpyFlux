package jp.co.drecom.spyflux.dispatcher;

import org.greenrobot.eventbus.EventBus;

import jp.co.drecom.spyflux.action.SpyAction;

/**
 * Created by huang_liangjin on 2016/03/02.
 */

/**
 * FLUXによると、Dispatcherは一つのプロジェクトの中に一つしかない -> singleton.
 * 主な役割は: Actionsを特定のStoreにdispatchすること.
 * 1. 各Storeの登録、解除
 * 2. ActionCreatorから各StoreへのActionのdispatch
 * 3. StoreでAcitonの状態は変化した場合、Viewに通知
 * 4. 各Viewの登録、解除
 * 各Storeは、Dispatcherに登録しなければならない.
 *
 */
public class SpyDispatcher {
    public static final String TAG = "SpyDispatcher";
    private static final SpyDispatcher mInstance = new SpyDispatcher();
    private static final EventBus mBus = EventBus.getDefault();

    private static SpyDispatcher getInstance() {
        return mInstance;
    }

    public static final void register(Object object) {
        getInstance().registerInternal(object);
    }

    public static final void unregister(Object object) {
        getInstance().unregisterInternal(object);
    }

    public static final void notifyView(SpyAction action) {
        getInstance().notifyViewInternal(action);
    }

    /**
     * Store, ViewなどをDispatcherに登録する
     * @param object store, view.
     */
    private void registerInternal(Object object) {
        mBus.register(object);
    }

    /**
     * Store, ViewなどをDispatcherから解除する
     * @param object
     */
    private void unregisterInternal(Object object) {
        mBus.unregister(object);
    }

    //StoreはEvent busのsubscriber対象から外されることと共に、こちらの関数もいらなくなる
//    /**
//     * ActionCreatorからStoreにactionsを送る.
//     * @param action
//     */
//    public void dispatch(SpyAction action) {
//        mBus.post(action);
//    }

    /**
     * Storeで状態変化したactionをViewに送る
     * dispatch と notifyChange両方ともEvent busを使って、Actionをsubscriberにpostするですが
     * architecture視点から見ると、前者はActionCreator -> Storeで、後者はStore -> View という違いがあるので
     * 名前分けて実装しました
     * @param action
     */
    private void notifyViewInternal(SpyAction action) {
        mBus.post(action);
    }

}
