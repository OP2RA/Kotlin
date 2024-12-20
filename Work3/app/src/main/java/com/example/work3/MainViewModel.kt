package com.example.work3

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel

class MainViewModel : ViewModel() {
    private var name_ = MutableLiveData<String>()
    private var email_ = MutableLiveData<String>()
    var name : LiveData<String> = name_
    var email: LiveData<String> = email_

    fun setName(str: String) {
        name_.value = str
    }

    fun  setEmail(str: String){
        email_.value = str
    }
}