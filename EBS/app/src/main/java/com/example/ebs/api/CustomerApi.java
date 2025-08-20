package com.example.ebs.api;

import com.example.ebs.model.Customer;
import java.util.List;
import retrofit2.Call;
import retrofit2.http.Body;
import retrofit2.http.DELETE;
import retrofit2.http.GET;
import retrofit2.http.POST;
import retrofit2.http.PUT;
import retrofit2.http.Path;

public interface CustomerApi {
    @GET("customer")
    Call<List<Customer>> getAllCustomers();

    @POST("customer")
    Call<Customer> save(@Body Customer customer);

    @PUT("customer/{id}")
    Call<Customer> update(@Path(value = "id") Long id, @Body Customer customer);

    @DELETE("customer/{id}")
    Call<Void> delete(@Path(value = "id") Long id);
}