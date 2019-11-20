package aregmi.ramapo.edu.five_crowns.model.players;

import aregmi.ramapo.edu.five_crowns.model.setup.Card;
import aregmi.ramapo.edu.five_crowns.model.setup.Deck;

public class Human extends  Player implements  PlayerMoves{

    public Human(){

    }

    public void pickCardHelp(){
        Card topDiscardCard = Deck.getTopDiscardCard();
        Card topDrawCard = Deck.getTopDrawCard();
        String reason = "";

        if (checkIfJoker(topDiscardCard.cardToString())){
            reason = "I recommend picking from top of discard pile since it's a joker";
        }

        else if (checkIfWildcard(topDiscardCard.cardToString())){
            reason = "I recommend picking from top of discard pile since it's a wildcard";
        }

        else {

            goOut();
            int points_before_adding_discard_card = getHandScore();
            addCardToHand(topDiscardCard);
            boolean check_if_goout = goOut();

            if (check_if_goout){
                reason = "I recommend picking from top of discard pile since it helps you assemble cards to goOut";
            }

            else {
                int points_after_adding_discard_card = getHandScore();
                if (points_after_adding_discard_card <= points_before_adding_discard_card){
                    reason = "I recommend picking from top of discard pile since it helps you assemble cards to goOut";
                }
                else {
                    reason = "I recommend picking card from top of draw pile since top of discard pile is neither wildcard, nor a joker," +
                            "and does not help form a better book/run combination or help you goOut";
                }
            }

            current_player_hand.removeElementAt(total_cards_in_hand-1);
            current_player_hand_str.removeElementAt(total_cards_in_hand-1);
            total_cards_in_hand--;

        }

        System.out.print("Recommendation: ");
        System.out.println(reason);
    }

    public void pickCard(){


    }

    public void dropCard(){

    }

}
