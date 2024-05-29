package com.example.playlistmaker.search.data.network

import com.example.playlistmaker.search.data.NetworkClient
import com.example.playlistmaker.search.data.dto.Response
import com.example.playlistmaker.search.data.dto.TrackSearchRequest
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext
import retrofit2.HttpException
import java.lang.Exception

class RetrofitNetworkClient(private val iTunesApi: iTunesApi) : NetworkClient {

    override suspend fun doRequest(dto: Any): Response {
        return withContext(Dispatchers.IO) {
            try {
                if (dto is TrackSearchRequest) {
                    val resp = iTunesApi.search(dto.expression)
                    resp.apply { resultCode = 200 }
                } else {
                    Response().apply { resultCode = 400 }
                }
            } catch (exception: HttpException) {
                Response().apply { resultCode = exception.code() }
            } catch (e: Exception) {
                Response().apply { resultCode = 400 }
            }
        }
    }
}