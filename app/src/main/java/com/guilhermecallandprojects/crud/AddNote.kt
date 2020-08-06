package com.guilhermecallandprojects.crud

import android.content.ContentValues
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.View
import android.widget.Toast
import kotlinx.android.synthetic.main.activity_add_notes.*

class AddNote : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_add_notes)
    }

    fun buAdd(view: View){
        var dbManager = DbManager(this)

        var values = ContentValues()
        values.put("Title", etTitle.text.toString())
        values.put("Description", etDescription.text.toString())

        val ID = dbManager.insert(values)

        if(ID>=-1){
            Toast.makeText(this, "Note is added", Toast.LENGTH_LONG).show()
        }else {
            Toast.makeText(this, "Cannot add note", Toast.LENGTH_LONG).show()
        }
        finish()
    }

}