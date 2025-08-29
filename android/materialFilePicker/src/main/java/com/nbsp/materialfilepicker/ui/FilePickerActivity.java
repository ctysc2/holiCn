package com.nbsp.materialfilepicker.ui;

import android.content.Intent;
import android.graphics.Color;
import android.os.Build;
import android.os.Bundle;
import android.os.Environment;
import android.os.Handler;
import android.text.TextUtils;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.view.WindowManager;
import android.widget.ImageButton;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;

import com.nbsp.materialfilepicker.R;
import com.nbsp.materialfilepicker.filter.FileFilter;
import com.nbsp.materialfilepicker.filter.PatternFilter;
import com.nbsp.materialfilepicker.utils.DimenUtil;
import com.nbsp.materialfilepicker.utils.FileUtils;

import java.io.File;
import java.io.Serializable;
import java.lang.reflect.Field;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.regex.Pattern;

public class FilePickerActivity extends AppCompatActivity implements DirectoryFragment.FileClickListener {
    public static final String ARG_START_FILE = "arg_start_path";
    public static final String ARG_CURRENT_FILE = "arg_current_path";

    public static final String ARG_FILTER = "arg_filter";
    public static final String ARG_CLOSEABLE = "arg_closeable";
    public static final String ARG_TITLE = "arg_title";

    public static final String STATE_START_FILE = "state_start_path";
    private static final String STATE_CURRENT_FILE = "state_current_path";

    public static final String RESULT_FILE_PATH = "result_file_path";
    private static final int HANDLE_CLICK_DELAY = 150;

    private Toolbar mToolbar;

    private TextView midText;

    private ImageButton back;

    private File mStart = Environment.getExternalStorageDirectory();
    private File mCurrent = mStart;

    private CharSequence mTitle;

    private Boolean mCloseable = true;

    private FileFilter mFilter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_file_picker);

        initArguments(savedInstanceState);
        initViews();
        initToolbar();
        setStatusBarTranslucent();
        if (savedInstanceState == null) {
            initBackStackState();
        }
    }

    private void initArguments(Bundle savedInstanceState) {
        if (getIntent().hasExtra(ARG_FILTER)) {
            Serializable filter = getIntent().getSerializableExtra(ARG_FILTER);

            if (filter instanceof Pattern) {
                mFilter = new PatternFilter((Pattern) filter, false);
            } else {
                mFilter = (FileFilter) filter;
            }
        }

        if (savedInstanceState != null) {
            mStart = (File) savedInstanceState.getSerializable(STATE_START_FILE);
            mCurrent = (File) savedInstanceState.getSerializable(STATE_CURRENT_FILE);
            updateTitle();
        } else {
            if (getIntent().hasExtra(ARG_START_FILE)) {
                mStart = (File) getIntent().getSerializableExtra(ARG_START_FILE);
                mCurrent = mStart;
            }

            if (getIntent().hasExtra(ARG_CURRENT_FILE)) {
                File currentFile = (File) getIntent().getSerializableExtra(ARG_CURRENT_FILE);

                if (FileUtils.isParent(currentFile, mStart)) {
                    mCurrent = currentFile;
                }
            }
        }

        if (getIntent().hasExtra(ARG_TITLE)) {
            mTitle = getIntent().getCharSequenceExtra(ARG_TITLE);
        }

        if (getIntent().hasExtra(ARG_CLOSEABLE)) {
            mCloseable = getIntent().getBooleanExtra(ARG_CLOSEABLE, true);
        }
    }

    private void initToolbar() {
        back = findViewById(R.id.back);
        back.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });
        midText = findViewById(R.id.midText);
        midText.setText("选择文件");
