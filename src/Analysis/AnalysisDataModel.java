package Analysis;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.HashMap;

import RawData.RawDataModel;
import RawData.RawDataModel.TrackData;
import RoadInfo.RoadInfoModel;
import Utility.Util;
import RoadInfo.RoadInfoManager;

public class AnalysisDataModel {
	private static int LIMIT_SPEED = 110;
	private static int LIMIT_TTC = 3;
	
	private RawDataModel rawData;
	private RoadInfoModel baseCarRoadInfo;
	public RoadInfoModel getBaseCarRoadInfo() {
		return baseCarRoadInfo;
	}

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

	public class ExtractCarSet {
		int frontCarTrackIdx;
		int backCarTrackIdx;
		RoadInfoModel roadInfoData;
		AnalysisResult analysisResult;
		public int getFrontCarTrackIdx() {			return frontCarTrackIdx;		}
		public int getBackCarTrackIdx() {			return backCarTrackIdx;		}
		public RoadInfoModel getRoadInfoData() {			return roadInfoData;		}
		public AnalysisResult getAnalysisResult() {			return analysisResult;		}
	}
	
	public class AnalysisResult {
		int 	SL = 0;			// 직선도로 제한속도 안전판단  
		int 	SS = 0;			// 곡선도로 안전속도 안전판단
		double 	SD_F = 0.0;		// 추종후행 정지거리 
		double 	SD_L = 0.0;		// 추종선행 정지거리
		int 	SDI = 0;		// 선후차량 정지거리 안전판
		double 	TTC_T = 0.0;	// 추돌까지 시간
		int 	TTC = 0;		// 선후차량 정지시간
		int 	TOT_SAFE = 0;	// 종합판단.
		public int getSL() {			return SL;		}
		public int getSS() {			return SS;		}
		public double getSD_F() {			return SD_F;		}
		public double getSD_L() {			return SD_L;		}
		public int getSDI() {		return SDI;		}
		public double getTTC_T() {		return TTC_T;		}
		public int getTTC() {			return TTC;		}
		public int getTOT_SAFE() {			return TOT_SAFE;		}
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

	public AnalysisDataModel(RawDataModel rawData, RoadInfoManager roadInfoMGR) {
		this.rawData = rawData;
		this.baseCarRoadInfo = roadInfoMGR.GetRoadInfoFromGPSTime(rawData.getGpsTime());
		if (this.baseCarRoadInfo == null) {
			this.baseCarRoadInfo = roadInfoMGR.GetRoadInfoFromTM(rawData.getLatitude(), rawData.getLongitude());
		}
		
		AnalysisBaseCarSetInfo(roadInfoMGR);
		AnalysisExtractCarSetInfo();
		AnalysisTotalCarSetInfo();
	}
	
