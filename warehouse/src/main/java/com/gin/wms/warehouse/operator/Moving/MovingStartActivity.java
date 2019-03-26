package com.gin.wms.warehouse.operator.Moving;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
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
import com.gin.wms.manager.db.data.MovingTaskSourceItemData;
import com.gin.wms.manager.db.data.enums.MutationTypeEnum;
import com.gin.wms.manager.db.data.enums.RefDocUriEnum;
import com.gin.wms.manager.db.data.enums.TaskTypeEnum;
import com.gin.wms.warehouse.R;
import com.gin.wms.warehouse.base.WarehouseActivity;
import com.gin.wms.warehouse.component.SingleInputDialogFragment;
import com.gin.wms.warehouse.operator.TaskSwitcherActivity;

import java.util.ArrayList;
import java.util.List;

import bolts.Task;

public class MovingStartActivity extends WarehouseActivity
        implements View.OnClickListener, SingleInputDialogFragment.InputManualInterface {
    private MovingTaskManager movingTaskManager;
    private MovingTaskData movingTaskData;
    private MovingTaskSourceItemData sourceItemData;
    private MovingTaskDestItemData destItemData;
    private TextView txtPalletNo, txtSourceNo;
    private Button btnProcess, btnTakePallet;
    private String mutationType;
    private NgemartRecyclerView recyclerView;
    private NgemartRecyclerViewAdapter<List<MovingTaskSourceItemData>> adapter;
    private final List<MovingTaskSourceItemData> sourceItemDataList = new ArrayList<>();
    private Toolbar toolbar;
    private static int SCAN_PALLET_ID_CODE = 2345;
    private static int SCAN_SOURCE_ID_CODE = 5432;
    private static int TAKE_PALLET_ID_CODE = 6543;
    public static final String MOVING_TASK_CODE = "movingTaskData";
    public static final String MUTATION_TYPE_CODE = "mutationType";

    //region override functions
    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        try{
            super.onCreate(savedInstanceState);
            setContentView(R.layout.activity_moving_start);

            init();
            getDataFromIntent();
            isMutationTypeFromBadArea();
            setButtonVisibility();
            showDataToUi(movingTaskData);

        }catch (Exception e){
            showErrorDialog(e);
        }
    }

    private void setButtonVisibility() {
        if(!isMutationTypeFromBadArea() && movingTaskData.docRefUri.equals(RefDocUriEnum.MUTATION.getValue())){
            btnTakePallet.setVisibility(View.VISIBLE);
            btnProcess.setVisibility(View.VISIBLE);
        }
    }

    private boolean isMutationTypeFromBadArea() {
        if(mutationType != null && mutationType.equals(MutationTypeEnum.BADTOGOOD.toString())){
            moveToMovingFinishActivity();
            return true;
        }
        return false;
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        try{
            IntentResult result = IntentIntegrator.parseActivityResult(requestCode, resultCode, data);
            if(result != null){
                if(resultCode == Activity.RESULT_OK)
                    validateInputAndShowToUi(requestCode, result.getContents());
            }
        }catch (Exception e){
            showErrorDialog(e);
        }
    }

    @Override
    public void onClick(View view) {
        String PALLET_TITLE_DIALOG = getResources().getString(R.string.pallet_id);
        String SOURCE_TITLE_DIALOG = getResources().getString(R.string.source_id);

        switch (view.getId()){
            case R.id.txtPalletNo:
                showInputFragment(SCAN_PALLET_ID_CODE, PALLET_TITLE_DIALOG, txtPalletNo.getText().toString());
                break;
            case R.id.txtSourceNo:
                showInputFragment(SCAN_SOURCE_ID_CODE, SOURCE_TITLE_DIALOG, txtSourceNo.getText().toString());
                break;
            case R.id.btnScanPallet:
                LaunchBarcodeScanner(SCAN_PALLET_ID_CODE);
                break;
            case R.id.btnScanSource:
                LaunchBarcodeScanner(SCAN_SOURCE_ID_CODE);
                break;
            case R.id.btnTakePallet:
                showInputFragment(TAKE_PALLET_ID_CODE, PALLET_TITLE_DIALOG, txtPalletNo.getText().toString());
                break;
            case R.id.btnMoveToDestination:
                if(validateInputTextAlreadyFilled())
                    showConfirmationToStartTask();
                break;
        }
    }

    @Override
    public void onTextInput(int typeCode, String inputText) {
        try{
            validateInputAndShowToUi(typeCode, inputText);
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

    //endregion

    //region init
    private void init()throws Exception{
        initObjectManager();
        initComponent();
        initToolbar();
        initAdapter();
        initRecyclerAdapter();
    }

    private void initObjectManager()throws Exception{
        movingTaskManager = new MovingTaskManager(getApplicationContext());
    }

    private void initComponent(){
        btnProcess = findViewById(R.id.btnMoveToDestination);
        btnTakePallet = findViewById(R.id.btnTakePallet);
        ImageButton btnScanPallet = findViewById(R.id.btnScanPallet);
        ImageButton btnScanSource = findViewById(R.id.btnScanSource);
        txtPalletNo = findViewById(R.id.txtPalletNo);
        txtSourceNo = findViewById(R.id.txtSourceNo);
        recyclerView = findViewById(R.id.rvMovingStart);

        btnProcess.setOnClickListener(this);
        btnTakePallet.setOnClickListener(this);
        btnScanPallet.setOnClickListener(this);
        btnScanSource.setOnClickListener(this);
        txtPalletNo.setOnClickListener(this);
        txtSourceNo.setOnClickListener(this);
    }

    private void initToolbar() {
        toolbar = findViewById(R.id.toolbarReplenishStart);
        toolbar.setTitle(getResources().getString(R.string.title_replenish_start));
        setSupportActionBar(toolbar);
        if(getSupportActionBar() != null)
            getSupportActionBar().setDisplayHomeAsUpEnabled(true);
    }

    private void initAdapter(){
        adapter = new NgemartRecyclerViewAdapter(this, R.layout.card_moving_source_item, sourceItemDataList);
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

    //region threads

    private void takeNewPalletThread(String palletNo) {
        ThreadStart(new ThreadHandler<Boolean>() {
            @Override
            public void onPrepare() throws Exception {
                startProgressDialog(getString(R.string.progress_processing_pallet), ProgressType.SPINNER);
            }

            @Override
            public Boolean onBackground() throws Exception {
                movingTaskManager.updatePalletNoForMutationOrder(movingTaskData.movingId, palletNo, sourceItemData.productId);
                return true;
            }

            @Override
            public void onError(Exception e) {
                showErrorDialog(e);
            }

            @Override
            public void onSuccess(Boolean data) throws Exception {
                txtPalletNo.setText(palletNo);
                for(int index = 0; index < movingTaskData.sourceItemList.size(); index++){
                    if(sourceItemData.sourceBinId.equals(movingTaskData.sourceItemList.get(index).sourceBinId)){
                        movingTaskData.sourceItemList.get(index).palletNo = palletNo;
                    }
                }
                dismissProgressDialog();
            }

            @Override
            public void onFinish(){
            }
        });
    }
    //endregion

    //region other functions

    private void showDataToUi(MovingTaskData data){
        sourceItemDataList.clear();
        if(data != null){
            if(data.startTime != null){
                setToolbarTitle(data);
                if(data.movingType == TaskTypeEnum.INTERNAL_MOVEMENT.getValue()){
                    sourceItemDataList.add(sourceItemData);
                }else{
                    sourceItemDataList.addAll(data.sourceItemList);
                }
            }
        }
        adapter.notifyDataSetChanged();
    }

    private void setToolbarTitle(MovingTaskData data){
        if(data.getTaskType() == TaskTypeEnum.REPLENISHMENT_BOX || data.getTaskType() == TaskTypeEnum.REPLENISHMENT_PCS)
            toolbar.setTitle(getResources().getString(R.string.title_replenish_start));
        else if (data.getTaskType() == TaskTypeEnum.REWAREHOUSING)
            toolbar.setTitle(getResources().getString(R.string.title_re_warehousing_start));
        else if (data.getTaskType() == TaskTypeEnum.INTERNAL_MOVEMENT) {
            if (data.docRefUri.equals(RefDocUriEnum.DESTRUCTION.getValue()))
                toolbar.setTitle(getResources().getString(R.string.title_destruction_start));
            else if (data.docRefUri.equals(RefDocUriEnum.MUTATION.getValue())) {
                toolbar.setTitle(getResources().getString(R.string.title_mutation_start));
            }
            else if (data.docRefUri.equals(RefDocUriEnum.BANDED.getValue()))
                toolbar.setTitle(getResources().getString(R.string.title_banded_start));
        }
    }

    private void moveToTaskSwitcherActivity() {
        Intent intent = new Intent(this, TaskSwitcherActivity.class);
        intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK | Intent.FLAG_ACTIVITY_NEW_TASK);
        startActivity(intent);
    }

    private void backToProductList() {
        Intent intent = new Intent(this, ProductListForMovingActivity.class);
        Bundle bundle = new Bundle();
        bundle.putSerializable(MOVING_TASK_CODE, movingTaskData);
        intent.putExtra(MovingStartActivity.MUTATION_TYPE_CODE, mutationType);
        intent.putExtras(bundle);
        startActivity(intent);
    }

    private void getDataFromIntent() {
        Bundle bundle = getIntent().getExtras();
        if(bundle != null) {
            movingTaskData = (MovingTaskData)bundle.getSerializable(MovingStartActivity.MOVING_TASK_CODE);
            sourceItemData = (MovingTaskSourceItemData)bundle.getSerializable(ProductListForMovingActivity.SOURCE_ITEM_CODE);
            destItemData = (MovingTaskDestItemData)bundle.getSerializable(ProductListForMovingActivity.DEST_ITEM_CODE);
        }
        if(getIntent() != null && getIntent().hasExtra(MovingStartActivity.MUTATION_TYPE_CODE)){
            mutationType = getIntent().getStringExtra(MovingStartActivity.MUTATION_TYPE_CODE);
        }
    }

    private void showInputFragment(int typeCode, String dialogTitle, String previousValue){
        DialogFragment dialogFragment;
        dialogFragment = SingleInputDialogFragment.newInstance(typeCode, dialogTitle, previousValue);
        dialogFragment.show(getSupportFragmentManager(), "");
    }

    private void validateInputAndShowToUi(int typeCode, String inputText) throws Exception{
        if(typeCode == SCAN_PALLET_ID_CODE){
            if(validateInputText(typeCode, inputText)){
                txtPalletNo.setText(inputText);
            } else{
                String pallet_warning = getResources().getString(R.string.pallet_id_is_not_match);
                showErrorDialog(pallet_warning);
                txtPalletNo.setText("");
            }
        } else if (typeCode == SCAN_SOURCE_ID_CODE){
            if(validateInputText(typeCode, inputText)){
                txtSourceNo.setText(inputText);
            } else{
                String dest_warning = getResources().getString(R.string.source_id_is_not_match);
                showErrorDialog(dest_warning);
                txtSourceNo.setText("");
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
                if(inputText.toLowerCase().equals(sourceItemData.palletNo.toLowerCase()))
                    isCorrect = true;
            } else if (inputText.toLowerCase().equals(sourceItemDataList.get(0).palletNo.toLowerCase())){
                isCorrect = true;
            }
        } else if (typeCode == SCAN_SOURCE_ID_CODE){
            if(movingTaskData.getTaskType() == TaskTypeEnum.INTERNAL_MOVEMENT) {
                if(inputText.toLowerCase().equals(sourceItemData.sourceBinId.toLowerCase()))
                    isCorrect = true;
            } else if (inputText.toLowerCase().equals(sourceItemDataList.get(0).sourceBinId.toLowerCase())){
                isCorrect = true;
            }
        }
        return isCorrect;
    }

    private void showConfirmationToStartTask(){
        String title = getResources().getString(R.string.common_confirmation_title);
        String body = getResources().getString(R.string.ask_to_move_to_dest);
        showAskDialog(title, body,
                (dialog, which) -> moveToMovingFinishActivity(),
                (dialog, which) -> dialog.dismiss());
    }

    private void moveToMovingFinishActivity(){
        Intent intent = new Intent(this, MovingFinishActivity.class);
        Bundle bundle = new Bundle();
        bundle.putSerializable(MOVING_TASK_CODE, movingTaskData);
        bundle.putSerializable(ProductListForMovingActivity.DEST_ITEM_CODE, destItemData);
        intent.putExtra(MovingStartActivity.MUTATION_TYPE_CODE, mutationType);
        intent.putExtras(bundle);
        startActivity(intent);
    }

    private boolean validateInputTextAlreadyFilled(){
        String warning = getResources().getString(R.string.warning_data_should_be_filled);
        if(txtPalletNo.getText().toString().equals("")){
            txtPalletNo.requestFocus();
            txtPalletNo.setError(warning);
            return false;
        }

        if (txtSourceNo.getText().toString().equals("")){
            txtSourceNo.requestFocus();
            txtSourceNo.setError(warning);
            return false;
        }
        return true;
    }
    //endregion
}
