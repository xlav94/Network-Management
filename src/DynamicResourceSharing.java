import java.util.*;

public class DynamicResourceSharing {
    private List<City> cities;
    private Map<String, String> parent; // Union-Find parent mapping
    private List<Map<String, Object>> mergingSteps;
    private List<Map<String, String>> queries;

    public DynamicResourceSharing() {
        this.cities = ReseauSingleton.getInstance().getCities();
        this.parent = new HashMap<>();
        this.mergingSteps = new ArrayList<>();
        this.queries = new ArrayList<>();

        // Step 1: Initialize Union-Find
        initializeClusters();

        // Debug: Initial Clusters
        System.out.println("Initial Clusters:");
        printClusters();

        // Step 2: Perform Merging Steps
        performMergingSteps();

        // Debug: Merging Steps
        System.out.println("Merging Steps:");
        for (Map<String, Object> step : mergingSteps) {
            System.out.println(step);
        }

        // Step 3: Add Queries
        addQueries();

        // Debug: Cluster Membership After Merging
        System.out.println("Cluster Membership After Merging:");
        printClusters();

        // Debug: Queries
        System.out.println("Queries:");
        for (Map<String, String> query : queries) {
            System.out.println(query);
        }


    }

    private void initializeClusters() {
        for (City city : cities) {
            parent.put(city.getId(), city.getId()); // Each city is its own parent
        }
    }

    private String find(String cityId) {
        if (!parent.get(cityId).equals(cityId)) {
            parent.put(cityId, find(parent.get(cityId))); // Path compression
        }
        return parent.get(cityId);
    }

    private void union(String cityId1, String cityId2) {
        String root1 = find(cityId1);
        String root2 = find(cityId2);

        if (!root1.equals(root2)) {
            parent.put(root2, root1); // Merge the two clusters
        }
    }

    private void performMergingSteps() {
        if (cities.size() >= 2) {
            City cityA = cities.get(0);
            City cityB = cities.get(1);

            // Union the two cities
            union(cityA.getId(), cityB.getId());

            // Record the merge
            Map<String, Object> mergeStep = new LinkedHashMap<>();
            mergeStep.put("Action", "Merge");
            mergeStep.put("Cities", List.of("City " + cityA.getId(), "City " + cityB.getId()));
            mergeStep.put("Cluster After Merge", find(cityA.getId()));
            mergingSteps.add(mergeStep);
        }
    }

    private void addQueries() {
        for (int i = 0; i < cities.size(); i++) {
            for (int j = i + 1; j < cities.size(); j++) {
                City cityA = cities.get(i);
                City cityB = cities.get(j);

                String query = "Are City " + cityA.getId() + " and City " + cityB.getId() + " in the same cluster?";
                String result = find(cityA.getId()).equals(find(cityB.getId())) ? "Yes" : "No";

                queries.add(Map.of(
                        "Query", query,
                        "Result", result
                ));

                // Debug: Print query and result
                System.out.println("Query: " + query + ", Result: " + result);
            }
        }
    }



    private void printClusters() {
        Map<String, String> clusters = getClusters();
        for (Map.Entry<String, String> entry : clusters.entrySet()) {
            System.out.println("City " + entry.getKey() + " -> " + entry.getValue());
        }
    }

    public Map<String, String> getClusters() {
        // Use a LinkedHashMap to maintain the sorted order after sorting
        Map<String, String> sortedClusters = new LinkedHashMap<>();

        // Sort the keys numerically and add them back in order
        parent.keySet().stream()
                .sorted(Comparator.comparingInt(Integer::parseInt)) // Sort by numeric value
                .forEach(cityId -> sortedClusters.put(cityId, find(cityId)));

        return sortedClusters;
    }


    public List<Map<String, Object>> getMergingSteps() {
        return mergingSteps;
    }

    public List<Map<String, String>> getQueries() {
        return queries;
    }
}
