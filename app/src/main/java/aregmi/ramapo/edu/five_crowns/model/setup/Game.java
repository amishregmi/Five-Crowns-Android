package aregmi.ramapo.edu.five_crowns.model.setup;

import aregmi.ramapo.edu.five_crowns.model.players.Computer;
import aregmi.ramapo.edu.five_crowns.model.players.Human;
import aregmi.ramapo.edu.five_crowns.model.setup.Deck;
import aregmi.ramapo.edu.five_crowns.model.setup.Round;


public class Game {

    private static int human_player_points;
    private static int computer_player_points;
    private int round_number;
    //private Human human;
    //private Computer computer;
    //private String next_player;
    private boolean read_from_file;
    //private Deck deck;

    public Game(){
        human_player_points = 0;
        computer_player_points = 0;
    }

    public void setReadFromFile(boolean readfromfile){
        read_from_file = readfromfile;
    }

    public Round startNewGame(){
        setReadFromFile(false);
        Round round = new Round();
        round.setReadFromFile(false);
        return round;
    }

    public int getHumanTotalPoints(){
        return human_player_points;
    }

    public int getComputerTotalPoints(){
        return computer_player_points;
    }

}
