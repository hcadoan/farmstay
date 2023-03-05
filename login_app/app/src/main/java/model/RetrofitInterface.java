package model;

import java.util.HashMap;
import java.util.List;

import retrofit2.Call;
import retrofit2.http.Body;
import retrofit2.http.GET;
import retrofit2.http.Header;
import retrofit2.http.POST;

public interface RetrofitInterface {

    @POST("/auth/login")
    Call<LoginResult> executeLogin(@Body HashMap<String, String> map);

    @POST("/auth/register")
    Call<Response> executeSignup (@Body HashMap<String, String> map);

    @GET("/auth/logout")
    Call<LoginResult> Logout(@Header("authenticate_jwt") String token);

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
