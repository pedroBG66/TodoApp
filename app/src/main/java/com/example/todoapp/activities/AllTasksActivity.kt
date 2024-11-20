package com.example.todoapp.activities

import android.content.Intent
import android.os.Bundle
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.todoapp.adapter.TaskAdapter
import com.example.todoapp.daos.TaskDao
import com.example.todoapp.data.Task
import com.example.todoapp.databinding.ActivityAllTasksBinding


class AllTasksActivity : AppCompatActivity() {


    lateinit var binding: ActivityAllTasksBinding

    lateinit var adapter: TaskAdapter

    lateinit var taskDAO: TaskDao

    var taskList: List<Task> = emptyList()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        //enableEdgeToEdge()

        binding = ActivityAllTasksBinding.inflate(layoutInflater)
        setContentView(binding.root)

        ViewCompat.setOnApplyWindowInsetsListener(binding.root) { v, insets ->
            val systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars())
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom)
            insets
        }

        taskDAO = TaskDao(this)

        /*taskDAO.insert(Task(-1, "Comprar leche"))
        taskDAO.insert(Task(-1, "Pagar el alquiler"))
        taskDAO.insert(Task(-1, "Pasear al perro"))*/

        adapter = TaskAdapter(taskList) {
            val task = taskList[it]
            task.done = !task.done
            taskDAO.update(task)
            adapter.updateItems(taskList)
        }

        binding.recyclerView.adapter = adapter
        binding.recyclerView.layoutManager =
            LinearLayoutManager(this, LinearLayoutManager.VERTICAL, false)

        binding.addTaskButton.setOnClickListener {
            val intent = Intent(this, TaskActivity2::class.java)
            startActivity(intent)
        }
    }

    override fun onResume() {
        super.onResume()

        taskList = taskDAO.findAll()

        adapter.updateItems(taskList)
    }
}
