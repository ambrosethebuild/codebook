package com.wwdevelopers.codebook.adapters;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.Color;
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
import com.wwdevelopers.codebook.models.Platform;

import java.util.ArrayList;

public class PlatformsAdapter extends RecyclerView.Adapter<PlatformsAdapter.ViewHolder> {


    private Context mContext;
    private ItemClickListener clickListener;
    private ArrayList<Platform> platforms;
    private LayoutInflater mInflater;

    // stores and recycles views as they are scrolled off screen
    public class ViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener {

        TextView nameTextView;
        TextView descriptionTextView;
        ImageView platformIcon;
        LinearLayout parent_view_holder;

        ViewHolder(View itemView) {

            super(itemView);
            nameTextView = itemView.findViewById(R.id.nameTextView);
            descriptionTextView = itemView.findViewById(R.id.descriptionTextView);
            platformIcon = itemView.findViewById(R.id.platformIcon);
            parent_view_holder = itemView.findViewById(R.id.parent_view_holder);
            itemView.setOnClickListener(this);

        }


        @Override
        public void onClick(View view) {
            if (clickListener != null) clickListener.onClick(view, getAdapterPosition());
        }

    }


    // data is passed into the constructor
    public PlatformsAdapter(Context context, ArrayList<Platform> data) {
        this.mContext = context;
        this.mInflater = LayoutInflater.from(context);
        this.platforms = data;
    }



    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {

        View view = mInflater.inflate(R.layout.platforms_recycler_view_item, parent, false);
        return new ViewHolder(view);

    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {

        Platform platform = platforms.get(position);
        holder.nameTextView.setText(platform.getName());
        holder.descriptionTextView.setText(platform.getDescription());

        if( position % 2 == 0) {
            holder.parent_view_holder.setBackgroundResource(R.drawable.platform_bg);
        }else {
            holder.parent_view_holder.setBackgroundResource(R.drawable.platform_bg_2);
        }

        Picasso.get().load(platform.getImage()).placeholder(R.drawable.splash_screen).error(R.drawable.splash_screen).into(new Target() {
            @Override
            public void onBitmapLoaded(Bitmap bitmap, Picasso.LoadedFrom from) {

                holder.platformIcon.setImageBitmap(bitmap);
                Palette.from(bitmap)
                        .generate(new Palette.PaletteAsyncListener() {
                            @Override
                            public void onGenerated(Palette palette) {

                                Palette.Swatch textSwatch = palette.getDarkVibrantSwatch();
                                GradientDrawable drawable = (GradientDrawable) holder.parent_view_holder.getBackground();

                                if (textSwatch == null) {
                                    drawable.setColor( mContext.getResources().getColor(R.color.colorPrimary) );
                                    return;
                                }

                                drawable.setColor( textSwatch.getRgb() );

                            }
                        });

            }

            @Override
            public void onBitmapFailed(Exception e, Drawable errorDrawable) {
                e.printStackTrace();
            }

            @Override
            public void onPrepareLoad(Drawable placeHolderDrawable) {

            }
        });




    }


    @Override
    public int getItemCount() {
        return platforms.size();
    }

    public void setClickListener(ItemClickListener itemClickListener) {
        this.clickListener = itemClickListener;
    }





}
