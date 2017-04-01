package com.jaaziel.work4kits;

import android.app.Dialog;
import android.content.Context;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.view.WindowManager;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.GridView;
import android.widget.ListView;
import android.widget.TextView;

import com.android.volley.Request;
import com.android.volley.toolbox.Volley;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

/**
 * Created by Arthur on 18/03/2017.
 *
 */

public class EmpresarioFragment extends android.support.v4.app.Fragment {
    private List<Map<String, String>> listVagas;
    private ArrayList<String> listVagasStringFormated;
    private ArrayAdapter<String> adapter;
    private ListView listView;

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.empresario_fragment, container, false);
        listView = (ListView) view.findViewById(R.id.listviewEmpresario);
        setHasOptionsMenu(true);

        listVagas = IOSingleton.Instance().getVagasPendentes();

        listVagasStringFormated = new ArrayList<>();

        for (Map<String,String> m : listVagas){
            listVagasStringFormated.add("Vaga: "+ m.get("Vaga") + "\n" +
                    "Candidato: "+ m.get("Usuário") + "\n" +
            "Status: " + m.get("Status") + "\n");
        }

        adapter = new ArrayAdapter<>(getContext(),
                android.R.layout.simple_list_item_1, listVagasStringFormated);
        listView.setAdapter(adapter);


        listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {
                if (listVagas.get(i).get("Status").equals("Trabalho em andamento.")){
                    Log.d("E", listVagas.get(i).get("Status"));

                    showConfirmarDialog(getContext(), listVagas.get(i));
                } else if (listVagas.get(i).get("Status").equals("Esperando aprovação.")) {
                    showAprovarDialog(getContext(), listVagas.get(i));
                }

            }
        });

        return view;
    }

    private void removeVagas() {
        listVagas = IOSingleton.Instance().getVagasPendentes();
        listVagasStringFormated = new ArrayList<>();

        for (Map<String,String> m : listVagas){
            listVagasStringFormated.add("\nVaga: "+ m.get("Vaga") + "\n" +
                    "Candidato: "+ m.get("Usuário") + m.get("Status"));
        }

        adapter = new ArrayAdapter<>(getContext(),
                android.R.layout.simple_list_item_1, listVagasStringFormated);

        listView.setAdapter(adapter);
        adapter.notifyDataSetChanged();
    }

    public void showAprovarDialog(final Context context, final Map<String, String> details){
        final Dialog dialog = new Dialog(context);
        dialog.setContentView(R.layout.vagaaceitadialog);

        WindowManager.LayoutParams lp = new WindowManager.LayoutParams();
        lp.copyFrom(dialog.getWindow().getAttributes());
        lp.width = WindowManager.LayoutParams.WRAP_CONTENT;
        lp.height = WindowManager.LayoutParams.WRAP_CONTENT;
        dialog.getWindow().setAttributes(lp);

        final String vagaId = details.get("id");
        String nomeVaga = details.get("Vaga");
        String usuario = details.get("Usuário");


        ((TextView) dialog.findViewById(R.id.vagaAceitaVaga)).setText("Vaga: " + nomeVaga);
        ((TextView) dialog.findViewById(R.id.vagaAceitaUsuario)).setText("Usuário: " + usuario);

        dialog.findViewById(R.id.vagaAceitaOk).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                RESTUtil ut = new RESTUtil(Volley.newRequestQueue(context), Request.Method.PATCH, vagaId, "APROVADO", getContext());
                IOSingleton.Instance().aceitaVaga(vagaId);
                removeVagas();
                dialog.dismiss();
            }
        });

        dialog.findViewById(R.id.vagaAceitaRecusa).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                RESTUtil ut = new RESTUtil(Volley.newRequestQueue(context), Request.Method.PATCH, vagaId, "REJEITADO", getContext());
                IOSingleton.Instance().rejeitaVaga(vagaId);
                removeVagas();
                dialog.dismiss();
            }
        });
        dialog.show();
    }

    private void showConfirmarDialog(final Context context, Map<String, String> details) {
        final Dialog dialog = new Dialog(context);
        dialog.setContentView(R.layout.vagaconfirmardialog);

        WindowManager.LayoutParams lp = new WindowManager.LayoutParams();
        lp.copyFrom(dialog.getWindow().getAttributes());
        lp.width = WindowManager.LayoutParams.WRAP_CONTENT;
        lp.height = WindowManager.LayoutParams.WRAP_CONTENT;
        dialog.getWindow().setAttributes(lp);

        final String vagaId = details.get("id");
        String nomeVaga = details.get("Vaga");
        String usuario = details.get("Usuário");


        ((TextView) dialog.findViewById(R.id.vagaConfirmaUsuario)).setText("Vaga: " + nomeVaga);
        ((TextView) dialog.findViewById(R.id.vagaConfirmaVaga)).setText("Usuário: " + usuario);

        dialog.findViewById(R.id.vagaConfirmarOk).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                RESTUtil ut = new RESTUtil(Volley.newRequestQueue(context), Request.Method.PATCH, vagaId,
                        "TRABALHO_CONFIRMADO", getContext());
                IOSingleton.Instance().confirmaTrabalho(vagaId);
                removeVagas();
                dialog.dismiss();
            }
        });
        dialog.show();
    }

    @Override
    public void onPrepareOptionsMenu(Menu menu) {
        MenuItem item=menu.findItem(R.id.action_notifications);
        item.setVisible(false);
        super.onPrepareOptionsMenu(menu);
    }
}
