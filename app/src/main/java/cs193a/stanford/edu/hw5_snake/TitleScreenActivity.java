package cs193a.stanford.edu.hw5_snake;

import android.app.Activity;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.TextView;
import android.widget.Toast;

import stanford.androidlib.SimpleActivity;

public class TitleScreenActivity extends SimpleActivity {
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_title_screen);
        Log.i("testing", "onStart was called!");
    }

    @Override
    public void onResume() {
        super.onResume();

        SharedPreferences prefs = getSharedPreferences("myprefs", Activity.MODE_PRIVATE);
        int highScore = prefs.getInt("a", -1);

        Log.v("the score", Integer.toString(highScore));
        TextView scoreText = (TextView) findViewById(R.id.highscore);
        scoreText.setText("High Score: " + highScore);

        Log.i("testing", "onResume was called!");
    }

    public void playClick(View view) {
        startActivity(SnakeGameActivity.class);
    }

    public void exitClick(View view) {
        finish();
    }
}
