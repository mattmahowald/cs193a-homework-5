package cs193a.stanford.edu.hw5_snake;

import android.app.Activity;
import android.content.SharedPreferences;
import android.net.Uri;
import android.os.Bundle;
import android.util.Log;
import android.view.View;

import com.google.android.gms.appindexing.Action;
import com.google.android.gms.appindexing.AppIndex;
import com.google.android.gms.common.api.GoogleApiClient;

import stanford.androidlib.SimpleActivity;

public class SnakeGameActivity extends SimpleActivity {
    private GoogleApiClient client;
    int highScore;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_snake_game);

        SharedPreferences prefs = getPreferences(MODE_PRIVATE);
        highScore = prefs.getInt("a", -1);

        SnakeGameView game = find(R.id.snakegameview);
        game.startGame();
        client = new GoogleApiClient.Builder(this).addApi(AppIndex.API).build();

    }

    public void leftRotateClick(View view) {
        SnakeGameView game = findById(R.id.snakegameview);
        game.turnLeft();
    }



    public void rightRotateClick(View view) {
        SnakeGameView game = findById(R.id.snakegameview);
        game.turnRight();
    }

    @Override
    public void onStart() {
        super.onStart();


        client.connect();
        Action viewAction = Action.newAction(
                Action.TYPE_VIEW,
                "SnakeGame Page",
                Uri.parse("http://host/path"),
                Uri.parse("android-app://cs193a.stanford.edu.hw5_snake/http/host/path")
        );
        AppIndex.AppIndexApi.start(client, viewAction);
    }

    @Override
    public void onStop() {
        super.onStop();



        Action viewAction = Action.newAction(
                Action.TYPE_VIEW,
                "SnakeGame Page",
                Uri.parse("http://host/path"),
                Uri.parse("android-app://cs193a.stanford.edu.hw5_snake/http/host/path")
        );
        AppIndex.AppIndexApi.end(client, viewAction);
        client.disconnect();
    }

    @Override
    public void onPause() {
        super.onPause();
        SnakeGameView game = findById(R.id.snakegameview);
        int points = game.points;
        SharedPreferences prefs = getSharedPreferences("myprefs", Activity.MODE_PRIVATE);
        SharedPreferences.Editor prefsEditor = prefs.edit();
        if (points > highScore) {
            Log.i("testing", "prefs were changed");
            prefsEditor.putInt("a", points);
            prefsEditor.commit();
        }

        Log.i("testing", Integer.toString(points));
        Log.i("testing", "onPause was called!");
    }

    public void startClick(View view) {
        SnakeGameView game = findById(R.id.snakegameview);
        game.startNewGame();
    }
}
