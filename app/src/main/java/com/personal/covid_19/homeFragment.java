package com.personal.covid_19;

import android.Manifest;
import android.annotation.SuppressLint;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.pm.PackageManager;
import android.graphics.Color;
import android.location.Location;
import android.location.LocationManager;
import android.net.Uri;
import android.os.AsyncTask;
import android.os.Build;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.annotation.RequiresApi;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentTransaction;

import android.os.Looper;
import android.provider.Settings;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;


import java.nio.file.Path;
import java.text.DecimalFormat;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;

import java.util.LinkedHashMap;
import java.util.List;


import co.blankkeys.animatedlinegraphview.AnimatedLineGraphView;
import io.reactivex.Observer;
import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.disposables.Disposable;
import io.reactivex.schedulers.Schedulers;
import ir.farshid_roohi.linegraph.ChartEntity;


import com.github.mikephil.charting.charts.BarChart;


import com.github.mikephil.charting.components.Legend;
import ir.farshid_roohi.linegraph.LineChart;
import com.github.mikephil.charting.data.BarData;
import com.github.mikephil.charting.data.BarDataSet;
import com.github.mikephil.charting.data.BarEntry;
import com.github.mikephil.charting.formatter.ValueFormatter;
import com.google.android.gms.auth.api.signin.GoogleSignIn;
import com.google.android.gms.auth.api.signin.GoogleSignInAccount;
import com.google.android.gms.auth.api.signin.GoogleSignInClient;
import com.google.android.gms.auth.api.signin.GoogleSignInOptions;
import com.google.android.gms.location.FusedLocationProviderClient;
import com.google.android.gms.location.LocationCallback;
import com.google.android.gms.location.LocationRequest;
import com.google.android.gms.location.LocationResult;
import com.google.android.gms.location.LocationServices;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.personal.covid_19.model.dailyrjstatus;
import com.personal.covid_19.model.india_Data;
import  com.personal.covid_19.utils.*;
import com.ramijemli.percentagechartview.PercentageChartView;

import static android.content.Context.MODE_PRIVATE;
import static androidx.constraintlayout.widget.Constraints.TAG;


/**
 * A simple {@link Fragment} subclass.
 */
public class homeFragment extends Fragment {
    ImageView casemap;
    String currenSTATE;
    SharedPreferences pref;
    AnimatedLineGraphView affectedGraphView,activegraphview,graphfcon,graphscon,graphtcon,deadgraph,recovgraph;
    LineChart chart;
    TextView affected,active,fconame,factive,sconame,sactive,tconame,tactive,recovered,dead;

    Float affected1,recovered1,dead1,active1;
    PercentageChartView percentageChartView;
//    JSONObject jsonValuecases,jsonValuedeath
//            ,jsonValuerecover= null;
    TextView readmore;


//    List<String> casesdata = new ArrayList<>();
//    List<String> deathdata = new ArrayList<>();
//    List<String> recoverdata = new ArrayList<>();
//    String cars[];
//    List<String> topthreecon=new ArrayList<>();
//    List<countrydailydata> countrydatadaily1=null;
//    List<countrydailydata> countrydatadaily2=null;
//    List<countrydailydata> countrydatadaily3=null;

//    ObjectMapper objectMapper = new ObjectMapper();
    HashMap<Integer, String> statecases=new HashMap<>();
    LinkedHashMap<Integer, String> sortedCASES = new LinkedHashMap<>();
    List<String> top3states=new ArrayList<>();
    List<Integer> top3statecases=new ArrayList<>();
    TextView name;
    TextView currentstate,dailyact,dailyaffect,dailydead,dailyrecov;
    ImageView indian;
//    Boolean indianbutton=false;
    TextView top3tview;

    boolean checkin=false;
    ArrayList<BarEntry> data;
    Legend legend;
    String statecode;
    GoogleSignInClient signInClient;





    private static String TAG="Check";


