package com.app.oktpus.model.carinsurance;

import com.app.oktpus.constants.ListViewType;
import com.app.oktpus.model.SimpleListItemModel;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

/**
 * Created by Gyandeep on 4/11/17.
 */

public class QuestionItems extends ListViewType {
    private ArrayList<QuestionItems> mSubQuestions;
    private String mQuestion;
    private String mHint;
    private String paramKey;
    private List<String> mOptions;
    private String mSetValue = "";
    private String mSetExtraValue;

    private List<String> mSetValueList;
    private boolean hasExtension = false, isExpanded = false, isExtension = false;
    private int viewType = ListViewType.VIEW_BLANK, reviewViewType = ListViewType.VIEW_REVIEW_ITEM;

    private Map<String, Boolean> multiChoiceValues;

    private List<SimpleListItemModel> mPopupOptions;    //For Insurance company list
    private int modelStatus;

    public QuestionItems() {}
    public QuestionItems(String paramKey, String question, String value) {
        this.paramKey = paramKey;
        this.mQuestion = question;
        this.mSetValue = value;
    }

    public void setHasExtension(boolean val) { hasExtension = val; }
    public boolean getHasExtension() { return hasExtension; }
    public void setExpanded(boolean val) { isExpanded = val; }
    public boolean isExpanded() { return isExpanded; }


    public boolean isExtension() {
        return isExtension;
    }
    public void setExtension(boolean extension) {
        isExtension = extension;
    }

    /*public void setExtensionItemValue(String value) { mSetExtensionItemValue = value; }
        public String getExtensionItemValue() {}*/
    public int getModelStatus() {
        return modelStatus;
    }

    public void setModelStatus(int modelStatus) {
        this.modelStatus = modelStatus;
    }

    public void setSubQuestions(ArrayList<QuestionItems> items) { mSubQuestions = items; }
    public ArrayList<QuestionItems> getSubQuestionsList() { return mSubQuestions; }

    public String getmHint() {
        return mHint;
    }
    public void setmHint(String mHint) {
        this.mHint = mHint;
    }
    public void setValue(String val) { mSetValue = val;}
    public String getValue() { return mSetValue; }

    public void setExtraValue(String val) { this.mSetExtraValue = val; }
    public String getExtraValue() { return mSetExtraValue; }

    public List<String> getMCValueList() {
        return mSetValueList;
    }
    public void setMCValueList(List<String> mSetValueList) {
        this.mSetValueList = mSetValueList;
    }

    public void setQuestion(String question) {
        mQuestion = question;
    }

    public void setOptions(List<String> options) {
        mOptions = options;
    }

    public String getQuestion() {
        return mQuestion;
    }
    public String getParamKey() { return paramKey; }
    public void setParamKey(String paramKey) { this.paramKey = paramKey; }

    public List<String> getOptions() {
        return mOptions;
    }

    public Map<String, Boolean> getMultiChoiceValues() {
        return multiChoiceValues;
    }
    public void setMultiChoiceValues(Map<String, Boolean> values) {
        multiChoiceValues = values;
    }

    public void setViewType(int type) {
        viewType = type;
    }
    @Override
    public int getType() {
        return viewType;
    }

    //For Car insurance company list
    public List<SimpleListItemModel> getmPopupOptions() {
        return mPopupOptions;
    }

    public void setmPopupOptions(List<SimpleListItemModel> mPopupOptions) {
        this.mPopupOptions = mPopupOptions;
    }

    public int getReviewViewType() {
        return reviewViewType;
    }

    public void setReviewViewType(int reviewViewType) {
        this.reviewViewType = reviewViewType;
    }
}