	private AnalysisResult AnalysisCarInfo(RoadInfoModel roadInfo, double BASE_SPEED, double TARGET_SPEED, double TARGET_Y ){
		// baseSpeed km/h 단위 
		// targetSpeed m/s 단위 
		AnalysisResult result = new AnalysisResult();
		if (roadInfo == null) { // 이건 없을수 있다.
			return result;
		}
		
		double ALIGN_RADIUS = roadInfo.getALIGN_RADIUS();
        double PROFILE_SLOPE = roadInfo.getPROFILE_SLOPE();
        double CROSS_SLOPE_UP = roadInfo.getCROSS_SLOPE_UP();
        double HEADWAY = (TARGET_Y + 4.7)/(BASE_SPEED);
        double safeSpeed =-1;
        if (ALIGN_RADIUS == 0.0) {// 직선부
            if (BASE_SPEED > LIMIT_SPEED) {
            	result.SS = 1;
            }
        } else { // 곡선부 
            safeSpeed = Math.pow(ALIGN_RADIUS*127*((Math.abs(CROSS_SLOPE_UP)/100)+0.11), 0.5);
            if (safeSpeed - BASE_SPEED <= 0 ) {
            	result.SL = 1;
            }
		}

        //후행차량 정지 거리;
        result.SD_F = ((BASE_SPEED/3.6)*2.5)+(Math.pow(BASE_SPEED/3.6,2))/(254*(0.11+((Math.abs(PROFILE_SLOPE))/100)));       	
        result.SD_L = (TARGET_SPEED+(BASE_SPEED/3.6))*HEADWAY+(Math.pow(TARGET_SPEED+(BASE_SPEED/3.6),2))/(254*(0.11+((Math.abs(ALIGN_RADIUS)/100)))+4.7);   //선행차량 정지 거리;	
        result.TTC_T= (TARGET_Y)/(TARGET_SPEED/3.6-(TARGET_SPEED+TARGET_SPEED/3.6));
        if (result.TTC_T < 0) {
        	result.TTC_T = -1;
        }
        
        // TTC_First
        if (result.SS == 0 && result.SL == 0) {
	        if (result.TTC_T > LIMIT_TTC)  {
	        	result.TTC = 1;
	        }
	        if (result.TTC == 0 && result.SD_L < result.SD_F) {
	        	result.SDI = 1;
	        }
        }        
        
        if (result.SS == 1 || result.SL == 1 || result.SDI == 1 || result.TTC ==1) {
        	result.TOT_SAFE = 1;
        }
        return result;
	}
	
	private void AnalysisBaseCarSetInfo(RoadInfoManager roadInfoMGR) {
		if (this.baseCarRoadInfo == null) { return;}
		
		this.BASE_ID_L = Util.getBaseTargetTrackIdx(rawData);
	    if (BASE_ID_L < 0) { return;}
	    
        // 찾아진 선행 차량이 있을때 
		BASE_ID_F = 99; // Base차량은는 99로 한다.
		double BASE_SPEED = rawData.getSpeed(); 										// km 단위
	    double TARGET_Y = rawData.getTrackDatas().get(BASE_ID_L).Y;
		double TARGET_SPEED = rawData.getSpeed()/3.6 + rawData.getTrackDatas().get(BASE_ID_L).RangeRate; // m/s 
	    if (BASE_SPEED != 0) {
	     	this.headway = (TARGET_Y+4.7)/(BASE_SPEED/3.6);
	    }
//		System.out.print(rawData.getIndex() + "\t");
	    BaseAnalysisResult = AnalysisCarInfo(this.baseCarRoadInfo, BASE_SPEED, TARGET_SPEED, TARGET_Y);
	}
	
