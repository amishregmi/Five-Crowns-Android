package aregmi.ramapo.edu.five_crowns.view;

import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.graphics.drawable.Drawable;
import android.os.Environment;
import android.os.SystemClock;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import java.io.File;
import java.io.FileOutputStream;
import java.io.PrintWriter;
import java.sql.CallableStatement;
import java.util.ArrayList;
import java.util.List;
import java.util.Vector;

import aregmi.ramapo.edu.five_crowns.R;
import aregmi.ramapo.edu.five_crowns.model.players.Computer;
import aregmi.ramapo.edu.five_crowns.model.players.Human;
import aregmi.ramapo.edu.five_crowns.model.setup.Card;
import aregmi.ramapo.edu.five_crowns.model.setup.Deck;
import aregmi.ramapo.edu.five_crowns.model.setup.Game;
import aregmi.ramapo.edu.five_crowns.model.setup.Round;


public class MainGameActivity extends AppCompatActivity implements View.OnClickListener{

    private Round round;
    private Game game;
    private TextView human_points_textview;
    private TextView computer_points_textview;
    private Button get_help_button;
    private Button assemble_cards_button;
    private Button log_button;
    private boolean verify_goout_human;
    private boolean verify_goout_computer;
    private boolean human_picked_card;
    private String next_player;
    private String file_name;
    private int round_num;
    private boolean read_from_file;
    private ImageView imageview;
    private String help_reason = "";
    private TextView computer_move_explanation;
    private Button save_game_button;
    private String all_log_details = "";
    //private Human human = new Human();
    //private Computer computer = new Computer();
    private String log_dir;
    private String log_file_name;
    private File file;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main_game);
        game = new Game();

        //NEW GAME INTENTS
        read_from_file = getIntent().getBooleanExtra("read_from_file", false);

        round_num = getIntent().getIntExtra("round",1);
        next_player = getIntent().getStringExtra("next_player");


        //FINDING VIEWS
        human_points_textview = findViewById(R.id.human_points_textview);
        computer_points_textview = findViewById(R.id.computer_points_textview);
        human_points_textview.setText("ROUND: " + round_num +"  HUMAN POINTS: " + game.getHumanTotalPoints());
        computer_points_textview.setText("COMPUTER POINTS: "+ game.getComputerTotalPoints());
        get_help_button = findViewById(R.id.get_help_button);
        save_game_button = findViewById(R.id.save_game_button);
        //assemble_cards_button = findViewById(R.id.assemble_cards_button);
        log_button = findViewById(R.id.log_button);
        computer_move_explanation = findViewById(R.id.computer_move_explanation);


