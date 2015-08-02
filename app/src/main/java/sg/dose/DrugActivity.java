package sg.dose;

import android.app.Activity;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.net.Uri;
import android.os.Bundle;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import com.parse.FindCallback;
import com.parse.GetCallback;
import com.parse.GetDataCallback;
import com.parse.Parse;
import com.parse.ParseException;
import com.parse.ParseFile;
import com.parse.ParseObject;
import com.parse.ParseQuery;

import java.io.ByteArrayOutputStream;
import java.util.ArrayList;
import java.util.List;

public class DrugActivity extends Activity {
    private ListView listView;
    String drugdetails = "";
    String licenceno = "";
    ImageView imageView;
    Bitmap image;
    String newLicenceno = "";

    String detailstitle="";//TODO
    TextView txt_help_gest;
    ArrayList<String> comments = new ArrayList<String>();
    ArrayAdapter<String> myAdapter;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.drugs_details);

        Bundle extras = getIntent().getExtras();
        if (extras != null) {
            licenceno = extras.getString("licenceno");
        }

        // Enable Local Datastore.
//        Parse.enableLocalDatastore(this);
//
//        Parse.initialize(this, "T73AsYtH6A26QzdJMgLGder3fr3aOOmMi4ZbRn0L", "dRK7lNk5SqXOCgEqWXJ7QFCSey5BkyGMqlWFtbpm");

       //listView = (ListView) findViewById(R.id.listView);


        //TODO
        txt_help_gest = (TextView) findViewById(R.id.textView);
        // hide until its title is clicked
        txt_help_gest.setVisibility(View.GONE);

        //query.whereEqualTo("licenceno", "SIN14799P"); //This is to filter things!
        ParseQuery<ParseObject> query = ParseQuery.getQuery("drugs");
        query.whereEqualTo("productname", licenceno);

        query.getFirstInBackground(new GetCallback<ParseObject>() {
            public void done(ParseObject p, ParseException e) {
                newLicenceno = p.getString("licenceno");

                drugdetails = "Licence No:\n" + p.getString("licenceno") + "\n\n" +
                        "Product Name:\n" + p.getString("productname") + "\n\n" +
                        "License holder:\n" + p.getString("licenseholder") + "\n\n" +
                        "Approval date:\n" + p.getString("approvaldate") + "\n\n" +
                        "Forensic classification:\n" + p.getString("forensicclassification") + "\n\n" +
                        "ATC Code:\n" + p.getString("atccode") + "\n\n" +
                        "Dosage form:\n" + p.getString("dosageform") + "\n\n" +
                        "Route of Administration:\n" + p.getString("routeofadministration") + "\n\n" +
                        "Manufacturer:\n" + p.getString("manufacturer") + "\n\n" +
                        "Country of manufacturer:\n" + p.getString("countryofmanufacturer") + "\n\n" +
                        "Active ingredients:\n" + p.getString("activeingredients") + "\n\n" +
                        "Strength:\n" + p.getString("strength");

                detailstitle = p.getString("productname");
                setDetails();
            }
        });

        imageView = (ImageView) findViewById(R.id.imageView);

        ImageButton button= (ImageButton) findViewById(R.id.imageButton);
        button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(android.provider.MediaStore.ACTION_IMAGE_CAPTURE);
                startActivityForResult(intent, 0);
            }
        });

    }

    private void setList() {
        myAdapter = new ArrayAdapter<String>(this, android.R.layout.simple_list_item_1, comments);
        listView = (ListView) findViewById(R.id.listView2);
        listView.setAdapter(myAdapter);
    }


    Bitmap bp;

    protected void onActivityResult(int requestCode, int resultCode, Intent data) {

        super.onActivityResult(requestCode, resultCode, data);
        bp = (Bitmap) data.getExtras().get("data");

        if (bp != null) {
            // Convert it to byte
            ByteArrayOutputStream stream = new ByteArrayOutputStream();
            // Compress image to lower quality scale 1 - 100
            bp.compress(Bitmap.CompressFormat.PNG, 100, stream);
            byte[] image = stream.toByteArray();

            // Create thse ParseFile
            ParseFile file = new ParseFile(newLicenceno + ".png", image);
            // Upload the image into Parse Cloud
            file.saveInBackground();

            // Create a New Class called "ImageUpload" in Parse
            ParseObject imgupload = new ParseObject("drugimages");

            // Create a column named "ImageName" and set the string
            imgupload.put("licenceno", newLicenceno);

            // Create a column named "ImageFile" and insert the image
            imgupload.put("ImageFile", file);

            // Create the class and the columns
            imgupload.saveInBackground();

            // Show a simple toast message
            Toast.makeText(DrugActivity.this, "Image Uploaded",
                    Toast.LENGTH_SHORT).show();

            ImageView iw = (ImageView) findViewById(R.id.imageView);
            iw.setImageBitmap(bp);
        } else {
            //Log.e("ADASDASDASDASD", "ASDASDASDASD");
        }

    }

    @Override
    public void onResume() {
        super.onResume();  // Always call the superclass method first
        setDetails();
        Log.e("asd", "OnResume Called");
        setList();
        comments.clear();
        getComments();
    }

    public void setDetails() {
        if(drugdetails != null) {
            //ArrayAdapter<String> adapter = new ArrayAdapter<String>(this, android.R.layout.simple_list_item_1, drugdetails);
            //listView.setAdapter(adapter);
            TextView text = (TextView) findViewById(R.id.textView);
            text.setText(drugdetails);
            TextView tt = (TextView) findViewById(R.id.drugsDetailsTitle);
            tt.setText(detailstitle);

            Button bt = (Button) findViewById(R.id.commentbutton);
            bt.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    Intent intent = new Intent(getBaseContext(), CommentsActivity.class);
                    intent.putExtra("licenceno", licenceno);
                    startActivity(intent);
                }
            });

            // load the image
            ParseQuery query2 = new ParseQuery("drugimages");
            Log.d("LICENCEN", newLicenceno);
            query2.addDescendingOrder("updatedAt");
            query2.whereEqualTo("licenceno", newLicenceno);
            query2.getFirstInBackground(new GetCallback<ParseObject>() {
                public void done(ParseObject object, ParseException e) {
                    if (object == null) {
                        Log.d("score", "The getFirst request failed.");
                    } else {
                        ParseFile p = object.getParseFile("ImageFile");
                        p.getDataInBackground(new GetDataCallback() {
                            public void done(byte[] data, ParseException e) {
                                Bitmap bmp = BitmapFactory.decodeByteArray(data, 0, data.length);
                                imageView.setImageBitmap(bmp);
                            }
                        });

                        Log.d("score", "The getFirst request passed.");
                    }
                }
            });
        }
    }


