package org.setu.beautystorefinder.models
import java.util.concurrent.atomic.AtomicLong
import timber.log.Timber
import com.google.gson.GsonBuilder
import com.google.gson.reflect.TypeToken
import java.io.File

// Stores beauty store records in memory and persists them to a JSON file
class BeautyStoreMemStore(private val file: File) {
    private val beautyStores = ArrayList<BeautyStoreModel>()

    // Generates a unique ID for each new beauty store
    // I didn't show the IDs visually in my View Stores, as I didn't want it to be viewed as a numbered list
    private val lastId = AtomicLong(0L)

    private val gson = GsonBuilder()
        .setPrettyPrinting()
        .create()

    init {
        load()
    }

    private fun load() {
        if (!file.exists()) return

        val type = object : TypeToken<ArrayList<BeautyStoreModel>>() {}.type

        val loaded: ArrayList<BeautyStoreModel> =
            gson.fromJson(file.readText(), type) ?: arrayListOf()

        beautyStores.clear()
        beautyStores.addAll(loaded)

        lastId.set(beautyStores.maxOfOrNull { it.id } ?: 0L)
    }

    private fun save() {
        file.parentFile?.mkdirs()
        file.writeText(gson.toJson(beautyStores))
    }






    fun findAll(): List<BeautyStoreModel> {
        return beautyStores
    }

    fun create(beautyStore : BeautyStoreModel) {
        beautyStore.validate()

        beautyStore.id = lastId.incrementAndGet()
        beautyStores.add(beautyStore)
        save()

        Timber.d("Store created in memory, id=${beautyStore.id}")
    }

    // Find a store by its ID and update its details if it exists
    fun update(beautyStore: BeautyStoreModel): Boolean {

        beautyStore.validate()

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
            foundStore.imageUri = beautyStore.imageUri
            save()


            Timber.d("Store updated in memory, id=${beautyStore.id}")


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
            save()

            Timber.d("Store deleted from memory, id=$id")


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