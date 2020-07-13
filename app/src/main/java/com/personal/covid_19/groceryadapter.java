package com.personal.covid_19;

import android.Manifest;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.media.Image;
import android.net.Uri;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.cardview.widget.CardView;
import androidx.core.app.ActivityCompat;
import androidx.recyclerview.widget.RecyclerView;

import com.personal.covid_19.model.newsallcn;
import com.personal.covid_19.model.resources;

import java.util.ArrayList;
import java.util.List;

public class groceryadapter extends RecyclerView.Adapter<groceryadapter.ViewHolder> {

    private ArrayList<resources> grocery = new ArrayList<>();
    private Context context;


    public groceryadapter(ArrayList<resources> newsitems, Context context) {
        this.grocery = newsitems;
        this.context = context;
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View v = LayoutInflater.from(parent.getContext()).
                inflate(R.layout.groceryconteainer, parent, false);
        return new ViewHolder(v);

    }

    @Override
    public void onBindViewHolder(@NonNull groceryadapter.ViewHolder holder, int position) {
        resources groceryresources = grocery.get(position);
        if (groceryresources.getCategory().equals("Hospitals and Centers")) {
            holder.description.setText(groceryresources.getNameoftheorganisation());
            holder.name.setText(groceryresources.getDescriptionandorserviceprovided());

            holder.frameimage.setImageResource(R.drawable.ic_hospital);

        } else if (groceryresources.getCategory().equals("Delivery [Vegetables, Fruits, Groceries, Medicines, etc.]")) {
            holder.name.setText(groceryresources.getNameoftheorganisation());
            holder.description.setText(groceryresources.getDescriptionandorserviceprovided());
        } else {
            holder.description.setText(groceryresources.getNameoftheorganisation());
            holder.name.setText(groceryresources.getDescriptionandorserviceprovided());
            holder.frameimage.setImageResource(R.drawable.ic_lab3);
        }
        holder.call.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String phone=groceryresources.getPhonenumber().toString();
                Intent phoneIntent = new Intent(Intent.ACTION_CALL);
                phoneIntent.setData(Uri.parse("tel:"+phone));

                if (ActivityCompat.checkSelfPermission(context, Manifest.permission.CALL_PHONE) != PackageManager.PERMISSION_GRANTED) {
                    // TODO: Consider calling
                    //    ActivityCompat#requestPermissions
                    // here to request the missing permissions, and then overriding
                    //   public void onRequestPermissionsResult(int requestCode, String[] permissions,
                    //                                          int[] grantResults)
                    // to handle the case where the user grants the permission. See the documentation
                    // for ActivityCompat#requestPermissions for more details.
                    return;
                }
                context.startActivity(phoneIntent);
            }
        });


    }

    @Override
    public int getItemViewType(int position) {
        return super.getItemViewType(position);
    }

    @Override
    public int getItemCount() {
        return grocery.size();
    }
    public class ViewHolder extends RecyclerView.ViewHolder{
        public TextView name;
        public TextView description;
        public ImageView frameimage;
        public ImageView call;

        public ViewHolder(@NonNull View itemView) {
            super(itemView);
            name=itemView.findViewById(R.id.name);
            description=itemView.findViewById(R.id.description);
            call=itemView.findViewById(R.id.callgrocery);
            frameimage=itemView.findViewById(R.id.frameimage);


        }
    }
}
