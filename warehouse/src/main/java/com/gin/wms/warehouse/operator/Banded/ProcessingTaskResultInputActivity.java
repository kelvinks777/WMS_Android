package com.gin.wms.warehouse.operator.Banded;

import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageButton;

import com.bosnet.ngemart.libgen.barcode_scanner.integration.IntentIntegrator;
import com.bosnet.ngemart.libgen.barcode_scanner.integration.IntentResult;
import com.gin.wms.manager.ProcessingTaskManager;
import com.gin.wms.manager.db.data.BandedData;
import com.gin.wms.manager.db.data.MasterBandedData;
import com.gin.wms.manager.db.data.ProcessingTaskData;
import com.gin.wms.manager.db.data.StockLocationData;
import com.gin.wms.warehouse.R;
import com.gin.wms.warehouse.base.WarehouseActivity;
import com.gin.wms.warehouse.component.CompUomInterpreter;

import java.io.Serializable;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.List;

public class ProcessingTaskResultInputActivity extends WarehouseActivity implements View.OnClickListener {
    private BandedData bandedData;
    private ProcessingTaskManager processingTaskManager;
    private ProcessingTaskData processingTaskData;
    private List<MasterBandedData> masterBandedDataList = new ArrayList<>();
    private List<StockLocationData> stockLocationData = new ArrayList<>();
    private EditText etPalletNo;
    private CompUomInterpreter compUomInterpreter;
    private double qtyRemaining;
    private Date nearestExpiredDate = null;

