package com.gin.wms.manager.db.data.enums;

/**
 * Created by Fernandes on 10/22/2018.
 */

public enum WarehouseProblemTypeEnum {
    WRONG_LOCATION(0);

    private final int value;
    WarehouseProblemTypeEnum(int value) {
        this.value = value;
    }

    public static WarehouseProblemTypeEnum init (int value) {
        for (WarehouseProblemTypeEnum typeEnum : values()) {
            if (typeEnum.getValue() == value)
                return typeEnum;
        }
        return null;
    }

    public int getValue() {
        return value;
    }
}