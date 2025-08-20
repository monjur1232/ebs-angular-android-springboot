package com.example.ebs.api;

import com.example.ebs.model.PurchaseOrder;
import java.util.List;
import retrofit2.Call;
import retrofit2.http.Body;
import retrofit2.http.DELETE;
import retrofit2.http.GET;
import retrofit2.http.POST;
import retrofit2.http.PUT;
import retrofit2.http.Path;

public interface PurchaseOrderApi {
    @GET("purchase-order")
    Call<List<PurchaseOrder>> getAllPurchaseOrders();

    @POST("purchase-order")
    Call<PurchaseOrder> save(@Body PurchaseOrder purchaseOrder);

    @PUT("purchase-order/{id}")
    Call<PurchaseOrder> update(@Path(value = "id") Long id, @Body PurchaseOrder purchaseOrder);

    @DELETE("purchase-order/{id}")
    Call<Void> delete(@Path(value = "id") Long id);
}