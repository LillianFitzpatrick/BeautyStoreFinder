package org.setu.beautystorefinder




import android.os.Bundle

import android.widget.Button
import android.widget.EditText
import android.widget.TextView
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import android.widget.Spinner
import android.widget.ArrayAdapter
import timber.log.Timber
import android.net.Uri
import android.widget.ImageView
import androidx.activity.result.contract.ActivityResultContracts
import android.content.Intent

import org.setu.beautystorefinder.models.BeautyStoreModel

class AddEditActivity : AppCompatActivity() {

    private lateinit var titleInput: EditText
    private lateinit var descriptionInput: EditText
    private lateinit var xInput: EditText
    private lateinit var yInput: EditText

    // spinner used for dropdown menu
    private lateinit var categorySpinner: Spinner

    private lateinit var brandsInput: EditText
    private lateinit var addressInput: EditText
    private lateinit var websiteInput: EditText

    private var editingId: Long? = null

    private var selectedLatitude: Double? = null
    private var selectedLongitude: Double? = null


    private lateinit var imagePreview: ImageView
    private var selectedImageUri: Uri? = null

    private val imagePicker =
        registerForActivityResult(ActivityResultContracts.OpenDocument()) { uri: Uri? ->
            if (uri != null) {
                // Keep access to the document URI for later use, when supported.
                try {
                    contentResolver.takePersistableUriPermission(
                        uri,
                        android.content.Intent.FLAG_GRANT_READ_URI_PERMISSION
                    )
                } catch (_: SecurityException) {
                    // Some providers may not grant persistable access.
                }

                selectedImageUri = uri
                imagePreview.setImageURI(uri)
            }
        }



    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        setContentView(R.layout.activity_add_edit)

        imagePreview = findViewById(R.id.imageViewAdd)

        findViewById<Button>(R.id.imagePickButton).setOnClickListener {
            imagePicker.launch(arrayOf("image/*"))
        }

        titleInput =
            findViewById(R.id.titleInput)

        descriptionInput =
            findViewById(R.id.descriptionInput)

        xInput =
            findViewById(R.id.xInput)

        yInput =
            findViewById(R.id.yInput)


        val mapPicker =
            registerForActivityResult(
                ActivityResultContracts.StartActivityForResult()
            ) { result ->
                if (result.resultCode == RESULT_OK) {
                    val data = result.data ?: return@registerForActivityResult
                    selectedLatitude = data.getDoubleExtra("latitude", 0.0)
                    selectedLongitude = data.getDoubleExtra("longitude", 0.0)

                    selectedLatitude?.let {
                        xInput.setText(it.toString())
                    }

                    selectedLongitude?.let {
                        yInput.setText(it.toString())
                    }
                }
            }

        findViewById<Button>(R.id.buttonLoc).setOnClickListener {
            val intent = Intent(this, MapPickerActivity::class.java)


            val latitude = xInput.text.toString().toDoubleOrNull()
            val longitude = yInput.text.toString().toDoubleOrNull()

            if(latitude !=null && longitude !=null){
                intent.putExtra("latitude", latitude)
                intent.putExtra("longitude", longitude)
            }


            mapPicker.launch(intent)
        }



        brandsInput = findViewById(R.id.brandsInput)
        addressInput = findViewById(R.id.addressInput)
        websiteInput = findViewById(R.id.websiteInput)

        // Populates the category dropdown using the options in strings.xml

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

        // Checks if a store ID was passed to determine whether we are editing
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

    // Load the selected store's details into the form for editing
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

        selectedImageUri = store.imageUri?.let { Uri.parse(it) }

        if (selectedImageUri != null) {
            imagePreview.setImageURI(selectedImageUri)
        } else {
            imagePreview.setImageResource(android.R.drawable.ic_menu_gallery)
        }


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



        //Validate the required store name and coordinate inputs before saving

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

        // Create a new store or update the existing store

        try{

        if (editingId == null) {

            val store = BeautyStoreModel(
                title = title,
                description = description,
                x = x,
                y = y,
                category = category,
                brands = brands,
                address = address,
                website = website,
                imageUri = selectedImageUri?.toString()

            )

            AppData.beautyStores.create(store)
            Timber.d("Created beauty store id=${store.id}, title=${store.title}")

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
                website = website,
                imageUri = selectedImageUri?.toString()

            )

            AppData.beautyStores.update(store)
            Timber.d("Updated beauty store id=${store.id}, title=${store.title}")

            Toast.makeText(
                this,
                "Store updated",
                Toast.LENGTH_SHORT
            ).show()
        }
        } catch (e: IllegalArgumentException) {
            android.widget.Toast.makeText(
                this,
                e.message ?: "Please check the entered values.",
                android.widget.Toast.LENGTH_LONG
            ).show()
            return
        }

        finish()
    }
}
