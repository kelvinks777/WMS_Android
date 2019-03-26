package com.gin.wms.warehouse.administrator;

import android.app.Dialog;
import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.Toolbar;
import android.view.MenuItem;
import android.view.View;

import com.gin.ngemart.baseui.NgemartActivity;
import com.gin.ngemart.baseui.adapter.NgemartRecyclerView;
import com.gin.ngemart.baseui.adapter.NgemartRecyclerViewAdapter;
import com.gin.ngemart.baseui.component.NgemartCardView;
import com.gin.wms.manager.EquipmentManager;
import com.gin.wms.manager.OperatorStatusTaskManager;
import com.gin.wms.manager.db.data.EquipmentData;
import com.gin.wms.manager.db.data.OperatorEquipmentData;
import com.gin.wms.manager.db.data.OperatorStatusData;
import com.gin.wms.warehouse.R;
import com.gin.wms.warehouse.base.WarehouseActivity;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

public class EquipmentListActivity extends WarehouseActivity implements NgemartCardView.CardListener<OperatorStatusData>{
    private NgemartRecyclerView rv;
    private NgemartRecyclerViewAdapter<List<OperatorStatusData>> adapter;
    private List<OperatorStatusData> allResultData = new ArrayList<>();
    private final List<OperatorStatusData> operatorStatusResultData = new ArrayList<>();
    private OperatorStatusData resultData;
    private OperatorStatusTaskManager operatorStatusTaskManager;
    private EquipmentManager equipmentManager;
    private List<EquipmentData> listEquipment= new ArrayList<>();
    private List<EquipmentData> listEquipmentForklif= new ArrayList<>();
    private List<EquipmentData> listEquipmentHandpallet= new ArrayList<>();
    private List<OperatorEquipmentData> getAllEquipmentData = new ArrayList<>();
    private Dialog dialogs;

    public static final String FORKLIFT ="forklift";
    public static final String HANDPALLET ="handpalet";
    public static final String ALL_UNCHECKED_RESULT = "ALL_UNCHECKED_RESULT";
    public static final String LiST_EQUIPMENT = "LiST_EQUIPMENT";
    public static final String EQUIPMENT_TASK_PER_RESULT_DATA_CODE = "EQUIPMENT_TASK_PER_RESULT_DATA_CODE";
    public static final String EQUIPMENT_INFO_ALL = "EQUIPMENT_INFO_ALL";

    //region Override function
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        try{
            super.onCreate(savedInstanceState);
            setContentView(R.layout.activity_equipment_list);
            operatorStatusTaskManager = new OperatorStatusTaskManager(this);
            equipmentManager = new EquipmentManager(this);
            getIntentData();
            init();
            showItemList();
        }catch (Exception e){
            showErrorDialog(e);
        }
    }

    @Override
    public void onBackPressed() {
        moveToAdministratorActivity();
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()){
            case android.R.id.home:
                moveToAdministratorActivity();
                break;
             default:
                 return super.onOptionsItemSelected(item);
        }
        return true;
    }

    @Override
    public void onCardClick(int position, View view, OperatorStatusData data) {
        try{
            if(view.getId() == R.id.btnUpdateEquipment){
                //DoAddEquipment(data);
                getListEqupiment(data);
            }
            else if(view.getId() == R.id.btnInfo){
                getListEquipmentInfo(data);

            }
        }catch (Exception e){
            showErrorDialog(e);
        }
    }
    //endregion

    private void getIntentData(){
        Bundle bundle = getIntent().getExtras();
        if(bundle != null){
            allResultData = (List<OperatorStatusData>) bundle.getSerializable(ALL_UNCHECKED_RESULT);
            //getAllEquipmentData = (List<OperatorEquipmentData>) bundle.getSerializable(EQUIPMENT_INFO_ALL);
//            if(getIntent().hasExtra(EQUIPMENT_TASK_PER_RESULT_DATA_CODE)){
//                resultData = (OperatorStatusData) bundle.getSerializable(EQUIPMENT_TASK_PER_RESULT_DATA_CODE);
//                checkedResultData = (List<OperatorStatusData>) bundle.getSerializable(ALL_CHECKED_RESULT);
//                addCheckedResultToList();
//            }
        }
    }

    //region init
    private void init(){
        rv = findViewById(R.id.rvEquipment);
        dialogs= new Dialog(this);
        initToolbar();
        initRecyclerView();
    }

    private void initToolbar(){
        Toolbar toolbar = findViewById(R.id.tbEquipment);
        toolbar.setTitle(getResources().getString(R.string.toolbar_list_equipment));
        setSupportActionBar(toolbar);
        if(getSupportActionBar() != null)
            getSupportActionBar().setDisplayHomeAsUpEnabled(true);
    }

    private void initRecyclerView() {
        initAdapter();
        initRvAdapter();
    }

    private void initAdapter(){
        adapter = new NgemartRecyclerViewAdapter(this, R.layout.card_equipment_list, operatorStatusResultData);
        adapter.setHasStableIds(true);
        adapter.setRecyclerListener(this);
        rv.setAdapter(adapter);
    }

    private void initRvAdapter(){
        int numColumn = 1;
        GridLayoutManager gridLayoutManager = new GridLayoutManager(this, numColumn, GridLayoutManager.VERTICAL, false);
        rv.setHasFixedSize(true);
        rv.SetDefaultDecoration();
        rv.setLayoutManager(gridLayoutManager);
    }

    //endregion

    //region other function
    private void showItemList() throws Exception{
        operatorStatusResultData.clear();

        if(resultData != null){
//            operatorStatusResultData.addAll(checkedResultData);
//            operatorStatusResultData.addAll(allResultData);
        }
        else{
            operatorStatusResultData.addAll(allResultData);
            operatorStatusTaskManager.saveEquipmentDataToLocal(operatorStatusResultData);
        }
        adapter.notifyDataSetChanged();
    }

    private void moveToAdministratorActivity() {
        Intent intent = new Intent(this, AdministratorActivity.class);
        intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
        startActivity(intent);
        finish();
    }

