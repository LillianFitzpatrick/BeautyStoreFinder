package org.setu.beautystorefinder



import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import org.setu.beautystorefinder.models.BeautyStoreModel

class BeautyStoreAdapter(
    private var stores: List<BeautyStoreModel>,
    private val onEdit: (BeautyStoreModel) -> Unit,
    private val onDelete: (BeautyStoreModel) -> Unit
) : RecyclerView.Adapter<BeautyStoreAdapter.StoreViewHolder>() {

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
            "${store.id}: ${store.title}"

        holder.descriptionText.text =
            store.description

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

    fun updateStores(newStores: List<BeautyStoreModel>) {
        stores = newStores
        notifyDataSetChanged()
    }
}
