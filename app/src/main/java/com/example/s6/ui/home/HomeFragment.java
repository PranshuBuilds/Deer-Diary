package com.example.s6.ui.home;

import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.drawable.AnimationDrawable;
import android.os.Bundle;
import android.os.Handler;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuItem;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.Nullable;
import androidx.annotation.NonNull;
import androidx.appcompat.view.menu.MenuBuilder;
import androidx.coordinatorlayout.widget.CoordinatorLayout;
import androidx.core.view.MotionEventCompat;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.Observer;
import androidx.lifecycle.ViewModelProviders;
import androidx.recyclerview.widget.DefaultItemAnimator;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.s6.DiaryDetailActivity;
import com.example.s6.MainActivity;
import com.example.s6.R;
import com.example.s6.model.Diary;
import com.example.s6.util.DiaryDataAccessor;
//import com.fevziomurtekin.searchview.SearchView;
import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.google.android.material.snackbar.Snackbar;

import java.lang.reflect.Method;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.List;

//import me.anwarshahriar.calligrapher.Calligrapher;

public class HomeFragment extends Fragment  {

//    SearchView searchView;
    private TextView noDiaryHintTextView;
    private RecyclerView recyclerView;

    private MyRecyclerViewAdapter myRecyclerViewAdapter;


    private List<Diary> diaries = new ArrayList<Diary>();

    public View onCreateView(@NonNull LayoutInflater inflater,
                             ViewGroup container, Bundle savedInstanceState) {

        View root = inflater.inflate(R.layout.fragment_home, container, false);
        final FloatingActionButton fab =  root.findViewById(R.id.fab);
        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                startDiaryDetailActivityForNewDiary();
            }
        });
//        searchView      = new SearchView(getContext());
//        searchView      . setSearchView_hint("Search");
//        searchView      . setSearchView_textSize(14f);
//        searchView      . setSearchView_animationTime(250);
//        searchView      . setSearchView_textColor(getResources().getColor(R.color.pomegranate));
//        searchView      . setResult_textSize(14f);
//        searchView      . setResult_textColor(getResources().getColor(R.color.colorPrimaryDark));
        recyclerView =  root.findViewById(R.id.list);
        // LinearLayoutManager
        recyclerView.setLayoutManager(new LinearLayoutManager(getContext()));
        // ItemAnimator
        recyclerView.setItemAnimator(new DefaultItemAnimator());
        recyclerView.setHasFixedSize(true);
        noDiaryHintTextView =  root.findViewById(R.id.noDiaryHintTextView);
        initialize();

        recyclerView.addOnScrollListener(new RecyclerView.OnScrollListener(){
            @Override
            public void onScrolled(RecyclerView recyclerView, int dx, int dy){
                if (dy<0 && !fab.isShown())
                    fab.show();
                else if(dy>0 && fab.isShown())
                    fab.hide();
            }

            @Override
            public void onScrollStateChanged(RecyclerView recyclerView, int newState) {
                super.onScrollStateChanged(recyclerView, newState);
            }
        });

        return root;

    }


    private void initialize() {


        loadData();
        refreshDiaryViews();
    }



    private void loadData() {
        diaries = new DiaryDataAccessor(getContext()).getAll();
    }

    private void startDiaryDetailActivityForNewDiary() {
        Intent intent = new Intent(getContext(), DiaryDetailActivity.class);
        startActivityForResult(intent, 1);
    }

    private void refreshDiaryViews() {
        //
        //Initialize a custom adapter
        myRecyclerViewAdapter = new MyRecyclerViewAdapter(getContext(), diaries);

        myRecyclerViewAdapter.setOnItemClickListener(new MyItemClickListener() {
            @Override
            public void onItemClick(View view, int position) {
                Intent intent = new Intent(getContext(), DiaryDetailActivity.class);
                intent.putExtra("diaryId", diaries.get(position)._id);
                startActivityForResult(intent, 1);
            }

            @Override
            public void onItemLongClick(View view, int position) {
                deleteDiary(view, position);
            }
        });

        // mRecyclerView
        recyclerView.setAdapter(myRecyclerViewAdapter);

        if (!diaries.isEmpty()) {
            noDiaryHintTextView.setVisibility(View.GONE);
        } else {
            noDiaryHintTextView.setVisibility(View.VISIBLE);
        }

    }

    private void deleteDiary(final View view, final int position) {
        new AlertDialog.Builder(getContext())
                .setMessage("do you want to delete this note？")
                .setPositiveButton("delete", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialogInterface, int i) {
                        final int deletingDiaryId = diaries.get(position)._id;
                        final Handler handler = new Handler();
                        final Runnable runnable = new Runnable() {
                            @Override
                            public void run() {
                                new DiaryDataAccessor(getContext()).delete(deletingDiaryId);
//                                loadData();
//                                refreshDiaryViews();
                            }
                        };
                        handler.postDelayed(runnable, 3500);

                        Snackbar.make(view, "Note deleted", Snackbar.LENGTH_LONG)
                                .setAction("Revoke", new View.OnClickListener() {
                                    @Override
                                    public void onClick(View view) {
                                        handler.removeCallbacks(runnable);
                                        loadData();
                                        refreshDiaryViews();
                                    }
                                }).show();

                        diaries.remove(position);
                        refreshDiaryViews();
                    }
                })
                .setNegativeButton("Cancel", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialogInterface, int i) {}
                }).setIcon(R.drawable.common_google_signin_btn_icon_dark)
                .create()
                .show();

    }


    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (resultCode == 1) {
            loadData();
            refreshDiaryViews();
        }
    }


}

