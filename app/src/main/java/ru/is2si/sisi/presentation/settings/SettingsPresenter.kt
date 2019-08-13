package ru.is2si.sisi.presentation.settings

import android.util.Log
import ru.is2si.sisi.BuildConfig
import ru.is2si.sisi.base.BasePresenter
import ru.is2si.sisi.base.rx.RxSchedulers
import ru.is2si.sisi.domain.UseCase.None
import ru.is2si.sisi.domain.auth.GetServerUrl
import ru.is2si.sisi.domain.auth.SetServerUrl
import ru.is2si.sisi.domain.auth.SetServerUrl.Params
import javax.inject.Inject

class SettingsPresenter @Inject constructor(
    private val rxSchedulers: RxSchedulers,
    private val setServerUrl: SetServerUrl,
    private val getServerUrl: GetServerUrl
) : BasePresenter<SettingsContract.View>(), SettingsContract.Presenter {

    override fun start() {
        disposables += getServerUrl.execute(None())
            .subscribeOn(rxSchedulers.io)
            .observeOn(rxSchedulers.ui)
            .subscribe(view::showCurrentUrl)
    }

    override fun changeServerUrl(newUrl: String) {
        disposables += setServerUrl.execute(Params(newUrl))
            .subscribeOn(rxSchedulers.io)
            .observeOn(rxSchedulers.ui)
            .subscribe({
                view.backScreen()
            })
            {if (BuildConfig.DEBUG)Log.e("_debug","Change url error:",it)}
    }

}