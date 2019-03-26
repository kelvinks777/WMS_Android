package com.gin.wms.warehouse.operator.DockingAndStaging;

import android.app.Activity;
import android.content.Intent;
import android.support.v4.app.DialogFragment;
import android.os.Bundle;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.Toolbar;
import android.text.InputType;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.TextView;

import com.bosnet.ngemart.libgen.barcode_scanner.integration.IntentIntegrator;
import com.bosnet.ngemart.libgen.barcode_scanner.integration.IntentResult;
import com.gin.ngemart.baseui.adapter.NgemartRecyclerView;
import com.gin.ngemart.baseui.adapter.NgemartRecyclerViewAdapter;
import com.gin.wms.manager.MovingTaskManager;
import com.gin.wms.manager.StockLocationManager;
import com.gin.wms.manager.db.data.CheckerTaskData;
import com.gin.wms.manager.db.data.MovingTaskData;
import com.gin.wms.manager.db.data.StockLocationData;
import com.gin.wms.manager.db.data.enums.RefDocUriEnum;
import com.gin.wms.warehouse.R;
import com.gin.wms.warehouse.base.WarehouseActivity;
import com.gin.wms.warehouse.component.SingleInputDialogFragment;
import com.gin.wms.warehouse.operator.TaskSwitcherActivity;
import com.gin.wms.warehouse.warehouseProblem.WarehouseProblemReportActivity;

import java.util.ArrayList;
import java.util.List;

