package com.gin.wms.manager.db.data.base;

import com.bosnet.ngemart.libgen.Data;

public abstract class CountingTaskItemBaseData extends Data {
    public String BinId = "";
    public String PalletNo = "";
    public String ProductId = "";
    public double Qty = 0;
    public boolean hasChecked = false;
}
