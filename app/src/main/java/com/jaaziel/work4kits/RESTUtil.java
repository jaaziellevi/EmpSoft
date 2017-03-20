package com.jaaziel.work4kits;

import android.content.Context;
import android.support.v7.app.AlertDialog;

import com.android.volley.AuthFailureError;
import com.android.volley.NetworkError;
import com.android.volley.NoConnectionError;
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
    private Context context;

    public RESTUtil(RequestQueue requestQueue, int reqMethod, Context context) {

        this.requestQueue = requestQueue;
        this.context = context;

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
                params.put("Status", "Solicitação enviada.");
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
            String mensagem = "";
            if (error instanceof NoConnectionError){
                mensagem = "Não há conexão disponível, tente novamente. Lista ficará vazia";
                showDialog(mensagem);
            }
            else if (error instanceof NetworkError) {
                mensagem = "Erro na rede, tente novamente. Lista ficará vazia";
                showDialog(mensagem);
            }
        }
    };

    private void showDialog(String mensagem) {
        AlertDialog.Builder builder = new AlertDialog.Builder(context);
        builder.setMessage(mensagem);
        builder.show();
    }


}
