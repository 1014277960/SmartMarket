package com.factory.model.card;

import com.factory.model.db.User;

/**
 * @author wulinpeng
 * @datetime: 18/2/13 下午1:46
 * @description:
 */
public class UserCard {

    public UserCard(User user) {
        this.id = user.getId();
        this.name = user.getName();
        this.phone = user.getPhone();
        this.portrait = user.getPortrait();
    }

    public User buildUser() {
        User user = new User();
        user.setId(id);
        user.setName(name);
        user.setPhone(phone);
        user.setPortrait(portrait);

        return user;
    }

    private String id;

    private String name;

    private String phone;

    private String portrait;

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getPhone() {
        return phone;
    }

    public void setPhone(String phone) {
        this.phone = phone;
    }

    public String getPortrait() {
        return portrait;
    }

    public void setPortrait(String portrait) {
        this.portrait = portrait;
    }
}
