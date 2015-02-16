package by.bstu.robotics.guide.activities;

import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.res.AssetFileDescriptor;
import android.graphics.drawable.Drawable;
import android.media.MediaPlayer;
import android.support.v7.app.ActionBarActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.GestureDetector;
import android.view.Menu;
import android.view.MenuItem;
import android.view.MotionEvent;
import android.view.View;
import android.widget.ImageButton;
import android.widget.ImageSwitcher;
import android.widget.ImageView;
import android.widget.RadioGroup;
import android.widget.SeekBar;
import android.widget.TextView;
import android.widget.ViewSwitcher;

import java.io.IOException;
import java.io.InputStream;

import by.bstu.robotics.guide.R;
import by.bstu.robotics.guide.classes.Excursion;
import by.bstu.robotics.guide.classes.ExcursionsService;
import by.bstu.robotics.guide.classes.Exhibit;

public class ShowExcursionActivity extends ActionBarActivity implements View.OnClickListener, ViewSwitcher.ViewFactory, GestureDetector.OnGestureListener, MediaPlayer.OnCompletionListener {

    Excursion currentExcursion;
    Exhibit currentExhibit;

    ImageSwitcher imageSwitcher;
    TextView tvExhibitDescription;
    ImageButton btnNextExhibit;
    ImageButton btnPause;
    ImageButton btnNextImage;
    ImageButton btnPrevImage;
    ImageButton btnStop;
    SeekBar seekBar;
    int indexOfExcursion;

    Runnable updateSeekBar;

    MediaPlayer mediaPlayer;

    GestureDetector gd;
    private static final int SWIPE_MIN_DISTANCE = 70;
    private static final int SWIPE_MAX_OFF_PATH = 250;
    private static final int SWIPE_THRESHOLD_VELOCITY = 100;

    private static final int REQ_CODE_MOVE_TO_EXHIBIT_ACTIVITY = 1;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_show_excursion);

        imageSwitcher = (ImageSwitcher) findViewById(R.id.isExhibitVisual);
        imageSwitcher.setOnClickListener(this);
        imageSwitcher.setFactory(this);
        imageSwitcher.setOnTouchListener(new View.OnTouchListener() {
            @Override
            public boolean onTouch(View v, MotionEvent event) {
                if(gd.onTouchEvent(event))
                    return true;
                return false;
            }
        });



        tvExhibitDescription = (TextView) findViewById(R.id.tvExhibitDescription);
        btnNextExhibit = (ImageButton) findViewById(R.id.btnNextExhibit);
        btnNextExhibit.setOnClickListener(this);
        btnPause = (ImageButton) findViewById(R.id.btnPause);
        btnPause.setOnClickListener(this);
        btnNextImage = (ImageButton) findViewById(R.id.btnNextImage);
        btnNextImage.setOnClickListener(this);
        btnPrevImage = (ImageButton) findViewById(R.id.btnPrevImage);
        btnPrevImage.setOnClickListener(this);
        btnStop = (ImageButton) findViewById(R.id.btnStop);
        btnStop.setOnClickListener(this);

        seekBar = (SeekBar) findViewById(R.id.seekBar);

        updateSeekBar = new Runnable() {
            @Override
            public void run() {
                if(mediaPlayer != null && mediaPlayer.isPlaying()) {
                    seekBar.setProgress(mediaPlayer.getCurrentPosition());
                    seekBar.postDelayed(updateSeekBar, 1000);
                }
            }
        };

        Intent intent = getIntent();
        indexOfExcursion = intent.getIntExtra("excursion_index", 0);
