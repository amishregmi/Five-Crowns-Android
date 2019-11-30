package aregmi.ramapo.edu.five_crowns.model.players;

import java.util.Collections;
import java.util.HashSet;
import java.util.Iterator;
import java.util.Set;
import java.util.Vector;

import aregmi.ramapo.edu.five_crowns.model.setup.Card;
import aregmi.ramapo.edu.five_crowns.model.setup.Deck;


public class Player {

    private int hand_score;
    private int total_wildcards_num;
    private int total_jokers_num;
    private int current_round_num;
    Vector<Vector<String>> recursive_bookrun_hands = new Vector<Vector<String>>();


    protected Vector<Card> current_player_hand = new Vector<Card>();
    protected Vector<String> current_player_hand_str = new Vector<String>();
    protected int total_cards_in_hand;
    protected Vector<String> child_returning_smallest_sum = new Vector<String>();


    public Player(){
        total_cards_in_hand = 0;
        total_wildcards_num = 0;
        total_jokers_num = 0;
        current_player_hand.clear();
        current_player_hand_str.clear();
    }

    public void addCardToHand(Card card){
        total_cards_in_hand++;
        current_player_hand.add(card);
        String current_card_str = card.cardToString();
        current_player_hand_str.add(current_card_str);
    }

    public void setPlayerHand(Vector<String> hand){
        current_player_hand.clear();
        current_player_hand_str.clear();
        total_cards_in_hand = 0;
        total_wildcards_num = 0;
        total_jokers_num = 0;
        Iterator value = hand.iterator();
        char face, suit;

        while (value.hasNext()){
            String card = (String) value.next();
            face = card.charAt(0);
            suit = card.charAt(1);
            String s_face = String.valueOf(face);
            String s_suit = String.valueOf(suit);
            Card current_card = new Card(s_face, s_suit);
            addCardToHand(current_card);
        }

        checkWildcards();
        checkJokercards();
    }

    public void checkJokercards() {
        if (current_round_num != 0){
            total_jokers_num = 0;
            Iterator value = current_player_hand_str.iterator();
            while (value.hasNext()){
                String current_card = (String) value.next();
                boolean check_joker = checkIfJoker(current_card);
                if (check_joker){
                    total_jokers_num++;
                }
            }
        }
    }

    public void checkWildcards() {
        if (current_round_num != 0){
            total_wildcards_num = 0;
            Iterator value = current_player_hand_str.iterator();
            while (value.hasNext()) {
                String current_card = (String) value.next();
                boolean check_card = checkIfWildcard(current_card);
                if (check_card) {
                    total_wildcards_num++;
                }
            }
        }
        //System.out.println("TOTAL NUM OF WILDCARDS IS: "+ total_wildcards_num);
    }

    public void setCurrentRoundNum(int roundNumber){

        //System.out.println("round number in player set to: "+ roundNumber);
        current_round_num = roundNumber;
    }

    public void printCurrentHand(){
        //System.out.println("The current player hand is: ");
        Iterator value = current_player_hand.iterator();
        while (value.hasNext()){
            Card current_card = (Card) value.next();
            //System.out.println(current_card.cardToString());
        }
    }




    Vector<String> handToStr(Vector<Card> present_hand_card){
        Vector<String> present_hand_card_str = new Vector<String>();
        Iterator value = present_hand_card.iterator();
        while (value.hasNext()){
            Card current_card = (Card) value.next();
            String current_card_str = current_card.cardToString();
            present_hand_card_str.add(current_card_str);
        }
        return present_hand_card_str;
    }

    int totalApplicableWildcardsNum(Vector<Card> present_hand){
        int total_applicable_wildcards = 0;
        Vector<String> present_hand_str = handToStr(present_hand);
        Vector<String> temp;
        Iterator value = present_hand_str.iterator();

        while (value.hasNext()){
            //Card current_card = (Card) value.next();
            String current_card_str = (String) value.next();
            boolean check_if_joker = checkIfJoker(current_card_str);
            boolean check_if_wildcard = checkIfWildcard(current_card_str);

            if (check_if_joker){
                total_applicable_wildcards+=1;
            }

            else if (check_if_wildcard){
                total_applicable_wildcards+=1;
            }
        }

        return total_applicable_wildcards;
    }

