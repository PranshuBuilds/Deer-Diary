package com.example.s6;

import androidx.appcompat.app.AppCompatActivity;
import androidx.constraintlayout.widget.ConstraintLayout;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.graphics.drawable.AnimationDrawable;
import android.os.Bundle;
import android.view.MenuItem;
import android.view.View;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.example.s6.model.Diary;
import com.example.s6.model.DiarySaveStatus;
import com.example.s6.util.DiaryDataAccessor;

import java.text.SimpleDateFormat;
import java.util.concurrent.Executors;
import java.util.concurrent.ScheduledExecutorService;
import java.util.concurrent.TimeUnit;

//import me.anwarshahriar.calligrapher.Calligrapher;

public class DiaryDetailActivity extends AppCompatActivity {

    private TextView timeTextView;
    private EditText titleEditText;
    private EditText contentEditText;
    private ScheduledExecutorService executorService;
    private long currentDiaryId;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_diary_detail);
        //getSupportActionBar().setDisplayHomeAsUpEnabled(true);

        ConstraintLayout constraintLayout = findViewById(R.id.detaillayout);
        AnimationDrawable animationDrawable = (AnimationDrawable) constraintLayout.getBackground();
        animationDrawable.setEnterFadeDuration(2000);
        animationDrawable.setExitFadeDuration(4000);
        animationDrawable.start();

        initialize();
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        executorService.shutdownNow();
        saveDiary();
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case android.R.id.home:
                saveDiary();
                finish();
        }
        return super.onOptionsItemSelected(item);
    }

    @Override
    public void onBackPressed() {
        saveDiary();
        finish();
    }

    private void initialize() {
        bindViews();

        Intent intent = getIntent();
        currentDiaryId = intent.getIntExtra("diaryId", -1);
        if (currentDiaryId != -1) {
            Diary currentDiary = new DiaryDataAccessor(getApplicationContext()).get((int)currentDiaryId);
            showData(currentDiary);
        } else {
            showEmptyData();
        }
        setResult(1, new Intent());

        executorService = Executors.newScheduledThreadPool(1);
     }

    private void bindViews() {
        timeTextView = (TextView) findViewById(R.id.timeTextView);
        titleEditText = (EditText) findViewById(R.id.titleEditText);
        contentEditText = (EditText) findViewById(R.id.contentEditText);
    }

    @SuppressLint("SetTextI18n")
    private void showData(Diary diary) {
        timeTextView.setText(getFormattedUpdateTimeString(diary.updatedAt));
        titleEditText.setText(diary.title);
        contentEditText.setText(diary.content);
    }

    private void refreshUpdateTime() {
        if (currentDiaryId == -1) throw new IllegalStateException("currentDiaryId not specified");
        Diary currentDiary = new DiaryDataAccessor(getApplicationContext()).get((int)currentDiaryId);
        timeTextView.setText(getFormattedUpdateTimeString(currentDiary.updatedAt));
    }

    private String getFormattedUpdateTimeString(long time) {
        SimpleDateFormat dateformat = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
        String dateStr = dateformat.format(time);
        return "Last edited on " + dateStr + " (saved in real time)";
    }

    private void showEmptyData() {
        timeTextView.setVisibility(View.GONE);
    }

    private DiarySaveStatus saveDiary() {
        if (!titleEditText.getText().toString().trim().equals("")) {
            if (currentDiaryId == -1) {
                DiaryDataAccessor da = new DiaryDataAccessor(getApplicationContext());
                currentDiaryId = da.post(new Diary(
                        (int)currentDiaryId,
                        titleEditText.getText().toString(),
                        contentEditText.getText().toString(),
                        System.currentTimeMillis()));
                return DiarySaveStatus.CREATED;
            } else {
                return new DiaryDataAccessor(getApplicationContext())
                        .put(new Diary(
                                (int)currentDiaryId,
                                titleEditText.getText().toString(),
                                contentEditText.getText().toString(),
                                System.currentTimeMillis()));
            }
        } else {
            return DiarySaveStatus.NO_NEED_TO_SAVE;
        }
    }

    private class DataPersistenceService implements Runnable{
        public void run(){
            if (saveDiary() != DiarySaveStatus.NO_NEED_TO_SAVE) {
                runOnUiThread(new Runnable() {
                    @Override
                    public void run() {
                        refreshUpdateTime();
                    }
                });
            }
        }
    }


}
