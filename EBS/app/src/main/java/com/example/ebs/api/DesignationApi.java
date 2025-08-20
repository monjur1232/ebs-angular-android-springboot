package com.example.ebs.api;

import com.example.ebs.model.Designation;
import java.util.List;
import retrofit2.Call;
import retrofit2.http.Body;
import retrofit2.http.DELETE;
import retrofit2.http.GET;
import retrofit2.http.POST;
import retrofit2.http.PUT;
import retrofit2.http.Path;

public interface DesignationApi {
    @GET("designation")
    Call<List<Designation>> getAllDesignations();

    @POST("designation")
    Call<Designation> save(@Body Designation designation);

    @PUT("designation/{id}")
    Call<Designation> update(@Path(value = "id") Long id, @Body Designation designation);

    @DELETE("designation/{id}")
    Call<Void> delete(@Path(value = "id") Long id);
}