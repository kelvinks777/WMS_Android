package com.gin.wms.warehouse.operator.Banded;

import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.support.v7.widget.GridLayoutManager;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

import com.gin.ngemart.baseui.adapter.NgemartRecyclerView;
import com.gin.ngemart.baseui.adapter.NgemartRecyclerViewAdapter;
import com.gin.wms.manager.ProcessingTaskManager;
import com.gin.wms.manager.db.data.BandedData;
import com.gin.wms.manager.db.data.CompUomItemData;
import com.gin.wms.manager.db.data.MasterBandedData;
import com.gin.wms.manager.db.data.ProcessingTaskData;
import com.gin.wms.manager.db.data.ProcessingTaskItemResultData;
import com.gin.wms.manager.db.data.StockLocationData;
import com.gin.wms.manager.db.data.helper.CompUomHelper;
import com.gin.wms.warehouse.R;
import com.gin.wms.warehouse.base.WarehouseActivity;
import com.gin.wms.warehouse.operator.TaskSwitcherActivity;
import com.gin.wms.warehouse.warehouseProblem.WarehouseProblemReportActivity;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;


public class ProcessingTaskCheckActivity extends WarehouseActivity implements View.OnClickListener {

    private ProcessingTaskManager processingTaskManager;
    private ProcessingTaskData processingTaskData;
    private BandedData bandedData;
    private final List<MasterBandedData> masterBandedData = new ArrayList<>();
    private List<MasterBandedData> masterBandedDataList = new ArrayList<>();
    private List<StockLocationData> stockLocationData = new ArrayList<>();
    private CompUomItemData compUomItemData;

    private TextView tvBandedId, tvQtyNeeded, tvQtyRemaining;
    private int qtyRemaining;

    public static final String PROCESSING_TASK_CODE = "processingTaskData";
    public static final String BANDED_ORDER_DATA_CODE = "bandedOrderData";
    public static final String MASTER_BANDED_DATA_CODE = "masterBandedData";
    public static final String STOCK_LOCATION_DATA_CODE = "stockLocationData";
    public static final String ARG_ACTIVITY_COME_FROM = "activityComeFrom";
    public static final String ARG_FROM_ACTIVITY_RESULT_INPUT = "activityResultInput";
    public static final String ARG_FROM_ACTIVITY_TASK_SWITCHER = "activityTaskSwitcher";
    public static final String ARG_QTY_REMAINING = "qtyRemaining";

    private NgemartRecyclerViewAdapter<List<MasterBandedData>> adapter;
    private NgemartRecyclerView rvBandedInfoList;

