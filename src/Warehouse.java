public class Warehouse extends Network {
    private double capacite;

    public Warehouse(int id, Coordonnes<Integer, Integer> coordonne, double capacite) {
        super(id, coordonne);
        this.capacite = capacite;
    }

    public double getCapacite() {
        return capacite;
    }

    public void setCapacite(double capacite) {
        this.capacite = capacite;
    }
    public String toString() {
        return "ID : " + id + ", " + coordonne.toString() + ", Capacity = " + capacite + "units";
    }
}
