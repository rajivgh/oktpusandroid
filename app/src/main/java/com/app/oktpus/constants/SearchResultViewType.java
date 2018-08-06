package com.app.oktpus.constants;

/**
 * Created by Gyandeep on 27/11/17.
 */

public abstract class SearchResultViewType {

    public static final int
            VIEW_HEADER = 0,
            VIEW_ITEM = 1,
            VIEW_AD_CARD = 8,
            VIEW_RATE_THIS_APP = 9;
/*
    public static final int VIEW_TYPE_HEADER = 0;
    public static final int VIEW_TYPE_ITEM = 1;
    //private final int VIEW_TYPE_FOOTER = 2;
    public static final int VIEW_TYPE_SORT_LAYOUT = 2;
    public static final int VIEW_TYPE_PROGRESSBAR = 3;
    private final int VIEW_TYPE_NO_RESULT = 4;
    private final int VIEW_BLANK_VIEW = 5;
    public static final int VIEW_NO_MORE_RESULTS = 6;
    public static final int VIEW_NETWORK_FAILURE = 7;*/

    abstract public int getType();

}
