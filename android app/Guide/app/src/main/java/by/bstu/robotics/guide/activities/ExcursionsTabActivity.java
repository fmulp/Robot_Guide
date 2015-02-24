package by.bstu.robotics.guide.activities;

import java.util.Locale;

import android.content.Intent;
import android.support.v7.app.ActionBarActivity;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;
import android.os.Bundle;
import android.support.v4.view.ViewPager;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.GridView;
import android.widget.TextView;

import by.bstu.robotics.guide.R;
import by.bstu.robotics.guide.adapters.SimpleExhibitArrayAdapter;
import by.bstu.robotics.guide.classes.Excursion;
import by.bstu.robotics.guide.classes.ExcursionsService;
import by.bstu.robotics.guide.classes.Exhibit;
import by.bstu.robotics.guide.classes.HeaderGridView;

public class ExcursionsTabActivity extends ActionBarActivity implements View.OnClickListener{

    /**
     * The {@link android.support.v4.view.PagerAdapter} that will provide
     * fragments for each of the sections. We use a
     * {@link FragmentPagerAdapter} derivative, which will keep every
     * loaded fragment in memory. If this becomes too memory intensive, it
     * may be best to switch to a
     * {@link android.support.v4.app.FragmentStatePagerAdapter}.
     */
    SectionsPagerAdapter mSectionsPagerAdapter;

    ExcursionsService excursionsService = null;



    /**
     * The {@link ViewPager} that will host the section contents.
     */
    ViewPager mViewPager;
    Button btnStartExcursion;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_excursions_tab);

        excursionsService = new ExcursionsService(this.getApplicationContext());
        // Create the adapter that will return a fragment for each of the three
        // primary sections of the activity.
        mSectionsPagerAdapter = new SectionsPagerAdapter(getSupportFragmentManager());

        // Set up the ViewPager with the sections adapter.
        mViewPager = (ViewPager) findViewById(R.id.pager);
        mViewPager.setAdapter(mSectionsPagerAdapter);

        btnStartExcursion = (Button) findViewById(R.id.btnStartExcursion);
        btnStartExcursion.setOnClickListener(this);
    }




    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_excursions_tab, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        //noinspection SimplifiableIfStatement
        if (id == R.id.action_settings) {
            return true;
        }

        return super.onOptionsItemSelected(item);
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()){
            case R.id.btnStartExcursion:
                int indexOfExcursion = mViewPager.getCurrentItem();

                if(numberOfCheckedExhibit(indexOfExcursion) > 0) {
                    Intent intent = new Intent(this, ShowExcursionActivity.class);
                    intent.putExtra("excursion_index", indexOfExcursion);
                    startActivity(intent);
                }
                else{
                    Log.d("mylog", "number of exhibit = 0 !!");
                }

                break;
            default:
                break;
        }
    }

    private int numberOfCheckedExhibit(int indexOfExcursion) {
        int numberOfExhibit = 0;
        Excursion excursion = ExcursionsService.getExcursionByIndex(indexOfExcursion);

        for(Exhibit exhibit:excursion.getListOfExhibit()){
            if (exhibit.isChecked())
                numberOfExhibit++;
        }
        return numberOfExhibit;
    }

    public void updateExcursionDuration(){

    }


    /**
     * A {@link FragmentPagerAdapter} that returns a fragment corresponding to
     * one of the sections/tabs/pages.
     */
    public class SectionsPagerAdapter extends FragmentPagerAdapter {

        public SectionsPagerAdapter(FragmentManager fm) {
            super(fm);
        }

        @Override
        public Fragment getItem(int position) {
            // getItem is called to instantiate the fragment for the given page.
            // Return a PlaceholderFragment (defined as a static inner class below).
            return PlaceholderFragment.newInstance(position);
        }

        @Override
        public int getCount() {
            return ExcursionsService.EXCURSION_COUNT;
        }



        @Override
        public CharSequence getPageTitle(int position) {
            String pageTitle;

            pageTitle = ExcursionsService.getExcursionByIndex(position).getTitle();

            return pageTitle;
        }
    }

    /**
     * A placeholder fragment containing a simple view.
     */
    public static class PlaceholderFragment extends Fragment {
        /**
         * The fragment argument representing the section number for this
         * fragment.
         */
        private static final String ARG_SECTION_NUMBER = "section_number";

        /**
         * Returns a new instance of this fragment for the given section
         * number.
         */
        public static PlaceholderFragment newInstance(int sectionNumber) {
            PlaceholderFragment fragment = new PlaceholderFragment();
            Bundle args = new Bundle();
            args.putInt(ARG_SECTION_NUMBER, sectionNumber);
            fragment.setArguments(args);
            return fragment;
        }

        public PlaceholderFragment() {
        }


        @Override
        public View onCreateView
                (LayoutInflater inflater, ViewGroup container,
                                 Bundle savedInstanceState) {
            View rootView = inflater.inflate(R.layout.fragment_excursion, container, false);
            int excursionIndex = getArguments().getInt(ARG_SECTION_NUMBER);
            Excursion excursion = ExcursionsService.getExcursionByIndex(excursionIndex);

            HeaderGridView gridView = (HeaderGridView) rootView.findViewById(R.id.gvGridOfExhibit);
            View header = inflater.inflate(R.layout.header_grid_view, null);

            TextView tvDuration = (TextView) header.findViewById(R.id.tvDuration);
            tvDuration.setText(ExcursionsService.durationToString(excursion.getDuration()));

            TextView tvDescription = (TextView) header.findViewById(R.id.tvDescription);
            tvDescription.setText(excursion.getDescription());


            gridView.addHeaderView(header);
            SimpleExhibitArrayAdapter adapter = new SimpleExhibitArrayAdapter(getActivity(), R.layout.simple_exhibit_item, excursion, tvDuration);
            gridView.setAdapter(adapter);



            return rootView;
        }
    }



}
