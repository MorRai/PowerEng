package com.rai.powereng.usecase

import com.rai.powereng.repository.UnitsRepository

class GetUnitsUseCase(private val repoUnits: UnitsRepository){
    operator fun invoke() = repoUnits.getUnitsData()
}