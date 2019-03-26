package com.gin.wms.manager;

import android.content.Context;

import com.bosnet.ngemart.libgen.Manager;
import com.gin.wms.manager.config.ManagerSetup;
import com.gin.wms.manager.config.NgRouteUrl;
import com.gin.wms.manager.db.contract.CountingTaskResultContract;
import com.gin.wms.manager.db.data.CountingTaskData;
import com.gin.wms.manager.db.data.CountingTaskResultData;

import java.util.List;

public class CountingTaskManager extends Manager {
    public CountingTaskManager(Context context) throws Exception {
        super(context,new NgRouteUrl().getUrlDomainTask() + "CountingTask", new UserContext(context), new ManagerSetup());
    }

    public CountingTaskData getCountingTask() throws Exception{
        String functionName = "GetCountingTask";
        return restClient.get(CountingTaskData.class, functionName);
    }

    public void startCountingTask(String countingId) throws Exception{
        String function = "StartCountingTask?CountingId=" + countingId;
        restClient.post(CountingTaskData.class, function);
    }

    public void startCountingTaskByBinId(String countingId) throws Exception{
        String functionName = "StartCountingTaskByBinId?countingId="+ countingId;
        restClient.post(CountingTaskData.class, functionName);
    }

    public void startCountingTaskByProductId(String countingId) throws Exception{
        String functionName = "StartCountingTaskByProductId?countingId="+ countingId;
        restClient.post(CountingTaskData.class, functionName);
    }

    public void finishCountingTask(String countingTaskId) throws Exception{
        String function = "FinishCountingTask?CountingId=" + countingTaskId;
        restClient.post(function);

        DbExecuteWrite(dbClient -> {
            dbClient.DeleteAll(CountingTaskData.class);
            dbClient.DeleteAll(CountingTaskResultData.class);
        });
    }

    public void updateResult(String countingId, String binId, String palletNo, String productId, double qty, boolean hasChecked) throws Exception{
        CountingTaskResultData result = new CountingTaskResultData();
        result.countingId = countingId;
        result.BinId = binId;
        result.PalletNo = palletNo;
        result.ProductId = productId;
        result.Qty = qty;
        result.hasChecked = hasChecked;
        String function = "UpdateResult?CountingTaskResultData=" + result;
        restClient.post(function, result);
    }

    public List<CountingTaskResultData> getListCountingResultFromLocal(String countingId) throws Exception{
        return DbExecuteRead(dbClient ->
                dbClient.Query(CountingTaskResultData.class, CountingTaskResultContract.Query.getSelectList(countingId))
        );
    }

    public void saveCountingTaskData(List<CountingTaskResultData> countingTaskResultData) throws Exception {
        DbExecuteWrite(dbClient -> {
            dbClient.DeleteAll(CountingTaskResultData.class);

            for (CountingTaskResultData resultData: countingTaskResultData) {
                dbClient.Save(resultData);
            }
        });
    }
}
