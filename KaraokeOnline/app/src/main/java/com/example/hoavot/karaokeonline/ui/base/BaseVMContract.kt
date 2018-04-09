package com.example.hoavot.karaokeonline.ui.base

import io.reactivex.Single

/**
 *
 * @author at-hoavo.
 */
interface BaseVMContract {
    /**
     * Logout clear all memory
     */
    fun logOut(): Single<Boolean>

    /**
     * Re Init Api Token
     */
    fun reInitApiToken()
}