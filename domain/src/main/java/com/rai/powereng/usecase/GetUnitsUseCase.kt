package com.rai.powereng.usecase

import com.rai.powereng.model.Response
import com.rai.powereng.model.UnitData
import com.rai.powereng.repository.FirebaseAuthRepository
import com.rai.powereng.repository.UnitsRepository
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flow

class GetUnitsUseCase(private val repoUnits: UnitsRepository){

    operator fun invoke() = repoUnits.getUnitsData()

}