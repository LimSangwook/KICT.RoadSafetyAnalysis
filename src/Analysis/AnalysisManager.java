package Analysis;
import java.io.FileWriter;
import java.io.IOException;
import java.util.ArrayList;

import Analysis.Analyzer.AnalyzeBase;
import Analysis.Formatter.DefaultFormatter;
import Analysis.Analyzer.AnalyzeBasicCarSet;
import Analysis.Analyzer.AnalyzeExtractCarSet;
import Analysis.Analyzer.AnalyzeSector;
import Analysis.Analyzer.AnalyzeTotal;
import Analysis.Analyzer.SectorAnalyzeModel;
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
	
	private AnalyzeBase firstStepAnalyzer = null;
	
	public AnalysisManager(RadarDataManager rawDataManager, RoadInfoManager roadInfoManager, BaseTargetType baseTargetType) {
		this.rawDataMGR = rawDataManager;
		this.roadInfoMGR = roadInfoManager;
		this.baseTargetType = baseTargetType;
		
		firstStepAnalyzer = new AnalyzeBasicCarSet(baseTargetType);
		firstStepAnalyzer.next(new AnalyzeExtractCarSet()).next(new AnalyzeTotal()).next(new AnalyzeSector());
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
			//Data Cleansing
			if (data.getTOT_CD() == 0) continue;
			
			String str = defaultFormatter.getCSVFormatData(data);
			writer.write(str);
		}
		writer.close();
		System.out.printf("####	 END \t AnalysisManager.WriteCSV()\n");
	}
	
	// TTC 결과정리 
	public void REPORT_2(String path) throws IOException {
		System.out.printf("####	 START \t TTC 결과정리 .WriteCSV()\n");
		FileWriter writer = new FileWriter(path, false);
		writer.write("\uFEFF");
		writer.write("TTC 결과 정리 \n");
		writer.write("구간,선행차속도_평균,선행차속도_최대,선행차속도_최소,후행차속도_평균,후행차속도_최대,후행차속도_최소,차량이격거리_평균,차량이격거리_최대,차량이격거리_최소\n");
		
		SectorAnalyzeModel totModel = new SectorAnalyzeModel();
		for(String key : AnalyzeSector.sectorAnayze.keySet()) {
			SectorAnalyzeModel model = AnalyzeSector.sectorAnayze.get(key);
			double baseCarAVGSpeed = model.totalSumBaseCarSpeed/model.totalSetCount;
			double targetCarAVGpeed = model.totalSumTargetCarSpeed/model.totalSetCount;
			double distanceAVG = model.totalSumBetweenDistance/model.totalSetCount;
			
			writer.write(key + "," + baseCarAVGSpeed + "," + model.maxBaseCarSpeed + "," + model.minBaseCarSpeed
						+ "," + targetCarAVGpeed + "," + model.maxTargetCarSpeed + "," + model.minTargetCarSpeed
						+ "," + distanceAVG + "," + model.maxBetweenDistanceCarSpeed + "," + model.maxBetweenDistanceCarSpeed + "\n");

			totModel.totalSetCount += model.totalSetCount;
			totModel.totalSumBaseCarSpeed += model.totalSumBaseCarSpeed;
			totModel.totalSumTargetCarSpeed += model.totalSumTargetCarSpeed;
			totModel.totalSumBetweenDistance += model.totalSumBetweenDistance;
			totModel.maxBaseCarSpeed += model.maxBaseCarSpeed;
			totModel.minBaseCarSpeed += model.minBaseCarSpeed;
			totModel.maxTargetCarSpeed += model.maxTargetCarSpeed;
			totModel.minTargetCarSpeed += model.minTargetCarSpeed;
			totModel.maxBetweenDistanceCarSpeed += model.maxBetweenDistanceCarSpeed;
			totModel.maxBetweenDistanceCarSpeed += model.maxBetweenDistanceCarSpeed;
		}
		
		double baseCarAVGSpeed = totModel.totalSumBaseCarSpeed/totModel.totalSetCount;
		double targetCarAVGpeed = totModel.totalSumTargetCarSpeed/totModel.totalSetCount;
		double distanceAVG = totModel.totalSumBetweenDistance/totModel.totalSetCount;
		writer.write("전체," + baseCarAVGSpeed + "," + totModel.maxBaseCarSpeed + "," + totModel.minBaseCarSpeed
				+ "," + targetCarAVGpeed + "," + totModel.maxTargetCarSpeed + "," + totModel.minTargetCarSpeed
				+ "," + distanceAVG + "," + totModel.maxBetweenDistanceCarSpeed + "," + totModel.maxBetweenDistanceCarSpeed + "\n");

		
		writer.close();
		System.out.printf("####	 END \t TTC 결과정리 .WriteCSV()\n");
	}
	
	// TTCe 결과정리 
	public void REPORT_3(String path) throws IOException {
		System.out.printf("####	 START \t 3.TTCE 결과정리 .WriteCSV()\n");
		FileWriter writer = new FileWriter(path, false);
		writer.write("\uFEFF");
		writer.write("TTCE 결과 정리 \n");
		writer.write("구간,평균\n");
		double TOT_SumTTCE = 0;
		double TOT_Cnt = 0;
		for(String key : AnalyzeSector.sectorAnayze.keySet()) {
			SectorAnalyzeModel model = AnalyzeSector.sectorAnayze.get(key);
			double TTCeAVG = model.totalSumTTCE/model.totalSetCount;
			writer.write(key + "," + TTCeAVG +"\n");
			TOT_SumTTCE += model.totalSumTTCE;
			TOT_Cnt += model.totalSetCount;
		}
		writer.write("전체," + TOT_SumTTCE/TOT_Cnt);
		writer.close();
		System.out.printf("####	 END \t 3.TTCE 결과정리 .WriteCSV()\n");
	}
	
	// 가중치 결과정 결과정리 
	public void REPORT_4(String path) throws IOException {
		System.out.printf("####	 START \t 4.가중 결과정리 .WriteCSV()\n");
		FileWriter writer = new FileWriter(path, false);
		writer.write("\uFEFF");
		writer.write("가중 결과 정리 \n");
		writer.write("구간,유클리드_평균,유클리드_최대,유클리드_최소,Uniform_평균,Uniform_최대,Uniform_최소,triweight_평균,triweight_최대,triweight_최소,Gaussian_평균,Gaussian_최대,Gaussian_최소\n");
		SectorAnalyzeModel totModel = new SectorAnalyzeModel();
		for(String key : AnalyzeSector.sectorAnayze.keySet()) {
			SectorAnalyzeModel model = AnalyzeSector.sectorAnayze.get(key);
			double WA_AVG = model.totalWeightWA/model.totalSetCount;
			double Uniform_AVG = model.totalWeightUniform/model.totalSetCount;
			double TW_AVG = model.totalWeightTW/model.totalSetCount;
			double Gaussia_AVG = model.totalWeightGaussian/model.totalSetCount;
			
			writer.write("전체," + WA_AVG + "," + model.maxWeightWA + "," + model.minWeightWA
					+ "," + Uniform_AVG + "," + model.maxWeightUniform + "," + model.minWeightUniform
					+ "," + TW_AVG + "," + model.maxWeightTW + "," + model.minWeightTW
					+ "," + Gaussia_AVG + "," + model.maxWeightGaussian + "," + model.minWeightGaussian + "\n");
			
			totModel.totalWeightWA += model.totalWeightWA;
			totModel.totalWeightUniform += model.totalWeightUniform;
			totModel.totalWeightTW += model.totalWeightTW;
			totModel.totalWeightGaussian += model.totalWeightGaussian;
			totModel.totalSetCount += model.totalSetCount;
		}
		double WA_AVG = totModel.totalWeightUniform/totModel.totalSetCount;
		double Uniform_AVG = totModel.totalWeightUniform/totModel.totalSetCount;
		double TW_AVG = totModel.totalWeightTW/totModel.totalSetCount;
		double Gaussia_AVG = totModel.totalWeightGaussian/totModel.totalSetCount;
		writer.write("전체," + WA_AVG + ",,," + Uniform_AVG + ",,," + TW_AVG + ",,," + Gaussia_AVG + ",,\n");
		writer.close();
		System.out.printf("####	 END \t 4.가중 결과정리 .WriteCSV()\n");
	}
	
	// 가중치 비교 
	public void REPORT_6(String path) throws IOException {
		System.out.printf("####	 START \t 6.가중치 비교  .WriteCSV()\n");
		FileWriter writer = new FileWriter(path, false);
		writer.write("\uFEFF");
		writer.write("5. FCRE 가중치 비교  \n");
		writer.write("구간,가속도,유클리드_평균,Uniform_평균,triweight_평균,Gaussian_평균\n");
		SectorAnalyzeModel totModel = new SectorAnalyzeModel();
		for(String key : AnalyzeSector.sectorAnayze.keySet()) {
			SectorAnalyzeModel model = AnalyzeSector.sectorAnayze.get(key);
			
			writer.write((key + ",> 6," + model.FCRE_WA_Over6ValueSum/model.FCRE_WA_Over6Cnt + "," + model.FCRE_UNIFORM_Over6ValueSum/model.FCRE_UNIFORM_Over6Cnt + "," + model.FCRE_TRIWEIGHT_Over6ValueSum/model.FCRE_TRIWEIGHT_Over6Cnt + "," + model.FCRE_GAUSSIAN_Over6ValueSum/model.FCRE_GAUSSIAN_Over6Cnt + "\n").replace("NaN","0"));
			writer.write((key + ",<= 6," + model.FCRE_WA_Under6ValueSum/model.FCRE_WA_Under6Cnt + "," + model.FCRE_UNIFORM_Under6ValueSum/model.FCRE_UNIFORM_Under6Cnt + "," + model.FCRE_TRIWEIGHT_Under6ValueSum/model.FCRE_TRIWEIGHT_Under6Cnt + "," + model.FCRE_GAUSSIAN_Under6ValueSum/model.FCRE_GAUSSIAN_Under6Cnt + "\n").replace("NaN","0"));
			writer.write((key + ",<= 4," + model.FCRE_WA_Under4ValueSum/model.FCRE_WA_Under4Cnt + "," + model.FCRE_UNIFORM_Under4ValueSum/model.FCRE_UNIFORM_Under4Cnt + "," + model.FCRE_TRIWEIGHT_Under4ValueSum/model.FCRE_TRIWEIGHT_Under4Cnt + "," + model.FCRE_GAUSSIAN_Under4ValueSum/model.FCRE_GAUSSIAN_Under4Cnt + "\n").replace("NaN","0"));
			writer.write((key + ",<= 2," + model.FCRE_WA_Under2ValueSum/model.FCRE_WA_Under2Cnt + "," + model.FCRE_UNIFORM_Under2ValueSum/model.FCRE_UNIFORM_Under2Cnt + "," + model.FCRE_TRIWEIGHT_Under2ValueSum/model.FCRE_TRIWEIGHT_Under2Cnt + "," + model.FCRE_GAUSSIAN_Under2ValueSum/model.FCRE_GAUSSIAN_Under2Cnt + "\n").replace("NaN","0"));
			writer.write((key + ",<= 0," + model.FCRE_WA_Under0ValueSum/model.FCRE_WA_Under0Cnt + "," + model.FCRE_UNIFORM_Under0ValueSum/model.FCRE_UNIFORM_Under0Cnt + "," + model.FCRE_TRIWEIGHT_Under0ValueSum/model.FCRE_TRIWEIGHT_Under0Cnt + "," + model.FCRE_GAUSSIAN_Under0ValueSum/model.FCRE_GAUSSIAN_Under0Cnt + "\n").replace("NaN","0"));
			writer.write((key + ",<=-2," + model.FCRE_WA_UnderM2ValueSum/model.FCRE_WA_UnderM2Cnt + "," + model.FCRE_UNIFORM_UnderM2ValueSum/model.FCRE_UNIFORM_UnderM2Cnt + "," + model.FCRE_TRIWEIGHT_UnderM2ValueSum/model.FCRE_TRIWEIGHT_UnderM2Cnt + "," + model.FCRE_GAUSSIAN_UnderM2ValueSum/model.FCRE_GAUSSIAN_UnderM2Cnt + "\n").replace("NaN","0"));
			writer.write((key + ",<=-4," + model.FCRE_WA_UnderM4ValueSum/model.FCRE_WA_UnderM4Cnt + "," + model.FCRE_UNIFORM_UnderM4ValueSum/model.FCRE_UNIFORM_UnderM4Cnt + "," + model.FCRE_TRIWEIGHT_UnderM4ValueSum/model.FCRE_TRIWEIGHT_UnderM4Cnt + "," + model.FCRE_GAUSSIAN_UnderM4ValueSum/model.FCRE_GAUSSIAN_UnderM4Cnt + "\n").replace("NaN","0"));
			writer.write((key + ",<=-6," + model.FCRE_WA_UnderM6ValueSum/model.FCRE_WA_UnderM6Cnt + "," + model.FCRE_UNIFORM_UnderM6ValueSum/model.FCRE_UNIFORM_UnderM6Cnt + "," + model.FCRE_TRIWEIGHT_UnderM6ValueSum/model.FCRE_TRIWEIGHT_UnderM6Cnt + "," + model.FCRE_GAUSSIAN_UnderM6ValueSum/model.FCRE_GAUSSIAN_UnderM6Cnt + "\n").replace("NaN","0"));
			writer.write((key + ",전체," + model.FCRE_WA_SECTOR_TOT_ValueSum/model.totalSetCount + "," + model.FCRE_UNIFORM_SECTOR_TOT_ValueSum/model.totalSetCount + "," + model.FCRE_TRIWEIGHT_SECTOR_TOT_ValueSum/model.totalSetCount + "," + model.FCRE_GAUSSIAN_SECTOR_TOT_ValueSum/model.totalSetCount + "\n").replace("NaN","0"));
			
			totModel.addFCRE(model);
		}
		writer.write(("전체" + ",> 6," + totModel.FCRE_WA_Over6ValueSum/totModel.FCRE_WA_Over6Cnt + "," + totModel.FCRE_UNIFORM_Over6ValueSum/totModel.FCRE_UNIFORM_Over6Cnt + "," + totModel.FCRE_TRIWEIGHT_Over6ValueSum/totModel.FCRE_TRIWEIGHT_Over6Cnt + "," + totModel.FCRE_GAUSSIAN_Over6ValueSum/totModel.FCRE_GAUSSIAN_Over6Cnt + "\n").replace("NaN","0"));
		writer.write(("전체" + ",<= 6," + totModel.FCRE_WA_Under6ValueSum/totModel.FCRE_WA_Under6Cnt + "," + totModel.FCRE_UNIFORM_Under6ValueSum/totModel.FCRE_UNIFORM_Under6Cnt + "," + totModel.FCRE_TRIWEIGHT_Under6ValueSum/totModel.FCRE_TRIWEIGHT_Under6Cnt + "," + totModel.FCRE_GAUSSIAN_Under6ValueSum/totModel.FCRE_GAUSSIAN_Under6Cnt + "\n").replace("NaN","0"));
		writer.write(("전체" + ",<= 4," + totModel.FCRE_WA_Under4ValueSum/totModel.FCRE_WA_Under4Cnt + "," + totModel.FCRE_UNIFORM_Under4ValueSum/totModel.FCRE_UNIFORM_Under4Cnt + "," + totModel.FCRE_TRIWEIGHT_Under4ValueSum/totModel.FCRE_TRIWEIGHT_Under4Cnt + "," + totModel.FCRE_GAUSSIAN_Under4ValueSum/totModel.FCRE_GAUSSIAN_Under4Cnt + "\n").replace("NaN","0"));
		writer.write(("전체" + ",<= 2," + totModel.FCRE_WA_Under2ValueSum/totModel.FCRE_WA_Under2Cnt + "," + totModel.FCRE_UNIFORM_Under2ValueSum/totModel.FCRE_UNIFORM_Under2Cnt + "," + totModel.FCRE_TRIWEIGHT_Under2ValueSum/totModel.FCRE_TRIWEIGHT_Under2Cnt + "," + totModel.FCRE_GAUSSIAN_Under2ValueSum/totModel.FCRE_GAUSSIAN_Under2Cnt + "\n").replace("NaN","0"));
		writer.write(("전체" + ",<= 0," + totModel.FCRE_WA_Under0ValueSum/totModel.FCRE_WA_Under0Cnt + "," + totModel.FCRE_UNIFORM_Under0ValueSum/totModel.FCRE_UNIFORM_Under0Cnt + "," + totModel.FCRE_TRIWEIGHT_Under0ValueSum/totModel.FCRE_TRIWEIGHT_Under0Cnt + "," + totModel.FCRE_GAUSSIAN_Under0ValueSum/totModel.FCRE_GAUSSIAN_Under0Cnt + "\n").replace("NaN","0"));
		writer.write(("전체" + ",<=-2," + totModel.FCRE_WA_UnderM2ValueSum/totModel.FCRE_WA_UnderM2Cnt + "," + totModel.FCRE_UNIFORM_UnderM2ValueSum/totModel.FCRE_UNIFORM_UnderM2Cnt + "," + totModel.FCRE_TRIWEIGHT_UnderM2ValueSum/totModel.FCRE_TRIWEIGHT_UnderM2Cnt + "," + totModel.FCRE_GAUSSIAN_UnderM2ValueSum/totModel.FCRE_GAUSSIAN_UnderM2Cnt + "\n").replace("NaN","0"));
		writer.write(("전체" + ",<=-4," + totModel.FCRE_WA_UnderM4ValueSum/totModel.FCRE_WA_UnderM4Cnt + "," + totModel.FCRE_UNIFORM_UnderM4ValueSum/totModel.FCRE_UNIFORM_UnderM4Cnt + "," + totModel.FCRE_TRIWEIGHT_UnderM4ValueSum/totModel.FCRE_TRIWEIGHT_UnderM4Cnt + "," + totModel.FCRE_GAUSSIAN_UnderM4ValueSum/totModel.FCRE_GAUSSIAN_UnderM4Cnt + "\n").replace("NaN","0"));
		writer.write(("전체" + ",<=-6," + totModel.FCRE_WA_UnderM6ValueSum/totModel.FCRE_WA_UnderM6Cnt + "," + totModel.FCRE_UNIFORM_UnderM6ValueSum/totModel.FCRE_UNIFORM_UnderM6Cnt + "," + totModel.FCRE_TRIWEIGHT_UnderM6ValueSum/totModel.FCRE_TRIWEIGHT_UnderM6Cnt + "," + totModel.FCRE_GAUSSIAN_UnderM6ValueSum/totModel.FCRE_GAUSSIAN_UnderM6Cnt + "\n").replace("NaN","0"));
		writer.write(("전체" + ",전체," + totModel.FCRE_WA_SECTOR_TOT_ValueSum/totModel.totalSetCount + "," + totModel.FCRE_UNIFORM_SECTOR_TOT_ValueSum/totModel.totalSetCount + "," + totModel.FCRE_TRIWEIGHT_SECTOR_TOT_ValueSum/totModel.totalSetCount + "," + totModel.FCRE_GAUSSIAN_SECTOR_TOT_ValueSum/totModel.totalSetCount + "\n").replace("NaN","0"));
		writer.close();
		System.out.printf("####	 END \t  6.가중치 비교 .WriteCSV()\n");
	}
}
