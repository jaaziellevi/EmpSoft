package empsoft.liseuuniversitario;


import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;

import empsoft.liseuuniversitario.models.Vaga;

public class ExpandableListDataPump {


    public static HashMap<String, List<String>> getData(List<Vaga> vagasList) {
        HashMap<String, List<String>> expandableListDetail = new HashMap<>();

        for (Vaga v : vagasList) {
            List<String> descricaoVaga = new ArrayList<>();
            descricaoVaga.add(v.getDescricao());
            String formatada = formatDate(v.getHorario());
            descricaoVaga.add("Horário: " + formatDate(v.getHorario()));

            expandableListDetail.put(v.getCargo(), descricaoVaga);
        }
        return expandableListDetail;
    }

    private static String formatDate(Date date) {
        SimpleDateFormat formatHoras = new SimpleDateFormat("HH:mm");
        SimpleDateFormat formatDia = new SimpleDateFormat("dd/MM/yyyy");
        String horas = formatHoras.format(date);
        String dias = formatDia.format(date);
        return horas + " do dia " + dias;
    }
}
