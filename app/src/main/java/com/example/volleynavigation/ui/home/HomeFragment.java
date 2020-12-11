package com.example.volleynavigation.ui.home;

import android.app.Activity;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ListView;
import android.widget.SimpleAdapter;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.Observer;
import androidx.lifecycle.ViewModelProviders;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;
import com.example.volleynavigation.GZipRequest;
import com.example.volleynavigation.R;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class HomeFragment extends Fragment {

    private HomeViewModel homeViewModel;
    private ListView listView;

    public View onCreateView(@NonNull LayoutInflater inflater,
                             ViewGroup container, Bundle savedInstanceState) {
        homeViewModel =
                ViewModelProviders.of(this).get(HomeViewModel.class);
        View root = inflater.inflate(R.layout.fragment_home, container, false);
        listView = root.findViewById(R.id.listView);
 //       final TextView textView = root.findViewById(R.id.text_home);
//        homeViewModel.getText().observe(getViewLifecycleOwner(), new Observer<String>() {
//            @Override
//            public void onChanged(@Nullable String s) {
//                textView.setText(s);
//            }
//        });

        connect("https://tcgbusfs.blob.core.windows.net/blobbus/GetRoute.gz");
        return root;
    }

    private void connect(String url){
        //RequestQueue實體
        //2.context可以用getActivity() getContext()取得 但建議用requireActivity()以防取得null
        RequestQueue queue = Volley.newRequestQueue(requireActivity());
        //url ="http://www.google.com";//1.將url設為connect參數

        // Request a string response from the provided URL.
        GZipRequest stringRequest = new GZipRequest(Request.Method.GET, url,
                new Response.Listener<String>() {
                    @Override
                    public void onResponse(String response) {
                        // Display the first 500 characters of the response string. substring可以取得0~500的字符
                        //textView.setText("Response is: "+ response.substring(0,500));
                        //3.用Toast取代textView來顯示取得的回應資料
                        //Toast.makeText(requireActivity(), "Response is: "+ response.substring(0,10000), Toast.LENGTH_LONG).show();
                        try {
                            JSONObject jsonObject = new JSONObject(response);
                            JSONArray jsonArray = jsonObject.getJSONArray("BusInfo");
                            setAdapter(jsonArray);
//                            JSONObject firstObject = (JSONObject) jsonArray.get(0);
//                            String routeName = firstObject.getString("nameZh");
//                            String departureZh = firstObject.getString("departureZh");
//                            String destinationZh = firstObject.getString("destinationZh");
//                            Toast.makeText(requireActivity(), "路線："+routeName+"\n起點"+departureZh+"\n終點"+destinationZh, Toast.LENGTH_SHORT).show();
                        } catch (JSONException e) {
                            e.printStackTrace();
                            Toast.makeText(requireActivity(), "抓取失敗", Toast.LENGTH_SHORT).show();
                        }

                    }
                }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                //4.用Toast取代textView來顯示回應錯誤
                Toast.makeText(requireActivity(), "That didn't work!", Toast.LENGTH_LONG).show();
            }
        });

        // Add the request to the RequestQueue.
        queue.add(stringRequest);
    }

    private void setAdapter(JSONArray jArray) {
        List<Map<String,String>> data = new ArrayList<>();
        JSONObject jsonObject = null;
            try {
                for(int i =0 ; i<jArray.length();i++){
                  jsonObject = (JSONObject)jArray.get(i);
                  Map<String,String> map = new HashMap<>();
                  map.put("nameZh",jsonObject.getString("nameZh"));
                  map.put("depart-dest",jsonObject.getString("departureZh")+"~"+jsonObject.getString("destinationZh"));
                  data.add(map);
                }
            } catch (JSONException e) {
                e.printStackTrace();
            }

        SimpleAdapter adapter = new SimpleAdapter(
                getActivity(),
                data,
                R.layout.item_list,
                new String[]{"nameZh","depart-dest"},
                new int[] {R.id.textView1,R.id.textView2});
            listView.setAdapter(adapter);
    }
}