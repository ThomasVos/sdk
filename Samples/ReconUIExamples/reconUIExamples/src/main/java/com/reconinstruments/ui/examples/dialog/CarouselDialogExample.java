package com.reconinstruments.ui.examples.dialog;

import android.support.v4.app.FragmentActivity;
import android.view.View;
import android.widget.TextView;
import com.reconinstruments.ui.carousel.CarouselItem;
import com.reconinstruments.ui.carousel.CarouselViewPager;
import com.reconinstruments.ui.carousel.StandardCarouselItem;
import com.reconinstruments.ui.dialog2.CarouselDialog;
import com.reconinstruments.ui.dialog2.ReconDialog;
import com.reconinstruments.ui.examples.R;

import java.util.Arrays;

/**
 * Created by chris on 06/05/15.
 */
public class CarouselDialogExample extends CarouselDialog implements ReconDialog.Builder.ViewCallback {

    static CarouselItem[] selections = {
            new CheckedSelectionItem("Option 1"),
            new CheckedSelectionItem("Option 2"),
            new CheckedSelectionItem("Option 3")
    };
    OnItemSelectedListener onItemSelectedListener;

    public static class CheckedSelectionItem extends StandardCarouselItem {
        public CheckedSelectionItem(String title) {
            super(title);
        }
        @Override
        public int getLayoutId() {
            return R.layout.carousel_item_checkmark_selector;
        }
    }

    public CarouselDialogExample(FragmentActivity context,int initialSelection,OnItemSelectedListener onItemSelectedListener) {
        super(context,R.layout.custom_carousel_host, Arrays.asList(selections), initialSelection);
        this.onItemSelectedListener = onItemSelectedListener;
    }

    @Override
    public void onStart() {
        super.onStart();
        setOnItemSelectedListener(onItemSelectedListener);
        getCarousel().addOnPageSelectListener(new CarouselViewPager.OnPageSelectListener() {
            @Override
            public void onPageSelected(CarouselItem item, int position) {
                updateView();
            }
        });
    }

    @Override
    public void updateView(View view) {
        TextView titleView = (TextView) view.findViewById(R.id.title);

        StandardCarouselItem item = (StandardCarouselItem) getCarousel().getCurrentCarouselItem();
        //StandardCarouselItem item = (StandardCarouselItem) itemFragment.getItem();
        int position = getCarousel().getCurrentItem();//itemFragment.getPosition();
        titleView.setText(item.getTitle());

        TextView subtitleView = (TextView) view.findViewById(R.id.subtitle);
        subtitleView.setText((position+1)+"/"+selections.length);
    }
}
