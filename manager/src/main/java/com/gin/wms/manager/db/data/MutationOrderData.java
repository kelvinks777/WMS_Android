package com.gin.wms.manager.db.data;

import com.bosnet.ngemart.libgen.Contract;
import com.bosnet.ngemart.libgen.Data;
import com.gin.wms.manager.db.contract.MutationOrderContract;
import com.gin.wms.manager.db.data.enums.MutationTypeEnum;

import java.io.Serializable;
import java.util.Date;

public class MutationOrderData extends Data implements Serializable {
    public String recOrderNo;
    public String trxTypeId;
    public int iSource;
    public String sourceId;
    public Date date;
    public Date docDate;
    public String prevRO;
    public String infoId;
    public String longInfo;
    public int docStatus;
    public int orderType;
    public int operatorCount;
    public MutationTypeEnum enumMutationType;

    @Override
    public Contract getContract() throws Exception {
        return new MutationOrderContract();
    }
}
