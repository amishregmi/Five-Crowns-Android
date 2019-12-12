/************************************************************
 * Name: Amish Regmi                                        *
 * Project: Project 3, Five Crowns Android                  *
 * Class: OPL Fall 19                                       *
 * Date: 12/11/2019                                         *
 ************************************************************/

package aregmi.ramapo.edu.five_crowns.view;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

import aregmi.ramapo.edu.five_crowns.R;

public class GameCompleted extends AppCompatActivity {

    private TextView total_human_points;
    private TextView total_computer_points;
    private Button main_menu_button;
    private  TextView display_winner;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_game_completed);

        total_computer_points = findViewById(R.id.total_computer_points);
        total_human_points = findViewById(R.id.total_human_points);
        display_winner = findViewById(R.id.display_winner);
        main_menu_button = findViewById(R.id.main_menu_button);

        String human_total = getIntent().getStringExtra("total_human_points");
        String computer_total = getIntent().getStringExtra("total_computer_points");

        total_human_points.setText("Human total points: "+ human_total);
        total_computer_points.setText("Computer total points: "+ computer_total);

        int human_points = Integer.parseInt(human_total);
        int computer_points = Integer.parseInt(computer_total);

        if (human_points > computer_points){
            display_winner.setText("Computer won the game ! ");
        }

        else if (computer_points > human_points){
            display_winner.setText("Human won the game ! ");
        }

        else {
            display_winner.setText("The game ended in a tie! ");
        }

        main_menu_button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(GameCompleted.this, StartActivity.class);
                startActivity(intent);
            }
        });

    }
}
