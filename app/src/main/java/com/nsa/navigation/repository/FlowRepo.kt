package com.nsa.navigation.repository

import com.nsa.navigation.models.RecyclerList
import com.nsa.navigation.network.RetroService
import kotlinx.coroutines.delay
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flow

class FlowRepo (
    private val newsApi: RetroService,
    private val refreshIntervalMs: Long = 5000
    ) {
        val latestNews: Flow<RecyclerList> = flow {
            while(true) {
                val latestNews = newsApi.getDataFromAPI("1")
                emit(latestNews) // Emits the result of the request to the flow
                delay(refreshIntervalMs) // Suspends the coroutine for some time
            }
        }
    }


