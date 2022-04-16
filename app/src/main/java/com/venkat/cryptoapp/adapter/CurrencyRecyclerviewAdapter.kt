package com.venkat.cryptoapp.adapter

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.venkat.cryptoapp.R
import com.venkat.cryptoapp.model.CryptoCurrencyItem

class CurrencyRecyclerviewAdapter(private val currencies: List<CryptoCurrencyItem>) :
    RecyclerView.Adapter<CurrencyViewHolder>() {
    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): CurrencyViewHolder {
        val layoutInflater = LayoutInflater.from(parent.context)
        val listItem = layoutInflater.inflate(R.layout.currency_item_layout, parent, false)
        return CurrencyViewHolder(listItem)
    }

    override fun onBindViewHolder(holder: CurrencyViewHolder, position: Int) {
        val currencyItem = currencies[position]
        holder.bind(currencyItem)
    }

    override fun getItemCount(): Int {
        return currencies.size
    }
}

class CurrencyViewHolder(val view: View) : RecyclerView.ViewHolder(view) {
    val currencyNameTextView = view.findViewById<TextView>(R.id.currency_name_text_view)
    val currencyPriceTextView = view.findViewById<TextView>(R.id.current_price_text_view)
    val changePercentageTextView = view.findViewById<TextView>(R.id.price_difference_text_view)

    fun bind(currency: CryptoCurrencyItem) {
        val currencyName = "${currency.baseAsset.uppercase()} / ${currency.quoteAsset.uppercase()}"
        currencyNameTextView.text = currencyName
        val currencyPrice = "&#8377;${currency.openPrice}"
        currencyPriceTextView.text = currencyPrice
        val changedAmount = currency.openPrice.toDouble().minus(currency.lastPrice.toDouble())
        val changedPercentage = (changedAmount * 100) / currency.lastPrice.toDouble()
        val changePercentageString = "&#9652; $changedPercentage%"
    }
}
