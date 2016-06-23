package com.reconinstruments.ui.examples.carousel;

import android.content.Context;
import android.graphics.Rect;
import android.os.Bundle;
import android.os.Handler;
import android.util.Log;
import android.view.KeyEvent;
import android.view.View;
import android.widget.TextView;
import com.reconinstruments.ui.carousel.CarouselActivity;
import com.reconinstruments.ui.carousel.CarouselItem;
import com.reconinstruments.ui.carousel.CarouselPagerViewAdapter;
import com.reconinstruments.ui.examples.R;

import java.text.DecimalFormat;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

public class StatCarousel extends CarouselActivity {

    static class ListItem extends CarouselItem {
        String value;
        String unit;
        String type;
        TextView typeText;
        public ListItem(String value,String unit,String type){
            this.value = value;
            this.unit = unit;
            this.type = type;
        }
        @Override
        public int getLayoutId() {
            return R.layout.carousel_item_stat;
        }
        @Override
        public void updateView(View view) {
            TextView valueText = (TextView)view.findViewById(R.id.value);
            TextView unitText = (TextView)view.findViewById(R.id.unit);
            typeText = (TextView)view.findViewById(R.id.type);
            valueText.setText(value);
            unitText.setText(unit);
            typeText.setText(type);
        }

        @Override
        public void updateViewForPosition(View view,POSITION position) {
            if(position==POSITION.CENTER)
                typeText.setVisibility(View.VISIBLE);
            else
                typeText.setVisibility(View.GONE);
        }
    }
    float distance = 34.5f;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.carousel_host_stat);

        TextView title = (TextView)findViewById(R.id.title);
        title.setText("ALL TIME BEST");

        final ListItem distanceItem = new ListItem(new DecimalFormat("#.#").format(distance), "km", "DISTANCE");
        getCarousel().setContents(
                distanceItem,
                new ListItem("51.12", "/km", "AVG. PACE"),
                new ListItem("14", "cal", "CALORIES BURNED"),
                new ListItem("5:50", "", "DURATION"));

        final Handler handler = new Handler();
        handler.postDelayed(new Runnable() {
            @Override
            public void run() {
                distance += 0.1;
                distanceItem.value = new DecimalFormat("#.#").format(distance);
                distanceItem.updateView();
                handler.postDelayed(this,1000);
            }
        }, 1000);
    }
}