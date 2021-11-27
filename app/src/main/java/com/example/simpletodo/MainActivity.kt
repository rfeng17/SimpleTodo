package com.example.simpletodo

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.View
import android.widget.Button
import android.widget.EditText
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import java.io.File
import java.io.IOException
import java.nio.charset.Charset

class MainActivity : AppCompatActivity() {

    var listOfTasks = mutableListOf<String>()
    lateinit var adapter: TaskItemAdapter
    //initialize vars
    var taskItem: String = ""
    var taskPos: Int = 0

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        val inputTextField = findViewById<EditText>(R.id.addTaskField)

        val recyclerView = findViewById<RecyclerView>(R.id.recyclerView)

        val onLongClickListener = object : TaskItemAdapter.OnLongClickListener {
            override fun onItemLongClicked(position: Int) {
                //Remove item from list
                listOfTasks.removeAt(position)
                //Notify the change to the adapter
                adapter.notifyDataSetChanged()

                //save changes
                saveItems()
            }
        }

        val onClickListener = object : TaskItemAdapter.OnClickListener {
            override fun onItemClicked(position: Int, view: View) {
                //store element in var
                taskItem = listOfTasks.elementAt(position)
                taskPos = position
                //call activity
                onClick(view)
            }
        }

        loadItems()

        //Create adapter passing in the sample user data
        adapter = TaskItemAdapter(listOfTasks, onLongClickListener, onClickListener)
        // Attach the adapter to the recyclerview to populate items
        recyclerView.adapter = adapter
        // Set layout manager to position the items
        recyclerView.layoutManager = LinearLayoutManager(this)

        //Set up button and input field for adding to list
        //reference button
        findViewById<Button>(R.id.button).setOnClickListener() {
            //grab text from input
            val userInputtedTask = inputTextField.text.toString()
            //add string to listOfTasks
            listOfTasks.add(userInputtedTask)
            //Tell adapter that data has been updated
            adapter.notifyItemInserted(listOfTasks.size - 1)
            //Reset field
            inputTextField.setText("")

            //Save to file
            saveItems()
        }

    }

    //Save inputted data by reading and writing from a file
    //Get the file
    fun getDataFile() : File {
        //Every line represents a specific task in listOfTasks
        return File(filesDir, "data.txt")
    }
    //Load items by reading every line in file
    fun loadItems() {
        try {
            listOfTasks =
                org.apache.commons.io.FileUtils.readLines(getDataFile(), Charset.defaultCharset())
        } catch (ioException: IOException) {
            ioException.printStackTrace()
        }
    }
    //Save items by writing them into our data file
    fun saveItems() {
        try {//catch err if file can't load
            org.apache.commons.io.FileUtils.writeLines(getDataFile(), listOfTasks)
        } catch (ioException: IOException) {
            ioException.printStackTrace()
        }
    }

    fun onClick(view: View) {
        val i = Intent(this, EditActivity::class.java)
        i.putExtra("taskname", taskItem)
        startActivityForResult(i, 20)
    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)
        // REQUEST_CODE is defined above
        if (resultCode == RESULT_OK && requestCode == 20) {
            val newStr = data?.getExtras()?.getString("editname")

            if (newStr != null) {
                listOfTasks.set(taskPos, newStr)
            }
        }
        //update list
        adapter.notifyDataSetChanged()
        saveItems()
    }
}
