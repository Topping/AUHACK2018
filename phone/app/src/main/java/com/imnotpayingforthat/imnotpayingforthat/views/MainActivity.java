package com.imnotpayingforthat.imnotpayingforthat.views;

import android.content.BroadcastReceiver;
import android.content.ComponentName;
import android.content.Context;
import android.content.IntentFilter;
import android.content.ServiceConnection;
import android.net.Uri;
import android.os.IBinder;
import android.support.annotation.NonNull;
import android.support.v4.app.Fragment;
import android.arch.lifecycle.ViewModelProviders;
import android.content.Intent;
import android.os.Bundle;
import android.support.design.widget.NavigationView;
import android.support.v4.app.FragmentManager;
import android.support.v4.view.GravityCompat;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBarDrawerToggle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.iid.FirebaseInstanceId;
import com.imnotpayingforthat.imnotpayingforthat.R;
import com.imnotpayingforthat.imnotpayingforthat.TestQueryActivity;
import com.imnotpayingforthat.imnotpayingforthat.models.Team;
import com.imnotpayingforthat.imnotpayingforthat.repositories.UserRepository;
import com.imnotpayingforthat.imnotpayingforthat.services.register.FirebaseInstanceIDService;
import com.imnotpayingforthat.imnotpayingforthat.services.register.ListenService;
import com.imnotpayingforthat.imnotpayingforthat.viewmodels.MainViewModel;
import com.google.android.gms.auth.api.signin.GoogleSignIn;
import com.google.android.gms.auth.api.signin.GoogleSignInClient;
import com.google.android.gms.auth.api.signin.GoogleSignInOptions;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.UserInfo;