    //region Override function
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        try{
            super.onCreate(savedInstanceState);
            setContentView(R.layout.activity_processing_task_check);
            processingTaskManager = new ProcessingTaskManager(this);
            getIntentData();
            initComponent();
            showDataToUI();
        } catch (Exception e) {
            showErrorDialog(e);
        }
    }

    @Override
    public void onClick(View v) {
        try{
            switch (v.getId()){
                case R.id.btnInfoBanded:
                    ShowBandedInfo();
                    break;
                case R.id.btnInputBanded:
                    if(tvQtyRemaining.getText().toString().equals("0 " + compUomItemData.uomId)){
                        showInfoDialog(getResources().getString(R.string.info), getResources().getString(R.string.msg_banded_data_already_available));
                    } else {
                        showInputCheckData();
                    }
                    break;
                case R.id.btnReport:
                    Intent intent = new Intent(this, WarehouseProblemReportActivity.class);
                    startActivity(intent);
                    break;
                case R.id.btnFinishBanded:
                    if(!tvQtyRemaining.getText().toString().equals("0 " + compUomItemData.uomId)) {
                        showInfoDialog(getResources().getString(R.string.info), getResources().getString(R.string.msg_you_have_not_completed_task));
                    } else {
                        DialogInterface.OnClickListener okListener = FinishTaskThread();
                        showAskDialog(getString(R.string.common_confirmation_title), getResources().getString(R.string.msg_ask_save_processing_task), okListener, null);
                    }
                    break;
            }
        }catch (Exception e){
            showErrorDialog(e);
        }
    }

    @Override
    public void onBackPressed() {
        showInfoDialog(getResources().getString(R.string.common_information_title),getResources().getString(R.string.warning_must_complete_the_task));
    }
    //endregion

    //region Init
    private void initComponent() {
        Button btnFinishBanded = findViewById(R.id.btnFinishBanded);
        Button btnInfo = findViewById(R.id.btnInfoBanded);
        Button btnInput = findViewById(R.id.btnInputBanded);
        Button btnReport = findViewById(R.id.btnReport);
        tvBandedId = findViewById(R.id.tvBandedId);
        tvQtyNeeded = findViewById(R.id.tvQtyNeeded);
        tvQtyRemaining = findViewById(R.id.tvQtyRemaining);
        rvBandedInfoList = findViewById(R.id.bandedInfoList);
        rvBandedInfoList.setVisibility(View.INVISIBLE);
        btnInfo.setOnClickListener(this);
        btnInput.setOnClickListener(this);
        btnReport.setOnClickListener(this);
        btnFinishBanded.setOnClickListener(this);

        initRecyclerView();
        initToolbar();
    }

    private void initToolbar() {
        android.support.v7.widget.Toolbar toolbar = findViewById(R.id.toolbarBanded);
        toolbar.setTitle(getResources().getString(R.string.title_check_banded_result));
        setSupportActionBar(toolbar);

        if(getSupportActionBar() != null)
            getSupportActionBar().setDisplayHomeAsUpEnabled(false);
    }

    private void initRecyclerView() {
        int numColumn = 1;

        adapter = new NgemartRecyclerViewAdapter(this, R.layout.card_banded_info, masterBandedData);
        adapter.setHasStableIds(true);
        rvBandedInfoList.setAdapter(adapter);

        GridLayoutManager gridLayoutManager = new GridLayoutManager(this, numColumn, GridLayoutManager.VERTICAL, false);
        rvBandedInfoList.setHasFixedSize(true);
        rvBandedInfoList.SetDefaultDecoration();
        rvBandedInfoList.setLayoutManager(gridLayoutManager);
    }
    //endregion

    //region Other Function

    private void ShowBandedInfo(){
        rvBandedInfoList.setVisibility(View.VISIBLE);
    }

    private void getIntentData() {
        Bundle bundle = getIntent().getExtras();
        Intent intent = getIntent();

        if (bundle != null) {
            processingTaskData = (ProcessingTaskData) bundle.getSerializable(PROCESSING_TASK_CODE);
            bandedData = (BandedData) bundle.getSerializable(BANDED_ORDER_DATA_CODE);
            masterBandedDataList = (List<MasterBandedData>) bundle.getSerializable(MASTER_BANDED_DATA_CODE);
            stockLocationData = (List<StockLocationData>) bundle.getSerializable(STOCK_LOCATION_DATA_CODE);

            if (intent != null) {
                String activity = intent.getStringExtra(ARG_ACTIVITY_COME_FROM);
                if (activity.equals(ARG_FROM_ACTIVITY_RESULT_INPUT)) {
                    qtyRemaining = (int)bundle.getDouble(ARG_QTY_REMAINING);
                }
            }
        }
    }

    private void showDataToUI() throws Exception{
        int qty = 0;
        int qtyTotal = (int) bandedData.bandedItemDataList.get(0).qty;
        List<ProcessingTaskItemResultData> itemResultDataList;
        CompUomHelper helper = new CompUomHelper(processingTaskData.itemProduct.getCompUom());
        compUomItemData = helper.getUomTail();

        itemResultDataList = processingTaskManager.getItemResultDataFromLocal(processingTaskData.processingTaskId, bandedData.bandedItemDataList.get(0).productId);
        qtyRemaining = (int) bandedData.bandedItemDataList.get(0).qty;

        if (itemResultDataList != null && itemResultDataList.size() != 0) {
            for(ProcessingTaskItemResultData resultData : itemResultDataList){
                qty = qty + (int) resultData.qty;
            }
            qtyRemaining = qtyRemaining - qty;
        }

        tvBandedId.setText(bandedData.bandedItemDataList.get(0).productId);
        tvQtyNeeded.setText(String.valueOf(qtyTotal + " " + compUomItemData.uomId));
        tvQtyRemaining.setText(String.valueOf(qtyRemaining + " " + compUomItemData.uomId));

        masterBandedData.clear();
        masterBandedData.addAll(masterBandedDataList);

        adapter.notifyDataSetChanged();
    }

    private void showInputCheckData(){
        Intent intent = new Intent(this, ProcessingTaskResultInputActivity.class);
        Bundle bundle = new Bundle();
        bundle.putSerializable(BANDED_ORDER_DATA_CODE, bandedData);
        bundle.putSerializable(PROCESSING_TASK_CODE, processingTaskData);
        bundle.putSerializable(MASTER_BANDED_DATA_CODE, (Serializable) masterBandedDataList);
        bundle.putSerializable(STOCK_LOCATION_DATA_CODE, (Serializable) stockLocationData);
        bundle.putDouble(ARG_QTY_REMAINING, qtyRemaining);
        intent.putExtras(bundle);
        startActivity(intent);
    }

    //endregion

    //region Thread
    private void FinishCheckTask() throws Exception {
        ThreadStart(new ThreadHandler<Boolean>() {
            @Override
            public void onPrepare() throws Exception {
                startProgressDialog(getString(R.string.common_progress_content), ProgressType.SPINNER);
            }

            @Override
            public Boolean onBackground() throws Exception {
                processingTaskManager.finishProcessingTask(processingTaskData);
                return true;
            }

            @Override
            public void onError(Exception e) {
                dismissProgressDialog();
                showErrorDialog(e);
            }

            @Override
            public void onSuccess(Boolean data) throws Exception {
                moveToTaskSwitcherActivity();
            }

            @Override
            public void onFinish() {
                dismissProgressDialog();
            }
        });
    }

    private void moveToTaskSwitcherActivity() {
        Intent intent = new Intent(this, TaskSwitcherActivity.class);
        intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
        startActivity(intent);
        finish();
    }

    private DialogInterface.OnClickListener FinishTaskThread(){
        return new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                try {
                    FinishCheckTask();
                } catch (Exception e) {
                    showErrorDialog(e);
                }
            }
        };
    }
    //endregion
}
