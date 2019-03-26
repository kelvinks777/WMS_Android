package com.gin.wms.warehouse.operator.Counting;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.widget.Toolbar;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;

import com.gin.wms.manager.StockCountingOrderManager;
import com.gin.wms.manager.db.data.StockCountingOrderData;
import com.gin.wms.manager.db.data.StockCountingOrderItem;
import com.gin.wms.manager.db.data.StockCountingOrderItemByBinIdData;
import com.gin.wms.warehouse.R;
import com.gin.wms.warehouse.base.WarehouseActivity;

import java.io.Console;
import java.io.Serializable;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.List;

public class StockCountingOrderActivity extends WarehouseActivity  implements View.OnClickListener{
    private EditText edtBinId,edtOperatorId;
    private StockCountingOrderManager stockCountingOrderManager;
    private StockCountingOrderData stockCountingOrderData;
    private List<StockCountingOrderItemByBinIdData> listStockCountingItemByBinIdData = new ArrayList<>();
    private List<StockCountingOrderItem> listStockCountingItem = new ArrayList<>();
    private List<StockCountingOrderItemByBinIdData> ListBinData  = new ArrayList<>();

    private Button btnAddStockcountingOrderItemByBin,btnListStockcountingOrderItemByBin;
    private Toolbar toolbar;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        try{
            super.onCreate(savedInstanceState);
            setContentView(R.layout.activity_counting_order);
            //listStockCountingItem.clear();
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
        toolbar = findViewById(R.id.toolbarStockcountingOrderItemManualByBin);
        toolbar.setTitle(getResources().getString(R.string.toolbar_stock_counting_order_by_bin));
        setSupportActionBar(toolbar);
        if(getSupportActionBar() != null)
            getSupportActionBar().setDisplayHomeAsUpEnabled(true);
    }

    private void justBindingProperties(){
        edtBinId=findViewById(R.id.edtBinId);
        edtOperatorId=findViewById(R.id.edtOperatorId);
        btnAddStockcountingOrderItemByBin=findViewById(R.id.btnAddStockcountingOrderItemByBin);
        btnListStockcountingOrderItemByBin=findViewById(R.id.btnListStockcountingOrderItemByBin);
    }

    private boolean validateStockcountingOrderItemByBin(){
        String warning=getResources().getString(R.string.msg_warning_field_must_be_filled);
        boolean isValid= true;
        if(edtBinId.getText().toString().equals("")){
            edtBinId.setError(warning);
            isValid=false;
        }
        if(edtOperatorId.getText().toString().equals("")){
            edtOperatorId.setError(warning);
            isValid=false;
        }
        return isValid;
    }

    private void initPropertyHandler(){
        btnAddStockcountingOrderItemByBin.setOnClickListener(this);
        btnListStockcountingOrderItemByBin.setOnClickListener(this);
    }

    private void clearUI(){
        edtBinId.setText("");
        edtOperatorId.setText("");
        edtBinId.requestFocus();
    }

    private StockCountingOrderData prepareStockCountingOrderData(){
        StockCountingOrderData preparedData= new StockCountingOrderData();
        StockCountingOrderItemByBinIdData item =new StockCountingOrderItemByBinIdData();

        item.id= "";
        item.operatorId=edtOperatorId.getText().toString();
        item.binId=edtBinId.getText().toString();

        preparedData.clientId="BKS.01";
        preparedData.docDate= Calendar.getInstance().getTime();
        preparedData.longInfo="";
        preparedData.Status=1;
        preparedData.docType=9;
        preparedData.sourceId="BKS.01";
        preparedData.docRefId="";
        preparedData.docRefUri="com.gin.wms.task.stockcountingorder";
        preparedData.ItemsByBin.add(item);

        return preparedData;
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()){
            case R.id.btnAddStockcountingOrderItemByBin:
                try{
                    if(validateStockcountingOrderItemByBin()) {
                        addListOrderItemByBin();

                    }
                }catch (Exception e) {
                    showErrorDialog(e);
                }
                break;
            case R.id.btnListStockcountingOrderItemByBin:
                try{
                    showToListStockCountingOrder();
                }catch (Exception e){
                    showErrorDialog(e);
                }
                break;
        }
    }

    private void saveToLocalListStockCountingItemByBinIdData () throws Exception{
       // listStockCountingItemByBinIdData.removeAll(ListBinData);
        listStockCountingItemByBinIdData.addAll(ListBinData);
        stockCountingOrderManager.SaveStockCountingOrderData(listStockCountingItemByBinIdData);
    }

    private void showToListStockCountingOrder() throws Exception{
        //listStockCountingItemByBinIdData= stockCountingOrderManager.getListCountingResultFromLocal("1");
        listStockCountingItem = stockCountingOrderManager.getListStockCountingIdResultFromLocal("1");

        //saveToLocalListStockCountingItemByBinIdData();
        Intent intent=new Intent(this,StockCountingOrderListActivity.class);
        Bundle bundle = new Bundle();
        bundle.putSerializable(StockCountingOrderListActivity.LIST_STOCKCOUNTING_ORDER_DATA_BY_ID, (Serializable) listStockCountingItem);
        intent.putExtras(bundle);
        startActivity(intent);
    }

    @Override
    public void onBackPressed() {
        showInfoDialog(getResources().getString(R.string.common_information_title),getResources().getString(R.string.warning_must_complete_the_task));
    }

    private void addListOrderItemByBin() throws Exception{
       // StockCountingOrderItemByBinIdData item =new StockCountingOrderItemByBinIdData();
        //StockCountingOrderItemByBinIdData item2 =new StockCountingOrderItemByBinIdData();
        StockCountingOrderItem stockCountingOrderItem =new StockCountingOrderItem();

//        item.id="";
////        item.binId=edtBinId.getText().toString();
////        item.operatorId=edtOperatorId.getText().toString();
////        ListBinData.add(item);

        //for add data
//        item2.id="1";
//        item2.binId=edtBinId.getText().toString();
//        item2.operatorId=edtOperatorId.getText().toString();
//        stockCountingOrderManager.SaveStockCountingOne(item2);

        //for add data
        stockCountingOrderItem.id="1";
        stockCountingOrderItem.binId=edtBinId.getText().toString();
        stockCountingOrderItem.productId ="";
        stockCountingOrderItem.operatorId=edtOperatorId.getText().toString();
        stockCountingOrderManager.SaveStockCountingOrderItem(stockCountingOrderItem);

        showInfoSnackBar(getResources().getString(R.string.msg_counting_order_has_saved));
        clearUI();
    }


}
