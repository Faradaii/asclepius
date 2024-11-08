package com.dicoding.asclepius.viewModel

import android.app.Application
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.viewModelScope
import com.dicoding.asclepius.database.PredictionDatabase
import com.dicoding.asclepius.database.PredictionHistory
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch

class HistoryViewModel(application: Application) : AndroidViewModel(application) {

    private val predictionDao = PredictionDatabase.getInstance(application).predictionHistoryDao()

    private val _predictionHistory = MutableLiveData<List<PredictionHistory>>()
    val predictionHistory: LiveData<List<PredictionHistory>> get() = _predictionHistory

    init {
        fetchPredictionHistory()
    }

    private fun fetchPredictionHistory() {
        viewModelScope.launch(Dispatchers.IO) {
            val historyList = predictionDao.getAllHistories()
            _predictionHistory.postValue(historyList)
        }
    }
}

