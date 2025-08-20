package com.example.ebs.api;

import com.example.ebs.model.Supplier;
import java.util.List;
import retrofit2.Call;
import retrofit2.http.Body;
import retrofit2.http.DELETE;
import retrofit2.http.GET;
import retrofit2.http.POST;
import retrofit2.http.PUT;
import retrofit2.http.Path;

public interface SupplierApi {
    @GET("supplier")
    Call<List<Supplier>> getAllSuppliers();

    @POST("supplier")
    Call<Supplier> save(@Body Supplier supplier);

    @PUT("supplier/{id}")
    Call<Supplier> update(@Path(value = "id") Long id, @Body Supplier supplier);

    @DELETE("supplier/{id}")
    Call<Void> delete(@Path(value = "id") Long id);
}