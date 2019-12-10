package aregmi.ramapo.edu.five_crowns.model.players;

import java.util.Iterator;
import java.util.Vector;

import aregmi.ramapo.edu.five_crowns.model.setup.Card;
import aregmi.ramapo.edu.five_crowns.model.setup.Deck;

public class Human extends  Player{

    public Human(){

    }

    public String pickCardHelp(){
        Card topDiscardCard = Deck.getTopDiscardCard();
        Card topDrawCard = Deck.getTopDrawCard();
        String reason = "";

        if (checkIfJoker(topDiscardCard.cardToString())){
            //reason = "I recommend picking from top of discard pile since it's a joker";
            reason = "Pick from discard pile since it's joker";
        }

        else if (checkIfWildcard(topDiscardCard.cardToString())){
            //reason = "I recommend picking from top of discard pile since it's a wildcard";
            reason = "Pick from discard pile since it's wildcard";
        }

        else {

            goOut();
            int points_before_adding_discard_card = getHandScore();
            addCardToHand(topDiscardCard);
            boolean check_if_goout = goOut();

            if (check_if_goout){
                //reason = "I recommend picking from top of discard pile since it helps you assemble cards to goOut";
                reason = "Pick from discard pile since you can goout";
            }

            else {
                int points_after_adding_discard_card = getHandScore();
                if (points_after_adding_discard_card <= points_before_adding_discard_card){
                    //reason = "I recommend picking from top of discard pile since it helps you assemble cards to goOut";
                    reason = "Pick from discard pile since you can make better book/run combination";

                }
                else {
                    //reason = "I recommend picking card from top of draw pile since top of discard pile is neither wildcard, nor a joker," +
                     //       "and does not help form a better book/run combination or help you goOut";
                    reason = "Pick from draw pile since discard pile doesn't help in better combination";
                }
            }

            current_player_hand.removeElementAt(total_cards_in_hand-1);
            current_player_hand_str.removeElementAt(total_cards_in_hand-1);
            total_cards_in_hand--;

        }

        return reason;
    }

    public String dropCardHelp(){
        //TODO -> SOME ERROR HERE
        String reason = "";
        Vector<Integer> points_after_drop = new Vector<Integer>();
        int current_index = 0;
        Vector<Card> temp = new Vector<Card>(current_player_hand);
        //temp = current_player_hand;
        Vector<String> temp_str = new Vector<String>(current_player_hand_str);
        //temp_str = current_player_hand_str;

        while (current_index < total_cards_in_hand){

            current_player_hand.remove(current_index);
            total_cards_in_hand--;
            current_player_hand_str.remove(current_index);
            goOut();
            points_after_drop.add(getHandScore());

            //RESET VALUES
            current_player_hand = new Vector<Card>(temp);
            total_cards_in_hand++;
            current_player_hand_str = new Vector<String>(temp_str);
            current_index++;
        }

        int min = 50000;
        int required_index;

        for (int i = 0; i < points_after_drop.size(); i++){

            if (! (checkIfJoker(current_player_hand_str.get(i)) || checkIfWildcard(current_player_hand_str.get(i)))){

                if (points_after_drop.get(i) < min){
                    reason = "Drop card " + current_player_hand_str.get(i) + " since not wildcard and joker and helps for best combination";

                    if (points_after_drop.get(i) == 0){
                        reason = "Drop card "+ current_player_hand_str.get(i) + " since you can go out with remaining cards";
                    }
                    min = points_after_drop.get(i);
                    required_index = i;

                }

            }

        }

        return reason;

    }

    public void pickCard(){


    }

    public void dropCard(String card_to_drop){
        System.out.println("INSIDE human.dropCard card_to_drop is: "+ card_to_drop);
        checkJokercards();
        checkWildcards();
        Iterator value = current_player_hand_str.iterator();
        int req_index = -5000;
        int current_index = 0;

        while (value.hasNext()){
            String current_card = (String) value.next();
            if (current_card.equals(card_to_drop)){
                req_index = current_index;
                break;
            }
            current_index++;
        }

        total_cards_in_hand--;
        current_player_hand.remove(req_index);
        current_player_hand_str.remove(req_index);

        String face = String.valueOf(card_to_drop.charAt(0));
        String suit = String.valueOf(card_to_drop.charAt(1));
        Card card = new Card(face, suit);
        Deck.pushToDiscardPile(card);

        checkJokercards();
        checkWildcards();


    }

}
