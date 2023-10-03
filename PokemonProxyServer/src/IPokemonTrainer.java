import java.io.IOException;

public interface IPokemonTrainer {

    boolean hasAction() throws IOException;
    void setActionStatus(boolean hasAction) throws IOException;

    boolean readyForBattle() throws IOException;

    void receiveMessage(String message) throws IOException;

    void receiveCommand(String command) throws IOException;

    String getName() throws IOException;

}
