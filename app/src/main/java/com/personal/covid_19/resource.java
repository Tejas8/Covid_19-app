package com.personal.covid_19;

import android.content.SharedPreferences;
import android.graphics.Color;
import android.os.Build;
import android.os.Bundle;

import androidx.annotation.RequiresApi;
import androidx.cardview.widget.CardView;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentTransaction;

import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.FrameLayout;
import android.widget.ListView;

import com.personal.covid_19.model.india_Data;
import com.personal.covid_19.model.resources;
import com.personal.covid_19.model.responseresource;
import com.personal.covid_19.utils.utils;

import java.text.DecimalFormat;
import java.util.ArrayList;
import java.util.List;

import io.reactivex.Observer;
import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.disposables.Disposable;
import io.reactivex.schedulers.Schedulers;

import static android.content.Context.MODE_PRIVATE;
import static androidx.constraintlayout.widget.Constraints.TAG;


/**
 * A simple {@link Fragment} subclass.
 */
public class resource extends Fragment implements View.OnClickListener {
    CardView hospital,grocery,lab;
    FrameLayout fmcr;
    String state;
    ArrayList<resources> nearby =new ArrayList<resources>();
    SharedPreferences.Editor editor ;
    Bundle bundle;

    grocery gfragment;
    hospitals hfragment;
    labs lfragment;
    FragmentManager fragmentManager;
    FragmentTransaction fragmentTransaction;
    public resource() {
        // Required empty public constructor
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        ViewGroup   root=(ViewGroup)inflater.inflate(R.layout.fragment_resource, container, false);
        hospital=root.findViewById(R.id.hospitalframe);
        grocery=root.findViewById(R.id.groceryframe);

        lab=root.findViewById(R.id.testinglabframe);
        hospital.setOnClickListener(this);
        grocery.setOnClickListener(this);
        lab.setOnClickListener(this);

        fmcr=root.findViewById(R.id.framecontentresource);
        SharedPreferences pref = getActivity().getApplicationContext().getSharedPreferences("state", MODE_PRIVATE);
        Log.d(TAG, "onCreateView: "+pref.getString("state",null));
        state=pref.getString("state",null);
        fragmentManager = getChildFragmentManager();
       fragmentTransaction = fragmentManager.beginTransaction();
        gfragment = new grocery();
        fragmentTransaction.addToBackStack(null);

        fragmentTransaction.add(R.id.framecontentresource, gfragment);

        utils apiinterfaceindia=RetrofitClient.getindiaretrofit(getContext()).create(utils.class);
        apiinterfaceindia.resourceresponse().subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(new Observer<responseresource>() {
                    @Override
                    public void onSubscribe(Disposable d) {

                    }

                    @Override
                    public void onNext(responseresource responseresource) {
                        for(int i=0;i<responseresource.getResources().size()-1;i++)
                        {
                            if(responseresource.getResources().get(i).getState().equals(state))
                            {
                                nearby.add(responseresource.getResources().get(i));

                            }
                        }

                    }

                    @Override
                    public void onError(Throwable e) {
                        Log.d(TAG, "onError: "+e.getLocalizedMessage());
                    }

                    @Override
                    public void onComplete() {

                            bundle = new Bundle();
                            bundle.putSerializable("key", nearby);

                        fragmentTransaction.commitAllowingStateLoss();
                           gfragment.setArguments(bundle);

                        hospital.setCardBackgroundColor(Color.WHITE);
                        lab.setCardBackgroundColor(Color.WHITE);
                        grocery.setCardBackgroundColor(Color.LTGRAY);


                    }
                });



        return root;
    }

    @Override
    public void onClick(View v) {
    switch (v.getId())
    {
        case R.id.hospitalframe:
            hospital.setCardBackgroundColor(Color.LTGRAY);
            lab.setCardBackgroundColor(Color.WHITE);
            grocery.setCardBackgroundColor(Color.WHITE);
            fragmentManager = getChildFragmentManager();
            fragmentTransaction = fragmentManager.beginTransaction();
            hfragment = new hospitals();
            fragmentTransaction.addToBackStack("xyz");


            fragmentTransaction.add(R.id.framecontentresource, hfragment);
            fragmentTransaction.replace(R.id.framecontentresource,hfragment);
            hfragment.setArguments(bundle);
            fragmentTransaction.commit();

            break;
        case  R.id.groceryframe:

            hospital.setCardBackgroundColor(Color.WHITE);
            lab.setCardBackgroundColor(Color.WHITE);
            grocery.setCardBackgroundColor(Color.LTGRAY);
            fragmentManager = getChildFragmentManager();
            fragmentTransaction = fragmentManager.beginTransaction();
           gfragment = new grocery();
            fragmentTransaction.addToBackStack("xyz");


            fragmentTransaction.add(R.id.framecontentresource, gfragment);
            fragmentTransaction.replace(R.id.framecontentresource,gfragment);
            gfragment.setArguments(bundle);
            fragmentTransaction.commit();

            break;
        case  R.id.testinglabframe:

            hospital.setCardBackgroundColor(Color.WHITE);
            lab.setCardBackgroundColor(Color.LTGRAY);
            grocery.setCardBackgroundColor(Color.WHITE);
            fragmentManager = getChildFragmentManager();
            fragmentTransaction = fragmentManager.beginTransaction();
            lfragment = new labs();
            fragmentTransaction.addToBackStack("xyz");

            fragmentTransaction.add(R.id.framecontentresource, lfragment);
            fragmentTransaction.replace(R.id.framecontentresource,lfragment);
            lfragment.setArguments(bundle);
            fragmentTransaction.commit();
            break;
    }
    }
}
