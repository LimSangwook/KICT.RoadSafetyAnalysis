package Analysis;

import java.util.ArrayList;

import RawData.RawDataModel;
import RoadInfo.RoadInfoModel;

public class AnalysisDataModel {
	private RawDataModel rawData;
	private RoadInfoModel baseCarRoadInfo;
	private double headway = 0;
	

	private ArrayList<ExtractCarSet> extractCarSetList;
		
	// BaseCarSet Info
	int BASE_ID_F = -1;					// 추종후행 Index : Base 차량의 Index는 99라고 보면 된다.
	int BASE_ID_L = -1;					// 추종선행 Index: Base 차량과 짝을 이루는 선행차량. 있을수 있고(TrackIdx) 없을수 있다(-1)
	AnalysisResult BaseAnalysisResult; // 측량차량에 대한 처리 결과

	// Total Info
	int TOT_CD = 0;						// 추종 추종 갯수				
	AnalysisResult TOT_AnalysisResult;	// 모든 추종 처리 결과 합
	String TOT_SECTION = "";			// IC기반 구역 
	
	public double getHeadway() {
		return headway;
	}
	public void setHeadway(double headway) {
		this.headway = headway;
	}
	public void setRawData(RawDataModel rawData) {
		this.rawData = rawData;
	}
	public void setExtractCarSetList(ArrayList<ExtractCarSet> extractCarSetList) {
		this.extractCarSetList = extractCarSetList;
	}
	public void setBASE_ID_F(int bASE_ID_F) {
		BASE_ID_F = bASE_ID_F;
	}
	public void setBASE_ID_L(int bASE_ID_L) {
		BASE_ID_L = bASE_ID_L;
	}
	public void setTOT_CD(int tOT_CD) {
		TOT_CD = tOT_CD;
	}
	public void setTOT_AnalysisResult(AnalysisResult tOT_AnalysisResult) {
		TOT_AnalysisResult = tOT_AnalysisResult;
	}
	public void setTOT_SECTION(String tOT_SECTION) {
		TOT_SECTION = tOT_SECTION;
	}
	
	public double getALIGN_RADIUS() 	{ return (baseCarRoadInfo==null)?0:baseCarRoadInfo.getALIGN_RADIUS(); }
	public double getPROFILE_SLOPE() 	{ return (baseCarRoadInfo==null)?0:baseCarRoadInfo.getPROFILE_SLOPE(); }
	public double getCROSS_SLOPE_UP() 	{ return (baseCarRoadInfo==null)?0:baseCarRoadInfo.getCROSS_SLOPE_UP(); }
	public double getHEADWAY() 			{ return headway; }
	public int getBASE_ID_F() {		return BASE_ID_F;	}
	public int getBASE_ID_L() {		return BASE_ID_L;	}
	public AnalysisResult getBaseAnalysisResult() {		return BaseAnalysisResult;	}
	public int getTOT_CD() {		return TOT_CD;	}
	public String getTOT_SECTION() {		return TOT_SECTION;	}
	public AnalysisResult getTOT_AnalysisResult() {		return TOT_AnalysisResult;	}
	public RawDataModel getRawData() {		return rawData;	}
	public ArrayList<ExtractCarSet> getExtractCarSetList() {return extractCarSetList;	}
	public RoadInfoModel getBaseCarRoadInfo() {		return baseCarRoadInfo;	}

	public void setBaseCarRoadInfo(RoadInfoModel baseCarRoadInfo) {
		this.baseCarRoadInfo = baseCarRoadInfo;
	}
	public void setBaseAnalysisResult(AnalysisResult baseAnalysisResult) {
		BaseAnalysisResult = baseAnalysisResult;
	}

	public AnalysisDataModel(RawDataModel rawData) {
		this.rawData = rawData;
	}
}


