import java.util.*;

public class DynamicResourceSharing {
    private List<City> cities = ReseauSingleton.getInstance().getCities();
    public DynamicResourceSharing() {
        Map<Set<String>, Set<City>> villesParFournisseur = new HashMap<>();

        for (City city : cities) {
            Set<String> warehousesId = new HashSet<>(city.getWarehouses());
            villesParFournisseur.computeIfAbsent(warehousesId, k -> new HashSet<>()).add(city);
        }

        for (Map.Entry<Set<String>, Set<City>> entry : villesParFournisseur.entrySet()){
            System.out.println(entry);
        }
    }
}
