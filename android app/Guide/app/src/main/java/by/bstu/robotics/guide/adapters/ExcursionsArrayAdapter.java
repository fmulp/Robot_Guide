package by.bstu.robotics.guide.adapters;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.TextView;

import java.util.ArrayList;
import java.util.List;

import by.bstu.robotics.guide.R;
import by.bstu.robotics.guide.classes.Excursion;

/**
 * Created by Admin on 08.01.2015.
 */
public class ExcursionsArrayAdapter extends ArrayAdapter<Excursion> {

    private Context mContext = null;
    private int resourceId;
    private ArrayList<Excursion> listOfExcursions = null;

    public ExcursionsArrayAdapter(Context context, int resource, List<Excursion> objects) {
        super(context, resource, objects);

        this.mContext = context;
        this.resourceId = resource;
        listOfExcursions = (ArrayList<Excursion>) objects;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {

        ViewHolder viewHolder;

        if(convertView == null){
            LayoutInflater inflater = (LayoutInflater) mContext.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
            convertView = inflater.inflate(resourceId, null);

            viewHolder = new ViewHolder();
            viewHolder.tvExcursionTitle = (TextView) convertView.findViewById(R.id.tvExcursionTitle);
            convertView.setTag(viewHolder);
        }
        else{
            viewHolder = (ViewHolder) convertView.getTag();
        }

        Excursion excursion = listOfExcursions.get(position);

        viewHolder.tvExcursionTitle.setText(excursion.getTitle());

        return convertView;
    }

    @Override
    public int getCount() {
        return listOfExcursions.size();
    }

    static class ViewHolder{
        TextView tvExcursionTitle;
    }

}
