package com.example.xionico_app.view.extra;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.CheckBox;
import android.widget.TextView;
import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;
import com.example.xionico_app.R;
import com.example.xionico_app.data.models.Task;
import com.example.xionico_app.data.repository.TaskRepository;
import android.graphics.Paint;
import org.jetbrains.annotations.NotNull;
import java.util.List;
import lombok.NoArgsConstructor;

@NoArgsConstructor
public class TaskDetailsAdapter extends RecyclerView.Adapter<TaskDetailsAdapter.TaskDetailHolder> {

    private Context context;
    private TaskRepository taskRepository;
    private List<Task> listTask;

    public TaskDetailsAdapter(Context context, TaskRepository taskRepository) {
        this.context = context;
        this.taskRepository = taskRepository;
        this.listTask = taskRepository.getAllTasks();
    }

    @NonNull
    @Override
    public TaskDetailHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(context).inflate(R.layout.task_view, parent, false);
        return new TaskDetailHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull TaskDetailHolder holder, int position) {
        Task currentTask = listTask.get(position);
        holder.id = currentTask.getId();
        holder.apiId = currentTask.getApiId();
        String status = currentTask.getStatus();
        boolean isCompleted = status != null && status.equalsIgnoreCase("COMPLETED");

        holder.txtTitle.setText(currentTask.getTitle());
        holder.txtDescripcion.setText(currentTask.getDescription());

        if (isCompleted) {
            holder.txtTitle.setPaintFlags(holder.txtTitle.getPaintFlags() | Paint.STRIKE_THRU_TEXT_FLAG);
            holder.txtDescripcion.setPaintFlags(holder.txtDescripcion.getPaintFlags() | Paint.STRIKE_THRU_TEXT_FLAG);
        } else {
            holder.txtTitle.setPaintFlags(holder.txtTitle.getPaintFlags() & ~Paint.STRIKE_THRU_TEXT_FLAG);
            holder.txtDescripcion.setPaintFlags(holder.txtDescripcion.getPaintFlags() & ~Paint.STRIKE_THRU_TEXT_FLAG);
        }

        holder.chkStatus.setOnCheckedChangeListener(null);
        holder.chkStatus.setChecked(isCompleted);

        holder.chkStatus.setOnCheckedChangeListener((compoundButton, isChecked) -> {
            taskRepository.updateTask(holder.id, isChecked ? "Completed" : "Pending", holder.apiId);

            if (isChecked) {
                holder.txtTitle.setPaintFlags(holder.txtTitle.getPaintFlags() | Paint.STRIKE_THRU_TEXT_FLAG);
                holder.txtDescripcion.setPaintFlags(holder.txtDescripcion.getPaintFlags() | Paint.STRIKE_THRU_TEXT_FLAG);
            } else {
                holder.txtTitle.setPaintFlags(holder.txtTitle.getPaintFlags() & ~Paint.STRIKE_THRU_TEXT_FLAG);
                holder.txtDescripcion.setPaintFlags(holder.txtDescripcion.getPaintFlags() & ~Paint.STRIKE_THRU_TEXT_FLAG);
            }
        });
    }

    @Override
    public int getItemCount() {
        return listTask.size();
    }

    public void setTasks(List<Task> newTasks) {
        this.listTask = newTasks;
        notifyDataSetChanged();
    }


    public class TaskDetailHolder extends RecyclerView.ViewHolder {

        private TextView txtTitle, txtDescripcion;
        private CheckBox chkStatus;

        private int id = 0;

        private int apiId = 0;

        public TaskDetailHolder(@NotNull View itemView) {
            super(itemView);

            txtTitle = itemView.findViewById(R.id.txtTitle);
            txtDescripcion = itemView.findViewById(R.id.txtDescripcion);
            chkStatus = itemView.findViewById(R.id.chkStatus);
        }
    }
}
