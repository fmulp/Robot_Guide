package by.bstu.robotics.guide.classes;

import android.content.Context;
import android.content.res.AssetFileDescriptor;
import android.media.MediaPlayer;

import java.io.IOException;
import java.util.ArrayList;

/**
 * Created by Admin on 06.01.2015.
 */
public class Exhibit {
    int id;
    String name;
    String description;
    String audioURL;
    boolean isChecked;
    ArrayList<String> listOfVisualURL;
    int currentIndexVisual;
    private int duration;

    public Exhibit(int id, String name) {
        this.id = id;
        this.name = name;
        description = "default description";
        audioURL = null;
        isChecked = true;
        listOfVisualURL = new ArrayList<>();
        currentIndexVisual = -1;
    }

    public boolean isChecked() {
        return isChecked;
    }

    public void setChecked(boolean isVisible) {
        this.isChecked = isVisible;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public String getAudioURL() {
        return audioURL;
    }

    public void setAudioURL(Context mContext, String audioURL) {
        this.audioURL = audioURL;

        int duration = 0;
        MediaPlayer mediaPlayer = new MediaPlayer();
        AssetFileDescriptor afd = null;
        try {
            afd = mContext.getAssets().openFd(ExcursionsService.SOUNDS_PATH + "/" + audioURL);
            mediaPlayer.setDataSource(afd.getFileDescriptor(),afd.getStartOffset(),afd.getLength());
            mediaPlayer.prepare();

        } catch (IOException e) {
            e.printStackTrace();
        }
        this.duration = mediaPlayer.getDuration();
    }

    public int getDuration() {
        return duration;
    }

    public ArrayList<String> getListOfVisualURL() {
        return listOfVisualURL;
    }

    public void addVisualURL(String visualURL){
        this.listOfVisualURL.add(visualURL);
    }
//    public void setVisualURL(ArrayList<String> listOfVisualURL) {
//        this.listOfVisualURL = listOfVisualURL;
//    }



    @Override
    public String toString() {
        return "Exhibit{" +
                "isChecked=" + isChecked +
                ", id=" + id +
                ", name='" + name + '\'' +
                ", description='" + description + '\'' +
                ", audioURL='" + audioURL + '\'' +
                '}';
    }

    public String getMainVisualURL() {
        if(listOfVisualURL.size() >= 1) {
            currentIndexVisual = 0;
            return listOfVisualURL.get(0);
        }
        else
            return "";
    }

    public String getNextVisualURL(){
        if(!isLastVisual())
            currentIndexVisual++;
        else
            currentIndexVisual = 0;
        return listOfVisualURL.get(currentIndexVisual);
    }

    public String getPrevVisualURL(){
        if(!isFirstVisual())
            currentIndexVisual--;
        else
            currentIndexVisual = listOfVisualURL.size() - 1;
        return  listOfVisualURL.get(currentIndexVisual);
    }

    public boolean isLastVisual() {
        return (listOfVisualURL.size() - 1) < (currentIndexVisual + 1);
    }

    public boolean isFirstVisual(){
        return currentIndexVisual == 0;
    }

    public int visualCount() {
        return listOfVisualURL.size();
    }

//    public String getFirstImageURL() {
//        if(listOfVisualURL.size() >= 1)
//            return listOfVisualURL.get(0);
//        else
//            return "";
//    }
}
