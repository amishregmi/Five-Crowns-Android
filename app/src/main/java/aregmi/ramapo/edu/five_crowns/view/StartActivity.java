package aregmi.ramapo.edu.five_crowns.view;

import android.content.DialogInterface;
import android.content.Intent;
import android.os.Environment;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.ListView;

import java.io.File;
import java.util.ArrayList;
import java.util.List;
import java.util.Random;

import aregmi.ramapo.edu.five_crowns.R;

public class StartActivity extends AppCompatActivity {

    private Button start_new_game_button;
    private Button load_game_button;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_start);
        start_new_game_button = findViewById(R.id.new_game_button);
        load_game_button = findViewById(R.id.load_game_button);

        start_new_game_button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                AlertDialog.Builder coin_toss_popup = new AlertDialog.Builder(StartActivity.this);
                coin_toss_popup.setTitle("COIN TOSS");
                String[] options = {"Heads", "Tails"};

                coin_toss_popup.setItems(options, new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {

                        ListView cointoss_options = ((AlertDialog)dialog).getListView();
                        String user_cointoss_selection = (String) cointoss_options.getAdapter().getItem(which);
                        String toss_winner = chooseTossWinner(user_cointoss_selection);
                        Intent intent = new Intent(StartActivity.this, MainGameActivity.class);
                        intent.putExtra("round", 1);
                        intent.putExtra("next_player", toss_winner);
                        intent.putExtra("read_from_file", false);
                        intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK);
                        startActivity(intent);
                    }
                });

                coin_toss_popup.show();
            }
        });

        load_game_button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                AlertDialog.Builder load_file = new AlertDialog.Builder(StartActivity.this);
                load_file.setTitle("LOAD A GAME");
                String saved_files_dir = Environment.getExternalStorageDirectory().getAbsolutePath()+"/saved_games";
                //System.out.println("saved_files_dir is: "+ saved_files_dir);
                final File[] all_files = new File(saved_files_dir).listFiles();
                List<String> text_files = new ArrayList<String>();
                //System.out.println("ALL_FILES LEN IS: "+ all_files.length);

                for(File onefile: all_files){
                    String file_name = onefile.getName();
                    if (file_name.endsWith(".txt")){
                        text_files.add(file_name);
                    }
                }

                int size = text_files.size();
                String[] options = new String[size];
                options = text_files.toArray(options);

                load_file.setItems(options, new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        ListView options = ((AlertDialog)dialog).getListView();
                        String selected_file = (String) options.getAdapter().getItem(which);
                        Intent intent = new Intent(StartActivity.this, MainGameActivity.class);
                        intent.putExtra("read_from_file", true);
                        intent.putExtra("file_name", selected_file);
                        intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK);
                        startActivity(intent);

                    }
                });

                load_file.show();
            }
        });

    }

    private String chooseTossWinner(String user_cointoss_selection) {
        Random random = new Random();
        int rand = random.nextInt(2);

        System.out.println("The random number generated is: "+ rand);
        String toss_val = "";
        String winner = "";

        if (rand == 0){
            toss_val = "Heads";
        }
        else {
            toss_val = "Tails";
        }

        if (user_cointoss_selection.equals(toss_val)){
            winner = "Human";
        }
        else {
            winner = "Computer";
        }

        //TODO -> MAKE THIS RANDOM
        return winner;
    }

}
