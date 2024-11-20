package com.example.todoapp.activities

import android.os.Bundle
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat
import com.example.todoapp.R
import com.example.todoapp.daos.TaskDao
import com.example.todoapp.data.Task
import com.example.todoapp.databinding.ActivityTask2Binding

class TaskActivity2 : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {

        lateinit var binding: ActivityTask2Binding

        super.onCreate(savedInstanceState)
        //enableEdgeToEdge()

        binding = ActivityTask2Binding.inflate(layoutInflater)
        setContentView(binding.root)

        ViewCompat.setOnApplyWindowInsetsListener(binding.root) { v, insets ->
            val systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars())
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom)
            insets
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
            val task = Task(-1, taskName)

            val taskDAO = TaskDao(this)
            taskDAO.insert(task)

            finish()
        }
    }
}