package empsoft.liseuuniversitrio;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.ExpandableListAdapter;
import android.widget.ExpandableListView;
import android.widget.Toast;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Calendar;
import java.util.Date;
import java.util.HashMap;
import java.util.List;

import empsoft.liseuuniversitrio.models.Vaga;

public class MainActivity extends AppCompatActivity {

    ExpandableListView expandableListView;
    ExpandableListAdapter expandableListAdapter;
    List<String> expandableListTitle;
    HashMap<String, List<String>> expandableListDetail;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);


        expandableListView = (ExpandableListView) findViewById(R.id.list);

        List<Vaga> listVagas = new ArrayList<>();
        listVagas.add(new Vaga("Repositor", "Lorem ipsum dolor sit amet, ius ne cibo aliquid, has sent" +
                "entiae scripserit no. In malis putent intellegat pri, sed ne senserit quali" +
                "sque voluptaria. Vis nisl delenit no, eu pri omnium interesset. Has tota nostrum ut.\n" + "\n" +
                "Suas quodsi omnium ne eam, brute tollit cum ne. Et eos facer apeirian repudiare, iudico.",
                new Date(Calendar.getInstance().getTimeInMillis())));

        listVagas.add(new Vaga("Repositor 2", "Lorem ipsum dolor sit amet, ius ne cibo aliquid, has sent" +
                "entiae scripserit no. In malis putent intellegat pri, sed ne senserit quali" +
                "sque voluptaria. Vis nisl delenit no, eu pri omnium interesset. Has tota nostrum ut.\n" + "\n" +
                "Suas quodsi omnium ne eam, brute tollit cum ne. Et eos facer apeirian repudiare, iudico.",
                new Date(Calendar.getInstance().getTimeInMillis())));

        expandableListDetail = ExpandableListDataPump.getData(listVagas);

        expandableListTitle = new ArrayList<>(expandableListDetail.keySet());

        expandableListAdapter = new CustomExpandableListAdapter(this, expandableListTitle, expandableListDetail);

        expandableListView.setAdapter(expandableListAdapter);


    }

}