package com.example.todoapp.activities

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
        binding = ActivityAllTasksBinding.inflate(layoutInflater)
        setContentView(binding.root)

        // Configuración de márgenes de la vista para bordes
        ViewCompat.setOnApplyWindowInsetsListener(binding.root) { v, insets ->
            val systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars())
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom)
            insets
        }

        // Inicialización del DAO
        taskDAO = TaskDao(this)

        // Configuración del adaptador
        adapter = TaskAdapter(taskList, { position ->
            val task = taskList[position]
            showTask(task) // Editar tarea
        }, { position ->
            val task = taskList[position]
            checkTask(task) // Marcar tarea como hecha/no hecha
        }, { position ->
            val task = taskList[position]
            deleteTask(task) // Borrar tarea
        })

        // Configuración del RecyclerView
        binding.recyclerView.adapter = adapter
        binding.recyclerView.layoutManager =
            LinearLayoutManager(this, LinearLayoutManager.VERTICAL, false)

        // Botón para agregar una nueva tarea
        binding.addTaskButton.setOnClickListener {
            popUpHelper(onSave = { newTask ->
                taskDAO.insert(newTask) // Guardar la nueva tarea
                updateTaskList() // Actualizar la lista y adaptador
            })
        }
    }

    override fun onResume() {
        super.onResume()
        updateTaskList()
    }

    // Actualizar lista y adaptador
    private fun updateTaskList() {
        taskList = taskDAO.findAll().toMutableList()
        adapter.updateItems(taskList)
    }

    // Función para marcar una tarea como hecha/no hecha
    private fun checkTask(task: Task) {
        task.done = !task.done
        taskDAO.update(task)
        updateTaskList()
    }

    // Función para borrar la tarea
    private fun deleteTask(task: Task) {
        taskDAO.delete(task)
        taskList.remove(task)
        updateTaskList()
        //caso de que queremos preguntar si queremos eliminar la tarea!
        /*
        AlertDialog.Builder(this)
            .setTitle("Borrar tarea")
            .setMessage("¿Estás seguro de que quieres borrar esta tarea?")
            .setPositiveButton(android.R.string.ok) { _, _ ->

            }
            .setNegativeButton(android.R.string.cancel, null)
            .setIcon(R.drawable.ic_delete)
            .show()*/
    }

    // Mostrar un diálogo para editar una tarea existente
    private fun showTask(task: Task) {
        popUpHelper(task = task, onSave = { updatedTask ->
            taskDAO.update(updatedTask) // Actualizar la tarea en la base de datos
            updateTaskList() // Actualizar la lista y adaptador
        })
    }

    // Función genérica para mostrar un diálogo (reutilizable para agregar o editar tareas)
    private fun popUpHelper(
        task: Task? = null, // Tarea opcional (nula si es una nueva tarea)
        onSave: (Task) -> Unit // Callback para guardar la tarea
    ) {
        // Inflar el layout del diálogo
        val alertDialogBinding = ActivityTask2Binding.inflate(layoutInflater)

        // Crear el diálogo
        val alertDialog = AlertDialog.Builder(this)
            .setView(alertDialogBinding.root)
            .create()

        // Precargar datos si la tarea no es nula (modo edición)
        task?.let {
            alertDialogBinding.nameTextField.editText?.setText(it.name)
        }

        // Configurar botón "Guardar"
        alertDialogBinding.saveButton.setOnClickListener {
            val taskName = alertDialogBinding.nameTextField.editText?.text.toString()

            // Validaciones
            if (taskName.isEmpty()) {
                alertDialogBinding.nameTextField.error = "Escribe algo"
                return@setOnClickListener
            }
            if (taskName.length > 50) {
                alertDialogBinding.nameTextField.error = "Te pasaste"
                return@setOnClickListener
            }

            // Crear o actualizar la tarea
            val updatedTask = task?.apply { name = taskName } ?: Task(-1, taskName)

            // Ejecutar la acción de guardado
            onSave(updatedTask)

            // Cerrar el diálogo
            alertDialog.dismiss()
        }

        // Mostrar el diálogo
        alertDialog.show()
    }
}
