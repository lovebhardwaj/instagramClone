/*
 * Copyright (c) 2015-present, Parse, LLC.
 * All rights reserved.
 *
 * This source code is licensed under the BSD-style license found in the
 * LICENSE file in the root directory of this source tree. An additional grant
 * of patent rights can be found in the PATENTS file in the same directory.
 */
package com.parse.starter;

import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.KeyEvent;
import android.view.View;
import android.view.inputmethod.InputMethodManager;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.parse.LogInCallback;
import com.parse.ParseAnalytics;
import com.parse.ParseException;
import com.parse.ParseUser;
import com.parse.SignUpCallback;


public class MainActivity extends AppCompatActivity implements View.OnClickListener, View.OnKeyListener {
  private static final String TAG = "MainActivity";

  boolean signUpModeActive = true;
  TextView changeModeTextView;
  EditText passwordEditText;

  @Override
  public boolean onKey(View view, int i, KeyEvent keyEvent) {
    if (i == KeyEvent.KEYCODE_ENTER && keyEvent.getAction() == KeyEvent.ACTION_DOWN){ //To prevent calling the signup method two times
      signup(view); //Since signup does not have any view dependent code we can use any view

    }
    return false;
  }

  @Override
  public void onClick(View view) {
    //Called whenever the screen is tapped, this method is called by the view

    if (view.getId() == R.id.changeModeTextView){
      Button modeButton = (Button) findViewById(R.id.modeButton); //Reference to the button widget

      if (signUpModeActive){ //If the button says signup we need to change is to login when text view is tapped

        signUpModeActive = false;
        modeButton.setText("Login");
        changeModeTextView.setText("or Signup");

      }else {
        //Do the opposite
        signUpModeActive = true;
        modeButton.setText("Signup");
        changeModeTextView.setText("or Login");
      }

    }else if (view.getId() == R.id.background_image_view || view.getId() == R.id.instaLogoImageView){
      //If tapped hide the keyboard
      InputMethodManager inputMethodManager = (InputMethodManager) getSystemService(INPUT_METHOD_SERVICE);
      inputMethodManager.hideSoftInputFromWindow(getCurrentFocus().getWindowToken(), 0);

    }
  }

  public void signup(View view){

    EditText usernameEditText = (EditText) findViewById(R.id.usernameEditText);//Reference to edit text widgets

    passwordEditText = (EditText) findViewById(R.id.passwordEditText);


    //Check to see if they are empty
    if (usernameEditText.getText().toString().isEmpty() || passwordEditText.getText().toString().isEmpty()){
      //Tell the user
      Toast.makeText(this, "A username and a password are required", Toast.LENGTH_SHORT).show();

    }else {
      if (signUpModeActive) {
        //If not empty we will sign the user up
        ParseUser user = new ParseUser(); //Creates a new parse user object

        user.setUsername(usernameEditText.getText().toString()); //Creates a username for user

        user.setPassword(passwordEditText.getText().toString()); //Set the desired password for the user

        user.signUpInBackground(new SignUpCallback() { //Parse libraries used to signup user
          @Override
          public void done(ParseException e) {
            //Will be called when signup is done
            if (e == null) {//Check if the error is null or in other words signup was successful

              Toast.makeText(MainActivity.this, "Signup successful", Toast.LENGTH_SHORT).show();

            } else {

              Toast.makeText(MainActivity.this, e.getMessage(), Toast.LENGTH_SHORT).show(); //parse server handles if the user already exists
              //Here e.message will just display that if user already exists
            }
          }
        });
      } else {//Login code will go here
        ParseUser.logInInBackground(usernameEditText.getText().toString(), passwordEditText.getText().toString(), new LogInCallback() {
          @Override
          public void done(ParseUser user, ParseException e) {
            if (user != null || e == null){
              Log.d(TAG, "Login successful");
            }else {
              Toast.makeText(MainActivity.this, e.getMessage(), Toast.LENGTH_SHORT).show();
            }
          }
        });

      }
    }



  }

  @Override
  protected void onCreate(Bundle savedInstanceState) {
    super.onCreate(savedInstanceState);
    setContentView(R.layout.activity_main);

    changeModeTextView = (TextView) findViewById(R.id.changeModeTextView);

    ImageView background = (ImageView) findViewById(R.id.background_image_view);
    ImageView logo = (ImageView) findViewById(R.id.instaLogoImageView);

    background.setOnClickListener(this);
    logo.setOnClickListener(this);


    changeModeTextView.setOnClickListener(this);

    passwordEditText.setOnKeyListener(this);




    ParseAnalytics.trackAppOpenedInBackground(getIntent());
  }


}