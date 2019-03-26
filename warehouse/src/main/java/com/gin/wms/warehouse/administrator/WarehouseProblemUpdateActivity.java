package com.gin.wms.warehouse.administrator;

import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toolbar;

import com.gin.ngemart.baseui.NgemartActivity;
import com.gin.wms.manager.StockLocationManager;
import com.gin.wms.manager.WarehouseProblemManager;
import com.gin.wms.manager.db.data.StockLocationData;
import com.gin.wms.manager.db.data.WarehouseProblemData;
import com.gin.wms.manager.db.data.enums.StockLocationEnum;
import com.gin.wms.warehouse.R;

import com.gin.wms.warehouse.base.WarehouseActivity;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

public class WarehouseProblemUpdateActivity extends WarehouseActivity implements View.OnClickListener{
    private TextView etPalletWarehouseProblem, etProductWarehouseProblem, etQtyStocklocation;
    private Button btnUpdateWarehouseProblem;
    private WarehouseProblemData warehouseProblemData;
    private StockLocationData stockLocationData;
    private StockLocationData collectStocklocationData;
    private WarehouseProblemData collectWarehouseProblem;
    private WarehouseProblemManager warehouseProblemManager;
    private StockLocationManager stockLocationManager;
    private List<WarehouseProblemData> listWarehouseProblemData = new ArrayList<>();
    private Toolbar toolbar;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        try{
            super.onCreate(savedInstanceState);
            setContentView(R.layout.activity_warehouse_problem_update);
            getIntentData();
            init();
        }catch(Exception e){
            showErrorDialog(e);
        }
    }

    @Override
    public void onBackPressed() {
        showInfoDialog(getResources().getString(R.string.common_information_title),getResources().getString(R.string.warning_must_complete_the_task));
    }

    private void getIntentData(){
        Bundle bundle = getIntent().getExtras();
        if(bundle != null){
            warehouseProblemData = (WarehouseProblemData) bundle.getSerializable(WarehouseProblemListActivity.LIST_WAREHOUSEPROBLEM);
            stockLocationData = (StockLocationData) bundle.getSerializable(WarehouseProblemListActivity.GET_STOCKLOCATION);
        }
    }

    private void init() throws Exception{
        initToolbar();
        justBindingProperties();
        initPropertyHandler();
        initObjectManager();
        showDataToUI();
    }

    private void initToolbar() {
        android.support.v7.widget.Toolbar toolbar = findViewById(R.id.toolbarUpdateWarehouseProblem);
        toolbar.setTitle(getResources().getString(R.string.update_list_warehouse_problem));
        setSupportActionBar(toolbar);
        if(getSupportActionBar() != null)
            getSupportActionBar().setDisplayHomeAsUpEnabled(true);
    }

    private void justBindingProperties(){
        etPalletWarehouseProblem=findViewById(R.id.etPalletWarehouseProblem);
        etProductWarehouseProblem=findViewById(R.id.etProductWarehouseProblem);
        etQtyStocklocation =findViewById(R.id.etQtyStocklocation);
        btnUpdateWarehouseProblem= findViewById(R.id.btnUpdateWarehouseProblem);
    }

    private void initPropertyHandler(){
        btnUpdateWarehouseProblem.setOnClickListener(this);
    }

    private void initObjectManager()throws Exception{
        warehouseProblemManager = new WarehouseProblemManager(this);
        stockLocationManager = new StockLocationManager(this);
    }

    @Override
    public void onClick(View v) {
        try{
            switch (v.getId()){
                case R.id.btnUpdateWarehouseProblem:
                    if(ValidateData()){
                        DialogInterface.OnClickListener okListener = UpdateWarehouseProblemThread();
                        showAskDialog(getString(R.string.common_confirmation_title), getResources().getString(R.string.ask_to_update_warehouse_problem), okListener, null);
                    }
                    break;
            }
        }catch (Exception e){
            showErrorDialog(e);
        }

    }

    private void showDataToUI() {
        etPalletWarehouseProblem.setText(warehouseProblemData.palletNo);
        etProductWarehouseProblem.setText(warehouseProblemData.productId);
        etQtyStocklocation.setText(String.valueOf(stockLocationData.qty));
    }

    private Boolean ValidateData(){
        Boolean check = true;
        String Warning="Harus Diisi!!!";
        if(etPalletWarehouseProblem.getText().toString().equals("")){
            etPalletWarehouseProblem.requestFocus();
            etPalletWarehouseProblem.setError(Warning);
            check=false;
        }
        if(etProductWarehouseProblem.getText().toString().equals("")){
            etProductWarehouseProblem.requestFocus();
            etProductWarehouseProblem.setError(Warning);
            check=false;
        }
        if(etQtyStocklocation.getText().toString().equals("")){
            etQtyStocklocation.requestFocus();
            etQtyStocklocation.setError(Warning);
            check=false;
        }
            return check;
    }

    private WarehouseProblemData prepareDataForUpdate() throws Exception{
        WarehouseProblemData item= new WarehouseProblemData();
       // item.binId = etBinWarehouseProblem.getText().toString();
        item.palletNo = etPalletWarehouseProblem.getText().toString();
        item.productId = etProductWarehouseProblem.getText().toString();
        //item.operatorId = etOperatorWarehouseProblem.getText().toString();
       // item.operatorName = etOperatorNameWarehouseProblem.getText().toString();
        item.Type = warehouseProblemData.Type;
        item.Status = warehouseProblemData.Status;
        item.Action = warehouseProblemData.Action;
        item.inputTime = warehouseProblemData.inputTime;
        return item;
    }

    private StockLocationData prepareStocklocationDataForUpdate() throws Exception{
        StockLocationData item= new StockLocationData();
        item.productId = etProductWarehouseProblem.getText().toString();
        item.bin = warehouseProblemData.binId;
        item.palletNo = etPalletWarehouseProblem.getText().toString();
        item.clientId = stockLocationData.clientId;
        item.clientLocationId =stockLocationData.clientLocationId;
        item.qty = Double.parseDouble(etQtyStocklocation.getText().toString());
        item.bookInQty = stockLocationData.bookInQty;
        item.bookOutQty = stockLocationData.bookOutQty;
        item.expiredDate = stockLocationData.expiredDate;
        item.stockStatus = StockLocationEnum.UNFREEZE_STOCK.getValue();

        return item;
    }

    private DialogInterface.OnClickListener UpdateWarehouseProblemThread(){
        return new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                try {
                    collectStocklocationData = prepareStocklocationDataForUpdate();
                   // collectWarehouseProblem=prepareDataForUpdate();
                    updateToServerStockLocationData(collectStocklocationData);
                  //  updateToServerWarehouseProblemData(collectWarehouseProblem);
                } catch (Exception e) {
                    showErrorDialog(e);
                }
            }
        };
    }

