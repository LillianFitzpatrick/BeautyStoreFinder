package org.setu.beautystorefinder.models



/**
 * Data class representing a beauty store and stores its details.
 * Kotlin automatically generates toString(), equals(), hashCode(), and copy().
 */
data class BeautyStoreModel(
    var id:  Long = 0L,
    var title: String = "",
    var description: String = "",
    var x: Double = 0.0,
    var y: Double = 0.0,
    var category: String = "Drugstore",
    var brands: String= "",
    var address: String= "",
    var website: String= "",
    var imageUri: String? = null
) {
    fun validate() {
        require(title.isNotBlank()) {
            "Name must not be empty."
        }

        require(x.isFinite() && x in -90.0..90.0) {
            "Latitude must be between -90 and 90."
        }

        require(y.isFinite() && y in -180.0..180.0) {
            "Longitude must be between -180 and 180."
        }
    }
}
