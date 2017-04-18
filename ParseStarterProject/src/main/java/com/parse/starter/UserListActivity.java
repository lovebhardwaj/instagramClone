package com.parse.starter;

import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.widget.ArrayAdapter;
import android.widget.ListView;

import com.parse.FindCallback;
import com.parse.ParseException;
import com.parse.ParseQuery;
import com.parse.ParseUser;

import java.util.ArrayList;
import java.util.List;

public class UserListActivity extends AppCompatActivity {
    private static final String TAG = "UserListActivity";


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_user_list);

        final  ArrayList<String> userList = new ArrayList<>();

        final ListView userListView = (ListView) findViewById(R.id.userListView); //Reference to the list view

        final ArrayAdapter arrayAdapter = new ArrayAdapter<String>(this, android.R.layout.simple_list_item_1, userList);

        ParseQuery<ParseUser> userParseQuery = ParseUser.getQuery();

        userParseQuery.whereNotEqualTo("username", ParseUser.getCurrentUser().getUsername()); //Exclude the current user from the list
        //Have the list im alphabetical order
        userParseQuery.addAscendingOrder("username"); //Arrange by their username

        userParseQuery.findInBackground(new FindCallback<ParseUser>() {
            @Override
            public void done(List<ParseUser> objects, ParseException e) {
                if (e == null){
                    //If there are any users
                    if (objects.size()>0){

                        for (ParseUser user : objects){

                            Log.d(TAG, user.getUsername());
                            userList.add(user.getUsername()); //Add all the users to the list we created for the list view
                        }
                        userListView.setAdapter(arrayAdapter); //Sets a array adapter for the list view
                    }


                }else {
                    e.printStackTrace();
                }
            }
        });

        for (String user : userList){
            Log.d(TAG, "Users : " + user);
        }




    }
}
