package com.gin.wms.manager;

import android.content.Context;

import com.bosnet.ngemart.libgen.Manager;
import com.gin.wms.manager.config.ManagerSetup;
import com.gin.wms.manager.config.NgRouteUrl;
import com.gin.wms.manager.db.data.MasterBandedData;

import java.util.List;

public class MasterBandedManager extends Manager {

    public MasterBandedManager(Context context) throws Exception {
        super(context, new NgRouteUrl().getUrlDomainSL() + "MasterBanded", new UserContext(context), new ManagerSetup());
    }

    public List<MasterBandedData> getMasterBandedData(String refDocId) throws Exception{
        String function = "GetMasterBanded?refDocId=" + refDocId;
        return restClient.getList(MasterBandedData[].class, function);
    }
}
