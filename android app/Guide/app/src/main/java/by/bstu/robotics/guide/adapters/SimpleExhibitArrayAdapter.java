package by.bstu.robotics.guide.adapters;

import android.content.Context;
import android.graphics.drawable.Drawable;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.CheckBox;
import android.widget.CompoundButton;
import android.widget.ImageView;
import android.widget.TextView;

import java.io.IOException;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.List;

import by.bstu.robotics.guide.R;
import by.bstu.robotics.guide.classes.ExcursionsService;
import by.bstu.robotics.guide.classes.Exhibit;

public class SimpleExhibitArrayAdapter extends ArrayAdapter<Exhibit> {

    private Context mContext = null;
    private ArrayList<Exhibit> listOfExhibit = null;
    private int resourceId;

    public SimpleExhibitArrayAdapter(Context context, int resource, List<Exhibit> objects) {
        super(context, resource, objects);
        resourceId = resource;
        mContext = context;
        listOfExhibit = (ArrayList<Exhibit>) objects;
    }

    @Override
    public int getCount() {
        return listOfExhibit.size();
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        ViewHolder viewHolder;

        if(convertView == null){
            LayoutInflater inflater = (LayoutInflater) mContext.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
            convertView = inflater.inflate(resourceId, null);

            viewHolder = new ViewHolder();
            viewHolder.tvSimpleExhibitName = (TextView) convertView.findViewById(R.id.tvSimpleExhibitName);
            viewHolder.ivSimpleExhibit = (ImageView) convertView.findViewById(R.id.ivSimpleExhibit);
            viewHolder.cbIsChecked = (CheckBox) convertView.findViewById(R.id.cbIsChecked);
            viewHolder.tvDuration = (TextView) convertView.findViewById(R.id.tvDuration);

//            viewHolder.tvSimpleExhibitDescription = (TextView) convertView.findViewById(R.id.tvSimpleExhibitDescription);

//            int p = 50;
//            convertView.setPadding(p, p, p, p);
            convertView.setTag(viewHolder);
        }
        else{
            viewHolder = (ViewHolder) convertView.getTag();
        }

        final Exhibit exhibit = listOfExhibit.get(position);

        viewHolder.tvSimpleExhibitName.setText(exhibit.getName());
        viewHolder.ivSimpleExhibit.setImageDrawable(openAssetsDrawable(exhibit.getMainVisualURL()));
        viewHolder.cbIsChecked.setChecked(exhibit.isChecked());
        viewHolder.cbIsChecked.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
               exhibit.setChecked(isChecked);
            }
        });
        viewHolder.tvDuration.setText(ExcursionsService.durationToString(exhibit.getDuration()));
//        viewHolder.tvSimpleExhibitDescription.setText(exhibit.getDescription());

        return convertView;
    }

    private Drawable openAssetsDrawable(String mainVisualURL) {
        InputStream ims = null;
        try {
            ims = mContext.getAssets().open(ExcursionsService.IMAGES_PATH + "/" + mainVisualURL);
        } catch (IOException e) {
            e.printStackTrace();
        }
        Drawable drawable = Drawable.createFromStream(ims, null);
        return drawable;
    }


    static class ViewHolder{
        TextView tvSimpleExhibitName;
        ImageView ivSimpleExhibit;
        CheckBox cbIsChecked;
        TextView tvDuration;
    }
}
