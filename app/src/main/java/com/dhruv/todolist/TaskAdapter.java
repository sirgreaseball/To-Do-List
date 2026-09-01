package com.dhruv.todolist;

import android.app.AlertDialog;
import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.CheckBox;
import android.widget.EditText;
import android.widget.ImageButton;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import java.util.ArrayList;

public class TaskAdapter
        extends RecyclerView.Adapter<TaskAdapter.ViewHolder> {


    Context context;

    ArrayList<Task> list;

    DatabaseHelper databaseHelper;


    // CONSTRUCTOR
    public TaskAdapter(
            Context context,
            ArrayList<Task> list,
            DatabaseHelper databaseHelper
    ) {

        this.context = context;

        this.list = list;

        this.databaseHelper = databaseHelper;
    }


    // VIEW HOLDER
    public class ViewHolder
            extends RecyclerView.ViewHolder {


        CheckBox add_task;

        ImageButton btnadd;

        ImageButton btndelete;


        public ViewHolder(
                @NonNull View itemView
        ) {

            super(itemView);


            add_task = itemView.findViewById(
                    R.id.add_task
            );


            btnadd = itemView.findViewById(
                    R.id.btnadd
            );


            btndelete = itemView.findViewById(
                    R.id.btndelete
            );
        }
    }


    // CREATE VIEW HOLDER
    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(
            @NonNull ViewGroup parent,
            int viewType
    ) {

        View view = LayoutInflater
                .from(context)
                .inflate(
                        R.layout.task_items,
                        parent,
                        false
                );


        return new ViewHolder(view);
    }


    // BIND DATA
    @Override
    public void onBindViewHolder(
            @NonNull ViewHolder holder,
            int position
    ) {


        Task task = list.get(position);


        // -------------------------
        // TASK NAME
        // -------------------------

        holder.add_task.setText(
                task.getTitle()
        );


        // Remove old listener
        // This prevents unwanted database updates
        // while RecyclerView is recycling views.

        holder.add_task.setOnCheckedChangeListener(
                null
        );


        // Set checkbox state

        holder.add_task.setChecked(
                task.isCompleted()
        );


        // -------------------------
        // CHECKBOX
        // -------------------------

        holder.add_task.setOnCheckedChangeListener(
                (buttonView, isChecked) -> {

                    task.setCompleted(
                            isChecked
                    );


                    databaseHelper.updateTaskStatus(
                            task.getId(),
                            isChecked
                    );
                }
        );


        // -------------------------
        // DELETE
        // -------------------------

        holder.btndelete.setOnClickListener(
                v -> {

                    int currentPosition =
                            holder.getAdapterPosition();


                    if (
                            currentPosition ==
                                    RecyclerView.NO_POSITION
                    ) {
                        return;
                    }


                    Task currentTask =
                            list.get(currentPosition);


                    // Delete from database

                    databaseHelper.deleteTask(
                            currentTask.getId()
                    );


                    // Delete from list

                    list.remove(
                            currentPosition
                    );


                    // Update RecyclerView

                    notifyItemRemoved(
                            currentPosition
                    );
                }
        );


        // -------------------------
        // EDIT
        // -------------------------

        holder.btnadd.setOnClickListener(
                v -> {


                    EditText editText =
                            new EditText(context);


                    editText.setText(
                            task.getTitle()
                    );


                    new AlertDialog.Builder(context)

                            .setTitle("Edit Task")

                            .setView(editText)

                            .setPositiveButton(
                                    "Save",
                                    (dialog, which) -> {


                                        String newName =
                                                editText
                                                        .getText()
                                                        .toString()
                                                        .trim();


                                        if (
                                                !newName.isEmpty()
                                        ) {


                                            // Update Task object

                                            task.setTitle(
                                                    newName
                                            );


                                            // Update database

                                            databaseHelper
                                                    .updateTaskName(
                                                            task.getId(),
                                                            newName
                                                    );


                                            // Update RecyclerView

                                            int currentPosition =
                                                    holder.getAdapterPosition();


                                            if (
                                                    currentPosition !=
                                                            RecyclerView.NO_POSITION
                                            ) {

                                                notifyItemChanged(
                                                        currentPosition
                                                );
                                            }
                                        }
                                    }
                            )

                            .setNegativeButton(
                                    "Cancel",
                                    null
                            )

                            .show();
                }
        );
    }


    // NUMBER OF TASKS
    @Override
    public int getItemCount() {

        return list.size();
    }
}