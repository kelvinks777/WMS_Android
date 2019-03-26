package com.gin.wms.warehouse.operator.Counting;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.Toolbar;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;

import com.gin.ngemart.baseui.adapter.NgemartRecyclerView;
import com.gin.ngemart.baseui.adapter.NgemartRecyclerViewAdapter;
import com.gin.ngemart.baseui.component.NgemartCardView;
import com.gin.wms.manager.CountingTaskManager;
import com.gin.wms.manager.db.data.CountingTaskResultData;
import com.gin.wms.warehouse.R;
import com.gin.wms.warehouse.base.WarehouseActivity;
import com.gin.wms.warehouse.operator.TaskSwitcherActivity;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

public class CountingTaskActivity extends WarehouseActivity implements NgemartCardView.CardListener<CountingTaskResultData> {
    private NgemartRecyclerView rv;
    private Button btnFinish;
    private NgemartRecyclerViewAdapter<List<CountingTaskResultData>> adapter;
    private final List<CountingTaskResultData> countingResultData = new ArrayList<>();
    private List<CountingTaskResultData> checkedResultData = new ArrayList<>();
    private List<CountingTaskResultData> uncheckedResultData = new ArrayList<>();
    private CountingTaskResultData resultData;
    private CountingTaskManager countingTaskManager;

    public static final String ALL_UNCHECKED_RESULT = "ALL_UNCHECKED_RESULT";
    public static final String ALL_CHECKED_RESULT = "ALL_CHECKED_RESULT";
    public static final String COUNTING_TASK_RESULT_CHECKED_CODE = "COUNTING_TASK_RESULT_CHECKED_CODE";
    public static final String COUNTING_TASK_PER_RESULT_DATA_CODE = "COUNTING_TASK_PER_RESULT_DATA_CODE";
    public static final String ARG_MODE_EDIT = "ARG_MODE_EDIT";
    public static final String ARG_INPUT_MODE = "ARG_INPUT_MODE";

