package com.example.playlistmaker.settings.data

import android.content.Context
import android.content.Intent
import android.net.Uri
import com.example.playlistmaker.settings.domain.ExternalNavigator

class ExternalNavigatorImpl(private val context: Context) : ExternalNavigator {

    override fun shareLink(link: String) {
        val intent = Intent().apply {
            action = Intent.ACTION_SEND
            putExtra(Intent.EXTRA_TEXT, link)
            type = "text/plain"
        }
        intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK)
        context.startActivity(intent)
    }

    override fun sendSupport(email: String, textTop: String, text: String) {
        Intent().setFlags(Intent.FLAG_ACTIVITY_NEW_TASK).apply {
            action = Intent.ACTION_SENDTO
            data = Uri.parse("mailto:")
            putExtra(Intent.EXTRA_EMAIL, arrayOf(email))
            putExtra(Intent.EXTRA_SUBJECT, textTop)
            putExtra(Intent.EXTRA_TEXT, text)
            context.startActivity(this)
        }
    }

    override fun userPolicy(link: String) {
        val userPolicyOpen =
            Intent(Intent.ACTION_VIEW, Uri.parse(link))
        userPolicyOpen.flags = Intent.FLAG_ACTIVITY_NEW_TASK
        context.startActivity(userPolicyOpen)
    }
}