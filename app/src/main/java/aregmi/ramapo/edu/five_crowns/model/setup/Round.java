package aregmi.ramapo.edu.five_crowns.model.setup;

import java.util.Iterator;
import java.util.Vector;
import aregmi.ramapo.edu.five_crowns.model.players.Computer;
import aregmi.ramapo.edu.five_crowns.model.players.Human;
import aregmi.ramapo.edu.five_crowns.model.players.Player;
import aregmi.ramapo.edu.five_crowns.model.setup.Deck;
import aregmi.ramapo.edu.five_crowns.model.setup.Game;

public class Round {

    private Vector<Player> playersList = new Vector<>();
    private String[] player_names = new String[2];
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
                playersList.setElementAt(human_player, 0);
                playersList.setElementAt(computer_player, 1);
                player_names[0] = "Human";
                player_names[1] = "Computer";
            }

            else {
                playersList.setElementAt(computer_player,0);
                playersList.setElementAt(human_player,1);
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

    }

    public String getDrawPile(){
        return Deck.getCurrentDrawPile();
    }

    public String getDiscardPile(){
        return Deck.getCurrentDiscardPile();
    }







}
