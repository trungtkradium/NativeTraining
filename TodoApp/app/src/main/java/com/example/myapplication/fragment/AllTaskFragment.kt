package com.example.myapplication.fragment

import android.os.Bundle
import android.view.*
import androidx.core.os.bundleOf
import androidx.fragment.app.Fragment
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.Observer
import androidx.navigation.fragment.findNavController
import androidx.recyclerview.widget.RecyclerView
import com.example.myapplication.MainActivity
import com.example.myapplication.R
import com.example.myapplication.data.taskRepository.Task
import com.example.myapplication.databinding.FragmentAllTaskBinding
import com.example.myapplication.utils.TasksAdapter
import com.example.myapplication.utils.TasksFilterType
import com.example.myapplication.utils.setItems
import com.example.myapplication.viewmodel.TaskViewModel
import com.google.android.material.floatingactionbutton.FloatingActionButton
import dagger.hilt.android.AndroidEntryPoint
import javax.inject.Inject


@AndroidEntryPoint
class AllTaskFragment : Fragment() {
    @Inject
    lateinit var taskViewModel: TaskViewModel

    private lateinit var taskAdapter: TasksAdapter
    private var tasks = MutableLiveData<List<Task?>?>()
    private var filterType = MutableLiveData(TasksFilterType.ALL_TASKS)
    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        val root = inflater.inflate(R.layout.fragment_all_task, container, false)
        setHasOptionsMenu(true)
        return root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        val activity = activity as? MainActivity
        activity?.supportActionBar?.setDisplayHomeAsUpEnabled(false)
        val recyclerView = view.findViewById<RecyclerView>(R.id.tasks_list)
        taskAdapter = TasksAdapter(taskViewModel)
        recyclerView.adapter = taskAdapter

//        Only for Paging
//        lifecycleScope.launch {
//            taskViewModel.flow(TasksFilterType.ALL_TASKS).collectLatest { pagingData ->
//                taskAdapter.submitData(pagingData)
//            }
//        }

        filterType.observe(viewLifecycleOwner, Observer {
            activity?.supportActionBar?.title = getTitleByFilter(it)
            val tempTasks = getTasksByFilter(filterType.value)
            setItems(recyclerView, tempTasks)
        })

        taskViewModel.tasks?.observe(viewLifecycleOwner, Observer {
            tasks.value = it
            tasks.value = getTasksByFilter(filterType.value)
            setItems(
                recyclerView,
                tasks.value
            )
        })

        view.findViewById<FloatingActionButton>(R.id.add_task_fab).setOnClickListener {
            findNavController().navigate(
                R.id.action_AllTasksFragment_to_AddEditTaskFragment, bundleOf(
                    "taskId" to null
                )
            )
        }
    }

    override fun onCreateOptionsMenu(menu: Menu, inflater: MenuInflater) {
        inflater.inflate(R.menu.filter_action, menu)
        super.onCreateOptionsMenu(menu, inflater)
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        when (item.itemId) {
            R.id.filter_all_task -> filterType.value = TasksFilterType.ALL_TASKS

            R.id.filter_incomplete_task -> filterType.value = TasksFilterType.INCOMPLETE_TASKS

            R.id.filter_complete_task -> filterType.value = TasksFilterType.COMPLETED_TASKS
        }
        return true
    }

    private fun getTasksByFilter(filterType: TasksFilterType? = TasksFilterType.ALL_TASKS): List<Task?>? {
        return when (filterType) {
            TasksFilterType.ALL_TASKS -> {
                tasks.value
            }
            TasksFilterType.COMPLETED_TASKS -> {
                tasks.value?.filter { it?.isDone == true }
            }
            else -> {
                tasks.value?.filter { it?.isDone == false }
            }
        }
    }

    private fun getTitleByFilter(filterType: TasksFilterType? = TasksFilterType.ALL_TASKS): String {
        return when (filterType) {
            TasksFilterType.ALL_TASKS -> getString(R.string.all_task_title)
            TasksFilterType.COMPLETED_TASKS -> getString(R.string.completed_task_title)
            else -> getString(R.string.incomplete_task_title)
        }
    }
}
