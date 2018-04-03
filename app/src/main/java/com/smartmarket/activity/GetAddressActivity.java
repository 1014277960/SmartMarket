package com.smartmarket.activity;

import android.widget.EditText;

import com.common.Common;
import com.common.app.view.BaseActivity;
import com.smartmarket.R;

import butterknife.BindView;
import butterknife.OnClick;

/**
 * @author wulinpeng
 * @datetime: 18/3/25 上午1:23
 * @description: 开发阶段用于手动获取服务器ip地址
 */
public class GetAddressActivity extends BaseActivity {

    @BindView(R.id.et_address)
    public EditText mAddress;

    @Override
    protected int getLayoutId() {
        return R.layout.activity_get_address;
    }

    @OnClick(R.id.submit)
    void onSubmit() {
        Common.BASE_URL = buildUrl(mAddress.getText().toString());
        LaunchActivity.show(this);
        finish();
    }

    private String buildUrl(String ip) {
        return "http://" + ip + ":8080/api/";
    }
}
