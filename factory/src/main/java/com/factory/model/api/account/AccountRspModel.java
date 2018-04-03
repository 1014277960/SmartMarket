package com.factory.model.api.account;

import com.factory.model.card.UserCard;
import com.factory.model.db.User;

/**
 * @author wulinpeng
 * @datetime: 18/2/13 下午1:47
 * @description:
 */
public class AccountRspModel {

    public AccountRspModel(User user) {
        this.userCard = new UserCard(user);
        this.token = user.getToken();
    }

    private UserCard userCard;

    // account返回的是登陆／注册的api，需要返回token
    private String token;

    public UserCard getUserCard() {
        return userCard;
    }

    public void setUserCard(UserCard userCard) {
        this.userCard = userCard;
    }

    public String getToken() {
        return token;
    }

    public void setToken(String token) {
        this.token = token;
    }
}
