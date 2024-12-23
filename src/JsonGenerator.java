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

    }



    public String getInputFileName() {
        return inputFileName;
    }

    public void setInputFileName(String inputFileName) {
        this.inputFileName = inputFileName;
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
        new Allocation(); // Perform resource allocation

        List<City> cities = ReseauSingleton.getInstance().getCities();

        List<Map<String, Object>> resourceAllocations = new ArrayList<>();
        for (City city : cities) {
            Map<String, Object> allocationEntry = new LinkedHashMap<>();
            allocationEntry.put("City", "City " + city.getId());
            allocationEntry.put("Priority", city.getPriorite());

            List<Map<String, Object>> detailedAllocations = new ArrayList<>();
            for (Map.Entry<String, Double> entry : city.getWarehouseAllocations().entrySet()) {
                Map<String, Object> allocationDetail = new LinkedHashMap<>();
                allocationDetail.put("Units", entry.getValue());
                allocationDetail.put("Warehouse", "Warehouse " + entry.getKey());
                detailedAllocations.add(allocationDetail);
            }
            allocationEntry.put("Allocated", detailedAllocations);

            resourceAllocations.add(allocationEntry);
        }

        Map<String, Object> task1And2 = Map.of("Resource Allocation", resourceAllocations);

        try (FileWriter writer = new FileWriter(fileName)) {
            Gson gson = new GsonBuilder().setPrettyPrinting().create();
            gson.toJson(Map.of("Task 1 and 2", task1And2), writer);
            System.out.println("Resource Allocation JSON written to " + fileName);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public void generateResourceRedistributionJson(String fileName, List<Map<String, Object>> transfers, Map<String, Double> finalResourceLevels) {
        // Ensure the transfers list contains multiple entries
        if (transfers == null || transfers.isEmpty()) {
            System.out.println("No transfers recorded.");
            return;
        }

        // Prepare the "Resource Redistribution" structure
        Map<String, Object> resourceRedistribution = Map.of(
                "Transfers", transfers,
                "Final Resource Levels", finalResourceLevels
        );

        // Prepare the "Task 3" structure
        Map<String, Object> task3 = Map.of("Task 3", Map.of("Resource Redistribution", resourceRedistribution));

        // Write the JSON to a file
        try (FileWriter writer = new FileWriter(fileName)) {
            Gson gson = new GsonBuilder().setPrettyPrinting().create();
            gson.toJson(task3, writer);
            System.out.println("Resource Redistribution JSON written to " + fileName);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }


    public void generateDynamicResourceSharingJson(String fileName) {
        DynamicResourceSharing dynamicResourceSharing = new DynamicResourceSharing();

        // Build JSON structure for Task 4 using LinkedHashMap
        Map<String, Object> dynamicResourceSharingData = new LinkedHashMap<>();
        dynamicResourceSharingData.put("Initial Clusters", dynamicResourceSharing.getClusters());
        dynamicResourceSharingData.put("Merging Steps", dynamicResourceSharing.getMergingSteps());
        dynamicResourceSharingData.put("Queries", dynamicResourceSharing.getQueries());

        Map<String, Object> task4 = new LinkedHashMap<>();
        task4.put("Task 4", Map.of("Dynamic Resource Sharing", dynamicResourceSharingData));

        // Write to JSON
        try (FileWriter writer = new FileWriter(fileName)) {
            Gson gson = new GsonBuilder().setPrettyPrinting().create();
            gson.toJson(task4, writer);
            System.out.println("Dynamic Resource Sharing JSON written to " + fileName);
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

    public void setCities(List<City> cities) {
        this.cities = cities;
    }

    public void setWarehouses(List<Warehouse> warehouses) {
        this.warehouses = warehouses;
    }
}

