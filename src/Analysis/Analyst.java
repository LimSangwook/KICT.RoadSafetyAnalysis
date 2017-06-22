package Analysis;
import java.io.FileWriter;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Iterator;

import DataController.RawDataControllr;
import RawData.RawDataManager;
import RawData.RawDataModel;
import RoadInfo.RoadInfoManager;

public class Analyst {
	private RawDataManager rawDataMGR = null;
	private RoadInfoManager roadInfoMGR = null;
	private ArrayList<AnalysisDataModel> analysisResult= null;
	
	class AnalysisInfo {
		AnalysisDataModel defaultInfo;
	}	
	
	public Analyst(RawDataManager rawDataManager, RoadInfoManager roadInfoManager) {
		rawDataMGR = rawDataManager;
		roadInfoMGR = roadInfoManager;
	}
	public void DoAnalysis() {
		System.out.printf("####	 START \tAnalyst.DoAnalysis()\n");

		if (rawDataMGR == null || roadInfoMGR == null) {
			System.out.printf("Error - Analyst.DoAnalysis() : rawDataMGR == null || roadDataMGR == null");
		}
		
		analysisResult = new ArrayList<AnalysisDataModel>();
		for (RawDataModel rawData : rawDataMGR.getDatas()) {
			AnalysisDataModel analysisDataModel = new AnalysisDataModel(rawData, roadInfoMGR);
			analysisResult.add(analysisDataModel);
		}
		System.out.printf("####	 END \tAnalyst.DoAnalysis()\n");
	}

	public void WriteCSV(String path, RawDataControllr rdController) throws IOException {
		System.out.printf("####	 START \tAnalyst.WriteCSV()\n");
		FileWriter writer = new FileWriter(path, false);
		writer.write("\uFEFF");
		writer.write(rdController.getCSVFormatHeader());
		Iterator<AnalysisDataModel> iter = analysisResult.iterator();
		while(iter.hasNext()) {
			AnalysisDataModel data = iter.next();
			String str = rdController.getCSVFormatData(data);
			writer.write(str);
		}
		writer.close();
		System.out.printf("####	 END \tAnalyst.WriteCSV()\n");
		// TODO Auto-generated method stub
		
	}
}
