package com.factory.presenter.goods;

import com.common.factory.presenter.BaseContract;
import com.factory.model.api.cart.AddGoodsModel;
import com.factory.model.db.Cart;
import com.factory.model.db.Goods;

/**
 * @author wulinpeng
 * @datetime: 18/2/17 下午4:25
 * @description:
 */
public interface CartContract {

    interface Presenter extends BaseContract.Presenter {
        void loadCarts();
        void deleteCarts(Cart... carts);
        void addCarts(AddGoodsModel.Item... items);
    }

    interface View extends BaseContract.RecyclerView<Presenter, Cart> {

    }
}
