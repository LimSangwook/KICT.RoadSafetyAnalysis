package Analysis.Model;

import java.util.ArrayList;

import RadarData.RadarDataModel;
import RoadInfo.RoadInfoModel;

public class AnalysisDataModel {
	private RadarDataModel rawData;
	private RoadInfoModel baseCarRoadInfo;
	private double headway = 0;
	
	private ArrayList<ExtractCarSet> baseCarSetList;
	private ArrayList<ExtractCarSet> extractCarSetList;

	public double BASE_DISTANCE = 0.0; // 주행차량과의 거리 

	// Total Info
	int TOT_CD = 0;						// 추종 추종 갯수				
	AnalysisResultModel TOT_AnalysisResult;	// 모든 추종 처리 결과 합
	String TOT_SECTION = "";			// IC기반 구역 
	
	public double getHeadway() {
		return headway;
	}
	public void setHeadway(double headway) {
		this.headway = headway;
	}
	public void setRawData(RadarDataModel rawData) {
		this.rawData = rawData;
	}
	public void setBaseCarSetList(ArrayList<ExtractCarSet> carSetList) {
		this.baseCarSetList = carSetList;
	}
 
	public void setExtractCarSetList(ArrayList<ExtractCarSet> extractCarSetList) {
		this.extractCarSetList = extractCarSetList;
	}
//	public void setBASE_ID_F(int bASE_ID_F) {
//		BASE_ID_F = bASE_ID_F;
//	}
//	public void setBASE_ID_L(int bASE_ID_L) {
//		BASE_ID_L = bASE_ID_L;
//	}
	public void setTOT_CD(int tOT_CD) {
		TOT_CD = tOT_CD;
	}
	public void setTOT_AnalysisResult(AnalysisResultModel tOT_AnalysisResult) {
		TOT_AnalysisResult = tOT_AnalysisResult;
	}
	public void setTOT_SECTION(String tOT_SECTION) {
		TOT_SECTION = tOT_SECTION;
	}
	
	public double getALIGN_RADIUS() 	{ return (baseCarRoadInfo==null)?0:baseCarRoadInfo.getALIGN_RADIUS(); }
	public double getPROFILE_SLOPE() 	{ return (baseCarRoadInfo==null)?0:baseCarRoadInfo.getPROFILE_SLOPE(); }
	public double getCROSS_SLOPE_UP() 	{ return (baseCarRoadInfo==null)?0:baseCarRoadInfo.getCROSS_SLOPE_UP(); }
	public double getHEADWAY() 			{ return headway; }
//	public int getBASE_ID_F() {		return BASE_ID_F;	}
//	public int getBASE_ID_L() {		return BASE_ID_L;	}
//	public AnalysisResultModel getBaseAnalysisResult() {		return BaseAnalysisResult;	}
	public int getTOT_CD() {		return TOT_CD;	}
	public String getTOT_SECTION() {		return TOT_SECTION;	}
	public AnalysisResultModel getTOT_AnalysisResult() {		return TOT_AnalysisResult;	}
	public RadarDataModel getRawData() {		return rawData;	}
	public ArrayList<ExtractCarSet> getBaseCarSetList() {return baseCarSetList;	}
	public ArrayList<ExtractCarSet> getExtractCarSetList() {return extractCarSetList;	}
	
	public RoadInfoModel getBaseCarRoadInfo() {		return baseCarRoadInfo;	}

	public void setBaseCarRoadInfo(RoadInfoModel baseCarRoadInfo) {
		this.baseCarRoadInfo = baseCarRoadInfo;
	}
	public AnalysisDataModel(RadarDataModel rawData) {
		this.rawData = rawData;
	}
}


