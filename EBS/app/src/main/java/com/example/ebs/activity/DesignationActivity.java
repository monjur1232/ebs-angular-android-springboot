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
import com.example.ebs.api.DesignationApi;
import com.example.ebs.model.Designation;
import com.example.ebs.util.RetrofitService;

import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class DesignationActivity extends AppCompatActivity implements View.OnClickListener {

    EditText designationCode, designationName, level;
    Button btnSave;

    LinearLayout updateFormLayout;
    EditText updateId, updateDesignationCode, updateDesignationName, updateLevel;
    Button btnUpdate;

    Button btnHome;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        setContentView(R.layout.activity_designation);
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.mainLayout), (v, insets) -> {
            Insets systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars());
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom);
            return insets;
        });

        designationCode = findViewById(R.id.designation_code);
        designationName = findViewById(R.id.designation_name);
        level = findViewById(R.id.level);
        btnSave = findViewById(R.id.btnSave);
        btnSave.setOnClickListener(this);

        updateFormLayout = findViewById(R.id.updateFormLayout);
        updateId = findViewById(R.id.update_id);
        updateDesignationCode = findViewById(R.id.update_designation_code);
        updateDesignationName = findViewById(R.id.update_designation_name);
        updateLevel = findViewById(R.id.update_level);
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
            addDesignation();
        }
        else if(v.getId() == R.id.btnUpdate) {
            updateDesignation();
        }
    }

    private void addDesignation() {
        Designation designation = new Designation();

        String codeStr = designationCode.getText().toString();
        if(!codeStr.isEmpty()) {
            designation.setDesignationCode(Long.valueOf(codeStr));
        }

        String nameStr = designationName.getText().toString();
        designation.setDesignationName(nameStr.isEmpty() ? null : nameStr);

        String levelStr = level.getText().toString();
        if(!levelStr.isEmpty()) {
            designation.setLevel(Integer.valueOf(levelStr));
        }

        RetrofitService retrofitService = new RetrofitService();
        DesignationApi designationApi = retrofitService.getRetrofit().create(DesignationApi.class);
        designationApi.save(designation).enqueue(new Callback<Designation>() {
            @Override
            public void onResponse(Call<Designation> call, Response<Designation> response) {
                if(response.isSuccessful()) {
                    Toast.makeText(DesignationActivity.this, "Designation added!", Toast.LENGTH_SHORT).show();
                    refreshTable();
                    clearAddForm();
                    hideUpdateForm();
                }
            }

            @Override
            public void onFailure(Call<Designation> call, Throwable t) {
                Toast.makeText(DesignationActivity.this, "Failed to add designation", Toast.LENGTH_SHORT).show();
            }
        });
    }

    private void updateDesignation() {
        Designation designation = new Designation();
        designation.setId(Long.valueOf(updateId.getText().toString()));

        String updateCodeStr = updateDesignationCode.getText().toString();
        if(!updateCodeStr.isEmpty()) {
            designation.setDesignationCode(Long.valueOf(updateCodeStr));
        }

        String updateNameStr = updateDesignationName.getText().toString();
        designation.setDesignationName(updateNameStr.isEmpty() ? null : updateNameStr);

        String updateLevelStr = updateLevel.getText().toString();
        if(!updateLevelStr.isEmpty()) {
            designation.setLevel(Integer.valueOf(updateLevelStr));
        }

        RetrofitService retrofitService = new RetrofitService();
        DesignationApi designationApi = retrofitService.getRetrofit().create(DesignationApi.class);
        designationApi.update(designation.getId(), designation).enqueue(new Callback<Designation>() {
            @Override
            public void onResponse(Call<Designation> call, Response<Designation> response) {
                if(response.isSuccessful()) {
                    Toast.makeText(DesignationActivity.this, "Designation updated!", Toast.LENGTH_SHORT).show();
                    refreshTable();
                    hideUpdateForm();
                    clearUpdateForm();
                }
            }

            @Override
            public void onFailure(Call<Designation> call, Throwable t) {
                Toast.makeText(DesignationActivity.this, "Failed to update designation", Toast.LENGTH_SHORT).show();
            }
        });
    }

    public void addHeaders() {
        TableLayout tl = findViewById(R.id.table);
        TableRow tr = new TableRow(this);
        tr.setLayoutParams(getLayoutParams());

        tr.addView(getTextView(0, "Code", Color.WHITE, Typeface.BOLD, Color.BLUE));
        tr.addView(getTextView(0, "Name", Color.WHITE, Typeface.BOLD, Color.BLUE));
//        tr.addView(getTextView(0, "Level", Color.WHITE, Typeface.BOLD, Color.BLUE));
        tr.addView(getTextView(0, "Edit", Color.WHITE, Typeface.BOLD, Color.BLUE));
        tr.addView(getTextView(0, "Delete", Color.WHITE, Typeface.BOLD, Color.BLUE));

        tl.addView(tr, getTblLayoutParams());
    }

    public void addData() {
        TableLayout tl = findViewById(R.id.table);
        RetrofitService retrofitService = new RetrofitService();
        DesignationApi designationApi = retrofitService.getRetrofit().create(DesignationApi.class);

        designationApi.getAllDesignations().enqueue(new Callback<List<Designation>>() {
            @Override
            public void onResponse(Call<List<Designation>> call, Response<List<Designation>> response) {
                if(response.isSuccessful()) {
                    List<Designation> designations = response.body();
                    for (Designation designation : designations) {
                        TableRow tr = new TableRow(DesignationActivity.this);
                        tr.setLayoutParams(getLayoutParams());

                        String code = designation.getDesignationCode() != null ?
                                String.valueOf(designation.getDesignationCode()) : "";
                        String name = designation.getDesignationName() != null ?
                                designation.getDesignationName() : "";
//                        String level = designation.getLevel() != null ?
//                                String.valueOf(designation.getLevel()) : "";

                        tr.addView(getTextView(0, code, Color.BLACK, Typeface.BOLD, Color.WHITE));
                        tr.addView(getTextView(0, name, Color.BLACK, Typeface.BOLD, Color.WHITE));
//                        tr.addView(getTextView(0, level, Color.BLACK, Typeface.BOLD, Color.WHITE));

                        TextView editBtn = getTextView(designation.getId().intValue(), "Edit",
                                Color.BLUE, Typeface.BOLD, Color.WHITE);
                        editBtn.setOnClickListener(v -> showUpdateForm(designation));
                        tr.addView(editBtn);

                        TextView deleteBtn = getTextView(designation.getId().intValue(), "Delete",
                                Color.RED, Typeface.BOLD, Color.WHITE);
                        deleteBtn.setOnClickListener(v -> showDeleteDialog(designation.getId(), tr));
                        tr.addView(deleteBtn);

                        tl.addView(tr, getTblLayoutParams());
                    }
                }
            }

            @Override
            public void onFailure(Call<List<Designation>> call, Throwable t) {
                Toast.makeText(DesignationActivity.this, "Failed to load designations", Toast.LENGTH_SHORT).show();
            }
        });
    }

    private void showUpdateForm(Designation designation) {
        updateId.setText(String.valueOf(designation.getId()));

        updateDesignationCode.setText(designation.getDesignationCode() != null ?
                String.valueOf(designation.getDesignationCode()) : "");
        updateDesignationName.setText(designation.getDesignationName() != null ?
                designation.getDesignationName() : "");
        updateLevel.setText(designation.getLevel() != null ?
                String.valueOf(designation.getLevel()) : "");

        updateFormLayout.setVisibility(View.VISIBLE);
    }

    private void showDeleteDialog(Long designationId, TableRow row) {
        new AlertDialog.Builder(this)
                .setTitle("Delete Designation")
                .setMessage("Are you sure you want to delete this designation?")
                .setPositiveButton("Delete", (dialog, which) -> {
                    RetrofitService retrofitService = new RetrofitService();
                    DesignationApi designationApi = retrofitService.getRetrofit().create(DesignationApi.class);
                    designationApi.delete(designationId).enqueue(new Callback<Void>() {
                        @Override
                        public void onResponse(Call<Void> call, Response<Void> response) {
                            if(response.isSuccessful()) {
                                ((TableLayout)row.getParent()).removeView(row);
                                Toast.makeText(DesignationActivity.this, "Designation deleted", Toast.LENGTH_SHORT).show();
                                hideUpdateForm();
                            }
                        }

                        @Override
                        public void onFailure(Call<Void> call, Throwable t) {
                            Toast.makeText(DesignationActivity.this, "Failed to delete", Toast.LENGTH_SHORT).show();
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
        designationCode.setText("");
        designationName.setText("");
        level.setText("");
    }

    private void clearUpdateForm() {
        updateId.setText("");
        updateDesignationCode.setText("");
        updateDesignationName.setText("");
        updateLevel.setText("");
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