package com.gin.wms.manager.db.data;

import com.bosnet.ngemart.libgen.Contract;
import com.bosnet.ngemart.libgen.Data;
import com.gin.wms.manager.db.contract.BandedItemContract;

import java.io.Serializable;

public class BandedItemData extends Data implements Serializable {
    public String id = "";
    public String productId = "";
    public double qty = 0;

    @Override
    public Contract getContract() throws Exception {
        return new BandedItemContract();
    }

    @Override
    public String toString() {
        return "BandedItemData{" +
                "id='" + id + '\'' +
                ", productId='" + productId + '\'' +
                ", qty='" + qty + '\'' +
                '}';
    }
}
