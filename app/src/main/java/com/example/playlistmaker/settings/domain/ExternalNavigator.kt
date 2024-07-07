package com.example.playlistmaker.settings.domain

interface ExternalNavigator {

    fun shareLink(link: String)

    fun sendSupport(email: String, textTop: String, text: String)

    fun userPolicy(link: String)

}