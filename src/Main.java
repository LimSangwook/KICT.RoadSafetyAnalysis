import Analysis.Analyst;
import DataController.RawDataControllr;
import RawData.RawDataManager;
import RoadInfo.RoadInfoManager;

public class Main {
	public static void main(String[] args) throws Exception {
		System.out.printf("############## START Analysis ##############\n");

		RawDataControllr rdControllerA = RawDataControllr.Create(RawDataControllr.TYPE.A);
		RawDataManager rawDataManager = new RawDataManager();
		rawDataManager.Add("/Users/iswook/Documents/KICT/01_radar.csv", rdControllerA);
		rawDataManager.Add("/Users/iswook/Documents/KICT/02_radar.csv", rdControllerA);
		rawDataManager.Add("/Users/iswook/Documents/KICT/03_radar.csv", rdControllerA);

		RoadInfoManager roadInfoManager = new RoadInfoManager();
		roadInfoManager.Add("/Users/iswook/Documents/KICT/01_an.csv");
		roadInfoManager.Add("/Users/iswook/Documents/KICT/02_an.csv");
		roadInfoManager.Add("/Users/iswook/Documents/KICT/03_an.csv");

		Analyst analyst = new Analyst(rawDataManager, roadInfoManager);
		analyst.DoAnalysis();
		analyst.WriteCSV("/Users/iswook/Documents/KICT/02_Output.csv", rdControllerA);

		System.out.printf("############## END Analysis ##############\n");
	}
}