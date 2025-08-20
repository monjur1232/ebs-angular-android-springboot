package com.example.ebs.api;

import com.example.ebs.model.Attendance;
import java.util.List;
import retrofit2.Call;
import retrofit2.http.Body;
import retrofit2.http.DELETE;
import retrofit2.http.GET;
import retrofit2.http.POST;
import retrofit2.http.PUT;
import retrofit2.http.Path;

public interface AttendanceApi {
    @GET("attendance")
    Call<List<Attendance>> getAllAttendances();

    @POST("attendance")
    Call<Attendance> save(@Body Attendance attendance);

    @POST("attendance/bulk")
    Call<List<Attendance>> saveBulk(@Body List<Attendance> attendances);

    @PUT("attendance/{id}")
    Call<Attendance> update(@Path(value = "id") Long id, @Body Attendance attendance);

    @DELETE("attendance/{id}")
    Call<Void> delete(@Path(value = "id") Long id);
}