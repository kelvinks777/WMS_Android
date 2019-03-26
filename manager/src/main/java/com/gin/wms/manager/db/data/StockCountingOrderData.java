package com.gin.wms.manager.db.data;

import com.bosnet.ngemart.libgen.Contract;
import com.bosnet.ngemart.libgen.Data;
import com.gin.wms.manager.db.contract.StockCountingOrderContract;

import org.joda.time.DateTime;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

public class StockCountingOrderData extends Data implements Serializable {
    public String Id;
    public String clientId;
    public Date docDate;
    public String longInfo;
    public boolean isFinished;
    public int Status;
    public int docType;
    public String sourceId;
    public String docRefId;
    public String docRefUri;
    public List<StockCountingOrderItem> Items = new ArrayList<>();
    public List<StockCountingOrderItemByBinIdData> ItemsByBin = new ArrayList<>();
    public List<StockCountingOrderItemByProductIdData> itemsByProduct = new ArrayList<>();


    @Override
    public Contract getContract() throws Exception {
        return new StockCountingOrderContract();
    }

}