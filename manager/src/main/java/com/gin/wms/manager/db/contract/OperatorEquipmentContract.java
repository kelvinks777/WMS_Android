package com.gin.wms.manager.db.contract;

import com.bosnet.ngemart.libgen.Contract;
import com.bosnet.ngemart.libgen.SqlLiteDataType;

import java.text.MessageFormat;

public class OperatorEquipmentContract extends Contract {
    private static final String TABLE_NAME = "operatorEquipment";

    public OperatorEquipmentContract(){
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
                Column.EQUIPMENT_ID,
                Column.EQUIPMENT_TYPE_ID,
                Column.PRIORITY,
                Column.UPDATED
        };
    }

    @Override
    public String[] getListType() {
        return new String[]{
                Type.OPERATOR_ID,
                Type.EQUIPMENT_ID,
                Type.EQUIPMENT_TYPE_ID,
                Type.PRIORITY,
                Type.UPDATED
        };
    }

    @Override
    public String[] getPrimaryKey() {
        return new String[]{
                Column.EQUIPMENT_ID
        };
    }

    public class Column {
        public final static String OPERATOR_ID = "operatorId";
        public final static String EQUIPMENT_ID = "equipmentId";
        public final static String EQUIPMENT_TYPE_ID = "equipmentTypeId";
        public final static String PRIORITY = "priority";
        public final static String UPDATED = "updated";
    }

    public class Type{
        public final static String OPERATOR_ID = SqlLiteDataType.TEXT;
        public final static String EQUIPMENT_ID = SqlLiteDataType.TEXT;
        public final static String EQUIPMENT_TYPE_ID = SqlLiteDataType.TEXT;
        public final static String PRIORITY = SqlLiteDataType.INTEGER;
        public final static String UPDATED = SqlLiteDataType.DATE;
    }

    public static class Query {
        private final static String SELECT = "SELECT * FROM ";

        private final static String DELETE = "DELETE FROM ";

        private final static String LIST_EXPRESSION = TABLE_NAME
                + " WHERE "
                + Column.OPERATOR_ID + " = ''{0}'' ";

//        private final static String LIST_EXPRESSION_PRODUCT_ID = TABLE_NAME
//                + " WHERE "
//                + Column.PRODUCTID+ " =''{0}'' ";
//
//        public static String getDeleteListByBin(String binId) {
//            return DELETE + MessageFormat.format(LIST_EXPRESSION_BIN_ID, binId);
//        }
//
//        public static String getDeleteListByProductId(String productId) {
//            return DELETE + MessageFormat.format(LIST_EXPRESSION_PRODUCT_ID, productId);
//        }

        public static String getSelectList(String operatorId) {
            return SELECT + MessageFormat.format(LIST_EXPRESSION, operatorId);
        }


    }

}
