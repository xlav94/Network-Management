import com.google.gson.Gson;
import com.google.gson.GsonBuilder;

import java.io.FileWriter;
import java.io.IOException;
import java.util.*;

public class JsonGenerator {
    private List<City> cities;
    private List<Warehouse> warehouses;
    private List<List<Double>> matriceDeCout;

    public JsonGenerator() { }

    public Map<String, Object> getCostMatrixData() {
        cities = ReseauSingleton.getInstance().getCities();
        warehouses = ReseauSingleton.getInstance().getWarehouses();
        matriceDeCout = ReseauSingleton.getInstance().getMatriceDeCout();

        List<Map<String, Object>> costMatrix = new ArrayList<>();
        for (int i = 0; i < cities.size(); i++) {
            Map<String, Object> cityCosts = new LinkedHashMap<>();
            cityCosts.put("City", "City " + cities.get(i).getId());
            for (int j = 0; j < warehouses.size(); j++) {
                cityCosts.put("Warehouse " + warehouses.get(j).getId(), matriceDeCout.get(i).get(j));
            }
            costMatrix.add(cityCosts);
        }
        return Map.of("Cost Matrix", costMatrix);
    }

    public Map<String, Object> getResourceAllocationData() {
        Allocation allocation = new Allocation();

        List<Map<String, Object>> resourceAllocations = new ArrayList<>();
        for (City city : ReseauSingleton.getInstance().getCities()) {
            Map<String, Object> allocationEntry = new LinkedHashMap<>();
            allocationEntry.put("City", "City " + city.getId());
            allocationEntry.put("Priority", city.getPriorite());

            if (city.getWarehouseAllocations().size() == 1) {
                for (Map.Entry<String, Double> entry : city.getWarehouseAllocations().entrySet()) {
                    allocationEntry.put("Allocated", entry.getValue());
                    allocationEntry.put("Warehouse", "Warehouse " + entry.getKey());
                }
            } else {
                List<Map<String, Object>> detailedAllocations = new ArrayList<>();
                for (Map.Entry<String, Double> entry : city.getWarehouseAllocations().entrySet()) {
                    Map<String, Object> allocationDetail = new LinkedHashMap<>();
                    allocationDetail.put("Units", entry.getValue());
                    allocationDetail.put("Warehouse", "Warehouse " + entry.getKey());
                    detailedAllocations.add(allocationDetail);
                }
                allocationEntry.put("Allocated", detailedAllocations);
            }

            resourceAllocations.add(allocationEntry);
        }

        Map<String, Object> result = new LinkedHashMap<>();
        result.put("Resource Allocations", resourceAllocations);

        return result;
    }





    public Map<String, Object> getResourceRedistributionData(List<Map<String, Object>> transfers, Map<String, Double> finalResourceLevels) {
        Map<String, Object> resourceRedistribution = new LinkedHashMap<>();
        resourceRedistribution.put("Transfers", transfers);
        resourceRedistribution.put("Final Resource Levels", finalResourceLevels);
        return Map.of("Resource Redistribution", resourceRedistribution);
    }

    public Map<String, Object> getDynamicResourceSharingData() {
        DynamicResourceSharing dynamicResourceSharing = new DynamicResourceSharing();
        Map<String, Object> dynamicResource= new LinkedHashMap<>();
        dynamicResource.put("Initial Clusters", dynamicResourceSharing.getInitialClusters());
        dynamicResource.put("Merging Steps", dynamicResourceSharing.getMergingSteps());
        dynamicResource.put("Cluster Membership", dynamicResourceSharing.getClusterMembershipAfterMerging());
        //dynamicResource.put("Final Clusters", dynamicResourceSharing.getClusters());
        dynamicResource.put("Queries", dynamicResourceSharing.getQueries());
        return Map.of("Dynamic Resource Sharing", dynamicResource);

    }

    public void generateCompleteJson(String fileName, List<Map<String, Object>> transfers, Map<String, Double> finalResourceLevels, Map<String, Double> remainingCapacitiesAfterAllocation) {
        Gson gson = new GsonBuilder().setPrettyPrinting().create();
        Map<String, Object> fullData = new LinkedHashMap<>();
        fullData.put("Task 1 and 2", Map.of("Graph Representation", getCostMatrixData()));
        fullData.put("Resource Allocation", getResourceAllocationData());
        fullData.put("Remaining Capacities After Allocation", remainingCapacitiesAfterAllocation);
        fullData.put("Task 3", getResourceRedistributionData(transfers, finalResourceLevels));
        fullData.put("Task 4", getDynamicResourceSharingData());

        try (FileWriter writer = new FileWriter(fileName)) {
            gson.toJson(fullData, writer);
            System.out.println("Complete JSON written to " + fileName);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public void setCities(List<City> cities) {
        this.cities = cities;
    }

    public void setWarehouses(List<Warehouse> warehouses) {
        this.warehouses = warehouses;
    }
}
