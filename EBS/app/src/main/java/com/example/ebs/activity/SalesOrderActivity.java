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
import com.example.ebs.api.SalesOrderApi;
import com.example.ebs.model.SalesOrder;
import com.example.ebs.util.RetrofitService;

import java.util.Calendar;
import java.util.List;
import java.util.Locale;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class SalesOrderActivity extends AppCompatActivity implements View.OnClickListener {

    // Form fields
    private EditText salesOrderCode, customerCode, customerName, orderDate, deliveryDate;
    private EditText productCode, productName, unitPrice, salesQuantity, totalAmount;
    private EditText paymentStatus, status;
    private Button btnSave;

    // Update form fields
    private LinearLayout updateFormLayout;
    private EditText updateId, updateSalesOrderCode, updateCustomerCode, updateCustomerName;
    private EditText updateOrderDate, updateDeliveryDate, updateProductCode, updateProductName;
    private EditText updateUnitPrice, updateSalesQuantity, updateTotalAmount;
    private EditText updatePaymentStatus, updateStatus;
    private Button btnUpdate;

    // Navigation
    private Button btnHome;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        setContentView(R.layout.activity_sales_order);
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
        // Main form fields
        salesOrderCode = findViewById(R.id.sales_order_code);
        customerCode = findViewById(R.id.customer_code);
        customerName = findViewById(R.id.customer_name);
        orderDate = findViewById(R.id.order_date);
        deliveryDate = findViewById(R.id.delivery_date);
        productCode = findViewById(R.id.product_code);
        productName = findViewById(R.id.product_name);
        unitPrice = findViewById(R.id.unit_price);
        salesQuantity = findViewById(R.id.sales_quantity);
        totalAmount = findViewById(R.id.total_amount);
        paymentStatus = findViewById(R.id.payment_status);
        status = findViewById(R.id.status);
        btnSave = findViewById(R.id.btnSave);

        // Update form fields
        updateFormLayout = findViewById(R.id.updateFormLayout);
        updateId = findViewById(R.id.update_id);
        updateSalesOrderCode = findViewById(R.id.update_sales_order_code);
        updateCustomerCode = findViewById(R.id.update_customer_code);
        updateCustomerName = findViewById(R.id.update_customer_name);
        updateOrderDate = findViewById(R.id.update_order_date);
        updateDeliveryDate = findViewById(R.id.update_delivery_date);
        updateProductCode = findViewById(R.id.update_product_code);
        updateProductName = findViewById(R.id.update_product_name);
        updateUnitPrice = findViewById(R.id.update_unit_price);
        updateSalesQuantity = findViewById(R.id.update_sales_quantity);
        updateTotalAmount = findViewById(R.id.update_total_amount);
        updatePaymentStatus = findViewById(R.id.update_payment_status);
        updateStatus = findViewById(R.id.update_status);
        btnUpdate = findViewById(R.id.btnUpdate);

        // Navigation
        btnHome = findViewById(R.id.btnHome);
    }

    private void setupDatePickers() {
        orderDate.setOnClickListener(v -> showDatePickerDialog(orderDate));
        deliveryDate.setOnClickListener(v -> showDatePickerDialog(deliveryDate));
        updateOrderDate.setOnClickListener(v -> showDatePickerDialog(updateOrderDate));
        updateDeliveryDate.setOnClickListener(v -> showDatePickerDialog(updateDeliveryDate));
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
            addSalesOrder();
        } else if (v.getId() == R.id.btnUpdate) {
            updateSalesOrder();
        }
    }

    private void addSalesOrder() {
        SalesOrder salesOrder = new SalesOrder();

        try {
            if (!salesOrderCode.getText().toString().isEmpty()) {
                salesOrder.setSalesOrderCode(Long.parseLong(salesOrderCode.getText().toString()));
            }
            if (!customerCode.getText().toString().isEmpty()) {
                salesOrder.setCustomerCode(Long.parseLong(customerCode.getText().toString()));
            }
            salesOrder.setCustomerName(customerName.getText().toString());
            salesOrder.setOrderDate(orderDate.getText().toString());
            salesOrder.setDeliveryDate(deliveryDate.getText().toString());

            if (!productCode.getText().toString().isEmpty()) {
                salesOrder.setProductCode(Long.parseLong(productCode.getText().toString()));
            }
            salesOrder.setProductName(productName.getText().toString());

            if (!unitPrice.getText().toString().isEmpty()) {
                salesOrder.setUnitPrice(Double.parseDouble(unitPrice.getText().toString()));
            }
            if (!salesQuantity.getText().toString().isEmpty()) {
                salesOrder.setSalesQuantity(Integer.parseInt(salesQuantity.getText().toString()));
            }
            if (!totalAmount.getText().toString().isEmpty()) {
                salesOrder.setTotalAmount(Double.parseDouble(totalAmount.getText().toString()));
            }

            salesOrder.setPaymentStatus(paymentStatus.getText().toString());
            salesOrder.setStatus(status.getText().toString());

            RetrofitService retrofitService = new RetrofitService();
            SalesOrderApi salesOrderApi = retrofitService.getRetrofit().create(SalesOrderApi.class);

            salesOrderApi.save(salesOrder).enqueue(new Callback<SalesOrder>() {
                @Override
                public void onResponse(Call<SalesOrder> call, Response<SalesOrder> response) {
                    if (response.isSuccessful()) {
                        Toast.makeText(SalesOrderActivity.this,
                                "Sales Order added!", Toast.LENGTH_SHORT).show();
                        refreshTable();
                        clearAddForm();
                        hideUpdateForm();
                    }
                }

                @Override
                public void onFailure(Call<SalesOrder> call, Throwable t) {
                    Toast.makeText(SalesOrderActivity.this,
                            "Failed to add sales order", Toast.LENGTH_SHORT).show();
                }
            });
        } catch (NumberFormatException e) {
            Toast.makeText(this, "Please enter valid numbers", Toast.LENGTH_SHORT).show();
        }
    }

    private void updateSalesOrder() {
        SalesOrder salesOrder = new SalesOrder();
        salesOrder.setId(Long.parseLong(updateId.getText().toString()));

        try {
            if (!updateSalesOrderCode.getText().toString().isEmpty()) {
                salesOrder.setSalesOrderCode(Long.parseLong(updateSalesOrderCode.getText().toString()));
            }
            if (!updateCustomerCode.getText().toString().isEmpty()) {
                salesOrder.setCustomerCode(Long.parseLong(updateCustomerCode.getText().toString()));
            }
            salesOrder.setCustomerName(updateCustomerName.getText().toString());
            salesOrder.setOrderDate(updateOrderDate.getText().toString());
            salesOrder.setDeliveryDate(updateDeliveryDate.getText().toString());

            if (!updateProductCode.getText().toString().isEmpty()) {
                salesOrder.setProductCode(Long.parseLong(updateProductCode.getText().toString()));
            }
            salesOrder.setProductName(updateProductName.getText().toString());

            if (!updateUnitPrice.getText().toString().isEmpty()) {
                salesOrder.setUnitPrice(Double.parseDouble(updateUnitPrice.getText().toString()));
            }
            if (!updateSalesQuantity.getText().toString().isEmpty()) {
                salesOrder.setSalesQuantity(Integer.parseInt(updateSalesQuantity.getText().toString()));
            }
            if (!updateTotalAmount.getText().toString().isEmpty()) {
                salesOrder.setTotalAmount(Double.parseDouble(updateTotalAmount.getText().toString()));
            }

            salesOrder.setPaymentStatus(updatePaymentStatus.getText().toString());
            salesOrder.setStatus(updateStatus.getText().toString());

            RetrofitService retrofitService = new RetrofitService();
            SalesOrderApi salesOrderApi = retrofitService.getRetrofit().create(SalesOrderApi.class);

            salesOrderApi.update(salesOrder.getId(), salesOrder).enqueue(new Callback<SalesOrder>() {
                @Override
                public void onResponse(Call<SalesOrder> call, Response<SalesOrder> response) {
                    if (response.isSuccessful()) {
                        Toast.makeText(SalesOrderActivity.this,
                                "Sales Order updated!", Toast.LENGTH_SHORT).show();
                        refreshTable();
                        hideUpdateForm();
                        clearUpdateForm();
                    }
                }

                @Override
                public void onFailure(Call<SalesOrder> call, Throwable t) {
                    Toast.makeText(SalesOrderActivity.this,
                            "Failed to update sales order", Toast.LENGTH_SHORT).show();
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

        // Add headers for all sales order fields
        tr.addView(getTextView(0, "SO Code", Color.WHITE, Typeface.BOLD, Color.BLUE));
        tr.addView(getTextView(0, "Customer", Color.WHITE, Typeface.BOLD, Color.BLUE));
        tr.addView(getTextView(0, "Order Date", Color.WHITE, Typeface.BOLD, Color.BLUE));
        tr.addView(getTextView(0, "Delivery", Color.WHITE, Typeface.BOLD, Color.BLUE));
        tr.addView(getTextView(0, "Product", Color.WHITE, Typeface.BOLD, Color.BLUE));
        tr.addView(getTextView(0, "Unit Price", Color.WHITE, Typeface.BOLD, Color.BLUE));
        tr.addView(getTextView(0, "Qty", Color.WHITE, Typeface.BOLD, Color.BLUE));
        tr.addView(getTextView(0, "Total", Color.WHITE, Typeface.BOLD, Color.BLUE));
        tr.addView(getTextView(0, "Payment", Color.WHITE, Typeface.BOLD, Color.BLUE));
        tr.addView(getTextView(0, "Status", Color.WHITE, Typeface.BOLD, Color.BLUE));
        tr.addView(getTextView(0, "Edit", Color.WHITE, Typeface.BOLD, Color.BLUE));
        tr.addView(getTextView(0, "Delete", Color.WHITE, Typeface.BOLD, Color.BLUE));

        tl.addView(tr, getTblLayoutParams());
    }

    public void addData() {
        TableLayout tl = findViewById(R.id.table);
        RetrofitService retrofitService = new RetrofitService();
        SalesOrderApi salesOrderApi = retrofitService.getRetrofit().create(SalesOrderApi.class);

        salesOrderApi.getAllSalesOrders().enqueue(new Callback<List<SalesOrder>>() {
            @Override
            public void onResponse(Call<List<SalesOrder>> call, Response<List<SalesOrder>> response) {
                if (response.isSuccessful()) {
                    List<SalesOrder> salesOrders = response.body();
                    if (salesOrders != null) {
                        for (SalesOrder so : salesOrders) {
                            addTableRow(so, tl);
                        }
                    }
                }
            }

            @Override
            public void onFailure(Call<List<SalesOrder>> call, Throwable t) {
                Toast.makeText(SalesOrderActivity.this,
                        "Failed to load sales orders", Toast.LENGTH_SHORT).show();
            }
        });
    }

    private void addTableRow(SalesOrder salesOrder, TableLayout tl) {
        TableRow tr = new TableRow(this);
        tr.setLayoutParams(getLayoutParams());

        // Add data cells
        tr.addView(getTextView(0, salesOrder.getSalesOrderCode() != null ?
                        String.valueOf(salesOrder.getSalesOrderCode()) : "",
                Color.BLACK, Typeface.NORMAL, Color.WHITE));

        tr.addView(getTextView(0, salesOrder.getCustomerName() != null ?
                        salesOrder.getCustomerName() : "",
                Color.BLACK, Typeface.NORMAL, Color.WHITE));

        tr.addView(getTextView(0, salesOrder.getOrderDate() != null ?
                        salesOrder.getOrderDate() : "",
                Color.BLACK, Typeface.NORMAL, Color.WHITE));

        tr.addView(getTextView(0, salesOrder.getDeliveryDate() != null ?
                        salesOrder.getDeliveryDate() : "",
                Color.BLACK, Typeface.NORMAL, Color.WHITE));

        tr.addView(getTextView(0, salesOrder.getProductName() != null ?
                        salesOrder.getProductName() : "",
                Color.BLACK, Typeface.NORMAL, Color.WHITE));

        tr.addView(getTextView(0, salesOrder.getUnitPrice() != null ?
                        String.valueOf(salesOrder.getUnitPrice()) : "",
                Color.BLACK, Typeface.NORMAL, Color.WHITE));

        tr.addView(getTextView(0, salesOrder.getSalesQuantity() != null ?
                        String.valueOf(salesOrder.getSalesQuantity()) : "",
                Color.BLACK, Typeface.NORMAL, Color.WHITE));

        tr.addView(getTextView(0, salesOrder.getTotalAmount() != null ?
                        String.valueOf(salesOrder.getTotalAmount()) : "",
                Color.BLACK, Typeface.NORMAL, Color.WHITE));

        tr.addView(getTextView(0, salesOrder.getPaymentStatus() != null ?
                        salesOrder.getPaymentStatus() : "",
                Color.BLACK, Typeface.NORMAL, Color.WHITE));

        tr.addView(getTextView(0, salesOrder.getStatus() != null ?
                        salesOrder.getStatus() : "",
                Color.BLACK, Typeface.NORMAL, Color.WHITE));

        // Add edit button
        TextView editBtn = getTextView(salesOrder.getId().intValue(), "Edit",
                Color.BLUE, Typeface.BOLD, Color.WHITE);
        editBtn.setOnClickListener(v -> showUpdateForm(salesOrder));
        tr.addView(editBtn);

        // Add delete button
        TextView deleteBtn = getTextView(salesOrder.getId().intValue(), "Delete",
                Color.RED, Typeface.BOLD, Color.WHITE);
        deleteBtn.setOnClickListener(v -> showDeleteDialog(salesOrder.getId(), tr));
        tr.addView(deleteBtn);

        tl.addView(tr, getTblLayoutParams());
    }

    private void showUpdateForm(SalesOrder salesOrder) {
        updateId.setText(String.valueOf(salesOrder.getId()));
        updateSalesOrderCode.setText(salesOrder.getSalesOrderCode() != null ?
                String.valueOf(salesOrder.getSalesOrderCode()) : "");
        updateCustomerCode.setText(salesOrder.getCustomerCode() != null ?
                String.valueOf(salesOrder.getCustomerCode()) : "");
        updateCustomerName.setText(salesOrder.getCustomerName() != null ? salesOrder.getCustomerName() : "");
        updateOrderDate.setText(salesOrder.getOrderDate() != null ? salesOrder.getOrderDate() : "");
        updateDeliveryDate.setText(salesOrder.getDeliveryDate() != null ? salesOrder.getDeliveryDate() : "");
        updateProductCode.setText(salesOrder.getProductCode() != null ?
                String.valueOf(salesOrder.getProductCode()) : "");
        updateProductName.setText(salesOrder.getProductName() != null ? salesOrder.getProductName() : "");
        updateUnitPrice.setText(salesOrder.getUnitPrice() != null ?
                String.valueOf(salesOrder.getUnitPrice()) : "");
        updateSalesQuantity.setText(salesOrder.getSalesQuantity() != null ?
                String.valueOf(salesOrder.getSalesQuantity()) : "");
        updateTotalAmount.setText(salesOrder.getTotalAmount() != null ?
                String.valueOf(salesOrder.getTotalAmount()) : "");
        updatePaymentStatus.setText(salesOrder.getPaymentStatus() != null ? salesOrder.getPaymentStatus() : "");
        updateStatus.setText(salesOrder.getStatus() != null ? salesOrder.getStatus() : "");

        updateFormLayout.setVisibility(View.VISIBLE);
    }

    private void showDeleteDialog(Long soId, TableRow row) {
        new AlertDialog.Builder(this)
                .setTitle("Delete Sales Order")
                .setMessage("Are you sure you want to delete this sales order?")
                .setPositiveButton("Delete", (dialog, which) -> deleteSalesOrder(soId, row))
                .setNegativeButton("Cancel", (dialog, which) ->
                        Toast.makeText(this, "Cancelled", Toast.LENGTH_SHORT).show())
                .show();
    }

    private void deleteSalesOrder(Long soId, TableRow row) {
        RetrofitService retrofitService = new RetrofitService();
        SalesOrderApi salesOrderApi = retrofitService.getRetrofit().create(SalesOrderApi.class);

        salesOrderApi.delete(soId).enqueue(new Callback<Void>() {
            @Override
            public void onResponse(Call<Void> call, Response<Void> response) {
                if (response.isSuccessful()) {
                    ((TableLayout) row.getParent()).removeView(row);
                    Toast.makeText(SalesOrderActivity.this,
                            "Sales Order deleted", Toast.LENGTH_SHORT).show();
                    hideUpdateForm();
                }
            }

            @Override
            public void onFailure(Call<Void> call, Throwable t) {
                Toast.makeText(SalesOrderActivity.this,
                        "Failed to delete", Toast.LENGTH_SHORT).show();
            }
        });
    }

    private void hideUpdateForm() {
        updateFormLayout.setVisibility(View.GONE);
    }

    private void clearAddForm() {
        salesOrderCode.setText("");
        customerCode.setText("");
        customerName.setText("");
        orderDate.setText("");
        deliveryDate.setText("");
        productCode.setText("");
        productName.setText("");
        unitPrice.setText("");
        salesQuantity.setText("");
        totalAmount.setText("");
        paymentStatus.setText("");
        status.setText("");
    }

    private void clearUpdateForm() {
        updateId.setText("");
        updateSalesOrderCode.setText("");
        updateCustomerCode.setText("");
        updateCustomerName.setText("");
        updateOrderDate.setText("");
        updateDeliveryDate.setText("");
        updateProductCode.setText("");
        updateProductName.setText("");
        updateUnitPrice.setText("");
        updateSalesQuantity.setText("");
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