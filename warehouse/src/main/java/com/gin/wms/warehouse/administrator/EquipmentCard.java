package com.gin.wms.warehouse.administrator;

import android.content.Context;
import android.util.AttributeSet;
import android.view.View;
import android.widget.Button;
import android.widget.ProgressBar;
import android.widget.TextView;

import com.gin.ngemart.baseui.component.NgemartCardView;
import com.gin.wms.manager.db.data.OperatorStatusData;
import com.gin.wms.warehouse.R;

public class EquipmentCard extends NgemartCardView<OperatorStatusData> {
    private TextView tvOperatorId, tvOperatorName;
    private Button btnUpdateEquipment,btnInfo;
    private ProgressBar progressbar;

    public EquipmentCard(Context context) {
        super(context);
    }

    public EquipmentCard(Context context, AttributeSet attrs) {
        super(context, attrs);
    }

    public EquipmentCard(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
    }

    @Override
    public void setData(OperatorStatusData data) {
        super.setData(data);
        try{
            initComponent();
            setDataToUI(data);
            onClickListener();
        }catch (Exception e){
            ShowError(e);
        }
    }

    private void setDataToUI(OperatorStatusData data) {
        tvOperatorId.setText(data.operatorId);
        tvOperatorName.setText(data.operatorName);
    }

    private void initComponent(){
        tvOperatorId = findViewById(R.id.tvOperatorId);
        tvOperatorName = findViewById(R.id.tvOperatorName);
        btnUpdateEquipment = findViewById(R.id.btnUpdateEquipment);
        btnInfo =findViewById(R.id.btnInfo);
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

        btnUpdateEquipment.setOnClickListener(v -> cardListener.onCardClick(getVerticalScrollbarPosition(), v, data));

        btnInfo.setOnClickListener(v -> cardListener.onCardClick(getVerticalScrollbarPosition(), v, data));
    }
}
