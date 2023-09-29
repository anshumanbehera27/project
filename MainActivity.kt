package com.example.project2_todolistapp

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import androidx.room.Room
import com.example.project2_todolistapp.databinding.ActivityMainBinding
import com.example.project2_todolistapp.databinding.BottomSheetBinding
import com.example.project2_todolistapp.db.Todo
import com.example.project2_todolistapp.db.TodoListDatabase
import com.google.android.material.bottomsheet.BottomSheetDialog
import java.util.Date
import kotlin.concurrent.thread

// TODO 4: Create a ViewHolder for the Recycler View
// TODO 5: Create an Adapter for the Recycler View
// TODO 6: Handle Click events on the ToDos
// TODO 7: Add a Floating Action Button
// TODO 8: Create a Dialog Box to create a ToDo (Bottom Sheet Optional)
// TODO 9: Build a DBHelper class with (Entities, DAOs, Database and TypeConverters)
// TODO 10: Push new ToDos in the DB
// TODO 11: Whenever the App is launched sync your data with DB

// Optional TODOs

// 1. Create a user login/signup flow
// 2. Add a side navigation bar
// 3. Add a profile section where users can set the profile (Profile Pic, Name, DOB, Bio, etc.)
// 4. Push all todos data in Firebase (if user logs in from another device)
// 5. Add search feature
// 6. Add filter by date feature
// 7. Add section in Recycler View, on the basis of Date
// 8. Add reminders on Todos that have a deadline
// 9. Add new screen to display the tasks that are done

class MainActivity : AppCompatActivity() {
    private lateinit var binding: ActivityMainBinding
    private lateinit var database: TodoListDatabase
    private lateinit var adapter: TodoListAdapter
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)

        adapter =  TodoListAdapter(mutableListOf())

        binding.rvTodoList.layoutManager =
            LinearLayoutManager(this@MainActivity, RecyclerView.VERTICAL, false)
        binding.rvTodoList.adapter = adapter

        binding.fabAddTodo.setOnClickListener {
            ShowBottomsheet()
        }
        database = Room.databaseBuilder(
            this@MainActivity,
            TodoListDatabase::class.java,
            "todoListDB"
        ).build()
        Thread {
            // create a  to-do to push this in my db



//                val todo1 = Todo1(
//                    title = "RamdomTodoTitle1",
//                    desc = "RandomTodoDesc1",
//                    date = Date(System.currentTimeMillis())
//

            // database.todoDao().insertTodo(todo1)
            val listofTodo = database.todoDao().fetchAllTodos()
            // Log.d("listofthetodos" , listofTodo.toString())
            adapter.updatedata(listofTodo)


        }

    }
    private fun ShowBottomsheet(){
        val bottomSheet = BottomSheetBinding.inflate(layoutInflater)
        val dilog  = BottomSheetDialog(this)
        dilog.setContentView(bottomSheet.root)

        bottomSheet.btn.setOnClickListener {
            //TODO  ADD to-do in DB
            if (bottomSheet.tietTitle.text.isNullOrBlank()){
                bottomSheet.tielTitle.error = "Cannot be Empty"
                return@setOnClickListener
            }
            if (bottomSheet.tietTitle.text.isNullOrBlank()){
                bottomSheet.tielDesc.error = "Cannot be Empty"
                return@setOnClickListener
            }

            val todo = Todo(
                title = bottomSheet.tietTitle.text.toString() ,
                desc =  bottomSheet.tietDesc.text.toString() ,
                date = Date(System.currentTimeMillis())
            )
            adapter.addItemInserted(todo)
            thread {
                database.todoDao().insertTodo(todo)
            }
            dilog.dismiss()
        }
        dilog.show()


    }

}
