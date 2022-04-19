package com.venkat.cryptoapp

import android.os.Bundle
import android.util.Log
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.ViewModelProvider
import com.venkat.cryptoapp.databinding.ActivityCurrencyDetailsBinding
import com.venkat.cryptoapp.viewmodel.CurrencyDetailsActivityViewModel
import com.venkat.cryptoapp.viewmodel.CurrencyDetailsActivityViewModelFactory
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext

class CurrencyDetailsActivity : AppCompatActivity() {
    private lateinit var binding: ActivityCurrencyDetailsBinding
    private lateinit var viewModelFactory: CurrencyDetailsActivityViewModelFactory
    private lateinit var viewModel: CurrencyDetailsActivityViewModel
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityCurrencyDetailsBinding.inflate(layoutInflater)
        val view = binding.root
        setContentView(view)
        val symbol = intent.getStringExtra("currency_symbol") ?: ""
        viewModelFactory = CurrencyDetailsActivityViewModelFactory(symbol)
        viewModel = ViewModelProvider(
            this,
            viewModelFactory
        ).get(CurrencyDetailsActivityViewModel::class.java)
        lifecycle.addObserver(viewModel)

    }

    override fun onResume() {
        super.onResume()
        setDataToViews()
    }

    private fun setDataToViews() {
        val data = viewModel.refresh(1000)
        CoroutineScope(Dispatchers.IO).launch {
            data.collect{ response ->
                val responses = response.body()
                responses?.let { currency ->
                    withContext(Dispatchers.Main){
                        val symbolString = "${currency.baseAsset} / ${currency.quoteAsset}"
                        binding.symbolTextView.text = symbolString
                        binding.currentPriceTextView.text = currency.lastPrice
                        binding.volumeTextView.text = currency.volume
                        binding.openPriceTextView.text = currency.openPrice
                        binding.lowPriceTextView.text = currency.lowPrice
                        binding.highPriceTextView.text = currency.highPrice
                        binding.bidPriceTextView.text = currency.bidPrice
                        binding.askPriceTextView.text = currency.askPrice
                        binding.atTimeTextView.text = viewModel.getDateFromMilliseconds(currency.at)

                    }
                }
            }
        }
    }



    override fun onPause() {
        super.onPause()
    }
}