//    private void updateToServerWarehouseProblemData(WarehouseProblemData warehouseProblemData) throws Exception{
//        ThreadStart(new ThreadHandler<Boolean>() {
//            @Override
//            public void onPrepare() throws Exception {
//                startProgressDialog(getString(R.string.msg_update_data_warehouse_problem), ProgressType.SPINNER);
//            }
//
//            @Override
//            public Boolean onBackground() throws Exception {
//                warehouseProblemManager.SaveWarehouseProblemData(warehouseProblemData);
//                return true;
//            }
//
//            @Override
//            public void onError(Exception e) {
//                progressDialog.dismiss();
//                showErrorDialog(e);
//            }
//
//            @Override
//            public void onSuccess(Boolean data) throws Exception {
//                showAlertToast(getResources().getString(R.string.msg_update_data_warehouse_problem_has_saved));
//                clearUI();
//                getListWarehouseproblem();
//            }
//
//            @Override
//            public void onFinish() {
//                dismissProgressDialog();
//            }
//        });
//    }

    private void updateToServerStockLocationData(StockLocationData stockLocationData) throws Exception{
        ThreadStart(new ThreadHandler<Boolean>() {
            @Override
            public void onPrepare() throws Exception {
                startProgressDialog(getString(R.string.msg_update_data_warehouse_problem), ProgressType.SPINNER);
            }

            @Override
            public Boolean onBackground() throws Exception {
                stockLocationManager.UpdateStockLocationData(stockLocationData);
                return true;
            }

            @Override
            public void onError(Exception e) {
                progressDialog.dismiss();
                showErrorDialog(e);
            }

            @Override
            public void onSuccess(Boolean data) throws Exception {
                showAlertToast(getResources().getString(R.string.msg_update_data_warehouse_problem_has_saved));
                clearUI();
                deleteWarehouseproblem();
            }

            @Override
            public void onFinish() {
                dismissProgressDialog();
            }
        });
    }

    private void clearUI(){
        etPalletWarehouseProblem.setText("");
        etProductWarehouseProblem.setText("");
        etQtyStocklocation.setText("");
    }

    public void getListWarehouseproblem(){
        ThreadStart(new ThreadHandler<List<WarehouseProblemData>>() {
            @Override
            public void onPrepare() throws Exception {
               // startProgressDialog(getString(R.string.progress_get_warehouse_problem), NgemartActivity.ProgressType.SPINNER);
            }

            @Override
            public List<WarehouseProblemData> onBackground() throws Exception {
                listWarehouseProblemData = warehouseProblemManager.GetWarehouseProblem();
                return listWarehouseProblemData;
            }

            @Override
            public void onError(Exception e) {
                dismissProgressDialog();
                showErrorDialog(e);
            }

            @Override
            public void onSuccess(List<WarehouseProblemData> data) throws Exception {
                if(data != null){
                    goToWarehouseProblem(data);
                }
            }

            @Override
            public void onFinish() {
                dismissProgressDialog();
            }
        });
    }

    public void deleteWarehouseproblem(){
        ThreadStart(new ThreadHandler<Boolean>() {
            @Override
            public void onPrepare() throws Exception {
                // startProgressDialog(getString(R.string.progress_get_warehouse_problem), NgemartActivity.ProgressType.SPINNER);
            }

            @Override
            public Boolean onBackground() throws Exception {
                warehouseProblemManager.DeleteData(warehouseProblemData);
                return true;
            }

            @Override
            public void onError(Exception e) {
                dismissProgressDialog();
                showErrorDialog(e);
            }

            @Override
            public void onSuccess(Boolean data) throws Exception {
                getListWarehouseproblem();
            }

            @Override
            public void onFinish() {
                dismissProgressDialog();
            }
        });
    }


    private void goToWarehouseProblem(List<WarehouseProblemData> data){
        Intent intent = new Intent(this, WarehouseProblemListActivity.class);
        Bundle bundle = new Bundle();
        bundle.putSerializable(WarehouseProblemListActivity.LIST_WAREHOUSEPROBLEM, (Serializable) data);
        intent.putExtras(bundle);
        startActivity(intent);
        finish();
    }

}
