package com.example.oirapp.ui.viewmodel

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel

open class BaseViewModel : ViewModel() {
    private val _showSuccessDialog = MutableLiveData(false)
    val showSuccessDialog: LiveData<Boolean> = _showSuccessDialog

    private val _showErrorDialog = MutableLiveData(false)
    val showErrorDialog: LiveData<Boolean> = _showErrorDialog

    fun setShowSuccessDialog(show: Boolean) {
        _showSuccessDialog.value = show
    }

    fun setShowErrorDialog(show: Boolean) {
        _showErrorDialog.value = show
    }
}
