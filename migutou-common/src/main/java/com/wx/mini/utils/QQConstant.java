package com.wx.mini.utils;

/**
 * @author alan.chen
 * @date 2019/11/10 7:30 PM
 */
public enum QQConstant {

    /**
     * 土味情话
     */
    EARTH_SWEET_WORD("1"),

    /**
     * 彩虹屁
     */
    RAINBOW_WORD("2"),

    /**
     * 聊天
     */
    CHAT("3");

    public final String value;

    private QQConstant(String value) {
        this.value = value;
    }

    public String getValue() {
        return value;
    }

    /**
     * 根据value返回枚举类型，主要在switch中使用
     */
    public static QQConstant getByValue(String value) {
        for (QQConstant constant : values()) {
            if(constant.getValue().equals(value)) {
                return constant;
            }
        }
        return null;
    }

}
