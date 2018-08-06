/*
 * Copyright 2013 Google Inc.
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *     http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */

package com.app.oktpus.fragment;

import android.app.Activity;
import android.os.Bundle;
import android.support.v4.app.ListFragment;
import android.support.v7.widget.CardView;
import android.text.TextUtils;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.Button;
import android.widget.ListView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.android.volley.DefaultRetryPolicy;
import com.android.volley.Request;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.app.oktpus.R;
import com.app.oktpus.constants.Flags;
import com.app.oktpus.listener.OnCallListener;
import com.app.oktpus.model.Default;
import com.app.oktpus.model.carinsurance.CarInsuranceRequestModel;
import com.app.oktpus.model.carinsurance.QuestionItems;
import com.app.oktpus.utils.Client;
import com.google.gson.Gson;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import wizardlib.model.AbstractWizardModel;
import wizardlib.model.ModelCallbacks;
import wizardlib.model.Page;
import wizardlib.model.ReviewItem;

public class ReviewFragmentOld extends ListFragment implements ModelCallbacks {
    private Callbacks mCallbacks;
    private AbstractWizardModel mWizardModel;
    private List<ReviewItem> mCurrentReviewItems;
    private RelativeLayout mBottomStickyButton;
    private CardView mBottomStickyButtonLayout;
    private ReviewAdapter mReviewAdapter;

