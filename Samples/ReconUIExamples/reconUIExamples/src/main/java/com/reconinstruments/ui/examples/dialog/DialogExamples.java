package com.reconinstruments.ui.examples.dialog;

import android.content.Context;
import android.os.Bundle;
import android.view.KeyEvent;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;
import com.reconinstruments.ui.carousel.CarouselItem;
import com.reconinstruments.ui.carousel.StandardCarouselItem;
import com.reconinstruments.ui.dialog.BaseDialog;
import com.reconinstruments.ui.dialog.CarouselDialog;
import com.reconinstruments.ui.dialog.DialogBuilder;
import com.reconinstruments.ui.examples.R;
import com.reconinstruments.ui.list.SimpleListActivity;
import com.reconinstruments.ui.list.SimpleListItem;
import com.reconinstruments.ui.list.StandardListItem;

import java.util.Arrays;
import java.util.List;


public class DialogExamples extends SimpleListActivity {

    public class ListItem extends StandardListItem {
        String subtitle;
        OnClickCallback callback;
        public ListItem(String text, OnClickCallback callback){
            super(text);
            this.callback = callback;
        }
        public void onClick(Context context) {
            callback.onClick(this);
        }
        public void setSubtitle(String subtitle) {
            this.subtitle = subtitle;
            TextView subtitleView = (TextView)getView().findViewById(R.id.subtitle);
            subtitleView.setVisibility(View.VISIBLE);
            subtitleView.setText(subtitle);
        }
        public String getSubtitle() {
            return subtitle;
        }
    }
    public interface OnClickCallback {
        void onClick(ListItem item);
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.list_standard_layout);
        setContents(
                new ListItem("Selection Dialog",new OnClickCallback() {
                    public void onClick(ListItem item) {
                        createSelectionDialog(item);
                    }
                }),
                new ListItem("Pop up Dialog",new OnClickCallback() {
                    public void onClick(ListItem item) {
                        createPopupDialog();
                    }
                }),
                new ListItem("Basic Dialog",new OnClickCallback() {
                    public void onClick(ListItem item) {
                        createBasicDialog();
                    }
                }),
                new ListItem("Progress Dialog",new OnClickCallback() {
                    public void onClick(ListItem item) {
                        createProgressDialog();
                    }
                }),
                new ListItem("Custom Selection Dialog",new OnClickCallback() {
                    public void onClick(ListItem item) {
                        createCustomSelectionDialog(item);
                    }
                })
        );
    }

    CarouselItem[] selections = {
            new CheckedSelectionItem("5 mins",0),
            new CheckedSelectionItem("10 mins",1),
            new CheckedSelectionItem("15 mins",2)
    };
    public class CheckedSelectionItem extends StandardCarouselItem {
        int value;
        public CheckedSelectionItem(String title,int value) {
            super(title);
            this.value = value;
        }

        @Override
        public void updateView(View view) {
            super.updateView(view);
            view.findViewById(R.id.checkmark).setVisibility(value==timeSelection?View.VISIBLE:View.INVISIBLE);
        }

        @Override
        public int getLayoutId() {
            return R.layout.carousel_item_checkmark;
        }

    }

    public int timeSelection = 0;

    public void createSelectionDialog(final ListItem listItem) {

        DialogBuilder builder = new DialogBuilder(this).setTitle("Timeout");
        builder.createSelectionDialog(Arrays.asList(selections), timeSelection, new CarouselDialog.OnItemSelectedListener() {
            @Override
            public void onItemSelected(CarouselDialog dialog, CarouselItem item, int position) {
                listItem.setSubtitle(((StandardCarouselItem) item).getTitle());
                timeSelection = position;
                dialog.dismiss();
            }
        }).show();
    }

    private void createPopupDialog() {
        new DialogBuilder(this).setTitle("Warning").setSubtitle("Showing for 2 seconds").setWarningIcon().setDismissTimeout().createDialog().show();
    }

    private void createBasicDialog() {
        new DialogBuilder(this).setTitle("DIALOG").setSubtitle("subtitle").setLayout(R.layout.action_bar_dialog).createDialog().show();
    }

    private void createProgressDialog() {
        new DialogBuilder(this).setTitle("Loading").setSubtitle("(press select to finish)").showProgress().setOnKeyListener(new BaseDialog.OnKeyListener() {
            @Override
            public boolean onKey(BaseDialog dialog, int keyCode, KeyEvent event) {
                if (event.getAction()==KeyEvent.ACTION_UP&&keyCode==KeyEvent.KEYCODE_DPAD_CENTER) {
                    ImageView icon = (ImageView)dialog.getView().findViewById(R.id.icon);
                    icon.setImageResource(R.drawable.icon_checkmark);
                    icon.setVisibility(View.VISIBLE);
                    dialog.getView().findViewById(R.id.progress_bar).setVisibility(View.GONE);
                    dialog.setDismissTimeout(2);
                    return true;
                }
                return false;
            }
        }).createDialog().show();
    }


    int optionSelected = 0;
    public void createCustomSelectionDialog(final ListItem listItem) {
        new CarouselDialogExample(this,optionSelected,new CarouselDialog.OnItemSelectedListener() {
            @Override
            public void onItemSelected(CarouselDialog dialog, CarouselItem item, int position) {
                optionSelected = position;
                listItem.setSubtitle("#"+(position+1));
                dialog.dismiss();
            }
        }).show();
    }
}