import java.util.List;
public class NetworkApp {
    public static void main(String[] args) {
        if (args.length < 1) {
            System.err.println("Please provide the input file path as an argument.");
            System.exit(1);
        }

        // Set the file path for the singleton
        String testFilePath = args[0];
        ReseauSingleton.setFilePath(testFilePath);
        System.out.println("Input File Path: " + testFilePath);

        // Initialize singleton after setting file path
        ReseauSingleton singleton = ReseauSingleton.getInstance();



        // Now initialize other components
        EmergencySupplyNetwork emergencySupplyNetwork = new EmergencySupplyNetwork();
        ResourceRedistribution resourceRedistribution = new ResourceRedistribution();
        DynamicResourceSharing dynamicResourceSharing = new DynamicResourceSharing();
    }
}
