package com.guilhermecallandprojects.crud

import android.app.SearchManager
import android.content.Context
import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.*
import android.widget.BaseAdapter
import android.widget.SearchView
import android.widget.TextView
import android.widget.Toast
import kotlinx.android.synthetic.main.activity_main.*

class MainActivity : AppCompatActivity() {
    var listNotes = ArrayList<Note>()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        //Load from DB
        LoadQuery("%")
    }

    private fun LoadQuery(title:String){
        var dbManager = DbManager(this)
        val projections = arrayOf("ID", "Title", "Description")
        val selectionArgs = arrayOf(title)
        val cursor = dbManager.Query(projections, "Title like ?", selectionArgs, "Title")
        listNotes.clear()

        if(cursor.moveToFirst()){
            do{
                val ID = cursor.getInt(cursor.getColumnIndex("ID"))
                val Title = cursor.getString(cursor.getColumnIndex("Title"))
                val Description = cursor.getString(cursor.getColumnIndex("Description"))

                listNotes.add(Note(ID, Title, Description))
            }while(cursor.moveToNext())
        }

        lvNotes.adapter = NotesAdapter(applicationContext, listNotes)

    }

    class  NotesAdapter(var context: Context, var notesList: ArrayList<Note>): BaseAdapter(){

        private class ViewHolder(view: View){
            var tvName: TextView = view.findViewById(R.id.tvName)
            var tvDescription:TextView = view.findViewById(R.id.tvDescription)
        }

        override fun getView(position: Int, convertView: View?, parent: ViewGroup?): View? {
            var view: View?
            var viewHolder: ViewHolder
            if(convertView == null){
                var layout = LayoutInflater.from(context)
                view = layout.inflate(R.layout.ticket, convertView, false)
                viewHolder = ViewHolder(view)
                view.tag = viewHolder
            } else {
                view = convertView
                viewHolder = view.tag as ViewHolder
            }
            var note: Note = getItem(position) as Note
            viewHolder.tvName.text = note.title
            viewHolder.tvDescription.text = note.description
            return view
        }

        override fun getItem(position: Int): Any {
            return notesList.get(position)
        }

        override fun getItemId(position: Int): Long {
            return position.toLong()
        }

        override fun getCount(): Int {
            return notesList.count()
        }
    }

    override fun onCreateOptionsMenu(menu: Menu): Boolean {
        menuInflater.inflate(R.menu.main_menu, menu)

        val sv: SearchView = menu.findItem(R.id.iSearch).actionView as SearchView
        val sm = getSystemService(Context.SEARCH_SERVICE) as SearchManager
        sv.setSearchableInfo(sm.getSearchableInfo(componentName))
        sv.setOnQueryTextListener(object: SearchView.OnQueryTextListener{
            override fun onQueryTextSubmit(query: String?): Boolean {
                Toast.makeText(applicationContext, query, Toast.LENGTH_LONG).show()
                LoadQuery("%" + query + "%")
                return false
            }

            override fun onQueryTextChange(newText: String?): Boolean {
                return false
            }
        })
        return super.onCreateOptionsMenu(menu)
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        when(item.itemId){
            R.id.iAddNote -> {
                var intent = Intent(this,AddNote::class.java)
                startActivity(intent)
            }
        }
        return super.onOptionsItemSelected(item)
    }



}