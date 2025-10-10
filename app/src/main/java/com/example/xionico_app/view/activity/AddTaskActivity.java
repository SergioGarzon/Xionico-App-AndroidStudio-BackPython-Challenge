package com.example.xionico_app.view.activity;

import android.os.Bundle;
import android.view.View;
import android.widget.Spinner;
import android.widget.Button;
import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;
import androidx.appcompat.app.AlertDialog;
import com.example.xionico_app.R;
import com.example.xionico_app.data.models.Task;
import com.example.xionico_app.data.repository.TaskRepository;
import com.google.android.material.textfield.TextInputLayout;
import android.content.DialogInterface;

public class AddTaskActivity extends AppCompatActivity {

    private Button btnCancelar, btnAgregar;
    private TextInputLayout txtTitulo, txtDescripcion;
    private TaskRepository taskRepository;
    private Task task;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        setContentView(R.layout.activity_new);
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main), (v, insets) -> {
            Insets systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars());
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom);
            return insets;
        });

        btnCancelar = (Button) findViewById(R.id.btnCancelar);
        btnAgregar = (Button) findViewById(R.id.btnAgregar);
        txtTitulo = (TextInputLayout) findViewById(R.id.txtTitulo);
        txtDescripcion = (TextInputLayout) findViewById(R.id.txtDescripcion);

        taskRepository = new TaskRepository(getApplicationContext());

        btnAgregar.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                validateFields();
            }
        });

        btnCancelar.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                backActivity();
                finish();
            }
        });
    }

    private void backActivity() {
        txtTitulo.getEditText().setText("");
        txtDescripcion.getEditText().setText("");
    }

    private void validateFields() {
        if(txtTitulo.getEditText().getText().toString().equals("") || txtDescripcion.getEditText().getText().toString().equals("")) {
            mensajeInformativo("Faltan campos por completar", "Error!");
        } else {
            task = new Task(0, txtTitulo.getEditText().getText().toString(), txtDescripcion.getEditText().getText().toString(), "PENDING");
            taskRepository.insertTask(task);

            backActivity();
            mensajeInformativo("Se ha guardado correctamente", "Información");
        }
    }

    private void mensajeInformativo(String mensaje, String tituloMensaje) {
        AlertDialog.Builder dialog = new AlertDialog.Builder(this);
        dialog.setMessage(mensaje);
        dialog.setTitle(tituloMensaje);
        dialog.setPositiveButton("Ok",
                new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int which) {
                        dialog.cancel();
                    }
                });
        dialog.setCancelable(true);
        dialog.create().show();
    }

}
