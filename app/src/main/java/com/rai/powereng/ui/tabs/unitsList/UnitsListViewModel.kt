package com.rai.powereng.ui.tabs.unitsList

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.rai.powereng.model.Response
import com.rai.powereng.usecase.GetUnitsUseCase
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.stateIn

class UnitsListViewModel(
    getUnitsUseCase: GetUnitsUseCase,
) : ViewModel() {

    val unitsFlow = getUnitsUseCase.invoke()
        .stateIn(
            scope = viewModelScope,
            started = SharingStarted.Eagerly,
            initialValue = Response.Loading
        )

}