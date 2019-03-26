package com.gin.wms.warehouse.warehouseProblem;

import android.content.Intent;
import android.os.Bundle;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;

import com.gin.ngemart.baseui.NgemartActivity;
import com.gin.wms.manager.OperatorManager;
import com.gin.wms.manager.WarehouseProblemManager;
import com.gin.wms.manager.db.data.WarehouseProblemData;
import com.gin.wms.warehouse.R;
import com.gin.wms.warehouse.base.WarehouseActivity;
import com.gin.wms.warehouse.operator.TaskSwitcherActivity;

import java.util.Date;

public class WarehouseProblemReportActivity extends WarehouseActivity implements View.OnClickListener {

    private Button btnSubmit;
    private TextView operatorId, operatorName;
    private EditText sourceBin, palletNo, productId;
    private WarehouseProblemManager warehouseProblemManager;
    private OperatorManager operatorManager;
    private WarehouseProblemData previousWarehouseProblemData;
    private String opId, opName;

    //region Override function
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        try{
            super.onCreate(savedInstanceState);
            setContentView(R.layout.activity_warehouse_problem_report);

            warehouseProblemManager = new WarehouseProblemManager(getApplicationContext());
            operatorManager = new OperatorManager(getApplicationContext());

            initComponent();
            getOperatorDataThread();
        }
        catch (Exception e) {
            showErrorDialog(e);
        }
    }

    @Override
    public void onClick(View v) {
        try{
            HideSoftInputKeyboard();
            switch (v.getId()){
                case R.id.btnSubmit:
                    if (validatedProblemDataIsCompleted()){
                        WarehouseProblemData dataToSave = prepareWarehouseData();
                        justSaveWarehouseDataThread(dataToSave);
                    }
                    break;
            }
        }catch (Exception e){
            showErrorDialog(e);
        }
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()){
            case android.R.id.home:
                moveToTaskSwitcherActivity();
                break;
        }
        return super.onOptionsItemSelected(item);
    }

    @Override
    public void onBackPressed() {
        moveToTaskSwitcherActivity();
    }

    //endregion

    //region Init
    private void initComponent() {
        btnSubmit = findViewById(R.id.btnSubmit);
        sourceBin = findViewById(R.id.edSourceBin);
        palletNo = findViewById(R.id.edPalletNo);
        productId = findViewById(R.id.edProductId);
        operatorId = findViewById(R.id.edOperatorId);
        operatorName = findViewById(R.id.edOperatorName);
        btnSubmit.setOnClickListener(this);

        initToolbar();
    }

    private void initToolbar() {
        android.support.v7.widget.Toolbar toolbarTitle = findViewById(R.id.toolbarReport);
        toolbarTitle.setTitle(R.string.warehouse_toolbar);
        setSupportActionBar(toolbarTitle);
        if(getSupportActionBar() != null)
            getSupportActionBar().setDisplayHomeAsUpEnabled(true);
    }

    //region Other Function

    private void getOperatorDataThread() {
        ThreadStart(new ThreadHandler<String>() {

            @Override
            public void onPrepare() throws Exception {

            }

            @Override
            public String onBackground() throws Exception {
                opId = operatorManager.getOperatorId();
                opName = operatorManager.getOperatorName(opId);

                return opId;
            }

            @Override
            public void onError(Exception e) {
                showErrorDialog(e);
            }

            @Override
            public void onSuccess(String data) throws Exception {
                setOperatorData();
            }

            @Override
            public void onFinish() {

            }
        });
    }

    private void justSaveWarehouseDataThread(WarehouseProblemData warehouseProblemData) throws Exception {
        ThreadStart(new ThreadHandler<Boolean>() {
            @Override
            public void onPrepare() throws Exception{
                startProgressDialog(getString(R.string.warehouse_problem_saving), NgemartActivity.ProgressType.SPINNER);
            }

            @Override
            public Boolean onBackground() throws Exception {
                warehouseProblemManager.CreateWarehouseProblemData(warehouseProblemData);
                previousWarehouseProblemData = warehouseProblemData;
                return true;
            }

            @Override
            public void onError(Exception e) {
                previousWarehouseProblemData = null;
                progressDialog.dismiss();
                showErrorDialog(e);
            }

            @Override
            public void onSuccess(Boolean data) throws Exception {
                showAlertToast(getResources().getString(R.string.warehouse_problem_successfully_saved));
                clearUi();
            }

            @Override
            public void onFinish() {
                dismissProgressDialog();
            }
        });
    }

    private void clearUi() {
        sourceBin.setText("");
        palletNo.setText("");
        productId.setText("");
    }

    private void moveToTaskSwitcherActivity(){
        Intent intent = new Intent(this, TaskSwitcherActivity.class);
        intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
        startActivity(intent);
        finish();
    }

    private void setOperatorData() {
        operatorId.setText(opId);
        operatorName.setText(opName);
    }
    //endregion

    private boolean validatedProblemDataIsCompleted(){
        boolean isValid = true;
        String warningText = getResources().getString(R.string.warning_data_should_be_filled);

        if(sourceBin.getText().toString().equals("")){
            sourceBin.setError(warningText);
            isValid = false;
        }

        if(palletNo.getText().toString().equals("")){
            palletNo.setError(warningText);
            isValid = false;
        }

        if(productId.getText().toString().equals("")){
            productId.setError(warningText);
            isValid = false;
        }

        return isValid;
    }

    private WarehouseProblemData prepareWarehouseData(){
        WarehouseProblemData preparedData = new WarehouseProblemData();

        preparedData.binId = sourceBin.getText().toString();
        preparedData.palletNo = palletNo.getText().toString();
        preparedData.productId = productId.getText().toString();
        preparedData.operatorId = opId;
        preparedData.operatorName = opName;
        preparedData.Type = 0;
        preparedData.Status = 1;
        preparedData.Action = 0;
        preparedData.inputTime = new Date();

        return preparedData;
    }
}