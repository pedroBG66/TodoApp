package com.example.todoapp.adapter

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.example.todoapp.data.Task
import com.example.todoapp.databinding.ItemTasklayoutBinding

class TaskAdapter(var items: List<Task>,
                  val onItemUpdate: (Int) -> Unit,
                  val onItemCheck: (Int) -> Unit,
                  val onItemDelete: (Int) -> Unit
) : RecyclerView.Adapter<ViewHolder>() {
    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        val task = items[position]
        holder.render(task)
        holder.itemView.setOnClickListener {
            onItemCheck(position)
        }
        holder.binding.doneCheckBox.setOnCheckedChangeListener { checkBox, _ ->
            //esto era para resolver el problema del checkbox
            if (checkBox.isPressed) {
                onItemCheck(position)
            }
        }
        holder.binding.deleteButton.setOnClickListener {
            onItemDelete(position)
        }
        holder.binding.updateButton.setOnClickListener {
            onItemUpdate(position)
        }
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val binding = ItemTasklayoutBinding.inflate(LayoutInflater.from(parent.context), parent, false)
        return ViewHolder(binding)
    }

    override fun getItemCount(): Int {
        return items.size
    }

    fun updateItems(items: List<Task>) {
        this.items = items
        notifyDataSetChanged()
    }
}

class ViewHolder(val binding: ItemTasklayoutBinding) : RecyclerView.ViewHolder(binding.root) {

    fun render(task: Task) {
        binding.taskTitleTextView.text = task.name
        binding.doneCheckBox.isChecked = task.done
    }
}