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
        cityQueue.addAll(cities); // on construit la file de priorité
        List<Warehouse> avalaibleWarehouses = warehouses;

        while (!cityQueue.isEmpty()) {
            City currentCity = cityQueue.poll();
            while (0 < currentCity.getDemande()){
                StringBuilder message = new StringBuilder();
                int index = findWarehouse(matriceDeCout.get(cities.indexOf(currentCity)));

                Warehouse warehouse = avalaibleWarehouses.get(index);

                double avalaibleRessources = warehouse.getCapacite();
                double demande = currentCity.getDemande();

                double ressourcesAllocate;


                if (avalaibleRessources <= demande){
                    currentCity.setAllocation(currentCity.getAllocation() +  avalaibleRessources);  // ajoute les ressources alloue
                    currentCity.setDemande(currentCity.getDemande() -  avalaibleRessources);  // reduit la demande

                    //avalaibleWarehouses.remove(warehouse); // l'entrepot n'est plus disponible

                    for (int i = 0; i < matriceDeCout.size(); i++) {
                        matriceDeCout.get(i).set(index, Double.MAX_VALUE);
                    }

                    ressourcesAllocate = avalaibleRessources;

                    warehouse.setCapacite(0);


                }

                else{
                    currentCity.setAllocation(currentCity.getAllocation() +  avalaibleRessources);
                    currentCity.setDemande(0);  // reduit la demande
                    warehouse.setCapacite(warehouse.getCapacite() - demande);
                    ressourcesAllocate = demande;
                }


                message.append("Allocating resources for City ")
                        .append(currentCity.getId())
                        .append(" (Priority: ")
                        .append(currentCity.getPriorite())
                        .append(")\n")
                        .append("Allocated " + ressourcesAllocate + " units from Warehouse ")
                        .append(warehouse.getId());

                System.out.println(message);
            }
        }
    }

    private int findWarehouse(List<Double> couts){
        Double minCost = Collections.min(couts);
        return couts.indexOf(minCost);
    }

}
