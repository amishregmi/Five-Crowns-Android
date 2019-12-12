/************************************************************
 * Name: Amish Regmi                                        *
 * Project: Project 3, Five Crowns Android                  *
 * Class: OPL Fall 19                                       *
 * Date: 12/11/2019                                         *
 ************************************************************/

package aregmi.ramapo.edu.five_crowns.model.setup;

public class Card {

    private String face;
    private String suit;

    /**
     * Constructor for Card
     */

    public Card(){

    }

    /**
     *
     * @param face, String containing the face to set for the card
     */

    public void setFace(String face){
        this.face = face;
    }

    /**
     *
     * @param suit, String containing the suit to set for the card
     */

    public void setSuit(String suit){
        this.suit = suit;
    }

    /**
     *
     * @param face, the face to set
     * @param suit, the suit to set
     */

    public Card(String face, String suit){
        this.face = face;
        this.suit = suit;
    }

    /**
     *
     * @return String containing face of the card
     */

    public String getFace() {
        return face;
    }

    /**
     *
     * @return String containing suit of the card
     */

    public String getSuit(){
        return suit;
    }

    /**
     *
     * @return String representation of the card object
     */

    public String cardToString(){
        return face+suit;
    }

}

