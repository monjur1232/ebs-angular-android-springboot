package com.example.ebs.activity;

import android.app.AlertDialog;
import android.app.DatePickerDialog;
import android.app.TimePickerDialog;
import android.content.Intent;
import android.graphics.Color;
import android.graphics.Typeface;
import android.os.Bundle;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.Spinner;
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
import com.example.ebs.api.AttendanceApi;
import com.example.ebs.api.EmployeeApi;
import com.example.ebs.model.Attendance;
import com.example.ebs.model.Employee;
import com.example.ebs.util.RetrofitService;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.HashMap;
import java.util.List;
import java.util.Locale;
import java.util.Map;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class AttendanceActivity extends AppCompatActivity implements View.OnClickListener {

    // Bulk attendance components
    private EditText attendanceDate;
    private Button btnMarkAllPresent, btnMarkAllAbsent, btnSaveBulk;
    private TableLayout attendanceTable;
    private List<Employee> employees = new ArrayList<>();
    private Map<Long, Attendance> employeeAttendanceMap = new HashMap<>();

    // Records components
    private EditText searchText, filterDate;
    private TableLayout recordsTable;
    private List<Attendance> attendances = new ArrayList<>();

    // Update form components
    private LinearLayout updateFormLayout;
    private EditText updateId, updateEmployeeCode, updateEmployeeName, updateDate, updateInTime, updateOutTime;
    private Spinner updateStatus;
    private Button btnUpdate;

    // Common components
    private Button btnHome;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        setContentView(R.layout.activity_attendance);
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.mainLayout), (v, insets) -> {
            Insets systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars());
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom);
            return insets;
        });

        // Initialize bulk attendance components
        attendanceDate = findViewById(R.id.attendanceDate);
        btnMarkAllPresent = findViewById(R.id.btnMarkAllPresent);
        btnMarkAllAbsent = findViewById(R.id.btnMarkAllAbsent);
        btnSaveBulk = findViewById(R.id.btnSaveBulk);
        attendanceTable = findViewById(R.id.attendanceTable);

        btnMarkAllPresent.setOnClickListener(this);
        btnMarkAllAbsent.setOnClickListener(this);
        btnSaveBulk.setOnClickListener(this);

        // Set up date pickers
        attendanceDate.setOnClickListener(v -> showDatePickerDialog(attendanceDate));
        attendanceDate.setFocusable(false);

        // Set current date as default
        SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd", Locale.getDefault());
        attendanceDate.setText(sdf.format(Calendar.getInstance().getTime()));

        // Initialize records components
        searchText = findViewById(R.id.searchText);
        filterDate = findViewById(R.id.filterDate);
        recordsTable = findViewById(R.id.recordsTable);

        // Set up date picker for filter date
        filterDate.setOnClickListener(v -> showDatePickerDialog(filterDate));
        filterDate.setFocusable(false);

        // Initialize update form components
        updateFormLayout = findViewById(R.id.updateFormLayout);
        updateId = findViewById(R.id.updateId);
        updateEmployeeCode = findViewById(R.id.updateEmployeeCode);
        updateEmployeeName = findViewById(R.id.updateEmployeeName);
        updateDate = findViewById(R.id.updateDate);
        updateInTime = findViewById(R.id.updateInTime);
        updateOutTime = findViewById(R.id.updateOutTime);
        updateStatus = findViewById(R.id.updateStatus);
        btnUpdate = findViewById(R.id.btnUpdate);
        btnUpdate.setOnClickListener(this);

        // Set up date picker for update date
        updateDate.setOnClickListener(v -> showDatePickerDialog(updateDate));
        updateDate.setFocusable(false);

        // Set up status spinner
        ArrayAdapter<CharSequence> statusAdapter = ArrayAdapter.createFromResource(this,
                R.array.attendance_status_array, android.R.layout.simple_spinner_item);
        statusAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        updateStatus.setAdapter(statusAdapter);

        // Initialize common components
        btnHome = findViewById(R.id.btnHome);
        btnHome.setOnClickListener(v -> {
            Intent i = new Intent(getApplicationContext(), HomeActivity.class);
            startActivity(i);
        });

        // Load employees and attendance data
        loadEmployees();
        loadAttendanceRecords();
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

                    // ✅ যদি এইটা attendanceDate হলে তাহলে attendanceMap ও table আপডেট করুন
                    if (editText.getId() == R.id.attendanceDate) {
                        initializeAttendanceMap();       // নতুন তারিখ দিয়ে Attendance map বানান
                        populateAttendanceTable();       // টেবিল আবার দেখান
                    }
                },
                year, month, day);
        datePickerDialog.show();
    }


    private void loadEmployees() {
        RetrofitService retrofitService = new RetrofitService();
        EmployeeApi employeeApi = retrofitService.getRetrofit().create(EmployeeApi.class);
        employeeApi.getAllEmployees().enqueue(new Callback<List<Employee>>() {
            @Override
            public void onResponse(Call<List<Employee>> call, Response<List<Employee>> response) {
                if (response.isSuccessful()) {
                    employees = response.body();
                    initializeAttendanceMap();
                    populateAttendanceTable();
                } else {
                    Toast.makeText(AttendanceActivity.this, "Failed to load employees", Toast.LENGTH_SHORT).show();
                }
            }

            @Override
            public void onFailure(Call<List<Employee>> call, Throwable t) {
                Toast.makeText(AttendanceActivity.this, "Failed to load employees", Toast.LENGTH_SHORT).show();
            }
        });
    }

    private void loadAttendanceRecords() {
        RetrofitService retrofitService = new RetrofitService();
        AttendanceApi attendanceApi = retrofitService.getRetrofit().create(AttendanceApi.class);
        attendanceApi.getAllAttendances().enqueue(new Callback<List<Attendance>>() {
            @Override
            public void onResponse(Call<List<Attendance>> call, Response<List<Attendance>> response) {
                if (response.isSuccessful()) {
                    attendances = response.body();
                    populateRecordsTable();
                } else {
                    Toast.makeText(AttendanceActivity.this, "Failed to load attendance records", Toast.LENGTH_SHORT).show();
                }
            }

            @Override
            public void onFailure(Call<List<Attendance>> call, Throwable t) {
                Toast.makeText(AttendanceActivity.this, "Failed to load attendance records", Toast.LENGTH_SHORT).show();
            }
        });
    }

    private void initializeAttendanceMap() {
        employeeAttendanceMap.clear();
        for (Employee employee : employees) {
            Attendance attendance = new Attendance();
            attendance.setEmployeeCode(employee.getEmployeeCode());
            attendance.setEmployeeName(employee.getFirstName() + " " + employee.getLastName());
            attendance.setDate(attendanceDate.getText().toString());
            attendance.setInTime("09:00");
            attendance.setOutTime("17:00");
            attendance.setStatus("Present");
            employeeAttendanceMap.put(employee.getEmployeeCode(), attendance);
        }
    }

    private void populateAttendanceTable() {
        attendanceTable.removeAllViews();

        // Add headers
        TableRow headerRow = new TableRow(this);
        headerRow.setLayoutParams(getLayoutParams());

        headerRow.addView(getTextView(0, "Employee Code", Color.WHITE, Typeface.BOLD, Color.BLUE));
        headerRow.addView(getTextView(0, "Employee Name", Color.WHITE, Typeface.BOLD, Color.BLUE));
        headerRow.addView(getTextView(0, "In Time", Color.WHITE, Typeface.BOLD, Color.BLUE));
        headerRow.addView(getTextView(0, "Out Time", Color.WHITE, Typeface.BOLD, Color.BLUE));
        headerRow.addView(getTextView(0, "Status", Color.WHITE, Typeface.BOLD, Color.BLUE));
        headerRow.addView(getTextView(0, "Action", Color.WHITE, Typeface.BOLD, Color.BLUE));

        attendanceTable.addView(headerRow, getTblLayoutParams());

        // Add data rows
        for (Employee employee : employees) {
            Attendance attendance = employeeAttendanceMap.get(employee.getEmployeeCode());

            TableRow row = new TableRow(this);
            row.setLayoutParams(getLayoutParams());

            row.addView(getTextView(0, String.valueOf(employee.getEmployeeCode()), Color.BLACK, Typeface.NORMAL, Color.WHITE));
            row.addView(getTextView(0, employee.getFirstName() + " " + employee.getLastName(), Color.BLACK, Typeface.NORMAL, Color.WHITE));

            // In Time
            TextView inTimeTv = getTextView(0, attendance.getInTime(), Color.BLACK, Typeface.NORMAL, Color.WHITE);
            inTimeTv.setOnClickListener(v -> showTimePickerDialog(inTimeTv, attendance, true));
            row.addView(inTimeTv);

            // Out Time
            TextView outTimeTv = getTextView(0, attendance.getOutTime(), Color.BLACK, Typeface.NORMAL, Color.WHITE);
            outTimeTv.setOnClickListener(v -> showTimePickerDialog(outTimeTv, attendance, false));
            row.addView(outTimeTv);

            // Status
            TextView statusTv = getTextView(0, attendance.getStatus(),
                    attendance.getStatus().equals("Absent") ? Color.RED :
                            attendance.getStatus().equals("Late") ? Color.YELLOW : Color.GREEN,
                    Typeface.NORMAL, Color.WHITE);
            statusTv.setOnClickListener(v -> showStatusDialog(attendance));
            row.addView(statusTv);

            // Clear button
            TextView clearBtn = getTextView(0, "Clear", Color.BLUE, Typeface.BOLD, Color.WHITE);
            clearBtn.setOnClickListener(v -> clearAttendance(attendance));
            row.addView(clearBtn);

            attendanceTable.addView(row, getTblLayoutParams());
        }
    }

    private void populateRecordsTable() {
        recordsTable.removeAllViews();

        // Add headers
        TableRow headerRow = new TableRow(this);
        headerRow.setLayoutParams(getLayoutParams());

        headerRow.addView(getTextView(0, "Employee Code", Color.WHITE, Typeface.BOLD, Color.BLUE));
        headerRow.addView(getTextView(0, "Employee Name", Color.WHITE, Typeface.BOLD, Color.BLUE));
        headerRow.addView(getTextView(0, "Date", Color.WHITE, Typeface.BOLD, Color.BLUE));
        headerRow.addView(getTextView(0, "In Time", Color.WHITE, Typeface.BOLD, Color.BLUE));
        headerRow.addView(getTextView(0, "Out Time", Color.WHITE, Typeface.BOLD, Color.BLUE));
        headerRow.addView(getTextView(0, "Status", Color.WHITE, Typeface.BOLD, Color.BLUE));
        headerRow.addView(getTextView(0, "Edit", Color.WHITE, Typeface.BOLD, Color.BLUE));
        headerRow.addView(getTextView(0, "Delete", Color.WHITE, Typeface.BOLD, Color.BLUE));

        recordsTable.addView(headerRow, getTblLayoutParams());

        // Add data rows
        for (Attendance attendance : attendances) {
            TableRow row = new TableRow(this);
            row.setLayoutParams(getLayoutParams());

            row.addView(getTextView(0, String.valueOf(attendance.getEmployeeCode()), Color.BLACK, Typeface.NORMAL, Color.WHITE));
            row.addView(getTextView(0, attendance.getEmployeeName(), Color.BLACK, Typeface.NORMAL, Color.WHITE));
            row.addView(getTextView(0, attendance.getDate(), Color.BLACK, Typeface.NORMAL, Color.WHITE));
            row.addView(getTextView(0, attendance.getInTime(), Color.BLACK, Typeface.NORMAL, Color.WHITE));
            row.addView(getTextView(0, attendance.getOutTime(), Color.BLACK, Typeface.NORMAL, Color.WHITE));

            // Status with color coding
            TextView statusTv = getTextView(0, attendance.getStatus(),
                    attendance.getStatus().equals("Absent") ? Color.RED :
                            attendance.getStatus().equals("Late") ? Color.YELLOW : Color.GREEN,
                    Typeface.NORMAL, Color.WHITE);
            row.addView(statusTv);

            // Edit button
            TextView editBtn = getTextView(0, "Edit", Color.BLUE, Typeface.BOLD, Color.WHITE);
            editBtn.setOnClickListener(v -> showUpdateForm(attendance));
            row.addView(editBtn);

            // Delete button
            TextView deleteBtn = getTextView(0, "Delete", Color.RED, Typeface.BOLD, Color.WHITE);
            deleteBtn.setOnClickListener(v -> showDeleteDialog(attendance.getId(), row));
            row.addView(deleteBtn);

            recordsTable.addView(row, getTblLayoutParams());
        }
    }

    private void showTimePickerDialog(final TextView textView, final Attendance attendance, final boolean isInTime) {
        Calendar calendar = Calendar.getInstance();
        int hour = calendar.get(Calendar.HOUR_OF_DAY);
        int minute = calendar.get(Calendar.MINUTE);

        TimePickerDialog timePickerDialog = new TimePickerDialog(this,
                (view, hourOfDay, minute1) -> {
                    String time = String.format(Locale.getDefault(), "%02d:%02d", hourOfDay, minute1);
                    textView.setText(time);
                    if (isInTime) {
                        attendance.setInTime(time);
                    } else {
                        attendance.setOutTime(time);
                    }
                }, hour, minute, true);
        timePickerDialog.show();
    }

    private void showStatusDialog(final Attendance attendance) {
        final String[] statuses = {"Present", "Late", "Absent"};

        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        builder.setTitle("Select Status");
        builder.setItems(statuses, (dialog, which) -> {
            attendance.setStatus(statuses[which]);
            if (statuses[which].equals("Absent")) {
                attendance.setInTime("");
                attendance.setOutTime("");
            } else {
                attendance.setInTime("09:00");
                attendance.setOutTime("17:00");
            }
            populateAttendanceTable();
        });
        builder.show();
    }

    private void clearAttendance(Attendance attendance) {
        attendance.setInTime("09:00");
        attendance.setOutTime("17:00");
        attendance.setStatus("Present");
        populateAttendanceTable();
    }

    private void showUpdateForm(Attendance attendance) {
        updateId.setText(String.valueOf(attendance.getId()));
        updateEmployeeCode.setText(String.valueOf(attendance.getEmployeeCode()));
        updateEmployeeName.setText(attendance.getEmployeeName());
        updateDate.setText(attendance.getDate());
        updateInTime.setText(attendance.getInTime());
        updateOutTime.setText(attendance.getOutTime());

        ArrayAdapter adapter = (ArrayAdapter) updateStatus.getAdapter();
        int position = adapter.getPosition(attendance.getStatus());
        updateStatus.setSelection(position);

        updateFormLayout.setVisibility(View.VISIBLE);
    }

    private void showDeleteDialog(Long attendanceId, TableRow row) {
        new AlertDialog.Builder(this)
                .setTitle("Delete Attendance")
                .setMessage("Are you sure you want to delete this attendance record?")
                .setPositiveButton("Delete", (dialog, which) -> {
                    deleteAttendance(attendanceId, row);
                })
                .setNegativeButton("Cancel", (dialog, which) -> {
                    dialog.dismiss();
                })
                .show();
    }

    private void deleteAttendance(Long attendanceId, TableRow row) {
        RetrofitService retrofitService = new RetrofitService();
        AttendanceApi attendanceApi = retrofitService.getRetrofit().create(AttendanceApi.class);
        attendanceApi.delete(attendanceId).enqueue(new Callback<Void>() {
            @Override
            public void onResponse(Call<Void> call, Response<Void> response) {
                if (response.isSuccessful()) {
                    ((TableLayout) row.getParent()).removeView(row);
                    Toast.makeText(AttendanceActivity.this, "Attendance deleted", Toast.LENGTH_SHORT).show();
                    resetAttendanceForm();
                } else {
                    Toast.makeText(AttendanceActivity.this, "Failed to delete", Toast.LENGTH_SHORT).show();
                }
            }

            @Override
            public void onFailure(Call<Void> call, Throwable t) {
                Toast.makeText(AttendanceActivity.this, "Failed to delete", Toast.LENGTH_SHORT).show();
            }
        });
    }

    @Override
    public void onClick(View v) {
        if (v.getId() == R.id.btnMarkAllPresent) {
            markAllAsPresent();
        } else if (v.getId() == R.id.btnMarkAllAbsent) {
            markAllAsAbsent();
        } else if (v.getId() == R.id.btnSaveBulk) {
            saveBulkAttendance();
        } else if (v.getId() == R.id.btnUpdate) {
            updateAttendance();
        }
    }

    private void markAllAsPresent() {
        for (Employee employee : employees) {
            Attendance attendance = employeeAttendanceMap.get(employee.getEmployeeCode());
            attendance.setStatus("Present");
            attendance.setInTime("09:00");
            attendance.setOutTime("17:00");
        }
        populateAttendanceTable();
    }

    private void markAllAsAbsent() {
        for (Employee employee : employees) {
            Attendance attendance = employeeAttendanceMap.get(employee.getEmployeeCode());
            attendance.setStatus("Absent");
            attendance.setInTime("");
            attendance.setOutTime("");
        }
        populateAttendanceTable();
    }

    private void saveBulkAttendance() {
        List<Attendance> attendancesToSave = new ArrayList<>();
        for (Employee employee : employees) {
            attendancesToSave.add(employeeAttendanceMap.get(employee.getEmployeeCode()));
        }

        RetrofitService retrofitService = new RetrofitService();
        AttendanceApi attendanceApi = retrofitService.getRetrofit().create(AttendanceApi.class);
        attendanceApi.saveBulk(attendancesToSave).enqueue(new Callback<List<Attendance>>() {
            @Override
            public void onResponse(Call<List<Attendance>> call, Response<List<Attendance>> response) {
                if (response.isSuccessful()) {
                    Toast.makeText(AttendanceActivity.this, "Bulk attendance saved successfully", Toast.LENGTH_SHORT).show();
                    loadAttendanceRecords();
                    resetAttendanceForm();
                } else {
                    Toast.makeText(AttendanceActivity.this, "Failed to save bulk attendance", Toast.LENGTH_SHORT).show();
                }
            }

            @Override
            public void onFailure(Call<List<Attendance>> call, Throwable t) {
                Toast.makeText(AttendanceActivity.this, "Failed to save bulk attendance", Toast.LENGTH_SHORT).show();
            }
        });
    }

    private void updateAttendance() {
        Attendance attendance = new Attendance();
        attendance.setId(Long.valueOf(updateId.getText().toString()));
        attendance.setEmployeeCode(Long.valueOf(updateEmployeeCode.getText().toString()));
        attendance.setEmployeeName(updateEmployeeName.getText().toString());
        attendance.setDate(updateDate.getText().toString());
        attendance.setInTime(updateInTime.getText().toString());
        attendance.setOutTime(updateOutTime.getText().toString());
        attendance.setStatus(updateStatus.getSelectedItem().toString());

        RetrofitService retrofitService = new RetrofitService();
        AttendanceApi attendanceApi = retrofitService.getRetrofit().create(AttendanceApi.class);
        attendanceApi.update(attendance.getId(), attendance).enqueue(new Callback<Attendance>() {
            @Override
            public void onResponse(Call<Attendance> call, Response<Attendance> response) {
                if (response.isSuccessful()) {
                    Toast.makeText(AttendanceActivity.this, "Attendance updated", Toast.LENGTH_SHORT).show();
                    updateFormLayout.setVisibility(View.GONE);
                    loadAttendanceRecords();
                } else {
                    Toast.makeText(AttendanceActivity.this, "Failed to update", Toast.LENGTH_SHORT).show();
                }
            }

            @Override
            public void onFailure(Call<Attendance> call, Throwable t) {
                Toast.makeText(AttendanceActivity.this, "Failed to update", Toast.LENGTH_SHORT).show();
            }
        });
    }

    private TextView getTextView(int id, String title, int color, int typeface, int bgColor) {
        TextView tv = new TextView(this);
        tv.setId(id);
        tv.setText(title);
        tv.setTextColor(color);
        tv.setPadding(20, 20, 20, 20);
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

    private void resetAttendanceForm() {
        Calendar calendar = Calendar.getInstance();
        String currentDate = String.format(Locale.getDefault(), "%04d-%02d-%02d",
                calendar.get(Calendar.YEAR),
                calendar.get(Calendar.MONTH) + 1,
                calendar.get(Calendar.DAY_OF_MONTH));
        attendanceDate.setText(currentDate);
        updateFormLayout.setVisibility(View.GONE);

        initializeAttendanceMap();
        populateAttendanceTable();
    }
}