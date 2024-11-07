package com.dicoding.asclepius.adapter

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.dicoding.asclepius.R
import com.dicoding.asclepius.database.PredictionHistory
import com.dicoding.asclepius.network.Article
import com.squareup.picasso.Picasso
import java.text.SimpleDateFormat
import java.util.Locale


class ItemAdapter(private val itemList: List<Any>) :
    RecyclerView.Adapter<RecyclerView.ViewHolder>() {

    companion object {
        private const val TYPE_PREDICTION_HISTORY = 0
        private const val TYPE_ARTICLE = 1
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): RecyclerView.ViewHolder {
        return when (viewType) {
            TYPE_PREDICTION_HISTORY -> {
                val view = LayoutInflater.from(parent.context)
                    .inflate(R.layout.item_adapter, parent, false)
                PredictionHistoryViewHolder(view)
            }

            TYPE_ARTICLE -> {
                val view = LayoutInflater.from(parent.context)
                    .inflate(R.layout.item_adapter, parent, false)
                ArticleViewHolder(view)
            }

            else -> throw IllegalArgumentException("Unknown view type")
        }
    }

    override fun onBindViewHolder(holder: RecyclerView.ViewHolder, position: Int) {
        when (holder) {
            is PredictionHistoryViewHolder -> {
                val predictionHistory = itemList[position] as PredictionHistory
                Picasso.get().load(predictionHistory.imagePath)
                    .placeholder(R.drawable.ic_place_holder)
                    .error(R.drawable.ic_place_holder)
                    .into(holder.historyImage)
                holder.historyTitle.text = predictionHistory.result
                holder.historyDesc.text = buildString {
                    append("Confidence: ")
                    append(predictionHistory.confidenceScore * 100)
                    append("%")
                }
                val sdf = SimpleDateFormat("dd MMM yyyy", Locale.getDefault())
                holder.historyDate.text = sdf.format(predictionHistory.date)
            }

            is ArticleViewHolder -> {
                val article = itemList[position] as Article
                Picasso.get().load(article.urlToImage).resize(500, 500)
                    .placeholder(R.drawable.ic_place_holder)
                    .error(R.drawable.ic_place_holder)
                    .into(holder.articleImage)
                holder.articleTitle.text = article.title
                holder.articleDesc.text = article.description
                holder.articleDate.text = article.publishedAt
            }
        }
    }

    override fun getItemCount(): Int = itemList.size

    override fun getItemViewType(position: Int): Int {
        return when (itemList[position]) {
            is PredictionHistory -> TYPE_PREDICTION_HISTORY
            is Article -> TYPE_ARTICLE
            else -> throw IllegalArgumentException("Unknown data type")
        }
    }

    class PredictionHistoryViewHolder(view: View) : RecyclerView.ViewHolder(view) {
        val historyImage: ImageView = view.findViewById(R.id.item_image)
        val historyTitle: TextView = view.findViewById(R.id.item_title)
        val historyDesc: TextView = view.findViewById(R.id.item_desc)
        val historyDate: TextView = view.findViewById(R.id.item_date)
    }

    class ArticleViewHolder(view: View) : RecyclerView.ViewHolder(view) {
        val articleImage: ImageView = view.findViewById(R.id.item_image)
        val articleTitle: TextView = view.findViewById(R.id.item_title)
        val articleDesc: TextView = view.findViewById(R.id.item_desc)
        val articleDate: TextView = view.findViewById(R.id.item_date)
    }
}
