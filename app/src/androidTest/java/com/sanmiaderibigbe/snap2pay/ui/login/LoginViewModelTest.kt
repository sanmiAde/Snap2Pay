package com.sanmiaderibigbe.snap2pay.ui.login

import androidx.arch.core.executor.testing.InstantTaskExecutorRule
import com.jraska.livedata.test
import com.sanmiaderibigbe.snap2pay.repo.Status
import com.sanmiaderibigbe.snap2pay.ui.login.LoginViewModel.AuthenticationState
import org.junit.Rule
import org.junit.Test
import org.junit.rules.TestRule
import org.koin.core.inject
import org.koin.test.KoinTest

class LoginViewModelTest : KoinTest {

    val viewModel: LoginViewModel by inject()

    @get:Rule
    var rule: TestRule = InstantTaskExecutorRule()


    @Test
    fun signIn_userCredentials_authenticated() {
        viewModel.signout()
        viewModel.signIn("user@test.com", "12345678")

        viewModel.getLoginResource()
            .test()
            .awaitValue()
            .awaitNextValue()
            .assertValue{
                it.status == Status.SUCCESS
            }
            .assertValue {
                it.data == AuthenticationState.AUTHENTICATED
            }

    }

    @Test
    fun signIn_userCredentials_unauthenticated() {

        viewModel.signout()
        viewModel.signIn("arabmoney2@test.com", "123456789")

        viewModel.getLoginResource()
            .test()
            .awaitValue()
            .awaitNextValue()
            .assertValue{
                it.status == Status.ERROR
            }
            .assertValue {
                it.data == AuthenticationState.UNAUTHENTICATED
            }

    }
}

