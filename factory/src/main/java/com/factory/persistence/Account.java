package com.factory.persistence;

import android.content.Context;
import android.content.SharedPreferences;
import android.text.TextUtils;

import com.factory.Factory;
import com.factory.model.api.account.AccountRspModel;
import com.factory.model.card.UserCard;
import com.factory.model.db.User;
import com.raizlabs.android.dbflow.sql.language.SQLite;

/**
 * @author wulinpeng
 * @datetime: 18/2/3 下午10:20
 * @description:
 */
// TODO: 18/2/9 感觉有点乱，需要整理一波
public class Account {

    private static final String KEY_TOKEN = "KEY_TOKEN";
    private static final String KEY_USER_ID = "KEY_USER_ID";
    private static final String KEY_ACCOUNT = "KEY_ACCOUNT";
    private static final String KEY_PORTRAIT = "KEY_PORTRAIT";
    private static final String KEY_NAME = "KEY_NAME";


    private static String token;
    private static String userId;
    private static String account;
    private static String portrait;
    private static String name;

    public static void load(Context context) {
        SharedPreferences sp = context.getSharedPreferences(Account.class.getName(), Context.MODE_PRIVATE);
        token = sp.getString(KEY_TOKEN, "");
        userId = sp.getString(KEY_USER_ID, "");
        account = sp.getString(KEY_ACCOUNT, "");
        portrait = sp.getString(KEY_PORTRAIT, "");
        name = sp.getString(KEY_NAME, "");
    }

    /**
     * 持久化
     */
    private static void save(Context context) {
        SharedPreferences sp = context.getSharedPreferences(Account.class.getName(), Context.MODE_PRIVATE);
        sp.edit()
                .putString(KEY_TOKEN, token)
                .putString(KEY_ACCOUNT, account)
                .putString(KEY_USER_ID, userId)
                .putString(KEY_PORTRAIT, portrait)
                .putString(KEY_NAME, name)
                .apply();

    }

    public static boolean isLogin() {
        // 用户id和token不为空
        return !TextUtils.isEmpty(userId)
                && !TextUtils.isEmpty(token);
    }

    /**
     * 用户信息是否完善
     * @return
     */
    public static boolean isComplete() {
        if (isLogin()) {
            User user = getUser();
            return !TextUtils.isEmpty(user.getPortrait());
        }
        return false;
    }

    /**
     * 保存我自己的信息到xml中(token/id)
     * @param model
     */
    public static void saveUser(AccountRspModel model) {
        // 存储当前用户account/id/token，方便从本地数据库中查询当前用户信息
        token = model.getToken();
        saveUserCard(model.getUserCard());
        save(Factory.app());
    }

    private static void saveUserCard(UserCard card) {
        userId = card.getId();
        account = card.getPhone();
        name = card.getName();
        portrait = card.getPortrait();
    }

    /**
     * 获取当前用户登陆信息
     * @return
     */
    public static User getUser() {
        return TextUtils.isEmpty(userId)? new User(): SQLite.select()
                .from(User.class)
                //.where(User_Table.id.eq(userId))
                .querySingle();
    }

    public static String getUserId() {
        return userId;
    }

    public static String getToken() {
        return token;
    }

    public static void setToken(String token) {
        Account.token = token;
        save(Factory.app());
    }

    public static void setUserId(String userId) {
        Account.userId = userId;
        save(Factory.app());
    }

    public static String getPortrait() {
        return portrait;
    }

    public static void setPortrait(String portrait) {
        Account.portrait = portrait;
        save(Factory.app());
    }

    public static String getName() {
        return name;
    }

    public static void setName(String name) {
        Account.name = name;
        save(Factory.app());
    }
}
