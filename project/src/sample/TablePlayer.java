package src.sample;

import javafx.beans.property.*;

public class TablePlayer {
    private final SimpleStringProperty firstName = new SimpleStringProperty("");
    private final SimpleStringProperty status = new SimpleStringProperty("");
    private final SimpleStringProperty role = new SimpleStringProperty("");
    private final SimpleIntegerProperty voteAgainst = new SimpleIntegerProperty(0);
    private final SimpleBooleanProperty hasVoted = new SimpleBooleanProperty(false);

    public TablePlayer() {
        this("", "", "", 0, false);
    }

    public TablePlayer(String firstName, String status, String role, Integer voteAgainst, Boolean hasVoted) {
        setFirstName(firstName);
        setStatus(status);
        setRole(role);
        setVoteAgainst(voteAgainst);
        setHasVoted(hasVoted);
    }

    public String getFirstName() {
        return firstName.get();
    }

    public void setFirstName(String fName) {
        firstName.set(fName);
    }


    public String getStatus() {
        return status.get();
    }

    public void setStatus(String fName) {
        status.set(fName);
    }

    public String getRole() {
        return role.get();
    }

    public void setRole(String fName) {
        role.set(fName);
    }

    public Integer getVoteAgainst() {
        return voteAgainst.get();
    }

    public void setVoteAgainst(Integer fVote) {
        voteAgainst.set(fVote);
    }

    public Boolean getHasVoted() {
        return hasVoted.get();
    }

    public void setHasVoted(Boolean fVote) {
        hasVoted.set(fVote);
    }
}
