package com.example.fa_deepanparikh_c0824505_android;

import android.app.Activity;
import android.content.Intent;
import android.os.AsyncTask;
import android.os.Bundle;
import android.widget.Toast;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.DividerItemDecoration;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import java.util.ArrayList;
import java.util.List;

public class MainActivity extends AppCompatActivity {

    private static final int UPDATE_LIST = 200;
    private RecyclerView rv_places;
    private PlaceAdapter placeAdapter;
    private RecyclerTouchListener touchListener;
    private ArrayList<Place> placesList;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        findViewById(R.id.floating_button_add).setOnClickListener(view -> {
            editPlace("add", -1);
        });
        
        rv_places = findViewById(R.id.rv_list);
        rv_places.setLayoutManager(new LinearLayoutManager(this));
        rv_places.addItemDecoration(new DividerItemDecoration(rv_places.getContext(), DividerItemDecoration.VERTICAL));
        placeAdapter = new PlaceAdapter(this);
        placesList = new ArrayList<>();
        rv_places.setAdapter(placeAdapter);
        getPlacesList();

        touchListener = new RecyclerTouchListener(this, rv_places);
        touchListener
                .setClickable(new RecyclerTouchListener.OnRowClickListener() {
                    @Override
                    public void onRowClicked(int position) {
                        //editPlace("", position);
                    }

                    @Override
                    public void onIndependentViewClicked(int independentViewID, int position) {

                    }
                })
                .setSwipeOptionViews(R.id.delete_place, R.id.edit_place, R.id.visit_place)
                .setSwipeable(R.id.rowFG, R.id.rowBG, new RecyclerTouchListener.OnSwipeOptionsClickListener() {
                    @Override
                    public void onSwipeOptionClicked(int viewID, int position) {
                        switch (viewID) {
                            case R.id.delete_place:
                                deletePlace(placesList.get(position));
                                break;
                            case R.id.edit_place:
                                editPlace("edit", position);
                                break;
                            case R.id.visit_place:
                                UpdatePlace up = new UpdatePlace(position);
                                up.execute();
                                break;
                        }
                    }
                });
        rv_places.addOnItemTouchListener(touchListener);
        
    }

    private void updateUI() {
        getPlacesList();
    }

    private void editPlace(String action, int position) {
        Intent intent = new Intent(MainActivity.this, MapsActivity.class);
        if (position != -1) {

            intent.putExtra("ClickedPlace", placesList.get(position));

            String address = placesList.get(position).getAddress();
            intent.putExtra("address", address.isEmpty() ? placesList.get(position).getDate() : address);
            intent.putExtra("lat", placesList.get(position).getLatitude());
            intent.putExtra("lng", placesList.get(position).getLongitude());
        }
        intent.putExtra("action", action);
        if (!action.isEmpty()) {
            startActivityForResult(intent, UPDATE_LIST);
        } else {
            startActivity(intent);
        }
    }

    @Override
    public void onResume() {
        super.onResume();
        rv_places.addOnItemTouchListener(touchListener);
    }

    private void getPlacesList() {
        class GetTasks extends AsyncTask<Void, Void, List<Place>> {

            @Override
            protected List<Place> doInBackground(Void... voids) {
                List<Place> placeList = DatabaseClient
                        .getInstance(getApplicationContext())
                        .getAppDatabase()
                        .placeDao()
                        .getAll();
                return placeList;
            }

            @Override
            protected void onPostExecute(List<Place> favoritePlaces) {
                super.onPostExecute(favoritePlaces);
                placesList.clear();
                placesList.addAll(favoritePlaces);
                placeAdapter.setFavoritePlaces(placesList);
            }
        }

        GetTasks gt = new GetTasks();
        gt.execute();
    }

    private void deletePlace(final Place place) {
        class DeletePlace extends AsyncTask<Void, Void, Void> {

            @Override
            protected Void doInBackground(Void... voids) {
                DatabaseClient.getInstance(getApplicationContext()).getAppDatabase()
                        .placeDao()
                        .delete(place);
                return null;
            }

            @Override
            protected void onPostExecute(Void aVoid) {
                super.onPostExecute(aVoid);
                updateUI();
                Toast.makeText(getApplicationContext(), "Deleted", Toast.LENGTH_LONG).show();
            }
        }

        DeletePlace dp = new DeletePlace();
        dp.execute();
    }

    class UpdatePlace extends AsyncTask<Void, Void, Void> {

        Place place;

        public UpdatePlace(int position) {
            place = placesList.get(position);
        }

        @Override
        protected Void doInBackground(Void... voids) {
            place.setCategory("Visited");
            DatabaseClient.getInstance(getApplicationContext()).getAppDatabase()
                    .placeDao()
                    .update(place);
            return null;
        }

        @Override
        protected void onPostExecute(Void aVoid) {
            super.onPostExecute(aVoid);
            updateUI();
        }
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (resultCode == Activity.RESULT_OK && requestCode == UPDATE_LIST) {
            updateUI();
        }
    }

}