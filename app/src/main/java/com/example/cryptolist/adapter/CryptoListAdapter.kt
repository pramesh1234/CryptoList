package com.example.cryptolist.adapter

import android.content.Context
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import androidx.constraintlayout.widget.ConstraintLayout
import androidx.recyclerview.widget.RecyclerView
import com.example.cryptolist.R
import com.example.cryptolist.model.CryptoModel
import kotlin.math.abs

class CryptoListAdapter(val context: Context, val cryptoList: ArrayList<CryptoModel>) :
    RecyclerView.Adapter<CryptoListAdapter.CryptoViewHolder>() {
    inner class CryptoViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        val cryptoNameTv: TextView = itemView.findViewById(R.id.cryptoNameTV)
        val cryptoAssetTv: TextView = itemView.findViewById(R.id.cryptoAssetTV)
        val cryptoPriceTV: TextView = itemView.findViewById(R.id.cryptoPriceTV)
        val updateTV: TextView = itemView.findViewById(R.id.updateTV)
        val trendTV: TextView = itemView.findViewById(R.id.trendTV)
        val lowestPriceTV: TextView = itemView.findViewById(R.id.lowestPriceTV)
        val highestPriceTV: TextView = itemView.findViewById(R.id.highestTV)

        val viewMore: ImageView = itemView.findViewById(R.id.viewMoreIV)
        val fullCL: ConstraintLayout = itemView.findViewById(R.id.fullCL)

        init {
            viewMore.setOnClickListener {
                if (cryptoList[adapterPosition].full) {
                    fullCL.visibility = View.GONE
                    cryptoList[adapterPosition].full = false
                } else {
                    fullCL.visibility = View.VISIBLE
                    cryptoList[adapterPosition].full = true
                }
            }
        }


    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): CryptoViewHolder {
        return CryptoViewHolder(
            LayoutInflater.from(context).inflate(R.layout.crypto_item, parent, false)
        )
    }

    override fun onBindViewHolder(holder: CryptoViewHolder, position: Int) {
        cryptoList[position].let {
            holder.cryptoNameTv.text = "${it.baseAsset}/"
            holder.cryptoAssetTv.text = it.quoteAsset
            holder.cryptoPriceTV.text = it.lastPrice
            it.full = false
            val diff = it.lastPrice.toFloat().minus(it.openPrice.toFloat())
            if (diff < 0) {
                holder.trendTV.setCompoundDrawablesWithIntrinsicBounds(
                    R.drawable.ic_baseline_arrow_downward_24,
                    0,
                    0,
                    0
                )
            } else if (diff > 0) {
                holder.trendTV.setCompoundDrawablesWithIntrinsicBounds(
                    R.drawable.ic_baseline_arrow_upward_24,
                    0,
                    0,
                    0
                )
            }
            val per: Float = abs(diff) * 100 / it.openPrice.toFloat()
            holder.trendTV.text = String.format("%.2f%%", per)
            holder.lowestPriceTV.text = it.lowPrice
            holder.highestPriceTV.text = it.highPrice
        }
    }

    override fun getItemCount(): Int {
        return cryptoList.size
    }

    fun updateList(list: ArrayList<CryptoModel>) {
        cryptoList.clear()
        cryptoList.addAll(list)
        notifyDataSetChanged()
    }
}