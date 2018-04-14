package com.imnotpayingforthat.imnotpayingforthat.views;

import android.arch.lifecycle.ViewModelProviders;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ProgressBar;
import android.widget.Toast;

import com.imnotpayingforthat.imnotpayingforthat.R;
import com.imnotpayingforthat.imnotpayingforthat.viewmodels.RegisterViewModel;

import java.util.List;

import static com.imnotpayingforthat.imnotpayingforthat.util.ErrorCodes.*;

// TODO: 24-03-2018 Referencer til google docs, osv.
public class RegisterActivity extends AppCompatActivity implements View.OnClickListener {

    private static final String TAG = "RegisterActivity";

    private EditText email, password, confirmPassword, firstName, lastName;
    private Button registerButton;
    private ProgressBar progressBar;
    private RegisterViewModel registerViewModel;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_register);

        firstName = findViewById(R.id.Register_editText_Firstname);
        lastName = findViewById(R.id.Register_editText_Lastname);
        email = findViewById(R.id.editText_RegisterEmail);
        password = findViewById(R.id.editText_RegisterPassword);
        confirmPassword = findViewById(R.id.editText_RegisterConfirmPassword);
        registerButton = findViewById(R.id.button_Register);
        progressBar = findViewById(R.id.progressBar_Register);

        registerViewModel = ViewModelProviders.of(this).get(RegisterViewModel.class);
        registerButton.setOnClickListener(this);
    }

    @Override
    protected void onResume() {
        super.onResume();
        registerViewModel.getUserResponse().observe(this, response -> {
            hideProgressBar();
            if (response.getSuccess()) {
                redirectToMainActivity();
            } else {
                handleErrors(response.getErrors());
            }
        });
    }

    @Override
    protected void onStop() {
        super.onStop();
        registerViewModel.getUserResponse().removeObservers(this);
    }

    private void handleErrors(List<String> errors) {
        for (String error : errors) {
            switch (error) {
                case REGISTRATION_EMAIL_MUST_BE_FILLED:
                    email.setError(getString(R.string.errorMustBeFilled));
                    break;
                case REGISTRATION_FAILED:
                    Toast.makeText(this, R.string.Register_Failed, Toast.LENGTH_SHORT).show();
                    break;
                case REGISTRATION_PASSWORD_NOT_EQUAL:
                    confirmPassword.setError(getString(R.string.errorPasswordNotEqual));
                    break;
                case REGISTRATION_PASSWORD_MUST_BE_FILLED:
                    password.setError(getString(R.string.errorMustBeFilled));
                    break;
                case REGISTRATION_CONFIRMPASSWORD_MUST_BE_FILLED:
                    confirmPassword.setError(getString(R.string.errorMustBeFilled));
                    break;
                case REGISTRATION_FIRSTNAME_MUST_BE_FILLED:
                    firstName.setError(getString(R.string.errorMustBeFilled));
                    break;
                case REGISTRATION_LASTNAME_MUST_BE_FILLED:
                    lastName.setError(getString(R.string.errorMustBeFilled));
                    break;
                default:
                    break;
            }
        }
    }

    @Override
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.button_Register:
                showProgressBar();
                Log.d(TAG, "Register clicked: Attempting to register");
                registerViewModel.registerUser(
                        firstName.getText().toString(),
                        lastName.getText().toString(),
                        email.getText().toString(),
                        password.getText().toString(),
                        confirmPassword.getText().toString());
                break;
        }
    }

    private void redirectToMainActivity() {
        finish();
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
