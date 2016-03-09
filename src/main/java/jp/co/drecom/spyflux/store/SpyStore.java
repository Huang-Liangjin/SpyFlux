package jp.co.drecom.spyflux.store;

import jp.co.drecom.spyflux.action.SpyActionCreator;
import jp.co.drecom.spyflux.action.SpyViewAction;

/**
 * Created by huang_liangjin on 2016/03/02.
 *
 * Storeは、アプリの状態（データ）を保管するデータ倉庫であり、
 * データを操作するメソッド（ビジネスロジック）の集合でもあります。
 * 全てのビジネスロジックはStoreで実装すべきです。
 *
 * 非同期操作は、XXXSpyStoreに所属しているXXXSpyActionCreatorに譲るべきです。
 * Storeの全てのmethodは、同期メソッドです。
 *
 * ActionCreator -> Storeの通信は、
 * Storeで実装されるinterface ActionCreatorListenerのonActionCreated()メソッドを使います。
 * Store -> UIの通信は、SpyDispatcherを使います。
 * UI -> Storeの通信は、StoreのonProcess()メソッドを使います。
 *
 */
public interface SpyStore extends SpyActionCreator.ActionCreatorListener{
    String TAG = "SpyStore";
    //Event busのsubscriber対象から外す
//    public final SpyDispatcher mDispatcher = SpyDispatcher.getInstance();

    //subscriberとしてevent busに登録する
    //恐らくいならい
//    public final void register() {
//        mDispatcher.register(this);
//    }
//
//    public final void unregister() {
//        mDispatcher.unregister(this);
//    }

//    /**
//     * こちらのメソッドをoverrideして、actionのタイプを判断してそれぞれのbiz logicメソッドを呼び出すべき
//     * @param action
//     */
//    public abstract void onReceive(SpyStoreAction action);
//
//    public abstract void cleanStore();
//
//    public void onDataProcessed(SpyViewAction processedAction) {
////        mDispatcher.notifyChange(processedAction);
//    }

    /**
     * this method should be called by UI, tell store what kind of operation or data is needed.
     * after processing, you should call SpyDispatcher.post(newAction) to notice UI that the
     * processing is over and pass the processed data to UI.
     *
     * こちらはのメソッドは、UI用のメソッドです。
     * UIはこちらのメソッドを使って、Storeにデータを処理するリクエストを出します。
     * 処理する必要のあるデータはSpyViewActionの中にあります。
     * @param viewAction
     */
    void onProcessViewRequest(SpyViewAction viewAction);
}
