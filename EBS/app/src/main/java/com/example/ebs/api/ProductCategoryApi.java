package com.example.ebs.api;

import com.example.ebs.model.ProductCategory;
import java.util.List;
import retrofit2.Call;
import retrofit2.http.Body;
import retrofit2.http.DELETE;
import retrofit2.http.GET;
import retrofit2.http.POST;
import retrofit2.http.PUT;
import retrofit2.http.Path;

public interface ProductCategoryApi {
    @GET("product-category")
    Call<List<ProductCategory>> getAllProductCategories();

    @POST("product-category")
    Call<ProductCategory> save(@Body ProductCategory productCategory);

    @PUT("product-category/{id}")
    Call<ProductCategory> update(@Path(value = "id") Long id, @Body ProductCategory productCategory);

    @DELETE("product-category/{id}")
    Call<Void> delete(@Path(value = "id") Long id);
}