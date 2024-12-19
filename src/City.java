import java.util.ArrayList;
import java.util.List;

public class City extends Network {
    private double demande;
    private String priorite;
    private double allocation = 0;
    private List<String> warehouses = new ArrayList<>();

    public City(String id, Coordonnes <Integer, Integer> coordonnes, double demande, String priorite) {
        super(id, coordonnes);
        this.demande = demande;
        this.priorite = priorite;
        this.allocation = allocation;
    }

    public double getDemande() {
        return demande;
    }

    public void setDemande(double demande) {
        this.demande = demande;
    }

    public String getPriorite() {
        return priorite;
    }
    public double getAllocation() {
        return allocation;
    }

    public void setAllocation(double allocation) {
        this.allocation = allocation;
    }

    public void setPriorite(String priorite) {
        this.priorite = priorite;
    }

    public List<String> getWarehouses() {
        return warehouses;
    }

    public void setWarehouses(List<String> warehouses) {
        this.warehouses = warehouses;
    }

    public String toString() {
        return "ID = " + id + ", " + coordonne.toString() + ", Demand = " + demande + " units, Priority = " + priorite;
    }
}
