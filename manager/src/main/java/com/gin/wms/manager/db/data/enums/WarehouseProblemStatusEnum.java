package com.gin.wms.manager.db.data.enums;

/**
 * Created by Fernandes on 10/22/2018.
 */

public enum WarehouseProblemStatusEnum {
    NEW(1), RESOLVED(2);

    private final int value;
    WarehouseProblemStatusEnum(int value) {
        this.value = value;
    }

    public static WarehouseProblemStatusEnum init (int value) {
        for (WarehouseProblemStatusEnum statusEnum : values()) {
            if (statusEnum.getValue() == value)
                return statusEnum;
        }
        return null;
    }

    public int getValue() {
        return value;
    }
}
