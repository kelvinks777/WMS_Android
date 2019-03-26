package com.gin.wms.manager.db.data.enums;

public enum MutationTypeEnum {
    BADTOGOOD(0), GOODTOBAD(1), NA(2);

    private final int value;

    MutationTypeEnum(int value) {
        this.value = value;
    }

    public static MutationTypeEnum init(int value){
        for (MutationTypeEnum typeEnum : values()) {
            if (typeEnum.getValue() == value)
                return typeEnum;
        }
        return null;
    }

    public int getValue() {
        return value;
    }
}
