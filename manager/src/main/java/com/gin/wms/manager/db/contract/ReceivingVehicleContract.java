package com.gin.wms.manager.db.contract;

import com.bosnet.ngemart.libgen.Contract;
import com.bosnet.ngemart.libgen.SqlLiteDataType;

public class ReceivingVehicleContract extends Contract {

    public static final String TABLE_NAME = "receiving_vehicle";

    public ReceivingVehicleContract() {
        super();
    }

    @Override
    public String getTableName() {
       return TABLE_NAME;
    }

    @Override
    public String[] getListColumn() {
        return new String[]{
                Column.RECEIVINGVEHICLEINO,
                Column.POLICENO,
                Column.RECEIVINGDATE,
                Column.DRIVERNAME,
                Column.STATUS,
                Column.UPDATED
        };
    }

    @Override
    public String[] getListType() {
        return new String[]{
                Type.RECEIVINGVEHICLEINO,
                Type.POLICENO,
                Type.RECEIVINGDATE,
                Type.DRIVERNAME,
                Type.STATUS,
                Type.UPDATED
        };
    }

    @Override
    public String[] getPrimaryKey() {
        return new String[]{
                Column.RECEIVINGVEHICLEINO
        };
    }

    public class Column {

        public final static String RECEIVINGVEHICLEINO = "receivingVehicleNo";
        public final static String POLICENO = "policeNo";
        public final static String RECEIVINGDATE = "receivingDate";
        public final static String DRIVERNAME = "driverName";
        public final static String STATUS = "status";
        public final static String UPDATED = "updated";
    }

    public class Type {

        public final static String RECEIVINGVEHICLEINO =  SqlLiteDataType.TEXT;
        public final static String POLICENO = SqlLiteDataType.TEXT;
        public final static String RECEIVINGDATE = SqlLiteDataType.DATE;
        public final static String DRIVERNAME = SqlLiteDataType.TEXT;
        public final static String STATUS = SqlLiteDataType.INTEGER;
        public final static String UPDATED = SqlLiteDataType.DATE;
    }
}
