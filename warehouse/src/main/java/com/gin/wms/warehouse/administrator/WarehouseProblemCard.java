package com.gin.wms.warehouse.administrator;

import android.content.Context;
import android.util.AttributeSet;
import android.widget.Button;
import android.widget.ProgressBar;
import android.widget.TextView;

import com.gin.ngemart.baseui.component.NgemartCardView;
import com.gin.wms.manager.db.data.CountingTaskResultData;
import com.gin.wms.manager.db.data.WarehouseProblemData;
import com.gin.wms.warehouse.R;

public class WarehouseProblemCard extends NgemartCardView<WarehouseProblemData> {
    private TextView tvOperatorId, tvOperatorName, tvSourceBin, tvPalletNo, tvProductId;
    private Button btnEditDataWarehouseProblem;
    private ProgressBar progressbar;

    public WarehouseProblemCard(Context context) {
        super(context);
    }

    public WarehouseProblemCard(Context context, AttributeSet attrs) {
        super(context, attrs);
    }

    public WarehouseProblemCard(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
    }

    @Override
    public void setData(WarehouseProblemData data) {
        super.setData(data);
        try{
            initComponent();
            setDataToUi(data);
            onClickListener();
        }catch(Exception e){
            ShowError(e);
        }
    }

    private void initComponent(){
        tvOperatorId = findViewById(R.id.tvOperatorId);
        tvOperatorName = findViewById(R.id.tvOperatorName);
        tvSourceBin = findViewById(R.id.tvSourceBin);
        tvPalletNo = findViewById(R.id.tvPalletNo);
        tvProductId =findViewById(R.id.tvProductId);
        progressbar = findViewById(R.id.progressbar);
        btnEditDataWarehouseProblem =findViewById(R.id.btnEditDataWarehouseProblem);
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

    private void clearUi(){
        tvOperatorId.setText("");
        tvOperatorName.setText("");
        tvSourceBin.setText("");
        tvPalletNo.setText("");
        tvProductId.setText("");
    }

    private void setDataToUi(WarehouseProblemData warehouseProblemData){
        clearUi();
        tvOperatorId.setText(warehouseProblemData.operatorId);
        tvOperatorName.setText(warehouseProblemData.operatorName);
        tvSourceBin.setText(warehouseProblemData.binId);
        tvPalletNo.setText(warehouseProblemData.palletNo);
        tvProductId.setText(warehouseProblemData.productId);
    }

    private void onClickListener() {
        btnEditDataWarehouseProblem.setOnClickListener(v->cardListener.onCardClick(getVerticalScrollbarPosition(), v, data));
    }
}
