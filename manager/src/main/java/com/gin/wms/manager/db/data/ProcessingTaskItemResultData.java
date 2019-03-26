package com.gin.wms.manager.db.data;

import com.bosnet.ngemart.libgen.Contract;
import com.bosnet.ngemart.libgen.Data;
import com.gin.wms.manager.db.contract.processing.ProcessingTaskItemResultContract;

import java.util.Date;

import static com.bosnet.ngemart.libgen.DateUtil.GetMinDate;

/**
 * Created by Fernandes on 10/05/2018.
 */

public class ProcessingTaskItemResultData extends Data {
    public String processingTaskId = "";
    public String productId = "";
    public String clientId = "";
    public String palletNo = "";
    public double qty = 0;
    public double qtyRemaining = 0;
    public Date expiredDate = new Date(GetMinDate());

    public String compUomValue = "";

    public boolean alreadyUsed = false;

    @Override
    public Contract getContract() throws Exception {
        return new ProcessingTaskItemResultContract();
    }
}
