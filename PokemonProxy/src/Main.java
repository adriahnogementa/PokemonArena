import java.io.IOException;
import java.net.*;
import java.util.Scanner;

public class Main {
    public static void main(String[] args) throws IOException {

        PokemonTeam pokemonTeam = new PokemonTeam();
        Scanner scanner = new Scanner(System.in);
        System.out.println("Welcome to Pokemon Arena!");
        boolean invalidIpadress = true;
        Socket socket = null;
        while (invalidIpadress) {
            try {
                System.out.println("Write the IP address of the Pokemon Arena Server: ");
                String ipAddress = scanner.nextLine();
                socket = new Socket(ipAddress, 9090);
                invalidIpadress = false;
            } catch (ConnectException | UnknownHostException e) {
                System.err.println("Invalid IP address");
                invalidIpadress = true;
            }
        }
        System.out.println("Enter your Pokemon Trainer name: ");
        String pokemonTrainerName = scanner.nextLine();
        System.out.println("Welcome " + pokemonTrainerName);
        boolean notChosenPokemon = true;
        String pokeNr = "";
        while (notChosenPokemon) {
            System.out.println("Choose your Pokemon: ");
            System.out.println(pokemonTeam.toString());
            pokeNr = scanner.nextLine();
            if (Integer.parseInt(pokeNr) > 0 && Integer.parseInt(pokeNr) < 6) {
                notChosenPokemon = false;
            } else {
                System.err.println("Invalid input. Only numbers between 1 and 5 are allowed \n");
            }
        }
        PokemonTrainer pokemonTrainer = new PokemonTrainer(pokemonTrainerName, pokemonTeam.getPokemon(Integer.parseInt(pokeNr)));

        PokemonArenaProxy pokemonArenaProxy = new PokemonArenaProxy(socket);

        boolean isRunning = true;

        while (isRunning) {
            System.out.println(">> 1. Start Battle; 2. Enter Arena; 3. Leave Arena; 4. Disconnect");
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
                    System.err.println("Invalid input");
                    break;
            }

        }

    }

    private static void battleMenu(PokemonArenaProxy pokemonArenaProxy, PokemonTrainer pokemonTrainer) throws IOException {
        Scanner scanner = new Scanner(System.in);
        while (true) {
            System.out.println(">> 1.Attack ; 2.Dodge ; 3.Show my Pokemon ;4. Show Enemy's Pokemon ;5. Show Enemy's HP");
            String input = scanner.nextLine();
            switch (input) {
                case "1":
                    pokemonArenaProxy.sendCommand("1. Attack", pokemonTrainer);
                    break;
                case "2":
                    pokemonArenaProxy.sendCommand("2. Dodge", pokemonTrainer);
                    break;
                case "3":
                    System.out.println(pokemonTrainer.getPokemon().toString());
                    break;
                case "4":
                    System.out.println(pokemonArenaProxy.getEnemysPokemon(pokemonTrainer));
                    break;
                case "5":
                    System.out.println(pokemonArenaProxy.getEnemysPokemonHealth(pokemonTrainer));
                    break;
                default:
                    System.err.println("Invalid input");
                    break;
            }
        }
    }
}