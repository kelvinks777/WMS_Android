package com.gin.wms.manager.db.data;

import com.bosnet.ngemart.libgen.Contract;
import com.bosnet.ngemart.libgen.Data;
import com.gin.wms.manager.db.contract.ReceivingVehicleContract;

import org.joda.time.DateTime;

import java.io.Serializable;
import java.util.Date;

public class ReceivingVehicleData extends Data implements Serializable {
    public String receivingVehicleNo;
    public String policeNo;
    public Date receivingDate;
    public String driverName;
    public int status;

    @Override
    public Contract getContract() throws Exception {
        return new ReceivingVehicleContract();
    }
}
