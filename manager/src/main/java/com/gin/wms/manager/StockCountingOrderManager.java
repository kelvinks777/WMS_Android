package com.gin.wms.manager;

import android.content.Context;

import com.bosnet.ngemart.libgen.Manager;
import com.gin.wms.manager.config.ManagerSetup;
import com.gin.wms.manager.config.NgRouteUrl;
import com.gin.wms.manager.db.contract.StockCountingOrderItemDataByBinIdContract;
import com.gin.wms.manager.db.contract.StockCountingOrderItemDataByProductIdContract;
import com.gin.wms.manager.db.contract.StockCountingOrderItemContract;
import com.gin.wms.manager.db.data.StockCountingOrderData;
import com.gin.wms.manager.db.data.StockCountingOrderItem;
import com.gin.wms.manager.db.data.StockCountingOrderItemByBinIdData;
import com.gin.wms.manager.db.data.StockCountingOrderItemByProductIdData;



import java.util.List;

public class StockCountingOrderManager extends Manager {

    public StockCountingOrderManager(Context context) throws Exception {
        super(context, new NgRouteUrl().getUrlDomainTask() + "StockCountingOrder", new UserContext(context), new ManagerSetup());
    }

    public void  CreateStockCountingOrder(StockCountingOrderData stockCountingOrder, String key) throws Exception{
        String function="CreateStockCountingOrder?stockCountingOrder="+stockCountingOrder+"&key="+key;
        restClient.post(function, stockCountingOrder);
    }

    public void  CreateStockCountingOrder2(StockCountingOrderData stockCountingOrder) throws Exception{
        String function="CreateStockCountingOrderByBinId?stockCountingOrder";
        restClient.post(function, stockCountingOrder);
    }

    public void  CreateStockCountingOrderByProduct(StockCountingOrderData stockCountingOrder) throws Exception{
        String function="CreateStockCountingOrderByProductId?stockCountingOrder";
        restClient.post(function, stockCountingOrder);
    }

    public void SaveStockCountingOrderData(List<StockCountingOrderItemByBinIdData> stockCountingOrderItemByBinIdData) throws Exception{
        DbExecuteWrite(dbClient -> {
            dbClient.DeleteAll(StockCountingOrderItemByBinIdData.class);

            for(StockCountingOrderItemByBinIdData resulData: stockCountingOrderItemByBinIdData){
                dbClient.Save(resulData);
            }

        });
    }

    public void SaveStockCountingOne(StockCountingOrderItemByBinIdData stockCountingOrderItemByBinIdData) throws Exception{
        DbExecuteWrite(dbClient -> {
            //dbClient.DeleteAll(StockCountingOrderItemByBinIdData.class);
            dbClient.Insert(stockCountingOrderItemByBinIdData);
        });
    }

    public void SaveStockCountingOneByProduct(StockCountingOrderItemByProductIdData stockCountingOrderItemByProductIdData) throws Exception{
        DbExecuteWrite(dbClient -> {
            //dbClient.DeleteAll(StockCountingOrderItemByBinIdData.class);
            dbClient.Insert(stockCountingOrderItemByProductIdData);
        });
    }

    public void SaveStockCountingOrderItem(StockCountingOrderItem stockCountingOrderItem) throws Exception{
        DbExecuteWrite(dbClient -> {
            dbClient.Insert(stockCountingOrderItem);
        });
    }

    public List<StockCountingOrderItemByBinIdData> getListCountingResultFromLocal(String Id) throws Exception{
        return DbExecuteRead(dbClient ->
                dbClient.Query(StockCountingOrderItemByBinIdData.class, StockCountingOrderItemDataByBinIdContract.Query.getSelectList(Id))
        );
    }

    public List<StockCountingOrderItem> getListStockCountingIdResultFromLocal(String id) throws Exception{
        return DbExecuteRead(dbClient ->
                dbClient.Query(StockCountingOrderItem.class, StockCountingOrderItemContract.Query.getSelectList(id))
        );
    }

    public List<StockCountingOrderItemByProductIdData> getListCountingByProductFromLocal(String productId) throws Exception{
        return DbExecuteRead(dbClient ->
                dbClient.Query(StockCountingOrderItemByProductIdData.class, StockCountingOrderItemDataByProductIdContract.Query.getSelectList(productId))
        );
    }

    public void DeleteStockCountingOneByBin(List<StockCountingOrderItem> stockCountingOrderItemByBinIdData) throws Exception{
        DbExecuteWrite(dbClient -> {
            dbClient.DeleteAll(StockCountingOrderItem.class);

        });
    }

    public void DeleteStockCountingOneByProduct(List<StockCountingOrderItem> stockCountingOrderItemByProductIdData) throws Exception{
        DbExecuteWrite(dbClient -> {
            dbClient.DeleteAll(StockCountingOrderItem.class);

        });
    }

