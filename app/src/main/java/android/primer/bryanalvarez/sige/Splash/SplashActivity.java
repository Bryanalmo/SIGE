package android.primer.bryanalvarez.sige.Splash;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.primer.bryanalvarez.sige.Activities.LoginActivity;
import android.primer.bryanalvarez.sige.Activities.MainActivity;
import android.primer.bryanalvarez.sige.Util.Util;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.primer.bryanalvarez.sige.R;
import android.text.TextUtils;

public class SplashActivity extends AppCompatActivity {

    private SharedPreferences prefs;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        prefs = getSharedPreferences("Preferences", Context.MODE_PRIVATE);

        prefs = getSharedPreferences("Preferences", Context.MODE_PRIVATE);

        Intent intentLogin = new Intent(this, LoginActivity.class);
        Intent intentMain = new Intent(this, MainActivity.class);

        if(!TextUtils.isEmpty(Util.getuserUserPrefs(prefs)) && !TextUtils.isEmpty(Util.getuserPasswordPrefs(prefs)) ){
            Util.setUsuario(Util.getuserUserPrefs(prefs));
            startActivity(intentMain);
        }else{
            startActivity(intentLogin);
        }
        finish();
    }
}
