package com.example.notestasks.ui.tasks

import android.graphics.Paint
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import com.example.notestasks.data.model.Task
import com.example.notestasks.databinding.ItemTaskBinding

class TasksAdapter(
    private val onToggle: (Task) -> Unit,
    private val onDeleteClick: (Task) -> Unit
) : ListAdapter<Task, TasksAdapter.TaskViewHolder>(DiffCallback) {

    inner class TaskViewHolder(private val binding: ItemTaskBinding) :
        RecyclerView.ViewHolder(binding.root) {

        fun bind(task: Task) {
            binding.checkboxTask.setOnCheckedChangeListener(null)
            binding.checkboxTask.isChecked = task.isCompleted
            binding.textTaskTitle.text = task.title

            if (task.isCompleted) {
                binding.textTaskTitle.paintFlags =
                    binding.textTaskTitle.paintFlags or Paint.STRIKE_THRU_TEXT_FLAG
                binding.textTaskTitle.alpha = 0.5f
            } else {
                binding.textTaskTitle.paintFlags =
                    binding.textTaskTitle.paintFlags and Paint.STRIKE_THRU_TEXT_FLAG.inv()
                binding.textTaskTitle.alpha = 1.0f
            }

            binding.checkboxTask.setOnCheckedChangeListener { _, _ -> onToggle(task) }
            binding.btnDeleteTask.setOnClickListener { onDeleteClick(task) }
        }
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): TaskViewHolder {
        val binding = ItemTaskBinding.inflate(LayoutInflater.from(parent.context), parent, false)
        return TaskViewHolder(binding)
    }

    override fun onBindViewHolder(holder: TaskViewHolder, position: Int) {
        holder.bind(getItem(position))
    }

    companion object DiffCallback : DiffUtil.ItemCallback<Task>() {
        override fun areItemsTheSame(oldItem: Task, newItem: Task) = oldItem.id == newItem.id
        override fun areContentsTheSame(oldItem: Task, newItem: Task) = oldItem == newItem
    }
}
