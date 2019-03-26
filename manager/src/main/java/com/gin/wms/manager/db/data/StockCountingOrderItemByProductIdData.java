package com.gin.wms.manager.db.data;

import com.bosnet.ngemart.libgen.Contract;
import com.bosnet.ngemart.libgen.Data;
import com.gin.wms.manager.db.contract.StockCountingOrderItemDataByProductIdContract;

import java.io.Serializable;

public class StockCountingOrderItemByProductIdData extends Data implements Serializable {
    public String id;
    public String productId;
    public String operatorId;

    @Override
    public Contract getContract() throws Exception {
        return new StockCountingOrderItemDataByProductIdContract();
    }

}
