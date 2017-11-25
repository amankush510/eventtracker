package com.example.akashpc.eventtracker.constants;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.LinkedHashMap;

/**
 * Created by Akash PC on 11-01-2017.
 */

public class EventConstants {
    public static Integer EVENT_TYPE_DEFAULT_INDEX = 0;
    public static Integer EVENT_TYPE_All_INDEX = 1;
    public static Integer EVENT_TYPE_NORMAL_INDEX = 2;
    public static Integer EVENT_TYPE_EXCLUSIVE_INDEX = 3;
    public static Integer EVENT_TYPE_PEOPLE_INDEX = 4;

    public static String EVENT_TYPE_DEFAULT = "Select";
    public static String EVENT_TYPE_ALL = "All";
    public static String EVENT_TYPE_NORMAL = "Normal";
    public static String EVENT_TYPE_EXCLUSIVE = "Exclusive";
    public static String EVENT_TYPE_PEOPLE = "People";

    public static ArrayList<Integer> EVENTS_KEYS = new ArrayList();
    public static ArrayList<String> EVENTS_VALUES = new ArrayList();



    public static LinkedHashMap<Integer, String> EVENTS_TYPE_MAP = new LinkedHashMap<>();

    static {
        EVENTS_TYPE_MAP.put(EVENT_TYPE_DEFAULT_INDEX, EVENT_TYPE_DEFAULT);
        EVENTS_TYPE_MAP.put(EVENT_TYPE_All_INDEX, EVENT_TYPE_ALL);
        EVENTS_TYPE_MAP.put(EVENT_TYPE_NORMAL_INDEX, EVENT_TYPE_NORMAL);
        EVENTS_TYPE_MAP.put(EVENT_TYPE_EXCLUSIVE_INDEX, EVENT_TYPE_EXCLUSIVE);
        EVENTS_TYPE_MAP.put(EVENT_TYPE_PEOPLE_INDEX, EVENT_TYPE_PEOPLE);

        for (Integer key : EVENTS_TYPE_MAP.keySet()) {
            EVENTS_KEYS.add(key);
        }

        for (String value : EVENTS_TYPE_MAP.values()) {
            EVENTS_VALUES.add(value);
        }
    }

    public static String EVENT_TYPE_PEOPLE_SHORT = "pe";
    public static String EVENT_TYPE_EXCLUSIVE_SHORT = "ex";
    public static String EVENT_TYPE_NORMAL_SHORT = "ev";

}