//        FileOutputStream write_stream = new FileOutputStream(file);


        get_help_button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                if (human_picked_card == false){
                    help_reason = round.getHumanPlayer().pickCardHelp();
                }

                else{
                    help_reason = round.getHumanPlayer().dropCardHelp();
                }

                Toast.makeText(getApplicationContext(), help_reason, Toast.LENGTH_LONG ).show();
            }
        });

        save_game_button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                final EditText file_name = new EditText(MainGameActivity.this);
                AlertDialog.Builder dialog = new AlertDialog.Builder(MainGameActivity.this);
                dialog.setTitle("Enter file name")
                        .setView(file_name)
                        .setPositiveButton("Save", new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialog, int which) {
                                String file = String.valueOf(file_name.getText());
                                System.out.println("THE FILENAME IS: "+ file);
                                saveGame(MainGameActivity.this, file);
                            }
                        })
                        .setNegativeButton("Cancel", null)
                        .create();

                dialog.show();
            }
        });

        log_button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                log_dir = Environment.getExternalStorageDirectory().getAbsolutePath()+"/saved_games/log";
                log_file_name = "current_log.txt";
                file = new File(log_dir, log_file_name);
                try{
                    PrintWriter writer = new PrintWriter(file);
                    writer.print("");
                    writer.close();

                }catch (Exception ex){
                    System.out.println(ex);
                }
                saveGameLog(MainGameActivity.this);
                Intent intent = new Intent(MainGameActivity.this, GameLog.class);
                startActivity(intent);
            }
        });



        if (read_from_file == false){

            setUpRound();
        }

        else{
            file_name = getIntent().getStringExtra("file_name");
            //System.out.println("FILE NAME RECEIVED IN MAINGAMEACTIVITY IS: "+ file_name);
            setUpRound();

        }
    }

    private void setUpRound() {


        if (!read_from_file){
            round = game.startRound(round_num);
        }
        else{
            round = game.setRoundFromFile(file_name);
        }
        Human human = round.getHumanPlayer();
        Computer computer = round.getComputerPlayer();
        verify_goout_human = false;
        verify_goout_computer = false;
        human_picked_card = false;

        if (!read_from_file){
            round.setPlayerList(next_player);
            round.dealForRound();
        }

        if (read_from_file){
            next_player = round.getNextPlayer();
            round_num = round.getRoundNum();
            human_points_textview.setText("ROUND: " + round_num+ "  HUMAN POINTS: " + game.getHumanTotalPoints());
            computer_points_textview.setText("COMPUTER POINTS: "+ game.getComputerTotalPoints());

        }

        human_points_textview.setText("ROUND: " + round_num +"  HUMAN POINTS: " + game.getHumanTotalPoints());
        computer_points_textview.setText("COMPUTER POINTS: "+ game.getComputerTotalPoints());

        all_log_details+= "\n\n--------------------------------------\n";
        all_log_details+="HUMAN POINTS: "+ game.getHumanTotalPoints()+"\n";
        all_log_details+="COMPUTER POINTS: "+ game.getComputerTotalPoints()+"\n";
        all_log_details+="------------------------------------------\n\n";

        getCurrentRoundDetails();



        if (next_player.equals("Computer")) {
            makeComputerMove();
            //makeHumanMove();
        }

        else if (next_player.equals("Human")) {
            makeHumanMove();
            //makeComputerMove();
        }

        if (verify_goout_computer && !verify_goout_human){
            //makeHumanMove();
            game.addHumanTotalPoints(round.getHumanPlayer().getHandScore());

        }

        else if (!verify_goout_computer && verify_goout_human){
            //makeComputerMove();
            game.addComputerTotalPoints(round.getComputerPlayer().getHandScore());

        }

        getCurrentRoundDetails();
    }

    private void disableHumanButtons() {
        get_help_button.setEnabled(false);
//        assemble_cards_button.setEnabled(false);
        save_game_button.setEnabled(false);
    }

    private void enableHumanButtons(){
        get_help_button.setEnabled(true);
        //assemble_cards_button.setEnabled(true);
        //String reason;
        save_game_button.setEnabled(true);

    }

    private void makeComputerMove(){
        //all_log_details += writeLogDetails();
        all_log_details += writeLogDetails();
        getCurrentRoundDetails();
        disableHumanButtons();

        System.out.println("INSIDE MAKECOMPUTERMOVE");

        if (!verify_goout_computer){
            round.getComputerPlayer().pickCard();
            Toast.makeText(getApplicationContext(), round.getComputerPlayer().getComputerMove(), Toast.LENGTH_LONG ).show();
            computer_move_explanation.setText(round.getComputerPlayer().getComputerMove());
        }

        all_log_details += round.getComputerPlayer().getComputerMove()+"\n\n";
        all_log_details += "After dropping card, hand is: "+ round.getComputerPlayer().getPlayerHandStr()+"\n\n";

        //getCurrentRoundDetails();
        next_player = "Human";
        human_picked_card = false;
        getCurrentRoundDetails();


        if (round.getComputerPlayer().goOut()){
            verify_goout_computer = true;
            Toast.makeText(getApplicationContext(), "COMPUTER WENT OUT!", Toast.LENGTH_LONG).show();
            computer_move_explanation.setText(round.getComputerPlayer().getComputerMove()+" and went out");
        }

        all_log_details+=round.getComputerPlayer().printAvailableBooksandRuns()+"\n";
        all_log_details+= "THE BEST COMBINATION OF BOOKS AND RUNS IS: \n";
        all_log_details+= round.getComputerPlayer().getMinBranch().toString()+"\n\n";


        System.out.println("VERIFY_GOOUT_HUMAN IS: "+ verify_goout_human);

        if (!verify_goout_human){
            System.out.println("INSIDE MAKEHUMANMOVE");
            makeHumanMove();
        }

        else if (verify_goout_human || verify_goout_computer){
            game.addComputerTotalPoints(round.getComputerPlayer().getHandScore());
            game.addHumanTotalPoints(round.getHumanPlayer().getHandScore());
            human_points_textview.setText("ROUND: " + round_num+ " HUMAN POINTS: " + game.getHumanTotalPoints());
            computer_points_textview.setText("COMPUTER POINTS: "+ game.getComputerTotalPoints());
            round_num = round_num + 1;

            if (round_num > 11){
                Intent intent = new Intent(MainGameActivity.this, GameCompleted.class);
                intent.putExtra("total_human_points", Integer.toString( game.getHumanTotalPoints()));
                intent.putExtra("total_computer_points", Integer.toString(game.getComputerTotalPoints()));
                startActivity(intent);
            }

            else{
                SystemClock.sleep(200);
                read_from_file = false;
                next_player = "Human";
                setUpRound();
            }
        }

    }

    private void makeHumanMove() {
        //all_log_details+= writeLogDetails();
        enableHumanButtons();
        //TODO -> Disable clickable from human hand. Allow clickable from top of draw pile and discard pile
        getCurrentRoundDetails();
        all_log_details += writeLogDetails();
        //makeComputerMove();

    }


    //TODO -> CHANGE THIS
    @Override
    public void onClick(View view){
        Drawable select = getResources().getDrawable(R.drawable.selected);
        view.setBackground(select);
        String card_selected = (String)view.getTag();
        Card controller_card_selected = round.convertToControllerCard(card_selected);
        System.out.println("CARD SELECTED IS: "+ card_selected);
        String controller_card_selected_str = controller_card_selected.cardToString();
        System.out.println("String controller card selected is: "+ controller_card_selected_str);
        //System.out.println("CONTROLLER CONVERSION IS:  " + controller_card_selected.cardToString());


        if (controller_card_selected_str.equals(Deck.getTopDrawCard().cardToString())){
            //System.out.println("TOP DRAW CARD");
            all_log_details += "Human picked "+ controller_card_selected_str+"\n";
            round.getHumanPlayer().addCardToHand(Deck.takeTopDrawCard());
            human_picked_card = true;
            getCurrentRoundDetails();
        }

        //TODO -> Some error here. Attempt to invoke virtual method cardToString() on null object reference.

        else if(!Deck.getCurrentDiscardPile().isEmpty() && controller_card_selected_str.equals(Deck.getTopDiscardCard().cardToString())){
            all_log_details += "Human picked "+ controller_card_selected_str+"\n";
            round.getHumanPlayer().addCardToHand(Deck.takeTopDiscardCard());
            human_picked_card = true;
            getCurrentRoundDetails();
        }


        else{
            //TODO -> DROP CARD
            //human_picked_card = false;
            System.out.println("INSIDE DROP CARD");
            save_game_button.setEnabled(false);

            getCurrentRoundDetails();
            round.getHumanPlayer().dropCard(controller_card_selected_str);
            all_log_details += "Human dropped "+ controller_card_selected_str+"\n";
            all_log_details+= "After dropping card, human hand is: "+ round.getHumanPlayer().getPlayerHandStr()+"\n\n";


            next_player = "Computer";
            getCurrentRoundDetails();

            if (round.getHumanPlayer().goOut()){
                verify_goout_human = true;
                Toast.makeText(getApplicationContext(), "HUMAN WENT OUT!", Toast.LENGTH_LONG ).show();
            }

            all_log_details+=round.getHumanPlayer().printAvailableBooksandRuns()+"\n";
            all_log_details+= "THE BEST COMBINATION OF BOOKS AND RUNS IS: \n";
            all_log_details+= round.getHumanPlayer().getMinBranch().toString()+"\n\n";

            if (!verify_goout_computer){
                System.out.println("SHOULD NOT BE INSIDE THIS");
                makeComputerMove();
            }

            else{
                System.out.println("STARTING NEW ROUND FROM HUMAN CLICK");
                game.addHumanTotalPoints(round.getHumanPlayer().getHandScore());
                human_points_textview.setText("ROUND: "+round_num+" HUMAN POINTS: " + game.getHumanTotalPoints());
                computer_points_textview.setText("COMPUTER POINTS: "+ game.getComputerTotalPoints());
                round_num++;

                if (round_num > 11){
                    Intent intent = new Intent(MainGameActivity.this, GameCompleted.class);
                    intent.putExtra("total_human_points", Integer.toString( game.getHumanTotalPoints()));
                    intent.putExtra("total_computer_points", Integer.toString(game.getComputerTotalPoints()));
                    startActivity(intent);
                }

                else{
                    SystemClock.sleep(200);

                    read_from_file = false;
                    next_player = "Computer";
                    setUpRound();
                }
            }
        }

    }


    private void getCurrentRoundDetails() {

        //List<String> draw_pile = convertToDrawableString(round.getDrawPile());
        List<String> draw_pile = round.convertToDrawableString(round.getDrawPile());
        List<String> discard_pile = round.convertToDrawableString(round.getDiscardPile());


        List<String> human_hand = round.convertToDrawableString(round.getHumanHand());
        List<String> computer_hand = round.convertToDrawableString(round.getComputerHand());

        LinearLayout linearLayout1 = (LinearLayout) findViewById(R.id.computer_hand_linearlayout);
        linearLayout1.removeAllViews();
        addCardToView(linearLayout1, computer_hand, "Computer");

        linearLayout1 = (LinearLayout) findViewById(R.id.discard_pile_linearlayout);
        linearLayout1.removeAllViews();
        addCardToView(linearLayout1, discard_pile, "DiscardPile");

        linearLayout1 = (LinearLayout) findViewById(R.id.draw_pile_linearlayout);
        linearLayout1.removeAllViews();
        addCardToView(linearLayout1, draw_pile, "DrawPile");

        linearLayout1 = (LinearLayout) findViewById(R.id.human_hand_linearlayout);
        linearLayout1.removeAllViews();
        addCardToView(linearLayout1, human_hand, "Human");


    }

    private void addCardToView(LinearLayout linearLayout, List<String> cards_to_display, String card_belonging_to) {

        int index_pos = 0;

        for (String onecard : cards_to_display){
            imageview = new ImageView(this);
            LinearLayout.LayoutParams layout_params = new LinearLayout.LayoutParams(210, 250);
            layout_params.setMargins(10, 10, 10, 10);
            imageview.setLayoutParams(layout_params);

            Context context = linearLayout.getContext();
            int id = context.getResources().getIdentifier(onecard, "drawable", context.getPackageName());
            //System.out.println("ID IS: "+ id);
            imageview.setImageResource(id);

            imageview.setTag(onecard);
            imageview.setPadding(5, 5,5,5);
            imageview.setId(id);

            if (card_belonging_to.equals("Human") && human_picked_card == true && next_player.equals("Human")){
                imageview.setClickable(true);
                imageview.setOnClickListener(this);
            }

            else if (card_belonging_to.equals("DrawPile") && index_pos == 0 && human_picked_card == false && next_player.equals("Human")){
                //System.out.println("INSIDE HERE");
                imageview.setClickable(true);
                imageview.setOnClickListener(this);
            }

            else if (card_belonging_to.equals("DiscardPile") && index_pos == 0 && human_picked_card == false && next_player.equals("Human")){
                //System.out.println("INSIDE HERE 2");
                imageview.setClickable(true);
                imageview.setOnClickListener(this);
            }
//            else if (card_belonging_to.equals(""))

            linearLayout.addView(imageview);

            index_pos++;
        }

    }

    private void saveGameLog(Context context){
        try{
            FileOutputStream write_stream = new FileOutputStream(file, true);
            write_stream.write(all_log_details.getBytes());
            write_stream.close();

        } catch (Exception ex){
            ex.printStackTrace();
        }

    }

    private String writeLogDetails() {
        StringBuilder log_details = new StringBuilder();
        log_details.append("***--------------------***\n\n");
        log_details.append("DRAW PILE: \n");
        log_details.append(Deck.getCurrentDrawPile()).append("\n\n");
        log_details.append("DISCARD PILE: \n");
        log_details.append(Deck.getCurrentDiscardPile()).append("\n\n");
        log_details.append("ROUND NUMBER: ").append(round_num).append("\n\n");
        log_details.append("Playing: ").append(next_player).append("\n");
        //log_details.append(round.ge)
        if (next_player.equals("Human")){
            log_details.append("HUMAN HAND: \t");
            log_details.append(round.getHumanPlayer().getPlayerHandStr()).append("\n");
        }
        else{
            log_details.append("COMPUTER HAND: \t");
            log_details.append(round.getComputerPlayer().getPlayerHandStr()).append("\n");
        }
        //log_details.append(Deck.get)

        String log_detailss = log_details.toString();
        return log_detailss;
    }

    private void saveGame(Context context, String file_name){
        String saved_files_dir = Environment.getExternalStorageDirectory().getAbsolutePath()+"/saved_games";

        file_name+= ".txt";
        System.out.println("FILE NAME IS: "+ file_name);
        //File directory = context.getFilesDir();
        //new File(saved_files_dir).listFiles();
        File file = new File(saved_files_dir, file_name);
        try{
            FileOutputStream write_stream = new FileOutputStream(file);
            write_stream.write(writeGameDetails().getBytes());
            write_stream.close();
        } catch (Exception ex){
            ex.printStackTrace();
        }

        System.exit(1);

    }

    private String writeGameDetails() {
        StringBuilder save_details = new StringBuilder();
        save_details.append("Round: ")
                .append(round.getRoundNum()).append("\n").append("\n");
        save_details.append("Computer: \n");
        save_details.append("\tScore: ")
                .append(game.getComputerTotalPoints()).append("\n");
        save_details.append("\tHand: ")
                .append(round.getComputerHand()).append("\n").append("\n");

        save_details.append("Human: \n");
        save_details.append("\tScore: ")
                .append(game.getHumanTotalPoints()).append("\n");
        save_details.append("\tHand: ")
                .append(round.getHumanHand()).append("\n").append("\n");
        save_details.append("Draw Pile: ")
                .append(Deck.getCurrentDrawPile()).append("\n").append("\n");
        save_details.append("Discard Pile: ")
                .append(Deck.getCurrentDiscardPile()).append("\n").append("\n");
        save_details.append("Next Player: ")
                .append(next_player).append("\n");

        String save_file_string = save_details.toString();
        return save_file_string;
    }


}
