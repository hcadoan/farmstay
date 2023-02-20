package model;

import java.util.HashMap;
import java.util.List;

import retrofit2.Call;
import retrofit2.http.Body;
import retrofit2.http.GET;
import retrofit2.http.POST;

public interface RetrofitInterface {

    @POST("/login")
    Call<LoginResult> executeLogin(@Body HashMap<String, String> map);

    @POST("/signup")
    Call<Void> executeSignup (@Body HashMap<String, String> map);

    @POST("/forgotpassword")
    Call<LoginResult> forgotPassword (@Body HashMap<String, String> map);

    @POST("/verifyresetpassword")
    Call<Void> verifyresetpassword (@Body HashMap<String, String> map);

    @POST("/resetpassword")
    Call<Void> resetpassword (@Body HashMap<String, String> map);

    @GET("/sensor/moisture")
    Call<List<SensorResuilt>> getLatestMoisData();
    @GET("/sensor/temp")
    Call<List<SensorResuilt>> getLatestTempData();
    @GET("/sensor/humi")
    Call<List<SensorResuilt>> getLatestHumiData();
    @GET("/sensor/waterlevel")
    Call<List<SensorResuilt>> getLatestwaterlevelData();
    @GET("/sensor/fire")
    Call<List<SensorResuilt>> getLatestfireData();
}
