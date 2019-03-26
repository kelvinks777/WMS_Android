package com.gin.wms.manager.db.data;

import com.bosnet.ngemart.libgen.Contract;
import com.bosnet.ngemart.libgen.Data;
import com.gin.wms.manager.db.contract.CountingTaskContract;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

public class CountingTaskData extends Data implements Serializable {
    public String countingId = "";
    public String operatorId = "";
    public String ClientId = "";
    public String ClientLocationId = "";
    public Date StartTime;
    public Date EndTime;
    public int StatusAsInt = 0;
    public String refDocUri = "";
    public String docRefId = "";
    public boolean hasBeenStart = false;
    public List<CountingTaskResultData> CountingResults = new ArrayList<>();

    @Override
    public Contract getContract() throws Exception {
        return new CountingTaskContract();
    }
}
