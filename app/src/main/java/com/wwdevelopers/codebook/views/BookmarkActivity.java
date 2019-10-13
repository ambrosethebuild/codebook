package com.wwdevelopers.codebook.views;

import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;


import com.shreyaspatil.MaterialDialog.MaterialDialog;
import com.shreyaspatil.MaterialDialog.interfaces.DialogInterface;
import com.wwdevelopers.codebook.R;
import com.wwdevelopers.codebook.adapters.BookmarksAdapter;
import com.wwdevelopers.codebook.interfaces.ItemClickListener;
import com.wwdevelopers.codebook.interfaces.ItemLongClickListener;
import com.wwdevelopers.codebook.models.BookmarkPage;
import com.wwdevelopers.codebook.models.Platform;

import org.litepal.LitePal;
import org.litepal.crud.callback.UpdateOrDeleteCallback;

import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;

public class BookmarkActivity extends Activity {


    Activity mActivity;
    @BindView(R.id.bookmarks_recycler_view) RecyclerView bookmarks_recycler_view;

    List<BookmarkPage> bookmarkPageList;
    BookmarksAdapter bookmarksAdapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_bookmark);

        mActivity = BookmarkActivity.this;
        ButterKnife.bind(mActivity);

        bookmarkPageList = LitePal.findAll(BookmarkPage.class);

        populateBookmarkView();

    }



    private void populateBookmarkView() {

        bookmarksAdapter = new BookmarksAdapter(mActivity, bookmarkPageList);
        bookmarksAdapter.setClickListener(bookmarkClickListener);
        bookmarksAdapter.setLongClickListener(bookmarkLongClickListener);
        bookmarks_recycler_view.setLayoutManager(new LinearLayoutManager(mActivity));
        bookmarks_recycler_view.setAdapter(bookmarksAdapter);

    }


    ItemClickListener bookmarkClickListener = new ItemClickListener() {
        @Override
        public void onClick(View view, int position) {

            BookmarkPage bookmarkPage = bookmarkPageList.get(position);

            Platform platform = new Platform();
            platform.setLink(bookmarkPage.getUrl());

            Intent intent = new Intent(mActivity, WebViewActivity.class);
            intent.putExtra("platform", platform);
            startActivity(intent);

        }



    };


    ItemLongClickListener bookmarkLongClickListener = new ItemLongClickListener() {

        @Override
        public void onLongClick(View view, int position) {

            Log.e("Long Clicked","Yes");
            BookmarkPage bookmarkPage = bookmarkPageList.get(position);
            deletedBookmark( bookmarkPage, position );

        }

    };


    private void deletedBookmark(BookmarkPage selectedBookmarkPage, int position) {

        MaterialDialog mDialog = new MaterialDialog.Builder(mActivity)
                .setAnimation(R.raw.file_deletion)
                .setTitle("Delete?")
                .setMessage("Are you sure want to delete this bookmark?")
                .setCancelable(false)
                .setPositiveButton("Delete", R.drawable.ic_delete, new MaterialDialog.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialogInterface, int which) {
                        // Delete Operation

                        dialogInterface.dismiss();

                        selectedBookmarkPage.deleteAsync().listen(new UpdateOrDeleteCallback() {
                            @Override
                            public void onFinish(int rowsAffected) {

                                bookmarkPageList.remove(position);
                                bookmarksAdapter.notifyDataSetChanged();

                            }
                        });


                    }
                })
                .setNegativeButton("Cancel", R.drawable.ic_close, new MaterialDialog.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialogInterface, int which) {
                        dialogInterface.dismiss();
                    }
                })
                .build();

        // Show Dialog
        mDialog.show();

    }


}
