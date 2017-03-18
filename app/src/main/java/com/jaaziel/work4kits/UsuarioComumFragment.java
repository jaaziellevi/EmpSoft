package com.jaaziel.work4kits;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ExpandableListView;
import android.widget.Toast;

import com.android.volley.Request;
import com.android.volley.toolbox.Volley;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;

/**
 * Created by Arthur on 18/03/2017.
 */

public class UsuarioComumFragment extends Fragment {
    private ExpandableListView mExpandablelistView;
    private List<String> listDataHeader;
    private HashMap<String, List<String>> listDataChild;
    private ExpandableListAdapter mExpandableListAdapter;
    private int lastExpandedPosition = -1;

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view=inflater.inflate(R.layout.usuario_comum_fragment, container,false);

        listDataChild = IOSingleton.Instance().getListDataChild();
        listDataHeader = IOSingleton.Instance().getListDataHeader();

        // get the listview
        mExpandablelistView = (ExpandableListView) view.findViewById(R.id.expandablelistview);


        mExpandableListAdapter = new ExpandableListAdapter(getContext(),
                listDataHeader, listDataChild);

        // setting list adapter
        mExpandablelistView.setAdapter(mExpandableListAdapter);

        mExpandablelistView.setOnGroupClickListener(new ExpandableListView.OnGroupClickListener() {
            @Override
            public boolean onGroupClick(ExpandableListView parent, View v,
                                        int groupPosition, long id) {
                return false; // This way the expander cannot be collapsed
            }
        });

        mExpandablelistView.setOnGroupExpandListener(new ExpandableListView.OnGroupExpandListener() {
            @Override
            public void onGroupExpand(int i) {
                String[] d = IOSingleton.Instance().getJobs((String) mExpandableListAdapter.getGroup(i));
                Log.d("T", String.valueOf(d.length));
                if (d.length <= 1) {
                    mExpandablelistView.collapseGroup(i);
                    Toast.makeText(getContext(), "Não há vaga para esse dia", Toast.LENGTH_SHORT).show();
                }
                if (lastExpandedPosition != -1
                        && i != lastExpandedPosition) {
                    mExpandablelistView.collapseGroup(lastExpandedPosition);
                }
                lastExpandedPosition = i;
            }
        });

        return view;
    }

}
