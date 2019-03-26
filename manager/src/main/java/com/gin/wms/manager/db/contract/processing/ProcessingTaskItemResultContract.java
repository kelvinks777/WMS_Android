package com.gin.wms.manager.db.contract.processing;

import com.bosnet.ngemart.libgen.Contract;
import com.bosnet.ngemart.libgen.SqlLiteDataType;

import java.text.MessageFormat;

/**
 * Created by manbaul on 10/05/2018.
 */

public class ProcessingTaskItemResultContract extends Contract {
    public final static String TABLE_NAME = "processing_task_item_result";

    public ProcessingTaskItemResultContract() throws Exception {
        super();
    }

    @Override
    public String getTableName() {
        return TABLE_NAME;
    }

    @Override
    public String[] getListColumn() {
        return new String[] {
                Column.PROCESSING_TASK_ID,
                Column.PRODUCT_ID,
                Column.CLIENT_ID,
                Column.PALLET_NO,
                Column.QTY,
                Column.QTY_REMAINING,
                Column.COMPUOM_VALUE,
                Column.ALREADY_USED,
                Column.EXPIRED_DATE,
                Column.UPDATED,
        };
    }

    @Override
    public String[] getListType() {
        return new String[] {
                Type.PROCESSING_TASK_ID,
                Type.PRODUCT_ID,
                Type.CLIENT_ID,
                Type.PALLET_NO,
                Type.QTY,
                Type.QTY_REMAINING,
                Type.COMPUOM_VALUE,
                Type.ALREADY_USED,
                Type.EXPIRED_DATE,
                Type.UPDATED,
        };
    }

    @Override
    public String[] getPrimaryKey() {
        return new String[] {
                Column.PROCESSING_TASK_ID,
                Column.PRODUCT_ID,
                Column.CLIENT_ID,
                Column.PALLET_NO,
        };
    }
    public class Column {
        public final static String UPDATED = "updated";
        public final static String PROCESSING_TASK_ID = "processingTaskId";
        public final static String PRODUCT_ID = "productId";
        public final static String CLIENT_ID = "clientId";
        public final static String PALLET_NO = "palletNo";
        public final static String QTY = "qty";
        public final static String QTY_REMAINING = "qtyRemaining";
        public final static String COMPUOM_VALUE = "compUomValue";
        public final static String ALREADY_USED = "alreadyUsed";
        public final static String EXPIRED_DATE = "expiredDate";
    }

    public class Type {
        public final static String UPDATED = SqlLiteDataType.DATE;
        public final static String PROCESSING_TASK_ID = SqlLiteDataType.TEXT;
        public final static String PRODUCT_ID = SqlLiteDataType.TEXT;
        public final static String CLIENT_ID = SqlLiteDataType.TEXT;
        public final static String PALLET_NO = SqlLiteDataType.TEXT;
        public final static String QTY = SqlLiteDataType.DOUBLE;
        public final static String QTY_REMAINING = SqlLiteDataType.DOUBLE;
        public final static String COMPUOM_VALUE = SqlLiteDataType.TEXT;
        public final static String ALREADY_USED = SqlLiteDataType.INTEGER;
        public final static String EXPIRED_DATE = SqlLiteDataType.DATE;
    }

    public static class Query {
        private static final String SELECT_LIST = "select * from " + TABLE_NAME +
                " where " + Column.PROCESSING_TASK_ID + "=''{0}'' and " +
                Column.PRODUCT_ID + "=''{1}''";
        public static String getSelectList(String taskId, String productId) {
            return MessageFormat.format(SELECT_LIST, taskId, productId);
        }
    }

}
