package com.imnotpayingforthat.imnotpayingforthat.views;

import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.View;
import android.widget.EditText;
import android.widget.ProgressBar;
import android.widget.Toast;

import com.imnotpayingforthat.imnotpayingforthat.R;
import com.facebook.AccessToken;
import com.facebook.CallbackManager;
import com.facebook.FacebookCallback;
import com.facebook.FacebookException;
import com.facebook.GraphRequest;
import com.facebook.Profile;
import com.facebook.login.LoginManager;
import com.facebook.login.LoginResult;
import com.google.android.gms.auth.api.signin.GoogleSignIn;
import com.google.android.gms.auth.api.signin.GoogleSignInAccount;
import com.google.android.gms.auth.api.signin.GoogleSignInClient;
import com.google.android.gms.auth.api.signin.GoogleSignInOptions;
import com.google.android.gms.common.api.ApiException;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthCredential;
import com.google.firebase.auth.FacebookAuthProvider;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.auth.GoogleAuthProvider;
import com.google.firebase.firestore.FirebaseFirestore;
import com.imnotpayingforthat.imnotpayingforthat.models.User;

import org.json.JSONException;

import java.util.Arrays;

import static com.imnotpayingforthat.imnotpayingforthat.util.Globals.dbCollection_user;
//Doc for Google Login https://firebase.google.com/docs/auth/android/google-signin
//Doc for Facebook login https://firebase.google.com/docs/auth/android/facebook-login

public class LoginActivity extends AppCompatActivity implements View.OnClickListener {

    private static final String TAG = "LoginActivity";
    private static final String EMAIL = "email";
    private static final String PROFILE = "public_profile";
    private static final int RC_SIGN_IN = 9001;

    private GoogleSignInClient googleSignInClient;
    private FirebaseAuth firebaseAuth;
    private CallbackManager callbackManager;
    private EditText email, password;
    private User user;
    // Facebook login
    private LoginManager loginManager;
    private ProgressBar progressBar;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        firebaseAuth = FirebaseAuth.getInstance();
        checkForSignedInUser();
        setContentView(R.layout.activity_login);

        findViewById(R.id.textview_Register).setOnClickListener(this);
        findViewById(R.id.main_googleLoginButton).setOnClickListener(this);
        findViewById(R.id.button_loginButton).setOnClickListener(this);
        progressBar = findViewById(R.id.progressBar_Login);
        email = findViewById(R.id.editText_email);
        password = findViewById(R.id.editText_password);
    }

    @Override
    protected void onStart() {
        super.onStart();
        GoogleLoginSetup();
    }


    private void GoogleLoginSetup() {
        GoogleSignInOptions gso = new GoogleSignInOptions.Builder(GoogleSignInOptions.DEFAULT_SIGN_IN)
                .requestIdToken(getString(R.string.default_web_client_id))
                .requestProfile()
                .requestEmail()
                .build();
        googleSignInClient = GoogleSignIn.getClient(this, gso);
    }

    @Override
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.textview_Register:
                Intent intent = new Intent(this, RegisterActivity.class);
                startActivity(intent);
                break;
            case R.id.main_googleLoginButton:
                Intent signInIntent = googleSignInClient.getSignInIntent();
                startActivityForResult(signInIntent, RC_SIGN_IN);
                break;
            case R.id.button_loginButton:
                firebaseAuth.signInWithEmailAndPassword(email.getText().toString(), password.getText().toString())
                        .addOnCompleteListener(task -> redirectToTimerScreen());
        }
    }

    private void redirectToTimerScreen() {
        Intent intent = new Intent(this, MainActivity.class);
        
        // TODO: 11/04/2018 Hvad gør de her flag? Og er de nødvendige hvis man alligevel finisher den her activity?
        intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP | Intent.FLAG_ACTIVITY_NEW_TASK);
        startActivity(intent);
        finish();
    }

    //todo mangler result fra RegisterActivity
    // TODO: 11/04/2018 Samme fejl halløj med allahu (sn)ackbar
    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == RC_SIGN_IN) {
            Task<GoogleSignInAccount> task = GoogleSignIn.getSignedInAccountFromIntent(data);
            try {
                GoogleSignInAccount account = task.getResult(ApiException.class);
                firebaseAuthWithGoogle(account);
            } catch (ApiException e) {
                Log.w(TAG, "Google sign in failed", e);
            }
        }
    }

    private void firebaseAuthWithGoogle(@NonNull GoogleSignInAccount account) {
        Log.d(TAG, "firebaseAuthWithGoogle:" + account.getId());
        AuthCredential credential = GoogleAuthProvider.getCredential(account.getIdToken(), null);
        signInWithCredential(credential);
        user = new User(account.getGivenName(),account.getFamilyName());
        user.setProfile_Image(account.getPhotoUrl().toString());
    }

    private void signInWithCredential(AuthCredential credential) {
        firebaseAuth.signInWithCredential(credential)
                .addOnCompleteListener(this, task -> {
                    if (task.isSuccessful()) {
                        Log.d(TAG, "signInWithCredential:success");
                        // TODO: 11/04/2018 Tror alligevel godt det her kunne komme ind i et user repository. Det er en add or update operation
                        // TODO: 11/04/2018 Det er meget sandsynligt at vi senere vil få brug for at opdatere en bruger.
                        // TODO: 11/04/2018 På et punkt nu hvor det måske vil give mening at lave en LoginViewModel som kan holde nogen af de ting her
                        // TODO: 11/04/2018 FirebaseAuth, repository, user osv.
                        FirebaseFirestore instance = FirebaseFirestore.getInstance();
                        instance.collection
                                (dbCollection_user)
                                .document(FirebaseAuth.getInstance().getCurrentUser().getUid())
                                .set(user)
                                .addOnSuccessListener(documentReference -> {
                                    hideProgressBar();
                                    redirectToTimerScreen();
                                });
                    } else {
                        hideProgressBar();
                        Log.w(TAG, "signInWithCredential:failure", task.getException());
                        Toast.makeText(LoginActivity.this, "Authentication Failed.", Toast.LENGTH_SHORT).show();
                    }
                });
    }

    private void checkForSignedInUser() {
        FirebaseUser currentUser = firebaseAuth.getCurrentUser();
        if(currentUser != null) {
            Log.d(TAG, "User is already logged in");
            redirectToTimerScreen();
        }
    }

    private void showProgressBar() {
        progressBar.setVisibility(View.VISIBLE);
    }

    private void hideProgressBar() {
        if (progressBar.getVisibility() == View.VISIBLE) {
            progressBar.setVisibility(View.INVISIBLE);
        }
    }
}
