package com.ricardosfp.zapping.infrastructure.di

import com.ricardosfp.zapping.infrastructure.alarm.*
import com.ricardosfp.zapping.infrastructure.util.date.*
import dagger.*
import dagger.hilt.*
import dagger.hilt.components.*
import javax.inject.*

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