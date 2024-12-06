import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

public class ReseauSingleton {
    // Instance unique
    private static ReseauSingleton instance;

    private List<City> cities = new ArrayList<>();
    private List<Warehouse> warehouses = new ArrayList<>();

    // Constructeur privé pour empêcher l'instanciation
    private ReseauSingleton() {
        City cityA = new City(1, new Coordonnes <> (2, 7) , 70, "Medium");
        City cityB = new City(2, new Coordonnes <> (4, 7) , 30, "High");
        City cityC = new City(3, new Coordonnes <> (9, 2) , 20, "Low");
        City cityD = new City(4, new Coordonnes <> (18, 2) , 50, "High");

        Warehouse wX = new Warehouse(101, new Coordonnes<>(0, 10), 100);
        Warehouse wY = new Warehouse(102, new Coordonnes<>(9, 1), 50);
        Warehouse wZ = new Warehouse(103, new Coordonnes<>(19, 1), 150);

        cities.addAll(Arrays.asList(cityA, cityB, cityC, cityD));
        warehouses.addAll(Arrays.asList(wX, wY, wZ));
    }

    public List<City> getCities() {
        return cities;
    }

    public void setCities(List<City> cities) {
        this.cities = cities;
    }

    public void setWarehouses(List<Warehouse> warehouses) {
        this.warehouses = warehouses;
    }

    public List<Warehouse> getWarehouses() {
        return warehouses;
    }

    // Méthode pour obtenir l'instance unique
    public static ReseauSingleton getInstance() {
        if (instance == null) {
            instance = new ReseauSingleton();
        }
        return instance;
    }
}
