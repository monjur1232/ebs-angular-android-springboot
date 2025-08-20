package com.example.ebs.api;

import com.example.ebs.model.Product;
import java.util.List;
import retrofit2.Call;
import retrofit2.http.Body;
import retrofit2.http.DELETE;
import retrofit2.http.GET;
import retrofit2.http.POST;
import retrofit2.http.PUT;
import retrofit2.http.Path;

public interface ProductApi {
    @GET("product")
    Call<List<Product>> getAllProducts();

    @POST("product")
    Call<Product> save(@Body Product product);

    @PUT("product/{id}")
    Call<Product> update(@Path(value = "id") Long id, @Body Product product);

    @DELETE("product/{id}")
    Call<Void> delete(@Path(value = "id") Long id);
}