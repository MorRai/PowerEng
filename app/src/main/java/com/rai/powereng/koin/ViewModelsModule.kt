package com.rai.powereng.koin

import org.koin.androidx.viewmodel.dsl.viewModelOf
import org.koin.dsl.module
import com.rai.powereng.ui.authorization.AuthViewModel
import com.rai.powereng.ui.authorization.forgotPassword.ForgotPasswordViewModel
import com.rai.powereng.ui.authorization.signIn.SignInViewModel
import com.rai.powereng.ui.authorization.signUp.SignUpViewModel
import com.rai.powereng.ui.authorization.verifyEmail.VerifyEmailViewModel
import com.rai.powereng.ui.partTasks.PartTasksFinishViewModel
import com.rai.powereng.ui.partTasks.PartTasksViewModel
import com.rai.powereng.ui.splash.SplashViewModel
import com.rai.powereng.ui.tabs.profile.ProfileContainerViewModel
import com.rai.powereng.ui.tabs.unitsList.UnitsListViewModel
import com.rai.powereng.ui.tabs.usersRating.UserRatingContainerViewModel
import org.koin.androidx.viewmodel.dsl.viewModel
import org.koin.core.qualifier.named

val viewModelsModule = module {
    viewModelOf(::AuthViewModel)
    viewModelOf(::SignUpViewModel)
    viewModelOf(::ForgotPasswordViewModel)
    viewModelOf(::VerifyEmailViewModel)
    viewModelOf(::UnitsListViewModel)
    viewModelOf(::SplashViewModel)
    viewModelOf(::ProfileContainerViewModel)
    viewModelOf(::UserRatingContainerViewModel)
    viewModelOf(::PartTasksViewModel)
    viewModelOf(::PartTasksFinishViewModel)
    viewModel {
        SignInViewModel(get(),
            get(),
            get(),
            get(named("signInRequest")),
            get(named("signUpRequest"))
        )
    }
}