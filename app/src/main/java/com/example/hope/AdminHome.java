package com.example.hope;

import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.FragmentPagerAdapter;
import androidx.viewpager.widget.ViewPager;

import android.os.Bundle;

import com.google.android.material.tabs.TabLayout;

public class AdminHome extends AppCompatActivity {

    private TabLayout tabLayout;
    private ViewPager viewPager;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_admin_home);

        tabLayout = findViewById(R.id.admintablayout);
        viewPager = findViewById(R.id.adminviewpager);
        tabLayout.setupWithViewPager(viewPager);

        AdminVPAdapter adminVPAdapter = new AdminVPAdapter(getSupportFragmentManager(), FragmentPagerAdapter.BEHAVIOR_RESUME_ONLY_CURRENT_FRAGMENT);

        adminVPAdapter.addFragment(new AdminUserApprovalFragment(),"VOLUNTEER REGISTRATION");
        adminVPAdapter.addFragment(new AdminActivityApprovalFragment(),"VOLUNTEER REQUEST");
        adminVPAdapter.addFragment(new AdminLogoutFragment(),"ADMIN LOGOUT");

        viewPager.setAdapter(adminVPAdapter);

    }
}