package com.factory.model.api.account;

/**
 * @author wulinpeng
 * @datetime: 18/1/26 下午4:10
 * @description:
 */
public class RegisterModel {

    private String phone;
    private String password;
    private String name;

    public RegisterModel(String account, String password, String name) {
        this.phone = account;
        this.password = password;
        this.name = name;
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

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    @Override
    public String toString() {
        return "RegisterModel{" +
                "phone='" + phone + '\'' +
                ", password='" + password + '\'' +
                ", name='" + name + '\'' +
                '}';
    }
}
