package org.ieselcaminas.daniel.viajes;

import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.res.Resources;
import android.graphics.Typeface;
import android.preference.PreferenceManager;
import android.support.annotation.NonNull;
import android.support.design.widget.NavigationView;
import android.support.v4.app.Fragment;
import android.os.Bundle;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBarDrawerToggle;
import android.support.v7.widget.Toolbar;
import android.util.DisplayMetrics;
import android.util.Log;
import android.view.Menu;
import android.widget.TextView;
import android.view.MenuItem;

import com.firebase.ui.auth.AuthUI;
import com.firebase.ui.auth.IdpResponse;
import com.google.android.gms.auth.api.signin.GoogleSignIn;
import com.google.android.gms.auth.api.signin.GoogleSignInClient;
import com.google.android.gms.auth.api.signin.GoogleSignInOptions;
import com.google.android.gms.common.api.Status;
import com.google.android.gms.location.places.ui.PlaceAutocomplete;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.luseen.simplepermission.permissions.PermissionActivity;

import java.util.Arrays;
import java.util.List;
import java.util.Locale;

public class MapsActivity extends PermissionActivity {
    private static FirebaseAuth mAuth;
    private FirebaseAuth.AuthStateListener mAuthListener;
    private GoogleSignInClient mGoogleSignInClient;
    private static final int RC_SIGN_IN = 123;
    private SharedPreferences pref;
    private NavigationView navView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        pref = PreferenceManager.getDefaultSharedPreferences(this);

        if(pref!=null) {
            if (pref.getString("option3", "").equals("English")) {
                Resources res2 = getApplicationContext().getResources();
                DisplayMetrics dm2 = res2.getDisplayMetrics();
                android.content.res.Configuration conf2 = res2.getConfiguration();
                conf2.locale = new Locale("en");
                res2.updateConfiguration(conf2, dm2);


            } else if (pref.getString("option3", "").equals("Spanish")) {
                Resources res2 = getApplicationContext().getResources();
                DisplayMetrics dm2 = res2.getDisplayMetrics();
                android.content.res.Configuration conf2 = res2.getConfiguration();
                conf2.locale = new Locale("es");
                res2.updateConfiguration(conf2, dm2);
            }
        }
        setContentView(R.layout.main);

        final DrawerLayout drawerLayout = (DrawerLayout) findViewById(R.id.drawer_layout);
         navView = (NavigationView) findViewById(R.id.navview);
        final Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        final Typeface typeface = Typeface.createFromAsset(getAssets(), "Xelita.ttf");
        final TextView mTitle = (TextView) toolbar.findViewById(R.id.toolbar_title);
        mTitle.setTypeface(typeface);
        toolbar.setTitle("");
        setSupportActionBar(toolbar);
        mAuth = FirebaseAuth.getInstance();

        mAuthListener = new FirebaseAuth.AuthStateListener() {
            @Override
            public void onAuthStateChanged(@NonNull FirebaseAuth firebaseAuth) {
                FirebaseUser user = firebaseAuth.getCurrentUser();
                if (user != null) {
                    // User is signed in
                    Log.d("Firebase", "onAuthStateChanged:signed_in:" + user.getUid());
                } else {
                    // User is signed out
                    Log.d("Firebase", "onAuthStateChanged:signed_out");

                    List<AuthUI.IdpConfig> providers = Arrays.asList(
                            new AuthUI.IdpConfig.Builder(AuthUI.EMAIL_PROVIDER).build(),
                            new AuthUI.IdpConfig.Builder(AuthUI.GOOGLE_PROVIDER).build());
                    // Create and launch sign-in intent
                    startActivityForResult(
                            AuthUI.getInstance()
                                    .createSignInIntentBuilder()
                                    .setAvailableProviders(providers)
                                    .setLogo(R.drawable.travel)
                                    .setIsSmartLockEnabled(false)
                                    .setTheme(R.style.Theme_AppCompat_DayNight_NoActionBar)
                                    .build(),
                            RC_SIGN_IN);

                }
                // ...
            }
        };

        Fragment f = new FragmentInicio();

        if (savedInstanceState == null) {
            getSupportFragmentManager()
                    .beginTransaction()
                    .add(R.id.content_frame, f)
                    .commit();
        }

        ActionBarDrawerToggle toggle = new ActionBarDrawerToggle(MapsActivity.this, drawerLayout, toolbar, 0, 0);
        toggle.syncState();
        toggle.getDrawerArrowDrawable().setColor(getColor(R.color.titulo));
        drawerLayout.addDrawerListener(toggle);

