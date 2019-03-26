package com.gin.wms.manager;

import android.content.Context;

import com.bosnet.ngemart.libgen.Manager;
import com.bosnet.ngemart.libgen.NgContext;
import com.bosnet.ngemart.libgen.Setup;
import com.gin.wms.manager.config.ManagerSetup;
import com.gin.wms.manager.config.NgRouteUrl;
import com.gin.wms.manager.db.data.OperatorStatusData;

import java.util.List;

public class OperatorStatusTaskManager extends Manager {

    public OperatorStatusTaskManager(Context context) throws Exception {
        super(context, new NgRouteUrl().getUrlDomainMs() + "OperatorStatus", new UserContext(context), new ManagerSetup());
    }

    public List<OperatorStatusData> getListOperatorStatus() throws Exception{
        List<OperatorStatusData> results = restClient.getList(OperatorStatusData[].class, "GetAllAttendOperator");
        return results;
    }

    public void saveEquipmentDataToLocal(List<OperatorStatusData> operatorStatusData)throws Exception{
        DbExecuteWrite(dbClient -> {
            dbClient.DeleteAll(OperatorStatusData.class);

            for(OperatorStatusData resulData: operatorStatusData){
                dbClient.Save(resulData);
            }
        });
    }


}
