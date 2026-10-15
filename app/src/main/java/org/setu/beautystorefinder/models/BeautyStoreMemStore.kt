package org.setu.beautystorefinder.models
import java.util.concurrent.atomic.AtomicLong


// Stores beauty store records in memory while the app is running
class BeautyStoreMemStore {
    private val beautyStores = ArrayList<BeautyStoreModel>()

    // Generates a unique ID for each new beauty store
    // I didn't show the IDs visually in my View Stores, as I didn't want it to be viewed as a numbered list
    private val lastId = AtomicLong(0L)


    fun findAll(): List<BeautyStoreModel> {
        return beautyStores
    }

    fun create(beautyStore : BeautyStoreModel) {
        beautyStore.id = lastId.incrementAndGet()
        beautyStores.add(beautyStore)
    }

    // Find a store by its ID and update its details if it exists
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

    // Removes a store using its ID and returns whether deletion succeeded
    fun delete(id: Long): Boolean {
        val foundStore = findOne(id)
        return if (foundStore != null) {
            beautyStores.remove(foundStore)
            true
        } else {
            false
        }
    }

    // Find a specific store by its ID
    fun findOne(id: Long): BeautyStoreModel? {
        return beautyStores.find { p -> p.id == id }
    }
}