import java.io.IOException;

public interface IPokemonTrainer {

    boolean hasAction() throws IOException;

    void setActionStatus(boolean hasAction) throws IOException;

    boolean readyForBattle() throws IOException;

    void receiveMessage(String message) throws IOException;

    boolean pokemonIsAlive() throws IOException;

    void takeDamage(int damage) throws IOException;

    int getAttackDamage() throws IOException;

    int getInitiative() throws IOException;

    int getDodgeChance() throws IOException;

    String getName() throws IOException;

    String getPokemonName() throws IOException;
}