public class MoveToDockingOrStagingDetailActivity extends WarehouseActivity
        implements View.OnClickListener, SingleInputDialogFragment.InputManualInterface{
    private NgemartRecyclerView recyclerView;
    private NgemartRecyclerViewAdapter<List<StockLocationData>> adapter;
    private final List<StockLocationData> stockLocationDataList = new ArrayList<>();
    private MovingTaskManager movingTaskManager;
    private StockLocationManager stockLocationManager;
    private ImageButton btnScanDestDocking;
    private Button btnFinishTaskAndBackToDocking, btnReport;
    private TextView tvSourceId, tvDestId, tvPalletNumber, tvDest;
    private EditText txtDestination;
    private MovingTaskData movingTaskData;
    private CheckerTaskData checkerTaskData;
    private static int SCAN_STAGING_DEST_DOCKING_ID_CODE = 4321;
    private static int DIALOG_FRAGMENT_TYPE_CODE_FOR_PALLET = 555;

    //region override function

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        try{
            super.onCreate(savedInstanceState);
            setContentView(R.layout.activity_move_to_docking_or_staging_detail);
            getDataFromIntent();
            init();
            showDataToUiThread();
        }catch (Exception e){
            showErrorDialog(e);
        }
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        try{
            IntentResult result = IntentIntegrator.parseActivityResult(requestCode, resultCode, data);
            if(result != null){
                if(resultCode == Activity.RESULT_OK && requestCode == SCAN_STAGING_DEST_DOCKING_ID_CODE)
                    txtDestination.setText(result.getContents());
            }
        }catch (Exception e){
            showErrorDialog(e);
        }
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()){
            case R.id.txtDestDocking:
                DialogFragment dialogFragment;
                if(isRefDocUriReceiving()){
                    dialogFragment = SingleInputDialogFragment.newInstance(DIALOG_FRAGMENT_TYPE_CODE_FOR_PALLET,"Staging", txtDestination.getText().toString());
                } else {
                    dialogFragment = SingleInputDialogFragment.newInstance(DIALOG_FRAGMENT_TYPE_CODE_FOR_PALLET,"Docking", txtDestination.getText().toString());
                }
                dialogFragment.show(getSupportFragmentManager(), "");
                break;

            case R.id.btnScanDestDocking:
                LaunchBarcodeScanner(SCAN_STAGING_DEST_DOCKING_ID_CODE);
                break;

            case R.id.btnReport:
                Intent intent = new Intent(this, WarehouseProblemReportActivity.class);
                startActivity(intent);
                break;

            case R.id.btnFinishAndBackToDocking:
                validateToFinishTaskAndBackToDockingOrStaging();
                break;
        }
    }

    @Override
    public void onBackPressed() {
        showInfoDialog(getResources().getString(R.string.common_information_title),getResources().getString(R.string.warning_must_complete_the_task));
    }

    @Override
    public void onTextInput(int typeCode, String inputText) {
        try{
            if(typeCode == DIALOG_FRAGMENT_TYPE_CODE_FOR_PALLET)
                txtDestination.setText(inputText);
            else
                throw new ClassNotFoundException("typeCode is not match !");
        }catch (Exception e){
            showErrorDialog(e);
        }
    }

    //endregion

    //region Init

    private void initObjectManager()throws Exception{
        movingTaskManager = new MovingTaskManager(getApplicationContext());
        stockLocationManager = new StockLocationManager(getApplicationContext());
    }

    private void init()throws Exception{
        initObjectManager();
        initComponent();
        initToolbar();
        initComponentHandler();
    }

    private void initComponent(){
        btnScanDestDocking = findViewById(R.id.btnScanDestDocking);
        btnFinishTaskAndBackToDocking = findViewById(R.id.btnFinishAndBackToDocking);
        btnReport = findViewById(R.id.btnReport);
        txtDestination = findViewById(R.id.txtDestDocking);
        recyclerView = findViewById(R.id.rvFinishDocking);
        tvSourceId = findViewById(R.id.tvStagingId);
        tvDestId = findViewById(R.id.tvDockingId);
        tvPalletNumber = findViewById(R.id.tvPalletNumber);
        tvDest = findViewById(R.id.txtDest);

        if(isRefDocUriReceiving()){
            btnFinishTaskAndBackToDocking.setText(getResources().getString(R.string.btn_moving_to_staging));
            tvDest.setText(getResources().getString(R.string.title_putaway_with_staging));
        }
    }

    private void initToolbar() {
        Toolbar toolbar;
        toolbar = findViewById(R.id.toolbarMoveToDocking);
        if(isRefDocUriReceiving()){
            toolbar.setTitle(getResources().getString(R.string.title_moving_to_staging));
        } else {
            toolbar.setTitle(getResources().getString(R.string.title_moving_to_docking));
        }
    }

    private void initComponentHandler(){
        btnScanDestDocking.setOnClickListener(this);
        btnFinishTaskAndBackToDocking.setOnClickListener(this);
        txtDestination.setOnClickListener(this);
        btnReport.setOnClickListener(this);
    }

    private void initAdapter(){
        adapter = new NgemartRecyclerViewAdapter(this, R.layout.card_move_to_docking_or_staging_item, stockLocationDataList);
        adapter.setHasStableIds(true);
        recyclerView.setAdapter(adapter);
    }

    private void initRecyclerView(){
        int numColumn = 1;
        GridLayoutManager gridLayoutManager = new GridLayoutManager(this, numColumn, GridLayoutManager.VERTICAL, false);
        recyclerView.setHasFixedSize(true);
        recyclerView.SetDefaultDecoration();
        recyclerView.setLayoutManager(gridLayoutManager);
    }

    //endregion

    //region other function

    private void validateToFinishTaskAndBackToDockingOrStaging(){
        String dockingOrStagingId = txtDestination.getText().toString();
        if(dockingOrStagingId.equals("")){
            showErrorDialog(getResources().getString(R.string.error_dest_cannot_empty));
        }else{
            if(!validateDestLocationId(dockingOrStagingId))
                showErrorDialog(getResources().getString(R.string.error_destination_not_match));
            else
                showFinishTaskAndBackToDockingOrStagingDialog();
        }
    }

    private void getDataFromIntent(){
        Bundle bundle = getIntent().getExtras();
        if(bundle != null){
            movingTaskData = (MovingTaskData) bundle.getSerializable(MoveToDockingOrStagingActivity.MOVING_TASK_STATE);
            checkerTaskData = (CheckerTaskData) bundle.getSerializable(MoveToDockingOrStagingActivity.CHECKER_TASK_STATE);
        }
    }

    private boolean isRefDocUriReceiving() {
        if(checkerTaskData != null && checkerTaskData.refDocUri.equals(RefDocUriEnum.RECEIVING.getValue())) {
            return true;
        }
        return false;
    }

    private Boolean validateDestLocationId(String destLocation){
        String dockingOrStagingId;
        String IdFromInput = destLocation.toLowerCase();

        if(isRefDocUriReceiving()){
            dockingOrStagingId = movingTaskData.destItemList.get(0).destBinId.toLowerCase();
        } else {
            dockingOrStagingId = movingTaskData.dockingId.toLowerCase();
        }

        return dockingOrStagingId.toLowerCase().equals(IdFromInput.toLowerCase());
    }

    private void showFinishTaskAndBackToDockingOrStagingDialog(){
        showAskDialog(getResources().getString(R.string.ask_finish_moving_now_title),getResources().getString(R.string.ask_finish_moving_and_back_to_docking_body),
                (dialog, which) -> finishTaskThread(movingTaskData.movingId),
                (dialog, which) -> dialog.dismiss()
        );
    }

    private void moveToTaskSwitcher(){
        Intent intent = new Intent(this, TaskSwitcherActivity.class);
        intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
        startActivity(intent);
        finish();
    }

    //endregion

    //region thread

    private void showDataToUiThread(){
        ThreadStart(new ThreadHandler<List<StockLocationData>>() {
            @Override
            public void onPrepare() throws Exception {
                startProgressDialog(getString(R.string.progress_content_load), ProgressType.SPINNER);
            }

            @Override
            public List<StockLocationData> onBackground() throws Exception {
                List<StockLocationData> stockList = new ArrayList<>();
                if(movingTaskData != null) {
                    if(isRefDocUriReceiving()){
                        stockList = stockLocationManager.getStockLocationByBinPallet(movingTaskData.dockingId, movingTaskData.palletNo);
                    } else {
                        stockList = stockLocationManager.getStockLocationByBinPallet(movingTaskData.stagingBinId, movingTaskData.palletNo);
                    }
                }

                return stockList;
            }

            @Override
            public void onError(Exception e) {
                dismissProgressDialog();
                showErrorDialog(e);
            }

            @Override
            public void onSuccess(List<StockLocationData> resultList) throws Exception {
                showMovingTaskToUi();
                showStockLocationToUi(resultList);
            }

            @Override
            public void onFinish() {
                dismissProgressDialog();
            }

            private void showStockLocationToUi(List<StockLocationData> resultList){
                stockLocationDataList.clear();
                stockLocationDataList.addAll(resultList);
                initAdapter();
                initRecyclerView();
                adapter.notifyDataSetChanged();
            }

            private void showMovingTaskToUi(){
                if(isRefDocUriReceiving()){
                    tvSourceId.setText(movingTaskData.sourceItemList.get(0).sourceBinId);
                    tvDestId.setText(movingTaskData.destItemList.get(0).destBinId);
                } else {
                    tvSourceId.setText(movingTaskData.stagingBinId);
                    tvDestId.setText(movingTaskData.dockingId);
                }
                tvPalletNumber.setText(movingTaskData.palletNo);
            }
        });
    }

    private void finishTaskThread(String movingTaskId){
        ThreadStart(new ThreadHandler<Boolean>() {
            @Override
            public void onPrepare() throws Exception {
                startProgressDialog(getString(R.string.progress_finish_moving_task), ProgressType.SPINNER);
            }

            @Override
            public Boolean onBackground() throws Exception {
                List<StockLocationData> dataList = new ArrayList<>();
                if(isRefDocUriReceiving()){
                    dataList = stockLocationManager.getStockLocationByBinId(checkerTaskData.dockingIds);
                    if(dataList.size() == 1){
                        movingTaskManager.finishMovingTaskAndBackToUnloadingTask(movingTaskId);
                    } else {
                        movingTaskManager.finishMovingTaskAndBackToMovingToStaging(movingTaskId);
                    }
                } else {
                    dataList = stockLocationManager.getStockLocationByBinId(checkerTaskData.stagingId);
                    if(dataList.size() == 1){
                        movingTaskManager.finishMovingTaskAndBackToLoadingTask(movingTaskId);
                    }
                    else{
                        movingTaskManager.finishMovingTaskAndBackToMovingToDockingTask(movingTaskId);
                    }
                }
                movingTaskManager.deleteLocalMovingTask();
                return true;
            }

            @Override
            public void onError(Exception e) {
                try{
                    movingTaskManager.saveMovingTaskData(movingTaskData);
                    dismissProgressDialog();
                    showErrorDialog(e);
                }catch (Exception ex){
                    showErrorDialog(ex);
                }
            }

            @Override
            public void onSuccess(Boolean data) throws Exception {
                moveToTaskSwitcher();
            }

            @Override
            public void onFinish() {
                dismissProgressDialog();
            }
        });
    }

    //endregion
}
