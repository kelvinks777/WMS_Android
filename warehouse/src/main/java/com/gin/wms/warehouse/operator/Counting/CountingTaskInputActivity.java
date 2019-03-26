package com.gin.wms.warehouse.operator.Counting;

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
import com.gin.wms.manager.CountingTaskManager;
import com.gin.wms.manager.db.data.CountingTaskResultData;
import com.gin.wms.warehouse.R;
import com.gin.wms.warehouse.base.WarehouseActivity;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

public class CountingTaskInputActivity extends WarehouseActivity implements View.OnClickListener{
    private EditText etPalletNo, etInputQty, etInputProduct;
    private CountingTaskResultData resultData;
    private CountingTaskManager countingTaskManager;
    private List<CountingTaskResultData> uncheckedResultData = new ArrayList<>();
    private List<CountingTaskResultData> checkedResultData = new ArrayList<>();
    private String mode;

    //region override function
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        try {
            super.onCreate(savedInstanceState);
            setContentView(R.layout.activity_stock_counting_input);

            countingTaskManager = new CountingTaskManager(this);
            getDataFromIntent();
            initComponent();
        }
        catch (Exception e){
            showErrorDialog(e);
        }
    }

    @Override
    public void onClick(View v) {
        try{
            switch (v.getId()){
                case R.id.btnScanPalletCounting:
                    LaunchBarcodeScanner();
                    break;
                case R.id.btnAddCounting:
                    String palletNo = etPalletNo.getText().toString();
                    String productId = etInputProduct.getText().toString();
                    double qty = Double.parseDouble(etInputQty.getText().toString());
                    if (etPalletNo.getText().toString().isEmpty()) {
                        showErrorDialog(getResources().getString(R.string.error_cannot_input_text_empty));
                        return;
                    }
                    if(etInputProduct.getText().toString().isEmpty()){
                        showErrorDialog(getResources().getString(R.string.msg_product_id_empty));
                        return;
                    }
                    if (etInputQty.getText().toString().isEmpty()) {
                        showErrorDialog(getResources().getString(R.string.msg_qty_not_filled));
                        return;
                    }
                    else {
                        saveCountingResultConfirmation(palletNo, productId, qty);
                    }
                    break;
            }
        }catch (Exception e){
            showErrorDialog(e);
        }
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
                moveBackToStockCountingActivity();
                break;
            default:
                return super.onOptionsItemSelected(item);
        }
        return true;
    }

    private DialogInterface.OnClickListener addResult(String palletNo, String productId, double qty) {
        return new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                try {
                    addResultThread(palletNo, productId, qty);
                } catch (Exception e) {
                    showErrorDialog(e);
                }
            }
        };
    }

    //endregion

    //region init
    private void initComponent() {
        etPalletNo = findViewById(R.id.etPalletNoCounting);
        ImageButton btnScanPallet = findViewById(R.id.btnScanPalletCounting);
        etInputQty = findViewById(R.id.etInputQty);
        etInputProduct = findViewById(R.id.etInputProduct);
        btnScanPallet.setOnClickListener(this);
        setExternalBarcodeActive(true);
        initToolBar();
        initButtonAddResult();
    }

    private void initButtonAddResult() {
        Button btnAddCountingResult = findViewById(R.id.btnAddCounting);
        btnAddCountingResult.setOnClickListener(this);

        if(mode != null && mode.equals(CountingTaskActivity.ARG_MODE_EDIT)) {
            btnAddCountingResult.setText(getResources().getString(R.string.btnSave));
            btnAddCountingResult.setBackgroundColor(getResources().getColor(R.color.colorAccent));
            showDataToUI();
        }
    }

    private void initToolBar() {
        android.support.v7.widget.Toolbar tbCountingTask = findViewById(R.id.toolbarInputCounting);
        tbCountingTask.setTitle(getResources().getString(R.string.toolbar_input_stock));
        setSupportActionBar(tbCountingTask);
        if(getSupportActionBar() != null)
            getSupportActionBar().setDisplayHomeAsUpEnabled(true);
    }

    //endregion

    //region other function
    private void getDataFromIntent() {
        Bundle bundle = getIntent().getExtras();
        if(bundle != null) {
            resultData = (CountingTaskResultData) bundle.getSerializable(CountingTaskActivity.COUNTING_TASK_PER_RESULT_DATA_CODE);

            uncheckedResultData = (List<CountingTaskResultData>)bundle.getSerializable(CountingTaskActivity.ALL_UNCHECKED_RESULT);

            checkedResultData = (List<CountingTaskResultData>)bundle.getSerializable(CountingTaskActivity.ALL_CHECKED_RESULT);
        }

        if(getIntent() != null && getIntent().hasExtra(CountingTaskActivity.ARG_INPUT_MODE))
            mode = getIntent().getStringExtra(CountingTaskActivity.ARG_INPUT_MODE);

    }

    private void showDataToUI() {
        etPalletNo.setText(resultData.PalletNo);
        etInputProduct.setText(resultData.ProductId);
        etInputQty.setText(String.valueOf((int)resultData.Qty));
    }

    private void moveBackToStockCountingActivity() {
        Intent intent = new Intent(this, CountingTaskActivity.class);
        Bundle bundle = new Bundle();
        bundle.putSerializable(CountingTaskActivity.ALL_UNCHECKED_RESULT, (Serializable) uncheckedResultData);
        bundle.putSerializable(CountingTaskActivity.ALL_CHECKED_RESULT, (Serializable) checkedResultData);
        bundle.putSerializable(CountingTaskActivity.COUNTING_TASK_RESULT_CHECKED_CODE, resultData);
        intent.putExtras(bundle);
        startActivity(intent);
    }

    private void saveCountingResultConfirmation(String palletNo, String productId, double qty) {
        DialogInterface.OnClickListener okListener = addResult(palletNo, productId, qty);
        showAskDialog(getString(R.string.common_confirmation_title), getResources().getString(R.string.msg_ask_to_save_data_counting), okListener, null);
    }

    private void clearUi() {
        etPalletNo.setText("");
        etInputProduct.setText("");
        etInputQty.setText("");
    }

    //endregion

    //region Thread
    private void addResultThread(String palletNo, String productId, double qty) {
        ThreadStart(new ThreadHandler<Boolean>() {
            @Override
            public void onPrepare() throws Exception {
                startProgressDialog(getString(R.string.common_progress_content), ProgressType.SPINNER);
            }

            @Override
            public Boolean onBackground() throws Exception {
                countingTaskManager.updateResult(resultData.countingId, resultData.BinId,
                        palletNo, productId, qty, resultData.hasChecked);
                return true;
            }

            @Override
            public void onError(Exception e) {
                resultData.hasChecked = false;
                showErrorDialog(e);
            }

            @Override
            public void onSuccess(Boolean data) throws Exception {
                resultData.hasChecked = true;
                resultData.PalletNo = palletNo;
                resultData.ProductId = productId;
                resultData.Qty = qty;
                clearUi();
                showAlertToast(getResources().getString(R.string.msg_data_has_been_saved));
            }

            @Override
            public void onFinish() {
                dismissProgressDialog();
            }
        });
    }
    //endregion
}
