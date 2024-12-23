import com.google.gson.Gson;
import com.google.gson.GsonBuilder;

import java.io.BufferedReader;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.util.*;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class JsonGenerator {
    private List<City> cities;
    private List<Warehouse> warehouses;
    private List<List<Double>> matriceDeCout;
    private String inputFileName;

    public JsonGenerator() {
        parseInputFile();

    }

    public String getInputFileName() {
        return inputFileName;
    }

    public void setInputFileName(String inputFileName) {
        this.inputFileName = inputFileName;
    }

    public void parseInputFile() {
        try (BufferedReader reader = new BufferedReader(new FileReader(inputFileName))) {
            String line;
            while ((line = reader.readLine()) != null) {
                line = line.trim(); // Éliminer les espaces inutiles
                if (line.isEmpty()) {
                    continue; // Ignorer les lignes vides
                }

                try {
                    if (line.startsWith("City")) {
                        // Utiliser un regex pour capturer les parties
                        String regex = "ID\\s*=\\s*(\\d+),\\s*Coordinates\\s*=\\s*\\((\\d+),\\s*(\\d+)\\),\\s*Demand\\s*=\\s*(\\d+) units,\\s*Priority\\s*=\\s*(\\w+)";
                        Matcher matcher = Pattern.compile(regex).matcher(line);
                        if (!matcher.find()) {
                            throw new IllegalArgumentException("Invalid City line format: " + line);
                        }

                        // Extraire les données à partir du matcher
                        String id = matcher.group(1);
                        int x = Integer.parseInt(matcher.group(2));
                        int y = Integer.parseInt(matcher.group(3));
                        int demand = Integer.parseInt(matcher.group(4));
                        String priority = matcher.group(5);

                        cities.add(new City(id, new Coordonnes<>(x, y), demand, priority));
                    } else if (line.startsWith("Warehouse")) {
                        // Utiliser un regex pour capturer les parties
                        String regex = "ID\\s*=\\s*(\\d+),\\s*Coordinates\\s*=\\s*\\((\\d+),\\s*(\\d+)\\),\\s*Capacity\\s*=\\s*(\\d+) units";
                        Matcher matcher = Pattern.compile(regex).matcher(line);
                        if (!matcher.find()) {
                            throw new IllegalArgumentException("Invalid Warehouse line format: " + line);
                        }

                        // Extraire les données à partir du matcher
                        String id = matcher.group(1);
                        int x = Integer.parseInt(matcher.group(2));
                        int y = Integer.parseInt(matcher.group(3));
                        int capacity = Integer.parseInt(matcher.group(4));

                        warehouses.add(new Warehouse(id, new Coordonnes<>(x, y), capacity));
                    } else {
                        System.err.println("Unrecognized line format: " + line);
                    }
                } catch (Exception e) {
                    System.err.println("Error processing line: " + line);
                    e.printStackTrace();
                }
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public void setMatriceDeCout(List<List<Double>> matriceDeCout) {
        this.matriceDeCout = matriceDeCout;
    }
    public void generateCostMatrixJson(String fileName) {
        // Build the cost matrix
        List<Map<String, Object>> costMatrix = construireMatriceDeCout();

        // Build the JSON structure
        Map<String, Object> graphRepresentation = Map.of("Graph Representation", Map.of("Cost Matrix", costMatrix));
        Map<String, Object> task1And2 = Map.of("Task 1 and 2", graphRepresentation);

        // Write to JSON file
        try (FileWriter writer = new FileWriter(fileName)) {
            Gson gson = new GsonBuilder().setPrettyPrinting().create();
            gson.toJson(task1And2, writer);
            System.out.println("JSON written to " + fileName);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
    private List<Map<String, Object>> construireMatriceDeCout() {
        List<Map<String, Object>> costMatrix = new ArrayList<>();
        for (int i = 0; i < cities.size(); i++) {
            Map<String, Object> cityCosts = new LinkedHashMap<>();
            cityCosts.put("City", "City " + cities.get(i).getId());
            for (int j = 0; j < warehouses.size(); j++) {
                cityCosts.put("Warehouse " + warehouses.get(j).getId(), matriceDeCout.get(i).get(j));
            }
            costMatrix.add(cityCosts);
        }
        return costMatrix;
    }


    public void generateResourceAllocationJson(String fileName) {
        // Step 1: Perform resource allocation
        new Allocation(); // This updates the cities and warehouses via ReseauSingleton

        // Step 2: Fetch updated data
        List<City> cities = ReseauSingleton.getInstance().getCities();
        List<Warehouse> warehouses = ReseauSingleton.getInstance().getWarehouses();

        // Step 3: Prepare the "Resource Allocation" section
        List<Map<String, Object>> resourceAllocations = new ArrayList<>();
        for (City city : cities) {
            Map<String, Object> allocationEntry = new LinkedHashMap<>();
            allocationEntry.put("City", "City " + city.getId());
            allocationEntry.put("Priority", city.getPriorite());

            if (city.getWarehouses().size() == 1) {
                allocationEntry.put("Allocated", city.getAllocation());
                allocationEntry.put("Warehouse", "Warehouse " + city.getWarehouses().get(0));
            } else {
                List<Map<String, Object>> detailedAllocations = new ArrayList<>();
                for (String warehouseId : city.getWarehouses()) {
                    Map<String, Object> detailedAllocation = new LinkedHashMap<>();
                    double allocatedUnits = calculateAllocatedUnits(city, warehouseId);
                    detailedAllocation.put("Units", allocatedUnits);
                    detailedAllocation.put("Warehouse", "Warehouse " + warehouseId);
                    detailedAllocations.add(detailedAllocation);
                }
                allocationEntry.put("Allocated", detailedAllocations);
            }

            resourceAllocations.add(allocationEntry);
        }

        // Step 4: Prepare the "Remaining Capacities" section
        Map<String, Double> remainingCapacities = new LinkedHashMap<>();
        for (Warehouse warehouse : warehouses) {
            remainingCapacities.put("Warehouse " + warehouse.getId(), warehouse.getCapacite());
        }

        // Step 5: Build the JSON structure
        Map<String, Object> task1And2 = Map.of(
                "Resource Allocation", resourceAllocations,
                "Remaining Capacities", remainingCapacities
        );

        // Step 6: Write to JSON file
        try (FileWriter writer = new FileWriter(fileName)) {
            Gson gson = new GsonBuilder().setPrettyPrinting().create();
            gson.toJson(Map.of("Task 1 and 2", task1And2), writer);
            System.out.println("Resource Allocation JSON written to " + fileName);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }


    private double calculateAllocatedUnits(City city, String warehouseId) {
        double allocatedUnits = 0;
            allocatedUnits = city.getAllocation();
        return allocatedUnits;
    }



    public List<City> getCities() {
        return cities;
    }

    public List<Warehouse> getWarehouses() {
        return warehouses;
    }
}
