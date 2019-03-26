package com.gin.wms.warehouse.administrator;

import android.app.AlertDialog;
import android.app.Dialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.LinearLayoutManager;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.gin.ngemart.baseui.NgemartActivity;
import com.gin.ngemart.baseui.adapter.NgemartRecyclerView;
import com.gin.ngemart.baseui.adapter.NgemartRecyclerViewAdapter;
import com.gin.ngemart.baseui.component.NgemartCardView;
import com.gin.wms.manager.EquipmentManager;
import com.gin.wms.manager.db.data.EquipmentData;
import com.gin.wms.manager.db.data.OperatorEquipmentData;
import com.gin.wms.manager.db.data.OperatorStatusData;
import com.gin.wms.warehouse.R;
import com.gin.wms.warehouse.base.WarehouseActivity;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

public class EquipmentUpdateActivity extends WarehouseActivity implements NgemartCardView.CardListener<EquipmentData>, View.OnClickListener{
    private EditText etIdOperator, etNameOperator;
    private CheckBox chk_handpallet, chk_forklif;
    private Button btnAddEquipment;
    private TextView txtEquipmentIdHandpallet, txtEquipmentTypeIdHandpallet, txtEquipmentIdForklif, txtEquipmentTypeIdForklif;
    private OperatorStatusData resulData;
    private OperatorEquipmentData operatorEquipmentDataForHandpallet;
    private OperatorEquipmentData operatorEquipmentDataForForklif;
    private EquipmentManager equipmentManager;
    private List<OperatorStatusData> uncheckedResultData = new ArrayList<>();
    private List<OperatorStatusData> checkedResultData = new ArrayList<>();
    private List<EquipmentData> forklif = new ArrayList<>();
    private List<EquipmentData> handpallet = new ArrayList<>();
    private List<OperatorEquipmentData> getAllEquipmentData = new ArrayList<>();
    private Dialog dialogs;
    private Dialog dialogs_forklif;
    private int count;
    private static final int PRIORITY_FORKLIF= 1;
    private static final int PRIORITY_HANDPALLET = 2;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        try{
            super.onCreate(savedInstanceState);
            setContentView(R.layout.activity_equipment_update);
            equipmentManager = new EquipmentManager(this);
            getDataFromIntent();
            init();
            showDataToUI();
        }catch (Exception e){
            showErrorDialog(e);
        }
    }

    @Override
    public void onClick(View v) {
        try{
            switch (v.getId()){
                case R.id.btnAddEquipment:
//                    String operatorId=etIdOperator.getText().toString();
//                    String equipmentIdHandpallet=txtEquipmentIdHandpallet.getText().toString();
//                    String equipmentidForklif=txtEquipmentIdForklif.getText().toString();
//                    String equipmentTypeIdHandpallet =txtEquipmentTypeIdHandpallet.getText().toString();
//                    String equipmentTypeIdForklif = txtEquipmentTypeIdForklif.getText().toString();
//                    int priorityForklif = 1;
//                    int priorityHandpallet = 2;

//                    if(txtEquipmentIdHandpallet.getText().toString().isEmpty() && txtEquipmentTypeIdHandpallet.getText().toString().isEmpty()){
//                        showErrorDialog(getResources().getString(R.string.msg_input_handpallet_empty));
//                        return;
//                    }
//                    if(txtEquipmentIdForklif.getText().toString().isEmpty() && txtEquipmentTypeIdForklif.getText().toString().isEmpty()){
//                        showErrorDialog(getResources().getString(R.string.msg_input_forklif_empty));
//                        return;
//                    }
                    if(chk_handpallet.isChecked() && chk_forklif.isChecked()){
                        operatorEquipmentDataForHandpallet = prepareDataHandpallet();
                        //saveEquipmentConfirmation(operatorEquipmentDataForHandpallet);
                        operatorEquipmentDataForForklif = prepareDataForklif();
                        // saveEquipmentForkliftConfirmation(operatorEquipmentDataForForklif);
                        saveEquipmentForkliftHandpalletConfirmation(operatorEquipmentDataForHandpallet,operatorEquipmentDataForForklif);
                        return;
                    }
                    else if(chk_handpallet.isChecked()){
                        operatorEquipmentDataForHandpallet = prepareDataHandpallet();
                        saveEquipmentConfirmation(operatorEquipmentDataForHandpallet);
                    }
                    else if(chk_forklif.isChecked()){
                        operatorEquipmentDataForForklif = prepareDataForklif();
                        saveEquipmentForkliftConfirmation(operatorEquipmentDataForForklif);
                    }

//                    else{

//                    }
                    break;
            }
        }catch (Exception e){
            showErrorDialog(e);
        }
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()){
            case android.R.id.home:
                moveBackToEquipmentListActivity();
                break;
            default:
                return super.onOptionsItemSelected(item);
        }
        return true;
    }

    @Override
    public void onBackPressed() {
        moveBackToEquipmentListActivity();
    }

    public void onCheckboxClicked(View view) {
        // Is the view now checked?
        boolean checked = ((CheckBox) view).isChecked();
        // Check which checkbox was clicked
        switch(view.getId()) {
            case R.id.chk_handpallet:
                if (checked){
                    count= checkedCount(chk_handpallet,count);
                    showChooseListHandPallet();
                    return;
                }
            else
                    count= checkedCount(chk_handpallet,count);
                    clearUIHandpallet();
                break;
            case R.id.chk_forklif:
                if (checked){
                    count= checkedCount(chk_forklif,count);
                    showChooseListForklif();
                    return;
                }
            else
                    count= checkedCount(chk_forklif,count);
                    clearUIForklif();
                break;

        }
    }

    private void init() throws Exception{
        initToolbar();
        justBindingProperties();
        setButtonDisable();
    }

    private void justBindingProperties(){
        etIdOperator=findViewById(R.id.etIdOperator);
        etNameOperator = findViewById(R.id.etNameOperator);
        chk_forklif = findViewById(R.id.chk_forklif);
        chk_handpallet =findViewById(R.id.chk_handpallet);
        txtEquipmentIdHandpallet = findViewById(R.id.txtEquipmentIdHandpallet);
        txtEquipmentTypeIdHandpallet = findViewById(R.id.txtEquipmentTypeIdHandpallet);
        txtEquipmentIdForklif = findViewById(R.id.txtEquipmentIdForklif);
        txtEquipmentTypeIdForklif = findViewById(R.id.txtEquipmentTypeIdForklif);
        btnAddEquipment = findViewById(R.id.btnAddEquipment);
        btnAddEquipment.setOnClickListener(this);
        dialogs= new Dialog(this);
        dialogs_forklif = new Dialog(this);
    }

    private void setButtonDisable(){
        etIdOperator.setEnabled(false);
        etNameOperator.setEnabled(false);
//        chk_handpallet.setEnabled(false);
//        chk_forklif.setEnabled(false);
    }

    private void initToolbar() {
        android.support.v7.widget.Toolbar toolbar = findViewById(R.id.toolbarUpdateEquipment);
        toolbar.setTitle(getResources().getString(R.string.toolbar_update_list_equipment));
        setSupportActionBar(toolbar);
        if(getSupportActionBar() != null)
            getSupportActionBar().setDisplayHomeAsUpEnabled(true);
    }

    private void showChooseListHandPallet(){
        //dialogs.setTitle("Pilih Jenis HandPallet");
        dialogs.setCancelable(false);
        dialogs.setContentView(R.layout.activity_equipment_handpallet_list);
        final NgemartRecyclerView rvHandpallet = (NgemartRecyclerView) dialogs.findViewById(R.id.rvEquipmentHandpallet);
        NgemartRecyclerViewAdapter<List<EquipmentData>> adapter;
        adapter = new NgemartRecyclerViewAdapter(this, R.layout.card_equipment_handpallet, handpallet);
        adapter.setHasStableIds(true);
        adapter.setRecyclerListener(this);
        rvHandpallet.setAdapter(adapter);
        NgemartRecyclerView.LayoutManager layoutManager= new LinearLayoutManager(this);
        rvHandpallet.setLayoutManager(layoutManager);
        dialogs.show();
    }

    private void showChooseListForklif(){
//        AlertDialog.Builder dialog = new AlertDialog.Builder(this);
//        dialog.setTitle("Pilih Jenis Forklif");
//        dialog.create().show();
        dialogs_forklif.setCancelable(false);
        dialogs_forklif.setContentView(R.layout.activity_equipment_forklif_list);
        final NgemartRecyclerView rvForklif = (NgemartRecyclerView) dialogs_forklif.findViewById(R.id.rvEquipmentForklif);
        NgemartRecyclerViewAdapter<List<EquipmentData>> adapter;
        adapter = new NgemartRecyclerViewAdapter(this, R.layout.card_equipment_forklif, forklif);
        adapter.setHasStableIds(true);
        adapter.setRecyclerListener(this);
        rvForklif.setAdapter(adapter);
        NgemartRecyclerView.LayoutManager layoutManager = new LinearLayoutManager(this);
        rvForklif.setLayoutManager(layoutManager);
        dialogs_forklif.show();
    }

    private void getDataFromIntent(){
        Bundle bundle = getIntent().getExtras();
        if(bundle !=null){
            resulData = (OperatorStatusData) bundle.getSerializable(EquipmentListActivity.EQUIPMENT_TASK_PER_RESULT_DATA_CODE);
            uncheckedResultData = (List<OperatorStatusData>) bundle.getSerializable(EquipmentListActivity.ALL_UNCHECKED_RESULT);
            forklif = (List<EquipmentData>) bundle.getSerializable(EquipmentListActivity.FORKLIFT);
            handpallet = (List<EquipmentData>) bundle.getSerializable(EquipmentListActivity.HANDPALLET);
//            uncheckedResultData =(List<OperatorStatusData>)bundle.getSerializable()
        }
    }

    private void showDataToUI() {
        etIdOperator.setText(resulData.operatorId);
        etNameOperator.setText(resulData.operatorName);
        setCheckButtonEnable();
    }

    private void setCheckButtonEnable(){
//        List<Boolean> checkedResult =new ArrayList<>();
//        for(EquipmentData result: forklif){
//        }
        if(forklif.size()==0){
            chk_forklif.setEnabled(false);
            btnAddEquipment.setEnabled(false);
        }
        else if(handpallet.size() == 0){
            chk_handpallet.setEnabled(false);
            btnAddEquipment.setEnabled(false);
        }
        else{
            btnAddEquipment.setEnabled(false);
        }
    }

    private void setCheckAfterInput(){
        btnAddEquipment.setEnabled(false);
        chk_forklif.setChecked(false);
        chk_handpallet.setChecked(false);
        count= checkedCount(chk_handpallet,count);
        count= checkedCount(chk_forklif,count);
    }

    private void setCheckAfterInputHandpallet(){
        btnAddEquipment.setEnabled(false);
        chk_handpallet.setChecked(false);
        count= checkedCount(chk_handpallet,count);
    }

    private void setCheckAfterInputForklif(){
        btnAddEquipment.setEnabled(false);
        chk_forklif.setChecked(false);
        count= checkedCount(chk_forklif,count);
    }

    @Override
    public void onCardClick(int position, View view, EquipmentData data) {
        try{
            if(view.getId() == R.id.btnAddHandpallet){
                setDataToFieldHandpallet(data);
                dialogs.dismiss();
            }
            else if(view.getId() == R.id.btnAddForklif){
                setDataToFieldForkliff(data);
                dialogs_forklif.dismiss();
            }
        }catch (Exception e){
            showErrorDialog(e);
        }
    }

    private void setDataToFieldHandpallet(EquipmentData data){
        txtEquipmentIdHandpallet.setText(data.equipmentId);
        txtEquipmentTypeIdHandpallet.setText(data.equipmentTypeId);
    }

    private void setDataToFieldForkliff(EquipmentData data){
        txtEquipmentIdForklif.setText(data.equipmentId);
        txtEquipmentTypeIdForklif.setText(data.equipmentTypeId);
    }

    private void moveBackToEquipmentListActivity(){
        Intent intent = new Intent(this, EquipmentListActivity.class);
        Bundle bundle = new Bundle();
        bundle.putSerializable(EquipmentListActivity.ALL_UNCHECKED_RESULT, (Serializable) uncheckedResultData);
        bundle.putSerializable(EquipmentListActivity.EQUIPMENT_TASK_PER_RESULT_DATA_CODE, resulData);
        //bundle.putSerializable(EquipmentListActivity.EQUIPMENT_INFO_ALL, (Serializable) getAllEquipmentData);
        intent.putExtras(bundle);
        startActivity(intent);
    }

    private void clearUIForklif(){
        txtEquipmentIdForklif.setText("");
        txtEquipmentTypeIdForklif.setText("");
    }

    private void clearUIHandpallet(){
        txtEquipmentIdHandpallet.setText("");
        txtEquipmentTypeIdHandpallet.setText("");
    }

    private void saveEquipmentConfirmation(OperatorEquipmentData operatorEquipmentData) {
       // double qty = Double.parseDouble(qtyFromInput);
        DialogInterface.OnClickListener okListener = saveResult(operatorEquipmentData);
        showAskDialog(getString(R.string.common_confirmation_title), getResources().getString(R.string.msg_ask_to_save_data_equipment_handpallet), okListener, null);
    }

    private DialogInterface.OnClickListener saveResult(OperatorEquipmentData operatorEquipmentData) {
        return (dialog, which) -> {
            try {
                saveResultThread(operatorEquipmentData);
              //  getListEquipmentInfo();
            } catch (Exception e) {
                showErrorDialog(e);
            }
        };
    }

    private void saveResultThread(OperatorEquipmentData operatorEquipmentData) {
        ThreadStart(new ThreadHandler<Boolean>() {

            @Override
            public void onPrepare() throws Exception {
                startProgressDialog(getString(R.string.common_progress_content), ProgressType.SPINNER);
            }

            @Override
            public Boolean onBackground() throws Exception {
                equipmentManager.SaveOperatorEquipment(operatorEquipmentData);
                return true;
            }

            @Override
            public void onError(Exception e) {
                //resulData.hasChecked = false;
                showErrorDialog(e);
            }

            @Override
            public void onSuccess(Boolean data) throws Exception {
                //resulData.hasChecked = true;
                clearAllHandpallet();
                showAlertToast(getResources().getString(R.string.msg_data_has_been_saved));
            }

            @Override
            public void onFinish() {
                dismissProgressDialog();
            }
        });
    }

    private void clearAll(){
        setCheckAfterInput();
        clearUIHandpallet();
        clearUIForklif();
    }

    private void clearAllHandpallet(){
        setCheckAfterInputHandpallet();
        clearUIHandpallet();
        clearUIForklif();
    }

    private void clearAllForklif(){
        setCheckAfterInputForklif();
        clearUIHandpallet();
        clearUIForklif();
    }


    private int checkedCount(CheckBox checkBox, int count){
        if(checkBox.isChecked()){
            count++;
        }
        else if(!checkBox.isChecked()){
            count--;
        }
        if(count == 0){
            btnAddEquipment.setEnabled(false);
        }
        if (count == 1){
            btnAddEquipment.setEnabled(true);
        }
        if (count == 2){
            btnAddEquipment.setEnabled(true);
        }
        return count;
    }

    private void saveEquipmentForkliftConfirmation(OperatorEquipmentData operatorEquipmentData) {
        // double qty = Double.parseDouble(qtyFromInput);
        DialogInterface.OnClickListener okListener = saveResultForklift(operatorEquipmentData);
        showAskDialog(getString(R.string.common_confirmation_title), getResources().getString(R.string.msg_ask_to_save_data_equipment_forklif), okListener, null);
    }

    private DialogInterface.OnClickListener saveResultForklift(OperatorEquipmentData operatorEquipmentData) {
        return (dialog, which) -> {
            try {
                saveResultForkliftThread(operatorEquipmentData);
                //getListEquipmentInfo();
            } catch (Exception e) {
                showErrorDialog(e);
            }
        };
    }

    private void saveResultForkliftThread(OperatorEquipmentData operatorEquipmentData) {
        ThreadStart(new ThreadHandler<Boolean>() {

            @Override
            public void onPrepare() throws Exception {
                startProgressDialog(getString(R.string.common_progress_content), ProgressType.SPINNER);
            }

            @Override
            public Boolean onBackground() throws Exception {
                equipmentManager.SaveOperatorEquipment(operatorEquipmentData);
                return true;
            }

            @Override
            public void onError(Exception e) {
                showErrorDialog(e);
            }

            @Override
            public void onSuccess(Boolean data) throws Exception {
                clearAllForklif();
                showAlertToast(getResources().getString(R.string.msg_data_has_been_saved));
            }

            @Override
            public void onFinish() {
                dismissProgressDialog();
            }
        });
    }

    private void saveEquipmentForkliftHandpalletConfirmation(OperatorEquipmentData operatorEquipmentDataHandpallet, OperatorEquipmentData operatorEquipmentDataForForklif) {
        // double qty = Double.parseDouble(qtyFromInput);
        DialogInterface.OnClickListener okListener = saveResultHandPalletForklift(operatorEquipmentDataHandpallet, operatorEquipmentDataForForklif);
        showAskDialog(getString(R.string.common_confirmation_title), getResources().getString(R.string.msg_ask_to_save_data_equipment), okListener, null);
    }

    private DialogInterface.OnClickListener saveResultHandPalletForklift(OperatorEquipmentData operatorEquipmentDataHandpallet, OperatorEquipmentData operatorEquipmentDataForForklif) {
        return (dialog, which) -> {
            try {
                saveResultHandpalletForkliftThread(operatorEquipmentDataHandpallet, operatorEquipmentDataForForklif);
                //getListEquipmentInfo();
            } catch (Exception e) {
                showErrorDialog(e);
            }
        };
    }

    private void saveResultHandpalletForkliftThread(OperatorEquipmentData operatorEquipmentDataHandpallet, OperatorEquipmentData operatorEquipmentDataForForklif) {
        ThreadStart(new ThreadHandler<Boolean>() {

            @Override
            public void onPrepare() throws Exception {
                startProgressDialog(getString(R.string.common_progress_content), ProgressType.SPINNER);
            }

            @Override
            public Boolean onBackground() throws Exception {
                equipmentManager.SaveOperatorEquipment(operatorEquipmentDataHandpallet);
                equipmentManager.SaveOperatorEquipment(operatorEquipmentDataForForklif);
                return true;
            }

            @Override
            public void onError(Exception e) {
                showErrorDialog(e);
            }

            @Override
            public void onSuccess(Boolean data) throws Exception {
                clearAll();
                showAlertToast(getResources().getString(R.string.msg_data_has_been_saved));
            }

            @Override
            public void onFinish() {
                dismissProgressDialog();
            }
        });
    }

    private OperatorEquipmentData prepareDataHandpallet() throws Exception{
        OperatorEquipmentData item = new OperatorEquipmentData();
        item.operatorId = etIdOperator.getText().toString();
        item.equipmentId =txtEquipmentIdHandpallet.getText().toString();
        item.equipmentTypeId = txtEquipmentTypeIdHandpallet.getText().toString();
        item.priority = PRIORITY_HANDPALLET;
        return item;
    }

    private OperatorEquipmentData prepareDataForklif() throws Exception{
        OperatorEquipmentData item = new OperatorEquipmentData();
        item.operatorId = etIdOperator.getText().toString();
        item.equipmentId =txtEquipmentIdForklif.getText().toString();
        item.equipmentTypeId = txtEquipmentTypeIdForklif.getText().toString();
        item.priority = PRIORITY_FORKLIF;
        return item;
    }

}
