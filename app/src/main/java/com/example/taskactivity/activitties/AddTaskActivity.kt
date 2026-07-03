package com.example.taskactivity.activitties

import android.os.Bundle
import android.widget.ArrayAdapter
import android.widget.Button
import android.widget.ImageView
import android.widget.Spinner
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity
import androidx.core.content.ContextCompat
import androidx.core.widget.doOnTextChanged
import com.example.taskactivity.R
import com.example.taskactivity.database.Task
import com.example.taskactivity.database.TaskDatabase
import com.google.android.material.textfield.TextInputEditText
import com.google.android.material.textfield.TextInputLayout
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch

class AddTaskActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContentView(R.layout.activity_add_task)

        // Set status bar styling
        window.statusBarColor = ContextCompat.getColor(this, R.color.status_bar)

        val tilTitle = findViewById<TextInputLayout>(R.id.til_title)
        val tilDesc = findViewById<TextInputLayout>(R.id.til_desc)
        val ettitle = findViewById<TextInputEditText>(R.id.ettitle)
        val etdesc = findViewById<TextInputEditText>(R.id.etdesc)
        val priority = findViewById<Spinner>(R.id.spinner)
        val btnsave = findViewById<Button>(R.id.btnsave)
        val btnBack = findViewById<ImageView>(R.id.btn_back)

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
        priority.adapter = adapter

        // Clear error styling dynamically on text changes
        ettitle.doOnTextChanged { _, _, _, _ -> tilTitle.error = null }
        etdesc.doOnTextChanged { _, _, _, _ -> tilDesc.error = null }

        btnsave.setOnClickListener {
            val title = ettitle.text.toString().trim()
            val desc = etdesc.text.toString().trim()
            val selectedpriority = priority.selectedItem.toString()

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
                val db = TaskDatabase.getDatabse(this)
                val task = Task(title = title, description = desc, priority = selectedpriority)

                CoroutineScope(Dispatchers.IO).launch {
                    db.taskdao().insert(task)
                    runOnUiThread {
                        finish()
                        overridePendingTransition(R.anim.slide_in_left, R.anim.slide_out_right)
                    }
                }
            }
        }
    }

    @Deprecated("Deprecated in Java")
    override fun onBackPressed() {
        super.onBackPressed()
        overridePendingTransition(R.anim.slide_in_left, R.anim.slide_out_right)
    }
}