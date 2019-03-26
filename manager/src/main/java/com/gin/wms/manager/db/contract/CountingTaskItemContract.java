package com.gin.wms.manager.db.contract;

import com.bosnet.ngemart.libgen.SqlLiteDataType;
import com.gin.wms.manager.db.contract.base.ProductBaseContract;

public class CountingTaskItemContract extends ProductBaseContract {
    public static final String TABLE_NAME = "countingTaskItem";

    public CountingTaskItemContract() throws Exception{
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
                Type.COUNTING_ID,
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
                Column.COUNTING_ID
        };
    }

    public class Column {
        public final static String COUNTING_ID = "countingId";
        public final static String QTY_COMP_UOM_VALUE = "qtyCompUomValue";
        public final static String PALLET_QTY = "palletQty";
        public final static String QTY_CHECK_RESULT_COMP_UOM_VALUE = "qtyCheckResultCompUomValue";
        public final static String QTY = "qty";
        public final static String QTY_CHECK_RESULT = "qtyCheckResult";
    }

    public class Type {
        public final static String COUNTING_ID = SqlLiteDataType.TEXT;
        public final static String QTY = SqlLiteDataType.DOUBLE;
        public final static String PALLET_QTY = SqlLiteDataType.DOUBLE;
        public final static String QTY_CHECK_RESULT = SqlLiteDataType.DOUBLE;
        public final static String QTY_COMP_UOM_VALUE = SqlLiteDataType.TEXT;
        public final static String QTY_CHECK_RESULT_COMP_UOM_VALUE = SqlLiteDataType.TEXT;
    }
}
