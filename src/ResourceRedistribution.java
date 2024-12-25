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
    private List<Map<String, Object>> transfers;
    private Map<String, Double> remainingCapacitiesAfterAllocation;


    public ResourceRedistribution() {
        minHeap = new PriorityQueue<>(Comparator.comparingDouble(Warehouse::getCapacite));
        maxHeap = new PriorityQueue<>(Comparator.comparingDouble(Warehouse::getCapacite).reversed());
        warehouses = ReseauSingleton.getInstance().getWarehouses();
        for (Warehouse warehouse : warehouses) {
            warehouse.getId();
            warehouse.getCapacite();
        }
        transfers = new ArrayList<>();
        JsonGenerator jsonGenerator = new JsonGenerator();
        jsonGenerator.getResourceAllocationData();
        remainingCapacitiesAfterAllocation = captureCapacities();
        //System.out.println(remainingCapacitiesAfterAllocation);
        initHeap();

        distribute();


    }

    private Map<String, Double> captureCapacities() {
        Map<String, Double> capacities = new LinkedHashMap<>();
        for (Warehouse warehouse : warehouses) {
            capacities.put("Warehouse " + warehouse.getId(), warehouse.getCapacite());
        }
        return capacities;
    }


    private void initHeap(){
        for (Warehouse warehouse : warehouses) {
            double capacite = warehouse.getCapacite();
            if (capacite > 50){
                //System.out.println("Max Heap test: " + warehouse);
                maxHeap.add(warehouse);
            }
            else if (capacite < 50){
                //System.out.println("Min Heap test: "+ minHeap);
                minHeap.add(warehouse);
            }

        }
    }

    private void distribute() {
        remainingCapacitiesAfterAllocation = captureCapacities();
        // System.out.println("Remaining capacities: " + remainingCapacitiesAfterAllocation);
        // System.out.println("Initial Min Heap: " + minHeap);
        // System.out.println("Initial Max Heap: " + maxHeap);

        while (!maxHeap.isEmpty() && !minHeap.isEmpty()) {
            Warehouse minWarehouse = minHeap.poll();
            double minCapacite = minWarehouse.getCapacite();
            Warehouse maxWarehouse = maxHeap.poll();
            double maxCapacite = maxWarehouse.getCapacite();
            // calcule les transferts
            double transferAmount = Math.min(50 - minCapacite, maxCapacite - 50);

            if (transferAmount > 0) {
                // mise à jour des capacites
                minWarehouse.setCapacite(minCapacite + transferAmount);
                maxWarehouse.setCapacite(maxCapacite - transferAmount);

                // enregistrer les transferts
                saveTransfer(maxWarehouse.getId(), minWarehouse.getId(), transferAmount);

            }

            // reinserer warehouses si ils ont encore des capacites
            if (maxWarehouse.getCapacite() > 50) {
                maxHeap.add(maxWarehouse);
            }
            if (minWarehouse.getCapacite() < 50) {
                minHeap.add(minWarehouse);
            }

        }

    }


    private void saveTransfer(String from, String to, double units) {
        Map<String, Object> transfer = new LinkedHashMap<>();
        transfer.put("From", "Warehouse " + from);
        transfer.put("To", "Warehouse " + to);
        transfer.put("Units", units);
        transfers.add(transfer);

        //System.out.println("Saved transfer: " + transfer);
    }
    public Map<String, Double> getFinalResourceLevels() {
        Map<String, Double> finalResourceLevels = new LinkedHashMap<>();
        for (Warehouse warehouse : warehouses) {
            finalResourceLevels.put("Warehouse " + warehouse.getId(), warehouse.getCapacite());
        }
        return finalResourceLevels;
    }

    public Map<String, Double> getRemainingCapacitiesAfterAllocation() {
        return remainingCapacitiesAfterAllocation;
    }
    public List<Map<String, Object>> getTransfers() {
        return transfers;
    }

    private void message(String minId, String maxId, double units){
        StringBuilder message = new StringBuilder();

        message.append("Transferred ")
                .append(units)
                .append(" units from Warehouse ")
                .append(maxId)
                .append(" to Warehouse ")
                .append(minId);
        //System.out.println(message);
    }

}
