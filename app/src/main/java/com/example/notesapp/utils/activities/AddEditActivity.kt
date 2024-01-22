package com.example.notesapp.utils.activities

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.Menu
import android.view.MenuItem
import android.widget.EditText
import android.widget.NumberPicker
import android.widget.Toast
import android.widget.Toolbar
import androidx.room.util.splitToIntList
import com.example.notesapp.R
import com.example.notesapp.databinding.ActivityAddEditBinding
import com.example.notesapp.databinding.ActivityMainBinding
import com.example.notesapp.utils.Constants

class AddEditActivity : AppCompatActivity() {
    private lateinit var editTextTitle: EditText
    private lateinit var editTextDescription: EditText
    private lateinit var numberPicker: NumberPicker
    private lateinit var binding: ActivityAddEditBinding
    private lateinit var toolbar: androidx.appcompat.widget.Toolbar
    override fun onCreate(savedInstanceState: Bundle?) {

        binding = ActivityAddEditBinding.inflate(layoutInflater)
        super.onCreate(savedInstanceState)
        setContentView(binding.root)

        editTextTitle = binding.editTextTitle
        editTextDescription = binding.editTextDescription
        numberPicker = binding.numberPickerPriority
        toolbar = binding.toolbar


        numberPicker.minValue = 0
        numberPicker.maxValue = 10

        setSupportActionBar(toolbar)
        supportActionBar?.setDisplayHomeAsUpEnabled(true)
        supportActionBar?.setHomeAsUpIndicator(R.drawable.close)
        if (intent.hasExtra(Constants.EXTRA_ID)) {
            title = "Edit Note"
            editTextTitle.setText(intent.getStringExtra(Constants.EXTRA_TITLE))
            editTextDescription.setText(intent.getStringExtra(Constants.EXTRA_DESCRIPTION))
            numberPicker.value = intent.getIntExtra(Constants.EXTRA_PRIORITY, -1)
        } else {
            title = "Add Note"
        }


    }

    override fun onCreateOptionsMenu(menu: Menu?): Boolean {
        menuInflater.inflate(R.menu.save_menu, menu)
        return true
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        when (item.itemId) {
            R.id.save_menu_item -> {
                saveNote()
            }
        }
        return super.onOptionsItemSelected(item)
    }

    private fun saveNote() {
        val title = editTextTitle.text.toString()
        val description = editTextDescription.text.toString()
        val priority = numberPicker.value

        if (title.trim().isEmpty() || description.trim().isEmpty()) {
            Toast.makeText(this, "Please insert title and description", Toast.LENGTH_SHORT).show()
            return
        }

        val id = intent.getIntExtra(Constants.EXTRA_ID, -1)
        if (id != -1) {
            setResult(Constants.EDIT_REQUEST_CODE, Intent().apply {
                putExtra(Constants.EXTRA_TITLE, title)
                putExtra(Constants.EXTRA_DESCRIPTION, description)
                putExtra(Constants.EXTRA_PRIORITY, priority)
                putExtra(Constants.EXTRA_ID, id)
            })
        } else {
            setResult(Constants.REQUEST_CODE, Intent().apply {
                putExtra(Constants.EXTRA_TITLE, title)
                putExtra(Constants.EXTRA_DESCRIPTION, description)
                putExtra(Constants.EXTRA_PRIORITY, priority)
            })
        }
        finish()
    }


}