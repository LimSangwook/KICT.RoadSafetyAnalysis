package Analysis.Analyzer;

import java.util.HashMap;
import java.util.Map;

import Analysis.Model.AnalysisDataModel;
import Analysis.Model.ExtractCarSet;
import RoadInfo.RoadInfoManager;

public class AnalyzeSector extends AnalyzeBase {
	public static Map<String, SectorAnalyzeModel> sectorAnayze = new HashMap<String, SectorAnalyzeModel>();

	public AnalyzeSector() {
		sectorAnayze.clear();
	}

	@Override
	protected void process(AnalysisDataModel analysisDataModel, RoadInfoManager roadInfoMGR) {
		if (analysisDataModel == null) {
			return;
		}
		String sectorName = analysisDataModel.getTOT_SECTION();
		SectorAnalyzeModel sectorData = null;
		if (sectorAnayze.containsKey(sectorName) == true) {
			sectorData = sectorAnayze.get(sectorName);
		} else {
			sectorData = new SectorAnalyzeModel();
		}
		if (analysisDataModel.getBaseCarSetList() != null)
		for (ExtractCarSet carSetData : analysisDataModel.getBaseCarSetList()) {
			if (carSetData.getAnalysisResult().TTC_T <= 0) continue;
			// TTC 결과 정리를 위한 변수 
			sectorData.totalSetCount++;
			sectorData.totalSumBaseCarSpeed += carSetData.analysisResult.RAW_BACK_SPPED_km_p_h;
			sectorData.totalSumTargetCarSpeed += carSetData.analysisResult.RAW_FRONT_SPPED_km_p_h;
			sectorData.totalSumBetweenDistance += carSetData.analysisResult.RAW_BETWEEN_DISTANCE;
			if (carSetData.analysisResult.RAW_BACK_SPPED_km_p_h < sectorData.minBaseCarSpeed) sectorData.minBaseCarSpeed = carSetData.analysisResult.RAW_BACK_SPPED_km_p_h;
			if (carSetData.analysisResult.RAW_BACK_SPPED_km_p_h > sectorData.maxBaseCarSpeed) sectorData.maxBaseCarSpeed = carSetData.analysisResult.RAW_BACK_SPPED_km_p_h;

			if (carSetData.analysisResult.RAW_BACK_SPPED_km_p_h < sectorData.minBaseCarSpeed) sectorData.minBaseCarSpeed = carSetData.analysisResult.RAW_BACK_SPPED_km_p_h;
			if (carSetData.analysisResult.RAW_BACK_SPPED_km_p_h > sectorData.maxBaseCarSpeed) sectorData.maxBaseCarSpeed = carSetData.analysisResult.RAW_BACK_SPPED_km_p_h;
			
			if (carSetData.analysisResult.RAW_FRONT_SPPED_km_p_h < sectorData.minTargetCarSpeed) sectorData.minTargetCarSpeed = carSetData.analysisResult.RAW_FRONT_SPPED_km_p_h;
			if (carSetData.analysisResult.RAW_FRONT_SPPED_km_p_h > sectorData.minTargetCarSpeed) sectorData.maxTargetCarSpeed = carSetData.analysisResult.RAW_FRONT_SPPED_km_p_h;
			
			if (carSetData.analysisResult.RAW_BETWEEN_DISTANCE < sectorData.minBetweenDistanceCarSpeed) sectorData.minBetweenDistanceCarSpeed = carSetData.analysisResult.RAW_BETWEEN_DISTANCE;
			if (carSetData.analysisResult.RAW_BETWEEN_DISTANCE > sectorData.maxBetweenDistanceCarSpeed) sectorData.maxBetweenDistanceCarSpeed = carSetData.analysisResult.RAW_BETWEEN_DISTANCE;

			// TTCe 결과 정리 변수
			sectorData.totalSumTTCE += carSetData.analysisResult.TTC_EXP;
			
			// 가중치 결과
			sectorData.totalWeightWA += carSetData.analysisResult.TTC_Weighted_Average;
			if (carSetData.analysisResult.TTC_Weighted_Average < sectorData.minWeightWA) sectorData.minWeightWA = carSetData.analysisResult.TTC_Weighted_Average;
			if (carSetData.analysisResult.TTC_Weighted_Average > sectorData.maxWeightWA) sectorData.maxWeightWA = carSetData.analysisResult.TTC_Weighted_Average;
			

			sectorData.totalWeightUniform += carSetData.analysisResult.TTC_Uniform_Kernel;
			if (carSetData.analysisResult.TTC_Uniform_Kernel < sectorData.minWeightUniform) sectorData.minWeightUniform = carSetData.analysisResult.TTC_Uniform_Kernel;
			if (carSetData.analysisResult.TTC_Uniform_Kernel > sectorData.maxWeightUniform) sectorData.maxWeightUniform = carSetData.analysisResult.TTC_Uniform_Kernel;

			sectorData.totalWeightTW += carSetData.analysisResult.TTC_Triweight_Kernel;
			if (carSetData.analysisResult.TTC_Triweight_Kernel < sectorData.minWeightTW) sectorData.minWeightTW = carSetData.analysisResult.TTC_Triweight_Kernel;
			if (carSetData.analysisResult.TTC_Triweight_Kernel > sectorData.maxWeightTW) sectorData.maxWeightTW = carSetData.analysisResult.TTC_Triweight_Kernel;
			
			sectorData.totalWeightGaussian += carSetData.analysisResult.TTC_Gaussian_Kernel;
			if (carSetData.analysisResult.TTC_Gaussian_Kernel < sectorData.minWeightGaussian) sectorData.minWeightGaussian = carSetData.analysisResult.TTC_Gaussian_Kernel;
			if (carSetData.analysisResult.TTC_Gaussian_Kernel > sectorData.maxWeightGaussian) sectorData.maxWeightGaussian = carSetData.analysisResult.TTC_Gaussian_Kernel;
		}
		
		if (analysisDataModel.getExtractCarSetList() != null)
		for (ExtractCarSet carSetData : analysisDataModel.getExtractCarSetList()) {
			if (carSetData.getAnalysisResult().TTC_T <= 0) continue;
			if (Double.isNaN(carSetData.analysisResult.FCRI_TTC_Weighted_Average) == true || carSetData.analysisResult.FCRI_TTC_Weighted_Average == 0.0) continue;
			if (Double.isNaN(carSetData.analysisResult.FCRI_TTC_Uniform_Kernel) == true || carSetData.analysisResult.FCRI_TTC_Uniform_Kernel == 0.0) continue;
			if (Double.isNaN(carSetData.analysisResult.FCRI_TTC_Triweight_Kernel) == true || carSetData.analysisResult.FCRI_TTC_Triweight_Kernel == 0.0) continue;
			if (Double.isNaN(carSetData.analysisResult.FCRI_TTC_Gaussian_Kernel) == true || carSetData.analysisResult.FCRI_TTC_Gaussian_Kernel == 0.0) continue;

			
			// TTC 결과 정리를 위한 변수 
			sectorData.totalSetCount++;
			sectorData.totalSumBaseCarSpeed += carSetData.analysisResult.RAW_BACK_SPPED_km_p_h;
			sectorData.totalSumTargetCarSpeed += carSetData.analysisResult.RAW_FRONT_SPPED_km_p_h;
			sectorData.totalSumBetweenDistance += carSetData.analysisResult.RAW_BETWEEN_DISTANCE;
			if (carSetData.analysisResult.RAW_BACK_SPPED_km_p_h < sectorData.minBaseCarSpeed) sectorData.minBaseCarSpeed = carSetData.analysisResult.RAW_BACK_SPPED_km_p_h;
			if (carSetData.analysisResult.RAW_BACK_SPPED_km_p_h > sectorData.maxBaseCarSpeed) sectorData.maxBaseCarSpeed = carSetData.analysisResult.RAW_BACK_SPPED_km_p_h;

			if (carSetData.analysisResult.RAW_BACK_SPPED_km_p_h < sectorData.minBaseCarSpeed) sectorData.minBaseCarSpeed = carSetData.analysisResult.RAW_BACK_SPPED_km_p_h;
			if (carSetData.analysisResult.RAW_BACK_SPPED_km_p_h > sectorData.maxBaseCarSpeed) sectorData.maxBaseCarSpeed = carSetData.analysisResult.RAW_BACK_SPPED_km_p_h;
			
			if (carSetData.analysisResult.RAW_FRONT_SPPED_km_p_h < sectorData.minTargetCarSpeed) sectorData.minTargetCarSpeed = carSetData.analysisResult.RAW_FRONT_SPPED_km_p_h;
			if (carSetData.analysisResult.RAW_FRONT_SPPED_km_p_h > sectorData.minTargetCarSpeed) sectorData.maxTargetCarSpeed = carSetData.analysisResult.RAW_FRONT_SPPED_km_p_h;
			
			if (carSetData.analysisResult.RAW_BETWEEN_DISTANCE < sectorData.minBetweenDistanceCarSpeed) sectorData.minBetweenDistanceCarSpeed = carSetData.analysisResult.RAW_BETWEEN_DISTANCE;
			if (carSetData.analysisResult.RAW_BETWEEN_DISTANCE > sectorData.maxBetweenDistanceCarSpeed) sectorData.maxBetweenDistanceCarSpeed = carSetData.analysisResult.RAW_BETWEEN_DISTANCE;

			// TTCe 결과 정리 변수
			sectorData.totalSumTTCE += carSetData.analysisResult.TTC_EXP;
			
			// 가중치 결과
			sectorData.totalWeightWA += carSetData.analysisResult.TTC_Weighted_Average;
			if (carSetData.analysisResult.TTC_Weighted_Average < sectorData.minWeightWA) sectorData.minWeightWA = carSetData.analysisResult.TTC_Weighted_Average;
			if (carSetData.analysisResult.TTC_Weighted_Average > sectorData.maxWeightWA) sectorData.minWeightWA = carSetData.analysisResult.TTC_Weighted_Average;
			

			sectorData.totalWeightUniform += carSetData.analysisResult.TTC_Uniform_Kernel;
			if (carSetData.analysisResult.TTC_Uniform_Kernel < sectorData.minWeightUniform) sectorData.minWeightUniform = carSetData.analysisResult.TTC_Uniform_Kernel;
			if (carSetData.analysisResult.TTC_Uniform_Kernel > sectorData.maxWeightUniform) sectorData.maxWeightUniform = carSetData.analysisResult.TTC_Uniform_Kernel;

			sectorData.totalWeightTW += carSetData.analysisResult.TTC_Triweight_Kernel;
			if (carSetData.analysisResult.TTC_Triweight_Kernel < sectorData.minWeightTW) sectorData.minWeightTW = carSetData.analysisResult.TTC_Triweight_Kernel;
			if (carSetData.analysisResult.TTC_Triweight_Kernel > sectorData.maxWeightTW) sectorData.maxWeightTW = carSetData.analysisResult.TTC_Triweight_Kernel;
			
			sectorData.totalWeightGaussian += carSetData.analysisResult.TTC_Gaussian_Kernel;
			if (carSetData.analysisResult.TTC_Gaussian_Kernel < sectorData.minWeightGaussian) sectorData.minWeightGaussian = carSetData.analysisResult.TTC_Gaussian_Kernel;
			if (carSetData.analysisResult.TTC_Gaussian_Kernel > sectorData.maxWeightGaussian) sectorData.maxWeightGaussian = carSetData.analysisResult.TTC_Gaussian_Kernel;
			
		}
		// FCRE 가속도 -  유클리드 가중평균 
		double accVal = analysisDataModel.getRawData().getCalACCEL();
		sectorData.FCRE_WA_SECTOR_TOT_ValueSum = analysisDataModel.getTOT_AnalysisResult().FCRI_TTC_Weighted_Average;
		sectorData.FCRE_UNIFORM_SECTOR_TOT_ValueSum = analysisDataModel.getTOT_AnalysisResult().FCRI_TTC_Uniform_Kernel;
		sectorData.FCRE_TRIWEIGHT_SECTOR_TOT_ValueSum = analysisDataModel.getTOT_AnalysisResult().FCRI_TTC_Triweight_Kernel;
		sectorData.FCRE_GAUSSIAN_SECTOR_TOT_ValueSum = analysisDataModel.getTOT_AnalysisResult().FCRI_TTC_Gaussian_Kernel;
		
		if (accVal > 6.0) {
			sectorData.FCRE_WA_Over6ValueSum += analysisDataModel.getTOT_AnalysisResult().FCRI_TTC_Weighted_Average;
			sectorData.FCRE_WA_Over6Cnt ++;
			sectorData.FCRE_UNIFORM_Over6ValueSum = analysisDataModel.getTOT_AnalysisResult().FCRI_TTC_Uniform_Kernel;
			sectorData.FCRE_UNIFORM_Over6Cnt ++;
			sectorData.FCRE_TRIWEIGHT_Over6ValueSum = analysisDataModel.getTOT_AnalysisResult().FCRI_TTC_Triweight_Kernel;
			sectorData.FCRE_TRIWEIGHT_Over6Cnt ++;
			sectorData.FCRE_GAUSSIAN_Over6ValueSum = analysisDataModel.getTOT_AnalysisResult().FCRI_TTC_Gaussian_Kernel;
			sectorData.FCRE_GAUSSIAN_Over6Cnt ++;		
		} else if (accVal > 4.0) {
			sectorData.FCRE_WA_Under6ValueSum += analysisDataModel.getTOT_AnalysisResult().FCRI_TTC_Weighted_Average;
			sectorData.FCRE_WA_Under6Cnt ++;
			sectorData.FCRE_UNIFORM_Under6ValueSum = analysisDataModel.getTOT_AnalysisResult().FCRI_TTC_Uniform_Kernel;
			sectorData.FCRE_UNIFORM_Under6Cnt ++;
			sectorData.FCRE_TRIWEIGHT_Under6ValueSum = analysisDataModel.getTOT_AnalysisResult().FCRI_TTC_Triweight_Kernel;
			sectorData.FCRE_TRIWEIGHT_Under6Cnt ++;
			sectorData.FCRE_GAUSSIAN_Under6ValueSum = analysisDataModel.getTOT_AnalysisResult().FCRI_TTC_Gaussian_Kernel;
			sectorData.FCRE_GAUSSIAN_Under6Cnt ++;						
		} else if (accVal > 2.0) {
			sectorData.FCRE_WA_Under4ValueSum += analysisDataModel.getTOT_AnalysisResult().FCRI_TTC_Weighted_Average;
			sectorData.FCRE_WA_Under4Cnt ++;
			sectorData.FCRE_UNIFORM_Under4ValueSum = analysisDataModel.getTOT_AnalysisResult().FCRI_TTC_Uniform_Kernel;
			sectorData.FCRE_UNIFORM_Under4Cnt ++;
			sectorData.FCRE_TRIWEIGHT_Under4ValueSum = analysisDataModel.getTOT_AnalysisResult().FCRI_TTC_Triweight_Kernel;
			sectorData.FCRE_TRIWEIGHT_Under4Cnt ++;
			sectorData.FCRE_GAUSSIAN_Under4ValueSum = analysisDataModel.getTOT_AnalysisResult().FCRI_TTC_Gaussian_Kernel;
			sectorData.FCRE_GAUSSIAN_Under4Cnt ++;						
		} else if (accVal > 0.0) {
			sectorData.FCRE_WA_Under2ValueSum += analysisDataModel.getTOT_AnalysisResult().FCRI_TTC_Weighted_Average;
			sectorData.FCRE_WA_Under2Cnt ++;
			sectorData.FCRE_UNIFORM_Under2ValueSum = analysisDataModel.getTOT_AnalysisResult().FCRI_TTC_Uniform_Kernel;
			sectorData.FCRE_UNIFORM_Under2Cnt ++;
			sectorData.FCRE_TRIWEIGHT_Under2ValueSum = analysisDataModel.getTOT_AnalysisResult().FCRI_TTC_Triweight_Kernel;
			sectorData.FCRE_TRIWEIGHT_Under2Cnt ++;
			sectorData.FCRE_GAUSSIAN_Under2ValueSum = analysisDataModel.getTOT_AnalysisResult().FCRI_TTC_Gaussian_Kernel;
			sectorData.FCRE_GAUSSIAN_Under2Cnt ++;						
		} else if (accVal > -2.0) {
			sectorData.FCRE_WA_Under0ValueSum += analysisDataModel.getTOT_AnalysisResult().FCRI_TTC_Weighted_Average;
			sectorData.FCRE_WA_Under0Cnt ++;
			sectorData.FCRE_UNIFORM_Under0ValueSum = analysisDataModel.getTOT_AnalysisResult().FCRI_TTC_Uniform_Kernel;
			sectorData.FCRE_UNIFORM_Under0Cnt ++;
			sectorData.FCRE_TRIWEIGHT_Under0ValueSum = analysisDataModel.getTOT_AnalysisResult().FCRI_TTC_Triweight_Kernel;
			sectorData.FCRE_TRIWEIGHT_Under0Cnt ++;
			sectorData.FCRE_GAUSSIAN_Under0ValueSum = analysisDataModel.getTOT_AnalysisResult().FCRI_TTC_Gaussian_Kernel;
			sectorData.FCRE_GAUSSIAN_Under0Cnt ++;						
		} else if (accVal > -4.0) {
			sectorData.FCRE_WA_UnderM2ValueSum += analysisDataModel.getTOT_AnalysisResult().FCRI_TTC_Weighted_Average;
			sectorData.FCRE_WA_UnderM2Cnt ++;
			sectorData.FCRE_UNIFORM_UnderM2ValueSum = analysisDataModel.getTOT_AnalysisResult().FCRI_TTC_Uniform_Kernel;
			sectorData.FCRE_UNIFORM_UnderM2Cnt ++;
			sectorData.FCRE_TRIWEIGHT_UnderM2ValueSum = analysisDataModel.getTOT_AnalysisResult().FCRI_TTC_Triweight_Kernel;
			sectorData.FCRE_TRIWEIGHT_UnderM2Cnt ++;
			sectorData.FCRE_GAUSSIAN_UnderM2ValueSum = analysisDataModel.getTOT_AnalysisResult().FCRI_TTC_Gaussian_Kernel;
			sectorData.FCRE_GAUSSIAN_UnderM2Cnt ++;		
		} else if (accVal > -6.0) {
			sectorData.FCRE_WA_UnderM4ValueSum += analysisDataModel.getTOT_AnalysisResult().FCRI_TTC_Weighted_Average;
			sectorData.FCRE_WA_UnderM4Cnt ++;
			sectorData.FCRE_UNIFORM_UnderM4ValueSum = analysisDataModel.getTOT_AnalysisResult().FCRI_TTC_Uniform_Kernel;
			sectorData.FCRE_UNIFORM_UnderM4Cnt ++;
			sectorData.FCRE_TRIWEIGHT_UnderM4ValueSum = analysisDataModel.getTOT_AnalysisResult().FCRI_TTC_Triweight_Kernel;
			sectorData.FCRE_TRIWEIGHT_UnderM4Cnt ++;
			sectorData.FCRE_GAUSSIAN_UnderM4ValueSum = analysisDataModel.getTOT_AnalysisResult().FCRI_TTC_Gaussian_Kernel;
			sectorData.FCRE_GAUSSIAN_UnderM4Cnt ++;					
		} else {
			sectorData.FCRE_WA_UnderM6ValueSum += analysisDataModel.getTOT_AnalysisResult().FCRI_TTC_Weighted_Average;
			sectorData.FCRE_WA_UnderM6Cnt ++;
			sectorData.FCRE_UNIFORM_UnderM6ValueSum = analysisDataModel.getTOT_AnalysisResult().FCRI_TTC_Uniform_Kernel;
			sectorData.FCRE_UNIFORM_UnderM6Cnt ++;
			sectorData.FCRE_TRIWEIGHT_UnderM6ValueSum = analysisDataModel.getTOT_AnalysisResult().FCRI_TTC_Triweight_Kernel;
			sectorData.FCRE_TRIWEIGHT_UnderM6Cnt ++;
			sectorData.FCRE_GAUSSIAN_UnderM6ValueSum = analysisDataModel.getTOT_AnalysisResult().FCRI_TTC_Gaussian_Kernel;
			sectorData.FCRE_GAUSSIAN_UnderM6Cnt ++;
		}
		sectorAnayze.put(sectorName, sectorData);
	}
}