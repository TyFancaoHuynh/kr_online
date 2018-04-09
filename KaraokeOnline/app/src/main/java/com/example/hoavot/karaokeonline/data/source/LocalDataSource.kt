package com.example.hoavot.karaokeonline.data.source

import io.reactivex.Single
import io.reactivex.subjects.BehaviorSubject

/**
 *
 * @author at-hoavo.
 */
interface LocalDataSource {

    /**
     * This method set device token
     *
     * @param deviceToken
     */
    fun saveApiToken(deviceToken: String)

    /**
     * This method get device token
     *
     * @return device token
     */
    fun getApiToken(): String

    /**
     *
     * This method logout
     */
    fun logout(): Single<Boolean>

    /**
     *  Check memory usage
     */
    fun checkMemoryUsage(): BehaviorSubject<Boolean>
}