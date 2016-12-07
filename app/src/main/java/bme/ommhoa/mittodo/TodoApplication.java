package bme.ommhoa.mittodo;

import com.backendless.Backendless;
import com.orm.SugarApp;

public class TodoApplication extends SugarApp {

    @Override
    public void onCreate() {
        super.onCreate();
        String appVersion = "v1";
        Backendless.initApp( this, "C895FD81-6D98-6A4A-FF61-F6541ED2C000",
                "DE619820-48AE-417B-FF38-5F02CAF46B00", appVersion );
    }

}
