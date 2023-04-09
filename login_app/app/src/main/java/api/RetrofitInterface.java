package api;

import java.util.HashMap;

import retrofit2.Call;
import retrofit2.http.Body;
import retrofit2.http.GET;
import retrofit2.http.Header;
import retrofit2.http.POST;
import retrofit2.http.PUT;

public interface RetrofitInterface {

    @POST("/auth/login")
    Call<LoginResult> executeLogin(@Body HashMap<String, String> map);

    @POST("/auth/register")
    Call<Response> executeSignup (@Body HashMap<String, String> map);

    @GET("/auth/logout")
    Call<LoginResult> Logout(@Header("authenticate_jwt") String token);

    @POST("/user/active")
    Call<LoginResult> activeEmail (@Header("authenticate_jwt") String token, @Body HashMap<String, String> map);

    @PUT("/user/active")
    Call<LoginResult> verifyAccount (@Header("authenticate_jwt") String token, @Body HashMap<String, String> map);

    @POST("/user/forgot-password")
    Call<LoginResult> forgotPassword (@Body HashMap<String, String> map);

    @PUT("/user/forgot-password")
    Call<LoginResult> resetPass (@Body HashMap<String, String> map);

    @POST("/user/otp")
    Call<LoginResult> Active (@Body HashMap<String, String> map);

    @PUT("/user/change-password")
    Call<LoginResult> ChangePass (@Header("authenticate_jwt") String token, @Body HashMap<String, String> map);

    @GET("/user")
    Call<LoginResult> getUser(@Header("authenticate_jwt") String token);

    @GET("/farmstays")
    Call<FarmModel> getFarm(@Header("authenticate_jwt") String token);

    //du lieu cam bien
    @GET("/customer/farmstay/equipments")
    Call<Response> SocketIO (@Header("authenticate_jwt") String token);

    //@GET("/sensor/moisture")
    //Call<List<SensorResuilt>> getLatestMoisData();
    //GET("/sensor/temp")
    //Call<List<SensorResuilt>> getLatestTempData();
    //@GET("/sensor/humi")
    //Call<List<SensorResuilt>> getLatestHumiData();
    //@GET("/sensor/waterlevel")
    //Call<List<SensorResuilt>> getLatestwaterlevelData();
    //@GET("/sensor/fire")
    //Call<List<SensorResuilt>> getLatestfireData();
}
