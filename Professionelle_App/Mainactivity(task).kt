data class Task(
    val title: String = ""
)

package com.example.todoapp

import android.os.Bundle
import android.widget.*
import androidx.appcompat.app.AppCompatActivity
import androidx.recyclerview.widget.LinearLayoutManager
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.firestore.FirebaseFirestore

class MainActivity : AppCompatActivity() {

    private val db = FirebaseFirestore.getInstance()
    private val auth = FirebaseAuth.getInstance()
    private val tasks = ArrayList<Task>()
    private lateinit var adapter: TaskAdapter

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        // Wenn nicht eingeloggt → Login Screen
        if (auth.currentUser == null) {
            startActivity(android.content.Intent(this, LoginActivity::class.java))
            finish()
            return
        }

        setContentView(R.layout.activity_main)

        val editText = findViewById<EditText>(R.id.editTextTask)
        val button = findViewById<Button>(R.id.buttonAdd)
        val recyclerView = findViewById<androidx.recyclerview.widget.RecyclerView>(R.id.recyclerView)

        adapter = TaskAdapter(tasks) { task ->
            deleteTask(task)
        }

        recyclerView.layoutManager = LinearLayoutManager(this)
        recyclerView.adapter = adapter

        loadTasks()

        button.setOnClickListener {
            val text = editText.text.toString()
            if (text.isNotEmpty()) {
                addTask(text)
                editText.text.clear()
            }
        }
    }

    private fun addTask(title: String) {
        val uid = auth.currentUser!!.uid
        val task = hashMapOf("title" to title)

        db.collection("users")
            .document(uid)
            .collection("tasks")
            .add(task)
    }

    private fun loadTasks() {
        val uid = auth.currentUser!!.uid

        db.collection("users")
            .document(uid)
            .collection("tasks")
            .addSnapshotListener { value, _ ->
                tasks.clear()
                value?.forEach {
                    tasks.add(Task(it.getString("title")!!))
                }
                adapter.notifyDataSetChanged()
            }
    }

    private fun deleteTask(task: Task) {
        val uid = auth.currentUser!!.uid

        db.collection("users")
            .document(uid)
            .collection("tasks")
            .whereEqualTo("title", task.title)
            .get()
            .addOnSuccessListener {
                for (doc in it) {
                    doc.reference.delete()
                }
            }
    }
}