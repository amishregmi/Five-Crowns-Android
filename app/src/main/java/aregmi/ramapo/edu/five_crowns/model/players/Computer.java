/************************************************************
 * Name: Amish Regmi                                        *
 * Project: Project 3, Five Crowns Android                  *
 * Class: OPL Fall 19                                       *
 * Date: 12/11/2019                                         *
 ************************************************************/

package aregmi.ramapo.edu.five_crowns.model.players;

import java.util.Vector;

import aregmi.ramapo.edu.five_crowns.model.setup.Card;
import aregmi.ramapo.edu.five_crowns.model.players.Player;
import aregmi.ramapo.edu.five_crowns.model.setup.Deck;

public class Computer extends Player{

    private String computer_move = "";

    /**
     * Constructor for Computer
     */

    public Computer(){
    }

    /**
     * To decide whether to pick from draw or discard pile and add the card to the hand.
     */

    public void pickCard(){
        computer_move = "";
        Card topDiscardCard = Deck.getTopDiscardCard();
        Card topDrawCard = Deck.getTopDrawCard();
        String reason;

        if (checkIfJoker(topDiscardCard.cardToString())){
            reason = "Computer picked from top of Discard pile because the card is a joker";
            addCardToHand(topDiscardCard);
            Deck.takeTopDiscardCard();
            computer_move += "Computer picked "+ topDiscardCard.cardToString()+ " from discard pile";
        }

        else if (checkIfWildcard(topDiscardCard.cardToString())){
            reason = "Computer picked from top of discard pile because card is a wildcard";
            addCardToHand(topDiscardCard);
            Deck.takeTopDiscardCard();
            computer_move+= "Computer picked "+ topDiscardCard.cardToString()+ " from discard pile";
        }

        else{
            goOut();
            int points_before_adding_discard_card = getHandScore();
            addCardToHand(topDiscardCard);
            boolean check_if_goout = goOut();

            if (check_if_goout){
                reason = "Computer picked from top of Discard pile because it helps to goOut";
                Deck.takeTopDiscardCard();
                computer_move += "Computer picked "+ topDiscardCard.cardToString()+ " from discard pile";
            }

            else {
                int points_after_adding_discard_card = getHandScore();

                if (points_after_adding_discard_card <= points_before_adding_discard_card){
                    reason = "Computer picked from top of discard pile because it helps in a better book/run combination in hand";
                    Deck.takeTopDiscardCard();
                    computer_move+= "Computer picked "+ topDiscardCard.cardToString()+ " from discard pile";
                }

                else {
                    int drop_index = total_cards_in_hand-1;
                    current_player_hand.remove(drop_index);
                    current_player_hand_str.remove(drop_index);
                    total_cards_in_hand--;
                    reason = "Computer picked from top of draw pile because discard pile didn't have wildcard or joker and didn't help " +
                            "assemble a better book run combination";
                    addCardToHand(Deck.takeTopDrawCard());
                    computer_move+= "Computer picked "+ topDrawCard.cardToString() + " from draw pile";
                }

            }

        }

        //System.out.println(reason);
        dropCard();
    }

    /**
     * To drop the card from the current hand
     */

    public void dropCard(){
        String reason = "";
        //System.out.println("Before dropping card, ");
        printCurrentHand();
        Vector<Integer> points_after_drop = new Vector<Integer>();
        int current_index = 0;
        Vector<Card> temp = new Vector<Card>(current_player_hand);
        Vector<String> temp_str = new Vector<String>(current_player_hand_str);

        while (current_index < total_cards_in_hand){
            //current_player_hand.remove(current_index);
            current_player_hand.removeElementAt(current_index);
            //current_player_hand_str.remove(current_index);

            total_cards_in_hand--;
            goOut();
            points_after_drop.add(getHandScore());

            current_player_hand.clear();
            current_player_hand.addAll(temp);
            current_player_hand_str.clear();
            current_player_hand_str.addAll(temp_str);
            total_cards_in_hand++;
            current_index++;
        }
        int min = 50000;
        int required_index = min;

        for (int i = 0; i < points_after_drop.size(); i++){

            if (!(checkIfJoker(current_player_hand_str.get(i)) || checkIfWildcard(current_player_hand_str.get(i)))){

                int compare_val = points_after_drop.get(i);
                if (compare_val < min){
                    reason = "Computer is dropping card at index "+i + " because it's not wildcard/joker and helps reduce sum of cards in hand after forming best book and run combination";

                    if (points_after_drop.get(i) == 0){
                        reason = "Computer is dropping card at index "+ i + " because the player can go out";
                    }
                    min = points_after_drop.get(i);
                    required_index = i;

                }
            }

        }


        computer_move += " and dropped "+ current_player_hand_str.get(required_index);

        Card card_dropped = current_player_hand.get(required_index);
        current_player_hand.removeElementAt(required_index);
        current_player_hand_str.removeElementAt(required_index);
        total_cards_in_hand--;
        Deck.pushToDiscardPile(card_dropped);
        printCurrentHand();
    }

    /**
     *
     * @return String containing the card computer picked and dropped explanation
     */

    public String getComputerMove(){
        return computer_move;
    }

}
