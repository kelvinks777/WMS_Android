package com.gin.wms.warehouse.administrator;

import android.content.Context;
import android.util.AttributeSet;
import android.widget.Button;
import android.widget.ProgressBar;
import android.widget.TextView;

import com.gin.ngemart.baseui.component.NgemartCardView;
import com.gin.wms.manager.db.data.EquipmentData;
import com.gin.wms.manager.db.data.OperatorEquipmentData;
import com.gin.wms.warehouse.R;

public class EquipmentInfoCard extends NgemartCardView<OperatorEquipmentData> {
    private TextView tvEquipmentIdInfo, tvEquipmentTypeInfo;
    private ProgressBar progressbar;

    public EquipmentInfoCard(Context context) {
        super(context);
    }

    public EquipmentInfoCard(Context context, AttributeSet attrs) {
        super(context, attrs);
    }

    public EquipmentInfoCard(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
    }

    @Override
    public void setData(OperatorEquipmentData data) {
        super.setData(data);
        try{
            initComponent();
            setDataToUI(data);
            onClickListener();
        }catch (Exception e){

        }
    }

    private void setDataToUI(OperatorEquipmentData data) {
        tvEquipmentIdInfo.setText(data.equipmentId);
        tvEquipmentTypeInfo.setText(data.equipmentTypeId);
    }

    private void initComponent(){
        tvEquipmentIdInfo = findViewById(R.id.tvEquipmentIdInfo);
        tvEquipmentTypeInfo = findViewById(R.id.tvEquipmentTypeInfo);
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
    }

}
