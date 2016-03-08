package jp.co.drecom.spyflux.store;

import jp.co.drecom.spyflux.action.SpyAction;
import jp.co.drecom.spyflux.action.SpyActionCreator;

/**
 * Created by huang_liangjin on 2016/03/02.
 *
 * Storeは、アプリの状態（データ）とビジネスロジック（データを操作するメソッド）を含みます。
 * データは：Action、Actionを扱うメソッドのkey
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
//    public abstract void onReceive(SpyAction action);
//
//    public abstract void cleanStore();
//
//    public void onDataProcessed(SpyProcessedAction processedAction) {
////        mDispatcher.notifyChange(processedAction);
//    }

    /**
     * this method should be called by UI, tell store what kind of operation or data is needed.
     * after processing, you should call SpyDispatcher.post(newAction) to notice UI that the
     * processing is over and pass the processed data to UI.
     * @param viewAction
     */
    void onProcess(SpyAction viewAction);
}