    public boolean checkIfWildcard(String current_card_str) {
        //System.out.println("CURRENT WILDCARD IS: " + (current_round_num+2));

        //System.out.println("INSIDE CHECKIFWILDCARD ROUND NUMB IS "+ current_round_num);
        if (Character.isDigit(current_card_str.charAt(0))){
            //System.out.println("INSIDE IF FIRST CHAR IS NUMBER");
            int number = Character.getNumericValue(current_card_str.charAt(0));
            //System.out.println("NUMBER IS: "+ number);
            if (number == (current_round_num + 2)){
                return true;
            }
        }

        if (current_round_num == 8 && current_card_str.charAt(0) == 'X'){
            return true;
        }

        if (current_round_num == 9 && (current_card_str.charAt(0) == 'J' && !(Character.isDigit(current_card_str.charAt(1))))){
            return true;
        }

        if (current_round_num == 10 && current_card_str.charAt(0) == 'Q'){
            return true;
        }

        if (current_round_num == 11 && current_card_str.charAt(0) == 'K'){
            return true;
        }

        return false;
    }

    public boolean checkIfJoker(String current_card_str) {
        if ((current_card_str.charAt(0) == 'J') && Character.isDigit(current_card_str.charAt(1))){
            int number = Character.getNumericValue(current_card_str.charAt(1));

            if (number > 0 && number < 4){
                return true;
            }
        }
        return false;
    }

    Vector<String> handWithoutWildcards(Vector<Card> present_hand){
        Vector<String> present_hand_str = handToStr(present_hand);
        Iterator value = present_hand_str.iterator();

        while(value.hasNext()){
            //Card current_card = (Card) value.next();
            String current_card_str = (String) value.next();
            boolean check_if_joker = checkIfJoker(current_card_str);
            boolean check_if_wildcard = checkIfWildcard(current_card_str);

            if (check_if_joker){
                value.remove();
            }
            else if (check_if_wildcard){
                value.remove();
            }

        }

        return present_hand_str;
    }

    //TODO -> CHANGE TO PRIVATE

    public boolean checkBook (Vector<Card> handToCheck){
        if (handToCheck.size() < 3){
            return false;
        }

        int total_applicable_wildcards = totalApplicableWildcardsNum(handToCheck);
        Vector<String> temp = handWithoutWildcards(handToCheck);

        if (temp.size() == 1){
            return true;
        }

        Vector<Integer> face_numbers = new Vector<Integer>();
        Iterator value = temp.iterator();

        while (value.hasNext()){
            String current_card_str = (String) value.next();
            int number = Character.getNumericValue(current_card_str.charAt(0));
            face_numbers.add(number);
        }

        Collections.sort(face_numbers);
        Set<Integer> unique = new HashSet<Integer>();
        unique.addAll(face_numbers);

        if (unique.size() <= 1){
            return true;
        }

        return false;

    }


    //TODO -> CHANGE TO PRIVATE
    public boolean checkRun(Vector<Card> current_hand_to_check){

        if (current_hand_to_check.size() < 3){
            return false;
        }

        int total_applicable_wildcards = totalApplicableWildcardsNum(current_hand_to_check);
        Vector<String> temp = handWithoutWildcards(current_hand_to_check);

        //System.out.println("TOTAL APPLICABLE WILDCARDS IS: "+ total_applicable_wildcards);
        //System.out.println("HAND WITHOUT WILDCARDS : ");
        //Iterator val = temp.iterator();
        //while (val.hasNext()){
        //    System.out.print((String)val.next()+ " ");
        //}
        //System.out.println("");

        if (temp.size() <= 1){
            return true;
        }

        Iterator value  = temp.iterator();
        Vector<String> temp_suits = new Vector<String>();
        Vector<String> temp_cards = new Vector<String>();

        while(value.hasNext()){
            String current_tempandsuit = (String) value.next();
            //System.out.println("CURRENT TEMP AND SUIT SHOULD BE EITHER 4H OR XH " + current_tempandsuit);
            char suit = current_tempandsuit.charAt(1);
            //System.out.println("CHAR SUIT SHOULD BE H "+ suit);
            String s = String.valueOf(suit);
            //System.out.println("STRING S SHOULD BE H "+ s);

            if (!(temp_cards.contains(current_tempandsuit))){
                temp_cards.add(current_tempandsuit);
            }

            else {
                return false;
            }

            if (!(temp_suits.contains(s))){
                temp_suits.add(s);
            }
        }


        if (temp_suits.size() > 1){
            return false;
        }

        Vector<Integer> face_values = new Vector<Integer>();
        value = temp.iterator();

        while(value.hasNext()){
            String current_tempandsuit = (String) value.next();
            char face = current_tempandsuit.charAt(0);
            int int_face = Character.getNumericValue(current_tempandsuit.charAt(0));

            if (face == 'X'){
                int_face -= 23;
            }

            if (face == 'J'){
                int_face -= 8;
            }

            if (face == 'Q'){
                int_face -= 12;
            }

            if (face == 'K'){
                int_face -= 7;
            }

            if (!(face_values.contains(int_face))){
                face_values.add(int_face);
            }
        }

        Collections.sort(face_values);

        if (face_values.size() == 1){
            return false;
        }

        int max_diff = facesMaxDiff(face_values, face_values.size());

        if (max_diff <= (total_applicable_wildcards+1)){
            return true;
        }

        return false;
    }

