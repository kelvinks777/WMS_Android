package com.gin.wms.manager.db.data;

import com.bosnet.ngemart.libgen.Contract;
import com.bosnet.ngemart.libgen.Data;

import com.gin.wms.manager.db.contract.StockCountingOrderItemDataByBinIdContract;

import java.io.Serializable;

public class StockCountingOrderItemByBinIdData extends Data implements Serializable {
    public String id;
    public String binId;
    public String operatorId;

    @Override
    public Contract getContract() throws Exception {
        return new StockCountingOrderItemDataByBinIdContract();
    }

}
