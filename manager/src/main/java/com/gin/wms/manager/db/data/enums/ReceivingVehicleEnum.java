package com.gin.wms.manager.db.data.enums;

public enum ReceivingVehicleEnum {
    ARRIVED(0), PROCESS(1), FINISH(2);

    private final int value;
    ReceivingVehicleEnum(int value) {
        this.value = value;
    }

    public static ReceivingVehicleEnum init (int value) {
        for (ReceivingVehicleEnum taskEnum : values()) {
            if (taskEnum.getValue() == value)
                return taskEnum;
        }
        return null;
    }

    public int getValue() {
        return value;
    }
}
