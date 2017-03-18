package com.jaaziel.work4kits;

import android.util.Log;

import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;

import java.lang.reflect.Type;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

/**
 * Created by Arthur on 17/03/2017.
 */

public class IOSingleton {

    private static IOSingleton instance;
    private List<Map<String, String>> response;
    //no outer class can initialize this class's object
    private IOSingleton() {}

    public static IOSingleton Instance()
    {
        //if no instance is initialized yet then create new instance
        //else return stored instance
        if (instance == null)
        {
            instance = new IOSingleton();
        }
        return instance;
    }

    public void saveResponse(String response) {
        Type type = new TypeToken<List<Map<String, String>>>(){}.getType();
        Gson gson = new Gson();
        List<Map<String, String>> responseJson = gson.fromJson(response, type);
        this.response = responseJson;
    }

    public List<Map<String, String>> getResponse() {
        return response;
    }

    public String[] getJobs (String diaDaSemana) {
        List<String> list = new ArrayList<>();
        for (Map<String,String> m : getResponse()) {
            if (m.get("DiaDaSemana").equals(diaDaSemana)) {
                list.add(m.get("id"));
            }
        }
        if (list.size() % 2 != 0) {
            list.add("");
        }
        String[] id  = list.toArray(new String[0]);
        return id;
    }

    public List<String> getDays() {
        List<String> list = new ArrayList<>();
        for (Map<String,String> m : getResponse()) {
            list.add(m.get("DiaDaSemana"));
        }
        return list;
    }

    public  Map<String, String> getJobDetails(String id) {
        for (Map<String, String> m : getResponse()) {
            if (m.get("id").equals(id)) {
                return m;
            }
        }
        return null;
    }


}
