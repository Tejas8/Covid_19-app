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
public class grocery extends Fragment implements View.OnClickListener {
    ArrayList<resources> grocery;
    ArrayList<resources> delivery=new ArrayList<>();
    private RecyclerView recyclerView;
    private  groceryadapter groceryadapter;
    private ArrayList<resources> groceryitem=new ArrayList<>();
    TextView nodata;
    public grocery() {
        // Required empty public constructor
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        ViewGroup   root=(ViewGroup)inflater.inflate(R.layout.fragment_grocery, container, false);
        nodata=root.findViewById(R.id.nodata);
        recyclerView=root.findViewById(R.id.groceryview);
        recyclerView.setHasFixedSize(true);
        recyclerView.setLayoutManager(new LinearLayoutManager(getActivity()));

        grocery = (ArrayList<resources>)getArguments().getSerializable("key");
        for(int i=0;i<grocery.size()-1;i++)
        {
            if(grocery.get(i).getCategory().equals("Delivery [Vegetables, Fruits, Groceries, Medicines, etc.]"))
            {
                delivery.add(grocery.get(i));
            }
        }
        if (!delivery.isEmpty()) {
            groceryadapter = new groceryadapter(delivery, getContext());
            groceryadapter.notifyDataSetChanged();
            recyclerView.setAdapter(groceryadapter);
            Log.d(TAG, "onCreateView: " + delivery.toString());
            Log.d(TAG, "onCreateView: " + grocery.get(0).getNameoftheorganisation());

        }
        else{
            recyclerView.setVisibility(View.INVISIBLE);
            nodata.setVisibility(View.VISIBLE);
        }
        return root;
    }

    @Override
    public void onClick(View v) {

    }
}
