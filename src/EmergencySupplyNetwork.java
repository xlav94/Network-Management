import java.util.*;

public class EmergencySupplyNetwork {
    private List<List<Double>> matriceDeCout = new ArrayList<>();

    private List<City> cities = ReseauSingleton.getInstance().getCities();
    private List<Warehouse> warehouses = ReseauSingleton.getInstance().getWarehouses();

    //private JsonGenerator jsonGenerator = ReseauSingleton.getInstance().getJsonFile();

    public EmergencySupplyNetwork() {
        // Calculer la matrice de coût
        matriceCout();
        //affichage();
    }

    public List<List<Double>> getMatriceDeCout() {
        return matriceDeCout;
    }



    // matrice de cout
    private void matriceCout(){
        for (City city : cities){
            List<Double> row = new ArrayList<>();
            for (Warehouse warehouse : warehouses){
                Double cost = calculCost(getDistance(warehouse.getCoordonne(), city.getCoordonne()));
                row.add((Math.round(cost * 100.0) / 100.0));
            }
            matriceDeCout.add(row);
        }
        ReseauSingleton.getInstance().setMatriceDeCout(matriceDeCout);
    }

    private double calculCost(double distance){
        if (distance <= 10 ){
            // Utilisation d'un drone
            return distance;   // coefficient du mode de transport = 1
        } else if (distance <= 20) {
            // Utilisation d'un camion
            return distance * 2; // coefficient du mode de transport = 2
        }else {
            // Utilisation d'un train
            return distance * 3; // coefficient du mode de transport = 3
        }
    }

    private double getDistance (Coordonnes<Integer, Integer> warehouse, Coordonnes<Integer, Integer> city){
        return Math.sqrt(Math.pow(warehouse.getX() - city.getX(), 2) + Math.pow(warehouse.getY() - city.getY(), 2));
    }

    // affiche le tableau
    private void affichage() {
        String title = "\nGraph Representation (Cost Matrix): ";
        int citySpace = String.valueOf(getCityNameSpace()).length() + 12;
        int warehouseSpace = String.valueOf(getWarehouseNameSpace()).length() + 14;

        System.out.println(title);
        System.out.println(getSeparation(warehouses.size(), warehouseSpace, citySpace));
        System.out.println(getTitle());
        System.out.println(getSeparation(warehouses.size(), warehouseSpace, citySpace));


        for (int i = 0; i < matriceDeCout.size(); i++) {
            List<Double> row = matriceDeCout.get(i);
            StringBuilder couts = new StringBuilder();
            couts.append("City ")
                    .append(cities.get(i).getId())
                    .append(getSpace(7 - cities.get(i).getId().length()))
                    .append("|");
            for (Double col : row) {
                int space = String.valueOf(col).length(); // Longueur actuelle du nombre
                int rightSpace = (int) Math.round(((double) warehouseSpace - space) /2) ; // Espaces à droite
                int leftSpace = warehouseSpace -  rightSpace - space; // Espaces à gauche

                couts.append(getSpace(rightSpace))
                        .append(col)
                        .append(getSpace(leftSpace))
                        .append("|");
            }
            System.out.println(couts);
        }
        System.out.println(getSeparation(warehouses.size(), warehouseSpace, citySpace) + "\n");
    }

    private String getTitle(){
        StringBuilder title = new StringBuilder();

        title.append("   Cities   |");
        for (Warehouse warehouse : warehouses){
            title.append(" Warehouse ")
                    .append(warehouse.getId())
                    .append(" |");
        }
        return title.toString();
    }

    private String getSeparation(int numbWarehouse, int warehouseSpace, int citySpace){
        String line = "";
        for (int i = 0; i < numbWarehouse; i++) {
            for (int j = 0; j < warehouseSpace; j++) {  // on met autant de "--" Warehouse 101
                line += "-";
            }
            line += "-";   // correspond a "|"
        }
        for (int i = 0; i < citySpace; i++) {
            line += "-";
        }
        return line;
    }

    private String getSpace(int space){
        String spaces = "";
        for (int i = 0; i < space; i++){
            spaces += " ";
        }
        return spaces;
    }

    private int getCityNameSpace(){
        return cities.stream()
                .map(city -> city.getId())        // Obtenez l'ID en tant que chaîne
                .mapToInt(String::length)        // Calculez la longueur de chaque ID
                .max()                           // Trouvez la longueur maximale
                .orElse(0);
    }

    private int getWarehouseNameSpace() {
        return warehouses.stream()
                .map(warehouse -> warehouse.getId()) // Obtenez l'ID en tant que chaîne
                .mapToInt(String::length)          // Calculez la longueur de chaque ID
                .max()                             // Trouvez la longueur maximale
                .orElse(0);                        // Retournez 0 si la liste est vide
    }
}
