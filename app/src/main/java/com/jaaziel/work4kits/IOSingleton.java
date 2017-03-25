package com.jaaziel.work4kits;

import android.content.Context;
import android.content.SharedPreferences;

import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;

import java.io.UnsupportedEncodingException;
import java.lang.reflect.Type;
import java.net.URLEncoder;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * Created by Arthur on 17/03/2017.
 */

public class IOSingleton {

    private static IOSingleton instance;
    private List<Map<String, String>> response = new ArrayList<Map<String, String>>();
    private List<String> listDataHeader;
    private HashMap<String, List<String>> listDataChild;
    private List<Map<String, String>> vagasPendentes;
    private String pureResponse;
    private final int NOTIFICATION_CODE = 999;
    private boolean mudanca;

    private IOSingleton() {
    }

    public static IOSingleton Instance() {
        //if no instance is initialized yet then create new instance
        //else return stored instance
        if (instance == null) {
            instance = new IOSingleton();
            Map<String, String> map = new HashMap<>();
        }
        return instance;
    }

    public void saveResponse(String response) {
        this.pureResponse = response;
        Type type = new TypeToken<List<Map<String, String>>>() {
        }.getType();
        Gson gson = new Gson();
        List<Map<String, String>> responseJson = gson.fromJson(response, type);


        this.response = responseJson;
    }

    public List<Map<String, String>> getResponse() {
        return response;
    }

    public String getPureResponse() { return pureResponse; }


    public String[] getJobs(String diaDaSemana) {
        List<String> list = new ArrayList<>();
        for (Map<String, String> m : getResponse()) {
            if (m.get("DiaDaSemana").equals(diaDaSemana)) {
                list.add(m.get("id"));
            }
        }
        if (list.size() % 2 != 0) {
            list.add("");
        }
        String[] id = list.toArray(new String[0]);
        return id;
    }

    public List<String> getDays() {
        List<String> list = new ArrayList<>();
        for (Map<String, String> m : getResponse()) {
            list.add(m.get("DiaDaSemana"));
        }
        return list;
    }

    public Map<String, String> getJobDetails(String id) {
        for (Map<String, String> m : getResponse()) {
            if (m.get("id").equals(id)) {
                return m;
            }
        }
        return null;
    }

    public void changeStatus(String id) {
        for (Map<String, String> m : getResponse()) {
            if (m.get("id").equals(id)) {
                m.put("Status", "Solicitação enviada.");
                m.put("Usuário", "Usuário Teste");
            }
        }
    }

    public static byte[] encodeParameters(Map<String, String> params, String paramsEncoding) {
        StringBuilder encodedParams = new StringBuilder();
        try {
            for (Map.Entry<String, String> entry : params.entrySet()) {
                encodedParams.append(URLEncoder.encode(entry.getKey(), paramsEncoding));
                encodedParams.append('=');
                encodedParams.append(URLEncoder.encode(entry.getValue(), paramsEncoding));
                encodedParams.append('&');
            }
            encodedParams.replace(encodedParams.length() - 1, encodedParams.length(), "");

            return encodedParams.toString().getBytes(paramsEncoding);
        } catch (UnsupportedEncodingException uee) {
            throw new RuntimeException("Encoding not supported: " + paramsEncoding, uee);
        }
    }


    public HashMap<String, List<String>> getListDataChild() {
        return listDataChild;
    }

    public void setListDataChild(HashMap<String, List<String>> listDataChild) {
        this.listDataChild = listDataChild;
    }

    public List<String> getListDataHeader() {
        return listDataHeader;
    }

    public void setListDataHeader(List<String> listDataHeader) {
        this.listDataHeader = listDataHeader;
    }

    public List<Map<String, String>> getVagasPendentes() {
        List<Map<String, String>> list = new ArrayList<>();
        for (Map<String, String> m : getResponse()) {
            if (m.get("Status").equals("Solicitação enviada."))
                list.add(m);
        }
        return list;
    }

    public void aceitaVaga(String id) {
        for (Map<String, String> m : getResponse()) {
            if (m.get("id").equals(id)) {
                m.put("Status", "Aprovado.");
            }
        }
    }

    public void rejeitaVaga(String id) {
        for (Map<String, String> m : getResponse()) {
            if (m.get("id").equals(id)) {
                m.put("Status", "Rejeitado.");
            }
        }
    }

    public int getNOTIFICATION_CODE() {
        return NOTIFICATION_CODE;
    }

    public void mudanca(boolean mudanca) {
        this.mudanca = mudanca;
    }

    public boolean temMudanca() {
        return mudanca;
    }
}
