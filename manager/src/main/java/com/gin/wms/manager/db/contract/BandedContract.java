package com.gin.wms.manager.db.contract;

import com.bosnet.ngemart.libgen.Contract;
import com.bosnet.ngemart.libgen.SqlLiteDataType;

public class BandedContract extends Contract {
    public static final String TABLE_NAME = "bandedOrder";

    public BandedContract(){
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
                Column.CLIENT_ID,
                Column.DATE_,
                Column.LONG_INFO,
                Column.PARTY,
                Column.DOC_DATE,
                Column.FINISHED,
                Column.STATUS,
                Column.DOC_TYPE,
                Column.SOURCE_ID,
                Column.DOC_REF,
                Column.DOC_REF_URI,
                Column.SOURCE_TYPE_ID,
                Column.OPERATOR_NUMBER
        };
    }

    @Override
    public String[] getListType() {
        return new String[]{
                Type.ID,
                Type.CLIENT_ID,
                Type.DATE_,
                Type.LONG_INFO,
                Type.PARTY,
                Type.DOC_DATE,
                Type.FINISHED,
                Type.STATUS,
                Type.DOC_TYPE,
                Type.SOURCE_ID,
                Type.DOC_REF,
                Type.DOC_REF_URI,
                Type.SOURCE_TYPE_ID,
                Type.OPERATOR_NUMBER
        };
    }

    @Override
    public String[] getPrimaryKey() {
        return new String[]{
                Column.ID
        };
    }

    public class Column{
        public final static String ID = "id";
        public final static String CLIENT_ID = "clientId";
        public final static String DATE_ = "date";
        public final static String LONG_INFO = "longInfo";
        public final static String PARTY = "party";
        public final static String DOC_DATE = "docDate";
        public final static String FINISHED = "finished";
        public final static String STATUS = "status";
        public final static String DOC_TYPE = "docType";
        public final static String SOURCE_ID ="sourceId";
        public final static String DOC_REF = "docRef";
        public final static String DOC_REF_URI = "docRefUri";
        public final static String SOURCE_TYPE_ID = "sourceTypeId";
        public final static String OPERATOR_NUMBER = "operatorNumber";
    }

    public class Type{
        public final static String ID = SqlLiteDataType.TEXT;
        public final static String CLIENT_ID = SqlLiteDataType.TEXT;
        public final static String DATE_ = SqlLiteDataType.DATE;
        public final static String LONG_INFO = SqlLiteDataType.TEXT;
        public final static String PARTY = SqlLiteDataType.INTEGER;
        public final static String DOC_DATE = SqlLiteDataType.DATE;
        public final static String FINISHED = SqlLiteDataType.INTEGER;
        public final static String STATUS = SqlLiteDataType.INTEGER;
        public final static String DOC_TYPE = SqlLiteDataType.INTEGER;
        public final static String SOURCE_ID = SqlLiteDataType.TEXT;
        public final static String DOC_REF = SqlLiteDataType.TEXT;
        public final static String DOC_REF_URI = SqlLiteDataType.TEXT;
        public final static String SOURCE_TYPE_ID = SqlLiteDataType.INTEGER;
        public final static String OPERATOR_NUMBER = SqlLiteDataType.TEXT;
    }
}