    //region Override Functions
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        try {
            super.onCreate(savedInstanceState);
            setContentView(R.layout.activity_processing_task_result_input);
            initObject();
            getDataFromIntent();
            initComponent();
        } catch (Exception e) {
            showErrorDialog(e);
        }
    }

    @Override
    public void onClick(View v) {
        try{
            switch (v.getId()){
                case R.id.btnScanBandedPallet:
                    LaunchBarcodeScanner();
                    break;
                case R.id.btnAddBanded:
                    String palletNo = etPalletNo.getText().toString();
                    if (etPalletNo.getText().toString().isEmpty()) {
                        showErrorDialog(getResources().getString(R.string.error_cannot_input_text_empty));
                        return;
                    }
                    if (compUomInterpreter.getQty() == 0.0) {
                        showErrorDialog(getResources().getString(R.string.msg_qty_not_filled));
                        return;
                    }
                    if (compUomInterpreter.getQty() >  compUomInterpreter.getPalletConversion()) {
                        showErrorDialog(getResources().getString(R.string.msg_more_than_value_per_pallet));
                        return;
                    }
                    else {
                        saveCheckResultThreadConfirmation(palletNo);
                    }
                    break;
            }
        }catch (Exception e){
            showErrorDialog(e);
        }
    }

    @Override
    public void onBackPressed() {
        moveBackToTaskProcessingActivity();
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        try {
            IntentResult result = IntentIntegrator.parseActivityResult(requestCode, resultCode, data);
            if (result != null) {
                if (result.getContents() != null) {
                    String barcodeId = result.getContents();
                    etPalletNo.setText(barcodeId);
                }
            }
        } catch (Exception e) {
            showErrorDialog(e);
        }
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()){
            case android.R.id.home:
                moveBackToTaskProcessingActivity();
                break;
            default:
                return super.onOptionsItemSelected(item);
        }
        return true;
    }

    //endregion

    //region Init

    private void initObject() throws Exception{
        processingTaskManager = new ProcessingTaskManager(this);
        compUomInterpreter = new CompUomInterpreter(this);
    }

    private void getDataFromIntent() {
        Bundle bundle = getIntent().getExtras();

        if(bundle != null){
            bandedData = (BandedData) bundle.getSerializable(ProcessingTaskCheckActivity.BANDED_ORDER_DATA_CODE);
            processingTaskData = (ProcessingTaskData) bundle.getSerializable(ProcessingTaskCheckActivity.PROCESSING_TASK_CODE);
            masterBandedDataList = (List<MasterBandedData>) bundle.getSerializable(ProcessingTaskCheckActivity.MASTER_BANDED_DATA_CODE);
            stockLocationData = (List<StockLocationData>) bundle.getSerializable(ProcessingTaskCheckActivity.STOCK_LOCATION_DATA_CODE);

            qtyRemaining = bundle.getDouble(ProcessingTaskCheckActivity.ARG_QTY_REMAINING);
        }
    }

    private void initComponent() {
        setExternalBarcodeActive(true);
        ImageButton btnScanPallet = findViewById(R.id.btnScanBandedPallet);
        Button btnSave = findViewById(R.id.btnAddBanded);
        etPalletNo = findViewById(R.id.etPalletNo);
        compUomInterpreter = findViewById(R.id.compUomInterpreterBanded);

        btnScanPallet.setOnClickListener(this);
        btnSave.setOnClickListener(this);

        initCompUomInterpreter();
        compUomInterpreter.setEditButtonActive(true);
        initToolbar();
    }

    private void initCompUomInterpreter() {
        compUomInterpreter.setPalletConversion(processingTaskData.itemProduct.palletConversionValue);
        compUomInterpreter.setCompUomData(processingTaskData.itemProduct.getCompUom());
        compUomInterpreter.setQty(processingTaskData.itemProduct.qty);
        clearUi();
    }

    private void initToolbar() {
        android.support.v7.widget.Toolbar toolbar = findViewById(R.id.toolbarInputBanded);
        toolbar.setTitle(getResources().getString(R.string.toolbar_input_banded));
        setSupportActionBar(toolbar);
        if(getSupportActionBar() != null)
            getSupportActionBar().setDisplayHomeAsUpEnabled(true);
    }

    private Date getDateExpired() throws Exception{
        List<Date> dateList = new ArrayList<>();

        DateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd HH:mm");
        Date current = dateFormat.parse(dateFormat.format(Calendar.getInstance().getTime()));
        long currentTimeInLong = current.getTime();
        long different = -1;

        for (StockLocationData slData: stockLocationData) {
            dateList.add(slData.expiredDate);
        }

        for (Date date: dateList) {
            long differentTime = Math.abs(currentTimeInLong - date.getTime());
            if((different == -1) || (differentTime < different)){
                different = differentTime;
                nearestExpiredDate = date;
            }
        }

        return nearestExpiredDate;
    }
    //endregion

    //region Other Function

    private void moveBackToTaskProcessingActivity() {
        Intent intent = new Intent(this, ProcessingTaskCheckActivity.class);
        Bundle bundle = new Bundle();
        bundle.putSerializable(ProcessingTaskCheckActivity.BANDED_ORDER_DATA_CODE, bandedData);
        bundle.putSerializable(ProcessingTaskCheckActivity.MASTER_BANDED_DATA_CODE, (Serializable) masterBandedDataList);
        bundle.putSerializable(ProcessingTaskCheckActivity.PROCESSING_TASK_CODE, processingTaskData);
        bundle.putSerializable(ProcessingTaskCheckActivity.STOCK_LOCATION_DATA_CODE, (Serializable) stockLocationData);
        bundle.putDouble(ProcessingTaskCheckActivity.ARG_QTY_REMAINING, qtyRemaining);
        intent.putExtra(ProcessingTaskCheckActivity.ARG_ACTIVITY_COME_FROM, ProcessingTaskCheckActivity.ARG_FROM_ACTIVITY_RESULT_INPUT);
        intent.putExtras(bundle);
        startActivity(intent);
        finish();
    }

    private void saveCheckResultThreadConfirmation(String palletNo) {
        DialogInterface.OnClickListener okListener = addThread(palletNo);
        showAskDialog(getString(R.string.common_confirmation_title), getResources().getString(R.string.msg_ask_to_save_banded_data), okListener, null);
    }

    private DialogInterface.OnClickListener addThread(String palletNo){
        return new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                try {
                    checkResultAddThread(palletNo);
                } catch (Exception e) {
                    showErrorDialog(e);
                }
            }
        };
    }

    //endregion

    //region UI Functions

    private void clearUi() {
        etPalletNo.setText("");
        compUomInterpreter.setQty(0);
        HideSoftInputKeyboard();
    }

    //endregion

    //region Thread

    private void checkResultAddThread(String palletNo)throws Exception{
        ThreadStart(new ThreadHandler <Boolean> () {
        @Override
            public void onPrepare() {
                startProgressDialog(getString(R.string.common_progress_content), ProgressType.SPINNER);
            }

            @Override
            public Boolean onBackground()throws Exception{
                processingTaskData.itemProduct.qty = compUomInterpreter.getQty();
                processingTaskManager.createResult(processingTaskData.processingTaskId, processingTaskData.itemProduct.productId, processingTaskData.itemProduct.clientId,
                        palletNo, getDateExpired(), processingTaskData.itemProduct.qty);
                return true;
            }

            @Override
            public void onError(Exception e) {
                showErrorDialog(e);
            }

            @Override
            public void onSuccess(Boolean data) throws Exception {
                clearUi();
                showAlertToast(getResources().getString(R.string.msg_data_has_been_saved));
            }

            @Override
            public void onFinish() {
                dismissProgressDialog();
                moveBackToTaskProcessingActivity();
            }
        });
    }
    //endregion
}
