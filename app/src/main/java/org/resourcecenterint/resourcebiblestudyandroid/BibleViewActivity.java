package org.resourcecenterint.resourcebiblestudyandroid;

import android.annotation.SuppressLint;
import android.content.Context;
import android.database.ContentObserver;
import android.graphics.Typeface;
import android.os.Bundle;
import android.os.Handler;
import android.provider.Settings;
import android.support.v7.app.ActionBar;
import android.support.v7.app.AppCompatActivity;
import android.text.Html;
import android.view.GestureDetector;
import android.view.MotionEvent;
import android.view.View;
import android.widget.CheckBox;
import android.widget.CompoundButton;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.SeekBar;
import android.widget.TextView;

import org.resourcecenterint.resourcebiblestudyandroid.model.Book;
import org.resourcecenterint.resourcebiblestudyandroid.model.Chapters;
import org.resourcecenterint.resourcebiblestudyandroid.model.Verse;
import org.resourcecenterint.resourcebiblestudyandroid.widgets.CustomScaleGestures;
import org.resourcecenterint.resourcebiblestudyandroid.widgets.ScreenUtils;
import org.resourcecenterint.resourcebiblestudyandroid.widgets.SettingManager;
import org.resourcecenterint.resourcebiblestudyandroid.widgets.SharedPreferencesUtil;


public class BibleViewActivity extends AppCompatActivity {
    static Book mBook;
    private static Chapters mSelectedChapter;
    /**
     * Whether or not the system UI should be auto-hidden after
     * {@link #AUTO_HIDE_DELAY_MILLIS} milliseconds.
     */
    private static final boolean AUTO_HIDE = true;

    /**
     * If {@link #AUTO_HIDE} is set, the number of milliseconds to wait after
     * user interaction before hiding the system UI.
     */
    LinearLayout rlReadAaSet;

    View rlBookReadRoot;

    ImageView ivBrightnessMinus;

    SeekBar seekbarLightness;
    SeekBar seekbarFontSize;
    ImageView ivBrightnessPlus;
    TextView tvFontsizeMinus;

    TextView tvBookReadMode;


    private boolean isAutoLightness = false;

    private boolean isNightMode = false;

    CheckBox cbVolume;

    CheckBox cbAutoBrightness;

    TextView tvFontsizePlus;

    private static final int AUTO_HIDE_DELAY_MILLIS = 3000;

    /**
     * Some older devices needs a small delay between UI widget updates
     * and a change of the status and navigation bar.
     */
    static Typeface mTypeface;
    private static final int UI_ANIMATION_DELAY = 300;
    private final Handler mHideHandler = new Handler();
    public TextView mContentView;
    private TextView mBookTitleView;
    GestureDetector mDetector;
    private final Runnable mHidePart2Runnable = new Runnable() {
        @SuppressLint("InlinedApi")
        @Override
        public void run() {
            // Delayed removal of status and navigation bar

            // Note that some of these constants are new as of API 16 (Jelly Bean)
            // and API 19 (KitKat). It is safe to use them, as they are inlined
            // at compile-time and do nothing on earlier devices.
            mContentView.setSystemUiVisibility(View.SYSTEM_UI_FLAG_LOW_PROFILE
                    | View.SYSTEM_UI_FLAG_FULLSCREEN
                    | View.SYSTEM_UI_FLAG_LAYOUT_STABLE
                    | View.SYSTEM_UI_FLAG_IMMERSIVE_STICKY
                    | View.SYSTEM_UI_FLAG_LAYOUT_HIDE_NAVIGATION
                    | View.SYSTEM_UI_FLAG_HIDE_NAVIGATION);
        }
    };
    private View mControlsView;
    private final Runnable mShowPart2Runnable = new Runnable() {
        @Override
        public void run() {
            // Delayed display of UI elements
            ActionBar actionBar = getSupportActionBar();
            if (actionBar != null) {
                actionBar.show();
            }
            mControlsView.setVisibility(View.VISIBLE);
        }
    };
    private boolean mVisible;
    private final Runnable mHideRunnable = new Runnable() {
        @Override
        public void run() {
            hide();
        }
    };
    /**
     * Touch listener to use for in-layout UI controls to delay hiding the
     * system UI. This is to prevent the jarring behavior of controls going away
     * while interacting with activity UI.
     */
    private final View.OnTouchListener mDelayHideTouchListener = new View.OnTouchListener() {
        @Override
        public boolean onTouch(View view, MotionEvent motionEvent) {
            if (AUTO_HIDE) {
                delayedHide(AUTO_HIDE_DELAY_MILLIS);
            }
            return false;
        }
    };

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        setContentView(R.layout.activity_bible_view);
        mTypeface = Typeface.createFromAsset(getAssets(), "OpenSans-Light.ttf");
        AppUtils.init(this);


