package org.setu.beautystorefinder

import org.setu.beautystorefinder.models.BeautyStoreMemStore

// Shared in-memory store so all activities access the same beauty store records
object AppData {
    val beautyStores = BeautyStoreMemStore()
}