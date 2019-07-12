package com.lyft.sdk.testing

import android.os.Bundle
import android.view.View
import android.widget.AdapterView
import android.widget.ArrayAdapter
import android.widget.Button
import android.widget.TextView
import androidx.appcompat.app.AppCompatActivity
import androidx.appcompat.widget.AppCompatSpinner
import com.lyft.deeplink.RideTypeEnum
import com.lyft.networking.apiObjects.EtaEstimateResponse
import com.lyft.networking.apiObjects.RideTypesResponse
import com.lyft.sdk.R
import com.lyft.sdk.testing.helpers.retrofitCallBack
import com.lyft.sdk.testing.ui.SampleData
import com.lyft.sdk.testing.ui.SpinnerPairItem
import retrofit2.Response

class RideTypeApiTestActivity: AppCompatActivity() {

    private lateinit var rideTypeSpinner: AppCompatSpinner
    private lateinit var apiCallButton: Button
    private lateinit var apiResultTextView: TextView
    private lateinit var loadingLayout: View

    private val rideTypeSpinnerList = listOf(
            SpinnerPairItem("All Ride Types", null),
            SpinnerPairItem(RideTypeEnum.SHARED.displayName, RideTypeEnum.SHARED),
            SpinnerPairItem(RideTypeEnum.STANDARD.displayName, RideTypeEnum.STANDARD),
            SpinnerPairItem(RideTypeEnum.XL.displayName, RideTypeEnum.XL),
            SpinnerPairItem(RideTypeEnum.LUX.displayName, RideTypeEnum.LUX),
            SpinnerPairItem(RideTypeEnum.LUX_BLACK.displayName, RideTypeEnum.LUX_BLACK),
            SpinnerPairItem(RideTypeEnum.LUX_BLACK_XL.displayName, RideTypeEnum.LUX_BLACK_XL)
    )

    private val lyftApi = TestLyftApiFactory().getRetrofitClient()
    private var selectedRideType: RideTypeEnum? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_test_ridetypes_api)

        initViews()
        setupSpinner()
        setupButton()
    }

    private fun initViews() {
        rideTypeSpinner = findViewById(R.id.ridetype_api_test_ride_type_spinner)
        apiCallButton = findViewById(R.id.ridetype_api_test_make_call_button)
        apiResultTextView = findViewById(R.id.ridetype_api_test_result_textview)
        loadingLayout = findViewById(R.id.ridetype_api_test_loading_layout)
    }

    private fun setupSpinner() {
        val spinnerAdapter = ArrayAdapter(this, android.R.layout.simple_spinner_item, rideTypeSpinnerList)
        spinnerAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item)
        rideTypeSpinner.adapter = spinnerAdapter
        rideTypeSpinner.onItemSelectedListener = object: AdapterView.OnItemSelectedListener {
            override fun onNothingSelected(parent: AdapterView<*>?) {
                //Do nothing
            }

            override fun onItemSelected(parent: AdapterView<*>?, view: View?, position: Int, id: Long) {
                selectedRideType = rideTypeSpinnerList[position].data
            }
        }
    }

    private fun setupButton() {
        apiCallButton.setOnClickListener {
            val call = lyftApi.getRidetypes(SampleData.PICKUP_LAT, SampleData.PICKUP_LNG, selectedRideType?.rideTypeKey)

            showLoading()
            call.enqueue(retrofitCallBack(
                    this::populateResultsTextView,
                    this::handleCallError
            ))
        }
    }

    private fun populateResultsTextView(estimateResponse: Response<RideTypesResponse>) {
        hideLoading()
        if (estimateResponse.isSuccessful) {
            apiResultTextView.text = estimateResponse.body()?.ride_types?.toString()
        } else {
            val errorMessage = "Network Request failed with the http code: " + estimateResponse.code()
            apiResultTextView.text = errorMessage
        }

    }

    private fun handleCallError(throwable: Throwable) {
        hideLoading()

        val errorMessage = "The network request returned the following throwable:" + throwable.message
        apiResultTextView.text = errorMessage

    }

    private fun showLoading() {
        loadingLayout.visibility = View.VISIBLE
    }

    private fun hideLoading() {
        loadingLayout.visibility = View.GONE
    }
}