interface MyItemClickListener {
    public void onItemClick(View view, int position);
    public void onItemLongClick(View view, int position);
}

class MyRecyclerViewAdapter extends RecyclerView.Adapter<MyViewHolder>
{

    private List<Diary> diaries;
    private Context mContext;

    private MyItemClickListener mItemClickListener;

    public MyRecyclerViewAdapter( Context context , List<Diary> diaries)
    {
        this.mContext = context;
        this.diaries = diaries;
    }

    @Override
    public MyViewHolder onCreateViewHolder( ViewGroup viewGroup, int i )
    {
        // ViewHolder
        View v = LayoutInflater.from(viewGroup.getContext()).inflate(R.layout.card_view, viewGroup, false);
        return new MyViewHolder(v, mItemClickListener);
    }

    @Override
    public void onBindViewHolder( MyViewHolder viewHolder, int i )
    {
        // ViewHolder
        Diary diary = diaries.get(i);
        SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
        Log.d("time",dateFormat.format(diary.updatedAt));
        String date = dateFormat.format(diary.updatedAt);
        viewHolder.mMonthTextView.setText(date.substring(5, 7));
        viewHolder.mDayTextView.setText(date.substring(8, 10));
        viewHolder.mTitleTextView.setText(diary.title);
        viewHolder.mContentTextView.setText(diary.content);
        String Icon=date.substring(11, 13);
        if((Integer.parseInt(Icon)<=15)&&(Integer.parseInt(Icon)>=9)){
            viewHolder.ImageIcon.setImageResource(R.drawable.sunny_day);
        }else if((Integer.parseInt(Icon)<=5)||(Integer.parseInt(Icon)>=20)){
        viewHolder.ImageIcon.setImageResource(R.drawable.moon);
        }else{
            viewHolder.ImageIcon.setImageResource(R.drawable.cloudy);
        }
    }

    @Override
    public int getItemCount()
    {
        return diaries == null ? 0 : diaries.size();
    }

    /**
     * Item
     * @param listener
     */
    public void setOnItemClickListener(MyItemClickListener listener){
        this.mItemClickListener = listener;
    }

}

class MyViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener, View.OnLongClickListener {

    public TextView mMonthTextView;
    public TextView mDayTextView;
    public TextView mTitleTextView;
    public ImageView ImageIcon;
    public TextView mContentTextView;
    private MyItemClickListener mListener;

    public MyViewHolder(View rootView,MyItemClickListener listener) {
        super(rootView);
        mTitleTextView =  rootView.findViewById(R.id.title);
        mContentTextView =  rootView.findViewById(R.id.content);
        ImageIcon =  rootView.findViewById(R.id.imageView2);
        mMonthTextView =  rootView.findViewById(R.id.monthTextView);
        mDayTextView =  rootView.findViewById(R.id.dayTextView);
        this.mListener = listener;
        rootView.setOnClickListener(this);
        rootView.setOnLongClickListener(this);
    }
    
    @Override
    public void onClick(View v) {
        if(mListener != null){
            mListener.onItemClick(v, getPosition());
        }
    }

    @Override
    public boolean onLongClick(View v) {
        if (mListener != null) {
            mListener.onItemLongClick(v, getPosition());
        }
        return true;
    }
}
