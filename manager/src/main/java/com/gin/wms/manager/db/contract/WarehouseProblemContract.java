package com.gin.wms.manager.db.contract;

import com.bosnet.ngemart.libgen.Contract;
import com.bosnet.ngemart.libgen.SqlLiteDataType;

/**
 * Created by Fernandes on 10/18/2018.
 */

public class WarehouseProblemContract extends Contract {
    private static final String TABLE_NAME = "warehouseProblem";

    public WarehouseProblemContract() {
        super();
    }

    @Override
    public String getTableName() {
        return TABLE_NAME;
    }

    @Override
    public String[] getListColumn() {
        return new String[]{
                Column.BIN_ID,
                Column.PALLET_NO,
                Column.PRODUCT_ID,
                Column.OPERATOR_ID,
                Column.OPERATOR_NAME,
                Column.TYPE,
                Column.STATUS,
                Column.ACTION,
                Column.INPUT_TIME,
                Column.UPDATED
        };
    }

    @Override
    public String[] getListType() {
        return new String[]{
                Type.BIN_ID,
                Type.PALLET_NO,
                Type.PRODUCT_ID,
                Type.OPERATOR_ID,
                Type.OPERATOR_NAME,
                Type.TYPE,
                Type.STATUS,
                Type.ACTION,
                Type.INPUT_TIME,
                Type.UPDATED
        };
    }

    @Override
    public String[] getPrimaryKey() {
        return new String[]{
                Column.BIN_ID
        };
    }

    public class Column {
        public final static String BIN_ID = "binId";
        public final static String PALLET_NO = "palletNo";
        public final static String PRODUCT_ID = "productId";
        public final static String OPERATOR_ID = "operatorId";
        public final static String OPERATOR_NAME = "operatorName";
        public final static String TYPE = "Type";
        public final static String STATUS = "Status";
        public final static String ACTION = "Action";
        public final static String INPUT_TIME = "inputTime";
        public final static String UPDATED = "updated";
    }

    public class Type{
        public final static String BIN_ID = SqlLiteDataType.TEXT;
        public final static String PALLET_NO = SqlLiteDataType.TEXT;
        public final static String PRODUCT_ID = SqlLiteDataType.TEXT;
        public final static String OPERATOR_ID = SqlLiteDataType.TEXT;
        public final static String OPERATOR_NAME = SqlLiteDataType.TEXT;
        public final static String TYPE = SqlLiteDataType.INTEGER;
        public final static String STATUS = SqlLiteDataType.INTEGER;
        public final static String ACTION = SqlLiteDataType.INTEGER;
        public final static String INPUT_TIME = SqlLiteDataType.DATE ;
        public final static String UPDATED = SqlLiteDataType.DATE;
    }

    public class Query {
        public final static String SELECT = "select * from "
                + TABLE_NAME
                + " order by "
                + Column.BIN_ID
                + " desc limit 1";
    }
}