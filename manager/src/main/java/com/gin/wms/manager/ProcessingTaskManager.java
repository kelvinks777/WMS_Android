package com.gin.wms.manager;

import android.content.Context;

import com.bosnet.ngemart.libgen.DbClient;
import com.bosnet.ngemart.libgen.Manager;
import com.gin.wms.manager.config.ManagerSetup;
import com.gin.wms.manager.config.NgRouteUrl;
import com.gin.wms.manager.db.contract.processing.ProcessingTaskItemResultContract;
import com.gin.wms.manager.db.data.CompUomData;
import com.gin.wms.manager.db.data.CompUomItemData;
import com.gin.wms.manager.db.data.ProcessingTaskData;
import com.gin.wms.manager.db.data.ProcessingTaskItemResultData;
import com.gin.wms.manager.db.data.ProductData;
import com.gin.wms.manager.db.data.UomConversionData;
import com.gin.wms.manager.db.data.helper.CompUomHelper;

import java.util.Date;
import java.util.List;

/**
 * Created by Fernandes on 10/05/2018.
 */

public class ProcessingTaskManager extends Manager {

    private ProductManager productManager;

    public ProcessingTaskManager(Context context) throws Exception {
        super(context, new NgRouteUrl().getUrlDomainTask() + "processingTask", new UserContext(context), new ManagerSetup());
        productManager = new ProductManager(context);
    }

    public void startProcessingTask(ProcessingTaskData processingTaskData) throws Exception {
        String function = "startProcessingTask?docUri=" + processingTaskData.refDocUri + "&processingTaskId=" + processingTaskData.processingTaskId;
        restClient.post(ProcessingTaskData.class, function);
    }

    public void finishProcessingTask(ProcessingTaskData processingTaskData) throws Exception {
        restClient.post("FinishProcessingTask?refDocUri=" + processingTaskData.refDocUri + "&refDocId=" + processingTaskData.refDocId);
        DbExecuteWrite(dbClient -> {
            dbClient.DeleteAll(ProcessingTaskData.class);
            dbClient.DeleteAll(ProcessingTaskItemResultData.class);
        });
    }

    private void saveProcessingTaskData(DbClient dbClient, ProcessingTaskData processingTaskData) throws Exception {
        CompUomData compUomData = processingTaskData.itemProduct.getCompUom();
        CompUomHelper helper = new CompUomHelper(processingTaskData.itemProduct.getCompUom());

        if (compUomData != null) {
            processingTaskData.compUomId = processingTaskData.itemProduct.getCompUom().compUomId;
            dbClient.Save(processingTaskData.itemProduct.getCompUom());
            for (CompUomItemData compUomItemData :
                    compUomData.compUomItems) {
                compUomItemData.compUomId = compUomData.compUomId;
                dbClient.Save(compUomItemData);
            }

            for (UomConversionData uomConversionData :
                    compUomData.uomConversions) {
                dbClient.Save(uomConversionData);
            }
        }

        dbClient.Save(ProductData.getFromBase(processingTaskData));
        processingTaskData.itemProduct.palletQty = processingTaskData.itemProduct.getCalcPalletByQty();
        processingTaskData.itemProduct.qtyCompUomValue = processingTaskData.itemProduct.getCompUomValueFromQty();
        double qty = 0;
        for (ProcessingTaskItemResultData resultData :
                processingTaskData.itemProduct.results) {
            resultData.processingTaskId = processingTaskData.processingTaskId;
            resultData.productId = processingTaskData.itemProduct.productId;
            resultData.clientId = processingTaskData.itemProduct.clientId;
            resultData.compUomValue = helper.getCompUomValueFromTotal(resultData.qty);
            qty += resultData.qty;
            resultData.qtyRemaining = qty;
            dbClient.Save(resultData);
        }
        processingTaskData.itemProduct.qty = qty;

        processingTaskData.itemProduct.qtyCheckResultCompUomValue = helper.getCompUomValueFromTotal(processingTaskData.itemProduct.qtyCheckResult);
        dbClient.Save(processingTaskData);
    }

    public void createResult(String taskId, String productId, String clientId, String palletNo, Date expiredDate, double qty) throws Exception {
        ProcessingTaskItemResultData processingTaskItemResultData = DbExecuteRead(dbClient ->
                dbClient.Find(ProcessingTaskItemResultData.class, new String[]{taskId,productId,clientId, palletNo})
        );

        if (processingTaskItemResultData == null) {
            ProcessingTaskItemResultData result = new ProcessingTaskItemResultData();
            result.processingTaskId = taskId;
            result.productId = productId;
            result.clientId = clientId;
            result.palletNo = palletNo;
            result.qty = qty;
            result.expiredDate = expiredDate;
            String function = "CreateProcessingResult?processingTaskResultData=" + result;
            restClient.post(function, result);

            DbExecuteWrite(dbClient -> {
                dbClient.Insert(result);
            });
        }
    }

    public ProcessingTaskData getProcessingTask() throws  Exception{
        String functionName = "GetProcessingTask";
        ProcessingTaskData processingTaskData = restClient.get(ProcessingTaskData.class, functionName);
        DbExecuteWrite(dbClient -> {
            if (processingTaskData != null) {
                saveProcessingTaskData(dbClient, processingTaskData);
            }
        });

        return processingTaskData;
    }

    public List<ProcessingTaskItemResultData> getItemResultDataFromLocal(String taskId, String productId) throws Exception {
        return DbExecuteRead(dbClient ->
                dbClient.Query(ProcessingTaskItemResultData.class, ProcessingTaskItemResultContract.Query.getSelectList(taskId, productId)));
    }
}
