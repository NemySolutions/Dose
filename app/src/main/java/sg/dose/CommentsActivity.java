package sg.dose;

import android.app.Activity;
import android.content.Intent;
import android.graphics.Bitmap;
import android.os.Bundle;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import com.parse.FindCallback;
import com.parse.GetCallback;
import com.parse.Parse;
import com.parse.ParseException;
import com.parse.ParseFile;
import com.parse.ParseObject;
import com.parse.ParseQuery;
import com.parse.ParseQueryAdapter;

import java.io.ByteArrayOutputStream;
import java.util.ArrayList;
import java.util.List;

public class CommentsActivity extends Activity {
    private ListView listView;
    String drugdetails = "";
    String licenceno = "";
    ArrayAdapter<String> myAdapter;
    List<String> commentList = new ArrayList<String>();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_comments);

        Bundle extras = getIntent().getExtras();
        if (extras != null) {
            licenceno = extras.getString("licenceno");

            ParseQuery query = new ParseQuery("drugcomments");
            query.whereEqualTo("licenceno", licenceno);
//            query.addDescendingOrder("updatedAt");
            query.findInBackground(new FindCallback<ParseObject>() {

                @Override
                public void done(List<ParseObject> objects,
                                 ParseException e) {
                    if (e == null) {
                        for (ParseObject p : objects) {
                            commentList.add(p.getString("user") + "\n" + p.getString("comment"));
                        }
                    }
                    setList();
                }
            });
        }

        final EditText tx = (EditText) findViewById(R.id.editText);

        tx.setHint("Enter comments here");

        Button bt = (Button) findViewById(R.id.button);
        bt.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                ParseObject c = new ParseObject("drugcomments");
                c.put("user", "Ming Sheng");
                c.put("comment", tx.getText().toString());
                c.put("licenceno", licenceno);
                c.saveInBackground();

                commentList.add("Ming Sheng" + "\n" +  tx.getText().toString());

//                myAdapter.n(commentList);
                myAdapter.notifyDataSetChanged();
                tx.setText("");
            }
        });
    }

    private void setList() {
        myAdapter = new ArrayAdapter<String>(this, android.R.layout.simple_list_item_1, commentList);
        listView = (ListView) findViewById(R.id.commentList);
        listView.setAdapter(myAdapter);
    }

    @Override
    public void onResume() {
        super.onResume();  // Always call the superclass method first

        Log.e("asd", "OnResume Called");
//        ImageView imageView = (ImageView) findViewById(R.id.imageView);
//        imageView.setImageBitmap(bp);
    }

}
