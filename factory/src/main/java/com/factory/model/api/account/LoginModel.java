package com.factory.model.api.account;

/**
 * @author wulinpeng
 * @datetime: 18/2/13 下午2:00
 * @description:
 */
public class LoginModel {

    private String phone;
    private String password;

    public LoginModel(String phone, String password) {
        this.phone = phone;
        this.password = password;
    }

    public String getPhone() {
        return phone;
    }

    public void setPhone(String phone) {
        this.phone = phone;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

}
