//player interface to define the necessary methods to impliment a player
//get colour and name are most important. The score methods are future proofing

import java.awt.Color;

public interface PlayerInterface {

    Color getColour();
    String getName();
    int getScore();
    void incrementScore();

}
