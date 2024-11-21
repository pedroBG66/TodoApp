package com.example.todoapp.activities

import android.os.Bundle
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AlertDialog
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat
import com.example.todoapp.R
import com.example.todoapp.daos.TaskDao
import com.example.todoapp.data.Task
import com.example.todoapp.databinding.ActivityTask2Binding
import com.example.todoapp.databinding.ItemTasklayoutBinding

class TaskActivity2 : AppCompatActivity() {

    companion object {
        //esto lo usamos para el extra put
        const val EXTRA_TASK_ID = "TASK_ID"
    }

    lateinit var binding: ActivityTask2Binding
    lateinit var taskDAO: TaskDao
    lateinit var task: Task

    override fun onCreate(savedInstanceState: Bundle?) {

        super.onCreate(savedInstanceState)
        //enableEdgeToEdge()

        binding = ActivityTask2Binding.inflate(layoutInflater)
        setContentView(binding.root)

        ViewCompat.setOnApplyWindowInsetsListener(binding.root) { v, insets ->
            val systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars())
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom)
            insets
        }

        val id = intent.getLongExtra(EXTRA_TASK_ID, -1L)

        taskDAO = TaskDao(this)

        val isEditing = id != -1L // Verificar si la tarea existe
        if (isEditing) {
            task = taskDAO.findById(id)!!
            binding.nameTextField.editText?.setText(task.name) // Precarga del nombre
        }else {
            task = Task(-1, "")
        }

        binding.saveButton.setOnClickListener {

            val taskName = binding.nameTextField.editText?.text.toString()
            if (taskName.isEmpty()) {
                binding.nameTextField.error = "Escribe algo"
                return@setOnClickListener
            }
            if (taskName.length > 50) {
                binding.nameTextField.error = "Te pasaste"
                return@setOnClickListener
            }


            task.name = taskName

            // Si la tarea existe la actualizamos si no la insertamos
            if (isEditing) {
                taskDAO.update(task)
            } else {
                taskDAO.insert(task)
            }

            finish()
        }
    }
}