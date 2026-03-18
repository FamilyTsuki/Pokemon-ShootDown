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
        if (allAvailableMoves.isEmpty()) loadAllMoves();
        
        List<Pokemon> pokemons = new ArrayList<>();
        try (InputStream is = PokemonDataManager.class.getResourceAsStream(pokemonPath);
            BufferedReader br = new BufferedReader(new InputStreamReader(is))) {
            
            if (is == null) return pokemons;
            br.readLine();

            String line;
            while ((line = br.readLine()) != null && pokemons.size() < 60) {
                parseAndAddPokemon(line, pokemons);
            }
        } catch (Exception e) { e.printStackTrace(); }

        System.out.println("[INFO] " + pokemons.size() + " Pokémon chargés.");
        return pokemons;
    }

    private static void parseAndAddPokemon(String line, List<Pokemon> list) {
        if (line.trim().isEmpty()) return;
        try {
            String[] data = line.split(",");
            Pokemon p = createPokemonFromData(data);
            list.add(p);
        } catch (Exception e) {
            System.err.println("Erreur sur la ligne : " + line + " -> " + e.getMessage());
        }
    }

    private static Pokemon createPokemonFromData(String[] data) {
        int id = Integer.parseInt(data[0].trim());
        String name = data[1].trim();
        int hp = Integer.parseInt(data[2].trim());
        int atk = Integer.parseInt(data[3].trim());
        int def = Integer.parseInt(data[4].trim());
        int spd = Integer.parseInt(data[5].trim());

        PokemonType[] types = parseTypes(data[6], (data.length > 7) ? data[7] : null);
        
        int spAtk = Integer.parseInt(data[8].trim());
        int spDef = Integer.parseInt(data[9].trim());
        String moveStr = (data.length > 10) ? data[10].trim() : "Charge";

        return new Pokemon(id, name, hp, atk, def, spd, types, 
                        new Attack[4], null, filterMovesFromLibrary(moveStr), spAtk, spDef);
    }

    private static PokemonType[] parseTypes(String t1, String t2) {
        PokemonType type1 = PokemonType.valueOf(t1.trim().toUpperCase());
        if (t2 != null && !t2.trim().isEmpty() && !t2.trim().equalsIgnoreCase("NULL")) {
            return new PokemonType[]{type1, PokemonType.valueOf(t2.trim().toUpperCase())};
        }
        return new PokemonType[]{type1};
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