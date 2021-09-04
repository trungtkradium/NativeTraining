package com.example.myapplication.utils

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.core.os.bundleOf
import androidx.databinding.BindingAdapter
import androidx.navigation.findNavController
import androidx.paging.PagingDataAdapter
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import com.example.myapplication.R
import com.example.myapplication.databinding.TaskItemBinding
import com.example.myapplication.viewmodel.TaskViewModel
import com.example.myapplication.data.taskRepository.Task
import javax.inject.Inject


class TasksAdapter @Inject constructor(
    var taskViewModel: TaskViewModel
) :
    ListAdapter<Task, TasksAdapter.ViewHolder>(TaskDiffCallback()) {
    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        val item = getItem(position)
        holder.bind(item, taskViewModel)
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        return ViewHolder.from(parent)
    }

    class ViewHolder private constructor(private val binding: TaskItemBinding) :
        RecyclerView.ViewHolder(binding.root) {

        fun bind(item: Task, taskViewModel: TaskViewModel) {
            binding.task = item
            binding.titleText.paint.isStrikeThruText = item.isDone
            binding.descriptionText.paint.isStrikeThruText = item.isDone
            binding.checkBoxCompleted.setOnCheckedChangeListener { _, isChecked ->
                taskViewModel.saveTask(item.id, item.title, item.description, isChecked)
            }
            binding.root.setOnClickListener {
                binding.root.findNavController().navigate(R.id.action_AllTasksFragment_to_AddEditTaskFragment, bundleOf("taskId" to item.id))
            }
            binding.executePendingBindings()
        }

        companion object {
            fun from(parent: ViewGroup): ViewHolder {
                val layoutInflater = LayoutInflater.from(parent.context)
                val binding = TaskItemBinding.inflate(layoutInflater, parent, false)

                return ViewHolder(binding)
            }
        }
    }
}

class TaskDiffCallback : DiffUtil.ItemCallback<Task>() {
    override fun areItemsTheSame(oldItem: Task, newItem: Task): Boolean {
        return oldItem.id == newItem.id
    }

    override fun areContentsTheSame(oldItem: Task, newItem: Task): Boolean {
        return (oldItem.id == newItem.id && oldItem.title == newItem.title && oldItem.description == newItem.description && oldItem.status == newItem.status)
    }
}

@BindingAdapter("android:items")
fun setItems(listView: RecyclerView, items: List<Task?>?) {
    items?.let {
        (listView.adapter as TasksAdapter).submitList(items)
    }
}


// Adapter for Paging
//class TasksAdapter @Inject constructor(
//    private val taskViewModel: TaskViewModel
//) :
//    PagingDataAdapter<Task, TaskViewHolder>(REPO_COMPARATOR) {
//    override fun onCreateViewHolder(
//        parent: ViewGroup,
//        viewType: Int
//    ): TaskViewHolder {
//        return TaskViewHolder.from(parent, taskViewModel)
//    }
//
//    override fun onBindViewHolder(holder: TaskViewHolder, position: Int) {
//        val item = getItem(position)
//        if (item != null) holder.bind(item)
//    }
//
//    companion object {
//        private val REPO_COMPARATOR = object : DiffUtil.ItemCallback<Task>() {
//            override fun areItemsTheSame(oldItem: Task, newItem: Task): Boolean {
//                return oldItem.id == newItem.id
//            }
//
//            override fun areContentsTheSame(oldItem: Task, newItem: Task): Boolean {
//                return oldItem == newItem
//            }
//        }
//    }
//}
//
//class TaskViewHolder private constructor(private val binding: TaskItemBinding, private val taskViewModel: TaskViewModel) :
//    RecyclerView.ViewHolder(binding.root) {
//
//    fun bind(item: Task) {
//        binding.task = item
//        binding.titleText.paint.isStrikeThruText = item.isDone
//        binding.descriptionText.paint.isStrikeThruText = item.isDone
//        binding.checkBoxCompleted.setOnCheckedChangeListener { _, isChecked ->
//            taskViewModel.saveTask(item.id, item.title, item.description, isChecked)
//        }
//        binding.root.setOnClickListener {
//            binding.root.findNavController().navigate(R.id.action_AllTasksFragment_to_AddEditTaskFragment, bundleOf("taskId" to item.id))
//        }
//        binding.executePendingBindings()
//    }
//
//    companion object {
//        fun from(parent: ViewGroup, taskViewModel: TaskViewModel): TaskViewHolder {
//            val layoutInflater = LayoutInflater.from(parent.context)
//            val binding = TaskItemBinding.inflate(layoutInflater, parent, false)
//
//            return TaskViewHolder(binding, taskViewModel)
//        }
//    }
//}
