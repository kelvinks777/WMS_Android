package com.gin.wms.warehouse.checker

import android.content.Context
import android.support.v4.content.ContextCompat
import android.util.AttributeSet
import android.view.View
import com.bosnet.ngemart.libgen.Common.GetNumberWithoutFractionFormat
import com.gin.ngemart.baseui.component.NgemartCardView
import com.gin.wms.manager.CheckerTaskManager
import com.gin.wms.manager.db.data.CheckerTaskData
import com.gin.wms.manager.db.data.CheckerTaskItemData
import com.gin.wms.manager.db.data.CheckerTaskItemResultData
import com.gin.wms.manager.db.data.enums.RefDocUriEnum
import com.gin.wms.warehouse.R
import kotlinx.android.synthetic.main.card_checker_task_detail.view.*

/**
 * Created by manbaul on 3/14/2018.
 */
class CheckerTaskDetailCard:NgemartCardView<CheckerTaskItemData>{
    private var checkerTaskManager: CheckerTaskManager? = null
    private var checkerTaskData: CheckerTaskData? = null
    private var onPutaway: Boolean? = false

    constructor(context: Context) : super(context) {}

    constructor(context: Context, attrs: AttributeSet) : super(context, attrs) {}

    constructor(context: Context, attrs: AttributeSet, defStyleAttr: Int) : super(context, attrs, defStyleAttr) {}

    override fun setData(data: CheckerTaskItemData?) {
        try {
            super.setData(data)
            checkerTaskManager = CheckerTaskManager(cardContext)
            checkerTaskData = checkerTaskManager?.getLocalCheckerTaskData(data?.taskId)
            setDataToUi(data)
            setOnclickListener(data)
        } catch (e: Exception) {
            ShowError(e)
        }
    }

    private fun setDataToUi(data: CheckerTaskItemData?){
        data?.let {
            tvProductName.text = it.productName
            tvClientId.text = it.clientId
            tvClientLocationId.text = it.clientLocationId
            tvQty.text = it.compUomValueFromQty
            tvPalletQty.text = GetNumberWithoutFractionFormat().format(it.palletQty)
            tvGoodQty.text = it.compUomValueFromGoodCheckResultQty
            tvBadQty.text = it.compUomValueFromBadCheckResultQty
            tvPalletConversion.text = GetNumberWithoutFractionFormat().format(it.palletConversionValue)
            tvCompUom.text = it.compUomId
            tvPalletResult.text = GetNumberWithoutFractionFormat()
                    .format(it.results
                            .groupBy { checkerTaskItemResultData: CheckerTaskItemResultData? -> checkerTaskItemResultData?.palletNo }
                            .keys.size)
        }

        checkDataIsAlreadyChecked(data)
    }

    private fun checkDataIsAlreadyChecked(data: CheckerTaskItemData?){
        if(data?.results?.size != 0){
            onPutaway = data?.results?.get(0)?.alreadyUsed
        }

        if(data != null){
            if (data.badQtyCheckResult + data.goodQtyCheckResult > 0.00 && data.badQtyCheckResult + data.goodQtyCheckResult >= data.qty){
                if(onPutaway == false){
                    if(checkerTaskData?.refDocUri == RefDocUriEnum.RECEIVING.value) {
                        btnPutaway.visibility = View.VISIBLE
                    }
                    tvCheckedInfo.visibility = View.VISIBLE
                    tvCheckedInfo.text = "Checked"
                    data.hasChecked = true
                }
                if(onPutaway == true) {
                    tvCheckedInfo.visibility = View.VISIBLE
                    btnPutaway.visibility = View.GONE
                    tvCheckedInfo.text = "On Putaway"
                    data.hasChecked = true
                }
                tvCheckedInfo.setBackgroundColor(ContextCompat.getColor(cardActivity, R.color.colorClickable))
            }

            else {
                tvCheckedInfo.visibility = View.VISIBLE
                tvCheckedInfo.text = ""

                tvCheckedInfo.setBackgroundColor(ContextCompat.getColor(cardActivity, R.color.white))
            }
        }
    }

    private fun setOnclickListener(data: CheckerTaskItemData?){
        setOnClickListener { view ->
            cardListener?.onCardClick(verticalScrollbarPosition, view, data)
        }

        btnPutaway.setOnClickListener {
            cardListener?.onCardClick(verticalScrollbarPosition, btnPutaway, data)
        }
    }

    /*inner class getCheckerTaskData: AsyncTask<String, Void, AsyncTaskResult<CheckerTaskData>?>(){

        override fun doInBackground(vararg params: String?): AsyncTaskResult<CheckerTaskData>? {
            val result: AsyncTaskResult<CheckerTaskData>? = AsyncTaskResult()


            return result
        }

        override fun onPostExecute(result: AsyncTaskResult<CheckerTaskData>?) {
            setDataToUi(data)
        }
    }*/
}