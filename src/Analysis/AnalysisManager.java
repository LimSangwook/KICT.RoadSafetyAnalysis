package Analysis;
import java.io.FileWriter;
import java.io.IOException;
import java.util.ArrayList;

import Analysis.Analyzer.AnalyzerBase;
import Analysis.Formatter.DefaultFormatter;
import Analysis.Analyzer.AnalyzeBasicCarSet;
import Analysis.Analyzer.AnalyzeExtractCarSet;
import Analysis.Analyzer.AnalyzeTotal;
import Analysis.Model.AnalysisDataModel;
import RadarData.RadarDataManager;
import RadarData.RadarDataModel;
import RoadInfo.RoadInfoManager;

public class AnalysisManager {
	public enum BaseTargetType{SINGLE_BASE_TARGET, MULTI_BASE_TARGET};
	private BaseTargetType baseTargetType = BaseTargetType.SINGLE_BASE_TARGET;
	private RadarDataManager rawDataMGR = null;
	private RoadInfoManager roadInfoMGR = null;
	private ArrayList<AnalysisDataModel> analysisResult = null;
	
	private AnalyzerBase firstStepAnalyzer = null;
	
	public AnalysisManager(RadarDataManager rawDataManager, RoadInfoManager roadInfoManager, BaseTargetType baseTargetType) {
		this.rawDataMGR = rawDataManager;
		this.roadInfoMGR = roadInfoManager;
		this.baseTargetType = baseTargetType;
		
		firstStepAnalyzer = new AnalyzeBasicCarSet(baseTargetType);
		firstStepAnalyzer.next(new AnalyzeExtractCarSet()).next(new AnalyzeTotal());
	}


	public void DoAnalysis() {
		System.out.printf("####	 START \tAnalysisManager.DoAnalysis()\n");
		if (rawDataMGR == null || roadInfoMGR == null) {
			System.out.printf("Error - AnalysisManager.DoAnalysis() : rawDataMGR == null || roadDataMGR == null");
		}
		
		analysisResult = new ArrayList<AnalysisDataModel>();
		for (RadarDataModel rawData : rawDataMGR.getDatas()) {
			AnalysisDataModel analysisDataModel = new AnalysisDataModel(rawData);
			firstStepAnalyzer.doAnalysis(analysisDataModel, roadInfoMGR);
			analysisResult.add(analysisDataModel);
		}
		System.out.printf("####	 END \t AnalysisManager.DoAnalysis()\n");
	}

	public void WriteCSV(String path, DefaultFormatter defaultFormatter) throws IOException {
		System.out.printf("####	 START \t AnalysisManager.WriteCSV()\n");
		FileWriter writer = new FileWriter(path, false);
		writer.write("\uFEFF");
		writer.write(defaultFormatter.getCSVFormatHeader());
		for(AnalysisDataModel data : analysisResult) {
			String str = defaultFormatter.getCSVFormatData(data);
			writer.write(str);
		}
		writer.close();
		System.out.printf("####	 END \t AnalysisManager.WriteCSV()\n");
	}
}
