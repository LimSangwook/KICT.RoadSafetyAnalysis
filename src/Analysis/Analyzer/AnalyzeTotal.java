package Analysis.Analyzer;

import java.util.ArrayList;

import Analysis.Model.AnalysisDataModel;
import Analysis.Model.AnalysisResultModel;
import Analysis.Model.ExtractCarSet;
import RadarData.RadarDataModel;
import RoadInfo.RoadInfoManager;
import Utility.Util;

public class AnalyzeTotal extends AnalyzerBase {

	@Override
	protected void process(AnalysisDataModel data, RoadInfoManager roadInfoMGR) {
		AnalysisResultModel TOT_AnalysisResult = new AnalysisResultModel();
		 // 측량차랑 기준 - 같은차로에 차량이 있는경우
		int TOT_CD = 0; // 총 추종갯
		RadarDataModel rawData = data.getRawData();
		ArrayList<ExtractCarSet> baseCarSetList = data.getBaseCarSetList();
		double SUM_TTC_EXP = 0.0;
		double SUM_TTC_Uniform_Kernel = 0.0;
		double SUM_TTC_Triweight_Kernel = 0.0;
		double SUM_TTC_Gaussian_Kernel = 0.0;
		double SUM_TTC_Weighted_Average = 0.0;
		
		double SUM_1_DIV_DISTANCE = 0.0;
		double SUM_1_DIV_DISTANCE_MUL_TTC = 0.0;
		
		if (baseCarSetList != null) {
			TOT_CD += baseCarSetList.size();
			for (ExtractCarSet carSet : baseCarSetList) {
		        TOT_AnalysisResult.SL += carSet.getAnalysisResult().SL; 
		        TOT_AnalysisResult.SS += carSet.getAnalysisResult().SS;
		        TOT_AnalysisResult.SDI += carSet.getAnalysisResult().SDI;
		        TOT_AnalysisResult.TTC += carSet.getAnalysisResult().TTC;
		        TOT_AnalysisResult.TOT_SAFE += carSet.getAnalysisResult().TOT_SAFE;
		        
		        SUM_TTC_EXP += carSet.getAnalysisResult().TTC_EXP;
		        SUM_TTC_Uniform_Kernel += carSet.getAnalysisResult().TTC_Uniform_Kernel;
		        SUM_TTC_Triweight_Kernel += carSet.getAnalysisResult().TTC_Triweight_Kernel;
		        SUM_TTC_Gaussian_Kernel += carSet.getAnalysisResult().TTC_Gaussian_Kernel;
		        
		        double DISATNCE = rawData.getTrackDatas().get(carSet.frontCarTrackIdx).Y;
		        SUM_1_DIV_DISTANCE += 1 / DISATNCE;
		        SUM_1_DIV_DISTANCE_MUL_TTC += 1 / DISATNCE * carSet.getAnalysisResult().TTC_EXP;
			}
	
			ArrayList<ExtractCarSet> extractCarSetList = data.getExtractCarSetList();
	        TOT_CD += extractCarSetList.size();
	        for (ExtractCarSet extractCarSet : extractCarSetList) {
	            TOT_AnalysisResult.SL += extractCarSet.getAnalysisResult().SL;
	            TOT_AnalysisResult.SS += extractCarSet.getAnalysisResult().SS;
	            TOT_AnalysisResult.SDI += extractCarSet.getAnalysisResult().SDI;
	            TOT_AnalysisResult.TTC += extractCarSet.getAnalysisResult().TTC;
	            TOT_AnalysisResult.TOT_SAFE += extractCarSet.getAnalysisResult().TOT_SAFE;
	            
		        SUM_TTC_EXP += extractCarSet.getAnalysisResult().TTC_EXP;
		        SUM_TTC_Uniform_Kernel += extractCarSet.getAnalysisResult().TTC_Uniform_Kernel;
		        SUM_TTC_Triweight_Kernel += extractCarSet.getAnalysisResult().TTC_Triweight_Kernel;
		        SUM_TTC_Gaussian_Kernel += extractCarSet.getAnalysisResult().TTC_Gaussian_Kernel;

		        	double DISATNCE = rawData.getTrackDatas().get(extractCarSet.frontCarTrackIdx).Y;
		        SUM_1_DIV_DISTANCE += 1 / DISATNCE;
		        SUM_1_DIV_DISTANCE_MUL_TTC += 1 / DISATNCE * extractCarSet.getAnalysisResult().TTC_EXP;
	        	}
		}
		// 가중평균을 위해한번더 돈다. 
		if (baseCarSetList != null) {
			for (ExtractCarSet carSet : baseCarSetList) {
				double DISATNCE = rawData.getTrackDatas().get(carSet.frontCarTrackIdx).Y;
				carSet.getAnalysisResult().TTC_Weighted_Average = (1/DISATNCE)/(SUM_1_DIV_DISTANCE)*carSet.getAnalysisResult().TTC_EXP;
				SUM_TTC_Weighted_Average += carSet.getAnalysisResult().TTC_Weighted_Average;
			}
			ArrayList<ExtractCarSet> extractCarSetList = data.getExtractCarSetList();
	        for (ExtractCarSet extractCarSet : extractCarSetList) {
	        		double BETWEEN_DISATNCE = extractCarSet.getAnalysisResult().RAW_BETWEEN_DISTANCE;
	        		extractCarSet.getAnalysisResult().TTC_Weighted_Average = (1/BETWEEN_DISATNCE)/(SUM_1_DIV_DISTANCE)*extractCarSet.getAnalysisResult().TTC_EXP;
				SUM_TTC_Weighted_Average += extractCarSet.getAnalysisResult().TTC_Weighted_Average;
	        	}
		}
		TOT_AnalysisResult.FCRI_TTC_EXP = SUM_TTC_EXP / TOT_CD;
		TOT_AnalysisResult.FCRI_TTC_Uniform_Kernel = SUM_TTC_Uniform_Kernel / TOT_CD;
		TOT_AnalysisResult.FCRI_TTC_Triweight_Kernel = SUM_TTC_Triweight_Kernel / TOT_CD;
		TOT_AnalysisResult.FCRI_TTC_Gaussian_Kernel = SUM_TTC_Gaussian_Kernel / TOT_CD;
		TOT_AnalysisResult.FCRI_TTC_Weighted_Average = SUM_TTC_Weighted_Average / TOT_CD;
		
        data.setTOT_CD(TOT_CD);
        data.setTOT_AnalysisResult(TOT_AnalysisResult);
        data.setTOT_SECTION(Util.GetSectionName(rawData.getLatitude(), rawData.getLongitude()));
	}

}
