package ru.is2si.sisi

import io.mockk.mockk
import org.junit.Before
import org.junit.Test
import ru.is2si.sisi.base.rx.RxSchedulers
import ru.is2si.sisi.domain.auth.AuthTeam
import ru.is2si.sisi.domain.auth.GetSaveTeam
import ru.is2si.sisi.presentation.auth.AuthPresenter

class AuthTest {

    private lateinit var presenter: AuthPresenter
    private val rxSchedulers: RxSchedulers = mockk()
    private val authTeam: AuthTeam = mockk()
    private val getSaveTeam: GetSaveTeam = mockk()

    @Before
    fun setUp() {
        presenter = AuthPresenter(rxSchedulers, authTeam, getSaveTeam)
    }

    @Test
    fun `тест_авторизации`(){
        // TODO: Red_byte 2019-09-26 release it
    }

}