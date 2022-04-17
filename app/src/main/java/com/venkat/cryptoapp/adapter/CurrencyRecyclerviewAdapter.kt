package com.venkat.cryptoapp.adapter

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.venkat.cryptoapp.R
import com.venkat.cryptoapp.model.CryptoCurrencyItem

class CurrencyRecyclerviewAdapter(private val clickListener: (CryptoCurrencyItem) -> Unit) :
    RecyclerView.Adapter<CurrencyViewHolder>() {
    private val currencies = ArrayList<CryptoCurrencyItem>()
    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): CurrencyViewHolder {
        val layoutInflater = LayoutInflater.from(parent.context)
        val listItem = layoutInflater.inflate(R.layout.currency_item_layout, parent, false)
        return CurrencyViewHolder(listItem)
    }

    override fun onBindViewHolder(holder: CurrencyViewHolder, position: Int) {
        val currencyItem = currencies[position]
        holder.bind(currencyItem,clickListener)
    }

    override fun getItemCount(): Int {
        return currencies.size
    }
    fun setList(currencyList : List<CryptoCurrencyItem>){
        currencies.clear()
        currencies.addAll(currencyList)
        notifyDataSetChanged()
    }
}

class CurrencyViewHolder(val view: View) : RecyclerView.ViewHolder(view) {
    val currencyNameTextView = view.findViewById<TextView>(R.id.currency_name_text_view)
    val currencyPriceTextView = view.findViewById<TextView>(R.id.current_price_text_view)
    val changePercentageTextView = view.findViewById<TextView>(R.id.price_difference_text_view)

    fun bind(currency: CryptoCurrencyItem,clickListener: (CryptoCurrencyItem) -> Unit) {
        val currencyName = "${currency.baseAsset.uppercase()} / ${currency.quoteAsset.uppercase()}"
        currencyNameTextView.text = currencyName
        val currencyPrice = "&#8377;${currency.openPrice}"
        currencyPriceTextView.text = currencyPrice
        val changedAmount = currency.openPrice.toDouble().minus(currency.lastPrice.toDouble())
        val changedPercentage = (changedAmount * 100) / currency.lastPrice.toDouble()
        val changePercentageString = "&#9652; $changedPercentage%"

        clickListener(currency)
    }
}
