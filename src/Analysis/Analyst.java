package Analysis;
import java.io.FileWriter;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Iterator;

import Analysis.Analyze.Analyze;
import Analysis.Analyze.AnalyzeBasicCarSet;
import Analysis.Analyze.AnalyzeExtractCarSet;
import Analysis.Analyze.AnalyzeTotal;
import DataController.RawDataControllr;
import RawData.RawDataManager;
import RawData.RawDataModel;
import RoadInfo.RoadInfoManager;

public class Analyst {
	private RawDataManager rawDataMGR = null;
	private RoadInfoManager roadInfoMGR = null;
	private ArrayList<AnalysisDataModel> analysisResult= null;
	
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
			AnalysisDataModel analysisDataModel = new AnalysisDataModel(rawData);
			Analyze anayze1 = new AnalyzeBasicCarSet();
			Analyze anayze2 = new AnalyzeExtractCarSet();
			Analyze anayze3 = new AnalyzeTotal();
			anayze1.next(anayze2);
			anayze2.next(anayze3);
			anayze1.doAnalysis(analysisDataModel, roadInfoMGR);
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
	}
}
