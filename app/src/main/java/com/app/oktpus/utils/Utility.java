package com.app.oktpus.utils;

import android.app.Activity;
import android.app.job.JobInfo;
import android.app.job.JobScheduler;
import android.content.ComponentName;
import android.content.Context;
import android.content.Intent;
import android.graphics.Typeface;
import android.os.Build;
import android.os.PersistableBundle;
import android.support.annotation.RequiresApi;
import android.support.design.widget.Snackbar;
import android.text.Spannable;
import android.text.SpannableString;
import android.text.style.ForegroundColorSpan;
import android.view.View;
import android.view.ViewGroup;
import android.view.inputmethod.InputMethodManager;
import android.widget.ScrollView;
import android.widget.TextView;

import com.android.volley.Request;
import com.android.volley.VolleyError;
import com.app.oktpus.Lib.featureDiscovery.TapTarget;
import com.app.oktpus.Lib.featureDiscovery.TapTargetSequence;
import com.app.oktpus.Lib.featureDiscovery.TapTargetView;
import com.app.oktpus.R;
import com.app.oktpus.controller.AppController;
import com.app.oktpus.responseModel.Config.CommonFields;

import java.io.UnsupportedEncodingException;
import java.net.URLEncoder;
import java.text.DecimalFormat;
import java.util.List;
import java.util.Random;

/**
 * Created by Gyandeep on 6/12/16.
 */

public class Utility {

    //Number formatter
    /*function formNumberFormat(raw_value, type, direction) {
        type = type.replace('okt-range-', ''); // This is not important for you

        var num_default = okt_display_format[type]['default_value'][direction];
        var formatted_value = raw_value;

        if (raw_value == '') {
            formatted_value = okt_display_format[type]['mask'][direction].replace("{val}", formatNumber_byType(num_default, type));
        } else {
            formatted_value = parseFloat(Math.floor(raw_value).toString().replace(/[^0-9]/gi, ""));;
            if (direction == 'max' && formatted_value >= num_default) {
                formatted_value = okt_display_format[type]['mask']['max'].replace("{val}",
                formatNumber_byType(num_default, type));
            } else if (direction == 'min' && formatted_value <= num_default) {
                formatted_value = okt_display_format[type]['mask']['min'].replace("{val}",
                formatNumber_byType(num_default, type));
            } else {
                formatted_value = okt_display_format[type]['mask']['regular'].replace("{val}",
                formatNumber_byType(formatted_value, type));
            }
        }

        return formatted_value;
    }*/

    public static void hideKeyboard(Context ctx, View view) {
        if (view != null) {
            ((InputMethodManager) ctx.getSystemService(Context.INPUT_METHOD_SERVICE)).
                    hideSoftInputFromWindow(view.getWindowToken(), InputMethodManager.HIDE_NOT_ALWAYS);
        }
    }

    public static void setViewAndChildrenEnabled(View view, boolean enabled) {
        view.setEnabled(enabled);
        if (view instanceof ViewGroup) {
            ViewGroup viewGroup = (ViewGroup) view;
            for (int i = 0; i < viewGroup.getChildCount(); i++) {
                View child = viewGroup.getChildAt(i);
                setViewAndChildrenEnabled(child, enabled);
            }
        }
    }

    public static final String VAL = "{val}";
    public static String formMinNumberFormat(int rawVal, CommonFields fields) {
        String formattedVal = "";
        String mask = fields.getMask().getMin();
        formattedVal = formatNumberByType(rawVal, fields.getValueFormatType());
        return (rawVal <= fields.getDefaultValue().getMin()) ? mask.replace(VAL, formattedVal) :
                formRegularNumberFormat(formattedVal, fields);
    }

    public static String formMaxNumberFormat(int rawVal, CommonFields fields) {
        String formattedVal = "";
        String mask = fields.getMask().getMax();
        if(rawVal >= fields.getDefaultValue().getMax()) {
            formattedVal = formatNumberByType(fields.getDefaultValue().getMax(), fields.getValueFormatType());
            formattedVal = mask.replace(VAL, formattedVal);
        }
        else {
            formattedVal = formatNumberByType(rawVal, fields.getValueFormatType());
            formattedVal = formRegularNumberFormat(formattedVal, fields);
        }

        return formattedVal;
    }

