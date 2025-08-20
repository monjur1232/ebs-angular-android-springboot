package com.example.ebs.activity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;

import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;

import com.example.ebs.R;

public class HomeActivity extends AppCompatActivity {

    Button btnDepartment;
    Button btnEmployee;
    Button btnDesignation;
    Button btnAttendance;
    Button btnProduct;
    Button btnProductCategory;
    Button btnSupplier;
    Button btnCustomer;
    Button btnPurchaseOrder;
    Button btnSalesOrder;
    Button btnInventory;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        setContentView(R.layout.activity_home);
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main), (v, insets) -> {
            Insets systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars());
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom);
            return insets;
        });

        btnDepartment = findViewById(R.id.btnDepartment);
        btnEmployee = findViewById(R.id.btnEmployee);
        btnDesignation = findViewById(R.id.btnDesignation);
        btnAttendance = findViewById(R.id.btnAttendance);
        btnProduct = findViewById(R.id.btnProduct);
        btnProductCategory = findViewById(R.id.btnProductCategory);
        btnSupplier = findViewById(R.id.btnSupplier);
        btnCustomer = findViewById(R.id.btnCustomer);
        btnPurchaseOrder = findViewById(R.id.btnPurchaseOrder);
        btnSalesOrder = findViewById(R.id.btnSalesOrder);
        btnInventory = findViewById(R.id.btnInventory);

        btnDepartment.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent i = new Intent(getApplicationContext(), DepartmentActivity.class);
                startActivity(i);
            }
        });

        btnEmployee.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent i = new Intent(getApplicationContext(), EmployeeActivity.class);
                startActivity(i);
            }
        });

        btnDesignation.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent i = new Intent(getApplicationContext(), DesignationActivity.class);
                startActivity(i);
            }
        });

        btnAttendance.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent i = new Intent(getApplicationContext(), AttendanceActivity.class);
                startActivity(i);
            }
        });

        btnProduct.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent i = new Intent(getApplicationContext(), ProductActivity.class);
                startActivity(i);
            }
        });

        btnProductCategory.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent i = new Intent(getApplicationContext(), ProductCategoryActivity.class);
                startActivity(i);
            }
        });

        btnSupplier.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent i = new Intent(getApplicationContext(), SupplierActivity.class);
                startActivity(i);
            }
        });

        btnCustomer.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent i = new Intent(getApplicationContext(), CustomerActivity.class);
                startActivity(i);
            }
        });

        btnPurchaseOrder.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent i = new Intent(getApplicationContext(), PurchaseOrderActivity.class);
                startActivity(i);
            }
        });

        btnSalesOrder.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent i = new Intent(getApplicationContext(), SalesOrderActivity.class);
                startActivity(i);
            }
        });

        btnInventory.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent i = new Intent(getApplicationContext(), InventoryActivity.class);
                startActivity(i);
            }
        });
    }

}
