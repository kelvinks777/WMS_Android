package com.gin.wms.manager.db.data;

import com.bosnet.ngemart.libgen.Contract;
import com.bosnet.ngemart.libgen.Data;
import com.gin.wms.manager.db.contract.WarehouseProblemContract;
import com.gin.wms.manager.db.data.enums.WarehouseProblemStatusEnum;
import com.gin.wms.manager.db.data.enums.WarehouseProblemTypeEnum;
import com.gin.wms.manager.db.data.enums.WarehouseProblemActionEnum;

import org.joda.time.DateTime;

import java.io.Serializable;
import java.util.Date;

/**
 * Created by Fernandes on 10/18/2018.
 */

public class WarehouseProblemData extends Data implements Serializable {
    public String binId;
    public String palletNo;
    public String productId;
    public String operatorId;
    public String operatorName;
    public Integer Type;
    public Integer Status;
    public Integer Action;
    public Date inputTime;

    @Override
    public String toString() {
        return "warehouseProblemData{" +
                "binId='" + binId + '\'' +
                ", palletId='" + palletNo + '\'' +
                ", productId='" + productId + '\'' +
                ", operatorId='" + operatorId + '\'' +
                ", operatorName='" + operatorName + '\'' +
                ", Type=" + Type +
                ", Status=" + Status +
                ", Action=" + Action +
                ", inputTime=" + inputTime +
                '}';
    }

    public WarehouseProblemStatusEnum getStatus(){
        return WarehouseProblemStatusEnum.init(Status);
    }

    public WarehouseProblemActionEnum getAction(){
        return WarehouseProblemActionEnum.init(Action);
    }

    public WarehouseProblemTypeEnum getType(){
        return WarehouseProblemTypeEnum.init(Type);
    }

    @Override
    public Contract getContract() throws Exception {
        return new WarehouseProblemContract();
    }
}
