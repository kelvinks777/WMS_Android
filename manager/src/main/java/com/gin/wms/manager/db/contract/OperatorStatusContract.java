package com.gin.wms.manager.db.contract;

import com.bosnet.ngemart.libgen.Contract;
import com.bosnet.ngemart.libgen.SqlLiteDataType;

public class OperatorStatusContract extends Contract {
    private static final String TABLE_NAME = "operatorStatus";

    public OperatorStatusContract(){
        super();
    }

    @Override
    public String getTableName() {
        return TABLE_NAME;
    }

    @Override
    public String[] getListColumn() {
        return new String[]{
                Column.OPERATOR_ID,
                Column.OPERATOR_NAME,
                Column.TYPE,
                Column.STATUS,
                Column.ATTENDANCE,
                Column.NUMBER_OF_TASK,
                Column.DATE_RECORD,
                Column.UPDATED
        };
    }

    @Override
    public String[] getListType() {
        return new String[]{
                Type.OPERATOR_ID,
                Type.OPERATOR_NAME,
                Type.TYPE,
                Type.STATUS,
                Type.ATTENDANCE,
                Type.NUMBER_OF_TASK,
                Type.DATE_RECORD,
                Type.UPDATED
        };
    }

    @Override
    public String[] getPrimaryKey() {
        return new String[]{
                Column.OPERATOR_ID
        };
    }

    public class Column {
        public final static String OPERATOR_ID = "operatorId";
        public final static String OPERATOR_NAME = "operatorName";
        public final static String TYPE = "Type";
        public final static String STATUS = "Status";
        public final static String ATTENDANCE = "Attendance";
        public final static String NUMBER_OF_TASK = "numberOfTask";
        public final static String DATE_RECORD = "dateRecord";
        public final static String UPDATED = "updated";
    }

    public class Type{
        public final static String OPERATOR_ID = SqlLiteDataType.TEXT;
        public final static String OPERATOR_NAME = SqlLiteDataType.TEXT;
        public final static String TYPE = SqlLiteDataType.INTEGER;
        public final static String STATUS = SqlLiteDataType.INTEGER;
        public final static String ATTENDANCE = SqlLiteDataType.INTEGER;
        public final static String NUMBER_OF_TASK = SqlLiteDataType.INTEGER;
        public final static String DATE_RECORD = SqlLiteDataType.DATE ;
        public final static String UPDATED = SqlLiteDataType.DATE;
    }

}
