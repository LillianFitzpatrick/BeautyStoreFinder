package org.setu.beautystorefinder



import android.adservices.ondevicepersonalization.ExecuteInput
import android.os.Bundle
import android.widget.AbsSpinner
import android.widget.Button
import android.widget.EditText
import android.widget.TextView
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import android.widget.Spinner
import android.widget.ArrayAdapter

import org.setu.beautystorefinder.models.BeautyStoreModel

class AddEditActivity : AppCompatActivity() {

    private lateinit var titleInput: EditText
    private lateinit var descriptionInput: EditText
    private lateinit var xInput: EditText
    private lateinit var yInput: EditText

    private lateinit var categorySpinner: Spinner

    private lateinit var brandsInput: EditText
    private lateinit var addressInput: EditText
    private lateinit var websiteInput: EditText

    private var editingId: Long? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        setContentView(R.layout.activity_add_edit)

        titleInput =
            findViewById(R.id.titleInput)

        descriptionInput =
            findViewById(R.id.descriptionInput)

        xInput =
            findViewById(R.id.xInput)

        yInput =
            findViewById(R.id.yInput)

        brandsInput = findViewById(R.id.brandsInput)
        addressInput = findViewById(R.id.addressInput)
        websiteInput = findViewById(R.id.websiteInput)

        categorySpinner = findViewById(R.id.categorySpinner)
        ArrayAdapter.createFromResource(
            this,
            R.array.makeup_categories,
            android.R.layout.simple_spinner_item
        ).also {adapter ->
            adapter.setDropDownViewResource(
                android.R.layout.simple_spinner_dropdown_item
            )
            categorySpinner.adapter = adapter
        }

        val saveButton =
            findViewById<Button>(R.id.saveButton)

        val cancelButton =
            findViewById<Button>(R.id.cancelButton)

        editingId =
            intent.getLongExtra("id", -1L)
                .takeIf { it != -1L }

        if (editingId != null) {

            findViewById<TextView>(R.id.formTitle)
                .text = "Edit Store"

            loadExistingStore(editingId!!)
        }

        saveButton.setOnClickListener {
            saveStore()
        }

        cancelButton.setOnClickListener {
            finish()
        }
    }

    private fun loadExistingStore(id: Long) {

        val store =
            AppData.beautyStores.findOne(id)

        if (store == null) {

            Toast.makeText(
                this,
                "Store not found",
                Toast.LENGTH_SHORT
            ).show()

            finish()
            return
        }

        titleInput.setText(store.title)
        descriptionInput.setText(store.description)
        xInput.setText(store.x.toString())
        yInput.setText(store.y.toString())
        brandsInput.setText(store.brands)
        addressInput.setText(store.address)
        websiteInput.setText(store.website)

        val categories = resources.getStringArray(R.array.makeup_categories)

        val categoryPosition = categories.indexOf(store.category)

        if (categoryPosition >= 0) {
            categorySpinner.setSelection(categoryPosition)
        }

    }

    private fun saveStore() {

        val title =
            titleInput.text.toString().trim()

        val description =
            descriptionInput.text.toString().trim()

        val category =
            categorySpinner.selectedItem.toString()

        val brands =
            brandsInput.text.toString().trim()

        val address =
            addressInput.text.toString().trim()

        val website =
            websiteInput.text.toString().trim()




        if (title.isEmpty()) {
            titleInput.error = "Title is required"
            return
        }

        val x =
            xInput.text.toString().toDoubleOrNull()

        if (x == null) {
            xInput.error = "Enter a valid number"
            return
        }

        val y =
            yInput.text.toString().toDoubleOrNull()

        if (y == null) {
            yInput.error = "Enter a valid number"
            return
        }

        if (editingId == null) {

            val store = BeautyStoreModel(
                title = title,
                description = description,
                x = x,
                y = y,
                category = category,
                brands = brands,
                address = address,
                website = website
            )

            AppData.beautyStores.create(store)

            Toast.makeText(
                this,
                "Store created",
                Toast.LENGTH_SHORT
            ).show()

        } else {

            val store = BeautyStoreModel(
                id = editingId!!,
                title = title,
                description = description,
                x = x,
                y = y,
                category = category,
                brands = brands,
                address = address,
                website = website
            )

            AppData.beautyStores.update(store)

            Toast.makeText(
                this,
                "Store updated",
                Toast.LENGTH_SHORT
            ).show()
        }

        finish()
    }
}
