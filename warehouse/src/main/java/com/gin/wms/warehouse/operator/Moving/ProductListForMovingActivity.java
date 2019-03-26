package com.gin.wms.warehouse.operator.Moving;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.Toolbar;
import android.text.InputType;
import android.view.View;
import android.widget.Button;

import com.gin.ngemart.baseui.adapter.NgemartRecyclerView;
import com.gin.ngemart.baseui.adapter.NgemartRecyclerViewAdapter;
import com.gin.ngemart.baseui.component.NgemartCardView;
import com.gin.wms.manager.MovingTaskManager;
import com.gin.wms.manager.db.data.MovingTaskData;
import com.gin.wms.manager.db.data.MovingTaskDestItemData;
import com.gin.wms.manager.db.data.MovingTaskSourceItemData;
import com.gin.wms.manager.db.data.enums.TaskTypeEnum;
import com.gin.wms.warehouse.R;
import com.gin.wms.warehouse.base.WarehouseActivity;
import com.gin.wms.warehouse.operator.TaskSwitcherActivity;
import com.gin.wms.warehouse.warehouseProblem.WarehouseProblemReportActivity;

import java.util.ArrayList;
import java.util.List;

public class ProductListForMovingActivity extends WarehouseActivity implements
        NgemartCardView.CardListener<MovingTaskDestItemData> {
    private MovingTaskManager movingTaskManager;
    private MovingTaskData movingTaskData;
    private MovingTaskSourceItemData sourceItemData;
    private String mutationOrderType;
    private NgemartRecyclerView rv;
    private NgemartRecyclerViewAdapter<List<MovingTaskDestItemData>> adapter;
    private final List<MovingTaskDestItemData> destItemDataList = new ArrayList<>();
    public static final String SOURCE_ITEM_CODE = "sourceItemData";
    public static final String DEST_ITEM_CODE = "destItemCode";

    //region override function

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        try {
            super.onCreate(savedInstanceState);
            setContentView(R.layout.activity_product_list_for_moving);

            getDataFromIntent();
            init();
            showDataToUI();

        } catch (Exception e){
            showErrorDialog(e);
        }
    }

    @Override
    public void onBackPressed() {
        moveToTaskSwitcherActivity();
    }

    @Override
    public void onCardClick(int position, View view, MovingTaskDestItemData data) {
        try {
            findSourceItemData(data, movingTaskData);
            showMovingStartActivity(data, sourceItemData, movingTaskData, mutationOrderType);
        } catch (Exception e){
            showErrorDialog(e);
        }
    }

    //endregion

    //region init

    private void init() throws Exception{
        initObjectManager();
        initToolBar();
        initComponent();
        initAdapter();
        initRecyclerAdapter();
    }

    private void initObjectManager() throws Exception{
        movingTaskManager = new MovingTaskManager(getApplicationContext());
    }

    private void initRecyclerAdapter() {
        int numColumn = 1;
        GridLayoutManager gridLayoutManager = new GridLayoutManager(this, numColumn, GridLayoutManager.VERTICAL, false);
        rv.setHasFixedSize(true);
        rv.SetDefaultDecoration();
        rv.setLayoutManager(gridLayoutManager);
    }

    private void initAdapter() {
        adapter = new NgemartRecyclerViewAdapter(this,R.layout.card_product_list_mutation_order, destItemDataList);
        adapter.setHasStableIds(true);
        adapter.setRecyclerListener(this);
        rv.setAdapter(adapter);
    }

    private void initComponent() {
        rv = findViewById(R.id.rvListProductMovingStart);
        Button btnFinish = findViewById(R.id.btnFinishMoving);
        Button btnReport = findViewById(R.id.btnReport);
        setButtonVisibility(btnReport);
        btnFinish.setOnClickListener(v -> showConfirmationToFinishTask());
        btnReport.setOnClickListener(v -> showWarehouseProblemActivity());
    }

    private void initToolBar() {
        Toolbar toolbar = findViewById(R.id.tbProductlistMovingStart);
        toolbar.setTitle(getResources().getString(R.string.title_moving_start));
        setSupportActionBar(toolbar);
        if(getSupportActionBar() != null)
            getSupportActionBar().setDisplayHomeAsUpEnabled(false);
    }

    //endregion

    //region other function

    private void showDataToUI() {
        destItemDataList.clear();
        destItemDataList.addAll(movingTaskData.destItemList);
        adapter.notifyDataSetChanged();
    }

    private void setButtonVisibility(Button btnReport) {
        if(movingTaskData.getTaskType() != TaskTypeEnum.INTERNAL_MOVEMENT){
            btnReport.setVisibility(View.GONE);
        }
    }

    private void showConfirmationToFinishTask() {
        String title = getResources().getString(R.string.common_confirmation_title);
        String body = getResources().getString(R.string.ask_to_finish_replenish);
        showAskDialog(title, body,
                (dialog, which) -> finishMutationTaskThread(),
                (dialog, which) -> dialog.dismiss());
    }

    private void finishMutationTaskThread() {
        ThreadStart(new ThreadHandler<Boolean>() {
            @Override
            public void onPrepare() throws Exception {
                startProgressDialog(getResources().getString(R.string.progress_finish_replenishData), ProgressType.SPINNER);
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

    private void moveToTaskSwitcherActivity() {
        Intent intent = new Intent(this, TaskSwitcherActivity.class);
        intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
        startActivity(intent);
        finish();
    }

    private void getDataFromIntent() {
        Bundle bundle = getIntent().getExtras();
        Intent intent = getIntent();
        if(bundle != null){
            movingTaskData = (MovingTaskData)bundle.getSerializable(MovingStartActivity.MOVING_TASK_CODE);
        }
        if(intent != null && intent.hasExtra(MovingStartActivity.MUTATION_TYPE_CODE)){
            mutationOrderType = intent.getStringExtra(MovingStartActivity.MUTATION_TYPE_CODE);
        }
    }

    private void findSourceItemData(MovingTaskDestItemData destItemData, MovingTaskData movingTaskData) {
        for (MovingTaskSourceItemData sourceItem: movingTaskData.sourceItemList) {
            if(destItemData.productId.equals(sourceItem.productId)){
                sourceItemData = sourceItem;
            }
        }
    }

    private void showMovingStartActivity(MovingTaskDestItemData destItemData, MovingTaskSourceItemData sourceItemData, MovingTaskData movingTaskData, String mutationOrderType) {
        Intent intent = new Intent(this, MovingStartActivity.class);
        Bundle bundle = new Bundle();
        bundle.putSerializable(MovingStartActivity.MOVING_TASK_CODE, movingTaskData);
        bundle.putSerializable(SOURCE_ITEM_CODE, sourceItemData);
        bundle.putSerializable(DEST_ITEM_CODE, destItemData);
        intent.putExtra(MovingStartActivity.MUTATION_TYPE_CODE, mutationOrderType);
        intent.putExtras(bundle);
        startActivity(intent);
    }

    private void showWarehouseProblemActivity(){
        Intent intent = new Intent(this, WarehouseProblemReportActivity.class);
        startActivity(intent);
    }

    //endregion
}
