package com.dicoding.asclepius.adapter

import android.view.LayoutInflater
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.dicoding.asclepius.R
import com.dicoding.asclepius.database.PredictionHistory
import com.squareup.picasso.Picasso
import java.text.SimpleDateFormat
import java.util.Locale

class PredictionHistoryAdapter(private val historyList: List<PredictionHistory>) :
    RecyclerView.Adapter<PredictionHistoryAdapter.PredictionHistoryViewHolder>() {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): PredictionHistoryViewHolder {
        val view = LayoutInflater.from(parent.context)
            .inflate(R.layout.item_prediction_history, parent, false)
        return PredictionHistoryViewHolder(view)
    }

    override fun onBindViewHolder(holder: PredictionHistoryViewHolder, position: Int) {
        val history = historyList[position]

        Picasso.get().load(history.imagePath).into(holder.historyImage)

        holder.historyResult.text = history.result

        holder.historyConfidenceScore.text = "Confidence Score: ${history.confidenceScore}%"

        val sdf = SimpleDateFormat("dd MMM yyyy", Locale.getDefault())
        holder.historyDate.text = sdf.format(history.date)
    }

    override fun getItemCount(): Int {
        return historyList.size
    }

    class PredictionHistoryViewHolder(view: android.view.View) : RecyclerView.ViewHolder(view) {
        val historyImage: ImageView = view.findViewById(R.id.history_image)
        val historyResult: TextView = view.findViewById(R.id.history_result)
        val historyConfidenceScore: TextView = view.findViewById(R.id.history_confidence_score)
        val historyDate: TextView = view.findViewById(R.id.history_date)
    }
}
