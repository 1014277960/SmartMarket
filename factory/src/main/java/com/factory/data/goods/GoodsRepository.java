package com.factory.data.goods;

import com.common.factory.data.DBDataSource;
import com.factory.data.BaseDBRepository;
import com.factory.model.db.Goods;
import com.factory.model.db.User;

import java.util.List;

/**
 * @author wulinpeng
 * @datetime: 18/2/9 下午2:58
 * @description:
 */
public class GoodsRepository extends BaseDBRepository<Goods> {
    public GoodsRepository(Class<Goods> goodsClass) {
        super(goodsClass);
    }

    @Override
    public void load(SucceedCallback<List<Goods>> callback) {
        super.load(callback);
    }

    @Override
    protected boolean isRequire(Goods goods) {
        return true;
    }
}
