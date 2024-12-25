import java.util.List;
import java.util.Map;

public class NetworkApp {
    public static void main(String[] args) {
        // verifier que le chemin du fichier d'entrée est fourni comme argument
        if (args.length < 1) {
            System.err.println("Please provide the input file path as an argument.");
            System.exit(1);
        }

        // définir le chemin du fichier pour le singleton
        String testFilePath = args[0];
        ReseauSingleton.setFilePath(testFilePath);
        System.out.println("Input File Path: " + testFilePath);

        // initialiser le singleton après avoir défini le chemin du fichier
        ReseauSingleton singleton = ReseauSingleton.getInstance();

        // initialiser le reste
        EmergencySupplyNetwork emergencySupplyNetwork = new EmergencySupplyNetwork();
        Allocation allocation = new Allocation(); // Perform allocations
        ResourceRedistribution resourceRedistribution = new ResourceRedistribution();
        DynamicResourceSharing dynamicResourceSharing = new DynamicResourceSharing();

        //Capture les capacités restantes après allocation
        Map<String, Double> remainingCapacitiesAfterAllocation = resourceRedistribution.getRemainingCapacitiesAfterAllocation();
        // Capture final resource levels after redistribution
        List<Map<String, Object>> transfers = resourceRedistribution.getTransfers();
        Map<String, Double> finalResourceLevels = resourceRedistribution.getFinalResourceLevels();

        // générer le fichier json
        JsonGenerator jsonGenerator = new JsonGenerator();
        //changer le 1er argument au nom du fichier output voulu
        jsonGenerator.generateCompleteJson("Output_testCase1.json", transfers, finalResourceLevels, remainingCapacitiesAfterAllocation);

        System.out.println("JSON generation completed.");
    }
}
