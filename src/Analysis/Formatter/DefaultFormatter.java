package Analysis.Formatter;

import java.util.ArrayList;
import java.util.Iterator;

import Analysis.Analyzer.AnalyzeBasicCarSet;
import Analysis.Analyzer.AnalyzeExtractCarSet;
import Analysis.Model.AnalysisDataModel;
import Analysis.Model.AnalysisResultModel;
import Analysis.Model.ExtractCarSet;
import RadarData.RadarDataModel;

public class DefaultFormatter {
	public static DefaultFormatter instance = null;
	
	public static DefaultFormatter getInstance() {
		if (instance == null) {
			instance = new DefaultFormatter();
		}
		return instance;
	}

	public String getCSVFormatHeader() {
		String header = getHeaderOfDefault()
				+ ","+getHeaderOfTotalInfo()
				+ ","+getHeaderOfBaseCarSet()
				+ ","+getHeaderOfExtractCarSet();
		return header;
	}

	public String getCSVFormatData(AnalysisDataModel data) {
		String csv = getCSVOfDefaultInfo(data) 
				+ "," + getCSVOfTotalInfo(data)
				+ "," + getCSVOfBaseCarSet(data)
				+ "," + getCSVOfExtractCarSet(data) + "\n";
		return csv;
	}

	private String getHeaderOfDefault() {
		return "Index(distance),time(progress),week,GPSrealtime,Latitide,Longitide,Distance(m),차량속도(km/h),ALIGN_RADIUS,PROFILE_SLOPE,CROSS_SLOPE_UP,H(headway)";
	}
	private String getCSVOfDefaultInfo(AnalysisDataModel data) {
		RadarDataModel rawData = data.getRawData();
		return rawData.getIndex() + "," + rawData.getTime() 
				+ "," + rawData.getWeek() + "," + rawData.getGpsTime() 
				+ "," + rawData.getLatitude() + "," + rawData.getLongitude() 
				+ "," + rawData.getDistance() + "," + rawData.getSpeed() 
				+ "," + data.getALIGN_RADIUS() + "," + data.getPROFILE_SLOPE() 
				+ "," + data.getCROSS_SLOPE_UP() + "," + data.getHEADWAY();
	}


	private String getHeaderOfTotalInfo() {
		return "총 추종개수,총 제한속도 초과,총 안전속도 초과,총 SDI 위험 개수,총 TTC 초과(3초 기준) 개수,총 종합판단에 따른 위험판단 개수, 가속도(m/s^2),EXP FCRI,WeightedAverage FCRI, Uniform FCRI, Triweight FCRI, Gaussian FCRI,근접IC";
	}
	
	private String getCSVOfTotalInfo(AnalysisDataModel data) {
		AnalysisResultModel TOT_AnalysisResult = data.getTOT_AnalysisResult();
		if (TOT_AnalysisResult != null) {
			return data.getTOT_CD() 
					+ "," + TOT_AnalysisResult.SL + "," + TOT_AnalysisResult.SS 
					+ "," + TOT_AnalysisResult.SDI+ "," + TOT_AnalysisResult.TTC 
					+ "," + TOT_AnalysisResult.TOT_SAFE			
					+ "," + data.getRawData().getCalACCEL() 
					+ "," + TOT_AnalysisResult.FCRI_TTC_EXP
					+ "," + TOT_AnalysisResult.FCRI_TTC_Weighted_Average
					+ "," + TOT_AnalysisResult.FCRI_TTC_Uniform_Kernel
					+ "," + TOT_AnalysisResult.FCRI_TTC_Triweight_Kernel					
					+ "," + TOT_AnalysisResult.FCRI_TTC_Gaussian_Kernel					
					+ "," + data.getTOT_SECTION();
		} else {
			return data.getTOT_CD() + ",-1,-1,-1,-1,-1," + "-1,-1,-1,-1,-1," + data.getTOT_SECTION();
		}
	}
	
	private String getHeaderOfBaseCarSet() {
		String defaultHeader = "추종 후행,추종 선행,후행 속도(km/h),선행 속도(km/h),차량간 거리,후행차량정지거리,선행차량정지거리,TTC 시간,EXP_TTCw, WeightedAverage TTCw, Uniform TTCw,Triweight TTCw,Gaussian TTCw,";
		StringBuilder ret = new StringBuilder();
		for (int i = 0 ; i < AnalyzeBasicCarSet.MAX_COUNT_SET ; i++) {
			ret.append(defaultHeader);
		}
		return ret.toString();
	}
	
	private String getCSVOfBaseCarSet(AnalysisDataModel data) {
		if (data.getBaseCarSetList() == null) {
			return "";
		}
		return getCSVOfCarSet(data.getBaseCarSetList());
	}

	private String getHeaderOfExtractCarSet() {
		String defaultHeader = "추종 후행,추종 선행,후행 속도(km/h),선행 속도(km/h),차량간 거리,후행차량정지거리,선행차량정지거리,TTC 시간,EXP_TTCw, WeightedAverage TTC, Uniform TTCw,Triweight TTCw,Gaussian TTCw,";
		StringBuilder ret = new StringBuilder();
		for (int i = 0 ; i < AnalyzeExtractCarSet.MAX_COUNT_SET ; i++) {
			ret.append(defaultHeader);
		}
		ret.append('\n');
		return ret.toString();
	}
	
	private String getCSVOfExtractCarSet(AnalysisDataModel data) {
		if (data.getExtractCarSetList() == null) {
			return "";
		}
		return getCSVOfCarSet(data.getExtractCarSetList());
	}

	private String getCSVOfCarSet(ArrayList<ExtractCarSet> carSetList) {
		StringBuilder str = new StringBuilder();
		boolean isFirst = true;
		for (ExtractCarSet carSet : carSetList) {
			str.append(isFirst ? "" : ",").append(carSet.getBackCarTrackIdx())
				.append("," + carSet.getFrontCarTrackIdx())
				.append("," + carSet.getAnalysisResult().RAW_BACK_SPPED_km_p_h)
				.append("," + carSet.getAnalysisResult().RAW_FRONT_SPPED_km_p_h)
				.append("," + carSet.getAnalysisResult().RAW_BETWEEN_DISTANCE)
				.append("," + carSet.getAnalysisResult().SD_F)
				.append("," + carSet.getAnalysisResult().SD_L)
				.append("," + carSet.getAnalysisResult().TTC_T)
				.append("," + carSet.getAnalysisResult().TTC_EXP)
				.append("," + carSet.getAnalysisResult().TTC_Weighted_Average)
				.append("," + carSet.getAnalysisResult().TTC_Uniform_Kernel)
				.append("," + carSet.getAnalysisResult().TTC_Triweight_Kernel)
				.append("," + carSet.getAnalysisResult().TTC_Gaussian_Kernel);
			isFirst=false;
		}
		return str.toString();
	}

}