	private void AnalysisExtractCarSetInfo() {
		if (this.baseCarRoadInfo == null)
			return;
		extractCarSetList = GetExtractCarSetList();
		for (ExtractCarSet carSetData : extractCarSetList) {
//			TrackData backCarTrackData = rawData.getTrackDatas().get(carSetData.backCarTrackIdx);
//			Deg2UTM deg2UTM = new Deg2UTM(rawData.getLatitude(), rawData.getLongitude());;
//			UTM utm = new UTM();
//			utm.X = deg2UTM.Easting + backCarTrackData.X;
//			utm.Y = deg2UTM.Northing + backCarTrackData.Y;
//			carSetData.roadInfoData = roadInfoMGR.GetRoadInfoFromTM(rawData.getLatitude(), rawData.getLongitude());
			if (this.baseCarRoadInfo !=null) {
				carSetData.roadInfoData = this.baseCarRoadInfo;
			}
			
            double BASE_SPEED = rawData.getSpeed() + rawData.getTrackDatas().get(carSetData.backCarTrackIdx).RangeRate * 3.6;   	// km/h 로 바꿔준다.
            double TARGET_Y = rawData.getTrackDatas().get(carSetData.frontCarTrackIdx).Y - rawData.getTrackDatas().get(carSetData.backCarTrackIdx).Y;
            double TARGET_SPEED = rawData.getSpeed()/3.6 + rawData.getTrackDatas().get(carSetData.frontCarTrackIdx).RangeRate; 		// m/s 단위
            
            carSetData.analysisResult = AnalysisCarInfo(carSetData.roadInfoData, BASE_SPEED, TARGET_SPEED, TARGET_Y);
		}
	}
	private ArrayList<ExtractCarSet> GetExtractCarSetList() {
		// HashMap 		Key : lane	,	Value : ArrayList<TrackIdx>
		HashMap<Integer,ArrayList<Integer>> carSetMap = new HashMap<Integer,ArrayList<Integer>>();
		int trackIdx = 0;
		for (TrackData nowTrackData : rawData.getTrackDatas()) {
			if (nowTrackData.isValid() == false) {
				trackIdx++;
				continue;
			}
			int lane = Util.getRelativeLaneNumber(nowTrackData.X);
			if (carSetMap.containsKey(lane) == false) {
				ArrayList<Integer> mapData = new ArrayList<Integer>();
				mapData.add(trackIdx);
				carSetMap.put(lane, mapData);
			} else {
				carSetMap.get(lane).add(trackIdx);
			}
			trackIdx++;
		}
		
        //2개 이상 모인것은 쌍으로 추출해 냄
		ArrayList<ExtractCarSet> carSetList = new ArrayList<ExtractCarSet>();
		for (Integer key : carSetMap.keySet()) {
			ArrayList<Integer> mapData = carSetMap.get(key);
			if (mapData.size() >= 2) {
				Descending descending = new Descending();
		        Collections.sort(mapData, descending);
		        for (int idx = 0; idx < mapData.size() - 1 ; idx ++) {
		        	ExtractCarSet newExtractCarSet = new ExtractCarSet();
		        	newExtractCarSet.backCarTrackIdx = mapData.get(idx);
		        	newExtractCarSet.frontCarTrackIdx = mapData.get(idx + 1);
		        	carSetList.add(newExtractCarSet);
		        }
			}
		}
		
        return carSetList;
	}
	private void AnalysisTotalCarSetInfo() {
        
        // 측량차랑 기준 - 같은차로에 차량이 있는경우
		if (this.BASE_ID_F == 99) TOT_CD++;
        TOT_AnalysisResult = new AnalysisResult();
        if (BaseAnalysisResult != null) {
	        TOT_AnalysisResult.SL += this.BaseAnalysisResult.SL; 
	        TOT_AnalysisResult.SS += this.BaseAnalysisResult.SS;
	        TOT_AnalysisResult.SDI += this.BaseAnalysisResult.SDI;
	        TOT_AnalysisResult.TTC += this.BaseAnalysisResult.TTC;
	        TOT_AnalysisResult.TOT_SAFE += this.BaseAnalysisResult.TOT_SAFE;
	        if ((this.BaseAnalysisResult.SL ==1 || this.BaseAnalysisResult.SS ==1 ) && (this.BaseAnalysisResult.SDI ==1 || this.BaseAnalysisResult.TTC ==1 ))
	        	System.out.println("########");
	        // ExtractCarSet 처리;
	        TOT_CD += extractCarSetList.size();
	        for (ExtractCarSet extractCarSet : extractCarSetList) {
	            TOT_AnalysisResult.SL += extractCarSet.analysisResult.SL;
	            TOT_AnalysisResult.SS += extractCarSet.analysisResult.SS;
	            TOT_AnalysisResult.SDI += extractCarSet.analysisResult.SDI;
	            TOT_AnalysisResult.TTC += extractCarSet.analysisResult.TTC;
	            TOT_AnalysisResult.TOT_SAFE += extractCarSet.analysisResult.TOT_SAFE;
	        }
        }
        TOT_SECTION = Util.GetSectionName(rawData.getLatitude(), rawData.getLongitude());
	}
	
	class Descending implements Comparator<Integer> {
	    @Override
	    public int compare(Integer o1, Integer o2) {
	    	double Y1 = rawData.getTrackDatas().get(o1).Y;
	    	double Y2 = rawData.getTrackDatas().get(o2).Y;
	        return Double.compare(Y1, Y2);
	    }
	 
	}
		
}


