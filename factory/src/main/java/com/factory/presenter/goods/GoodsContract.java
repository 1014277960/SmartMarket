package com.factory.presenter.goods;

import com.common.factory.presenter.BaseContract;
import com.factory.model.db.Goods;
import com.factory.model.db.User;

/**
 * @author wulinpeng
 * @datetime: 18/2/9 下午2:39
 * @description:
 */
public interface GoodsContract {

    interface Presenter extends BaseContract.Presenter {
        void loadGoods();
    }

    interface View extends BaseContract.RecyclerView<Presenter, Goods> {

    }
}
