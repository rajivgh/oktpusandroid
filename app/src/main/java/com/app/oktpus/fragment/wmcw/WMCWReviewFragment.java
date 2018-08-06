package com.app.oktpus.fragment.wmcw;

import android.app.Activity;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.text.TextUtils;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;

import com.android.volley.AuthFailureError;
import com.android.volley.DefaultRetryPolicy;
import com.android.volley.Request;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.app.oktpus.R;
import com.app.oktpus.activity.WhatsMyCarWorth;
import com.app.oktpus.constants.Flags;
import com.app.oktpus.constants.ListViewType;
import com.app.oktpus.controller.AppController;
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

import static com.app.oktpus.constants.ListViewType.VIEW_BOTTOM;
import static com.app.oktpus.constants.ListViewType.VIEW_REVIEW_ITEM;

/**
 * Created by Gyandeep on 13/3/18.
 */

public class WMCWReviewFragment extends Fragment implements ModelCallbacks {

    private WMCWReviewFragment.Callbacks mCallbacks;
    private AbstractWizardModel mWizardModel;
    private List<ReviewItem> mCurrentReviewItems;
    private RecyclerView recyclerView;
    private WMCWReviewFragment.ReviewAdapter mReviewAdapter;
    private List<QuestionItems> mReviewListData;
    public WMCWReviewFragment() {}

    @Override
    public void onAttach(Activity activity) {
        super.onAttach(activity);

        if (!(activity instanceof WMCWReviewFragment.Callbacks)) {
            throw new ClassCastException("Activity must implement fragment's callbacks");
        }

        mCallbacks = (WMCWReviewFragment.Callbacks) activity;

        mWizardModel = mCallbacks.onGetModel();
        mWizardModel.registerListener(this);
        onPageTreeChanged();
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

        if(mReviewListData == null){
            mReviewListData = new ArrayList<>();
        }
        else {
            mReviewListData.clear();
        }

        /*if(getActivity() instanceof WhatsMyCarWorth) {
            if(((CarInsurance)getActivity()).zipAndCountry != null) {
                for(QuestionItems item: ((CarInsurance)getActivity()).zipAndCountry) {
                    if(item.getValue().length() > 0) {
                        mReviewListData.add(item);
                    }
                }
            }
        }*/

        for(ReviewItem reviewItem : mCurrentReviewItems) {
            for(QuestionItems qItem : reviewItem.getDisplayItems()) {
                if(!qItem.getValue().isEmpty() || qItem.getHasExtension() || qItem.getMultiChoiceValues().size() > 0) {

                    if(qItem.getMultiChoiceValues() != null && qItem.getMultiChoiceValues().size()>0) {
                        List<String> list = new ArrayList<>();
                        for(Map.Entry<String, Boolean> entry: qItem.getMultiChoiceValues().entrySet()) {
                            list.add(entry.getKey());
                        }
                        mReviewListData.add(new QuestionItems("", "", TextUtils.join(", ",list)));
                        continue;
                    }

                    if(qItem.getHasExtension()) {
                        for(QuestionItems item : qItem.getSubQuestionsList()) {
                            if(!item.getValue().isEmpty())
                                mReviewListData.add(item);
                        }
                        continue;
                    }

                    mReviewListData.add(qItem);
                }
            }
        }

        if (mReviewAdapter != null) {
            mReviewAdapter.notifyDataSetChanged();
        }
    }

