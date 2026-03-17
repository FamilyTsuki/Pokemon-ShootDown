package com.pokemon.core;

import com.pokemon.models.Pokemon;
import com.pokemon.models.PokemonType;
import com.pokemon.models.Attack;
import java.io.*;
import java.util.ArrayList;
import java.util.List;

public class PokemonDataManager {

    private static List<Attack> allAvailableMoves = new ArrayList<>();

    public static List<Pokemon> loadPokemonsFromCSV(String pokemonPath) {
        if (allAvailableMoves.isEmpty()) {
            loadAllMoves();
        }

        List<Pokemon> pokemons = new ArrayList<>();
        
        try (InputStream is = PokemonDataManager.class.getResourceAsStream(pokemonPath)) {
            if (is == null) return pokemons;

            try (BufferedReader br = new BufferedReader(new InputStreamReader(is))) {
                String line;
                br.readLine();

                while ((line = br.readLine()) != null) {
                    if (line.trim().isEmpty()) continue;
                    String[] data = line.split(",");
                    
                    try {
                        int id = Integer.parseInt(data[0].trim());
                        String name = data[1].trim();
                        int hp = Integer.parseInt(data[2].trim());
                        int attack = Integer.parseInt(data[3].trim());
                        int defense = Integer.parseInt(data[4].trim());
                        int speed = Integer.parseInt(data[5].trim());

                        PokemonType t1 = PokemonType.valueOf(data[6].trim().toUpperCase());
                        PokemonType[] types;
                        if (data.length > 7 && !data[7].trim().isEmpty() && !data[7].trim().equalsIgnoreCase("NULL")) {
                            types = new PokemonType[]{t1, PokemonType.valueOf(data[7].trim().toUpperCase())};
                        } else {
                            types = new PokemonType[]{t1};
                        }

                        int spAtk = Integer.parseInt(data[8].trim());
                        int spDef = Integer.parseInt(data[9].trim());

                        String movesListString = (data.length > 10) ? data[10].trim() : "Charge";

                        Attack[] learableMoves = filterMovesFromLibrary(movesListString);

                        pokemons.add(new Pokemon(
                            id, name, hp, attack, defense, speed, 
                            types, new Attack[4], null, learableMoves, spAtk, spDef
                        ));
                        
                    } catch (Exception e) {
                        System.err.println("Erreur sur le Pokémon : " + line + " -> " + e.getMessage());
                    }
                }
            }
        } catch (Exception e) { e.printStackTrace(); }
        return pokemons;
    }

    private static void loadAllMoves() {
        try (InputStream is = PokemonDataManager.class.getResourceAsStream("/com/pokemon/data/moves.csv")) {
            if (is == null) {
                System.err.println("Fichier moves.csv introuvable !");
                return;
            }
            try (BufferedReader br = new BufferedReader(new InputStreamReader(is))) {
                String line;
                br.readLine(); 
                while ((line = br.readLine()) != null) {
                    String[] d = line.split(",");
                    Attack atk = MoveDataManager.createAttackFromData(d);
                    if (atk != null) allAvailableMoves.add(atk);
                }
            }
        } catch (Exception e) { e.printStackTrace(); }
    }

    private static Attack[] filterMovesFromLibrary(String movesListString) {
        List<Attack> found = new ArrayList<>();
        String[] names = movesListString.split(";");

        for (String n : names) {
            String targetName = n.trim();
            for (Attack libAtk : allAvailableMoves) {
                if (libAtk.getName().equalsIgnoreCase(targetName)) {
                    found.add(libAtk);
                    break;
                }
            }
        }

        if (found.isEmpty() && !allAvailableMoves.isEmpty()) {
            found.add(allAvailableMoves.get(0)); 
        }

        Attack[] result = new Attack[10];
        for (int i = 0; i < Math.min(found.size(), 10); i++) {
            result[i] = found.get(i);
        }
        return result;
    }
}