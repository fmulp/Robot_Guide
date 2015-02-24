package by.bstu.robotics.guide.activities;

import android.app.Activity;
import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.drawable.Drawable;
import android.support.v7.app.ActionBarActivity;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;

import java.io.IOException;
import java.io.InputStream;

import by.bstu.robotics.guide.R;
import by.bstu.robotics.guide.classes.Excursion;
import by.bstu.robotics.guide.classes.ExcursionsService;
import by.bstu.robotics.guide.classes.Exhibit;

public class MoveToExhibitActivity extends Activity implements View.OnClickListener {

    TextView tvExhibitName;
    Button btnReady;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_move_to_exhibit);

        tvExhibitName = (TextView) findViewById(R.id.tvExhibitName);
        btnReady = (Button) findViewById(R.id.btnReady);
        btnReady.setOnClickListener(this);


        Intent intent = getIntent();
        int excursionIndex = intent.getIntExtra("excursion_index", 0);

        Excursion currentExcursion = ExcursionsService.getExcursionByIndex(excursionIndex);
        Exhibit currentExhibit = currentExcursion.getNextCheckedExhibit();

        TextView tvExhibitName = (TextView) findViewById(R.id.tvExhibitName);
        tvExhibitName.setText(currentExhibit.getName());
        TextView tvCurrentExhibit = (TextView) findViewById(R.id.tvCurrentExhibit);
        tvCurrentExhibit.setText((currentExcursion.getCurrentExhibitIndex() + 1) + "/" + currentExcursion.numberOfExhibits());
        ImageView ivExhibitVisual = (ImageView) findViewById(R.id.ivExhibitVisual);
        ivExhibitVisual.setImageDrawable(openImage(currentExhibit.getMainVisualURL()));

    }


    private Drawable openImage(String imageURL) {
        InputStream ims = null;

        try {
            ims = getAssets().open(ExcursionsService.IMAGES_PATH + "/" + imageURL);
        } catch (IOException e) {
            e.printStackTrace();
        }
        Drawable drawable = Drawable.createFromStream(ims, null);

        return drawable;
    }



    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_move_to_exhibit, menu);
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
    public void onBackPressed() {

    }

    @Override
    public void onClick(View v) {
        switch (v.getId()){
            case R.id.btnReady:
                setResult(RESULT_OK);
                finish();
                break;

            default:
                break;
        }
    }
}
