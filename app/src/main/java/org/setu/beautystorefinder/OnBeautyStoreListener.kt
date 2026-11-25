package org.setu.beautystorefinder
import org.setu.beautystorefinder.models.BeautyStoreModel

interface OnBeautyStoreListener {

    fun onBeautyStoreClick(store: BeautyStoreModel)

    fun onBeautyStoreLongClick(store: BeautyStoreModel): Boolean

}
