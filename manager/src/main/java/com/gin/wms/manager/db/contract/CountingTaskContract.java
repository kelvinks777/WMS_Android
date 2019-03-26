package com.gin.wms.manager.db.contract;

import com.bosnet.ngemart.libgen.Contract;
import com.bosnet.ngemart.libgen.SqlLiteDataType;

import java.text.MessageFormat;

public class CountingTaskContract extends Contract {
    public static final String TABLE_NAME = "counting_task";

    public CountingTaskContract(){
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
                Column.OPERATOR_ID,
                Column.CLIENT_ID,
                Column.CLIENT_LOCATION_ID,
                Column.START_TIME,
                Column.END_TIME,
                Column.STATUS,
                Column.REF_DOC_URI,
                Column.REF_DOC_ID,
                Column.HAS_BEEN_START,
                Column.UPDATED,
        };
    }

    @Override
    public String[] getListType() {
        return new String[]{
                Type.COUNTING_ID,
                Type.OPERATOR_ID,
                Type.CLIENT_ID,
                Type.CLIENT_LOCATION_ID,
                Type.START_TIME,
                Type.END_TIME,
                Type.STATUS,
                Type.REF_DOC_URI,
                Type.REF_DOC_ID,
                Type.HAS_BEEN_START,
                Type.UPDATED,
        };
    }

    @Override
    public String[] getPrimaryKey() {
        return new String[]{
                Column.COUNTING_ID
        };
    }

    public class Column{
        public final static String COUNTING_ID = "countingId";
        public final static String OPERATOR_ID = "operatorId";
        public final static String CLIENT_ID = "ClientId";
        public final static String CLIENT_LOCATION_ID = "ClientLocationId";
        public final static String START_TIME = "StartTime";
        public final static String END_TIME = "EndTime";
        public final static String STATUS = "StatusAsInt";
        public final static String REF_DOC_URI = "refDocUri";
        public final static String REF_DOC_ID = "refDocId";
        public final static String UPDATED = "updated";
        public final static String HAS_BEEN_START = "hasBeenStart";

    }

    public class Type{
        public final static String COUNTING_ID = SqlLiteDataType.TEXT;
        public final static String OPERATOR_ID = SqlLiteDataType.TEXT;
        public final static String CLIENT_ID = SqlLiteDataType.TEXT;
        public final static String CLIENT_LOCATION_ID = SqlLiteDataType.TEXT;
        public final static String START_TIME = SqlLiteDataType.DATE;
        public final static String END_TIME = SqlLiteDataType.DATE;
        public final static String STATUS = SqlLiteDataType.INTEGER;
        public final static String REF_DOC_URI = SqlLiteDataType.TEXT;
        public final static String REF_DOC_ID = SqlLiteDataType.TEXT;
        public final static String UPDATED = SqlLiteDataType.DATE;
        public final static String HAS_BEEN_START = SqlLiteDataType.INTEGER;
    }

    public static class Query {
        private final static String SELECT = "SELECT * FROM ";
        private final static String DELETE_LIST = "DELETE FROM " + TABLE_NAME +
                " WHERE " +
                "  " + Column.COUNTING_ID + "=''{0}'' " ;

        private final static String EXPRESSION = TABLE_NAME
                + " WHERE "
                + Column.COUNTING_ID + " = ''{0}'' ";

        public static String getDeleteList(String taskId) {
            return MessageFormat.format(DELETE_LIST, taskId);
        }

        public static String getSelectAll(String taskId) {
            return SELECT + MessageFormat.format(EXPRESSION, taskId);
        }
    }
}