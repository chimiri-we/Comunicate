package com.example.comunicate.sync.login;

import android.content.Intent;
import android.os.Bundle;
import android.view.Menu;
import android.view.View;

import androidx.appcompat.app.AppCompatActivity;
import androidx.viewpager.widget.ViewPager;

import com.example.comunicate.MainActivity;
import com.example.comunicate.R;
import com.google.android.material.tabs.TabLayout;

public class ContentRegistro extends AppCompatActivity {
    private TabLayout tabLayout;
    private ViewPager viewPager;


    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.main);
Intent in = getIntent();
String ID = in.getStringExtra("id");
String NOMBRE = in.getStringExtra("nombre");



        viewPager = (ViewPager) findViewById(R.id.viewPagerRegistro);
//        tabLayout.setupWithViewPager(viewPager);
        viewPager.setAdapter(new AdapterRegistros(getSupportFragmentManager()));
        viewPager.addOnPageChangeListener(new ViewPager.OnPageChangeListener() {
            @Override
            public void onPageScrolled(int position, float positionOffset, int positionOffsetPixels) {

            }

            @Override
            public void onPageSelected(int position) {
                if(position==0){
                    //setTitle("Chat");
                }else if(position==1){
                    //setTitle("Registro");
                }
            }

            @Override
            public void onPageScrollStateChanged(int state) {

            }
        });
        viewPager.setOffscreenPageLimit(3);
    }

    @Override
    protected void onResume() {
        super.onResume();
        //  actualizarSolicitudes();
    }




    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.menu_activity_amigos,menu);
        return super.onCreateOptionsMenu(menu);
    }

    public void siguiente(View view) {
        Intent intent = new Intent(this, MainActivity.class);
        startActivity(intent);
    }
}
