import java.util.ArrayList;
import java.util.List;

public class EmergencySupplyNetwork {
    private List<List<Double>> matriceDeCout = new ArrayList<>();

    private List<City> cities = ReseauSingleton.getInstance().getCities();
    private List<Warehouse> warehouses = ReseauSingleton.getInstance().getWarehouses();

    public EmergencySupplyNetwork(){
        matriceCout();
        affichage();
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

    private void affichage() {
        String title = "Graph Representation (Cost Matrix): ";
        int citySpace = String.valueOf(getCitySpace()).length() + 12;
        int warehouseSpace = String.valueOf(getWarehouseNameSpace()).length() + 13;

        System.out.println(title);
        System.out.println(getSeparation(warehouses.size(), warehouseSpace, citySpace));
        System.out.println(getTitle());
        System.out.println(getSeparation(warehouses.size(), warehouseSpace, citySpace));


        for (int i = 0; i < matriceDeCout.size(); i++) {
            List<Double> row = matriceDeCout.get(i);
            StringBuilder couts = new StringBuilder();
            couts.append("City ")
                    .append(cities.get(i).getId())
                    .append(getSpace(7 - getCitySpace()))
                    .append("|");
            int maxSpace = getWarehouseCostSpace(row);
            for (Double col : row) {
                int space = String.valueOf(col).length(); // Longueur actuelle du nombre
                int leftSpace = (warehouseSpace - space) / 2; // Espaces à gauche
                int rightSpace = warehouseSpace - space - leftSpace; // Espaces à droite

                couts.append(getSpace(leftSpace))
                        .append(col)
                        .append(getSpace(rightSpace))
                        .append("|");
            }
            System.out.println(couts);
        }
    }

    private String getTitle(){
        StringBuilder title = new StringBuilder();

        title.append("   Cities   |");
        for (Warehouse warehouse : warehouses){
            title.append(" Warehouse ")
                    .append(warehouse.getId())
                    .append("  ");
        }
        return title.toString();
    }

    private String getSeparation(int numbWarehouse, int warehouseSpace, int citySpace){
        String line = "";
        for (int i = 0; i < numbWarehouse; i++) {
            for (int j = 0; j < warehouseSpace; j++) {
                line += "-";
            }
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

    private int getCitySpace(){
        return String.valueOf(
                cities.stream()
                        .mapToInt(City::getId)
                        .max()
                        .orElse(0)
        ).length();
    }

    private int getWarehouseNameSpace(){
        return String.valueOf(warehouses.stream()
                .mapToInt(Warehouse::getId)
                .max()
                .orElse(0)
        ).length();
    }

    private int getWarehouseCostSpace(List<Double> row) {
        return String.valueOf(
                        row.stream()
                                .mapToDouble(Double::doubleValue) // Obtenez les valeurs double
                                .max()                            // Trouvez le maximum
                                .orElse(0)                        // Fournissez une valeur par défaut si vide
                )
                .replaceAll("\\.0+$", "") // Supprime les décimales inutiles
                .length();                // Compte les caractères
    }
}
