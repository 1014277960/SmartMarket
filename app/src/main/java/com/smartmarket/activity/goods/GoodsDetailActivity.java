package com.smartmarket.activity.goods;

import android.content.Context;
import android.content.Intent;
import android.support.annotation.Nullable;
import android.text.TextUtils;
import android.util.Log;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.bumptech.glide.Glide;
import com.common.app.view.BaseActivity;
import com.common.factory.data.DataSource;
import com.factory.Factory;
import com.factory.data.helper.CartHelper;
import com.factory.data.helper.DBHelper;
import com.factory.model.api.cart.AddGoodsModel;
import com.factory.model.db.Cart;
import com.factory.model.db.Goods;
import com.factory.model.db.Goods_Table;
import com.raizlabs.android.dbflow.sql.language.From;
import com.raizlabs.android.dbflow.sql.language.SQLite;
import com.smartmarket.App;
import com.smartmarket.R;
import com.smartmarket.fragment.goods.AddToCartFragment;

import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;
import butterknife.OnClick;

public class GoodsDetailActivity extends BaseActivity implements AddToCartFragment.OnSelectListener, DataSource.Callback<List<Cart>> {

    private final static String KEY_CODE = "KEY_CODE";

    private int mCodeType;
    private String mCode;

    private Goods mGoods;

    @BindView(R.id.goods_pic)
    ImageView mPic;
    @BindView(R.id.goods_name)
    TextView mName;
    @BindView(R.id.goods_desc)
    TextView mDesc;
    @BindView(R.id.goods_price)
    TextView mPrice;

    private AddToCartFragment mAddToCartFragment;

    public static void show(Context context, @Nullable String code, @Nullable String barcode) {
        if (TextUtils.isEmpty(code)) {
            return;
        }
        Intent intent = new Intent(context, GoodsDetailActivity.class);
        intent.putExtra(KEY_CODE, code);
        context.startActivity(intent);
    }

    /**
     * 获取商品的id或者barcode
     * @param intent
     * @return
     */
    @Override
    protected boolean initArgs(Intent intent) {
        mCode = intent.getStringExtra(KEY_CODE);

        return (mCodeType == 1 || mCodeType == 0) && !TextUtils.isEmpty(mCode);
    }

    @Override
    protected void initData() {
        super.initData();
        // 从本地数据库获取商品信息
        loadGoods();
        setupGoodsData();
    }

    private void setupGoodsData() {
        // TODO: 18/2/22 封装图片加载框架，或者像portraitview一样
        Glide.with(this)
                .load(mGoods.getPicture())
                .placeholder(R.drawable.ic_goods_place_holder)
                .centerCrop()
                .into(mPic);
        mName.setText("品名:" + mGoods.getName());
        mDesc.setText("简介:" + mGoods.getDescription());
        mPrice.setText("单价:" + mGoods.getPrice() + "元");
    }

    private void loadGoods() {
        From<Goods> goodsFrom =  SQLite.select().from(Goods.class);
        if (mCodeType == 0) {
            mGoods = goodsFrom.where(Goods_Table.id.eq(mCode)).querySingle();
        } else {
            mGoods = goodsFrom.where(Goods_Table.barcode.eq(mCode)).querySingle();
        }
        if (mGoods == null) {
            finish();
        }
    }

    @Override
    protected void initView() {
        super.initView();
    }

    @OnClick(R.id.add_to_cart)
    public void addToCart() {
        if (mAddToCartFragment == null) {
            mAddToCartFragment = new AddToCartFragment();
        }
        mAddToCartFragment.show(getSupportFragmentManager(), "goods", mGoods.getCount());
        mAddToCartFragment.setOnSelectListener(this);

    }

    @Override
    protected int getLayoutId() {
        return R.layout.activity_goods_detail;
    }

    @Override
    public void onSelect(int count) {
        AddGoodsModel model = buildRequestModel(count);
        CartHelper.addGoods(model, this);
    }

    private AddGoodsModel buildRequestModel(int count) {
        AddGoodsModel model = new AddGoodsModel();
        List<AddGoodsModel.Item> items = new ArrayList<>();
        items.add(new AddGoodsModel.Item(mGoods.getId(), count));
        model.setItems(items);
        return model;
    }

    @Override
    public void onDataLoaded(List<Cart> carts) {
        DBHelper.getInstance().save(Cart.class, carts.toArray(new Cart[0]));
        App.showToast("添加成功");
    }

    @Override
    public void onDataLoadFailed(String msg) {
        App.showToast("添加失败");
    }
}
