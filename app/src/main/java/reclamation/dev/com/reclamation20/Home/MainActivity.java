package reclamation.dev.com.reclamation20.Home;

import android.content.Intent;
import android.content.SharedPreferences;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;

import reclamation.dev.com.reclamation20.Login.LoginActivity;
import reclamation.dev.com.reclamation20.R;

import static reclamation.dev.com.reclamation20.Login.LoginActivity.PREFS_NAME;

public class MainActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        final SharedPreferences settings = getSharedPreferences(PREFS_NAME, 0);
        if (!(settings.getString("logged", "").toString().equals("logged"))) {
            Intent intent = new Intent(MainActivity.this, LoginActivity.class);
            startActivity(intent);

        }
    }
}
