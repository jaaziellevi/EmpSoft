package empsoft.liseuuniversitario;

import android.os.Build;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ExpandableListView;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.HashMap;
import java.util.List;

import empsoft.liseuuniversitario.models.Vaga;

/**
 * Created by Arthur on 12/03/2017.
 */

public class VagasFragment extends Fragment {

    private ExpandableListView expandableListView;
    private HashMap<String, List<String>> expandableListDetail;
    private ArrayList<String> expandableListTitle;
    private CustomExpandableListAdapter expandableListAdapter;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View rootView = inflater.inflate(R.layout.fragment_main, container, false);

        expandableListView = (ExpandableListView) rootView.findViewById(R.id.list);

        List<Vaga> listVagas = new ArrayList<>();
        listVagas.add(new Vaga("Motorista", "Lorem ipsum dolor sit amet, ius ne cibo aliquid, has sent" +
                "entiae scripserit no. In malis putent intellegat pri, sed ne senserit quali" +
                "sque voluptaria. Vis nisl delenit no, eu pri omnium interesset. Has tota nostrum ut.\n" + "\n" +
                "Suas quodsi omnium ne eam, brute tollit cum ne. Et eos facer apeirian repudiare, iudico.",
                new Date(Calendar.getInstance().getTimeInMillis())));

        listVagas.add(new Vaga("Repositor", "Lorem ipsum dolor sit amet, ius ne cibo aliquid, has sent" +
                "entiae scripserit no. In malis putent intellegat pri, sed ne senserit quali" +
                "sque voluptaria. Vis nisl delenit no, eu pri omnium interesset. Has tota nostrum ut.\n" + "\n" +
                "Suas quodsi omnium ne eam, brute tollit cum ne. Et eos facer apeirian repudiare, iudico.",
                new Date(Calendar.getInstance().getTimeInMillis())));

        listVagas.add(new Vaga("Limpeza", "Lorem ipsum dolor sit amet, ius ne cibo aliquid, has sent" +
                "entiae scripserit no. In malis putent intellegat pri, sed ne senserit quali" +
                "sque voluptaria. Vis nisl delenit no, eu pri omnium interesset. Has tota nostrum ut.\n" + "\n" +
                "Suas quodsi omnium ne eam, brute tollit cum ne. Et eos facer apeirian repudiare, iudico.",
                new Date(Calendar.getInstance().getTimeInMillis())));

        expandableListDetail = ExpandableListDataPump.getData(listVagas);

        expandableListTitle = new ArrayList<>(expandableListDetail.keySet());

        expandableListAdapter = new CustomExpandableListAdapter(getContext(), expandableListTitle, expandableListDetail);

        expandableListView.setAdapter(expandableListAdapter);

        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
            expandableListView.setNestedScrollingEnabled(true);
        }

        return rootView;
    }
}