//        currentExcursion = ExcursionsService.getModExcursionByIndex(indexOfExcursion);
        currentExcursion = ExcursionsService.getExcursionByIndex(indexOfExcursion);
        currentExcursion.reset();

        gd = new GestureDetector(this, this);



        moveToNextExhibit();
    }

    private void moveToNextExhibit(){
        if(!currentExcursion.isLastCheckedExhibit()) {
//            currentExhibit = currentExcursion.getNextExhibit();

            Intent intent = new Intent(this, MoveToExhibitActivity.class);
            intent.putExtra("excursion_index", indexOfExcursion);
            startActivityForResult(intent, REQ_CODE_MOVE_TO_EXHIBIT_ACTIVITY);

        }else{
//            btnNextExhibit.setEnabled(false);
        }
    }

    private void showExhibit() {
        currentExhibit = currentExcursion.getCurrentExhibit();
        setTitle(currentExhibit.getName());
        if(currentExcursion.isLastCheckedExhibit()){
            btnNextExhibit.setEnabled(false);
        }
        if(currentExhibit.visualCount() > 1){
            hideButtonsOfVisual(false);
        }else {
            hideButtonsOfVisual(true);
        }
        tvExhibitDescription.setText(currentExhibit.getDescription());
        showImage(currentExhibit.getMainVisualURL());
        releaseMP();
        playMedia();
    }

    private void hideButtonsOfVisual(boolean hide) {
        if(hide){
            btnNextImage.setVisibility(View.INVISIBLE);
            btnPrevImage.setVisibility(View.INVISIBLE);
        }else {
            btnNextImage.setVisibility(View.VISIBLE);
            btnPrevImage.setVisibility(View.VISIBLE);
        }
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        if (requestCode == REQ_CODE_MOVE_TO_EXHIBIT_ACTIVITY){
            switch (resultCode){
                case RESULT_OK:
                    showExhibit();
                    break;
                case RESULT_CANCELED:
                    finish();
                    break;

                default:
                    break;
            }
        }

    }

    private void playMedia() {
        mediaPlayer = new MediaPlayer();
        AssetFileDescriptor afd = null;
        try {
            afd = getAssets().openFd(ExcursionsService.SOUNDS_PATH + "/" + currentExhibit.getAudioURL());
            mediaPlayer.setDataSource(afd.getFileDescriptor(),afd.getStartOffset(),afd.getLength());
            mediaPlayer.prepare();

        } catch (IOException e) {
            e.printStackTrace();
        }
        seekBar.setMax(mediaPlayer.getDuration());
        mediaPlayer.setOnCompletionListener(this);
        resumeMP();

    }

    private void pauseMP(){
        if(mediaPlayer.isPlaying()){
            mediaPlayer.pause();
        }
        btnPause.setImageResource(R.drawable.resume);
    }


    private  void resumeMP(){
        if(!mediaPlayer.isPlaying()){
            mediaPlayer.start();
        }
        btnPause.setImageResource(R.drawable.pause1);
        seekBar.post(updateSeekBar);
    }

    private void releaseMP() {
        if (mediaPlayer != null) {
            try {
                mediaPlayer.release();
                mediaPlayer = null;
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
    }

    private void showNextImage(){

//        Animation inAnim = AnimationUtils.loadAnimation(this, android.R.anim.slide_in_left);
//        Animation outAnim = AnimationUtils.loadAnimation(this, android.R.anim.slide_out_right);
//
//        imageSwitcher.setInAnimation(inAnim);
//        imageSwitcher.setOutAnimation(outAnim);

        showImage(currentExhibit.getNextVisualURL());
    }

    private void showPrevImage(){

//        Animation inAnim = AnimationUtils.loadAnimation(this, android.R.anim.slide_in_left);
//        Animation outAnim = AnimationUtils.loadAnimation(this, android.R.anim.slide_out_right);
//
//        imageSwitcher.setInAnimation(outAnim);
//        imageSwitcher.setOutAnimation(inAnim);

        showImage(currentExhibit.getPrevVisualURL());
    }

    private void showImage(String imageURL) {
        InputStream ims = null;

        try {
            ims = getAssets().open(ExcursionsService.IMAGES_PATH + "/" + imageURL);
        } catch (IOException e) {
            e.printStackTrace();
        }
        Drawable drawable = Drawable.createFromStream(ims, null);

        imageSwitcher.setImageDrawable(drawable);
    }


    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_show_excursion, menu);
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
            case R.id.btnNextExhibit: {
                AlertDialog.Builder builder = new AlertDialog.Builder(this);
                builder
                        .setMessage(getResources().getString(R.string.dialog_next_message))
//                        .setIcon(R.drawable.ic_android_cat)
                        .setCancelable(false)
                        .setNegativeButton(getResources().getString(R.string.no),
                                new DialogInterface.OnClickListener() {
                                    public void onClick(DialogInterface dialog, int id) {
                                        dialog.cancel();
                                    }
                                })
                        .setPositiveButton(getResources().getString(R.string.yes), new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialog, int which) {
                                moveToNextExhibit();
                            }
                        });
                AlertDialog alert = builder.create();
                alert.show();
            }
                break;
            case R.id.btnNextImage:
                showNextImage();
                break;
            case R.id.btnPrevImage:
                showPrevImage();
            case R.id.isExhibitVisual:
