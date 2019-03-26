package com.gin.wms.manager.db.data;

import com.bosnet.ngemart.libgen.Contract;
import com.bosnet.ngemart.libgen.Data;
import com.gin.wms.manager.db.contract.OperatorEquipmentContract;

import java.io.Serializable;

public class OperatorEquipmentData extends Data implements Serializable{
    public String operatorId;
    public String equipmentId;
    public String equipmentTypeId;
    public int priority;

    @Override
    public Contract getContract() throws Exception {
        return new OperatorEquipmentContract();
    }
}
