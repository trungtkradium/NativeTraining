package com.example.myapplication.fragment

import android.os.Bundle
import android.text.Editable
import android.text.TextWatcher
import android.view.*
import android.view.View.GONE
import android.view.View.VISIBLE
import android.widget.CheckBox
import android.widget.EditText
import android.widget.Toast
import androidx.constraintlayout.widget.ConstraintLayout
import androidx.fragment.app.Fragment
import androidx.fragment.app.activityViewModels
import androidx.fragment.app.viewModels
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModelProvider
import androidx.navigation.fragment.findNavController
import com.example.myapplication.MainActivity
import com.example.myapplication.R
import com.example.myapplication.data.taskRepository.Task
import com.example.myapplication.viewmodel.TaskViewModel
import com.google.android.material.floatingactionbutton.FloatingActionButton
import dagger.hilt.android.AndroidEntryPoint
import javax.inject.Inject

@AndroidEntryPoint
class AddEditTaskFragment : Fragment() {
    @Inject
    lateinit var taskViewModel: TaskViewModel

    private lateinit var taskId: String
    private val title = MutableLiveData<String>()
    private val description = MutableLiveData<String>()
    private val status = MutableLiveData<Boolean>()

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        val root = inflater.inflate(R.layout.fragment_add_edit_task, container, false)
        this.taskId = arguments?.getString("taskId") ?: ""
        setHasOptionsMenu(true)
        return root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        val activity = activity as? MainActivity
        activity?.supportActionBar?.setDisplayHomeAsUpEnabled(true)
        if (this.taskId.isEmpty()) {
            activity?.supportActionBar?.title = getString(R.string.add_task_title)
        } else {
            activity?.supportActionBar?.title = getString(R.string.edit_task_title)
        }
        setUpView(view)
    }

    override fun onCreateOptionsMenu(menu: Menu, inflater: MenuInflater) {
        if (taskId.isNotEmpty()) inflater.inflate(R.menu.delete_action, menu)
        super.onCreateOptionsMenu(menu, inflater)
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        when (item.itemId) {
            R.id.delete_task -> {
                taskViewModel.deleteTask(taskId)
                findNavController().navigate(R.id.action_AddEditTaskFragment_to_AllTasksFragment)
            }
            else -> findNavController().navigate(R.id.action_AddEditTaskFragment_to_AllTasksFragment)
        }
        return true
    }

    private fun setTask(task: Task?) {
        title.value = task?.title
        description.value = task?.description
        status.value = task?.status
    }

    private fun setUpView(view: View) {
        val titleEditText = view.findViewById<EditText>(R.id.add_task_title_edit_text)
        val descriptionEditText = view.findViewById<EditText>(R.id.add_task_description_edit_text)
        val checkBox = view.findViewById<CheckBox>(R.id.check_box_completed_edit_task)
        val editStatus = view.findViewById<ConstraintLayout>(R.id.edit_status_container)
        editStatus.visibility = GONE

        if (this.taskId.isNotEmpty()) {
            taskViewModel.tasks?.value?.forEach {
                if (it?.id == taskId) {
                    titleEditText.setText(it.title)
                    descriptionEditText.setText(it.description)
                    setTask(it)
                    editStatus.visibility = VISIBLE
                    checkBox.isChecked = it.isDone
                    checkBox.setOnCheckedChangeListener { _, isChecked ->
                        status.value = isChecked
                    }
                }
            }
        }

        view.findViewById<FloatingActionButton>(R.id.save_task_fab).setOnClickListener {
            onFloatingButtonClick()
        }

        titleEditText.addTextChangedListener(object : TextWatcher {
            override fun beforeTextChanged(s: CharSequence, start: Int, count: Int, after: Int) {}
            override fun onTextChanged(s: CharSequence, start: Int, before: Int, count: Int) {
                title.value = titleEditText.text.toString()
            }

            override fun afterTextChanged(s: Editable) {
            }
        })

        descriptionEditText.addTextChangedListener(object : TextWatcher {
            override fun beforeTextChanged(s: CharSequence, start: Int, count: Int, after: Int) {}
            override fun onTextChanged(s: CharSequence, start: Int, before: Int, count: Int) {
                description.value = descriptionEditText.text.toString()
            }

            override fun afterTextChanged(s: Editable) {
            }
        })
    }

    private fun onFloatingButtonClick() {
        if (title.value?.isEmpty() != false || description.value?.isEmpty() != false) {
            Toast.makeText(context, "Please add title and description", Toast.LENGTH_SHORT)
                .show()
            return
        }
        val result = taskViewModel.saveTask(
            taskId,
            title.value.toString(),
            description.value.toString(),
            status.value ?: false
        )
        if (result == 0) {
            Toast.makeText(
                context,
                "Create new TODO or Update TODO failed, please try again!",
                Toast.LENGTH_SHORT
            ).show()
        } else {
            findNavController().navigate(R.id.action_AddEditTaskFragment_to_AllTasksFragment)
        }
    }
}