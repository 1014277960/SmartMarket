package com.factory.net;

import android.text.format.DateFormat;
import android.util.Log;

import com.alibaba.sdk.android.oss.OSS;
import com.alibaba.sdk.android.oss.OSSClient;
import com.alibaba.sdk.android.oss.common.auth.OSSCredentialProvider;
import com.alibaba.sdk.android.oss.common.auth.OSSPlainTextAKSKCredentialProvider;
import com.alibaba.sdk.android.oss.model.PutObjectRequest;
import com.alibaba.sdk.android.oss.model.PutObjectResult;
import com.common.utils.HashUtil;
import com.factory.Factory;

import java.io.File;
import java.util.Date;

/**
 * @author wulinpeng
 * @datetime: 17/11/29 下午11:50
 * @description: 用于上传任意文件到阿里oss,同步操作，需要调用者在子线程调用
 */
public class UploadHelper {

    private static final String ENDPOINT = "http://oss-cn-qingdao.aliyuncs.com";
    private static final String BUCKET_NAME = "qtalker";

    private static OSS getClient() {
        OSSCredentialProvider ossCredentialProvider = new OSSPlainTextAKSKCredentialProvider(
                "", "");
        return new OSSClient(Factory.app(), ENDPOINT, ossCredentialProvider);
    }

    /**
     * 上传文件最终方法，返回一个存储url
     * @param objKey
     * @param path
     * @return
     */
    private static String upload(String objKey, String path) {
        PutObjectRequest request = new PutObjectRequest(BUCKET_NAME, objKey, path);
        String url = null;
        try {
            OSS client = getClient();
            // 开始上传
            PutObjectResult result = client.putObject(request);
            // 得到一个外网访问地址
            url = client.presignPublicObjectURL(BUCKET_NAME, objKey);
        } catch (Exception e) {
            Log.d("Debug", e.getMessage());
        } finally {
            return url;
        }
    }

    public static String uploadImage(String path) {
        String key = getImageKey(path);
        return upload(key, path);
    }

    public static String uploadPortrait(String path) {
        String key = getPortraitKey(path);
        return upload(key, path);
    }

    public static String uploadAudio(String path) {
        String key = getAudioKey(path);
        return upload(key, path);
    }

    /**
     * 分月存储，避免一个文件夹文件太多
     * @return yyyyMM
     */
    private static String getDateString() {
        return DateFormat.format("yyyyMM", new Date()).toString();
    }

    // /image/201703/asdfsgsdskfhak.jpg
    private static String getImageKey(String path) {
        String md5 = HashUtil.getMD5String(new File(path));
        String dateString = getDateString();
        return String.format("image/%s/%s.jpg", dateString, md5);
    }

    // /portrait/201703/asdfsgsdskfhak.jpg
    private static String getPortraitKey(String path) {
        String md5 = HashUtil.getMD5String(new File(path));
        String dateString = getDateString();
        return String.format("portrait/%s/%s.jpg", dateString, md5);
    }

    // /audio/201703/asdfsgsdskfhak.jpg
    private static String getAudioKey(String path) {
        String md5 = HashUtil.getMD5String(new File(path));
        String dateString = getDateString();
        return String.format("audio/%s/%s.jpg", dateString, md5);
    }
}
