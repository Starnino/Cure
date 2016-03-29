package com.doctorfinderapp.doctorfinder.activity;

import android.content.Context;
import android.content.Intent;
import android.graphics.BitmapFactory;
import android.os.Build;
import android.support.design.widget.NavigationView;
import android.support.design.widget.Snackbar;
import android.support.design.widget.TabLayout;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;
import android.support.v4.view.GravityCompat;
import android.support.v4.view.MenuItemCompat;
import android.support.v4.view.ViewPager;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBarDrawerToggle;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.SearchView;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.WindowManager;
import android.widget.TextView;
import android.widget.Toast;

import com.doctorfinderapp.doctorfinder.R;
import com.doctorfinderapp.doctorfinder.activity.access.SplashActivity;
import com.doctorfinderapp.doctorfinder.fragment.DoctorListFragment;
import com.doctorfinderapp.doctorfinder.fragment.DoctorMapsFragment;
import com.doctorfinderapp.doctorfinder.functions.GlobalVariable;
import com.doctorfinderapp.doctorfinder.functions.RoundedImageView;
import com.doctorfinderapp.doctorfinder.functions.Util;
import com.parse.FindCallback;
import com.parse.GetCallback;
import com.parse.GetDataCallback;
import com.parse.LogOutCallback;
import com.parse.ParseException;
import com.parse.ParseFile;
import com.parse.ParseObject;
import com.parse.ParseQuery;
import com.parse.ParseUser;
import java.util.ArrayList;
import java.util.List;

import cn.pedant.SweetAlert.SweetAlertDialog;


public class ResultsActivity extends AppCompatActivity implements NavigationView.OnNavigationItemSelectedListener{

    private static final String TAG ="Results Activity" ;
    private DrawerLayout mDrawerLayout;
    private static ViewPager viewPager;
    private static TabLayout tabs;
    private com.melnykov.fab.FloatingActionButton fab, fab_location;
    private MenuItem searchItem;
    private MenuItem filterItem;
    private SearchView searchView;
    private static Context c;
    private NavigationView navigationView;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        c=getApplicationContext();
        //adding doctors data
        //AddDoctors.addData();

        //aggiungo le foto dei dottori
       // AddDoctors.addPhoto(getResources());

