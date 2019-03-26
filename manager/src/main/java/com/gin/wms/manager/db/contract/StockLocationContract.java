package com.gin.wms.manager.db.contract;

import com.bosnet.ngemart.libgen.Contract;
import com.bosnet.ngemart.libgen.SqlLiteDataType;

public class StockLocationContract extends Contract {

    private static final String TABLE_NAME = "stockLocation";

    public StockLocationContract() {
        super();
    }

    @Override
    public String getTableName() {
        return TABLE_NAME;
    }

    @Override
    public String[] getListColumn() {
        return new String[]{
                Column.PRODUCT_ID,
                Column.BIN,
                Column.PALLET_NO,
                Column.CLIENT_ID,
                Column.CLIENT_LOCATION_ID,
                Column.QTY,
                Column.BOOK_IN_QTY,
                Column.BOOK_OUT_QTY,
                Column.EXPIRED_DATE,
                Column.STOCK_STATUS,
                Column.UPDATED
        };
    }

    @Override
    public String[] getListType() {
        return new String[]{
                Type.PRODUCT_ID,
                Type.BIN,
                Type.PALLET_NO,
                Type.CLIENT_ID,
                Type.CLIENT_LOCATION_ID,
                Type.QTY,
                Type.BOOK_IN_QTY,
                Type.BOOK_OUT_QTY,
                Type.EXPIRED_DATE,
                Type.STOCK_STATUS,
                Type.UPDATED
        };
    }

    @Override
    public String[] getPrimaryKey() {
        return new String[]{
                Column.PRODUCT_ID,
                Column.BIN,
                Column.PALLET_NO
        };
    }

    public class Column {
        public final static String PRODUCT_ID = "productId";
        public final static String BIN = "bin";
        public final static String PALLET_NO = "palletNo";
        public final static String CLIENT_ID = "clientId";
        public final static String CLIENT_LOCATION_ID = "clientLocationId";
        public final static String QTY = "qty";
        public final static String BOOK_IN_QTY = "bookInQty";
        public final static String BOOK_OUT_QTY = "bookOutQty";
        public final static String EXPIRED_DATE = "expiredDate";
        public final static String STOCK_STATUS="stockStatus";
        public final static String UPDATED = "updated";
    }

    public class Type{
        public final static String PRODUCT_ID = SqlLiteDataType.TEXT;
        public final static String BIN = SqlLiteDataType.TEXT;
        public final static String PALLET_NO = SqlLiteDataType.TEXT;
        public final static String CLIENT_ID = SqlLiteDataType.TEXT;
        public final static String CLIENT_LOCATION_ID = SqlLiteDataType.TEXT;
        public final static String QTY = SqlLiteDataType.DOUBLE;
        public final static String BOOK_IN_QTY = SqlLiteDataType.DOUBLE;
        public final static String BOOK_OUT_QTY = SqlLiteDataType.DOUBLE;
        public final static String EXPIRED_DATE = SqlLiteDataType.DATE ;
        public final static String STOCK_STATUS = SqlLiteDataType.INTEGER;
        public final static String UPDATED = SqlLiteDataType.DATE;
    }
}
