package com.gin.wms.warehouse.operator.Counting;

import android.content.Context;
import android.util.AttributeSet;
import android.widget.Button;
import android.widget.ProgressBar;
import android.widget.TextView;

import com.gin.ngemart.baseui.component.NgemartCardView;
import com.gin.wms.manager.StockCountingOrderManager;
import com.gin.wms.manager.db.data.StockCountingOrderItem;
import com.gin.wms.manager.db.data.StockCountingOrderItemByProductIdData;
import com.gin.wms.warehouse.R;

public class StockCountingOrderItemByProductCard extends NgemartCardView<StockCountingOrderItem> {
    private TextView tvProductId, tvOperatorId;
    private Button btnRemoveItemStockCountingOrderByProduct,btnEditItemStockCountingOrderByProduct;
    private StockCountingOrderManager stockCountingOrderManager;
    private ProgressBar progressbar;

    public StockCountingOrderItemByProductCard(Context context) {
        super(context);
    }

    public StockCountingOrderItemByProductCard(Context context, AttributeSet attrs) {
        super(context, attrs);
    }

    public StockCountingOrderItemByProductCard(Context context, AttributeSet attrs, int defStyleAttr) {
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

    private void initComponent(){
        tvProductId = findViewById(R.id.tvProductId);
        tvOperatorId = findViewById(R.id.tvOperatorId);
        btnRemoveItemStockCountingOrderByProduct = findViewById(R.id.btnRemoveItemStockCountingOrderByProduct);
        btnEditItemStockCountingOrderByProduct = findViewById(R.id.btnEditItemStockCountingOrderByProduct);
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
        tvProductId.setText("");
        tvOperatorId.setText("");
    }

    private void setDataToUi(StockCountingOrderItem stockCountingOrderItemData){
        clearUi();
        tvProductId.setText(stockCountingOrderItemData.productId);
        tvOperatorId.setText(stockCountingOrderItemData.operatorId);
    }

    private void onClickListener() {
        btnRemoveItemStockCountingOrderByProduct.setOnClickListener(v->cardListener.onCardClick(getVerticalScrollbarPosition(), v, data));
        btnEditItemStockCountingOrderByProduct.setOnClickListener(v->cardListener.onCardClick(getVerticalScrollbarPosition(), v, data));
    }
}
