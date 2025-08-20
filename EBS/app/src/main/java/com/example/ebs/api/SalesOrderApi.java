package com.example.ebs.api;

import com.example.ebs.model.SalesOrder;
import java.util.List;
import retrofit2.Call;
import retrofit2.http.Body;
import retrofit2.http.DELETE;
import retrofit2.http.GET;
import retrofit2.http.POST;
import retrofit2.http.PUT;
import retrofit2.http.Path;

public interface SalesOrderApi {
    @GET("sales-order")
    Call<List<SalesOrder>> getAllSalesOrders();

    @POST("sales-order")
    Call<SalesOrder> save(@Body SalesOrder salesOrder);

    @PUT("sales-order/{id}")
    Call<SalesOrder> update(@Path(value = "id") Long id, @Body SalesOrder salesOrder);

    @DELETE("sales-order/{id}")
    Call<Void> delete(@Path(value = "id") Long id);
}