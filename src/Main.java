import Analysis.AnalysisManager;
import Analysis.AnalysisManager.BaseTargetType;
import Analysis.Formatter.DefaultFormatter;
import RadarData.RadarDataManager;
import RadarData.Controller.RadarDataController;
import RoadInfo.RoadInfoManager;

public class Main {
	public static void main(String[] args) throws Exception {
		System.out.printf("############## START Analysis ##############\n");

		RadarDataManager radarDataManager = new RadarDataManager();
		radarDataManager.add("./Data/RadarA_Type/01_radar.csv", RadarDataController.TYPE.A);
		radarDataManager.add("./Data/RadarA_Type/02_radar.csv", RadarDataController.TYPE.A);
		radarDataManager.add("./Data/RadarA_Type/03_radar.csv", RadarDataController.TYPE.A);

		RoadInfoManager roadInfoManager = new RoadInfoManager();
		roadInfoManager.add("./Data/AN/01_an.csv");
		roadInfoManager.add("./Data/AN/02_an.csv");
		roadInfoManager.add("./Data/AN/03_an.csv");
		
		// UTM 좌표를 알기 위해... 
//		roadInfoManager.addLocationData("./Data/RadarA_Type/01_radar.csv", RadarDataController.TYPE.A);
//		roadInfoManager.addLocationData("./Data/RadarA_Type/02_radar.csv", RadarDataController.TYPE.A);
//		roadInfoManager.addLocationData("./Data/RadarA_Type/03_radar.csv", RadarDataController.TYPE.A);
//		roadInfoManager.syncLocationData();

		AnalysisManager analysisManager = new AnalysisManager(radarDataManager, roadInfoManager, BaseTargetType.MULTI_BASE_TARGET);
		analysisManager.DoAnalysis();
		analysisManager.WriteCSV("./Data/Output_TYPE-A.csv", DefaultFormatter.getInstance());

		System.out.printf("############## END Analysis ##############\n");
	}
}