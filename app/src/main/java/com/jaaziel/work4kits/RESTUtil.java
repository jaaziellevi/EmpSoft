package com.jaaziel.work4kits;

import android.content.Context;
import android.util.Log;

import com.android.volley.AuthFailureError;
import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;

import java.util.HashMap;
import java.util.Map;


public class RESTUtil {

    private static final String ENDPOINT = "https://empsoftserver.herokuapp.com/solicitacoes";

    private RequestQueue requestQueue;

    public RESTUtil(RequestQueue requestQueue, int reqMethod, Context context) {

        this.requestQueue = requestQueue;

        if (reqMethod == Request.Method.GET) {
            fetchSolicitacoes(context);
        }
    }

    public RESTUtil(RequestQueue requestQueue, int reqMethod, String id) {

        this.requestQueue = requestQueue;

        if (reqMethod == Request.Method.PATCH) {
            postSolicitacao(id);
        }
    }



        private void postSolicitacao(final String id) {
        StringRequest request = new StringRequest(Request.Method.PATCH, ENDPOINT +"/"+id, onPatch, onPostsError) {

            @Override
            protected Map<String, String> getParams() throws AuthFailureError {
                Map<String,String> params = new HashMap<>();
                params.put("Status", "Pendente");
                return params;
            }

            @Override
            public byte[] getBody() throws AuthFailureError {
                Map<String, String> params = getParams();
                if (params != null && params.size() > 0) {
                    return IOSingleton.encodeParameters(params, getParamsEncoding());
                }
                return null;
            }
        };
        requestQueue.add(request);
        }


    private void fetchSolicitacoes(Context context) {
        StringRequest request = new StringRequest(Request.Method.GET, ENDPOINT, onPostsLoaded, onPostsError);
        requestQueue.add(request);
    }

    private final Response.Listener<String> onPatch = new Response.Listener<String>() {
        @Override
        public void onResponse(String response) {
            StringRequest request = new StringRequest(Request.Method.GET, ENDPOINT, onPostsLoaded, onPostsError);
            requestQueue.add(request);
        }
    };



    private final Response.Listener<String> onPostsLoaded = new Response.Listener<String>() {
        @Override
        public void onResponse(String response) {
            IOSingleton singleton = IOSingleton.Instance();
            singleton.saveResponse(response);
        }
    };

    private final Response.ErrorListener onPostsError = new Response.ErrorListener() {
        @Override
        public void onErrorResponse(VolleyError error) {
            Log.e("PostActivity", error.toString());
        }
    };


}
