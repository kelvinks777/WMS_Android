package com.gin.wms.manager;

import android.content.Context;

import com.bosnet.ngemart.libgen.Manager;
import com.gin.wms.manager.config.ManagerSetup;
import com.gin.wms.manager.config.NgRouteUrl;
import com.gin.wms.manager.db.contract.EquipmentContract;
import com.gin.wms.manager.db.data.EquipmentData;
import com.gin.wms.manager.db.data.OperatorEquipmentData;

import java.util.List;

public class EquipmentManager extends Manager {

    public EquipmentManager(Context context) throws Exception {
        super(context, new NgRouteUrl().getUrlDomainMs() + "Equipment", new UserContext(context), new ManagerSetup());
    }

    public List<EquipmentData> GetEquipmentNotUsed()throws Exception{
        List<EquipmentData> results = restClient.getList(EquipmentData[].class, "GetEquipmentNotUsed");

        if(results != null){
            DbExecuteWrite(dbClient -> {
                dbClient.DeleteAll(EquipmentData.class);

                for(EquipmentData resulData: results){
                    dbClient.Save(resulData);
                }
            });
        }

        return results;
    }

    public List<EquipmentData> getListEquipmentResultFromLocal(String equipmentId) throws Exception{
        return DbExecuteRead(dbClient ->
                dbClient.Query(EquipmentData.class, EquipmentContract.Query.getSelectList(equipmentId))
        );
    }

    public void SaveOperatorEquipment(OperatorEquipmentData operatorEquipmentData) throws Exception{
        String function ="SaveOperatorEquipment";
        restClient.post(function, operatorEquipmentData);
    }

    public List<OperatorEquipmentData> GetAllOperatorEquipment(String operatorId)throws Exception{
        List<OperatorEquipmentData> results = restClient.getList(OperatorEquipmentData[].class, "GetOperatorEquipmentByOperatorId?operatorId="+operatorId);
        return results;
    }

}
