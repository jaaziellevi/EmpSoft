package com.jaaziel.work4kits;

import android.app.Activity;
import android.app.Notification;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.app.Service;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.support.v4.app.ActivityCompat;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentActivity;
import android.support.v4.app.FragmentTransaction;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.app.NotificationCompat;
import android.util.Log;

import com.android.volley.AuthFailureError;
import com.android.volley.NetworkError;
import com.android.volley.NoConnectionError;
import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.google.gson.JsonElement;
import com.google.gson.JsonParser;

import java.util.HashMap;
import java.util.Map;


public class RESTUtil {

    private static final String ENDPOINT = "https://empsoftserver.herokuapp.com/solicitacoes";

    private RequestQueue requestQueue;
    private Context context;
    private AlertDialog.Builder builder;

    public RESTUtil(RequestQueue requestQueue, int reqMethod, Context context) {

        this.requestQueue = requestQueue;
        this.context = context;

        if (reqMethod == Request.Method.GET) {
            fetchSolicitacoes(context);
        }
    }

    public RESTUtil(RequestQueue requestQueue, int reqMethod, String id, Context context) {

        this.requestQueue = requestQueue;
        this.context = context;

        if (reqMethod == Request.Method.PATCH) {
            postSolicitacao(id);
        }
    }

    public RESTUtil(RequestQueue requestQueue, int reqMethod, String id, boolean b, Context context) {

        this.requestQueue = requestQueue;
        this.context = context;

        if (reqMethod == Request.Method.PATCH) {
            notificaVaga(id, b);
        }
    }

    public RESTUtil(RequestQueue requestQueue, Context context) {
        this.context = context;
        this.requestQueue = requestQueue;
        checaMudanca();
    }

    private void notificaVaga(String id, final boolean b) {
        StringRequest request = new StringRequest(Request.Method.PATCH, ENDPOINT +"/"+id, onPatch, onPostsError) {

            @Override
            protected Map<String, String> getParams() throws AuthFailureError {
                Map<String,String> params = new HashMap<>();
                if (b) {
                    params.put("Status", "Aprovado.");
                } else {
                    params.put("Status", "Rejeitado.");
                }
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


    private void postSolicitacao(final String id) {
        StringRequest request = new StringRequest(Request.Method.PATCH, ENDPOINT +"/"+id, onPatch, onPostsError) {

            @Override
            protected Map<String, String> getParams() throws AuthFailureError {
                Map<String,String> params = new HashMap<>();
                params.put("Status", "Solicitação enviada.");
                params.put("Usuário", "Usuário Teste");
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


    private void checaMudanca() {

        final Response.Listener<String> onChecaMudanca = new Response.Listener<String>() {

            @Override
            public void onResponse(String response) {
                SharedPreferences prefs = context.getSharedPreferences("json", Context.MODE_PRIVATE);
                String json = prefs.getString("json", "");

                IOSingleton singleton = IOSingleton.Instance();
                singleton.mudanca(false);
                JsonParser parser = new JsonParser();
                JsonElement o1 = parser.parse(response);
                JsonElement o2 = parser.parse(json);
                if (!o1.equals(o2)) {
                    singleton.mudanca(true);
                    showNotification(context);
                }
            }
        };
        StringRequest request = new StringRequest(Request.Method.GET, ENDPOINT, onChecaMudanca, onPostsError);
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

            SharedPreferences prefs = context.getSharedPreferences("json", Context.MODE_PRIVATE);
            SharedPreferences.Editor editor = prefs.edit();
            editor.putString("json",IOSingleton.Instance().getPureResponse());
            editor.commit();
            refreshCurrentFragment();
        }
    };

    private void refreshCurrentFragment() {
        Fragment frg = null;
        if (context instanceof Activity) {
            frg = ((AppCompatActivity) context).getSupportFragmentManager().findFragmentByTag("Fragment");
            if (frg != null) {
                final FragmentTransaction ft = ((AppCompatActivity) context).getSupportFragmentManager().beginTransaction();
                ft.detach(frg);
                ft.attach(frg);
                ft.commit();
            }
        } else {
                Log.d("Contexto", String.valueOf(context.getClass()));
        }
    }

    private final Response.ErrorListener onPostsError = new Response.ErrorListener() {
        @Override
        public void onErrorResponse(VolleyError error) {
            String mensagem = "";
            if (error instanceof NoConnectionError){
                mensagem = "Não há conexão disponível, tente novamente.";
                showDialog(mensagem);
            }
            else if (error instanceof NetworkError) {
                mensagem = "Erro na rede, tente novamente.";
                showDialog(mensagem);
            }
        }
    };

    private void showDialog(String mensagem) {
        if (builder == null) {
            builder = new AlertDialog.Builder(context);
            builder.setMessage(mensagem);
            builder.show();
        }
    }

    private static void showNotification(Context context) {
        PendingIntent pi = PendingIntent.getActivity(context, 0, new Intent(context, Work4kits.class), 0);
        Notification notification = new NotificationCompat.Builder(context)
                .setTicker(context.getResources().getString(R.string.app_name))
                .setSmallIcon(android.R.drawable.ic_popup_reminder)
                .setContentTitle(context.getResources().getString(R.string.app_name))
                .setContentIntent(pi)
                .setOngoing(false)
                .setAutoCancel(true)
                .setContentText("Houve uma mudança nas suas vagas.")
                .build();
        NotificationManager notificationManager = (NotificationManager) context.getSystemService(context.NOTIFICATION_SERVICE);
        notificationManager.notify(0, notification);
    }


}
