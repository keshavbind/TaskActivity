package com.example.taskactivity.adapter

import android.app.Activity
import android.content.res.ColorStateList
import android.graphics.Paint
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.CheckBox
import android.widget.TextView
import androidx.core.content.ContextCompat
import androidx.recyclerview.widget.RecyclerView
import com.example.taskactivity.R
import com.example.taskactivity.database.Task

class MyAdapter(var context: Activity,
                var taskarraylist: ArrayList<Task>,
                var onItemLongClick: ((Task) -> Unit)? = null,
                var onItemClick: ((Task) -> Unit)? = null,
                var onCheckBoxClick: ((Task) -> Unit)? = null) :
    RecyclerView.Adapter<MyAdapter.MyViewHolder>() {

    class MyViewHolder(itemview: View) : RecyclerView.ViewHolder(itemview) {
        val priorityStrip: View = itemview.findViewById(R.id.priority_strip)
        val iscomplete: CheckBox = itemview.findViewById(R.id.checkbox)
        val title: TextView = itemview.findViewById(R.id.tvtitle)
        val description: TextView = itemview.findViewById(R.id.tvdesc)
        val priority: TextView = itemview.findViewById(R.id.tvpriority)
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): MyViewHolder {
        val itemview = LayoutInflater.from(context).inflate(R.layout.eachitem, parent, false)
        return MyViewHolder(itemview)
    }

    override fun getItemCount(): Int {
        return taskarraylist.size
    }

    override fun onBindViewHolder(holder: MyViewHolder, position: Int) {
        val currentitem = taskarraylist[position]
        holder.title.text = currentitem.title
        holder.description.text = currentitem.description
        holder.priority.text = currentitem.priority.uppercase()
        holder.iscomplete.isChecked = currentitem.iscompleted

        // Set visual treatment for completed tasks (faded text + strikethrough)
        if (currentitem.iscompleted) {
            holder.title.paintFlags = holder.title.paintFlags or Paint.STRIKE_THRU_TEXT_FLAG
            holder.title.setTextColor(ContextCompat.getColor(context, R.color.text_completed))
            holder.description.setTextColor(ContextCompat.getColor(context, R.color.text_completed))
            holder.itemView.alpha = 0.6f
        } else {
            holder.title.paintFlags = holder.title.paintFlags and Paint.STRIKE_THRU_TEXT_FLAG.inv()
            holder.title.setTextColor(ContextCompat.getColor(context, R.color.text_primary))
            holder.description.setTextColor(ContextCompat.getColor(context, R.color.text_secondary))
            holder.itemView.alpha = 1.0f
        }

        holder.itemView.setOnLongClickListener {
            onItemLongClick?.invoke(currentitem)
            true
        }

        holder.itemView.setOnClickListener {
            onItemClick?.invoke(currentitem)
        }

        holder.iscomplete.setOnCheckedChangeListener(null)
        holder.iscomplete.isChecked = currentitem.iscompleted
        holder.iscomplete.setOnCheckedChangeListener { _, isChecked ->
            currentitem.iscompleted = isChecked
            onCheckBoxClick?.invoke(currentitem)
        }

        // Apply theme color codes for priority labels and strips
        val colorRes = when (currentitem.priority) {
            "High" -> R.color.priority_high
            "Medium" -> R.color.priority_medium
            "Low" -> R.color.priority_low
            else -> R.color.priority_low
        }

        val resolvedColor = ContextCompat.getColor(context, colorRes)
        holder.priority.background.setTint(resolvedColor)
        holder.priorityStrip.background.setTint(resolvedColor)
        
        // Also tint checkbox based on priority for extra flair
        holder.iscomplete.buttonTintList = ColorStateList.valueOf(
            ContextCompat.getColor(context, R.color.primary)
        )
    }
}