package com.gin.wms.manager.db.contract.processing;

import com.bosnet.ngemart.libgen.SqlLiteDataType;
import com.gin.wms.manager.db.contract.base.ProductBaseContract;

import java.text.MessageFormat;

/**
 * Created by Fernandes on 10/05/2018.
 */

public class ProcessingTaskItemContract extends ProductBaseContract {
    public static final String TABLE_NAME = "processing_task_item";

    public ProcessingTaskItemContract() throws Exception {
        super();
    }

    @Override
    public String getTableName() {
        return TABLE_NAME;
    }

    @Override
    public String[] getListColumn() {
        return new String[]{
                Column.PROCESSING_TASK_ID,
                Column.QTY,
                Column.PALLET_QTY,
                Column.QTY_CHECK_RESULT,
                Column.QTY_COMP_UOM_VALUE,
                Column.QTY_CHECK_RESULT_COMP_UOM_VALUE,
        };
    }

    @Override
    public String[] getListType() {
        return new String[]{
                Type.PROCESSING_TASK_ID,
                Type.QTY,
                Type.PALLET_QTY,
                Type.QTY_CHECK_RESULT,
                Type.QTY_COMP_UOM_VALUE,
                Type.QTY_CHECK_RESULT_COMP_UOM_VALUE,
        };
    }


    @Override
    public String[] getPrimaryKey() {
        return new String[]{
                Column.PROCESSING_TASK_ID,
        };
    }

    public class Column {
        public final static String PROCESSING_TASK_ID = "processingTaskId";
        public final static String QTY_COMP_UOM_VALUE = "qtyCompUomValue";
        public final static String PALLET_QTY = "palletQty";
        public final static String QTY_CHECK_RESULT_COMP_UOM_VALUE = "qtyCheckResultCompUomValue";
        public final static String QTY = "qty";
        public final static String QTY_CHECK_RESULT = "qtyCheckResult";
    }

    public class Type {
        public final static String PROCESSING_TASK_ID = SqlLiteDataType.TEXT;
        public final static String QTY = SqlLiteDataType.DOUBLE;
        public final static String PALLET_QTY = SqlLiteDataType.DOUBLE;
        public final static String QTY_CHECK_RESULT = SqlLiteDataType.DOUBLE;
        public final static String QTY_COMP_UOM_VALUE = SqlLiteDataType.TEXT;
        public final static String QTY_CHECK_RESULT_COMP_UOM_VALUE = SqlLiteDataType.TEXT;
    }

    public static class Query {
        private final static String SELECT = "SELECT * FROM ";
        private final static String DELETE = "DELETE FROM ";

        private final static String PK_EXPRESSION = TABLE_NAME
                + " WHERE "
                + Column.PROCESSING_TASK_ID + " = ''{0}'' AND "
                + ProductBaseContract.Column.PRODUCT_ID + " = ''{1}'' AND "
                + ProductBaseContract.Column.CLIENT_LOCATION_ID + " = ''{2}'' ";

        private final static String LIST_EXPRESSION = TABLE_NAME
                + " WHERE "
                + Column.PROCESSING_TASK_ID + " = ''{0}'' ";

        public static String getDeleteList(String taskId) {
            return DELETE + MessageFormat.format(LIST_EXPRESSION, taskId);
        }

        public static String getSelectList(String taskId) {
            return SELECT + MessageFormat.format(LIST_EXPRESSION, taskId) + " ORDER BY " + ProductBaseContract.Column.PRODUCT_NAME;
        }
    }
}