    private int facesMaxDiff(Vector<Integer> face_values, int size) {
        int start = -5;
        int end = -5;
        //int max_diff = face_values.get(1) - face_values.get(0);
        int max_diff = -5000;

        for (int i = 0; i< size; i++){
            for (int j = i + 1; j < size; j++){

                if (face_values.get(j) - face_values.get(i) > max_diff){
                    max_diff = face_values.get(j) - face_values.get(i);
                    end = face_values.get(j);
                    start = face_values.get(i);
                }
            }
        }

        for (int i = 0; i < size; i++){
            if (face_values.get(i) > start && face_values.get(i) < end){
                max_diff--;
            }
        }
        return max_diff;
    }

    public String pickCardHelp(){
        System.out.println("Virtual function pickCardHelp needs to be overwritten in child class");
        return "";
    }

    public void pickCard(){
        System.out.println("VIRTUAL FUNCTION PICKCARD");
    }

    public void dropCard(){
        System.out.println("VIRTUAL FUNCTION DROPCARD");
    }



    public boolean goOut(){
        checkJokercards();
        checkWildcards();
        int total_cards = current_player_hand.size();
        hand_score = calculateSumOfCards(current_player_hand);
        child_returning_smallest_sum.clear();
        bestBookRunCombination(current_player_hand);

        if (hand_score == 0){
            //System.out.println("GOING OUT WITH");
            //printCurrentHand();

            return true;
        }

        return false;

    }

    private void bestBookRunCombination(Vector<Card> current_hand) {

        Vector<String> current_hand_str = handToStr(current_hand);
        Vector<Vector<Card>> listof_booksandruns_currenthand = generatePossibleCombinations(current_hand_str);

        if (listof_booksandruns_currenthand.size() == 0){
            int score = calculateSumOfCards(current_hand);

            if (score <= hand_score){
                recursive_bookrun_hands.clear();
                recursive_bookrun_hands.add(current_hand_str);
                Vector<String> temp = new Vector<String>();
                Iterator value = current_player_hand_str.iterator();
                while (value.hasNext()){
                    String current_card = (String) value.next();
                    if (!(current_hand_str.contains(current_card))){
                        temp.add(current_card);
                    }
                }

                if (score < hand_score){
                    child_returning_smallest_sum = temp;
                }

                hand_score = score;

            }
            //return hand_score;
        }

        Iterator value = listof_booksandruns_currenthand.iterator();

        while (value.hasNext()){
            Vector<Card> hand_after_removal = new Vector<Card>();
            Vector<String> temp_hand_after_removal = new Vector<String>(current_hand_str);
            //temp_hand_after_removal = current_hand_str;

            Vector<Card> inside = ((Vector<Card>) value.next());
            Iterator inside_it = inside.iterator();

            while (inside_it.hasNext()){
                Card current_s = (Card) inside_it.next();
                String current = current_s.cardToString();
                temp_hand_after_removal.remove(current);
            }

            inside_it = temp_hand_after_removal.iterator();
            while (inside_it.hasNext()){
                String current = (String) inside_it.next();
                char face = current.charAt(0);
                char suit = current.charAt(1);
                String s_face = String.valueOf(face);
                String s_suit = String.valueOf(suit);
                Card current_card = new Card(s_face, s_suit);
                hand_after_removal.add(current_card);
            }
            //TODO -> CHECK THIS. Without return
            bestBookRunCombination(hand_after_removal);
        }

        //return 0;
    }

