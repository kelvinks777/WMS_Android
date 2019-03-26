package com.gin.wms.manager.db.data;

import com.bosnet.ngemart.libgen.Contract;
import com.bosnet.ngemart.libgen.Data;
import com.gin.wms.manager.db.contract.StockCountingOrderItemContract;

import java.io.Serializable;

public class StockCountingOrderItem extends Data implements Serializable {
    public String id;
    public String binId;
    public String productId;
    public String operatorId;

    @Override
    public Contract getContract() throws Exception {
        return new StockCountingOrderItemContract();
    }
}