    public void deleteOneListStockCounting(String bin) throws Exception{
        DbExecuteWrite(dbClient -> {
            //removeOperator(taskId);
            dbClient.Execute(StockCountingOrderItemDataByBinIdContract.Query.getDeleteList(bin));
        });
    }

    public void deleteOneListStockCountingByProduct(String productId) throws Exception{
        DbExecuteWrite(dbClient -> {
            //removeOperator(taskId);
            dbClient.Execute(StockCountingOrderItemDataByProductIdContract.Query.getDeleteList(productId));
        });
    }

    public void deleteOneListStockCountingOrderItemByBin(String binId)throws Exception{
        DbExecuteWrite(dbClient -> {
            dbClient.Execute(StockCountingOrderItemContract.Query.getDeleteListByBin(binId));
        });
    }

    public void deleteOneListStockCountingOrderItemByProduct(String productId)throws Exception{
        DbExecuteWrite(dbClient -> {
            dbClient.Execute(StockCountingOrderItemContract.Query.getDeleteListByProductId(productId));
        });
    }

    public void UpdateOneListStockCounting(StockCountingOrderItemByBinIdData stockCountingOrderItemByBinIdData) throws Exception{
        DbExecuteWrite(dbClient -> {
            //dbClient.Execute(StockCountingOrderItemDataByBinIdContract.Query.getDeleteList(bin));
            dbClient.Insert(stockCountingOrderItemByBinIdData);
        });
    }

    public void UpdateOneListStockCountingByProduct(StockCountingOrderItemByProductIdData stockCountingOrderItemByProductIdData) throws Exception{
        DbExecuteWrite(dbClient -> {
            //dbClient.Execute(StockCountingOrderItemDataByBinIdContract.Query.getDeleteList(bin));
            dbClient.Insert(stockCountingOrderItemByProductIdData);
            //dbClient.Update();
        });
    }

    public void UpdateOneListStockCountingOrderByBin(StockCountingOrderItem stockCountingOrderItem) throws Exception{
        DbExecuteWrite(dbClient -> {
            dbClient.Update(stockCountingOrderItem);
        });
    }

    public StockCountingOrderData GetStockCountingOrder(String refDocId) throws Exception{
        String function="GetStockCountingOrder?refDocId="+refDocId;
        return restClient.get(StockCountingOrderData.class,function);
    }

//    public void saveStockCountingOrderItemDataLocal(StockCountingOrderData stockCountingOrderData) throws Exception {
//        DbExecuteWrite(dbClient -> {
//            dbClient.Insert(stockCountingOrderData);
//        });
//    }

//    private void deleteAllAndSaveLocalStockCountingOrderData(StockCountingOrderData stockCountingOrderData) throws Exception {
//        DbExecuteWrite(dbClient -> {
//            dbClient.DeleteAll(StockCountingOrderData.class);
//            dbClient.DeleteAll(StockCountingOrderItemData.class);
//        });
//        saveStockCountingOrderItemDataLocal(stockCountingOrderData);
//    }
//
//
//    public StockCountingOrderItemData getAllStockCountingOrderItemDataLocal() throws Exception{
//        return DbExecuteRead(dbClient -> {
//            StockCountingOrderItemData stockCountingOrderItemData=null;
//            String stockCountingOrderItemTask = "SELECT * FROM " + new StockCountingOrderItemContract().getTableName() + " LIMIT 1";
//            List<StockCountingOrderItemData> stockCountingOrderItemData1 = dbClient.Query(StockCountingOrderItemData.class, stockCountingOrderItemTask);
//            if(stockCountingOrderItemData1.size() > 0){
//                stockCountingOrderItemData=stockCountingOrderItemData1.get(0);
//                String queryBinId = "SELECT * FROM " + new StockCountingOrderItemContract().getTableName()
//                        + " WHERE " + StockCountingOrderItemContract.Column.BINID + " = '" + stockCountingOrderItemData.binId + "'";
//                String queryOperatorId= "SELECT * FROM " + new StockCountingOrderItemContract().getTableName()
//                        + " WHERE " + StockCountingOrderItemContract.Column.OPERATORID + " = '" + stockCountingOrderItemData.operatorId + "'";
//                stockCountingOrderItemData.binId=queryBinId;
//                stockCountingOrderItemData.operatorId=queryOperatorId;
//
//            }
//            return  stockCountingOrderItemData;
//        });
//    }
//
//    public List<StockCountingOrderItemData> getStockCountingOrderItemDataLocal() throws Exception{
////        return DbExecuteRead(dbClient -> {
////                List<StockCountingOrderItemData> results=dbClient.Query(StockCountingOrderItemData.class, StockCountingOrderItemContract.Query.SELECT_ALL);
////                if(results.size() == 0){
////                    return null;
////                }
////                else{
////                    return results;
////                }
////        });
//
//        return DbExecuteRead(dbClient -> dbClient.Query(StockCountingOrderItemData.class, StockCountingOrderItemContract.Query.SELECT_ALL));
//
//    }








}
