package com.nsa.navigation.viewmodel

import android.util.Log
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.nsa.navigation.models.RecyclerList
import com.nsa.navigation.models.RecylcerData
import com.nsa.navigation.network.RetroInstance
import com.nsa.navigation.network.RetroService
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch

class MainActivityViewModel:ViewModel() {
     var recyclerListLiveData :MutableLiveData<RecyclerList>
    private lateinit var list:RecyclerList
    private lateinit var data:ArrayList<RecylcerData>



    init {
        data= arrayListOf()
        recyclerListLiveData= MutableLiveData()
        list=RecyclerList(data)
    }

    fun getRecyclerListObserver():MutableLiveData<RecyclerList>{
        return recyclerListLiveData
    }
    var pageNumber=1;
    fun refresh(){
        pageNumber++;
        makeApiCall()
    }
    fun makeApiCall(){

        viewModelScope.launch (Dispatchers.IO){
            val retroInstance = RetroInstance.getRetroInstance().create(RetroService::class.java)
            val response = retroInstance.getDataFromAPI(pageNumber.toString())
            list.Search.addAll(response.Search)
            recyclerListLiveData.postValue(list)
        }
    }
}