package com.gin.wms.warehouse.operator.Order;

import android.app.Activity;
import android.app.DatePickerDialog;
import android.content.Intent;
import android.os.Build;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.widget.Toolbar;
import android.view.View;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.ImageButton;

import com.bosnet.ngemart.libgen.Common;
import com.bosnet.ngemart.libgen.DateUtil;
import com.bosnet.ngemart.libgen.barcode_scanner.integration.IntentIntegrator;
import com.bosnet.ngemart.libgen.barcode_scanner.integration.IntentResult;
import com.gin.wms.manager.ManualMutationManager;
import com.gin.wms.manager.MovingTaskManager;
import com.gin.wms.manager.db.data.ManualMutationData;
import com.gin.wms.manager.db.data.MovingTaskDestItemData;
import com.gin.wms.manager.db.data.MovingTaskSourceItemData;
import com.gin.wms.warehouse.R;
import com.gin.wms.warehouse.base.WarehouseActivity;
import com.gin.wms.warehouse.component.CompUomInterpreter;
import com.gin.wms.warehouse.component.SingleInputDialogFragment;

import org.joda.time.DateTime;

import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.Locale;

public class ManualMutationOrderActivity extends WarehouseActivity
        implements View.OnClickListener{

    public static final String MANUAL_MUTATION_DATA_CODE = "mutationData";
    private Toolbar toolbar;
    private MovingTaskManager movingTaskManager;
    private CompUomInterpreter compUomInterpreterManualMutation;
    private MovingTaskSourceItemData movingTaskSourceItemData;
    private MovingTaskDestItemData movingTaskDestItemData;
    private ManualMutationData manualMutationData;
    private ManualMutationManager manualMutationManager;
    private EditText edtProductId, edtSoucePallete, edtSouceBin, edtDestPallete, edtDestBin, edtQty, edtExpiredDate;
    private Button btnMovingManualMutation;
    private ImageButton btnExpiredDate;
    private DatePickerDialog datePickerDialog;
    private Date selectedDate;
    private static final String SHORT_DATE_PATTERN = "d MMM yyyy";

    View.OnClickListener listener =new View.OnClickListener() {
        @Override
        public void onClick(View v) {
            try {
                datePickerDialog.setTitle("");
                datePickerDialog.setCancelable(false);
                datePickerDialog.getDatePicker().setMinDate(DateUtil.GetMinDate());
                datePickerDialog.show();
            } catch (Exception e) {
                showErrorDialog(e);
            }
        }
    };

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        try{
            super.onCreate(savedInstanceState);
            setContentView(R.layout.activity_manual_mutation_order);
            init();
        }catch (Exception e){
            showErrorDialog(e);
        }
    }

    private void initDatePicker() {
        btnExpiredDate.setOnClickListener(listener);

        Calendar calendar = Calendar.getInstance();

        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.HONEYCOMB) {
            int mYear= calendar.get(Calendar.YEAR);
            int mMonth = calendar.get(Calendar.MONTH);
            int mDay = calendar.get(Calendar.DAY_OF_MONTH);
            DatePickerDialog.OnDateSetListener dateSetListener =new DatePickerDialog.OnDateSetListener() {
                @Override
                public void onDateSet(DatePicker view, int year, int month, int dayOfMonth) {
                    DateTime dateTime = new DateTime(year, month + 1, dayOfMonth,0,0);
                    selectedDate = dateTime.toDate();
                    edtExpiredDate.setText(Common.getShortDateString(selectedDate));

                }
            };

            datePickerDialog =  new DatePickerDialog(this, dateSetListener, mYear,mMonth,mDay);
        }


    }

    @Override
    public void onClick(View v) {
        switch (v.getId()){
            case R.id.btnMovingManualMutation:
                try {
                    if(validateInputTextAlreadyFilled()){
                        manualMutationData=prepareManualMutationData();
                        finishMutationManualTaskThread(manualMutationData);
                    }

                } catch (Exception e) {
                    dismissProgressDialog();
                }
                break;
        }
    }

    @Override
    public void onBackPressed() {
        showInfoDialog(getResources().getString(R.string.common_information_title),getResources().getString(R.string.warning_must_complete_the_task));
    }

    private void init()throws Exception{
        initObjectManager();
        initComponent();
        initToolbar();
    }

    private void initObjectManager()throws Exception{
        movingTaskManager = new MovingTaskManager(getApplicationContext());
        manualMutationManager = new ManualMutationManager(getApplicationContext());
    }

    private void initToolbar() {
        toolbar = findViewById(R.id.toolbarMovingManualMutationStart);
        toolbar.setTitle(getResources().getString(R.string.toolbar_manual_mutation_order));
        setSupportActionBar(toolbar);
        if(getSupportActionBar() != null)
            getSupportActionBar().setDisplayHomeAsUpEnabled(true);
    }

    private void initComponent(){
        edtProductId=findViewById(R.id.edtProductId);
        edtQty=findViewById(R.id.edtQty);
        edtSoucePallete=findViewById(R.id.edtSoucePallete);
        edtSouceBin=findViewById(R.id.edtSouceBin);
        edtDestPallete=findViewById(R.id.edtDestPallete);
        edtDestBin=findViewById(R.id.edtDestBin);
        edtExpiredDate = findViewById(R.id.edtExpiredDate);
        btnMovingManualMutation=findViewById(R.id.btnMovingManualMutation);
        btnExpiredDate = findViewById(R.id.btnExpiredDate);
        btnExpiredDate.setOnClickListener(this);
        btnMovingManualMutation.setOnClickListener(this);
        initDatePicker();

    }

    private boolean validateInputTextAlreadyFilled(){
        String warning = getResources().getString(R.string.warning_data_should_be_filled);
        boolean hasil=true;
        if(edtProductId.getText().toString().equals("")){
            edtProductId.requestFocus();
            edtProductId.setError(warning);
            hasil= false;
        }
        if(edtQty.getText().toString().equals("")){
            edtQty.requestFocus();
            edtQty.setError(warning);
            hasil= false;
        }
        if(edtSoucePallete.getText().toString().equals("")){
            edtSoucePallete.requestFocus();
            edtSoucePallete.setError(warning);
            hasil= false;
        }
        if(edtSouceBin.getText().toString().equals("")){
            edtSouceBin.requestFocus();
            edtSouceBin.setError(warning);
            hasil= false;
        }
        if(edtDestPallete.getText().toString().equals("")){
            edtDestPallete.requestFocus();
            edtDestPallete.setError(warning);
            hasil= false;
        }
        if(edtDestBin.getText().toString().equals("")){
            edtDestBin.requestFocus();
            edtDestBin.setError(warning);
            hasil= false;
        }
        if(edtExpiredDate.getText().toString().isEmpty()){
            edtExpiredDate.requestFocus();
            edtExpiredDate.setError(warning);
            hasil= false;
        }

        return hasil;

    }

    private ManualMutationData prepareManualMutationData() throws Exception{
        ManualMutationData preparedData=new ManualMutationData();
        Date date;
        SimpleDateFormat sf = new SimpleDateFormat(SHORT_DATE_PATTERN, Locale.getDefault());
        date = sf.parse (edtExpiredDate.getText().toString());
        preparedData.recOrderNo=" ";
        preparedData.trxTypeId="DocumentNumber.NO_1";
        preparedData.docStatus="1";
        preparedData.created=new Date();
        preparedData.lastUpdated=new Date();
        preparedData.orderType=2;
        preparedData.operator=1;
        preparedData.productId=edtProductId.getText().toString();
        preparedData.qty=Double.parseDouble(edtQty.getText().toString());
        preparedData.sourcePalletNo=edtSoucePallete.getText().toString();
        preparedData.sourceBinId=edtSouceBin.getText().toString();
        preparedData.destPalletNo=edtDestPallete.getText().toString();
        preparedData.destBinId=edtDestBin.getText().toString();
        preparedData.uOMId="Pcs";
        preparedData.clientId="BKS.01";
        preparedData.clientLocationId="BKS.01";
        preparedData.expiredDate=date;
        preparedData.refDocUri=" ";
        preparedData.finished=0;

        return preparedData;
    }

    private void finishMutationManualTaskThread(ManualMutationData manualMutationData) throws Exception{
        ThreadStart(new ThreadHandler<Boolean>() {

            @Override
            public void onPrepare() throws Exception {
                startProgressDialog(getString(R.string.msg_finish_manual_mutation), ProgressType.SPINNER);
            }

            @Override
            public Boolean onBackground() throws Exception {
                manualMutationManager.CreateManualMutationOrderToServer(manualMutationData);
                return true;
            }

            @Override
            public void onError(Exception e) {
                dismissProgressDialog();
                showErrorDialog(e);

            }

            @Override
            public void onSuccess(Boolean data) throws Exception {
                showInfoSnackBar(getResources().getString(R.string.msg_manual_mutation_order_has_saved));
                clearUI();
            }

            @Override
            public void onFinish() {
                dismissProgressDialog();
            }
        });
    }

    private void clearUI(){
        edtProductId.setText("");
        edtQty.setText("");
        edtSoucePallete.setText("");
        edtSouceBin.setText("");
        edtDestPallete.setText("");
        edtDestBin.setText("");
        edtExpiredDate.setText("");
    }

}
