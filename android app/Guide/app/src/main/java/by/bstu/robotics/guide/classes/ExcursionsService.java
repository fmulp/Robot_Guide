package by.bstu.robotics.guide.classes;

import android.content.Context;
import android.content.res.AssetManager;
import android.util.Log;

import org.xmlpull.v1.XmlPullParser;
import org.xmlpull.v1.XmlPullParserException;
import org.xmlpull.v1.XmlPullParserFactory;

import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.util.ArrayList;

/**
 * Created by Admin on 01.01.2015.
 */
public class ExcursionsService {

    public static String EXCURSIONS_PATH = "excursions";
    public static String SOUNDS_PATH = "sound";
    public static String IMAGES_PATH = "img";

    private static String TAG_EXCURSION = "excursion";
    private static String TAG_EXCURSION_DESCRIPTION = "description";
    private static String ATTRIBUTE_EXCURSION_NAME = "name";
    private static String TAG_EXHIBIT = "exhibit";
    private static String ATTRIBUTE_EXHIBIT_ID = "id";
    private static String ATTRIBUTE_EXHIBIT_NAME = "name";
    private static String TAG_EXHIBIT_DESCRIPTION = "description";
    private static String TAG_AUDIO = "audio";
    private static String TAG_VISUAL = "visual";
    private static String ATTRIBUTE_URL = "URL";

    public static int EXCURSION_COUNT;



    static ArrayList<Excursion> listOfExcursions = null;
    Context mContext = null;


    public ExcursionsService(Context context) {
        mContext = context;
        if(listOfExcursions == null) {
            listOfExcursions = new ArrayList<>();
            readListOfExcursions();
            EXCURSION_COUNT = listOfExcursions.size();
        }
    }

    public ArrayList<Excursion> getListOfExcursions() {
            readListOfExcursions();
        return listOfExcursions;
    }

    private void readListOfExcursions(){

        AssetManager assetManager = mContext.getAssets();

        try {

            String listFileName[] = assetManager.list(EXCURSIONS_PATH);

            if (listFileName != null) {
                InputStream inputStream = null;
                for (String fileName : listFileName) {
                    Log.d("FILE:", fileName);
                    inputStream = assetManager.open(EXCURSIONS_PATH + "/" + fileName);
                    Excursion excursion = readExcursionXMLFile(inputStream);
                    if (excursion != null)
                        listOfExcursions.add(excursion);
                }
            }

        } catch (IOException e) {
            Log.e("List error:", "can't list excursions");
        }

    }

    private Excursion readExcursionXMLFile(InputStream inputStream) {

        Excursion excursion = null;

        XmlPullParserFactory factory = null;
        try {
            factory = XmlPullParserFactory.newInstance();
            XmlPullParser parser = factory.newPullParser();
            parser.setInput(new InputStreamReader(inputStream));


            while (parser.getEventType() != XmlPullParser.END_DOCUMENT){

                if((parser.getEventType() == XmlPullParser.START_TAG) && parser.getName().equals(TAG_EXCURSION)) {
                    excursion = new Excursion(parser.getAttributeValue(parser.getNamespace(), ATTRIBUTE_EXCURSION_NAME));
                } else if((parser.getEventType() == XmlPullParser.START_TAG) && parser.getName().equals(TAG_EXCURSION_DESCRIPTION)){
                    excursion.setDescription(readDescription(parser));
                }
                else if ((parser.getEventType() == XmlPullParser.START_TAG) && parser.getName().equals(TAG_EXHIBIT)){
                    Exhibit exhibit = readExhibit(parser);
                    excursion.addExhibit(exhibit);
                }

                parser.next();
            }
        } catch (XmlPullParserException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }

        return excursion;

    }

    private Exhibit readExhibit(XmlPullParser parser) throws IOException, XmlPullParserException {
//        parser.getAttributeValue(parser.getNamespace(), ATTRIBUTE_EXHIBIT_ID);
        Exhibit exhibit = new Exhibit(Integer.parseInt(parser.getAttributeValue(parser.getNamespace(), ATTRIBUTE_EXHIBIT_ID)), parser.getAttributeValue(parser.getNamespace(), ATTRIBUTE_EXHIBIT_NAME));

        parser.next();
        while (!(parser.getEventType() == XmlPullParser.END_TAG && parser.getName().equals(TAG_EXHIBIT))){
            if(parser.getEventType() == XmlPullParser.START_TAG && parser.getName().equals(TAG_EXHIBIT_DESCRIPTION)){
                exhibit.setDescription(readDescription(parser));
            } else  if(parser.getEventType() == XmlPullParser.START_TAG && parser.getName().equals(TAG_AUDIO)){
                exhibit.setAudioURL(mContext, parser.getAttributeValue(parser.getNamespace(), ATTRIBUTE_URL));
            } else if(parser.getEventType() == XmlPullParser.START_TAG && parser.getName().equals(TAG_VISUAL)){
                exhibit.addVisualURL(parser.getAttributeValue(parser.getNamespace(), ATTRIBUTE_URL));
            }

            parser.next();
        }



        return exhibit;
    }

    private String readDescription(XmlPullParser parser) throws IOException, XmlPullParserException {
        String description = null;
        parser.next();
        while (!(parser.getEventType() == XmlPullParser.END_TAG && parser.getName().equals(TAG_EXHIBIT_DESCRIPTION))) {
            if (parser.getEventType() == XmlPullParser.TEXT) {
                description = parser.getText();
            }
            parser.next();
        }
        return description;
    }

    @Override
    public String toString() {
        return "ExcursionsService{" +
                "listOfExcursions=" + listOfExcursions +
                '}';
    }

    public static Excursion getExcursionByIndex(int excursionIndex) {
        Excursion excursion = listOfExcursions.get(excursionIndex);
//        excursion.reset();
        return excursion;
    }

    public static Excursion getModExcursionByIndex(int indexOfExcursion) {

        Excursion excursion = listOfExcursions.get(indexOfExcursion);
        Excursion modExcursion = new Excursion(excursion.getTitle());
        modExcursion.setDescription(excursion.getDescription());

        ArrayList<Exhibit> modList = modExcursion.getListOfExhibit();
        for(Exhibit exhibit:excursion.getListOfExhibit()){
            if(exhibit.isChecked()){
                modList.add(exhibit);
            }
        }

        return modExcursion;
    }

    public static String durationToString(int duration) {

        StringBuilder durationStr = new StringBuilder();
        int min, sec;
        duration /= 1000;
        sec = duration%60;
        min = duration/60;
        if(min > 0 )
            durationStr.append(min).append(" мин ");
        if(sec > 0 )
        durationStr.append(sec).append(" сек");
        return durationStr.toString();
    }
}
