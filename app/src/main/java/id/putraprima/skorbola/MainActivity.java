package id.putraprima.skorbola;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.graphics.Bitmap;
import android.net.Uri;
import android.os.Bundle;
import android.provider.MediaStore;
import android.text.TextUtils;
import android.view.View;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.Toast;

import java.io.IOException;

public class MainActivity extends AppCompatActivity implements View.OnClickListener {

    private static final int REQUEST_CODE_HOME_TEAM = 1;
    private static final int REQUEST_CODE_AWAY_TEAM = 2;

    private EditText edtHomeTeam;
    private EditText edtAwayTeam;
    private ImageView ivHomeLogo;
    private ImageView ivAwayLogo;

    private Uri homeLogo;
    private Uri awayLogo;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        // Fitur Main Activity
        // 1. Validasi Input Home Team
        edtHomeTeam = findViewById(R.id.home_team);
        // 2. Validasi Input Away Team
        edtAwayTeam = findViewById(R.id.away_team);
        // 3. Ganti Logo Home Team
        ivHomeLogo = findViewById(R.id.home_logo);
        ivHomeLogo.setOnClickListener(this);
        // 4. Ganti Logo Away Team
        ivAwayLogo = findViewById(R.id.away_logo);
        ivAwayLogo.setOnClickListener(this);
        // 5. Next Button Pindah Ke MatchActivity
        findViewById(R.id.btn_team).setOnClickListener(this);
    }

    @Override
    public void onClick(View v) {
        Intent intent = new Intent(Intent.ACTION_PICK,
                MediaStore.Images.Media.EXTERNAL_CONTENT_URI);

        switch (v.getId()) {
            case R.id.home_logo:
                startActivityForResult(intent, REQUEST_CODE_HOME_TEAM);
                break;

            case R.id.away_logo:
                startActivityForResult(intent, REQUEST_CODE_AWAY_TEAM);
                break;

            case R.id.btn_team:
                String homeTeam = edtHomeTeam.getText().toString();
                String awayTeam = edtAwayTeam.getText().toString();
                if (validate(homeTeam, "Home")) return;
                if (validate(awayTeam, "Away")) return;

                Match match = new Match(homeTeam, awayTeam, homeLogo, awayLogo);
                Intent intentData = new Intent(this, MatchActivity.class);
                intentData.putExtra(MatchActivity.EXTRA_DATA, match);
                startActivity(intentData);
                break;
        }
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (resultCode == RESULT_CANCELED) return;

        switch (requestCode) {
            case REQUEST_CODE_HOME_TEAM:
                homeLogo = data.getData();
                setImage(ivHomeLogo, homeLogo);
                break;

            case REQUEST_CODE_AWAY_TEAM:
                awayLogo = data.getData();
                setImage(ivAwayLogo, awayLogo);
                break;
        }
    }

    private Boolean validate(String team, String label) {
        Boolean check = TextUtils.isEmpty(team);
        if (!check) return false;

        Toast.makeText(this, "Lengkapi Nama Team "+ label, Toast.LENGTH_SHORT).show();
        return true;
    }

    private void setImage(ImageView view, Uri uri) {
        try {
            Bitmap bitmap = MediaStore.Images.Media.getBitmap(this.getContentResolver(), uri);
            view.setImageBitmap(bitmap);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}
