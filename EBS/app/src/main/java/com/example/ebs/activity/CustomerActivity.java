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
import com.example.ebs.api.CustomerApi;
import com.example.ebs.model.Customer;
import com.example.ebs.util.RetrofitService;

import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class CustomerActivity extends AppCompatActivity implements View.OnClickListener {

    EditText customerCode, customerName, contactPerson, phone, email, address, taxId, status;
    Button btnSave;

    LinearLayout updateFormLayout;
    EditText updateId, updateCustomerCode, updateCustomerName, updateContactPerson,
            updatePhone, updateEmail, updateAddress, updateTaxId, updateStatus;
    Button btnUpdate;

    Button btnHome;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        setContentView(R.layout.activity_customer);
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.mainLayout), (v, insets) -> {
            Insets systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars());
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom);
            return insets;
        });

        customerCode = findViewById(R.id.customer_code);
        customerName = findViewById(R.id.customer_name);
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
        updateCustomerCode = findViewById(R.id.update_customer_code);
        updateCustomerName = findViewById(R.id.update_customer_name);
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
            addCustomer();
        }
        else if(v.getId() == R.id.btnUpdate) {
            updateCustomer();
        }
    }

    private void addCustomer() {
        Customer customer = new Customer();

        String codeStr = customerCode.getText().toString();
        if(!codeStr.isEmpty()) {
            customer.setCustomerCode(Long.valueOf(codeStr));
        }

        customer.setCustomerName(customerName.getText().toString());
        customer.setContactPerson(contactPerson.getText().toString());
        customer.setPhone(phone.getText().toString());
        customer.setEmail(email.getText().toString());
        customer.setAddress(address.getText().toString());

        String taxIdStr = taxId.getText().toString();
        if(!taxIdStr.isEmpty()) {
            customer.setTaxId(Long.valueOf(taxIdStr));
        }

        customer.setStatus(status.getText().toString());

        RetrofitService retrofitService = new RetrofitService();
        CustomerApi customerApi = retrofitService.getRetrofit().create(CustomerApi.class);
        customerApi.save(customer).enqueue(new Callback<Customer>() {
            @Override
            public void onResponse(Call<Customer> call, Response<Customer> response) {
                if(response.isSuccessful()) {
                    Toast.makeText(CustomerActivity.this, "Customer added!", Toast.LENGTH_SHORT).show();
                    refreshTable();
                    clearAddForm();
                    hideUpdateForm();
                }
            }

            @Override
            public void onFailure(Call<Customer> call, Throwable t) {
                Toast.makeText(CustomerActivity.this, "Failed to add customer", Toast.LENGTH_SHORT).show();
            }
        });
    }

    private void updateCustomer() {
        Customer customer = new Customer();
        customer.setId(Long.valueOf(updateId.getText().toString()));

        String updateCodeStr = updateCustomerCode.getText().toString();
        if(!updateCodeStr.isEmpty()) {
            customer.setCustomerCode(Long.valueOf(updateCodeStr));
        }

        customer.setCustomerName(updateCustomerName.getText().toString());
        customer.setContactPerson(updateContactPerson.getText().toString());
        customer.setPhone(updatePhone.getText().toString());
        customer.setEmail(updateEmail.getText().toString());
        customer.setAddress(updateAddress.getText().toString());

        String updateTaxIdStr = updateTaxId.getText().toString();
        if(!updateTaxIdStr.isEmpty()) {
            customer.setTaxId(Long.valueOf(updateTaxIdStr));
        }

        customer.setStatus(updateStatus.getText().toString());

        RetrofitService retrofitService = new RetrofitService();
        CustomerApi customerApi = retrofitService.getRetrofit().create(CustomerApi.class);
        customerApi.update(customer.getId(), customer).enqueue(new Callback<Customer>() {
            @Override
            public void onResponse(Call<Customer> call, Response<Customer> response) {
                if(response.isSuccessful()) {
                    Toast.makeText(CustomerActivity.this, "Customer updated!", Toast.LENGTH_SHORT).show();
                    refreshTable();
                    hideUpdateForm();
                    clearUpdateForm();
                }
            }

            @Override
            public void onFailure(Call<Customer> call, Throwable t) {
                Toast.makeText(CustomerActivity.this, "Failed to update customer", Toast.LENGTH_SHORT).show();
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
        CustomerApi customerApi = retrofitService.getRetrofit().create(CustomerApi.class);

        customerApi.getAllCustomers().enqueue(new Callback<List<Customer>>() {
            @Override
            public void onResponse(Call<List<Customer>> call, Response<List<Customer>> response) {
                if(response.isSuccessful()) {
                    List<Customer> customers = response.body();
                    for (Customer customer : customers) {
                        TableRow tr = new TableRow(CustomerActivity.this);
                        tr.setLayoutParams(getLayoutParams());

                        String code = customer.getCustomerCode() != null ? String.valueOf(customer.getCustomerCode()) : "";
                        String name = customer.getCustomerName() != null ? customer.getCustomerName() : "";
                        String contact = customer.getContactPerson() != null ? customer.getContactPerson() : "";
                        String phone = customer.getPhone() != null ? customer.getPhone() : "";
                        String email = customer.getEmail() != null ? customer.getEmail() : "";
                        String address = customer.getAddress() != null ? customer.getAddress() : "";
                        String taxId = customer.getTaxId() != null ? String.valueOf(customer.getTaxId()) : "";
                        String status = customer.getStatus() != null ? customer.getStatus() : "";

                        tr.addView(getTextView(0, code, Color.BLACK, Typeface.BOLD, Color.WHITE));
                        tr.addView(getTextView(0, name, Color.BLACK, Typeface.BOLD, Color.WHITE));
                        tr.addView(getTextView(0, contact, Color.BLACK, Typeface.BOLD, Color.WHITE));
                        tr.addView(getTextView(0, phone, Color.BLACK, Typeface.BOLD, Color.WHITE));
                        tr.addView(getTextView(0, email, Color.BLACK, Typeface.BOLD, Color.WHITE));
                        tr.addView(getTextView(0, address, Color.BLACK, Typeface.BOLD, Color.WHITE));
                        tr.addView(getTextView(0, taxId, Color.BLACK, Typeface.BOLD, Color.WHITE));
                        tr.addView(getTextView(0, status, Color.BLACK, Typeface.BOLD, Color.WHITE));

                        TextView editBtn = getTextView(customer.getId().intValue(), "Edit",
                                Color.BLUE, Typeface.BOLD, Color.WHITE);
                        editBtn.setOnClickListener(v -> showUpdateForm(customer));
                        tr.addView(editBtn);

                        TextView deleteBtn = getTextView(customer.getId().intValue(), "Delete",
                                Color.RED, Typeface.BOLD, Color.WHITE);
                        deleteBtn.setOnClickListener(v -> showDeleteDialog(customer.getId(), tr));
                        tr.addView(deleteBtn);

                        tl.addView(tr, getTblLayoutParams());
                    }
                }
            }

            @Override
            public void onFailure(Call<List<Customer>> call, Throwable t) {
                Toast.makeText(CustomerActivity.this, "Failed to load customers", Toast.LENGTH_SHORT).show();
            }
        });
    }

    private void showUpdateForm(Customer customer) {
        updateId.setText(String.valueOf(customer.getId()));

        updateCustomerCode.setText(customer.getCustomerCode() != null ? String.valueOf(customer.getCustomerCode()) : "");
        updateCustomerName.setText(customer.getCustomerName() != null ? customer.getCustomerName() : "");
        updateContactPerson.setText(customer.getContactPerson() != null ? customer.getContactPerson() : "");
        updatePhone.setText(customer.getPhone() != null ? customer.getPhone() : "");
        updateEmail.setText(customer.getEmail() != null ? customer.getEmail() : "");
        updateAddress.setText(customer.getAddress() != null ? customer.getAddress() : "");
        updateTaxId.setText(customer.getTaxId() != null ? String.valueOf(customer.getTaxId()) : "");
        updateStatus.setText(customer.getStatus() != null ? customer.getStatus() : "");

        updateFormLayout.setVisibility(View.VISIBLE);
    }

    private void showDeleteDialog(Long customerId, TableRow row) {
        new AlertDialog.Builder(this)
                .setTitle("Delete Customer")
                .setMessage("Are you sure you want to delete this customer?")
                .setPositiveButton("Delete", (dialog, which) -> {
                    RetrofitService retrofitService = new RetrofitService();
                    CustomerApi customerApi = retrofitService.getRetrofit().create(CustomerApi.class);
                    customerApi.delete(customerId).enqueue(new Callback<Void>() {
                        @Override
                        public void onResponse(Call<Void> call, Response<Void> response) {
                            if(response.isSuccessful()) {
                                ((TableLayout)row.getParent()).removeView(row);
                                Toast.makeText(CustomerActivity.this, "Customer deleted", Toast.LENGTH_SHORT).show();
                                hideUpdateForm();
                            }
                        }

                        @Override
                        public void onFailure(Call<Void> call, Throwable t) {
                            Toast.makeText(CustomerActivity.this, "Failed to delete", Toast.LENGTH_SHORT).show();
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
        customerCode.setText("");
        customerName.setText("");
        contactPerson.setText("");
        phone.setText("");
        email.setText("");
        address.setText("");
        taxId.setText("");
        status.setText("");
    }

    private void clearUpdateForm() {
        updateId.setText("");
        updateCustomerCode.setText("");
        updateCustomerName.setText("");
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