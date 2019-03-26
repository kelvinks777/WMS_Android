package com.gin.wms.manager.db.data;

import com.bosnet.ngemart.libgen.Contract;
import com.gin.wms.manager.db.contract.CountingTaskResultContract;
import com.gin.wms.manager.db.data.base.CountingTaskItemBaseData;

import java.io.Serializable;

public class CountingTaskResultData extends CountingTaskItemBaseData implements Serializable{
    public String countingId = "";

    @Override
    public Contract getContract() throws Exception {
        return new CountingTaskResultContract();
    }
}