    private Vector<Vector<Card>> generatePossibleCombinations(Vector<String> current_player_hand_str) {
        Vector<Vector<String>> possible_combinations = new Vector<Vector<String>>();
        Collections.sort(current_player_hand_str);
        Vector<String> x_faces = new Vector<String>();
        Vector<String> k_faces = new Vector<String>();
        Vector<String> wildcardsandjokers = new Vector<String>();

        Iterator value = current_player_hand_str.iterator();

        while (value.hasNext()){
            String current_card = (String) value.next();

            if (checkIfWildcard(current_card)){
                wildcardsandjokers.add(current_card);
                value.remove();
            }

            else if (checkIfJoker((current_card))){
                wildcardsandjokers.add(current_card);
                value.remove();
            }
            else {
                if (current_card.charAt(0)=='X'){
                    x_faces.add(current_card);
                    value.remove();
                }
                else if (current_card.charAt(0) == 'K'){
                    k_faces.add(current_card);
                    value.remove();
                }
            }
        }

        Collections.sort(x_faces);
        Collections.sort(k_faces);

        int first = 50;

        value = current_player_hand_str.iterator();

        while (value.hasNext()){
            String current_card = (String) value.next();
            if (current_card.charAt(0) == 'J' || current_card.charAt(0) == 'Q'){
                int index = current_player_hand_str.indexOf(current_card);
                if (index < first){
                    first = index;
                }
            }
        }

        value = x_faces.iterator();

        while (value.hasNext()){
            String current_card = (String) value.next();
            if (first == 50){
                current_player_hand_str.add(current_card);
            }
            else {
                current_player_hand_str.add(first, current_card);
                first++;
            }
        }

        value = k_faces.iterator();

        while (value.hasNext()){
            String current_card = (String) value.next();
            current_player_hand_str.add(current_card);
        }

        int max_numofcards_in_combination = current_player_hand_str.size();
        int min_numofcards_in_combination = 3;

        if (wildcardsandjokers.size() == 1){
            min_numofcards_in_combination--;
        }

        else if (wildcardsandjokers.size() >= 2){
            min_numofcards_in_combination -= 2;
        }

        int numofcards_in_current_combination = min_numofcards_in_combination;
        int start_index_current_combination;

        while (numofcards_in_current_combination <= max_numofcards_in_combination){
            start_index_current_combination = 0;

            while (start_index_current_combination <= (max_numofcards_in_combination - numofcards_in_current_combination)){

                Vector<String> current_combination = new Vector<String>();
                for (int i = start_index_current_combination; i < (numofcards_in_current_combination + start_index_current_combination); i++) {
                    if (i < current_player_hand_str.size()) {
                        current_combination.add(current_player_hand_str.get(i));
                    }
                }

                possible_combinations.add(current_combination);
                start_index_current_combination++;
            }
            numofcards_in_current_combination++;
        }

        String[] suits = {"S", "C", "D", "H", "T"};

        for (int it = 0; it < suits.length; it++){
            String current_suit = suits[it];
            char current_char = current_suit.charAt(0);
            Vector<String> current_suit_cards = new Vector<String>();

            value = current_player_hand_str.iterator();
            while (value.hasNext()){

                String current_card = (String) value.next();
                if (current_card.charAt(1) == current_char){
                    current_suit_cards.add(current_card);
                }
            }

            max_numofcards_in_combination = current_suit_cards.size();
            numofcards_in_current_combination = min_numofcards_in_combination;

            while (numofcards_in_current_combination <= max_numofcards_in_combination){
                start_index_current_combination = 0;
                while (start_index_current_combination <= (max_numofcards_in_combination - numofcards_in_current_combination)){
                    Vector<String> current_combination = new Vector<String>();
                    for (int i = start_index_current_combination; i< (numofcards_in_current_combination + start_index_current_combination); i++){
                        if ( i < current_suit_cards.size()){
                            current_combination.add(current_suit_cards.get(i));
                        }
                    }

                    if (!(possible_combinations.contains(current_combination))){
                        possible_combinations.add(current_combination);
                    }

                    start_index_current_combination++;
                }
                numofcards_in_current_combination++;
            }

        }

        Vector<Vector<String>> copy_possible_combinations = new Vector<Vector<String>>(possible_combinations);

        Iterator it = copy_possible_combinations.iterator();
        while (it.hasNext()){
            Vector<String> temp = new Vector<String>();
            temp = (Vector<String>) it.next();
            Iterator wildcards_it = wildcardsandjokers.iterator();
            while (wildcards_it.hasNext()){
                temp.add((String)wildcards_it.next());
                possible_combinations.add(temp);
            }
        }

        return listBooksAndRuns(possible_combinations);
    }

