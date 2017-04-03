package com.jaaziel.work4kits;

import android.content.Context;
import android.content.SharedPreferences;
import android.graphics.drawable.Drawable;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v4.content.ContextCompat;
import android.support.v4.view.MenuItemCompat;
import android.support.v7.app.ActionBar;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.view.animation.AnimationUtils;
import android.widget.ExpandableListView;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.android.volley.Request;
import com.android.volley.toolbox.Volley;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

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
        View view = inflater.inflate(R.layout.usuario_comum_fragment, container, false);

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
        setHasOptionsMenu(true);
        return view;
    }

    @Override
    public void onPrepareOptionsMenu(Menu menu) {
        IOSingleton singleton = IOSingleton.Instance();
        final MenuItem notifItem = menu.findItem(R.id.action_notifications);
        notifItem.setActionView(R.layout.badgelayout);
        RelativeLayout notifCount = (RelativeLayout) MenuItemCompat.getActionView(notifItem);

        int aprovados = IOSingleton.Instance().getTrabalhosAprovados().size();
        Drawable drawable = ContextCompat.getDrawable(getActivity(), R.drawable.ic_notifications_active_white_24dp);


        SharedPreferences prefs = getContext().getSharedPreferences("json", Context.MODE_PRIVATE);
        int aprovadosOld = prefs.getInt("aprovados" , 0);

        if (aprovados >= 0) {
            notifItem.setIcon(drawable);
            notifItem.setTitle(String.valueOf(aprovados));

            TextView tv = (TextView) notifCount.findViewById(R.id.actionbar_notifcation_textview);
            tv.setText(String.valueOf(singleton.getTrabalhosAprovados().size()));

            prefs = getContext().getSharedPreferences("json", Context.MODE_PRIVATE);
            SharedPreferences.Editor editor = prefs.edit();
            editor.putInt("aprovados", aprovados);
            editor.commit();
            Log.d("Aprovados: ", aprovados+"ao: "+aprovadosOld);
            if (aprovados > aprovadosOld) {
                AlertDialog.Builder builder = new AlertDialog.Builder(getContext());
                builder.setMessage("Você teve uma vaga aprovada, clique na notificação para ver mais");
                builder.show();
            }

        }

        notifItem.getActionView().findViewById(R.id.badgeImageView).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                onOptionsItemSelected(notifItem);
            }
        });

        MenuItem notifications = menu.findItem(R.id.action_notifications);

        notifications.setVisible(true);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        if (item.getItemId() == R.id.action_notifications){
            item.getActionView().startAnimation(AnimationUtils.loadAnimation(getContext(), R.anim.anim));

            Fragment newFragment = new TrabalhoInfoFragment();
            getActivity().getSupportFragmentManager().beginTransaction().replace(R.id.content_main, newFragment, "Fragment")
                    .commit();

        }

        return true;
    }

}
