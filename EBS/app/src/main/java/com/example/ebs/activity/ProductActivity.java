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
import com.example.ebs.api.ProductApi;
import com.example.ebs.model.Product;
import com.example.ebs.util.RetrofitService;

import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class ProductActivity extends AppCompatActivity implements View.OnClickListener {

    EditText productCode, productName, productCategoryCode, productCategoryName,
            description, reorderLevel, status;
    Button btnSave;

    LinearLayout updateFormLayout;
    EditText updateId, updateProductCode, updateProductName, updateProductCategoryCode,
            updateProductCategoryName, updateDescription, updateReorderLevel, updateStatus;
    Button btnUpdate;

    Button btnHome;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        setContentView(R.layout.activity_product);
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.mainLayout), (v, insets) -> {
            Insets systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars());
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom);
            return insets;
        });

        productCode = findViewById(R.id.product_code);
        productName = findViewById(R.id.product_name);
        productCategoryCode = findViewById(R.id.product_category_code);
        productCategoryName = findViewById(R.id.product_category_name);
        description = findViewById(R.id.description);
        reorderLevel = findViewById(R.id.reorder_level);
        status = findViewById(R.id.status);
        btnSave = findViewById(R.id.btnSave);
        btnSave.setOnClickListener(this);

        updateFormLayout = findViewById(R.id.updateFormLayout);
        updateId = findViewById(R.id.update_id);
        updateProductCode = findViewById(R.id.update_product_code);
        updateProductName = findViewById(R.id.update_product_name);
        updateProductCategoryCode = findViewById(R.id.update_product_category_code);
        updateProductCategoryName = findViewById(R.id.update_product_category_name);
        updateDescription = findViewById(R.id.update_description);
        updateReorderLevel = findViewById(R.id.update_reorder_level);
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
            addProduct();
        }
        else if(v.getId() == R.id.btnUpdate) {
            updateProduct();
        }
    }

    private void addProduct() {
        Product product = new Product();

        String codeStr = productCode.getText().toString();
        if(!codeStr.isEmpty()) {
            product.setProductCode(Long.valueOf(codeStr));
        }

        product.setProductName(productName.getText().toString());

        String catCodeStr = productCategoryCode.getText().toString();
        if(!catCodeStr.isEmpty()) {
            product.setProductCategoryCode(Long.valueOf(catCodeStr));
        }

        product.setProductCategoryName(productCategoryName.getText().toString());
        product.setDescription(description.getText().toString());
        product.setReorderLevel(reorderLevel.getText().toString());
        product.setStatus(status.getText().toString());

        RetrofitService retrofitService = new RetrofitService();
        ProductApi productApi = retrofitService.getRetrofit().create(ProductApi.class);
        productApi.save(product).enqueue(new Callback<Product>() {
            @Override
            public void onResponse(Call<Product> call, Response<Product> response) {
                if(response.isSuccessful()) {
                    Toast.makeText(ProductActivity.this, "Product added!", Toast.LENGTH_SHORT).show();
                    refreshTable();
                    clearAddForm();
                    hideUpdateForm();
                }
            }

            @Override
            public void onFailure(Call<Product> call, Throwable t) {
                Toast.makeText(ProductActivity.this, "Failed to add product", Toast.LENGTH_SHORT).show();
            }
        });
    }

    private void updateProduct() {
        Product product = new Product();
        product.setId(Long.valueOf(updateId.getText().toString()));

        String updateCodeStr = updateProductCode.getText().toString();
        if(!updateCodeStr.isEmpty()) {
            product.setProductCode(Long.valueOf(updateCodeStr));
        }

        product.setProductName(updateProductName.getText().toString());

        String updateCatCodeStr = updateProductCategoryCode.getText().toString();
        if(!updateCatCodeStr.isEmpty()) {
            product.setProductCategoryCode(Long.valueOf(updateCatCodeStr));
        }

        product.setProductCategoryName(updateProductCategoryName.getText().toString());
        product.setDescription(updateDescription.getText().toString());
        product.setReorderLevel(updateReorderLevel.getText().toString());
        product.setStatus(updateStatus.getText().toString());

        RetrofitService retrofitService = new RetrofitService();
        ProductApi productApi = retrofitService.getRetrofit().create(ProductApi.class);
        productApi.update(product.getId(), product).enqueue(new Callback<Product>() {
            @Override
            public void onResponse(Call<Product> call, Response<Product> response) {
                if(response.isSuccessful()) {
                    Toast.makeText(ProductActivity.this, "Product updated!", Toast.LENGTH_SHORT).show();
                    refreshTable();
                    hideUpdateForm();
                    clearUpdateForm();
                }
            }

            @Override
            public void onFailure(Call<Product> call, Throwable t) {
                Toast.makeText(ProductActivity.this, "Failed to update product", Toast.LENGTH_SHORT).show();
            }
        });
    }

    public void addHeaders() {
        TableLayout tl = findViewById(R.id.table);
        TableRow tr = new TableRow(this);
        tr.setLayoutParams(getLayoutParams());

        tr.addView(getTextView(0, "Code", Color.WHITE, Typeface.BOLD, Color.BLUE));
        tr.addView(getTextView(0, "Name", Color.WHITE, Typeface.BOLD, Color.BLUE));
        tr.addView(getTextView(0, "Category Code", Color.WHITE, Typeface.BOLD, Color.BLUE));
        tr.addView(getTextView(0, "Category Name", Color.WHITE, Typeface.BOLD, Color.BLUE));
        tr.addView(getTextView(0, "Description", Color.WHITE, Typeface.BOLD, Color.BLUE));
//        tr.addView(getTextView(0, "Reorder", Color.WHITE, Typeface.BOLD, Color.BLUE));
        tr.addView(getTextView(0, "Status", Color.WHITE, Typeface.BOLD, Color.BLUE));
        tr.addView(getTextView(0, "Edit", Color.WHITE, Typeface.BOLD, Color.BLUE));
        tr.addView(getTextView(0, "Delete", Color.WHITE, Typeface.BOLD, Color.BLUE));

        tl.addView(tr, getTblLayoutParams());
    }

    public void addData() {
        TableLayout tl = findViewById(R.id.table);
        RetrofitService retrofitService = new RetrofitService();
        ProductApi productApi = retrofitService.getRetrofit().create(ProductApi.class);

        productApi.getAllProducts().enqueue(new Callback<List<Product>>() {
            @Override
            public void onResponse(Call<List<Product>> call, Response<List<Product>> response) {
                if(response.isSuccessful()) {
                    List<Product> products = response.body();
                    for (Product product : products) {
                        TableRow tr = new TableRow(ProductActivity.this);
                        tr.setLayoutParams(getLayoutParams());

                        String code = product.getProductCode() != null ? String.valueOf(product.getProductCode()) : "";
                        String name = product.getProductName() != null ? product.getProductName() : "";
                        String catCode = product.getProductCategoryCode() != null ? String.valueOf(product.getProductCategoryCode()) : "";
                        String catName = product.getProductCategoryName() != null ? product.getProductCategoryName() : "";
                        String desc = product.getDescription() != null ? product.getDescription() : "";
//                        String reorder = product.getReorderLevel() != null ? product.getReorderLevel() : "";
                        String stat = product.getStatus() != null ? product.getStatus() : "";

                        tr.addView(getTextView(0, code, Color.BLACK, Typeface.BOLD, Color.WHITE));
                        tr.addView(getTextView(0, name, Color.BLACK, Typeface.BOLD, Color.WHITE));
                        tr.addView(getTextView(0, catCode, Color.BLACK, Typeface.BOLD, Color.WHITE));
                        tr.addView(getTextView(0, catName, Color.BLACK, Typeface.BOLD, Color.WHITE));
                        tr.addView(getTextView(0, desc, Color.BLACK, Typeface.BOLD, Color.WHITE));
//                        tr.addView(getTextView(0, reorder, Color.BLACK, Typeface.BOLD, Color.WHITE));
                        tr.addView(getTextView(0, stat, Color.BLACK, Typeface.BOLD, Color.WHITE));

                        TextView editBtn = getTextView(product.getId().intValue(), "Edit",
                                Color.BLUE, Typeface.BOLD, Color.WHITE);
                        editBtn.setOnClickListener(v -> showUpdateForm(product));
                        tr.addView(editBtn);

                        TextView deleteBtn = getTextView(product.getId().intValue(), "Delete",
                                Color.RED, Typeface.BOLD, Color.WHITE);
                        deleteBtn.setOnClickListener(v -> showDeleteDialog(product.getId(), tr));
                        tr.addView(deleteBtn);

                        tl.addView(tr, getTblLayoutParams());
                    }
                }
            }

            @Override
            public void onFailure(Call<List<Product>> call, Throwable t) {
                Toast.makeText(ProductActivity.this, "Failed to load products", Toast.LENGTH_SHORT).show();
            }
        });
    }

    private void showUpdateForm(Product product) {
        updateId.setText(String.valueOf(product.getId()));

        updateProductCode.setText(product.getProductCode() != null ? String.valueOf(product.getProductCode()) : "");
        updateProductName.setText(product.getProductName() != null ? product.getProductName() : "");
        updateProductCategoryCode.setText(product.getProductCategoryCode() != null ? String.valueOf(product.getProductCategoryCode()) : "");
        updateProductCategoryName.setText(product.getProductCategoryName() != null ? product.getProductCategoryName() : "");
        updateDescription.setText(product.getDescription() != null ? product.getDescription() : "");
        updateReorderLevel.setText(product.getReorderLevel() != null ? product.getReorderLevel() : "");
        updateStatus.setText(product.getStatus() != null ? product.getStatus() : "");

        updateFormLayout.setVisibility(View.VISIBLE);
    }

    private void showDeleteDialog(Long productId, TableRow row) {
        new AlertDialog.Builder(this)
                .setTitle("Delete Product")
                .setMessage("Are you sure you want to delete this product?")
                .setPositiveButton("Delete", (dialog, which) -> {
                    RetrofitService retrofitService = new RetrofitService();
                    ProductApi productApi = retrofitService.getRetrofit().create(ProductApi.class);
                    productApi.delete(productId).enqueue(new Callback<Void>() {
                        @Override
                        public void onResponse(Call<Void> call, Response<Void> response) {
                            if(response.isSuccessful()) {
                                ((TableLayout)row.getParent()).removeView(row);
                                Toast.makeText(ProductActivity.this, "Product deleted", Toast.LENGTH_SHORT).show();
                                hideUpdateForm();
                            }
                        }

                        @Override
                        public void onFailure(Call<Void> call, Throwable t) {
                            Toast.makeText(ProductActivity.this, "Failed to delete", Toast.LENGTH_SHORT).show();
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
        productCode.setText("");
        productName.setText("");
        productCategoryCode.setText("");
        productCategoryName.setText("");
        description.setText("");
        reorderLevel.setText("");
        status.setText("");
    }

    private void clearUpdateForm() {
        updateId.setText("");
        updateProductCode.setText("");
        updateProductName.setText("");
        updateProductCategoryCode.setText("");
        updateProductCategoryName.setText("");
        updateDescription.setText("");
        updateReorderLevel.setText("");
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