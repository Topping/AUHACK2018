package com.imnotpayingforthat.imnotpayingforthat.viewModels;

import android.app.Application;
import android.arch.lifecycle.AndroidViewModel;
import android.arch.lifecycle.LiveData;
import android.arch.lifecycle.MutableLiveData;
import android.graphics.Bitmap;
import android.support.annotation.NonNull;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;

public class MainViewModel extends AndroidViewModel {

    private MutableLiveData<FirebaseUser> currentUser;
    private MutableLiveData<Bitmap> profilePicture;
    private final FirebaseAuth firebaseAuth;

    public MainViewModel(@NonNull Application application) {
        super(application);
        this.firebaseAuth = FirebaseAuth.getInstance();
    }

    public MainViewModel(@NonNull Application application, FirebaseAuth firebaseAuth) {
        super(application);
        this.firebaseAuth = firebaseAuth;
    }

    public LiveData<FirebaseUser> getCurrentUser() {
        if(currentUser == null) {
            currentUser = new MutableLiveData<>();
        }
        return currentUser;
    }

    public LiveData<Bitmap> getProfilePicture() {
        if(profilePicture == null) {
            profilePicture = new MutableLiveData<>();
        }
        return profilePicture;
    }

    // TODO: 10/04/2018 Something if the user is null?
    private void setCurrentUser(FirebaseUser user) {
        if(currentUser == null) {
            currentUser = new MutableLiveData<>();
        }
        currentUser.setValue(user);
    }

    public void updateCurrentUser() {
        setCurrentUser(firebaseAuth.getCurrentUser());
    }

    public void logout() {
        firebaseAuth.signOut();
    }

}
