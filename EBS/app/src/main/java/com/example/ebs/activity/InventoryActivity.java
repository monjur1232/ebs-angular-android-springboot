package com.example.ebs.activity;

import android.content.Intent;
import android.graphics.Color;
import android.graphics.Typeface;
import android.os.Bundle;
import android.widget.Button;
import android.widget.TableLayout;
import android.widget.TableRow;
import android.widget.TextView;
import android.widget.Toast;

import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;

import com.example.ebs.R;
import com.example.ebs.api.PurchaseOrderApi;
import com.example.ebs.api.SalesOrderApi;
import com.example.ebs.model.InventoryItem;
import com.example.ebs.model.PurchaseOrder;
import com.example.ebs.model.SalesOrder;
import com.example.ebs.util.RetrofitService;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class InventoryActivity extends AppCompatActivity {

    private Button btnHome, btnRefresh;
    private TableLayout tableLayout;
    private Map<Long, InventoryItem> inventoryMap = new HashMap<>();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        setContentView(R.layout.activity_inventory);
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.mainLayout), (v, insets) -> {
            Insets systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars());
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom);
            return insets;
        });

        btnHome = findViewById(R.id.btnHome);
        btnRefresh = findViewById(R.id.btnRefresh);
        tableLayout = findViewById(R.id.table);

        btnHome.setOnClickListener(v -> {
            Intent i = new Intent(getApplicationContext(), HomeActivity.class);
            startActivity(i);
        });

        btnRefresh.setOnClickListener(v -> calculateInventory());

        addTableHeaders();
        calculateInventory();
    }

    private void calculateInventory() {
        inventoryMap.clear();

        RetrofitService retrofitService = new RetrofitService();
        PurchaseOrderApi purchaseOrderApi = retrofitService.getRetrofit().create(PurchaseOrderApi.class);
        SalesOrderApi salesOrderApi = retrofitService.getRetrofit().create(SalesOrderApi.class);

        // Fetch purchase orders
        purchaseOrderApi.getAllPurchaseOrders().enqueue(new Callback<List<PurchaseOrder>>() {
            @Override
            public void onResponse(Call<List<PurchaseOrder>> call, Response<List<PurchaseOrder>> response) {
                if (response.isSuccessful() && response.body() != null) {
                    processPurchaseOrders(response.body());

                    // After processing purchases, fetch sales orders
                    salesOrderApi.getAllSalesOrders().enqueue(new Callback<List<SalesOrder>>() {
                        @Override
                        public void onResponse(Call<List<SalesOrder>> call, Response<List<SalesOrder>> salesResponse) {
                            if (salesResponse.isSuccessful() && salesResponse.body() != null) {
                                processSalesOrders(salesResponse.body());
                                displayInventory();
                            } else {
                                Toast.makeText(InventoryActivity.this,
                                        "Failed to load sales data", Toast.LENGTH_SHORT).show();
                            }
                        }

                        @Override
                        public void onFailure(Call<List<SalesOrder>> call, Throwable t) {
                            Toast.makeText(InventoryActivity.this,
                                    "Sales data error: " + t.getMessage(), Toast.LENGTH_SHORT).show();
                        }
                    });
                } else {
                    Toast.makeText(InventoryActivity.this,
                            "Failed to load purchase data", Toast.LENGTH_SHORT).show();
                }
            }

            @Override
            public void onFailure(Call<List<PurchaseOrder>> call, Throwable t) {
                Toast.makeText(InventoryActivity.this,
                        "Purchase data error: " + t.getMessage(), Toast.LENGTH_SHORT).show();
            }
        });
    }

    private void processPurchaseOrders(List<PurchaseOrder> purchaseOrders) {
        for (PurchaseOrder po : purchaseOrders) {
            if (po.getProductCode() != null) {
                InventoryItem item = inventoryMap.get(po.getProductCode());
                if (item == null) {
                    item = new InventoryItem();
                    item.setProductCode(po.getProductCode());
                    item.setProductName(po.getProductName());
                    item.setUnitPrice(po.getUnitPrice());
                    inventoryMap.put(po.getProductCode(), item);
                }
                if (po.getPurchaseQuantity() != null) {
                    item.addPurchased(po.getPurchaseQuantity());
                }
            }
        }
    }

    private void processSalesOrders(List<SalesOrder> salesOrders) {
        for (SalesOrder so : salesOrders) {
            if (so.getProductCode() != null) {
                InventoryItem item = inventoryMap.get(so.getProductCode());
                if (item == null) {
                    item = new InventoryItem();
                    item.setProductCode(so.getProductCode());
                    item.setProductName(so.getProductName());
                    item.setUnitPrice(so.getUnitPrice());
                    inventoryMap.put(so.getProductCode(), item);
                }
                if (so.getSalesQuantity() != null) {
                    item.addSold(so.getSalesQuantity());
                }
            }
        }
    }

    private void addTableHeaders() {
        tableLayout.removeAllViews();

        TableRow headerRow = new TableRow(this);
        headerRow.setLayoutParams(new TableRow.LayoutParams(
                TableRow.LayoutParams.MATCH_PARENT,
                TableRow.LayoutParams.WRAP_CONTENT));

        String[] headers = {"Product Code", "Product Name",
                "Purchased", "Sold", "In Stock"};

        for (String header : headers) {
            TextView tv = new TextView(this);
            tv.setText(header);
            tv.setTextColor(Color.WHITE);
            tv.setTypeface(Typeface.DEFAULT_BOLD);
            tv.setPadding(16, 16, 16, 16);
            tv.setBackgroundColor(Color.parseColor("#3F51B5"));
            headerRow.addView(tv);
        }

        tableLayout.addView(headerRow);
    }

    private void displayInventory() {
        tableLayout.removeAllViews();
        addTableHeaders();

        for (InventoryItem item : inventoryMap.values()) {
            TableRow row = new TableRow(this);

            // Product Code
            addCellToRow(row, item.getProductCode() != null ?
                    item.getProductCode().toString() : "");

            // Product Name
            addCellToRow(row, item.getProductName() != null ?
                    item.getProductName() : "");

            // Unit Price
//            addCellToRow(row, item.getUnitPrice() != null ?
//                    String.format("%.2f", item.getUnitPrice()) : "");

            // Purchased Quantity
            addCellToRow(row, String.valueOf(item.getTotalPurchased()));

            // Sold Quantity
            addCellToRow(row, String.valueOf(item.getTotalSold()));

            // Current Stock (with color coding)
            TextView stockCell = new TextView(this);
            stockCell.setText(String.valueOf(item.getCurrentStock()));
            stockCell.setPadding(16, 16, 16, 16);

            if (item.getCurrentStock() < 5) {
                stockCell.setTextColor(Color.RED);
                stockCell.setTypeface(null, Typeface.BOLD);
            } else if (item.getCurrentStock() < 10) {
                stockCell.setTextColor(Color.parseColor("#FFA500")); // Orange
            } else {
                stockCell.setTextColor(Color.BLACK);
            }

            row.addView(stockCell);
            tableLayout.addView(row);
        }
    }

    private void addCellToRow(TableRow row, String text) {
        TextView tv = new TextView(this);
        tv.setText(text);
        tv.setPadding(16, 16, 16, 16);
        tv.setTextColor(Color.BLACK);
        row.addView(tv);
    }
}