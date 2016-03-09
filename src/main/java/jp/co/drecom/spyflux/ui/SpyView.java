package jp.co.drecom.spyflux.ui;

import jp.co.drecom.spyflux.action.SpyStoreAction;

/**
 * Created by huang_liangjin on 2016/03/02.
 *
 * アプリ内でStoreとやりとりする必要がある全てのView(Activity, Fragmentなど)は、
 * SpyViewInterfaceを実現する必要があります。
 *
 * Store側で、SpyDispatcher.notifyView(action)を使って、ViewにActionを送ります。
 * View側で、onReceiveStoreMsg()関数で、Storeから送られたactionを受けます。
 * Viewは、onReceiveStoreMsg()関数で、Actionタイプを判断した上で、データを取り出して、
 * それらのデータをユーザに反映します。
 *
 * SpyDispatcherは正しく動作できるように、
 * Viewがcreateされた時、必ずSpyDispatcher.register(this)して、
 * Viewがdestroyされた時、必ずSpyDispatcher.unregister(this)してださい。
 *
 */
public interface SpyView {
    String TAG = "SpyView";

    /**
     * こちらのメソッドをoverrideして、actionのタイプを判断した上で、Viewを更新します
     * overrideする時に、必ず@Subscribe(threadMode = ThreadMode.MAIN)を付けてください。
     *
     * @param action
     */
    void onReceiveStoreMsg(SpyStoreAction action);
}
