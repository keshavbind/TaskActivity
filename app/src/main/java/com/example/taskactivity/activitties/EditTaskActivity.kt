package com.example.taskactivity.activitties

import android.os.Bundle
import android.widget.ArrayAdapter
import android.widget.Button
import android.widget.CheckBox
import android.widget.ImageView
import android.widget.Spinner
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity
import androidx.core.content.ContextCompat
import androidx.core.widget.doOnTextChanged
import androidx.lifecycle.ViewModelProvider
import com.example.taskactivity.R
import com.example.taskactivity.database.Task
import com.example.taskactivity.database.TaskDatabase
import com.example.taskactivity.repository.TaskRepository
import com.example.taskactivity.viewmodel.TaskViewModel
import com.example.taskactivity.viewmodel.TaskViewModelFactory
import com.google.android.material.textfield.TextInputEditText
import com.google.android.material.textfield.TextInputLayout

class EditTaskActivity : AppCompatActivity() {
    private lateinit var viewModel: TaskViewModel

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContentView(R.layout.activity_edit_task)

        // Set status bar styling
        window.statusBarColor = ContextCompat.getColor(this, R.color.status_bar)

        val tilTitle = findViewById<TextInputLayout>(R.id.til_title)
        val tilDesc = findViewById<TextInputLayout>(R.id.til_desc)
        val utitle = findViewById<TextInputEditText>(R.id.utitle)
        val udesc = findViewById<TextInputEditText>(R.id.udesc)
        val uspinner = findViewById<Spinner>(R.id.uspinner)
        val uiscomplete = findViewById<CheckBox>(R.id.ucheckbox)
        val btnupdate = findViewById<Button>(R.id.btnupdate)
        val btnBack = findViewById<ImageView>(R.id.btn_back)

        // Fetch intent values
        val oldtitle = intent.getStringExtra("title")
        val olddesc = intent.getStringExtra("desc")
        val oldpriority = intent.getStringExtra("priority")
        val oldid = intent.getIntExtra("id", 0)
        val oldcomplete = intent.getBooleanExtra("completed", false)

        // Back button navigation
        btnBack.setOnClickListener {
            onBackPressedDispatcher.onBackPressed()
        }

        // Set up priority spinner list
        val prioritylist = arrayOf(
            getString(R.string.priority_high),
            getString(R.string.priority_medium),
            getString(R.string.priority_low)
        )
        val adapter = ArrayAdapter(this, R.layout.spinner_item, prioritylist)
        adapter.setDropDownViewResource(R.layout.spinner_dropdown_item)
        uspinner.adapter = adapter

        // Set spinner selection
        val position = prioritylist.indexOf(oldpriority)
        if (position >= 0) {
            uspinner.setSelection(position)
        }

        // Prepopulate text fields
        utitle.setText(oldtitle)
        udesc.setText(olddesc)
        uiscomplete.isChecked = oldcomplete

        // Clear error styling dynamically on text changes
        utitle.doOnTextChanged { _, _, _, _ -> tilTitle.error = null }
        udesc.doOnTextChanged { _, _, _, _ -> tilDesc.error = null }

        // Setup ViewModel
        val db = TaskDatabase.getDatabse(this)
        val repository = TaskRepository(db.taskdao())
        val factory = TaskViewModelFactory(repository)
        viewModel = ViewModelProvider(this, factory)[TaskViewModel::class.java]

        btnupdate.setOnClickListener {
            val title = utitle.text.toString().trim()
            val desc = udesc.text.toString().trim()
            val selectedpriority = uspinner.selectedItem.toString()

            var isValid = true
            if (title.isEmpty()) {
                tilTitle.error = getString(R.string.error_empty_title)
                isValid = false
            }
            if (desc.isEmpty()) {
                tilDesc.error = getString(R.string.error_empty_desc)
                isValid = false
            }

            if (isValid) {
                val updatetask = Task(
                    id = oldid,
                    title = title,
                    description = desc,
                    priority = selectedpriority,
                    iscompleted = uiscomplete.isChecked
                )

                viewModel.update(updatetask)
                finish()
                overridePendingTransition(R.anim.slide_in_left, R.anim.slide_out_right)
            }
        }
    }

    @Deprecated("Deprecated in Java")
    override fun onBackPressed() {
        super.onBackPressed()
        overridePendingTransition(R.anim.slide_in_left, R.anim.slide_out_right)
    }
}