package com.example.firebasetry;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;

import com.github.mikephil.charting.charts.LineChart;
import com.github.mikephil.charting.data.Entry;
import com.github.mikephil.charting.data.LineData;
import com.github.mikephil.charting.data.LineDataSet;
import com.github.mikephil.charting.interfaces.datasets.ILineDataSet;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.Query;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;

public class MainActivity extends AppCompatActivity {
    EditText xval,yval;
    LineChart lineChart;
    Button button,button1;

    FirebaseDatabase firebaseDatabase;
    DatabaseReference myRef;
    LineDataSet lineDataSet=new LineDataSet(null,null);
    ArrayList<ILineDataSet> iLineDataSets=new ArrayList<>();
    LineData lineData;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        xval=findViewById(R.id.editTextX);
        yval=findViewById(R.id.editTextY);
        lineChart=findViewById(R.id.lineChart);
        button=findViewById(R.id.button);

        firebaseDatabase=FirebaseDatabase.getInstance();
        myRef=firebaseDatabase.getReference("ChartValues");
        insertData();
        retData();

    }

    private void insertData() {

        button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                String id = myRef.push().getKey();
                int x=Integer.parseInt( xval.getText().toString());
                int y=Integer.parseInt( yval.getText().toString());

                DataPoint dataPoint=new DataPoint(x,y);
                myRef.child(id).setValue(dataPoint);

                retData();
            }
        });
    }

    public void clearGraph(View view){
        Query applesQuery = myRef.child("ChartValues").orderByChild("xValues");

        applesQuery.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                for (DataSnapshot appleSnapshot: dataSnapshot.getChildren()) {
                    appleSnapshot.getRef().removeValue();
                }
            }

            @Override
            public void onCancelled(DatabaseError databaseError) {
//                Log.e(TAG, "onCancelled", databaseError.toException());
            }
        });
        retData();
    }

    private void retData() {
        myRef.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                ArrayList<Entry> dataValus=new ArrayList<>();
                if(snapshot.hasChildren()){
                    for(DataSnapshot my:snapshot.getChildren()){
                        DataPoint dataPoint=my.getValue(DataPoint.class);
                        dataValus.add(new Entry(dataPoint.getxValues(),dataPoint.getyValues()));

                    }
                    showChart(dataValus);
                }
                else{
                    lineChart.clear();
                    lineChart.invalidate();
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });
    }

    private void showChart(ArrayList<Entry> dataValus) {
        lineDataSet.setValues(dataValus);
        lineDataSet.setLabel("DataSet1");
        iLineDataSets.clear();
        iLineDataSets.add(lineDataSet);
        lineData=new LineData(iLineDataSets);
        lineChart.clear();
        lineChart.setData(lineData);
        lineChart.invalidate();
    }


}