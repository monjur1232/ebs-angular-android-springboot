package com.example.ebs.api;

import com.example.ebs.model.Department;

import java.util.List;
import retrofit2.Call;
import retrofit2.http.Body;
import retrofit2.http.DELETE;
import retrofit2.http.GET;
import retrofit2.http.POST;
import retrofit2.http.PUT;
import retrofit2.http.Path;

public interface DepartmentApi {
    @GET("department")
    Call<List<Department>> getAllDepartments();

    @POST("department")
    Call<Department> save(@Body Department department);

    @PUT("department/{id}")
    Call<Department> update(@Path(value = "id") Long id, @Body Department department);

    @DELETE("department/{id}")
    Call<Void> delete(@Path(value = "id") Long id);
}