public class MainActivity extends AppCompatActivity
        implements NavigationView.OnNavigationItemSelectedListener,
        TeamListFragment.OnTeamFragmentInteractionListener, CreateTeamFragment.OnCreateTeamFragmentListener,
        TeamDetailsFragment.OnTeamDetailsInteractionListener, ShoppingListFragment.OnShoppingListInteractionListener{

    private MainViewModel viewModel;
    private final String TAG = this.getClass().getSimpleName();
    private ServiceConnection listenService;
    private ListenService boundListenService;
    boolean isBound = false;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        Toolbar toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        viewModel = ViewModelProviders.of(this).get(MainViewModel.class);

        DrawerLayout drawer = findViewById(R.id.drawer_layout);
        ActionBarDrawerToggle toggle = new ActionBarDrawerToggle(
                this, drawer, toolbar, R.string.navigation_drawer_open, R.string.navigation_drawer_close);
        drawer.addDrawerListener(toggle);
        toggle.syncState();

        NavigationView navigationView = findViewById(R.id.nav_view);
        navigationView.setNavigationItemSelectedListener(this);

        View header = navigationView.getHeaderView(0);

        TextView name = header.findViewById(R.id.textview_nav_name);
        TextView email = header.findViewById(R.id.textView_nav_email);

        FirebaseUser currentUser = FirebaseAuth.getInstance().getCurrentUser();

        if(currentUser != null){
            name.setText(currentUser.getDisplayName());
            email.setText(currentUser.getEmail());
        }

        configureBoundService();
        Intent intent = new Intent(this, ListenService.class);
        startService(intent);
        bindService(intent, listenService, BIND_AUTO_CREATE);

        IntentFilter intentFilter = new IntentFilter("GIVEROUND");
        registerReceiver(giveRoundReceiver, intentFilter);
    }

    private void configureBoundService() {
        listenService = new ServiceConnection() {
            @Override
            public void onServiceConnected(ComponentName componentName, IBinder iBinder) {
                ListenService.ListenBinder listenBinder = (ListenService.ListenBinder) iBinder;
                boundListenService = listenBinder.getService();
                isBound = true;
            }

            @Override
            public void onServiceDisconnected(ComponentName componentName) {
                isBound = false;
            }
        };
    }

    @Override
    protected void onStart() {
        super.onStart();
        viewModel.getCurrentUser().observe(this, user -> {
            // TODO: 10/04/2018 set email og andre informationer i navigation drawer
            // TODO: 10/04/2018 Slet når der er rigtig funktionalitet

            Toast.makeText(this, "HELLO you are using " + user.getProviderId(), Toast.LENGTH_SHORT).show();
        });
        viewModel.updateCurrentUser();

    }

    @Override
    protected void onResume() {
        super.onResume();
        UserRepository ur = new UserRepository();
        String refreshToken = FirebaseInstanceId.getInstance().getToken();
        ur.updateFcmToken(FirebaseAuth.getInstance().getCurrentUser().getUid(), refreshToken);
    }

    @Override
    protected void onPause() {
        super.onPause();
    }

    @Override
    public void onBackPressed() {
        DrawerLayout drawer = findViewById(R.id.drawer_layout);
        if (drawer.isDrawerOpen(GravityCompat.START)) {
            drawer.closeDrawer(GravityCompat.START);
        } else {
            super.onBackPressed();
        }
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();

        if (isBound) {
            boundListenService.stopService();
            unbindService(listenService);
            unregisterReceiver(giveRoundReceiver);
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
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        //noinspection SimplifiableIfStatement
        if (id == R.id.action_settings) {
            finish();
            System.exit(0);
            return true;
        }

        return super.onOptionsItemSelected(item);
    }

    @SuppressWarnings("StatementWithEmptyBody")
    @Override
    public boolean onNavigationItemSelected(@NonNull MenuItem item) {
        // Handle navigation view item clicks here.
        getSupportFragmentManager().popBackStack(null, FragmentManager.POP_BACK_STACK_INCLUSIVE);
        // TODO: 4/14/2018 Button remains selected first time the app is launched
        item.setChecked(false);
        int id = item.getItemId();
        Fragment fragment = null;
        Class fragmentClass = null;

        if (id == R.id.nav_camera) {
            fragmentClass = TeamListFragment.class;
        }else if (id == R.id.nav_logout) {
            logOut();
        }

        if (fragmentClass != null) {
            try {
                if (fragment == null) {
                    fragment = (Fragment) fragmentClass.newInstance();
                }
                supportInvalidateOptionsMenu();
                getSupportFragmentManager().beginTransaction()
                        .replace(R.id.main_frameLayout_fragment, fragment)
                        .addToBackStack(null)
                        .commit();
            } catch (Exception e) {
                Log.e(TAG, "Failed to instantiate fragment on navigation");
                Toast.makeText(this, "Something went wrong", Toast.LENGTH_LONG).show();
            }
        }

        DrawerLayout drawer = findViewById(R.id.drawer_layout);
        drawer.closeDrawer(GravityCompat.START);
        return false;
    }

    private void logOut() {
        for (UserInfo user : FirebaseAuth.getInstance().getCurrentUser().getProviderData()) {
            if (user != null) {
                String provider = user.getProviderId();
                viewModel.logout();
                switch (provider) {
                    case "password":
                        redirectLoginScreen();
                        break;
                    case "google.com":
                        // TODO: 05-04-2018 Change me
                        GoogleSignInOptions gso = new GoogleSignInOptions.Builder(GoogleSignInOptions.DEFAULT_SIGN_IN)
                                .requestIdToken(getString(R.string.default_web_client_id))
                                .requestEmail()
                                .build();
                        GoogleSignInClient googleSignInClient = GoogleSignIn.getClient(this, gso);
                        googleSignInClient.signOut();
                        redirectLoginScreen();
                        break;
                }
            }
        }
    }

    private void redirectLoginScreen() {
        Intent intent = new Intent(this, LoginActivity.class);
        intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP | Intent.FLAG_ACTIVITY_NEW_TASK);
        startActivity(intent);
        finish();
    }

    private BroadcastReceiver giveRoundReceiver = new BroadcastReceiver() {
        @Override
        public void onReceive(Context context, Intent intent) {
            Toast.makeText(MainActivity.this, intent.getStringExtra("RoundMessage"), Toast.LENGTH_SHORT).show();
        }
    };

    @Override
    public void navigateToDeleteTeam() {

    }

    @Override
    public void onFragmentInteraction(Uri uri) {

    }

    @Override
    public void navigateToShoppingList(String teamId) {
        Bundle bundle = new Bundle();
        bundle.putString("teamId", teamId);
        Fragment fragment = ShoppingListFragment.newInstance();
        fragment.setArguments(bundle);
        getSupportFragmentManager().beginTransaction()
                .replace(R.id.main_frameLayout_fragment, fragment)
                .addToBackStack(null)
                .commit();
    }

    @Override
    public void navigateToCreateTeam() {
        Fragment fragment = CreateTeamFragment.newInstance();
        getSupportFragmentManager().beginTransaction()
                .replace(R.id.main_frameLayout_fragment, fragment)
                .addToBackStack(null)
                .commit();
    }

    @Override
    public void navigateToTeamDetail(Team t) {
        Fragment fragment = TeamDetailsFragment.newInstance(t);
        getSupportFragmentManager().beginTransaction()
                .replace(R.id.main_frameLayout_fragment, fragment)
                .addToBackStack(null)
                .commit();
    }

    @Override
    public void navigateToAddMember(Team t) {
        Bundle bundle = new Bundle();
        bundle.putString("teamId", "hej");
        Fragment fragment = ShoppingListFragment.newInstance();
        fragment.setArguments(bundle);
        getSupportFragmentManager().beginTransaction()
                .replace(R.id.main_frameLayout_fragment, fragment)
                .addToBackStack(null)
                .commit();
    }
}
