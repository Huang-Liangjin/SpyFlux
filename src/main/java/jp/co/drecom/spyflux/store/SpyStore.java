package jp.co.drecom.spyflux.store;

import jp.co.drecom.spyflux.action.SpyAction;
import jp.co.drecom.spyflux.action.SpyProcessedAction;
import jp.co.drecom.spyflux.dispatcher.SpyDispatcher;

/**
 * Created by huang_liangjin on 2016/03/02.
 *
 * Storeは、アプリの状態（データ）とビジネスロジック（データを操作するメソッド）を含みます。
 * データは：Action、Actionを扱うメソッドのkey
 */
public abstract class SpyStore {
    public static final String TAG = "SpyStore";
    public final SpyDispatcher mDispatcher = SpyDispatcher.getInstance();

    public void register() {
        mDispatcher.register(this);
    }

    public void unregister() {
        mDispatcher.unregister(this);
    }

    /**
     * こちらのメソッドをoverrideして、actionのタイプを判断してそれぞれのbiz logicメソッドを呼び出すべき
     * @param action
     */
    public abstract void onReceive(SpyAction action);

    public void onDataProcessed(SpyProcessedAction processedAction) {
        mDispatcher.notifyChange(processedAction);
    }
}
