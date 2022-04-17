package com.venkat.cryptoapp.adapter

import android.graphics.Color
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.cardview.widget.CardView
import androidx.core.text.HtmlCompat
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
        if(position%2==0)
            holder.cardView.setCardBackgroundColor(Color.parseColor("#233446"))
        else
            holder.cardView.setCardBackgroundColor(Color.parseColor("#203040"))
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
    val cardView : CardView = view.findViewById(R.id.currency_card_view)
    private val currencyNameTextView: TextView = view.findViewById(R.id.currency_name_text_view)
    private val currencyPriceTextView: TextView = view.findViewById(R.id.current_price_text_view)
    private val changePercentageTextView: TextView = view.findViewById(R.id.price_difference_text_view)

    fun bind(currency: CryptoCurrencyItem,clickListener: (CryptoCurrencyItem) -> Unit) {
        val currencyName = "${currency.baseAsset.uppercase()} / ${currency.quoteAsset.uppercase()}"
        currencyNameTextView.text = currencyName

        val currencyPrice = "&#8377;${currency.lastPrice}"
        val currencyPriceWithSymbol = HtmlCompat.fromHtml(currencyPrice, HtmlCompat.FROM_HTML_MODE_LEGACY)
        currencyPriceTextView.text = currencyPriceWithSymbol

        val changedAmount = currency.lastPrice.toDouble().minus(currency.openPrice.toDouble())
        val changedPercentage = (changedAmount * 100) / currency.openPrice.toDouble()
        var changePercentageString = ""
        if(changedPercentage>=0){
            changePercentageTextView.setBackgroundResource(android.R.color.holo_green_dark)
            changePercentageString = "&#9652; ${String.format("%.2f",changedPercentage)}%"
        }else{
            changePercentageTextView.setBackgroundResource(android.R.color.holo_red_light)
            changePercentageString = "&#9662; ${String.format("%.2f",changedPercentage*-1)}%"
        }

        val changePercentageStringWithSymbol = HtmlCompat.fromHtml(changePercentageString, HtmlCompat.FROM_HTML_MODE_LEGACY)
        changePercentageTextView.text = changePercentageStringWithSymbol

        clickListener(currency)
    }
}
