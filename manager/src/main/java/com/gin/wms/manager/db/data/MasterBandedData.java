package com.gin.wms.manager.db.data;

import com.bosnet.ngemart.libgen.Contract;
import com.bosnet.ngemart.libgen.Data;
import com.gin.wms.manager.db.contract.MasterBandedContract;

import java.io.Serializable;

public class MasterBandedData extends Data implements Serializable {
    public String bandedId;
    public String productBandedId;
    public int productComp;

    @Override
    public Contract getContract() throws Exception {
        return new MasterBandedContract();
    }

    @Override
    public String toString() {
        return "MasterBandedData{" +
                "bandedId='" + bandedId + '\'' +
                ", productBandedId='" + productBandedId + '\'' +
                ", productComp='" + productComp + '\'' +
                '}';
    }
}