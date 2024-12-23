import java.util.List;

public class NetworkApp {
    public static void main(String[] args) {
        String inputFileName = args[0];
        EmergencySupplyNetwork emergencySupplyNetwork = new EmergencySupplyNetwork(inputFileName);

        ResourceRedistribution resourceRedistribution = new ResourceRedistribution();

        DynamicResourceSharing dynamicResourceSharing = new DynamicResourceSharing();

    }
}
