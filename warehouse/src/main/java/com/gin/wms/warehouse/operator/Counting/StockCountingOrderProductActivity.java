package com.gin.wms.warehouse.operator.Counting;

import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.widget.Toolbar;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;

import com.gin.wms.manager.StockCountingOrderManager;
import com.gin.wms.manager.db.data.StockCountingOrderItem;
import com.gin.wms.manager.db.data.StockCountingOrderItemByProductIdData;
import com.gin.wms.warehouse.R;
import com.gin.wms.warehouse.base.WarehouseActivity;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

public class StockCountingOrderProductActivity extends WarehouseActivity implements View.OnClickListener {
    private EditText edtProductId,edtOperatorId;
    private StockCountingOrderManager stockCountingOrderManager;
    private List<StockCountingOrderItemByProductIdData> listStockCountingItemByProductIdData = new ArrayList<>();
    private List<StockCountingOrderItem> listStockCountingItem = new ArrayList<>();
    private List<StockCountingOrderItemByProductIdData> ListProductData  = new ArrayList<>();

    private Button btnAddStockcountingOrderItemByProduct,btnListStockcountingOrderItemByProduct;
    private Toolbar toolbar;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        try{
            super.onCreate(savedInstanceState);
            setContentView(R.layout.activity_counting_order_product);
            initManager();
            initComponent();
        }catch (Exception e){
            showErrorDialog(e);
        }

    }

    private void initManager() throws Exception{
        stockCountingOrderManager = new StockCountingOrderManager(this);
    }

    private void initComponent(){
        initToolbar();
        justBindingProperties();
        initPropertyHandler();
    }

    private void initToolbar() {
        toolbar = findViewById(R.id.toolbarStockcountingOrderItemManualProduct);
        toolbar.setTitle(getResources().getString(R.string.toolbar_stock_counting_order_by_product));
        setSupportActionBar(toolbar);
        if(getSupportActionBar() != null)
            getSupportActionBar().setDisplayHomeAsUpEnabled(true);
    }

    private void justBindingProperties(){
        edtProductId=findViewById(R.id.edtProductId);
        edtOperatorId=findViewById(R.id.edtOperatorId);
        btnAddStockcountingOrderItemByProduct=findViewById(R.id.btnAddStockcountingOrderItemByProduct);
        btnListStockcountingOrderItemByProduct=findViewById(R.id.btnListStockcountingOrderItemByProduct);
    }

    private boolean validateStockcountingOrderItemByProduct(){
        String warning=getResources().getString(R.string.msg_warning_field_must_be_filled);
        boolean isValid= true;
        if(edtProductId.getText().toString().equals("")){
            edtProductId.setError(warning);
            isValid=false;
        }
        if(edtOperatorId.getText().toString().equals("")){
            edtOperatorId.setError(warning);
            isValid=false;
        }
        return isValid;
    }

    private void initPropertyHandler(){
        btnAddStockcountingOrderItemByProduct.setOnClickListener(this);
        btnListStockcountingOrderItemByProduct.setOnClickListener(this);
    }

    private void clearUI(){
        edtProductId.setText("");
        edtOperatorId.setText("");
        edtProductId.requestFocus();
    }

    @Override
    public void onBackPressed() {
        showInfoDialog(getResources().getString(R.string.common_information_title),getResources().getString(R.string.warning_must_complete_the_task));
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()){
            case R.id.btnAddStockcountingOrderItemByProduct:
                try{
                    if(validateStockcountingOrderItemByProduct()) {
                        addListOrderItemByPorduct();

                    }
                }catch (Exception e){
                    showErrorDialog(e);
                }
                break;
            case R.id.btnListStockcountingOrderItemByProduct :
                try {
                    showToListStockCountingOrder();
                }catch (Exception e){
                    showErrorDialog(e);
                }
        }
    }

    private void addListOrderItemByPorduct() throws Exception{
        StockCountingOrderItemByProductIdData itemsByProduct =new StockCountingOrderItemByProductIdData();
        StockCountingOrderItemByProductIdData itemsByProduct2 =new StockCountingOrderItemByProductIdData();
        StockCountingOrderItem stockCountingOrderItem =new StockCountingOrderItem();

        stockCountingOrderItem.id="1";
        stockCountingOrderItem.binId="";
        stockCountingOrderItem.productId =edtProductId.getText().toString();
        stockCountingOrderItem.operatorId=edtOperatorId.getText().toString();
        stockCountingOrderManager.SaveStockCountingOrderItem(stockCountingOrderItem);

//        itemsByProduct.id="";
//        itemsByProduct.productId=edtProductId.getText().toString();
//        itemsByProduct.operatorId=edtOperatorId.getText().toString();
//        ListProductData.add(itemsByProduct);

        //for add data
//        itemsByProduct2.id="1";
//        itemsByProduct2.productId=edtProductId.getText().toString();
//        itemsByProduct2.operatorId=edtOperatorId.getText().toString();
//        stockCountingOrderManager.SaveStockCountingOneByProduct(itemsByProduct2);

        showInfoSnackBar(getResources().getString(R.string.msg_counting_order_has_saved));
        clearUI();
    }

    private void showToListStockCountingOrder() throws Exception{
        //listStockCountingItemByProductIdData= stockCountingOrderManager.getListCountingByProductFromLocal("1");
        //saveToLocalListStockCountingItemByBinIdData();
        listStockCountingItem = stockCountingOrderManager.getListStockCountingIdResultFromLocal("1");

        Intent intent=new Intent(this,StockCountingOrderListByProductActivity.class);
        Bundle bundle = new Bundle();
        bundle.putSerializable(StockCountingOrderListByProductActivity.LIST_STOCKCOUNTING_ORDER_DATA_BY_PRODUCT, (Serializable) listStockCountingItem);
        intent.putExtras(bundle);
        startActivity(intent);
    }
}
