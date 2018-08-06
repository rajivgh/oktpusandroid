package com.app.oktpus.utils;

import android.content.Context;
import android.graphics.Color;
import android.view.Gravity;
import android.widget.CheckBox;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.app.oktpus.model.AttrValSpinnerModel;
import com.app.oktpus.R;
import com.app.oktpus.responseModel.Config.CommonFields;

/**
 * Created by Gyandeep on 19/4/17.
 */

public class TagTokenUtils{

    private Context context;

    public TagTokenUtils(Context ctx){
        context = ctx;
    };

    public LinearLayout createRangeTag(final CommonFields cf) {
        TextView tv = createTagTextView();
        tv.setTag(cf.getKeyName());

        String val = cf.getMinFormatted() + " - " + cf.getMaxFormatted();
        tv.setText(val);

        ImageView iv = createCloseBtn();

        final LinearLayout tagLayout = createTagLayout();
        cf.setTagId(cf.getViewPos());
        tagLayout.setTag(cf);

        tagLayout.addView(tv);
        tagLayout.addView(iv);

        return tagLayout;
    }

    /*public void updateRangeTag(CommonFields rangeAttr) {
        TextView tv = (TextView) parentTagLayout.findViewWithTag(rangeAttr);
        String val = rangeAttr.getMinFormatted() + " - " + rangeAttr.getMaxFormatted();
        tv.setText(val);
    }*/

    /*public void addOrUpdateRangeTag(Context context, final CommonFields cf, final String keyname) {
        if(cf.isTagCreated())
            updateRangeTag(cf);
        else
            createRangeTag(context, cf, keyname);
    }*/

    /*public void removeRangeTag(CommonFields rangeAttr) {
        rangeAttr.setTagCreated(false);
        parentTagLayout.removeView((LinearLayout)parentTagLayout.findViewById(rangeAttr.getTagId()));

    }*/

    private TextView createTagTextView() {
        TextView tv =  new TextView(context);
        tv.setLayoutParams(new RelativeLayout.LayoutParams(RelativeLayout.LayoutParams.WRAP_CONTENT,
                RelativeLayout.LayoutParams.MATCH_PARENT));
        RelativeLayout.LayoutParams lp = new RelativeLayout.LayoutParams(RelativeLayout.LayoutParams.WRAP_CONTENT, RelativeLayout.LayoutParams.WRAP_CONTENT);
        lp.setMargins(10,10,10,10);
        lp.alignWithParent = true;
        tv.setLayoutParams(lp);
        tv.setTextSize(15f);
        tv.setGravity(Gravity.CENTER_VERTICAL);
        tv.setPadding(10,10,10,10);
        return tv;
    }

    private ImageView createCloseBtn() {
        ImageView iv = new ImageView(context);
        RelativeLayout.LayoutParams lp = new RelativeLayout.LayoutParams(RelativeLayout.LayoutParams.WRAP_CONTENT, RelativeLayout.LayoutParams.MATCH_PARENT);
        lp.setMargins(10,10,10,10);
        lp.alignWithParent = true;
        iv.setBackground(context.getResources().getDrawable(R.drawable.ic_close_tag));
        iv.setLayoutParams(lp);
        iv.setPadding(10,10,10,10);
        return iv;
    }

    private LinearLayout createTagLayout() {

        LinearLayout tagLayout = new LinearLayout(context);
        FlowLayout.LayoutParams layoutParams = new FlowLayout.LayoutParams(
                FlowLayout.LayoutParams.WRAP_CONTENT, FlowLayout.LayoutParams.WRAP_CONTENT);
        layoutParams.rightMargin = 5;
        layoutParams.bottomMargin = 5;

        tagLayout.setLayoutParams(layoutParams);
        tagLayout.setOrientation(LinearLayout.HORIZONTAL);
        tagLayout.setBackground(context.getResources().getDrawable(R.drawable.tag_bg));

        return tagLayout;
    }

    public LinearLayout createMultiSelectTag(final AttrValSpinnerModel attr, final String keyname) {

        TextView tv = createTagTextView();

        String val = attr.getValue();
        tv.setText((val.contains(","))? val.substring(0, val.indexOf(",")):val);
        attr.setAttrbuteKey(keyname);

        ImageView iv = createCloseBtn();

        final LinearLayout tagLayout1 = createTagLayout();
        //tagLayout1.setId(attr.getId());
        tagLayout1.setTag(keyname+attr.getInteger_representation());
        tagLayout1.addView(tv);
        tagLayout1.addView(iv);

        return tagLayout1;
    }


