package com.example.playlistmaker.search.data.network

import com.example.playlistmaker.search.data.NetworkClient
import com.example.playlistmaker.search.data.dto.Response
import com.example.playlistmaker.search.data.dto.TrackSearchRequest
import retrofit2.HttpException
import java.lang.Exception

class RetrofitNetworkClient(private val iTunesApi: iTunesApi) : NetworkClient {

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