package com.example.myapplication;

import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.database.sqlite.SQLiteDatabase;
import android.os.Bundle;

import com.example.myapplication.databases.DatabaseHelper;
import com.google.android.material.floatingactionbutton.FloatingActionButton;

import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.widget.Toolbar;
import android.view.View;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.app.ActionBar;
import android.view.MenuItem;
import android.widget.Toast;

/**
 * An activity representing a single Item detail screen. This
 * activity is only used on narrow width devices. On tablet-size devices,
 * item details are presented side-by-side with a list of items
 * in a {@link ItemListActivity}.
 */
public class ItemDetailActivity extends AppCompatActivity {

    private static final String SHARED_PREF_NAME = "username";
    private static final String KEY_MOMENT = "key_moment";
    private SharedPreferences mySharedPreferences;
    

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_item_detail);

        DatabaseHelper helper = new DatabaseHelper(this);
        final SQLiteDatabase db = helper.open();
        Toolbar toolbar = (Toolbar) findViewById(R.id.detail_toolbar);
        setSupportActionBar(toolbar);

        //SupportMapFragment mapFragment = (SupportMapFragment) getSupportFragmentManager().findFragmentById(R.id.map);
        //mapFragment.getMapAsync(this);

        FloatingActionButton fab = (FloatingActionButton) this.findViewById(R.id.fab);
        FloatingActionButton del = this.findViewById(R.id.delete);

        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view){
                Intent edit = new Intent(ItemDetailActivity.this, ItemEdit.class);
                startActivity(edit);
                finish();
                //Snackbar.make(view, "Replace with your own detail action", Snackbar.LENGTH_LONG).setAction("Action", null).show();
            }
        });

        del.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                AlertDialog.Builder builder = new AlertDialog.Builder(ItemDetailActivity.this);
                builder.setTitle("Seleccione una opcion");
                builder.setMessage("¿Quiere borrar este momento?");
                mySharedPreferences = getSharedPreferences(SHARED_PREF_NAME, MODE_PRIVATE);
                final String moment = mySharedPreferences.getString(KEY_MOMENT,null);
                builder.setPositiveButton("Si", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        String query = "DELETE FROM moments WHERE id = "+moment;
                        try{
                            db.execSQL(query);
                            Toast toast = Toast.makeText(getApplicationContext(),"Momento eliminado", Toast.LENGTH_SHORT);
                            toast.show();
                        }
                        catch (Exception e){
                            e.printStackTrace();
                        }
                        finally {
                            db.close();
                            Intent itemlist = new Intent(ItemDetailActivity.this,ItemListActivity.class);
                            finish();
                            startActivity(itemlist);
                        }
                    }
                });

                builder.setNegativeButton("No", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        return;
                    }
                });

                AlertDialog dialog = builder.create();
                dialog.show();
            }
        });

        // Show the Up button in the action bar.
        ActionBar actionBar = getSupportActionBar();
        if (actionBar != null) {
            actionBar.setDisplayHomeAsUpEnabled(true);
        }

        // savedInstanceState is non-null when there is fragment state
        // saved from previous configurations of this activity
        // (e.g. when rotating the screen from portrait to landscape).
        // In this case, the fragment will automatically be re-added
        // to its container so we don't need to manually add it.
        // For more information, see the Fragments API guide at:
        //
        // http://developer.android.com/guide/components/fragments.html
        //
        if (savedInstanceState == null) {
            // Create the detail fragment and add it to the activity
            // using a fragment transaction.
            Bundle arguments = new Bundle();
            arguments.putString(ItemDetailFragment.ARG_ITEM_ID,
                    getIntent().getStringExtra(ItemDetailFragment.ARG_ITEM_ID));
            ItemDetailFragment fragment = new ItemDetailFragment();
            fragment.setArguments(arguments);
            getSupportFragmentManager().beginTransaction()
                    .add(R.id.item_detail_container, fragment)
                    .commit();
        }
    }

    public void hideFab(){
        FloatingActionButton fab = (FloatingActionButton) this.findViewById(R.id.fab);
        fab.hide();
    }

    public void hideDel(){
        FloatingActionButton del = (FloatingActionButton) this.findViewById(R.id.delete);
        del.hide();
    }

    @Override
    public void onBackPressed(){
        super.onBackPressed();
        Intent itemlist = new Intent(ItemDetailActivity.this,ItemListActivity.class);
        finish();
        startActivity(itemlist);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        int id = item.getItemId();
        if (id == android.R.id.home) {
            // This ID represents the Home or Up button. In the case of this
            // activity, the Up button is shown. For
            // more details, see the Navigation pattern on Android Design:
            //
            // http://developer.android.com/design/patterns/navigation.html#up-vs-back
            //
            navigateUpTo(new Intent(this, ItemListActivity.class));
            return true;
        }
        return super.onOptionsItemSelected(item);
    }
}
