package com.example.akashpc.eventtracker;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

import com.example.akashpc.eventtracker.constants.EventConstants;
import com.example.akashpc.eventtracker.constants.JSONParameterConstants;
import com.example.akashpc.eventtracker.utils.GetAddress;

import org.json.JSONObject;

public class EventDetailsActivity extends AppCompatActivity {

    private JSONObject event;

    //Event details
    private Double latitude, longitude;
    private String name;
    private String address;
    private String type;

    //UI Elements
    private TextView tv_event_type, tv_event_address, tv_event_name;
    private Button but_go_back;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_event_details);
        init();
        initUI();
        initUIActions();
    }

    private void init(){
        try {
            event = new JSONObject(getIntent().getStringExtra("eventData"));
            extractCoordinates(event.getString(JSONParameterConstants.COORDINATE));
            name = event.getString(JSONParameterConstants.TITLE);
            type = event.getString(JSONParameterConstants.TYPE);
            getEventTypeFullName();
        }
        catch (Exception e){
            e.printStackTrace();
        }
    }

    private void initUI(){
        tv_event_name = (TextView)findViewById(R.id.tv_event_name);
        tv_event_type= (TextView)findViewById(R.id.tv_event_type);
        tv_event_address = (TextView)findViewById(R.id.tv_event_address);
        but_go_back = (Button)findViewById(R.id.but_back);
    }

    private void initUIActions(){
        but_go_back.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent in = new Intent(EventDetailsActivity.this , MainActivity.class);
                startActivity(in);
            }
        });
        tv_event_name.setText(name);
        tv_event_type.setText(type);
        tv_event_address.setText(address);
    }

    private void extractCoordinates(String coordinate){
        address = coordinate;
        String coordinates[] = coordinate.split(",");
        latitude = Double.parseDouble(coordinates[0]);
        longitude = Double.parseDouble(coordinates[1]);
        getAddressFromCoordinates(latitude, longitude);
    }

    private void getAddressFromCoordinates(Double latitude, Double longitude){
        GetAddress getAddress = new GetAddress(latitude, longitude, this);
        address = getAddress.getAddressFromCoordinates();
    }

    private void getEventTypeFullName(){
        if(type == EventConstants.EVENT_TYPE_PEOPLE_SHORT){
            type = EventConstants.EVENT_TYPE_PEOPLE;
        }
        else if(type == EventConstants.EVENT_TYPE_EXCLUSIVE_SHORT){
            type = EventConstants.EVENT_TYPE_EXCLUSIVE;
        }
        else if(type == EventConstants.EVENT_TYPE_NORMAL_SHORT){
            type = EventConstants.EVENT_TYPE_NORMAL;
        }
    }
}
