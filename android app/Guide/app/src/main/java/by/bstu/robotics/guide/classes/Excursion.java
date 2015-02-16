package by.bstu.robotics.guide.classes;

import java.util.ArrayList;
import java.util.Collections;

/**
 * Created by Admin on 01.01.2015.
 */
public class Excursion {

    String title;
    String description;
    ArrayList<Exhibit> listOfExhibit;

    int currentExhibitIndex = -1;

    public Excursion(String title) {
        this.title = title;
        this.description = "default description";
        listOfExhibit = new ArrayList<>();
    }

    public Excursion(Excursion excursion) {
        this.title = excursion.getTitle();
        this.description = excursion.getDescription();
        this.listOfExhibit = new ArrayList<>();
        Collections.copy(this.listOfExhibit, excursion.getListOfExhibit());
    }

    public Exhibit getNextCheckedExhibit(){
        Exhibit exhibit = null;
        if(!isLastCheckedExhibit())
            for(int i = currentExhibitIndex + 1; i < listOfExhibit.size(); i++){
                if(listOfExhibit.get(i).isChecked()) {
                    currentExhibitIndex = i;
                    exhibit = listOfExhibit.get(i);
                    break;
                }
            }
        return exhibit;
    }

    public Exhibit getCurrentExhibit(){
        return listOfExhibit.get(currentExhibitIndex);
    }

    public int numberOfExhibits(){
        return listOfExhibit.size();
    }

    public boolean isLastCheckedExhibit() {
        boolean ret = true;
        for(int i = currentExhibitIndex + 1; i < listOfExhibit.size(); i++){
            if(listOfExhibit.get(i).isChecked()) {
                ret = false;
                break;
            }
        }
        return ret;
//        if((currentExhibitIndex + 1) > (listOfExhibit.size() - 1))
//            return true;
//        else
//            return false;
    }

    public int getCurrentExhibitIndex() {
        return currentExhibitIndex;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public ArrayList<Exhibit> getListOfExhibit() {
        return listOfExhibit;
    }

    public void setListOfExhibit(ArrayList<Exhibit> listOfExhibit) {
        this.listOfExhibit = listOfExhibit;
    }

    public void addExhibit(Exhibit exhibit){
        if(exhibit != null)
            listOfExhibit.add(exhibit);
    }

    @Override
    public String toString() {
        return "Excursion{" +
                "description='" + description + '\'' +
                ", title='" + title + '\'' +
                ", listOfExhibit=" + listOfExhibit +
                '}';
    }

    public void reset() {
        currentExhibitIndex = -1;
    }

    public int getDuration() {
        int duration = 0;
        for(Exhibit exhibit:listOfExhibit){
            if(exhibit.getDuration() > 0)
                duration += exhibit.getDuration();
        }
        return duration;
    }
}