        seekbarFontSize = (SeekBar) findViewById(R.id.seekbarFontSize);
        seekbarLightness = (SeekBar) findViewById(R.id.seekbarLightness);
        ivBrightnessMinus = (ImageView) findViewById(R.id.ivBrightnessMinus);
        ivBrightnessPlus = (ImageView) findViewById(R.id.ivBrightnessPlus);
        tvFontsizeMinus = (TextView) findViewById(R.id.tvFontsizeMinus);
        tvFontsizePlus = (TextView) findViewById(R.id.tvFontsizePlus);
        tvBookReadMode = (TextView) findViewById(R.id.tvBookReadMode);
        cbVolume = (CheckBox) findViewById(R.id.cbVolume);
        cbAutoBrightness = (CheckBox) findViewById(R.id.cbAutoBrightness);
        rlReadAaSet = (LinearLayout) findViewById(R.id.rlReadAaSet);

        rlBookReadRoot = findViewById(R.id.rlBookReadRoot);

        initPrefs();
        initAASet();


        mVisible = true;
        mControlsView = findViewById(R.id.llBookReadBottom);
        mContentView = (TextView) findViewById(R.id.bible_content);
        mBookTitleView = (TextView) findViewById(R.id.book_title);


        String bookTitle = String.format("<strong>The Book of %s </strong>", mBook.getBookName());
        mBookTitleView.setTypeface(mTypeface);
        mBookTitleView.setText(Html.fromHtml(bookTitle));


