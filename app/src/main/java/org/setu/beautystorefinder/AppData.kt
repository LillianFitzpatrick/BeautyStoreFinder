package org.setu.beautystorefinder

import android.content.Context
import org.setu.beautystorefinder.models.BeautyStoreMemStore
import java.io.File

// Shared store so all activities access the same beauty store records
object AppData {

    lateinit var beautyStores: BeautyStoreMemStore
        private set

    fun initialize(context: Context) {
        val file = File(
            context.filesDir,
            "beauty_store_list.json"
        )

        beautyStores = BeautyStoreMemStore(file)
    }
}