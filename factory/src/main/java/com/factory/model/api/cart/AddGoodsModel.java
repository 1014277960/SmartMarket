package com.factory.model.api.cart;

import java.util.List;

/**
 * @author wulinpeng
 * @datetime: 18/2/25 下午1:45
 * @description:
 */
public class AddGoodsModel {

    private List<Item> items;

    public List<Item> getItems() {
        return items;
    }

    public void setItems(List<Item> items) {
        this.items = items;
    }

    public static class Item {
        private String goodsId;
        private int count;

        public Item(String goodsId, int count) {
            this.goodsId = goodsId;
            this.count = count;
        }

        public String getGoodsId() {
            return goodsId;
        }

        public void setGoodsId(String goodsId) {
            this.goodsId = goodsId;
        }

        public int getCount() {
            return count;
        }

        public void setCount(int count) {
            this.count = count;
        }
    }
}
