package com.example.taskactivity.activitties

import android.content.Intent
import android.os.Bundle
import android.view.View
import android.widget.TextView
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AlertDialog
import androidx.appcompat.app.AppCompatActivity
import androidx.core.content.ContextCompat
import androidx.lifecycle.ViewModelProvider
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.example.taskactivity.R
import com.example.taskactivity.adapter.MyAdapter
import com.example.taskactivity.database.Task
import com.example.taskactivity.database.TaskDatabase
import com.example.taskactivity.repository.TaskRepository
import com.example.taskactivity.viewmodel.TaskViewModel
import com.example.taskactivity.viewmodel.TaskViewModelFactory
import com.google.android.material.dialog.MaterialAlertDialogBuilder
import com.google.android.material.floatingactionbutton.FloatingActionButton

class MainActivity : AppCompatActivity() {
    private lateinit var recyclerview: RecyclerView
    private lateinit var myAdapter: MyAdapter
    private lateinit var taskList: ArrayList<Task>
    private lateinit var db: TaskDatabase
    private lateinit var viewModel: TaskViewModel
    private lateinit var tvTaskCount: TextView
    private lateinit var emptyStateContainer: View

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContentView(R.layout.activity_main)

        // Set status bar styling for a polished look
        window.statusBarColor = ContextCompat.getColor(this, R.color.status_bar)

        recyclerview = findViewById(R.id.recyclerview)
        tvTaskCount = findViewById(R.id.tv_task_count)
        emptyStateContainer = findViewById(R.id.empty_state_container)
        val btnadd = findViewById<FloatingActionButton>(R.id.btnadd)

        taskList = ArrayList()
        myAdapter = MyAdapter(this, taskList)
        recyclerview.layoutManager = LinearLayoutManager(this)
        recyclerview.adapter = myAdapter

        db = TaskDatabase.getDatabse(this)
        val repository = TaskRepository(db.taskdao())
        val factory = TaskViewModelFactory(repository)
        viewModel = ViewModelProvider(this, factory)[TaskViewModel::class.java]

        viewModel.allTasks.observe(this) { tasks ->
            taskList.clear()
            taskList.addAll(tasks)
            myAdapter.notifyDataSetChanged()
            updateUIState(tasks.size)
        }

        btnadd.setOnClickListener {
            val intent = Intent(this, AddTaskActivity::class.java)
            startActivity(intent)
            overridePendingTransition(R.anim.slide_in_right, R.anim.slide_out_left)
        }

        myAdapter.onItemLongClick = { task ->
            MaterialAlertDialogBuilder(this)
                .setTitle(getString(R.string.delete_title))
                .setMessage(getString(R.string.delete_message))
                .setPositiveButton(getString(R.string.yes)) { _, _ ->
                    viewModel.delete(task)
                }
                .setNegativeButton(getString(R.string.no), null)
                .show()
        }

        myAdapter.onItemClick = { task ->
            val intent = Intent(this, EditTaskActivity::class.java)
            intent.putExtra("id", task.id)
            intent.putExtra("title", task.title)
            intent.putExtra("desc", task.description)
            intent.putExtra("priority", task.priority)
            intent.putExtra("completed", task.iscompleted)
            startActivity(intent)
            overridePendingTransition(R.anim.slide_in_right, R.anim.slide_out_left)
        }

        myAdapter.onCheckBoxClick = { task ->
            viewModel.update(task)
        }
    }

    private fun updateUIState(count: Int) {
        // Toggle empty state
        if (count == 0) {
            emptyStateContainer.visibility = View.VISIBLE
            recyclerview.visibility = View.GONE
            tvTaskCount.text = getString(R.string.main_subtitle_empty)
        } else {
            emptyStateContainer.visibility = View.GONE
            recyclerview.visibility = View.VISIBLE
            tvTaskCount.text = if (count == 1) {
                getString(R.string.main_subtitle_one)
            } else {
                String.format(getString(R.string.main_subtitle_format), count)
            }
        }
    }
}