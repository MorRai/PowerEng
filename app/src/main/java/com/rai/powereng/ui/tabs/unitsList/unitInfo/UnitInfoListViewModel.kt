package com.rai.powereng.ui.tabs.unitsList.unitInfo

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.rai.powereng.model.Response
import com.rai.powereng.usecase.GetAllTasksOfUnitUseCase
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.stateIn

class UnitInfoListViewModel(getAllTasksOfUnitUseCase: GetAllTasksOfUnitUseCase,
                            unitId: Int,) :ViewModel() {

    val unitsFlow = getAllTasksOfUnitUseCase.invoke(unitId)
        .stateIn(
            scope = viewModelScope,
            started = SharingStarted.Eagerly,
            initialValue = Response.Loading
        )
}