package org.setu.beautystorefinder

import android.os.Bundle
import android.widget.Button
import android.widget.EditText
import android.widget.LinearLayout
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import org.setu.beautystorefinder.models.BeautyStoreModel

class AddEditActivity : AppCompatActivity() {

    private lateinit var titleInput: EditText
    private lateinit var descriptionInput: EditText
    private lateinit var xInput: EditText
    private lateinit var yInput: EditText

    private var editingId: Long? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        createUserInterface()

        editingId = intent.getLongExtra("id", -1L)

        if (editingId != -1L) {
            loadExistingStore(editingId!!)
        }
    }

    private fun createUserInterface() {

        val root = LinearLayout(this).apply {
            orientation = LinearLayout.VERTICAL
            setPadding(32, 320, 32, 32)
        }

        titleInput = EditText(this).apply {
            hint = "Title"
        }

        descriptionInput = EditText(this).apply {
            hint = "Description"
        }

        xInput = EditText(this).apply {
            hint = "X coordinate"
            inputType =
                android.text.InputType.TYPE_CLASS_NUMBER or
                        android.text.InputType.TYPE_NUMBER_FLAG_DECIMAL
        }

        yInput = EditText(this).apply {
            hint = "Y coordinate"
            inputType =
                android.text.InputType.TYPE_CLASS_NUMBER or
                        android.text.InputType.TYPE_NUMBER_FLAG_DECIMAL
        }

        val saveButton = Button(this).apply {
            text = "Save"

            setOnClickListener {
                saveStore()
            }
        }

        val cancelButton = Button(this).apply {
            text = "Cancel"

            setOnClickListener {
                finish()
            }
        }

        root.addView(titleInput)
        root.addView(descriptionInput)
        root.addView(xInput)
        root.addView(yInput)
        root.addView(saveButton)
        root.addView(cancelButton)

        setContentView(root)
    }

    private fun loadExistingStore(id: Long) {

        val store = AppData.beautyStores.findOne(id)

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
    }

    private fun saveStore() {

        val title = titleInput.text.toString().trim()
        val description = descriptionInput.text.toString().trim()

        if (title.isEmpty()) {
            titleInput.error = "Title is required"
            return
        }

        val x = xInput.text.toString().toDoubleOrNull()

        if (x == null) {
            xInput.error = "Enter a valid number"
            return
        }

        val y = yInput.text.toString().toDoubleOrNull()

        if (y == null) {
            yInput.error = "Enter a valid number"
            return
        }

        if (editingId == null || editingId == -1L) {

            val mark = BeautyStoreModel(
                title = title,
                description = description,
                x = x,
                y = y
            )

            AppData.beautyStores.create(mark)

            Toast.makeText(
                this,
                "Store created",
                Toast.LENGTH_SHORT
            ).show()

        } else {

            val mark = BeautyStoreModel(
                id = editingId!!,
                title = title,
                description = description,
                x = x,
                y = y
            )

            AppData.beautyStores.update(mark)

            Toast.makeText(
                this,
                "Store updated",
                Toast.LENGTH_SHORT
            ).show()
        }

        finish()
    }
}
