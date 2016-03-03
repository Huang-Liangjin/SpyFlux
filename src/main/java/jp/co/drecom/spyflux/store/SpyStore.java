package jp.co.drecom.spyflux.store;

import org.greenrobot.eventbus.Subscribe;
import org.greenrobot.eventbus.ThreadMode;

import jp.co.drecom.spyflux.action.SpyAction;
import jp.co.drecom.spyflux.dispatcher.SpyDispatcher;
import jp.co.drecom.spyflux.util.SpyLog;

/**
 * Created by huang_liangjin on 2016/03/02.
 *
 * Storeは、アプリの状態（データ）とビジネスロジック（データを操作するメソッド）を含みます。
 * データは：Action、Actionを扱うメソッドのkey
 */
public abstract class SpyStore {
    public static final String TAG = "SpyStore";
    private final SpyDispatcher mDispatcher = SpyDispatcher.getInstance();


    /**
     * ただ、biz logicの複雑性により、dataを処理する操作は時間掛かっちゃうかもしれません、
     * その場合、データ処理も別スレッドでやった方がいいと思います。
     * （ActionCreatorのpost()メソッドは別スレッドの中に呼び出す必要がある）
     * @param action
     */
    @Subscribe(threadMode = ThreadMode.POSTING) //default , no thread switching
    private final void onReceive(SpyAction action) {
        SpyLog.printLog(TAG, "SpyStore.post() called by event bus");
        //この中に、switchより、biz logicの分け処理します
        onDispatch(action);
    }

    /**
     * こちらのメソッドをoverrideして、actionのタイプを判断してそれぞれのbiz logicメソッドを呼び出すべき
     * @param action
     */
    public abstract void onDispatch(SpyAction action);
}