//    private void DoAddEquipment(OperatorStatusData data){
//        Intent intent = new Intent(this, EquipmentUpdateActivity.class);
//        Bundle bundle = new Bundle();
//        bundle.putSerializable(EQUIPMENT_TASK_PER_RESULT_DATA_CODE, data);
//        bundle.putSerializable(ALL_UNCHECKED_RESULT, (Serializable) allResultData);
//        bundle.putSerializable(ALL_CHECKED_RESULT, (Serializable) checkedResultData);
//        intent.putExtras(bundle);
//        startActivity(intent);
//
//    }

    private void getListEqupiment(OperatorStatusData datas){
        ThreadStart(new ThreadHandler<List<EquipmentData>>() {
            @Override
            public void onPrepare() throws Exception {
                startProgressDialog(getString(R.string.progress_get_warehouse_problem), NgemartActivity.ProgressType.SPINNER);
            }

            @Override
            public List<EquipmentData> onBackground() throws Exception {
                listEquipment = equipmentManager.GetEquipmentNotUsed();
                return listEquipment;
            }

            @Override
            public void onError(Exception e) {
                dismissProgressDialog();
                showErrorDialog(e);
            }

            @Override
            public void onSuccess(List<EquipmentData> data) throws Exception {
                if(data != null){
                    getDataListEquipment(data, datas);
                }
            }

            @Override
            public void onFinish() {
                dismissProgressDialog();
            }
        });
    }

    private void getDataListEquipment(List<EquipmentData> data,  OperatorStatusData datas)throws Exception{
        listEquipmentForklif = equipmentManager.getListEquipmentResultFromLocal(FORKLIFT);
        listEquipmentHandpallet =equipmentManager.getListEquipmentResultFromLocal(HANDPALLET);
        Intent intent = new Intent(this, EquipmentUpdateActivity.class);
        Bundle bundle = new Bundle();
        bundle.putSerializable(EQUIPMENT_TASK_PER_RESULT_DATA_CODE, datas);
        bundle.putSerializable(ALL_UNCHECKED_RESULT, (Serializable) allResultData);
        bundle.putSerializable(FORKLIFT, (Serializable) listEquipmentForklif);
        bundle.putSerializable(HANDPALLET, (Serializable) listEquipmentHandpallet);
        intent.putExtras(bundle);
        startActivity(intent);
    }

    private void showChooseAllInfoEquipmentList(){
        //dialogs.setTitle("Pilih Jenis HandPallet");
        dialogs.setCancelable(true);
        dialogs.setContentView(R.layout.activity_equipment_info_list);
        final NgemartRecyclerView rvHandpallet = (NgemartRecyclerView) dialogs.findViewById(R.id.rvEquipmentInfo);
        NgemartRecyclerViewAdapter<List<EquipmentData>> adapter;
        adapter = new NgemartRecyclerViewAdapter(this, R.layout.card_use_equipment_list, getAllEquipmentData);
        adapter.setHasStableIds(true);
        adapter.setRecyclerListener(this);
        rvHandpallet.setAdapter(adapter);
        NgemartRecyclerView.LayoutManager layoutManager= new LinearLayoutManager(this);
        rvHandpallet.setLayoutManager(layoutManager);
        dialogs.show();
    }

    private void getListEquipmentInfo(OperatorStatusData operatorStatusData){
        ThreadStart(new ThreadHandler<List<OperatorEquipmentData>>() {
            @Override
            public void onPrepare() throws Exception {
                //startProgressDialog(getString(R.string.progress_get_warehouse_problem), NgemartActivity.ProgressType.SPINNER);
            }

            @Override
            public List<OperatorEquipmentData> onBackground() throws Exception {
                getAllEquipmentData = equipmentManager.GetAllOperatorEquipment(operatorStatusData.operatorId);
                return getAllEquipmentData;
            }

            @Override
            public void onError(Exception e) {
                dismissProgressDialog();
                showErrorDialog(e);
            }

            @Override
            public void onSuccess(List<OperatorEquipmentData> data) throws Exception {
               // dismissProgressDialog();
            }

            @Override
            public void onFinish() {
                showChooseAllInfoEquipmentList();
            }
        });
    }

}
