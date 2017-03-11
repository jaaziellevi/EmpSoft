package empsoft.liseuuniversitrio;

/**
 * Created by Arthur on 11/03/2017.
 */

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

import empsoft.liseuuniversitrio.models.Vaga;

public class ExpandableListDataPump {


    public static HashMap<String, List<String>> getData(List<Vaga> vagasList) {
        HashMap<String, List<String>> expandableListDetail = new HashMap<String, List<String>>();

        for (Vaga v : vagasList) {
            List<String> descricaoVaga = new ArrayList<>();
            descricaoVaga.add(v.getDescricao());
            descricaoVaga.add(v.getHorario().toString());

            expandableListDetail.put(v.getCargo(), descricaoVaga);
        }
        return expandableListDetail;
    }
}
