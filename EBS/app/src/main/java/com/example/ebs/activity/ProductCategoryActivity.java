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
import com.example.ebs.api.ProductCategoryApi;
import com.example.ebs.model.ProductCategory;
import com.example.ebs.util.RetrofitService;

import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class ProductCategoryActivity extends AppCompatActivity implements View.OnClickListener {

    EditText productCategoryCode, productCategoryName, description;
    Button btnSave;

    LinearLayout updateFormLayout;
    EditText updateId, updateProductCategoryCode, updateProductCategoryName, updateDescription;
    Button btnUpdate;

    Button btnHome;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        setContentView(R.layout.activity_product_category);
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.mainLayout), (v, insets) -> {
            Insets systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars());
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom);
            return insets;
        });

        productCategoryCode = findViewById(R.id.product_category_code);
        productCategoryName = findViewById(R.id.product_category_name);
        description = findViewById(R.id.description);
        btnSave = findViewById(R.id.btnSave);
        btnSave.setOnClickListener(this);

        updateFormLayout = findViewById(R.id.updateFormLayout);
        updateId = findViewById(R.id.update_id);
        updateProductCategoryCode = findViewById(R.id.update_product_category_code);
        updateProductCategoryName = findViewById(R.id.update_product_category_name);
        updateDescription = findViewById(R.id.update_description);
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
            addProductCategory();
        }
        else if(v.getId() == R.id.btnUpdate) {
            updateProductCategory();
        }
    }

    private void addProductCategory() {
        ProductCategory category = new ProductCategory();

        String codeStr = productCategoryCode.getText().toString();
        if(!codeStr.isEmpty()) {
            category.setProductCategoryCode(Long.valueOf(codeStr));
        }

        category.setProductCategoryName(productCategoryName.getText().toString());
        category.setDescription(description.getText().toString());

        RetrofitService retrofitService = new RetrofitService();
        ProductCategoryApi categoryApi = retrofitService.getRetrofit().create(ProductCategoryApi.class);
        categoryApi.save(category).enqueue(new Callback<ProductCategory>() {
            @Override
            public void onResponse(Call<ProductCategory> call, Response<ProductCategory> response) {
                if(response.isSuccessful()) {
                    Toast.makeText(ProductCategoryActivity.this, "Product Category added!", Toast.LENGTH_SHORT).show();
                    refreshTable();
                    clearAddForm();
                    hideUpdateForm();
                }
            }

            @Override
            public void onFailure(Call<ProductCategory> call, Throwable t) {
                Toast.makeText(ProductCategoryActivity.this, "Failed to add product category", Toast.LENGTH_SHORT).show();
            }
        });
    }

    private void updateProductCategory() {
        ProductCategory category = new ProductCategory();
        category.setId(Long.valueOf(updateId.getText().toString()));

        String updateCodeStr = updateProductCategoryCode.getText().toString();
        if(!updateCodeStr.isEmpty()) {
            category.setProductCategoryCode(Long.valueOf(updateCodeStr));
        }

        category.setProductCategoryName(updateProductCategoryName.getText().toString());
        category.setDescription(updateDescription.getText().toString());

        RetrofitService retrofitService = new RetrofitService();
        ProductCategoryApi categoryApi = retrofitService.getRetrofit().create(ProductCategoryApi.class);
        categoryApi.update(category.getId(), category).enqueue(new Callback<ProductCategory>() {
            @Override
            public void onResponse(Call<ProductCategory> call, Response<ProductCategory> response) {
                if(response.isSuccessful()) {
                    Toast.makeText(ProductCategoryActivity.this, "Product Category updated!", Toast.LENGTH_SHORT).show();
                    refreshTable();
                    hideUpdateForm();
                    clearUpdateForm();
                }
            }

            @Override
            public void onFailure(Call<ProductCategory> call, Throwable t) {
                Toast.makeText(ProductCategoryActivity.this, "Failed to update product category", Toast.LENGTH_SHORT).show();
            }
        });
    }

    public void addHeaders() {
        TableLayout tl = findViewById(R.id.table);
        TableRow tr = new TableRow(this);
        tr.setLayoutParams(getLayoutParams());

        tr.addView(getTextView(0, "Code", Color.WHITE, Typeface.BOLD, Color.BLUE));
        tr.addView(getTextView(0, "Name", Color.WHITE, Typeface.BOLD, Color.BLUE));
        tr.addView(getTextView(0, "Description", Color.WHITE, Typeface.BOLD, Color.BLUE));
        tr.addView(getTextView(0, "Edit", Color.WHITE, Typeface.BOLD, Color.BLUE));
        tr.addView(getTextView(0, "Delete", Color.WHITE, Typeface.BOLD, Color.BLUE));

        tl.addView(tr, getTblLayoutParams());
    }

    public void addData() {
        TableLayout tl = findViewById(R.id.table);
        RetrofitService retrofitService = new RetrofitService();
        ProductCategoryApi categoryApi = retrofitService.getRetrofit().create(ProductCategoryApi.class);

        categoryApi.getAllProductCategories().enqueue(new Callback<List<ProductCategory>>() {
            @Override
            public void onResponse(Call<List<ProductCategory>> call, Response<List<ProductCategory>> response) {
                if(response.isSuccessful()) {
                    List<ProductCategory> categories = response.body();
                    for (ProductCategory category : categories) {
                        TableRow tr = new TableRow(ProductCategoryActivity.this);
                        tr.setLayoutParams(getLayoutParams());

                        String code = category.getProductCategoryCode() != null ?
                                String.valueOf(category.getProductCategoryCode()) : "";
                        String name = category.getProductCategoryName() != null ?
                                category.getProductCategoryName() : "";
                        String desc = category.getDescription() != null ?
                                category.getDescription() : "";

                        tr.addView(getTextView(0, code, Color.BLACK, Typeface.BOLD, Color.WHITE));
                        tr.addView(getTextView(0, name, Color.BLACK, Typeface.BOLD, Color.WHITE));
                        tr.addView(getTextView(0, desc, Color.BLACK, Typeface.BOLD, Color.WHITE));

                        TextView editBtn = getTextView(category.getId().intValue(), "Edit",
                                Color.BLUE, Typeface.BOLD, Color.WHITE);
                        editBtn.setOnClickListener(v -> showUpdateForm(category));
                        tr.addView(editBtn);

                        TextView deleteBtn = getTextView(category.getId().intValue(), "Delete",
                                Color.RED, Typeface.BOLD, Color.WHITE);
                        deleteBtn.setOnClickListener(v -> showDeleteDialog(category.getId(), tr));
                        tr.addView(deleteBtn);

                        tl.addView(tr, getTblLayoutParams());
                    }
                }
            }

            @Override
            public void onFailure(Call<List<ProductCategory>> call, Throwable t) {
                Toast.makeText(ProductCategoryActivity.this, "Failed to load product categories", Toast.LENGTH_SHORT).show();
            }
        });
    }

    private void showUpdateForm(ProductCategory category) {
        updateId.setText(String.valueOf(category.getId()));

        updateProductCategoryCode.setText(category.getProductCategoryCode() != null ?
                String.valueOf(category.getProductCategoryCode()) : "");
        updateProductCategoryName.setText(category.getProductCategoryName() != null ?
                category.getProductCategoryName() : "");
        updateDescription.setText(category.getDescription() != null ?
                category.getDescription() : "");

        updateFormLayout.setVisibility(View.VISIBLE);
    }

    private void showDeleteDialog(Long categoryId, TableRow row) {
        new AlertDialog.Builder(this)
                .setTitle("Delete Product Category")
                .setMessage("Are you sure you want to delete this product category?")
                .setPositiveButton("Delete", (dialog, which) -> {
                    RetrofitService retrofitService = new RetrofitService();
                    ProductCategoryApi categoryApi = retrofitService.getRetrofit().create(ProductCategoryApi.class);
                    categoryApi.delete(categoryId).enqueue(new Callback<Void>() {
                        @Override
                        public void onResponse(Call<Void> call, Response<Void> response) {
                            if(response.isSuccessful()) {
                                ((TableLayout)row.getParent()).removeView(row);
                                Toast.makeText(ProductCategoryActivity.this, "Product Category deleted", Toast.LENGTH_SHORT).show();
                                hideUpdateForm();
                            }
                        }

                        @Override
                        public void onFailure(Call<Void> call, Throwable t) {
                            Toast.makeText(ProductCategoryActivity.this, "Failed to delete", Toast.LENGTH_SHORT).show();
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
        productCategoryCode.setText("");
        productCategoryName.setText("");
        description.setText("");
    }

    private void clearUpdateForm() {
        updateId.setText("");
        updateProductCategoryCode.setText("");
        updateProductCategoryName.setText("");
        updateDescription.setText("");
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