    public static String formRegularNumberFormat(String numberToMask, CommonFields fields) {
        String mask = fields.getMask().getRegular();
        return mask.replace(VAL, numberToMask);
    }

    public static String formatNumberByType(int rawVal, String formatType){
        return new DecimalFormat(formatType).format(rawVal);
    }

    public static String getFormattedQuery(String url) {
        try {
            return URLEncoder.encode((url), "utf-8");
        } catch (UnsupportedEncodingException e) {
            e.printStackTrace();
            return url;
        }
    }

    public final static void anchorViewToTop(final int amountToScroll, final ScrollView scrollView){
        try {
            scrollView.post(new Runnable() {
                @Override
                public void run() {
                    scrollView.smoothScrollTo(0, amountToScroll);
                }
            });
        }
        catch(Exception e){
            e.printStackTrace();
        }
    }

    public static String getSearchHint(Context context) {
        String [] hintArray = context.getResources().getStringArray(R.array.search_hints);
        //int rnd = new Random().nextInt(hintArray.length);
        return hintArray[new Random().nextInt(hintArray.length)];
    }

    /*Feature Display class*/
    public static void startFeatureAnim(Activity activity, final View view, String title, String titleDesc) {
        TapTargetView.showFor(activity, TapTarget.forView(view, title, titleDesc)
                // All options below are optional
                .outerCircleColor(R.color.buttonColor)      // Specify a color for the outer circle
                .outerCircleAlpha(0.99f)            // Specify the alpha amount for the outer circle
                .targetCircleColor(R.color.white)   // Specify a color for the target circle
                .titleTextSize(20)                  // Specify the size (in sp) of the title text
                .titleTextColor(R.color.white)      // Specify the color of the title text
                .descriptionTextSize(15)            // Specify the size (in sp) of the description text
                //.descriptionTextColor(R.color.red)  // Specify the color of the description text
                //.textColor(R.color.blue)            // Specify a color for both the title and description text
                .textTypeface(Typeface.SANS_SERIF)  // Specify a typeface for the text
                //.dimColor(R.color.black)            // If set, will dim behind the view with 30% opacity of the given color
                .drawShadow(false)                   // Whether to draw a drop shadow or not
                .cancelable(true)                  // Whether tapping outside the outer circle dismisses the view
                .tintTarget(false)                   // Whether to tint the target view's color
                .transparentTarget(false)           // Specify whether the target is transparent (displays the content underneath)
                //.icon(Drawable)                     // Specify a custom drawable to draw as the target
                .targetRadius(50), new TapTargetView.Listener() {          // The listener can listen for regular clicks, long clicks or cancels
            @Override
            public void onTargetClick(TapTargetView view) {
                super.onTargetClick(view);
            }
        });
    }

    public static TapTarget buildTapTarget(View view, String title, String titleDesc) {
        return TapTarget.forView(view, title, titleDesc)
                .outerCircleColor(R.color.buttonColor)
                .outerCircleAlpha(0.99f)
                .targetCircleColor(R.color.white)
                .titleTextSize(20)
                .titleTextColor(R.color.white)
                .descriptionTextSize(15)
                //.descriptionTextColor(R.color.red)
                //.textColor(R.color.blue)
                .textTypeface(Typeface.SANS_SERIF)
                //.dimColor(R.color.black)
                .drawShadow(true)
                .cancelable(true)
                .tintTarget(false)
                .transparentTarget(false)
                //.icon(Drawable)
                .targetRadius(50);
    }

    public static void startSequenceFeatureAnim(final Activity activity, List<TapTarget> targetList) {
        /*TapTarget.forView(findViewById(R.id.never), "Gonna"),
                        TapTarget.forView(findViewById(R.id.give), "You", "Up")
                                .dimColor(android.R.color.never)
                                .outerCircleColor(R.color.gonna)
                                .targetCircleColor(R.color.let)
                                .textColor(android.R.color.you),
                        TapTarget.forBounds(rickTarget, "Down", ":^)")
                                .cancelable(false)
                                .icon(rick)*/

        new TapTargetSequence(activity)
                .targets(targetList).considerOuterCircleCanceled(true).continueOnCancel(true)
                .listener(new TapTargetSequence.Listener() {
                    // This listener will tell us when interesting(tm) events happen in regards
                    // to the sequence
                    @Override
                    public void onSequenceFinish() {
                        new SessionManagement(activity).setIsDemoChecked(true);
                    }

                    @Override
                    public void onSequenceStep(TapTarget lastTarget, boolean targetClicked) {}

                    @Override
                    public void onSequenceCanceled(TapTarget lastTarget) { }
                }).start();
    }

