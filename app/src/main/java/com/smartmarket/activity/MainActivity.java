package com.smartmarket.activity;

import android.content.Context;
import android.content.Intent;
import android.support.annotation.NonNull;
import android.support.design.widget.BottomNavigationView;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;

import com.common.app.view.BaseActivity;
import com.common.factory.data.DataSource;
import com.factory.data.helper.GoodsHelper;
import com.factory.model.db.Goods;
import com.factory.scanner.ScannerActivity;
import com.google.zxing.BarcodeFormat;
import com.smartmarket.App;
import com.smartmarket.R;
import com.smartmarket.activity.goods.GoodsDetailActivity;
import com.smartmarket.fragment.main.GoodsFragment;
import com.smartmarket.fragment.main.ProfileFragment;
import com.smartmarket.fragment.main.ShoppingCartFragment;
import com.smartmarket.helper.NavHelper;

import butterknife.BindView;


/**
 * @author wulinpeng
 * @datetime: 18/1/23 下午10:52
 * @description:
 */
public class MainActivity extends BaseActivity
        implements BottomNavigationView.OnNavigationItemSelectedListener, NavHelper.NavListener, MenuItem.OnMenuItemClickListener {

    @BindView(R.id.navigation)
    public BottomNavigationView mNavigationView;

    private NavHelper mHelper;

    private final static int SCANNER_CODE = 1;

    // 购物车页面的编辑按钮/扫描按钮
    private MenuItem mEditItem;

    private boolean mIsFirstLoad = true;

    private boolean mIsGoodsCurrent = false;

    private boolean mIsCartEditing = false;

    public static void show(Context context) {
        context.startActivity(new Intent(context, MainActivity.class));
    }

    @Override
    protected int getLayoutId() {
        return R.layout.activity_main;
    }

    @Override
    protected void initView() {
        super.initView();
        // 设置navHelper
        mHelper = new NavHelper(getSupportFragmentManager(), R.id.container, this, this);
        mHelper.addTab(R.id.action_goods, GoodsFragment.class)
                .addTab(R.id.action_cart, ShoppingCartFragment.class)
                .addTab(R.id.action_profile, ProfileFragment.class);

        mNavigationView.setOnNavigationItemSelectedListener(this);
        Menu menu = mNavigationView.getMenu();
        // 触发首次选中goods
        menu.performIdentifierAction(R.id.action_goods, 0);
    }

    @Override
    protected void initData() {
        super.initData();
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.menu_main, menu);
        mEditItem = menu.findItem(R.id.edit);
        mEditItem.setVisible(false);
        mEditItem.setOnMenuItemClickListener(this);
        if (mIsFirstLoad) {
            // 手动触发
            // 刚进入app，触发onChange设置扫描icon
            onChange(-1, R.id.action_goods);
            mIsFirstLoad = false;
        }
        return true;
    }

    @Override
    public boolean onNavigationItemSelected(@NonNull MenuItem item) {
        return mHelper.onTabSelect(item.getItemId());
    }

    @Override
    public void onFirstAdd(int tag) {

    }

    @Override
    public boolean onReselect(int tag) {
        return false;
    }

    @Override
    public void onChange(int oldTag, int newTag) {
        mIsGoodsCurrent = (newTag == R.id.action_goods);
        if (mEditItem != null) {
            if (newTag == R.id.action_cart) {
                mEditItem.setVisible(true);
                mEditItem.setTitle(mIsCartEditing ? "完成" : "编辑");
                mEditItem.setIcon(null);
            } else if (newTag == R.id.action_goods) {
                mEditItem.setVisible(true);
                mEditItem.setTitle("");
                mEditItem.setIcon(R.drawable.ic_scan);
            } else {
                mEditItem.setVisible(false);
            }
        }
    }

    @Override
    public boolean onMenuItemClick(MenuItem item) {
        if (item.getItemId() != R.id.edit) {
            return false;
        }
        if (mIsGoodsCurrent) {
            startScanner();
            return true;
        }
        ShoppingCartFragment fragment = (ShoppingCartFragment) mHelper.getTabs().get(R.id.action_cart).fragment;
        fragment.edit();

        mIsCartEditing = fragment.isEditing;
        if (mIsCartEditing) {
            mEditItem.setTitle("完成");
        } else {
            mEditItem.setTitle("编辑");
        }
        return true;
    }

    private void startScanner() {
        ScannerActivity.startActivityForResult(this, SCANNER_CODE);
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        if (resultCode != RESULT_OK) {
            return;
        }
        if (requestCode != SCANNER_CODE) {
            return;
        }
        String code = data.getStringExtra(ScannerActivity.KEY_SCANNER_RESULT);
        BarcodeFormat format = (BarcodeFormat) data.getSerializableExtra(ScannerActivity.KEY_BARCODE_FORMAT);
        Log.d("Debug", format.name() + ":" + code);
        if (format == BarcodeFormat.QR_CODE) {
            // 如果是二维码，那么code代表商品id
            getGoodsByCode(code);
        } else {
            // TODO: 18/3/6 barcode的解析
            App.showToast("not QRCode");
        }
    }

    private void getGoodsByCode(final String id) {
        GoodsHelper.getGoodsByCode(id, new DataSource.Callback<Goods>() {
            @Override
            public void onDataLoadFailed(String msg) {
                App.showToast(msg);
            }

            @Override
            public void onDataLoaded(Goods goods) {
                GoodsDetailActivity.show(MainActivity.this, id, null);
            }
        });
    }
}
