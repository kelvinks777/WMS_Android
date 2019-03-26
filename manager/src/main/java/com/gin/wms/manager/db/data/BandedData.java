package com.gin.wms.manager.db.data;

import com.bosnet.ngemart.libgen.Contract;
import com.bosnet.ngemart.libgen.Data;
import com.gin.wms.manager.db.contract.BandedContract;

import java.io.Serializable;
import java.util.Date;
import java.util.List;

public class BandedData extends Data implements Serializable {
    public String id;
    public String clientId;
    public Date date;
    public String longInfo;
    public int partyAsInt;
    public Date docDate;
    public boolean isFinished;
    public int statusAsInt;
    public int docTypeAsInt;
    public String sourceId;
    public String docRef;
    public String docRefUri;
    public int sourceTypeIdasInt;
    public String operatorNumber;
    public List<BandedItemData> bandedItemDataList;

    @Override
    public Contract getContract() throws Exception {
        return new BandedContract();
    }

    @Override
    public String toString() {
        return "BandedData{" +
                "id='" + id + '\'' +
                ", clientId='" + clientId + '\'' +
                ", date='" + date + '\'' +
                ", longInfo='" + longInfo + '\'' +
                ", partyAsInt=" + partyAsInt +
                ", docDate=" + docDate +
                ", isFinished=" + isFinished +
                ", statusAsInt=" + statusAsInt +
                ", docTypeAsInt='" + docTypeAsInt + '\'' +
                ", sourceId='" + sourceId + '\'' +
                ", docRef='" + docRef + '\'' +
                ", docRefUri='" + docRefUri + '\'' +
                ", sourceTypeIdasInt='" + sourceTypeIdasInt + '\'' +
                ", operatorNumber=" + operatorNumber +
                ", bandedItemDataList=" + bandedItemDataList +
                '}';
    }
}
