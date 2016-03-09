package jp.co.drecom.spyflux.action;

import jp.co.drecom.spyflux.store.SpyStore;

/**
 * Created by huang_liangjin on 2016/03/03.
 *
 * XXXStoreに使われる非同期操作はSpyActionCreatorから継承したXXXActionCreatorで実装します。
 * The asynchronous operation which used by XXXStore should be placed in this class.
 *
 * ActionCreatorとStoreの通信手段ですが、最初は
 * StoreはSubscriberにして、
 * ActionCreatorの非同期操作終わって、データ生成したタイミングで全てのStoreにpostメッセージ送る
 * というやり方です。
 *
 * こうなると、ActionCreatorのpostメッセージは、Storeだけではなく、subscriberであるviewも送信しちゃいます。
 * view側で受けたpostメッセージを無視することが可能ですが、やはり若干気持ち悪いです。
 * ActionCreatorとStoreの間のpub/sub関係を解消するため、ActionCreatorはStoreをメンバー変数として持つ、
 * 非同期操作終わったら、メンバー変数であるStoreのonActionCreated() callback関数を呼び出します。
 */
public abstract class SpyActionCreator {

    //Storeはsingletonとして実装されるので、特にmemory leakの問題はありません。
    protected final SpyStore store;

    public SpyActionCreator(SpyStore store) {
        this.store = store;
    }

    /**
     * こちらのinterfaceは、storeとActionCreatorとの通信手段であり、必ずstore側に実装してもらう必要があります。
     * ActionCreatorはデータ生成し、そのデータをActionとしてカプセルしたタイミングで、
     * store.onActionCreated(Action)を呼び出す必要があります。
     */
    public interface ActionCreatorListener {
        /**
         * This method should be implemented by any ExtendedStore, for noticing the store that
         * the data(inside the action) which UI need is generated.
         * Then you should store the data in the ExtendedStores, and use SpyDispatcher.post(action)
         * to notice UI if necessary.
         * @param action ActionCreatorが生成したactionで、データが含まれてます。
         */
        void onActionCreated(SpyStoreAction action);
    }

}
