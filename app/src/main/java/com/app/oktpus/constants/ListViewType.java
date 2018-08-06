package com.app.oktpus.constants;

/**
 * Created by Gyandeep on 17/11/17.
 */

public abstract class ListViewType {

    public static final int VIEW_SINGLE_CHOICE = 1,
            VIEW_MULTI_CHOICE = 2,
            VIEW_EDIT_FIELD = 3,
            VIEW_BOTTOM = 4,
            VIEW_POPUP = 5,
            VIEW_BLANK = 6,
            VIEW_DATE_PICKER_EDIT_FIELD = 7,
            VIEW_SINGLE_CHOICE_EXPANDABLE = 8,
            VIEW_SIMPLE_TEXT= 9,
            VIEW_ITEM_COUNTER = 10,
            VIEW_EDIT_FIELD_NO_TITLE = 11,
            VIEW_REVIEW_ITEM = 12,
            VIEW_CHECKBOX_ROW = 13,
            VIEW_SIMPLE_POPUP = 14,
            VIEW_EDITTEXT_WITH_UNIT = 15;

    abstract public int getType();
}
