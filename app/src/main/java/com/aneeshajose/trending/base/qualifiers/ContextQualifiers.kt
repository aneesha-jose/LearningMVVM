package com.aneeshajose.trending.base.qualifiers

import javax.inject.Qualifier

/**
 * Created by Aneesha Jose on 2020-03-22.
 * Class that contains the qualifier definitions for context types
 */

@Retention(AnnotationRetention.BINARY)
@Qualifier
annotation class ApplicationContext

@Retention(AnnotationRetention.BINARY)
@Qualifier
annotation class ActivityContext

@Retention(AnnotationRetention.BINARY)
@Qualifier
annotation class BaseUrl