public class City extends Network {
    private int demande;
    private String priorite;

    public City(int id, Coordonnes <Integer, Integer> coordonnes, int demande, String priorite) {
        super(id, coordonnes);
        this.demande = demande;
        this.priorite = priorite;
    }

    public int getDemande() {
        return demande;
    }
    public String getPriorite() {
        return priorite;
    }

    public void setDemande(int demande) {
        this.demande = demande;
    }
    public void setPriorite(String priorite) {
        this.priorite = priorite;
    }

    public String toString() {
        return "ID = " + id + ", " + coordonne.toString() + ", Demand = " + demande + " units, Priority = " + priorite;
    }
}
