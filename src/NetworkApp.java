import java.util.List;

public class NetworkApp {
    public static void main(String[] args) {
        ReseauSingleton.getInstance().getJsonFile().setInputFileName(args[0]);

        EmergencySupplyNetwork emergencySupplyNetwork = new EmergencySupplyNetwork();

        ResourceRedistribution resourceRedistribution = new ResourceRedistribution();

        DynamicResourceSharing dynamicResourceSharing = new DynamicResourceSharing();

    }
}
