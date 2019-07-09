package com.sanmiaderibigbe.snap2pay.ui.registration

import androidx.arch.core.executor.testing.InstantTaskExecutorRule
import com.sanmiaderibigbe.snap2pay.model.User
import org.junit.After
import org.junit.Before
import org.junit.Rule
import org.junit.rules.TestRule
import org.koin.core.inject
import org.koin.test.KoinTest
import kotlin.random.Random

class RegistrationBankViewModelTest : KoinTest {

    val viewModel: RegistrationBankViewModel by inject()

    @get:Rule
    var rule: TestRule = InstantTaskExecutorRule()
    lateinit var user: User
    lateinit var password: String

    @Before
    fun setup(){
        user = User(
            "Testing testing",
            "Testing${Random.nextInt()}@testing.com",
            "08030502661",
            "testing",
            "1234567890",
            "Sterling Bank",
            "12345678901",
            "Savings"
        )
        password = "testing"
    }

    @After
    fun tearDown() {
    }

//    @Test
//    fun registerUser_userDetails_shouldRegister() {
//        viewModel.registerUser(user,password)
//
//        viewModel.getRegisterResource()
//            .test()
//            .awaitNextValue()
//            .awaitNextValue()
//            .awaitNextValue()
//            .assertValue{
//                it.status == Status.SUCCESS
//            }.assertValue{
//                it.data == true
//            }
//    }

}