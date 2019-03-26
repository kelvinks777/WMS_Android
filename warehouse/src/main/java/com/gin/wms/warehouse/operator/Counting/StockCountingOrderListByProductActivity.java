package com.gin.wms.warehouse.operator.Counting;

import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.Toolbar;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;

import com.gin.ngemart.baseui.adapter.NgemartRecyclerView;
import com.gin.ngemart.baseui.adapter.NgemartRecyclerViewAdapter;
import com.gin.ngemart.baseui.component.NgemartCardView;
import com.gin.wms.manager.StockCountingOrderManager;
import com.gin.wms.manager.db.data.StockCountingOrderData;
import com.gin.wms.manager.db.data.StockCountingOrderItem;
import com.gin.wms.manager.db.data.StockCountingOrderItemByBinIdData;
import com.gin.wms.manager.db.data.StockCountingOrderItemByProductIdData;
import com.gin.wms.warehouse.R;
import com.gin.wms.warehouse.administrator.AdministratorActivity;
import com.gin.wms.warehouse.base.WarehouseActivity;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.List;

public class StockCountingOrderListByProductActivity extends WarehouseActivity implements
        NgemartCardView.CardListener<StockCountingOrderItem>, View.OnClickListener {

    private Button btnCreateOrderByProduct;
    private NgemartRecyclerView recyclerView;
    private NgemartRecyclerViewAdapter<List<StockCountingOrderItemByProductIdData>> adapter;
    private List<StockCountingOrderItemByProductIdData> stockCountingOrderItemByProductDataList = new ArrayList<>();
    private List<StockCountingOrderItem> stockCountingOrderItems = new ArrayList<>();
    private StockCountingOrderData stockCountingOrderData;
    private StockCountingOrderManager stockCountingOrderManager;
    private Toolbar toolbar;

    protected TextView titleDialogEditProduct,titleDialogEditOperator;
    protected EditText etEditProduct,etEditOperator;
    protected Button btnAction;

    public static final String LIST_STOCKCOUNTING_ORDER_DATA_BY_PRODUCT= "stockCountingOrderItemDataByProduct";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        try{
            super.onCreate(savedInstanceState);
            setContentView(R.layout.activity_stock_counting_order_by_product_list);
            getIntentData();
            init();
        }catch (Exception e){
            showErrorDialog(e);
        }
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()){
            case R.id.btnCreateOrderByProduct :
                try{
                    DialogInterface.OnClickListener okListener = FinishOrderTaskThread();
                    showAskDialog(getString(R.string.common_confirmation_title), getResources().getString(R.string.msg_ask_to_save_counting_order), okListener, null);
                }catch(Exception e){
                    showErrorDialog(e);
                }
        }
    }

    @Override
    public void onCardClick(int position, View view, StockCountingOrderItem data) {
        try{
            if(view.getId()==R.id.btnRemoveItemStockCountingOrderByProduct ) {
                showConfirmationToDeleteItemStockCountingOrderByProduct(data);
//                    Toast.makeText(getApplicationContext(), "saya tekan", Toast.LENGTH_SHORT).show();
            }
            else if(view.getId()==R.id.btnEditItemStockCountingOrderByProduct ){
                doEditItemStockCountingOrderByProduct(data,position);
            }
        }  catch(Exception e){
            showErrorDialog(e);
        }
    }

    private void getIntentData(){
        Bundle bundle = getIntent().getExtras();
        if(bundle != null){
            stockCountingOrderItems=(List<StockCountingOrderItem>)bundle.getSerializable(LIST_STOCKCOUNTING_ORDER_DATA_BY_PRODUCT);
        }
    }

    private void init()throws Exception{
        initToolbar();
        justBindingProperties();
        initPropertyHandler();
        initObjectManager();
        initAdapter();
        initRecyclerAdapter();
    }

    private void initToolbar() {
        toolbar = findViewById(R.id.toolbarStockcountingOrderByProduct);
        toolbar.setTitle(getResources().getString(R.string.toolbar_stock_counting_order_by_product));
        setSupportActionBar(toolbar);
        if(getSupportActionBar() != null)
            getSupportActionBar().setDisplayHomeAsUpEnabled(true);
    }

    private void justBindingProperties(){
        btnCreateOrderByProduct=findViewById(R.id.btnCreateOrderByProduct);
        recyclerView=findViewById(R.id.rvStockCountingOrderByProduct);
    }

    private void initPropertyHandler(){
        btnCreateOrderByProduct.setOnClickListener(this);
    }

    private void initObjectManager()throws Exception{
        stockCountingOrderManager = new StockCountingOrderManager(getApplicationContext());
    }

    private void initAdapter(){
        adapter=  new NgemartRecyclerViewAdapter(this, R.layout.card_stock_counting_order_item_by_product_list, stockCountingOrderItems);
        adapter.setHasStableIds(true);
        adapter.setRecyclerListener(this);
        recyclerView.setAdapter(adapter);
    }

    private void initRecyclerAdapter(){
        int numColumn = 1;
        GridLayoutManager gridLayoutManager = new GridLayoutManager(this, numColumn, GridLayoutManager.VERTICAL, false);
        recyclerView.setHasFixedSize(true);
        recyclerView.SetDefaultDecoration();
        recyclerView.setLayoutManager(gridLayoutManager);
    }

    @Override
    public void onBackPressed() {
        showInfoDialog(getResources().getString(R.string.common_information_title),getResources().getString(R.string.warning_must_complete_the_task));
    }

    private void showConfirmationToDeleteItemStockCountingOrderByProduct(StockCountingOrderItem data){
        String title = getResources().getString(R.string.common_information_title);
        String body = "Apakah anda ingin menghapus item ini ?";
        showAskDialog(title, body,
                (dialog, which) -> {
                    try {
                        doDeleteItemStockCountingOrderByProduct(data);
                    } catch (Exception e) {
                        e.printStackTrace();
                    }
                },
                (dialog, which) -> dialog.dismiss());
    }

    private void doDeleteItemStockCountingOrderByProduct(StockCountingOrderItem data) throws Exception{
        stockCountingOrderManager.deleteOneListStockCountingOrderItemByProduct(data.productId);
        stockCountingOrderItems.remove(data);
        adapter.notifyDataSetChanged();
        showInfoSnackBar(getResources().getString(R.string.msg_counting_order_has_deleted));
    }

    private void doEditItemStockCountingOrderByProduct(StockCountingOrderItem data, int position) throws Exception{
        //stockCountingOrderManager.deleteOneListStockCountingByProduct(data.productId);
        AlertDialog.Builder dialog = new AlertDialog.Builder(this);
        LayoutInflater inflater = LayoutInflater.from(this);
        View view = inflater.inflate(R.layout.dialog_edit_item_counting_order_by_product, null);
        dialog.setView(view);

        titleDialogEditProduct=view.findViewById(R.id.tvEditStockcountingOrderByProduct);
        titleDialogEditOperator=view.findViewById(R.id.tvEditStockcountingOrderOperator);
        etEditProduct=view.findViewById(R.id.txtEditStockcountingOrderByProduct);
        etEditOperator=view.findViewById(R.id.txtEditStockcountingOrderOperatorByProduct);
        btnAction=view.findViewById(R.id.btnUpdateStockcountingOrderItemByProduct);

        AlertDialog alertDialog = dialog.create();
        alertDialog.setCanceledOnTouchOutside(false);
        alertDialog.show();

        btnAction.setOnClickListener(v->{
            if(etEditProduct.getText().toString().isEmpty()){
                String warning=getResources().getString(R.string.msg_warning_field_must_be_filled);
                etEditProduct.setError(warning);
            }
            else{
                try{
                    String product=etEditProduct.getText().toString();
                    String operator=etEditOperator.getText().toString();
                    data.productId=product;
                    data.operatorId=operator;
                    //stockCountingOrderItemByProductDataList.set(position,data);
                    stockCountingOrderManager.UpdateOneListStockCountingOrderByBin(data);
                    adapter.notifyDataSetChanged();
                    alertDialog.cancel();
                }catch (Exception e){
                    showErrorDialog(e);
                }
            }
        });
    }

    private StockCountingOrderData prepareStockCountingOrderData(){
        StockCountingOrderData preparedData= new StockCountingOrderData();

        preparedData.Id="";
        preparedData.clientId="BKS.01";
        preparedData.docDate= Calendar.getInstance().getTime();
        preparedData.longInfo="";
        preparedData.Status=1;
        preparedData.docType=9;
        preparedData.sourceId="BKS.01";
        preparedData.docRefId="";
        preparedData.docRefUri="com.gin.wms.task.stockcountingorder";
        preparedData.Items.addAll(stockCountingOrderItems);

        return preparedData;
    }

    private void moveToTaskSwitcherActivity(){
        Intent intent = new Intent(this, AdministratorActivity.class);
        //intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
        startActivity(intent);
        finish();
    }

    private void saveToServerStockcountingOrderDataByProduct(StockCountingOrderData stockCountingOrderData) throws Exception{
        ThreadStart(new ThreadHandler<Boolean>() {

            @Override
            public void onPrepare() throws Exception {
                startProgressDialog(getString(R.string.msg_saving_counting_order), ProgressType.SPINNER);
            }

            @Override
            public Boolean onBackground() throws Exception {
                stockCountingOrderManager.CreateStockCountingOrderByProduct(stockCountingOrderData);
                return true;
            }

            @Override
            public void onError(Exception e) {
                progressDialog.dismiss();
                showErrorDialog(e);
            }

            @Override
            public void onSuccess(Boolean data) throws Exception {

                //showInfoSnackBar(getResources().getString(R.string.msg_counting_order_has_saved));
                showAlertToast(getResources().getString(R.string.msg_counting_order_has_saved));
                stockCountingOrderItems.clear();
                adapter.notifyDataSetChanged();
                stockCountingOrderManager.DeleteStockCountingOneByProduct(stockCountingOrderItems);
                moveToTaskSwitcherActivity();

                //clearUI();
            }

            @Override
            public void onFinish() {
                dismissProgressDialog();
            }
        });
    }

    private DialogInterface.OnClickListener FinishOrderTaskThread(){
        return new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                try {
                    stockCountingOrderData=prepareStockCountingOrderData();
                    saveToServerStockcountingOrderDataByProduct(stockCountingOrderData);
                } catch (Exception e) {
                    showErrorDialog(e);
                }
            }
        };
    }


}
