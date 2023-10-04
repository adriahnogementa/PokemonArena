import java.io.IOException;

public interface IPokemonTrainer {

    boolean readyForBattle() throws IOException;
    boolean hasAction();
    void setActionStatus(boolean hasAction);
    void receiveMessage(String message) throws IOException;
    //TODO: Remove this method

    void receiveCommand(String command) throws IOException;
    boolean pokemonIsAlive();

    void takeDamage(int damage);
    int getAttackDamage();
    int getInitiative();
    int getDodgeChance();

    String getName();


}
