package org.resourcecenterint.resourcebiblestudyandroid.fragments;

import android.app.Activity;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.Typeface;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v7.app.AlertDialog;
import android.text.Html;
import android.util.Log;
import android.view.GestureDetector;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import com.getbase.floatingactionbutton.FloatingActionButton;
import com.prolificinteractive.materialcalendarview.CalendarDay;
import com.prolificinteractive.materialcalendarview.MaterialCalendarView;

import org.resourcecenterint.resourcebiblestudyandroid.MainActivity;
import org.resourcecenterint.resourcebiblestudyandroid.R;
import org.resourcecenterint.resourcebiblestudyandroid.model.Bible;
import org.resourcecenterint.resourcebiblestudyandroid.model.Book;
import org.resourcecenterint.resourcebiblestudyandroid.model.Chapters;
import org.resourcecenterint.resourcebiblestudyandroid.model.DailyScriptures;
import org.resourcecenterint.resourcebiblestudyandroid.model.Verse;
import org.resourcecenterint.resourcebiblestudyandroid.widgets.BibleHelper;
import org.resourcecenterint.resourcebiblestudyandroid.widgets.CustomScaleGestures;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.List;



/**
 * A simple {@link Fragment} subclass.
 * Activities that contain this fragment must implement the
 * to handle interaction events.
 * Use the {@link DailyScriptureFragment#newInstance} factory method to
 * create an instance of this fragment.
 */
public class DailyScriptureFragment extends Fragment
{

    private static final String TAB_POSITION = "tab_position";


    String TAG =  DailyScriptureFragment.class.getName();
    private TextView mDailyReadingTitle;
    private Book mDailyReadingBook;
    private TextView mDailyReadingContent;

    Activity mActivity;
    static Typeface mTypeface;
    private Button mPreviousButton;
    private Button mNextButton;
    private FloatingActionButton mDailyScripturesFloatingActionButton;
    private FloatingActionButton mDiscussionFloatingActionButton;

    static ArrayList<DailyScriptures> mDailyScriptures;
    DailyScriptures todaysReading;

    public static Bible mBible;
    public DailyScriptureFragment() {
        // Required empty public constructor
    }

