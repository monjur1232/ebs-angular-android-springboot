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
import com.example.ebs.api.SupplierApi;
import com.example.ebs.model.Supplier;
import com.example.ebs.util.RetrofitService;

import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class SupplierActivity extends AppCompatActivity implements View.OnClickListener {

    EditText supplierCode, supplierName, contactPerson, phone, email, address, taxId, status;
    Button btnSave;

    LinearLayout updateFormLayout;
    EditText updateId, updateSupplierCode, updateSupplierName, updateContactPerson,
            updatePhone, updateEmail, updateAddress, updateTaxId, updateStatus;
    Button btnUpdate;

    Button btnHome;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        setContentView(R.layout.activity_supplier);
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.mainLayout), (v, insets) -> {
            Insets systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars());
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom);
            return insets;
        });

        supplierCode = findViewById(R.id.supplier_code);
        supplierName = findViewById(R.id.supplier_name);
        contactPerson = findViewById(R.id.contact_person);
        phone = findViewById(R.id.phone);
        email = findViewById(R.id.email);
        address = findViewById(R.id.address);
        taxId = findViewById(R.id.tax_id);
        status = findViewById(R.id.status);
        btnSave = findViewById(R.id.btnSave);
        btnSave.setOnClickListener(this);

        updateFormLayout = findViewById(R.id.updateFormLayout);
        updateId = findViewById(R.id.update_id);
        updateSupplierCode = findViewById(R.id.update_supplier_code);
        updateSupplierName = findViewById(R.id.update_supplier_name);
        updateContactPerson = findViewById(R.id.update_contact_person);
        updatePhone = findViewById(R.id.update_phone);
        updateEmail = findViewById(R.id.update_email);
        updateAddress = findViewById(R.id.update_address);
        updateTaxId = findViewById(R.id.update_tax_id);
        updateStatus = findViewById(R.id.update_status);
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
            addSupplier();
        }
        else if(v.getId() == R.id.btnUpdate) {
            updateSupplier();
        }
    }

    private void addSupplier() {
        Supplier supplier = new Supplier();

        String codeStr = supplierCode.getText().toString();
        if(!codeStr.isEmpty()) {
            supplier.setSupplierCode(Long.valueOf(codeStr));
        }

        supplier.setSupplierName(supplierName.getText().toString());
        supplier.setContactPerson(contactPerson.getText().toString());
        supplier.setPhone(phone.getText().toString());
        supplier.setEmail(email.getText().toString());
        supplier.setAddress(address.getText().toString());

        String taxIdStr = taxId.getText().toString();
        if(!taxIdStr.isEmpty()) {
            supplier.setTaxId(Long.valueOf(taxIdStr));
        }

        supplier.setStatus(status.getText().toString());

        RetrofitService retrofitService = new RetrofitService();
        SupplierApi supplierApi = retrofitService.getRetrofit().create(SupplierApi.class);
        supplierApi.save(supplier).enqueue(new Callback<Supplier>() {
            @Override
            public void onResponse(Call<Supplier> call, Response<Supplier> response) {
                if(response.isSuccessful()) {
                    Toast.makeText(SupplierActivity.this, "Supplier added!", Toast.LENGTH_SHORT).show();
                    refreshTable();
                    clearAddForm();
                    hideUpdateForm();
                }
            }

            @Override
            public void onFailure(Call<Supplier> call, Throwable t) {
                Toast.makeText(SupplierActivity.this, "Failed to add supplier", Toast.LENGTH_SHORT).show();
            }
        });
    }

    private void updateSupplier() {
        Supplier supplier = new Supplier();
        supplier.setId(Long.valueOf(updateId.getText().toString()));

        String updateCodeStr = updateSupplierCode.getText().toString();
        if(!updateCodeStr.isEmpty()) {
            supplier.setSupplierCode(Long.valueOf(updateCodeStr));
        }

        supplier.setSupplierName(updateSupplierName.getText().toString());
        supplier.setContactPerson(updateContactPerson.getText().toString());
        supplier.setPhone(updatePhone.getText().toString());
        supplier.setEmail(updateEmail.getText().toString());
        supplier.setAddress(updateAddress.getText().toString());

        String updateTaxIdStr = updateTaxId.getText().toString();
        if(!updateTaxIdStr.isEmpty()) {
            supplier.setTaxId(Long.valueOf(updateTaxIdStr));
        }

        supplier.setStatus(updateStatus.getText().toString());

        RetrofitService retrofitService = new RetrofitService();
        SupplierApi supplierApi = retrofitService.getRetrofit().create(SupplierApi.class);
        supplierApi.update(supplier.getId(), supplier).enqueue(new Callback<Supplier>() {
            @Override
            public void onResponse(Call<Supplier> call, Response<Supplier> response) {
                if(response.isSuccessful()) {
                    Toast.makeText(SupplierActivity.this, "Supplier updated!", Toast.LENGTH_SHORT).show();
                    refreshTable();
                    hideUpdateForm();
                    clearUpdateForm();
                }
            }

            @Override
            public void onFailure(Call<Supplier> call, Throwable t) {
                Toast.makeText(SupplierActivity.this, "Failed to update supplier", Toast.LENGTH_SHORT).show();
            }
        });
    }

    public void addHeaders() {
        TableLayout tl = findViewById(R.id.table);
        TableRow tr = new TableRow(this);
        tr.setLayoutParams(getLayoutParams());

        tr.addView(getTextView(0, "Code", Color.WHITE, Typeface.BOLD, Color.BLUE));
        tr.addView(getTextView(0, "Name", Color.WHITE, Typeface.BOLD, Color.BLUE));
        tr.addView(getTextView(0, "Contact Person", Color.WHITE, Typeface.BOLD, Color.BLUE));
        tr.addView(getTextView(0, "Phone", Color.WHITE, Typeface.BOLD, Color.BLUE));
        tr.addView(getTextView(0, "Email", Color.WHITE, Typeface.BOLD, Color.BLUE));
        tr.addView(getTextView(0, "Address", Color.WHITE, Typeface.BOLD, Color.BLUE));
        tr.addView(getTextView(0, "Tax ID", Color.WHITE, Typeface.BOLD, Color.BLUE));
        tr.addView(getTextView(0, "Status", Color.WHITE, Typeface.BOLD, Color.BLUE));
        tr.addView(getTextView(0, "Edit", Color.WHITE, Typeface.BOLD, Color.BLUE));
        tr.addView(getTextView(0, "Delete", Color.WHITE, Typeface.BOLD, Color.BLUE));

        tl.addView(tr, getTblLayoutParams());
    }

    public void addData() {
        TableLayout tl = findViewById(R.id.table);
        RetrofitService retrofitService = new RetrofitService();
        SupplierApi supplierApi = retrofitService.getRetrofit().create(SupplierApi.class);

        supplierApi.getAllSuppliers().enqueue(new Callback<List<Supplier>>() {
            @Override
            public void onResponse(Call<List<Supplier>> call, Response<List<Supplier>> response) {
                if(response.isSuccessful()) {
                    List<Supplier> suppliers = response.body();
                    for (Supplier supplier : suppliers) {
                        TableRow tr = new TableRow(SupplierActivity.this);
                        tr.setLayoutParams(getLayoutParams());

                        String code = supplier.getSupplierCode() != null ? String.valueOf(supplier.getSupplierCode()) : "";
                        String name = supplier.getSupplierName() != null ? supplier.getSupplierName() : "";
                        String contact = supplier.getContactPerson() != null ? supplier.getContactPerson() : "";
                        String phone = supplier.getPhone() != null ? supplier.getPhone() : "";
                        String email = supplier.getEmail() != null ? supplier.getEmail() : "";
                        String address = supplier.getAddress() != null ? supplier.getAddress() : "";
                        String taxId = supplier.getTaxId() != null ? String.valueOf(supplier.getTaxId()) : "";
                        String status = supplier.getStatus() != null ? supplier.getStatus() : "";

                        tr.addView(getTextView(0, code, Color.BLACK, Typeface.BOLD, Color.WHITE));
                        tr.addView(getTextView(0, name, Color.BLACK, Typeface.BOLD, Color.WHITE));
                        tr.addView(getTextView(0, contact, Color.BLACK, Typeface.BOLD, Color.WHITE));
                        tr.addView(getTextView(0, phone, Color.BLACK, Typeface.BOLD, Color.WHITE));
                        tr.addView(getTextView(0, email, Color.BLACK, Typeface.BOLD, Color.WHITE));
                        tr.addView(getTextView(0, address, Color.BLACK, Typeface.BOLD, Color.WHITE));
                        tr.addView(getTextView(0, taxId, Color.BLACK, Typeface.BOLD, Color.WHITE));
                        tr.addView(getTextView(0, status, Color.BLACK, Typeface.BOLD, Color.WHITE));

                        TextView editBtn = getTextView(supplier.getId().intValue(), "Edit",
                                Color.BLUE, Typeface.BOLD, Color.WHITE);
                        editBtn.setOnClickListener(v -> showUpdateForm(supplier));
                        tr.addView(editBtn);

                        TextView deleteBtn = getTextView(supplier.getId().intValue(), "Delete",
                                Color.RED, Typeface.BOLD, Color.WHITE);
                        deleteBtn.setOnClickListener(v -> showDeleteDialog(supplier.getId(), tr));
                        tr.addView(deleteBtn);

                        tl.addView(tr, getTblLayoutParams());
                    }
                }
            }

            @Override
            public void onFailure(Call<List<Supplier>> call, Throwable t) {
                Toast.makeText(SupplierActivity.this, "Failed to load suppliers", Toast.LENGTH_SHORT).show();
            }
        });
    }

    private void showUpdateForm(Supplier supplier) {
        updateId.setText(String.valueOf(supplier.getId()));

        updateSupplierCode.setText(supplier.getSupplierCode() != null ? String.valueOf(supplier.getSupplierCode()) : "");
        updateSupplierName.setText(supplier.getSupplierName() != null ? supplier.getSupplierName() : "");
        updateContactPerson.setText(supplier.getContactPerson() != null ? supplier.getContactPerson() : "");
        updatePhone.setText(supplier.getPhone() != null ? supplier.getPhone() : "");
        updateEmail.setText(supplier.getEmail() != null ? supplier.getEmail() : "");
        updateAddress.setText(supplier.getAddress() != null ? supplier.getAddress() : "");
        updateTaxId.setText(supplier.getTaxId() != null ? String.valueOf(supplier.getTaxId()) : "");
        updateStatus.setText(supplier.getStatus() != null ? supplier.getStatus() : "");

        updateFormLayout.setVisibility(View.VISIBLE);
    }

    private void showDeleteDialog(Long supplierId, TableRow row) {
        new AlertDialog.Builder(this)
                .setTitle("Delete Supplier")
                .setMessage("Are you sure you want to delete this supplier?")
                .setPositiveButton("Delete", (dialog, which) -> {
                    RetrofitService retrofitService = new RetrofitService();
                    SupplierApi supplierApi = retrofitService.getRetrofit().create(SupplierApi.class);
                    supplierApi.delete(supplierId).enqueue(new Callback<Void>() {
                        @Override
                        public void onResponse(Call<Void> call, Response<Void> response) {
                            if(response.isSuccessful()) {
                                ((TableLayout)row.getParent()).removeView(row);
                                Toast.makeText(SupplierActivity.this, "Supplier deleted", Toast.LENGTH_SHORT).show();
                                hideUpdateForm();
                            }
                        }

                        @Override
                        public void onFailure(Call<Void> call, Throwable t) {
                            Toast.makeText(SupplierActivity.this, "Failed to delete", Toast.LENGTH_SHORT).show();
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
        supplierCode.setText("");
        supplierName.setText("");
        contactPerson.setText("");
        phone.setText("");
        email.setText("");
        address.setText("");
        taxId.setText("");
        status.setText("");
    }

    private void clearUpdateForm() {
        updateId.setText("");
        updateSupplierCode.setText("");
        updateSupplierName.setText("");
        updateContactPerson.setText("");
        updatePhone.setText("");
        updateEmail.setText("");
        updateAddress.setText("");
        updateTaxId.setText("");
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
}