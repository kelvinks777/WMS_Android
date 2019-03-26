package com.gin.wms.manager.db.data.base;

/**
 * Created by Fernandes on 10/05/2018.
 */

public abstract class ProcessingTaskBaseData extends ProductBaseData {
    public  String operatorId;
    public String refDocUri;
    public String refDocId;
    public String stagingAreaId;
    public int minOperator;
    public int maxOperator;
    public int multiplyOperator;
    public boolean hasBeenStart;
}
