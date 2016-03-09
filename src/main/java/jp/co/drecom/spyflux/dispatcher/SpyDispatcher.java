package jp.co.drecom.spyflux.dispatcher;

import org.greenrobot.eventbus.EventBus;

import jp.co.drecom.spyflux.action.SpyStoreAction;
import jp.co.drecom.spyflux.ui.SpyView;

/**
 * Created by huang_liangjin on 2016/03/02.
 */

/**
 * SpyDispatcher:
 * DispatcherはFlux frameworkでの位置付けは、
 * Center Hubみたいなもので、アプリのデータフローをコントロールします。
 * Store -> View, View -> Storeのデータフローは、全部dispatcher経由です。
 * SpyFluxのSpyDispatcherとFluxのdispatcherちょっと違うところがありまして、
 * UI -> StoreのデータはSpyDispatcher経由しないところです。
 * SpyDispatcherが管理するのは、Store -> UIのデータフローだけです。
 * そうする理由は、やはりSpyDispatcherの負担を減らしたいからです。
 * それと、
 * UIからStoreの呼び出しは、わざわざDispatcher経由しなくても、簡単にできるからです。
 * (SpyStoreはPOJOであり、Singletonであるからです )
 *
 * SpyDispatcherの主な役割は: .
 * 1. 各Viewをsubscribeとしての登録と解除すること
 * 2. Storeから生成したActionsをViewにdispatchすること
 *
 */
public class SpyDispatcher {
    public static final String TAG = "SpyDispatcher";
    private static final SpyDispatcher mInstance = new SpyDispatcher();
    private static final EventBus mBus = EventBus.getDefault();


    private static SpyDispatcher getInstance() {
        return mInstance;
    }

    /**
     * Viewをsubscriber(observer)としてDispatcherに登録する
     * @param view UI, activity, fragmentなど.
     */
    public static void register(SpyView view) {
        getInstance().registerInternal(view);
    }

    /**
     * Dispatcherに登録したViewを解除する
     * @param view UI: activity, fragmentなど.
     */
    public static void unregister(SpyView view) {
        getInstance().unregisterInternal(view);
    }

    /**
     * Storeはこの関数を使って、Viewに通知する。
     * @param action Storeで生成した、Viewに渡すAction（アプリ用のデータは内包しています）
     */
    public static void notifyView(SpyStoreAction action) {
        getInstance().notifyViewInternal(action);
    }


    private void registerInternal(SpyView view) {
        mBus.register(view);
    }


    private void unregisterInternal(SpyView view) {
        mBus.unregister(view);
    }

    /**
     * Storeで状態変化したactionをViewに送る
     * dispatch と notifyChange両方ともEvent busを使って、Actionをsubscriberにpostするですが
     * architecture視点から見ると、前者はActionCreator -> Storeで、後者はStore -> View という違いがあるので
     * 名前分けて実装しました
     * @param action
     */
    private void notifyViewInternal(SpyStoreAction action) {
        mBus.post(action);
    }

}
