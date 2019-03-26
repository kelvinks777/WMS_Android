package com.gin.wms.manager.db.data;

import com.bosnet.ngemart.libgen.Contract;
import com.bosnet.ngemart.libgen.Data;
import com.gin.wms.manager.db.contract.EquipmentContract;

import java.io.Serializable;

public class EquipmentData extends Data  implements Serializable {
    public String equipmentId;
    public String equipmentTypeId;
    public String name;

    @Override
    public Contract getContract() throws Exception {
        return new EquipmentContract();
    }
}
