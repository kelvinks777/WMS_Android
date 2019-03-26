package com.gin.wms.manager.db.data.enums;

public enum DocumentEnum {
    NA(0),
    RECEIVING(1),
    RELEASE(2),
    CONDITION_MOVEMENT(3),
    OTHER(4),
    REWAREHOUSING(5),
    MUTATION(6),
    DESTRUCTION(7),
    BANDED(8),
    STOCK_COUNTING(9);

    private final int value;

    DocumentEnum(int value) {
        this.value = value;
    }

    public static DocumentEnum init(int value){
        for (DocumentEnum typeEnum : values()) {
            if (typeEnum.getValue() == value)
                return typeEnum;
        }
        return null;
    }

    public int getValue() {
        return value;
    }
}