    public static DailyScriptureFragment newInstance(String param1, String param2) {
        DailyScriptureFragment fragment = new DailyScriptureFragment();

        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (getArguments() != null) {

        }
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View dailyFragmentView =  inflater.inflate(R.layout.fragment_daily_scripture, container, false);

        mActivity = getActivity();

        mDailyScriptures =  BibleHelper.GetDailyScriptures(mActivity);
        mBible =  BibleHelper.GetBible(mActivity);

        mDailyReadingTitle = (TextView)dailyFragmentView.findViewById(R.id.daily_reading_book_title);
        mDailyReadingContent = (TextView)dailyFragmentView.findViewById(R.id.daily_reading_bible_content);
        mPreviousButton = (Button) dailyFragmentView.findViewById(R.id.btnPrevious);
        mNextButton = (Button) dailyFragmentView.findViewById(R.id.btnNext);

        mDiscussionFloatingActionButton = (FloatingActionButton)dailyFragmentView.findViewById(R.id.discussion_fab);
        mDiscussionFloatingActionButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(getActivity(), MainActivity.class));
            }
        });

        mDailyScripturesFloatingActionButton = (FloatingActionButton)dailyFragmentView.findViewById(R.id.daily_fab);
        mDailyScripturesFloatingActionButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                showCalender();
            }
        });

        mNextButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                String dailyReadingText = mDailyReadingTitle.getText().toString();
                if (dailyReadingText.toLowerCase().contains("first"))
                {
                    loadDailyReading(13);

                }else if (dailyReadingText.toLowerCase().contains("second"))
                {
                    loadDailyReading(18);
                }
                else
                {
                    loadDailyReading(1);
                }
            }
        });
        mPreviousButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                String dailyReadingText = mDailyReadingTitle.getText().toString();
                if (dailyReadingText.toLowerCase().contains("first"))
                {
                    loadDailyReading(18);

                }else if (dailyReadingText.toLowerCase().contains("second"))
                {
                    loadDailyReading(1);
                }
                else
                {
                    loadDailyReading(13);
                }
            }
        });

        Calendar now = Calendar.getInstance();
        todaysReading = mDailyScriptures.get(now.get(Calendar.DAY_OF_YEAR)-1);
        int timeOfTheDay =  now.get(Calendar.HOUR_OF_DAY);
        try{

            loadDailyReading(timeOfTheDay);
        }
        catch(Exception exception)
        {
            Toast.makeText(mActivity, String.format("Unable to load reading plan.. :%s", exception.getLocalizedMessage()), Toast.LENGTH_LONG);
        }


        return dailyFragmentView;
    }
    private void loadDailyReading(int timeOfDay)
    {

        mDailyReadingTitle.setTypeface(mTypeface);
        mDailyReadingContent.setTypeface(mTypeface);

        if(timeOfDay < 12){
            String bookTitle;
            bookTitle = String.format("<strong>First Reading | %s </strong>",
                    todaysReading.getFirstReading());

            mDailyReadingTitle.setText(Html.fromHtml(bookTitle));

            String[] firstReading = todaysReading.getFirstReading().split("\\.");

            for (Book book : mBible.getBooks()) {
                if (book.getBookName().toLowerCase().contains(firstReading[0].trim().toLowerCase())) {

                    mDailyReadingBook = book;
                    break;
                }
            }
            String bibleText = "";

            //--------So we get the chapters if it contains a to (-) string --------
            List<Integer> chapterNumbers = new ArrayList<>();

            if (firstReading[1].trim().contains("-")) {
                String[] readingPlanChapter = firstReading[1].split("-");

                chapterNumbers.add(Integer.valueOf(readingPlanChapter[0].trim()));
                chapterNumbers.add(Integer.valueOf(readingPlanChapter[1].trim()));
            } else {
                chapterNumbers.add(Integer.valueOf(firstReading[1].trim()));
            }
            //-------------------- That's all folks --------------------------------


            for (int chapter : chapterNumbers) {

                Chapters chapters = mDailyReadingBook.getBookChapter().get(chapter - 1);

                bibleText += String.format("<p><strong>Chapter. %s </strong></p>", chapters.getChapterId());

                for (Verse verse : chapters.getChapterVerses()) {
                    bibleText += String.format("%s. %s <br/>", verse.getId(), verse.getVerseText());
                }
            }

            mDailyReadingContent.setText(Html.fromHtml(bibleText));
        }
        else if(timeOfDay<17){
            //fail safe should the section be empty..
            if (todaysReading.getSecondReading() == null ||todaysReading.getSecondReading().equalsIgnoreCase(""))
            {

                mDailyReadingTitle.setText(Html.fromHtml("<strong>Second Reading | None </strong>"));
                mDailyReadingContent.setText(getString(R.string.app_name));

            }else{
                String bookTitle;
                bookTitle = String.format("<strong>Second Reading | %s </strong>",
                        todaysReading.getSecondReading());

                mDailyReadingTitle.setText(Html.fromHtml(bookTitle));
                String[] secondReading = todaysReading.getSecondReading().split("\\.");

                for (Book book : mBible.getBooks()) {
                    if (book.getBookName().toLowerCase().contains(secondReading[0].trim().toLowerCase())) {

                        mDailyReadingBook = book;
                        break;
                    }
                }
                String bibleText = "";

                //--------So we get the chapters if it contains a to (-) string --------
                List<Integer> chapterNumbers = new ArrayList<>();
                if(secondReading[1].trim().contains("-")&& secondReading[1].trim().contains(":")){
                    String[] readingPlanChapter = secondReading[1].split(":");
                    chapterNumbers.add(Integer.valueOf(readingPlanChapter[0].trim()));
                }else
                if (secondReading[1].trim().contains("-")) {
                    String[] readingPlanChapter = secondReading[1].split("-");
                    chapterNumbers.add(Integer.valueOf(readingPlanChapter[0].trim()));
                    chapterNumbers.add(Integer.valueOf(readingPlanChapter[1].trim()));
                } else {
                    chapterNumbers.add(Integer.valueOf(secondReading[1].trim()));
                }
                //-------------------- That's all folks --------------------------------


                for (int chapter : chapterNumbers) {

                    Chapters chapters = mDailyReadingBook.getBookChapter().get(chapter - 1);
                    bibleText += String.format("<p><strong>Chapter. %s </strong></p>", chapters.getChapterId());
                    for (Verse verse : chapters.getChapterVerses()) {
                        bibleText += String.format("%s. %s <br/>", verse.getId(), verse.getVerseText());
                    }
                }
                mDailyReadingContent.setText(Html.fromHtml(bibleText));
                // get the gesture detector
                final GestureDetector mDetector = new GestureDetector(new CustomScaleGestures(getActivity()));

                View.OnTouchListener touchListener = new View.OnTouchListener() {
                    @Override
                    public boolean onTouch(View v, MotionEvent event) {
                        // pass the events to the gesture detector
                        // a return value of true means the detector is handling it
                        // a return value of false means the detector didn't
                        // recognize the event
                        return mDetector.onTouchEvent(event);

                    }
                };
                mDailyReadingContent.setOnTouchListener(touchListener);
            }
        }
        else{

            if (todaysReading.getSecondReading() == null ||todaysReading.getSecondReading().equals(""))
            {
                mDailyReadingTitle.setText(Html.fromHtml("<strong>Third Reading | None </strong>"));
                mDailyReadingContent.setText(R.string.app_name);

            }else {
                String bookTitle;

                bookTitle = String.format("<strong>Third Reading | %s </strong>",
                        todaysReading.getThirdReading());

                mDailyReadingTitle.setText(Html.fromHtml(bookTitle));
                String[] thirdReading = todaysReading.getThirdReading().split("\\.");

                for (Book book : mBible.getBooks()) {
                    if (book.getBookName().toLowerCase().contains(thirdReading[0].trim().toLowerCase())) {

                        mDailyReadingBook = book;
                        break;
                    }
                }
                String bibleText = "";

                //--------So we get the chapters if it contains a to (-) string --------
                List<Integer> chapterNumbers = new ArrayList<>();

                if (thirdReading[1].trim().contains("-")) {
                    String[] readingPlanChapter = thirdReading[1].split("-");
                    chapterNumbers.add(Integer.valueOf(readingPlanChapter[0].trim()));
                    chapterNumbers.add(Integer.valueOf(readingPlanChapter[1].trim()));
                } else {
                    chapterNumbers.add(Integer.valueOf(thirdReading[1].trim()));
                }
                //-------------------- That's all folks --------------------------------


                for (int chapter : chapterNumbers) {
                    Log.d(TAG, "Chapters to load"+ (chapter - 1));
                    Chapters chapters = mDailyReadingBook.getBookChapter().get(chapter - 1);
                    bibleText += String.format("<p><strong>Chapter. %s </strong></p>", chapters.getChapterId());
                   try{
                       for (Verse verse : chapters.getChapterVerses()) {
                           bibleText += String.format("%s. %s <br/>", verse.getId(), verse.getVerseText());
                       }
                   }catch(Exception ex){
                       ex.printStackTrace();
                   }
                }
                mDailyReadingContent.setText(Html.fromHtml(bibleText));
            }

        }
    }
    private void showCalender()
    {// Created a new Dialog

        String title = "Select A Date";
        String cancel = "Cancel";
        String ok = "Ok";

        final AlertDialog.Builder alertDialog = new AlertDialog.Builder(getActivity());
        alertDialog.setIcon(R.drawable.logo);
        alertDialog.setTitle(title);
        LayoutInflater factory = LayoutInflater.from(getActivity());

        final View calenderView =  factory.inflate(R.layout.resource_calender,null);
        alertDialog.setView(calenderView);
        alertDialog.setPositiveButton(
                ok,
                new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int arg1) {
                        MaterialCalendarView materialCalendarView = (MaterialCalendarView)calenderView.findViewById(R.id.calendarView);
                        CalendarDay selectedDate =  materialCalendarView.getSelectedDate();
                        if (selectedDate == null)return;

                        Calendar cal = Calendar.getInstance();
                        cal.set(selectedDate.getYear(),selectedDate.getMonth(),selectedDate.getDay());
                        int dayOfTheYear = cal.get(Calendar.DAY_OF_YEAR);


                        todaysReading =  mDailyScriptures.get(dayOfTheYear-1);
                        Log.d(TAG, "Selected Day "+ dayOfTheYear);
                        try{
                            loadDailyReading(1);
                        }
                        catch(Exception exception)
                        {
                            Toast.makeText(mActivity, String.format("Unable to load reading plan.. :%s", exception.getLocalizedMessage()), Toast.LENGTH_LONG);
                        }


                    }
                }
        );

        alertDialog.setNegativeButton(
                cancel,
                new DialogInterface.OnClickListener(){
                    public void onClick(DialogInterface dialog, int arg1) {
                        // do nothing
                    }
                }
        );

        //
        alertDialog.show();

    }


}
