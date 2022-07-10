package com.example.hisyam.mycgmobileapps;

import android.support.design.widget.AppBarLayout;
import android.support.design.widget.TabLayout;
import android.support.v4.view.ViewPager;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;

public class Events2Activity extends AppCompatActivity {

    private TabLayout tabLayout;
    private AppBarLayout appBarLayout;
    private ViewPager viewPager;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_events2);

        tabLayout = findViewById(R.id.tablelayout_id);
        appBarLayout = findViewById(R.id.appbarid);
        viewPager = findViewById(R.id.ViewerPagerId);
        events2_fragment_ViewpagerAdapter adapter = new events2_fragment_ViewpagerAdapter(getSupportFragmentManager());
        //Adding Fragments
        adapter.AddFragment(new events2_info_fragment(),"Info");
        adapter.AddFragment(new events2_register_fragment(),"Register Ticket");
        //Adapter setup
        viewPager.setAdapter(adapter);
        tabLayout.setupWithViewPager(viewPager);
    }
}
