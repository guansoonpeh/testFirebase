package com.example.testfirebase.adapter

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.example.testfirebase.R
import com.example.testfirebase.models.Student

class StudentAdapter (
    private val studentList:List<Student>,
    private val listener: OnItemClickListener
    ) : RecyclerView.Adapter<StudentAdapter.myViewHolder>() {

        inner class myViewHolder(studentView: View) : RecyclerView.ViewHolder(studentView), View.OnClickListener {
            val regNo: TextView = studentView.findViewById(R.id.tvRegNo)
            val name: TextView = studentView.findViewById(R.id.tvName)
            val programme :TextView = studentView.findViewById(R.id.tvProgramme)

            init {
                studentView.setOnClickListener(this)
            }

            override fun onClick(v: View?) {
                val position:Int = bindingAdapterPosition
                if(position != RecyclerView.NO_POSITION) {
                    listener.onItemClick(position)
                }
            }
        }

        interface OnItemClickListener{
            fun onItemClick(position: Int)
        }

        override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): myViewHolder {
            val personView =  LayoutInflater.from(parent.context)
                .inflate(R.layout.student_view, parent, false)

            return myViewHolder(personView)
        }

        override fun onBindViewHolder(holder: myViewHolder, position: Int) {
            val currentRec  = studentList[position]

            holder.regNo.text = currentRec.regNo
            holder.name.text = currentRec.name
            holder.programme.text  = currentRec.programme

        }

        override fun getItemCount(): Int {
            return studentList.size
        }


    }