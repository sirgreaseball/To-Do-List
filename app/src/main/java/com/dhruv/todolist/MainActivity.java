package com.dhruv.todolist;

import android.app.AlertDialog;
import android.database.Cursor;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.EditText;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.google.android.material.floatingactionbutton.FloatingActionButton;

import java.util.ArrayList;

public class MainActivity extends AppCompatActivity {

    RecyclerView tasksview;
    FloatingActionButton addtaskbtn;

    ArrayList<Task> taskList;
    TaskAdapter adapter;

    DatabaseHelper databaseHelper;


    @Override
    protected void onCreate(Bundle savedInstanceState) {

        super.onCreate(savedInstanceState);

        setContentView(R.layout.activity_main);


        // Find views
        tasksview = findViewById(R.id.tasksview);
        addtaskbtn = findViewById(R.id.addtaskbtn);


        // Database
        databaseHelper = new DatabaseHelper(this);


        // Task list
        taskList = new ArrayList<>();


        // Adapter
        adapter = new TaskAdapter(
                this,
                taskList,
                databaseHelper
        );


        // RecyclerView
        tasksview.setLayoutManager(
                new LinearLayoutManager(this)
        );

        tasksview.setAdapter(adapter);


        // Load existing tasks
        loadTasks();


        // Add task button
        addtaskbtn.setOnClickListener(
                v -> showDialog()
        );
    }


    // LOAD TASKS FROM DATABASE
    private void loadTasks() {

        Cursor cursor = databaseHelper.getAllTasks();

        taskList.clear();


        while (cursor.moveToNext()) {

            int id = cursor.getInt(
                    cursor.getColumnIndexOrThrow(
                            DatabaseHelper.COLUMN_ID
                    )
            );


            String title = cursor.getString(
                    cursor.getColumnIndexOrThrow(
                            DatabaseHelper.COLUMN_TASK
                    )
            );


            int status = cursor.getInt(
                    cursor.getColumnIndexOrThrow(
                            DatabaseHelper.COLUMN_STATUS
                    )
            );


            boolean completed = status == 1;


            Task task = new Task(
                    id,
                    title,
                    completed
            );


            taskList.add(task);
        }


        cursor.close();


        adapter.notifyDataSetChanged();
    }


    // SHOW ADD TASK DIALOG
    private void showDialog() {

        View view = LayoutInflater
                .from(this)
                .inflate(
                        R.layout.dialog_add_task,
                        null
                );


        EditText editText = view.findViewById(
                R.id.editext
        );


        new AlertDialog.Builder(this)

                .setTitle("Add Task")

                .setView(view)

                .setPositiveButton(
                        "Add",
                        (dialog, which) -> {

                            String taskName =
                                    editText
                                            .getText()
                                            .toString()
                                            .trim();


                            if (!taskName.isEmpty()) {

                                // Save to database
                                long id =
                                        databaseHelper.addTask(
                                                taskName
                                        );


                                // Create Task object
                                Task task = new Task(
                                        (int) id,
                                        taskName,
                                        false
                                );


                                // Add to list
                                taskList.add(task);


                                // Update RecyclerView
                                adapter.notifyItemInserted(
                                        taskList.size() - 1
                                );
                            }
                        }
                )

                .setNegativeButton(
                        "Cancel",
                        null
                )

                .show();
    }
}