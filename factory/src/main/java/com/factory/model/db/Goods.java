package com.factory.model.db;

import com.factory.util.DiffUiDataCallback;
import com.raizlabs.android.dbflow.annotation.Column;
import com.raizlabs.android.dbflow.annotation.PrimaryKey;
import com.raizlabs.android.dbflow.annotation.Table;
import com.raizlabs.android.dbflow.structure.BaseModel;

/**
 * @author wulinpeng
 * @datetime: 18/2/14 下午3:30
 * @description:
 */
@Table(database = AppDatabase.class)
public class Goods extends BaseModel implements DiffUiDataCallback.UiDataDiffer {

    @PrimaryKey
    private String id;
    @Column
    private String picture;
    @Column
    private String barcode;
    @Column
    private String name;
    @Column
    private String description;
    @Column
    private int price;
    @Column
    private int count;

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getBarcode() {
        return barcode;
    }

    public void setBarcode(String barcode) {
        this.barcode = barcode;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public int getPrice() {
        return price;
    }

    public void setPrice(int price) {
        this.price = price;
    }

    public int getCount() {
        return count;
    }

    public void setCount(int count) {
        this.count = count;
    }

    public String getPicture() {
        return picture;
    }

    public void setPicture(String picture) {
        this.picture = picture;
    }

    @Override
    public boolean isItemSame(Object old) {
        return old != null && old instanceof Goods && ((Goods) old).getId().equalsIgnoreCase(id);
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;

        Goods goods = (Goods) o;

        if (price != goods.price) return false;
        if (count != goods.count) return false;
        if (id != null ? !id.equals(goods.id) : goods.id != null) return false;
        if (barcode != null ? !barcode.equals(goods.barcode) : goods.barcode != null) return false;
        if (name != null ? !name.equals(goods.name) : goods.name != null) return false;
        if (picture != null ? !picture.equals(goods.picture) : goods.picture != null) return false;
        return description != null ? description.equals(goods.description) : goods.description == null;

    }

    @Override
    public boolean isContentSame(Object old) {
        return equals(old);
    }
}
