package com.example.akashpc.eventtracker;

import android.app.Activity;
import android.content.Intent;
import android.support.v4.app.FragmentActivity;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Spinner;

import com.example.akashpc.eventtracker.constants.EventConstants;
import com.example.akashpc.eventtracker.constants.JSONParameterConstants;
import com.example.akashpc.eventtracker.utils.GetJSONFromFile;
import com.google.android.gms.common.ConnectionResult;
import com.google.android.gms.common.GoogleApiAvailability;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.MapFragment;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.BitmapDescriptor;
import com.google.android.gms.maps.model.BitmapDescriptorFactory;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.Marker;
import com.google.android.gms.maps.model.MarkerOptions;

import org.json.JSONArray;
import org.json.JSONObject;

import java.util.HashMap;

public class MainActivity extends FragmentActivity {

    private GoogleMap mapView;

    private Spinner spn_event_type;

    private Integer eventType;

    private ArrayAdapter adapter;

    private JSONArray eventsArray;

    private HashMap<Marker, Integer> MARKER_MAP = new HashMap<>();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        if(isGooglePlayServicesAvailable()) {
            setUpMapIfNeeded();
            initUI();
            initUIActions();
        }
    }

    @Override
    protected void onResume() {
        super.onResume();
        setUpMapIfNeeded();
    }

    private void initUI() {
        spn_event_type = (Spinner) findViewById(R.id.spn_event_type);
        adapter = new ArrayAdapter(this, android.R.layout.simple_list_item_1, EventConstants.EVENTS_VALUES);
        spn_event_type.setAdapter(adapter);
    }

    private void initUIActions() {
        spn_event_type.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                mapView.clear();
                initUIValues();
                setUpMapIfNeeded();
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {

            }

        });
        mapView.setOnMarkerClickListener(new GoogleMap.OnMarkerClickListener() {
            @Override
            public boolean onMarkerClick(Marker marker) {
                callDetailActivity(marker);
                return true;
            }

        });


    }

    private void initUIValues() {
        eventType = spn_event_type.getSelectedItemPosition();
    }

    private void setUpMapIfNeeded() {
        if (mapView == null) {
            getSupportFragmentManager().beginTransaction().add(R.id.map, new SupportMapFragment(), "tag").commit();
            MapFragment mapFragment = (MapFragment) getFragmentManager().findFragmentById(R.id.map);
            Log.e("ta", mapFragment.toString());
            mapView = mapFragment.getMap();
            mapView.setMapType(GoogleMap.MAP_TYPE_HYBRID);
            if (mapView != null) {
                setUpMap();
            }
        } else {
            addEventsToMap();
        }
    }

    private void setUpMap() {
        new Thread(new Runnable() {
            @Override
            public void run() {
                try {
                    addEventsToMap();
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }
        });
    }

    private void addEventsToMap() {
        GetJSONFromFile getJSONFromFile = new GetJSONFromFile(getApplicationContext());
        JSONObject data = getJSONFromFile.getEventsData();
        eventsArray = null;
        try {
            eventsArray = data.getJSONArray(JSONParameterConstants.DATA);
        } catch (Exception e) {
            e.printStackTrace();
        }

        final JSONArray events = eventsArray;

        runOnUiThread(new Runnable() {
            @Override
            public void run() {
                try {
                    createMarkersForEvents(events);
                } catch (Exception e) {

                }
            }
        });
    }

    private void createMarkersForEvents(JSONArray events) {
        String coordinate, title;
        double latitude, longitude;
        try {
            for (int i = 0; i < events.length(); i++) {
                JSONObject jsonObject = events.getJSONObject(i);
                coordinate = jsonObject.getString(JSONParameterConstants.COORDINATE);
                String coordinateList[] = coordinate.split(",");
                latitude = Double.parseDouble(coordinateList[0]);
                longitude = Double.parseDouble(coordinateList[1]);

                title = jsonObject.getString(JSONParameterConstants.TITLE);

                LatLng latLng = new LatLng(latitude, longitude);
                addMarker(latLng, i, title);
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    private void addMarker(final LatLng latLng, final Integer i, final String title) {
        runOnUiThread(new Runnable() {
            @Override
            public void run() {
                MarkerOptions options = null;
                try {
                    String type = eventsArray.getJSONObject(i).getString(JSONParameterConstants.TYPE);
                    options = setUpMarkerDetails(latLng, type, title);
                    if (eventType == EventConstants.EVENT_TYPE_All_INDEX) {
                        Marker marker = mapView.addMarker(options);
                        MARKER_MAP.put(marker, i);
                    } else if (eventType == EventConstants.EVENT_TYPE_EXCLUSIVE_INDEX) {
                        if (type == EventConstants.EVENT_TYPE_EXCLUSIVE_SHORT) {
                            Marker marker = mapView.addMarker(options);
                            MARKER_MAP.put(marker, i);
                        }
                    } else if (eventType == EventConstants.EVENT_TYPE_NORMAL_INDEX) {
                        if (type == EventConstants.EVENT_TYPE_NORMAL_SHORT) {
                            Marker marker = mapView.addMarker(options);
                            MARKER_MAP.put(marker, i);
                        }
                    } else if (eventType == EventConstants.EVENT_TYPE_PEOPLE_INDEX) {
                        if (type == EventConstants.EVENT_TYPE_PEOPLE_SHORT) {
                            Marker marker = mapView.addMarker(options);
                            MARKER_MAP.put(marker, i);
                        }
                    }

                } catch (Exception e) {
                    e.printStackTrace();
                }
            }
        });
    }

    private MarkerOptions setUpMarkerDetails(LatLng latLng, String type, String title) {
        MarkerOptions options = null;
        if (type == EventConstants.EVENT_TYPE_PEOPLE_SHORT) {
            options = new MarkerOptions().position(latLng).title(title).icon(BitmapDescriptorFactory.defaultMarker(BitmapDescriptorFactory.HUE_AZURE));
        } else if (type == EventConstants.EVENT_TYPE_EXCLUSIVE_SHORT) {
            options = new MarkerOptions().position(latLng).title(title).icon(BitmapDescriptorFactory.defaultMarker(BitmapDescriptorFactory.HUE_BLUE));
        } else {
            options = new MarkerOptions().position(latLng).title(title).icon(BitmapDescriptorFactory.defaultMarker(BitmapDescriptorFactory.HUE_GREEN));
        }
        return options;
    }

    private void callDetailActivity(Marker marker) {
        Integer i = MARKER_MAP.get(marker);
        try {
            JSONObject event = eventsArray.getJSONObject(i);
            Intent in = new Intent(this, EventDetailsActivity.class);
            in.putExtra("eventData", event.toString());
            startActivity(in);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public boolean isGooglePlayServicesAvailable() {

        GoogleApiAvailability googleApiAvailability = GoogleApiAvailability.getInstance();
        int status = googleApiAvailability.isGooglePlayServicesAvailable(this);
        if(status != ConnectionResult.SUCCESS) {
            if(googleApiAvailability.isUserResolvableError(status)) {
                googleApiAvailability.getErrorDialog(this, status, 2404).show();
            }
            return false;
        }
        return true;
    }


}
