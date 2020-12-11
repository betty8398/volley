package com.example.volleynavigation;

import androidx.annotation.Nullable;

import com.android.volley.NetworkResponse;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.HttpHeaderParser;
import com.android.volley.toolbox.StringRequest;

import java.io.BufferedReader;
import java.io.ByteArrayInputStream;
import java.io.IOException;
import java.io.InputStreamReader;
import java.util.zip.GZIPInputStream;

public class GZipRequest extends StringRequest {
    public GZipRequest(int method, String url, Response.Listener<String> listener, @Nullable Response.ErrorListener errorListener) {
        super(method, url, listener, errorListener);
    }

    @Override
    protected Response parseNetworkResponse(NetworkResponse response) {
        try {
            GZIPInputStream gis = new GZIPInputStream(new ByteArrayInputStream(response.data));
            BufferedReader br = new BufferedReader(new InputStreamReader(gis));
            StringBuffer sb = new StringBuffer();
            String line = "";
            while ((line=br.readLine())!=null){
                sb.append(line);
            }

            return Response.success(sb.toString(), HttpHeaderParser.parseCacheHeaders(response));
        } catch (IOException e) {
            e.printStackTrace();
            return Response.error(new VolleyError());
        }//handle errors


    }
}
