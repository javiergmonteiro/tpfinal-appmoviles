package com.example.myapplication;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.graphics.BitmapFactory;
import android.net.Uri;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.RecyclerView;
import androidx.appcompat.widget.Toolbar;
import androidx.swiperefreshlayout.widget.SwipeRefreshLayout;

import com.example.myapplication.databases.DatabaseHelper;
import com.google.android.material.floatingactionbutton.FloatingActionButton;

import android.os.Handler;
import android.view.KeyEvent;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.view.inputmethod.EditorInfo;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.example.myapplication.dummy.DummyContent;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.nio.file.Path;
import java.util.List;

/**
 * An activity representing a list of Items. This activity
 * has different presentations for handset and tablet-size devices. On
 * handsets, the activity presents a list of items, which when touched,
 * lead to a {@link ItemDetailActivity} representing
 * item details. On tablets, the activity presents the list of items and
 * item details side-by-side using two vertical panes.
 */
public class ItemListActivity extends AppCompatActivity implements SwipeRefreshLayout.OnRefreshListener {

    /**
     * Whether or not the activity is in two-pane mode, i.e. running on a tablet
     * device.
     */
    private boolean mTwoPane;
    private SharedPreferences mySharedPreferences;
    private SharedPreferences.Editor mEditor;
    private static final String SHARED_PREF_NAME = "username";
    private static final String KEY_SEARCH = "key_search";
    private static final String KEY_MOMENT = "key_moment";
    DatabaseHelper helper;
    private static final DummyContent dummyContent = new DummyContent();
    private SwipeRefreshLayout mSwipeRefreshLayout;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_item_list);
        //mSwipeRefreshLayout = (SwipeRefreshLayout) findViewById(R.id.container);
        //mSwipeRefreshLayout.setOnRefreshListener(this);
        mySharedPreferences = getSharedPreferences(SHARED_PREF_NAME, MODE_PRIVATE);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        toolbar.setTitle(getTitle());
        helper = new DatabaseHelper(this);
        SQLiteDatabase db = helper.open();

        final EditText searchtext = findViewById(R.id.sarchbox);
        final Button launch_search = findViewById(R.id.launch_search);

        dummyContent.removeItems();

        if (!(mySharedPreferences.getString(KEY_SEARCH,"null") == "null")){
            String search = mySharedPreferences.getString(KEY_SEARCH,null);
            String query = "SELECT * FROM moments where tags like '%"+search+"'";
            Cursor c = db.rawQuery(query,null);
            if (c.moveToFirst()) {
                for (c.moveToFirst(); !c.isAfterLast(); c.moveToNext()) {
                    String id = c.getString(0);
                    String autor = c.getString(1);
                    String descripcion = c.getString(2);
                    String tags = c.getString(3);
                    String image = c.getString(4);
                    String date = c.getString(5);
                    String alt = c.getString(6);
                    String lat = c.getString(7);
                    dummyContent.addItem(new DummyContent.DummyItem(id, descripcion, image, autor, tags, date, alt, lat));
                }
            }

            else{
                Toast toast = Toast.makeText(getApplicationContext(),"No se encontraron resultados", Toast.LENGTH_SHORT);
            }

            db.close();
        }

        else{
            Cursor c = db.rawQuery("SELECT * FROM moments",null);
            for (c.moveToFirst(); !c.isAfterLast(); c.moveToNext()) {
                String id = c.getString(0);
                String autor = c.getString(1);
                String descripcion = c.getString(2);
                String tags = c.getString(3);
                String image = c.getString(4);
                String date = c.getString(5);
                String alt = c.getString(6);
                String lat = c.getString(7);
                dummyContent.addItem(new DummyContent.DummyItem(id,descripcion,image,autor,tags,date,alt,lat));
            }
            db.close();
        }

        launch_search.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String mysearch = searchtext.getText().toString();
                mySharedPreferences = getSharedPreferences(SHARED_PREF_NAME, MODE_PRIVATE);
                mEditor = mySharedPreferences.edit();
                if (mysearch == "") {
                    mEditor.putString(KEY_SEARCH,"null");
                    mEditor.apply();
                }
                else{
                    mEditor.putString(KEY_SEARCH,mysearch);
                    mEditor.apply();
                }
                finish();
                startActivity(getIntent());
            }
        });

        FloatingActionButton fab = (FloatingActionButton) findViewById(R.id.fab);
        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                try{
                    Intent newitem = new Intent(ItemListActivity.this, NewItemActivity.class);
                    startActivity(newitem);
                } catch (Exception e){
                    e.printStackTrace();
                }
            }
        });

        if (findViewById(R.id.item_detail_container) != null) {
            // The detail container view will be present only in the
            // large-screen layouts (res/values-w900dp).
            // If this view is present, then the
            // activity should be in two-pane mode.
            mTwoPane = true;
        }

        View recyclerView = findViewById(R.id.item_list);
        assert recyclerView != null;
        setupRecyclerView((RecyclerView) recyclerView);
    }


    @Override
    public boolean onCreateOptionsMenu(Menu menu){
        MenuInflater inflater = getMenuInflater();
        inflater.inflate(R.menu.menu, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item){
        switch (item.getItemId()){
            case R.id.logout:
                logout();
                return true;
                default:
                    return super.onOptionsItemSelected(item);
        }
    }

    @Override
    public void onRefresh() {
        new Handler().postDelayed(new Runnable() {
            @Override
            public void run() {
                //mSwipeRefreshLayout.setRefreshing(false);
            }
        }, 2000);
    }

    private void logout() {
        mySharedPreferences = getSharedPreferences(SHARED_PREF_NAME, MODE_PRIVATE);
        mEditor = mySharedPreferences.edit();
        mEditor.clear().commit();
        Intent Main = new Intent(ItemListActivity.this, MainActivity.class);
        startActivity(Main);
        finish();
    }

    private void setupRecyclerView(@NonNull RecyclerView recyclerView) {
        recyclerView.setAdapter(new SimpleItemRecyclerViewAdapter(this, DummyContent.ITEMS, mTwoPane));
    }

    public class SimpleItemRecyclerViewAdapter
            extends RecyclerView.Adapter<SimpleItemRecyclerViewAdapter.ViewHolder> {

        private final ItemListActivity mParentActivity;
        private final List<DummyContent.DummyItem> mValues;
        private final boolean mTwoPane;
        private final View.OnClickListener mOnClickListener = new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                DummyContent.DummyItem item = (DummyContent.DummyItem) view.getTag();
                if (mTwoPane) {
                    Bundle arguments = new Bundle();
                    arguments.putString(ItemDetailFragment.ARG_ITEM_ID, item.id);
                    ItemDetailFragment fragment = new ItemDetailFragment();
                    fragment.setArguments(arguments);
                    mParentActivity.getSupportFragmentManager().beginTransaction()
                            .replace(R.id.item_detail_container, fragment)
                            .commit();
                } else {
                    Context context = view.getContext();
                    Intent intent = new Intent(context, ItemDetailActivity.class);
                    intent.putExtra(ItemDetailFragment.ARG_ITEM_ID, item.id);
                    mySharedPreferences = getSharedPreferences(SHARED_PREF_NAME, MODE_PRIVATE);
                    SharedPreferences.Editor editor = mySharedPreferences.edit();
                    editor.putString(KEY_MOMENT, item.id);
                    editor.apply();
                    finish();
                    context.startActivity(intent);
                }
            }
        };

        SimpleItemRecyclerViewAdapter(ItemListActivity parent,
                                      List<DummyContent.DummyItem> items,
                                      boolean twoPane) {
            mValues = items;
            mParentActivity = parent;
            mTwoPane = twoPane;
        }

        @Override
        public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
            View view = LayoutInflater.from(parent.getContext())
                    .inflate(R.layout.item_list_content, parent, false);
            return new ViewHolder(view);
        }

        public int getImageID(String imageName){
          return getResources().getIdentifier("drawable/" + imageName, null, getPackageName());
        }

        @Override
        public void onBindViewHolder(final ViewHolder holder, int position) {

            String autor = mValues.get(position).author_name + " ha publicado algo:";
            String descripcion = '"' + mValues.get(position).description + '"';
            holder.mIdView.setText(autor);
            holder.mContentView.setText(descripcion);

            String spath = mValues.get(position).image;

            if (spath.contains("/data/user")){

                File path = new File(mValues.get(position).image);
                holder.mImageView.setImageBitmap(BitmapFactory.decodeFile(path.getAbsolutePath()));
            }

            else{
                holder.mImageView.setImageResource(getImageID(mValues.get(position).image));
            }

            holder.itemView.setTag(mValues.get(position));
            holder.itemView.setOnClickListener(mOnClickListener);
        }

        @Override
        public int getItemCount() {
            return mValues.size();
        }

        class ViewHolder extends RecyclerView.ViewHolder {
            final TextView mIdView;
            final TextView mContentView;
            final ImageView mImageView;

            ViewHolder(View view) {
                super(view);
                mIdView = (TextView) view.findViewById(R.id.id_text);
                mContentView = (TextView) view.findViewById(R.id.content);
                mImageView = (ImageView) view.findViewById(R.id.image);
            }
        }
    }
}
