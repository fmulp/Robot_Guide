package by.bstu.robotics.guide.activities;

import android.content.res.AssetFileDescriptor;
import android.graphics.drawable.Drawable;
import android.media.MediaPlayer;
import android.support.v7.app.ActionBarActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.ImageSwitcher;
import android.widget.ImageView;

import java.io.IOException;
import java.io.InputStream;

import by.bstu.robotics.guide.R;
import by.bstu.robotics.guide.classes.ExcursionsService;


public class MainActivity extends ActionBarActivity{

    ImageSwitcher imageSwitcher;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

//        imageSwitcher = (ImageSwitcher) findViewById(R.id.imageSwitcher);
//
//
//        ImageView imageView = new ImageView(this);
//
//        InputStream ims = null;
//        try {
//            ims = getAssets().open("img/bar.png");
//        } catch (IOException e) {
//            e.printStackTrace();
//        }
//        Drawable d = Drawable.createFromStream(ims, null);
//        imageView.setImageDrawable(d);
//
//
//        imageSwitcher.addView(imageView);

//        ExcursionsService excursionsService = new ExcursionsService(this);
//        excursionsService.readListOfExcursions();

//        MediaPlayer player = new MediaPlayer();
//        AssetFileDescriptor afd = null;
//        try {
//            afd = getAssets().openFd("sound/welcome.mp3");
//            player.setDataSource(afd.getFileDescriptor(),afd.getStartOffset(),afd.getLength());
//            player.prepare();
//
//        } catch (IOException e) {
//            e.printStackTrace();
//        }
//
//        player.start();

//        ExcursionsService excursionsService = new ExcursionsService(this);
//        excursionsService.readListOfExcursions();
//        Log.d("myLog", excursionsService.toString());

    }


    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_main, menu);
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

}
