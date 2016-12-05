package bme.ommhoa.mittodo.helper;


import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.support.v7.app.AppCompatActivity;
import android.widget.Toast;

public abstract class NetworkRelatedHelper {

    public static boolean isNetworkOnline(ConnectivityManager cm) {
        try {
            NetworkInfo activeNetwork = cm.getActiveNetworkInfo();
            return activeNetwork != null &&
                    activeNetwork.isConnected();
        } catch (Exception e) {
            e.printStackTrace();
            return false;
        }
    }

    public static void notifyUserNoNetwork(AppCompatActivity activity) {
        Toast.makeText(activity, "No network connection", Toast.LENGTH_SHORT).show();
    }
}