        //set status bar color because in xml don't work
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
            getWindow().addFlags(WindowManager.LayoutParams.FLAG_DRAWS_SYSTEM_BAR_BACKGROUNDS);
            getWindow().setStatusBarColor(getResources().getColor(R.color.colorPrimaryDark));
        }

        //ParseUser currentUser = ParseUser.getCurrentUser();

        //download from db
        showDataM();

        setContentView(R.layout.activity_results);

        //find fab buttons
        fab_location = (com.melnykov.fab.FloatingActionButton) findViewById(R.id.fab_location);
        fab = (com.melnykov.fab.FloatingActionButton) findViewById(R.id.fab);

        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent_dottore = new Intent(ResultsActivity.this, WebViewActivity.class);

                Bundle dottore = new Bundle();
                dottore.putString("URL",
                        GlobalVariable.URLDoctorForm );
                intent_dottore.putExtras(dottore);
                startActivity(intent_dottore);
            }
        });

        //onClick button
        fab_location.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(final View v) {

               /* DoctorMapsFragment.googleMap.setOnMyLocationButtonClickListener(new GoogleMap.OnMyLocationButtonClickListener() {
                    @Override
                    public boolean onMyLocationButtonClick() {
                        Snackbar snackbar = Snackbar
                                .make(v, "Cercando la tua posizione", Snackbar.LENGTH_LONG);

                        snackbar.show();
                        LocationManager mgr = (LocationManager) getSystemService(Context.LOCATION_SERVICE);
                        if (!mgr.isProviderEnabled(LocationManager.GPS_PROVIDER)) {
                           // Toast.makeText(getApplicationContext(), "GPS is disabled!", Toast.LENGTH_SHORT).show();

                            Snackbar snackbar2 = Snackbar
                                    .make(v, "GPS is disabled! ", Snackbar.LENGTH_LONG);
                            snackbar2.show();


                        }
                        return false;
                    }
                });*/
            }
        });

        // Setting ViewPager for each Tabs
        viewPager = (ViewPager) findViewById(R.id.viewpager);

        // Give the TabLayout
        tabs = (TabLayout) findViewById(R.id.tabs);


        // Adding Toolbar to Main screen
        Toolbar toolbar = (Toolbar) findViewById(R.id.results_toolbar);
        setSupportActionBar(toolbar);


        mDrawerLayout = (DrawerLayout) findViewById(R.id.drawer_results);
        ActionBarDrawerToggle toggle = new ActionBarDrawerToggle(
                this, mDrawerLayout, toolbar, R.string.navigation_drawer_open, R.string.navigation_drawer_close);
        mDrawerLayout.setDrawerListener(toggle);
        toggle.syncState();

        navigationView = (NavigationView) findViewById(R.id.nav_view_results);
        navigationView.setNavigationItemSelectedListener(this);

        //setting header

        ParseUser user = ParseUser.getCurrentUser();
        setProfileInformation(user);

    }

    public void setProfileInformation(ParseUser user){
        if (user != null
                //&& GlobalVariable.SEMAPHORE
                ) {
            getUserImage(user);
            View header = navigationView.getHeaderView(0);
            TextView nome = (TextView) header.findViewById(R.id.name_user);

            Log.d("", user.getString("fName"));
            String name = user.getString("fName");
            nome.setText(name);
            TextView email = (TextView) header.findViewById(R.id.email_user);

            email.setText(user.getEmail());
            //Re -set image

            if (GlobalVariable.UserPropic != null) {
                //Log.d("ciao", GlobalVariable.UserPropic.toString());
                RoundedImageView mImg = (RoundedImageView) header.findViewById(R.id.user_propic);
                mImg.setImageBitmap(GlobalVariable.UserPropic);
            }
        }
    }


    // Add Fragments to Tabs
    private void setupViewPager(ViewPager viewPager) {
        Adapter adapter = new Adapter(getSupportFragmentManager());
        adapter.addFragment(new DoctorListFragment(), "Lista");
        adapter.addFragment(new DoctorMapsFragment(), "Mappa");

        viewPager.addOnPageChangeListener(new ViewPager.OnPageChangeListener() {
            @Override
            public void onPageScrolled(int position, float positionOffset, int positionOffsetPixels) {

            }

            @Override
            public void onPageSelected(int position) {
                switchFAB(position);
                switchMenuItem(position);
            }

            @Override
            public void onPageScrollStateChanged(int state) {

            }
        });
        viewPager.setAdapter(adapter);
        tabs.setupWithViewPager(viewPager);
        fab.show();
    }

    @Override
    protected void onResume() {
        setProfileInformation(ParseUser.getCurrentUser());
        super.onResume();
    }

    //search view
    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.menu_search, menu);
        searchItem = menu.findItem(R.id.action_search);
        //TODO REMOVE filterItem = menu.findItem(R.id.action_filter);

        searchView = (SearchView) MenuItemCompat.getActionView(searchItem);


            searchView.setOnQueryTextListener(new SearchView.OnQueryTextListener() {
                @Override
                public boolean onQueryTextSubmit(String query) {
                return true;
            }

                @Override
                public boolean onQueryTextChange(String newText) {
                    final List<ParseObject> doctorsFilter = filter(GlobalVariable.DOCTORS, newText);
                    DoctorListFragment.refreshDoctors(doctorsFilter);
                    return false;
                }
            });

        // Configure the search info and add any event listeners...
        return super.onCreateOptionsMenu(menu);
    }


    @Override
    public boolean onNavigationItemSelected(MenuItem item) {
        // Handle navigation view item clicks here.

        switch (item.getItemId()) {

            case R.id.profile:
                if(ParseUser.getCurrentUser() != null
                       // && GlobalVariable.SEMAPHORE
                        ){
                    Intent intent_user = new Intent(this, UserProfileActivity.class);
                    startActivity(intent_user);
                }
                else Snackbar.make(mDrawerLayout, "Connetti il tuo profilo a Facebook!", Snackbar.LENGTH_SHORT)
                        .setAction("Action", null).show();
                break;


            case R.id.inserisci_dottore:
                Intent intent_dottore = new Intent(this, WebViewActivity.class);

                Bundle dottore = new Bundle();
                dottore.putString("URL",
                        GlobalVariable.URLDoctorForm );
                intent_dottore.putExtras(dottore);
                startActivity(intent_dottore);
                break;

            case R.id.support:
                Util.sendFeedbackMail(ResultsActivity.this);
                break;

            case R.id.like:
                Intent intent_like = Util.getOpenFacebookIntent(this);

                startActivity(intent_like);
                break;

            case R.id.logout:
                ParseUser.logOutInBackground(new LogOutCallback() {
                    @Override
                    public void done(ParseException e) {
                        Log.d("R", "Logged out");
                        Toast.makeText(getApplicationContext(),
                                "Logged out",
                                Toast.LENGTH_LONG).show();
                        Intent intent = new Intent(ResultsActivity.this ,  SplashActivity.class);
                        intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
                        startActivity(intent);
                        finish();
                    }
                });

                break;

            case R.id.informativa:
                Intent informativa = new Intent(this, Informativa.class);
                startActivity(informativa);
                break;

        }

        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_results);
        drawer.closeDrawer(GravityCompat.START);
        return true;
    }


    static class Adapter extends FragmentPagerAdapter {

        private final List<Fragment> mFragmentList = new ArrayList<>();
        private final List<String> mFragmentTitleList = new ArrayList<>();

        public Adapter(FragmentManager manager) {
            super(manager);
        }

        @Override
        public Fragment getItem(int position) {
            return mFragmentList.get(position);
        }

        @Override
        public int getCount() {
            return mFragmentList.size();
        }

        public void addFragment(Fragment fragment, String title) {
            mFragmentList.add(fragment);
            mFragmentTitleList.add(title);
        }


        @Override
        public CharSequence getPageTitle(int position) {
            return mFragmentTitleList.get(position);
        }
    }

    //switch fab
    public void switchFAB(int position){
        switch(position){
            case 0:
                Log.d("fab", "open");
                //fab_location.startAnimation(fab_close);
                //fab_location.setClickable(false);
                fab.show();
                fab.setClickable(true);
                break;

            case 1:
                Log.d("fab_location", "open");
                fab.hide();
                fab.setClickable(false);
                //fab_location.startAnimation(fab_open_normal);
                //fab_location.setClickable(true);
                break;
        }
    }

    public void switchMenuItem(int position){
        if (position == 1) {
            searchItem.setVisible(false);
            searchView.setVisibility(View.INVISIBLE);
            //TODO REMOVE filterItem.setVisible(false);
        }
        else {
            searchItem.setVisible(true);
            searchView.setVisibility(View.VISIBLE);
            //TODO REMOVE filterItem.setVisible(true);
        }
    }

    @Override
    public void onBackPressed() {
        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_results);
        if (drawer.isDrawerOpen(GravityCompat.START)) {
            drawer.closeDrawer(GravityCompat.START);
        } else {
            super.onBackPressed();
        }

        this.finish();
    }

    @Override
    public void finish() {
        super.finish();
    }

    //download doctors from DB

    public void showDataM() {

        //get query: All doctor
        ParseQuery<ParseObject> doctorsQuery = ParseQuery.getQuery("Doctor");

        //retrieve object with multiple city
        if (MainActivity.CITY.size() != 0 && MainActivity.CITY.size() != MainActivity.citta.length)
            doctorsQuery.whereContainedIn("Province", MainActivity.CITY);

        //Log.d("RESULTS SEARCH FOR --> ", MainActivity.CITY + "");

        //retrieve object with multiple city
        if (MainActivity.SPECIAL.size() != 0 && MainActivity.SPECIAL.size() != MainActivity.special.length)
            doctorsQuery.whereContainedIn("Specialization", MainActivity.SPECIAL);

        //order by LastName
        if (MainActivity.CITY.size() != 0 || MainActivity.SPECIAL.size() != 0) {
            doctorsQuery.orderByAscending("LastName");
        }

        //progress dialog
        final SweetAlertDialog dialog = new SweetAlertDialog(this, SweetAlertDialog.PROGRESS_TYPE);
        dialog.setTitleText("Caricamento");
        dialog.getProgressHelper().setBarColor(getResources().getColor(R.color.docfinder));
        dialog.show();

        doctorsQuery.findInBackground(new FindCallback<ParseObject>() {
            @Override
            public void done(List<ParseObject> objects, ParseException e) {

                if (e == null) {

                    GlobalVariable.DOCTORS = objects;
                    /*for (int i = 0; i < GlobalVariable.DOCTORS.size(); i++) {
                        int j = i + 1;
                        Log.d("DOCTOR " + j, " --> " + objects.get(i).get("FirstName") + " " + objects.get(i).get("LastName"));
                    }*/

                    dialog.cancel();
                    setupViewPager(viewPager);

                    Toast.makeText(getApplicationContext(), GlobalVariable.DOCTORS.size() + " specialisti trovati", Toast.LENGTH_LONG).show();
                    Util.dowloadDoctorPhoto(GlobalVariable.DOCTORS);
                    Log.d(TAG,"DOCTORS.size() "+GlobalVariable.DOCTORS.size());
                } else {
                    dialog.cancel();
                    new SweetAlertDialog(getApplicationContext(), SweetAlertDialog.ERROR_TYPE)
                            .setTitleText("Oops...")
                            .setContentText("Qualcosa è andato storto!")
                            .show();
                }
            }
        });

    }

    public List<ParseObject> filter(List<ParseObject> Doctors,String query){
        query = query.toLowerCase();
        final List<ParseObject> filteredModelList = new ArrayList<>();
        for (ParseObject doctor : Doctors) {
            final String textName = doctor.getString("FirstName").toLowerCase();
            final String textSurname = doctor.getString("LastName").toLowerCase();
            if (textSurname.startsWith(query) || textName.startsWith(query)) {
                //Log.d("QUERY: " + query + "--> ", textSurname);
                filteredModelList.add(doctor);
            }
        }
        return filteredModelList;
    }

    public void profile_click(View v){
        if(ParseUser.getCurrentUser()!=null){
            Intent intent_user = new Intent(ResultsActivity.this, UserProfileActivity.class);
            startActivity(intent_user);
        }
    }

    private void getUserImage(ParseUser user){
        if(GlobalVariable.UserPropic==null) {
            ParseQuery<ParseObject> query = ParseQuery.getQuery("UserPhoto");

            query.whereEqualTo("username", user.getEmail());
            query.getFirstInBackground(new GetCallback<ParseObject>() {
                @Override
                public void done(ParseObject userPhoto, ParseException e) {

                    //userphoto exists

                    if (userPhoto == null) {
                        Log.d("userphoto", "isnull");

                    } else {
                        ParseFile file = (ParseFile) userPhoto.get("profilePhoto");
                        file.getDataInBackground(new GetDataCallback() {
                            public void done(byte[] data, ParseException e) {
                                if (e == null) {
                                    // data has the bytes for the resume
                                    //data is the image in array byte
                                    //must change image on profile
                                    GlobalVariable.UserPropic = BitmapFactory.decodeByteArray(data, 0, data.length);
                                    Log.d("Userphoto", "downloaded");

                                    RoundedImageView mImg = (RoundedImageView) findViewById(R.id.user_propic);
                                    mImg.setImageBitmap(GlobalVariable.UserPropic);
                                    //iv.setImageBitmap(bitmap );


                                } else {
                                    // something went wrong
                                    Log.d("UserPhoto ", "problem download image");
                                }
                            }
                        });
                    }
                }
            });
        }

    }



}


