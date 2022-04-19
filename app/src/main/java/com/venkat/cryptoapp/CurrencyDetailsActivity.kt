package com.venkat.cryptoapp

import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import androidx.core.text.HtmlCompat
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
            data.collect { response ->
                val responses = response.body()
                responses?.let { currency ->
                    withContext(Dispatchers.Main) {
                        val symbolString = "${currency.baseAsset} / ${currency.quoteAsset}"
                        binding.symbolTextView.text = symbolString
                        binding.currentPriceTextView.text =
                            viewModel.formatCurrency(currency.lastPrice, currency.quoteAsset)
                        binding.volumeTextView.text = viewModel.formatNumber(currency.volume)
                        binding.openPriceTextView.text =
                            viewModel.formatCurrency(currency.openPrice, currency.quoteAsset)
                        binding.lowPriceTextView.text =
                            viewModel.formatCurrency(currency.lowPrice, currency.quoteAsset)
                        binding.highPriceTextView.text =
                            viewModel.formatCurrency(currency.highPrice, currency.quoteAsset)
                        binding.bidPriceTextView.text =
                            viewModel.formatCurrency(currency.bidPrice, currency.quoteAsset)
                        binding.askPriceTextView.text =
                            viewModel.formatCurrency(currency.askPrice, currency.quoteAsset)
                        binding.atTimeTextView.text = viewModel.getDateFromMilliseconds(currency.at)

                        val changedAmount =
                            currency.lastPrice.toDouble().minus(currency.openPrice.toDouble())
                        val changedPercentage =
                            (changedAmount * 100) / currency.openPrice.toDouble()
                        var changePercentageString = ""
                        if (changedPercentage >= 0) {
                            binding.priceDifferenceTextView.setBackgroundResource(android.R.color.holo_green_dark)
                            changePercentageString =
                                "&#9652; ${String.format("%.2f", changedPercentage)}%"
                        } else {
                            binding.priceDifferenceTextView.setBackgroundResource(android.R.color.holo_red_light)
                            changePercentageString =
                                "&#9662; ${String.format("%.2f", changedPercentage * -1)}%"
                        }

                        val changePercentageStringWithSymbol = HtmlCompat.fromHtml(
                            changePercentageString,
                            HtmlCompat.FROM_HTML_MODE_LEGACY
                        )
                        binding.priceDifferenceTextView.text = changePercentageStringWithSymbol

                    }
                }
            }
        }
    }


    override fun onPause() {
        super.onPause()
    }
}