package online.jne.com.jneapps.helper;

import android.content.Context;
import android.content.SharedPreferences;
import android.content.SharedPreferences.Editor;
import android.util.Log;
import online.jne.com.jneapps.model.User;


public class SessionManager {
    // LogCat tag
    private static String TAG = SessionManager.class.getSimpleName();

    // Shared Preferences
    SharedPreferences pref;

    Editor editor;
    Context _context;

    // Shared pref mode
    int PRIVATE_MODE = 0;

    // Shared preferences file name
    private static final String PREF_NAME = "RockerLogin";

    private static final String KEY_IS_LOGGEDIN = "isLoggedIn";
    private static final String KEY_NAME = "nameUser";
    private static final String KEY_EMAIL = "emailUser";
    private static final String KEY_ID = "idUser";


    public SessionManager(Context context) {
        this._context = context;
        pref = _context.getSharedPreferences(PREF_NAME, PRIVATE_MODE);
        editor = pref.edit();
    }

    public String getId(){
        return pref.getString(KEY_ID,"null");
    }

    public String getImage(){
        Log.d("tes","get image from " + getId());
        return pref.getString(getId(),"null");}

    public void setImage(String image){
        editor.putString(getId(), image);
        Log.d("tes","set image from " + getId());
        Log.d("Tes","imageset" + image);
        editor.commit();
    }

    public void setLogin(boolean isLoggedIn,String name,String hp,String image,String id) {

        editor.putBoolean(KEY_IS_LOGGEDIN, isLoggedIn);

        saveData(name,hp,id);
        // commit changes
        setImage(image);
        editor.commit();


        Log.d(TAG, "User login session modified!");
    }

    public void setLogin(boolean isLoggedIn) {

        editor.putBoolean(KEY_IS_LOGGEDIN, isLoggedIn);

        editor.commit();

        Log.d(TAG, "User login session modified!");
    }


    public boolean isLoggedIn() {
        return pref.getBoolean(KEY_IS_LOGGEDIN, false);
    }



    private void saveData(String name,String email,String id){


        editor.putString(KEY_NAME, name);
        editor.putString(KEY_EMAIL, email);
        editor.putString(KEY_ID,id);
        editor.commit();

    }

    public User getData(){
        User temp = new User();

        temp.setName(pref.getString(KEY_NAME,"Unknown"));
        temp.setEmail(pref.getString(KEY_EMAIL,""));
        temp.setIdCustomer(pref.getString(KEY_ID,""));

        return temp;
    }

}