//        setSupportActionBar(mToolbar);
//
//        // Show back button
//        if (getSupportActionBar() != null) {
//            getSupportActionBar().setDisplayHomeAsUpEnabled(true);
//        }
//
//        // Truncate start of path
//        try {
//            Field f;
//            if (TextUtils.isEmpty(mTitle)) {
//                f = mToolbar.getClass().getDeclaredField("mTitleTextView");
//            } else {
//                f = mToolbar.getClass().getDeclaredField("mSubtitleTextView");
//            }
//
//            f.setAccessible(true);
//            TextView textView = (TextView) f.get(mToolbar);
//            textView.setEllipsize(TextUtils.TruncateAt.START);
//        } catch (Exception ignored) {
//        }
//
//        if (!TextUtils.isEmpty(mTitle)) {
//            setTitle(mTitle);
//        }
//        updateTitle();
    }

    private void initViews() {
        mToolbar = findViewById(R.id.toolbar);
    }

    private void initBackStackState() {
        final List<File> path = new ArrayList<>();

        File current = mCurrent;

        while (current != null) {
            path.add(current);

            if (current.equals(mStart)) {
                break;
            }

            current = FileUtils.getParentOrNull(current);
        }

        Collections.reverse(path);

        for (File file : path) {
            addFragmentToBackStack(file);
        }
    }

    private void updateTitle() {
        if (getSupportActionBar() != null) {
            final String titlePath = mCurrent.getAbsolutePath();
            if (TextUtils.isEmpty(mTitle)) {
                getSupportActionBar().setTitle(titlePath);
            } else {
                getSupportActionBar().setSubtitle(titlePath);
            }
        }
    }

    private void addFragmentToBackStack(File file) {
        getSupportFragmentManager()
                .beginTransaction()
                .replace(
                        R.id.container,
                        DirectoryFragment.getInstance(
                                file,
                                mFilter
                        )
                )
                .addToBackStack(null)
                .commit();
    }

    public void setStatusBarTranslucent(){
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);

        if (toolbar != null) {
            int statusBarHeight = DimenUtil.getStatusBarHeight(this);
            toolbar.setPadding(0, statusBarHeight, 0, 0);
            ViewGroup.LayoutParams params = toolbar.getLayoutParams();
            params.height = (int) DimenUtil.dp2px(44,this)+statusBarHeight;
            toolbar.setLayoutParams(params);
        }

        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
            getWindow().addFlags(WindowManager.LayoutParams.FLAG_TRANSLUCENT_STATUS);//透明状态栏
            // 状态栏字体设置为深色，SYSTEM_UI_FLAG_LIGHT_STATUS_BAR 为SDK23增加
            getWindow().getDecorView().setSystemUiVisibility(View.SYSTEM_UI_FLAG_LAYOUT_FULLSCREEN | View.SYSTEM_UI_FLAG_LIGHT_STATUS_BAR);
            getWindow().addFlags(WindowManager.LayoutParams.FLAG_DRAWS_SYSTEM_BAR_BACKGROUNDS);
            getWindow().clearFlags(WindowManager.LayoutParams.FLAG_TRANSLUCENT_STATUS);
            getWindow().setStatusBarColor(Color.TRANSPARENT);// SDK21

        }else if(Build.VERSION.SDK_INT >= Build.VERSION_CODES.KITKAT){
            // 设置状态栏透明
            getWindow().addFlags(WindowManager.LayoutParams.FLAG_TRANSLUCENT_STATUS);

        }else{
            if (toolbar != null) {
                toolbar.setPadding(0, 0, 0, 0);
                ViewGroup.LayoutParams params = toolbar.getLayoutParams();
                params.height = (int) DimenUtil.dp2px(44,this);
                toolbar.setLayoutParams(params);
            }
        }
    }



    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.menu, menu);
        menu.findItem(R.id.action_close).setVisible(mCloseable);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem menuItem) {
        if (menuItem.getItemId() == android.R.id.home) {
            onBackPressed();
        } else if (menuItem.getItemId() == R.id.action_close) {
            finish();
        }
        return super.onOptionsItemSelected(menuItem);
    }

    @Override
    public void onBackPressed() {
        if (getSupportFragmentManager().getBackStackEntryCount() > 1) {
            getSupportFragmentManager().popBackStack();
            mCurrent = FileUtils.getParentOrNull(mCurrent);
            updateTitle();
        } else {
            setResult(RESULT_CANCELED);
            finish();
        }
    }

    @Override
    public void onSaveInstanceState(@NonNull Bundle outState) {
        super.onSaveInstanceState(outState);
        outState.putSerializable(STATE_CURRENT_FILE, mCurrent);
        outState.putSerializable(STATE_START_FILE, mStart);
    }

    @Override
    public void onFileClicked(final File clickedFile) {
        new Handler().postDelayed(() -> handleFileClicked(clickedFile), HANDLE_CLICK_DELAY);
    }

    private void handleFileClicked(final File clickedFile) {
        if (isFinishing()) {
            return;
        }

        if (clickedFile.isDirectory()) {
            mCurrent = clickedFile;
            // If the user wanna go to the emulated directory, he will be taken to the
            // corresponding user emulated folder.
            if (mCurrent.getAbsolutePath().equals("/storage/emulated")) {
                mCurrent = Environment.getExternalStorageDirectory();
            }
            addFragmentToBackStack(mCurrent);
            updateTitle();
        } else {
            setResultAndFinish(clickedFile);
        }
    }

    private void setResultAndFinish(File file) {
        Intent data = new Intent();

        data.putExtra(RESULT_FILE_PATH, file.getPath());
        setResult(RESULT_OK, data);

        finish();
    }
}
