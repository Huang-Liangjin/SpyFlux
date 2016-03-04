package jp.co.drecom.spyflux.action;

/**
 * Created by huang_liangjin on 2016/03/04.
 */
public class SpyProcessedAction {
    private int storeId;

    public SpyAction getAction() {
        return action;
    }

    public int getStoreId() {
        return storeId;
    }

    private SpyAction action;

    public SpyProcessedAction(int storeId, SpyAction action) {
        if (storeId == 0 || action == null) {
            throw new IllegalArgumentException("parameter is wrong.");
        }
        this.storeId = storeId;
        this.action = action;
    }

}