//    //TODO
    /**
     * onClick handler
     */
    public void toggle_contents(View v){

        if(txt_help_gest.isShown()){
            Fx.slide_up(this, txt_help_gest);
            TextView tt = (TextView)findViewById(R.id.drugsDetailsTitle);
            tt.setCompoundDrawablesWithIntrinsicBounds(0,0,R.drawable.ic_expand_holo_light,0);
            txt_help_gest.setVisibility(View.GONE);
        }
        else{
            txt_help_gest.setVisibility(View.VISIBLE);
            Fx.slide_down(this, txt_help_gest);
            TextView tt = (TextView)findViewById(R.id.drugsDetailsTitle);
            tt.setCompoundDrawablesWithIntrinsicBounds(0,0, R.drawable.ic_collapse_holo_light, 0);
        }
    }

    void getComments() {
        ParseQuery query2 = new ParseQuery("drugcomments");
        query2.whereEqualTo("licenceno", licenceno);
        query2.addDescendingOrder("updatedAt");
        query2.setLimit(3);
        query2.findInBackground(new FindCallback<ParseObject>() {
            @Override
            public void done(List<ParseObject> objects,
                             ParseException e) {
                if (e == null) {
                    for (ParseObject c : objects) {
                        comments.add("" + c.getString("user") + ": " + "\n" + c.getString("comment"));
                    }
                } else {
                    //Log.d("Brand", "Error: " + e.getMessage());
                }

                setList();
            }
        });
    }


}
