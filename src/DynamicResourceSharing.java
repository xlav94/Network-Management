import java.util.*;

public class DynamicResourceSharing {
    private List<City> cities;
    private Map<String, String> parent;
    private Map<String, String> initialClusters; // capture of initial clusters
    private List<Map<String, Object>> mergingSteps;
    private List<Map<String, String>> queries;

    public DynamicResourceSharing() {
        this.cities = ReseauSingleton.getInstance().getCities();
        this.cities.sort(Comparator.comparingInt(city -> Integer.parseInt(city.getId())));
        this.parent = new HashMap<>();
        this.initialClusters = new LinkedHashMap<>();
        this.mergingSteps = new ArrayList<>();
        this.queries = new ArrayList<>();

        initializeClusters();
        saveInitialClusters(); // capture of initial clusters
        performMergingSteps(); // merging steps

        addQueries();
        //System.out.println("Final Clusters: " + getClusters());
    }

    private void initializeClusters() {
        for (City city : cities) {
            parent.put(city.getId(), city.getId()); // each city starts in its unique cluster
        }
    }

    private void saveInitialClusters() {
        // sort by id and store in the initialClusters map
        for (City city : cities) {
            initialClusters.put("City " + city.getId(), "Cluster " + city.getId());
        }
    }

    private String find(String cityId) {
        if (!parent.get(cityId).equals(cityId)) {
            parent.put(cityId, find(parent.get(cityId)));
        }
        return parent.get(cityId);
    }

    private void union(String cityId1, String cityId2) {
        String root1 = find(cityId1);
        String root2 = find(cityId2);
        if (!root1.equals(root2)) {
            parent.put(root2, root1); // merge clusters
        }
    }

    private void performMergingSteps() {
        // grouping of cities by their exact set of warehouses
        Map<Set<String>, List<City>> warehouseSetsToCities = new HashMap<>();
        for (City city : cities) {
            Set<String> warehouseSet = new HashSet<>(city.getWarehouseAllocations().keySet());
            warehouseSetsToCities.computeIfAbsent(warehouseSet, k -> new ArrayList<>()).add(city);}

        // merge cities with the same warehouse set
        for (Map.Entry<Set<String>, List<City>> entry : warehouseSetsToCities.entrySet()) {
            Set<String> warehouseSet = entry.getKey();
            List<City> citiesSharingExactWarehouses = entry.getValue();
            for (int i = 0; i < citiesSharingExactWarehouses.size(); i++) {
                for (int j = i + 1; j < citiesSharingExactWarehouses.size(); j++) {
                    City cityA = citiesSharingExactWarehouses.get(i);
                    City cityB = citiesSharingExactWarehouses.get(j);

                    if (!find(cityA.getId()).equals(find(cityB.getId()))) {
                        // merge the cities
                        union(cityA.getId(), cityB.getId());

                        // record the merge step
                        Map<String, Object> mergeStep = new LinkedHashMap<>();
                        mergeStep.put("Action", "Merge");
                        mergeStep.put("Warehouses", warehouseSet);
                        mergeStep.put("Cities", List.of("City " + cityA.getId(), "City " + cityB.getId()));
                        mergeStep.put("Cluster After Merge", "Cluster " + find(cityA.getId()));
                        mergingSteps.add(mergeStep);
                    }
                }
            }
        }
    }

    private void addQueries() {
        for (int i = 0; i < cities.size(); i++) {
            for (int j = i + 1; j < cities.size(); j++) {
                City cityA = cities.get(i); // First city
                City cityB = cities.get(j); // Second city
                // query string in right order
                String query = "Are City " + cityA.getId() + " and City " + cityB.getId() + " in the same cluster?";
                String result = find(cityA.getId()).equals(find(cityB.getId())) ? "Yes" : "No";
                // for order
                Map<String, String> queryResult = new LinkedHashMap<>();
                queryResult.put("Query", query);
                queryResult.put("Result", result);
                queries.add(queryResult);  // add to queries list
            }
        }
    }

    public Map<String, String> getClusterMembershipAfterMerging() {
        Map<String, String> clusterMembership = new LinkedHashMap<>();
        for (City city : cities) {
            String clusterId = "Cluster " + find(city.getId());
            clusterMembership.put("City " + city.getId(), clusterId);}

        return clusterMembership;
    }


    public Map<String, String> getInitialClusters() {return initialClusters;}

    public Map<String, String> getClusters() {
        Map<String, String> sortedClusters = new LinkedHashMap<>();
        parent.keySet().stream().sorted(Comparator.comparingInt(Integer::parseInt)).forEach(cityId -> sortedClusters.put("City " + cityId, "Cluster " + find(cityId)));
        return sortedClusters;
    }

    public List<Map<String, Object>> getMergingSteps() {
        return mergingSteps;
    }

    public List<Map<String, String>> getQueries() {
        return queries;
    }
}
