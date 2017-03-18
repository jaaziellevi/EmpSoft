package com.jaaziel.work4kits;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.GridView;

import java.util.Arrays;
import java.util.List;
import java.util.Map;

/**
 * Created by Arthur on 18/03/2017.
 *
 */

public class EmpresarioFragment extends android.support.v4.app.Fragment {
    private List<String> vagasPendentes;

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.empresario_fragment, container, false);
        GridView gridView = (GridView) view.findViewById(R.id.gridView);

        List<String> listVagas = IOSingleton.Instance().getVagasPendentes();


        ArrayAdapter<String> adapter = new ArrayAdapter<>(getContext(),
                android.R.layout.simple_list_item_1, listVagas);
        gridView.setAdapter(adapter);
        return view;
    }

}
