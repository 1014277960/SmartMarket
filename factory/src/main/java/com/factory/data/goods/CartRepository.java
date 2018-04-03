package com.factory.data.goods;

import com.factory.data.BaseDBRepository;
import com.factory.model.db.Cart;

/**
 * @author wulinpeng
 * @datetime: 18/2/17 下午3:52
 * @description:
 */
public class CartRepository extends BaseDBRepository<Cart>  {
    public CartRepository(Class<Cart> cartClass) {
        super(cartClass);
    }

    @Override
    protected boolean isRequire(Cart cart) {
        return true;
    }
}
