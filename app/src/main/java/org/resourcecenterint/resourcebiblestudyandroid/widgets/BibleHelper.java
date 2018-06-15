package org.resourcecenterint.resourcebiblestudyandroid.widgets;

import android.app.Activity;

import com.google.gson.Gson;
import com.google.gson.JsonArray;
import com.google.gson.JsonElement;
import com.google.gson.JsonParser;

import org.resourcecenterint.resourcebiblestudyandroid.R;
import org.resourcecenterint.resourcebiblestudyandroid.model.Bible;
import org.resourcecenterint.resourcebiblestudyandroid.model.DailyScriptures;

import java.io.BufferedReader;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.util.ArrayList;

/**
 * Created by Olaolu.Ajose on 14/07/2016.
 */

public class BibleHelper {
    static Bible mBible;
    static ArrayList<DailyScriptures> mDailyScriptures;
    static Activity mActivity;

    public static Bible GetBible(Activity activity){
        mActivity = activity;
        if (mBible != null && mBible.getBooks().size() > 0 )return mBible;

        InputStream isReader = mActivity.getResources().openRawResource(R.raw.msg);

        BufferedReader reader = new BufferedReader(new InputStreamReader(isReader));
        return mBible = BibleExtractor(reader);

    }
     public static ArrayList<DailyScriptures> GetDailyScriptures(Activity activity){
         mActivity = activity;
         if (mDailyScriptures != null && mDailyScriptures.size() > 0 )return mDailyScriptures;

         InputStream isReader = mActivity.getResources().openRawResource(R.raw.dailyscriptures);

         BufferedReader reader = new BufferedReader(new InputStreamReader(isReader));
         return mDailyScriptures = DailyScriptureExtractor(reader);
    }
    public static Bible BibleExtractor(BufferedReader bibleJson){
        Gson gson = new Gson();
        Bible bible = new Bible();
        try{
            bible = gson.fromJson(bibleJson, Bible.class);
        }
        catch (Exception ex)
        {
            ex.printStackTrace();

            return  bible;
        }

        return bible;
    }

    public static ArrayList<DailyScriptures> DailyScriptureExtractor(BufferedReader bibleJson){
        Gson gson = new Gson();
        ArrayList<DailyScriptures> dailyScriptureList = new ArrayList<>();
        try{
            JsonArray jsonArray = new JsonParser().parse(bibleJson).getAsJsonArray();

            for (JsonElement dictionaryItem: jsonArray) {
                DailyScriptures dailyScripture = gson.fromJson(dictionaryItem, DailyScriptures.class);
                dailyScriptureList.add(dailyScripture);
            }}
        catch (Exception ex)
        {
            ex.printStackTrace();
            return  dailyScriptureList;
        }

        return dailyScriptureList;
    }
}
