import java.io.IOException;
import java.net.*;
import java.util.Scanner;

public class Main {
    public static void main(String[] args) throws IOException {

        Scanner scanner = new Scanner(System.in);
        Socket socket = new Socket("localhost", 9090);

        System.out.println("Enter your Pokemon Trainer name: ");
        String pokemonTrainerName = scanner.nextLine();
        PokemonTrainer pokemonTrainer = new PokemonTrainer(pokemonTrainerName, new Pokemon("Pikachu", 100, 20));

        PokemonArenaProxy pokemonArenaProxy = new PokemonArenaProxy(socket);

        boolean isRunning = true;

        while (isRunning) {
            System.out.println(">> 1. Start Battle; 2. Enter Arena ; 3. Leave Arena ; 4. Disconnect");
            String input = scanner.nextLine();
            switch (input) {
                case "1":
                    pokemonTrainer.setReadyForBattle(true);
                    if (pokemonArenaProxy.startBattle()) {
                        battleMenu(pokemonArenaProxy, pokemonTrainer);
                    }
                    break;
                case "2":
                    pokemonArenaProxy.addPokemonTrainer(pokemonTrainer);
                    break;
                case "3":
                    pokemonArenaProxy.removePokemonTrainer(pokemonTrainer);
                    break;
                case "4":
                    pokemonArenaProxy.endConnection();
                    isRunning = false;
                    break;
                default:
                    System.out.println("Invalid input");
                    break;
            }

        }

    }

    private static void battleMenu(PokemonArenaProxy pokemonArenaProxy, PokemonTrainer pokemonTrainer) throws IOException {
        Scanner scanner = new Scanner(System.in);
        while (true){
            System.out.println(">> 1. Attack ; 2. Dodge ;  " + pokemonTrainer.hasAction());
            String input = scanner.nextLine();
            switch (input) {
                case "1":
                    pokemonArenaProxy.sendCommand("1. Attack", pokemonTrainer);
                    break;
                case "2":
                    pokemonArenaProxy.sendCommand("2. Dodge", pokemonTrainer);
                    break;
                default:
                    System.out.println("Invalid input");
                    break;
            }
        }
    }
}