package com.gin.wms.manager.db.data.enums;

/**
 * Created by Fernandes on 10/22/2018.
 */

public enum WarehouseProblemActionEnum {
    NO_ACTION(0);

    private final int value;
    WarehouseProblemActionEnum(int value) {
        this.value = value;
    }

    public static WarehouseProblemActionEnum init (int value) {
        for (WarehouseProblemActionEnum actionEnum : values()) {
            if (actionEnum.getValue() == value)
                return actionEnum;
        }
        return null;
    }

    public int getValue() {
        return value;
    }
}