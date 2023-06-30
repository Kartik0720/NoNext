package com.project.nonext.users;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;

import com.project.nonext.R;
import com.smarteist.autoimageslider.SliderViewAdapter;

public class SliderAdapter extends SliderViewAdapter<SliderAdapter.SliderViewHolder> {
    int [] images;
    String [] text;

    public SliderAdapter(int[] images, String[] text) {
        this.images = images;
        this.text = text;
    }

    @Override
    public SliderAdapter.SliderViewHolder onCreateViewHolder(ViewGroup parent) {
        View view= LayoutInflater.from(parent.getContext()).inflate(R.layout.auto_slider_image,parent,false);
        return new SliderViewHolder(view);
    }

    @Override
    public void onBindViewHolder(SliderAdapter.SliderViewHolder viewHolder, int position) {
            viewHolder.img.setImageResource(images[position]);

    }

    @Override
    public int getCount() {
        return images.length;
    }

    public static class SliderViewHolder extends SliderViewAdapter.ViewHolder{
        ImageView img;
        public SliderViewHolder(View itemView) {
            super(itemView);
            img=itemView.findViewById(R.id.image_slider);
        }

    }
}