    public homeFragment() {
        // Required empty public constructor
    }




    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {

     ViewGroup   root=(ViewGroup)inflater.inflate(R.layout.fragment_home, container, false);
        SharedPreferences pref = getActivity().getApplicationContext().getSharedPreferences("state", MODE_PRIVATE);
        Log.d(TAG, "onCreateView: "+pref.getString("state",null));
        currenSTATE=pref.getString("state",null);
        currentstate=root.findViewById(R.id.statecurrent);
        name=root.findViewById(R.id.nameuser);

        deadgraph=root.findViewById(R.id.deadrgraph);
        recovgraph=root.findViewById(R.id.recovergraph);
        dead=root.findViewById(R.id.dead);
        recovered=root.findViewById(R.id.recover);

        affectedGraphView=root.findViewById(R.id.affectedgraph);
        activegraphview=root.findViewById(R.id.activegraph);

        affected=root.findViewById(R.id.textaffected);
        active=root.findViewById(R.id.textactive);
        chart=root.findViewById(R.id.lineChart);
        float graph1[]=  new float[]{0, 0, 0, 0, 0, 0, 0};
        String array[]=new String[]{"S", "S", "S", "S", "S", "S", "S"};

        ChartEntity first= new ChartEntity(Color.YELLOW,graph1);
        ArrayList<ChartEntity> list=new ArrayList<>();
        list.add(first);
        chart.setLegend(Arrays.asList(array));
        chart.setList(list);

















//        SharedPreferences pref = getActivity().getApplicationContext().getSharedPreferences("mode", MODE_PRIVATE);
//        SharedPreferences.Editor editor = pref.edit();
//
//        indian.setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View v) {
//                if(pref.getBoolean("india",false))
//                {   editor.putBoolean("india",false);
//                    editor.commit();
//                    Log.d(TAG, "onClick: "+pref.getBoolean("india",false));
//
//                }
//                else if(!pref.getBoolean("india",false)){
//                    editor.putBoolean("india",true);
//                    editor.commit();
//
//                }
//                FragmentTransaction ft = getFragmentManager().beginTransaction();
//                ft.detach(homeFragment.this).attach(homeFragment.this).commit();
//            }
//        });


//    if(!pref.getBoolean("india",false)) {
//        top3tview.setText("Top 3 Country");
//        indian.setImageResource(R.drawable.flagind);
//        globalcases();
//    }


        indiancases();




        // Inflate the layout for this fragment
        return root;
    }
    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);

    }
    private void rajasthandat(){
        utils apiinterfaceindia=RetrofitClient.getindiaretrofit(getContext()).create(utils.class);
        apiinterfaceindia.rajasthandata().subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(new Observer<dailyrjstatus>() {
                    @Override
                    public void onSubscribe(Disposable d) {

                    }

                    @Override
                    public void onNext(dailyrjstatus dailyrjstatus) {
                        Log.d(TAG, "onNext: "+dailyrjstatus.getStates_daily().get(5).getStatus().toString());
                        makerjgraphs(dailyrjstatus);
                    }

                    @Override
                    public void onError(Throwable e) {

                    }

                    @Override
                    public void onComplete() {

                    }
                });

    }

    private void makerjgraphs(dailyrjstatus dailyrjstatus) {

        int size=dailyrjstatus.getStates_daily().size();
        Log.d(TAG, "makerjgraphs: "+size);

if(statecode.equalsIgnoreCase("rj")) {

    float afectedcasep[] = new float[]{affected1 - Float.parseFloat(dailyrjstatus.getStates_daily().get(size - 6).getRj()),
            affected1 - Float.parseFloat(dailyrjstatus.getStates_daily().get(size - 9).getRj()) - Float.parseFloat(dailyrjstatus.getStates_daily().get(size - 6).getRj()),
            affected1 - Float.parseFloat(dailyrjstatus.getStates_daily().get(size - 12).getRj()) - Float.parseFloat(dailyrjstatus.getStates_daily().get(size - 9).getRj()) - Float.parseFloat(dailyrjstatus.getStates_daily().get(size - 6).getRj()),
            affected1 - Float.parseFloat(dailyrjstatus.getStates_daily().get(size - 15).getRj()) - Float.parseFloat(dailyrjstatus.getStates_daily().get(size - 12).getRj()) - Float.parseFloat(dailyrjstatus.getStates_daily().get(size - 9).getRj()) - Float.parseFloat(dailyrjstatus.getStates_daily().get(size - 6).getRj()),
            affected1 - Float.parseFloat(dailyrjstatus.getStates_daily().get(size - 18).getRj()) - Float.parseFloat(dailyrjstatus.getStates_daily().get(size - 15).getRj()) - Float.parseFloat(dailyrjstatus.getStates_daily().get(size - 12).getRj()) - Float.parseFloat(dailyrjstatus.getStates_daily().get(size - 9).getRj()) - Float.parseFloat(dailyrjstatus.getStates_daily().get(size - 6).getRj()),
            affected1 - Float.parseFloat(dailyrjstatus.getStates_daily().get(size - 21).getRj()) - Float.parseFloat(dailyrjstatus.getStates_daily().get(size - 18).getRj()) - Float.parseFloat(dailyrjstatus.getStates_daily().get(size - 15).getRj()) - Float.parseFloat(dailyrjstatus.getStates_daily().get(size - 12).getRj()) - Float.parseFloat(dailyrjstatus.getStates_daily().get(size - 9).getRj()) - Float.parseFloat(dailyrjstatus.getStates_daily().get(size - 6).getRj())

    };


    affectedGraphView.setData(afectedcasep);
    float deceased[] = new float[]{dead1 - Float.parseFloat(dailyrjstatus.getStates_daily().get(size - 4).getRj()),
            dead1 - Float.parseFloat(dailyrjstatus.getStates_daily().get(size - 7).getRj()) - Float.parseFloat(dailyrjstatus.getStates_daily().get(size - 4).getRj()),
            dead1 - Float.parseFloat(dailyrjstatus.getStates_daily().get(size - 10).getRj()) - Float.parseFloat(dailyrjstatus.getStates_daily().get(size - 7).getRj()) - Float.parseFloat(dailyrjstatus.getStates_daily().get(size - 4).getRj()),
            dead1 - Float.parseFloat(dailyrjstatus.getStates_daily().get(size - 13).getRj()) - Float.parseFloat(dailyrjstatus.getStates_daily().get(size - 10).getRj()) - Float.parseFloat(dailyrjstatus.getStates_daily().get(size - 7).getRj()) - Float.parseFloat(dailyrjstatus.getStates_daily().get(size - 4).getRj()),
            dead1 - Float.parseFloat(dailyrjstatus.getStates_daily().get(size - 16).getRj()) - Float.parseFloat(dailyrjstatus.getStates_daily().get(size - 13).getRj()) - Float.parseFloat(dailyrjstatus.getStates_daily().get(size - 10).getRj()) - Float.parseFloat(dailyrjstatus.getStates_daily().get(size - 7).getRj()) - Float.parseFloat(dailyrjstatus.getStates_daily().get(size - 4).getRj()),
            dead1 - Float.parseFloat(dailyrjstatus.getStates_daily().get(size - 19).getRj()) - Float.parseFloat(dailyrjstatus.getStates_daily().get(size - 16).getRj()) - Float.parseFloat(dailyrjstatus.getStates_daily().get(size - 13).getRj()) - Float.parseFloat(dailyrjstatus.getStates_daily().get(size - 10).getRj()) - Float.parseFloat(dailyrjstatus.getStates_daily().get(size - 7).getRj()) - Float.parseFloat(dailyrjstatus.getStates_daily().get(size - 4).getRj())

    };


    deadgraph.setData(deceased);
    float recov[] = new float[]{recovered1 - Float.parseFloat(dailyrjstatus.getStates_daily().get(size - 5).getRj()),
            recovered1 - Float.parseFloat(dailyrjstatus.getStates_daily().get(size - 8).getRj()) - Float.parseFloat(dailyrjstatus.getStates_daily().get(size - 5).getRj()),
            recovered1 - Float.parseFloat(dailyrjstatus.getStates_daily().get(size - 11).getRj()) - Float.parseFloat(dailyrjstatus.getStates_daily().get(size - 8).getRj()) - Float.parseFloat(dailyrjstatus.getStates_daily().get(size - 5).getRj()),
            recovered1 - Float.parseFloat(dailyrjstatus.getStates_daily().get(size - 14).getRj()) - Float.parseFloat(dailyrjstatus.getStates_daily().get(size - 11).getRj()) - Float.parseFloat(dailyrjstatus.getStates_daily().get(size - 8).getRj()) - Float.parseFloat(dailyrjstatus.getStates_daily().get(size - 5).getRj()),
            recovered1 - Float.parseFloat(dailyrjstatus.getStates_daily().get(size - 17).getRj()) - Float.parseFloat(dailyrjstatus.getStates_daily().get(size - 14).getRj()) - Float.parseFloat(dailyrjstatus.getStates_daily().get(size - 11).getRj()) - Float.parseFloat(dailyrjstatus.getStates_daily().get(size - 8).getRj()) - Float.parseFloat(dailyrjstatus.getStates_daily().get(size - 5).getRj()),
            recovered1 - Float.parseFloat(dailyrjstatus.getStates_daily().get(size - 20).getRj()) - Float.parseFloat(dailyrjstatus.getStates_daily().get(size - 17).getRj()) - Float.parseFloat(dailyrjstatus.getStates_daily().get(size - 14).getRj()) - Float.parseFloat(dailyrjstatus.getStates_daily().get(size - 11).getRj()) - Float.parseFloat(dailyrjstatus.getStates_daily().get(size - 8).getRj()) - Float.parseFloat(dailyrjstatus.getStates_daily().get(size - 5).getRj())

    };
    recovgraph.setData(null);// for example
    recovgraph.setData(recov);
    float active[] = new float[]{active1 - Float.parseFloat(dailyrjstatus.getStates_daily().get(size - 5).getRj()) - Float.parseFloat(dailyrjstatus.getStates_daily().get(size - 4).getRj()) - Float.parseFloat(dailyrjstatus.getStates_daily().get(size - 6).getRj()),
            active1 - Float.parseFloat(dailyrjstatus.getStates_daily().get(size - 8).getRj()) - Float.parseFloat(dailyrjstatus.getStates_daily().get(size - 5).getRj()) - Float.parseFloat(dailyrjstatus.getStates_daily().get(size - 7).getRj()) - Float.parseFloat(dailyrjstatus.getStates_daily().get(size - 4).getRj()) - Float.parseFloat(dailyrjstatus.getStates_daily().get(size - 8).getRj()) - Float.parseFloat(dailyrjstatus.getStates_daily().get(size - 5).getRj()),
            active1 - Float.parseFloat(dailyrjstatus.getStates_daily().get(size - 11).getRj()) - Float.parseFloat(dailyrjstatus.getStates_daily().get(size - 8).getRj()) - Float.parseFloat(dailyrjstatus.getStates_daily().get(size - 5).getRj()) - Float.parseFloat(dailyrjstatus.getStates_daily().get(size - 11).getRj()) - Float.parseFloat(dailyrjstatus.getStates_daily().get(size - 8).getRj()) - Float.parseFloat(dailyrjstatus.getStates_daily().get(size - 5).getRj()) - Float.parseFloat(dailyrjstatus.getStates_daily().get(size - 10).getRj()) - Float.parseFloat(dailyrjstatus.getStates_daily().get(size - 7).getRj()) - Float.parseFloat(dailyrjstatus.getStates_daily().get(size - 4).getRj()),

            active1 - Float.parseFloat(dailyrjstatus.getStates_daily().get(size - 14).getRj()) - Float.parseFloat(dailyrjstatus.getStates_daily().get(size - 11).getRj()) - Float.parseFloat(dailyrjstatus.getStates_daily().get(size - 8).getRj()) - Float.parseFloat(dailyrjstatus.getStates_daily().get(size - 5).getRj()) - Float.parseFloat(dailyrjstatus.getStates_daily().get(size - 14).getRj()) - Float.parseFloat(dailyrjstatus.getStates_daily().get(size - 11).getRj()) - Float.parseFloat(dailyrjstatus.getStates_daily().get(size - 8).getRj()) - Float.parseFloat(dailyrjstatus.getStates_daily().get(size - 5).getRj()) - Float.parseFloat(dailyrjstatus.getStates_daily().get(size - 13).getRj()) - Float.parseFloat(dailyrjstatus.getStates_daily().get(size - 10).getRj()) - Float.parseFloat(dailyrjstatus.getStates_daily().get(size - 7).getRj()) - Float.parseFloat(dailyrjstatus.getStates_daily().get(size - 4).getRj()),


    };

    activegraphview.setData(active);


//   data.add(new BarEntry(Float.parseFloat(dailyrjstatus.getStates_daily().get(size-3).getDate()),Float.parseFloat(dailyrjstatus.getStates_daily().get(size-3).getRj())));
//        data.add(new BarEntry(Float.parseFloat(dailyrjstatus.getStates_daily().get(size-6).getDate()),));
//        data.add(new BarEntry(Float.parseFloat(dailyrjstatus.getStates_daily().get(size-9).getDate()),Float.parseFloat(dailyrjstatus.getStates_daily().get(size-9).getRj())));
//        data.add(new BarEntry(Float.parseFloat(dailyrjstatus.getStates_daily().get(size-12).getDate()),Float.parseFloat(dailyrjstatus.getStates_daily().get(size-12).getRj())));
//        data.add(new BarEntry(Float.parseFloat(dailyrjstatus.getStates_daily().get(size-15).getDate()),Float.parseFloat(dailyrjstatus.getStates_daily().get(size-15).getRj())));
//        data.add(new BarEntry(Float.parseFloat(dailyrjstatus.getStates_daily().get(size-18).getDate()),Float.parseFloat(dailyrjstatus.getStates_daily().get(size-18).getRj())));
//        data.add(new BarEntry(Float.parseFloat(dailyrjstatus.getStates_daily().get(size-21).getDate()),Float.parseFloat(dailyrjstatus.getStates_daily().get(size-21).getRj())));
//        BarDataSet barDataSet=new BarDataSet(data,"Cases");
//        BarData barData=new BarData();
//        barData.addDataSet(barDataSet);
//        chart.setData(barData);
//        chart.invalidate();
    float graph1[] = new float[]{Float.parseFloat(dailyrjstatus.getStates_daily().get(size - 3).getRj()),
            Float.parseFloat(dailyrjstatus.getStates_daily().get(size - 6).getRj()),
            Float.parseFloat(dailyrjstatus.getStates_daily().get(size - 9).getRj()),
            Float.parseFloat(dailyrjstatus.getStates_daily().get(size - 12).getRj()),
            Float.parseFloat(dailyrjstatus.getStates_daily().get(size - 15).getRj()),
            Float.parseFloat(dailyrjstatus.getStates_daily().get(size - 18).getRj()),
            Float.parseFloat(dailyrjstatus.getStates_daily().get(size - 21).getRj())
    };
    String array[] = new String[]{dailyrjstatus.getStates_daily().get(size - 3).getDate(),
            dailyrjstatus.getStates_daily().get(size - 6).getDate(),
            dailyrjstatus.getStates_daily().get(size - 9).getDate(),
            dailyrjstatus.getStates_daily().get(size - 12).getDate(),
            dailyrjstatus.getStates_daily().get(size - 15).getDate(),
            dailyrjstatus.getStates_daily().get(size - 18).getDate(),
            dailyrjstatus.getStates_daily().get(size - 21).getDate()};
    ChartEntity first = new ChartEntity(Color.WHITE, graph1);
    ArrayList<ChartEntity> list = new ArrayList<>();
    list.add(first);
    chart.setLegend(Arrays.asList(array));
    chart.setList(list);

}

else if(statecode.equalsIgnoreCase("br"))
{

    float afectedcasep[]=  new float[]{affected1-Float.parseFloat(dailyrjstatus.getStates_daily().get(size-6).getBr()),
            affected1-Float.parseFloat(dailyrjstatus.getStates_daily().get(size-9).getBr())-Float.parseFloat(dailyrjstatus.getStates_daily().get(size-6).getBr()),
            affected1-Float.parseFloat(dailyrjstatus.getStates_daily().get(size-12).getBr())-Float.parseFloat(dailyrjstatus.getStates_daily().get(size-9).getBr())-Float.parseFloat(dailyrjstatus.getStates_daily().get(size-6).getBr()),
            affected1-Float.parseFloat(dailyrjstatus.getStates_daily().get(size-15).getBr())-Float.parseFloat(dailyrjstatus.getStates_daily().get(size-12).getBr())-Float.parseFloat(dailyrjstatus.getStates_daily().get(size-9).getBr())-Float.parseFloat(dailyrjstatus.getStates_daily().get(size-6).getBr()),
            affected1-Float.parseFloat(dailyrjstatus.getStates_daily().get(size-18).getBr())-Float.parseFloat(dailyrjstatus.getStates_daily().get(size-15).getBr())-Float.parseFloat(dailyrjstatus.getStates_daily().get(size-12).getBr())-Float.parseFloat(dailyrjstatus.getStates_daily().get(size-9).getBr())-Float.parseFloat(dailyrjstatus.getStates_daily().get(size-6).getBr()),
            affected1-Float.parseFloat(dailyrjstatus.getStates_daily().get(size-21).getBr())-Float.parseFloat(dailyrjstatus.getStates_daily().get(size-18).getBr())-Float.parseFloat(dailyrjstatus.getStates_daily().get(size-15).getBr())-Float.parseFloat(dailyrjstatus.getStates_daily().get(size-12).getBr())-Float.parseFloat(dailyrjstatus.getStates_daily().get(size-9).getBr())-Float.parseFloat(dailyrjstatus.getStates_daily().get(size-6).getBr())

    };


    affectedGraphView.setData(afectedcasep);
    float deceased[]=  new float[]{dead1-Float.parseFloat(dailyrjstatus.getStates_daily().get(size-4).getBr()),
            dead1-Float.parseFloat(dailyrjstatus.getStates_daily().get(size-7).getBr())-Float.parseFloat(dailyrjstatus.getStates_daily().get(size-4).getBr()),
            dead1-Float.parseFloat(dailyrjstatus.getStates_daily().get(size-10).getBr())-Float.parseFloat(dailyrjstatus.getStates_daily().get(size-7).getBr())-Float.parseFloat(dailyrjstatus.getStates_daily().get(size-4).getBr()),
            dead1-Float.parseFloat(dailyrjstatus.getStates_daily().get(size-13).getBr())-Float.parseFloat(dailyrjstatus.getStates_daily().get(size-10).getBr())-Float.parseFloat(dailyrjstatus.getStates_daily().get(size-7).getBr())-Float.parseFloat(dailyrjstatus.getStates_daily().get(size-4).getBr()),
            dead1-Float.parseFloat(dailyrjstatus.getStates_daily().get(size-16).getBr())-Float.parseFloat(dailyrjstatus.getStates_daily().get(size-13).getBr())-Float.parseFloat(dailyrjstatus.getStates_daily().get(size-10).getBr())-Float.parseFloat(dailyrjstatus.getStates_daily().get(size-7).getBr())-Float.parseFloat(dailyrjstatus.getStates_daily().get(size-4).getBr()),
            dead1-Float.parseFloat(dailyrjstatus.getStates_daily().get(size-19).getBr())-Float.parseFloat(dailyrjstatus.getStates_daily().get(size-16).getBr())-Float.parseFloat(dailyrjstatus.getStates_daily().get(size-13).getBr())-Float.parseFloat(dailyrjstatus.getStates_daily().get(size-10).getBr())-Float.parseFloat(dailyrjstatus.getStates_daily().get(size-7).getBr())-Float.parseFloat(dailyrjstatus.getStates_daily().get(size-4).getBr())

    };


    deadgraph.setData(deceased);
    float recov[]=  new float[]{recovered1-Float.parseFloat(dailyrjstatus.getStates_daily().get(size-5).getBr()),
            recovered1-Float.parseFloat(dailyrjstatus.getStates_daily().get(size-8).getBr())-Float.parseFloat(dailyrjstatus.getStates_daily().get(size-5).getBr()),
            recovered1-Float.parseFloat(dailyrjstatus.getStates_daily().get(size-11).getBr())-Float.parseFloat(dailyrjstatus.getStates_daily().get(size-8).getBr())-Float.parseFloat(dailyrjstatus.getStates_daily().get(size-5).getBr()),
            recovered1-Float.parseFloat(dailyrjstatus.getStates_daily().get(size-14).getBr())-Float.parseFloat(dailyrjstatus.getStates_daily().get(size-11).getBr())-Float.parseFloat(dailyrjstatus.getStates_daily().get(size-8).getBr())-Float.parseFloat(dailyrjstatus.getStates_daily().get(size-5).getBr()),
            recovered1-Float.parseFloat(dailyrjstatus.getStates_daily().get(size-17).getBr())-Float.parseFloat(dailyrjstatus.getStates_daily().get(size-14).getBr())-Float.parseFloat(dailyrjstatus.getStates_daily().get(size-11).getBr())-Float.parseFloat(dailyrjstatus.getStates_daily().get(size-8).getBr())-Float.parseFloat(dailyrjstatus.getStates_daily().get(size-5).getBr()),
            recovered1-Float.parseFloat(dailyrjstatus.getStates_daily().get(size-20).getBr())-Float.parseFloat(dailyrjstatus.getStates_daily().get(size-17).getBr())-Float.parseFloat(dailyrjstatus.getStates_daily().get(size-14).getBr())-Float.parseFloat(dailyrjstatus.getStates_daily().get(size-11).getBr())-Float.parseFloat(dailyrjstatus.getStates_daily().get(size-8).getBr())-Float.parseFloat(dailyrjstatus.getStates_daily().get(size-5).getBr())

    };
    recovgraph.setData(null);// for example
    recovgraph.setData(recov);
    float active[]=  new float[]{active1-Float.parseFloat(dailyrjstatus.getStates_daily().get(size-5).getBr())-Float.parseFloat(dailyrjstatus.getStates_daily().get(size-4).getBr())-Float.parseFloat(dailyrjstatus.getStates_daily().get(size-6).getBr()),
            active1-Float.parseFloat(dailyrjstatus.getStates_daily().get(size-8).getBr())-Float.parseFloat(dailyrjstatus.getStates_daily().get(size-5).getBr())-Float.parseFloat(dailyrjstatus.getStates_daily().get(size-7).getBr())-Float.parseFloat(dailyrjstatus.getStates_daily().get(size-4).getBr())-Float.parseFloat(dailyrjstatus.getStates_daily().get(size-8).getBr())-Float.parseFloat(dailyrjstatus.getStates_daily().get(size-5).getBr()),
            active1-Float.parseFloat(dailyrjstatus.getStates_daily().get(size-11).getBr())-Float.parseFloat(dailyrjstatus.getStates_daily().get(size-8).getBr())-Float.parseFloat(dailyrjstatus.getStates_daily().get(size-5).getBr())-Float.parseFloat(dailyrjstatus.getStates_daily().get(size-11).getBr())-Float.parseFloat(dailyrjstatus.getStates_daily().get(size-8).getBr())-Float.parseFloat(dailyrjstatus.getStates_daily().get(size-5).getBr())-Float.parseFloat(dailyrjstatus.getStates_daily().get(size-10).getBr())-Float.parseFloat(dailyrjstatus.getStates_daily().get(size-7).getBr())-Float.parseFloat(dailyrjstatus.getStates_daily().get(size-4).getBr()),

            active1-Float.parseFloat(dailyrjstatus.getStates_daily().get(size-14).getBr())-Float.parseFloat(dailyrjstatus.getStates_daily().get(size-11).getBr())-Float.parseFloat(dailyrjstatus.getStates_daily().get(size-8).getBr())-Float.parseFloat(dailyrjstatus.getStates_daily().get(size-5).getBr())-Float.parseFloat(dailyrjstatus.getStates_daily().get(size-14).getBr())-Float.parseFloat(dailyrjstatus.getStates_daily().get(size-11).getBr())-Float.parseFloat(dailyrjstatus.getStates_daily().get(size-8).getBr())-Float.parseFloat(dailyrjstatus.getStates_daily().get(size-5).getBr())-Float.parseFloat(dailyrjstatus.getStates_daily().get(size-13).getBr())-Float.parseFloat(dailyrjstatus.getStates_daily().get(size-10).getBr())-Float.parseFloat(dailyrjstatus.getStates_daily().get(size-7).getBr())-Float.parseFloat(dailyrjstatus.getStates_daily().get(size-4).getBr()),




    };

    activegraphview.setData(active);



//   data.add(new BarEntry(Float.parseFloat(dailyrjstatus.getStates_daily().get(size-3).getDate()),Float.parseFloat(dailyrjstatus.getStates_daily().get(size-3).getBr())));
//        data.add(new BarEntry(Float.parseFloat(dailyrjstatus.getStates_daily().get(size-6).getDate()),));
//        data.add(new BarEntry(Float.parseFloat(dailyrjstatus.getStates_daily().get(size-9).getDate()),Float.parseFloat(dailyrjstatus.getStates_daily().get(size-9).getBr())));
//        data.add(new BarEntry(Float.parseFloat(dailyrjstatus.getStates_daily().get(size-12).getDate()),Float.parseFloat(dailyrjstatus.getStates_daily().get(size-12).getBr())));
//        data.add(new BarEntry(Float.parseFloat(dailyrjstatus.getStates_daily().get(size-15).getDate()),Float.parseFloat(dailyrjstatus.getStates_daily().get(size-15).getBr())));
//        data.add(new BarEntry(Float.parseFloat(dailyrjstatus.getStates_daily().get(size-18).getDate()),Float.parseFloat(dailyrjstatus.getStates_daily().get(size-18).getBr())));
//        data.add(new BarEntry(Float.parseFloat(dailyrjstatus.getStates_daily().get(size-21).getDate()),Float.parseFloat(dailyrjstatus.getStates_daily().get(size-21).getBr())));
//        BarDataSet barDataSet=new BarDataSet(data,"Cases");
//        BarData barData=new BarData();
//        barData.addDataSet(barDataSet);
//        chart.setData(barData);
//        chart.invalidate();
    float graph1[]=  new float[]{Float.parseFloat(dailyrjstatus.getStates_daily().get(size-3).getBr()),
            Float.parseFloat(dailyrjstatus.getStates_daily().get(size-6).getBr()),
            Float.parseFloat(dailyrjstatus.getStates_daily().get(size-9).getBr()),
            Float.parseFloat(dailyrjstatus.getStates_daily().get(size-12).getBr()),
            Float.parseFloat(dailyrjstatus.getStates_daily().get(size-15).getBr()),
            Float.parseFloat(dailyrjstatus.getStates_daily().get(size-18).getBr()),
            Float.parseFloat(dailyrjstatus.getStates_daily().get(size-21).getBr())
    };
    String array[] = new String[]{dailyrjstatus.getStates_daily().get(size - 3).getDate(),
            dailyrjstatus.getStates_daily().get(size - 6).getDate(),
            dailyrjstatus.getStates_daily().get(size - 9).getDate(),
            dailyrjstatus.getStates_daily().get(size - 12).getDate(),
            dailyrjstatus.getStates_daily().get(size - 15).getDate(),
            dailyrjstatus.getStates_daily().get(size - 18).getDate(),
            dailyrjstatus.getStates_daily().get(size - 21).getDate()};
    ChartEntity first = new ChartEntity(Color.WHITE, graph1);
    ArrayList<ChartEntity> list = new ArrayList<>();
    list.add(first);
    chart.setLegend(Arrays.asList(array));
    chart.setList(list);



}






else if(statecode.equalsIgnoreCase("ch"))
{

    float afectedcasep[]=  new float[]{affected1-Float.parseFloat(dailyrjstatus.getStates_daily().get(size-6).getCh()),
            affected1-Float.parseFloat(dailyrjstatus.getStates_daily().get(size-9).getCh())-Float.parseFloat(dailyrjstatus.getStates_daily().get(size-6).getCh()),
            affected1-Float.parseFloat(dailyrjstatus.getStates_daily().get(size-12).getCh())-Float.parseFloat(dailyrjstatus.getStates_daily().get(size-9).getCh())-Float.parseFloat(dailyrjstatus.getStates_daily().get(size-6).getCh()),
            affected1-Float.parseFloat(dailyrjstatus.getStates_daily().get(size-15).getCh())-Float.parseFloat(dailyrjstatus.getStates_daily().get(size-12).getCh())-Float.parseFloat(dailyrjstatus.getStates_daily().get(size-9).getCh())-Float.parseFloat(dailyrjstatus.getStates_daily().get(size-6).getCh()),
            affected1-Float.parseFloat(dailyrjstatus.getStates_daily().get(size-18).getCh())-Float.parseFloat(dailyrjstatus.getStates_daily().get(size-15).getCh())-Float.parseFloat(dailyrjstatus.getStates_daily().get(size-12).getCh())-Float.parseFloat(dailyrjstatus.getStates_daily().get(size-9).getCh())-Float.parseFloat(dailyrjstatus.getStates_daily().get(size-6).getCh()),
            affected1-Float.parseFloat(dailyrjstatus.getStates_daily().get(size-21).getCh())-Float.parseFloat(dailyrjstatus.getStates_daily().get(size-18).getCh())-Float.parseFloat(dailyrjstatus.getStates_daily().get(size-15).getCh())-Float.parseFloat(dailyrjstatus.getStates_daily().get(size-12).getCh())-Float.parseFloat(dailyrjstatus.getStates_daily().get(size-9).getCh())-Float.parseFloat(dailyrjstatus.getStates_daily().get(size-6).getCh())

    };


    affectedGraphView.setData(afectedcasep);
    float deceased[]=  new float[]{dead1-Float.parseFloat(dailyrjstatus.getStates_daily().get(size-4).getCh()),
            dead1-Float.parseFloat(dailyrjstatus.getStates_daily().get(size-7).getCh())-Float.parseFloat(dailyrjstatus.getStates_daily().get(size-4).getCh()),
            dead1-Float.parseFloat(dailyrjstatus.getStates_daily().get(size-10).getCh())-Float.parseFloat(dailyrjstatus.getStates_daily().get(size-7).getCh())-Float.parseFloat(dailyrjstatus.getStates_daily().get(size-4).getCh()),
            dead1-Float.parseFloat(dailyrjstatus.getStates_daily().get(size-13).getCh())-Float.parseFloat(dailyrjstatus.getStates_daily().get(size-10).getCh())-Float.parseFloat(dailyrjstatus.getStates_daily().get(size-7).getCh())-Float.parseFloat(dailyrjstatus.getStates_daily().get(size-4).getCh()),
            dead1-Float.parseFloat(dailyrjstatus.getStates_daily().get(size-16).getCh())-Float.parseFloat(dailyrjstatus.getStates_daily().get(size-13).getCh())-Float.parseFloat(dailyrjstatus.getStates_daily().get(size-10).getCh())-Float.parseFloat(dailyrjstatus.getStates_daily().get(size-7).getCh())-Float.parseFloat(dailyrjstatus.getStates_daily().get(size-4).getCh()),
            dead1-Float.parseFloat(dailyrjstatus.getStates_daily().get(size-19).getCh())-Float.parseFloat(dailyrjstatus.getStates_daily().get(size-16).getCh())-Float.parseFloat(dailyrjstatus.getStates_daily().get(size-13).getCh())-Float.parseFloat(dailyrjstatus.getStates_daily().get(size-10).getCh())-Float.parseFloat(dailyrjstatus.getStates_daily().get(size-7).getCh())-Float.parseFloat(dailyrjstatus.getStates_daily().get(size-4).getCh())

    };


    deadgraph.setData(deceased);
    float recov[]=  new float[]{recovered1-Float.parseFloat(dailyrjstatus.getStates_daily().get(size-5).getCh()),
            recovered1-Float.parseFloat(dailyrjstatus.getStates_daily().get(size-8).getCh())-Float.parseFloat(dailyrjstatus.getStates_daily().get(size-5).getCh()),
            recovered1-Float.parseFloat(dailyrjstatus.getStates_daily().get(size-11).getCh())-Float.parseFloat(dailyrjstatus.getStates_daily().get(size-8).getCh())-Float.parseFloat(dailyrjstatus.getStates_daily().get(size-5).getCh()),
            recovered1-Float.parseFloat(dailyrjstatus.getStates_daily().get(size-14).getCh())-Float.parseFloat(dailyrjstatus.getStates_daily().get(size-11).getCh())-Float.parseFloat(dailyrjstatus.getStates_daily().get(size-8).getCh())-Float.parseFloat(dailyrjstatus.getStates_daily().get(size-5).getCh()),
            recovered1-Float.parseFloat(dailyrjstatus.getStates_daily().get(size-17).getCh())-Float.parseFloat(dailyrjstatus.getStates_daily().get(size-14).getCh())-Float.parseFloat(dailyrjstatus.getStates_daily().get(size-11).getCh())-Float.parseFloat(dailyrjstatus.getStates_daily().get(size-8).getCh())-Float.parseFloat(dailyrjstatus.getStates_daily().get(size-5).getCh()),
            recovered1-Float.parseFloat(dailyrjstatus.getStates_daily().get(size-20).getCh())-Float.parseFloat(dailyrjstatus.getStates_daily().get(size-17).getCh())-Float.parseFloat(dailyrjstatus.getStates_daily().get(size-14).getCh())-Float.parseFloat(dailyrjstatus.getStates_daily().get(size-11).getCh())-Float.parseFloat(dailyrjstatus.getStates_daily().get(size-8).getCh())-Float.parseFloat(dailyrjstatus.getStates_daily().get(size-5).getCh())

    };
    recovgraph.setData(null);// for example
    recovgraph.setData(recov);
    float active[]=  new float[]{active1-Float.parseFloat(dailyrjstatus.getStates_daily().get(size-5).getCh())-Float.parseFloat(dailyrjstatus.getStates_daily().get(size-4).getCh())-Float.parseFloat(dailyrjstatus.getStates_daily().get(size-6).getCh()),
            active1-Float.parseFloat(dailyrjstatus.getStates_daily().get(size-8).getCh())-Float.parseFloat(dailyrjstatus.getStates_daily().get(size-5).getCh())-Float.parseFloat(dailyrjstatus.getStates_daily().get(size-7).getCh())-Float.parseFloat(dailyrjstatus.getStates_daily().get(size-4).getCh())-Float.parseFloat(dailyrjstatus.getStates_daily().get(size-8).getCh())-Float.parseFloat(dailyrjstatus.getStates_daily().get(size-5).getCh()),
            active1-Float.parseFloat(dailyrjstatus.getStates_daily().get(size-11).getCh())-Float.parseFloat(dailyrjstatus.getStates_daily().get(size-8).getCh())-Float.parseFloat(dailyrjstatus.getStates_daily().get(size-5).getCh())-Float.parseFloat(dailyrjstatus.getStates_daily().get(size-11).getCh())-Float.parseFloat(dailyrjstatus.getStates_daily().get(size-8).getCh())-Float.parseFloat(dailyrjstatus.getStates_daily().get(size-5).getCh())-Float.parseFloat(dailyrjstatus.getStates_daily().get(size-10).getCh())-Float.parseFloat(dailyrjstatus.getStates_daily().get(size-7).getCh())-Float.parseFloat(dailyrjstatus.getStates_daily().get(size-4).getCh()),

            active1-Float.parseFloat(dailyrjstatus.getStates_daily().get(size-14).getCh())-Float.parseFloat(dailyrjstatus.getStates_daily().get(size-11).getCh())-Float.parseFloat(dailyrjstatus.getStates_daily().get(size-8).getCh())-Float.parseFloat(dailyrjstatus.getStates_daily().get(size-5).getCh())-Float.parseFloat(dailyrjstatus.getStates_daily().get(size-14).getCh())-Float.parseFloat(dailyrjstatus.getStates_daily().get(size-11).getCh())-Float.parseFloat(dailyrjstatus.getStates_daily().get(size-8).getCh())-Float.parseFloat(dailyrjstatus.getStates_daily().get(size-5).getCh())-Float.parseFloat(dailyrjstatus.getStates_daily().get(size-13).getCh())-Float.parseFloat(dailyrjstatus.getStates_daily().get(size-10).getCh())-Float.parseFloat(dailyrjstatus.getStates_daily().get(size-7).getCh())-Float.parseFloat(dailyrjstatus.getStates_daily().get(size-4).getCh()),




    };

    activegraphview.setData(active);



//   data.add(new BarEntry(Float.parseFloat(dailyrjstatus.getStates_daily().get(size-3).getDate()),Float.parseFloat(dailyrjstatus.getStates_daily().get(size-3).getCh())));
//        data.add(new BarEntry(Float.parseFloat(dailyrjstatus.getStates_daily().get(size-6).getDate()),));
//        data.add(new BarEntry(Float.parseFloat(dailyrjstatus.getStates_daily().get(size-9).getDate()),Float.parseFloat(dailyrjstatus.getStates_daily().get(size-9).getCh())));
//        data.add(new BarEntry(Float.parseFloat(dailyrjstatus.getStates_daily().get(size-12).getDate()),Float.parseFloat(dailyrjstatus.getStates_daily().get(size-12).getCh())));
//        data.add(new BarEntry(Float.parseFloat(dailyrjstatus.getStates_daily().get(size-15).getDate()),Float.parseFloat(dailyrjstatus.getStates_daily().get(size-15).getCh())));
//        data.add(new BarEntry(Float.parseFloat(dailyrjstatus.getStates_daily().get(size-18).getDate()),Float.parseFloat(dailyrjstatus.getStates_daily().get(size-18).getCh())));
//        data.add(new BarEntry(Float.parseFloat(dailyrjstatus.getStates_daily().get(size-21).getDate()),Float.parseFloat(dailyrjstatus.getStates_daily().get(size-21).getCh())));
//        BarDataSet barDataSet=new BarDataSet(data,"Cases");
//        BarData barData=new BarData();
//        barData.addDataSet(barDataSet);
//        chart.setData(barData);
//        chart.invalidate();
    float graph1[]=  new float[]{Float.parseFloat(dailyrjstatus.getStates_daily().get(size-3).getCh()),
            Float.parseFloat(dailyrjstatus.getStates_daily().get(size-6).getCh()),
            Float.parseFloat(dailyrjstatus.getStates_daily().get(size-9).getCh()),
            Float.parseFloat(dailyrjstatus.getStates_daily().get(size-12).getCh()),
            Float.parseFloat(dailyrjstatus.getStates_daily().get(size-15).getCh()),
            Float.parseFloat(dailyrjstatus.getStates_daily().get(size-18).getCh()),
            Float.parseFloat(dailyrjstatus.getStates_daily().get(size-21).getCh())
    };
    String array[] = new String[]{dailyrjstatus.getStates_daily().get(size - 3).getDate(),
            dailyrjstatus.getStates_daily().get(size - 6).getDate(),
            dailyrjstatus.getStates_daily().get(size - 9).getDate(),
            dailyrjstatus.getStates_daily().get(size - 12).getDate(),
            dailyrjstatus.getStates_daily().get(size - 15).getDate(),
            dailyrjstatus.getStates_daily().get(size - 18).getDate(),
            dailyrjstatus.getStates_daily().get(size - 21).getDate()};
    ChartEntity first = new ChartEntity(Color.WHITE, graph1);
    ArrayList<ChartEntity> list = new ArrayList<>();
    list.add(first);
    chart.setLegend(Arrays.asList(array));
    chart.setList(list);



}




else if(statecode.equalsIgnoreCase("dl"))
{

    float afectedcasep[]=  new float[]{affected1-Float.parseFloat(dailyrjstatus.getStates_daily().get(size-6).getDl()),
            affected1-Float.parseFloat(dailyrjstatus.getStates_daily().get(size-9).getDl())-Float.parseFloat(dailyrjstatus.getStates_daily().get(size-6).getDl()),
            affected1-Float.parseFloat(dailyrjstatus.getStates_daily().get(size-12).getDl())-Float.parseFloat(dailyrjstatus.getStates_daily().get(size-9).getDl())-Float.parseFloat(dailyrjstatus.getStates_daily().get(size-6).getDl()),
            affected1-Float.parseFloat(dailyrjstatus.getStates_daily().get(size-15).getDl())-Float.parseFloat(dailyrjstatus.getStates_daily().get(size-12).getDl())-Float.parseFloat(dailyrjstatus.getStates_daily().get(size-9).getDl())-Float.parseFloat(dailyrjstatus.getStates_daily().get(size-6).getDl()),
            affected1-Float.parseFloat(dailyrjstatus.getStates_daily().get(size-18).getDl())-Float.parseFloat(dailyrjstatus.getStates_daily().get(size-15).getDl())-Float.parseFloat(dailyrjstatus.getStates_daily().get(size-12).getDl())-Float.parseFloat(dailyrjstatus.getStates_daily().get(size-9).getDl())-Float.parseFloat(dailyrjstatus.getStates_daily().get(size-6).getDl()),
            affected1-Float.parseFloat(dailyrjstatus.getStates_daily().get(size-21).getDl())-Float.parseFloat(dailyrjstatus.getStates_daily().get(size-18).getDl())-Float.parseFloat(dailyrjstatus.getStates_daily().get(size-15).getDl())-Float.parseFloat(dailyrjstatus.getStates_daily().get(size-12).getDl())-Float.parseFloat(dailyrjstatus.getStates_daily().get(size-9).getDl())-Float.parseFloat(dailyrjstatus.getStates_daily().get(size-6).getDl())

    };


    affectedGraphView.setData(afectedcasep);
    float deceased[]=  new float[]{dead1-Float.parseFloat(dailyrjstatus.getStates_daily().get(size-4).getDl()),
            dead1-Float.parseFloat(dailyrjstatus.getStates_daily().get(size-7).getDl())-Float.parseFloat(dailyrjstatus.getStates_daily().get(size-4).getDl()),
            dead1-Float.parseFloat(dailyrjstatus.getStates_daily().get(size-10).getDl())-Float.parseFloat(dailyrjstatus.getStates_daily().get(size-7).getDl())-Float.parseFloat(dailyrjstatus.getStates_daily().get(size-4).getDl()),
            dead1-Float.parseFloat(dailyrjstatus.getStates_daily().get(size-13).getDl())-Float.parseFloat(dailyrjstatus.getStates_daily().get(size-10).getDl())-Float.parseFloat(dailyrjstatus.getStates_daily().get(size-7).getDl())-Float.parseFloat(dailyrjstatus.getStates_daily().get(size-4).getDl()),
            dead1-Float.parseFloat(dailyrjstatus.getStates_daily().get(size-16).getDl())-Float.parseFloat(dailyrjstatus.getStates_daily().get(size-13).getDl())-Float.parseFloat(dailyrjstatus.getStates_daily().get(size-10).getDl())-Float.parseFloat(dailyrjstatus.getStates_daily().get(size-7).getDl())-Float.parseFloat(dailyrjstatus.getStates_daily().get(size-4).getDl()),
            dead1-Float.parseFloat(dailyrjstatus.getStates_daily().get(size-19).getDl())-Float.parseFloat(dailyrjstatus.getStates_daily().get(size-16).getDl())-Float.parseFloat(dailyrjstatus.getStates_daily().get(size-13).getDl())-Float.parseFloat(dailyrjstatus.getStates_daily().get(size-10).getDl())-Float.parseFloat(dailyrjstatus.getStates_daily().get(size-7).getDl())-Float.parseFloat(dailyrjstatus.getStates_daily().get(size-4).getDl())

    };


    deadgraph.setData(deceased);
    float recov[]=  new float[]{recovered1-Float.parseFloat(dailyrjstatus.getStates_daily().get(size-5).getDl()),
            recovered1-Float.parseFloat(dailyrjstatus.getStates_daily().get(size-8).getDl())-Float.parseFloat(dailyrjstatus.getStates_daily().get(size-5).getDl()),
            recovered1-Float.parseFloat(dailyrjstatus.getStates_daily().get(size-11).getDl())-Float.parseFloat(dailyrjstatus.getStates_daily().get(size-8).getDl())-Float.parseFloat(dailyrjstatus.getStates_daily().get(size-5).getDl()),
            recovered1-Float.parseFloat(dailyrjstatus.getStates_daily().get(size-14).getDl())-Float.parseFloat(dailyrjstatus.getStates_daily().get(size-11).getDl())-Float.parseFloat(dailyrjstatus.getStates_daily().get(size-8).getDl())-Float.parseFloat(dailyrjstatus.getStates_daily().get(size-5).getDl()),
            recovered1-Float.parseFloat(dailyrjstatus.getStates_daily().get(size-17).getDl())-Float.parseFloat(dailyrjstatus.getStates_daily().get(size-14).getDl())-Float.parseFloat(dailyrjstatus.getStates_daily().get(size-11).getDl())-Float.parseFloat(dailyrjstatus.getStates_daily().get(size-8).getDl())-Float.parseFloat(dailyrjstatus.getStates_daily().get(size-5).getDl()),
            recovered1-Float.parseFloat(dailyrjstatus.getStates_daily().get(size-20).getDl())-Float.parseFloat(dailyrjstatus.getStates_daily().get(size-17).getDl())-Float.parseFloat(dailyrjstatus.getStates_daily().get(size-14).getDl())-Float.parseFloat(dailyrjstatus.getStates_daily().get(size-11).getDl())-Float.parseFloat(dailyrjstatus.getStates_daily().get(size-8).getDl())-Float.parseFloat(dailyrjstatus.getStates_daily().get(size-5).getDl())

    };
    recovgraph.setData(null);// for example
    recovgraph.setData(recov);
    float active[]=  new float[]{active1-Float.parseFloat(dailyrjstatus.getStates_daily().get(size-5).getDl())-Float.parseFloat(dailyrjstatus.getStates_daily().get(size-4).getDl())-Float.parseFloat(dailyrjstatus.getStates_daily().get(size-6).getDl()),
            active1-Float.parseFloat(dailyrjstatus.getStates_daily().get(size-8).getDl())-Float.parseFloat(dailyrjstatus.getStates_daily().get(size-5).getDl())-Float.parseFloat(dailyrjstatus.getStates_daily().get(size-7).getDl())-Float.parseFloat(dailyrjstatus.getStates_daily().get(size-4).getDl())-Float.parseFloat(dailyrjstatus.getStates_daily().get(size-8).getDl())-Float.parseFloat(dailyrjstatus.getStates_daily().get(size-5).getDl()),
            active1-Float.parseFloat(dailyrjstatus.getStates_daily().get(size-11).getDl())-Float.parseFloat(dailyrjstatus.getStates_daily().get(size-8).getDl())-Float.parseFloat(dailyrjstatus.getStates_daily().get(size-5).getDl())-Float.parseFloat(dailyrjstatus.getStates_daily().get(size-11).getDl())-Float.parseFloat(dailyrjstatus.getStates_daily().get(size-8).getDl())-Float.parseFloat(dailyrjstatus.getStates_daily().get(size-5).getDl())-Float.parseFloat(dailyrjstatus.getStates_daily().get(size-10).getDl())-Float.parseFloat(dailyrjstatus.getStates_daily().get(size-7).getDl())-Float.parseFloat(dailyrjstatus.getStates_daily().get(size-4).getDl()),

            active1-Float.parseFloat(dailyrjstatus.getStates_daily().get(size-14).getDl())-Float.parseFloat(dailyrjstatus.getStates_daily().get(size-11).getDl())-Float.parseFloat(dailyrjstatus.getStates_daily().get(size-8).getDl())-Float.parseFloat(dailyrjstatus.getStates_daily().get(size-5).getDl())-Float.parseFloat(dailyrjstatus.getStates_daily().get(size-14).getDl())-Float.parseFloat(dailyrjstatus.getStates_daily().get(size-11).getDl())-Float.parseFloat(dailyrjstatus.getStates_daily().get(size-8).getDl())-Float.parseFloat(dailyrjstatus.getStates_daily().get(size-5).getDl())-Float.parseFloat(dailyrjstatus.getStates_daily().get(size-13).getDl())-Float.parseFloat(dailyrjstatus.getStates_daily().get(size-10).getDl())-Float.parseFloat(dailyrjstatus.getStates_daily().get(size-7).getDl())-Float.parseFloat(dailyrjstatus.getStates_daily().get(size-4).getDl()),




    };

    activegraphview.setData(active);



//   data.add(new BarEntry(Float.parseFloat(dailyrjstatus.getStates_daily().get(size-3).getDate()),Float.parseFloat(dailyrjstatus.getStates_daily().get(size-3).getDl())));
//        data.add(new BarEntry(Float.parseFloat(dailyrjstatus.getStates_daily().get(size-6).getDate()),));
//        data.add(new BarEntry(Float.parseFloat(dailyrjstatus.getStates_daily().get(size-9).getDate()),Float.parseFloat(dailyrjstatus.getStates_daily().get(size-9).getDl())));
//        data.add(new BarEntry(Float.parseFloat(dailyrjstatus.getStates_daily().get(size-12).getDate()),Float.parseFloat(dailyrjstatus.getStates_daily().get(size-12).getDl())));
//        data.add(new BarEntry(Float.parseFloat(dailyrjstatus.getStates_daily().get(size-15).getDate()),Float.parseFloat(dailyrjstatus.getStates_daily().get(size-15).getDl())));
//        data.add(new BarEntry(Float.parseFloat(dailyrjstatus.getStates_daily().get(size-18).getDate()),Float.parseFloat(dailyrjstatus.getStates_daily().get(size-18).getDl())));
//        data.add(new BarEntry(Float.parseFloat(dailyrjstatus.getStates_daily().get(size-21).getDate()),Float.parseFloat(dailyrjstatus.getStates_daily().get(size-21).getDl())));
//        BarDataSet barDataSet=new BarDataSet(data,"Cases");
//        BarData barData=new BarData();
//        barData.addDataSet(barDataSet);
//        chart.setData(barData);
//        chart.invalidate();
    float graph1[]=  new float[]{Float.parseFloat(dailyrjstatus.getStates_daily().get(size-3).getDl()),
            Float.parseFloat(dailyrjstatus.getStates_daily().get(size-6).getDl()),
            Float.parseFloat(dailyrjstatus.getStates_daily().get(size-9).getDl()),
            Float.parseFloat(dailyrjstatus.getStates_daily().get(size-12).getDl()),
            Float.parseFloat(dailyrjstatus.getStates_daily().get(size-15).getDl()),
            Float.parseFloat(dailyrjstatus.getStates_daily().get(size-18).getDl()),
            Float.parseFloat(dailyrjstatus.getStates_daily().get(size-21).getDl())
    };
    String array[] = new String[]{dailyrjstatus.getStates_daily().get(size - 3).getDate(),
            dailyrjstatus.getStates_daily().get(size - 6).getDate(),
            dailyrjstatus.getStates_daily().get(size - 9).getDate(),
            dailyrjstatus.getStates_daily().get(size - 12).getDate(),
            dailyrjstatus.getStates_daily().get(size - 15).getDate(),
            dailyrjstatus.getStates_daily().get(size - 18).getDate(),
            dailyrjstatus.getStates_daily().get(size - 21).getDate()};
    ChartEntity first = new ChartEntity(Color.WHITE, graph1);
    ArrayList<ChartEntity> list = new ArrayList<>();
    list.add(first);
    chart.setLegend(Arrays.asList(array));
    chart.setList(list);



}



else if(statecode.equalsIgnoreCase("ga"))
{

    float afectedcasep[]=  new float[]{affected1-Float.parseFloat(dailyrjstatus.getStates_daily().get(size-6).getGa()),
            affected1-Float.parseFloat(dailyrjstatus.getStates_daily().get(size-9).getGa())-Float.parseFloat(dailyrjstatus.getStates_daily().get(size-6).getGa()),
            affected1-Float.parseFloat(dailyrjstatus.getStates_daily().get(size-12).getGa())-Float.parseFloat(dailyrjstatus.getStates_daily().get(size-9).getGa())-Float.parseFloat(dailyrjstatus.getStates_daily().get(size-6).getGa()),
            affected1-Float.parseFloat(dailyrjstatus.getStates_daily().get(size-15).getGa())-Float.parseFloat(dailyrjstatus.getStates_daily().get(size-12).getGa())-Float.parseFloat(dailyrjstatus.getStates_daily().get(size-9).getGa())-Float.parseFloat(dailyrjstatus.getStates_daily().get(size-6).getGa()),
            affected1-Float.parseFloat(dailyrjstatus.getStates_daily().get(size-18).getGa())-Float.parseFloat(dailyrjstatus.getStates_daily().get(size-15).getGa())-Float.parseFloat(dailyrjstatus.getStates_daily().get(size-12).getGa())-Float.parseFloat(dailyrjstatus.getStates_daily().get(size-9).getGa())-Float.parseFloat(dailyrjstatus.getStates_daily().get(size-6).getGa()),
            affected1-Float.parseFloat(dailyrjstatus.getStates_daily().get(size-21).getGa())-Float.parseFloat(dailyrjstatus.getStates_daily().get(size-18).getGa())-Float.parseFloat(dailyrjstatus.getStates_daily().get(size-15).getGa())-Float.parseFloat(dailyrjstatus.getStates_daily().get(size-12).getGa())-Float.parseFloat(dailyrjstatus.getStates_daily().get(size-9).getGa())-Float.parseFloat(dailyrjstatus.getStates_daily().get(size-6).getGa())

    };


    affectedGraphView.setData(afectedcasep);
    float deceased[]=  new float[]{dead1-Float.parseFloat(dailyrjstatus.getStates_daily().get(size-4).getGa()),
            dead1-Float.parseFloat(dailyrjstatus.getStates_daily().get(size-7).getGa())-Float.parseFloat(dailyrjstatus.getStates_daily().get(size-4).getGa()),
            dead1-Float.parseFloat(dailyrjstatus.getStates_daily().get(size-10).getGa())-Float.parseFloat(dailyrjstatus.getStates_daily().get(size-7).getGa())-Float.parseFloat(dailyrjstatus.getStates_daily().get(size-4).getGa()),
            dead1-Float.parseFloat(dailyrjstatus.getStates_daily().get(size-13).getGa())-Float.parseFloat(dailyrjstatus.getStates_daily().get(size-10).getGa())-Float.parseFloat(dailyrjstatus.getStates_daily().get(size-7).getGa())-Float.parseFloat(dailyrjstatus.getStates_daily().get(size-4).getGa()),
            dead1-Float.parseFloat(dailyrjstatus.getStates_daily().get(size-16).getGa())-Float.parseFloat(dailyrjstatus.getStates_daily().get(size-13).getGa())-Float.parseFloat(dailyrjstatus.getStates_daily().get(size-10).getGa())-Float.parseFloat(dailyrjstatus.getStates_daily().get(size-7).getGa())-Float.parseFloat(dailyrjstatus.getStates_daily().get(size-4).getGa()),
            dead1-Float.parseFloat(dailyrjstatus.getStates_daily().get(size-19).getGa())-Float.parseFloat(dailyrjstatus.getStates_daily().get(size-16).getGa())-Float.parseFloat(dailyrjstatus.getStates_daily().get(size-13).getGa())-Float.parseFloat(dailyrjstatus.getStates_daily().get(size-10).getGa())-Float.parseFloat(dailyrjstatus.getStates_daily().get(size-7).getGa())-Float.parseFloat(dailyrjstatus.getStates_daily().get(size-4).getGa())

    };


    deadgraph.setData(deceased);
    float recov[]=  new float[]{recovered1-Float.parseFloat(dailyrjstatus.getStates_daily().get(size-5).getGa()),
            recovered1-Float.parseFloat(dailyrjstatus.getStates_daily().get(size-8).getGa())-Float.parseFloat(dailyrjstatus.getStates_daily().get(size-5).getGa()),
            recovered1-Float.parseFloat(dailyrjstatus.getStates_daily().get(size-11).getGa())-Float.parseFloat(dailyrjstatus.getStates_daily().get(size-8).getGa())-Float.parseFloat(dailyrjstatus.getStates_daily().get(size-5).getGa()),
            recovered1-Float.parseFloat(dailyrjstatus.getStates_daily().get(size-14).getGa())-Float.parseFloat(dailyrjstatus.getStates_daily().get(size-11).getGa())-Float.parseFloat(dailyrjstatus.getStates_daily().get(size-8).getGa())-Float.parseFloat(dailyrjstatus.getStates_daily().get(size-5).getGa()),
            recovered1-Float.parseFloat(dailyrjstatus.getStates_daily().get(size-17).getGa())-Float.parseFloat(dailyrjstatus.getStates_daily().get(size-14).getGa())-Float.parseFloat(dailyrjstatus.getStates_daily().get(size-11).getGa())-Float.parseFloat(dailyrjstatus.getStates_daily().get(size-8).getGa())-Float.parseFloat(dailyrjstatus.getStates_daily().get(size-5).getGa()),
            recovered1-Float.parseFloat(dailyrjstatus.getStates_daily().get(size-20).getGa())-Float.parseFloat(dailyrjstatus.getStates_daily().get(size-17).getGa())-Float.parseFloat(dailyrjstatus.getStates_daily().get(size-14).getGa())-Float.parseFloat(dailyrjstatus.getStates_daily().get(size-11).getGa())-Float.parseFloat(dailyrjstatus.getStates_daily().get(size-8).getGa())-Float.parseFloat(dailyrjstatus.getStates_daily().get(size-5).getGa())

    };
    recovgraph.setData(null);// for example
    recovgraph.setData(recov);
    float active[]=  new float[]{active1-Float.parseFloat(dailyrjstatus.getStates_daily().get(size-5).getGa())-Float.parseFloat(dailyrjstatus.getStates_daily().get(size-4).getGa())-Float.parseFloat(dailyrjstatus.getStates_daily().get(size-6).getGa()),
            active1-Float.parseFloat(dailyrjstatus.getStates_daily().get(size-8).getGa())-Float.parseFloat(dailyrjstatus.getStates_daily().get(size-5).getGa())-Float.parseFloat(dailyrjstatus.getStates_daily().get(size-7).getGa())-Float.parseFloat(dailyrjstatus.getStates_daily().get(size-4).getGa())-Float.parseFloat(dailyrjstatus.getStates_daily().get(size-8).getGa())-Float.parseFloat(dailyrjstatus.getStates_daily().get(size-5).getGa()),
            active1-Float.parseFloat(dailyrjstatus.getStates_daily().get(size-11).getGa())-Float.parseFloat(dailyrjstatus.getStates_daily().get(size-8).getGa())-Float.parseFloat(dailyrjstatus.getStates_daily().get(size-5).getGa())-Float.parseFloat(dailyrjstatus.getStates_daily().get(size-11).getGa())-Float.parseFloat(dailyrjstatus.getStates_daily().get(size-8).getGa())-Float.parseFloat(dailyrjstatus.getStates_daily().get(size-5).getGa())-Float.parseFloat(dailyrjstatus.getStates_daily().get(size-10).getGa())-Float.parseFloat(dailyrjstatus.getStates_daily().get(size-7).getGa())-Float.parseFloat(dailyrjstatus.getStates_daily().get(size-4).getGa()),

            active1-Float.parseFloat(dailyrjstatus.getStates_daily().get(size-14).getGa())-Float.parseFloat(dailyrjstatus.getStates_daily().get(size-11).getGa())-Float.parseFloat(dailyrjstatus.getStates_daily().get(size-8).getGa())-Float.parseFloat(dailyrjstatus.getStates_daily().get(size-5).getGa())-Float.parseFloat(dailyrjstatus.getStates_daily().get(size-14).getGa())-Float.parseFloat(dailyrjstatus.getStates_daily().get(size-11).getGa())-Float.parseFloat(dailyrjstatus.getStates_daily().get(size-8).getGa())-Float.parseFloat(dailyrjstatus.getStates_daily().get(size-5).getGa())-Float.parseFloat(dailyrjstatus.getStates_daily().get(size-13).getGa())-Float.parseFloat(dailyrjstatus.getStates_daily().get(size-10).getGa())-Float.parseFloat(dailyrjstatus.getStates_daily().get(size-7).getGa())-Float.parseFloat(dailyrjstatus.getStates_daily().get(size-4).getGa()),




    };

    activegraphview.setData(active);



//   data.add(new BarEntry(Float.parseFloat(dailyrjstatus.getStates_daily().get(size-3).getDate()),Float.parseFloat(dailyrjstatus.getStates_daily().get(size-3).getGa())));
//        data.add(new BarEntry(Float.parseFloat(dailyrjstatus.getStates_daily().get(size-6).getDate()),));
//        data.add(new BarEntry(Float.parseFloat(dailyrjstatus.getStates_daily().get(size-9).getDate()),Float.parseFloat(dailyrjstatus.getStates_daily().get(size-9).getGa())));
//        data.add(new BarEntry(Float.parseFloat(dailyrjstatus.getStates_daily().get(size-12).getDate()),Float.parseFloat(dailyrjstatus.getStates_daily().get(size-12).getGa())));
//        data.add(new BarEntry(Float.parseFloat(dailyrjstatus.getStates_daily().get(size-15).getDate()),Float.parseFloat(dailyrjstatus.getStates_daily().get(size-15).getGa())));
//        data.add(new BarEntry(Float.parseFloat(dailyrjstatus.getStates_daily().get(size-18).getDate()),Float.parseFloat(dailyrjstatus.getStates_daily().get(size-18).getGa())));
//        data.add(new BarEntry(Float.parseFloat(dailyrjstatus.getStates_daily().get(size-21).getDate()),Float.parseFloat(dailyrjstatus.getStates_daily().get(size-21).getGa())));
//        BarDataSet barDataSet=new BarDataSet(data,"Cases");
//        BarData barData=new BarData();
//        barData.addDataSet(barDataSet);
//        chart.setData(barData);
//        chart.invalidate();
    float graph1[]=  new float[]{Float.parseFloat(dailyrjstatus.getStates_daily().get(size-3).getGa()),
            Float.parseFloat(dailyrjstatus.getStates_daily().get(size-6).getGa()),
            Float.parseFloat(dailyrjstatus.getStates_daily().get(size-9).getGa()),
            Float.parseFloat(dailyrjstatus.getStates_daily().get(size-12).getGa()),
            Float.parseFloat(dailyrjstatus.getStates_daily().get(size-15).getGa()),
            Float.parseFloat(dailyrjstatus.getStates_daily().get(size-18).getGa()),
            Float.parseFloat(dailyrjstatus.getStates_daily().get(size-21).getGa())
    };
    String array[] = new String[]{dailyrjstatus.getStates_daily().get(size - 3).getDate(),
            dailyrjstatus.getStates_daily().get(size - 6).getDate(),
            dailyrjstatus.getStates_daily().get(size - 9).getDate(),
            dailyrjstatus.getStates_daily().get(size - 12).getDate(),
            dailyrjstatus.getStates_daily().get(size - 15).getDate(),
            dailyrjstatus.getStates_daily().get(size - 18).getDate(),
            dailyrjstatus.getStates_daily().get(size - 21).getDate()};
    ChartEntity first = new ChartEntity(Color.WHITE, graph1);
    ArrayList<ChartEntity> list = new ArrayList<>();
    list.add(first);
    chart.setLegend(Arrays.asList(array));
    chart.setList(list);



}

else if(statecode.equalsIgnoreCase("gj"))
{

    float afectedcasep[]=  new float[]{affected1-Float.parseFloat(dailyrjstatus.getStates_daily().get(size-6).getGj()),
            affected1-Float.parseFloat(dailyrjstatus.getStates_daily().get(size-9).getGj())-Float.parseFloat(dailyrjstatus.getStates_daily().get(size-6).getGj()),
            affected1-Float.parseFloat(dailyrjstatus.getStates_daily().get(size-12).getGj())-Float.parseFloat(dailyrjstatus.getStates_daily().get(size-9).getGj())-Float.parseFloat(dailyrjstatus.getStates_daily().get(size-6).getGj()),
            affected1-Float.parseFloat(dailyrjstatus.getStates_daily().get(size-15).getGj())-Float.parseFloat(dailyrjstatus.getStates_daily().get(size-12).getGj())-Float.parseFloat(dailyrjstatus.getStates_daily().get(size-9).getGj())-Float.parseFloat(dailyrjstatus.getStates_daily().get(size-6).getGj()),
            affected1-Float.parseFloat(dailyrjstatus.getStates_daily().get(size-18).getGj())-Float.parseFloat(dailyrjstatus.getStates_daily().get(size-15).getGj())-Float.parseFloat(dailyrjstatus.getStates_daily().get(size-12).getGj())-Float.parseFloat(dailyrjstatus.getStates_daily().get(size-9).getGj())-Float.parseFloat(dailyrjstatus.getStates_daily().get(size-6).getGj()),
            affected1-Float.parseFloat(dailyrjstatus.getStates_daily().get(size-21).getGj())-Float.parseFloat(dailyrjstatus.getStates_daily().get(size-18).getGj())-Float.parseFloat(dailyrjstatus.getStates_daily().get(size-15).getGj())-Float.parseFloat(dailyrjstatus.getStates_daily().get(size-12).getGj())-Float.parseFloat(dailyrjstatus.getStates_daily().get(size-9).getGj())-Float.parseFloat(dailyrjstatus.getStates_daily().get(size-6).getGj())

    };


    affectedGraphView.setData(afectedcasep);
    float deceased[]=  new float[]{dead1-Float.parseFloat(dailyrjstatus.getStates_daily().get(size-4).getGj()),
            dead1-Float.parseFloat(dailyrjstatus.getStates_daily().get(size-7).getGj())-Float.parseFloat(dailyrjstatus.getStates_daily().get(size-4).getGj()),
            dead1-Float.parseFloat(dailyrjstatus.getStates_daily().get(size-10).getGj())-Float.parseFloat(dailyrjstatus.getStates_daily().get(size-7).getGj())-Float.parseFloat(dailyrjstatus.getStates_daily().get(size-4).getGj()),
            dead1-Float.parseFloat(dailyrjstatus.getStates_daily().get(size-13).getGj())-Float.parseFloat(dailyrjstatus.getStates_daily().get(size-10).getGj())-Float.parseFloat(dailyrjstatus.getStates_daily().get(size-7).getGj())-Float.parseFloat(dailyrjstatus.getStates_daily().get(size-4).getGj()),
            dead1-Float.parseFloat(dailyrjstatus.getStates_daily().get(size-16).getGj())-Float.parseFloat(dailyrjstatus.getStates_daily().get(size-13).getGj())-Float.parseFloat(dailyrjstatus.getStates_daily().get(size-10).getGj())-Float.parseFloat(dailyrjstatus.getStates_daily().get(size-7).getGj())-Float.parseFloat(dailyrjstatus.getStates_daily().get(size-4).getGj()),
            dead1-Float.parseFloat(dailyrjstatus.getStates_daily().get(size-19).getGj())-Float.parseFloat(dailyrjstatus.getStates_daily().get(size-16).getGj())-Float.parseFloat(dailyrjstatus.getStates_daily().get(size-13).getGj())-Float.parseFloat(dailyrjstatus.getStates_daily().get(size-10).getGj())-Float.parseFloat(dailyrjstatus.getStates_daily().get(size-7).getGj())-Float.parseFloat(dailyrjstatus.getStates_daily().get(size-4).getGj())

    };


    deadgraph.setData(deceased);
    float recov[]=  new float[]{recovered1-Float.parseFloat(dailyrjstatus.getStates_daily().get(size-5).getGj()),
            recovered1-Float.parseFloat(dailyrjstatus.getStates_daily().get(size-8).getGj())-Float.parseFloat(dailyrjstatus.getStates_daily().get(size-5).getGj()),
            recovered1-Float.parseFloat(dailyrjstatus.getStates_daily().get(size-11).getGj())-Float.parseFloat(dailyrjstatus.getStates_daily().get(size-8).getGj())-Float.parseFloat(dailyrjstatus.getStates_daily().get(size-5).getGj()),
            recovered1-Float.parseFloat(dailyrjstatus.getStates_daily().get(size-14).getGj())-Float.parseFloat(dailyrjstatus.getStates_daily().get(size-11).getGj())-Float.parseFloat(dailyrjstatus.getStates_daily().get(size-8).getGj())-Float.parseFloat(dailyrjstatus.getStates_daily().get(size-5).getGj()),
            recovered1-Float.parseFloat(dailyrjstatus.getStates_daily().get(size-17).getGj())-Float.parseFloat(dailyrjstatus.getStates_daily().get(size-14).getGj())-Float.parseFloat(dailyrjstatus.getStates_daily().get(size-11).getGj())-Float.parseFloat(dailyrjstatus.getStates_daily().get(size-8).getGj())-Float.parseFloat(dailyrjstatus.getStates_daily().get(size-5).getGj()),
            recovered1-Float.parseFloat(dailyrjstatus.getStates_daily().get(size-20).getGj())-Float.parseFloat(dailyrjstatus.getStates_daily().get(size-17).getGj())-Float.parseFloat(dailyrjstatus.getStates_daily().get(size-14).getGj())-Float.parseFloat(dailyrjstatus.getStates_daily().get(size-11).getGj())-Float.parseFloat(dailyrjstatus.getStates_daily().get(size-8).getGj())-Float.parseFloat(dailyrjstatus.getStates_daily().get(size-5).getGj())

    };
    recovgraph.setData(null);// for example
    recovgraph.setData(recov);
    float active[]=  new float[]{active1-Float.parseFloat(dailyrjstatus.getStates_daily().get(size-5).getGj())-Float.parseFloat(dailyrjstatus.getStates_daily().get(size-4).getGj())-Float.parseFloat(dailyrjstatus.getStates_daily().get(size-6).getGj()),
            active1-Float.parseFloat(dailyrjstatus.getStates_daily().get(size-8).getGj())-Float.parseFloat(dailyrjstatus.getStates_daily().get(size-5).getGj())-Float.parseFloat(dailyrjstatus.getStates_daily().get(size-7).getGj())-Float.parseFloat(dailyrjstatus.getStates_daily().get(size-4).getGj())-Float.parseFloat(dailyrjstatus.getStates_daily().get(size-8).getGj())-Float.parseFloat(dailyrjstatus.getStates_daily().get(size-5).getGj()),
            active1-Float.parseFloat(dailyrjstatus.getStates_daily().get(size-11).getGj())-Float.parseFloat(dailyrjstatus.getStates_daily().get(size-8).getGj())-Float.parseFloat(dailyrjstatus.getStates_daily().get(size-5).getGj())-Float.parseFloat(dailyrjstatus.getStates_daily().get(size-11).getGj())-Float.parseFloat(dailyrjstatus.getStates_daily().get(size-8).getGj())-Float.parseFloat(dailyrjstatus.getStates_daily().get(size-5).getGj())-Float.parseFloat(dailyrjstatus.getStates_daily().get(size-10).getGj())-Float.parseFloat(dailyrjstatus.getStates_daily().get(size-7).getGj())-Float.parseFloat(dailyrjstatus.getStates_daily().get(size-4).getGj()),

            active1-Float.parseFloat(dailyrjstatus.getStates_daily().get(size-14).getGj())-Float.parseFloat(dailyrjstatus.getStates_daily().get(size-11).getGj())-Float.parseFloat(dailyrjstatus.getStates_daily().get(size-8).getGj())-Float.parseFloat(dailyrjstatus.getStates_daily().get(size-5).getGj())-Float.parseFloat(dailyrjstatus.getStates_daily().get(size-14).getGj())-Float.parseFloat(dailyrjstatus.getStates_daily().get(size-11).getGj())-Float.parseFloat(dailyrjstatus.getStates_daily().get(size-8).getGj())-Float.parseFloat(dailyrjstatus.getStates_daily().get(size-5).getGj())-Float.parseFloat(dailyrjstatus.getStates_daily().get(size-13).getGj())-Float.parseFloat(dailyrjstatus.getStates_daily().get(size-10).getGj())-Float.parseFloat(dailyrjstatus.getStates_daily().get(size-7).getGj())-Float.parseFloat(dailyrjstatus.getStates_daily().get(size-4).getGj()),




    };

    activegraphview.setData(active);



//   data.add(new BarEntry(Float.parseFloat(dailyrjstatus.getStates_daily().get(size-3).getDate()),Float.parseFloat(dailyrjstatus.getStates_daily().get(size-3).getGj())));
//        data.add(new BarEntry(Float.parseFloat(dailyrjstatus.getStates_daily().get(size-6).getDate()),));
//        data.add(new BarEntry(Float.parseFloat(dailyrjstatus.getStates_daily().get(size-9).getDate()),Float.parseFloat(dailyrjstatus.getStates_daily().get(size-9).getGj())));
//        data.add(new BarEntry(Float.parseFloat(dailyrjstatus.getStates_daily().get(size-12).getDate()),Float.parseFloat(dailyrjstatus.getStates_daily().get(size-12).getGj())));
//        data.add(new BarEntry(Float.parseFloat(dailyrjstatus.getStates_daily().get(size-15).getDate()),Float.parseFloat(dailyrjstatus.getStates_daily().get(size-15).getGj())));
//        data.add(new BarEntry(Float.parseFloat(dailyrjstatus.getStates_daily().get(size-18).getDate()),Float.parseFloat(dailyrjstatus.getStates_daily().get(size-18).getGj())));
//        data.add(new BarEntry(Float.parseFloat(dailyrjstatus.getStates_daily().get(size-21).getDate()),Float.parseFloat(dailyrjstatus.getStates_daily().get(size-21).getGj())));
//        BarDataSet barDataSet=new BarDataSet(data,"Cases");
//        BarData barData=new BarData();
//        barData.addDataSet(barDataSet);
//        chart.setData(barData);
//        chart.invalidate();
    float graph1[]=  new float[]{Float.parseFloat(dailyrjstatus.getStates_daily().get(size-3).getGj()),
            Float.parseFloat(dailyrjstatus.getStates_daily().get(size-6).getGj()),
            Float.parseFloat(dailyrjstatus.getStates_daily().get(size-9).getGj()),
            Float.parseFloat(dailyrjstatus.getStates_daily().get(size-12).getGj()),
            Float.parseFloat(dailyrjstatus.getStates_daily().get(size-15).getGj()),
            Float.parseFloat(dailyrjstatus.getStates_daily().get(size-18).getGj()),
            Float.parseFloat(dailyrjstatus.getStates_daily().get(size-21).getGj())
    };
    String array[] = new String[]{dailyrjstatus.getStates_daily().get(size - 3).getDate(),
            dailyrjstatus.getStates_daily().get(size - 6).getDate(),
            dailyrjstatus.getStates_daily().get(size - 9).getDate(),
            dailyrjstatus.getStates_daily().get(size - 12).getDate(),
            dailyrjstatus.getStates_daily().get(size - 15).getDate(),
            dailyrjstatus.getStates_daily().get(size - 18).getDate(),
            dailyrjstatus.getStates_daily().get(size - 21).getDate()};
    ChartEntity first = new ChartEntity(Color.WHITE, graph1);
    ArrayList<ChartEntity> list = new ArrayList<>();
    list.add(first);
    chart.setLegend(Arrays.asList(array));
    chart.setList(list);



}

else if(statecode.equalsIgnoreCase("hp"))
{

    float afectedcasep[]=  new float[]{affected1-Float.parseFloat(dailyrjstatus.getStates_daily().get(size-6).getHp()),
            affected1-Float.parseFloat(dailyrjstatus.getStates_daily().get(size-9).getHp())-Float.parseFloat(dailyrjstatus.getStates_daily().get(size-6).getHp()),
            affected1-Float.parseFloat(dailyrjstatus.getStates_daily().get(size-12).getHp())-Float.parseFloat(dailyrjstatus.getStates_daily().get(size-9).getHp())-Float.parseFloat(dailyrjstatus.getStates_daily().get(size-6).getHp()),
            affected1-Float.parseFloat(dailyrjstatus.getStates_daily().get(size-15).getHp())-Float.parseFloat(dailyrjstatus.getStates_daily().get(size-12).getHp())-Float.parseFloat(dailyrjstatus.getStates_daily().get(size-9).getHp())-Float.parseFloat(dailyrjstatus.getStates_daily().get(size-6).getHp()),
            affected1-Float.parseFloat(dailyrjstatus.getStates_daily().get(size-18).getHp())-Float.parseFloat(dailyrjstatus.getStates_daily().get(size-15).getHp())-Float.parseFloat(dailyrjstatus.getStates_daily().get(size-12).getHp())-Float.parseFloat(dailyrjstatus.getStates_daily().get(size-9).getHp())-Float.parseFloat(dailyrjstatus.getStates_daily().get(size-6).getHp()),
            affected1-Float.parseFloat(dailyrjstatus.getStates_daily().get(size-21).getHp())-Float.parseFloat(dailyrjstatus.getStates_daily().get(size-18).getHp())-Float.parseFloat(dailyrjstatus.getStates_daily().get(size-15).getHp())-Float.parseFloat(dailyrjstatus.getStates_daily().get(size-12).getHp())-Float.parseFloat(dailyrjstatus.getStates_daily().get(size-9).getHp())-Float.parseFloat(dailyrjstatus.getStates_daily().get(size-6).getHp())

    };


    affectedGraphView.setData(afectedcasep);
    float deceased[]=  new float[]{dead1-Float.parseFloat(dailyrjstatus.getStates_daily().get(size-4).getHp()),
            dead1-Float.parseFloat(dailyrjstatus.getStates_daily().get(size-7).getHp())-Float.parseFloat(dailyrjstatus.getStates_daily().get(size-4).getHp()),
            dead1-Float.parseFloat(dailyrjstatus.getStates_daily().get(size-10).getHp())-Float.parseFloat(dailyrjstatus.getStates_daily().get(size-7).getHp())-Float.parseFloat(dailyrjstatus.getStates_daily().get(size-4).getHp()),
            dead1-Float.parseFloat(dailyrjstatus.getStates_daily().get(size-13).getHp())-Float.parseFloat(dailyrjstatus.getStates_daily().get(size-10).getHp())-Float.parseFloat(dailyrjstatus.getStates_daily().get(size-7).getHp())-Float.parseFloat(dailyrjstatus.getStates_daily().get(size-4).getHp()),
            dead1-Float.parseFloat(dailyrjstatus.getStates_daily().get(size-16).getHp())-Float.parseFloat(dailyrjstatus.getStates_daily().get(size-13).getHp())-Float.parseFloat(dailyrjstatus.getStates_daily().get(size-10).getHp())-Float.parseFloat(dailyrjstatus.getStates_daily().get(size-7).getHp())-Float.parseFloat(dailyrjstatus.getStates_daily().get(size-4).getHp()),
            dead1-Float.parseFloat(dailyrjstatus.getStates_daily().get(size-19).getHp())-Float.parseFloat(dailyrjstatus.getStates_daily().get(size-16).getHp())-Float.parseFloat(dailyrjstatus.getStates_daily().get(size-13).getHp())-Float.parseFloat(dailyrjstatus.getStates_daily().get(size-10).getHp())-Float.parseFloat(dailyrjstatus.getStates_daily().get(size-7).getHp())-Float.parseFloat(dailyrjstatus.getStates_daily().get(size-4).getHp())

    };


    deadgraph.setData(deceased);
    float recov[]=  new float[]{recovered1-Float.parseFloat(dailyrjstatus.getStates_daily().get(size-5).getHp()),
            recovered1-Float.parseFloat(dailyrjstatus.getStates_daily().get(size-8).getHp())-Float.parseFloat(dailyrjstatus.getStates_daily().get(size-5).getHp()),
            recovered1-Float.parseFloat(dailyrjstatus.getStates_daily().get(size-11).getHp())-Float.parseFloat(dailyrjstatus.getStates_daily().get(size-8).getHp())-Float.parseFloat(dailyrjstatus.getStates_daily().get(size-5).getHp()),
            recovered1-Float.parseFloat(dailyrjstatus.getStates_daily().get(size-14).getHp())-Float.parseFloat(dailyrjstatus.getStates_daily().get(size-11).getHp())-Float.parseFloat(dailyrjstatus.getStates_daily().get(size-8).getHp())-Float.parseFloat(dailyrjstatus.getStates_daily().get(size-5).getHp()),
            recovered1-Float.parseFloat(dailyrjstatus.getStates_daily().get(size-17).getHp())-Float.parseFloat(dailyrjstatus.getStates_daily().get(size-14).getHp())-Float.parseFloat(dailyrjstatus.getStates_daily().get(size-11).getHp())-Float.parseFloat(dailyrjstatus.getStates_daily().get(size-8).getHp())-Float.parseFloat(dailyrjstatus.getStates_daily().get(size-5).getHp()),
            recovered1-Float.parseFloat(dailyrjstatus.getStates_daily().get(size-20).getHp())-Float.parseFloat(dailyrjstatus.getStates_daily().get(size-17).getHp())-Float.parseFloat(dailyrjstatus.getStates_daily().get(size-14).getHp())-Float.parseFloat(dailyrjstatus.getStates_daily().get(size-11).getHp())-Float.parseFloat(dailyrjstatus.getStates_daily().get(size-8).getHp())-Float.parseFloat(dailyrjstatus.getStates_daily().get(size-5).getHp())

    };
    recovgraph.setData(null);// for example
    recovgraph.setData(recov);
    float active[]=  new float[]{active1-Float.parseFloat(dailyrjstatus.getStates_daily().get(size-5).getHp())-Float.parseFloat(dailyrjstatus.getStates_daily().get(size-4).getHp())-Float.parseFloat(dailyrjstatus.getStates_daily().get(size-6).getHp()),
            active1-Float.parseFloat(dailyrjstatus.getStates_daily().get(size-8).getHp())-Float.parseFloat(dailyrjstatus.getStates_daily().get(size-5).getHp())-Float.parseFloat(dailyrjstatus.getStates_daily().get(size-7).getHp())-Float.parseFloat(dailyrjstatus.getStates_daily().get(size-4).getHp())-Float.parseFloat(dailyrjstatus.getStates_daily().get(size-8).getHp())-Float.parseFloat(dailyrjstatus.getStates_daily().get(size-5).getHp()),
            active1-Float.parseFloat(dailyrjstatus.getStates_daily().get(size-11).getHp())-Float.parseFloat(dailyrjstatus.getStates_daily().get(size-8).getHp())-Float.parseFloat(dailyrjstatus.getStates_daily().get(size-5).getHp())-Float.parseFloat(dailyrjstatus.getStates_daily().get(size-11).getHp())-Float.parseFloat(dailyrjstatus.getStates_daily().get(size-8).getHp())-Float.parseFloat(dailyrjstatus.getStates_daily().get(size-5).getHp())-Float.parseFloat(dailyrjstatus.getStates_daily().get(size-10).getHp())-Float.parseFloat(dailyrjstatus.getStates_daily().get(size-7).getHp())-Float.parseFloat(dailyrjstatus.getStates_daily().get(size-4).getHp()),

            active1-Float.parseFloat(dailyrjstatus.getStates_daily().get(size-14).getHp())-Float.parseFloat(dailyrjstatus.getStates_daily().get(size-11).getHp())-Float.parseFloat(dailyrjstatus.getStates_daily().get(size-8).getHp())-Float.parseFloat(dailyrjstatus.getStates_daily().get(size-5).getHp())-Float.parseFloat(dailyrjstatus.getStates_daily().get(size-14).getHp())-Float.parseFloat(dailyrjstatus.getStates_daily().get(size-11).getHp())-Float.parseFloat(dailyrjstatus.getStates_daily().get(size-8).getHp())-Float.parseFloat(dailyrjstatus.getStates_daily().get(size-5).getHp())-Float.parseFloat(dailyrjstatus.getStates_daily().get(size-13).getHp())-Float.parseFloat(dailyrjstatus.getStates_daily().get(size-10).getHp())-Float.parseFloat(dailyrjstatus.getStates_daily().get(size-7).getHp())-Float.parseFloat(dailyrjstatus.getStates_daily().get(size-4).getHp()),




    };

    activegraphview.setData(active);



//   data.add(new BarEntry(Float.parseFloat(dailyrjstatus.getStates_daily().get(size-3).getDate()),Float.parseFloat(dailyrjstatus.getStates_daily().get(size-3).getHp())));
//        data.add(new BarEntry(Float.parseFloat(dailyrjstatus.getStates_daily().get(size-6).getDate()),));
//        data.add(new BarEntry(Float.parseFloat(dailyrjstatus.getStates_daily().get(size-9).getDate()),Float.parseFloat(dailyrjstatus.getStates_daily().get(size-9).getHp())));
//        data.add(new BarEntry(Float.parseFloat(dailyrjstatus.getStates_daily().get(size-12).getDate()),Float.parseFloat(dailyrjstatus.getStates_daily().get(size-12).getHp())));
//        data.add(new BarEntry(Float.parseFloat(dailyrjstatus.getStates_daily().get(size-15).getDate()),Float.parseFloat(dailyrjstatus.getStates_daily().get(size-15).getHp())));
//        data.add(new BarEntry(Float.parseFloat(dailyrjstatus.getStates_daily().get(size-18).getDate()),Float.parseFloat(dailyrjstatus.getStates_daily().get(size-18).getHp())));
//        data.add(new BarEntry(Float.parseFloat(dailyrjstatus.getStates_daily().get(size-21).getDate()),Float.parseFloat(dailyrjstatus.getStates_daily().get(size-21).getHp())));
//        BarDataSet barDataSet=new BarDataSet(data,"Cases");
//        BarData barData=new BarData();
//        barData.addDataSet(barDataSet);
//        chart.setData(barData);
//        chart.invalidate();
    float graph1[]=  new float[]{Float.parseFloat(dailyrjstatus.getStates_daily().get(size-3).getHp()),
            Float.parseFloat(dailyrjstatus.getStates_daily().get(size-6).getHp()),
            Float.parseFloat(dailyrjstatus.getStates_daily().get(size-9).getHp()),
            Float.parseFloat(dailyrjstatus.getStates_daily().get(size-12).getHp()),
            Float.parseFloat(dailyrjstatus.getStates_daily().get(size-15).getHp()),
            Float.parseFloat(dailyrjstatus.getStates_daily().get(size-18).getHp()),
            Float.parseFloat(dailyrjstatus.getStates_daily().get(size-21).getHp())
    };
    String array[] = new String[]{dailyrjstatus.getStates_daily().get(size - 3).getDate(),
            dailyrjstatus.getStates_daily().get(size - 6).getDate(),
            dailyrjstatus.getStates_daily().get(size - 9).getDate(),
            dailyrjstatus.getStates_daily().get(size - 12).getDate(),
            dailyrjstatus.getStates_daily().get(size - 15).getDate(),
            dailyrjstatus.getStates_daily().get(size - 18).getDate(),
            dailyrjstatus.getStates_daily().get(size - 21).getDate()};
    ChartEntity first = new ChartEntity(Color.WHITE, graph1);
    ArrayList<ChartEntity> list = new ArrayList<>();
    list.add(first);
    chart.setLegend(Arrays.asList(array));
    chart.setList(list);



}
else if(statecode.equalsIgnoreCase("hr"))
{

    float afectedcasep[]=  new float[]{affected1-Float.parseFloat(dailyrjstatus.getStates_daily().get(size-6).getHr()),
            affected1-Float.parseFloat(dailyrjstatus.getStates_daily().get(size-9).getHr())-Float.parseFloat(dailyrjstatus.getStates_daily().get(size-6).getHr()),
            affected1-Float.parseFloat(dailyrjstatus.getStates_daily().get(size-12).getHr())-Float.parseFloat(dailyrjstatus.getStates_daily().get(size-9).getHr())-Float.parseFloat(dailyrjstatus.getStates_daily().get(size-6).getHr()),
            affected1-Float.parseFloat(dailyrjstatus.getStates_daily().get(size-15).getHr())-Float.parseFloat(dailyrjstatus.getStates_daily().get(size-12).getHr())-Float.parseFloat(dailyrjstatus.getStates_daily().get(size-9).getHr())-Float.parseFloat(dailyrjstatus.getStates_daily().get(size-6).getHr()),
            affected1-Float.parseFloat(dailyrjstatus.getStates_daily().get(size-18).getHr())-Float.parseFloat(dailyrjstatus.getStates_daily().get(size-15).getHr())-Float.parseFloat(dailyrjstatus.getStates_daily().get(size-12).getHr())-Float.parseFloat(dailyrjstatus.getStates_daily().get(size-9).getHr())-Float.parseFloat(dailyrjstatus.getStates_daily().get(size-6).getHr()),
            affected1-Float.parseFloat(dailyrjstatus.getStates_daily().get(size-21).getHr())-Float.parseFloat(dailyrjstatus.getStates_daily().get(size-18).getHr())-Float.parseFloat(dailyrjstatus.getStates_daily().get(size-15).getHr())-Float.parseFloat(dailyrjstatus.getStates_daily().get(size-12).getHr())-Float.parseFloat(dailyrjstatus.getStates_daily().get(size-9).getHr())-Float.parseFloat(dailyrjstatus.getStates_daily().get(size-6).getHr())

    };


    affectedGraphView.setData(afectedcasep);
    float deceased[]=  new float[]{dead1-Float.parseFloat(dailyrjstatus.getStates_daily().get(size-4).getHr()),
            dead1-Float.parseFloat(dailyrjstatus.getStates_daily().get(size-7).getHr())-Float.parseFloat(dailyrjstatus.getStates_daily().get(size-4).getHr()),
            dead1-Float.parseFloat(dailyrjstatus.getStates_daily().get(size-10).getHr())-Float.parseFloat(dailyrjstatus.getStates_daily().get(size-7).getHr())-Float.parseFloat(dailyrjstatus.getStates_daily().get(size-4).getHr()),
            dead1-Float.parseFloat(dailyrjstatus.getStates_daily().get(size-13).getHr())-Float.parseFloat(dailyrjstatus.getStates_daily().get(size-10).getHr())-Float.parseFloat(dailyrjstatus.getStates_daily().get(size-7).getHr())-Float.parseFloat(dailyrjstatus.getStates_daily().get(size-4).getHr()),
            dead1-Float.parseFloat(dailyrjstatus.getStates_daily().get(size-16).getHr())-Float.parseFloat(dailyrjstatus.getStates_daily().get(size-13).getHr())-Float.parseFloat(dailyrjstatus.getStates_daily().get(size-10).getHr())-Float.parseFloat(dailyrjstatus.getStates_daily().get(size-7).getHr())-Float.parseFloat(dailyrjstatus.getStates_daily().get(size-4).getHr()),
            dead1-Float.parseFloat(dailyrjstatus.getStates_daily().get(size-19).getHr())-Float.parseFloat(dailyrjstatus.getStates_daily().get(size-16).getHr())-Float.parseFloat(dailyrjstatus.getStates_daily().get(size-13).getHr())-Float.parseFloat(dailyrjstatus.getStates_daily().get(size-10).getHr())-Float.parseFloat(dailyrjstatus.getStates_daily().get(size-7).getHr())-Float.parseFloat(dailyrjstatus.getStates_daily().get(size-4).getHr())

    };


    deadgraph.setData(deceased);
    float recov[]=  new float[]{recovered1-Float.parseFloat(dailyrjstatus.getStates_daily().get(size-5).getHr()),
            recovered1-Float.parseFloat(dailyrjstatus.getStates_daily().get(size-8).getHr())-Float.parseFloat(dailyrjstatus.getStates_daily().get(size-5).getHr()),
            recovered1-Float.parseFloat(dailyrjstatus.getStates_daily().get(size-11).getHr())-Float.parseFloat(dailyrjstatus.getStates_daily().get(size-8).getHr())-Float.parseFloat(dailyrjstatus.getStates_daily().get(size-5).getHr()),
            recovered1-Float.parseFloat(dailyrjstatus.getStates_daily().get(size-14).getHr())-Float.parseFloat(dailyrjstatus.getStates_daily().get(size-11).getHr())-Float.parseFloat(dailyrjstatus.getStates_daily().get(size-8).getHr())-Float.parseFloat(dailyrjstatus.getStates_daily().get(size-5).getHr()),
            recovered1-Float.parseFloat(dailyrjstatus.getStates_daily().get(size-17).getHr())-Float.parseFloat(dailyrjstatus.getStates_daily().get(size-14).getHr())-Float.parseFloat(dailyrjstatus.getStates_daily().get(size-11).getHr())-Float.parseFloat(dailyrjstatus.getStates_daily().get(size-8).getHr())-Float.parseFloat(dailyrjstatus.getStates_daily().get(size-5).getHr()),
            recovered1-Float.parseFloat(dailyrjstatus.getStates_daily().get(size-20).getHr())-Float.parseFloat(dailyrjstatus.getStates_daily().get(size-17).getHr())-Float.parseFloat(dailyrjstatus.getStates_daily().get(size-14).getHr())-Float.parseFloat(dailyrjstatus.getStates_daily().get(size-11).getHr())-Float.parseFloat(dailyrjstatus.getStates_daily().get(size-8).getHr())-Float.parseFloat(dailyrjstatus.getStates_daily().get(size-5).getHr())

    };
    recovgraph.setData(null);// for example
    recovgraph.setData(recov);
    float active[]=  new float[]{active1-Float.parseFloat(dailyrjstatus.getStates_daily().get(size-5).getHr())-Float.parseFloat(dailyrjstatus.getStates_daily().get(size-4).getHr())-Float.parseFloat(dailyrjstatus.getStates_daily().get(size-6).getHr()),
            active1-Float.parseFloat(dailyrjstatus.getStates_daily().get(size-8).getHr())-Float.parseFloat(dailyrjstatus.getStates_daily().get(size-5).getHr())-Float.parseFloat(dailyrjstatus.getStates_daily().get(size-7).getHr())-Float.parseFloat(dailyrjstatus.getStates_daily().get(size-4).getHr())-Float.parseFloat(dailyrjstatus.getStates_daily().get(size-8).getHr())-Float.parseFloat(dailyrjstatus.getStates_daily().get(size-5).getHr()),
            active1-Float.parseFloat(dailyrjstatus.getStates_daily().get(size-11).getHr())-Float.parseFloat(dailyrjstatus.getStates_daily().get(size-8).getHr())-Float.parseFloat(dailyrjstatus.getStates_daily().get(size-5).getHr())-Float.parseFloat(dailyrjstatus.getStates_daily().get(size-11).getHr())-Float.parseFloat(dailyrjstatus.getStates_daily().get(size-8).getHr())-Float.parseFloat(dailyrjstatus.getStates_daily().get(size-5).getHr())-Float.parseFloat(dailyrjstatus.getStates_daily().get(size-10).getHr())-Float.parseFloat(dailyrjstatus.getStates_daily().get(size-7).getHr())-Float.parseFloat(dailyrjstatus.getStates_daily().get(size-4).getHr()),

            active1-Float.parseFloat(dailyrjstatus.getStates_daily().get(size-14).getHr())-Float.parseFloat(dailyrjstatus.getStates_daily().get(size-11).getHr())-Float.parseFloat(dailyrjstatus.getStates_daily().get(size-8).getHr())-Float.parseFloat(dailyrjstatus.getStates_daily().get(size-5).getHr())-Float.parseFloat(dailyrjstatus.getStates_daily().get(size-14).getHr())-Float.parseFloat(dailyrjstatus.getStates_daily().get(size-11).getHr())-Float.parseFloat(dailyrjstatus.getStates_daily().get(size-8).getHr())-Float.parseFloat(dailyrjstatus.getStates_daily().get(size-5).getHr())-Float.parseFloat(dailyrjstatus.getStates_daily().get(size-13).getHr())-Float.parseFloat(dailyrjstatus.getStates_daily().get(size-10).getHr())-Float.parseFloat(dailyrjstatus.getStates_daily().get(size-7).getHr())-Float.parseFloat(dailyrjstatus.getStates_daily().get(size-4).getHr()),




    };

    activegraphview.setData(active);



//   data.add(new BarEntry(Float.parseFloat(dailyrjstatus.getStates_daily().get(size-3).getDate()),Float.parseFloat(dailyrjstatus.getStates_daily().get(size-3).getHr())));
//        data.add(new BarEntry(Float.parseFloat(dailyrjstatus.getStates_daily().get(size-6).getDate()),));
//        data.add(new BarEntry(Float.parseFloat(dailyrjstatus.getStates_daily().get(size-9).getDate()),Float.parseFloat(dailyrjstatus.getStates_daily().get(size-9).getHr())));
//        data.add(new BarEntry(Float.parseFloat(dailyrjstatus.getStates_daily().get(size-12).getDate()),Float.parseFloat(dailyrjstatus.getStates_daily().get(size-12).getHr())));
//        data.add(new BarEntry(Float.parseFloat(dailyrjstatus.getStates_daily().get(size-15).getDate()),Float.parseFloat(dailyrjstatus.getStates_daily().get(size-15).getHr())));
//        data.add(new BarEntry(Float.parseFloat(dailyrjstatus.getStates_daily().get(size-18).getDate()),Float.parseFloat(dailyrjstatus.getStates_daily().get(size-18).getHr())));
//        data.add(new BarEntry(Float.parseFloat(dailyrjstatus.getStates_daily().get(size-21).getDate()),Float.parseFloat(dailyrjstatus.getStates_daily().get(size-21).getHr())));
//        BarDataSet barDataSet=new BarDataSet(data,"Cases");
//        BarData barData=new BarData();
//        barData.addDataSet(barDataSet);
//        chart.setData(barData);
//        chart.invalidate();
    float graph1[]=  new float[]{Float.parseFloat(dailyrjstatus.getStates_daily().get(size-3).getHr()),
            Float.parseFloat(dailyrjstatus.getStates_daily().get(size-6).getHr()),
            Float.parseFloat(dailyrjstatus.getStates_daily().get(size-9).getHr()),
            Float.parseFloat(dailyrjstatus.getStates_daily().get(size-12).getHr()),
            Float.parseFloat(dailyrjstatus.getStates_daily().get(size-15).getHr()),
            Float.parseFloat(dailyrjstatus.getStates_daily().get(size-18).getHr()),
            Float.parseFloat(dailyrjstatus.getStates_daily().get(size-21).getHr())
    };
    String array[] = new String[]{dailyrjstatus.getStates_daily().get(size - 3).getDate(),
            dailyrjstatus.getStates_daily().get(size - 6).getDate(),
            dailyrjstatus.getStates_daily().get(size - 9).getDate(),
            dailyrjstatus.getStates_daily().get(size - 12).getDate(),
            dailyrjstatus.getStates_daily().get(size - 15).getDate(),
            dailyrjstatus.getStates_daily().get(size - 18).getDate(),
            dailyrjstatus.getStates_daily().get(size - 21).getDate()};
    ChartEntity first = new ChartEntity(Color.WHITE, graph1);
    ArrayList<ChartEntity> list = new ArrayList<>();
    list.add(first);
    chart.setLegend(Arrays.asList(array));
    chart.setList(list);



}

else if(statecode.equalsIgnoreCase("jh"))
{

    float afectedcasep[]=  new float[]{affected1-Float.parseFloat(dailyrjstatus.getStates_daily().get(size-6).getJh()),
            affected1-Float.parseFloat(dailyrjstatus.getStates_daily().get(size-9).getJh())-Float.parseFloat(dailyrjstatus.getStates_daily().get(size-6).getJh()),
            affected1-Float.parseFloat(dailyrjstatus.getStates_daily().get(size-12).getJh())-Float.parseFloat(dailyrjstatus.getStates_daily().get(size-9).getJh())-Float.parseFloat(dailyrjstatus.getStates_daily().get(size-6).getJh()),
            affected1-Float.parseFloat(dailyrjstatus.getStates_daily().get(size-15).getJh())-Float.parseFloat(dailyrjstatus.getStates_daily().get(size-12).getJh())-Float.parseFloat(dailyrjstatus.getStates_daily().get(size-9).getJh())-Float.parseFloat(dailyrjstatus.getStates_daily().get(size-6).getJh()),
            affected1-Float.parseFloat(dailyrjstatus.getStates_daily().get(size-18).getJh())-Float.parseFloat(dailyrjstatus.getStates_daily().get(size-15).getJh())-Float.parseFloat(dailyrjstatus.getStates_daily().get(size-12).getJh())-Float.parseFloat(dailyrjstatus.getStates_daily().get(size-9).getJh())-Float.parseFloat(dailyrjstatus.getStates_daily().get(size-6).getJh()),
            affected1-Float.parseFloat(dailyrjstatus.getStates_daily().get(size-21).getJh())-Float.parseFloat(dailyrjstatus.getStates_daily().get(size-18).getJh())-Float.parseFloat(dailyrjstatus.getStates_daily().get(size-15).getJh())-Float.parseFloat(dailyrjstatus.getStates_daily().get(size-12).getJh())-Float.parseFloat(dailyrjstatus.getStates_daily().get(size-9).getJh())-Float.parseFloat(dailyrjstatus.getStates_daily().get(size-6).getJh())

    };


    affectedGraphView.setData(afectedcasep);
    float deceased[]=  new float[]{dead1-Float.parseFloat(dailyrjstatus.getStates_daily().get(size-4).getJh()),
            dead1-Float.parseFloat(dailyrjstatus.getStates_daily().get(size-7).getJh())-Float.parseFloat(dailyrjstatus.getStates_daily().get(size-4).getJh()),
            dead1-Float.parseFloat(dailyrjstatus.getStates_daily().get(size-10).getJh())-Float.parseFloat(dailyrjstatus.getStates_daily().get(size-7).getJh())-Float.parseFloat(dailyrjstatus.getStates_daily().get(size-4).getJh()),
            dead1-Float.parseFloat(dailyrjstatus.getStates_daily().get(size-13).getJh())-Float.parseFloat(dailyrjstatus.getStates_daily().get(size-10).getJh())-Float.parseFloat(dailyrjstatus.getStates_daily().get(size-7).getJh())-Float.parseFloat(dailyrjstatus.getStates_daily().get(size-4).getJh()),
            dead1-Float.parseFloat(dailyrjstatus.getStates_daily().get(size-16).getJh())-Float.parseFloat(dailyrjstatus.getStates_daily().get(size-13).getJh())-Float.parseFloat(dailyrjstatus.getStates_daily().get(size-10).getJh())-Float.parseFloat(dailyrjstatus.getStates_daily().get(size-7).getJh())-Float.parseFloat(dailyrjstatus.getStates_daily().get(size-4).getJh()),
            dead1-Float.parseFloat(dailyrjstatus.getStates_daily().get(size-19).getJh())-Float.parseFloat(dailyrjstatus.getStates_daily().get(size-16).getJh())-Float.parseFloat(dailyrjstatus.getStates_daily().get(size-13).getJh())-Float.parseFloat(dailyrjstatus.getStates_daily().get(size-10).getJh())-Float.parseFloat(dailyrjstatus.getStates_daily().get(size-7).getJh())-Float.parseFloat(dailyrjstatus.getStates_daily().get(size-4).getJh())

    };


    deadgraph.setData(deceased);
    float recov[]=  new float[]{recovered1-Float.parseFloat(dailyrjstatus.getStates_daily().get(size-5).getJh()),
            recovered1-Float.parseFloat(dailyrjstatus.getStates_daily().get(size-8).getJh())-Float.parseFloat(dailyrjstatus.getStates_daily().get(size-5).getJh()),
            recovered1-Float.parseFloat(dailyrjstatus.getStates_daily().get(size-11).getJh())-Float.parseFloat(dailyrjstatus.getStates_daily().get(size-8).getJh())-Float.parseFloat(dailyrjstatus.getStates_daily().get(size-5).getJh()),
            recovered1-Float.parseFloat(dailyrjstatus.getStates_daily().get(size-14).getJh())-Float.parseFloat(dailyrjstatus.getStates_daily().get(size-11).getJh())-Float.parseFloat(dailyrjstatus.getStates_daily().get(size-8).getJh())-Float.parseFloat(dailyrjstatus.getStates_daily().get(size-5).getJh()),
            recovered1-Float.parseFloat(dailyrjstatus.getStates_daily().get(size-17).getJh())-Float.parseFloat(dailyrjstatus.getStates_daily().get(size-14).getJh())-Float.parseFloat(dailyrjstatus.getStates_daily().get(size-11).getJh())-Float.parseFloat(dailyrjstatus.getStates_daily().get(size-8).getJh())-Float.parseFloat(dailyrjstatus.getStates_daily().get(size-5).getJh()),
            recovered1-Float.parseFloat(dailyrjstatus.getStates_daily().get(size-20).getJh())-Float.parseFloat(dailyrjstatus.getStates_daily().get(size-17).getJh())-Float.parseFloat(dailyrjstatus.getStates_daily().get(size-14).getJh())-Float.parseFloat(dailyrjstatus.getStates_daily().get(size-11).getJh())-Float.parseFloat(dailyrjstatus.getStates_daily().get(size-8).getJh())-Float.parseFloat(dailyrjstatus.getStates_daily().get(size-5).getJh())

    };
    recovgraph.setData(null);// for example
    recovgraph.setData(recov);
    float active[]=  new float[]{active1-Float.parseFloat(dailyrjstatus.getStates_daily().get(size-5).getJh())-Float.parseFloat(dailyrjstatus.getStates_daily().get(size-4).getJh())-Float.parseFloat(dailyrjstatus.getStates_daily().get(size-6).getJh()),
            active1-Float.parseFloat(dailyrjstatus.getStates_daily().get(size-8).getJh())-Float.parseFloat(dailyrjstatus.getStates_daily().get(size-5).getJh())-Float.parseFloat(dailyrjstatus.getStates_daily().get(size-7).getJh())-Float.parseFloat(dailyrjstatus.getStates_daily().get(size-4).getJh())-Float.parseFloat(dailyrjstatus.getStates_daily().get(size-8).getJh())-Float.parseFloat(dailyrjstatus.getStates_daily().get(size-5).getJh()),
            active1-Float.parseFloat(dailyrjstatus.getStates_daily().get(size-11).getJh())-Float.parseFloat(dailyrjstatus.getStates_daily().get(size-8).getJh())-Float.parseFloat(dailyrjstatus.getStates_daily().get(size-5).getJh())-Float.parseFloat(dailyrjstatus.getStates_daily().get(size-11).getJh())-Float.parseFloat(dailyrjstatus.getStates_daily().get(size-8).getJh())-Float.parseFloat(dailyrjstatus.getStates_daily().get(size-5).getJh())-Float.parseFloat(dailyrjstatus.getStates_daily().get(size-10).getJh())-Float.parseFloat(dailyrjstatus.getStates_daily().get(size-7).getJh())-Float.parseFloat(dailyrjstatus.getStates_daily().get(size-4).getJh()),

            active1-Float.parseFloat(dailyrjstatus.getStates_daily().get(size-14).getJh())-Float.parseFloat(dailyrjstatus.getStates_daily().get(size-11).getJh())-Float.parseFloat(dailyrjstatus.getStates_daily().get(size-8).getJh())-Float.parseFloat(dailyrjstatus.getStates_daily().get(size-5).getJh())-Float.parseFloat(dailyrjstatus.getStates_daily().get(size-14).getJh())-Float.parseFloat(dailyrjstatus.getStates_daily().get(size-11).getJh())-Float.parseFloat(dailyrjstatus.getStates_daily().get(size-8).getJh())-Float.parseFloat(dailyrjstatus.getStates_daily().get(size-5).getJh())-Float.parseFloat(dailyrjstatus.getStates_daily().get(size-13).getJh())-Float.parseFloat(dailyrjstatus.getStates_daily().get(size-10).getJh())-Float.parseFloat(dailyrjstatus.getStates_daily().get(size-7).getJh())-Float.parseFloat(dailyrjstatus.getStates_daily().get(size-4).getJh()),




    };

    activegraphview.setData(active);



//   data.add(new BarEntry(Float.parseFloat(dailyrjstatus.getStates_daily().get(size-3).getDate()),Float.parseFloat(dailyrjstatus.getStates_daily().get(size-3).getJh())));
//        data.add(new BarEntry(Float.parseFloat(dailyrjstatus.getStates_daily().get(size-6).getDate()),));
//        data.add(new BarEntry(Float.parseFloat(dailyrjstatus.getStates_daily().get(size-9).getDate()),Float.parseFloat(dailyrjstatus.getStates_daily().get(size-9).getJh())));
//        data.add(new BarEntry(Float.parseFloat(dailyrjstatus.getStates_daily().get(size-12).getDate()),Float.parseFloat(dailyrjstatus.getStates_daily().get(size-12).getJh())));
//        data.add(new BarEntry(Float.parseFloat(dailyrjstatus.getStates_daily().get(size-15).getDate()),Float.parseFloat(dailyrjstatus.getStates_daily().get(size-15).getJh())));
//        data.add(new BarEntry(Float.parseFloat(dailyrjstatus.getStates_daily().get(size-18).getDate()),Float.parseFloat(dailyrjstatus.getStates_daily().get(size-18).getJh())));
//        data.add(new BarEntry(Float.parseFloat(dailyrjstatus.getStates_daily().get(size-21).getDate()),Float.parseFloat(dailyrjstatus.getStates_daily().get(size-21).getJh())));
//        BarDataSet barDataSet=new BarDataSet(data,"Cases");
//        BarData barData=new BarData();
//        barData.addDataSet(barDataSet);
//        chart.setData(barData);
//        chart.invalidate();
    float graph1[]=  new float[]{Float.parseFloat(dailyrjstatus.getStates_daily().get(size-3).getJh()),
            Float.parseFloat(dailyrjstatus.getStates_daily().get(size-6).getJh()),
            Float.parseFloat(dailyrjstatus.getStates_daily().get(size-9).getJh()),
            Float.parseFloat(dailyrjstatus.getStates_daily().get(size-12).getJh()),
            Float.parseFloat(dailyrjstatus.getStates_daily().get(size-15).getJh()),
            Float.parseFloat(dailyrjstatus.getStates_daily().get(size-18).getJh()),
            Float.parseFloat(dailyrjstatus.getStates_daily().get(size-21).getJh())
    };
    String array[] = new String[]{dailyrjstatus.getStates_daily().get(size - 3).getDate(),
            dailyrjstatus.getStates_daily().get(size - 6).getDate(),
            dailyrjstatus.getStates_daily().get(size - 9).getDate(),
            dailyrjstatus.getStates_daily().get(size - 12).getDate(),
            dailyrjstatus.getStates_daily().get(size - 15).getDate(),
            dailyrjstatus.getStates_daily().get(size - 18).getDate(),
            dailyrjstatus.getStates_daily().get(size - 21).getDate()};
    ChartEntity first = new ChartEntity(Color.WHITE, graph1);
    ArrayList<ChartEntity> list = new ArrayList<>();
    list.add(first);
    chart.setLegend(Arrays.asList(array));
    chart.setList(list);



}
else if(statecode.equalsIgnoreCase("jk"))
{

    float afectedcasep[]=  new float[]{affected1-Float.parseFloat(dailyrjstatus.getStates_daily().get(size-6).getJk()),
            affected1-Float.parseFloat(dailyrjstatus.getStates_daily().get(size-9).getJk())-Float.parseFloat(dailyrjstatus.getStates_daily().get(size-6).getJk()),
            affected1-Float.parseFloat(dailyrjstatus.getStates_daily().get(size-12).getJk())-Float.parseFloat(dailyrjstatus.getStates_daily().get(size-9).getJk())-Float.parseFloat(dailyrjstatus.getStates_daily().get(size-6).getJk()),
            affected1-Float.parseFloat(dailyrjstatus.getStates_daily().get(size-15).getJk())-Float.parseFloat(dailyrjstatus.getStates_daily().get(size-12).getJk())-Float.parseFloat(dailyrjstatus.getStates_daily().get(size-9).getJk())-Float.parseFloat(dailyrjstatus.getStates_daily().get(size-6).getJk()),
            affected1-Float.parseFloat(dailyrjstatus.getStates_daily().get(size-18).getJk())-Float.parseFloat(dailyrjstatus.getStates_daily().get(size-15).getJk())-Float.parseFloat(dailyrjstatus.getStates_daily().get(size-12).getJk())-Float.parseFloat(dailyrjstatus.getStates_daily().get(size-9).getJk())-Float.parseFloat(dailyrjstatus.getStates_daily().get(size-6).getJk()),
            affected1-Float.parseFloat(dailyrjstatus.getStates_daily().get(size-21).getJk())-Float.parseFloat(dailyrjstatus.getStates_daily().get(size-18).getJk())-Float.parseFloat(dailyrjstatus.getStates_daily().get(size-15).getJk())-Float.parseFloat(dailyrjstatus.getStates_daily().get(size-12).getJk())-Float.parseFloat(dailyrjstatus.getStates_daily().get(size-9).getJk())-Float.parseFloat(dailyrjstatus.getStates_daily().get(size-6).getJk())

    };


    affectedGraphView.setData(afectedcasep);
    float deceased[]=  new float[]{dead1-Float.parseFloat(dailyrjstatus.getStates_daily().get(size-4).getJk()),
            dead1-Float.parseFloat(dailyrjstatus.getStates_daily().get(size-7).getJk())-Float.parseFloat(dailyrjstatus.getStates_daily().get(size-4).getJk()),
            dead1-Float.parseFloat(dailyrjstatus.getStates_daily().get(size-10).getJk())-Float.parseFloat(dailyrjstatus.getStates_daily().get(size-7).getJk())-Float.parseFloat(dailyrjstatus.getStates_daily().get(size-4).getJk()),
            dead1-Float.parseFloat(dailyrjstatus.getStates_daily().get(size-13).getJk())-Float.parseFloat(dailyrjstatus.getStates_daily().get(size-10).getJk())-Float.parseFloat(dailyrjstatus.getStates_daily().get(size-7).getJk())-Float.parseFloat(dailyrjstatus.getStates_daily().get(size-4).getJk()),
            dead1-Float.parseFloat(dailyrjstatus.getStates_daily().get(size-16).getJk())-Float.parseFloat(dailyrjstatus.getStates_daily().get(size-13).getJk())-Float.parseFloat(dailyrjstatus.getStates_daily().get(size-10).getJk())-Float.parseFloat(dailyrjstatus.getStates_daily().get(size-7).getJk())-Float.parseFloat(dailyrjstatus.getStates_daily().get(size-4).getJk()),
            dead1-Float.parseFloat(dailyrjstatus.getStates_daily().get(size-19).getJk())-Float.parseFloat(dailyrjstatus.getStates_daily().get(size-16).getJk())-Float.parseFloat(dailyrjstatus.getStates_daily().get(size-13).getJk())-Float.parseFloat(dailyrjstatus.getStates_daily().get(size-10).getJk())-Float.parseFloat(dailyrjstatus.getStates_daily().get(size-7).getJk())-Float.parseFloat(dailyrjstatus.getStates_daily().get(size-4).getJk())

    };


    deadgraph.setData(deceased);
    float recov[]=  new float[]{recovered1-Float.parseFloat(dailyrjstatus.getStates_daily().get(size-5).getJk()),
            recovered1-Float.parseFloat(dailyrjstatus.getStates_daily().get(size-8).getJk())-Float.parseFloat(dailyrjstatus.getStates_daily().get(size-5).getJk()),
            recovered1-Float.parseFloat(dailyrjstatus.getStates_daily().get(size-11).getJk())-Float.parseFloat(dailyrjstatus.getStates_daily().get(size-8).getJk())-Float.parseFloat(dailyrjstatus.getStates_daily().get(size-5).getJk()),
            recovered1-Float.parseFloat(dailyrjstatus.getStates_daily().get(size-14).getJk())-Float.parseFloat(dailyrjstatus.getStates_daily().get(size-11).getJk())-Float.parseFloat(dailyrjstatus.getStates_daily().get(size-8).getJk())-Float.parseFloat(dailyrjstatus.getStates_daily().get(size-5).getJk()),
            recovered1-Float.parseFloat(dailyrjstatus.getStates_daily().get(size-17).getJk())-Float.parseFloat(dailyrjstatus.getStates_daily().get(size-14).getJk())-Float.parseFloat(dailyrjstatus.getStates_daily().get(size-11).getJk())-Float.parseFloat(dailyrjstatus.getStates_daily().get(size-8).getJk())-Float.parseFloat(dailyrjstatus.getStates_daily().get(size-5).getJk()),
            recovered1-Float.parseFloat(dailyrjstatus.getStates_daily().get(size-20).getJk())-Float.parseFloat(dailyrjstatus.getStates_daily().get(size-17).getJk())-Float.parseFloat(dailyrjstatus.getStates_daily().get(size-14).getJk())-Float.parseFloat(dailyrjstatus.getStates_daily().get(size-11).getJk())-Float.parseFloat(dailyrjstatus.getStates_daily().get(size-8).getJk())-Float.parseFloat(dailyrjstatus.getStates_daily().get(size-5).getJk())

    };
    recovgraph.setData(null);// for example
    recovgraph.setData(recov);
    float active[]=  new float[]{active1-Float.parseFloat(dailyrjstatus.getStates_daily().get(size-5).getJk())-Float.parseFloat(dailyrjstatus.getStates_daily().get(size-4).getJk())-Float.parseFloat(dailyrjstatus.getStates_daily().get(size-6).getJk()),
            active1-Float.parseFloat(dailyrjstatus.getStates_daily().get(size-8).getJk())-Float.parseFloat(dailyrjstatus.getStates_daily().get(size-5).getJk())-Float.parseFloat(dailyrjstatus.getStates_daily().get(size-7).getJk())-Float.parseFloat(dailyrjstatus.getStates_daily().get(size-4).getJk())-Float.parseFloat(dailyrjstatus.getStates_daily().get(size-8).getJk())-Float.parseFloat(dailyrjstatus.getStates_daily().get(size-5).getJk()),
            active1-Float.parseFloat(dailyrjstatus.getStates_daily().get(size-11).getJk())-Float.parseFloat(dailyrjstatus.getStates_daily().get(size-8).getJk())-Float.parseFloat(dailyrjstatus.getStates_daily().get(size-5).getJk())-Float.parseFloat(dailyrjstatus.getStates_daily().get(size-11).getJk())-Float.parseFloat(dailyrjstatus.getStates_daily().get(size-8).getJk())-Float.parseFloat(dailyrjstatus.getStates_daily().get(size-5).getJk())-Float.parseFloat(dailyrjstatus.getStates_daily().get(size-10).getJk())-Float.parseFloat(dailyrjstatus.getStates_daily().get(size-7).getJk())-Float.parseFloat(dailyrjstatus.getStates_daily().get(size-4).getJk()),

            active1-Float.parseFloat(dailyrjstatus.getStates_daily().get(size-14).getJk())-Float.parseFloat(dailyrjstatus.getStates_daily().get(size-11).getJk())-Float.parseFloat(dailyrjstatus.getStates_daily().get(size-8).getJk())-Float.parseFloat(dailyrjstatus.getStates_daily().get(size-5).getJk())-Float.parseFloat(dailyrjstatus.getStates_daily().get(size-14).getJk())-Float.parseFloat(dailyrjstatus.getStates_daily().get(size-11).getJk())-Float.parseFloat(dailyrjstatus.getStates_daily().get(size-8).getJk())-Float.parseFloat(dailyrjstatus.getStates_daily().get(size-5).getJk())-Float.parseFloat(dailyrjstatus.getStates_daily().get(size-13).getJk())-Float.parseFloat(dailyrjstatus.getStates_daily().get(size-10).getJk())-Float.parseFloat(dailyrjstatus.getStates_daily().get(size-7).getJk())-Float.parseFloat(dailyrjstatus.getStates_daily().get(size-4).getJk()),




    };

    activegraphview.setData(active);



//   data.add(new BarEntry(Float.parseFloat(dailyrjstatus.getStates_daily().get(size-3).getDate()),Float.parseFloat(dailyrjstatus.getStates_daily().get(size-3).getJk())));
//        data.add(new BarEntry(Float.parseFloat(dailyrjstatus.getStates_daily().get(size-6).getDate()),));
//        data.add(new BarEntry(Float.parseFloat(dailyrjstatus.getStates_daily().get(size-9).getDate()),Float.parseFloat(dailyrjstatus.getStates_daily().get(size-9).getJk())));
//        data.add(new BarEntry(Float.parseFloat(dailyrjstatus.getStates_daily().get(size-12).getDate()),Float.parseFloat(dailyrjstatus.getStates_daily().get(size-12).getJk())));
//        data.add(new BarEntry(Float.parseFloat(dailyrjstatus.getStates_daily().get(size-15).getDate()),Float.parseFloat(dailyrjstatus.getStates_daily().get(size-15).getJk())));
//        data.add(new BarEntry(Float.parseFloat(dailyrjstatus.getStates_daily().get(size-18).getDate()),Float.parseFloat(dailyrjstatus.getStates_daily().get(size-18).getJk())));
//        data.add(new BarEntry(Float.parseFloat(dailyrjstatus.getStates_daily().get(size-21).getDate()),Float.parseFloat(dailyrjstatus.getStates_daily().get(size-21).getJk())));
//        BarDataSet barDataSet=new BarDataSet(data,"Cases");
//        BarData barData=new BarData();
//        barData.addDataSet(barDataSet);
//        chart.setData(barData);
//        chart.invalidate();
    float graph1[]=  new float[]{Float.parseFloat(dailyrjstatus.getStates_daily().get(size-3).getJk()),
            Float.parseFloat(dailyrjstatus.getStates_daily().get(size-6).getJk()),
            Float.parseFloat(dailyrjstatus.getStates_daily().get(size-9).getJk()),
            Float.parseFloat(dailyrjstatus.getStates_daily().get(size-12).getJk()),
            Float.parseFloat(dailyrjstatus.getStates_daily().get(size-15).getJk()),
            Float.parseFloat(dailyrjstatus.getStates_daily().get(size-18).getJk()),
            Float.parseFloat(dailyrjstatus.getStates_daily().get(size-21).getJk())
    };
    String array[] = new String[]{dailyrjstatus.getStates_daily().get(size - 3).getDate(),
            dailyrjstatus.getStates_daily().get(size - 6).getDate(),
            dailyrjstatus.getStates_daily().get(size - 9).getDate(),
            dailyrjstatus.getStates_daily().get(size - 12).getDate(),
            dailyrjstatus.getStates_daily().get(size - 15).getDate(),
            dailyrjstatus.getStates_daily().get(size - 18).getDate(),
            dailyrjstatus.getStates_daily().get(size - 21).getDate()};
    ChartEntity first = new ChartEntity(Color.WHITE, graph1);
    ArrayList<ChartEntity> list = new ArrayList<>();
    list.add(first);
    chart.setLegend(Arrays.asList(array));
    chart.setList(list);



}


else if(statecode.equalsIgnoreCase("la"))
{

    float afectedcasep[]=  new float[]{affected1-Float.parseFloat(dailyrjstatus.getStates_daily().get(size-6).getLa()),
            affected1-Float.parseFloat(dailyrjstatus.getStates_daily().get(size-9).getLa())-Float.parseFloat(dailyrjstatus.getStates_daily().get(size-6).getLa()),
            affected1-Float.parseFloat(dailyrjstatus.getStates_daily().get(size-12).getLa())-Float.parseFloat(dailyrjstatus.getStates_daily().get(size-9).getLa())-Float.parseFloat(dailyrjstatus.getStates_daily().get(size-6).getLa()),
            affected1-Float.parseFloat(dailyrjstatus.getStates_daily().get(size-15).getLa())-Float.parseFloat(dailyrjstatus.getStates_daily().get(size-12).getLa())-Float.parseFloat(dailyrjstatus.getStates_daily().get(size-9).getLa())-Float.parseFloat(dailyrjstatus.getStates_daily().get(size-6).getLa()),
            affected1-Float.parseFloat(dailyrjstatus.getStates_daily().get(size-18).getLa())-Float.parseFloat(dailyrjstatus.getStates_daily().get(size-15).getLa())-Float.parseFloat(dailyrjstatus.getStates_daily().get(size-12).getLa())-Float.parseFloat(dailyrjstatus.getStates_daily().get(size-9).getLa())-Float.parseFloat(dailyrjstatus.getStates_daily().get(size-6).getLa()),
            affected1-Float.parseFloat(dailyrjstatus.getStates_daily().get(size-21).getLa())-Float.parseFloat(dailyrjstatus.getStates_daily().get(size-18).getLa())-Float.parseFloat(dailyrjstatus.getStates_daily().get(size-15).getLa())-Float.parseFloat(dailyrjstatus.getStates_daily().get(size-12).getLa())-Float.parseFloat(dailyrjstatus.getStates_daily().get(size-9).getLa())-Float.parseFloat(dailyrjstatus.getStates_daily().get(size-6).getLa())

    };


    affectedGraphView.setData(afectedcasep);
    float deceased[]=  new float[]{dead1-Float.parseFloat(dailyrjstatus.getStates_daily().get(size-4).getLa()),
            dead1-Float.parseFloat(dailyrjstatus.getStates_daily().get(size-7).getLa())-Float.parseFloat(dailyrjstatus.getStates_daily().get(size-4).getLa()),
            dead1-Float.parseFloat(dailyrjstatus.getStates_daily().get(size-10).getLa())-Float.parseFloat(dailyrjstatus.getStates_daily().get(size-7).getLa())-Float.parseFloat(dailyrjstatus.getStates_daily().get(size-4).getLa()),
            dead1-Float.parseFloat(dailyrjstatus.getStates_daily().get(size-13).getLa())-Float.parseFloat(dailyrjstatus.getStates_daily().get(size-10).getLa())-Float.parseFloat(dailyrjstatus.getStates_daily().get(size-7).getLa())-Float.parseFloat(dailyrjstatus.getStates_daily().get(size-4).getLa()),
            dead1-Float.parseFloat(dailyrjstatus.getStates_daily().get(size-16).getLa())-Float.parseFloat(dailyrjstatus.getStates_daily().get(size-13).getLa())-Float.parseFloat(dailyrjstatus.getStates_daily().get(size-10).getLa())-Float.parseFloat(dailyrjstatus.getStates_daily().get(size-7).getLa())-Float.parseFloat(dailyrjstatus.getStates_daily().get(size-4).getLa()),
            dead1-Float.parseFloat(dailyrjstatus.getStates_daily().get(size-19).getLa())-Float.parseFloat(dailyrjstatus.getStates_daily().get(size-16).getLa())-Float.parseFloat(dailyrjstatus.getStates_daily().get(size-13).getLa())-Float.parseFloat(dailyrjstatus.getStates_daily().get(size-10).getLa())-Float.parseFloat(dailyrjstatus.getStates_daily().get(size-7).getLa())-Float.parseFloat(dailyrjstatus.getStates_daily().get(size-4).getLa())

    };


    deadgraph.setData(deceased);
    float recov[]=  new float[]{recovered1-Float.parseFloat(dailyrjstatus.getStates_daily().get(size-5).getLa()),
            recovered1-Float.parseFloat(dailyrjstatus.getStates_daily().get(size-8).getLa())-Float.parseFloat(dailyrjstatus.getStates_daily().get(size-5).getLa()),
            recovered1-Float.parseFloat(dailyrjstatus.getStates_daily().get(size-11).getLa())-Float.parseFloat(dailyrjstatus.getStates_daily().get(size-8).getLa())-Float.parseFloat(dailyrjstatus.getStates_daily().get(size-5).getLa()),
            recovered1-Float.parseFloat(dailyrjstatus.getStates_daily().get(size-14).getLa())-Float.parseFloat(dailyrjstatus.getStates_daily().get(size-11).getLa())-Float.parseFloat(dailyrjstatus.getStates_daily().get(size-8).getLa())-Float.parseFloat(dailyrjstatus.getStates_daily().get(size-5).getLa()),
            recovered1-Float.parseFloat(dailyrjstatus.getStates_daily().get(size-17).getLa())-Float.parseFloat(dailyrjstatus.getStates_daily().get(size-14).getLa())-Float.parseFloat(dailyrjstatus.getStates_daily().get(size-11).getLa())-Float.parseFloat(dailyrjstatus.getStates_daily().get(size-8).getLa())-Float.parseFloat(dailyrjstatus.getStates_daily().get(size-5).getLa()),
            recovered1-Float.parseFloat(dailyrjstatus.getStates_daily().get(size-20).getLa())-Float.parseFloat(dailyrjstatus.getStates_daily().get(size-17).getLa())-Float.parseFloat(dailyrjstatus.getStates_daily().get(size-14).getLa())-Float.parseFloat(dailyrjstatus.getStates_daily().get(size-11).getLa())-Float.parseFloat(dailyrjstatus.getStates_daily().get(size-8).getLa())-Float.parseFloat(dailyrjstatus.getStates_daily().get(size-5).getLa())

    };
    recovgraph.setData(null);// for example
    recovgraph.setData(recov);
    float active[]=  new float[]{active1-Float.parseFloat(dailyrjstatus.getStates_daily().get(size-5).getLa())-Float.parseFloat(dailyrjstatus.getStates_daily().get(size-4).getLa())-Float.parseFloat(dailyrjstatus.getStates_daily().get(size-6).getLa()),
            active1-Float.parseFloat(dailyrjstatus.getStates_daily().get(size-8).getLa())-Float.parseFloat(dailyrjstatus.getStates_daily().get(size-5).getLa())-Float.parseFloat(dailyrjstatus.getStates_daily().get(size-7).getLa())-Float.parseFloat(dailyrjstatus.getStates_daily().get(size-4).getLa())-Float.parseFloat(dailyrjstatus.getStates_daily().get(size-8).getLa())-Float.parseFloat(dailyrjstatus.getStates_daily().get(size-5).getLa()),
            active1-Float.parseFloat(dailyrjstatus.getStates_daily().get(size-11).getLa())-Float.parseFloat(dailyrjstatus.getStates_daily().get(size-8).getLa())-Float.parseFloat(dailyrjstatus.getStates_daily().get(size-5).getLa())-Float.parseFloat(dailyrjstatus.getStates_daily().get(size-11).getLa())-Float.parseFloat(dailyrjstatus.getStates_daily().get(size-8).getLa())-Float.parseFloat(dailyrjstatus.getStates_daily().get(size-5).getLa())-Float.parseFloat(dailyrjstatus.getStates_daily().get(size-10).getLa())-Float.parseFloat(dailyrjstatus.getStates_daily().get(size-7).getLa())-Float.parseFloat(dailyrjstatus.getStates_daily().get(size-4).getLa()),

            active1-Float.parseFloat(dailyrjstatus.getStates_daily().get(size-14).getLa())-Float.parseFloat(dailyrjstatus.getStates_daily().get(size-11).getLa())-Float.parseFloat(dailyrjstatus.getStates_daily().get(size-8).getLa())-Float.parseFloat(dailyrjstatus.getStates_daily().get(size-5).getLa())-Float.parseFloat(dailyrjstatus.getStates_daily().get(size-14).getLa())-Float.parseFloat(dailyrjstatus.getStates_daily().get(size-11).getLa())-Float.parseFloat(dailyrjstatus.getStates_daily().get(size-8).getLa())-Float.parseFloat(dailyrjstatus.getStates_daily().get(size-5).getLa())-Float.parseFloat(dailyrjstatus.getStates_daily().get(size-13).getLa())-Float.parseFloat(dailyrjstatus.getStates_daily().get(size-10).getLa())-Float.parseFloat(dailyrjstatus.getStates_daily().get(size-7).getLa())-Float.parseFloat(dailyrjstatus.getStates_daily().get(size-4).getLa()),




    };

    activegraphview.setData(active);



//   data.add(new BarEntry(Float.parseFloat(dailyrjstatus.getStates_daily().get(size-3).getDate()),Float.parseFloat(dailyrjstatus.getStates_daily().get(size-3).getLa())));
//        data.add(new BarEntry(Float.parseFloat(dailyrjstatus.getStates_daily().get(size-6).getDate()),));
//        data.add(new BarEntry(Float.parseFloat(dailyrjstatus.getStates_daily().get(size-9).getDate()),Float.parseFloat(dailyrjstatus.getStates_daily().get(size-9).getLa())));
//        data.add(new BarEntry(Float.parseFloat(dailyrjstatus.getStates_daily().get(size-12).getDate()),Float.parseFloat(dailyrjstatus.getStates_daily().get(size-12).getLa())));
//        data.add(new BarEntry(Float.parseFloat(dailyrjstatus.getStates_daily().get(size-15).getDate()),Float.parseFloat(dailyrjstatus.getStates_daily().get(size-15).getLa())));
//        data.add(new BarEntry(Float.parseFloat(dailyrjstatus.getStates_daily().get(size-18).getDate()),Float.parseFloat(dailyrjstatus.getStates_daily().get(size-18).getLa())));
//        data.add(new BarEntry(Float.parseFloat(dailyrjstatus.getStates_daily().get(size-21).getDate()),Float.parseFloat(dailyrjstatus.getStates_daily().get(size-21).getLa())));
//        BarDataSet barDataSet=new BarDataSet(data,"Cases");
//        BarData barData=new BarData();
//        barData.addDataSet(barDataSet);
//        chart.setData(barData);
//        chart.invalidate();
    float graph1[]=  new float[]{Float.parseFloat(dailyrjstatus.getStates_daily().get(size-3).getLa()),
            Float.parseFloat(dailyrjstatus.getStates_daily().get(size-6).getLa()),
            Float.parseFloat(dailyrjstatus.getStates_daily().get(size-9).getLa()),
            Float.parseFloat(dailyrjstatus.getStates_daily().get(size-12).getLa()),
            Float.parseFloat(dailyrjstatus.getStates_daily().get(size-15).getLa()),
            Float.parseFloat(dailyrjstatus.getStates_daily().get(size-18).getLa()),
            Float.parseFloat(dailyrjstatus.getStates_daily().get(size-21).getLa())
    };
    String array[] = new String[]{dailyrjstatus.getStates_daily().get(size - 3).getDate(),
            dailyrjstatus.getStates_daily().get(size - 6).getDate(),
            dailyrjstatus.getStates_daily().get(size - 9).getDate(),
            dailyrjstatus.getStates_daily().get(size - 12).getDate(),
            dailyrjstatus.getStates_daily().get(size - 15).getDate(),
            dailyrjstatus.getStates_daily().get(size - 18).getDate(),
            dailyrjstatus.getStates_daily().get(size - 21).getDate()};
    ChartEntity first = new ChartEntity(Color.WHITE, graph1);
    ArrayList<ChartEntity> list = new ArrayList<>();
    list.add(first);
    chart.setLegend(Arrays.asList(array));
    chart.setList(list);



}
else if(statecode.equalsIgnoreCase("ld"))
{

    float afectedcasep[]=  new float[]{affected1-Float.parseFloat(dailyrjstatus.getStates_daily().get(size-6).getLd()),
            affected1-Float.parseFloat(dailyrjstatus.getStates_daily().get(size-9).getLd())-Float.parseFloat(dailyrjstatus.getStates_daily().get(size-6).getLd()),
            affected1-Float.parseFloat(dailyrjstatus.getStates_daily().get(size-12).getLd())-Float.parseFloat(dailyrjstatus.getStates_daily().get(size-9).getLd())-Float.parseFloat(dailyrjstatus.getStates_daily().get(size-6).getLd()),
            affected1-Float.parseFloat(dailyrjstatus.getStates_daily().get(size-15).getLd())-Float.parseFloat(dailyrjstatus.getStates_daily().get(size-12).getLd())-Float.parseFloat(dailyrjstatus.getStates_daily().get(size-9).getLd())-Float.parseFloat(dailyrjstatus.getStates_daily().get(size-6).getLd()),
            affected1-Float.parseFloat(dailyrjstatus.getStates_daily().get(size-18).getLd())-Float.parseFloat(dailyrjstatus.getStates_daily().get(size-15).getLd())-Float.parseFloat(dailyrjstatus.getStates_daily().get(size-12).getLd())-Float.parseFloat(dailyrjstatus.getStates_daily().get(size-9).getLd())-Float.parseFloat(dailyrjstatus.getStates_daily().get(size-6).getLd()),
            affected1-Float.parseFloat(dailyrjstatus.getStates_daily().get(size-21).getLd())-Float.parseFloat(dailyrjstatus.getStates_daily().get(size-18).getLd())-Float.parseFloat(dailyrjstatus.getStates_daily().get(size-15).getLd())-Float.parseFloat(dailyrjstatus.getStates_daily().get(size-12).getLd())-Float.parseFloat(dailyrjstatus.getStates_daily().get(size-9).getLd())-Float.parseFloat(dailyrjstatus.getStates_daily().get(size-6).getLd())

    };


    affectedGraphView.setData(afectedcasep);
    float deceased[]=  new float[]{dead1-Float.parseFloat(dailyrjstatus.getStates_daily().get(size-4).getLd()),
            dead1-Float.parseFloat(dailyrjstatus.getStates_daily().get(size-7).getLd())-Float.parseFloat(dailyrjstatus.getStates_daily().get(size-4).getLd()),
            dead1-Float.parseFloat(dailyrjstatus.getStates_daily().get(size-10).getLd())-Float.parseFloat(dailyrjstatus.getStates_daily().get(size-7).getLd())-Float.parseFloat(dailyrjstatus.getStates_daily().get(size-4).getLd()),
            dead1-Float.parseFloat(dailyrjstatus.getStates_daily().get(size-13).getLd())-Float.parseFloat(dailyrjstatus.getStates_daily().get(size-10).getLd())-Float.parseFloat(dailyrjstatus.getStates_daily().get(size-7).getLd())-Float.parseFloat(dailyrjstatus.getStates_daily().get(size-4).getLd()),
            dead1-Float.parseFloat(dailyrjstatus.getStates_daily().get(size-16).getLd())-Float.parseFloat(dailyrjstatus.getStates_daily().get(size-13).getLd())-Float.parseFloat(dailyrjstatus.getStates_daily().get(size-10).getLd())-Float.parseFloat(dailyrjstatus.getStates_daily().get(size-7).getLd())-Float.parseFloat(dailyrjstatus.getStates_daily().get(size-4).getLd()),
            dead1-Float.parseFloat(dailyrjstatus.getStates_daily().get(size-19).getLd())-Float.parseFloat(dailyrjstatus.getStates_daily().get(size-16).getLd())-Float.parseFloat(dailyrjstatus.getStates_daily().get(size-13).getLd())-Float.parseFloat(dailyrjstatus.getStates_daily().get(size-10).getLd())-Float.parseFloat(dailyrjstatus.getStates_daily().get(size-7).getLd())-Float.parseFloat(dailyrjstatus.getStates_daily().get(size-4).getLd())

    };


    deadgraph.setData(deceased);
    float recov[]=  new float[]{recovered1-Float.parseFloat(dailyrjstatus.getStates_daily().get(size-5).getLd()),
            recovered1-Float.parseFloat(dailyrjstatus.getStates_daily().get(size-8).getLd())-Float.parseFloat(dailyrjstatus.getStates_daily().get(size-5).getLd()),
            recovered1-Float.parseFloat(dailyrjstatus.getStates_daily().get(size-11).getLd())-Float.parseFloat(dailyrjstatus.getStates_daily().get(size-8).getLd())-Float.parseFloat(dailyrjstatus.getStates_daily().get(size-5).getLd()),
            recovered1-Float.parseFloat(dailyrjstatus.getStates_daily().get(size-14).getLd())-Float.parseFloat(dailyrjstatus.getStates_daily().get(size-11).getLd())-Float.parseFloat(dailyrjstatus.getStates_daily().get(size-8).getLd())-Float.parseFloat(dailyrjstatus.getStates_daily().get(size-5).getLd()),
            recovered1-Float.parseFloat(dailyrjstatus.getStates_daily().get(size-17).getLd())-Float.parseFloat(dailyrjstatus.getStates_daily().get(size-14).getLd())-Float.parseFloat(dailyrjstatus.getStates_daily().get(size-11).getLd())-Float.parseFloat(dailyrjstatus.getStates_daily().get(size-8).getLd())-Float.parseFloat(dailyrjstatus.getStates_daily().get(size-5).getLd()),
            recovered1-Float.parseFloat(dailyrjstatus.getStates_daily().get(size-20).getLd())-Float.parseFloat(dailyrjstatus.getStates_daily().get(size-17).getLd())-Float.parseFloat(dailyrjstatus.getStates_daily().get(size-14).getLd())-Float.parseFloat(dailyrjstatus.getStates_daily().get(size-11).getLd())-Float.parseFloat(dailyrjstatus.getStates_daily().get(size-8).getLd())-Float.parseFloat(dailyrjstatus.getStates_daily().get(size-5).getLd())

    };
    recovgraph.setData(null);// for example
    recovgraph.setData(recov);
    float active[]=  new float[]{active1-Float.parseFloat(dailyrjstatus.getStates_daily().get(size-5).getLd())-Float.parseFloat(dailyrjstatus.getStates_daily().get(size-4).getLd())-Float.parseFloat(dailyrjstatus.getStates_daily().get(size-6).getLd()),
            active1-Float.parseFloat(dailyrjstatus.getStates_daily().get(size-8).getLd())-Float.parseFloat(dailyrjstatus.getStates_daily().get(size-5).getLd())-Float.parseFloat(dailyrjstatus.getStates_daily().get(size-7).getLd())-Float.parseFloat(dailyrjstatus.getStates_daily().get(size-4).getLd())-Float.parseFloat(dailyrjstatus.getStates_daily().get(size-8).getLd())-Float.parseFloat(dailyrjstatus.getStates_daily().get(size-5).getLd()),
            active1-Float.parseFloat(dailyrjstatus.getStates_daily().get(size-11).getLd())-Float.parseFloat(dailyrjstatus.getStates_daily().get(size-8).getLd())-Float.parseFloat(dailyrjstatus.getStates_daily().get(size-5).getLd())-Float.parseFloat(dailyrjstatus.getStates_daily().get(size-11).getLd())-Float.parseFloat(dailyrjstatus.getStates_daily().get(size-8).getLd())-Float.parseFloat(dailyrjstatus.getStates_daily().get(size-5).getLd())-Float.parseFloat(dailyrjstatus.getStates_daily().get(size-10).getLd())-Float.parseFloat(dailyrjstatus.getStates_daily().get(size-7).getLd())-Float.parseFloat(dailyrjstatus.getStates_daily().get(size-4).getLd()),

            active1-Float.parseFloat(dailyrjstatus.getStates_daily().get(size-14).getLd())-Float.parseFloat(dailyrjstatus.getStates_daily().get(size-11).getLd())-Float.parseFloat(dailyrjstatus.getStates_daily().get(size-8).getLd())-Float.parseFloat(dailyrjstatus.getStates_daily().get(size-5).getLd())-Float.parseFloat(dailyrjstatus.getStates_daily().get(size-14).getLd())-Float.parseFloat(dailyrjstatus.getStates_daily().get(size-11).getLd())-Float.parseFloat(dailyrjstatus.getStates_daily().get(size-8).getLd())-Float.parseFloat(dailyrjstatus.getStates_daily().get(size-5).getLd())-Float.parseFloat(dailyrjstatus.getStates_daily().get(size-13).getLd())-Float.parseFloat(dailyrjstatus.getStates_daily().get(size-10).getLd())-Float.parseFloat(dailyrjstatus.getStates_daily().get(size-7).getLd())-Float.parseFloat(dailyrjstatus.getStates_daily().get(size-4).getLd()),




    };

    activegraphview.setData(active);



//   data.add(new BarEntry(Float.parseFloat(dailyrjstatus.getStates_daily().get(size-3).getDate()),Float.parseFloat(dailyrjstatus.getStates_daily().get(size-3).getLd())));
//        data.add(new BarEntry(Float.parseFloat(dailyrjstatus.getStates_daily().get(size-6).getDate()),));
//        data.add(new BarEntry(Float.parseFloat(dailyrjstatus.getStates_daily().get(size-9).getDate()),Float.parseFloat(dailyrjstatus.getStates_daily().get(size-9).getLd())));
//        data.add(new BarEntry(Float.parseFloat(dailyrjstatus.getStates_daily().get(size-12).getDate()),Float.parseFloat(dailyrjstatus.getStates_daily().get(size-12).getLd())));
//        data.add(new BarEntry(Float.parseFloat(dailyrjstatus.getStates_daily().get(size-15).getDate()),Float.parseFloat(dailyrjstatus.getStates_daily().get(size-15).getLd())));
//        data.add(new BarEntry(Float.parseFloat(dailyrjstatus.getStates_daily().get(size-18).getDate()),Float.parseFloat(dailyrjstatus.getStates_daily().get(size-18).getLd())));
//        data.add(new BarEntry(Float.parseFloat(dailyrjstatus.getStates_daily().get(size-21).getDate()),Float.parseFloat(dailyrjstatus.getStates_daily().get(size-21).getLd())));
//        BarDataSet barDataSet=new BarDataSet(data,"Cases");
//        BarData barData=new BarData();
//        barData.addDataSet(barDataSet);
//        chart.setData(barData);
//        chart.invalidate();
    float graph1[]=  new float[]{Float.parseFloat(dailyrjstatus.getStates_daily().get(size-3).getLd()),
            Float.parseFloat(dailyrjstatus.getStates_daily().get(size-6).getLd()),
            Float.parseFloat(dailyrjstatus.getStates_daily().get(size-9).getLd()),
            Float.parseFloat(dailyrjstatus.getStates_daily().get(size-12).getLd()),
            Float.parseFloat(dailyrjstatus.getStates_daily().get(size-15).getLd()),
            Float.parseFloat(dailyrjstatus.getStates_daily().get(size-18).getLd()),
            Float.parseFloat(dailyrjstatus.getStates_daily().get(size-21).getLd())
    };
    String array[] = new String[]{dailyrjstatus.getStates_daily().get(size - 3).getDate(),
            dailyrjstatus.getStates_daily().get(size - 6).getDate(),
            dailyrjstatus.getStates_daily().get(size - 9).getDate(),
            dailyrjstatus.getStates_daily().get(size - 12).getDate(),
            dailyrjstatus.getStates_daily().get(size - 15).getDate(),
            dailyrjstatus.getStates_daily().get(size - 18).getDate(),
            dailyrjstatus.getStates_daily().get(size - 21).getDate()};
    ChartEntity first = new ChartEntity(Color.WHITE, graph1);
    ArrayList<ChartEntity> list = new ArrayList<>();
    list.add(first);
    chart.setLegend(Arrays.asList(array));
    chart.setList(list);



}
else if(statecode.equalsIgnoreCase("mh"))
{

    float afectedcasep[]=  new float[]{affected1-Float.parseFloat(dailyrjstatus.getStates_daily().get(size-6).getMh()),
            affected1-Float.parseFloat(dailyrjstatus.getStates_daily().get(size-9).getMh())-Float.parseFloat(dailyrjstatus.getStates_daily().get(size-6).getMh()),
            affected1-Float.parseFloat(dailyrjstatus.getStates_daily().get(size-12).getMh())-Float.parseFloat(dailyrjstatus.getStates_daily().get(size-9).getMh())-Float.parseFloat(dailyrjstatus.getStates_daily().get(size-6).getMh()),
            affected1-Float.parseFloat(dailyrjstatus.getStates_daily().get(size-15).getMh())-Float.parseFloat(dailyrjstatus.getStates_daily().get(size-12).getMh())-Float.parseFloat(dailyrjstatus.getStates_daily().get(size-9).getMh())-Float.parseFloat(dailyrjstatus.getStates_daily().get(size-6).getMh()),
            affected1-Float.parseFloat(dailyrjstatus.getStates_daily().get(size-18).getMh())-Float.parseFloat(dailyrjstatus.getStates_daily().get(size-15).getMh())-Float.parseFloat(dailyrjstatus.getStates_daily().get(size-12).getMh())-Float.parseFloat(dailyrjstatus.getStates_daily().get(size-9).getMh())-Float.parseFloat(dailyrjstatus.getStates_daily().get(size-6).getMh()),
            affected1-Float.parseFloat(dailyrjstatus.getStates_daily().get(size-21).getMh())-Float.parseFloat(dailyrjstatus.getStates_daily().get(size-18).getMh())-Float.parseFloat(dailyrjstatus.getStates_daily().get(size-15).getMh())-Float.parseFloat(dailyrjstatus.getStates_daily().get(size-12).getMh())-Float.parseFloat(dailyrjstatus.getStates_daily().get(size-9).getMh())-Float.parseFloat(dailyrjstatus.getStates_daily().get(size-6).getMh())

    };


    affectedGraphView.setData(afectedcasep);
    float deceased[]=  new float[]{dead1-Float.parseFloat(dailyrjstatus.getStates_daily().get(size-4).getMh()),
            dead1-Float.parseFloat(dailyrjstatus.getStates_daily().get(size-7).getMh())-Float.parseFloat(dailyrjstatus.getStates_daily().get(size-4).getMh()),
            dead1-Float.parseFloat(dailyrjstatus.getStates_daily().get(size-10).getMh())-Float.parseFloat(dailyrjstatus.getStates_daily().get(size-7).getMh())-Float.parseFloat(dailyrjstatus.getStates_daily().get(size-4).getMh()),
            dead1-Float.parseFloat(dailyrjstatus.getStates_daily().get(size-13).getMh())-Float.parseFloat(dailyrjstatus.getStates_daily().get(size-10).getMh())-Float.parseFloat(dailyrjstatus.getStates_daily().get(size-7).getMh())-Float.parseFloat(dailyrjstatus.getStates_daily().get(size-4).getMh()),
            dead1-Float.parseFloat(dailyrjstatus.getStates_daily().get(size-16).getMh())-Float.parseFloat(dailyrjstatus.getStates_daily().get(size-13).getMh())-Float.parseFloat(dailyrjstatus.getStates_daily().get(size-10).getMh())-Float.parseFloat(dailyrjstatus.getStates_daily().get(size-7).getMh())-Float.parseFloat(dailyrjstatus.getStates_daily().get(size-4).getMh()),
            dead1-Float.parseFloat(dailyrjstatus.getStates_daily().get(size-19).getMh())-Float.parseFloat(dailyrjstatus.getStates_daily().get(size-16).getMh())-Float.parseFloat(dailyrjstatus.getStates_daily().get(size-13).getMh())-Float.parseFloat(dailyrjstatus.getStates_daily().get(size-10).getMh())-Float.parseFloat(dailyrjstatus.getStates_daily().get(size-7).getMh())-Float.parseFloat(dailyrjstatus.getStates_daily().get(size-4).getMh())

    };


    deadgraph.setData(deceased);
    float recov[]=  new float[]{recovered1-Float.parseFloat(dailyrjstatus.getStates_daily().get(size-5).getMh()),
            recovered1-Float.parseFloat(dailyrjstatus.getStates_daily().get(size-8).getMh())-Float.parseFloat(dailyrjstatus.getStates_daily().get(size-5).getMh()),
            recovered1-Float.parseFloat(dailyrjstatus.getStates_daily().get(size-11).getMh())-Float.parseFloat(dailyrjstatus.getStates_daily().get(size-8).getMh())-Float.parseFloat(dailyrjstatus.getStates_daily().get(size-5).getMh()),
            recovered1-Float.parseFloat(dailyrjstatus.getStates_daily().get(size-14).getMh())-Float.parseFloat(dailyrjstatus.getStates_daily().get(size-11).getMh())-Float.parseFloat(dailyrjstatus.getStates_daily().get(size-8).getMh())-Float.parseFloat(dailyrjstatus.getStates_daily().get(size-5).getMh()),
            recovered1-Float.parseFloat(dailyrjstatus.getStates_daily().get(size-17).getMh())-Float.parseFloat(dailyrjstatus.getStates_daily().get(size-14).getMh())-Float.parseFloat(dailyrjstatus.getStates_daily().get(size-11).getMh())-Float.parseFloat(dailyrjstatus.getStates_daily().get(size-8).getMh())-Float.parseFloat(dailyrjstatus.getStates_daily().get(size-5).getMh()),
            recovered1-Float.parseFloat(dailyrjstatus.getStates_daily().get(size-20).getMh())-Float.parseFloat(dailyrjstatus.getStates_daily().get(size-17).getMh())-Float.parseFloat(dailyrjstatus.getStates_daily().get(size-14).getMh())-Float.parseFloat(dailyrjstatus.getStates_daily().get(size-11).getMh())-Float.parseFloat(dailyrjstatus.getStates_daily().get(size-8).getMh())-Float.parseFloat(dailyrjstatus.getStates_daily().get(size-5).getMh())

    };
    recovgraph.setData(null);// for example
    recovgraph.setData(recov);
    float active[]=  new float[]{active1-Float.parseFloat(dailyrjstatus.getStates_daily().get(size-5).getMh())-Float.parseFloat(dailyrjstatus.getStates_daily().get(size-4).getMh())-Float.parseFloat(dailyrjstatus.getStates_daily().get(size-6).getMh()),
            active1-Float.parseFloat(dailyrjstatus.getStates_daily().get(size-8).getMh())-Float.parseFloat(dailyrjstatus.getStates_daily().get(size-5).getMh())-Float.parseFloat(dailyrjstatus.getStates_daily().get(size-7).getMh())-Float.parseFloat(dailyrjstatus.getStates_daily().get(size-4).getMh())-Float.parseFloat(dailyrjstatus.getStates_daily().get(size-8).getMh())-Float.parseFloat(dailyrjstatus.getStates_daily().get(size-5).getMh()),
            active1-Float.parseFloat(dailyrjstatus.getStates_daily().get(size-11).getMh())-Float.parseFloat(dailyrjstatus.getStates_daily().get(size-8).getMh())-Float.parseFloat(dailyrjstatus.getStates_daily().get(size-5).getMh())-Float.parseFloat(dailyrjstatus.getStates_daily().get(size-11).getMh())-Float.parseFloat(dailyrjstatus.getStates_daily().get(size-8).getMh())-Float.parseFloat(dailyrjstatus.getStates_daily().get(size-5).getMh())-Float.parseFloat(dailyrjstatus.getStates_daily().get(size-10).getMh())-Float.parseFloat(dailyrjstatus.getStates_daily().get(size-7).getMh())-Float.parseFloat(dailyrjstatus.getStates_daily().get(size-4).getMh()),

            active1-Float.parseFloat(dailyrjstatus.getStates_daily().get(size-14).getMh())-Float.parseFloat(dailyrjstatus.getStates_daily().get(size-11).getMh())-Float.parseFloat(dailyrjstatus.getStates_daily().get(size-8).getMh())-Float.parseFloat(dailyrjstatus.getStates_daily().get(size-5).getMh())-Float.parseFloat(dailyrjstatus.getStates_daily().get(size-14).getMh())-Float.parseFloat(dailyrjstatus.getStates_daily().get(size-11).getMh())-Float.parseFloat(dailyrjstatus.getStates_daily().get(size-8).getMh())-Float.parseFloat(dailyrjstatus.getStates_daily().get(size-5).getMh())-Float.parseFloat(dailyrjstatus.getStates_daily().get(size-13).getMh())-Float.parseFloat(dailyrjstatus.getStates_daily().get(size-10).getMh())-Float.parseFloat(dailyrjstatus.getStates_daily().get(size-7).getMh())-Float.parseFloat(dailyrjstatus.getStates_daily().get(size-4).getMh()),




    };

    activegraphview.setData(active);



//   data.add(new BarEntry(Float.parseFloat(dailyrjstatus.getStates_daily().get(size-3).getDate()),Float.parseFloat(dailyrjstatus.getStates_daily().get(size-3).getMh())));
//        data.add(new BarEntry(Float.parseFloat(dailyrjstatus.getStates_daily().get(size-6).getDate()),));
//        data.add(new BarEntry(Float.parseFloat(dailyrjstatus.getStates_daily().get(size-9).getDate()),Float.parseFloat(dailyrjstatus.getStates_daily().get(size-9).getMh())));
//        data.add(new BarEntry(Float.parseFloat(dailyrjstatus.getStates_daily().get(size-12).getDate()),Float.parseFloat(dailyrjstatus.getStates_daily().get(size-12).getMh())));
//        data.add(new BarEntry(Float.parseFloat(dailyrjstatus.getStates_daily().get(size-15).getDate()),Float.parseFloat(dailyrjstatus.getStates_daily().get(size-15).getMh())));
//        data.add(new BarEntry(Float.parseFloat(dailyrjstatus.getStates_daily().get(size-18).getDate()),Float.parseFloat(dailyrjstatus.getStates_daily().get(size-18).getMh())));
//        data.add(new BarEntry(Float.parseFloat(dailyrjstatus.getStates_daily().get(size-21).getDate()),Float.parseFloat(dailyrjstatus.getStates_daily().get(size-21).getMh())));
//        BarDataSet barDataSet=new BarDataSet(data,"Cases");
//        BarData barData=new BarData();
//        barData.addDataSet(barDataSet);
//        chart.setData(barData);
//        chart.invalidate();
    float graph1[]=  new float[]{Float.parseFloat(dailyrjstatus.getStates_daily().get(size-3).getMh()),
            Float.parseFloat(dailyrjstatus.getStates_daily().get(size-6).getMh()),
            Float.parseFloat(dailyrjstatus.getStates_daily().get(size-9).getMh()),
            Float.parseFloat(dailyrjstatus.getStates_daily().get(size-12).getMh()),
            Float.parseFloat(dailyrjstatus.getStates_daily().get(size-15).getMh()),
            Float.parseFloat(dailyrjstatus.getStates_daily().get(size-18).getMh()),
            Float.parseFloat(dailyrjstatus.getStates_daily().get(size-21).getMh())
    };
    String array[] = new String[]{dailyrjstatus.getStates_daily().get(size - 3).getDate(),
            dailyrjstatus.getStates_daily().get(size - 6).getDate(),
            dailyrjstatus.getStates_daily().get(size - 9).getDate(),
            dailyrjstatus.getStates_daily().get(size - 12).getDate(),
            dailyrjstatus.getStates_daily().get(size - 15).getDate(),
            dailyrjstatus.getStates_daily().get(size - 18).getDate(),
            dailyrjstatus.getStates_daily().get(size - 21).getDate()};
    ChartEntity first = new ChartEntity(Color.WHITE, graph1);
    ArrayList<ChartEntity> list = new ArrayList<>();
    list.add(first);
    chart.setLegend(Arrays.asList(array));
    chart.setList(list);



}


else if(statecode.equalsIgnoreCase("mp"))
{

    float afectedcasep[]=  new float[]{affected1-Float.parseFloat(dailyrjstatus.getStates_daily().get(size-6).getMp()),
            affected1-Float.parseFloat(dailyrjstatus.getStates_daily().get(size-9).getMp())-Float.parseFloat(dailyrjstatus.getStates_daily().get(size-6).getMp()),
            affected1-Float.parseFloat(dailyrjstatus.getStates_daily().get(size-12).getMp())-Float.parseFloat(dailyrjstatus.getStates_daily().get(size-9).getMp())-Float.parseFloat(dailyrjstatus.getStates_daily().get(size-6).getMp()),
            affected1-Float.parseFloat(dailyrjstatus.getStates_daily().get(size-15).getMp())-Float.parseFloat(dailyrjstatus.getStates_daily().get(size-12).getMp())-Float.parseFloat(dailyrjstatus.getStates_daily().get(size-9).getMp())-Float.parseFloat(dailyrjstatus.getStates_daily().get(size-6).getMp()),
            affected1-Float.parseFloat(dailyrjstatus.getStates_daily().get(size-18).getMp())-Float.parseFloat(dailyrjstatus.getStates_daily().get(size-15).getMp())-Float.parseFloat(dailyrjstatus.getStates_daily().get(size-12).getMp())-Float.parseFloat(dailyrjstatus.getStates_daily().get(size-9).getMp())-Float.parseFloat(dailyrjstatus.getStates_daily().get(size-6).getMp()),
            affected1-Float.parseFloat(dailyrjstatus.getStates_daily().get(size-21).getMp())-Float.parseFloat(dailyrjstatus.getStates_daily().get(size-18).getMp())-Float.parseFloat(dailyrjstatus.getStates_daily().get(size-15).getMp())-Float.parseFloat(dailyrjstatus.getStates_daily().get(size-12).getMp())-Float.parseFloat(dailyrjstatus.getStates_daily().get(size-9).getMp())-Float.parseFloat(dailyrjstatus.getStates_daily().get(size-6).getMp())

    };


    affectedGraphView.setData(afectedcasep);
    float deceased[]=  new float[]{dead1-Float.parseFloat(dailyrjstatus.getStates_daily().get(size-4).getMp()),
            dead1-Float.parseFloat(dailyrjstatus.getStates_daily().get(size-7).getMp())-Float.parseFloat(dailyrjstatus.getStates_daily().get(size-4).getMp()),
            dead1-Float.parseFloat(dailyrjstatus.getStates_daily().get(size-10).getMp())-Float.parseFloat(dailyrjstatus.getStates_daily().get(size-7).getMp())-Float.parseFloat(dailyrjstatus.getStates_daily().get(size-4).getMp()),
            dead1-Float.parseFloat(dailyrjstatus.getStates_daily().get(size-13).getMp())-Float.parseFloat(dailyrjstatus.getStates_daily().get(size-10).getMp())-Float.parseFloat(dailyrjstatus.getStates_daily().get(size-7).getMp())-Float.parseFloat(dailyrjstatus.getStates_daily().get(size-4).getMp()),
            dead1-Float.parseFloat(dailyrjstatus.getStates_daily().get(size-16).getMp())-Float.parseFloat(dailyrjstatus.getStates_daily().get(size-13).getMp())-Float.parseFloat(dailyrjstatus.getStates_daily().get(size-10).getMp())-Float.parseFloat(dailyrjstatus.getStates_daily().get(size-7).getMp())-Float.parseFloat(dailyrjstatus.getStates_daily().get(size-4).getMp()),
            dead1-Float.parseFloat(dailyrjstatus.getStates_daily().get(size-19).getMp())-Float.parseFloat(dailyrjstatus.getStates_daily().get(size-16).getMp())-Float.parseFloat(dailyrjstatus.getStates_daily().get(size-13).getMp())-Float.parseFloat(dailyrjstatus.getStates_daily().get(size-10).getMp())-Float.parseFloat(dailyrjstatus.getStates_daily().get(size-7).getMp())-Float.parseFloat(dailyrjstatus.getStates_daily().get(size-4).getMp())

    };


    deadgraph.setData(deceased);
    float recov[]=  new float[]{recovered1-Float.parseFloat(dailyrjstatus.getStates_daily().get(size-5).getMp()),
            recovered1-Float.parseFloat(dailyrjstatus.getStates_daily().get(size-8).getMp())-Float.parseFloat(dailyrjstatus.getStates_daily().get(size-5).getMp()),
            recovered1-Float.parseFloat(dailyrjstatus.getStates_daily().get(size-11).getMp())-Float.parseFloat(dailyrjstatus.getStates_daily().get(size-8).getMp())-Float.parseFloat(dailyrjstatus.getStates_daily().get(size-5).getMp()),
            recovered1-Float.parseFloat(dailyrjstatus.getStates_daily().get(size-14).getMp())-Float.parseFloat(dailyrjstatus.getStates_daily().get(size-11).getMp())-Float.parseFloat(dailyrjstatus.getStates_daily().get(size-8).getMp())-Float.parseFloat(dailyrjstatus.getStates_daily().get(size-5).getMp()),
            recovered1-Float.parseFloat(dailyrjstatus.getStates_daily().get(size-17).getMp())-Float.parseFloat(dailyrjstatus.getStates_daily().get(size-14).getMp())-Float.parseFloat(dailyrjstatus.getStates_daily().get(size-11).getMp())-Float.parseFloat(dailyrjstatus.getStates_daily().get(size-8).getMp())-Float.parseFloat(dailyrjstatus.getStates_daily().get(size-5).getMp()),
            recovered1-Float.parseFloat(dailyrjstatus.getStates_daily().get(size-20).getMp())-Float.parseFloat(dailyrjstatus.getStates_daily().get(size-17).getMp())-Float.parseFloat(dailyrjstatus.getStates_daily().get(size-14).getMp())-Float.parseFloat(dailyrjstatus.getStates_daily().get(size-11).getMp())-Float.parseFloat(dailyrjstatus.getStates_daily().get(size-8).getMp())-Float.parseFloat(dailyrjstatus.getStates_daily().get(size-5).getMp())

    };
    recovgraph.setData(null);// for example
    recovgraph.setData(recov);
    float active[]=  new float[]{active1-Float.parseFloat(dailyrjstatus.getStates_daily().get(size-5).getMp())-Float.parseFloat(dailyrjstatus.getStates_daily().get(size-4).getMp())-Float.parseFloat(dailyrjstatus.getStates_daily().get(size-6).getMp()),
            active1-Float.parseFloat(dailyrjstatus.getStates_daily().get(size-8).getMp())-Float.parseFloat(dailyrjstatus.getStates_daily().get(size-5).getMp())-Float.parseFloat(dailyrjstatus.getStates_daily().get(size-7).getMp())-Float.parseFloat(dailyrjstatus.getStates_daily().get(size-4).getMp())-Float.parseFloat(dailyrjstatus.getStates_daily().get(size-8).getMp())-Float.parseFloat(dailyrjstatus.getStates_daily().get(size-5).getMp()),
            active1-Float.parseFloat(dailyrjstatus.getStates_daily().get(size-11).getMp())-Float.parseFloat(dailyrjstatus.getStates_daily().get(size-8).getMp())-Float.parseFloat(dailyrjstatus.getStates_daily().get(size-5).getMp())-Float.parseFloat(dailyrjstatus.getStates_daily().get(size-11).getMp())-Float.parseFloat(dailyrjstatus.getStates_daily().get(size-8).getMp())-Float.parseFloat(dailyrjstatus.getStates_daily().get(size-5).getMp())-Float.parseFloat(dailyrjstatus.getStates_daily().get(size-10).getMp())-Float.parseFloat(dailyrjstatus.getStates_daily().get(size-7).getMp())-Float.parseFloat(dailyrjstatus.getStates_daily().get(size-4).getMp()),

            active1-Float.parseFloat(dailyrjstatus.getStates_daily().get(size-14).getMp())-Float.parseFloat(dailyrjstatus.getStates_daily().get(size-11).getMp())-Float.parseFloat(dailyrjstatus.getStates_daily().get(size-8).getMp())-Float.parseFloat(dailyrjstatus.getStates_daily().get(size-5).getMp())-Float.parseFloat(dailyrjstatus.getStates_daily().get(size-14).getMp())-Float.parseFloat(dailyrjstatus.getStates_daily().get(size-11).getMp())-Float.parseFloat(dailyrjstatus.getStates_daily().get(size-8).getMp())-Float.parseFloat(dailyrjstatus.getStates_daily().get(size-5).getMp())-Float.parseFloat(dailyrjstatus.getStates_daily().get(size-13).getMp())-Float.parseFloat(dailyrjstatus.getStates_daily().get(size-10).getMp())-Float.parseFloat(dailyrjstatus.getStates_daily().get(size-7).getMp())-Float.parseFloat(dailyrjstatus.getStates_daily().get(size-4).getMp()),




    };

    activegraphview.setData(active);



//   data.add(new BarEntry(Float.parseFloat(dailyrjstatus.getStates_daily().get(size-3).getDate()),Float.parseFloat(dailyrjstatus.getStates_daily().get(size-3).getMp())));
//        data.add(new BarEntry(Float.parseFloat(dailyrjstatus.getStates_daily().get(size-6).getDate()),));
//        data.add(new BarEntry(Float.parseFloat(dailyrjstatus.getStates_daily().get(size-9).getDate()),Float.parseFloat(dailyrjstatus.getStates_daily().get(size-9).getMp())));
//        data.add(new BarEntry(Float.parseFloat(dailyrjstatus.getStates_daily().get(size-12).getDate()),Float.parseFloat(dailyrjstatus.getStates_daily().get(size-12).getMp())));
//        data.add(new BarEntry(Float.parseFloat(dailyrjstatus.getStates_daily().get(size-15).getDate()),Float.parseFloat(dailyrjstatus.getStates_daily().get(size-15).getMp())));
//        data.add(new BarEntry(Float.parseFloat(dailyrjstatus.getStates_daily().get(size-18).getDate()),Float.parseFloat(dailyrjstatus.getStates_daily().get(size-18).getMp())));
//        data.add(new BarEntry(Float.parseFloat(dailyrjstatus.getStates_daily().get(size-21).getDate()),Float.parseFloat(dailyrjstatus.getStates_daily().get(size-21).getMp())));
//        BarDataSet barDataSet=new BarDataSet(data,"Cases");
//        BarData barData=new BarData();
//        barData.addDataSet(barDataSet);
//        chart.setData(barData);
//        chart.invalidate();
    float graph1[]=  new float[]{Float.parseFloat(dailyrjstatus.getStates_daily().get(size-3).getMp()),
            Float.parseFloat(dailyrjstatus.getStates_daily().get(size-6).getMp()),
            Float.parseFloat(dailyrjstatus.getStates_daily().get(size-9).getMp()),
            Float.parseFloat(dailyrjstatus.getStates_daily().get(size-12).getMp()),
            Float.parseFloat(dailyrjstatus.getStates_daily().get(size-15).getMp()),
            Float.parseFloat(dailyrjstatus.getStates_daily().get(size-18).getMp()),
            Float.parseFloat(dailyrjstatus.getStates_daily().get(size-21).getMp())
    };
    String array[] = new String[]{dailyrjstatus.getStates_daily().get(size - 3).getDate(),
            dailyrjstatus.getStates_daily().get(size - 6).getDate(),
            dailyrjstatus.getStates_daily().get(size - 9).getDate(),
            dailyrjstatus.getStates_daily().get(size - 12).getDate(),
            dailyrjstatus.getStates_daily().get(size - 15).getDate(),
            dailyrjstatus.getStates_daily().get(size - 18).getDate(),
            dailyrjstatus.getStates_daily().get(size - 21).getDate()};
    ChartEntity first = new ChartEntity(Color.WHITE, graph1);
    ArrayList<ChartEntity> list = new ArrayList<>();
    list.add(first);
    chart.setLegend(Arrays.asList(array));
    chart.setList(list);



}


else if(statecode.equalsIgnoreCase("or"))
{

    float afectedcasep[]=  new float[]{affected1-Float.parseFloat(dailyrjstatus.getStates_daily().get(size-6).getOr()),
            affected1-Float.parseFloat(dailyrjstatus.getStates_daily().get(size-9).getOr())-Float.parseFloat(dailyrjstatus.getStates_daily().get(size-6).getOr()),
            affected1-Float.parseFloat(dailyrjstatus.getStates_daily().get(size-12).getOr())-Float.parseFloat(dailyrjstatus.getStates_daily().get(size-9).getOr())-Float.parseFloat(dailyrjstatus.getStates_daily().get(size-6).getOr()),
            affected1-Float.parseFloat(dailyrjstatus.getStates_daily().get(size-15).getOr())-Float.parseFloat(dailyrjstatus.getStates_daily().get(size-12).getOr())-Float.parseFloat(dailyrjstatus.getStates_daily().get(size-9).getOr())-Float.parseFloat(dailyrjstatus.getStates_daily().get(size-6).getOr()),
            affected1-Float.parseFloat(dailyrjstatus.getStates_daily().get(size-18).getOr())-Float.parseFloat(dailyrjstatus.getStates_daily().get(size-15).getOr())-Float.parseFloat(dailyrjstatus.getStates_daily().get(size-12).getOr())-Float.parseFloat(dailyrjstatus.getStates_daily().get(size-9).getOr())-Float.parseFloat(dailyrjstatus.getStates_daily().get(size-6).getOr()),
            affected1-Float.parseFloat(dailyrjstatus.getStates_daily().get(size-21).getOr())-Float.parseFloat(dailyrjstatus.getStates_daily().get(size-18).getOr())-Float.parseFloat(dailyrjstatus.getStates_daily().get(size-15).getOr())-Float.parseFloat(dailyrjstatus.getStates_daily().get(size-12).getOr())-Float.parseFloat(dailyrjstatus.getStates_daily().get(size-9).getOr())-Float.parseFloat(dailyrjstatus.getStates_daily().get(size-6).getOr())

    };


    affectedGraphView.setData(afectedcasep);
    float deceased[]=  new float[]{dead1-Float.parseFloat(dailyrjstatus.getStates_daily().get(size-4).getOr()),
            dead1-Float.parseFloat(dailyrjstatus.getStates_daily().get(size-7).getOr())-Float.parseFloat(dailyrjstatus.getStates_daily().get(size-4).getOr()),
            dead1-Float.parseFloat(dailyrjstatus.getStates_daily().get(size-10).getOr())-Float.parseFloat(dailyrjstatus.getStates_daily().get(size-7).getOr())-Float.parseFloat(dailyrjstatus.getStates_daily().get(size-4).getOr()),
            dead1-Float.parseFloat(dailyrjstatus.getStates_daily().get(size-13).getOr())-Float.parseFloat(dailyrjstatus.getStates_daily().get(size-10).getOr())-Float.parseFloat(dailyrjstatus.getStates_daily().get(size-7).getOr())-Float.parseFloat(dailyrjstatus.getStates_daily().get(size-4).getOr()),
            dead1-Float.parseFloat(dailyrjstatus.getStates_daily().get(size-16).getOr())-Float.parseFloat(dailyrjstatus.getStates_daily().get(size-13).getOr())-Float.parseFloat(dailyrjstatus.getStates_daily().get(size-10).getOr())-Float.parseFloat(dailyrjstatus.getStates_daily().get(size-7).getOr())-Float.parseFloat(dailyrjstatus.getStates_daily().get(size-4).getOr()),
            dead1-Float.parseFloat(dailyrjstatus.getStates_daily().get(size-19).getOr())-Float.parseFloat(dailyrjstatus.getStates_daily().get(size-16).getOr())-Float.parseFloat(dailyrjstatus.getStates_daily().get(size-13).getOr())-Float.parseFloat(dailyrjstatus.getStates_daily().get(size-10).getOr())-Float.parseFloat(dailyrjstatus.getStates_daily().get(size-7).getOr())-Float.parseFloat(dailyrjstatus.getStates_daily().get(size-4).getOr())

    };


    deadgraph.setData(deceased);
    float recov[]=  new float[]{recovered1-Float.parseFloat(dailyrjstatus.getStates_daily().get(size-5).getOr()),
            recovered1-Float.parseFloat(dailyrjstatus.getStates_daily().get(size-8).getOr())-Float.parseFloat(dailyrjstatus.getStates_daily().get(size-5).getOr()),
            recovered1-Float.parseFloat(dailyrjstatus.getStates_daily().get(size-11).getOr())-Float.parseFloat(dailyrjstatus.getStates_daily().get(size-8).getOr())-Float.parseFloat(dailyrjstatus.getStates_daily().get(size-5).getOr()),
            recovered1-Float.parseFloat(dailyrjstatus.getStates_daily().get(size-14).getOr())-Float.parseFloat(dailyrjstatus.getStates_daily().get(size-11).getOr())-Float.parseFloat(dailyrjstatus.getStates_daily().get(size-8).getOr())-Float.parseFloat(dailyrjstatus.getStates_daily().get(size-5).getOr()),
            recovered1-Float.parseFloat(dailyrjstatus.getStates_daily().get(size-17).getOr())-Float.parseFloat(dailyrjstatus.getStates_daily().get(size-14).getOr())-Float.parseFloat(dailyrjstatus.getStates_daily().get(size-11).getOr())-Float.parseFloat(dailyrjstatus.getStates_daily().get(size-8).getOr())-Float.parseFloat(dailyrjstatus.getStates_daily().get(size-5).getOr()),
            recovered1-Float.parseFloat(dailyrjstatus.getStates_daily().get(size-20).getOr())-Float.parseFloat(dailyrjstatus.getStates_daily().get(size-17).getOr())-Float.parseFloat(dailyrjstatus.getStates_daily().get(size-14).getOr())-Float.parseFloat(dailyrjstatus.getStates_daily().get(size-11).getOr())-Float.parseFloat(dailyrjstatus.getStates_daily().get(size-8).getOr())-Float.parseFloat(dailyrjstatus.getStates_daily().get(size-5).getOr())

    };
    recovgraph.setData(null);// for example
    recovgraph.setData(recov);
    float active[]=  new float[]{active1-Float.parseFloat(dailyrjstatus.getStates_daily().get(size-5).getOr())-Float.parseFloat(dailyrjstatus.getStates_daily().get(size-4).getOr())-Float.parseFloat(dailyrjstatus.getStates_daily().get(size-6).getOr()),
            active1-Float.parseFloat(dailyrjstatus.getStates_daily().get(size-8).getOr())-Float.parseFloat(dailyrjstatus.getStates_daily().get(size-5).getOr())-Float.parseFloat(dailyrjstatus.getStates_daily().get(size-7).getOr())-Float.parseFloat(dailyrjstatus.getStates_daily().get(size-4).getOr())-Float.parseFloat(dailyrjstatus.getStates_daily().get(size-8).getOr())-Float.parseFloat(dailyrjstatus.getStates_daily().get(size-5).getOr()),
            active1-Float.parseFloat(dailyrjstatus.getStates_daily().get(size-11).getOr())-Float.parseFloat(dailyrjstatus.getStates_daily().get(size-8).getOr())-Float.parseFloat(dailyrjstatus.getStates_daily().get(size-5).getOr())-Float.parseFloat(dailyrjstatus.getStates_daily().get(size-11).getOr())-Float.parseFloat(dailyrjstatus.getStates_daily().get(size-8).getOr())-Float.parseFloat(dailyrjstatus.getStates_daily().get(size-5).getOr())-Float.parseFloat(dailyrjstatus.getStates_daily().get(size-10).getOr())-Float.parseFloat(dailyrjstatus.getStates_daily().get(size-7).getOr())-Float.parseFloat(dailyrjstatus.getStates_daily().get(size-4).getOr()),

            active1-Float.parseFloat(dailyrjstatus.getStates_daily().get(size-14).getOr())-Float.parseFloat(dailyrjstatus.getStates_daily().get(size-11).getOr())-Float.parseFloat(dailyrjstatus.getStates_daily().get(size-8).getOr())-Float.parseFloat(dailyrjstatus.getStates_daily().get(size-5).getOr())-Float.parseFloat(dailyrjstatus.getStates_daily().get(size-14).getOr())-Float.parseFloat(dailyrjstatus.getStates_daily().get(size-11).getOr())-Float.parseFloat(dailyrjstatus.getStates_daily().get(size-8).getOr())-Float.parseFloat(dailyrjstatus.getStates_daily().get(size-5).getOr())-Float.parseFloat(dailyrjstatus.getStates_daily().get(size-13).getOr())-Float.parseFloat(dailyrjstatus.getStates_daily().get(size-10).getOr())-Float.parseFloat(dailyrjstatus.getStates_daily().get(size-7).getOr())-Float.parseFloat(dailyrjstatus.getStates_daily().get(size-4).getOr()),




    };

    activegraphview.setData(active);



//   data.add(new BarEntry(Float.parseFloat(dailyrjstatus.getStates_daily().get(size-3).getDate()),Float.parseFloat(dailyrjstatus.getStates_daily().get(size-3).getOr())));
//        data.add(new BarEntry(Float.parseFloat(dailyrjstatus.getStates_daily().get(size-6).getDate()),));
//        data.add(new BarEntry(Float.parseFloat(dailyrjstatus.getStates_daily().get(size-9).getDate()),Float.parseFloat(dailyrjstatus.getStates_daily().get(size-9).getOr())));
//        data.add(new BarEntry(Float.parseFloat(dailyrjstatus.getStates_daily().get(size-12).getDate()),Float.parseFloat(dailyrjstatus.getStates_daily().get(size-12).getOr())));
//        data.add(new BarEntry(Float.parseFloat(dailyrjstatus.getStates_daily().get(size-15).getDate()),Float.parseFloat(dailyrjstatus.getStates_daily().get(size-15).getOr())));
//        data.add(new BarEntry(Float.parseFloat(dailyrjstatus.getStates_daily().get(size-18).getDate()),Float.parseFloat(dailyrjstatus.getStates_daily().get(size-18).getOr())));
//        data.add(new BarEntry(Float.parseFloat(dailyrjstatus.getStates_daily().get(size-21).getDate()),Float.parseFloat(dailyrjstatus.getStates_daily().get(size-21).getOr())));
//        BarDataSet barDataSet=new BarDataSet(data,"Cases");
//        BarData barData=new BarData();
//        barData.addDataSet(barDataSet);
//        chart.setData(barData);
//        chart.invalidate();
    float graph1[]=  new float[]{Float.parseFloat(dailyrjstatus.getStates_daily().get(size-3).getOr()),
            Float.parseFloat(dailyrjstatus.getStates_daily().get(size-6).getOr()),
            Float.parseFloat(dailyrjstatus.getStates_daily().get(size-9).getOr()),
            Float.parseFloat(dailyrjstatus.getStates_daily().get(size-12).getOr()),
            Float.parseFloat(dailyrjstatus.getStates_daily().get(size-15).getOr()),
            Float.parseFloat(dailyrjstatus.getStates_daily().get(size-18).getOr()),
            Float.parseFloat(dailyrjstatus.getStates_daily().get(size-21).getOr())
    };
    String array[] = new String[]{dailyrjstatus.getStates_daily().get(size - 3).getDate(),
            dailyrjstatus.getStates_daily().get(size - 6).getDate(),
            dailyrjstatus.getStates_daily().get(size - 9).getDate(),
            dailyrjstatus.getStates_daily().get(size - 12).getDate(),
            dailyrjstatus.getStates_daily().get(size - 15).getDate(),
            dailyrjstatus.getStates_daily().get(size - 18).getDate(),
            dailyrjstatus.getStates_daily().get(size - 21).getDate()};
    ChartEntity first = new ChartEntity(Color.WHITE, graph1);
    ArrayList<ChartEntity> list = new ArrayList<>();
    list.add(first);
    chart.setLegend(Arrays.asList(array));
    chart.setList(list);



}
else if(statecode.equalsIgnoreCase("pb"))
{

    float afectedcasep[]=  new float[]{affected1-Float.parseFloat(dailyrjstatus.getStates_daily().get(size-6).getPb()),
            affected1-Float.parseFloat(dailyrjstatus.getStates_daily().get(size-9).getPb())-Float.parseFloat(dailyrjstatus.getStates_daily().get(size-6).getPb()),
            affected1-Float.parseFloat(dailyrjstatus.getStates_daily().get(size-12).getPb())-Float.parseFloat(dailyrjstatus.getStates_daily().get(size-9).getPb())-Float.parseFloat(dailyrjstatus.getStates_daily().get(size-6).getPb()),
            affected1-Float.parseFloat(dailyrjstatus.getStates_daily().get(size-15).getPb())-Float.parseFloat(dailyrjstatus.getStates_daily().get(size-12).getPb())-Float.parseFloat(dailyrjstatus.getStates_daily().get(size-9).getPb())-Float.parseFloat(dailyrjstatus.getStates_daily().get(size-6).getPb()),
            affected1-Float.parseFloat(dailyrjstatus.getStates_daily().get(size-18).getPb())-Float.parseFloat(dailyrjstatus.getStates_daily().get(size-15).getPb())-Float.parseFloat(dailyrjstatus.getStates_daily().get(size-12).getPb())-Float.parseFloat(dailyrjstatus.getStates_daily().get(size-9).getPb())-Float.parseFloat(dailyrjstatus.getStates_daily().get(size-6).getPb()),
            affected1-Float.parseFloat(dailyrjstatus.getStates_daily().get(size-21).getPb())-Float.parseFloat(dailyrjstatus.getStates_daily().get(size-18).getPb())-Float.parseFloat(dailyrjstatus.getStates_daily().get(size-15).getPb())-Float.parseFloat(dailyrjstatus.getStates_daily().get(size-12).getPb())-Float.parseFloat(dailyrjstatus.getStates_daily().get(size-9).getPb())-Float.parseFloat(dailyrjstatus.getStates_daily().get(size-6).getPb())

    };


    affectedGraphView.setData(afectedcasep);
    float deceased[]=  new float[]{dead1-Float.parseFloat(dailyrjstatus.getStates_daily().get(size-4).getPb()),
            dead1-Float.parseFloat(dailyrjstatus.getStates_daily().get(size-7).getPb())-Float.parseFloat(dailyrjstatus.getStates_daily().get(size-4).getPb()),
            dead1-Float.parseFloat(dailyrjstatus.getStates_daily().get(size-10).getPb())-Float.parseFloat(dailyrjstatus.getStates_daily().get(size-7).getPb())-Float.parseFloat(dailyrjstatus.getStates_daily().get(size-4).getPb()),
            dead1-Float.parseFloat(dailyrjstatus.getStates_daily().get(size-13).getPb())-Float.parseFloat(dailyrjstatus.getStates_daily().get(size-10).getPb())-Float.parseFloat(dailyrjstatus.getStates_daily().get(size-7).getPb())-Float.parseFloat(dailyrjstatus.getStates_daily().get(size-4).getPb()),
            dead1-Float.parseFloat(dailyrjstatus.getStates_daily().get(size-16).getPb())-Float.parseFloat(dailyrjstatus.getStates_daily().get(size-13).getPb())-Float.parseFloat(dailyrjstatus.getStates_daily().get(size-10).getPb())-Float.parseFloat(dailyrjstatus.getStates_daily().get(size-7).getPb())-Float.parseFloat(dailyrjstatus.getStates_daily().get(size-4).getPb()),
            dead1-Float.parseFloat(dailyrjstatus.getStates_daily().get(size-19).getPb())-Float.parseFloat(dailyrjstatus.getStates_daily().get(size-16).getPb())-Float.parseFloat(dailyrjstatus.getStates_daily().get(size-13).getPb())-Float.parseFloat(dailyrjstatus.getStates_daily().get(size-10).getPb())-Float.parseFloat(dailyrjstatus.getStates_daily().get(size-7).getPb())-Float.parseFloat(dailyrjstatus.getStates_daily().get(size-4).getPb())

    };


    deadgraph.setData(deceased);
    float recov[]=  new float[]{recovered1-Float.parseFloat(dailyrjstatus.getStates_daily().get(size-5).getPb()),
            recovered1-Float.parseFloat(dailyrjstatus.getStates_daily().get(size-8).getPb())-Float.parseFloat(dailyrjstatus.getStates_daily().get(size-5).getPb()),
            recovered1-Float.parseFloat(dailyrjstatus.getStates_daily().get(size-11).getPb())-Float.parseFloat(dailyrjstatus.getStates_daily().get(size-8).getPb())-Float.parseFloat(dailyrjstatus.getStates_daily().get(size-5).getPb()),
            recovered1-Float.parseFloat(dailyrjstatus.getStates_daily().get(size-14).getPb())-Float.parseFloat(dailyrjstatus.getStates_daily().get(size-11).getPb())-Float.parseFloat(dailyrjstatus.getStates_daily().get(size-8).getPb())-Float.parseFloat(dailyrjstatus.getStates_daily().get(size-5).getPb()),
            recovered1-Float.parseFloat(dailyrjstatus.getStates_daily().get(size-17).getPb())-Float.parseFloat(dailyrjstatus.getStates_daily().get(size-14).getPb())-Float.parseFloat(dailyrjstatus.getStates_daily().get(size-11).getPb())-Float.parseFloat(dailyrjstatus.getStates_daily().get(size-8).getPb())-Float.parseFloat(dailyrjstatus.getStates_daily().get(size-5).getPb()),
            recovered1-Float.parseFloat(dailyrjstatus.getStates_daily().get(size-20).getPb())-Float.parseFloat(dailyrjstatus.getStates_daily().get(size-17).getPb())-Float.parseFloat(dailyrjstatus.getStates_daily().get(size-14).getPb())-Float.parseFloat(dailyrjstatus.getStates_daily().get(size-11).getPb())-Float.parseFloat(dailyrjstatus.getStates_daily().get(size-8).getPb())-Float.parseFloat(dailyrjstatus.getStates_daily().get(size-5).getPb())

    };
    recovgraph.setData(null);// for example
    recovgraph.setData(recov);
    float active[]=  new float[]{active1-Float.parseFloat(dailyrjstatus.getStates_daily().get(size-5).getPb())-Float.parseFloat(dailyrjstatus.getStates_daily().get(size-4).getPb())-Float.parseFloat(dailyrjstatus.getStates_daily().get(size-6).getPb()),
            active1-Float.parseFloat(dailyrjstatus.getStates_daily().get(size-8).getPb())-Float.parseFloat(dailyrjstatus.getStates_daily().get(size-5).getPb())-Float.parseFloat(dailyrjstatus.getStates_daily().get(size-7).getPb())-Float.parseFloat(dailyrjstatus.getStates_daily().get(size-4).getPb())-Float.parseFloat(dailyrjstatus.getStates_daily().get(size-8).getPb())-Float.parseFloat(dailyrjstatus.getStates_daily().get(size-5).getPb()),
            active1-Float.parseFloat(dailyrjstatus.getStates_daily().get(size-11).getPb())-Float.parseFloat(dailyrjstatus.getStates_daily().get(size-8).getPb())-Float.parseFloat(dailyrjstatus.getStates_daily().get(size-5).getPb())-Float.parseFloat(dailyrjstatus.getStates_daily().get(size-11).getPb())-Float.parseFloat(dailyrjstatus.getStates_daily().get(size-8).getPb())-Float.parseFloat(dailyrjstatus.getStates_daily().get(size-5).getPb())-Float.parseFloat(dailyrjstatus.getStates_daily().get(size-10).getPb())-Float.parseFloat(dailyrjstatus.getStates_daily().get(size-7).getPb())-Float.parseFloat(dailyrjstatus.getStates_daily().get(size-4).getPb()),

            active1-Float.parseFloat(dailyrjstatus.getStates_daily().get(size-14).getPb())-Float.parseFloat(dailyrjstatus.getStates_daily().get(size-11).getPb())-Float.parseFloat(dailyrjstatus.getStates_daily().get(size-8).getPb())-Float.parseFloat(dailyrjstatus.getStates_daily().get(size-5).getPb())-Float.parseFloat(dailyrjstatus.getStates_daily().get(size-14).getPb())-Float.parseFloat(dailyrjstatus.getStates_daily().get(size-11).getPb())-Float.parseFloat(dailyrjstatus.getStates_daily().get(size-8).getPb())-Float.parseFloat(dailyrjstatus.getStates_daily().get(size-5).getPb())-Float.parseFloat(dailyrjstatus.getStates_daily().get(size-13).getPb())-Float.parseFloat(dailyrjstatus.getStates_daily().get(size-10).getPb())-Float.parseFloat(dailyrjstatus.getStates_daily().get(size-7).getPb())-Float.parseFloat(dailyrjstatus.getStates_daily().get(size-4).getPb()),




    };

    activegraphview.setData(active);



//   data.add(new BarEntry(Float.parseFloat(dailyrjstatus.getStates_daily().get(size-3).getDate()),Float.parseFloat(dailyrjstatus.getStates_daily().get(size-3).getPb())));
//        data.add(new BarEntry(Float.parseFloat(dailyrjstatus.getStates_daily().get(size-6).getDate()),));
//        data.add(new BarEntry(Float.parseFloat(dailyrjstatus.getStates_daily().get(size-9).getDate()),Float.parseFloat(dailyrjstatus.getStates_daily().get(size-9).getPb())));
//        data.add(new BarEntry(Float.parseFloat(dailyrjstatus.getStates_daily().get(size-12).getDate()),Float.parseFloat(dailyrjstatus.getStates_daily().get(size-12).getPb())));
//        data.add(new BarEntry(Float.parseFloat(dailyrjstatus.getStates_daily().get(size-15).getDate()),Float.parseFloat(dailyrjstatus.getStates_daily().get(size-15).getPb())));
//        data.add(new BarEntry(Float.parseFloat(dailyrjstatus.getStates_daily().get(size-18).getDate()),Float.parseFloat(dailyrjstatus.getStates_daily().get(size-18).getPb())));
//        data.add(new BarEntry(Float.parseFloat(dailyrjstatus.getStates_daily().get(size-21).getDate()),Float.parseFloat(dailyrjstatus.getStates_daily().get(size-21).getPb())));
//        BarDataSet barDataSet=new BarDataSet(data,"Cases");
//        BarData barData=new BarData();
//        barData.addDataSet(barDataSet);
//        chart.setData(barData);
//        chart.invalidate();
    float graph1[]=  new float[]{Float.parseFloat(dailyrjstatus.getStates_daily().get(size-3).getPb()),
            Float.parseFloat(dailyrjstatus.getStates_daily().get(size-6).getPb()),
            Float.parseFloat(dailyrjstatus.getStates_daily().get(size-9).getPb()),
            Float.parseFloat(dailyrjstatus.getStates_daily().get(size-12).getPb()),
            Float.parseFloat(dailyrjstatus.getStates_daily().get(size-15).getPb()),
            Float.parseFloat(dailyrjstatus.getStates_daily().get(size-18).getPb()),
            Float.parseFloat(dailyrjstatus.getStates_daily().get(size-21).getPb())
    };
    String array[] = new String[]{dailyrjstatus.getStates_daily().get(size - 3).getDate(),
            dailyrjstatus.getStates_daily().get(size - 6).getDate(),
            dailyrjstatus.getStates_daily().get(size - 9).getDate(),
            dailyrjstatus.getStates_daily().get(size - 12).getDate(),
            dailyrjstatus.getStates_daily().get(size - 15).getDate(),
            dailyrjstatus.getStates_daily().get(size - 18).getDate(),
            dailyrjstatus.getStates_daily().get(size - 21).getDate()};
    ChartEntity first = new ChartEntity(Color.WHITE, graph1);
    ArrayList<ChartEntity> list = new ArrayList<>();
    list.add(first);
    chart.setLegend(Arrays.asList(array));
    chart.setList(list);



}



else if(statecode.equalsIgnoreCase("tn"))
{

    float afectedcasep[]=  new float[]{affected1-Float.parseFloat(dailyrjstatus.getStates_daily().get(size-6).getTn()),
            affected1-Float.parseFloat(dailyrjstatus.getStates_daily().get(size-9).getTn())-Float.parseFloat(dailyrjstatus.getStates_daily().get(size-6).getTn()),
            affected1-Float.parseFloat(dailyrjstatus.getStates_daily().get(size-12).getTn())-Float.parseFloat(dailyrjstatus.getStates_daily().get(size-9).getTn())-Float.parseFloat(dailyrjstatus.getStates_daily().get(size-6).getTn()),
            affected1-Float.parseFloat(dailyrjstatus.getStates_daily().get(size-15).getTn())-Float.parseFloat(dailyrjstatus.getStates_daily().get(size-12).getTn())-Float.parseFloat(dailyrjstatus.getStates_daily().get(size-9).getTn())-Float.parseFloat(dailyrjstatus.getStates_daily().get(size-6).getTn()),
            affected1-Float.parseFloat(dailyrjstatus.getStates_daily().get(size-18).getTn())-Float.parseFloat(dailyrjstatus.getStates_daily().get(size-15).getTn())-Float.parseFloat(dailyrjstatus.getStates_daily().get(size-12).getTn())-Float.parseFloat(dailyrjstatus.getStates_daily().get(size-9).getTn())-Float.parseFloat(dailyrjstatus.getStates_daily().get(size-6).getTn()),
            affected1-Float.parseFloat(dailyrjstatus.getStates_daily().get(size-21).getTn())-Float.parseFloat(dailyrjstatus.getStates_daily().get(size-18).getTn())-Float.parseFloat(dailyrjstatus.getStates_daily().get(size-15).getTn())-Float.parseFloat(dailyrjstatus.getStates_daily().get(size-12).getTn())-Float.parseFloat(dailyrjstatus.getStates_daily().get(size-9).getTn())-Float.parseFloat(dailyrjstatus.getStates_daily().get(size-6).getTn())

    };


    affectedGraphView.setData(afectedcasep);
    float deceased[]=  new float[]{dead1-Float.parseFloat(dailyrjstatus.getStates_daily().get(size-4).getTn()),
            dead1-Float.parseFloat(dailyrjstatus.getStates_daily().get(size-7).getTn())-Float.parseFloat(dailyrjstatus.getStates_daily().get(size-4).getTn()),
            dead1-Float.parseFloat(dailyrjstatus.getStates_daily().get(size-10).getTn())-Float.parseFloat(dailyrjstatus.getStates_daily().get(size-7).getTn())-Float.parseFloat(dailyrjstatus.getStates_daily().get(size-4).getTn()),
            dead1-Float.parseFloat(dailyrjstatus.getStates_daily().get(size-13).getTn())-Float.parseFloat(dailyrjstatus.getStates_daily().get(size-10).getTn())-Float.parseFloat(dailyrjstatus.getStates_daily().get(size-7).getTn())-Float.parseFloat(dailyrjstatus.getStates_daily().get(size-4).getTn()),
            dead1-Float.parseFloat(dailyrjstatus.getStates_daily().get(size-16).getTn())-Float.parseFloat(dailyrjstatus.getStates_daily().get(size-13).getTn())-Float.parseFloat(dailyrjstatus.getStates_daily().get(size-10).getTn())-Float.parseFloat(dailyrjstatus.getStates_daily().get(size-7).getTn())-Float.parseFloat(dailyrjstatus.getStates_daily().get(size-4).getTn()),
            dead1-Float.parseFloat(dailyrjstatus.getStates_daily().get(size-19).getTn())-Float.parseFloat(dailyrjstatus.getStates_daily().get(size-16).getTn())-Float.parseFloat(dailyrjstatus.getStates_daily().get(size-13).getTn())-Float.parseFloat(dailyrjstatus.getStates_daily().get(size-10).getTn())-Float.parseFloat(dailyrjstatus.getStates_daily().get(size-7).getTn())-Float.parseFloat(dailyrjstatus.getStates_daily().get(size-4).getTn())

    };


    deadgraph.setData(deceased);
    float recov[]=  new float[]{recovered1-Float.parseFloat(dailyrjstatus.getStates_daily().get(size-5).getTn()),
            recovered1-Float.parseFloat(dailyrjstatus.getStates_daily().get(size-8).getTn())-Float.parseFloat(dailyrjstatus.getStates_daily().get(size-5).getTn()),
            recovered1-Float.parseFloat(dailyrjstatus.getStates_daily().get(size-11).getTn())-Float.parseFloat(dailyrjstatus.getStates_daily().get(size-8).getTn())-Float.parseFloat(dailyrjstatus.getStates_daily().get(size-5).getTn()),
            recovered1-Float.parseFloat(dailyrjstatus.getStates_daily().get(size-14).getTn())-Float.parseFloat(dailyrjstatus.getStates_daily().get(size-11).getTn())-Float.parseFloat(dailyrjstatus.getStates_daily().get(size-8).getTn())-Float.parseFloat(dailyrjstatus.getStates_daily().get(size-5).getTn()),
            recovered1-Float.parseFloat(dailyrjstatus.getStates_daily().get(size-17).getTn())-Float.parseFloat(dailyrjstatus.getStates_daily().get(size-14).getTn())-Float.parseFloat(dailyrjstatus.getStates_daily().get(size-11).getTn())-Float.parseFloat(dailyrjstatus.getStates_daily().get(size-8).getTn())-Float.parseFloat(dailyrjstatus.getStates_daily().get(size-5).getTn()),
            recovered1-Float.parseFloat(dailyrjstatus.getStates_daily().get(size-20).getTn())-Float.parseFloat(dailyrjstatus.getStates_daily().get(size-17).getTn())-Float.parseFloat(dailyrjstatus.getStates_daily().get(size-14).getTn())-Float.parseFloat(dailyrjstatus.getStates_daily().get(size-11).getTn())-Float.parseFloat(dailyrjstatus.getStates_daily().get(size-8).getTn())-Float.parseFloat(dailyrjstatus.getStates_daily().get(size-5).getTn())

    };
    recovgraph.setData(null);// for example
    recovgraph.setData(recov);
    float active[]=  new float[]{active1-Float.parseFloat(dailyrjstatus.getStates_daily().get(size-5).getTn())-Float.parseFloat(dailyrjstatus.getStates_daily().get(size-4).getTn())-Float.parseFloat(dailyrjstatus.getStates_daily().get(size-6).getTn()),
            active1-Float.parseFloat(dailyrjstatus.getStates_daily().get(size-8).getTn())-Float.parseFloat(dailyrjstatus.getStates_daily().get(size-5).getTn())-Float.parseFloat(dailyrjstatus.getStates_daily().get(size-7).getTn())-Float.parseFloat(dailyrjstatus.getStates_daily().get(size-4).getTn())-Float.parseFloat(dailyrjstatus.getStates_daily().get(size-8).getTn())-Float.parseFloat(dailyrjstatus.getStates_daily().get(size-5).getTn()),
            active1-Float.parseFloat(dailyrjstatus.getStates_daily().get(size-11).getTn())-Float.parseFloat(dailyrjstatus.getStates_daily().get(size-8).getTn())-Float.parseFloat(dailyrjstatus.getStates_daily().get(size-5).getTn())-Float.parseFloat(dailyrjstatus.getStates_daily().get(size-11).getTn())-Float.parseFloat(dailyrjstatus.getStates_daily().get(size-8).getTn())-Float.parseFloat(dailyrjstatus.getStates_daily().get(size-5).getTn())-Float.parseFloat(dailyrjstatus.getStates_daily().get(size-10).getTn())-Float.parseFloat(dailyrjstatus.getStates_daily().get(size-7).getTn())-Float.parseFloat(dailyrjstatus.getStates_daily().get(size-4).getTn()),

            active1-Float.parseFloat(dailyrjstatus.getStates_daily().get(size-14).getTn())-Float.parseFloat(dailyrjstatus.getStates_daily().get(size-11).getTn())-Float.parseFloat(dailyrjstatus.getStates_daily().get(size-8).getTn())-Float.parseFloat(dailyrjstatus.getStates_daily().get(size-5).getTn())-Float.parseFloat(dailyrjstatus.getStates_daily().get(size-14).getTn())-Float.parseFloat(dailyrjstatus.getStates_daily().get(size-11).getTn())-Float.parseFloat(dailyrjstatus.getStates_daily().get(size-8).getTn())-Float.parseFloat(dailyrjstatus.getStates_daily().get(size-5).getTn())-Float.parseFloat(dailyrjstatus.getStates_daily().get(size-13).getTn())-Float.parseFloat(dailyrjstatus.getStates_daily().get(size-10).getTn())-Float.parseFloat(dailyrjstatus.getStates_daily().get(size-7).getTn())-Float.parseFloat(dailyrjstatus.getStates_daily().get(size-4).getTn()),




    };

    activegraphview.setData(active);



//   data.add(new BarEntry(Float.parseFloat(dailyrjstatus.getStates_daily().get(size-3).getDate()),Float.parseFloat(dailyrjstatus.getStates_daily().get(size-3).getTn())));
//        data.add(new BarEntry(Float.parseFloat(dailyrjstatus.getStates_daily().get(size-6).getDate()),));
//        data.add(new BarEntry(Float.parseFloat(dailyrjstatus.getStates_daily().get(size-9).getDate()),Float.parseFloat(dailyrjstatus.getStates_daily().get(size-9).getTn())));
//        data.add(new BarEntry(Float.parseFloat(dailyrjstatus.getStates_daily().get(size-12).getDate()),Float.parseFloat(dailyrjstatus.getStates_daily().get(size-12).getTn())));
//        data.add(new BarEntry(Float.parseFloat(dailyrjstatus.getStates_daily().get(size-15).getDate()),Float.parseFloat(dailyrjstatus.getStates_daily().get(size-15).getTn())));
//        data.add(new BarEntry(Float.parseFloat(dailyrjstatus.getStates_daily().get(size-18).getDate()),Float.parseFloat(dailyrjstatus.getStates_daily().get(size-18).getTn())));
//        data.add(new BarEntry(Float.parseFloat(dailyrjstatus.getStates_daily().get(size-21).getDate()),Float.parseFloat(dailyrjstatus.getStates_daily().get(size-21).getTn())));
//        BarDataSet barDataSet=new BarDataSet(data,"Cases");
//        BarData barData=new BarData();
//        barData.addDataSet(barDataSet);
//        chart.setData(barData);
//        chart.invalidate();
    float graph1[]=  new float[]{Float.parseFloat(dailyrjstatus.getStates_daily().get(size-3).getTn()),
            Float.parseFloat(dailyrjstatus.getStates_daily().get(size-6).getTn()),
            Float.parseFloat(dailyrjstatus.getStates_daily().get(size-9).getTn()),
            Float.parseFloat(dailyrjstatus.getStates_daily().get(size-12).getTn()),
            Float.parseFloat(dailyrjstatus.getStates_daily().get(size-15).getTn()),
            Float.parseFloat(dailyrjstatus.getStates_daily().get(size-18).getTn()),
            Float.parseFloat(dailyrjstatus.getStates_daily().get(size-21).getTn())
    };
    String array[] = new String[]{dailyrjstatus.getStates_daily().get(size - 3).getDate(),
            dailyrjstatus.getStates_daily().get(size - 6).getDate(),
            dailyrjstatus.getStates_daily().get(size - 9).getDate(),
            dailyrjstatus.getStates_daily().get(size - 12).getDate(),
            dailyrjstatus.getStates_daily().get(size - 15).getDate(),
            dailyrjstatus.getStates_daily().get(size - 18).getDate(),
            dailyrjstatus.getStates_daily().get(size - 21).getDate()};
    ChartEntity first = new ChartEntity(Color.WHITE, graph1);
    ArrayList<ChartEntity> list = new ArrayList<>();
    list.add(first);
    chart.setLegend(Arrays.asList(array));
    chart.setList(list);



}


else if(statecode.equalsIgnoreCase("up"))
{

    float afectedcasep[]=  new float[]{affected1-Float.parseFloat(dailyrjstatus.getStates_daily().get(size-6).getUp()),
            affected1-Float.parseFloat(dailyrjstatus.getStates_daily().get(size-9).getUp())-Float.parseFloat(dailyrjstatus.getStates_daily().get(size-6).getUp()),
            affected1-Float.parseFloat(dailyrjstatus.getStates_daily().get(size-12).getUp())-Float.parseFloat(dailyrjstatus.getStates_daily().get(size-9).getUp())-Float.parseFloat(dailyrjstatus.getStates_daily().get(size-6).getUp()),
            affected1-Float.parseFloat(dailyrjstatus.getStates_daily().get(size-15).getUp())-Float.parseFloat(dailyrjstatus.getStates_daily().get(size-12).getUp())-Float.parseFloat(dailyrjstatus.getStates_daily().get(size-9).getUp())-Float.parseFloat(dailyrjstatus.getStates_daily().get(size-6).getUp()),
            affected1-Float.parseFloat(dailyrjstatus.getStates_daily().get(size-18).getUp())-Float.parseFloat(dailyrjstatus.getStates_daily().get(size-15).getUp())-Float.parseFloat(dailyrjstatus.getStates_daily().get(size-12).getUp())-Float.parseFloat(dailyrjstatus.getStates_daily().get(size-9).getUp())-Float.parseFloat(dailyrjstatus.getStates_daily().get(size-6).getUp()),
            affected1-Float.parseFloat(dailyrjstatus.getStates_daily().get(size-21).getUp())-Float.parseFloat(dailyrjstatus.getStates_daily().get(size-18).getUp())-Float.parseFloat(dailyrjstatus.getStates_daily().get(size-15).getUp())-Float.parseFloat(dailyrjstatus.getStates_daily().get(size-12).getUp())-Float.parseFloat(dailyrjstatus.getStates_daily().get(size-9).getUp())-Float.parseFloat(dailyrjstatus.getStates_daily().get(size-6).getUp())

    };


    affectedGraphView.setData(afectedcasep);
    float deceased[]=  new float[]{dead1-Float.parseFloat(dailyrjstatus.getStates_daily().get(size-4).getUp()),
            dead1-Float.parseFloat(dailyrjstatus.getStates_daily().get(size-7).getUp())-Float.parseFloat(dailyrjstatus.getStates_daily().get(size-4).getUp()),
            dead1-Float.parseFloat(dailyrjstatus.getStates_daily().get(size-10).getUp())-Float.parseFloat(dailyrjstatus.getStates_daily().get(size-7).getUp())-Float.parseFloat(dailyrjstatus.getStates_daily().get(size-4).getUp()),
            dead1-Float.parseFloat(dailyrjstatus.getStates_daily().get(size-13).getUp())-Float.parseFloat(dailyrjstatus.getStates_daily().get(size-10).getUp())-Float.parseFloat(dailyrjstatus.getStates_daily().get(size-7).getUp())-Float.parseFloat(dailyrjstatus.getStates_daily().get(size-4).getUp()),
            dead1-Float.parseFloat(dailyrjstatus.getStates_daily().get(size-16).getUp())-Float.parseFloat(dailyrjstatus.getStates_daily().get(size-13).getUp())-Float.parseFloat(dailyrjstatus.getStates_daily().get(size-10).getUp())-Float.parseFloat(dailyrjstatus.getStates_daily().get(size-7).getUp())-Float.parseFloat(dailyrjstatus.getStates_daily().get(size-4).getUp()),
            dead1-Float.parseFloat(dailyrjstatus.getStates_daily().get(size-19).getUp())-Float.parseFloat(dailyrjstatus.getStates_daily().get(size-16).getUp())-Float.parseFloat(dailyrjstatus.getStates_daily().get(size-13).getUp())-Float.parseFloat(dailyrjstatus.getStates_daily().get(size-10).getUp())-Float.parseFloat(dailyrjstatus.getStates_daily().get(size-7).getUp())-Float.parseFloat(dailyrjstatus.getStates_daily().get(size-4).getUp())

    };


    deadgraph.setData(deceased);
    float recov[]=  new float[]{recovered1-Float.parseFloat(dailyrjstatus.getStates_daily().get(size-5).getUp()),
            recovered1-Float.parseFloat(dailyrjstatus.getStates_daily().get(size-8).getUp())-Float.parseFloat(dailyrjstatus.getStates_daily().get(size-5).getUp()),
            recovered1-Float.parseFloat(dailyrjstatus.getStates_daily().get(size-11).getUp())-Float.parseFloat(dailyrjstatus.getStates_daily().get(size-8).getUp())-Float.parseFloat(dailyrjstatus.getStates_daily().get(size-5).getUp()),
            recovered1-Float.parseFloat(dailyrjstatus.getStates_daily().get(size-14).getUp())-Float.parseFloat(dailyrjstatus.getStates_daily().get(size-11).getUp())-Float.parseFloat(dailyrjstatus.getStates_daily().get(size-8).getUp())-Float.parseFloat(dailyrjstatus.getStates_daily().get(size-5).getUp()),
            recovered1-Float.parseFloat(dailyrjstatus.getStates_daily().get(size-17).getUp())-Float.parseFloat(dailyrjstatus.getStates_daily().get(size-14).getUp())-Float.parseFloat(dailyrjstatus.getStates_daily().get(size-11).getUp())-Float.parseFloat(dailyrjstatus.getStates_daily().get(size-8).getUp())-Float.parseFloat(dailyrjstatus.getStates_daily().get(size-5).getUp()),
            recovered1-Float.parseFloat(dailyrjstatus.getStates_daily().get(size-20).getUp())-Float.parseFloat(dailyrjstatus.getStates_daily().get(size-17).getUp())-Float.parseFloat(dailyrjstatus.getStates_daily().get(size-14).getUp())-Float.parseFloat(dailyrjstatus.getStates_daily().get(size-11).getUp())-Float.parseFloat(dailyrjstatus.getStates_daily().get(size-8).getUp())-Float.parseFloat(dailyrjstatus.getStates_daily().get(size-5).getUp())

    };
    recovgraph.setData(null);// for example
    recovgraph.setData(recov);
    float active[]=  new float[]{active1-Float.parseFloat(dailyrjstatus.getStates_daily().get(size-5).getUp())-Float.parseFloat(dailyrjstatus.getStates_daily().get(size-4).getUp())-Float.parseFloat(dailyrjstatus.getStates_daily().get(size-6).getUp()),
            active1-Float.parseFloat(dailyrjstatus.getStates_daily().get(size-8).getUp())-Float.parseFloat(dailyrjstatus.getStates_daily().get(size-5).getUp())-Float.parseFloat(dailyrjstatus.getStates_daily().get(size-7).getUp())-Float.parseFloat(dailyrjstatus.getStates_daily().get(size-4).getUp())-Float.parseFloat(dailyrjstatus.getStates_daily().get(size-8).getUp())-Float.parseFloat(dailyrjstatus.getStates_daily().get(size-5).getUp()),
            active1-Float.parseFloat(dailyrjstatus.getStates_daily().get(size-11).getUp())-Float.parseFloat(dailyrjstatus.getStates_daily().get(size-8).getUp())-Float.parseFloat(dailyrjstatus.getStates_daily().get(size-5).getUp())-Float.parseFloat(dailyrjstatus.getStates_daily().get(size-11).getUp())-Float.parseFloat(dailyrjstatus.getStates_daily().get(size-8).getUp())-Float.parseFloat(dailyrjstatus.getStates_daily().get(size-5).getUp())-Float.parseFloat(dailyrjstatus.getStates_daily().get(size-10).getUp())-Float.parseFloat(dailyrjstatus.getStates_daily().get(size-7).getUp())-Float.parseFloat(dailyrjstatus.getStates_daily().get(size-4).getUp()),

            active1-Float.parseFloat(dailyrjstatus.getStates_daily().get(size-14).getUp())-Float.parseFloat(dailyrjstatus.getStates_daily().get(size-11).getUp())-Float.parseFloat(dailyrjstatus.getStates_daily().get(size-8).getUp())-Float.parseFloat(dailyrjstatus.getStates_daily().get(size-5).getUp())-Float.parseFloat(dailyrjstatus.getStates_daily().get(size-14).getUp())-Float.parseFloat(dailyrjstatus.getStates_daily().get(size-11).getUp())-Float.parseFloat(dailyrjstatus.getStates_daily().get(size-8).getUp())-Float.parseFloat(dailyrjstatus.getStates_daily().get(size-5).getUp())-Float.parseFloat(dailyrjstatus.getStates_daily().get(size-13).getUp())-Float.parseFloat(dailyrjstatus.getStates_daily().get(size-10).getUp())-Float.parseFloat(dailyrjstatus.getStates_daily().get(size-7).getUp())-Float.parseFloat(dailyrjstatus.getStates_daily().get(size-4).getUp()),




    };

    activegraphview.setData(active);



//   data.add(new BarEntry(Float.parseFloat(dailyrjstatus.getStates_daily().get(size-3).getDate()),Float.parseFloat(dailyrjstatus.getStates_daily().get(size-3).getUp())));
//        data.add(new BarEntry(Float.parseFloat(dailyrjstatus.getStates_daily().get(size-6).getDate()),));
//        data.add(new BarEntry(Float.parseFloat(dailyrjstatus.getStates_daily().get(size-9).getDate()),Float.parseFloat(dailyrjstatus.getStates_daily().get(size-9).getUp())));
//        data.add(new BarEntry(Float.parseFloat(dailyrjstatus.getStates_daily().get(size-12).getDate()),Float.parseFloat(dailyrjstatus.getStates_daily().get(size-12).getUp())));
//        data.add(new BarEntry(Float.parseFloat(dailyrjstatus.getStates_daily().get(size-15).getDate()),Float.parseFloat(dailyrjstatus.getStates_daily().get(size-15).getUp())));
//        data.add(new BarEntry(Float.parseFloat(dailyrjstatus.getStates_daily().get(size-18).getDate()),Float.parseFloat(dailyrjstatus.getStates_daily().get(size-18).getUp())));
//        data.add(new BarEntry(Float.parseFloat(dailyrjstatus.getStates_daily().get(size-21).getDate()),Float.parseFloat(dailyrjstatus.getStates_daily().get(size-21).getUp())));
//        BarDataSet barDataSet=new BarDataSet(data,"Cases");
//        BarData barData=new BarData();
//        barData.addDataSet(barDataSet);
//        chart.setData(barData);
//        chart.invalidate();
    float graph1[]=  new float[]{Float.parseFloat(dailyrjstatus.getStates_daily().get(size-3).getUp()),
            Float.parseFloat(dailyrjstatus.getStates_daily().get(size-6).getUp()),
            Float.parseFloat(dailyrjstatus.getStates_daily().get(size-9).getUp()),
            Float.parseFloat(dailyrjstatus.getStates_daily().get(size-12).getUp()),
            Float.parseFloat(dailyrjstatus.getStates_daily().get(size-15).getUp()),
            Float.parseFloat(dailyrjstatus.getStates_daily().get(size-18).getUp()),
            Float.parseFloat(dailyrjstatus.getStates_daily().get(size-21).getUp())
    };
    String array[] = new String[]{dailyrjstatus.getStates_daily().get(size - 3).getDate(),
            dailyrjstatus.getStates_daily().get(size - 6).getDate(),
            dailyrjstatus.getStates_daily().get(size - 9).getDate(),
            dailyrjstatus.getStates_daily().get(size - 12).getDate(),
            dailyrjstatus.getStates_daily().get(size - 15).getDate(),
            dailyrjstatus.getStates_daily().get(size - 18).getDate(),
            dailyrjstatus.getStates_daily().get(size - 21).getDate()};
    ChartEntity first = new ChartEntity(Color.WHITE, graph1);
    ArrayList<ChartEntity> list = new ArrayList<>();
    list.add(first);
    chart.setLegend(Arrays.asList(array));
    chart.setList(list);



}

else if(statecode.equalsIgnoreCase("ut"))
{

    float afectedcasep[]=  new float[]{affected1-Float.parseFloat(dailyrjstatus.getStates_daily().get(size-6).getUt()),
            affected1-Float.parseFloat(dailyrjstatus.getStates_daily().get(size-9).getUt())-Float.parseFloat(dailyrjstatus.getStates_daily().get(size-6).getUt()),
            affected1-Float.parseFloat(dailyrjstatus.getStates_daily().get(size-12).getUt())-Float.parseFloat(dailyrjstatus.getStates_daily().get(size-9).getUt())-Float.parseFloat(dailyrjstatus.getStates_daily().get(size-6).getUt()),
            affected1-Float.parseFloat(dailyrjstatus.getStates_daily().get(size-15).getUt())-Float.parseFloat(dailyrjstatus.getStates_daily().get(size-12).getUt())-Float.parseFloat(dailyrjstatus.getStates_daily().get(size-9).getUt())-Float.parseFloat(dailyrjstatus.getStates_daily().get(size-6).getUt()),
            affected1-Float.parseFloat(dailyrjstatus.getStates_daily().get(size-18).getUt())-Float.parseFloat(dailyrjstatus.getStates_daily().get(size-15).getUt())-Float.parseFloat(dailyrjstatus.getStates_daily().get(size-12).getUt())-Float.parseFloat(dailyrjstatus.getStates_daily().get(size-9).getUt())-Float.parseFloat(dailyrjstatus.getStates_daily().get(size-6).getUt()),
            affected1-Float.parseFloat(dailyrjstatus.getStates_daily().get(size-21).getUt())-Float.parseFloat(dailyrjstatus.getStates_daily().get(size-18).getUt())-Float.parseFloat(dailyrjstatus.getStates_daily().get(size-15).getUt())-Float.parseFloat(dailyrjstatus.getStates_daily().get(size-12).getUt())-Float.parseFloat(dailyrjstatus.getStates_daily().get(size-9).getUt())-Float.parseFloat(dailyrjstatus.getStates_daily().get(size-6).getUt())

    };


    affectedGraphView.setData(afectedcasep);
    float deceased[]=  new float[]{dead1-Float.parseFloat(dailyrjstatus.getStates_daily().get(size-4).getUt()),
            dead1-Float.parseFloat(dailyrjstatus.getStates_daily().get(size-7).getUt())-Float.parseFloat(dailyrjstatus.getStates_daily().get(size-4).getUt()),
            dead1-Float.parseFloat(dailyrjstatus.getStates_daily().get(size-10).getUt())-Float.parseFloat(dailyrjstatus.getStates_daily().get(size-7).getUt())-Float.parseFloat(dailyrjstatus.getStates_daily().get(size-4).getUt()),
            dead1-Float.parseFloat(dailyrjstatus.getStates_daily().get(size-13).getUt())-Float.parseFloat(dailyrjstatus.getStates_daily().get(size-10).getUt())-Float.parseFloat(dailyrjstatus.getStates_daily().get(size-7).getUt())-Float.parseFloat(dailyrjstatus.getStates_daily().get(size-4).getUt()),
            dead1-Float.parseFloat(dailyrjstatus.getStates_daily().get(size-16).getUt())-Float.parseFloat(dailyrjstatus.getStates_daily().get(size-13).getUt())-Float.parseFloat(dailyrjstatus.getStates_daily().get(size-10).getUt())-Float.parseFloat(dailyrjstatus.getStates_daily().get(size-7).getUt())-Float.parseFloat(dailyrjstatus.getStates_daily().get(size-4).getUt()),
            dead1-Float.parseFloat(dailyrjstatus.getStates_daily().get(size-19).getUt())-Float.parseFloat(dailyrjstatus.getStates_daily().get(size-16).getUt())-Float.parseFloat(dailyrjstatus.getStates_daily().get(size-13).getUt())-Float.parseFloat(dailyrjstatus.getStates_daily().get(size-10).getUt())-Float.parseFloat(dailyrjstatus.getStates_daily().get(size-7).getUt())-Float.parseFloat(dailyrjstatus.getStates_daily().get(size-4).getUt())

    };


    deadgraph.setData(deceased);
    float recov[]=  new float[]{recovered1-Float.parseFloat(dailyrjstatus.getStates_daily().get(size-5).getUt()),
            recovered1-Float.parseFloat(dailyrjstatus.getStates_daily().get(size-8).getUt())-Float.parseFloat(dailyrjstatus.getStates_daily().get(size-5).getUt()),
            recovered1-Float.parseFloat(dailyrjstatus.getStates_daily().get(size-11).getUt())-Float.parseFloat(dailyrjstatus.getStates_daily().get(size-8).getUt())-Float.parseFloat(dailyrjstatus.getStates_daily().get(size-5).getUt()),
            recovered1-Float.parseFloat(dailyrjstatus.getStates_daily().get(size-14).getUt())-Float.parseFloat(dailyrjstatus.getStates_daily().get(size-11).getUt())-Float.parseFloat(dailyrjstatus.getStates_daily().get(size-8).getUt())-Float.parseFloat(dailyrjstatus.getStates_daily().get(size-5).getUt()),
            recovered1-Float.parseFloat(dailyrjstatus.getStates_daily().get(size-17).getUt())-Float.parseFloat(dailyrjstatus.getStates_daily().get(size-14).getUt())-Float.parseFloat(dailyrjstatus.getStates_daily().get(size-11).getUt())-Float.parseFloat(dailyrjstatus.getStates_daily().get(size-8).getUt())-Float.parseFloat(dailyrjstatus.getStates_daily().get(size-5).getUt()),
            recovered1-Float.parseFloat(dailyrjstatus.getStates_daily().get(size-20).getUt())-Float.parseFloat(dailyrjstatus.getStates_daily().get(size-17).getUt())-Float.parseFloat(dailyrjstatus.getStates_daily().get(size-14).getUt())-Float.parseFloat(dailyrjstatus.getStates_daily().get(size-11).getUt())-Float.parseFloat(dailyrjstatus.getStates_daily().get(size-8).getUt())-Float.parseFloat(dailyrjstatus.getStates_daily().get(size-5).getUt())

    };
    recovgraph.setData(null);// for example
    recovgraph.setData(recov);
    float active[]=  new float[]{active1-Float.parseFloat(dailyrjstatus.getStates_daily().get(size-5).getUt())-Float.parseFloat(dailyrjstatus.getStates_daily().get(size-4).getUt())-Float.parseFloat(dailyrjstatus.getStates_daily().get(size-6).getUt()),
            active1-Float.parseFloat(dailyrjstatus.getStates_daily().get(size-8).getUt())-Float.parseFloat(dailyrjstatus.getStates_daily().get(size-5).getUt())-Float.parseFloat(dailyrjstatus.getStates_daily().get(size-7).getUt())-Float.parseFloat(dailyrjstatus.getStates_daily().get(size-4).getUt())-Float.parseFloat(dailyrjstatus.getStates_daily().get(size-8).getUt())-Float.parseFloat(dailyrjstatus.getStates_daily().get(size-5).getUt()),
            active1-Float.parseFloat(dailyrjstatus.getStates_daily().get(size-11).getUt())-Float.parseFloat(dailyrjstatus.getStates_daily().get(size-8).getUt())-Float.parseFloat(dailyrjstatus.getStates_daily().get(size-5).getUt())-Float.parseFloat(dailyrjstatus.getStates_daily().get(size-11).getUt())-Float.parseFloat(dailyrjstatus.getStates_daily().get(size-8).getUt())-Float.parseFloat(dailyrjstatus.getStates_daily().get(size-5).getUt())-Float.parseFloat(dailyrjstatus.getStates_daily().get(size-10).getUt())-Float.parseFloat(dailyrjstatus.getStates_daily().get(size-7).getUt())-Float.parseFloat(dailyrjstatus.getStates_daily().get(size-4).getUt()),

            active1-Float.parseFloat(dailyrjstatus.getStates_daily().get(size-14).getUt())-Float.parseFloat(dailyrjstatus.getStates_daily().get(size-11).getUt())-Float.parseFloat(dailyrjstatus.getStates_daily().get(size-8).getUt())-Float.parseFloat(dailyrjstatus.getStates_daily().get(size-5).getUt())-Float.parseFloat(dailyrjstatus.getStates_daily().get(size-14).getUt())-Float.parseFloat(dailyrjstatus.getStates_daily().get(size-11).getUt())-Float.parseFloat(dailyrjstatus.getStates_daily().get(size-8).getUt())-Float.parseFloat(dailyrjstatus.getStates_daily().get(size-5).getUt())-Float.parseFloat(dailyrjstatus.getStates_daily().get(size-13).getUt())-Float.parseFloat(dailyrjstatus.getStates_daily().get(size-10).getUt())-Float.parseFloat(dailyrjstatus.getStates_daily().get(size-7).getUt())-Float.parseFloat(dailyrjstatus.getStates_daily().get(size-4).getUt()),




    };

    activegraphview.setData(active);



//   data.add(new BarEntry(Float.parseFloat(dailyrjstatus.getStates_daily().get(size-3).getDate()),Float.parseFloat(dailyrjstatus.getStates_daily().get(size-3).getUt())));
//        data.add(new BarEntry(Float.parseFloat(dailyrjstatus.getStates_daily().get(size-6).getDate()),));
//        data.add(new BarEntry(Float.parseFloat(dailyrjstatus.getStates_daily().get(size-9).getDate()),Float.parseFloat(dailyrjstatus.getStates_daily().get(size-9).getUt())));
//        data.add(new BarEntry(Float.parseFloat(dailyrjstatus.getStates_daily().get(size-12).getDate()),Float.parseFloat(dailyrjstatus.getStates_daily().get(size-12).getUt())));
//        data.add(new BarEntry(Float.parseFloat(dailyrjstatus.getStates_daily().get(size-15).getDate()),Float.parseFloat(dailyrjstatus.getStates_daily().get(size-15).getUt())));
//        data.add(new BarEntry(Float.parseFloat(dailyrjstatus.getStates_daily().get(size-18).getDate()),Float.parseFloat(dailyrjstatus.getStates_daily().get(size-18).getUt())));
//        data.add(new BarEntry(Float.parseFloat(dailyrjstatus.getStates_daily().get(size-21).getDate()),Float.parseFloat(dailyrjstatus.getStates_daily().get(size-21).getUt())));
//        BarDataSet barDataSet=new BarDataSet(data,"Cases");
//        BarData barData=new BarData();
//        barData.addDataSet(barDataSet);
//        chart.setData(barData);
//        chart.invalidate();
    float graph1[]=  new float[]{Float.parseFloat(dailyrjstatus.getStates_daily().get(size-3).getUt()),
            Float.parseFloat(dailyrjstatus.getStates_daily().get(size-6).getUt()),
            Float.parseFloat(dailyrjstatus.getStates_daily().get(size-9).getUt()),
            Float.parseFloat(dailyrjstatus.getStates_daily().get(size-12).getUt()),
            Float.parseFloat(dailyrjstatus.getStates_daily().get(size-15).getUt()),
            Float.parseFloat(dailyrjstatus.getStates_daily().get(size-18).getUt()),
            Float.parseFloat(dailyrjstatus.getStates_daily().get(size-21).getUt())
    };
    String array[] = new String[]{dailyrjstatus.getStates_daily().get(size - 3).getDate(),
            dailyrjstatus.getStates_daily().get(size - 6).getDate(),
            dailyrjstatus.getStates_daily().get(size - 9).getDate(),
            dailyrjstatus.getStates_daily().get(size - 12).getDate(),
            dailyrjstatus.getStates_daily().get(size - 15).getDate(),
            dailyrjstatus.getStates_daily().get(size - 18).getDate(),
            dailyrjstatus.getStates_daily().get(size - 21).getDate()};
    ChartEntity first = new ChartEntity(Color.WHITE, graph1);
    ArrayList<ChartEntity> list = new ArrayList<>();
    list.add(first);
    chart.setLegend(Arrays.asList(array));
    chart.setList(list);



}

else if(statecode.equalsIgnoreCase("wb"))
{

    float afectedcasep[]=  new float[]{affected1-Float.parseFloat(dailyrjstatus.getStates_daily().get(size-6).getWb()),
            affected1-Float.parseFloat(dailyrjstatus.getStates_daily().get(size-9).getWb())-Float.parseFloat(dailyrjstatus.getStates_daily().get(size-6).getWb()),
            affected1-Float.parseFloat(dailyrjstatus.getStates_daily().get(size-12).getWb())-Float.parseFloat(dailyrjstatus.getStates_daily().get(size-9).getWb())-Float.parseFloat(dailyrjstatus.getStates_daily().get(size-6).getWb()),
            affected1-Float.parseFloat(dailyrjstatus.getStates_daily().get(size-15).getWb())-Float.parseFloat(dailyrjstatus.getStates_daily().get(size-12).getWb())-Float.parseFloat(dailyrjstatus.getStates_daily().get(size-9).getWb())-Float.parseFloat(dailyrjstatus.getStates_daily().get(size-6).getWb()),
            affected1-Float.parseFloat(dailyrjstatus.getStates_daily().get(size-18).getWb())-Float.parseFloat(dailyrjstatus.getStates_daily().get(size-15).getWb())-Float.parseFloat(dailyrjstatus.getStates_daily().get(size-12).getWb())-Float.parseFloat(dailyrjstatus.getStates_daily().get(size-9).getWb())-Float.parseFloat(dailyrjstatus.getStates_daily().get(size-6).getWb()),
            affected1-Float.parseFloat(dailyrjstatus.getStates_daily().get(size-21).getWb())-Float.parseFloat(dailyrjstatus.getStates_daily().get(size-18).getWb())-Float.parseFloat(dailyrjstatus.getStates_daily().get(size-15).getWb())-Float.parseFloat(dailyrjstatus.getStates_daily().get(size-12).getWb())-Float.parseFloat(dailyrjstatus.getStates_daily().get(size-9).getWb())-Float.parseFloat(dailyrjstatus.getStates_daily().get(size-6).getWb())

    };


    affectedGraphView.setData(afectedcasep);
    float deceased[]=  new float[]{dead1-Float.parseFloat(dailyrjstatus.getStates_daily().get(size-4).getWb()),
            dead1-Float.parseFloat(dailyrjstatus.getStates_daily().get(size-7).getWb())-Float.parseFloat(dailyrjstatus.getStates_daily().get(size-4).getWb()),
            dead1-Float.parseFloat(dailyrjstatus.getStates_daily().get(size-10).getWb())-Float.parseFloat(dailyrjstatus.getStates_daily().get(size-7).getWb())-Float.parseFloat(dailyrjstatus.getStates_daily().get(size-4).getWb()),
            dead1-Float.parseFloat(dailyrjstatus.getStates_daily().get(size-13).getWb())-Float.parseFloat(dailyrjstatus.getStates_daily().get(size-10).getWb())-Float.parseFloat(dailyrjstatus.getStates_daily().get(size-7).getWb())-Float.parseFloat(dailyrjstatus.getStates_daily().get(size-4).getWb()),
            dead1-Float.parseFloat(dailyrjstatus.getStates_daily().get(size-16).getWb())-Float.parseFloat(dailyrjstatus.getStates_daily().get(size-13).getWb())-Float.parseFloat(dailyrjstatus.getStates_daily().get(size-10).getWb())-Float.parseFloat(dailyrjstatus.getStates_daily().get(size-7).getWb())-Float.parseFloat(dailyrjstatus.getStates_daily().get(size-4).getWb()),
            dead1-Float.parseFloat(dailyrjstatus.getStates_daily().get(size-19).getWb())-Float.parseFloat(dailyrjstatus.getStates_daily().get(size-16).getWb())-Float.parseFloat(dailyrjstatus.getStates_daily().get(size-13).getWb())-Float.parseFloat(dailyrjstatus.getStates_daily().get(size-10).getWb())-Float.parseFloat(dailyrjstatus.getStates_daily().get(size-7).getWb())-Float.parseFloat(dailyrjstatus.getStates_daily().get(size-4).getWb())

    };


    deadgraph.setData(deceased);
    float recov[]=  new float[]{recovered1-Float.parseFloat(dailyrjstatus.getStates_daily().get(size-5).getWb()),
            recovered1-Float.parseFloat(dailyrjstatus.getStates_daily().get(size-8).getWb())-Float.parseFloat(dailyrjstatus.getStates_daily().get(size-5).getWb()),
            recovered1-Float.parseFloat(dailyrjstatus.getStates_daily().get(size-11).getWb())-Float.parseFloat(dailyrjstatus.getStates_daily().get(size-8).getWb())-Float.parseFloat(dailyrjstatus.getStates_daily().get(size-5).getWb()),
            recovered1-Float.parseFloat(dailyrjstatus.getStates_daily().get(size-14).getWb())-Float.parseFloat(dailyrjstatus.getStates_daily().get(size-11).getWb())-Float.parseFloat(dailyrjstatus.getStates_daily().get(size-8).getWb())-Float.parseFloat(dailyrjstatus.getStates_daily().get(size-5).getWb()),
            recovered1-Float.parseFloat(dailyrjstatus.getStates_daily().get(size-17).getWb())-Float.parseFloat(dailyrjstatus.getStates_daily().get(size-14).getWb())-Float.parseFloat(dailyrjstatus.getStates_daily().get(size-11).getWb())-Float.parseFloat(dailyrjstatus.getStates_daily().get(size-8).getWb())-Float.parseFloat(dailyrjstatus.getStates_daily().get(size-5).getWb()),
            recovered1-Float.parseFloat(dailyrjstatus.getStates_daily().get(size-20).getWb())-Float.parseFloat(dailyrjstatus.getStates_daily().get(size-17).getWb())-Float.parseFloat(dailyrjstatus.getStates_daily().get(size-14).getWb())-Float.parseFloat(dailyrjstatus.getStates_daily().get(size-11).getWb())-Float.parseFloat(dailyrjstatus.getStates_daily().get(size-8).getWb())-Float.parseFloat(dailyrjstatus.getStates_daily().get(size-5).getWb())

    };
    recovgraph.setData(null);// for example
    recovgraph.setData(recov);
    float active[]=  new float[]{active1-Float.parseFloat(dailyrjstatus.getStates_daily().get(size-5).getWb())-Float.parseFloat(dailyrjstatus.getStates_daily().get(size-4).getWb())-Float.parseFloat(dailyrjstatus.getStates_daily().get(size-6).getWb()),
            active1-Float.parseFloat(dailyrjstatus.getStates_daily().get(size-8).getWb())-Float.parseFloat(dailyrjstatus.getStates_daily().get(size-5).getWb())-Float.parseFloat(dailyrjstatus.getStates_daily().get(size-7).getWb())-Float.parseFloat(dailyrjstatus.getStates_daily().get(size-4).getWb())-Float.parseFloat(dailyrjstatus.getStates_daily().get(size-8).getWb())-Float.parseFloat(dailyrjstatus.getStates_daily().get(size-5).getWb()),
            active1-Float.parseFloat(dailyrjstatus.getStates_daily().get(size-11).getWb())-Float.parseFloat(dailyrjstatus.getStates_daily().get(size-8).getWb())-Float.parseFloat(dailyrjstatus.getStates_daily().get(size-5).getWb())-Float.parseFloat(dailyrjstatus.getStates_daily().get(size-11).getWb())-Float.parseFloat(dailyrjstatus.getStates_daily().get(size-8).getWb())-Float.parseFloat(dailyrjstatus.getStates_daily().get(size-5).getWb())-Float.parseFloat(dailyrjstatus.getStates_daily().get(size-10).getWb())-Float.parseFloat(dailyrjstatus.getStates_daily().get(size-7).getWb())-Float.parseFloat(dailyrjstatus.getStates_daily().get(size-4).getWb()),

            active1-Float.parseFloat(dailyrjstatus.getStates_daily().get(size-14).getWb())-Float.parseFloat(dailyrjstatus.getStates_daily().get(size-11).getWb())-Float.parseFloat(dailyrjstatus.getStates_daily().get(size-8).getWb())-Float.parseFloat(dailyrjstatus.getStates_daily().get(size-5).getWb())-Float.parseFloat(dailyrjstatus.getStates_daily().get(size-14).getWb())-Float.parseFloat(dailyrjstatus.getStates_daily().get(size-11).getWb())-Float.parseFloat(dailyrjstatus.getStates_daily().get(size-8).getWb())-Float.parseFloat(dailyrjstatus.getStates_daily().get(size-5).getWb())-Float.parseFloat(dailyrjstatus.getStates_daily().get(size-13).getWb())-Float.parseFloat(dailyrjstatus.getStates_daily().get(size-10).getWb())-Float.parseFloat(dailyrjstatus.getStates_daily().get(size-7).getWb())-Float.parseFloat(dailyrjstatus.getStates_daily().get(size-4).getWb()),




    };

    activegraphview.setData(active);



//   data.add(new BarEntry(Float.parseFloat(dailyrjstatus.getStates_daily().get(size-3).getDate()),Float.parseFloat(dailyrjstatus.getStates_daily().get(size-3).getWb())));
//        data.add(new BarEntry(Float.parseFloat(dailyrjstatus.getStates_daily().get(size-6).getDate()),));
//        data.add(new BarEntry(Float.parseFloat(dailyrjstatus.getStates_daily().get(size-9).getDate()),Float.parseFloat(dailyrjstatus.getStates_daily().get(size-9).getWb())));
//        data.add(new BarEntry(Float.parseFloat(dailyrjstatus.getStates_daily().get(size-12).getDate()),Float.parseFloat(dailyrjstatus.getStates_daily().get(size-12).getWb())));
//        data.add(new BarEntry(Float.parseFloat(dailyrjstatus.getStates_daily().get(size-15).getDate()),Float.parseFloat(dailyrjstatus.getStates_daily().get(size-15).getWb())));
//        data.add(new BarEntry(Float.parseFloat(dailyrjstatus.getStates_daily().get(size-18).getDate()),Float.parseFloat(dailyrjstatus.getStates_daily().get(size-18).getWb())));
//        data.add(new BarEntry(Float.parseFloat(dailyrjstatus.getStates_daily().get(size-21).getDate()),Float.parseFloat(dailyrjstatus.getStates_daily().get(size-21).getWb())));
//        BarDataSet barDataSet=new BarDataSet(data,"Cases");
//        BarData barData=new BarData();
//        barData.addDataSet(barDataSet);
//        chart.setData(barData);
//        chart.invalidate();
    float graph1[]=  new float[]{Float.parseFloat(dailyrjstatus.getStates_daily().get(size-3).getWb()),
            Float.parseFloat(dailyrjstatus.getStates_daily().get(size-6).getWb()),
            Float.parseFloat(dailyrjstatus.getStates_daily().get(size-9).getWb()),
            Float.parseFloat(dailyrjstatus.getStates_daily().get(size-12).getWb()),
            Float.parseFloat(dailyrjstatus.getStates_daily().get(size-15).getWb()),
            Float.parseFloat(dailyrjstatus.getStates_daily().get(size-18).getWb()),
            Float.parseFloat(dailyrjstatus.getStates_daily().get(size-21).getWb())
    };
    String array[] = new String[]{dailyrjstatus.getStates_daily().get(size - 3).getDate(),
            dailyrjstatus.getStates_daily().get(size - 6).getDate(),
            dailyrjstatus.getStates_daily().get(size - 9).getDate(),
            dailyrjstatus.getStates_daily().get(size - 12).getDate(),
            dailyrjstatus.getStates_daily().get(size - 15).getDate(),
            dailyrjstatus.getStates_daily().get(size - 18).getDate(),
            dailyrjstatus.getStates_daily().get(size - 21).getDate()};
    ChartEntity first = new ChartEntity(Color.WHITE, graph1);
    ArrayList<ChartEntity> list = new ArrayList<>();
    list.add(first);
    chart.setLegend(Arrays.asList(array));
    chart.setList(list);



}




    }

    private void indiancases() {
        utils apiinterfaceindia=RetrofitClient.getindiaretrofit(getContext()).create(utils.class);
        apiinterfaceindia.allindiadata().subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(new Observer<india_Data>() {
                    @Override
                    public void onSubscribe(Disposable d) {

                    }

                    @RequiresApi(api = Build.VERSION_CODES.N)
                    @Override
                    public void onNext(india_Data india_data) {
                        DecimalFormat precision = new DecimalFormat("0.0");

                        for(int i=0;i<india_data.getStatewise().size()-1;i++) {
                            if(india_data.getStatewise().get(i).getState().equalsIgnoreCase(currenSTATE))
                            {
                                currentstate.setText(india_data.getStatewise().get(i).getState()+" Cases Overview");
                            affected.setText(india_data.getStatewise().get(i).getConfirmed());
                            active.setText(india_data.getStatewise().get(i).getActive());
                            dead.setText(india_data.getStatewise().get(i).getDeaths());
                            recovered.setText(india_data.getStatewise().get(i).getRecovered());
                            statecode=india_data.getStatewise().get(i).getStatecode();
                                affected1 = Float.parseFloat(india_data.getStatewise().get(i).getConfirmed());
                                active1 = Float.parseFloat(india_data.getStatewise().get(i).getActive());
                                dead1 = Float.parseFloat(india_data.getStatewise().get(i).getDeaths());
                                recovered1 = Float.parseFloat(india_data.getStatewise().get(i).getRecovered());


                                break;
//                            affected1 = Float.parseFloat(india_data.getStatewise().get(5).getConfirmed());
//                            active1 = Float.parseFloat(india_data.getStatewise().get(5).getActive());
//                            dead1 = Float.parseFloat(india_data.getStatewise().get(5).getDeaths());
//                            recovered1 = Float.parseFloat(india_data.getStatewise().get(5).getRecovered());
                        }}


                    }

                    @Override
                    public void onError(Throwable e) {
                        Log.e("hiee error", "onError: "+e.getLocalizedMessage() );
                    }

                    @Override
                    public void onComplete() {
                        List<Integer> l=new ArrayList<>(sortedCASES.keySet());
                        Log.d(TAG, "onComplete: "+l.size());
                        rajasthandat();


                    }
                });
    }

    private void makegraph(india_Data india_data) {
        int size=india_data.getCases_time_series().size();
        float[] casedata= new float[10];
        casedata[0]=100;

        affectedGraphView.setData(null);// for example
        affectedGraphView.setData(casedata);

        float activecasep[]= new float[]{
                Float.valueOf(india_data.getCases_time_series().get(size-1).getTotalconfirmed())-Float.valueOf(india_data.getCases_time_series().get(size-1).getTotaldeceased())-Float.valueOf(india_data.getCases_time_series().get(size-1).getTotalrecovered()),
                Float.valueOf(india_data.getCases_time_series().get(size-2).getTotalconfirmed())-Float.valueOf(india_data.getCases_time_series().get(size-2).getTotaldeceased())-Float.valueOf(india_data.getCases_time_series().get(size-2).getTotalrecovered()),
                Float.valueOf(india_data.getCases_time_series().get(size-3).getTotalconfirmed())-Float.valueOf(india_data.getCases_time_series().get(size-3).getTotaldeceased())-Float.valueOf(india_data.getCases_time_series().get(size-3).getTotalrecovered()),
                Float.valueOf(india_data.getCases_time_series().get(size-4).getTotalconfirmed())-Float.valueOf(india_data.getCases_time_series().get(size-4).getTotaldeceased())-Float.valueOf(india_data.getCases_time_series().get(size-4).getTotalrecovered()),
                Float.valueOf(india_data.getCases_time_series().get(size-5).getTotalconfirmed())-Float.valueOf(india_data.getCases_time_series().get(size-5).getTotaldeceased())-Float.valueOf(india_data.getCases_time_series().get(size-5).getTotalrecovered()),
                Float.valueOf(india_data.getCases_time_series().get(size-6).getTotalconfirmed())-Float.valueOf(india_data.getCases_time_series().get(size-6).getTotaldeceased())-Float.valueOf(india_data.getCases_time_series().get(size-6).getTotalrecovered()),
                Float.valueOf(india_data.getCases_time_series().get(size-7).getTotalconfirmed())-Float.valueOf(india_data.getCases_time_series().get(size-7).getTotaldeceased())-Float.valueOf(india_data.getCases_time_series().get(size-7).getTotalrecovered()),
                Float.valueOf(india_data.getCases_time_series().get(size-8).getTotalconfirmed())-Float.valueOf(india_data.getCases_time_series().get(size-8).getTotaldeceased())-Float.valueOf(india_data.getCases_time_series().get(size-8).getTotalrecovered()),
                Float.valueOf(india_data.getCases_time_series().get(size-9).getTotalconfirmed())-Float.valueOf(india_data.getCases_time_series().get(size-9).getTotaldeceased())-Float.valueOf(india_data.getCases_time_series().get(size-9).getTotalrecovered()),
                Float.valueOf(india_data.getCases_time_series().get(size-10).getTotalconfirmed())-Float.valueOf(india_data.getCases_time_series().get(size-10).getTotaldeceased())-Float.valueOf(india_data.getCases_time_series().get(size-10).getTotalrecovered())};
        activegraphview.setData(null);
        activegraphview.setData(activecasep);
    }


}


