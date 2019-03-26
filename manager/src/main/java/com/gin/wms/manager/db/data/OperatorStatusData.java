package com.gin.wms.manager.db.data;

import com.bosnet.ngemart.libgen.Contract;
import com.bosnet.ngemart.libgen.Data;
import com.gin.wms.manager.db.contract.OperatorStatusContract;

import java.io.Serializable;
import java.util.Date;

public class OperatorStatusData extends Data implements Serializable {
    public String operatorId;
    public String operatorName;
    public int Type;
    public int Status;
    public int Attendance;
    public int numberOfTask;
    public Date dateRecord;

    @Override
    public Contract getContract() throws Exception {
        return new OperatorStatusContract();
    }
}
