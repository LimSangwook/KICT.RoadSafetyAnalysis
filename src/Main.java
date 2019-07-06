import Analysis.AnalysisManager;
import Analysis.AnalysisManager.BaseTargetType;
import Analysis.Formatter.DefaultFormatter;
import RadarData.RadarDataManager;
import RadarData.Controller.RadarDataController;
import RoadInfo.RoadInfoManager;

public class Main {
	public static void main(String[] args) throws Exception {
		System.out.printf("############## START Analysis ##############\n");

		// RawData 설정 
		RadarDataManager radarDataManager = new RadarDataManager();
		radarDataManager.add("./Data/RadarA_Type/01_radar.csv", RadarDataController.TYPE.A);
		radarDataManager.add("./Data/RadarA_Type/02_radar.csv", RadarDataController.TYPE.A);
		radarDataManager.add("./Data/RadarA_Type/03_radar.csv", RadarDataController.TYPE.A);

		//AN Data(도로데이터) 설정 
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
		analysisManager.WriteCSV("/Users/limsangwook/eclipse-workspace/KICT_RoadSafetyAnalysis/Data/Output/Output_TYPE.csv", DefaultFormatter.getInstance());
		analysisManager.REPORT_2("/Users/limsangwook/eclipse-workspace/KICT_RoadSafetyAnalysis/Data/Output/Output_2.TTC결과정리.csv");
		analysisManager.REPORT_3("/Users/limsangwook/eclipse-workspace/KICT_RoadSafetyAnalysis/Data/Output/Output_3.TTCe결과정리.csv");
		analysisManager.REPORT_4("/Users/limsangwook/eclipse-workspace/KICT_RoadSafetyAnalysis/Data/Output/Output_4.가중치 결과 정리.csv");
		analysisManager.REPORT_6("/Users/limsangwook/eclipse-workspace/KICT_RoadSafetyAnalysis/Data/Output/Output_6.가중치 비교.csv");

		System.out.printf("############## END Analysis ##############\n");
	}
}