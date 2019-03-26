package com.gin.wms.warehouse.operator.Moving;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.DialogFragment;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.Toolbar;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.TextView;

import com.bosnet.ngemart.libgen.barcode_scanner.integration.IntentIntegrator;
import com.bosnet.ngemart.libgen.barcode_scanner.integration.IntentResult;
import com.gin.ngemart.baseui.adapter.NgemartRecyclerView;
import com.gin.ngemart.baseui.adapter.NgemartRecyclerViewAdapter;
import com.gin.wms.manager.MovingTaskManager;
import com.gin.wms.manager.db.data.MovingTaskData;
import com.gin.wms.manager.db.data.MovingTaskDestItemData;
import com.gin.wms.manager.db.data.enums.MutationTypeEnum;
import com.gin.wms.manager.db.data.enums.RefDocUriEnum;
import com.gin.wms.manager.db.data.enums.TaskTypeEnum;
import com.gin.wms.warehouse.R;
import com.gin.wms.warehouse.base.WarehouseActivity;
import com.gin.wms.warehouse.component.SingleInputDialogFragment;
import com.gin.wms.warehouse.operator.TaskSwitcherActivity;
import com.gin.wms.warehouse.warehouseProblem.WarehouseProblemReportActivity;

import java.util.ArrayList;
import java.util.List;

