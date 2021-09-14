package com.example.comunicate;

import android.Manifest;
import android.app.NotificationChannel;
import android.app.NotificationManager;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.database.sqlite.SQLiteDatabase;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;
import androidx.core.app.NotificationCompat;
import androidx.core.content.ContextCompat;
import androidx.drawerlayout.widget.DrawerLayout;
import androidx.navigation.NavController;
import androidx.navigation.Navigation;
import androidx.navigation.ui.AppBarConfiguration;
import androidx.navigation.ui.NavigationUI;

import com.bumptech.glide.Glide;
import com.example.comunicate.databinding.ActivityMainBinding;
import com.example.comunicate.provider.BaseDatos;
import com.example.comunicate.sync.login.ContentLogin;
import com.example.comunicate.actividades.ActivityUsuarios;
import com.example.comunicate.utils.Usuario;
import com.google.android.material.navigation.NavigationView;

import de.hdodenhof.circleimageview.CircleImageView;


public class MainActivity extends AppCompatActivity {
    public static final String NOTIFICATION_CHANNEL_ID = "10001" ;
    private final static String default_notification_channel_id = "default" ;

    private static final String TABLE_USUARIO = "Usuario";

    private AppBarConfiguration mAppBarConfiguration;
    private ActivityMainBinding binding;
    BaseDatos bdLocal;
    Usuario usuario;
    String nombreUsuario, correoUsuario, ID;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        bdLocal = new BaseDatos(getApplicationContext());
        usuario = bdLocal.validarUsuario();
        if (usuario != null) {
            //  Toast.makeText(this, "el usuario es  "+usuario.getNombre(), Toast.LENGTH_SHORT).show();

            ID = String.valueOf(usuario.getId());
            nombreUsuario = usuario.getNombre();
            correoUsuario = usuario.getCorreo();

        }else {
            startActivity(new Intent(this, ContentLogin.class));
            finish();
            return;
        }

        binding = ActivityMainBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());

        setSupportActionBar(binding.appBarMain.toolbar);

        DrawerLayout drawer = binding.drawerLayout;
        NavigationView navigationView = binding.navView;
        View header = binding.navView.getHeaderView(0);
        TextView txt = header.findViewById(R.id.header_nombre);
        TextView correo = header.findViewById(R.id.header_correo);
        correo.setText(correoUsuario);
        CircleImageView cabeceraImg = header.findViewById(R.id.imageView);
        txt.setText(nombreUsuario);
        Glide.with(this)
                .asBitmap()
                .load(usuario.getUrlImagen())
                .error(R.drawable.ic_person_24)
                .centerCrop()
                .into(cabeceraImg);
        // Passing each menu ID as a set of Ids because each
        // menu should be considered as top level destinations.
        mAppBarConfiguration = new AppBarConfiguration.Builder(
                R.id.nav_home, R.id.nav_slideshow, R.id.nav_fragPerfil)
                .setOpenableLayout(drawer)
                .build();
     //   View header = navigationView.getHeaderView(0);
   //     CircleImageView cabeceraPerfil = header.findViewById(R.id.imageView);
        //.setImageBitmap(usuario.getImagen());
   /*    Glide.with(this)
                .load(usuario.getUrlImagen())
                .centerCrop()
                .into(cabeceraImg);
*/
      //  TextView txtNombreHeader = header.findViewById(R.id.header_nombre);
    //   txt.setText(usuario.getNombre());
        NavController navController = Navigation.findNavController(this, R.id.nav_host_fragment_content_main);
        NavigationUI.setupActionBarWithNavController(this, navController, mAppBarConfiguration);
        NavigationUI.setupWithNavController(navigationView, navController);

        if(ContextCompat.checkSelfPermission(this, Manifest.permission.CAMERA)
                != PackageManager.PERMISSION_GRANTED) {
            ActivityCompat.requestPermissions(this, new String[]{Manifest.permission.CAMERA, Manifest.permission.READ_EXTERNAL_STORAGE, Manifest.permission.WRITE_EXTERNAL_STORAGE}, 0);
        }
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.main, menu);

        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        int id = item.getItemId();

        switch (item.getItemId()) {
            case R.id.action_cerrar:
                cerrarSesion();
                break;
            case R.id.action_chat:
                Intent chatInt = new Intent(MainActivity.this, ActivityUsuarios.class);
              chatInt.putExtra("id", ID);
                startActivity(chatInt);
                break;
            case R.id.action_notifi:
                crearNotificacion();
              //  startActivity(new Intent(this, ActividadPrincipal.class));
                return true;
        }
        return super.onOptionsItemSelected(item);
    }


    private void crearNotificacion() {
        NotificationManager mNotificationManager = (NotificationManager) getSystemService( NOTIFICATION_SERVICE ) ;
        NotificationCompat.Builder mBuilder = new NotificationCompat.Builder(MainActivity. this, default_notification_channel_id ) ;
        mBuilder.setContentTitle( "Notify Me \uD83D\uDE00");
        mBuilder.setContentText( "Something important!" );
        mBuilder.setSmallIcon(R.drawable. ic_launcher_foreground );
        mBuilder.setAutoCancel( true ) ;
        if (android.os.Build.VERSION. SDK_INT >= android.os.Build.VERSION_CODES. O ) {
            int importance = NotificationManager. IMPORTANCE_HIGH ;
            NotificationChannel notificationChannel = new NotificationChannel(NOTIFICATION_CHANNEL_ID , "NOTIFICATION_CHANNEL_NAME" , importance) ;
            mBuilder.setChannelId(NOTIFICATION_CHANNEL_ID ) ;
            assert mNotificationManager != null;
            mNotificationManager.createNotificationChannel(notificationChannel) ;
        }
        assert mNotificationManager != null;
        mNotificationManager.notify(( int ) System.currentTimeMillis(), mBuilder.build()) ;
    }


    private void cerrarSesion() {
        bdLocal = new BaseDatos(this.getApplicationContext());
        SQLiteDatabase db = bdLocal.getReadableDatabase();
        db.execSQL("delete from " + TABLE_USUARIO);
        startActivity(new Intent(this, MainActivity.class));
    }


    @Override
    public boolean onSupportNavigateUp() {
        NavController navController = Navigation.findNavController(this, R.id.nav_host_fragment_content_main);
        return NavigationUI.navigateUp(navController, mAppBarConfiguration)
                || super.onSupportNavigateUp();
    }
}