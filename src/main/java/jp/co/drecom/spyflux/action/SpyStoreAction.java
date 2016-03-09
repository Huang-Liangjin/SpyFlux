package jp.co.drecom.spyflux.action;

import android.util.SparseArray;

/**
 * Created by huang_liangjin on 2016/03/02.
 *
 * SpyFlux Architectureを採用するアプリの一つ大きな特徴は、
 * アプリ内各component(UI部分とStore部分)の間流されるデータは、全部SpyXXXActionとしてカプセルされる
 * というところです。
 *
 * こちらのSpyStoreActionはStoreからUIにデータを渡すActionです。
 * もう一つのSpyViewActionは、UIからStoreにデータ処理をリクエストするActionです。
 *
 * 二つのActionを分ける理由としては、
 * StoreからUIに渡すActionの中に、アプリが使う本物データが含まれてます。
 * UIからStoreに渡すActionの中に、主にリクエストデータ（リストで選んだオブジェクトの番号、押したボタンの番号など）です。
 * アプリデータはリクエストデータより、重要性もデータの構造、データのサイズも全然違うので、
 * 分けて管理した方がよいかと。
 * もう一つのメリットは、このActionはStore -> UI方向か、UI -> Store方向かというのも一目瞭然です。
 */
public class SpyStoreAction {

    /**
     * Stringの代わりに、intタイプを使います。
     */
    private final int type;

    /**
     * keyはint typeを指定できるように、
     * SparseArrayを使います。
     */
    private final SparseArray<Object> data;

    /**
     * 構造関数はprivateにしちゃいます。
     * SpyActionの生成は、Builderを使ってください。
     * @param type SpyActionのタイプ
     * @param data StoreからUIに渡すデータ
     */
    private SpyStoreAction(int type, SparseArray<Object> data) {
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

        /**
         * Actionは必ずtypeを指定する必要があります
         * @param type Action type.
         */
        public Builder(int type) {
            if (type == 0) {
                throw new IllegalArgumentException("type may not be 0.");
            }
            this.type = type;
            //bundleを呼ばず、dataはnullという状況を回避するため、ここでemptyのmapを作ります
            data = new SparseArray<>();
        }

        public Builder bundle(int key, Object value) {
            if (value == null) {
                throw new IllegalArgumentException("value may not be value");
            }
            data.append(key, value);
            return this;
        }

        public SpyStoreAction build() {
            //note: SpyViewActionはこういうチェックがいらない
            if (type == 0 || data.size() == 0) {
                throw new IllegalArgumentException("the elements may not be empty");
            }
            return new SpyStoreAction(type, data);
        }
    }
}
