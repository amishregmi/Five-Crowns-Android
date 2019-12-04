package aregmi.ramapo.edu.five_crowns.view;

import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.graphics.drawable.Drawable;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

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
    //private Human human = new Human();
    //private Computer computer = new Computer();

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
        //assemble_cards_button = findViewById(R.id.assemble_cards_button);
        log_button = findViewById(R.id.log_button);
        computer_move_explanation = findViewById(R.id.computer_move_explanation);

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
    }

    private void enableHumanButtons(){
        get_help_button.setEnabled(true);
        //assemble_cards_button.setEnabled(true);
        //String reason;

    }

    private void makeComputerMove(){
        getCurrentRoundDetails();
        disableHumanButtons();

        System.out.println("INSIDE MAKECOMPUTERMOVE");

        if (!verify_goout_computer){
            round.getComputerPlayer().pickCard();
            Toast.makeText(getApplicationContext(), round.getComputerPlayer().getComputerMove(), Toast.LENGTH_LONG ).show();
            computer_move_explanation.setText(round.getComputerPlayer().getComputerMove());

        }
        //getCurrentRoundDetails();
        next_player = "Human";
        human_picked_card = false;
        getCurrentRoundDetails();


        if (round.getComputerPlayer().goOut()){
            verify_goout_computer = true;
            Toast.makeText(getApplicationContext(), "COMPUTER WENT OUT!", Toast.LENGTH_LONG).show();
        }

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
                read_from_file = false;
                next_player = "Human";
                setUpRound();
            }
        }

    }

    private void makeHumanMove() {
        enableHumanButtons();
        //TODO -> Disable clickable from human hand. Allow clickable from top of draw pile and discard pile
        getCurrentRoundDetails();

        //makeComputerMove();

    }


    //TODO -> CHANGE THIS
    @Override
    public void onClick(View view){
        Drawable select = getResources().getDrawable(R.drawable.selected);
        view.setBackground(select);
        String card_selected = (String)view.getTag();
        Card controller_card_selected = round.convertToControllerCard(card_selected);
        //System.out.println("CARD SELECTED IS: "+ card_selected);
        //System.out.println("CONTROLLER CONVERSION IS:  " + controller_card_selected.cardToString());

        if (controller_card_selected.cardToString().equals(Deck.getTopDrawCard().cardToString())){
            //System.out.println("TOP DRAW CARD");
            round.getHumanPlayer().addCardToHand(Deck.takeTopDrawCard());
            human_picked_card = true;
            getCurrentRoundDetails();
        }

        else if (controller_card_selected.cardToString().equals(Deck.getTopDiscardCard().cardToString())){
            round.getHumanPlayer().addCardToHand(Deck.takeTopDiscardCard());
            human_picked_card = true;
            getCurrentRoundDetails();
        }

        else{
            //TODO -> DROP CARD
            //human_picked_card = false;
            System.out.println("INSIDE DROP CARD");

            getCurrentRoundDetails();
            round.getHumanPlayer().dropCard(controller_card_selected.cardToString());
            next_player = "Computer";
            getCurrentRoundDetails();

            if (round.getHumanPlayer().goOut()){
                verify_goout_human = true;
                Toast.makeText(getApplicationContext(), "HUMAN WENT OUT!", Toast.LENGTH_LONG ).show();
            }

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


}
