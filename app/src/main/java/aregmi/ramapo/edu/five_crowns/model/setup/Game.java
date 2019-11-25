package aregmi.ramapo.edu.five_crowns.model.setup;

import aregmi.ramapo.edu.five_crowns.model.players.Computer;
import aregmi.ramapo.edu.five_crowns.model.players.Human;
import aregmi.ramapo.edu.five_crowns.model.setup.Deck;
import aregmi.ramapo.edu.five_crowns.model.setup.Round;


public class Game {

    private static int human_player_points;
    private static int computer_player_points;
    private int round_number;
    private boolean read_from_file;

    public Game(){
        human_player_points = 0;
        computer_player_points = 0;
    }

    public void setReadFromFile(boolean readfromfile){
        read_from_file = readfromfile;
    }

    public Round startRound(int round_num){
        Round round = new Round();
        round.setRoundNum(round_num);
        return round;
    }


    public void addHumanTotalPoints(int points){
        human_player_points += points;
    }

    public void addComputerTotalPoints(int points){
        computer_player_points += points;
    }

    public int getHumanTotalPoints(){
        return human_player_points;
    }

    public int getComputerTotalPoints(){
        return computer_player_points;
    }

}
