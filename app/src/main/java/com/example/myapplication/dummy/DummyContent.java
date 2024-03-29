package com.example.myapplication.dummy;

import com.example.myapplication.databases.DatabaseHelper;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * Helper class for providing sample content for user interfaces created by
 * Android template wizards.
 * <p>
 * TODO: Replace all uses of this class before publishing your app.
 */
public class DummyContent {

    /**
     * An array of sample (dummy) items.
     */
    public static final List<DummyItem> ITEMS = new ArrayList<DummyItem>();

    /**
     * A map of sample (dummy) items, by ID.
     */
    public static final Map<String, DummyItem> ITEM_MAP = new HashMap<String, DummyItem>();;

    private static final int COUNT = 25;

    /*static {
        addItem(new DummyItem("1","Una descripción para esta imagen Una descripción para esta imagen Una descripción para esta imagen Una descripción para esta imagen Una descripción para esta imagen","source1","javier"));
        addItem(new DummyItem("2","Una descripción para esta imagen Una descripción para esta imagen Una descripción para esta imagen Una descripción para esta imagen Una descripción para esta imagen","source2", "javier"));
        addItem(new DummyItem("3","Una descripción para esta imagen Una descripción para esta imagen Una descripción para esta imagen Una descripción para esta imagen Una descripción para esta imagen","source3", "javier"));
    }*/

    public static void addItem(DummyItem item) {
        ITEMS.add(item);
        ITEM_MAP.put(item.id, item);
    }

    public static void removeItems(){
        ITEMS.clear();
        ITEM_MAP.clear();
    }

    public static void sortItems(){
        return;
    }

    //private static DummyItem createDummyItem(int position) {
    //    return new DummyItem(String.valueOf(position), "Item " + position, makeDetails(position));
    //}

    private static String makeDetails(int position) {
        StringBuilder builder = new StringBuilder();
        builder.append("Details about Item: ").append(position);
        for (int i = 0; i < position; i++) {
            builder.append("\nMore details information here.");
        }
        return builder.toString();
    }

    /**
     * A dummy item representing a piece of content.
     */
    public static class DummyItem {
        public final String id;
        public final String description;
        public final String image;
        public final String author_name;
        public final String tags;
        public final String date;
        public final String alt;
        public final String lat;

        public DummyItem(String id, String description, String image, String author_name,String tags,String date,String alt,String lat) {
            this.id = id;
            this.description = description;
            this.image = image;
            this.author_name = author_name;
            this.tags = tags;
            this.date = date;
            this.alt = alt;
            this.lat = lat;
        }

        @Override
        public String toString() {
            return description;
        }
    }
}
