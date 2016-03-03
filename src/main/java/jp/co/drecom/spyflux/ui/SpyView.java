package jp.co.drecom.spyflux.ui;

import org.greenrobot.eventbus.Subscribe;
import org.greenrobot.eventbus.ThreadMode;

import jp.co.drecom.spyflux.action.SpyAction;
import jp.co.drecom.spyflux.util.SpyLog;

/**
 * Created by huang_liangjin on 2016/03/02.
 * アプリ内のActionを受け取るView（Activity, Fragmentなど）は、
 * 1. SpyViewを継承するべき
 * 2. SpyViewInterfaceを実装する
 *
 * こうする理由としては、EventBusの存在は、利用者に意識しないようにするためです。
 * このクラスは必ずViewよりextendする必要があります。
 */
public abstract class SpyView {
    public static final String TAG = "SpyView";
    /**
     * Storeからのデータを受け取って、Viewに反映するメソッドです。
     * 常にUI threadで実行します。
     * @param action
     */
    @Subscribe(threadMode = ThreadMode.MAIN)
    private final void onReceive(SpyAction action) {
        SpyLog.printLog(TAG, "SpyView.post() called by event bus");
        onNotifyChange(action);
    }

    /**
     * こちらのメソッドをoverrideして、actionのタイプを判断した上で、Viewを更新します
     * @param action
     */
    public abstract void onNotifyChange(SpyAction action);
}
