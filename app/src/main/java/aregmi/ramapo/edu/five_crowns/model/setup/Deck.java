package aregmi.ramapo.edu.five_crowns.model.setup;

import android.content.pm.PackageManager;

import java.util.Collection;
import java.util.Collections;
import java.util.Iterator;
import java.util.Vector;


public class Deck {
    private static Vector<Card> drawPile = new Vector<Card>();
    private static Vector<Card> discardPile = new Vector<Card>();

    public Deck(){
        resetDeck();
    }

    static private void resetDeck() {
        drawPile.clear();
        discardPile.clear();
        //C, D, H,S done
        //

        final String[] suits = {"S", "C", "D", "H", "T"};
        final String[] faces = { "3", "4", "5", "6", "7", "8", "9", "X", "J", "Q", "K" };
        final String joker_face = "J";
        final String[] joker_suits = {"1", "2", "3"};

        for (int i = 0; i < 2 ; i++){
            for (int j = 0; j < (suits.length); j++){
                for (int k = 0 ; k < (faces.length); k++){
                    Card current_card = new Card(faces[k], suits[j]);
                    drawPile.add(current_card);
                }
            }

            for (int l = 0; l < joker_suits.length; l++){
                Card current_card = new Card(joker_face, joker_suits[l]);
                drawPile.add(current_card);
            }
        }

        Collections.shuffle(drawPile);
        Collections.shuffle(discardPile);

    }


    static public Vector<Card> dealCards(int roundNumber){
        int current_round_par = roundNumber;
        resetDeck();

        //System.out.println("After resetDeck()");
        String draw_pile = getCurrentDrawPile();
        //System.out.println("IN DECK CLASS");
        //System.out.println("CURRENT draw_pile is: ");
        //System.out.println(draw_pile);
        String discard_pile = getCurrentDiscardPile();
        //System.out.println("CURRENT discard_pile is: ");
        //System.out.println(discard_pile);

        Vector<Card> cardstodeal = new Vector<Card>();
        int total_cards_per_player = (2+roundNumber)*2;
        for (int i = 0 ; i < total_cards_per_player; i++){
            if (!drawPile.isEmpty()){
                //System.out.println("Adding element to cardstodeal: ");
                //System.out.println(drawPile.firstElement().cardToString());
                cardstodeal.add(drawPile.firstElement());
                drawPile.removeElementAt(0);
            }
        }

        if (!drawPile.isEmpty()){
            discardPile.add(drawPile.firstElement());
            drawPile.removeElementAt(0);
        }

        return cardstodeal;
    }

    static public Card getTopDiscardCard(){
        if (!discardPile.isEmpty()){
            return discardPile.firstElement();
        }
        return null;
    }

    static public Card getTopDrawCard(){
        if (!drawPile.isEmpty()){
            return drawPile.firstElement();
        }
        return null;
    }

    static public Card takeTopDiscardCard(){
        if (!discardPile.isEmpty()){
            Card topcard = discardPile.firstElement();
            discardPile.removeElementAt(0);
            return topcard;
        }
        return null;
    }

    static public Card takeTopDrawCard(){
        if (!drawPile.isEmpty()){
            Card topcard = drawPile.firstElement();
            drawPile.removeElementAt(0);
            return topcard;
        }
        return null;
    }

    static public void pushToDiscardPile(Card card){
        discardPile.add(0, card);
    }

    static public void setDrawPile(Vector<String> draw_Pile){
        drawPile.clear();
        Iterator iterator = draw_Pile.iterator();
        char face, suit;
        while (iterator.hasNext()){
            face = iterator.next().toString().charAt(0);
            suit = iterator.next().toString().charAt(1);
            String s_face = String.valueOf(face);
            String s_suit = String.valueOf(suit);
            Card current_card = new Card(s_face, s_suit);
            drawPile.add(0, current_card);
        }
    }

    static public void setDiscardPile(Vector<String> discard_Pile){
        discardPile.clear();
        Iterator iterator = discard_Pile.iterator();
        char face, suit;
        while (iterator.hasNext()){
            face = iterator.next().toString().charAt(0);
            suit = iterator.next().toString().charAt(1);
            String s_face = String.valueOf(face);
            String s_suit = String.valueOf(suit);
            Card current_card = new Card(s_face, s_suit);
            discardPile.add(0, current_card);
        }
    }

    static public String getCurrentDrawPile(){
        String current_draw_pile = "";
        Iterator iterator = drawPile.iterator();
        while (iterator.hasNext()) {
            Card temp = (Card) (iterator.next());
            String card_str = temp.cardToString();
            //System.out.println("card_str is: ");
            //System.out.println(card_str);
            current_draw_pile = current_draw_pile + card_str + " ";
        }
        return current_draw_pile;
    }

    static public String getCurrentDiscardPile(){
        String current_discard_pile = "";
        Iterator iterator = discardPile.iterator();
        while (iterator.hasNext()) {
            Card temp = (Card) (iterator.next());
            String card_str = temp.cardToString();
            current_discard_pile = current_discard_pile + card_str + " ";
        }
        return current_discard_pile;
    }



}
