package com.gin.wms.manager.db.contract;

import com.bosnet.ngemart.libgen.Contract;
import com.bosnet.ngemart.libgen.SqlLiteDataType;

public class BandedItemContract extends Contract {
    public static final String TABLE_NAME = "bandedOrderItem";

    public BandedItemContract(){
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
                Column.PRODUCT_ID,
                Column.QTY
        };
    }

    @Override
    public String[] getListType() {
        return new String[]{
                Type.ID,
                Type.PRODUCT_ID,
                Type.QTY
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
        public final static String PRODUCT_ID = "productId";
        public final static String QTY = "qty";
    }

    public class Type{
        public final static String ID = SqlLiteDataType.TEXT;
        public final static String PRODUCT_ID = SqlLiteDataType.TEXT;
        public final static String QTY = SqlLiteDataType.INTEGER;
    }
}
