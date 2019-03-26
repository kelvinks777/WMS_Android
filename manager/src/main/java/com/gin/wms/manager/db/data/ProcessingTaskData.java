package com.gin.wms.manager.db.data;

import com.bosnet.ngemart.libgen.Contract;
import com.gin.wms.manager.db.contract.processing.ProcessingTaskContract;
import com.gin.wms.manager.db.data.base.ProcessingTaskBaseData;

import java.io.Serializable;

/**
 * Created by Fernandes on 10/05/2018.
 */

public class ProcessingTaskData extends ProcessingTaskBaseData implements Serializable {
    public String processingTaskId;
    public ProcessingTaskItemData itemProduct;

    @Override
    public Contract getContract() throws Exception {
        return new ProcessingTaskContract();
    }
}
