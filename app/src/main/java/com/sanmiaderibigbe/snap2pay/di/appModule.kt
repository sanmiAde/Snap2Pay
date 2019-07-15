package com.sanmiaderibigbe.snap2pay.di

import android.app.Application
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.database.FirebaseDatabase
import com.google.firebase.ml.vision.FirebaseVision
import com.google.firebase.ml.vision.text.FirebaseVisionTextRecognizer
import com.sanmiaderibigbe.snap2pay.api.VisionImage
import com.sanmiaderibigbe.snap2pay.repo.FirebaseRepository
import com.sanmiaderibigbe.snap2pay.repo.PaystackRepository
import com.sanmiaderibigbe.snap2pay.repo.TextRecognitionRepository
import com.sanmiaderibigbe.snap2pay.ui.home.HomeViewModel
import com.sanmiaderibigbe.snap2pay.ui.login.LoginViewModel
import com.sanmiaderibigbe.snap2pay.ui.profile.ProfileFragmentViewModel
import com.sanmiaderibigbe.snap2pay.ui.registration.RegistrationBankViewModel
import com.sanmiaderibigbe.snap2pay.ui.registration.RegistrationPersonalViewModel
import com.sanmiaderibigbe.snap2pay.ui.transaction.TransactionViewModel
import com.sanmiaderibigbe.snap2pay.ui.transactionslist.TransactonsViewModel
import com.sanmiaderibigbe.snap2pay.ui.updateUserDetails.UpdateUserDetailsViewModel
import org.koin.android.ext.koin.androidContext
import org.koin.androidx.viewmodel.dsl.viewModel
import org.koin.dsl.module

val appModule = module {

    single<FirebaseAuth> { FirebaseAuth.getInstance() }

    single<FirebaseDatabase> { FirebaseDatabase.getInstance()}

    single<VisionImage> { VisionImage() }

    single<FirebaseRepository> {
        FirebaseRepository(androidContext() as Application, get(), get())
    }

    single<FirebaseVisionTextRecognizer> { FirebaseVision.getInstance().onDeviceTextRecognizer }

    single<TextRecognitionRepository> { TextRecognitionRepository(androidContext() as Application, get(), get()) }

    single<PaystackRepository> { PaystackRepository() }

    viewModel { LoginViewModel(get()) }
    viewModel { RegistrationPersonalViewModel() }

    viewModel { RegistrationBankViewModel(get()) }

    viewModel { HomeViewModel(androidContext() as Application, get()) }

    viewModel { TransactionViewModel(get(), get()) }

    viewModel { ProfileFragmentViewModel(get()) }

    viewModel { UpdateUserDetailsViewModel(get()) }

    viewModel { TransactonsViewModel(get()) }

}