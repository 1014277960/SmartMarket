package com.factory.model.db;

import com.factory.util.DiffUiDataCallback;
import com.raizlabs.android.dbflow.annotation.ForeignKey;
import com.raizlabs.android.dbflow.annotation.PrimaryKey;
import com.raizlabs.android.dbflow.annotation.Table;
import com.raizlabs.android.dbflow.structure.BaseModel;

/**
 * @author wulinpeng
 * @datetime: 18/2/17 下午4:09
 * @description:
 */
@Table(database = AppDatabase.class)
public class Cart extends BaseModel implements DiffUiDataCallback.UiDataDiffer {
    @PrimaryKey
    private String id;
    @ForeignKey(tableClass = Goods.class)
    private Goods goods;
    private int count;

    public Cart() {
    }

    public Cart(String id, Goods goods, int count) {
        this.id = id;
        this.goods = goods;
        this.count = count;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public Goods getGoods() {
        return goods;
    }

    public void setGoods(Goods goods) {
        this.goods = goods;
    }

    public int getCount() {
        return count;
    }

    public void setCount(int count) {
        this.count = count;
    }

    @Override
    public boolean isItemSame(Object old) {
        return old != null
                && old instanceof Cart
                && id.equalsIgnoreCase(((Cart) old).getId());
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;

        Cart cart = (Cart) o;

        if (count != cart.count) return false;
        if (id != null ? !id.equals(cart.id) : cart.id != null) return false;
        return goods != null ? goods.getId().equals(cart.goods.getId()) : cart.goods == null;

    }

    @Override
    public boolean isContentSame(Object old) {
        return equals(old);
    }
}
