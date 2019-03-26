package com.gin.wms.warehouse.administrator;

import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.support.v7.widget.Toolbar;

import com.bosnet.ngemart.libgen.Common;
import com.gin.ngemart.baseui.NgemartActivity;
import com.gin.wms.manager.OperatorStatusTaskManager;
import com.gin.wms.manager.TokenLocalManager;
import com.gin.wms.manager.UserManager;
import com.gin.wms.manager.WarehouseProblemManager;
import com.gin.wms.manager.db.data.OperatorStatusData;
import com.gin.wms.manager.db.data.UserData;
import com.gin.wms.manager.db.data.WarehouseProblemData;
import com.gin.wms.warehouse.R;
import com.gin.wms.warehouse.base.WarehouseActivity;
import com.gin.wms.warehouse.component.LeftMenuModule;
import com.gin.wms.warehouse.login.LoginActivity;
import com.gin.wms.warehouse.operator.Order.ManualMutationOrderActivity;
import com.gin.wms.warehouse.operator.Counting.StockCountingOrderProductActivity;
import com.gin.wms.warehouse.operator.Counting.StockCountingOrderActivity;


import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.ListView;
import android.widget.Toast;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

public class AdministratorActivity extends WarehouseActivity implements  LeftMenuModule.ILeftMenu, DialogInterface.OnClickListener {

    private LeftMenuModule leftMenuModule;
    private Toolbar toolbar;
    private String[] adminmenu = {
            "Create Manual Mutation", "Create Stockcounting Order Manual", "Equipment","Warehouse Problem"
    };
    private UserData userdata;
    private UserManager userManager;
    private TokenLocalManager tokenLocalManager;
    private ListView listview;
    private WarehouseProblemManager warehouseProblemManager;
    private OperatorStatusTaskManager operatorStatusTaskManager;
    private List<WarehouseProblemData> listWarehouseProblemData = new ArrayList<>();
    private List<OperatorStatusData> listOperatorStatusData = new ArrayList<>();
    private Boolean isPressedTwice =false;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        try{
            super.onCreate(savedInstanceState);
            setContentView(R.layout.activity_administrator_main);
            initObject();
            initComponent();
        }catch (Exception e){
            showErrorDialog(e);
        }
    }

    private void initObject(){
        try {
            userManager = new UserManager(this);
            tokenLocalManager = new TokenLocalManager(this);
            userdata =  userManager.GetUserDataFromLocal();
            warehouseProblemManager = new WarehouseProblemManager(this);
            operatorStatusTaskManager = new OperatorStatusTaskManager(this);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    private void initComponent(){
        initToolbar();
        justBindingProperties();
        initLeftMenuModule();
        initAdapter();
    }

    private void justBindingProperties(){
        listview = findViewById(R.id.List);
    }

    private void initToolbar(){
        toolbar = findViewById(R.id.toolbarMainAdministrator);
        toolbar.setTitle(getResources().getString(R.string.toolbar_administrator_menu));
        setSupportActionBar(toolbar);
        if(getSupportActionBar() != null)
            getSupportActionBar().setDisplayHomeAsUpEnabled(true);
    }

    private void initLeftMenuModule(){
        leftMenuModule =  new LeftMenuModule(this, toolbar);
        leftMenuModule.setLeftMenuListener(this);
        leftMenuModule.setAppVersion(Common.GetVersionInfo(this));
        leftMenuModule.setProfileInfo(userdata);
    }

    private void initAdapter(){

        List<String> menu = new ArrayList<String>(Arrays.asList((adminmenu)));

        ArrayAdapter adapter = new ArrayAdapter<String>(this,
                android.R.layout.simple_list_item_1, menu);
        listview.setAdapter(adapter);

        listview.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                //Intent intent;
                switch (position){
                    case 0:
                        showConfirmToManualMutation();
                        break;
                    case 1:
                        showConfirmToStockCounting();
                        break;
                    case 2:
//                        Toast.makeText(AdministratorActivity.this, "Equipment", Toast.LENGTH_SHORT).show();
                        showConfirmToEquipment();
                        break;
                    case 3:
                        showConfirmToWarehouseProblem();
                }
            }
        });
    }

    @Override
    public void onBackPressed() {
        leftMenuModule.closeDrawer();
    }


    @Override
    public boolean onMenuItemSelected(int itemId) throws Exception {
        if(itemId == R.id.nav_administrator_logout)
            showAskDialog(getString(R.string.common_confirmation_title), getString(R.string.common_close_app_confirmation_content), this, null);
        return true;
    }

    @Override
    public void onHandleBackPressed() {
        if (isPressedTwice) {
            Intent homeIntent = new Intent(Intent.ACTION_MAIN);
            homeIntent.addCategory(Intent.CATEGORY_HOME);
            homeIntent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
            startActivity(homeIntent);
        }
        isPressedTwice = true;
        showAlertToast(getString(R.string.common_double_tap_for_close));
        Handler handler = new Handler();
        handler.postDelayed(new Runnable() {
            @Override
            public void run() {
                isPressedTwice = false;
            }
        },2000);
    }

    private void showConfirmToManualMutation() {
        String title = getResources().getString(R.string.common_information_title);
        String body = "Apakah Anda Ingin Menuju Menu Create Manual Mutation ?";
        showAskDialog(title, body,
                (dialog, which) -> goToManualMutation(),
                (dialog, which) -> dialog.dismiss());
    }

