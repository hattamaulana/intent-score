package id.putraprima.skorbola;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.graphics.Bitmap;
import android.net.Uri;
import android.os.Bundle;
import android.provider.MediaStore;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import java.io.IOException;

public class MatchActivity extends AppCompatActivity {

    public static String EXTRA_DATA = "EXTRA_DATA";

    private int homeScore = 0;
    private int awayScore = 0;

    private Match match;
    private TextView tvHomeTeam;
    private TextView tvAwayTeam;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_match);

        Intent extras = getIntent();
        match = extras.getParcelableExtra(EXTRA_DATA);

        TextView homeName = findViewById(R.id.txt_home);
        TextView awayName = findViewById(R.id.txt_away);
        ImageView homeLogo = findViewById(R.id.home_logo);
        ImageView awayLogo = findViewById(R.id.away_logo);
        tvHomeTeam = findViewById(R.id.score_home);
        tvAwayTeam = findViewById(R.id.score_away);

        // 1.Menampilkan detail match sesuai data dari main activity
        homeName.setText(match.getHomeTeam());
        awayName.setText(match.getAwayTeam());
        if (match.getHomeLogo() != null) setImage(homeLogo, match.getHomeLogo());
        if (match.getAwayLogo() != null) setImage(awayLogo, match.getAwayLogo());
    }

    private void setImage(ImageView view, Uri uri) {
        try {
            Bitmap bitmap = MediaStore.Images.Media.getBitmap(this.getContentResolver(), uri);
            view.setImageBitmap(bitmap);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public void homeScoreHandler(View view) {
        homeScore++;
        tvHomeTeam.setText(String.valueOf(homeScore));
    }

    public void awayScoreHandler(View view) {
        awayScore++;
        tvAwayTeam.setText(String.valueOf(awayScore));
    }

    public void CheckResult(View view) {
        String result = (homeScore == awayScore) ? " Imbang ":
                (homeScore > awayScore) ? match.getHomeTeam()+" Menang" : match.getAwayTeam() + " Menang";

        Intent intent = new Intent(this, ResultActivity.class);
        intent.putExtra(ResultActivity.EXTRA_RESULT_MATCH, result);
        startActivity(intent);
    }
}
