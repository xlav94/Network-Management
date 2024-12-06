public abstract class Network {
    protected String id;
    protected Coordonnes<Integer, Integer> coordonne;

    public Network(String id, Coordonnes<Integer, Integer> coordonne) {
        this.id = id;
        this.coordonne = coordonne;
    }

    public String getId() {
        return id;
    }
    public Coordonnes<Integer, Integer> getCoordonne() {
        return coordonne;
    }
    public void setCoordonne(Coordonnes<Integer, Integer> coordonne) {
        this.coordonne = coordonne;
    }
}
