package com.wceng.core.data.language.di

import com.wceng.core.data.language.LanguageStringResource
import com.wceng.core.data.language.StringResourceDataSource
import dagger.Binds
import dagger.Module
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent

@Module
@InstallIn(SingletonComponent::class)
internal interface StringResourceModule {

    @Binds
    fun bindLanguageDataSource(
        languageStringResource: LanguageStringResource
    ): StringResourceDataSource

}