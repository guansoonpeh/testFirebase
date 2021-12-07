package com.example.testfirebase

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.Button
import android.widget.TextView
import android.widget.Toast
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.example.testfirebase.adapter.StudentAdapter
import com.example.testfirebase.models.Student
import com.google.firebase.database.DataSnapshot
import com.google.firebase.database.DatabaseError
import com.google.firebase.database.FirebaseDatabase
import com.google.firebase.database.ValueEventListener


class MainActivity : AppCompatActivity(),  StudentAdapter.OnItemClickListener {
    private lateinit var studentList: MutableList<Student>

    val database = FirebaseDatabase.getInstance()
    private lateinit var tvResult: TextView
    private lateinit var student: Student

    override fun onItemClick(position: Int) {
        Toast.makeText(this, position.toString(), Toast.LENGTH_LONG).show()
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        studentList = mutableListOf()

        val btnAdd = findViewById<Button>(R.id.btnAdd)
        val btnSearch = findViewById<Button>(R.id.btnSearch)
        tvResult = findViewById(R.id.textView)

        var adapter = StudentAdapter(studentList, this@MainActivity)
        val myRV: RecyclerView = findViewById(R.id.rvStudent)

        myRV.adapter = adapter
        myRV.layoutManager = LinearLayoutManager(application)
        myRV.setHasFixedSize(true)



        btnAdd.setOnClickListener() {
            val tblStudent = database.getReference("Student")

            val regNo = findViewById<TextView>(R.id.tfRegNo).text.toString()
            val name = findViewById<TextView>(R.id.tfName).text.toString()
            val programme = findViewById<TextView>(R.id.tfProgramme).text.toString()

            student = Student(regNo, name, programme)

            tblStudent.child(student.regNo).setValue(student).addOnSuccessListener {
                Toast.makeText(this, "OK", Toast.LENGTH_SHORT).show()
            }.addOnFailureListener() {
                Toast.makeText(this, it.message.toString(), Toast.LENGTH_SHORT).show()
            }
        }

        btnSearch.setOnClickListener() {

         readData(object : ReadCallBack {
             override fun onCallBack(list: MutableList<Student>) {
                 adapter = StudentAdapter(list, this@MainActivity)
                 myRV.adapter = adapter
             }
         }, "RIT")
        }
    }

    fun readData(callback: ReadCallBack, key:String){
        val ref = database.getReference().child("Student")

          val refListener = object :  ValueEventListener {
            override fun onDataChange(p0: DataSnapshot) {
                if (p0.exists()) {
                    val studentList = mutableListOf<Student>()

                    for (c in p0.children) {
                        if (c.child("programme").value.toString() == key) {

                            val regNo =  c.child("regNo").value.toString()
                            val name =  c.child("name").value.toString()
                            val programme =  c.child("programme").value.toString()

                            student = Student(regNo, name, programme)
                            studentList.add(student)

                        }
                        callback.onCallBack(studentList)
                    }
                }
            }

            override fun onCancelled(p0: DatabaseError) {

            }
        }

        ref.addValueEventListener(refListener)

    }

    interface ReadCallBack{
        fun onCallBack(list: MutableList<Student> )
    }
}