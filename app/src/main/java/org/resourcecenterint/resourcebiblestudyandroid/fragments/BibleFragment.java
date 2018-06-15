package org.resourcecenterint.resourcebiblestudyandroid.fragments;

import android.app.Activity;
import android.app.ProgressDialog;
import android.content.Intent;
import android.content.res.Configuration;
import android.graphics.Bitmap;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.Rect;
import android.graphics.Typeface;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ImageView;

import com.getbase.floatingactionbutton.FloatingActionButton;

import org.resourcecenterint.resourcebiblestudyandroid.BibleViewActivity;
import org.resourcecenterint.resourcebiblestudyandroid.R;
import org.resourcecenterint.resourcebiblestudyandroid.model.Bible;
import org.resourcecenterint.resourcebiblestudyandroid.model.Book;
import org.resourcecenterint.resourcebiblestudyandroid.model.Chapters;
import org.resourcecenterint.resourcebiblestudyandroid.model.Verse;
import org.resourcecenterint.resourcebiblestudyandroid.widgets.BibleHelper;
import org.resourcecenterint.resourcebiblestudyandroid.widgets.CustomExceptionHandler;
import org.resourcecenterint.resourcebiblestudyandroid.widgets.DraggableGridView;

import java.util.Random;


/**
 * A simple {@link Fragment} subclass.
 * Activities that contain this fragment must implement the
 * to handle interaction events.
 * Use the {@link BibleFragment#newInstance} factory method to
 * create an instance of this fragment.
 */

public class BibleFragment extends Fragment {
    RecyclerView recyclerView;
    static  int GRID_ROWS =0;
    private DraggableGridView draggableGridView;
    Activity mActivity;
    static Typeface mTypeface;
    static Random random = new Random();
    private FloatingActionButton mBibleFloatingActionButton;
    private static Chapters mSelectedChapter;
    private static Book mSelectedBook;
    public static Bible mBible;

    private ProgressDialog mProgressDialog;

    public BibleFragment() {
        // Required empty public constructor
    }


    public static BibleFragment newInstance() {
        BibleFragment fragment = new BibleFragment();
        Bundle args = new Bundle();

        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (getArguments() != null) {
        }

        mActivity= getActivity();
        mBible =  BibleHelper.GetBible(mActivity);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {

        Object exception = Thread.getDefaultUncaughtExceptionHandler();
        if (!(exception.getClass().isInstance(CustomExceptionHandler.class))) {
            Thread.setDefaultUncaughtExceptionHandler(new CustomExceptionHandler());
        }

        showProgressDialog("Loading Fragment");

        mTypeface = Typeface.createFromAsset(mActivity.getAssets(), "OpenSans-Light.ttf");

        // Inflate the layout for this fragment
        View bibleView =  inflater.inflate(R.layout.fragment_bible, container, false);

        recyclerView = (RecyclerView)bibleView.findViewById(R.id.bible_recyclerview);

        recyclerView.setLayoutManager(new LinearLayoutManager(mActivity));

        draggableGridView = ((DraggableGridView)bibleView.findViewById(R.id.bibleDraggableView));

        mBibleFloatingActionButton = (FloatingActionButton) bibleView.findViewById(R.id.bible_fab);
        Configuration config = getResources().getConfiguration();

        if (config.orientation == Configuration.ORIENTATION_LANDSCAPE) {
            GRID_ROWS = 4;
        } else {
            GRID_ROWS = 3;
        }
        setUpDraggableViewForBible();
        recyclerView.setLayoutManager(new GridLayoutManager(mActivity, GRID_ROWS));
        hideProgressDialog();
        return  bibleView;
    }
    private void setUpDraggableViewForBible()
    {

        draggableGridView.removeAllViews();
        for (Book book : mBible.getBooks() ){

            ImageView view = new ImageView(mActivity);

            view.setImageBitmap(getThumb(book.getBookName()));
            draggableGridView.addView(view);
        }

        draggableGridView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> arg0, View arg1, int childPosition, long arg3) {
                mSelectedBook = mBible.getBooks().get(childPosition);
                setUpDraggableViewForChapters();

            }
        });
        setUpFloatingBarAction(0);
    }
    private void setUpDraggableViewForChapters()
    {
        draggableGridView.removeAllViews();

        for (Chapters chapters :  mSelectedBook.getBookChapter() ){

            ImageView view = new ImageView(mActivity);

            String chapterName =  String.format("Chapter %s", chapters.getChapterId());
            view.setImageBitmap(getThumb(chapterName));
            draggableGridView.addView(view);
        }

        draggableGridView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> arg0, View arg1, int childPosition, long arg3) {
                mSelectedChapter = mSelectedBook.getBookChapter().get(childPosition);
                setUpDraggableViewForVerses();

            }
        });
        setUpFloatingBarAction(1);
    }
    /**
     * set Up Draggable View For Verses
     */
    private void setUpDraggableViewForVerses()
    {
        showProgressDialog("Loading book...");
        draggableGridView.removeAllViews();


        for (Verse verse : mSelectedChapter.getChapterVerses() ){

            ImageView view = new ImageView(mActivity);

            String verseName =  String.format("%s %s",  "Verse", verse.getId());
            view.setImageBitmap(getThumb(verseName));
            draggableGridView.addView(view);

        }
        draggableGridView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> arg0, View arg1, int childPosition, long arg3) {

                BibleViewActivity.setBook(mSelectedBook,mSelectedChapter);
                startActivity(new Intent(mActivity, BibleViewActivity.class));
            }
        });
        setUpFloatingBarAction(2);
        hideProgressDialog();
    }

    private void hideProgressDialog() {
        if (mProgressDialog != null && mProgressDialog.isShowing()) {
            mProgressDialog.hide();
        }
    }
    private static Bitmap getThumb(String s)
    {
        Bitmap bmp = Bitmap.createBitmap(150, 150, Bitmap.Config.RGB_565);
        Canvas canvas = new Canvas(bmp);
        Paint paint = new Paint();

        paint.setColor(Color.rgb(random.nextInt(128), random.nextInt(128), random.nextInt(128)));
        paint.setTextSize(24);
        paint.setFlags(Paint.ANTI_ALIAS_FLAG);
        canvas.drawRect(new Rect(0, 0, 150, 150), paint);
        paint.setColor(Color.WHITE);
        paint.setTextAlign(Paint.Align.CENTER);
        canvas.drawText(s, 75, 75, paint);

        return bmp;
    }

    public void setUpFloatingBarAction(int action)
    {
        mBibleFloatingActionButton.setVisibility(View.VISIBLE);
        switch (action)
        {
            case 0:
                mBibleFloatingActionButton.setVisibility(View.GONE);
                break;

            case 1:
                mBibleFloatingActionButton.setIcon(R.drawable.ic_left);
                mBibleFloatingActionButton.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        setUpDraggableViewForBible();
                    }
                });
                break;
            case 2:
                mBibleFloatingActionButton.setIcon(R.drawable.ic_left);
                mBibleFloatingActionButton.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {

                        setUpDraggableViewForChapters();
                    }
                });

                break;
        }
    }


    private void showProgressDialog(String message)
    {
        if (mProgressDialog == null) {

            mProgressDialog = new ProgressDialog(getActivity());
            mProgressDialog.setIndeterminate(true);
            mProgressDialog.setMessage(message);

        }

        mProgressDialog.show();
    }
}
