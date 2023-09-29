public class PokemonTrainer implements IPokomonTrainer{

    private final String name;
    public PokemonTrainer(String name) {
        this.name = name;
    }

    @Override
    public void requestCommand(String command) {

    }

    @Override
    public String getName() {
        return this.name;
    }
}
