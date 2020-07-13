package com.personal.covid_19;

import android.os.Bundle;

import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.personal.covid_19.model.resources;

import java.util.ArrayList;

import static androidx.constraintlayout.widget.Constraints.TAG;


/**
 * A simple {@link Fragment} subclass.
 */
public class labs extends Fragment {
    ArrayList<resources> labs=new ArrayList<>();
    ArrayList<resources> list;
    private RecyclerView recyclerView;
    private  groceryadapter groceryadapter;
    TextView nodata;
    public labs() {
        // Required empty public constructor
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        ViewGroup   root=(ViewGroup)inflater.inflate(R.layout.fragment_labs, container, false);
        nodata=root.findViewById(R.id.nodata);
        recyclerView=root.findViewById(R.id.labview);
        recyclerView.setHasFixedSize(true);

        recyclerView.setLayoutManager(new LinearLayoutManager(getActivity()));
        list=(ArrayList<resources>)getArguments().getSerializable("key");
        for(int i=0;i<list.size()-1;i++)
        {
            if(list.get(i).getCategory().equals("CoVID-19 Testing Lab"))
            {
                labs.add(list.get(i));
            }
        }
        if(!labs.isEmpty()) {
            groceryadapter = new groceryadapter(labs, getContext());
            groceryadapter.notifyDataSetChanged();
            recyclerView.setAdapter(groceryadapter);
            Log.d(TAG, "onCreateView: " + labs.toString());
        }
        else{
            recyclerView.setVisibility(View.INVISIBLE);
            nodata.setVisibility(View.VISIBLE);
        }


        // Inflate the layout for this fragment
        return root;
    }
}
