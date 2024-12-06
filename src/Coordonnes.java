public class Coordonnes <X, Y> {
    private X x;
    private Y y;

    public Coordonnes(X x, Y y) {
        this.x = x;
        this.y = y;
    }
    public X getX() {
        return x;
    }
    public Y getY() {
        return y;
    }
    public void setX(X x){
        this.x = x;
    }
    public void setY(Y y){
        this.y = y;
    }
    public String toString() {
        return "Coordinates = (" + x + "," + y + ")";
    }
}