    public ReviewFragmentOld() {}

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        mReviewAdapter = new ReviewAdapter();
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
            Bundle savedInstanceState) {
        View rootView = inflater.inflate(R.layout.wizard_fragment_page, container, false);

        TextView titleView = (TextView) rootView.findViewById(android.R.id.title);
        titleView.setText("Review");
        titleView.setTextColor(getResources().getColor(R.color.headingGreen));

        ListView listView = (ListView) rootView.findViewById(android.R.id.list);
        setListAdapter(mReviewAdapter);
        listView.setChoiceMode(ListView.CHOICE_MODE_SINGLE);

        /*mBottomStickyButtonLayout = ((BaseActivity)getActivity()).mBottomLayout;
        mBottomStickyButtonLayout.setVisibility(View.VISIBLE);
        mBottomStickyButtonLayout.setRadius(0f);
        mBottomStickyButtonLayout.setCardElevation(0f);
        ((BaseActivity)getActivity()).mBottomLayoutContent.setVisibility(View.GONE);
        mBottomStickyButton = ((BaseActivity)getActivity()).mBottomStickyButton;
        mBottomStickyButton.setVisibility(View.VISIBLE);
*/
        Button button = (Button) rootView.findViewById(R.id.btn_ci_confirm_request);
        button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                requestCarInsuranceApi(getRequestData());
            }
        });

        return rootView;
    }

    @Override
    public void onPause() {
        super.onPause();

    }

    @Override
    public void onAttach(Activity activity) {
        super.onAttach(activity);

        if (!(activity instanceof Callbacks)) {
            throw new ClassCastException("Activity must implement fragment's callbacks");
        }

        mCallbacks = (Callbacks) activity;

        mWizardModel = mCallbacks.onGetModel();
        mWizardModel.registerListener(this);
        onPageTreeChanged();
    }

    private String getRequestData() {
        List<QuestionItems> finalList = new ArrayList<>();
        List<CarInsuranceRequestModel> requestModel = new ArrayList<>();
        for(ReviewItem item : mCurrentReviewItems) {
            for(QuestionItems qi : item.getDisplayItems()) {
                if(!qi.getValue().isEmpty() || qi.getHasExtension() || qi.getMultiChoiceValues().size() > 0) {
                    finalList.add(qi);
                }
            }
        }

        for(QuestionItems qi : finalList) {
            CarInsuranceRequestModel model = new CarInsuranceRequestModel(qi.getParamKey(), qi.getValue());
            if(qi.getMultiChoiceValues() != null && qi.getMultiChoiceValues().size()>0) {
                List<String> multiChoicevalues = new ArrayList<>();
                for(Map.Entry<String, Boolean> entry: qi.getMultiChoiceValues().entrySet()) {
                    multiChoicevalues.add(entry.getKey());
                }
                model.setValue(multiChoicevalues);
            }

            if(qi.getHasExtension()) {
                List<CarInsuranceRequestModel> itemList = new ArrayList<>();
                for(QuestionItems item : qi.getSubQuestionsList()) {
                    if(!item.getValue().isEmpty())
                        itemList.add(new CarInsuranceRequestModel(item.getQuestion(), item.getValue()));
                }
                model.setValue(itemList);
            }
            requestModel.add(model);
            ///Log.d("Review ", "qsn: "+ qi.getQuestion() + "\nval:"+qi.getValue()+"\n");
        }

        Gson gson = new Gson();
        String jsonInString = gson.toJson(requestModel);
        Log.d("toJSONReview:", jsonInString);
        return jsonInString;
    }

    private void requestCarInsuranceApi(final String params) {
        try {
            Map<String, Object> req = new HashMap<>();
            req.put("status", 1);
            req.put("result", params);

            //Log.d(Flags.ActivityTag.SEARCH_FORM_ACTIVITY,"attrib url: " );
            Client jsObjRequest = new Client(Request.Method.POST, "https://oktpus.com/api/insurance_request?domain_id=1", Default.class, req, new OnCallListener() {
                @Override
                public void nwResponseData(String resData) throws IOException {
                }
            }, new Response.Listener<Object>() {
                @Override
                public void onResponse(Object response) {
                    Log.d("Response: ", response.toString());
                }
            }, new Response.ErrorListener() {
                @Override
                public void onErrorResponse(VolleyError error) {
                    Log.e(Flags.ActivityTag.SEARCH_FORM_ACTIVITY, "Error: " + error);
                }
            })/*{
                @Override
                public byte[] getBody() throws AuthFailureError {
                    return param;
                }
            }*/;
            String tag_json_obj = "search_form_attribute";
            jsObjRequest.setRetryPolicy(new DefaultRetryPolicy(50000, 5, DefaultRetryPolicy.DEFAULT_BACKOFF_MULT));
            //AppController.getInstance().addToCacheRequestQueue(jsObjRequest, tag_json_obj);
        }
        catch(Exception e) {
            Log.d(Flags.ActivityTag.SEARCH_FORM_ACTIVITY, e.getMessage());
        }
    }

    @Override
    public void onPageTreeChanged() {
        onPageDataChanged(null);
    }

    @Override
    public void onDetach() {
        super.onDetach();
        mCallbacks = null;

        mWizardModel.unregisterListener(this);
    }

    @Override
    public void onPageDataChanged(Page changedPage) {
        ArrayList<ReviewItem> reviewItems = new ArrayList<ReviewItem>();
        for (Page page : mWizardModel.getCurrentPageSequence()) {
            page.getReviewItems(reviewItems);
        }
        Collections.sort(reviewItems, new Comparator<ReviewItem>() {
            @Override
            public int compare(ReviewItem a, ReviewItem b) {
                return a.getWeight() > b.getWeight() ? +1 : a.getWeight() < b.getWeight() ? -1 : 0;
            }
        });
        mCurrentReviewItems = reviewItems;

        if (mReviewAdapter != null) {
            mReviewAdapter.notifyDataSetInvalidated();
        }
    }

    @Override
    public void onListItemClick(ListView l, View v, int position, long id) {
        mCallbacks.onEditScreenAfterReview(mCurrentReviewItems.get(position).getPageKey());
    }

    public interface Callbacks {
        AbstractWizardModel onGetModel();
        void onEditScreenAfterReview(String pageKey);
    }

    private class ReviewAdapter extends BaseAdapter {
        @Override
        public boolean hasStableIds() {
            return true;
        }

        @Override
        public int getItemViewType(int position) {
            return 0;
        }

        @Override
        public int getViewTypeCount() {
            return 1;
        }

        @Override
        public boolean areAllItemsEnabled() {
            return true;
        }

        @Override
        public Object getItem(int position) {
            return mCurrentReviewItems.get(position);
        }

        @Override
        public long getItemId(int position) {
            return mCurrentReviewItems.get(position).hashCode();
        }

        @Override
        public View getView(int position, View view, ViewGroup container) {
            LayoutInflater inflater = LayoutInflater.from(getActivity());
            View rootView = inflater.inflate(R.layout.wizard_list_item_review, container, false);

            ReviewItem reviewItem = mCurrentReviewItems.get(position);

            String value = reviewItem.getDisplayValue();
            if (TextUtils.isEmpty(value)) {
                value = "(None)";
            }
            ((TextView) rootView.findViewById(android.R.id.text1)).setText(reviewItem.getTitle());
            ((TextView) rootView.findViewById(android.R.id.text2)).setText(value);
            return rootView;
        }

        @Override
        public int getCount() {
            return mCurrentReviewItems.size();
        }
    }
}
