import java.util.Comparator;
import java.util.List;
import java.util.Map;
import java.util.PriorityQueue;
import java.util.ArrayList;
import java.util.LinkedHashMap;

public class ResourceRedistribution {
    private PriorityQueue<Warehouse> minHeap;
    private PriorityQueue<Warehouse> maxHeap;
    private List<Warehouse> warehouses;
    private List<Map<String, Object>> transfers; // Track resource transfers


    public ResourceRedistribution() {
        minHeap = new PriorityQueue<>(
                Comparator.comparingDouble(Warehouse::getCapacite)
        );
        maxHeap = new PriorityQueue<>(
                Comparator.comparingDouble(Warehouse::getCapacite).reversed()
        );
        warehouses = ReseauSingleton.getInstance().getWarehouses();
        for (Warehouse warehouse : warehouses) {
            warehouse.getId();
            warehouse.getCapacite();
        }

        transfers = new ArrayList<>();



        JsonGenerator jsonGenerator = new JsonGenerator();
        jsonGenerator.generateResourceAllocationJson("Output.json");
        for (Warehouse warehouse : warehouses) {
            System.out.println("Final Capacity - " + warehouse.getId() + ": " + warehouse.getCapacite());
        }
        initHeap();

        distribute();
        System.out.println("Final Resource Levels :");
        for (Warehouse warehouse : warehouses) {
            System.out.println("Warehouse " + warehouse.getId() + " : " + warehouse.getCapacite() + " units");
        }
        System.out.println("Warehouse?" + warehouses);


        System.out.println("testing"+transfers);
        jsonGenerator.generateResourceRedistributionJson("Task3.json", transfers, getFinalResourceLevels());

    }

    private void initHeap(){
        for (Warehouse warehouse : warehouses) {
            double capacite = warehouse.getCapacite();
            if (capacite > 50){
                System.out.println("test3" + warehouse);
                maxHeap.add(warehouse);
            }
            else if (capacite < 50){
                System.out.println("test5"+ minHeap);
                minHeap.add(warehouse);
            }

        }
    }

    private void distribute() {
        System.out.println("Initial Min Heap: " + minHeap);
        System.out.println("Initial Max Heap: " + maxHeap);

        while (!maxHeap.isEmpty() && !minHeap.isEmpty()) {
            Warehouse minWarehouse = minHeap.poll();
            double minCapacite = minWarehouse.getCapacite();

            Warehouse maxWarehouse = maxHeap.poll();
            double maxCapacite = maxWarehouse.getCapacite();

            // Calculate the transfer amount
            double transferAmount = Math.min(50 - minCapacite, maxCapacite - 50);
            System.out.println("CACA" + transferAmount);

            if (transferAmount > 0) {
                // Update capacities
                minWarehouse.setCapacite(minCapacite + transferAmount);
                maxWarehouse.setCapacite(maxCapacite - transferAmount);

                // Record the transfer
                recordTransfer(maxWarehouse.getId(), minWarehouse.getId(), transferAmount);

                System.out.printf("Transferred %.2f units from Warehouse %s to Warehouse %s%n",
                        transferAmount, maxWarehouse.getId(), minWarehouse.getId());
            }

            // Reinsert warehouses if they still have capacity/resources
            if (maxWarehouse.getCapacite() > 50) {
                maxHeap.add(maxWarehouse);
            }
            if (minWarehouse.getCapacite() < 50) {
                minHeap.add(minWarehouse);
            }

            // Debugging current heap states
            System.out.println("Updated Min Heap: " + minHeap);
            System.out.println("Updated Max Heap: " + maxHeap);
        }

        // Final state of warehouses
        System.out.println("Final Min Heap: " + minHeap);
        System.out.println("Final Max Heap: " + maxHeap);
        System.out.println("Final Resource Levels:");
        for (Warehouse warehouse : warehouses) {
            System.out.printf("Warehouse %s: %.2f units%n", warehouse.getId(), warehouse.getCapacite());
        }
    }


    private void recordTransfer(String from, String to, double units) {
        Map<String, Object> transfer = new LinkedHashMap<>();
        transfer.put("From", "Warehouse " + from);
        transfer.put("To", "Warehouse " + to);
        transfer.put("Units", units);
        transfers.add(transfer);

        System.out.println("Recorded Transfer: " + transfer);
    }
    public Map<String, Double> getFinalResourceLevels() {
        Map<String, Double> finalResourceLevels = new LinkedHashMap<>();
        for (Warehouse warehouse : warehouses) {
            finalResourceLevels.put("Warehouse " + warehouse.getId(), warehouse.getCapacite());
        }
        return finalResourceLevels;
    }


    private void message(String minId, String maxId, double units){
        StringBuilder message = new StringBuilder();

        message.append("Transferred ")
                .append(units)
                .append(" units from Warehouse ")
                .append(maxId)
                .append(" to Warehouse ")
                .append(minId);
        System.out.println(message);
    }
}
