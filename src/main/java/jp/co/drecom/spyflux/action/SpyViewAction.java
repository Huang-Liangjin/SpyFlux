package jp.co.drecom.spyflux.action;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by huang_liangjin on 2016/03/04.
 *
 * UIからStoreにリクエストを出すときに使うActionです。
 * リクエストデータが含まれてます。
 * 場合によってリクエストデータはnullの可能性もあります（リクエスト出すだけ、データ処理はいらないとき）。
 */
public class SpyViewAction {
    /**
     * Stringの代わりに、intタイプを使います。
     */
    private final int type;

    /**
     * SpyViewActionにはkeyの指定はちょっと無駄だと思いますので、
     * mapではなくlistを使用。
     * ただ、エンジニアにはリクエストデータを作る時と使う時に、
     * そのデータの順番を気をつける必要があります。
     */
    private final List<Object> data;

    /**
     * 構造関数はprivateにしちゃいます。
     * SpyActionの生成は、Builderを使ってください。
     * @param type SpyActionのタイプ
     * @param data UIからStoreに渡すデータ
     */
    private SpyViewAction(int type, List<Object> data) {
        this.type = type;
        this.data = data;
    }

    public int getType() {
        return type;
    }

    public List<Object> getData() {
        return data;
    }

    public static class Builder {
        private final int type;
        private final List<Object> data;

        /**
         * Actionは必ずtypeを指定する必要があります
         * @param type Action type.
         */
        public Builder(int type) {
            if (type == 0) {
                throw new IllegalArgumentException("type may not be 0.");
            }
            this.type = type;
            //bundleを呼ばず、dataはnullという状況を回避するため、ここでemptyのlistを作ります
            data = new ArrayList<>();
        }

        public Builder bundle(Object value) {
            if (value == null) {
                throw new IllegalArgumentException("value may not be value");
            }
            data.add(value);
            return this;
        }

        public SpyViewAction build() {
            //SpyViewActionには、dataがemptyの場合がありえます。
            if (type == 0) {
                throw new IllegalArgumentException("the elements may not be empty");
            }
            return new SpyViewAction(type, data);
        }
    }

}
