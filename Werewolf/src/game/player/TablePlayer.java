package game.player;

import javafx.beans.property.*;

/**
 * A class representing a Player.
 *
 * A Player saves his name, a status (alive or dead), a role, a vote status ( true or false)  and the amount of
 * votes against him.
 */
public class TablePlayer {
    private final SimpleStringProperty firstName = new SimpleStringProperty("");
    private final SimpleStringProperty status = new SimpleStringProperty("");
    private final SimpleStringProperty role = new SimpleStringProperty("");
    private final SimpleIntegerProperty voteAgainst = new SimpleIntegerProperty(0);
    private final SimpleBooleanProperty hasVoted = new SimpleBooleanProperty(false);

    public TablePlayer() {
        this("", "", "", 0, false);
    }

    /**
     * Constructor for creating an object of this class
     * @param firstName : Name of this player
     * @param status    : status of this player ( alive or dead )
     * @param role      : Role of this player ( Seer, Human, Werewolf or Witch )
     * @param voteAgainst   : The amount of votes against this player
     * @param hasVoted      : Status of this player's vote  ( true or false )
     */
    public TablePlayer(String firstName, String status, String role, Integer voteAgainst, Boolean hasVoted) {
        setFirstName(firstName);
        setStatus(status);
        setRole(role);
        setVoteAgainst(voteAgainst);
        setHasVoted(hasVoted);
    }

    /**
     * get name of this player
     *
     * @return : name as string
     */
    public String getFirstName() {
        return firstName.get();
    }

    /**
     * set name for this player
     * @param fName : name to be given
     */
    public void setFirstName(String fName) {
        firstName.set(fName);
    }

    /**
     * Get status of this player
     * @return : status of this player
     */
    public String getStatus() {
        return status.get();
    }

    /**
     * set status for this player
     * @param fName : status to be given as string
     */
    public void setStatus(String fName) {
        status.set(fName);
    }

    /**
     * Get the role of this player
     *
     * @return : Role of this player
     */
    public String getRole() {
        return role.get();
    }

    /**
     * Set the role for this player
     * @param fName : role to be given
     */
    public void setRole(String fName) {
        role.set(fName);
    }

    /**
     * set the vote against this player
     *
     * @return : amount of vote against this player
     */
    public Integer getVoteAgainst() {
        return voteAgainst.get();
    }

    /**
     * set vote against this player
     * @param fVote : vote to be set as integer
     */
    public void setVoteAgainst(Integer fVote) {
        voteAgainst.set(fVote);
    }

    /**
     * Get vote status for this player
     * @return : current vote status as boolean
     */
    public Boolean getHasVoted() {
        return hasVoted.get();
    }

    /**
     * set vote status for this player
     * @param fVote : true of false depends if this player has voted or not
     */
    public void setHasVoted(Boolean fVote) {
        hasVoted.set(fVote);
    }

    /**
     * Return this player as String with all of his attribute
     * @return :
     */
    public String toString(){
        return "name "+this.getFirstName()+" status "+this.getStatus()+" hasVote "+String.valueOf(this.getHasVoted()+" voteAgainst "+this.getVoteAgainst()+ " role "+ this.getRole());
    }
}
