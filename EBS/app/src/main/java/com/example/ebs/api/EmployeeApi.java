package com.example.ebs.api;

import com.example.ebs.model.Employee;

import java.util.List;
import retrofit2.Call;
import retrofit2.http.Body;
import retrofit2.http.DELETE;
import retrofit2.http.GET;
import retrofit2.http.POST;
import retrofit2.http.PUT;
import retrofit2.http.Path;

public interface EmployeeApi {
    @GET("employee")
    Call<List<Employee>> getAllEmployees();

    @POST("employee")
    Call<Employee> save(@Body Employee employee);

    @PUT("employee/{id}")
    Call<Employee> update(@Path(value = "id") Long id, @Body Employee employee);

    @DELETE("employee/{id}")
    Call<Void> delete(@Path(value = "id") Long id);
}