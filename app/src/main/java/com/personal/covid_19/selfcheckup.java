package com.personal.covid_19;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.RecyclerView;
import androidx.recyclerview.widget.RecyclerView.ViewHolder;

import android.content.Context;
import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.Button;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.daprlabs.cardstack.SwipeDeck;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.FirebaseFirestore;
import com.shreyaspatil.MaterialDialog.BottomSheetMaterialDialog;
import com.shreyaspatil.MaterialDialog.interfaces.DialogInterface;
import com.yuyakaido.android.cardstackview.CardStackView;

import java.lang.reflect.Array;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;


public class selfcheckup extends AppCompatActivity {

    private int i;
        SwipeDeck flingContainer;
Button yes,no;
ImageView backbtn;
    public static cardAdapter myAppAdapter;
    public static ViewHolder viewHolder;
    private ArrayList<card> array;
    CustomProgressBar progressBar;
    TextView question;
    int position=1;
    int score=0;
    ArrayList<Integer> answers=new ArrayList<Integer>();
    FirebaseUser currentUser;
    FirebaseAuth mAuth;
    FirebaseFirestore firestore;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        setContentView(R.layout.activity_selfcheckup);
        flingContainer= findViewById(R.id.frame);
        mAuth= FirebaseAuth.getInstance();
        currentUser=mAuth.getCurrentUser();


