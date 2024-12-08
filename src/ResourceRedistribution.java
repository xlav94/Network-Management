import java.util.Comparator;
import java.util.List;
import java.util.PriorityQueue;

public class ResourceRedistribution {
    private PriorityQueue<Warehouse> minHeap;
    private PriorityQueue<Warehouse> maxHeap;
    private List<Warehouse> warehouses;

    public ResourceRedistribution() {
        minHeap = new PriorityQueue<>(
                Comparator.comparingDouble(Warehouse::getCapacite)
        );
        maxHeap = new PriorityQueue<>(
                Comparator.comparingDouble(Warehouse::getCapacite).reversed()
        );
        warehouses = ReseauSingleton.getInstance().getWarehouses();
        initHeap();

        distribute();

        System.out.println("Final Resource Levels :");
        for (Warehouse warehouse : warehouses) {
            System.out.println("Warehouse " + warehouse.getId() + " : " + warehouse.getCapacite() + " units");
        }
    }

    private void initHeap(){
        for (Warehouse warehouse : warehouses) {
            double capacite = warehouse.getCapacite();
            if (capacite > 50){
                maxHeap.add(warehouse);
            }
            else if (capacite < 50){
                minHeap.add(warehouse);
            }
        }
    }

    private void distribute(){
        while (!maxHeap.isEmpty() && !minHeap.isEmpty()){
            Warehouse minWarehouse = minHeap.poll();
            double minCapacite = minWarehouse.getCapacite();

            for (int index = 0; index < maxHeap.size() + 1; index++){
                Warehouse maxWarehouse = maxHeap.poll();
                double maxCapacite = maxWarehouse.getCapacite();
                double givenCapacite = 50 - minCapacite;

                minWarehouse.setCapacite(minCapacite + givenCapacite);
                maxWarehouse.setCapacite(maxCapacite - givenCapacite);

                message(minWarehouse.getId(), maxWarehouse.getId(), givenCapacite);
                if (maxWarehouse.getCapacite() != 0){
                    break;
                }
            }
        }
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
