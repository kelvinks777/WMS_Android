package com.gin.wms.manager.db.contract.processing;


import com.bosnet.ngemart.libgen.Contract;
import com.bosnet.ngemart.libgen.SqlLiteDataType;

import java.text.MessageFormat;


/**
 * Created by Fernandes on 10/05/2018.
 */

public class ProcessingTaskContract extends Contract {

    public static final String TABLE_NAME = "processingTask";

    public ProcessingTaskContract() throws Exception {
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
                Column.OPERATOR_ID,
                Column.REF_DOC_ID,
                Column.REF_DOC_URI,
                Column.PROCESSING_AREA_ID,
                Column.MIN_OPERATOR,
                Column.MAX_OPERATOR,
                Column.MULTIPLY_OPERATOR,
                Column.HAS_BEEN_START
        };
    }

    @Override
    public String[] getListType() {
        return new String[]{
                Type.PROCESSING_TASK_ID,
                Type.OPERATOR_ID,
                Type.REF_DOC_ID,
                Type.REF_DOC_URI,
                Type.PROCESSING_AREA_ID,
                Type.MIN_OPERATOR,
                Type.MAX_OPERATOR,
                Type.MULTIPLY_OPERATOR,
                Type.HAS_BEEN_START
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
        public final static String OPERATOR_ID = "operatorId";
        public final static String REF_DOC_ID = "refDocId";
        public final static String REF_DOC_URI = "refDocUri";
        public final static String PROCESSING_AREA_ID = "stagingAreaId";
        public final static String MIN_OPERATOR = "minOperator";
        public final static String MAX_OPERATOR = "maxOperator";
        public final static String MULTIPLY_OPERATOR = "multiplyOperator";
        public final static String HAS_BEEN_START = "hasBeenStart";
    }

    public class Type {
        public final static String PROCESSING_TASK_ID = SqlLiteDataType.TEXT;
        public final static String OPERATOR_ID = SqlLiteDataType.TEXT;
        public final static String REF_DOC_ID = SqlLiteDataType.TEXT;
        public final static String REF_DOC_URI = SqlLiteDataType.TEXT;
        public final static String PROCESSING_AREA_ID = SqlLiteDataType.TEXT;
        public final static String MIN_OPERATOR = SqlLiteDataType.INTEGER;
        public final static String MAX_OPERATOR = SqlLiteDataType.INTEGER;
        public final static String MULTIPLY_OPERATOR = SqlLiteDataType.INTEGER;
        public final static String HAS_BEEN_START = SqlLiteDataType.INTEGER;
    }

    public static class Query {
        private final static String SELECT_ALL = "SELECT ALL * FROM " + TABLE_NAME;
        private final static String DELETE_LIST = "DELETE FROM " + TABLE_NAME +
                " WHERE " +
                "  " + Column.PROCESSING_TASK_ID + "=''{0}'' " ;

        public static String getDeleteList(String taskId) {
            return MessageFormat.format(DELETE_LIST, taskId);
        }

        public static String getSelectAll() {
            return SELECT_ALL;
        }
    }
}
