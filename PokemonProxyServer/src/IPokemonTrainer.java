import java.io.IOException;

public interface IPokemonTrainer {

    void receiveCommand(String command) throws IOException;

    String name();
}
