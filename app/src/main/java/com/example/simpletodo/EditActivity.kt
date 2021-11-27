package com.example.simpletodo

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.Button
import android.widget.EditText
import android.content.Intent

class EditActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_edit)

        val inputStr = findViewById<EditText>(R.id.editString)
        val submit = findViewById<Button>(R.id.button2)
        val task = getIntent().getStringExtra("taskname")
        inputStr.setText(task)
        //when button is pressed, activity finishes
        submit?.setOnClickListener {
            val data = Intent()
            data.putExtra("editname", inputStr.text.toString())
            //saves new string
            setResult(RESULT_OK, data)
            finish()
        }
    }
}