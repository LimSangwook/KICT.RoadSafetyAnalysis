import Analysis.Analyst;
import RawData.RawDataManager;
import RoadInfo.RoadInfoManager;

public class Main {
	public static void main(String[] args) throws Exception {
		String RawDataFile = "/Users/iswook/Documents/KICT/01_radar.csv";
		String AnalysisDataFile = "/Users/iswook/Documents/KICT/01_an.csv";
		String SaveFile = "/Users/iswook/Documents/KICT/01_Output_Distance_SDI_First.csv";
		
		System.out.printf("############## START Analysis ##############\n");
		RawDataManager rawDataManager = new RawDataManager(RawDataFile, RawDataManager.TYPE.A);
		RoadInfoManager roadInfoManager = new RoadInfoManager(AnalysisDataFile);
		Analyst analyst = new Analyst(rawDataManager, roadInfoManager, Analyst.TotalInfoType.DEFAULT);
		analyst.DoAnalysis();
		analyst.WriteCSV(SaveFile);
		System.out.printf("############## END Analysis ##############\n");
	}
}