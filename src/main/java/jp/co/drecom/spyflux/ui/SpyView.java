package jp.co.drecom.spyflux.ui;

import jp.co.drecom.spyflux.action.SpyProcessedAction;

/**
 * Created by huang_liangjin on 2016/03/02.
 * アプリ内のActionを受け取るView（Activity, Fragmentなど）は、
 * 1. SpyViewを継承するべき
 * 2. SpyViewInterfaceを実装する
 *
 * こうする理由としては、EventBusの存在は、利用者に意識しないようにするためです。
 * このクラスは必ずViewよりextendする必要があります。
 *
 * abstract class からinterfaceに変更。
 */
public interface SpyView {
    public static final String TAG = "SpyView";
//    /**
//     * Storeからのデータを受け取って、Viewに反映するメソッドです。
//     * 常にUI threadで実行します。
//     * @param action
//     */
//    @Subscribe(threadMode = ThreadMode.MAIN)
//    void default onReceive(SpyAction action) {
//        SpyLog.printLog(TAG, "SpyView.post() called by event bus");
//        onNotifyChange(action);
//    }

    /**
     * こちらのメソッドをoverrideして、actionのタイプを判断した上で、Viewを更新します
     * overrideする時に、必ず@Subscribe(threadMode = ThreadMode.MAIN)を付けてください。
     * @param action
     */
    public abstract void onNotifyChange(SpyProcessedAction action);
}
