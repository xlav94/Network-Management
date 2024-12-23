import java.util.Collections;
import java.util.Comparator;
import java.util.List;
import java.util.PriorityQueue;

public class Allocation {
    private List<List<Double>> matriceDeCout = ReseauSingleton.getInstance().getMatriceDeCout();
    private List<Warehouse> warehouses = ReseauSingleton.getInstance().getWarehouses();
    private List<City>  cities = ReseauSingleton.getInstance().getCities();
    private PriorityQueue<City> cityQueue = new PriorityQueue<>(
            Comparator.comparingInt(city -> {
                switch (city.getPriorite()) {
                    case "High": return 1;
                    case "Medium": return 2;
                    case "Low": return 3;
                    default: return 4;
                }
            })
    );

    public Allocation() {
        for (City city : cities) {
            while (city.getDemande() > 0) {
                int bestWarehouseIndex = findBestWarehouse(city);
                if (bestWarehouseIndex == -1) {
                    System.out.println("No more resources available for City " + city.getId());
                    break;
                }

                Warehouse warehouse = warehouses.get(bestWarehouseIndex);
                double allocation = Math.min(city.getDemande(), warehouse.getCapacite());

                // Update allocations
                city.setDemande(city.getDemande() - allocation);
                city.addWarehouseAllocation(warehouse.getId(), allocation);
                warehouse.setCapacite(warehouse.getCapacite() - allocation);

                System.out.println("Allocating resources for City " + city.getId() + " (Priority: " + city.getPriorite() + ")");
                System.out.println("   Allocated " + allocation + " units from Warehouse " + warehouse.getId());
            }
        }
    }

    private int findBestWarehouse(City city) {
        List<Double> costs = matriceDeCout.get(cities.indexOf(city));
        double minCost = Double.MAX_VALUE;
        int bestIndex = -1;

        for (int i = 0; i < costs.size(); i++) {
            if (warehouses.get(i).getCapacite() > 0 && costs.get(i) < minCost) {
                minCost = costs.get(i);
                bestIndex = i;
            }
        }

        return bestIndex;
    }
}