    private Vector<Vector<Card>> listBooksAndRuns(Vector<Vector<String>> possible_combinations) {
        boolean checkbook, checkrun;
        Vector<Vector<Card>> list_books_and_runs = new Vector<Vector<Card>>();

        for (int i = 0; i < list_books_and_runs.size(); i++){
            list_books_and_runs.get(i).clear();
        }

        char face, suit;
        Iterator value = possible_combinations.iterator();
        while (value.hasNext()){
            Vector<Card> current_hand_combination = new Vector<Card>();
            Vector<String> inside = (Vector<String>) value.next();
            Iterator inside_iterator = inside.iterator();

            while (inside_iterator.hasNext()){
                String current = (String) inside_iterator.next();
                face = current.charAt(0);
                suit = current.charAt(1);
                String s_face = String.valueOf(face);
                String s_suit = String.valueOf(suit);
                Card temp = new Card(s_face, s_suit);
                current_hand_combination.add(temp);
            }

            checkbook = checkBook(current_hand_combination);
            checkrun = checkRun(current_hand_combination);
            if (checkbook || checkrun){
                list_books_and_runs.add(current_hand_combination);
            }
        }
        return list_books_and_runs;
    }


    private int calculateSumOfCards(Vector<Card> remaining_cards) {
        int score = 0;
        Iterator value = remaining_cards.iterator();
        boolean check_if_wildcard, check_if_joker;

        while (value.hasNext()){
            Card current_card = (Card) value.next();
            String current_card_str = current_card.cardToString();
            check_if_wildcard = checkIfWildcard(current_card_str);
            check_if_joker = checkIfJoker(current_card_str);

            if (check_if_wildcard){
                score += 20;
            }
            else if (check_if_joker){
                score += 50;
            }

            else {

                char face = current_card_str.charAt(0);
                int int_face = Character.getNumericValue(current_card_str.charAt(0));

                if (face == 'X'){
                    int_face -= 23;
                }

                if (face == 'J'){
                    int_face -= 8;
                }

                if (face == 'Q'){
                    int_face -= 12;
                }

                if (face == 'K'){
                    int_face -= 7;
                }

                score += int_face;
            }

        }

        return score;
    }

    public void printAvailableBooksandRuns(){
        if (child_returning_smallest_sum.size() == 0){
            System.out.println("No books or runs");
        }
        else {
            System.out.println("BOOKS AND RUNS ARE: ");
            Iterator value = child_returning_smallest_sum.iterator();
            while (value.hasNext()){
                System.out.print((String) value.next()+"   ");
            }
            System.out.println("");
        }
    }

    public int getHandScore(){
        return hand_score;
    }

    public static void main(String[] args){
        Player player = new Player();
        player.setCurrentRoundNum(1);
        Card first = new Card("9", "C");
        Card second = new Card ("9", "D");
        Card third = new Card ("9", "H");
        Card fourth = new Card ("J", "1");
        player.addCardToHand(first);
        player.addCardToHand(second);
        player.addCardToHand(third);
        player.addCardToHand(fourth);
        player.checkJokercards();
        player.checkWildcards();
        player.goOut();
        //System.out.println("TOTAL WILDCARD NUM: "+ player.total_wildcards_num);
        //player.printCurrentHand();
        //player.printCurrentHand();
        player.printAvailableBooksandRuns();
        System.out.println(player.goOut());


    }

    public String getPlayerHandStr(){
        String hand = "";
        Iterator value = current_player_hand_str.iterator();
        while (value.hasNext()){
            hand += (String) value.next() + " ";

        }
        return hand;
    }

}
