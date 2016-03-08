package jp.co.drecom.spyflux.action;

import android.util.SparseArray;

/**
 * Created by huang_liangjin on 2016/03/02.
 */
public class SpyAction {
    private static final String TAG = "SpyAction";

    //効率上がるために、typeはStringからintに変更.
    private final int type;
//    private final ArrayMap<String, Object> data;
    //mapもAndroidで特化したarrayを使う
    private final SparseArray<Object> data;

    private SpyAction(int type, SparseArray<Object> data) {
        this.type = type;
        this.data = data;
    }

    public int getType() {
        return type;
    }

    public SparseArray<Object> getData() {
        return data;
    }

    public static class Builder {
        private final int type;
        private final SparseArray<Object> data;

        public Builder(int type) {
            if (type == 0) {
                throw new IllegalArgumentException("type may not be 0.");
            }
            this.type = type;
            data = new SparseArray<>();
        }

        public Builder bundle(int key, Object value) {
            if (value == null) {
                throw new IllegalArgumentException("value may not be value");
            }
            data.append(key, value);
            return this;
        }

        public SpyAction build() {
            if (type == 0) {
                // data could be empty
//            if (type == 0 || data.size() == 0) {
                throw new IllegalArgumentException("the elements may not be empty");
            }
            return new SpyAction(type, data);
        }
    }
}
