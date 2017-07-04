package Analysis.Analyze;

import java.util.ArrayList;

import Analysis.AnalysisDataModel;
import Analysis.AnalysisResult;
import Analysis.ExtractCarSet;
import RawData.RawDataModel;
import RoadInfo.RoadInfoManager;
import Utility.Util;

public class AnalyzeTotal extends Analyze {

	@Override
	protected void process(AnalysisDataModel data, RoadInfoManager roadInfoMGR) {
		 // 측량차랑 기준 - 같은차로에 차량이 있는경우
		int TOT_CD = 0;
		RawDataModel rawData = data.getRawData();
		AnalysisResult BaseAnalysisResult = data.getBaseAnalysisResult();
		if (data.getBASE_ID_F() == 99) TOT_CD++;
		AnalysisResult TOT_AnalysisResult = new AnalysisResult();
        if (BaseAnalysisResult != null) {
	        TOT_AnalysisResult.SL += BaseAnalysisResult.SL; 
	        TOT_AnalysisResult.SS += BaseAnalysisResult.SS;
	        TOT_AnalysisResult.SDI += BaseAnalysisResult.SDI;
	        TOT_AnalysisResult.TTC += BaseAnalysisResult.TTC;
	        TOT_AnalysisResult.TOT_SAFE += BaseAnalysisResult.TOT_SAFE;
	        if ((BaseAnalysisResult.SL ==1 || BaseAnalysisResult.SS ==1 ) && (BaseAnalysisResult.SDI ==1 || BaseAnalysisResult.TTC ==1 ))
	        	System.out.println("########");
	        // ExtractCarSet 처리;
	        ArrayList<ExtractCarSet> extractCarSetList = data.getExtractCarSetList();
	        TOT_CD += extractCarSetList.size();
	        for (ExtractCarSet extractCarSet : extractCarSetList) {
	            TOT_AnalysisResult.SL += extractCarSet.analysisResult.SL;
	            TOT_AnalysisResult.SS += extractCarSet.analysisResult.SS;
	            TOT_AnalysisResult.SDI += extractCarSet.analysisResult.SDI;
	            TOT_AnalysisResult.TTC += extractCarSet.analysisResult.TTC;
	            TOT_AnalysisResult.TOT_SAFE += extractCarSet.analysisResult.TOT_SAFE;
	        }
        }
        data.setTOT_CD(TOT_CD);
        data.setTOT_AnalysisResult(TOT_AnalysisResult);
        data.setTOT_SECTION(Util.GetSectionName(rawData.getLatitude(), rawData.getLongitude()));
	}

}
