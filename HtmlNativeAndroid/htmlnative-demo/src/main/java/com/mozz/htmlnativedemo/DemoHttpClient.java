package com.mozz.htmlnativedemo;

import android.util.Log;

import com.android.volley.AuthFailureError;
import com.android.volley.NetworkResponse;
import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.HttpHeaderParser;
import com.android.volley.toolbox.Volley;
import com.mozz.htmlnative.http.HNHttpClient;
import com.mozz.htmlnative.http.Http;
import com.mozz.htmlnative.http.HttpRequest;
import com.mozz.htmlnative.http.HttpResponse;

/**
 * @author Yang Tao, 17/5/30.
 */

public class DemoHttpClient implements HNHttpClient {

    private static RequestQueue sQueue = Volley.newRequestQueue(DemoApplication.instance);

    @Override
    public void send(final HttpRequest request, final Http.RequestCallback callback) {
        Request<String> stringRequest = new Request<String>(request.method, request.url, new
                Response.ErrorListener() {


            @Override
            public void onErrorResponse(VolleyError error) {
                if (error != null) {
                    Log.e("DemoHttpClient", error.getMessage());
                } else {
                    Log.e("DemoHttpClient", "SOMETHIGN WENT WRONG!");
                }
            }
        }) {
            @Override
            protected Response<String> parseNetworkResponse(NetworkResponse response) {
                return Response.success(new String(response.data), HttpHeaderParser
                        .parseCacheHeaders(response));
            }

            @Override
            protected void deliverResponse(final String res) {
                callback.onResponse(new HttpResponse(res, 200));
            }

            @Override
            public void deliverError(VolleyError error) {
                callback.onResponse(new HttpResponse(error.getMessage(), 505));
            }

            @Override
            public byte[] getBody() throws AuthFailureError {
                return request.body.getBytes();
            }
        };

        sQueue.add(stringRequest);
    }
}
