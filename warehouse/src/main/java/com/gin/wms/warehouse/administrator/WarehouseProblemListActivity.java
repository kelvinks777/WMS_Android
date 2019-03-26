package com.gin.wms.warehouse.administrator;

import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.Toolbar;
import android.view.View;
import android.widget.Button;

import com.gin.ngemart.baseui.NgemartActivity;
import com.gin.ngemart.baseui.adapter.NgemartRecyclerView;
import com.gin.ngemart.baseui.adapter.NgemartRecyclerViewAdapter;
import com.gin.ngemart.baseui.component.NgemartCardView;
import com.gin.wms.manager.StockLocationManager;
import com.gin.wms.manager.WarehouseProblemManager;
import com.gin.wms.manager.db.data.StockCountingOrderItemByBinIdData;
import com.gin.wms.manager.db.data.StockLocationData;
import com.gin.wms.manager.db.data.WarehouseProblemData;
import com.gin.wms.warehouse.R;
import com.gin.wms.warehouse.base.WarehouseActivity;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

public class WarehouseProblemListActivity extends WarehouseActivity implements
        NgemartCardView.CardListener<WarehouseProblemData>, View.OnClickListener {

    private Button btnFinishWarehouseProblem;
    private NgemartRecyclerView recyclerView;
    private NgemartRecyclerViewAdapter<List<WarehouseProblemData>> adapter;
    private List<WarehouseProblemData> warehouseProblemDataList = new ArrayList<>();
    private WarehouseProblemData warehouseProblemData;
    private StockLocationData stockLocationData;
    private WarehouseProblemManager warehouseProblemManager;
    private StockLocationManager stockLocationManager;
    public static final String LIST_WAREHOUSEPROBLEM= "listWarehouseProblem";
    public static final String GET_STOCKLOCATION= "getStocklocation";
    public static final String UPDATE_WAREHOUSEPROBLEM= "updateWarehouseProblem";
    private Toolbar toolbar;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        try{
            super.onCreate(savedInstanceState);
            setContentView(R.layout.activity_warehouseproblem_list);
            getIntentData();
            init();
        }catch (Exception e){
            showErrorDialog(e);
        }
    }

    @Override
    public void onClick(View v) {

    }

    @Override
    public void onCardClick(int position, View view, WarehouseProblemData data) {
        try{
            if(view.getId() == R.id.btnEditDataWarehouseProblem){
                //doUpdateResult(data);
                getStocklocationData(data);
            }
        }catch (Exception e){
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
            warehouseProblemDataList=(List<WarehouseProblemData>)bundle.getSerializable(LIST_WAREHOUSEPROBLEM);
        }
    }

    private void init()throws Exception{
        initToolbar();
        justBindingProperties();
        initPropertyHandler();
        initObjectManager();
        initAdapter();
        initRecyclerAdapter();
    }

    private void initToolbar() {
        toolbar = findViewById(R.id.toolbarWarehouseProblemList);
        toolbar.setTitle(getResources().getString(R.string.list_warehouse_problem));
        setSupportActionBar(toolbar);
        if(getSupportActionBar() != null)
            getSupportActionBar().setDisplayHomeAsUpEnabled(true);
    }

    private void justBindingProperties(){
        btnFinishWarehouseProblem=findViewById(R.id.btnFinishWarehouseProblem);
        recyclerView=findViewById(R.id.rvWarehouseProblem);
    }

    private void initPropertyHandler(){
        btnFinishWarehouseProblem.setOnClickListener(this);
    }

    private void initAdapter(){
        adapter=  new NgemartRecyclerViewAdapter(this, R.layout.card_warehouseproblem_list, warehouseProblemDataList);
        adapter.setHasStableIds(true);
        adapter.setRecyclerListener(this);
        recyclerView.setAdapter(adapter);
    }

    private void initRecyclerAdapter(){
        int numColumn = 1;
        GridLayoutManager gridLayoutManager = new GridLayoutManager(this, numColumn, GridLayoutManager.VERTICAL, false);
        recyclerView.setHasFixedSize(true);
        recyclerView.SetDefaultDecoration();
        recyclerView.setLayoutManager(gridLayoutManager);
    }

    private void initObjectManager()throws Exception{
        warehouseProblemManager = new WarehouseProblemManager(this);
        stockLocationManager = new StockLocationManager(this);
    }

    private void doUpdateResult(WarehouseProblemData warehouseProblemData){
        Intent intent= new Intent(this, WarehouseProblemUpdateActivity.class);
        Bundle bundle = new Bundle();
        bundle.putSerializable(LIST_WAREHOUSEPROBLEM, warehouseProblemData);
        bundle.putSerializable(UPDATE_WAREHOUSEPROBLEM, (Serializable) warehouseProblemDataList);
        intent.putExtras(bundle);
        startActivity(intent);
        finish();
    }

    private void getStocklocationData(WarehouseProblemData warehouseProblemData){
        ThreadStart(new ThreadHandler<StockLocationData>() {
            @Override
            public void onPrepare() throws Exception {
                 startProgressDialog(getString(R.string.progress_get_stocklocation), NgemartActivity.ProgressType.SPINNER);
            }

            @Override
            public StockLocationData onBackground() throws Exception {
                stockLocationData = stockLocationManager.GetStockLocation(warehouseProblemData.binId, warehouseProblemData.palletNo, warehouseProblemData.productId);
                return stockLocationData;
            }

            @Override
            public void onError(Exception e) {
                dismissProgressDialog();
                showErrorDialog(e);
            }

            @Override
            public void onSuccess(StockLocationData data) throws Exception {
                if(data != null){
                    //goToWarehouseProblem(data);
                    doUpdateResultStocklocation(data, warehouseProblemData);
                }
            }

            @Override
            public void onFinish() {
                dismissProgressDialog();
            }
        });
    }

    private void doUpdateResultStocklocation(StockLocationData stockLocationData, WarehouseProblemData warehouseProblemData){
        Intent intent = new Intent(this, WarehouseProblemUpdateActivity.class);
        Bundle bundle = new Bundle();
        bundle.putSerializable(GET_STOCKLOCATION, stockLocationData);
        bundle.putSerializable(LIST_WAREHOUSEPROBLEM, warehouseProblemData);
        intent.putExtras(bundle);
        startActivity(intent);
        finish();
    }
}
