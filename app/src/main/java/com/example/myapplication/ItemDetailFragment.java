package com.example.myapplication;

import android.app.Activity;
import android.content.ClipData;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.net.Uri;
import android.os.Bundle;
import com.google.android.material.appbar.CollapsingToolbarLayout;
import androidx.fragment.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;
import com.example.myapplication.dummy.DummyContent;


import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;

import static android.content.Context.MODE_PRIVATE;

/**
 * A fragment representing a single Item detail screen.
 * This fragment is either contained in a {@link ItemListActivity}
 * in two-pane mode (on tablets) or a {@link ItemDetailActivity}
 * on handsets.
 */
public class ItemDetailFragment extends Fragment {
    /**
     * The fragment argument representing the item ID that this fragment
     * represents.
     */
    public static final String ARG_ITEM_ID = "item_id";
    private SharedPreferences mySharedPreferences;
    private SharedPreferences.Editor mEditor;
    private static final String SHARED_PREF_NAME = "username";
    private static final String KEY_NAME = "key_username";

    /**
     * The dummy content this fragment is presenting.
     */
    private DummyContent.DummyItem mItem;

    /**
     * Mandatory empty constructor for the fragment manager to instantiate the
     * fragment (e.g. upon screen orientation changes).
     */

    public ItemDetailFragment() {
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        mySharedPreferences = this.getActivity().getSharedPreferences(SHARED_PREF_NAME, MODE_PRIVATE);

        if (getArguments().containsKey(ARG_ITEM_ID)) {
            // Load the dummy content specified by the fragment
            // arguments. In a real-world scenario, use a Loader
            // to load content from a content provider.
            mItem = DummyContent.ITEM_MAP.get(getArguments().getString(ARG_ITEM_ID));

            Activity activity = this.getActivity();
            CollapsingToolbarLayout appBarLayout = (CollapsingToolbarLayout) activity.findViewById(R.id.toolbar_layout);
            appBarLayout.setCollapsedTitleTextAppearance(R.style.CollapsedAppBar);
            appBarLayout.setExpandedTitleTextAppearance(R.style.ExpandedAppBar);
            if (appBarLayout != null) {
                appBarLayout.setTitle("Publicación de " + mItem.author_name);
            }
        }
    }

    public int getImageID(String imageName){
        return getResources().getIdentifier("drawable/" + imageName, null, getActivity().getPackageName());
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View rootView = inflater.inflate(R.layout.item_detail, container, false);
        Button map_btn = rootView.findViewById(R.id.view_map);

        ImageView imagedetail = rootView.findViewById(R.id.item_image);
        map_btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent home = new Intent(getActivity(), MapsActivity.class);
                startActivity(home);
            }
        });
        // Show the dummy content as text in a TextView.
        if (!(mySharedPreferences.getString(KEY_NAME,null).equals(mItem.author_name))){
            ((ItemDetailActivity)getActivity()).hideFab();
            ((ItemDetailActivity) getActivity()).hideDel();
        }
        if (mItem != null) {
            ((TextView) rootView.findViewById(R.id.item_detail)).setText(mItem.description);
            ((TextView) rootView.findViewById(R.id.date)).setText(mItem.date);
            ((TextView) rootView.findViewById(R.id.tags_content)).setText(mItem.tags);
            String spath = mItem.image;

            if (spath.contains("/data/user")){

                File path = new File(mItem.image);
                ImageView imageView = (ImageView) rootView.findViewById(R.id.item_image);
                imageView.setImageBitmap(BitmapFactory.decodeFile(path.getAbsolutePath()));
            }

            else{
                ((ImageView) rootView.findViewById(R.id.item_image)).setImageResource(getImageID(mItem.image));
            }
        }

        imagedetail.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                final File path = new File(mItem.image);
                Intent mediaScanIntent = new Intent(Intent.ACTION_MEDIA_SCANNER_SCAN_FILE);
                Uri contentUri = Uri.fromFile(path);
                mediaScanIntent.setData(contentUri);
                getActivity().sendBroadcast(mediaScanIntent);
            }
        });

        return rootView;
    }
}