    /*public static void shareIntent(Intent share, Context context, String url, String imgUrl, ImageView iv) {
        try {
            //share.putExtra(Intent.EXTRA_TITLE, "Oktpus");
            share.setType("text/plain");
            *//*String path = MediaStore.Images.Media.insertImage(context.getContentResolver(), ((GlideBitmapDrawable)iv.getDrawable()).getBitmap(), "Oktpus", processShareUrl(url));
            Uri uri = Uri.parse(path);
            share.putExtra(Intent.EXTRA_STREAM, uri);*//*
            share.putExtra(Intent.EXTRA_TEXT, processShareUrl(url));
            //share.putExtra(Intent.EXTRA_TEMPLATE, uri);
            //share.putExtra(Intent.EXTRA_TEXT, processShareUrl(url));    //Html.fromHtml("<p>This is the text shared <img src="+imgUrl+"></p>")
            //share.setData(uri);
            //share.addFlags(Intent.FLAG_GRANT_WRITE_URI_PERMISSION);
            //share.putExtra(Intent.EXTRA_TEXT, url);
            context.startActivity(Intent.createChooser(share, "Share using"));
        } catch (Exception e) {
            e.printStackTrace();
        }
    }*/

    /*public static void shareIntent(Intent share, Context context, String url, ImageView iv){//}, String itemName, String date, String city) {
        try {
            //share.putExtra(Intent.EXTRA_TITLE, "Oktpus");
            share.setType("text/plain");
            Spanned txt = SpannableString.valueOf("<p>Make-model <br> <b>city</b> <br> year <br> date <br>"+processShareUrl(url));
            share.putExtra(Intent.EXTRA_TEXT, processShareUrl(url)); //"<p>Make-model <br> <b>city</b> <br> year <br> date <br>"+    ///Html.fromHtml("<p>Make-model <br> city <br> year <br> date <br>"+
            share.putExtra(Intent.EXTRA_SUBJECT, "make_model");
            context.startActivity(Intent.createChooser(share, "Share using"));
        } catch (Exception e) {
            e.printStackTrace();
        }
    }*/

