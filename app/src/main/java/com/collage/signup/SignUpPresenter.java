package com.collage.signup;


import android.util.Patterns;

class SignUpPresenter {

    private SignUpView signUpView;


    SignUpPresenter(SignUpView signUpView) {
        this.signUpView = signUpView;
    }

    void validateSignUpUserData(String fullName,
                                String email,
                                String password) {

        boolean isFullNameValid;
        boolean isEmailValid;
        boolean isPasswordValid;

        if (fullName.replaceAll("\\s+", "").isEmpty()) {
            signUpView.showEmptyFullNameError();
            isFullNameValid = false;
        } else {
            signUpView.hideEmptyFullNameError();
            isFullNameValid = true;
        }

        if (!Patterns.EMAIL_ADDRESS.matcher(email).matches()) {
            signUpView.showInvalidEmailError();
            isEmailValid = false;
        } else {
            signUpView.hideInvalidEmailError();
            isEmailValid = true;
        }

        if (password.length() < 6 ||
                password.contains(" ")) {
            signUpView.showInvalidPasswordError();
            isPasswordValid = false;
        } else {
            signUpView.hideInvalidPasswordError();
            isPasswordValid = true;
        }

        if (isEmailValid && isFullNameValid && isPasswordValid) {
            signUpView.createAccount(email, password);
        }
    }
}