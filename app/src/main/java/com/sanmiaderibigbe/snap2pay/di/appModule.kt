package com.sanmiaderibigbe.snap2pay.di

import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.database.FirebaseDatabase
import com.sanmiaderibigbe.snap2pay.repo.FirebaseRepository
import com.sanmiaderibigbe.snap2pay.ui.login.LoginViewModel
import com.sanmiaderibigbe.snap2pay.ui.registration.RegistrationBankViewModel
import com.sanmiaderibigbe.snap2pay.ui.registration.RegistrationPersonalViewModel
import org.koin.androidx.viewmodel.dsl.viewModel
import org.koin.dsl.module

val appModule = module {

    single<FirebaseAuth> { FirebaseAuth.getInstance() }

    single<FirebaseDatabase> { FirebaseDatabase.getInstance()}

    single<FirebaseRepository> {
        FirebaseRepository(get(), get())
    }

    viewModel { LoginViewModel(get()) }
    viewModel { RegistrationPersonalViewModel() }

    viewModel { RegistrationBankViewModel(get()) }

}