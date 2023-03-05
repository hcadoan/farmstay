package model;

import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

public class RetrofitServer2 {
    private Retrofit retrofit;
    private RelayApi relayApi;
    private String BASE_URL = "http://192.168.137.1:3000"; //ipv4 address

    public RelayApi Retrofit(){
        retrofit = new Retrofit.Builder()
                .baseUrl(BASE_URL)
                .addConverterFactory(GsonConverterFactory.create())
                .build();

        relayApi = retrofit.create(RelayApi.class);
        return relayApi;
    }
}
