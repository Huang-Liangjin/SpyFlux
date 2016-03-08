package jp.co.drecom.spyflux.action;

import jp.co.drecom.spyflux.store.SpyStore;

/**
 * Created by huang_liangjin on 2016/03/03.
 */
public abstract class SpyActionCreator {
//    private final SpyDispatcher mDispatcher = SpyDispatcher.getInstance();

    //ActionCreatorとStoreの通信は、event busからcallbackに変える
    //効率上がるため
//    public final void post(SpyAction action) {
//        mDispatcher.dispatch(action);
//    }
    protected final SpyStore store;

    public SpyActionCreator(SpyStore store) {
        this.store = store;
    }

    public interface ActionCreatorListener {
        /**
         * This method should be implemented by any ExtendedStore, for noticing the store that
         * the data(inside the action) which UI need is generated.
         * Then you should store the data in the ExtendedStores, and use SpyDispatcher.post(action)
         * to notice UI if necessary.
         * @param action it
         */
        void onActionCreated(SpyAction action);
    }

}