//                showImage(currentExhibit.getNextVisualURL());
                break;
            case R.id.btnPause:
                if(mediaPlayer.isPlaying()){
                    pauseMP();
                }
                else{
                    resumeMP();
                }
                break;
            case R.id.btnStop:
                pauseMP();
                 {
                AlertDialog.Builder builder = new AlertDialog.Builder(this);
                builder.setTitle(getResources().getString(R.string.dialog_stop_title))
                        .setMessage(getResources().getString(R.string.dialog_stop_message))
//                        .setIcon(R.drawable.ic_android_cat)
                        .setCancelable(false)
                        .setNegativeButton(getResources().getString(R.string.resume),
                                new DialogInterface.OnClickListener() {
                                    public void onClick(DialogInterface dialog, int id) {
                                        dialog.cancel();
                                        resumeMP();
                                    }
                                })
                        .setPositiveButton(getResources().getString(R.string.end_up), new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialog, int which) {
                                finish();
                            }
                        });
                AlertDialog alert = builder.create();
                alert.show();
                }
                break;
            default:
                break;
        }
    }

    @Override
    public View makeView() {
        ImageView imageView = new ImageView(this);
        imageView.setScaleType(ImageView.ScaleType.FIT_CENTER);
        imageView.setLayoutParams(new
                ImageSwitcher.LayoutParams(RadioGroup.LayoutParams.MATCH_PARENT, RadioGroup.LayoutParams.MATCH_PARENT));
        return imageView;
    }

    @Override
    public boolean onDown(MotionEvent e) {
        return false;
    }

    @Override
    public void onShowPress(MotionEvent e) {

    }

    @Override
    public boolean onSingleTapUp(MotionEvent e) {
        return false;
    }

    @Override
    public boolean onScroll(MotionEvent e1, MotionEvent e2, float distanceX, float distanceY) {
        return false;
    }

    @Override
    public void onLongPress(MotionEvent e) {

    }

    @Override
    public boolean onTouchEvent(MotionEvent event) {
        if (gd.onTouchEvent(event))
            return true;
        else
            return false;
    }

    @Override
    public boolean onFling(MotionEvent e1, MotionEvent e2, float velocityX, float velocityY) {
        try {

            if (Math.abs(e1.getY() - e2.getY()) > SWIPE_MAX_OFF_PATH)
                return false;

            // справа налево
            if (e1.getX() - e2.getX() > SWIPE_MIN_DISTANCE && Math.abs(velocityX) > SWIPE_THRESHOLD_VELOCITY)
            {
                showNextImage();
            } else if (e2.getX() - e1.getX() > SWIPE_MIN_DISTANCE && Math.abs(velocityX) > SWIPE_THRESHOLD_VELOCITY) {
//                showNextImage();
            }
        } catch (Exception e) {
            return true;
        }
        return true;
    }

    @Override
    protected void onPause() {
        super.onPause();
        releaseMP();
    }

    @Override
    public void onCompletion(MediaPlayer mp) {
        if(currentExcursion.isLastCheckedExhibit()) {
            AlertDialog.Builder builder = new AlertDialog.Builder(this);
            builder
                    .setMessage(getResources().getString(R.string.dialog_end_message))
//                        .setIcon(R.drawable.ic_android_cat)
                    .setCancelable(false)
                    .setNegativeButton(getResources().getString(R.string.no),
                            new DialogInterface.OnClickListener() {
                                public void onClick(DialogInterface dialog, int id) {
                                    dialog.cancel();
                                    btnPause.setImageResource(R.drawable.resume);
                                    seekBar.setProgress(0);
                                }
                            })
                    .setPositiveButton(getResources().getString(R.string.yes), new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialog, int which) {
                            finish();
                        }
                    });
            AlertDialog alert = builder.create();
            alert.show();
        }else
        {
            AlertDialog.Builder builder = new AlertDialog.Builder(this);
            builder
                    .setMessage(getResources().getString(R.string.dialog_next_message))
//                        .setIcon(R.drawable.ic_android_cat)
                    .setCancelable(false)
                    .setNegativeButton(getResources().getString(R.string.no),
                            new DialogInterface.OnClickListener() {
                                public void onClick(DialogInterface dialog, int id) {
                                    dialog.cancel();
                                    btnPause.setImageResource(R.drawable.resume);
                                    seekBar.setProgress(0);
                                }
                            })
                    .setPositiveButton(getResources().getString(R.string.yes), new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialog, int which) {
                            moveToNextExhibit();
                        }
                    });
            AlertDialog alert = builder.create();
            alert.show();
        }
    }
}
