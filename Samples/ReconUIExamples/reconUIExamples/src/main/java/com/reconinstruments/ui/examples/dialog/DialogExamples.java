package com.reconinstruments.ui.examples.dialog;

import android.content.Context;
import android.os.Bundle;
import android.view.KeyEvent;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;
import com.reconinstruments.ui.carousel.CarouselItem;
import com.reconinstruments.ui.carousel.StandardCarouselItem;
import com.reconinstruments.ui.dialog2.ReconDialog;
import com.reconinstruments.ui.dialog2.CarouselDialog;
import com.reconinstruments.ui.dialog2.SpinnerComboDialog;
import com.reconinstruments.ui.examples.R;
import com.reconinstruments.ui.list.SimpleListActivity;
import com.reconinstruments.ui.list.StandardListItem;
import com.reconinstruments.ui.spinner.SpinnerConfig;
import com.reconinstruments.ui.spinner.SpinnerComboView;

import java.util.ArrayList;
import java.util.Arrays;

/**
 * Shows examples of creating a variety of different dialog types
 */
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
                new ListItem("Number Selection",new OnClickCallback() {
                    public void onClick(ListItem item) {
                        createNumberDialog(item);
                    }
                }),
                new ListItem("Time Selection",new OnClickCallback() {
                    public void onClick(ListItem item) {
                        createTimeDialog(item);
                    }
                }),
                new ListItem("Month Selection",new OnClickCallback() {
                    public void onClick(ListItem item) {
                        createDateDialog(item);
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
                new ListItem("Custom View Dialog",new OnClickCallback() {
                    public void onClick(ListItem item) {
                        createCustomViewDialog();
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

    /**
     * Create a new dialog for selecting from a list of items
     * then displaying the selection result in the main list view
     */
    public void createSelectionDialog(final ListItem listItem) {

        CarouselDialog.Builder builder = new CarouselDialog.Builder(this).setTitle("Timeout");
        builder.setItems(Arrays.asList(selections)).setInitialSelection(timeSelection);
        builder.setOnSelectedListener(new CarouselDialog.OnItemSelectedListener() {
            @Override
            public void onItemSelected(CarouselDialog dialog, CarouselItem item, int position) {
                listItem.setSubtitle(((StandardCarouselItem) item).getTitle());
                timeSelection = position;
                dialog.dismiss();
            }
        }).createDialog().show();
    }

    int numValue = 0;
    private void createNumberDialog(final ListItem listItem) {
        SpinnerConfig.NumberSpinnerComboConfig comboConfig = new SpinnerConfig.NumberSpinnerComboConfig(2);

        SpinnerComboDialog.Builder<Integer> builder = new SpinnerComboDialog.Builder<Integer>(this).setTitle("Set number");
        builder.setSpinnerConfig(comboConfig);
        builder.setInitialValue(numValue);
        builder.setOnValueSetListener(new SpinnerComboDialog.OnValueSetListener<Integer>() {
            @Override
            public void onValueSet(Integer value) {
                listItem.setSubtitle(value.toString());
                numValue = value;
            }
        }).createDialog().show();
    }

    int timeValue = 0;
    private void createTimeDialog(final ListItem listItem) {

        SpinnerConfig.SpinnerComboConfig<Integer> comboConfig = new SpinnerConfig.SpinnerComboConfig<Integer>() {
            @Override
            public Integer getValue(ArrayList<SpinnerComboView.SpinnerView> spinners) {
                int seconds = spinners.get(4).getSelected()*10+spinners.get(5).getSelected();
                int minutes = spinners.get(2).getSelected()*10+spinners.get(3).getSelected();
                int hours = spinners.get(0).getSelected()*10+spinners.get(1).getSelected();
                return seconds+minutes*60+hours*60*60;
            }
            @Override
            public void setValue(ArrayList<SpinnerComboView.SpinnerView> spinners, Integer value) {
                int secondsTotal = value;
                int seconds = secondsTotal%60;
                int minutes = secondsTotal/60%60;
                int hours = secondsTotal/(60*60);
                spinners.get(5).setSelected(seconds%10);
                spinners.get(4).setSelected(seconds/10);
                spinners.get(3).setSelected(minutes%10);
                spinners.get(2).setSelected(minutes/10);
                spinners.get(1).setSelected(hours%10);
                spinners.get(0).setSelected(hours/10);
            }
        };
        // set combo format, max value is 99:59:59
        comboConfig.addDigit(9).addDigit(9).addText(":").addDigit(5).addDigit(9).addText(":").addDigit(5).addDigit(9);

        SpinnerComboDialog.Builder<Integer> builder = new SpinnerComboDialog.Builder<Integer>(this).setTitle("Set time");
        builder.setSpinnerConfig(comboConfig);
        builder.setInitialValue(timeValue);
        builder.setOnValueSetListener(new SpinnerComboDialog.OnValueSetListener<Integer>() {
            @Override
            public void onValueSet(Integer value) {
                int seconds = value;
                listItem.setSubtitle(String.format("%02d:%02d:%02d", seconds / (60 * 60), seconds / 60 % 60, seconds % 60));
                timeValue = seconds;
            }
        }).createDialog().show();
    }
    static String[] months = new String[]{
            "January","February","March","April","May","June",
            "July","August","September","October","November","December"};

    /** Basic number input config, input x digit number */
    public static class MonthSpinnerComboConfig extends SpinnerConfig.SpinnerComboConfig<Integer> {
        public MonthSpinnerComboConfig() {
            addComponent(new SpinnerConfig.TextOption(months));
        }
        @Override
        public Integer getValue(ArrayList<SpinnerComboView.SpinnerView> spinners) {
            return spinners.get(0).getSelected();
        }
        @Override
        public void setValue(ArrayList<SpinnerComboView.SpinnerView> spinners, Integer value) {
            spinners.get(0).setSelected(value);
        }
    }

    int monthValue = 0;
    private void createDateDialog(final ListItem listItem) {

        MonthSpinnerComboConfig comboConfig = new MonthSpinnerComboConfig();
        SpinnerComboDialog.Builder<Integer> builder = new SpinnerComboDialog.Builder<Integer>(this).setTitle("Set month");
        builder.setSpinnerConfig(comboConfig);
        builder.setInitialValue(monthValue);
        builder.setOnValueSetListener(new SpinnerComboDialog.OnValueSetListener<Integer>() {
            @Override
            public void onValueSet(Integer value) {
                listItem.setSubtitle(months[value]);
                monthValue = value;
            }
        }).createDialog().show();
    }

    /**
     * Create a dialog that appears for 2 seconds then is automatically dismissed
     */
    private void createPopupDialog() {
        new ReconDialog.Builder(this).setTitle("Warning").setSubtitle("Showing for 2 seconds")
                .setShowStatusBarTheme()
                .setWarningIcon().setDismissTimeout().createDialog().show();
    }

    /**
     * Create a static dialog box that uses a custom layout
     */
    private void createBasicDialog() {
        new ReconDialog.Builder(this).setTitle("DIALOG").setSubtitle("subtitle")
                .setLayout(R.layout.action_bar_dialog).createDialog().show();
    }

    /**
     * Create a progress indicator dialog box
     * Pressing select simulates
     */
    private void createProgressDialog() {
        new ReconDialog.Builder(this).setTitle("Loading").setSubtitle("(press select to finish)")
                .showProgress().setOnKeyListener(new ReconDialog.OnKeyListener() {
            @Override
            public boolean onKey(ReconDialog dialog, int keyCode, KeyEvent event) {
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

    /**
     * Create a dialog with a custom view callback, in order to set more view parameters than can be set
     * by the standard view callback (more than title, subtitle, icon)
     */
    public void createCustomViewDialog() {
        new ReconDialog.Builder(this).setLayout(R.layout.dialog_custom_layout)
                .setViewCallback(new ReconDialog.Builder.ViewCallback() {
                    @Override
                    public void updateView(View view) {
                        ((TextView)findViewById(R.id.text1)).setText("Title");
                        ((TextView)findViewById(R.id.text2)).setText("Subtitle 1");
                        ((TextView)findViewById(R.id.text3)).setText("Subtitle 2");
                    }
                }).createDialog().show();
    }

    int optionSelected = 0;

    /**
     * Create a dialog that uses it's own dialog class, allowing greater customization to the Dialog lifecycle
     * or more reusable code
     */
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