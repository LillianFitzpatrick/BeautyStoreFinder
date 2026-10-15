package org.setu.beautystorefinder



import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import org.setu.beautystorefinder.models.BeautyStoreModel

// Displays beauty stores in the RecyclerView and handles Edit/Delete Actions
class BeautyStoreAdapter(
    private var stores: List<BeautyStoreModel>,
    private val onEdit: (BeautyStoreModel) -> Unit,
    private val onDelete: (BeautyStoreModel) -> Unit
) : RecyclerView.Adapter<BeautyStoreAdapter.StoreViewHolder>() {

    // Holds references to the UI elements for each store card
    class StoreViewHolder (itemView: View) :
        RecyclerView.ViewHolder(itemView) {

        val titleText: TextView =
            itemView.findViewById(R.id.titleText)

        val descriptionText: TextView =
            itemView.findViewById(R.id.descriptionText)

        val coordinatesText: TextView =
            itemView.findViewById(R.id.coordinatesText)

        val editButton: Button =
            itemView.findViewById(R.id.editButton)

        val deleteButton: Button =
            itemView.findViewById(R.id.deleteButton)
    }

    // Creates a new store card using the item_beauty_store layout
    override fun onCreateViewHolder(
        parent: ViewGroup,
        viewType: Int
    ): StoreViewHolder {

        val view = LayoutInflater.from(parent.context)
            .inflate(
                R.layout.item_beauty_store,
                parent,
                false
            )

        return StoreViewHolder(view)
    }


    override fun onBindViewHolder(
        holder: StoreViewHolder,
        position: Int
    ) {

        val store = stores[position]

        holder.titleText.text =
            //"${store.id}: ${store.title}"
            // wanted to change this to not display numbers alongside the stores, for aesthetics
            store.title

        // Displays the selected store's details in its card
        holder.descriptionText.text =
            //store.description
            "${store.description}\n" +
                    "Category: ${store.category}\n" +
                    "Brands: ${store.brands}\n" +
                    "Address: ${store.address}\n" +
                    "Website: ${store.website}"




        holder.coordinatesText.text =
            "X: ${store.x}, Y: ${store.y}"

        holder.editButton.setOnClickListener {
            onEdit(store)
        }

        holder.deleteButton.setOnClickListener {
            onDelete(store)
        }
    }

    override fun getItemCount(): Int {
        return stores.size
    }

    // Refreshes the RecyclerView when the search results change
    fun updateStores(newStores: List<BeautyStoreModel>) {
        stores = newStores
        notifyDataSetChanged()
    }
}
