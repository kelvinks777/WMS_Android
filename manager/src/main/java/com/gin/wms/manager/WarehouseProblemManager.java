package com.gin.wms.manager;

import android.content.Context;

import com.bosnet.ngemart.libgen.Manager;
import com.gin.wms.manager.config.ManagerSetup;
import com.gin.wms.manager.config.NgRouteUrl;
import com.gin.wms.manager.db.data.WarehouseProblemData;

import java.util.List;


/**
 * Created by Fernandes on 10/18/2018.
 */

public class WarehouseProblemManager extends Manager  {

    public WarehouseProblemManager(Context context) throws Exception {
        super(context, new NgRouteUrl().getUrlDomainTask() + "WarehouseProblem", new UserContext(context), new ManagerSetup());
    }

    public void CreateWarehouseProblemData(WarehouseProblemData warehouseProblemData) throws Exception {
        String function = "CreateWarehouseProblemData";
        restClient.post(function, warehouseProblemData);
    }

    public List<WarehouseProblemData> GetWarehouseProblem() throws Exception{
        List<WarehouseProblemData> results = restClient.getList(WarehouseProblemData[].class, "GetWarehouseProblem");
        DbExecuteWrite(dbClient -> {
            if(results != null){

                dbClient.DeleteAll(WarehouseProblemData.class);
                for(WarehouseProblemData resulData: results){
                    dbClient.Save(resulData);
                }
            }
        });
        return results;
    }

    public void SaveWarehouseProblemData(WarehouseProblemData warehouseProblemData) throws Exception{
        String function="SaveWarehouseProblemData?warehouseProblemData";
        restClient.post(function, warehouseProblemData);

        DbExecuteWrite(dbClient -> {
            dbClient.Save(warehouseProblemData);
        });

    }

    public void DeleteData(WarehouseProblemData warehouseProblemData) throws Exception{
        String function="DeleteData?warehouseProblemData";
        restClient.post(function, warehouseProblemData);

        DbExecuteWrite(dbClient -> {
            dbClient.Delete(warehouseProblemData);
        });

    }


}
