package com.example.akashpc.eventtracker.utils;

import android.content.Context;

import org.apache.commons.io.IOUtils;
import org.json.JSONObject;

import java.io.InputStream;

/**
 * Created by Akash PC on 11-01-2017.
 */

public class GetJSONFromFile {
        private Context context;

        public GetJSONFromFile(Context context){
            this.context = context;
        }

        public JSONObject getEventsData() {
            JSONObject eventsData = null;
            try {
                InputStream is = context.getAssets().open("data.json");
                String jsonTxt = IOUtils.toString(is);
                eventsData = new JSONObject(jsonTxt);
            }
            catch (Exception e){
                e.printStackTrace();
            }
            return eventsData;
        }
}
