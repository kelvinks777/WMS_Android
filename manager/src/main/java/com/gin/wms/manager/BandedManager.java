package com.gin.wms.manager;

import android.content.Context;

import com.bosnet.ngemart.libgen.Manager;
import com.gin.wms.manager.config.ManagerSetup;
import com.gin.wms.manager.config.NgRouteUrl;
import com.gin.wms.manager.db.data.BandedData;

public class BandedManager extends Manager {

    public BandedManager(Context context) throws Exception {
        super(context, new NgRouteUrl().getUrlDomainTask() + "bandedOrder", new UserContext(context), new ManagerSetup());
    }

    public BandedData getBandedOrderData(String refDocId) throws Exception{
        String function = "GetBandedOrderData?Id=" + refDocId;
        return restClient.get(BandedData.class, function);
    }
}