        tvBookReadMode.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                setReadMode();
            }
        });
        tvFontsizeMinus.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                fontsizeMinus();
            }
        });
        tvFontsizePlus.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                fontsizePlus();
            }
        });

        String bibleText = "";
        //So lets load only the selected chapter
        bibleText += String.format("<p><strong>Chapter. %s </strong></p>", mSelectedChapter.getChapterId());
        for (Verse verse : mSelectedChapter.getChapterVerses()) {
            bibleText += String.format("%s. %s <br/>", verse.getId(), verse.getVerseText());
        }

        mContentView.setTypeface(mTypeface);
        mContentView.setText(Html.fromHtml(bibleText));

        // get the gesture detector
        mDetector = new GestureDetector(new CustomScaleGestures(this));
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

        mContentView.setOnTouchListener(touchListener);


        // Set up the user interaction to manually show or hide the system UI.
      /*  mContentView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                toggle();
            }
        });*/

        // Upon interacting with UI controls, delay any scheduled hide()
        // operations to prevent the jarring behavior of controls going away
        // while interacting with the UI.
        findViewById(R.id.tvBookReadSettings).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (rlReadAaSet.getVisibility() == View.VISIBLE)
                    rlReadAaSet.setVisibility(View.GONE);
                else
                    rlReadAaSet.setVisibility(View.VISIBLE);
            }
        });

    }

    public void setReadMode() {
        if (!isNightMode) {
            rlBookReadRoot.setBackgroundResource(R.drawable.theme_night_bg);
            mContentView.setTextColor(getResources().getColor(R.color.white));
            mBookTitleView.setTextColor(getResources().getColor(R.color.white));
            isNightMode = true;
        } else {
            rlBookReadRoot.setBackgroundResource(R.drawable.theme_leather_bg);
            mContentView.setTextColor(getResources().getColor(R.color.primary_dark_material_dark));
            mBookTitleView.setTextColor(getResources().getColor(R.color.primary_dark_material_dark));
            isNightMode = false;
        }


    }

    public void fontsizeMinus() {
        calcFontSize(seekbarFontSize.getProgress() - 1);
    }

    public void fontsizePlus() {
        calcFontSize(seekbarFontSize.getProgress() + 1);
    }

    protected void initPrefs() {
        SharedPreferencesUtil.init(this, getPackageName() + "_preference", Context.MODE_MULTI_PROCESS);
    }

    private ContentObserver Brightness = new ContentObserver(new Handler()) {
        @Override
        public void onChange(boolean selfChange) {
            super.onChange(selfChange);
            if (!ScreenUtils.isAutoBrightness(getApplication())) {
                seekbarLightness.setProgress(ScreenUtils.getScreenBrightness());
            }
        }
    };

    private void initAASet() {
        seekbarFontSize.setMax(10);
        int fontSizePx = SettingManager.getInstance().getReadFontSize();
        int progress = (int) ((ScreenUtils.pxToDpInt(fontSizePx) - 8) / 1.7f);
        seekbarFontSize.setProgress(progress);
        seekbarFontSize.setOnSeekBarChangeListener(new SeekBarChangeListener());

        seekbarLightness.setMax(100);
        seekbarLightness.setOnSeekBarChangeListener(new SeekBarChangeListener());
        seekbarLightness.setProgress(ScreenUtils.getScreenBrightness());
        isAutoLightness = ScreenUtils.isAutoBrightness(this);


        getContentResolver().registerContentObserver(Settings.System.getUriFor(Settings.System.SCREEN_BRIGHTNESS), true, Brightness);

        if (SettingManager.getInstance().isAutoBrightness()) {
            startAutoLightness();
        } else {
            stopAutoLightness();
        }

        cbVolume.setChecked(SettingManager.getInstance().isVolumeFlipEnable());
        cbVolume.setOnCheckedChangeListener(new ChechBoxChangeListener());

        cbAutoBrightness.setChecked(SettingManager.getInstance().isAutoBrightness());
        cbAutoBrightness.setOnCheckedChangeListener(new ChechBoxChangeListener());


    }

    @Override
    protected void onPostCreate(Bundle savedInstanceState) {
        super.onPostCreate(savedInstanceState);

        // Trigger the initial hide() shortly after the activity has been
        // created, to briefly hint to the user that UI controls
        // are available.
        delayedHide(100);
    }

    private void toggle() {
        if (mVisible) {
            hide();
        } else {
            show();
        }
    }

    private void hide() {
        // Hide UI first
        ActionBar actionBar = getSupportActionBar();
        if (actionBar != null) {
            actionBar.hide();
        }
        mControlsView.setVisibility(View.GONE);
        mVisible = false;

        rlReadAaSet.setVisibility(View.GONE);
        // Schedule a runnable to remove the status and navigation bar after a delay
        mHideHandler.removeCallbacks(mShowPart2Runnable);
        mHideHandler.postDelayed(mHidePart2Runnable, UI_ANIMATION_DELAY);
    }

    @SuppressLint("InlinedApi")
    private void show() {
        // Show the system bar
        mContentView.setSystemUiVisibility(View.SYSTEM_UI_FLAG_LAYOUT_FULLSCREEN
                | View.SYSTEM_UI_FLAG_LAYOUT_HIDE_NAVIGATION);
        mVisible = true;

        // Schedule a runnable to display UI elements after a delay
        mHideHandler.removeCallbacks(mHidePart2Runnable);
        mHideHandler.postDelayed(mShowPart2Runnable, UI_ANIMATION_DELAY);
    }

    /**
     * Schedules a call to hide() in [delay] milliseconds, canceling any
     * previously scheduled calls.
     */
    private void delayedHide(int delayMillis) {
        mHideHandler.removeCallbacks(mHideRunnable);
        mHideHandler.postDelayed(mHideRunnable, delayMillis);
    }

    public static void setBook(Book book, Chapters chapter) {
        mBook = book;
        mSelectedChapter = chapter;
    }

    private void startAutoLightness() {
        SettingManager.getInstance().saveAutoBrightness(true);
        ScreenUtils.startAutoBrightness(this);
        seekbarLightness.setEnabled(false);
    }

    private void stopAutoLightness() {
        SettingManager.getInstance().saveAutoBrightness(false);
        ScreenUtils.stopAutoBrightness(this);
        seekbarLightness.setProgress((int) (ScreenUtils.getScreenBrightnessInt255() / 255.0F * 100));
        seekbarLightness.setEnabled(true);
    }

    private void calcFontSize(int progress) {
        // progress range 1 - 10
        if (progress >= 0 && progress <= 10) {
            seekbarFontSize.setProgress(progress);
            mContentView.setTextSize(ScreenUtils.dpToPxInt(8 + 1.7f * progress));
        }
    }

    private class SeekBarChangeListener implements SeekBar.OnSeekBarChangeListener {

        @Override
        public void onProgressChanged(SeekBar seekBar, int progress, boolean fromUser) {
            if (seekBar.getId() == seekbarFontSize.getId() && fromUser) {
                calcFontSize(progress);
            } else if (seekBar.getId() == seekbarLightness.getId() && fromUser
                    && !SettingManager.getInstance().isAutoBrightness()) {
                ScreenUtils.saveScreenBrightnessInt100(progress, getApplicationContext());
                //SettingManager.getInstance().saveReadBrightness(progress);
            }
        }

        @Override
        public void onStartTrackingTouch(SeekBar seekBar) {

        }

        @Override
        public void onStopTrackingTouch(SeekBar seekBar) {

        }
    }

    private class ChechBoxChangeListener implements CompoundButton.OnCheckedChangeListener {

        @Override
        public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
            if (buttonView.getId() == cbVolume.getId()) {
                SettingManager.getInstance().saveVolumeFlipEnable(isChecked);
            } else if (buttonView.getId() == cbAutoBrightness.getId()) {
                if (isChecked) {
                    startAutoLightness();
                } else {
                    stopAutoLightness();
                    ScreenUtils.saveScreenBrightnessInt255(ScreenUtils.getScreenBrightnessInt255(), AppUtils.getAppContext());
                }
            }
        }
    }
}