    /* clear tag */
    public LinearLayout createClearTagLayout() {
        LinearLayout tagLayout = new LinearLayout(context);
        FlowLayout.LayoutParams layoutParams = new FlowLayout.LayoutParams(
                FlowLayout.LayoutParams.WRAP_CONTENT, FlowLayout.LayoutParams.WRAP_CONTENT);
        layoutParams.rightMargin = 5;
        layoutParams.bottomMargin = 5;
        layoutParams.gravity = Gravity.END;
        tagLayout.setLayoutParams(layoutParams);
        tagLayout.setOrientation(LinearLayout.HORIZONTAL);
        tagLayout.setBackground(context.getResources().getDrawable(R.drawable.tag_clear_all_bg_white));

        return tagLayout;
    }

    public LinearLayout createClearTag() {
        TextView tv = createTagTextView();
        tv.setText(" Clear all ");
        //tv.setTypeface(Typeface.DEFAULT_BOLD);
        //ImageView iv = createCloseBtn();

        final LinearLayout tagLayout1 = createClearTagLayout();
        //tagLayout1.setId(attr.getId());
        tagLayout1.setTag("cleartag");
        tagLayout1.addView(tv);
        //tagLayout1.addView(iv);

        return tagLayout1;
    }


    public LinearLayout createOption(String optionText, Context context) {
        TextView tv = createTagTextView();
        tv.setText(optionText);
        tv.setTextSize(15);
        //tv.setTextColor(context.getResources().getColor(R.color.colorAccent));
        tv.setId(R.id.dynamic_text_view);

        final LinearLayout layout = createOptionLayout(context);
        layout.addView(tv);
        return layout;
    }

    private LinearLayout createOptionLayout(Context context) {

        LinearLayout tagLayout = new LinearLayout(context);
        FlowLayout.LayoutParams layoutParams = new FlowLayout.LayoutParams(
                FlowLayout.LayoutParams.WRAP_CONTENT, FlowLayout.LayoutParams.WRAP_CONTENT);
        layoutParams.rightMargin = 15;
        layoutParams.bottomMargin = 15;

        tagLayout.setLayoutParams(layoutParams);
        tagLayout.setOrientation(LinearLayout.HORIZONTAL);
        tagLayout.setBackground(context.getResources().getDrawable(R.drawable.option_button_unselected));

        return tagLayout;
    }

    //Multi Choice rows
    public LinearLayout createMultiChoiceItem(String optionText, Context context) {
        final LinearLayout layout = createMultiChoiceLayout(context);
        CheckBox cb = createCheckBox(context);
        layout.addView(cb);

        TextView tv = createTagTextView();
        tv.setText(optionText);
        tv.setTextSize(15);
        tv.setId(R.id.dynamic_text_view);
        layout.addView(tv);

        return layout;
    }

    private LinearLayout createMultiChoiceLayout(Context context) {
        LinearLayout rowLayout = new LinearLayout(context);
        LinearLayout.LayoutParams layoutParams = new LinearLayout.LayoutParams(
                LinearLayout.LayoutParams.MATCH_PARENT, LinearLayout.LayoutParams.WRAP_CONTENT);
        layoutParams.rightMargin = 15;
        layoutParams.bottomMargin = 15;

        rowLayout.setLayoutParams(layoutParams);
        rowLayout.setOrientation(LinearLayout.HORIZONTAL);
        rowLayout.setId(R.id.multi_choice_row);

        return rowLayout;
    }

    private CheckBox createCheckBox(Context context) {
        CheckBox cb = new CheckBox(context);
        cb.setLayoutParams(new RelativeLayout.LayoutParams(RelativeLayout.LayoutParams.WRAP_CONTENT,
                RelativeLayout.LayoutParams.WRAP_CONTENT));
        RelativeLayout.LayoutParams lp = new RelativeLayout.LayoutParams(RelativeLayout.LayoutParams.WRAP_CONTENT, RelativeLayout.LayoutParams.WRAP_CONTENT);
        lp.setMargins(10,10,10,10);
        cb.setLayoutParams(lp);

        cb.setId(R.id.checkbox_item);
        cb.setDuplicateParentStateEnabled(true);
        cb.setClickable(false);
        cb.setFocusable(false);

        return cb;

    }
}