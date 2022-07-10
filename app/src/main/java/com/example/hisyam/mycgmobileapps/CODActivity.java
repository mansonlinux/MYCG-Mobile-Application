package com.example.hisyam.mycgmobileapps;

import android.support.design.widget.AppBarLayout;
import android.support.design.widget.TabLayout;
import android.support.v4.view.ViewPager;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;

public class CODActivity extends AppCompatActivity {

    private TabLayout tabLayout;
    private AppBarLayout appBarLayout;
    private ViewPager viewPager;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_cod);
        tabLayout = findViewById(R.id.tablelayout_id);
        appBarLayout = findViewById(R.id.appbarid);
        viewPager = findViewById(R.id.ViewerPagerId);
        cod_fragment_ViewpagerAdapter adapter = new cod_fragment_ViewpagerAdapter(getSupportFragmentManager());
        //Adding Fragments
        adapter.AddFragment(new cod_info_fragment(),"Info");
        adapter.AddFragment(new cod_bracket_fragment(),"Bracket");
        adapter.AddFragment(new cod_team_fragment(),"Team");
        adapter.AddFragment(new cod_rules_fragment(),"Rules");
        //Adapter setup
        viewPager.setAdapter(adapter);
        tabLayout.setupWithViewPager(viewPager);
    }
}
