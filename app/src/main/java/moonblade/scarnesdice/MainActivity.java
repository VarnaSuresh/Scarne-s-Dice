package moonblade.scarnesdice;

import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.View;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import java.util.Random;
import java.util.logging.Handler;
import java.util.logging.LogRecord;

public class MainActivity extends AppCompatActivity {

    private boolean isPlayerTurn = true;
    private long playerScore = 0, computerScore = 0, turnScore = 0;
    private Button roll, hold, reset;
    private ImageView diceImage;
    private TextView scoreText;
    private int currentDiceValue = 1;
    private String SCOREPLAYER = "Your Score : ";
    private String SCORECOMPUTER = "Computer Score : ";
    private String SCORETURN = "Turn Score : ";
    int images[] = {R.drawable.dice1, R.drawable.dice2, R.drawable.dice3, R.drawable.dice4, R.drawable.dice5, R.drawable.dice6};
    android.os.Handler handler = new android.os.Handler();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        scoreText = (TextView) findViewById(R.id.scoreText);
        diceImage = (ImageView) findViewById(R.id.dicePicture);
        roll = (Button) findViewById(R.id.rollButton);
        hold = (Button) findViewById(R.id.holdButton);
        reset = (Button) findViewById(R.id.resetButton);
        updateUi();

        roll.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (isPlayerTurn)
                    roll();
            }
        });

        hold.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (isPlayerTurn)
                    hold();
            }
        });

        reset.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                reset();
            }
        });
        FloatingActionButton fab = (FloatingActionButton) findViewById(R.id.fab);
        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
               reset();
            }
        });


    }

    private void roll() {
        currentDiceValue = new Random().nextInt(6) + 1;
        if (currentDiceValue == 1) {
            turnScore = 0;
            hold();
        } else {
            turnScore += currentDiceValue;
        }
        updateUi();
    }

    private void hold() {
        if (isPlayerTurn)
            playerScore += turnScore;
        else
            computerScore += turnScore;
        turnScore = 0;
        currentDiceValue = 1;
        updateUi();
        isPlayerTurn = !isPlayerTurn;
        if(computerScore>100|| playerScore>100)
        {
            Toast.makeText(this,(computerScore>100?"Computer":"Player")+" won",Toast.LENGTH_SHORT).show();
            reset();
        }
        if (!isPlayerTurn) {
            new android.os.Handler().postDelayed(new Runnable() {
                @Override
                public void run() {
                    computerTurn();
                }
            },1000);
        }
    }

    private void reset() {
        playerScore = turnScore = computerScore = 0;
        isPlayerTurn = true;
        updateUi();
    }

    private void computerTurn() {
        if (!isPlayerTurn) {
            if (turnScore < 20) {
                roll();
                new android.os.Handler().postDelayed(new Runnable() {
                    @Override
                    public void run() {
                        computerTurn();
                    }
                },1000);
            } else
                hold();
        }
    }

    private void updateUi() {
        scoreText.setText(SCOREPLAYER + playerScore + "\n" + SCORECOMPUTER + computerScore + "\n" + SCORETURN + turnScore);
        diceImage.setImageResource(images[(int) currentDiceValue - 1]);
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_main, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        //noinspection SimplifiableIfStatement
        if (id == R.id.action_settings) {
            return true;
        }

        return super.onOptionsItemSelected(item);
    }
}
