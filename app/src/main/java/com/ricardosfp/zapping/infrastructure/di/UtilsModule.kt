package com.ricardosfp.zapping.infrastructure.di

import com.ricardosfp.zapping.infrastructure.alarm.MyAlarmManager
import com.ricardosfp.zapping.infrastructure.alarm.MyAlarmManagerImpl
import com.ricardosfp.zapping.infrastructure.util.date.DateUtils
import com.ricardosfp.zapping.infrastructure.util.date.DateUtilsImpl
import dagger.Binds
import dagger.Module
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
abstract class UtilsModule {

    @Binds
    @Singleton
    abstract fun bindDateUtils(dateUtils: DateUtilsImpl): DateUtils

    @Binds
    @Singleton
    abstract fun bindAlarmManager(alarmManager: MyAlarmManagerImpl): MyAlarmManager

}