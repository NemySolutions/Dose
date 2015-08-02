package sg.dose;

import android.app.Activity;
import android.app.ListActivity;
import android.content.Intent;
import android.support.v7.app.ActionBarActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.GridView;
import android.widget.ListView;

import com.facebook.AccessToken;
import com.facebook.AccessTokenTracker;
import com.facebook.CallbackManager;
import com.facebook.FacebookSdk;
import com.parse.FindCallback;
import com.parse.Parse;
import com.parse.ParseException;
import com.parse.ParseObject;
import com.parse.ParseQuery;

import java.util.ArrayList;
import java.util.List;

public class MainActivity extends Activity {

    CallbackManager callbackManager;
    GridView gridView;
    String[]cat = {"Pharmacy", "Prescription", "Countries", "General"};

    List<String> drug = new ArrayList<String>();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        FacebookSdk.sdkInitialize(getApplicationContext());
        setContentView(R.layout.activity_main);

        callbackManager = CallbackManager.Factory.create();

        //setListAdapter(new DrugArrayAdapter(this, drug));

        final AccessToken accessToken = AccessToken.getCurrentAccessToken();
        if (accessToken == null) {
            Intent intent = new Intent(getApplicationContext(), LoginActivity.class);
            startActivity(intent);
        } else {
            Parse.enableLocalDatastore(this);
            Parse.initialize(this, "T73AsYtH6A26QzdJMgLGder3fr3aOOmMi4ZbRn0L", "dRK7lNk5SqXOCgEqWXJ7QFCSey5BkyGMqlWFtbpm");
            loadList();
        }
        new AccessTokenTracker() {
            @Override
            protected void onCurrentAccessTokenChanged(AccessToken accessToken, AccessToken accessToken2) {
                Log.d("sas", "onCurrentAccessTokenChanged()");
                if (accessToken == null) {
                    // Log in Logic
                } else if (accessToken2 == null) {
                    Intent intent = new Intent(getApplicationContext(), LoginActivity.class);
                    startActivity(intent);
                }
            }
        };

        gridView = (GridView) findViewById(R.id.gridView1);
        gridView.setAdapter(new BoxAdapter(MainActivity.this, cat));

        gridView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            public void onItemClick(AdapterView<?> parent, View v,
                                    int position, long id) {
                Intent intent = new Intent(MainActivity.this, DrugActivity.class);
                startActivity(intent);
            }
        });

        ListView listView = (ListView) findViewById(R.id.listView);
        listView.setOnItemClickListener(onItemClickListener);

    }

    void loadList() {

        ParseQuery<ParseObject> query = ParseQuery.getQuery("drugs");
        //query.whereEqualTo("playerName", "Dan Stemkoski");
        query.setLimit(40);
        query.findInBackground(new FindCallback<ParseObject>() {
            public void done(List<ParseObject> drugList, ParseException e) {
                if (e == null) {
                    //Log.e("score", "Retrieved " + drugList.size() + " drugs");
                    for (int i = 0; i < drugList.size(); i++) {
                        drug.add(drugList.get(i).getString("productname"));
                    }
                } else {
                    Log.e("score", "Error: " + e.getMessage());
                }

                setList();
            }
        });
    }

    private AdapterView.OnItemClickListener onItemClickListener = new AdapterView.OnItemClickListener() {
        @Override
        public void onItemClick(AdapterView<?> arg0, View arg1, int position,
                                long arg3) {
                    Intent intent = new Intent(getApplicationContext(), DrugActivity.class);
                    intent.putExtra("licenceno", drug.get(position));
                    startActivity(intent);
        }
    };


    private void setList() {
        ArrayAdapter<String> arrayAdapter = new ArrayAdapter<String>(this, android.R.layout.simple_list_item_1, drug);
        ListView listView = (ListView) findViewById(R.id.listView);
        listView.setAdapter(arrayAdapter);
    }

    @Override
    public void onResume() {
        super.onResume();  // Always call the superclass method first
//        loadList();
        Log.e("asd", "OnResume Called");
//        ImageView imageView = (ImageView) findViewById(R.id.imageView);
//        imageView.setImageBitmap(bp);
    }

}
