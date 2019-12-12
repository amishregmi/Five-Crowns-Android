/************************************************************
 * Name: Amish Regmi                                        *
 * Project: Project 3, Five Crowns Android                  *
 * Class: OPL Fall 19                                       *
 * Date: 12/11/2019                                         *
 ************************************************************/

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

    /**
     * Constructor for Round class.
     */

    public Round(){
        total_players_num = 2;
        next_player_index = 0;
        verify_go_out_first = false;
        verify_go_out_second = false;
        human_player = new Human();
        computer_player = new Computer();
    }

    /**
     * function to set the readFromFile member to a boolean
     * @param value, the boolean to be set
     */

    public void setReadFromFile(boolean value){
        readFromFile = value;
    }

    /**
     *
     * @return the current roundNumber
     */

    public int getRoundNum(){
        return roundNumber;
    }

    /**
     * Function to set the roundnumber in the round, human, and computer
     * @param round_number, the round number to be set
     */

    public void setRoundNum(int round_number){
        roundNumber = round_number;
        human_player.setCurrentRoundNum(roundNumber);
        computer_player.setCurrentRoundNum(roundNumber);
    }

    /**
     * Function to set the human player when passed in as parameter
     * @param human, the object to be cloned
     */
    public void setHumanPlayer(Human human){
        human_player = human;
    }

    /**
     *
     * @return the current human player object
     */

    public Human getHumanPlayer(){
        return human_player;
    }

    /**
     * Function to set the computer_player to object passed in as parameter
     * @param computer, Computer object
     */

    public void setComputerPlayer(Computer computer){
        computer_player = computer;
    }

    /**
     *
     * @return the current Computer player object
     */

    public Computer getComputerPlayer(){
        return computer_player;
    }

    /**
     * Function to keep track of the next player and set it when round starts
     * @param next_player_name, the name of the next player at the start of the round
     */

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

    /**
     * Function to deal cards for the current round.
     */

    public void dealForRound(){
        Vector<Card> dealtCards = Deck.dealCards(roundNumber);
        Iterator value = dealtCards.iterator();

        if (!dealtCards.isEmpty()){
            for (int i = 0 ; i < (dealtCards.size() -1); i +=2 ){
                Card cardtoadd = dealtCards.get(i);
                playersList.get(next_player_index)
                        .addCardToHand(cardtoadd);
                playersList.get(next_player_index).setCurrentRoundNum(roundNumber);
                next_player_index = (next_player_index + 1) % total_players_num;
                playersList.get(next_player_index).addCardToHand(dealtCards.get(i+1));
                playersList.get(next_player_index).setCurrentRoundNum(roundNumber);
                next_player_index = (next_player_index+1) % total_players_num;
            }
        }
    }

    /**
     *
     * @return a String containing the current Draw pile
     */

    public String getDrawPile(){
        return Deck.getCurrentDrawPile();
    }

    /**
     *
     * @return a String containing the current Discard pile
     */

    public String getDiscardPile(){
        return Deck.getCurrentDiscardPile();
    }

    /**
     *
     * @return String containing the current human hand.
     */

    public String getHumanHand(){
        return human_player.getPlayerHandStr();
    }

    /**
     *
     * @return String containing the current computer hand.
     */

    public String getComputerHand(){
        return computer_player.getPlayerHandStr();
    }

    /**
     * Function to convert a string containing cards as in the controller to strings as in the drawable
     * @param givenString, string containing cards corresponding to controller representation
     * @return List of String containing the cards as stored in drawable
     */

    public List<String> convertToDrawableString(String givenString) {
        String[] splitStr = givenString.split("\\s+");
        String combined = "";
        List<String> cards = new ArrayList<>();
        cards.clear();

        for (String one: splitStr){
            if (one.equals("J1") || one.equals("J2") || one.equals("J3")){
                combined += one.toLowerCase()+ " ";
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

    /**
     * Function to convert a drawable card to card as stored in controller
     * @param card_selected, a string containing the card to be converted
     * @return corresponding Card object.
     */

    public Card convertToControllerCard(String card_selected) {
        Card card = new Card();

        if (card_selected.equals("j1") || card_selected.equals("j2") || card_selected.equals("j3")){
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

        System.out.println("CONTROLLER CARD IS: "+ card.cardToString());
        return card;
    }

    /**
     *
     * @return String containing the next player
     */

    public String getNextPlayer(){
        return player_names[next_player_index];
    }


}
