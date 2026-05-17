package com.example.todoapp

import android.os.Bundle
import android.widget.*
import androidx.activity.viewModels
import androidx.appcompat.app.AppCompatActivity
import androidx.recyclerview.widget.LinearLayoutManager

class MainActivity : AppCompatActivity() {

    private val viewModel: TaskViewModel by viewModels()
    private lateinit var adapter: TaskAdapter

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        val editText = findViewById<EditText>(R.id.editTextTask)
        val button = findViewById<Button>(R.id.buttonAdd)
        val recyclerView = findViewById<androidx.recyclerview.widget.RecyclerView>(R.id.recyclerView)

        adapter = TaskAdapter(emptyList()) {
            viewModel.deleteTask(it)
        }

        recyclerView.layoutManager = LinearLayoutManager(this)
        recyclerView.adapter = adapter

        viewModel.tasks.observe(this) {
            adapter.updateData(it)
        }

        button.setOnClickListener {
            val text = editText.text.toString()
            if (text.isNotEmpty()) {
                viewModel.addTask(text)
                editText.text.clear()
            }
        }
    }
}