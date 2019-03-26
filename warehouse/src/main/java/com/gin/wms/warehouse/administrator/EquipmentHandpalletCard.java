package com.gin.wms.warehouse.administrator;

import android.content.Context;
import android.util.AttributeSet;
import android.widget.Button;
import android.widget.ProgressBar;
import android.widget.TextView;

import com.gin.ngemart.baseui.component.NgemartCardView;
import com.gin.wms.manager.db.data.EquipmentData;
import com.gin.wms.warehouse.R;

public class EquipmentHandpalletCard extends NgemartCardView<EquipmentData> {
    private TextView tvHandpallet, tvEquipmentTypeHandpallet, tvEquipmentIdHandpallet;
    private Button btnAddHandpallet;
    private ProgressBar progressbar;

    public EquipmentHandpalletCard(Context context) {
        super(context);
    }

    public EquipmentHandpalletCard(Context context, AttributeSet attrs) {
        super(context, attrs);
    }

    public EquipmentHandpalletCard(Context context, AttributeSet attrs, int defStyleAttr) {
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
        tvHandpallet.setText(data.name);
        tvEquipmentTypeHandpallet.setText(data.equipmentTypeId);
        tvEquipmentIdHandpallet.setText(data.equipmentId);
    }

    private void initComponent(){
        tvHandpallet = findViewById(R.id.tvHandpallet);
        tvEquipmentTypeHandpallet = findViewById(R.id.tvEquipmentTypeHandpallet);
        tvEquipmentIdHandpallet = findViewById(R.id.tvEquipmentIdHandpallet);
        btnAddHandpallet =findViewById(R.id.btnAddHandpallet);
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

        btnAddHandpallet.setOnClickListener(v -> cardListener.onCardClick(getVerticalScrollbarPosition(), v, data));
    }

}