    public interface Callbacks {
        AbstractWizardModel onGetModel();
        void onEditScreenAfterReview(String pageKey);
    }

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (mReviewAdapter == null) mReviewAdapter = new WMCWReviewFragment.ReviewAdapter();
        if (mReviewListData == null) mReviewListData = new ArrayList<>();
    }

    @Override
    public void onResume() {
        super.onResume();
        if (mReviewAdapter != null)
            mReviewAdapter.notifyDataSetChanged();
    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View rootView = inflater.inflate(R.layout.fragment_review, container, false);

        TextView titleView = (TextView) rootView.findViewById(android.R.id.title);
        titleView.setText("Review");

        recyclerView = (RecyclerView) rootView.findViewById(R.id.rv_review);
        recyclerView.setLayoutManager(new LinearLayoutManager(getActivity()));
        recyclerView.setAdapter(mReviewAdapter);
        mReviewAdapter.notifyDataSetChanged();
        /*Button button = (Button) rootView.findViewById(R.id.btn_ci_confirm_request);
        button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                ((CarInsurance)getActivity()).displaySuccess();
                //requestCarInsuranceApi(getRequestData());
            }
        });*/

        return rootView;
    }

    private String getRequestData() {
        List<QuestionItems> finalList = new ArrayList<>();
        List<CarInsuranceRequestModel> requestModel = new ArrayList<>();

        for(ReviewItem item : mCurrentReviewItems) {
            for(QuestionItems qi : item.getDisplayItems()) {
                if(!qi.getValue().isEmpty() || qi.getHasExtension() || qi.getMultiChoiceValues().size() > 0) {

                    if (qi.getExtraValue() != null) {
                        qi.setValue(qi.getValue() + " " + qi.getExtraValue());
                    }
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

    private void requestWMCWApi(final String params) {
        try {
            Map<String, Object> req = new HashMap<>();
            req.put("status", "1");
            req.put("result", params);
            Log.d("wmcw_req",""+req);
            Client jsObjRequest = new Client(Request.Method.POST, Flags.URL.POST_INSURANCE, Default.class, req, new OnCallListener() {
                @Override
                public void nwResponseData(String resData) throws IOException {
                }
            }, new Response.Listener<Object>() {
                @Override
                public void onResponse(Object response) {
                    Log.d("Response: ", response.toString());
                    ((WhatsMyCarWorth)getActivity()).displaySuccess();
                }
            }, new Response.ErrorListener() {
                @Override
                public void onErrorResponse(VolleyError error) {
                    Log.e(Flags.ActivityTag.WMCW, "Error: " + error);
                }
            });
            String tag_json_obj = "wmcw_request";
            jsObjRequest.setRetryPolicy(new DefaultRetryPolicy(50000, 5, DefaultRetryPolicy.DEFAULT_BACKOFF_MULT));
            AppController.getInstance().addToCacheRequestQueue(jsObjRequest, tag_json_obj);
        }
        catch(Exception e) {
            Log.e(Flags.ActivityTag.WMCW, e.getMessage());
        }
    }

    private class ReviewAdapter extends RecyclerView.Adapter<RecyclerView.ViewHolder> {
        @Override
        public RecyclerView.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
            RecyclerView.ViewHolder vh;
            if(viewType == VIEW_REVIEW_ITEM) {
                View v = LayoutInflater.from(parent.getContext())
                        .inflate(R.layout.layout_review_item, parent, false);
                vh = new WMCWReviewFragment.ReviewAdapter.ItemViewHolder(v);
                return vh;
            }
            else if(viewType == VIEW_BOTTOM) {
                View v = LayoutInflater.from(parent.getContext())
                        .inflate(R.layout.layout_navigate_buttons, parent, false);
                vh = new WMCWReviewFragment.ReviewAdapter.FooterView(v);
                return vh;
            }
            else {
                View v = LayoutInflater.from(parent.getContext())
                        .inflate(R.layout.blank_view, parent, false);
                vh = new WMCWReviewFragment.ReviewAdapter.BlankViewHolder(v);
                return vh;
            }
        }

        public class FooterView extends RecyclerView.ViewHolder {
            Button btnNext, btnPrevious;
            public FooterView(View v) {
                super(v);
                btnNext = (Button) v.findViewById(R.id.next_button);
                btnPrevious = (Button) v.findViewById(R.id.prev_button);
                btnNext.setText("Submit");
                btnNext.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        requestWMCWApi(getRequestData());
                        /*DialogFragment dg = new DialogFragment() {
                            @Override
                            public Dialog onCreateDialog(Bundle savedInstanceState) {

                                return new AlertDialog.Builder(getActivity())
                                        .setMessage("Confirm submission?")
                                        .setPositiveButton("Yes", new DialogInterface.OnClickListener() {
                                            @Override
                                            public void onClick(DialogInterface dialog, int which) {
                                                //requestCarInsuranceApi(getRequestData());
                                                //Toast.makeText(getContext(), "Done", Toast.LENGTH_SHORT).show();
                                            }
                                        })
                                        .setNegativeButton(android.R.string.cancel, new DialogInterface.OnClickListener() {
                                            @Override
                                            public void onClick(DialogInterface dialog, int which) {
                                                dialog.dismiss();
                                            }
                                        }).create();
                                *//*getDialog().setOnShowListener(new DialogInterface.OnShowListener() {
                                    @Override
                                    public void onShow(DialogInterface dialog) {

                                    }
                                });*//*
                                //return builder.create();
                            }
                        };
                        dg.show(getActivity().getSupportFragmentManager(), "place_order_dialog");*/
                    }
                });

                btnPrevious.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        ((WhatsMyCarWorth)getActivity()).onPreviousButtonClick();
                    }
                });
            }
        }

        private class BlankViewHolder extends RecyclerView.ViewHolder {
            public BlankViewHolder(View itemView) {
                super(itemView);
            }
        }

        private class ItemViewHolder extends RecyclerView.ViewHolder{
            TextView tvTitle, tvValue;
            public ItemViewHolder(View itemView) {
                super(itemView);
                tvTitle = (TextView) itemView.findViewById(R.id.tv_qsn_item);
                tvValue = (TextView) itemView.findViewById(R.id.tv_value);
            }
        }

        @Override
        public void onBindViewHolder(RecyclerView.ViewHolder holder, int position) {
            if(holder instanceof WMCWReviewFragment.ReviewAdapter.ItemViewHolder) {
                ((WMCWReviewFragment.ReviewAdapter.ItemViewHolder) holder).tvTitle.setText(mReviewListData.get(position).getQuestion());
                ((WMCWReviewFragment.ReviewAdapter.ItemViewHolder) holder).tvValue.setText(mReviewListData.get(position).getValue() + " " + ((mReviewListData.get(position).getExtraValue() != null)?mReviewListData.get(position).getExtraValue():""));
            }

            if(holder instanceof WMCWReviewFragment.ReviewAdapter.FooterView) {
                if(mReviewListData.size() == 0) {
                    ((WMCWReviewFragment.ReviewAdapter.FooterView) holder).btnNext.setVisibility(View.INVISIBLE);
                    ((WMCWReviewFragment.ReviewAdapter.FooterView) holder).btnPrevious.setVisibility(View.INVISIBLE);
                }
                else {
                    ((WMCWReviewFragment.ReviewAdapter.FooterView) holder).btnNext.setVisibility(View.VISIBLE);
                    ((WMCWReviewFragment.ReviewAdapter.FooterView) holder).btnPrevious.setVisibility(View.VISIBLE);
                }
            }
        }

        @Override
        public int getItemCount() {
            return mReviewListData.size() + 1;
        }

        @Override
        public int getItemViewType(int position) {
            if(position == mReviewListData.size())
                return ListViewType.VIEW_BOTTOM;

            return mReviewListData.get(position).getReviewViewType();
        }
    }
}
