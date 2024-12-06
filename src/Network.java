public abstract class Network {
    protected int id;
    protected Coordonnes<Integer, Integer> coordonne;

    public Network(int id, Coordonnes<Integer, Integer> coordonne) {
        this.id = id;
        this.coordonne = coordonne;
    }

    public int getId() {
        return id;
    }
    public Coordonnes<Integer, Integer> getCoordonne() {
        return coordonne;
    }
    public void setCoordonne(Coordonnes<Integer, Integer> coordonne) {
        this.coordonne = coordonne;
    }
}
