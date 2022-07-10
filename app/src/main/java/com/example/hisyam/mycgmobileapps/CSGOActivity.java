package com.example.hisyam.mycgmobileapps;

import android.support.design.widget.AppBarLayout;
import android.support.design.widget.TabLayout;
import android.support.v4.view.ViewPager;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;

public class CSGOActivity extends AppCompatActivity {

    private TabLayout tabLayout;
    private AppBarLayout appBarLayout;
    private ViewPager viewPager;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_csgo);
        tabLayout = findViewById(R.id.tablelayout_id);
        appBarLayout = findViewById(R.id.appbarid);
        viewPager = findViewById(R.id.ViewerPagerId);
        csgo_fragment_ViewpagerAdapter adapter = new csgo_fragment_ViewpagerAdapter(getSupportFragmentManager());
        //Adding Fragments
        adapter.AddFragment(new csgo_info_fragment(),"Info");
        adapter.AddFragment(new csgo_bracket_fragment(),"Bracket");
        adapter.AddFragment(new csgo_team_fragment(),"Team");
        adapter.AddFragment(new csgo_rules_fragment(),"Rules");
        //Adapter setup
        viewPager.setAdapter(adapter);
        tabLayout.setupWithViewPager(viewPager);
    }
}
