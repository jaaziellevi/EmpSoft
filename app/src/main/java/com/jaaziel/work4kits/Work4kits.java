package com.jaaziel.work4kits;

import android.content.Context;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.support.design.widget.NavigationView;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;
import android.support.v4.view.GravityCompat;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBarDrawerToggle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.animation.AnimationUtils;
import android.widget.ImageView;
import android.widget.Toast;

import com.android.volley.Request;
import com.android.volley.toolbox.Volley;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;

public class Work4kits extends AppCompatActivity
        implements NavigationView.OnNavigationItemSelectedListener {

    private List<String> listDataHeader;
    private HashMap<String, List<String>> listDataChild;
    private ImageView refreshView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {

        prepareListData();
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_work4kits);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);


        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        ActionBarDrawerToggle toggle = new ActionBarDrawerToggle(
                this, drawer, toolbar, R.string.navigation_drawer_open, R.string.navigation_drawer_close);
        drawer.setDrawerListener(toggle);
        toggle.syncState();


        NavigationView navigationView = (NavigationView) findViewById(R.id.nav_view);
        navigationView.setNavigationItemSelectedListener(this);

        refreshView = (ImageView) findViewById(R.id.imageView2);

        refreshView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Toast.makeText(Work4kits.this, "Atualizando...", Toast.LENGTH_LONG).show();
                v.startAnimation(AnimationUtils.loadAnimation(Work4kits.this, R.anim.anim));
                prepareListData();
            }
        });
    }

    @Override
    public void onBackPressed() {
        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        if (drawer.isDrawerOpen(GravityCompat.START)) {
            drawer.closeDrawer(GravityCompat.START);
        } else {
            super.onBackPressed();
        }
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.work4kits, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();


        return super.onOptionsItemSelected(item);
    }

    @SuppressWarnings("StatementWithEmptyBody")
    @Override
    public boolean onNavigationItemSelected(MenuItem item) {
        // Handle navigation view item clicks here.
        int id = item.getItemId();
        Fragment newFragment = null;

        if (id == R.id.nav_camera) {
            newFragment = new EmpresarioFragment();
        }
        else if (id == R.id.nav_gallery) {
            newFragment = new UsuarioComumFragment();
        }
        FragmentManager fragmentManager = getSupportFragmentManager();

        if (newFragment != null) {
            fragmentManager.beginTransaction().replace(R.id.content_main, newFragment, "Fragment").commit();
        }

        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        drawer.closeDrawer(GravityCompat.START);
        return true;
    }


    private void prepareListData() {
        listDataHeader = new ArrayList<String>();
        listDataChild = new HashMap<String, List<String>>();

        // Adding child data
        listDataHeader.add("Segunda-Feira");
        listDataHeader.add("Terça-Feira");
        listDataHeader.add("Quarta-Feira");
        listDataHeader.add("Quinta-Feira");
        listDataHeader.add("Sexta-Feira");
        listDataHeader.add("Sábado");


        listDataChild.put(listDataHeader.get(0), Arrays.asList("String"));
        listDataChild.put(listDataHeader.get(1), Arrays.asList("String"));
        listDataChild.put(listDataHeader.get(3), Arrays.asList("String"));
        listDataChild.put(listDataHeader.get(4), Arrays.asList("String"));
        listDataChild.put(listDataHeader.get(5), Arrays.asList("String"));

        IOSingleton.Instance().setListDataHeader(listDataHeader);
        IOSingleton.Instance().setListDataChild(listDataChild);

        RESTUtil ut = new RESTUtil(Volley.newRequestQueue(Work4kits.this), Request.Method.GET, Work4kits.this);


    }

}
