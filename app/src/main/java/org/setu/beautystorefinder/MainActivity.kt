package org.setu.beautystorefinder

import android.os.Bundle
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat
// import android.widget.Button unused, went for CardView option here instead for aesthetics
import android.content.Intent
import androidx.cardview.widget.CardView

class MainActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContentView(R.layout.activity_menu)
        // Adjusts screen padding to prevent content overlapping the system bars
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main)) { v, insets ->
            val systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars())
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom)
            insets
        }
        // Home menu cards navigate to the store list and store screens ( view stores and add stores)
        val viewStoresCard =
            findViewById<CardView>(R.id.viewStoresCard)

        val addStoreCard =
            findViewById<CardView>(R.id.addStoreCard)

        viewStoresCard.setOnClickListener {
            startActivity(
                Intent(this, StoreListActivity::class.java)
            )
        }

        addStoreCard.setOnClickListener {
            startActivity(
                Intent(this, AddEditActivity::class.java)
            )
        }

    }
}
