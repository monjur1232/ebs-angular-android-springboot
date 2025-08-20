package com.example.ebs.activity;

import android.app.AlertDialog;
import android.content.Intent;
import android.graphics.Color;
import android.graphics.Typeface;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.TableLayout;
import android.widget.TableRow;
import android.widget.TextView;
import android.widget.Toast;

import androidx.activity.EdgeToEdge;
import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;

import com.example.ebs.R;
import com.example.ebs.api.DepartmentApi;
import com.example.ebs.model.Department;
import com.example.ebs.util.RetrofitService;

import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class DepartmentActivity extends AppCompatActivity implements View.OnClickListener {

    EditText deptCode, deptName;
    Button btnSave;

    LinearLayout updateFormLayout;
    EditText updateId, updateDeptCode, updateDeptName;
    Button btnUpdate;

    Button btnHome;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        setContentView(R.layout.activity_department);
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.mainLayout), (v, insets) -> {
            Insets systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars());
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom);
            return insets;
        });

        deptCode = findViewById(R.id.dept_code);
        deptName = findViewById(R.id.dept_name);
        btnSave = findViewById(R.id.btnSave);
        btnSave.setOnClickListener(this);

        updateFormLayout = findViewById(R.id.updateFormLayout);
        updateId = findViewById(R.id.update_id);
        updateDeptCode = findViewById(R.id.update_dept_code);
        updateDeptName = findViewById(R.id.update_dept_name);
        btnUpdate = findViewById(R.id.btnUpdate);
        btnUpdate.setOnClickListener(this);

        addHeaders();
        addData();

        btnHome = findViewById(R.id.btnHome);

        btnHome.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent i = new Intent(getApplicationContext(), HomeActivity.class);
                startActivity(i);
            }
        });
    }

    @Override
    public void onClick(View v) {
        if(v.getId() == R.id.btnSave) {
            addDepartment();
        }
        else if(v.getId() == R.id.btnUpdate) {
            updateDepartment();
        }
    }

    private void addDepartment() {
        Department department = new Department();

        String codeStr = deptCode.getText().toString();
        if(!codeStr.isEmpty()) {
            department.setDepartmentCode(Long.valueOf(codeStr));
        }

        String nameStr = deptName.getText().toString();
        department.setDepartmentName(nameStr.isEmpty() ? null : nameStr);

        RetrofitService retrofitService = new RetrofitService();
        DepartmentApi departmentApi = retrofitService.getRetrofit().create(DepartmentApi.class);
        departmentApi.save(department).enqueue(new Callback<Department>() {
            @Override
            public void onResponse(Call<Department> call, Response<Department> response) {
                if(response.isSuccessful()) {
                    Toast.makeText(DepartmentActivity.this, "Department added!", Toast.LENGTH_SHORT).show();
                    refreshTable();
                    clearAddForm();
                    hideUpdateForm();
                }
            }

            @Override
            public void onFailure(Call<Department> call, Throwable t) {
                Toast.makeText(DepartmentActivity.this, "Failed to add department", Toast.LENGTH_SHORT).show();
            }
        });
    }

    private void updateDepartment() {
        Department department = new Department();
        department.setId(Long.valueOf(updateId.getText().toString()));

        String updateCodeStr = updateDeptCode.getText().toString();
        if(!updateCodeStr.isEmpty()) {
            department.setDepartmentCode(Long.valueOf(updateCodeStr));
        }

        String updateNameStr = updateDeptName.getText().toString();
        department.setDepartmentName(updateNameStr.isEmpty() ? null : updateNameStr);

        RetrofitService retrofitService = new RetrofitService();
        DepartmentApi departmentApi = retrofitService.getRetrofit().create(DepartmentApi.class);
        departmentApi.update(department.getId(), department).enqueue(new Callback<Department>() {
            @Override
            public void onResponse(Call<Department> call, Response<Department> response) {
                if(response.isSuccessful()) {
                    Toast.makeText(DepartmentActivity.this, "Department updated!", Toast.LENGTH_SHORT).show();
                    refreshTable();
                    hideUpdateForm();
                    clearUpdateForm();
                }
            }

            @Override
            public void onFailure(Call<Department> call, Throwable t) {
                Toast.makeText(DepartmentActivity.this, "Failed to update department", Toast.LENGTH_SHORT).show();
            }
        });
    }

    public void addHeaders() {
        TableLayout tl = findViewById(R.id.table);
        TableRow tr = new TableRow(this);
        tr.setLayoutParams(getLayoutParams());

        tr.addView(getTextView(0, "Code", Color.WHITE, Typeface.BOLD, Color.BLUE));
        tr.addView(getTextView(0, "Name", Color.WHITE, Typeface.BOLD, Color.BLUE));
        tr.addView(getTextView(0, "Edit", Color.WHITE, Typeface.BOLD, Color.BLUE));
        tr.addView(getTextView(0, "Delete", Color.WHITE, Typeface.BOLD, Color.BLUE));

        tl.addView(tr, getTblLayoutParams());
    }

    public void addData() {
        TableLayout tl = findViewById(R.id.table);
        RetrofitService retrofitService = new RetrofitService();
        DepartmentApi departmentApi = retrofitService.getRetrofit().create(DepartmentApi.class);

        departmentApi.getAllDepartments().enqueue(new Callback<List<Department>>() {
            @Override
            public void onResponse(Call<List<Department>> call, Response<List<Department>> response) {
                if(response.isSuccessful()) {
                    List<Department> departments = response.body();
                    for (Department department : departments) {
                        TableRow tr = new TableRow(DepartmentActivity.this);
                        tr.setLayoutParams(getLayoutParams());

                        String code = department.getDepartmentCode() != null ?
                                String.valueOf(department.getDepartmentCode()) : "";
                        String name = department.getDepartmentName() != null ?
                                department.getDepartmentName() : "";

                        tr.addView(getTextView(0, code, Color.BLACK, Typeface.BOLD, Color.WHITE));
                        tr.addView(getTextView(0, name, Color.BLACK, Typeface.BOLD, Color.WHITE));

                        TextView editBtn = getTextView(department.getId().intValue(), "Edit",
                                Color.BLUE, Typeface.BOLD, Color.WHITE);
                        editBtn.setOnClickListener(v -> showUpdateForm(department));
                        tr.addView(editBtn);

                        TextView deleteBtn = getTextView(department.getId().intValue(), "Delete",
                                Color.RED, Typeface.BOLD, Color.WHITE);
                        deleteBtn.setOnClickListener(v -> showDeleteDialog(department.getId(), tr));
                        tr.addView(deleteBtn);

                        tl.addView(tr, getTblLayoutParams());
                    }
                }
            }

            @Override
            public void onFailure(Call<List<Department>> call, Throwable t) {
                Toast.makeText(DepartmentActivity.this, "Failed to load departments", Toast.LENGTH_SHORT).show();
            }
        });
    }

    private void showUpdateForm(Department department) {
        updateId.setText(String.valueOf(department.getId()));

        updateDeptCode.setText(department.getDepartmentCode() != null ?
                String.valueOf(department.getDepartmentCode()) : "");
        updateDeptName.setText(department.getDepartmentName() != null ?
                department.getDepartmentName() : "");

        updateFormLayout.setVisibility(View.VISIBLE);
    }

    private void showDeleteDialog(Long departmentId, TableRow row) {
        new AlertDialog.Builder(this)
                .setTitle("Delete Department")
                .setMessage("Are you sure you want to delete this department?")
                .setPositiveButton("Delete", (dialog, which) -> {
                    RetrofitService retrofitService = new RetrofitService();
                    DepartmentApi departmentApi = retrofitService.getRetrofit().create(DepartmentApi.class);
                    departmentApi.delete(departmentId).enqueue(new Callback<Void>() {
                        @Override
                        public void onResponse(Call<Void> call, Response<Void> response) {
                            if(response.isSuccessful()) {
                                ((TableLayout)row.getParent()).removeView(row);
                                Toast.makeText(DepartmentActivity.this, "Department deleted", Toast.LENGTH_SHORT).show();
                                hideUpdateForm();
                            }
                        }

                        @Override
                        public void onFailure(Call<Void> call, Throwable t) {
                            Toast.makeText(DepartmentActivity.this, "Failed to delete", Toast.LENGTH_SHORT).show();
                        }
                    });
                })
                .setNegativeButton("Cancel", (dialog, which) -> {
                    Toast.makeText(this, "Cancelled", Toast.LENGTH_SHORT).show();
                })
                .show();
    }

    private void hideUpdateForm() {
        updateFormLayout.setVisibility(View.GONE);
    }

    private void clearAddForm() {
        deptCode.setText("");
        deptName.setText("");
    }

    private void clearUpdateForm() {
        updateId.setText("");
        updateDeptCode.setText("");
        updateDeptName.setText("");
    }

    private void refreshTable() {
        TableLayout tl = findViewById(R.id.table);
        tl.removeAllViews();
        addHeaders();
        addData();
    }

    private TextView getTextView(int id, String title, int color, int typeface, int bgColor) {
        TextView tv = new TextView(this);
        tv.setId(id);
        tv.setText(title);
        tv.setTextColor(color);
        tv.setPadding(40, 40, 40, 40);
        tv.setTypeface(Typeface.DEFAULT, typeface);
        tv.setBackgroundColor(bgColor);
        tv.setLayoutParams(getLayoutParams());
        return tv;
    }

    @NonNull
    private TableRow.LayoutParams getLayoutParams() {
        TableRow.LayoutParams params = new TableRow.LayoutParams(
                TableRow.LayoutParams.MATCH_PARENT,
                TableRow.LayoutParams.WRAP_CONTENT);
        params.setMargins(2, 2, 2, 2);
        return params;
    }

    @NonNull
    private TableLayout.LayoutParams getTblLayoutParams() {
        return new TableLayout.LayoutParams(
                TableRow.LayoutParams.MATCH_PARENT,
                TableRow.LayoutParams.WRAP_CONTENT);
    }
}