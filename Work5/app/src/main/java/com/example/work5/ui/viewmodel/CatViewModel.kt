package com.example.work5.ui.viewmodel

import android.app.Application
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.viewModelScope
import com.example.work5.data.repository.CatRepository
import com.example.work5.data.database.CatDatabase
import com.example.work5.data.network.CatApi
import kotlinx.coroutines.launch
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory

class CatViewModel(application: Application) : AndroidViewModel(application) {

    private val catDao = CatDatabase.getDatabase(application).catDao()

    private val retrofit = Retrofit.Builder()
        .baseUrl("https://api.thecatapi.com/")
        .addConverterFactory(GsonConverterFactory.create())
        .build()

    private val catApi = retrofit.create(CatApi::class.java)
    private val repository = CatRepository(catDao, catApi)

    val catImageUrl = MutableLiveData<String>()
    val error = MutableLiveData<String>()

    fun fetchCat() {
            repository.fetchCatFromApi { result ->
                result.onSuccess { cats ->
                    if (cats.isNotEmpty()) {
                        val cat = cats[0]
                        viewModelScope.launch {
                            repository.saveCatToDb(cat)
                        }
                        catImageUrl.postValue(cat.url)
                    } else {
                        error.postValue("No cat data found")
                    }
                }.onFailure { throwable ->
                    error.postValue(throwable.message)
                }
            }
    }

    fun loadCatFromDb() {
        viewModelScope.launch {
            val cat = repository.getCatFromDb()
            if (cat != null) {
                catImageUrl.postValue(cat.url)
            } else {
                error.postValue("No cat in database")
            }
        }
    }
}