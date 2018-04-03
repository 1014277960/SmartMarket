package com.factory.scanner;

import android.app.Activity;
import android.content.Intent;
import android.graphics.Bitmap;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;

import com.factory.R;
import com.google.zxing.BarcodeFormat;
import com.google.zxing.Result;
import com.google.zxing.client.result.ParsedResult;
import com.mylhyl.zxing.scanner.OnScannerCompletionListener;
import com.mylhyl.zxing.scanner.ScannerOptions;
import com.mylhyl.zxing.scanner.ScannerView;

/**
 * @author wulinpeng
 * @datetime: 18/1/28 下午8:35
 * @description: 码扫描实现activity
 */
public class ScannerActivity extends AppCompatActivity implements OnScannerCompletionListener {

    public static final String KEY_SCANNER_RESULT = "scanner_content";
    public static final String KEY_BARCODE_FORMAT = "barcode_format";

    private ScannerView mScannerView;

    public static void startActivityForResult(Activity activity, int requestCode) {
        activity.startActivityForResult(new Intent(activity, ScannerActivity.class), requestCode);
    }

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_scanner);

        mScannerView = (ScannerView) findViewById(R.id.scanner_view);
        // 设置可识别的码类型
        ScannerOptions options = new ScannerOptions.Builder().setScanMode(BarcodeFormat.AZTEC,
                BarcodeFormat.CODABAR,
                BarcodeFormat.CODE_39,
                BarcodeFormat.CODE_93,
                BarcodeFormat.CODE_128,
                BarcodeFormat.DATA_MATRIX,
                BarcodeFormat.EAN_8,
                BarcodeFormat.EAN_13,
                BarcodeFormat.ITF,
                BarcodeFormat.MAXICODE,
                BarcodeFormat.PDF_417,
                BarcodeFormat.QR_CODE,
                BarcodeFormat.RSS_14,
                BarcodeFormat.RSS_EXPANDED,
                BarcodeFormat.UPC_A,
                BarcodeFormat.UPC_E,
                BarcodeFormat.UPC_EAN_EXTENSION).build();
        mScannerView.setScannerOptions(options);
        mScannerView.setOnScannerCompletionListener(this);
    }

    /**
     * 扫描完成回调
     * @param result
     * @param parsedResult
     * @param bitmap
     */
    @Override
    public void onScannerCompletion(Result result, ParsedResult parsedResult, Bitmap bitmap) {
        Intent intent = new Intent();
        intent.putExtra(KEY_SCANNER_RESULT, result.getText());
        intent.putExtra(KEY_BARCODE_FORMAT, result.getBarcodeFormat());
        setResult(RESULT_OK, intent);
        finish();
    }

    @Override
    protected void onResume() {
        mScannerView.onResume();
        super.onResume();
    }

    @Override
    protected void onPause() {
        mScannerView.onPause();
        super.onPause();
    }

}
