import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;
import java.util.*;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class InputParser {
    private List<City> cities;
    private List<Warehouse> warehouses;

    public InputParser() {
        cities = new ArrayList<>();
        warehouses = new ArrayList<>();
    }

    public void parseInputFile(String filePath) {
        try (BufferedReader reader = new BufferedReader(new FileReader(filePath))) {
            String line;
            boolean isCities = false, isWarehouses = false;
            while ((line = reader.readLine()) != null) {
                line = line.trim();
                if (line.isEmpty()) {
                    continue; // skip lignes vide
                }
                if (line.startsWith("Cities:")) {
                    isCities = true;
                    isWarehouses = false;
                } else if (line.startsWith("Warehouses:")) {
                    isWarehouses = true;
                    isCities = false;
                } else if (isCities) {
                    parseCity(line);
                } else if (isWarehouses) {
                    parseWarehouse(line);
                } else {
                    System.err.println("Unrecognized line format: " + line);
                }
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    // utilisation de regex : ce site nous a aidé à naviguer l'utilisation
    // https://www.vogella.com/tutorials/JavaRegularExpressions/article.html
    private void parseCity(String line) {
        String regex = "City \\w+: ID\\s*=\\s*(\\d+),\\s*Coordinates\\s*=\\s*\\((\\d+),\\s*(\\d+)\\),\\s*Demand\\s*=\\s*(\\d+) units,\\s*Priority\\s*=\\s*(\\w+)";
        Matcher matcher = Pattern.compile(regex).matcher(line);
        if (matcher.find()) {
            String id = matcher.group(1);
            int x = Integer.parseInt(matcher.group(2));
            int y = Integer.parseInt(matcher.group(3));
            int demande = Integer.parseInt(matcher.group(4));
            String priority = matcher.group(5);
            cities.add(new City(id, new Coordonnes<>(x, y), demande, priority));
            //System.out.println("IND " + id);
        } else {
            System.err.println("Invalid City line format: " + line);
        }
    }

    private void parseWarehouse(String line) {
        String regex = "Warehouse \\w+: ID\\s*=\\s*(\\d+),\\s*Coordinates\\s*=\\s*\\((\\d+),\\s*(\\d+)\\),\\s*Capacity\\s*=\\s*(\\d+) units";
        Matcher matcher = Pattern.compile(regex).matcher(line);
        if (matcher.find()) {
            String id = matcher.group(1);
            int x = Integer.parseInt(matcher.group(2));
            int y = Integer.parseInt(matcher.group(3));
            int capacite = Integer.parseInt(matcher.group(4));
            warehouses.add(new Warehouse(id, new Coordonnes<>(x, y), capacite));
        } else {
            System.err.println("Invalid Warehouse line format: " + line);
        }
    }
    public void settingFromParser(JsonGenerator jsonGenerator) {
        jsonGenerator.setCities(cities);
        jsonGenerator.setWarehouses(warehouses);
    }

    public List<City> getCities() {
        return cities;
    }

    public List<Warehouse> getWarehouses() {
        return warehouses;
    }
}
