package com.example.login_app.farmstay_page;

import static android.content.Context.MODE_PRIVATE;

import android.content.SharedPreferences;
import android.os.Bundle;

import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentStatePagerAdapter;
import androidx.viewpager.widget.ViewPager;

import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.example.login_app.R;
import com.google.android.material.tabs.TabLayout;

import org.json.JSONException;
import org.json.JSONObject;

import adapter.ViewPagerAdapter_3;
import io.socket.client.Socket;
import api.Response;
import api.RetrofitInterface;
import api.RetrofitServer;
import model.SocketIO;
import retrofit2.Call;
import retrofit2.Callback;

/**
 * A simple {@link Fragment} subclass.
 * Use the {@link FarmstayFragment#newInstance} factory method to
 * create an instance of this fragment.
 */
public class FarmstayFragment extends Fragment {

    private TabLayout tabLayout;
    private ViewPager viewPager;
    private TextView tvMessage;

    SharedPreferences sharedPreferences;

    // TODO: Rename parameter arguments, choose names that match
    // the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
    private static final String ARG_PARAM1 = "param1";
    private static final String ARG_PARAM2 = "param2";

    // TODO: Rename and change types of parameters
    private String mParam1;
    private String mParam2;

    public FarmstayFragment() {
        // Required empty public constructor
    }

    /**
     * Use this factory method to create a new instance of
     * this fragment using the provided parameters.
     *
     * @param param1 Parameter 1.
     * @param param2 Parameter 2.
     * @return A new instance of fragment HelpFragment.
     */
    // TODO: Rename and change types and number of parameters
    public static FarmstayFragment newInstance(String param1, String param2) {
        FarmstayFragment fragment = new FarmstayFragment();
        Bundle args = new Bundle();
        args.putString(ARG_PARAM1, param1);
        args.putString(ARG_PARAM2, param2);
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (getArguments() != null) {
            mParam1 = getArguments().getString(ARG_PARAM1);
            mParam2 = getArguments().getString(ARG_PARAM2);
        }
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_farmstay, container, false);

        tabLayout = view.findViewById(R.id.tabLayout);
        viewPager = view.findViewById(R.id.viewPager_farmstay);
        tvMessage = view.findViewById(R.id.tvMessage);

//        tvMessage.setVisibility(View.VISIBLE);
//        viewPager.setVisibility(View.INVISIBLE);

        tvMessage.setVisibility(View.INVISIBLE);
        viewPager.setVisibility(View.VISIBLE);

        ViewPagerAdapter_3 viewPagerAdapter_3 = new ViewPagerAdapter_3(getActivity(), getParentFragmentManager(), FragmentStatePagerAdapter.BEHAVIOR_RESUME_ONLY_CURRENT_FRAGMENT);
        viewPager.setAdapter(viewPagerAdapter_3);
        tabLayout.setupWithViewPager(viewPager);

        getData();

        return view;
    }

    private void getData() {
        RetrofitServer retrofitServer = new RetrofitServer();
        RetrofitInterface retrofitInterface = retrofitServer.Retrofit();

        SocketIO socketIO = new SocketIO();
        Socket socket = socketIO.socket(getActivity());

        //test gửi lên socketIO
//        String mes = "hello";
//        String jsonstring = "{message: " + "'" + mes + "'" + "}";
//        try {
//            JSONObject messageJson = new JSONObject(jsonstring);
//            getActivity().runOnUiThread(new Runnable() {
//                @Override
//                public void run() {
//                    Log.e("message", String.valueOf(mes));
//                    socket.emit("subscribe", mes);
//                }
//            });
//        } catch (JSONException e) {
//            e.printStackTrace();
//        }
//
//        socket.on("message", new Emitter.Listener() {
//            @Override
//            public void call(Object... args) {
//                // Lấy giá trị cảm biến từ tham số args
//                try {
//                    JSONObject jsondata = new JSONObject(args[0].toString());
//                    String mesData = jsondata.getString("value");
//                    getActivity().runOnUiThread(new Runnable() {
//                        @Override
//                        public void run() {
//                            Log.e("messageData", String.valueOf(mesData));
//                        }
//                    });
//                } catch (JSONException e) {
//                    e.printStackTrace();
//                }
//            }
//        });

        sharedPreferences = getActivity().getSharedPreferences("SaveInfo", MODE_PRIVATE);
        String token = sharedPreferences.getString("token","");
        Call<Response> call = retrofitInterface.SocketIO(token);
        call.enqueue(new Callback<Response>() {
            @Override
            public void onResponse(Call<Response> call, retrofit2.Response<Response> response) {
                try {
                    JSONObject jsondata = new JSONObject(response.toString());
                    Log.e("callApi", String.valueOf(jsondata));
                } catch (JSONException e) {
                    e.printStackTrace();
                }
            }

            @Override
            public void onFailure(Call<Response> call, Throwable t) {

            }
        });

    }

}