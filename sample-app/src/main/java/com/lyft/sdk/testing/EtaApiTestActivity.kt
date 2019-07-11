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
import com.lyft.sdk.R
import com.lyft.sdk.testing.helpers.retrofitCallBack
import com.lyft.sdk.testing.ui.SampleData
import com.lyft.sdk.testing.ui.SpinnerPairItem
import retrofit2.Response

private enum class EtaCallType {
    PICKUP_ONLY,
    PICKUP_RIDE_TYPE,
    COMPLETE_PARAMS
}

class EtaApiTestActivity : AppCompatActivity() {

    private lateinit var etaCallSpinner: AppCompatSpinner
    private lateinit var rideTypeSpinner: AppCompatSpinner
    private lateinit var etaCallButton: Button
    private lateinit var etaTextView: TextView
    private lateinit var loadingLayout: View

    private val etaCallTypeSpinnerList = listOf(
            SpinnerPairItem("Select a call type", null),
            SpinnerPairItem("Pickup LatLng Only", EtaCallType.PICKUP_ONLY),
            SpinnerPairItem("Pickup with Ride Type", EtaCallType.PICKUP_RIDE_TYPE),
            SpinnerPairItem("Pickup, Destination, Ride Type", EtaCallType.COMPLETE_PARAMS)
    )
    private val etaRideTypeSpinnerList = listOf(
            SpinnerPairItem("All Ride Types", null),
            SpinnerPairItem(RideTypeEnum.SHARED.displayName, RideTypeEnum.SHARED),
            SpinnerPairItem(RideTypeEnum.STANDARD.displayName, RideTypeEnum.STANDARD),
            SpinnerPairItem(RideTypeEnum.XL.displayName, RideTypeEnum.XL),
            SpinnerPairItem(RideTypeEnum.LUX.displayName, RideTypeEnum.LUX),
            SpinnerPairItem(RideTypeEnum.LUX_BLACK.displayName, RideTypeEnum.LUX_BLACK),
            SpinnerPairItem(RideTypeEnum.LUX_BLACK_XL.displayName, RideTypeEnum.LUX_BLACK_XL)
    )

    private val lyftApi = TestLyftApiFactory().getRetrofitClient()
    private var selectedCallType: EtaCallType? = null
    private var selectedRideType: RideTypeEnum? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_test_eta_api)

        initViews()
        setupSpinners()
        setupButton()
    }

    private fun initViews() {
        etaCallSpinner = findViewById(R.id.eta_api_test_call_type_spinner)
        rideTypeSpinner = findViewById(R.id.eta_api_test_ride_type_spinner)
        etaCallButton = findViewById(R.id.eta_api_test_make_call_button)
        etaTextView = findViewById(R.id.eta_api_test_result_textview)
        loadingLayout = findViewById(R.id.eta_api_test_loading_layout)
    }

    private fun setupSpinners() {
        val callTypeSpinnerAdapter = ArrayAdapter(this, android.R.layout.simple_spinner_item, etaCallTypeSpinnerList)
        callTypeSpinnerAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item)
        etaCallSpinner.adapter = callTypeSpinnerAdapter
        etaCallSpinner.onItemSelectedListener = object : AdapterView.OnItemSelectedListener {
            override fun onItemSelected(parent: AdapterView<*>?, view: View?, position: Int, id: Long) {
                selectedCallType = etaCallTypeSpinnerList[position].data
            }

            override fun onNothingSelected(parent: AdapterView<*>?) {
                //Do nothing
            }
        }

        val rideTypeSpinnerAdapter = ArrayAdapter(this, android.R.layout.simple_spinner_item, etaRideTypeSpinnerList)
        rideTypeSpinnerAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item)
        rideTypeSpinner.adapter = rideTypeSpinnerAdapter
        rideTypeSpinner.onItemSelectedListener = object : AdapterView.OnItemSelectedListener {
            override fun onItemSelected(parent: AdapterView<*>?, view: View?, position: Int, id: Long) {
                selectedRideType = etaRideTypeSpinnerList[position].data
            }

            override fun onNothingSelected(parent: AdapterView<*>?) {
                //Do nothing
            }
        }
    }

    private fun setupButton() {
        etaCallButton.setOnClickListener {
            val call = when (selectedCallType) {
                EtaCallType.PICKUP_ONLY -> lyftApi.getEtas(SampleData.PICKUP_LAT, SampleData.PICKUP_LNG)
                EtaCallType.PICKUP_RIDE_TYPE -> lyftApi.getEtas(SampleData.PICKUP_LAT, SampleData.PICKUP_LNG, selectedRideType?.rideTypeKey)
                EtaCallType.COMPLETE_PARAMS -> lyftApi.getEtas(SampleData.PICKUP_LAT, SampleData.PICKUP_LNG, selectedRideType?.rideTypeKey, SampleData.DROPOFF_LAT, SampleData.DROPOFF_LNG)
                else -> null
            }

            call?.let {
                showLoading()
                it.enqueue(retrofitCallBack(
                        ::populateEtaTextView,
                        ::handleCallError
                ))
            }
        }
    }

    private fun populateEtaTextView(estimateResponse: Response<EtaEstimateResponse>) {
        hideLoading()
        if (estimateResponse.isSuccessful) {
            etaTextView.text = estimateResponse.body()?.eta_estimates?.toString()
        } else {
            val errorMessage = "Network Request failed with the http code: " + estimateResponse.code()
            etaTextView.text = errorMessage
        }

    }

    private fun handleCallError(throwable: Throwable) {
        hideLoading()

        val errorMessage = "The network request returned the following throwable:" + throwable.message
        etaTextView.text = errorMessage

    }

    private fun showLoading() {
        loadingLayout.visibility = View.VISIBLE
    }

    private fun hideLoading() {
        loadingLayout.visibility = View.GONE
    }
}