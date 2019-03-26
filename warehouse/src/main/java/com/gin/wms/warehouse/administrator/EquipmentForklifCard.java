package com.gin.wms.warehouse.administrator;

import android.content.Context;
import android.util.AttributeSet;
import android.widget.Button;
import android.widget.ProgressBar;
import android.widget.TextView;

import com.gin.ngemart.baseui.component.NgemartCardView;
import com.gin.wms.manager.db.data.EquipmentData;
import com.gin.wms.warehouse.R;

public class EquipmentForklifCard  extends NgemartCardView<EquipmentData> {
    private TextView tvForklif, tvEquipmentIdForklif, tvEquipmentTypeForklif;
    private Button btnAddForklif;
    private ProgressBar progressbar;

    public EquipmentForklifCard(Context context) {
        super(context);
    }

    public EquipmentForklifCard(Context context, AttributeSet attrs) {
        super(context, attrs);
    }

    public EquipmentForklifCard(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
    }

    @Override
    public void setData(EquipmentData data) {
        super.setData(data);
        try{
            initComponent();
            setDataToUI(data);
            onClickListener();
        }catch (Exception e){
            ShowError(e);
        }
    }

    private void setDataToUI(EquipmentData data) {
        tvForklif.setText(data.name);
        tvEquipmentTypeForklif.setText(data.equipmentTypeId);
        tvEquipmentIdForklif.setText(data.equipmentId);
    }

    private void initComponent(){
        tvForklif = findViewById(R.id.tvForklif);
        tvEquipmentIdForklif = findViewById(R.id.tvEquipmentIdForklif);
        tvEquipmentTypeForklif = findViewById(R.id.tvEquipmentTypeForklif);
        btnAddForklif =findViewById(R.id.btnAddForklif);
        progressbar = findViewById(R.id.progressbar);
        setProgressBarVisibility(false);
    }

    private void setProgressBarVisibility(boolean showNow){
        if(showNow) {
            if(progressbar.getVisibility() == GONE)
                progressbar.setVisibility(VISIBLE);
        }else {
            if (progressbar.getVisibility() == VISIBLE)
                progressbar.setVisibility(GONE);
        }
    }

    private void onClickListener() {
        setOnClickListener(v -> cardListener.onCardClick(getVerticalScrollbarPosition(), v, data));

        btnAddForklif.setOnClickListener(v -> cardListener.onCardClick(getVerticalScrollbarPosition(), v, data));
    }
}