        navView.setNavigationItemSelectedListener(new NavigationView.OnNavigationItemSelectedListener() {

            @Override
            public boolean onNavigationItemSelected(MenuItem menuItem) {
                boolean fragmentTransaction = false;
                Fragment fragment = null;
                switch (menuItem.getItemId()) {
                    case R.id.menu_seccion_inicio:
                        fragment = new FragmentInicio();
                        fragmentTransaction = true;
                        mTitle.setText(R.string.menuInicio);
                        break;
                    case R.id.menu_seccion_rutas:
                        fragment = new RutasFragment();
                        fragmentTransaction = true;
                        mTitle.setText(R.string.tusRutas);
                        break;
                    case R.id.menu_seccion_config:
                        Intent i = new Intent(MapsActivity.this, PreferencesActivity.class);
                        startActivity(i);
                        break;
                    case R.id.menu_seccion_cerrar:
                        MapsActivity.mAuth.signOut();

                        GoogleSignInOptions gso = new GoogleSignInOptions.Builder(GoogleSignInOptions.DEFAULT_SIGN_IN)
                                .requestIdToken(getString(R.string.default_web_client_id))
                                .requestEmail()
                                .build();

                        mGoogleSignInClient = GoogleSignIn.getClient(getApplicationContext(), gso);
                        mGoogleSignInClient.signOut();
                        break;

                    case R.id.menu_seccion_acerca:
                        AlertDialog.Builder alert = new AlertDialog.Builder(MapsActivity.this);
                        final String  message = "Easy Way has been developed by" + "\n" + "Dani Albiol.";

                        alert.setMessage(message)
                                .setTitle("Info")
                                .setPositiveButton("OK", new DialogInterface.OnClickListener() {
                                    @Override
                                    public void onClick(DialogInterface dialog, int which) {
                                        dialog.dismiss();
                                    }
                                });
                        alert.create().show();
                        break;
                }
                if (fragmentTransaction) {
                    getSupportFragmentManager().beginTransaction().replace(R.id.content_frame, fragment).commit();
                    menuItem.setChecked(true);
                    //getSupportActionBar().setTitle(mTitle.getText().toString());
                }
                drawerLayout.closeDrawers();
                return true;
            }
        });
    }

    @Override
    public void onResume() {
        super.onResume();
        if (pref != null) {
            if (pref.getString("option3", "").equals("English")) {
                Resources res2 = getApplicationContext().getResources();
                DisplayMetrics dm2 = res2.getDisplayMetrics();
                android.content.res.Configuration conf2 = res2.getConfiguration();
                conf2.locale = new Locale("en");
                res2.updateConfiguration(conf2, dm2);
                Menu menu = navView.getMenu();
                MenuItem nav_seccion = menu.findItem(R.id.navigation_header1);
                MenuItem nav_seccion2 = menu.findItem(R.id.navigation_header2);
                MenuItem nav_inicio = menu.findItem(R.id.menu_seccion_inicio);
                MenuItem nav_rutas = menu.findItem(R.id.menu_seccion_rutas);
                MenuItem nav_config = menu.findItem(R.id.menu_seccion_config);
                MenuItem nav_logout = menu.findItem(R.id.menu_seccion_cerrar);
                MenuItem nav_about = menu.findItem(R.id.menu_seccion_acerca);

                nav_inicio.setTitle(R.string.menuInicio);
                nav_rutas.setTitle(R.string.tusRutas);
                nav_config.setTitle(R.string.configuracion);
                nav_logout.setTitle(R.string.cerrarSesion);
                nav_seccion.setTitle(R.string.secciones);
                nav_seccion2.setTitle(R.string.otrasOpciones);
                nav_about.setTitle(R.string.acerca);


            } else if (pref.getString("option3", "").equals("Spanish")) {
                Resources res2 = getApplicationContext().getResources();
                DisplayMetrics dm2 = res2.getDisplayMetrics();
                android.content.res.Configuration conf2 = res2.getConfiguration();
                conf2.locale = new Locale("es");
                res2.updateConfiguration(conf2, dm2);
                Menu menu = navView.getMenu();
                MenuItem nav_seccion = menu.findItem(R.id.navigation_header1);
                MenuItem nav_seccion2 = menu.findItem(R.id.navigation_header2);
                MenuItem nav_inicio = menu.findItem(R.id.menu_seccion_inicio);
                MenuItem nav_rutas = menu.findItem(R.id.menu_seccion_rutas);
                MenuItem nav_config = menu.findItem(R.id.menu_seccion_config);
                MenuItem nav_logout = menu.findItem(R.id.menu_seccion_cerrar);
                MenuItem nav_about = menu.findItem(R.id.menu_seccion_acerca);

                nav_inicio.setTitle(R.string.menuInicio);
                nav_rutas.setTitle(R.string.tusRutas);
                nav_config.setTitle(R.string.configuracion);
                nav_logout.setTitle(R.string.cerrarSesion);
                nav_seccion.setTitle(R.string.secciones);
                nav_seccion2.setTitle(R.string.otrasOpciones);
                nav_about.setTitle(R.string.acerca);

            }
        }
    }

    @Override
    public void onStart() {
        super.onStart();
        mAuth.addAuthStateListener(mAuthListener);
    }

    @Override
    public void onStop() {
        super.onStop();
        if (mAuthListener != null) {
            mAuth.removeAuthStateListener(mAuthListener);
        }
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        if (requestCode == RC_SIGN_IN) {
            IdpResponse response = IdpResponse.fromResultIntent(data);

            if (resultCode == RESULT_OK) {
                // Successfully signed in
                FirebaseUser user = FirebaseAuth.getInstance().getCurrentUser();
                // ...
            } else if (resultCode == PlaceAutocomplete.RESULT_ERROR) {
                Status status = PlaceAutocomplete.getStatus(this, data);
                // TODO: Handle the error.
                Log.i("Travel", status.getStatusMessage());

            }
        }
    }
}
