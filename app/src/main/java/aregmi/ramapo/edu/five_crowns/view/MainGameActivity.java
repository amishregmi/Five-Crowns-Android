package aregmi.ramapo.edu.five_crowns.view;

import android.content.Context;
import android.graphics.drawable.Drawable;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.ImageView;
import android.widget.LinearLayout;

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
    //private Human human = new Human();
    //private Computer computer = new Computer();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main_game);

        int round_num = getIntent().getIntExtra("round",1);
        String next_player = getIntent().getStringExtra("next_player");
        boolean read_from_file = getIntent().getBooleanExtra("read_from_file", false);

        if (read_from_file == false && round_num == 1){
            Game new_game = new Game();
            round = new_game.startNewGame();
            round.setRoundNum(1);
            Human human = new Human();
            round.setHumanPlayer(human);
            Computer computer = new Computer();
            round.setComputerPlayer(computer);
            round.setPlayerList(next_player);
            round.dealForRound();
            human.printCurrentHand();
            computer.printCurrentHand();
            System.out.println("DRAW PILE IS: "+ round.getDrawPile().split("\\s+").length);//String draw_pile = round.getDrawPile();
            //String discard_pile = round.getDiscardPile();
            getCurrentRoundDetails(human, computer);

        }


    }

    //TODO -> CHANGE THIS
    @Override
    public void onClick(View view){
        Drawable select = getResources().getDrawable(R.drawable.selected);
        view.setBackground(select);

    }



    private void getCurrentRoundDetails(Human human, Computer computer) {
        System.out.println("INSIDE getCurrentRoundDetails");
        human.printCurrentHand();
        computer.printCurrentHand();
        List<String> draw_pile = convertToDrawableString(round.getDrawPile());
        //System.out.println("DRAW PILE IS: "+ round.getDrawPile());
        System.out.println("DISCARD PILE IS: "+ round.getDiscardPile());
        //System.out.println("HUMAN HAND IS: "+ human.getPlayerHandStr());
        //System.out.println("COMPUTER HAND IS: "+ computer.getPlayerHandStr());
        //System.out.println("DRAWABLE draw_pile is: "+ draw_pile);
        List<String> discard_pile = convertToDrawableString(round.getDiscardPile());
        System.out.println("LIST STRING DISCARD PILE : ");
        for (String one: discard_pile){
            System.out.println(one);
        }
        List<String> human_hand = convertToDrawableString(human.getPlayerHandStr());
        List<String> computer_hand = convertToDrawableString(computer.getPlayerHandStr());

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

        for (String onecard : cards_to_display){
            ImageView imageview = new ImageView(this);
            LinearLayout.LayoutParams layout_params = new LinearLayout.LayoutParams(210, 250);
            layout_params.setMargins(20, 10, 20, 10);
            imageview.setLayoutParams(layout_params);

            Context context = linearLayout.getContext();
            int id = context.getResources().getIdentifier(onecard, "drawable", context.getPackageName());
            imageview.setImageResource(id);

            imageview.setTag(onecard);
            imageview.setPadding(5, 5,5,5);
            imageview.setId(id);

            if (card_belonging_to.equals("Human")){
                imageview.setClickable(true);
                imageview.setOnClickListener(this);
            }

//            else if (card_belonging_to.equals(""))

            linearLayout.addView(imageview);

        }

    }

    private List<String> convertToDrawableString(String givenString) {
        String[] splitStr = givenString.split("\\s+");
        String combined = "";
        List<String> cards = new ArrayList<>();
        cards.clear();

        for (String one: splitStr){
            //System.out.println(one);
            if (one.equals("J1") || one.equals("J2") || one.equals("J3")){
                //System.out.println("INSIDE IF");
                combined += one.toLowerCase()+ " ";
                //System.out.println(combined);
            }
            else{
                if (!one.isEmpty()){
                    char first = one.charAt(1);
                    char second = one.charAt(0);
                    String reversed = (Character.toString(first)).concat(Character.toString(second));
                    combined += reversed.toLowerCase()+" ";
                }
            }
        }

        splitStr = combined.split("\\s+");

        for (String one: splitStr){
            cards.add(one);
        }

        return cards;
    }


}
