package com.gin.wms.warehouse.operator.Counting;

import android.content.Context;
import android.util.AttributeSet;
import android.widget.Button;
import android.widget.ProgressBar;
import android.widget.TextView;

import com.gin.ngemart.baseui.component.NgemartCardView;
import com.gin.wms.manager.StockCountingOrderManager;
import com.gin.wms.manager.db.data.StockCountingOrderItem;
import com.gin.wms.manager.db.data.StockCountingOrderItemByBinIdData;
import com.gin.wms.warehouse.R;

public class StockCountingOrderItemCard  extends NgemartCardView<StockCountingOrderItem> {
    private TextView tvBinId, tvOperatorId;
    private Button btnRemoveItemStockCountingOrder,btnEditItemStockCountingOrder;
    private StockCountingOrderManager stockCountingOrderManager;
    private ProgressBar progressbar;


    public StockCountingOrderItemCard(Context context) {
        super(context);
    }

    public StockCountingOrderItemCard(Context context, AttributeSet attrs) {
        super(context, attrs);
    }

    public StockCountingOrderItemCard(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
    }

    @Override
    public void setData(StockCountingOrderItem data) {
        super.setData(data);
        try{
            initComponent();
            setDataToUi(data);
            onClickListener();
        }catch (Exception e){
            ShowError(e);
        }
    }

    private void initObject()throws Exception{
        stockCountingOrderManager = new StockCountingOrderManager(getContext());
    }

    private void initComponent(){
        tvBinId = findViewById(R.id.tvBinId);
        tvOperatorId = findViewById(R.id.tvOperatorId);
        btnRemoveItemStockCountingOrder = findViewById(R.id.btnRemoveItemStockCountingOrder);
        btnEditItemStockCountingOrder = findViewById(R.id.btnEditItemStockCountingOrder);
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

    private void clearUi(){
        tvBinId.setText("");
        tvOperatorId.setText("");
    }

    private void setDataToUi(StockCountingOrderItem stockCountingOrderItemData){
        clearUi();
        tvBinId.setText(stockCountingOrderItemData.binId);
        tvOperatorId.setText(stockCountingOrderItemData.operatorId);
    }

    private void onClickListener() {
        btnRemoveItemStockCountingOrder.setOnClickListener(v->cardListener.onCardClick(getVerticalScrollbarPosition(), v, data));
        btnEditItemStockCountingOrder.setOnClickListener(v->cardListener.onCardClick(getVerticalScrollbarPosition(), v, data));
    }

}
