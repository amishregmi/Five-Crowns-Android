package aregmi.ramapo.edu.five_crowns.model.setup;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;
import java.util.Vector;
import aregmi.ramapo.edu.five_crowns.model.players.Computer;
import aregmi.ramapo.edu.five_crowns.model.players.Human;
import aregmi.ramapo.edu.five_crowns.model.players.Player;
import aregmi.ramapo.edu.five_crowns.model.setup.Deck;
import aregmi.ramapo.edu.five_crowns.model.setup.Game;

public class Round {

    private Vector<Player> playersList;
    private String[] player_names;
    private int next_player_index;
    private Human human_player;
    private Computer computer_player;
    private int total_players_num;
    private boolean verify_go_out_first;
    private boolean verify_go_out_second;
    private int roundNumber;
    private boolean readFromFile;

    public Round(){
        total_players_num = 2;
        next_player_index = 0;
        verify_go_out_first = false;
        verify_go_out_second = false;
        human_player = new Human();
        computer_player = new Computer();
    }

    public void setReadFromFile(boolean value){
        readFromFile = value;
    }

    public void setRoundNum(int round_number){
        roundNumber = round_number;
    }

    public void setHumanPlayer(Human human){
        human_player = human;
    }

    public Human getHumanPlayer(){
        return human_player;
    }

    public void setComputerPlayer(Computer computer){
        computer_player = computer;
    }

    public Computer getComputerPlayer(){
        return computer_player;
    }

    public void setPlayerList(String next_player_name){
        playersList = new Vector<Player>();
        player_names = new String[2];
        if (!readFromFile && roundNumber ==1){
            if (next_player_name.equals("Human")){
                //playersList.setElementAt(human_player,0);
                playersList.add(0, human_player);
                //playersList.setElementAt(computer_player,1);
                playersList.add(1, computer_player);
                player_names[0] = "Human";
                player_names[1] = "Computer";
            }
            else{
                //playersList.setElementAt(computer_player,0);
                playersList.add(0, computer_player);
                //playersList.setElementAt(human_player,1);
                playersList.add(1, human_player);
                player_names[0] = "Computer";
                player_names[1] = "Human";
            }

        }

        else{

            if (next_player_name.equals("Human")){
                //playersList.setElementAt(human_player, 0);
                playersList.add(0, human_player);
                //playersList.setElementAt(computer_player, 1);
                playersList.add(1, computer_player);
                player_names[0] = "Human";
                player_names[1] = "Computer";
            }

            else {
                //playersList.setElementAt(computer_player,0);
                playersList.add(0, computer_player);
                //playersList.setElementAt(human_player,1);
                playersList.add(1, human_player);
                player_names[0] = "Computer";
                player_names[1] = "Human";
            }

        }

    }

    public void dealForRound(){
        Vector<Card> dealtCards = Deck.dealCards(roundNumber);
        Iterator value = dealtCards.iterator();

        if (!dealtCards.isEmpty()){

            for (int i = 0 ; i < (dealtCards.size() -1); i +=2 ){
                //playersList.elementAt(next_player_index).addCardToHand(dealtCards.elementAt(i));
                Card cardtoadd = dealtCards.get(i);
                playersList.get(next_player_index)
                        .addCardToHand(cardtoadd);
                //playersList.elementAt(next_player_index).setCurrentRoundNum(roundNumber);
                playersList.get(next_player_index).setCurrentRoundNum(roundNumber);
                next_player_index = (next_player_index + 1) % total_players_num;
                //playersList.elementAt(next_player_index).addCardToHand(dealtCards.elementAt(i+1));
                playersList.get(next_player_index).addCardToHand(dealtCards.get(i+1));
                //playersList.elementAt(next_player_index).setCurrentRoundNum(roundNumber);
                playersList.get(next_player_index).setCurrentRoundNum(roundNumber);
                next_player_index = (next_player_index+1) % total_players_num;

            }

        }

        System.out.println("WHEN ROUND STARTED: ");
        System.out.println("DRAW PILE: ");
        System.out.println(Deck.getCurrentDrawPile());
        System.out.println("DISCARD PILE: ");
        System.out.println(Deck.getCurrentDiscardPile());
        System.out.println("HUMAN HAND: ");
        human_player.printCurrentHand();
        System.out.println("COMPUTER HAND: ");
        computer_player.printCurrentHand();

    }

    public String getDrawPile(){
        return Deck.getCurrentDrawPile();
    }

    public String getDiscardPile(){
        return Deck.getCurrentDiscardPile();
    }

    public String getHumanHand(){
        return human_player.getPlayerHandStr();
    }

    public String getComputerHand(){
        return computer_player.getPlayerHandStr();
    }

    public List<String> convertToDrawableString(String givenString) {
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

    public Card convertToControllerCard(String card_selected) {
        Card card = new Card();

        if (card_selected.equals("j1") || card_selected.equals("j2") || card_selected.equals("j3")){
            //System.out.println("INSIDE IF");
            card_selected = card_selected.toUpperCase();
            char face = card_selected.charAt(0);
            char suit = card_selected.charAt(1);
            String s_face = String.valueOf(face);
            String s_suit = String.valueOf(suit);
            card.setFace(s_face);
            card.setSuit(s_suit);
        }

        else{
            char first = card_selected.charAt(1);
            char second = card_selected.charAt(0);
            String s_first = (String.valueOf(first)).toUpperCase();
            String s_second = (String.valueOf(second)).toUpperCase();
            card.setFace(s_first);
            card.setSuit(s_second);

        }


        return card;
    }




}
