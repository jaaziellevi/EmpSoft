package com.jaaziel.work4kits;

import android.app.Dialog;
import android.content.Context;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.view.WindowManager;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.ListView;
import android.widget.TextView;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

/**
 * Created by Arthur on 26/03/2017.
 */

public class TrabalhoInfoFragment extends Fragment {
    private ArrayList<String> trabalhosNomes;
    private ArrayList<Integer> trabalhoIndex;
    private List<Map<String, String>> listTrabalhosAprovados;

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.trabalhoinfo_fragment, container, false);
        setHasOptionsMenu(true);

        trabalhosNomes = new ArrayList<>();
        trabalhoIndex = new ArrayList<>();
        listTrabalhosAprovados = IOSingleton.Instance().getTrabalhosAprovados();

        for (int i = 0; i < listTrabalhosAprovados.size(); i++) {
            Map<String,String> trabalhoAprovado = listTrabalhosAprovados.get(i);
            trabalhoIndex.add(i);
            trabalhosNomes.add(trabalhoAprovado.get("Vaga")+'\n'+
                    "Horário: "+trabalhoAprovado.get("Horário") + "\n" +
                    "Dia da Semana: "+trabalhoAprovado.get("DiaDaSemana") +"\n");
        }

        ListView trabalhoListView = (ListView) view.findViewById(R.id.listViewTrabalhoInfo);
        ArrayAdapter adapter = new ArrayAdapter<>(getContext(),
                android.R.layout.simple_list_item_1, trabalhosNomes);

        trabalhoListView.setAdapter(adapter);

        trabalhoListView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {
                showDialog(getContext(), listTrabalhosAprovados.get(trabalhoIndex.get(i)));
            }
        });

        return view;
    }

    public void showDialog(final Context context, final Map<String, String> details){
        final Dialog dialog = new Dialog(context);
        dialog.setContentView(R.layout.vagadiallog);

        WindowManager.LayoutParams lp = new WindowManager.LayoutParams();
        lp.copyFrom(dialog.getWindow().getAttributes());
        lp.width = WindowManager.LayoutParams.WRAP_CONTENT;
        lp.height = WindowManager.LayoutParams.WRAP_CONTENT;
        dialog.getWindow().setAttributes(lp);

        final String vagaId = details.get("id");
        String nomeVaga = details.get("Vaga");
        String descricao = details.get("Descrição");
        String horario = details.get("Horário");
        String empresa = details.get("Empresa");


        ((TextView) dialog.findViewById(R.id.vagaNome)).setText("Vaga: " + nomeVaga);
        ((TextView) dialog.findViewById(R.id.vagaEmpresa)).setText("Empresa: " + empresa + "\n" +
                "Telefone para Contato: (xx)xxxx-xxxx");
        ((TextView) dialog.findViewById(R.id.vagaHorario)).setText("Horário: " + horario);
        ((TextView) dialog.findViewById(R.id.vagaDescricao)).setText("Descrição: " + descricao);

        Button voltar = ((Button) dialog.findViewById(R.id.vagaRejeita));
        voltar.setText("Voltar");
        dialog.findViewById(R.id.vagaRejeita).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                dialog.dismiss();
            }
        });
        dialog.findViewById(R.id.vagaKit).setVisibility(View.INVISIBLE);
        dialog.findViewById(R.id.vagaAceita).setVisibility(View.INVISIBLE);
        dialog.show();
    }


    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        super.onOptionsItemSelected(item);

        int id = item.getItemId();
        if (id == R.id.action_notifications){
            Fragment newFragment = new UsuarioComumFragment();
            getActivity().getSupportFragmentManager().beginTransaction().replace(R.id.content_main, newFragment, "Fragment")
                    .commit();
        }
        return true;
    }

    @Override
    public void onPrepareOptionsMenu(Menu menu) {
        MenuItem notifications = menu.findItem(R.id.action_notifications);

        notifications.setVisible(true);
        super.onPrepareOptionsMenu(menu);
    }
}
