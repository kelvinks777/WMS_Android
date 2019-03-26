package com.gin.wms.manager.db.contract.checker;

import com.bosnet.ngemart.libgen.Contract;
import com.bosnet.ngemart.libgen.SqlLiteDataType;

import java.text.MessageFormat;

/**
 * Created by manbaul on 4/26/2018.
 */

public class CheckerTaskItemResultContract extends Contract {
    public final static String TABLE_NAME = "checker_task_item_result";

    public CheckerTaskItemResultContract() throws Exception {
        super();
    }

    @Override
    public String getTableName() {
        return TABLE_NAME;
    }

    @Override
    public String[] getListColumn() {
        return new String[] {
                Column.TASK_ID,
                Column.PRODUCT_ID,
                Column.CLIENT_LOCATION_ID,
                Column.CLIENT_ID,
                Column.QTY,
                Column.PALLET_NO,
                Column.GOOD_QTY,
                Column.BAD_QTY,
                Column.GOOD_COMPUOM_VALUE,
                Column.BAD_COMPUOM_VALUE,
                Column.ALREADY_USED,
                Column.EXPIRED_DATE,
                Column.UPDATED,
        };
    }

    @Override
    public String[] getListType() {
        return new String[] {
                Type.TASK_ID,
                Type.PRODUCT_ID,
                Type.CLIENT_LOCATION_ID,
                Type.CLIENT_ID,
                Type.QTY,
                Type.PALLET_NO,
                Type.GOOD_QTY,
                Type.BAD_QTY,
                Type.GOOD_COMPUOM_VALUE,
                Type.BAD_COMPUOM_VALUE,
                Type.ALREADY_USED,
                Type.EXPIRED_DATE,
                Type.UPDATED,
        };
    }

    @Override
    public String[] getPrimaryKey() {
        return new String[] {
                Column.TASK_ID,
                Column.PRODUCT_ID,
                Column.CLIENT_LOCATION_ID,
                Column.PALLET_NO,
        };
    }
    public class Column {
        public final static String UPDATED = "updated";
        public final static String TASK_ID = "taskId";
        public final static String PRODUCT_ID = "productId";
        public final static String CLIENT_LOCATION_ID = "clientLocationId";
        public final static String CLIENT_ID = "clientId";
        public final static String QTY = "qty";
        public final static String PALLET_NO = "palletNo";
        public final static String GOOD_QTY = "goodQty";
        public final static String BAD_QTY = "badQty";
        public final static String GOOD_COMPUOM_VALUE = "goodCompUomValue";
        public final static String BAD_COMPUOM_VALUE = "badCompUomValue";
        public final static String ALREADY_USED = "alreadyUsed";
        public final static String EXPIRED_DATE = "expiredDate";
    }

    public class Type {
        public final static String UPDATED = SqlLiteDataType.DATE;
        public final static String TASK_ID = SqlLiteDataType.TEXT;
        public final static String PRODUCT_ID = SqlLiteDataType.TEXT;
        public final static String CLIENT_LOCATION_ID = SqlLiteDataType.TEXT;
        public final static String CLIENT_ID = SqlLiteDataType.TEXT;
        public final static String QTY = SqlLiteDataType.DOUBLE;
        public final static String PALLET_NO = SqlLiteDataType.TEXT;
        public final static String GOOD_QTY = SqlLiteDataType.DOUBLE;
        public final static String BAD_QTY = SqlLiteDataType.DOUBLE;
        public final static String GOOD_COMPUOM_VALUE = SqlLiteDataType.TEXT;
        public final static String BAD_COMPUOM_VALUE = SqlLiteDataType.TEXT;
        public final static String ALREADY_USED = SqlLiteDataType.INTEGER;
        public final static String EXPIRED_DATE = SqlLiteDataType.DATE;
    }

    public static class Query {
        private static final String SELECT_LIST = "select * from " + TABLE_NAME +
                " where " + Column.TASK_ID + "=''{0}'' and " +
                Column.PRODUCT_ID + "=''{1}'' and " +
                Column.CLIENT_LOCATION_ID + "=''{2}'' ";
        public static String getSelectList(String taskId, String productId, String clientLocationId) {
            return MessageFormat.format(SELECT_LIST, taskId, productId, clientLocationId);
        }
    }

}
