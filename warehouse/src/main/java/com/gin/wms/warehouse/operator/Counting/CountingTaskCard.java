package com.gin.wms.warehouse.operator.Counting;

import android.content.Context;
import android.util.AttributeSet;
import android.view.View;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.gin.ngemart.baseui.component.NgemartCardView;
import com.gin.ngemart.baseui.component.NgemartListView;
import com.gin.wms.manager.db.data.CountingTaskResultData;
import com.gin.wms.warehouse.R;

public class CountingTaskCard extends NgemartCardView<CountingTaskResultData> {
    private TextView tvProductId, tvQty, tvPalletNo, tvBinId, tvChecked;
    private Button btnEdit;
    private LinearLayout llQty, llEdit;

    public CountingTaskCard(Context context) {
        super(context);
    }

    public CountingTaskCard(Context context, AttributeSet attrs) {
        super(context, attrs);
    }

    public CountingTaskCard(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
    }

    @Override
    public void setData(CountingTaskResultData data) {
        super.setData(data);
        try {
            initComponent();
            setDataToUI(data);
            onClickListener();
        }
        catch (Exception e){
            ShowError(e);
        }
    }

    private void setDataToUI(CountingTaskResultData data) {
        tvProductId.setText(data.ProductId);
        tvPalletNo.setText(data.PalletNo);
        tvBinId.setText(data.BinId);

        if(!data.hasChecked){
            llQty.setVisibility(View.GONE);
        }
        if(data.Qty > 0 && data.hasChecked){
            llQty.setVisibility(View.VISIBLE);
            llEdit.setVisibility(View.VISIBLE);
            tvQty.setText(String.valueOf(data.Qty));
            tvChecked.setVisibility(View.VISIBLE);
        }
    }


    private void onClickListener() {
        setOnClickListener(v -> cardListener.onCardClick(getVerticalScrollbarPosition(), v, data));

        btnEdit.setOnClickListener(v -> cardListener.onCardClick(getVerticalScrollbarPosition(), v, data));
    }

    private void initComponent() {
        tvProductId = findViewById(R.id.tvProductIdCounting);
        tvQty = findViewById(R.id.tvQtyCounting);
        tvPalletNo = findViewById(R.id.tvPalletNoCounting);
        tvBinId = findViewById(R.id.tvBinIdCounting);
        tvChecked = findViewById(R.id.tvCountingChecked);
        btnEdit = findViewById(R.id.btnEditCounting);
        llQty = findViewById(R.id.llQty);
        llEdit = findViewById(R.id.llEditCounting);
    }
}
