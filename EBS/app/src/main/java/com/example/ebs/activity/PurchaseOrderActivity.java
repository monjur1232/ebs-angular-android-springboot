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
import com.example.ebs.api.PurchaseOrderApi;
import com.example.ebs.model.PurchaseOrder;
import com.example.ebs.util.RetrofitService;

import java.util.Calendar;
import java.util.List;
import java.util.Locale;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class PurchaseOrderActivity extends AppCompatActivity implements View.OnClickListener {

    // Form fields
    private EditText purchaseOrderCode, supplierCode, supplierName, orderDate, receivedDate;
    private EditText productCode, productName, unitPrice, purchaseQuantity, totalAmount;
    private EditText paymentStatus, status;
    private Button btnSave;

    // Update form fields
    private LinearLayout updateFormLayout;
    private EditText updateId, updatePurchaseOrderCode, updateSupplierCode, updateSupplierName;
    private EditText updateOrderDate, updateReceivedDate, updateProductCode, updateProductName;
    private EditText updateUnitPrice, updatePurchaseQuantity, updateTotalAmount;
    private EditText updatePaymentStatus, updateStatus;
    private Button btnUpdate;

    // Navigation
    private Button btnHome;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        setContentView(R.layout.activity_purchase_order);
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.mainLayout), (v, insets) -> {
            Insets systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars());
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom);
            return insets;
        });

        initializeViews();
        setupDatePickers();
        setupButtons();
        addHeaders();
        addData();
    }

    private void initializeViews() {
        // Initialize main form fields
        purchaseOrderCode = findViewById(R.id.purchase_order_code);
        supplierCode = findViewById(R.id.supplier_code);
        supplierName = findViewById(R.id.supplier_name);
        orderDate = findViewById(R.id.order_date);
        receivedDate = findViewById(R.id.received_date);
        productCode = findViewById(R.id.product_code);
        productName = findViewById(R.id.product_name);
        unitPrice = findViewById(R.id.unit_price);
        purchaseQuantity = findViewById(R.id.purchase_quantity);
        totalAmount = findViewById(R.id.total_amount);
        paymentStatus = findViewById(R.id.payment_status);
        status = findViewById(R.id.status);
        btnSave = findViewById(R.id.btnSave);

        // Initialize update form fields
        updateFormLayout = findViewById(R.id.updateFormLayout);
        updateId = findViewById(R.id.update_id);
        updatePurchaseOrderCode = findViewById(R.id.update_purchase_order_code);
        updateSupplierCode = findViewById(R.id.update_supplier_code);
        updateSupplierName = findViewById(R.id.update_supplier_name);
        updateOrderDate = findViewById(R.id.update_order_date);
        updateReceivedDate = findViewById(R.id.update_received_date);
        updateProductCode = findViewById(R.id.update_product_code);
        updateProductName = findViewById(R.id.update_product_name);
        updateUnitPrice = findViewById(R.id.update_unit_price);
        updatePurchaseQuantity = findViewById(R.id.update_purchase_quantity);
        updateTotalAmount = findViewById(R.id.update_total_amount);
        updatePaymentStatus = findViewById(R.id.update_payment_status);
        updateStatus = findViewById(R.id.update_status);
        btnUpdate = findViewById(R.id.btnUpdate);

        // Initialize navigation button
        btnHome = findViewById(R.id.btnHome);
    }

    private void setupDatePickers() {
        orderDate.setOnClickListener(v -> showDatePickerDialog(orderDate));
        receivedDate.setOnClickListener(v -> showDatePickerDialog(receivedDate));
        updateOrderDate.setOnClickListener(v -> showDatePickerDialog(updateOrderDate));
        updateReceivedDate.setOnClickListener(v -> showDatePickerDialog(updateReceivedDate));
    }

    private void setupButtons() {
        btnSave.setOnClickListener(this);
        btnUpdate.setOnClickListener(this);
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
        if (v.getId() == R.id.btnSave) {
            addPurchaseOrder();
        } else if (v.getId() == R.id.btnUpdate) {
            updatePurchaseOrder();
        }
    }

    private void addPurchaseOrder() {
        PurchaseOrder po = new PurchaseOrder();

        // Set values from form fields
        try {
            if (!purchaseOrderCode.getText().toString().isEmpty()) {
                po.setPurchaseOrderCode(Long.parseLong(purchaseOrderCode.getText().toString()));
            }
            if (!supplierCode.getText().toString().isEmpty()) {
                po.setSupplierCode(Long.parseLong(supplierCode.getText().toString()));
            }
            po.setSupplierName(supplierName.getText().toString());
            po.setOrderDate(orderDate.getText().toString());
            po.setReceivedDate(receivedDate.getText().toString());

            if (!productCode.getText().toString().isEmpty()) {
                po.setProductCode(Long.parseLong(productCode.getText().toString()));
            }
            po.setProductName(productName.getText().toString());

            if (!unitPrice.getText().toString().isEmpty()) {
                po.setUnitPrice(Double.parseDouble(unitPrice.getText().toString()));
            }
            if (!purchaseQuantity.getText().toString().isEmpty()) {
                po.setPurchaseQuantity(Integer.parseInt(purchaseQuantity.getText().toString()));
            }
            if (!totalAmount.getText().toString().isEmpty()) {
                po.setTotalAmount(Double.parseDouble(totalAmount.getText().toString()));
            }

            po.setPaymentStatus(paymentStatus.getText().toString());
            po.setStatus(status.getText().toString());

            RetrofitService retrofitService = new RetrofitService();
            PurchaseOrderApi poApi = retrofitService.getRetrofit().create(PurchaseOrderApi.class);

            poApi.save(po).enqueue(new Callback<PurchaseOrder>() {
                @Override
                public void onResponse(Call<PurchaseOrder> call, Response<PurchaseOrder> response) {
                    if (response.isSuccessful()) {
                        Toast.makeText(PurchaseOrderActivity.this,
                                "Purchase Order added!", Toast.LENGTH_SHORT).show();
                        refreshTable();
                        clearAddForm();
                        hideUpdateForm();
                    }
                }

                @Override
                public void onFailure(Call<PurchaseOrder> call, Throwable t) {
                    Toast.makeText(PurchaseOrderActivity.this,
                            "Failed to add purchase order", Toast.LENGTH_SHORT).show();
                }
            });
        } catch (NumberFormatException e) {
            Toast.makeText(this, "Please enter valid numbers", Toast.LENGTH_SHORT).show();
        }
    }

    private void updatePurchaseOrder() {
        PurchaseOrder po = new PurchaseOrder();
        po.setId(Long.parseLong(updateId.getText().toString()));

        try {
            if (!updatePurchaseOrderCode.getText().toString().isEmpty()) {
                po.setPurchaseOrderCode(Long.parseLong(updatePurchaseOrderCode.getText().toString()));
            }
            if (!updateSupplierCode.getText().toString().isEmpty()) {
                po.setSupplierCode(Long.parseLong(updateSupplierCode.getText().toString()));
            }
            po.setSupplierName(updateSupplierName.getText().toString());
            po.setOrderDate(updateOrderDate.getText().toString());
            po.setReceivedDate(updateReceivedDate.getText().toString());

            if (!updateProductCode.getText().toString().isEmpty()) {
                po.setProductCode(Long.parseLong(updateProductCode.getText().toString()));
            }
            po.setProductName(updateProductName.getText().toString());

            if (!updateUnitPrice.getText().toString().isEmpty()) {
                po.setUnitPrice(Double.parseDouble(updateUnitPrice.getText().toString()));
            }
            if (!updatePurchaseQuantity.getText().toString().isEmpty()) {
                po.setPurchaseQuantity(Integer.parseInt(updatePurchaseQuantity.getText().toString()));
            }
            if (!updateTotalAmount.getText().toString().isEmpty()) {
                po.setTotalAmount(Double.parseDouble(updateTotalAmount.getText().toString()));
            }

            po.setPaymentStatus(updatePaymentStatus.getText().toString());
            po.setStatus(updateStatus.getText().toString());

            RetrofitService retrofitService = new RetrofitService();
            PurchaseOrderApi poApi = retrofitService.getRetrofit().create(PurchaseOrderApi.class);

            poApi.update(po.getId(), po).enqueue(new Callback<PurchaseOrder>() {
                @Override
                public void onResponse(Call<PurchaseOrder> call, Response<PurchaseOrder> response) {
                    if (response.isSuccessful()) {
                        Toast.makeText(PurchaseOrderActivity.this,
                                "Purchase Order updated!", Toast.LENGTH_SHORT).show();
                        refreshTable();
                        hideUpdateForm();
                        clearUpdateForm();
                    }
                }

                @Override
                public void onFailure(Call<PurchaseOrder> call, Throwable t) {
                    Toast.makeText(PurchaseOrderActivity.this,
                            "Failed to update purchase order", Toast.LENGTH_SHORT).show();
                }
            });
        } catch (NumberFormatException e) {
            Toast.makeText(this, "Please enter valid numbers", Toast.LENGTH_SHORT).show();
        }
    }

    public void addHeaders() {
        TableLayout tl = findViewById(R.id.table);
        TableRow tr = new TableRow(this);
        tr.setLayoutParams(getLayoutParams());

        // Add headers for all purchase order fields
        tr.addView(getTextView(0, "PO Code", Color.WHITE, Typeface.BOLD, Color.BLUE));
        tr.addView(getTextView(0, "Supplier Code", Color.WHITE, Typeface.BOLD, Color.BLUE));
        tr.addView(getTextView(0, "Supplier", Color.WHITE, Typeface.BOLD, Color.BLUE));
        tr.addView(getTextView(0, "Order Date", Color.WHITE, Typeface.BOLD, Color.BLUE));
        tr.addView(getTextView(0, "Received Date", Color.WHITE, Typeface.BOLD, Color.BLUE));
        tr.addView(getTextView(0, "Product Code", Color.WHITE, Typeface.BOLD, Color.BLUE));
        tr.addView(getTextView(0, "Product", Color.WHITE, Typeface.BOLD, Color.BLUE));
        tr.addView(getTextView(0, "Unit Price", Color.WHITE, Typeface.BOLD, Color.BLUE));
        tr.addView(getTextView(0, "Qty", Color.WHITE, Typeface.BOLD, Color.BLUE));
        tr.addView(getTextView(0, "Total", Color.WHITE, Typeface.BOLD, Color.BLUE));
        tr.addView(getTextView(0, "Payment Status", Color.WHITE, Typeface.BOLD, Color.BLUE));
        tr.addView(getTextView(0, "Status", Color.WHITE, Typeface.BOLD, Color.BLUE));
        tr.addView(getTextView(0, "Edit", Color.WHITE, Typeface.BOLD, Color.BLUE));
        tr.addView(getTextView(0, "Delete", Color.WHITE, Typeface.BOLD, Color.BLUE));

        tl.addView(tr, getTblLayoutParams());
    }

    public void addData() {
        TableLayout tl = findViewById(R.id.table);
        RetrofitService retrofitService = new RetrofitService();
        PurchaseOrderApi poApi = retrofitService.getRetrofit().create(PurchaseOrderApi.class);

        poApi.getAllPurchaseOrders().enqueue(new Callback<List<PurchaseOrder>>() {
            @Override
            public void onResponse(Call<List<PurchaseOrder>> call, Response<List<PurchaseOrder>> response) {
                if (response.isSuccessful()) {
                    List<PurchaseOrder> purchaseOrders = response.body();
                    if (purchaseOrders != null) {
                        for (PurchaseOrder po : purchaseOrders) {
                            addTableRow(po, tl);
                        }
                    }
                }
            }

            @Override
            public void onFailure(Call<List<PurchaseOrder>> call, Throwable t) {
                Toast.makeText(PurchaseOrderActivity.this,
                        "Failed to load purchase orders", Toast.LENGTH_SHORT).show();
            }
        });
    }

    private void addTableRow(PurchaseOrder po, TableLayout tl) {
        TableRow tr = new TableRow(this);
        tr.setLayoutParams(getLayoutParams());

        // Add data cells
        tr.addView(getTextView(0, po.getPurchaseOrderCode() != null ?
                        String.valueOf(po.getPurchaseOrderCode()) : "",
                Color.BLACK, Typeface.NORMAL, Color.WHITE));

        tr.addView(getTextView(0, po.getSupplierCode() != null ?
                        String.valueOf(po.getSupplierCode()) : "",
                Color.BLACK, Typeface.NORMAL, Color.WHITE));

        tr.addView(getTextView(0, po.getSupplierName() != null ?
                        po.getSupplierName() : "",
                Color.BLACK, Typeface.NORMAL, Color.WHITE));

        tr.addView(getTextView(0, po.getOrderDate() != null ?
                        po.getOrderDate() : "",
                Color.BLACK, Typeface.NORMAL, Color.WHITE));

        tr.addView(getTextView(0, po.getReceivedDate() != null ?
                        po.getReceivedDate() : "",
                Color.BLACK, Typeface.NORMAL, Color.WHITE));

        tr.addView(getTextView(0, po.getProductCode() != null ?
                        String.valueOf(po.getProductCode()) : "",
                Color.BLACK, Typeface.NORMAL, Color.WHITE));

        tr.addView(getTextView(0, po.getProductName() != null ?
                        po.getProductName() : "",
                Color.BLACK, Typeface.NORMAL, Color.WHITE));

        tr.addView(getTextView(0, po.getUnitPrice() != null ?
                        String.valueOf(po.getUnitPrice()) : "",
                Color.BLACK, Typeface.NORMAL, Color.WHITE));

        tr.addView(getTextView(0, po.getPurchaseQuantity() != null ?
                        String.valueOf(po.getPurchaseQuantity()) : "",
                Color.BLACK, Typeface.NORMAL, Color.WHITE));

        tr.addView(getTextView(0, po.getTotalAmount() != null ?
                        String.valueOf(po.getTotalAmount()) : "",
                Color.BLACK, Typeface.NORMAL, Color.WHITE));

        tr.addView(getTextView(0, po.getPaymentStatus() != null ?
                        po.getPaymentStatus() : "",
                Color.BLACK, Typeface.NORMAL, Color.WHITE));

        tr.addView(getTextView(0, po.getStatus() != null ?
                        po.getStatus() : "",
                Color.BLACK, Typeface.NORMAL, Color.WHITE));

        // Add edit button
        TextView editBtn = getTextView(po.getId().intValue(), "Edit",
                Color.BLUE, Typeface.BOLD, Color.WHITE);
        editBtn.setOnClickListener(v -> showUpdateForm(po));
        tr.addView(editBtn);

        // Add delete button
        TextView deleteBtn = getTextView(po.getId().intValue(), "Delete",
                Color.RED, Typeface.BOLD, Color.WHITE);
        deleteBtn.setOnClickListener(v -> showDeleteDialog(po.getId(), tr));
        tr.addView(deleteBtn);

        tl.addView(tr, getTblLayoutParams());
    }

    private void showUpdateForm(PurchaseOrder po) {
        updateId.setText(String.valueOf(po.getId()));
        updatePurchaseOrderCode.setText(po.getPurchaseOrderCode() != null ?
                String.valueOf(po.getPurchaseOrderCode()) : "");
        updateSupplierCode.setText(po.getSupplierCode() != null ?
                String.valueOf(po.getSupplierCode()) : "");
        updateSupplierName.setText(po.getSupplierName() != null ? po.getSupplierName() : "");
        updateOrderDate.setText(po.getOrderDate() != null ? po.getOrderDate() : "");
        updateReceivedDate.setText(po.getReceivedDate() != null ? po.getReceivedDate() : "");
        updateProductCode.setText(po.getProductCode() != null ?
                String.valueOf(po.getProductCode()) : "");
        updateProductName.setText(po.getProductName() != null ? po.getProductName() : "");
        updateUnitPrice.setText(po.getUnitPrice() != null ?
                String.valueOf(po.getUnitPrice()) : "");
        updatePurchaseQuantity.setText(po.getPurchaseQuantity() != null ?
                String.valueOf(po.getPurchaseQuantity()) : "");
        updateTotalAmount.setText(po.getTotalAmount() != null ?
                String.valueOf(po.getTotalAmount()) : "");
        updatePaymentStatus.setText(po.getPaymentStatus() != null ? po.getPaymentStatus() : "");
        updateStatus.setText(po.getStatus() != null ? po.getStatus() : "");

        updateFormLayout.setVisibility(View.VISIBLE);
    }

    private void showDeleteDialog(Long poId, TableRow row) {
        new AlertDialog.Builder(this)
                .setTitle("Delete Purchase Order")
                .setMessage("Are you sure you want to delete this purchase order?")
                .setPositiveButton("Delete", (dialog, which) -> deletePurchaseOrder(poId, row))
                .setNegativeButton("Cancel", (dialog, which) ->
                        Toast.makeText(this, "Cancelled", Toast.LENGTH_SHORT).show())
                .show();
    }

    private void deletePurchaseOrder(Long poId, TableRow row) {
        RetrofitService retrofitService = new RetrofitService();
        PurchaseOrderApi poApi = retrofitService.getRetrofit().create(PurchaseOrderApi.class);

        poApi.delete(poId).enqueue(new Callback<Void>() {
            @Override
            public void onResponse(Call<Void> call, Response<Void> response) {
                if (response.isSuccessful()) {
                    ((TableLayout) row.getParent()).removeView(row);
                    Toast.makeText(PurchaseOrderActivity.this,
                            "Purchase Order deleted", Toast.LENGTH_SHORT).show();
                    hideUpdateForm();
                }
            }

            @Override
            public void onFailure(Call<Void> call, Throwable t) {
                Toast.makeText(PurchaseOrderActivity.this,
                        "Failed to delete", Toast.LENGTH_SHORT).show();
            }
        });
    }

    private void hideUpdateForm() {
        updateFormLayout.setVisibility(View.GONE);
    }

    private void clearAddForm() {
        purchaseOrderCode.setText("");
        supplierCode.setText("");
        supplierName.setText("");
        orderDate.setText("");
        receivedDate.setText("");
        productCode.setText("");
        productName.setText("");
        unitPrice.setText("");
        purchaseQuantity.setText("");
        totalAmount.setText("");
        paymentStatus.setText("");
        status.setText("");
    }

    private void clearUpdateForm() {
        updateId.setText("");
        updatePurchaseOrderCode.setText("");
        updateSupplierCode.setText("");
        updateSupplierName.setText("");
        updateOrderDate.setText("");
        updateReceivedDate.setText("");
        updateProductCode.setText("");
        updateProductName.setText("");
        updateUnitPrice.setText("");
        updatePurchaseQuantity.setText("");
        updateTotalAmount.setText("");
        updatePaymentStatus.setText("");
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