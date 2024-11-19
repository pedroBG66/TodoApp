package com.example.todoapp.activities

import android.content.ContentValues
import android.os.Bundle
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity
import androidx.core.content.contentValuesOf
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat
import com.example.todoapp.R
import com.example.todoapp.daos.TaskDao
import com.example.todoapp.data.Task
import com.example.todoapp.utils.DbHelper

class MainActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContentView(R.layout.activity_main)
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main)) { v, insets ->
            val systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars())
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom)
            insets
        }
        val taskDao = TaskDao(this)
        taskDao.insert(Task(-1, "testing5", false))
        println("Inserted row ID: $taskDao")

        val taskList = taskDao.findAll()
        for(task in taskList){
            println("Task list size: ${taskList.size}")
            println(task)
        }
    }
}