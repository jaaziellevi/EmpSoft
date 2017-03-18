package com.jaaziel.work4kits;

import android.app.ProgressDialog;
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
    private ProgressDialog ringProgressDialog;

    public RESTUtil(RequestQueue requestQueue, int reqMethod, Context context) {

        this.requestQueue = requestQueue;

        if (reqMethod == Request.Method.GET) {
            fetchSolicitacoes(context);
        }
    }

    public RESTUtil(RequestQueue requestQueue, int reqMethod, String usuario, String vaga) {

        this.requestQueue = requestQueue;

        if (reqMethod == Request.Method.POST) {
            postSolicitacao(usuario, vaga);
        }
    }



        private void postSolicitacao(final String usuario, final String vaga) {
        StringRequest request = new StringRequest(Request.Method.POST, ENDPOINT,onPostsLoaded, onPostsError) {
            @Override
            protected Map<String, String> getParams() throws AuthFailureError {
                Map<String,String> params = new HashMap<>();
                params.put(vaga, "Pendente");
                return params;
            }
        };
        requestQueue.add(request);
        }


    private void fetchSolicitacoes(Context context) {
        StringRequest request = new StringRequest(Request.Method.GET, ENDPOINT, onPostsLoaded, onPostsError);
        requestQueue.add(request);
    }

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
