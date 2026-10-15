package org.setu.beautystorefinder.models
import java.util.concurrent.atomic.AtomicLong

class BeautyStoreMemStore {
    private val beautyStores = ArrayList<BeautyStoreModel>()
    private val lastId = AtomicLong(0L)

    fun findAll(): List<BeautyStoreModel> {
        return beautyStores
    }

    fun create(beautyStore : BeautyStoreModel) {
        beautyStore.id = lastId.incrementAndGet()
        beautyStores.add(beautyStore)
    }

    fun update(beautyStore: BeautyStoreModel): Boolean {
        val foundStore = findOne(beautyStore.id)
        return if (foundStore != null) {
            foundStore.title = beautyStore.title
            foundStore.description = beautyStore.description
            foundStore.x = beautyStore.x
            foundStore.y = beautyStore.y
            foundStore.category = beautyStore.category
            foundStore.brands = beautyStore.brands
            foundStore.address = beautyStore.address
            foundStore.website = beautyStore.website


            true
        } else {
            false
        }
    }

    fun delete(id: Long): Boolean {
        val foundStore = findOne(id)
        return if (foundStore != null) {
            beautyStores.remove(foundStore)
            true
        } else {
            false
        }
    }

    fun findOne(id: Long): BeautyStoreModel? {
        return beautyStores.find { p -> p.id == id }
    }
}