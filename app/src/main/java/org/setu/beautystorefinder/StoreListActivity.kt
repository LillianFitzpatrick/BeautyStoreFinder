package org.setu.beautystorefinder


import android.content.Intent
import android.os.Bundle
import android.widget.Button
import androidx.appcompat.app.AppCompatActivity
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import org.setu.beautystorefinder.models.BeautyStoreModel
import android.widget.SearchView


class StoreListActivity : AppCompatActivity() {

    private lateinit var recyclerView: RecyclerView
    private lateinit var adapter: BeautyStoreAdapter

    private lateinit var searchView: SearchView

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

        //Search listener
        searchView = findViewById(R.id.storeSearchView)

        searchView.setOnQueryTextListener(object : SearchView.OnQueryTextListener {

            override fun onQueryTextSubmit(query: String?): Boolean {
                return false
            }

            override fun onQueryTextChange(newText: String?): Boolean {
                filterStores(newText.orEmpty())
                return true
            }
        })



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

        if (::adapter.isInitialized  && ::searchView.isInitialized) {

            //adapter.updateStores(
            //    AppData.beautyStores.findAll()
           // )

            // Keeps the search updated after editing or deleting
            filterStores(searchView.query.toString())
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

        //adapter.updateStores(
        //    AppData.beautyStores.findAll()
       // )

        filterStores(searchView.query.toString())
    }

    // search by Store Name OR Brand Name
    private fun filterStores(query: String){
        val filteredStores = AppData.beautyStores.findAll().filter {
            it.title.contains(query, ignoreCase = true) ||
                    it.brands.contains(query, ignoreCase = true)
        }
        adapter.updateStores(filteredStores)
    }
}
