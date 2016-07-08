package com.hdfc.caregiver.fragments;

import android.app.ProgressDialog;
import android.content.Intent;
import android.graphics.Bitmap;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.RatingBar;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.hdfc.adapters.RatingsAdapter;
import com.hdfc.caregiver.MyProfileActivity;
import com.hdfc.caregiver.R;
import com.hdfc.config.Config;
import com.hdfc.libs.Utils;
import com.hdfc.views.RoundedImageView;

import java.io.File;


public class RatingsFragment extends Fragment {

    public static Bitmap bitmap = null;
    public static RatingsAdapter ratingsAdapter;

    private static int intWhichScreen;
    private static Handler backgroundThreadHandler;
    private static ProgressDialog mProgress = null;
    private static Utils utils;
    private static LinearLayout layout;
    public TextView textViewName, textViewEmpty;
    ImageView mytask, clients, feedback;
    RoundedImageView imageProfilePic;
    RelativeLayout myprofile;
    ListView listratings;
    private RatingBar ratingBar;

    public RatingsFragment(){
    }

    public static RatingsFragment newInstance() {
        RatingsFragment fragment = new RatingsFragment();
        Bundle args = new Bundle();
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_ratings, container, false);
        mytask = (ImageView) view.findViewById(R.id.buttonMyTasks);
        clients = (ImageView) view.findViewById(R.id.buttonClients);
        feedback = (ImageView) view.findViewById(R.id.buttonFeedback);
        listratings = (ListView) view.findViewById(R.id.listViewRatings);
        textViewName = (TextView) view.findViewById(R.id.name);
        textViewEmpty = (TextView) view.findViewById(android.R.id.empty);
        Button logout = (Button) view.findViewById(R.id.buttonlogout);

        //  layout = (LinearLayout) view.findViewById(R.id.linearLayoutRatings);
        ratingBar = (RatingBar) view.findViewById(R.id.ratingBar);

        imageProfilePic = (RoundedImageView)view.findViewById(R.id.img);
        mProgress = new ProgressDialog(getActivity());
        utils = new Utils(getActivity());
        intWhichScreen = Config.intRatingsScreen;

        if (Config.providerModel.getStrName() != null)
            textViewName.setText(Config.providerModel.getStrName());

        backgroundThreadHandler = new BackgroundThreadHandler();
        BackgroundThread backgroundThread = new BackgroundThread();
        backgroundThread.start();


        myprofile = (RelativeLayout) view.findViewById(R.id.relativelayoutRatings);

        logout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(getActivity(), MyProfileActivity.class);
                //intent.putExtra("WHICH_SCREEN", intWhichScreen);
                startActivity(intent);
            }
        });

        int iRatings = 0;

        if (Config.iRatingCount > 0)

            ratingBar.setRating((float) (Config.iRatings / Config.iRatingCount));

        /*int i = iRatings;
        layout.removeAllViews();

        int j, k;

        for (j = 0; j < i; j++) {

            ImageView imageView = new ImageView(getActivity());

            imageView.setPadding(0, 0, 10, 0);
            imageView.setImageBitmap(BitmapFactory.decodeResource(getResources(), R.mipmap.star_gold));
            imageView.setScaleType(ImageView.ScaleType.CENTER_CROP);
            layout.addView(imageView);
        }

        //Utils.log(String.valueOf(i + " ! " + j), " R ");

        for (k = i; k < 5; k++) {

            ImageView imageView = new ImageView(getActivity());

            imageView.setPadding(0, 0, 10, 0);
            imageView.setImageBitmap(BitmapFactory.decodeResource(getResources(),
                    R.mipmap.star_grey));
            imageView.setScaleType(ImageView.ScaleType.CENTER_CROP);

            layout.addView(imageView);
        }
*/
        //Utils.log(String.valueOf(i + " ! " + k), " R ");

        ratingsAdapter = new RatingsAdapter(getContext(), Config.feedBackModels);
        listratings.setAdapter(ratingsAdapter);
        listratings.setEmptyView(textViewEmpty);

        return view;
    }

    @Override
    public void onResume() {
        super.onResume();
    }


    public class BackgroundThreadHandler extends Handler {
        @Override
        public void handleMessage(Message msg) {
            mProgress.dismiss();

            try {

                if (imageProfilePic != null && bitmap != null)
                    imageProfilePic.setImageBitmap(bitmap);


            } catch (Exception | OutOfMemoryError e) {
                e.printStackTrace();
            }
        }
    }

    public class BackgroundThread extends Thread {
        @Override
        public void run() {
            try {
                File f = utils.getInternalFileImages(Config.providerModel.getStrProviderId());

                if(f!=null&&f.exists())
                    bitmap = utils.getBitmapFromFile(f.getAbsolutePath(), Config.intWidth,
                            Config.intHeight);

            } catch (Exception e) {
                e.printStackTrace();
            }

            /*try {
                if (activityFeedBackModels != null) {
                    for (int i = 0; i < activityFeedBackModels.size(); i++) {
                        Utils.log(activityFeedBackModels.get(i).getStrFeedBackByUrl(), " URL ");
                        utils.loadImageFromWeb(activityFeedBackModels.get(i).getStrFeedBackBy(),
                                activityFeedBackModels.get(i).getStrFeedBackByUrl());
                    }
                }
            } catch (Exception e) {
                e.printStackTrace();
            }*/
            backgroundThreadHandler.sendEmptyMessage(0);
        }
    }
}