    //region Override function
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        try {
            super.onCreate(savedInstanceState);
            setContentView(R.layout.activity_stock_counting);
            countingTaskManager = new CountingTaskManager(this);
            getIntentData();
            init();
            showItemList();
        } catch (Exception e){
            showErrorDialog(e);
        }
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()){
            case android.R.id.home:
                moveToTaskSwitcherActivity();
                break;
            default:
                return super.onOptionsItemSelected(item);
        }
        return true;
    }

    @Override
    public void onCardClick(int position, View view, CountingTaskResultData data) {
        try {
            if(view.getId() == R.id.btnEditCounting){
                doEditResult(data);
            }
            else if(data.hasChecked != true){
                switchToCountingTaskInputActivity(data);
            }
        }
        catch (Exception e){
            showErrorDialog(e);
        }
    }
    //endregion

    //region other function
    private void showItemList() throws Exception{
        countingResultData.clear();

        if(resultData != null) {
            for (int uncheckedIndex = 0; uncheckedIndex < uncheckedResultData.size(); uncheckedIndex++) {

                for (int checkedIndex = 0; checkedIndex < checkedResultData.size(); checkedIndex++) {

                    CountingTaskResultData listUnChecked = uncheckedResultData.get(uncheckedIndex);
                    CountingTaskResultData listChecked = checkedResultData.get(checkedIndex);

                    if (listUnChecked.countingId.equals(listChecked.countingId) && listUnChecked.BinId.equals(listChecked.BinId)) {
                        uncheckedResultData.remove(uncheckedIndex);
                    }
                }
            }
            countingResultData.addAll(checkedResultData);
            countingResultData.addAll(uncheckedResultData);

            countingTaskManager.saveCountingTaskData(countingResultData);

        } else {
            countingResultData.addAll(uncheckedResultData);

            countingTaskManager.saveCountingTaskData(countingResultData);
        }

        adapter.notifyDataSetChanged();

        setButtonFinishEnable();
    }

    private void setButtonFinishEnable() {
        List<Boolean> checkedResult = new ArrayList<>();
        for (CountingTaskResultData result: countingResultData) {
            checkedResult.add(result.hasChecked);
        }
        if(checkedResult.contains(false)){
            btnFinish.setEnabled(false);
        }
        else {
            btnFinish.setEnabled(true);
        }
    }

    private void getIntentData() {
        Bundle bundle = getIntent().getExtras();
        if (bundle != null) {

            uncheckedResultData = (List<CountingTaskResultData>) bundle.getSerializable(ALL_UNCHECKED_RESULT);

            if(getIntent().hasExtra(COUNTING_TASK_RESULT_CHECKED_CODE)) {
                resultData = (CountingTaskResultData) bundle.getSerializable(COUNTING_TASK_RESULT_CHECKED_CODE);

                checkedResultData = (List<CountingTaskResultData>) bundle.getSerializable(ALL_CHECKED_RESULT);

                addCheckedResultToList();
            }
        }
    }

    private void addCheckedResultToList() {
        if (checkedResultData != null) {
            for (int index = 0; index < checkedResultData.size(); index++) {
                CountingTaskResultData result = checkedResultData.get(index);
                if(result.BinId.equals(resultData.BinId)){
                    checkedResultData.remove(index);
                }
            }
            checkedResultData.add(resultData);
        }
    }

    private void showConfirmationFinishTask() {
        String title = getResources().getString(R.string.common_confirmation_title);
        String body = getResources().getString(R.string.ask_finish_counting);
        showAskDialog(title, body,
                (dialog, which) -> finishCountingTaskThread(),
                (dialog, which) -> dialog.dismiss());
    }

    private void moveToTaskSwitcherActivity() {
        Intent intent = new Intent(this, TaskSwitcherActivity.class);
        intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
        startActivity(intent);
        finish();
    }
    //endregion

    //region init
    private void init() {
        rv = findViewById(R.id.rvStockCounting);
        btnFinish = findViewById(R.id.btnFinishCounting);
        initToolbar();
        initRecyclerView();

        btnFinish.setOnClickListener(v -> showConfirmationFinishTask());
    }

    private void initRecyclerView() {
        initAdapter();
        initRvAdapter();
    }

    private void initRvAdapter() {
        int numColumn = 1;
        GridLayoutManager gridLayoutManager = new GridLayoutManager(this, numColumn, GridLayoutManager.VERTICAL, false);
        rv.setHasFixedSize(true);
        rv.SetDefaultDecoration();
        rv.setLayoutManager(gridLayoutManager);
    }

    private void initAdapter() {
        adapter = new NgemartRecyclerViewAdapter(this,R.layout.card_stock_counting, countingResultData);
        adapter.setHasStableIds(true);
        adapter.setRecyclerListener(this);
        rv.setAdapter(adapter);
    }

    private void initToolbar() {
        Toolbar toolbar = findViewById(R.id.tbStockCounting);
        toolbar.setTitle(getResources().getString(R.string.toolbar_stock_counting));
        setSupportActionBar(toolbar);
        if(getSupportActionBar() != null)
            getSupportActionBar().setDisplayHomeAsUpEnabled(true);
    }
    //endregion

    //region Intent
    private void switchToCountingTaskInputActivity(CountingTaskResultData data) {
        Intent intent = new Intent(this, CountingTaskInputActivity.class);
        Bundle bundle = new Bundle();
        bundle.putSerializable(COUNTING_TASK_PER_RESULT_DATA_CODE, data);
        bundle.putSerializable(ALL_UNCHECKED_RESULT, (Serializable) uncheckedResultData);
        bundle.putSerializable(ALL_CHECKED_RESULT, (Serializable) checkedResultData);
        intent.putExtras(bundle);
        startActivity(intent);
    }

    private void doEditResult(CountingTaskResultData data) {
        Intent intent = new Intent(this, CountingTaskInputActivity.class);
        Bundle bundle = new Bundle();
        bundle.putSerializable(COUNTING_TASK_PER_RESULT_DATA_CODE, data);
        bundle.putSerializable(ALL_UNCHECKED_RESULT, (Serializable) uncheckedResultData);
        bundle.putSerializable(ALL_CHECKED_RESULT, (Serializable) checkedResultData);
        intent.putExtra(ARG_INPUT_MODE, ARG_MODE_EDIT);
        intent.putExtras(bundle);
        startActivity(intent);
    }
    //endRegion

    //region Thread
    private void finishCountingTaskThread() {
        ThreadStart(new ThreadHandler<Boolean>(){
            @Override
            public void onPrepare() throws Exception {
                startProgressDialog(getString(R.string.common_progress_content), ProgressType.SPINNER);
            }

            @Override
            public Boolean onBackground() throws Exception {
                countingTaskManager.finishCountingTask(resultData.countingId);
                return true;
            }

            @Override
            public void onError(Exception e) {
                showErrorDialog(e);
            }

            @Override
            public void onSuccess(Boolean data) throws Exception {
                dismissProgressDialog();
            }

            @Override
            public void onFinish() {
                moveToTaskSwitcherActivity();
            }
        });
    }
    //endregion
}
