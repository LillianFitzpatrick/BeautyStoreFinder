package org.setu.beautystorefinder


import android.content.Intent
import android.os.Bundle
import android.widget.Button
import androidx.appcompat.app.AppCompatActivity
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import org.setu.beautystorefinder.models.BeautyStoreModel

class StoreListActivity : AppCompatActivity() {

    private lateinit var recyclerView: RecyclerView
    private lateinit var adapter: BeautyStoreAdapter

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        setContentView(R.layout.activity_store_list)

        recyclerView =
            findViewById(R.id.storesRecyclerView)

        adapter = BeautyStoreAdapter(
            stores = AppData.beautyStores.findAll(),

            onEdit = { store ->
                editStore(store)
            },

            onDelete = { store ->
                deleteStore(store)
            }
        )

        recyclerView.layoutManager =
            LinearLayoutManager(this)

        recyclerView.adapter = adapter

        val returnButton =
            findViewById<Button>(R.id.returnButton)

        returnButton.setOnClickListener {

            startActivity(
                Intent(this, MainActivity::class.java)
            )
        }
    }

    override fun onResume() {
        super.onResume()

        if (::adapter.isInitialized) {

            adapter.updateStores(
                AppData.beautyStores.findAll()
            )
        }
    }

    private fun editStore(store: BeautyStoreModel) {

        val intent =
            Intent(this, AddEditActivity::class.java)

        intent.putExtra("id", store.id)

        startActivity(intent)
    }

    private fun deleteStore(store: BeautyStoreModel) {

        AppData.beautyStores.delete(store.id)

        adapter.updateStores(
            AppData.beautyStores.findAll()
        )
    }
}