//    private  void coba(){
//        final CharSequence[] items = {"Red", "Green", "Blue"};
//        AlertDialog.Builder dialogBuilder = new AlertDialog.Builder(this);
//        dialogBuilder.setTitle("Pick a color");
//        dialogBuilder.setItems(items, new DialogInterface.OnClickListener() {
//
//            public void onClick(DialogInterface dialog, int which) {
//                // Do anything you want here
//            }
//
//        });
//        dialogBuilder.create().show();
//    }

    private void showConfirmToStockCounting() {
        String title = getResources().getString(R.string.common_selected_title);
        //String body = "Apakah Anda Ingin Menuju Menu Create StockCounting Order Manual ?";
         final CharSequence[] items = {
                "Berdasarkan Product ID", "Berdasarkan Bin ID"
        };
        DialogInterface.OnClickListener okListener = goToStockCountingOrder();
        showAskDialogItems(title, items,
                okListener,
               null);
    }

    private void showConfirmToWarehouseProblem(){
        String title = getResources().getString(R.string.common_information_title);
        String body = "Apakah Anda Ingin Menuju Menu Warehouse Problem ?";
        showAskDialog(title, body,
                (dialog, which) -> getListWarehouseroblem(),
                (dialog, which) -> dialog.dismiss());
    }

    private void showConfirmToEquipment(){
        String title =  getResources().getString(R.string.common_information_title);
        String body = getResources().getString(R.string.ask_to_start_equipment_administrator);

        showAskDialog(title, body,
                (dialog, which) -> getListEquipmentAdministrator(),
                (dialog, which) -> dialog.dismiss());

    }

    public DialogInterface.OnClickListener goToStockCountingOrder(){
        return new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                try {
                    switch (which){
                        case 0:
                            goToStockCountingOrderProduct();
                            break;
                        case 1:
                            goToStockCountingOrderBin();
                            break;
                    }

                } catch (Exception e) {
                    showErrorDialog(e);
                }
            }
        };
    }

    private void goToManualMutation(){
        Intent intent = new Intent(this, ManualMutationOrderActivity.class);
        startActivity(intent);
    }

    private void goToWarehouseProblem(List<WarehouseProblemData> data){
        Intent intent = new Intent(this, WarehouseProblemListActivity.class);
        Bundle bundle = new Bundle();
        bundle.putSerializable(WarehouseProblemListActivity.LIST_WAREHOUSEPROBLEM, (Serializable) data);
        intent.putExtras(bundle);
        startActivity(intent);
    }

    private void goToStockCountingOrderProduct(){
        Intent intent = new Intent(this, StockCountingOrderProductActivity.class);
        startActivity(intent);
    }

    private void goToStockCountingOrderBin(){
        Intent intent = new Intent(this, StockCountingOrderActivity.class);
        startActivity(intent);
    }

    private void goToEquipment(List<OperatorStatusData> data){
        Intent intent = new Intent(this, EquipmentListActivity.class);
        Bundle bundle = new Bundle();
        bundle.putSerializable(EquipmentListActivity.ALL_UNCHECKED_RESULT, (Serializable) data);
        intent.putExtras(bundle);
        startActivity(intent);
    }

    @Override
    public void onClick(DialogInterface dialog, int which) {
        ThreadStart(new ThreadHandler<Boolean>() {

            @Override
            public void onPrepare() throws Exception {
                startProgressDialog(getString(R.string.common_close_app_progress_content), ProgressType.SPINNER);
            }

            @Override
            public Boolean onBackground() throws Exception {
                tokenLocalManager.SaveNgToken("", "");
                userManager.SignOut();
                return true;
            }

            @Override
            public void onError(Exception e) {
                progressDialog.dismiss();
                showErrorDialog(e);
            }

            @Override
            public void onSuccess(Boolean data) throws Exception {
                progressDialog.dismiss();
                Intent intent= new Intent(getApplicationContext(), LoginActivity.class);
                intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK | Intent.FLAG_ACTIVITY_NEW_TASK);
                startActivity(intent);
                finish();
            }

            @Override
            public void onFinish() {
                progressDialog.dismiss();
            }
        });
    }

    private void getListWarehouseroblem(){
        ThreadStart(new ThreadHandler<List<WarehouseProblemData>>() {
            @Override
            public void onPrepare() throws Exception {
                startProgressDialog(getString(R.string.progress_get_warehouse_problem), NgemartActivity.ProgressType.SPINNER);
            }

            @Override
            public List<WarehouseProblemData> onBackground() throws Exception {
                listWarehouseProblemData = warehouseProblemManager.GetWarehouseProblem();
                return listWarehouseProblemData;
            }

            @Override
            public void onError(Exception e) {
                dismissProgressDialog();
                showErrorDialog(e);
            }

            @Override
            public void onSuccess(List<WarehouseProblemData> data) throws Exception {
                if(data != null){
                    goToWarehouseProblem(data);
                }
            }

            @Override
            public void onFinish() {
                dismissProgressDialog();
            }
        });
    }

    private void getListEquipmentAdministrator(){
        ThreadStart(new ThreadHandler<List<OperatorStatusData>>() {

            @Override
            public void onPrepare() throws Exception {
                startProgressDialog(getString(R.string.progress_get_warehouse_problem), NgemartActivity.ProgressType.SPINNER);
            }

            @Override
            public List<OperatorStatusData> onBackground() throws Exception {
                listOperatorStatusData = operatorStatusTaskManager.getListOperatorStatus();
                return listOperatorStatusData;
            }

            @Override
            public void onError(Exception e) {
                dismissProgressDialog();
                showErrorDialog(e);
            }

            @Override
            public void onSuccess(List<OperatorStatusData> data) throws Exception {
                if(data != null){
                    goToEquipment(data);
                }
            }

            @Override
            public void onFinish() {
                dismissProgressDialog();
            }
        });
    }


}
