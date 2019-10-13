package com.wwdevelopers.codebook.adapters;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.drawable.Drawable;
import android.graphics.drawable.GradientDrawable;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.palette.graphics.Palette;
import androidx.recyclerview.widget.RecyclerView;

import com.squareup.picasso.Picasso;
import com.squareup.picasso.Target;
import com.wwdevelopers.codebook.R;
import com.wwdevelopers.codebook.interfaces.ItemClickListener;
import com.wwdevelopers.codebook.interfaces.ItemLongClickListener;
import com.wwdevelopers.codebook.models.BookmarkPage;
import com.wwdevelopers.codebook.models.Platform;

import java.util.ArrayList;
import java.util.List;

public class BookmarksAdapter  extends RecyclerView.Adapter<BookmarksAdapter.ViewHolder> {


    private Context mContext;
    private ItemClickListener clickListener;
    private ItemLongClickListener longClickListener;
    private List<BookmarkPage> bookmarkPageList;
    private LayoutInflater mInflater;

    // stores and recycles views as they are scrolled off screen
    public class ViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener, View.OnLongClickListener {

        TextView nameTextView;
        TextView descriptionTextView;

        ViewHolder(View itemView) {

            super(itemView);
            nameTextView = itemView.findViewById(R.id.nameTextView);
            descriptionTextView = itemView.findViewById(R.id.descriptionTextView);
            itemView.setOnClickListener(this);
            itemView.setOnLongClickListener(this);

        }


        @Override
        public void onClick(View view) {
            if (clickListener != null) clickListener.onClick(view, getAdapterPosition());
        }


        @Override
        public boolean onLongClick(View view) {
            if (longClickListener != null) longClickListener.onLongClick(view, getAdapterPosition());
            return true;
        }


    }


    // data is passed into the constructor
    public BookmarksAdapter(Context context, List<BookmarkPage> data) {
        this.mContext = context;
        this.mInflater = LayoutInflater.from(context);
        this.bookmarkPageList = data;
    }



    @NonNull
    @Override
    public BookmarksAdapter.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {

        View view = mInflater.inflate(R.layout.bookmarks_recycler_view_item, parent, false);

        return new BookmarksAdapter.ViewHolder(view);

    }

    @Override
    public void onBindViewHolder(@NonNull BookmarksAdapter.ViewHolder holder, int position) {

        BookmarkPage bookmarkPage = bookmarkPageList.get(position);
        holder.nameTextView.setText(bookmarkPage.getTitle());
        holder.descriptionTextView.setText(bookmarkPage.getUrl());
        holder.itemView.setLongClickable(true);

    }


    @Override
    public int getItemCount() {
        return bookmarkPageList.size();
    }

    public void setClickListener(ItemClickListener itemClickListener) {
        this.clickListener = itemClickListener;
    }

    public void setLongClickListener(ItemLongClickListener itemLongClickListener) {
        this.longClickListener = itemLongClickListener;
    }




}
