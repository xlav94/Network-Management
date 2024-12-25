import java.util.*;

public class Allocation {
    private List<List<Double>> matriceDeCout = ReseauSingleton.getInstance().getMatriceDeCout();
    private List<Warehouse> warehouses = ReseauSingleton.getInstance().getWarehouses();
    private List<City> cities = ReseauSingleton.getInstance().getCities();
    private PriorityQueue<City> cityQueue;

    public Allocation() {
        initializePriorityQueue(); // initialise the priority queue
        allocateResources(); //perform priority-based resource allocation
        //printRemainingCapacities();
    }

    private void initializePriorityQueue() {
        cityQueue = new PriorityQueue<>(Comparator.comparingInt(city -> {
            switch (city.getPriorite()) {
                case "High": return 1;
                case "Medium": return 2;
                case "Low": return 3;
                default: return 4;
            }
        }));

        cityQueue.addAll(cities); // add all cities to the priority queue
    }

    private void allocateResources() {
        while (!cityQueue.isEmpty()) {
            City currentCity = cityQueue.poll(); //get the city with the highest priority
            //System.out.println("\nAllocating resources for City " + currentCity.getId() + " (Priority: " + currentCity.getPriorite() + ")");
            while (currentCity.getDemande() > 0) {
                int bestWarehouseIndex = findBestWarehouse(currentCity);
                if (bestWarehouseIndex == -1) {
                    //System.out.println("No more resources for City " + currentCity.getId());
                    break;
                }
                Warehouse warehouse = warehouses.get(bestWarehouseIndex);
                double allocation = Math.min(currentCity.getDemande(), warehouse.getCapacite());

                // update city and warehouse data
                currentCity.setDemande(currentCity.getDemande() - allocation);
                currentCity.addWarehouseAllocation(warehouse.getId(), allocation);
                warehouse.setCapacite(warehouse.getCapacite() - allocation);

                //System.out.println("Allocated " + allocation + " units from Warehouse " + warehouse.getId());
            }
        }
    }

    private int findBestWarehouse(City city) {
        int cityIndex = getCityIndex(city);
        List<Double> costs = matriceDeCout.get(cityIndex);
        double minCost = Double.MAX_VALUE;
        int bestIndex = -1;
        for (int i = 0; i < costs.size(); i++) {
            if (warehouses.get(i).getCapacite() > 0 && costs.get(i) < minCost) {
                minCost = costs.get(i);
                bestIndex = i;
            }
        }
        return bestIndex;}

    private int getCityIndex(City city) {
        for (int i = 0; i < cities.size(); i++) {
            if (cities.get(i).getId().equals(city.getId())) {
                return i;
            }
        }
        throw new IllegalArgumentException("City ID " + city.getId() + " not found in cities list.");
    }

    private void printRemainingCapacities() {
        System.out.println("\nRemaining Warehouse Capacities:");
        for (Warehouse warehouse : warehouses) {
            System.out.println("Warehouse " + warehouse.getId() + ": " + warehouse.getCapacite() + " units");
        }
    }
}
