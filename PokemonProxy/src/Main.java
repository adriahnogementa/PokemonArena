import java.io.IOException;
import java.net.*;
import java.util.Scanner;

public class Main {
    public static void main(String[] args) throws IOException {

        Scanner scanner = new Scanner(System.in);
        Socket socket = new Socket("localhost", 9090);

        System.out.println("Enter your Pokemon Trainer name: ");
        String pokemonTrainerName = scanner.nextLine();
        PokemonTrainer pokemonTrainer = new PokemonTrainer(pokemonTrainerName, new PokemonTeam());

        PokemonArenaProxy pokemonArenaProxy = new PokemonArenaProxy(socket, pokemonTrainer);

        boolean isRunning = true;

        while (isRunning) {
         System.out.println("1. Send Command ; 2. Enter Arena ; 3. Leave Arena ; 4. Disconnect");
        String input = scanner.nextLine();
        switch (input) {
            case "1":
                System.out.println("Enter the command");
                String command = scanner.nextLine();
                pokemonArenaProxy.sendCommand(command, pokemonTrainer);
                break;
            case "2":
                pokemonArenaProxy.enterPokemonArena(pokemonTrainer);
                break;
            case "3":
                pokemonArenaProxy.exitPokemonArena(pokemonTrainer);
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
}