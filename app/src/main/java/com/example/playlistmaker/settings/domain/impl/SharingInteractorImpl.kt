package com.example.playlistmaker.settings.domain.impl

import com.example.playlistmaker.settings.domain.ExternalNavigator
import com.example.playlistmaker.settings.domain.SettingsRepository
import com.example.playlistmaker.settings.domain.SharingInteractor

class SharingInteractorImpl(
    private val externalNavigator: ExternalNavigator,
    private val settingsRepository: SettingsRepository,
) : SharingInteractor {
    override fun shareApp() {
        externalNavigator.shareLink(getShareAppLink())
    }

    override fun openTerms() {
        externalNavigator.userPolicy(getTermsLink())
    }

    override fun openSupport() {
        externalNavigator.sendSupport(
            getSupportEmail(),
            getSupportEmailTop(),
            getSupportEmailText()
        )
    }

    private fun getShareAppLink() = settingsRepository.getAppShareLink()

    private fun getSupportEmail() = settingsRepository.getSupportEmail()
    private fun getSupportEmailTop() = settingsRepository.getSupportEmailTop()
    private fun getSupportEmailText() = settingsRepository.getSupportEmailText()

    override fun getTermsLink() = settingsRepository.getUserPolicy()
}