public class MovingFinishActivity extends WarehouseActivity implements
        View.OnClickListener, SingleInputDialogFragment.InputManualInterface {
    private MovingTaskData movingTaskData;
    private TextView tvPalletNo, tvDestNo;
    private static int SCAN_PALLET_ID_CODE = 3456;
    private static int SCAN_DEST_ID_CODE = 6543;
    private static int TAKE_PALLET_ID_CODE = 7654;
    private NgemartRecyclerView recyclerView;
    private NgemartRecyclerViewAdapter<List<MovingTaskDestItemData>> adapter;
    private final List<MovingTaskDestItemData> destItemDataList = new ArrayList<>();
    private MovingTaskManager movingTaskManager;
    private Toolbar toolbar;
    private MovingTaskDestItemData destItemData;
    private String mutationType;

    //region override functions

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        try{
            super.onCreate(savedInstanceState);
            setContentView(R.layout.activity_moving_finish);
            getDataFromIntent();
            isMutationFromBadArea();
            init();
            showDataToUi();
        }catch (Exception e){
            showErrorDialog(e);
        }
    }

    @Override
    public void onClick(View v) {
        String PALLET_TITLE_DIALOG = getResources().getString(R.string.pallet_id);
        String DEST_TITLE_DIALOG = getResources().getString(R.string.dest_id);

        switch (v.getId()){
            case R.id.tvPalletNo:
                showInputFragment(SCAN_PALLET_ID_CODE, PALLET_TITLE_DIALOG, tvPalletNo.getText().toString());
                break;
            case R.id.tvDestNo:
                showInputFragment(SCAN_DEST_ID_CODE, DEST_TITLE_DIALOG, tvDestNo.getText().toString());
                break;
            case R.id.btnScanPallet:
                LaunchBarcodeScanner(SCAN_PALLET_ID_CODE);
                break;
            case R.id.btnScanDest:
                LaunchBarcodeScanner(SCAN_DEST_ID_CODE);
                break;
            case R.id.btnTakeNewPallet:
                showInputFragment(TAKE_PALLET_ID_CODE, PALLET_TITLE_DIALOG, tvPalletNo.getText().toString());
                break;
            case R.id.btnBack:
                if(validateInputTextAlreadyFilled())
                    showConfirmationToFinishTask();
                break;
            case R.id.btnReport:
                Intent intent = new Intent(MovingFinishActivity.this, WarehouseProblemReportActivity.class);
                startActivity(intent);
                break;
            case R.id.btnFinishReplenish:
                if(validateInputTextAlreadyFilled())
                    showConfirmationToFinishReplenish();
                break;
        }
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        try{
            IntentResult result = IntentIntegrator.parseActivityResult(requestCode, resultCode, data);
            if(result != null) {
                if (resultCode == Activity.RESULT_OK) {
                    validateInputAndShowToUi(requestCode, result.getContents());
                }
            }
        }catch (Exception e){
            showErrorDialog(e);
        }
    }

    @Override
    public void onBackPressed() {
        if(movingTaskData.getTaskType() != TaskTypeEnum.INTERNAL_MOVEMENT){
            moveToTaskSwitcherActivity();
        } else {
            backToProductList();
        }
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()){
            case android.R.id.home:
                if(movingTaskData.getTaskType() != TaskTypeEnum.INTERNAL_MOVEMENT){
                    moveToTaskSwitcherActivity();
                } else {
                    backToProductList();
                }
                break;
        }
        return super.onOptionsItemSelected(item);
    }

    @Override
    public void onTextInput(int typeCode, String inputText) {
        try{
            validateInputAndShowToUi(typeCode, inputText);
        }catch (Exception e){
            showErrorDialog(e);
        }
    }

    //endregion

    //region init

    private void init()throws Exception{
        initToolbar();
        initObjectManager();
        initComponent();
        initAdapter();
        initRecyclerAdapter();
    }

    private void initObjectManager()throws Exception{
        movingTaskManager = new MovingTaskManager(getApplicationContext());
    }

    private void initComponent(){
        Button btnProcess = findViewById(R.id.btnFinishReplenish);
        Button btnTakePallet = findViewById(R.id.btnTakeNewPallet);
        Button btnBack = findViewById(R.id.btnBack);
        Button btnReport = findViewById(R.id.btnReport);
        ImageButton btnScanPallet = findViewById(R.id.btnScanPallet);
        ImageButton btnScanSource = findViewById(R.id.btnScanDest);
        tvPalletNo = findViewById(R.id.tvPalletNo);
        tvDestNo = findViewById(R.id.tvDestNo);
        recyclerView = findViewById(R.id.rvFinishReplenish);

        setButtonVisibility(btnTakePallet, btnBack, btnProcess, btnReport);

        btnTakePallet.setOnClickListener(this);
        btnProcess.setOnClickListener(this);
        btnBack.setOnClickListener(this);
        btnReport.setOnClickListener(this);
        btnScanPallet.setOnClickListener(this);
        btnScanSource.setOnClickListener(this);
        tvPalletNo.setOnClickListener(this);
        tvDestNo.setOnClickListener(this);
    }

    private void initToolbar() {
        toolbar = findViewById(R.id.toolbarReplenishFinish);
        toolbar.setTitle(getResources().getString(R.string.title_replenish_finish));
        setSupportActionBar(toolbar);

        if(getSupportActionBar() != null)
            getSupportActionBar().setDisplayHomeAsUpEnabled(true);
    }

    private void initAdapter(){
        adapter = new NgemartRecyclerViewAdapter(this, R.layout.card_moving_dest_item, destItemDataList);
        adapter.setHasStableIds(true);
        recyclerView.setAdapter(adapter);
    }

    private void initRecyclerAdapter(){
        int numColumn = 1;
        GridLayoutManager gridLayoutManager = new GridLayoutManager(this, numColumn, GridLayoutManager.VERTICAL, false);
        recyclerView.setHasFixedSize(true);
        recyclerView.SetDefaultDecoration();
        recyclerView.setLayoutManager(gridLayoutManager);
    }

    //endregion

    //region other functions

    private void setButtonVisibility(Button btnTakePallet, Button btnBack, Button btnProcess, Button btnReport) {
        if (RefDocUriEnum.MUTATION.getValue().equals(movingTaskData.docRefUri)) {
            if(!isMutationFromBadArea()){
                btnTakePallet.setVisibility(View.GONE);
                btnBack.setVisibility(View.VISIBLE);
                btnProcess.setVisibility(View.GONE);
                btnReport.setVisibility(View.GONE);
            } else {
                btnTakePallet.setVisibility(View.VISIBLE);
                btnBack.setVisibility(View.VISIBLE);
                btnProcess.setVisibility(View.GONE);
                btnReport.setVisibility(View.GONE);
            }
        }
        else if (movingTaskData.getTaskType() == TaskTypeEnum.INTERNAL_MOVEMENT) {
            btnTakePallet.setVisibility(View.GONE);
            btnBack.setVisibility(View.VISIBLE);
            btnProcess.setVisibility(View.GONE);
            btnReport.setVisibility(View.GONE);
        }
        else {
            btnTakePallet.setVisibility(View.GONE);
            btnBack.setVisibility(View.GONE);
            btnReport.setVisibility(View.VISIBLE);
            btnProcess.setVisibility(View.VISIBLE);
        }
    }

    private boolean isMutationFromBadArea() {
        if(mutationType != null && mutationType.equals(MutationTypeEnum.BADTOGOOD.toString())){
            return true;
        }
        return false;
    }

    private void showInputFragment(int typeCode, String dialogTitle, String previousValue){
        DialogFragment dialogFragment;
        dialogFragment = SingleInputDialogFragment.newInstance(typeCode, dialogTitle, previousValue);
        dialogFragment.show(getSupportFragmentManager(), "");
    }

    private void validateInputAndShowToUi(int typeCode, String inputText) throws Exception{
        if(typeCode == SCAN_PALLET_ID_CODE){
            if(validateInputText(typeCode, inputText)){
                tvPalletNo.setText(inputText);
            } else{
                String pallet_warning = getResources().getString(R.string.pallet_id_is_not_match);
                showErrorDialog(pallet_warning);
                tvPalletNo.setText("");
            }
        } else if (typeCode == SCAN_DEST_ID_CODE){
            if(validateInputText(typeCode, inputText)){
                tvDestNo.setText(inputText);
            } else{
                String dest_warning = getResources().getString(R.string.dest_id_is_not_match);
                showErrorDialog(dest_warning);
                tvDestNo.setText("");
            }
        } else if (typeCode == TAKE_PALLET_ID_CODE){
            takeNewPalletThread(inputText);
        }
        else{
            String default_warning = getResources().getString(R.string.typeCode_is_not_match);
            throw new ClassNotFoundException(default_warning);
        }
    }

    private boolean validateInputText(int typeCode, String inputText){
        boolean isCorrect = false;
        if(typeCode == SCAN_PALLET_ID_CODE){
            if(movingTaskData.getTaskType() == TaskTypeEnum.INTERNAL_MOVEMENT) {
                if(inputText.toLowerCase().equals(destItemData.palletNo.toLowerCase()))
                    isCorrect = true;
            } else if (inputText.toLowerCase().equals(destItemDataList.get(0).palletNo.toLowerCase())){
                isCorrect = true;
            }
        } else if (typeCode == SCAN_DEST_ID_CODE){
            if(movingTaskData.getTaskType() == TaskTypeEnum.INTERNAL_MOVEMENT) {
                if(inputText.toLowerCase().equals(destItemData.destBinId.toLowerCase()))
                    isCorrect = true;
            } else if (inputText.toLowerCase().equals(destItemDataList.get(0).destBinId.toLowerCase())){
                isCorrect = true;
            }
        }
        return isCorrect;
    }

    private boolean validateInputTextAlreadyFilled(){
        String warning = getResources().getString(R.string.warning_data_should_be_filled);
        if(tvPalletNo.getText().toString().equals("")){
            tvPalletNo.requestFocus();
            tvPalletNo.setError(warning);
            return false;
        }

        if (tvDestNo.getText().toString().equals("")){
            tvDestNo.requestFocus();
            tvDestNo.setError(warning);
            return false;
        }

        return true;
    }

    private void showConfirmationToFinishReplenish(){
        String title = getResources().getString(R.string.common_confirmation_title);
        String body = getResources().getString(R.string.ask_to_finish_replenish);
        showAskDialog(title, body,
                (dialog, which) -> finishMutationTaskThread(),
                (dialog, which) -> dialog.dismiss());
    }

    private void moveToTaskSwitcherActivity(){
        Intent intent = new Intent(this, TaskSwitcherActivity.class);
        intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
        startActivity(intent);
        finish();
    }

    private void showDataToUi(){
        setToolbarTitle(movingTaskData);
        destItemDataList.clear();
        if(movingTaskData.movingType == TaskTypeEnum.INTERNAL_MOVEMENT.getValue() &&
                movingTaskData.docRefUri.equals(RefDocUriEnum.MUTATION.getValue()) ||
                movingTaskData.docRefUri.equals(RefDocUriEnum.DESTRUCTION.getValue()) ||
                movingTaskData.docRefUri.equals(RefDocUriEnum.BANDED.getValue())){

            destItemDataList.add(destItemData);

        } else {
            destItemDataList.addAll(movingTaskData.destItemList);
        }
        adapter.notifyDataSetChanged();
    }

    private void getDataFromIntent(){
        Bundle bundle = getIntent().getExtras();
        if(bundle != null) {
            movingTaskData = (MovingTaskData) bundle.getSerializable(MovingStartActivity.MOVING_TASK_CODE);
            destItemData = (MovingTaskDestItemData) bundle.getSerializable(ProductListForMovingActivity.DEST_ITEM_CODE);
        }
        if(getIntent().hasExtra(MovingStartActivity.MUTATION_TYPE_CODE)){
            mutationType = getIntent().getStringExtra(MovingStartActivity.MUTATION_TYPE_CODE);
        }
    }

    private void setToolbarTitle(MovingTaskData data){
        if(data.getTaskType() == TaskTypeEnum.REPLENISHMENT_BOX || data.getTaskType() == TaskTypeEnum.REPLENISHMENT_PCS)
            toolbar.setTitle(getResources().getString(R.string.title_replenish_finish));
        else if (data.getTaskType() == TaskTypeEnum.REWAREHOUSING)
            toolbar.setTitle(getResources().getString(R.string.title_re_warehousing_finish));
        else if (data.getTaskType() == TaskTypeEnum.INTERNAL_MOVEMENT) {
            if (data.docRefUri.equals(RefDocUriEnum.DESTRUCTION.getValue()))
                toolbar.setTitle(getResources().getString(R.string.title_destruction_finish));
            else if (data.docRefUri.equals(RefDocUriEnum.MUTATION.getValue())) {
                toolbar.setTitle(getResources().getString(R.string.title_mutation_finish));
            }
            else if (data.docRefUri.equals(RefDocUriEnum.BANDED.getValue()))
                toolbar.setTitle(getResources().getString(R.string.title_banded_finish));
        }
    }

    private void showConfirmationToFinishTask() {
        String title = getResources().getString(R.string.common_confirmation_title);
        String body = getResources().getString(R.string.ask_finish_banded_task);
        showAskDialog(title, body,
                (dialog, which) -> backToProductList(),
                (dialog, which) -> dialog.dismiss());
    }

    private void backToProductList() {
        Intent intent = new Intent(this, ProductListForMovingActivity.class);
        Bundle bundle = new Bundle();
        bundle.putSerializable(MovingStartActivity.MOVING_TASK_CODE, movingTaskData);
        intent.putExtra(MovingStartActivity.MUTATION_TYPE_CODE, mutationType);
        intent.putExtras(bundle);
        startActivity(intent);
    }

    //endregion

    //region threads

    private void finishMutationTaskThread(){
        ThreadStart(new ThreadHandler<Boolean>() {
            @Override
            public void onPrepare() throws Exception {
                startProgressDialog(getString(R.string.progress_finish_replenishData), ProgressType.SPINNER);
            }

            @Override
            public Boolean onBackground() throws Exception {
                movingTaskManager.finishMovingTask(movingTaskData.movingId);
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

    private void takeNewPalletThread(String palletNo) {
        ThreadStart(new ThreadHandler<Boolean>() {
            @Override
            public void onPrepare() throws Exception {
                startProgressDialog(getString(R.string.progress_processing_pallet), ProgressType.SPINNER);
            }

            @Override
            public Boolean onBackground() throws Exception {
                movingTaskManager.updatePalletNoForMutationOrder(movingTaskData.movingId, palletNo, destItemData.productId);
                return true;
            }

            @Override
            public void onError(Exception e) {
                showErrorDialog(e);
            }

            @Override
            public void onSuccess(Boolean data) throws Exception {
                dismissProgressDialog();
                for(int index = 0; index < movingTaskData.destItemList.size(); index++){
                    if(destItemData.destBinId.equals(movingTaskData.destItemList.get(index).destBinId)){
                        movingTaskData.destItemList.get(index).palletNo = palletNo;
                    }
                }
                tvPalletNo.setText(palletNo);
            }

            @Override
            public void onFinish(){
                dismissProgressDialog();
            }
        });
    }

    //endregion

}
