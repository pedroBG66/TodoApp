package com.example.todoapp.activities

import android.content.Intent
import android.os.Bundle
import androidx.appcompat.app.AlertDialog
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.todoapp.R
import com.example.todoapp.adapter.TaskAdapter
import com.example.todoapp.daos.TaskDao
import com.example.todoapp.data.Task
import com.example.todoapp.databinding.ActivityAllTasksBinding
import com.example.todoapp.databinding.ActivityTask2Binding


class AllTasksActivity : AppCompatActivity() {


    lateinit var binding: ActivityAllTasksBinding

    lateinit var adapter: TaskAdapter

    lateinit var taskDAO: TaskDao

    var taskList: MutableList<Task> = mutableListOf()

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

        adapter = TaskAdapter(taskList, {
            // Editar tarea
            val task = taskList[it]
            showTask(task)
        }, {
            // Marcar tarea
            val task = taskList[it]
            checkTask(task)
        }, {
            // Borrar tarea
            val task = taskList[it]
            deleteTask(task)
        })


        //esto lo creamos al inicio para ver si funciona la actualizacion
        /*adapter = TaskAdapter(taskList) {
            val task = taskList[it]
            task.done = !task.done
            taskDAO.update(task)
            adapter.updateItems(taskList)
        }*/

        binding.recyclerView.adapter = adapter
        binding.recyclerView.layoutManager =
            LinearLayoutManager(this, LinearLayoutManager.VERTICAL, false)

        binding.addTaskButton.setOnClickListener {
            val alertDialogBinding =
                ActivityTask2Binding.inflate(layoutInflater) // Nombre generado por ViewBinding para dialog_task.xml
            val alertDialog = AlertDialog.Builder(this)
                .setView(alertDialogBinding.root)
                .create()
            alertDialog.show()

            alertDialogBinding.saveButton.setOnClickListener {

                val taskName = alertDialogBinding.nameTextField.editText?.text.toString()
                if (taskName.isEmpty()) {
                    alertDialogBinding.nameTextField.error = "Escribe algo"
                    return@setOnClickListener
                }
                if (taskName.length > 50) {
                    alertDialogBinding.nameTextField.error = "Te pasaste"
                    return@setOnClickListener
                }

                val task = Task(-1, taskName)
                taskDAO.insert(task)

                alertDialog.dismiss()
                taskList = taskDAO.findAll().toMutableList()
                adapter.updateItems(taskList)
            }
        }

    }

    override fun onResume() {
        super.onResume()
        taskList = taskDAO.findAll().toMutableList()
        adapter.updateItems(taskList)
    }

    // Funcion para cuando marcamos una tarea (finalizada/pendiente)
    fun checkTask(task: Task) {
        task.done = !task.done
        taskDAO.update(task)
        adapter.updateItems(taskList)
    }

    // Funciona para mostrar un dialogo para borrar la tarea
    fun deleteTask(task: Task) {
        // Mostramos un dialogo para asegurarnos de que el usuario quiere borrar la tarea
        AlertDialog.Builder(this)
            .setTitle("Borrar tarea")
            .setMessage("Estas seguro de que quieres borrar la tarea?")
            .setPositiveButton(android.R.string.ok) { _, which ->
                // Borramos la tarea en caso de pulsar el boton OK
                taskDAO.delete(task)
                taskList.remove(task)
                adapter.updateItems(taskList)
            }
            .setNegativeButton(android.R.string.cancel, null)
            .setIcon(R.drawable.ic_delete)
            .show()
    }

    // Mostramos la tarea para editarla
    fun showTask(task: Task) {
        val intent = Intent(this, TaskActivity2::class.java)
        intent.putExtra(TaskActivity2.EXTRA_TASK_ID, task.id)
        startActivity(intent)
    }
}
