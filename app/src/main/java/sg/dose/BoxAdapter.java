package sg.dose;

import android.content.Context;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

/**
 * Created by Ming Sheng on 23/7/2015.
 */
public class BoxAdapter extends BaseAdapter {
    private Context context;
    private final String[] drugValues;

    public BoxAdapter(Context context, String[] drugValues) {
        this.context = context;
        this.drugValues = drugValues;
    }

    public View getView(int position, View convertView, ViewGroup parent) {

        LayoutInflater inflater = (LayoutInflater) context
                .getSystemService(Context.LAYOUT_INFLATER_SERVICE);

        View gridView;

        if (convertView == null) {
            // get layout from mobile.xml
            gridView = inflater.inflate(R.layout.box, null);

            // set image based on selected text
            ImageView imageView = (ImageView) gridView.findViewById(R.id.grid_item_image);
            TextView textView = (TextView) gridView.findViewById(R.id.textView);

            String drug = drugValues[position];
//            textView.setText(drug);

            if (drug.equals("Countries")) {
                imageView.setImageResource(R.drawable.country);
                textView.setText("Countries");
            } else if (drug.equals("General")) {
                imageView.setImageResource(R.drawable.gs);
                textView.setText("General Sales");
            } else if (drug.equals("Pharmacy")) {
                imageView.setImageResource(R.drawable.pharmacy);
                textView.setText("Pharmacy");
            } else if (drug.equals("Prescription")){
                imageView.setImageResource(R.drawable.prescription);
                textView.setText("Prescription");
            } else {
                imageView.setImageResource(R.drawable.medi);
            }

        } else {

            gridView = convertView;
        }

        return gridView;
    }

    @Override
    public int getCount() {
        return drugValues.length;
    }

    @Override
    public Object getItem(int position) {
        return null;
    }

    @Override
    public long getItemId(int position) {
        return 0;
    }
}
