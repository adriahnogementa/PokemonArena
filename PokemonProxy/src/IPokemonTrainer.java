import java.io.IOException;

public interface IPokemonTrainer {

    boolean readyForBattle() throws IOException;
    boolean hasAction();
    void setActionStatus(boolean hasAction);
    void receiveMessage(String message) throws IOException;
    void receiveCommand(String command) throws IOException;

    String getName();


}
