/************************************************************
 * Name: Amish Regmi                                        *
 * Project: Project 3, Five Crowns Android                  *
 * Class: OPL Fall 19                                       *
 * Date: 12/11/2019                                         *
 ************************************************************/

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
    Vector<Vector<String>> min_branch = new Vector<Vector<String>>();


    protected Vector<Card> current_player_hand = new Vector<Card>();
    protected Vector<String> current_player_hand_str = new Vector<String>();
    protected int total_cards_in_hand;
    protected Vector<String> child_returning_smallest_sum = new Vector<String>();


    /**
     * Constructor for player class
     */

    public Player(){
        total_cards_in_hand = 0;
        total_wildcards_num = 0;
        total_jokers_num = 0;
        current_player_hand.clear();
        current_player_hand_str.clear();
    }

    /**
     * Function to add a card object to the current player's hand
     * @param card, the card object to add
     */

    public void addCardToHand(Card card){
        total_cards_in_hand++;
        current_player_hand.add(card);
        String current_card_str = card.cardToString();
        current_player_hand_str.add(current_card_str);
    }

    /**
     * To set the current player's hand
     * @param hand, Vector of String containing the hand to set.
     */

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

    /**
     * To find and set number of jokers in the hand
     */

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

    /**
     * To find and set the total number of wildcards in the hand.
     */

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

    /**
     * Function to set the current roundNumber
     * @param roundNumber, the round number to set
     */

    public void setCurrentRoundNum(int roundNumber){
        current_round_num = roundNumber;
    }

    /**
     * Function to get a vector of string representation of any hand passed as argument
     * @param present_hand_card, hand for which String vector representation is required
     * @return, a vector of Strings representing the hand
     */

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

    /**
     * Function to get the sum of total number of wildcards and jokers in the hand
     * @param present_hand, a vector of cards containing the hand for which to check
     * @return, an integer containing total sum of all wildcards and jokers in the hand
     */

    int totalApplicableWildcardsNum(Vector<Card> present_hand){
        int total_applicable_wildcards = 0;
        Vector<String> present_hand_str = handToStr(present_hand);
        Vector<String> temp;
        Iterator value = present_hand_str.iterator();

        while (value.hasNext()){
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

    /**
     * Function to check if the card passed as a parameter is a wildcard
     * @param current_card_str, a string containing the card to check
     * @return a boolean which is true if the parameter is a wildcard
     */

    public boolean checkIfWildcard(String current_card_str) {
        if (Character.isDigit(current_card_str.charAt(0))){
            int number = Character.getNumericValue(current_card_str.charAt(0));
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

    /**
     * Function to check if a card is a joker
     * @param current_card_str, string representation of the card to be checked
     * @return True if the card is a joker
     */

    public boolean checkIfJoker(String current_card_str) {
        if ((current_card_str.charAt(0) == 'J') && Character.isDigit(current_card_str.charAt(1))){
            int number = Character.getNumericValue(current_card_str.charAt(1));

            if (number > 0 && number < 4){
                return true;
            }
        }
        return false;
    }

    /**
     * Function to remove wildcards and jokers from a hand and return the remaining cards
     * @param present_hand, the hand to remove wildcards and jokers from
     * @return, the remaining hand
     */

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

    /**
     * Function to check if a hand is a book
     * @param handToCheck, a Vector of cards which is to be checked
     * @return True if the hand is a book
     */

    private boolean checkBook (Vector<Card> handToCheck){
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

    /**
     * Function to check if a hand is a run
     * @param current_hand_to_check, the hand to be checked
     * @return True if the hand forms a run
     */

    public boolean checkRun(Vector<Card> current_hand_to_check){

        if (current_hand_to_check.size() < 3){
            return false;
        }

        int total_applicable_wildcards = totalApplicableWildcardsNum(current_hand_to_check);
        Vector<String> temp = handWithoutWildcards(current_hand_to_check);

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

    /**
     * Purpose: To find the maximum difference between the minimum and max face values of the vector of integers passed minus the face_values that fall between the min and max.
     * @param face_values, Vector of Integers containing face values of the cards
     * @param size, the size of the vector
     * @return, an integer containing the difference calculated
     */

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

    /**
     * Function overridden in base classes
     *
     */

    public void pickCardHelp(){
        System.out.println("Virtual function pickCardHelp needs to be overwritten in child class");
        //return "";
    }

    /**
     * Function overridden in base classes
     */

    public void pickCard(){
        System.out.println("VIRTUAL FUNCTION PICKCARD");
    }

    /**
     * Function overridden in base classes
     */

    public void dropCard(){
        System.out.println("VIRTUAL FUNCTION DROPCARD");
    }

    /**
     * Function to check if the Player can go out
     * @return True if the player can go out
     */

    public boolean goOut(){
        checkJokercards();
        checkWildcards();
        int total_cards = current_player_hand.size();
        hand_score = calculateSumOfCards(current_player_hand);
        child_returning_smallest_sum.clear();
        bestBookRunCombination(current_player_hand);

        if (hand_score == 0){
            return true;
        }
        return false;
    }

    /**
     * Function to find the best book run combination of cards in the current hand.
     * @param current_hand, the hand for which best combination is to be generated
     */

    private void bestBookRunCombination(Vector<Card> current_hand) {
        Vector<String> current_hand_str = handToStr(current_hand);
        Vector<Vector<Card>> listof_booksandruns_currenthand = generatePossibleCombinations(current_hand_str);

        if (listof_booksandruns_currenthand.size() == 0){
            int score = calculateSumOfCards(current_hand);

            if (score <= hand_score){
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
                    min_branch.clear();
                    min_branch.addAll(recursive_bookrun_hands);
                    System.out.println("MIN_BRANCH IS: "+ recursive_bookrun_hands);
                }

                hand_score = score;

            }
        }

        Iterator value = listof_booksandruns_currenthand.iterator();

        while (value.hasNext()){
            Vector<Card> hand_after_removal = new Vector<Card>();
            Vector<String> temp_hand_after_removal = new Vector<String>(current_hand_str);
            Vector<String> removed_hand = new Vector<String>();
            Vector<Card> inside = ((Vector<Card>) value.next());
            Iterator inside_it = inside.iterator();

            while (inside_it.hasNext()){
                Card current_s = (Card) inside_it.next();
                String current = current_s.cardToString();
                removed_hand.add(current);
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

            recursive_bookrun_hands.add(removed_hand);
            bestBookRunCombination(hand_after_removal);
            recursive_bookrun_hands.remove(removed_hand);
        }
    }

    /**
     * Function to return the best book/run branch combination
     * @return
     */

    public Vector<Vector<String>> getMinBranch(){
        return min_branch;
    }

    /**
     * Function to generate all possible combinations of cards in the current hand
     * @param current_player_hand_str, a vector of String containing the current hand
     * @return Vector of Vector of Cards where each Vector of cards is a possible combination
     */

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

    /**
     * Function to return a list of books and runs in the current hand
     * @param possible_combinations, a list of all possible combinations of cards in the current hand
     * @return, Vector of Vectors where each inside vector contains card objects representing a book or run
     */
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

    /**
     * Function to calculate the sum of face values of cards in the current hand
     * @param remaining_cards, the cards for which sum is to be calculated
     * @return, sum of face values
     */

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
                    int_face -= 14;
                }

                if (face == 'K'){
                    int_face -= 7;
                }
            score += int_face;
            }
        }

        return score;
    }

    /**
     * Function that returns a String of all available books and runs
     * @return a String containing available books and runs
     */

    public String printAvailableBooksandRuns(){
        String return_val = "";
        System.out.println("CHILD RETURNING SMALLEST SUM IS: ");
        System.out.println(child_returning_smallest_sum);
        if (child_returning_smallest_sum.size() == 0){
            return "No books or runs";
        }
        else {
            return_val+= "Books and runs are: \n";
            System.out.println("BOOKS AND RUNS ARE: ");
            Iterator value = child_returning_smallest_sum.iterator();
            while (value.hasNext()){
                //System.out.print((String) value.next()+"   ");
                return_val+= (String)value.next()+"   ";
            }
            return_val+="\n";
        }
        return return_val;
    }

    /**
     * Function to get the hand score
     * @return integer containing the hand score
     */

    public int getHandScore(){
        return hand_score;
    }

    /**
     * Function to get the player hand
     * @return a String containing cards in the hand.
     */

    public String getPlayerHandStr(){
        String hand = "";
        Iterator value = current_player_hand_str.iterator();
        while (value.hasNext()){
            hand += (String) value.next() + " ";

        }
        return hand;
    }

}