        firestore=FirebaseFirestore.getInstance();
        progressBar=findViewById(R.id.progressbarques);
        question=findViewById(R.id.questionno);
        yes=findViewById(R.id.yesbtn);
        no=findViewById(R.id.nobtn);
        array = new ArrayList<>();
        backbtn=findViewById(R.id.backbtn);
        backbtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent i=new Intent(selfcheckup.this,MainActivity.class);
                i.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK |Intent.FLAG_ACTIVITY_CLEAR_TASK);
                startActivity(i);

            }
        });

        flingContainer.setHardwareAccelerationEnabled(true);
        array.add(new card("Have you been within 6 feet of a person with a lab-confirmed case of COVID-19 for at least 5 minutes, or had direct contact with their mucus or saliva, in the past 14 days?"));


        array.add(new card("In the last 48 hours, have you had any of the following :\nFever,Cough,Trouble breathing,Muscle aches,Sore throat,Diarrhea."));
        array.add(new card("Are you experiencing fatigue?"));
        array.add(new card("Do you have any of the following possible emergency symptoms :\nStruggling to breathe or fighting for breath even while inactive or when resting."));
        array.add(new card("Do you have a history of traveling to an area infected with COVID-19 ?"));
        myAppAdapter = new cardAdapter(array, selfcheckup.this);
        flingContainer.setAdapter(myAppAdapter);
        flingContainer.setEventCallback(new SwipeDeck.SwipeEventCallback() {
            @Override
            public void cardSwipedLeft(int positions) {
                position++;
                if(position<6) {
                    question.setText(Integer.toString(position));
                }
                progressBar.setCurProgress(position,600);
                score++;
                answers.add(1);

            }

            @Override
            public void cardSwipedRight(int positions) {
                position++;
                if(position<6) {
                    question.setText(Integer.toString(position));
                }
                progressBar.setCurProgress(position,600);
                answers.add(0);

                if(progressBar.getProgress()==5) {

                }
            }

            @Override
            public void cardsDepleted() {
                checkanswer();



            }

            @Override
            public void cardActionDown() {

            }

            @Override
            public void cardActionUp() {

            }
        });

       yes.setOnClickListener(new View.OnClickListener() {
           @Override
           public void onClick(View v) {
               flingContainer.swipeTopCardLeft(50000);





           }

       });


       no.setOnClickListener(new View.OnClickListener() {
           @Override
           public void onClick(View v) {
               flingContainer.swipeTopCardRight(50000);






           }
       });





        // Optionally add an OnItemClickListener





    }

    private void checkanswer() {
        Log.d("TAG", "onCreate: "+"HIEE");
        if (answers.get(0)==1&&answers.get(1)==1) {

                settest("positive");
            BottomSheetMaterialDialog mBottomSheetDialog = new BottomSheetMaterialDialog.Builder(selfcheckup.this)
                    .setTitle("ALERT!")
                    .setMessage("Your chances of getting infected are high #StayHome#StaySafe")
                    .setCancelable(false)
                    .setPositiveButton("Retest", new BottomSheetMaterialDialog.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialogInterface, int which) {

                            finish();
                            overridePendingTransition(0,0);
                            startActivity(getIntent());
                            overridePendingTransition(0,0);

                            dialogInterface.dismiss();
                        }
                    })
                    .setNegativeButton("Home", new BottomSheetMaterialDialog.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialogInterface, int which) {
                            Intent i=new Intent(selfcheckup.this,MainActivity.class);
                            i.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK |Intent.FLAG_ACTIVITY_CLEAR_TASK);
                            startActivity(i);
                        }
                    })
                    .setAnimation(R.raw.sickcorona)
                    .build();

            // Show Dialog
            mBottomSheetDialog.show();
        } else if (answers.get(0)==1 && answers.get(1)==0 ) {
            settest("positive");
            BottomSheetMaterialDialog mBottomSheetDialog = new BottomSheetMaterialDialog.Builder(selfcheckup.this)
                    .setTitle("Ooops!")
                    .setMessage("You might get infected\n#StayHome#StaySafe")
                    .setCancelable(false)
                    .setPositiveButton("Retest", new BottomSheetMaterialDialog.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialogInterface, int which) {

                            finish();
                            overridePendingTransition(0,0);
                            startActivity(getIntent());
                            overridePendingTransition(0,0);

                            dialogInterface.dismiss();
                        }
                    })
                    .setNegativeButton("Home", new BottomSheetMaterialDialog.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialogInterface, int which) {
                            Intent i=new Intent(selfcheckup.this,MainActivity.class);

                            i.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK |Intent.FLAG_ACTIVITY_CLEAR_TASK);
                            startActivity(i);
                        }
                    })
                    .setAnimation(R.raw.sickcorona)
                    .build();

            // Show Dialog
            mBottomSheetDialog.show();
        } else if (score > 2) {
            settest("positive");
            BottomSheetMaterialDialog mBottomSheetDialog = new BottomSheetMaterialDialog.Builder(selfcheckup.this)
                    .setTitle("ALERT!")
                    .setMessage("Your chances of getting infected are high #StayHome#StaySafe")
                    .setCancelable(false)
                    .setPositiveButton("Retest", new BottomSheetMaterialDialog.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialogInterface, int which) {

                            finish();
                            overridePendingTransition(0,0);
                            startActivity(getIntent());
                            overridePendingTransition(0,0);

                            dialogInterface.dismiss();
                        }
                    })
                    .setNegativeButton("Home", new BottomSheetMaterialDialog.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialogInterface, int which) {
                            Intent i=new Intent(selfcheckup.this,MainActivity.class);
                            i.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK |Intent.FLAG_ACTIVITY_CLEAR_TASK);

                            startActivity(i);
                        }
                    })
                    .setAnimation(R.raw.sickcorona)
                    .build();

            // Show Dialog
            mBottomSheetDialog.show();

        } else {
            settest("negative");
            BottomSheetMaterialDialog mBottomSheetDialog = new BottomSheetMaterialDialog.Builder(selfcheckup.this)
                    .setTitle("Kudos!")
                    .setMessage("Your chances of getting infected are low #StayHome#StaySafe")
                    .setCancelable(false)
                    .setPositiveButton("Retest", new BottomSheetMaterialDialog.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialogInterface, int which) {

                            finish();
                            overridePendingTransition(0,0);
                            startActivity(getIntent());
                            overridePendingTransition(0,0);

                            dialogInterface.dismiss();
                        }
                    })
                    .setNegativeButton("Home", new BottomSheetMaterialDialog.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialogInterface, int which) {
                            Intent i=new Intent(selfcheckup.this,MainActivity.class);
                            i.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK |Intent.FLAG_ACTIVITY_CLEAR_TASK);
                            startActivity(i);
                        }
                    })
                    .setAnimation(R.raw.coronanegative)
                    .build();

            // Show Dialog
            mBottomSheetDialog.show();
        }
    }

    private void settest(String result) {
        if(currentUser!=null)
        {

            String userid=currentUser.getUid();


            DocumentReference reference=firestore.collection("userid").document(userid);
            Map<String,Object> user1= new HashMap<>();

            user1.put("test",result);

            reference.update(user1).addOnSuccessListener(new OnSuccessListener<Void>() {
                @Override
                public void onSuccess(Void aVoid) {

                }
            });

        }
    }

    public static class ViewHolder {

        public TextView DataText;



    }

    public class cardAdapter extends BaseAdapter {


        public List<card> parkingList;
        public Context context;

        private cardAdapter(List<card> apps, Context context) {
            this.parkingList = apps;
            this.context = context;
        }

        @Override
        public int getCount() {
            return parkingList.size();
        }

        @Override
        public Object getItem(int position) {
            return position;
        }

        @Override
        public long getItemId(int position) {
            return position;
        }

        @Override
        public View getView(final int position, View convertView, ViewGroup parent) {

            View rowView = convertView;


            ViewHolder viewHolder;
            if (rowView == null) {

                LayoutInflater inflater = getLayoutInflater();
                rowView = inflater.inflate(R.layout.questions, parent, false);
                // configure view holder
                viewHolder = new ViewHolder();
                viewHolder.DataText = (TextView) rowView.findViewById(R.id.questions1);

                rowView.setTag(viewHolder);

            } else {
                viewHolder = (ViewHolder) convertView.getTag();
            }
            viewHolder.DataText.setText(parkingList.get(position).getName() + "");



            return rowView;
        }
    }
    static void makeToast(Context ctx, String s) {
        Toast.makeText(ctx, s, Toast.LENGTH_SHORT).show();
    }



}


