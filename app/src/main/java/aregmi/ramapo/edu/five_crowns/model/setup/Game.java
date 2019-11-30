package aregmi.ramapo.edu.five_crowns.model.setup;

import android.os.Environment;

import java.io.BufferedReader;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;
import java.util.Vector;

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

    public Round setRoundFromFile(String file_name){
        //System.out.println("INSIDE SETROUNDFROMFILE");
        Round round = new Round();
        String filenamewithpath = Environment.getExternalStorageDirectory().getAbsolutePath()+"/saved_games/"+file_name;
        try(BufferedReader br = new BufferedReader((new FileReader(filenamewithpath)))) {
            //System.out.println("INSIDE TRY");
            String line;
            int round_number = -5;
            while ((line = br.readLine()) != null){
                //System.out.println("INSIDE WHILE");
                String[] splitStr = line.split("\\s+");
                boolean round_num_found = false;

                for (String oneword: splitStr){

                    if (oneword.equals("Round:")){
                        round_num_found = true;
                        continue;
                    }

                    if (round_num_found){
                        round_number = Integer.parseInt(oneword);
                        round.setRoundNum(round_number);
                        round_num_found = false;
                        //System.out.println("ROUND NUMBER IS: "+ round_number);
                    }

                    if (oneword.equals("Draw")){
                        //
                        String drawpilefile = line.substring(line.indexOf(":") + 1);
                        //System.out.println("DRAW PILE FILE IS: "+ drawpilefile);
                        Vector<String> draw_pile_cards = extract_card_str(drawpilefile);

                        //TODO -> REVERSE DONE IN C++ CODE
                        Deck.setDrawPile(draw_pile_cards);
                    }

                    else if (oneword.equals("Discard")){
                        String discardpilefile = line.substring(line.indexOf(":")+1);
                        //System.out.println("DISCARD PILE FILE IS: "+ discardpilefile);
                        Vector<String> discard_pile_cards = extract_card_str(discardpilefile);
                        //System.out.println("PARAMETER WITH WHICH CALLED: ");
                        Deck.setDiscardPile(discard_pile_cards);
                    }

                    else if (oneword.equals("Next")){
                        String next_player_txt = line.substring(line.indexOf(":")+1);
                        Vector<String> next_playerr = extract_card_str(next_player_txt);
                        round.setPlayerList(next_playerr.get(0));
                    }

                    else if (oneword.equals("Human:") || oneword.equals("Computer:")){
                        String indented_line = "", values, player_hand;
                        Vector<String> cards_str = new Vector<String>();
                        int player_score = -5;

                        for (int i = 0; i < 2 ; i++){
                            line  = br.readLine();
                            String[] player_details  = line.split("\\s+");
                            boolean score_found = false;

                            for (String one_word : player_details){
                                if (one_word.equals("Score:")){
                                    score_found = true;
                                    continue;
                                }

                                if (score_found){
                                    try{
                                        player_score = Integer.parseInt(one_word);
                                        score_found = false;
                                    }
                                    catch (NumberFormatException ex){
                                        System.out.println("EXCEPTION WHEN GETTING SCORE: "+ ex);
                                    }

                                }

                                if (one_word.equals("Hand:")){
                                    player_hand = line.substring(line.indexOf(":")+1);
                                    //System.out.println("PLAYER HAND IS: "+ player_hand);
                                    cards_str = extract_card_str(player_hand);
                                }
                            }

                        }


                        if (oneword.equals("Human:")){
                            round.getHumanPlayer().setPlayerHand(cards_str);
                            if (round_number != -5){
                                round.getHumanPlayer().setCurrentRoundNum(round_number);
                            }
                            human_player_points = player_score;
                        }

                        else if (oneword.equals("Computer:")){
                            round.getComputerPlayer().setPlayerHand(cards_str);
                            if (round_number != -5){
                                round.getComputerPlayer().setCurrentRoundNum(round_number);
                            }
                            computer_player_points = player_score;
                        }


                    }

                }

            }

        } catch (FileNotFoundException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }


        return round;
    }

    private Vector<String> extract_card_str(String hand) {
        Vector<String> cards = new Vector<>();
        String[] given_str = hand.split(" ");
        for (String onecard: given_str){
            if (onecard.length() != 0){
                cards.add(onecard);
            }
        }
        return cards;
    }


}
