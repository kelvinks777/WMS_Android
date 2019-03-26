package com.gin.wms.warehouse.operator.Banded;

import android.content.Context;
import android.util.AttributeSet;
import android.widget.TextView;

import com.gin.ngemart.baseui.component.NgemartCardView;
import com.gin.wms.manager.db.data.MasterBandedData;
import com.gin.wms.warehouse.R;

public class BandedInfoCard <T extends MasterBandedData>  extends NgemartCardView<T> {
    private TextView tvProductId, tvQty;

    public BandedInfoCard(Context context) {
        super(context);
    }

    public BandedInfoCard(Context context, AttributeSet attrs) {
        super(context, attrs);
    }

    public BandedInfoCard(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
    }

    @Override
    public void setData(T data) {
        try {
            super.setData(data);
            initComponent();
            setDataToUi(data);
        } catch (Exception e) {
            ShowError(e);
        }
    }

    private void initComponent() {
        tvProductId = findViewById(R.id.tvProductIdBanded);
        tvQty = findViewById(R.id.tvQtyComp);
    }

    private void setDataToUi(MasterBandedData data) {
        clearUI();
        tvProductId.setText(data.productBandedId);
        tvQty.setText(String.valueOf(data.productComp));
    }

    private void clearUI() {
        tvProductId.setText("");
        tvQty.setText("");
    }
}
