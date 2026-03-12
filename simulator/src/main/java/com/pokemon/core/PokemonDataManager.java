package com.pokemon.core;

import com.pokemon.models.*;
import java.io.*;
import java.util.*;

public class PokemonDataManager {
    private static List<Attack> moveLib = new ArrayList<>();

    public static List<Pokemon> loadPokemonsFromCSV(String path) {
        if (moveLib.isEmpty()) loadAllMoves();
        List<Pokemon> list = new ArrayList<>();
        try (InputStream is = PokemonDataManager.class.getResourceAsStream(path);
             BufferedReader br = new BufferedReader(new InputStreamReader(is))) {
            if (is == null) return list;
            br.readLine();
            String line;
            while ((line = br.readLine()) != null && list.size() < 60) {
                parseLine(line, list);
            }
        } catch (Exception e) { e.printStackTrace(); }
        return list;
    }

    private static void parseLine(String line, List<Pokemon> list) {
        if (line.trim().isEmpty()) return;
        try {
            list.add(createPokemonFromData(line.split(",")));
        } catch (Exception e) {
            System.err.println("Line error: " + line);
        }
    }

    private static Pokemon createPokemonFromData(String[] d) {
        int id = Integer.parseInt(d[0].trim());
        String name = d[1].trim();
        int hp = Integer.parseInt(d[2].trim());
        int atk = Integer.parseInt(d[3].trim());
        int def = Integer.parseInt(d[4].trim());
        int spd = Integer.parseInt(d[5].trim());
        
        PokemonType[] types = parseTypes(d[6], (d.length > 7) ? d[7] : null);
        int sAtk = Integer.parseInt(d[8].trim());
        int sDef = Integer.parseInt(d[9].trim());
        String moves = (d.length > 10) ? d[10].trim() : "Charge";

        return new Pokemon(id, name, hp, atk, def, spd, types, 
            new Attack[4], null, filterMoves(moves), sAtk, sDef);
    }

    private static PokemonType[] parseTypes(String t1, String t2) {
        PokemonType type1 = PokemonType.valueOf(t1.trim().toUpperCase());
        if (t2 != null && !t2.trim().isEmpty() && !t2.trim().equalsIgnoreCase("NULL")) {
            return new PokemonType[]{type1, 
                PokemonType.valueOf(t2.trim().toUpperCase())};
        }
        return new PokemonType[]{type1};
    }

    private static void loadAllMoves() {
        String path = "/com/pokemon/data/moves.csv";
        try (InputStream is = PokemonDataManager.class.getResourceAsStream(path);
             BufferedReader br = new BufferedReader(new InputStreamReader(is))) {
            if (is == null) return;
            br.readLine();
            String line;
            while ((line = br.readLine()) != null) {
                Attack a = MoveDataManager.createAttackFromData(line.split(","));
                if (a != null) moveLib.add(a);
            }
        } catch (Exception e) { e.printStackTrace(); }
    }

    private static Attack[] filterMoves(String movesStr) {
        List<Attack> found = new ArrayList<>();
        for (String n : movesStr.split(";")) {
            moveLib.stream()
                .filter(a -> a.getName().equalsIgnoreCase(n.trim()))
                .findFirst().ifPresent(found::add);
        }
        if (found.isEmpty() && !moveLib.isEmpty()) found.add(moveLib.get(0));

        Attack[] res = new Attack[10];
        for (int i = 0; i < Math.min(found.size(), 10); i++) res[i] = found.get(i);
        return res;
    }
}