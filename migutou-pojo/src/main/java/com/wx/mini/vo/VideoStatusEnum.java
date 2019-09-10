package com.wx.mini.vo;

/**
 * @auther alan.chen
 * @time 2019/9/10 9:41 PM
 */
public enum VideoStatusEnum {

    SUCCESS(1), NOT_SUCCESS(2);

    public final int value;

    private VideoStatusEnum(int value) {
        this.value = value;
    }

    public int getValue() {
        return value;
    }
}
