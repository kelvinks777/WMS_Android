package com.gin.wms.manager.db.contract;

import com.bosnet.ngemart.libgen.Contract;
import com.bosnet.ngemart.libgen.SqlLiteDataType;

import java.text.MessageFormat;
import java.util.Date;

public class CountingTaskResultContract extends Contract {
    public static final String TABLE_NAME = "counting_task_result";

    public CountingTaskResultContract() throws Exception{
        super();
    }

    @Override
    public String getTableName() {
        return TABLE_NAME;
    }

    @Override
    public String[] getListColumn() {
        return new String[]{
                Column.COUNTING_ID,
                Column.BIN_ID,
                Column.PALLET_NO,
                Column.PRODUCT_ID,
                Column.QTY,
                Column.HAS_CHECKED,
                Column.UPDATED,
        };
    }

    @Override
    public String[] getListType() {
        return new String[]{
                Type.COUNTING_ID,
                Type.BIN_ID,
                Type.PALLET_NO,
                Type.PRODUCT_ID,
                Type.QTY,
                Type.HAS_CHECKED,
                Type.UPDATED,
        };
    }

    @Override
    public String[] getPrimaryKey() {
        return new String[]{
                Column.COUNTING_ID,
                Column.BIN_ID,
                Column.PALLET_NO,
                Column.PRODUCT_ID,
        };
    }

    public class Column{
        public final static String COUNTING_ID = "countingId";
        public final static String BIN_ID = "BinId";
        public final static String PALLET_NO = "PalletNo";
        public final static String PRODUCT_ID = "ProductId";
        public final static String QTY = "Qty";
        public final static String HAS_CHECKED = "hasChecked";
        public final static String UPDATED = "updated";
    }

    public class Type{
        public final static String COUNTING_ID = SqlLiteDataType.TEXT;
        public final static String BIN_ID = SqlLiteDataType.TEXT;
        public final static String PALLET_NO = SqlLiteDataType.TEXT;
        public final static String PRODUCT_ID = SqlLiteDataType.TEXT;
        public final static String QTY = SqlLiteDataType.DOUBLE;
        public final static String HAS_CHECKED = SqlLiteDataType.INTEGER;
        public final static String UPDATED = SqlLiteDataType.DATE;
    }

    public static class Query {
        private final static String SELECT = "SELECT * FROM ";

        private final static String DELETE = "DELETE FROM ";

        private final static String LIST_EXPRESSION = TABLE_NAME
                + " WHERE "
                + Column.COUNTING_ID + " = ''{0}'' ";

        public static String getDeleteList(String taskId) {
            return DELETE + MessageFormat.format(LIST_EXPRESSION, taskId);
        }

        public static String getSelectList(String taskId) {
            return SELECT + MessageFormat.format(LIST_EXPRESSION, taskId);
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
