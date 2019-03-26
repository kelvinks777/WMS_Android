package com.gin.wms.manager.db.contract;

import com.bosnet.ngemart.libgen.Contract;
import com.bosnet.ngemart.libgen.SqlLiteDataType;

import java.text.MessageFormat;
import java.util.Date;

public class StockCountingOrderItemContract extends Contract {

    public static final String TABLE_NAME = "StockCountingOrderItem";

    public StockCountingOrderItemContract()  throws Exception {
        super();
    }

    @Override
    public String getTableName() {
        return TABLE_NAME;
    }

    @Override
    public String[] getListColumn() {
        return new String[]{
                Column.ID,
                Column.BINID,
                Column.PRODUCTID,
                Column.OPERATORID,
                Column.UPDATED
        };
    }

    @Override
    public String[] getListType() {
        return new String[]{
                Type.ID,
                Type.BINID,
                Type.PRODUCTID,
                Type.OPERATORID,
                Type.UPDATED
        };
    }

    @Override
    public String[] getPrimaryKey() {
        return new String[]{
                Column.BINID,
                Column.PRODUCTID
        };
    }

    public class Column {
        public final static String ID = "id";
        public final static String BINID = "binId";
        public final static String PRODUCTID = "productId";
        public final static String OPERATORID = "operatorId";
        public final static String UPDATED = "updated";
    }

    public class Type {
        public final static String ID = SqlLiteDataType.TEXT;
        public final static String BINID = SqlLiteDataType.TEXT;
        public final static String PRODUCTID = SqlLiteDataType.TEXT;
        public final static String OPERATORID = SqlLiteDataType.TEXT;
        public final static String UPDATED = SqlLiteDataType.DATE;
    }

    public static class Query {
        private final static String SELECT = "SELECT * FROM ";

        private final static String DELETE = "DELETE FROM ";

        private final static String LIST_EXPRESSION = TABLE_NAME
                + " WHERE "
                + Column.ID + " = ''{0}'' ";

        private final static String LIST_EXPRESSION_BIN_ID  = TABLE_NAME
                + " WHERE "
                + Column.BINID+ " =''{0}'' ";

        private final static String LIST_EXPRESSION_PRODUCT_ID = TABLE_NAME
                + " WHERE "
                + Column.PRODUCTID+ " =''{0}'' ";

        public static String getDeleteListByBin(String binId) {
            return DELETE + MessageFormat.format(LIST_EXPRESSION_BIN_ID, binId);
        }

        public static String getDeleteListByProductId(String productId) {
            return DELETE + MessageFormat.format(LIST_EXPRESSION_PRODUCT_ID, productId);
        }

        public static String getSelectList(String Id) {
            return SELECT + MessageFormat.format(LIST_EXPRESSION, Id);
        }

        public static String insertResultList(String taskId, String binId, String palletNo, String productId, double qty, boolean hasChecked, Date updated) {
            int statusCheckedAsInt;
            if(!hasChecked){
                statusCheckedAsInt = 0;
            } else {
                statusCheckedAsInt = 1;
            }
            return ("INSERT INTO " + TABLE_NAME + " VALUES ('" + taskId + "', '" + binId + "', '" + palletNo + "', '" + productId + "', " + qty + ", " + statusCheckedAsInt + ", DATETIME('" + updated + "'));");
        }

    }

}