package com.doctorfinderapp.doctorfinder.functions;

import android.graphics.Bitmap;
import com.doctorfinderapp.doctorfinder.Class.Doctor;
import com.parse.ParseObject;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

public class GlobalVariable {

    public static String idDoctors;
    public static int idUsers;
    public static List<String> permissions = Arrays.asList("email", "public_profile", "user_friends");
    public static int numdoctors;
    public static List<ParseObject> DOCTORS;
    public static boolean locationActive=false;
    public static Bitmap UserPropic=null;
    public static ArrayList<Doctor> recentDoctors = new ArrayList();
    public static boolean FLAGCARDVISIBLE = false;

   // public static byte[]

    /**non sapevo dove metterla scusate :D by Starnino*/
    /*public void selectDrawerItem(MenuItem menuItem){
        switch (menuItem.getItemId()) {

            case R.id.profile:
                break;
            case R.id.gestisci:
                break;
            case R.id.suggerisci:
                break;
            case R.id.about:
                String url_github = "https://github.com/Starnino/Doctor-Finder";
                Intent i_github = new Intent(Intent.ACTION_VIEW);
                i_github.setData(Uri.parse(url_github));
                startActivity(i_github);
                break;
            case R.id.support:
                break;
            case R.id.like:
                String url_face = "https://www.facebook.com/dcfind/?ref=bookmarks";
                Intent i_face = new Intent(Intent.ACTION_VIEW);
                i_face.setData(Uri.parse(url_face));
                startActivity(i_face);
                break;
            case R.id.settings:
                break;
            case R.id.logout:
                ParseUser.logOut();
                Log.d("R", "Logged out");
                Toast.makeText(getApplicationContext(),
                        "Logged out",
                        Toast.LENGTH_LONG).show();
                Intent intent = new Intent(ResultsActivity.this, SplashActivity.class);
                startActivity(intent);
                break;

        }*/

}