package com.example.ebs.activity;

import android.app.AlertDialog;
import android.app.DatePickerDialog;
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
import com.example.ebs.api.EmployeeApi;
import com.example.ebs.model.Employee;
import com.example.ebs.util.RetrofitService;

import java.util.Calendar;
import java.util.List;
import java.util.Locale;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class EmployeeActivity extends AppCompatActivity implements View.OnClickListener {

    EditText employeeCode, firstName, lastName, gender, phone, email, address,
            dateOfBirth, hireDate, salary, departmentCode, departmentName,
            designationCode, designationName, status;
    Button btnSave;

    LinearLayout updateFormLayout;
    EditText updateId, updateEmployeeCode, updateFirstName, updateLastName, updateGender,
            updatePhone, updateEmail, updateAddress, updateDateOfBirth, updateHireDate,
            updateSalary, updateDepartmentCode, updateDepartmentName,
            updateDesignationCode, updateDesignationName, updateStatus;
    Button btnUpdate;

    Button btnHome;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        setContentView(R.layout.activity_employee);
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.mainLayout), (v, insets) -> {
            Insets systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars());
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom);
            return insets;
        });

        employeeCode = findViewById(R.id.employee_code);
        firstName = findViewById(R.id.first_name);
        lastName = findViewById(R.id.last_name);
        gender = findViewById(R.id.gender);
        phone = findViewById(R.id.phone);
        email = findViewById(R.id.email);
        address = findViewById(R.id.address);
        dateOfBirth = findViewById(R.id.date_of_birth);
        hireDate = findViewById(R.id.hire_date);
        salary = findViewById(R.id.salary);
        departmentCode = findViewById(R.id.department_code);
        departmentName = findViewById(R.id.department_name);
        designationCode = findViewById(R.id.designation_code);
        designationName = findViewById(R.id.designation_name);
        status = findViewById(R.id.status);
        btnSave = findViewById(R.id.btnSave);
        btnSave.setOnClickListener(this);

        dateOfBirth.setOnClickListener(v -> showDatePickerDialog(dateOfBirth));
        hireDate.setOnClickListener(v -> showDatePickerDialog(hireDate));

        updateFormLayout = findViewById(R.id.updateFormLayout);
        updateId = findViewById(R.id.update_id);
        updateEmployeeCode = findViewById(R.id.update_employee_code);
        updateFirstName = findViewById(R.id.update_first_name);
        updateLastName = findViewById(R.id.update_last_name);
        updateGender = findViewById(R.id.update_gender);
        updatePhone = findViewById(R.id.update_phone);
        updateEmail = findViewById(R.id.update_email);
        updateAddress = findViewById(R.id.update_address);
        updateDateOfBirth = findViewById(R.id.update_date_of_birth);
        updateHireDate = findViewById(R.id.update_hire_date);
        updateSalary = findViewById(R.id.update_salary);
        updateDepartmentCode = findViewById(R.id.update_department_code);
        updateDepartmentName = findViewById(R.id.update_department_name);
        updateDesignationCode = findViewById(R.id.update_designation_code);
        updateDesignationName = findViewById(R.id.update_designation_name);
        updateStatus = findViewById(R.id.update_status);
        btnUpdate = findViewById(R.id.btnUpdate);
        btnUpdate.setOnClickListener(this);

        updateDateOfBirth.setOnClickListener(v -> showDatePickerDialog(updateDateOfBirth));
        updateHireDate.setOnClickListener(v -> showDatePickerDialog(updateHireDate));

        addHeaders();
        addData();

        btnHome = findViewById(R.id.btnHome);
        btnHome.setOnClickListener(v -> {
            Intent i = new Intent(getApplicationContext(), HomeActivity.class);
            startActivity(i);
        });
    }

    private void showDatePickerDialog(final EditText editText) {
        final Calendar calendar = Calendar.getInstance();
        int year = calendar.get(Calendar.YEAR);
        int month = calendar.get(Calendar.MONTH);
        int day = calendar.get(Calendar.DAY_OF_MONTH);

        DatePickerDialog datePickerDialog = new DatePickerDialog(
                this,
                (view, selectedYear, selectedMonth, selectedDay) -> {
                    String selectedDate = String.format(Locale.getDefault(),
                            "%04d-%02d-%02d", selectedYear, selectedMonth + 1, selectedDay);
                    editText.setText(selectedDate);
                },
                year, month, day);
        datePickerDialog.show();
    }

    @Override
    public void onClick(View v) {
        if(v.getId() == R.id.btnSave) {
            addEmployee();
        }
        else if(v.getId() == R.id.btnUpdate) {
            updateEmployee();
        }
    }

    private void addEmployee() {
        Employee employee = new Employee();

        String codeStr = employeeCode.getText().toString();
        if(!codeStr.isEmpty()) {
            employee.setEmployeeCode(Long.valueOf(codeStr));
        }

        employee.setFirstName(firstName.getText().toString());
        employee.setLastName(lastName.getText().toString());
        employee.setGender(gender.getText().toString());
        employee.setPhone(phone.getText().toString());
        employee.setEmail(email.getText().toString());
        employee.setAddress(address.getText().toString());
        employee.setDateOfBirth(dateOfBirth.getText().toString());
        employee.setHireDate(hireDate.getText().toString());

        String salaryStr = salary.getText().toString();
        if(!salaryStr.isEmpty()) {
            employee.setSalary(Double.valueOf(salaryStr));
        }

        String deptCodeStr = departmentCode.getText().toString();
        if(!deptCodeStr.isEmpty()) {
            employee.setDepartmentCode(Long.valueOf(deptCodeStr));
        }

        employee.setDepartmentName(departmentName.getText().toString());

        String desigCodeStr = designationCode.getText().toString();
        if(!desigCodeStr.isEmpty()) {
            employee.setDesignationCode(Long.valueOf(desigCodeStr));
        }

        employee.setDesignationName(designationName.getText().toString());
        employee.setStatus(status.getText().toString());

        RetrofitService retrofitService = new RetrofitService();
        EmployeeApi employeeApi = retrofitService.getRetrofit().create(EmployeeApi.class);
        employeeApi.save(employee).enqueue(new Callback<Employee>() {
            @Override
            public void onResponse(Call<Employee> call, Response<Employee> response) {
                if(response.isSuccessful()) {
                    Toast.makeText(EmployeeActivity.this, "Employee added!", Toast.LENGTH_SHORT).show();
                    refreshTable();
                    clearAddForm();
                    hideUpdateForm();
                }
            }

            @Override
            public void onFailure(Call<Employee> call, Throwable t) {
                Toast.makeText(EmployeeActivity.this, "Failed to add employee", Toast.LENGTH_SHORT).show();
            }
        });
    }

    private void updateEmployee() {
        Employee employee = new Employee();
        employee.setId(Long.valueOf(updateId.getText().toString()));

        String updateCodeStr = updateEmployeeCode.getText().toString();
        if(!updateCodeStr.isEmpty()) {
            employee.setEmployeeCode(Long.valueOf(updateCodeStr));
        }

        employee.setFirstName(updateFirstName.getText().toString());
        employee.setLastName(updateLastName.getText().toString());
        employee.setGender(updateGender.getText().toString());
        employee.setPhone(updatePhone.getText().toString());
        employee.setEmail(updateEmail.getText().toString());
        employee.setAddress(updateAddress.getText().toString());
        employee.setDateOfBirth(updateDateOfBirth.getText().toString());
        employee.setHireDate(updateHireDate.getText().toString());

        String updateSalaryStr = updateSalary.getText().toString();
        if(!updateSalaryStr.isEmpty()) {
            employee.setSalary(Double.valueOf(updateSalaryStr));
        }

        String updateDeptCodeStr = updateDepartmentCode.getText().toString();
        if(!updateDeptCodeStr.isEmpty()) {
            employee.setDepartmentCode(Long.valueOf(updateDeptCodeStr));
        }

        employee.setDepartmentName(updateDepartmentName.getText().toString());

        String updateDesigCodeStr = updateDesignationCode.getText().toString();
        if(!updateDesigCodeStr.isEmpty()) {
            employee.setDesignationCode(Long.valueOf(updateDesigCodeStr));
        }

        employee.setDesignationName(updateDesignationName.getText().toString());
        employee.setStatus(updateStatus.getText().toString());

        RetrofitService retrofitService = new RetrofitService();
        EmployeeApi employeeApi = retrofitService.getRetrofit().create(EmployeeApi.class);
        employeeApi.update(employee.getId(), employee).enqueue(new Callback<Employee>() {
            @Override
            public void onResponse(Call<Employee> call, Response<Employee> response) {
                if(response.isSuccessful()) {
                    Toast.makeText(EmployeeActivity.this, "Employee updated!", Toast.LENGTH_SHORT).show();
                    refreshTable();
                    hideUpdateForm();
                    clearUpdateForm();
                }
            }

            @Override
            public void onFailure(Call<Employee> call, Throwable t) {
                Toast.makeText(EmployeeActivity.this, "Failed to update employee", Toast.LENGTH_SHORT).show();
            }
        });
    }

    public void addHeaders() {
        TableLayout tl = findViewById(R.id.table);
        TableRow tr = new TableRow(this);
        tr.setLayoutParams(getLayoutParams());

        tr.addView(getTextView(0, "Code", Color.WHITE, Typeface.BOLD, Color.BLUE));
        tr.addView(getTextView(0, "Name", Color.WHITE, Typeface.BOLD, Color.BLUE));
        tr.addView(getTextView(0, "Gender", Color.WHITE, Typeface.BOLD, Color.BLUE));
        tr.addView(getTextView(0, "Phone", Color.WHITE, Typeface.BOLD, Color.BLUE));
        tr.addView(getTextView(0, "Email", Color.WHITE, Typeface.BOLD, Color.BLUE));
        tr.addView(getTextView(0, "Address", Color.WHITE, Typeface.BOLD, Color.BLUE));
        tr.addView(getTextView(0, "Date of Birth", Color.WHITE, Typeface.BOLD, Color.BLUE));
        tr.addView(getTextView(0, "Hire Date", Color.WHITE, Typeface.BOLD, Color.BLUE));
//        tr.addView(getTextView(0, "Salary", Color.WHITE, Typeface.BOLD, Color.BLUE));
        tr.addView(getTextView(0, "Department Code", Color.WHITE, Typeface.BOLD, Color.BLUE));
        tr.addView(getTextView(0, "Department", Color.WHITE, Typeface.BOLD, Color.BLUE));
        tr.addView(getTextView(0, "Designation Code", Color.WHITE, Typeface.BOLD, Color.BLUE));
        tr.addView(getTextView(0, "Designation", Color.WHITE, Typeface.BOLD, Color.BLUE));
        tr.addView(getTextView(0, "Status", Color.WHITE, Typeface.BOLD, Color.BLUE));
        tr.addView(getTextView(0, "Edit", Color.WHITE, Typeface.BOLD, Color.BLUE));
        tr.addView(getTextView(0, "Delete", Color.WHITE, Typeface.BOLD, Color.BLUE));

        tl.addView(tr, getTblLayoutParams());
    }

    public void addData() {
        TableLayout tl = findViewById(R.id.table);
        RetrofitService retrofitService = new RetrofitService();
        EmployeeApi employeeApi = retrofitService.getRetrofit().create(EmployeeApi.class);

        employeeApi.getAllEmployees().enqueue(new Callback<List<Employee>>() {
            @Override
            public void onResponse(Call<List<Employee>> call, Response<List<Employee>> response) {
                if(response.isSuccessful()) {
                    List<Employee> employees = response.body();
                    for (Employee employee : employees) {
                        TableRow tr = new TableRow(EmployeeActivity.this);
                        tr.setLayoutParams(getLayoutParams());

                        String code = employee.getEmployeeCode() != null ?
                                String.valueOf(employee.getEmployeeCode()) : "";

                        String name = (employee.getFirstName() != null ? employee.getFirstName() : "") +
                                " " + (employee.getLastName() != null ? employee.getLastName() : "");

                        String gender = employee.getGender() != null ? employee.getGender() : "";

                        String phone = employee.getPhone() != null ? employee.getPhone() : "";

                        String email = employee.getEmail() != null ? employee.getEmail() : "";

                        String address = employee.getAddress() != null ? employee.getAddress() : "";

                        String dob = employee.getDateOfBirth() != null ?
                                employee.getDateOfBirth() : "";

                        String hireDate = employee.getHireDate() != null ?
                                employee.getHireDate() : "";

//                        String salary = employee.getSalary() != null ?
//                                String.valueOf(employee.getSalary()) : "";

                        String deptCode = employee.getDepartmentCode() != null ?
                                String.valueOf(employee.getDepartmentCode()) : "";

                        String dept = employee.getDepartmentName() != null ?
                                employee.getDepartmentName() : "";

                        String desiCode = employee.getDesignationCode() != null ?
                                String.valueOf(employee.getDesignationCode()) : "";

                        String designation = employee.getDesignationName() != null ?
                                employee.getDesignationName() : "";

                        String status = employee.getStatus() != null ?
                                employee.getStatus() : "";

                        tr.addView(getTextView(0, code, Color.BLACK, Typeface.NORMAL, Color.WHITE));
                        tr.addView(getTextView(0, name, Color.BLACK, Typeface.NORMAL, Color.WHITE));
                        tr.addView(getTextView(0, gender, Color.BLACK, Typeface.NORMAL, Color.WHITE));
                        tr.addView(getTextView(0, phone, Color.BLACK, Typeface.NORMAL, Color.WHITE));
                        tr.addView(getTextView(0, email, Color.BLACK, Typeface.NORMAL, Color.WHITE));
                        tr.addView(getTextView(0, address, Color.BLACK, Typeface.NORMAL, Color.WHITE));
                        tr.addView(getTextView(0, dob, Color.BLACK, Typeface.NORMAL, Color.WHITE));
                        tr.addView(getTextView(0, hireDate, Color.BLACK, Typeface.NORMAL, Color.WHITE));
//                        tr.addView(getTextView(0, salary, Color.BLACK, Typeface.NORMAL, Color.WHITE));
                        tr.addView(getTextView(0, deptCode, Color.BLACK, Typeface.NORMAL, Color.WHITE));
                        tr.addView(getTextView(0, dept, Color.BLACK, Typeface.NORMAL, Color.WHITE));
                        tr.addView(getTextView(0, desiCode, Color.BLACK, Typeface.NORMAL, Color.WHITE));
                        tr.addView(getTextView(0, designation, Color.BLACK, Typeface.NORMAL, Color.WHITE));
                        tr.addView(getTextView(0, status, Color.BLACK, Typeface.NORMAL, Color.WHITE));

                        TextView editBtn = getTextView(employee.getId().intValue(), "Edit",
                                Color.BLUE, Typeface.BOLD, Color.WHITE);
                        editBtn.setOnClickListener(v -> showUpdateForm(employee));
                        tr.addView(editBtn);

                        TextView deleteBtn = getTextView(employee.getId().intValue(), "Delete",
                                Color.RED, Typeface.BOLD, Color.WHITE);
                        deleteBtn.setOnClickListener(v -> showDeleteDialog(employee.getId(), tr));
                        tr.addView(deleteBtn);

                        tl.addView(tr, getTblLayoutParams());
                    }
                }
            }

            @Override
            public void onFailure(Call<List<Employee>> call, Throwable t) {
                Toast.makeText(EmployeeActivity.this, "Failed to load employees", Toast.LENGTH_SHORT).show();
            }
        });
    }

    private void showUpdateForm(Employee employee) {
        updateId.setText(String.valueOf(employee.getId()));

        updateEmployeeCode.setText(employee.getEmployeeCode() != null ?
                String.valueOf(employee.getEmployeeCode()) : "");
        updateFirstName.setText(employee.getFirstName() != null ? employee.getFirstName() : "");
        updateLastName.setText(employee.getLastName() != null ? employee.getLastName() : "");
        updateGender.setText(employee.getGender() != null ? employee.getGender() : "");
        updatePhone.setText(employee.getPhone() != null ? employee.getPhone() : "");
        updateEmail.setText(employee.getEmail() != null ? employee.getEmail() : "");
        updateAddress.setText(employee.getAddress() != null ? employee.getAddress() : "");
        updateDateOfBirth.setText(employee.getDateOfBirth() != null ? employee.getDateOfBirth() : "");
        updateHireDate.setText(employee.getHireDate() != null ? employee.getHireDate() : "");
        updateSalary.setText(employee.getSalary() != null ?
                String.valueOf(employee.getSalary()) : "");
        updateDepartmentCode.setText(employee.getDepartmentCode() != null ?
                String.valueOf(employee.getDepartmentCode()) : "");
        updateDepartmentName.setText(employee.getDepartmentName() != null ?
                employee.getDepartmentName() : "");
        updateDesignationCode.setText(employee.getDesignationCode() != null ?
                String.valueOf(employee.getDesignationCode()) : "");
        updateDesignationName.setText(employee.getDesignationName() != null ?
                employee.getDesignationName() : "");
        updateStatus.setText(employee.getStatus() != null ? employee.getStatus() : "");

        updateFormLayout.setVisibility(View.VISIBLE);
    }

    private void showDeleteDialog(Long employeeId, TableRow row) {
        new AlertDialog.Builder(this)
                .setTitle("Delete Employee")
                .setMessage("Are you sure you want to delete this employee?")
                .setPositiveButton("Delete", (dialog, which) -> {
                    RetrofitService retrofitService = new RetrofitService();
                    EmployeeApi employeeApi = retrofitService.getRetrofit().create(EmployeeApi.class);
                    employeeApi.delete(employeeId).enqueue(new Callback<Void>() {
                        @Override
                        public void onResponse(Call<Void> call, Response<Void> response) {
                            if(response.isSuccessful()) {
                                ((TableLayout)row.getParent()).removeView(row);
                                Toast.makeText(EmployeeActivity.this, "Employee deleted", Toast.LENGTH_SHORT).show();
                                hideUpdateForm();
                            }
                        }

                        @Override
                        public void onFailure(Call<Void> call, Throwable t) {
                            Toast.makeText(EmployeeActivity.this, "Failed to delete", Toast.LENGTH_SHORT).show();
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
        employeeCode.setText("");
        firstName.setText("");
        lastName.setText("");
        gender.setText("");
        phone.setText("");
        email.setText("");
        address.setText("");
        dateOfBirth.setText("");
        hireDate.setText("");
        salary.setText("");
        departmentCode.setText("");
        departmentName.setText("");
        designationCode.setText("");
        designationName.setText("");
        status.setText("");
    }

    private void clearUpdateForm() {
        updateId.setText("");
        updateEmployeeCode.setText("");
        updateFirstName.setText("");
        updateLastName.setText("");
        updateGender.setText("");
        updatePhone.setText("");
        updateEmail.setText("");
        updateAddress.setText("");
        updateDateOfBirth.setText("");
        updateHireDate.setText("");
        updateSalary.setText("");
        updateDepartmentCode.setText("");
        updateDepartmentName.setText("");
        updateDesignationCode.setText("");
        updateDesignationName.setText("");
        updateStatus.setText("");
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