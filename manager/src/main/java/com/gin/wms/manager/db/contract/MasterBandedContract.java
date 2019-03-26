package com.gin.wms.manager.db.contract;

import com.bosnet.ngemart.libgen.Contract;
import com.bosnet.ngemart.libgen.SqlLiteDataType;

public class MasterBandedContract extends Contract{
    public static final String TABLE_NAME = "masterBanded";

    public MasterBandedContract(){
        super();
    }

    @Override
    public String getTableName() {
        return TABLE_NAME;
    }

    @Override
    public String[] getListColumn() {
        return new String[]{
                Column.BANDED_ID,
                Column.PRODUCT_ID,
                Column.PRODUCT_COMP
        };
    }

    @Override
    public String[] getListType() {
        return new String[]{
                Type.BANDED_ID,
                Type.PRODUCT_ID,
                Type.PRODUCT_COMP
        };
    }

    @Override
    public String[] getPrimaryKey() {
        return new String[]{
                Column.BANDED_ID
        };
    }

    public class Column{
        public final static String BANDED_ID = "bandedId";
        public final static String PRODUCT_ID = "productId";
        public final static String PRODUCT_COMP = "productComp";
    }

    public class Type{
        public final static String BANDED_ID = SqlLiteDataType.TEXT;
        public final static String PRODUCT_ID = SqlLiteDataType.TEXT;
        public final static String PRODUCT_COMP = SqlLiteDataType.INTEGER;
    }

}