    public static void shareIntent(Intent share, Context context, String url, String itemName, String date) {
        try {
            //share.putExtra(Intent.EXTRA_TITLE, "Oktpus");
            share.setType("text/plain");
            share.setAction(Intent.ACTION_SEND);
            SpannableString ss = new SpannableString(itemName + "\n"+ date);
            //ss.setSpan(clickableSpan, 0, 18, Spanned.SPAN_EXCLUSIVE_EXCLUSIVE);
            //StyleSpan boldSpan = new StyleSpan(Typeface.BOLD);
            //ss.setSpan(boldSpan, 0, ss.length(), Spannable.SPAN_EXCLUSIVE_EXCLUSIVE);
            //ss.setSpan(new ForegroundColorSpan(context.getResources().getColor(R.color.link)), 0, ss.length(), 0);
            //ss.setSpan(new ForegroundColorSpan(context.getResources().getColor(R.color.sf_create_ac_msg)), 19, ss.length(), 0);

            //Spanned txt = SpannableString.valueOf("<p>Make-model <br> <b>city</b> <br> year <br> date <br>"+processShareUrl(url));
            share.putExtra(android.content.Intent.EXTRA_TEXT, ss + "\n" + processShareUrl(url)); //"<p>Make-model <br> <b>city</b> <br> year <br> date <br>"+ ///"<p>Make-model <br> city <br> year <br> date <br>"+
            context.startActivity(Intent.createChooser(share, "Share using"));
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public static String processShareUrl(String url) {
        String pUrl;
        if(url.contains("data_source=web")) {
            pUrl = url.replaceFirst("data_source=web", "data_source=share");
            return pUrl;
        }
        else
            return url;
    }

    /*public static void shareIntent(Intent share, Context context, String url, String imgUrl) {
        share.setType("image*//*");
        //share.putExtra(Intent.EXTRA_TITLE, "Oktpus");
        //share.putExtra(Intent.EXTRA_STREAM, getLocalBitmapUri(iv, context));

        //share.putExtra(Intent.EXTRA_TEXT, url);
        context.startActivity(Intent.createChooser(share, "Share with"));
    }*/

    /*public static Uri getLocalBitmapUri(ImageView imageView, Context context) {
        // Extract Bitmap from ImageView drawable
        Drawable drawable = imageView.getDrawable();
        Bitmap bmp = null;
        if (drawable instanceof BitmapDrawable){
            bmp = ((BitmapDrawable) imageView.getDrawable()).getBitmap();
        } else {
            return null;
        }
        // Store image to default external storage directory
        Uri bmpUri = null;
        try {
            // Use methods on Context to access package-specific directories on external storage.
            // This way, you don't need to request external read/write permission.
            // See https://youtu.be/5xVh-7ywKpE?t=25m25s
            File file =  new File(context.getExternalFilesDir(Environment.DIRECTORY_PICTURES), "share_image_" + System.currentTimeMillis() + ".png");
            FileOutputStream out = new FileOutputStream(file);
            bmp.compress(Bitmap.CompressFormat.PNG, 90, out);
            out.close();
            // **Warning:** This will fail for API >= 24, use a FileProvider as shown below instead.
            bmpUri = Uri.fromFile(file);
        } catch (IOException e) {
            e.printStackTrace();
        }
        return bmpUri;
    }*/

    public static Spannable boldText(String txt, Context context) {
        Spannable spanString = Spannable.Factory.getInstance().newSpannable(txt);
        spanString.setSpan(new ForegroundColorSpan(context.getResources().getColor(R.color.colorPrimaryDark)), 0, txt.length(), Spannable.SPAN_EXCLUSIVE_EXCLUSIVE);
        spanString.setSpan(new android.text.style.StyleSpan(android.graphics.Typeface.BOLD), 0, txt.length(), Spannable.SPAN_EXCLUSIVE_EXCLUSIVE);
        return spanString;
    }

    public static Spannable colorifyFilterText(Context context, String constraint, TextView view, int startPos, int endPos) {
        Spannable spanString = Spannable.Factory.getInstance().newSpannable(view.getText());
        spanString.setSpan(new ForegroundColorSpan(context.getResources().getColor(R.color.colorPrimaryDark)), startPos, endPos, Spannable.SPAN_EXCLUSIVE_EXCLUSIVE);
        spanString.setSpan(new android.text.style.StyleSpan(android.graphics.Typeface.BOLD), startPos, endPos, Spannable.SPAN_EXCLUSIVE_EXCLUSIVE);
        return spanString;
    }

    public static String numberInspector(String text) {
        return text.replaceAll("[^\\d.]", "");
    }

    public static void showErrorSnackbar(Context context, View mainView, final Request requestClient, VolleyError error, final String requestTag) {
        final Snackbar snackbarError = Snackbar.make(mainView, NetworkErrors.getErrorMessage(context, error), Snackbar.LENGTH_INDEFINITE);
        snackbarError.setAction(context.getResources().getString(R.string.label_try_again), new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                snackbarError.dismiss();
                AppController.getInstance().addToRequestQueue(requestClient, requestTag);
            }
        });
        snackbarError.setActionTextColor(context.getResources().getColor(R.color.primaryBtnPressed));
        snackbarError.show();
    }

    /*@RequiresApi(api = Build.VERSION_CODES.M)
    public static void scheduleJob(Context context, PersistableBundle extras) {
        ComponentName serviceComponent = new ComponentName(context, Scheduler.class);

        JobInfo.Builder builder = new JobInfo.Builder(0, serviceComponent);

        builder.setExtras(extras);
        //builder.setMinimumLatency(1 * 1000); // wait at least
        //builder.setOverrideDeadline(3 * 1000); // maximum delay
        builder.setRequiredNetworkType(JobInfo.NETWORK_TYPE_UNMETERED); // require unmetered network
        //builder.setRequiresDeviceIdle(true); // device should be idle
        //builder.setRequiresCharging(false); // we don't care if the device is charging or not
        JobScheduler jobScheduler = context.getSystemService(JobScheduler.class);

        jobScheduler.schedule(builder.build());
    }*/
}