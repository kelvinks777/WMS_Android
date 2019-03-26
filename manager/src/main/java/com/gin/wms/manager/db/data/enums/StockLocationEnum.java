package com.gin.wms.manager.db.data.enums;

public enum StockLocationEnum {
    UNFREEZE_STOCK(0), FREEZE_STOCK(1);

    private final int value;
    StockLocationEnum(int value) {
        this.value = value;
    }

    public static StockLocationEnum init (int value) {
        for (StockLocationEnum stockLocationEnum : values()) {
            if (stockLocationEnum.getValue() == value)
                return stockLocationEnum;
        }
        return null;
    }

    public int getValue() {
        return value;
    }
}
