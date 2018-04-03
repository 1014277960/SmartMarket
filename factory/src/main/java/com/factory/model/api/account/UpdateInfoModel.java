package com.factory.model.api.account;

/**
 * @author wulinpeng
 * @datetime: 18/2/14 下午2:28
 * @description:
 */
public class UpdateInfoModel {

    private String portrait;

    public UpdateInfoModel(String portrait) {
        this.portrait = portrait;
    }

    public String getPortrait() {
        return portrait;
    }

    public void setPortrait(String portrait) {
        this.portrait = portrait;
    }
}
