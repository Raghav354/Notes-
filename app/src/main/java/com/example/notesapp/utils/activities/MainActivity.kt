package com.example.notesapp.utils.activities

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.Menu
import android.view.MenuItem
import androidx.activity.result.ActivityResultLauncher
import androidx.activity.result.contract.ActivityResultContracts
import androidx.lifecycle.ViewModelProvider
import androidx.recyclerview.widget.GridLayoutManager
import androidx.recyclerview.widget.ItemTouchHelper
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.example.notesapp.R
import com.example.notesapp.adaptors.NoteAdaptor
import com.example.notesapp.databinding.ActivityMainBinding
import com.example.notesapp.room.Note
import com.example.notesapp.room.NoteViewModel
import com.example.notesapp.utils.Constants
import com.google.android.material.floatingactionbutton.FloatingActionButton
import com.google.android.material.snackbar.Snackbar

class MainActivity : AppCompatActivity(), NoteAdaptor.OnClickListener {
    private lateinit var binding: ActivityMainBinding
    private lateinit var noteViewModel: NoteViewModel
    private lateinit var recyclerView: RecyclerView
    private lateinit var notesAdaptor: NoteAdaptor
    private lateinit var addNoteButton: FloatingActionButton
    private lateinit var getResult: ActivityResultLauncher<Intent>

    override fun onCreate(savedInstanceState: Bundle?) {
        binding = ActivityMainBinding.inflate(layoutInflater)
        super.onCreate(savedInstanceState)
        setContentView(binding.root)


        addNoteButton = binding.addNoteButton
        recyclerView = binding.recyclerView
        notesAdaptor = NoteAdaptor(this)
        recyclerView.setHasFixedSize(true)
        recyclerView.adapter = notesAdaptor
        recyclerView.layoutManager = LinearLayoutManager(this)


        noteViewModel = ViewModelProvider(
            this,
            ViewModelProvider.AndroidViewModelFactory(application)
        ).get(NoteViewModel::class.java)


        noteViewModel.allNotes.observe(this) {


            notesAdaptor.submitList(it)
        }

        getResult = registerForActivityResult(ActivityResultContracts.StartActivityForResult()) {
            if (it.resultCode == Constants.REQUEST_CODE) {
                val title = it.data?.getStringExtra(Constants.EXTRA_TITLE)
                val description = it.data?.getStringExtra(Constants.EXTRA_DESCRIPTION)
                val priority = it.data?.getIntExtra(Constants.EXTRA_PRIORITY, -1)

                val note = Note(title!!, description!!, priority!!)
                noteViewModel.addNote(note)
            } else if (it.resultCode == Constants.EDIT_REQUEST_CODE) {
                val title = it.data?.getStringExtra(Constants.EXTRA_TITLE)
                val description = it.data?.getStringExtra(Constants.EXTRA_DESCRIPTION)
                val priority = it.data?.getIntExtra(Constants.EXTRA_PRIORITY, -1)
                val id = it.data?.getIntExtra(Constants.EXTRA_ID, -1)
                val note = Note(title!!, description!!, priority!!)
                note.id = id!!
                noteViewModel.updateNote(note)
            }
        }

        addNoteButton.setOnClickListener {
            val intent = Intent(this@MainActivity, AddEditActivity::class.java)
            getResult.launch(intent)
        }

        //On swipe delete
        ItemTouchHelper(object :
            ItemTouchHelper.SimpleCallback(0, ItemTouchHelper.LEFT or ItemTouchHelper.RIGHT) {
            override fun onMove(
                recyclerView: RecyclerView,
                viewHolder: RecyclerView.ViewHolder,
                target: RecyclerView.ViewHolder
            ): Boolean {
                return false
            }

            override fun onSwiped(viewHolder: RecyclerView.ViewHolder, direction: Int) {
                noteViewModel.deleteNote(notesAdaptor.getNoteAt(viewHolder.adapterPosition))

                //Undo the deleted element
                val removedItem = notesAdaptor.getNoteAt(viewHolder.adapterPosition)
                Snackbar.make(this@MainActivity, recyclerView, "Deleted Note", Snackbar.LENGTH_LONG)
                    .setAction("Undo") {
                        noteViewModel.addNote(removedItem)
                    }.show()
            }

        }).attachToRecyclerView(recyclerView)
    }

    override fun onCreateOptionsMenu(menu: Menu?): Boolean {
        menuInflater.inflate(R.menu.main_menu, menu)
        return true
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        when (item.itemId) {
            R.id.delete_all_note_menu -> {
                noteViewModel.deleteAllNotes()
            }
        }

        return super.onOptionsItemSelected(item)
    }

    override fun onClick(note: Note) {
        val title = note.title
        val description = note.description
        val priority = note.priority
        val id = note.id

        val intent = Intent(this@MainActivity, AddEditActivity::class.java)
        intent.putExtra(Constants.EXTRA_TITLE, title)
        intent.putExtra(Constants.EXTRA_DESCRIPTION, description)
        intent.putExtra(Constants.EXTRA_PRIORITY, priority)
        intent.putExtra(Constants.EXTRA_ID, id)
        getResult.launch(intent)
    }
}