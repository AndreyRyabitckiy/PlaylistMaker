package com.example.playlistmaker.data.network

import com.example.playlistmaker.data.NetworkClient
import com.example.playlistmaker.data.dto.Response
import com.example.playlistmaker.data.dto.TrackSearchRequest
import retrofit2.HttpException
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import java.lang.Exception

class RetrofitNetworkClient : NetworkClient {

    private val iTunesBaseUrl = "https://itunes.apple.com"

    private val retrofit = Retrofit.Builder()
        .baseUrl(iTunesBaseUrl)
        .addConverterFactory(GsonConverterFactory.create())
        .build()

    private val iTunesApi = retrofit.create(iTunesApi::class.java)

    override fun doRequest(dto: Any): Response {
        try {
            if (dto is TrackSearchRequest) {
                val resp = iTunesApi.search(dto.expression).execute()
                val body = resp.body() ?: Response()
                return body.apply { resultCode = resp.code() }
            } else {
                return Response().apply { resultCode = 400 }
            }
        } catch (exception: HttpException) {
            return Response().apply { resultCode = exception.code() }
        } catch (e: Exception) {
            return Response().apply { resultCode = 400 }
        }
    }
}