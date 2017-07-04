import Analysis.Analyst;
import DataController.RawDataControllr;
import RawData.RawDataManager;
import RoadInfo.RoadInfoManager;

public class Main {
	public static void main(String[] args) throws Exception {
		System.out.printf("############## START Analysis ##############\n");

		RawDataControllr rdControllerA = RawDataControllr.Create(RawDataControllr.TYPE.A);
		RawDataControllr rdControllerB = RawDataControllr.Create(RawDataControllr.TYPE.B);
		RawDataManager rawDataManager = new RawDataManager();
		rawDataManager.add("/Users/iswook/Documents/KICT/5Hz_170510_140420_A.csv", rdControllerB);
		rawDataManager.add("/Users/iswook/Documents/KICT/5Hz_170510_142420_A.csv", rdControllerB);
		rawDataManager.add("/Users/iswook/Documents/KICT/5Hz_170510_154655_A.csv", rdControllerB);
//		rawDataManager.add("/Users/iswook/Documents/KICT/01_radar.csv", rdControllerA);
//		rawDataManager.add("/Users/iswook/Documents/KICT/02_radar.csv", rdControllerA);
//		rawDataManager.add("/Users/iswook/Documents/KICT/03_radar.csv", rdControllerA);

		RoadInfoManager roadInfoManager = new RoadInfoManager();
		roadInfoManager.add("/Users/iswook/Documents/KICT/01_an.csv");
		roadInfoManager.add("/Users/iswook/Documents/KICT/02_an.csv");
		roadInfoManager.add("/Users/iswook/Documents/KICT/03_an.csv");
		// UTM 좌표를 알기 위해... 
		roadInfoManager.addLocationData("/Users/iswook/Documents/KICT/01_radar.csv", rdControllerA);
		roadInfoManager.addLocationData("/Users/iswook/Documents/KICT/02_radar.csv", rdControllerA);
		roadInfoManager.addLocationData("/Users/iswook/Documents/KICT/03_radar.csv", rdControllerA);
		roadInfoManager.syncLocationData();
		

		Analyst analyst = new Analyst(rawDataManager, roadInfoManager);
		analyst.DoAnalysis();
		analyst.WriteCSV("/Users/iswook/Documents/KICT/Output_TYPE-.csv", rdControllerA);

		System.out.printf("############## END Analysis ##############\n");
	}
}