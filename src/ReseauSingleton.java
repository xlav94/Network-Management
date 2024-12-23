import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

public class ReseauSingleton {
    // Instance unique
    private static ReseauSingleton instance;

    private List<City> cities = new ArrayList<>();
    private List<Warehouse> warehouses = new ArrayList<>();
    private List<List <Double>> matriceDeCout = new ArrayList<>();

    private JsonGenerator jsonFile = new JsonGenerator();

    // Constructeur privé pour empêcher l'instanciation
    private ReseauSingleton() {
        cities = jsonFile.getCities();
        warehouses = jsonFile.getWarehouses();
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

    public List<List<Double>> getMatriceDeCout() {
        return matriceDeCout;
    }

    public void setMatriceDeCout(List<List<Double>> matriceDeCout) {
        this.matriceDeCout = matriceDeCout;
    }

    public JsonGenerator getJsonFile() {
        return jsonFile;
    }

    // Méthode pour obtenir l'instance unique
    public static ReseauSingleton getInstance() {
        if (instance == null) {
            instance = new ReseauSingleton();
        }
        return instance;
    }
}
