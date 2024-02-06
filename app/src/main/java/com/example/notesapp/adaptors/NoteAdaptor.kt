package com.example.notesapp.adaptors

import android.graphics.Color
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.cardview.widget.CardView
import androidx.core.content.ContextCompat
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import com.example.notesapp.R
import com.example.notesapp.room.Note
import com.example.notesapp.utils.activities.MainActivity


class NoteAdaptor(val listener: MainActivity) :
    ListAdapter<Note, NoteAdaptor.NoteViewHolder>(DIFF_CALLBACK) {

    companion object {
        private val DIFF_CALLBACK = object : DiffUtil.ItemCallback<Note>() {
            override fun areItemsTheSame(oldItem: Note, newItem: Note): Boolean {
                return oldItem.id == newItem.id
            }

            override fun areContentsTheSame(oldItem: Note, newItem: Note): Boolean {
                return oldItem == newItem
            }

        }
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): NoteViewHolder {
        return NoteViewHolder(
            LayoutInflater.from(parent.context)
                .inflate(com.example.notesapp.R.layout.note_item, parent, false)
        )
    }

    override fun onBindViewHolder(holder: NoteViewHolder, position: Int) {
        val note = getItem(position)
        holder.textViewTitle.text = note.title
        holder.textViewDescription.text = note.description
        holder.textViewPriority.text = note.priority.toString()
        var colorPos = note.priority
        val color = when (colorPos) {


            1 -> ContextCompat.getColor(holder.itemView.context, R.color.list_color7)
            2 -> ContextCompat.getColor(holder.itemView.context, R.color.list_color7)
            3 -> ContextCompat.getColor(holder.itemView.context, R.color.list_color7)

            4 -> ContextCompat.getColor(holder.itemView.context, R.color.list_color4)
            5 -> ContextCompat.getColor(holder.itemView.context, R.color.list_color4)
            6 -> ContextCompat.getColor(holder.itemView.context, R.color.list_color4)
            7 -> ContextCompat.getColor(holder.itemView.context, R.color.list_color4)

            8 -> ContextCompat.getColor(holder.itemView.context, R.color.list_color3)
            9 -> ContextCompat.getColor(holder.itemView.context, R.color.list_color3)
            10 -> ContextCompat.getColor(holder.itemView.context, R.color.list_color3)
            else -> throw IllegalArgumentException("Invalid color position: $colorPos")
        }

        holder.cardView.setCardBackgroundColor(color)


    }


    fun getNoteAt(position: Int): Note {
        return getItem(position)

    }


    inner class NoteViewHolder(view: View) : RecyclerView.ViewHolder(view) {
        val textViewTitle = view.findViewById(R.id.text_view_title) as TextView
        val textViewDescription = view.findViewById(R.id.textView_description) as TextView
        val textViewPriority = view.findViewById(R.id.text_view_priority) as TextView
        val cardView = view.findViewById<CardView>(R.id.cardView)

        init {
            view.setOnClickListener {
                if (adapterPosition != RecyclerView.NO_POSITION) {
                    listener.onClick(getItem(adapterPosition))
                }
            }
        }
    }

    interface OnClickListener {
        fun onClick(note: Note)
    }
}
