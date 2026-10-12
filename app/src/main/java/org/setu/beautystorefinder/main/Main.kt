package org.setu.beautystorefinder.main



import org.setu.beautystorefinder.models.BeautyStoreMemStore
import org.setu.beautystorefinder.models.BeautyStoreModel


val store = BeautyStoreMemStore()

fun main() {
    println("=== Beauty Store Finder Console App ===")
    var input: Int
    do {
        input = menu()
        when (input) {
            1 -> addBeautyStore()
            2 -> listBeautyStores()
            3 -> updateBeautyStore()
            4 -> deleteBeautyStore()
            5 -> searchBeautyStore()
            0 -> println("\nExiting Beauty Store Finder application. Goodbye!")
            else -> println("\nInvalid option. Please try again.")
        }
    } while (input != 0)
}

fun menu(): Int {
    println("\n----------------------------------")
    println(" MAIN MENU")
    println("----------------------------------")
    println(" 1. Add Beauty Store")
    println(" 2. List All Beauty Stores")
    println(" 3. Update a Beauty Store")
    println(" 4. Delete a Beauty Store")
    println(" 5. Search Beauty Store by ID")
    println(" 0. Exit")
    print("\nEnter option: ")
    return readlnOrNull()?.toIntOrNull() ?: -1
}

fun addBeautyStore() {
    println("\n--- Add Beauty Store ---")
    print("Enter Store Name: ")
    val title = readlnOrNull()?.trim().orEmpty()
    print("Enter Description: ")
    val description = readlnOrNull()?.trim().orEmpty()

    if (title.isNotEmpty()) {
        val beautyStore = BeautyStoreModel(title = title, description = description)
        store.create(beautyStore)
        println("Beauty Store added successfully with ID: ${beautyStore.id}")
    } else {
        println("Title cannot be empty. Creation cancelled.")
    }
}

fun listBeautyStores() {
    println("\n--- All Beauty Stores ---")
    val beautyStores = store.findAll()
    if (beautyStores.isEmpty()) {
        println("No beauty stores stored yet.")
    } else {
        beautyStores.forEach { println("ID: ${it.id} | Title: ${it.title} | Description: ${it.description}") }
    }
}

fun updateBeautyStore() {
    println("\n--- Update Beauty Store ---")
    listBeautyStores()
    if (store.findAll().isEmpty()) return

    print("\nEnter ID of Beauty Store to update: ")
    val id = readlnOrNull()?.toLongOrNull()

    if (id != null && store.findOne(id) != null) {
        print("Enter New Store Name: ")
        val title = readlnOrNull()?.trim().orEmpty()
        print("Enter New Description: ")
        val description = readlnOrNull()?.trim().orEmpty()

        if (title.isNotEmpty()) {
            val updated = store.update(BeautyStoreModel(id = id, title = title, description = description))
            if (updated) println("Beauty Store updated successfully.")
        } else {
            println("Store Name cannot be empty. Update cancelled.")
        }
    } else {
        println("Beauty Store with ID $id not found.")
    }
}

fun deleteBeautyStore() {
    println("\n--- Delete Beauty Store ---")
    listBeautyStores()
    if (store.findAll().isEmpty()) return

    print("\nEnter ID of Beauty Store to delete: ")
    val id = readlnOrNull()?.toLongOrNull()

    if (id != null) {
        val deleted = store.delete(id)
        if (deleted) {
            println("Beauty Store with ID $id deleted successfully.")
        } else {
            println("Beauty Store with ID $id not found.")
        }
    } else {
        println("Invalid ID entered.")
    }
}

fun searchBeautyStore() {
    println("\n--- Search Beauty Store ---")
    print("Enter ID: ")
    val id = readlnOrNull()?.toLongOrNull()

    if (id != null) {
        val beautyStore = store.findOne(id)
        if (beautyStore != null) {
            println("Found: ID: ${beautyStore.id} | Title: ${beautyStore.title} | Description: ${beautyStore.description}")
        } else {
            println("No Beauty Store found with ID $id.")
        }
    } else {
        println("Invalid ID entered.")
    }
}
