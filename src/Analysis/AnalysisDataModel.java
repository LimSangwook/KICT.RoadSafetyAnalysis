package Analysis;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.HashMap;
import java.util.Iterator;

import RawData.RawDataModel;
import RawData.RawDataModel.TrackData;
import RoadInfo.RoadInfoModel;
import Utility.Util;
import Utility.Util.Deg2UTM;
import Utility.Util.UTM;
import RoadInfo.RoadInfoManager;

public class AnalysisDataModel {
	private static int LIMIT_SPEED = 110;
	private static int LIMIT_TTC = 3;
	
	private RawDataModel rawData;
	private RoadInfoModel baseCarRoadInfo;
	
	private RoadInfoManager roadDataMGR;
	private ArrayList<ExtractCarSet> extractCarSetList;
	private double headway = 0;
		
	// DefaultInfo
	public int getIndex() 				{ return rawData.getIndex(); }
	public int getTime() 				{ return rawData.getTime(); }
	public int getWeek() 				{ return rawData.getWeek(); }
	public double getGpsTime() 			{ return rawData.getGpsTime(); }
	public double getLatitude() 		{ return rawData.getLatitude(); }
	public double getLongitude() 		{ return rawData.getLongitude(); }
	public double getHeight() 			{ return rawData.getHeight(); }
	public double getNorthVel() 		{ return rawData.getNorthVel(); }
	public double getEastVel() 			{ return rawData.getEastVel(); }
	public double getUpVel() 			{ return rawData.getUpVel(); }
	public double getRoll() 			{ return rawData.getRoll(); }
	public double getPitch() 			{ return rawData.getPitch(); }
	public double getAzimouth() 		{ return rawData.getAzimouth(); }
	public double getINSstatus() 		{ return rawData.getINSstatus(); }
	public double getSpeed() 			{ return rawData.getSpeed(); }
	public double getALIGN_RADIUS() 	{ return (baseCarRoadInfo==null)?0:baseCarRoadInfo.getALIGN_RADIUS(); }
	public double getPROFILE_SLOPE() 	{ return (baseCarRoadInfo==null)?0:baseCarRoadInfo.getPROFILE_SLOPE(); }
	public double getCROSS_SLOPE_UP() 	{ return (baseCarRoadInfo==null)?0:baseCarRoadInfo.getCROSS_SLOPE_UP(); }
	public double getHEADWAY() 			{ return headway; }

	//
	class ExtractCarSet {
		int frontCarTrackIdx;
		int backCarTrackIdx;
		RoadInfoModel roadInfoData;
		AnalysisResult analysisResult;
	}
	
	class AnalysisResult {
		int 	SL = 0;			// 직선도로 제한속도 안전판단  
		int 	SS = 0;			// 곡선도로 안전속도 안전판단
		double 	SD_F = 0.0;		// 추종후행 정지거리 
		double 	SD_L = 0.0;		// 추종선행 정지거리
		int 	SDI = 0;		// 선후차량 정지거리 안전판
		double 	TTC_T = 0.0;	// 추돌까지 시간
		int 	TTC = 0;		// 선후차량 정지시간
		int 	TOT_SAFE = 0;	// 종합판단.
	}
	
	// BaseCarSet Info
	int BASE_ID_F = -1;					// 추종후행 Index : Base 차량의 Index는 99라고 보면 된다.
	int BASE_ID_L = -1;					// 추종선행 Index: Base 차량과 짝을 이루는 선행차량. 있을수 있고(TrackIdx) 없을수 있다(-1)
	AnalysisResult BASE_AnalysisResult; // 측량차량에 대한 처리 결과

	// Tot Info
	int TOT_CD = 0;						// 추종 추종 갯수				
	AnalysisResult TOT_AnalysisResult;	// 모든 추종 처리 결과 합
	String TOT_SECTION = "";			// IC기반 구역 
	
	public AnalysisDataModel(RawDataModel rawData, RoadInfoManager roadDataMGR) {
		if (rawData == null) {			// 이건 없을 수 없
			System.out.print("Error - new  AnalysisDefaultInfo() : rawData is null\n");
			return;
		}

		this.rawData = rawData;
		this.roadDataMGR = roadDataMGR;
		this.baseCarRoadInfo = roadDataMGR.GetRoadInfoFromGPSTime(rawData.getGpsTime());
//		if (this.baseCarRoadInfo == null) {
//			Deg2UTM deg2UTM = new Deg2UTM(rawData.getLatitude(), rawData.getLongitude());;
//			UTM utm = new UTM();
//			utm.X = deg2UTM.Easting;
//			utm.Y = deg2UTM.Northing;
//			this.baseCarRoadInfo = roadDataMGR.GetRoadInfoFromTM(utm);
//			System.out.println(this.baseCarRoadInfo.getIndex());
//		} 
		

		AnalysisDefaultInfo();
		AnalysisBaseCarSetInfo();
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
//        if (ALIGN_RADIUS != 0.0)
//        if (roadInfo.getIndex() != 39553) {
//        System.out.println(roadInfo.getIndex() + " \tALIGN_RADIUS : " + ALIGN_RADIUS + " \t BASE_SPEED : " + BASE_SPEED + "\tLIMIT_SPEED : " + LIMIT_SPEED 
//        		+ "\tsafeSpeed : " + safeSpeed);
//        }

        //후행차량 정지 거리;
        result.SD_F = ((BASE_SPEED/3.6)*2.5)+(Math.pow(BASE_SPEED/3.6,2))/(254*(0.11+((Math.abs(PROFILE_SLOPE))/100)));       	
        result.SD_L = (TARGET_SPEED+(BASE_SPEED/3.6))*HEADWAY+(Math.pow(TARGET_SPEED+(BASE_SPEED/3.6),2))/(254*(0.11+((Math.abs(ALIGN_RADIUS)/100)))+4.7);   //선행차량 정지 거리;	
        result.TTC_T= (TARGET_Y)/(TARGET_SPEED/3.6-(TARGET_SPEED+TARGET_SPEED/3.6));
        if (result.TTC_T < 0) {
        	result.TTC_T = -1;
        }
        
//        // OR
//        if (result.SS == 0 && result.SL == 0) {
//	        if (result.TTC_T > LIMIT_TTC)  {
//	        	result.TTC = 1;
//	        }
//	        if (result.SD_L -result.SD_F < 0) {
//	        	result.SDI = 1;
//	        }
//        }
//        // TTC_First
//        if (result.SS == 0 && result.SL == 0) {
//	        if (result.TTC_T > LIMIT_TTC)  {
//	        	result.TTC = 1;
//	        }
//	        if (result.TTC == 0 && result.SD_L -result.SD_F < 0) {
//	        	result.SDI = 1;
//	        }
//        }        
        // SDI_First
        if (result.SS == 0 && result.SL == 0) {
	        if (result.SD_L -result.SD_F < 0) {
	        	result.SDI = 1;
	        }
	        if (result.SDI == 0 && result.TTC_T > LIMIT_TTC)  {
	        	result.TTC = 1;
	        }
        }    
        
        
        if (result.SS == 1 || result.SL == 1 || result.SDI == 1 || result.TTC ==1) {
        	result.TOT_SAFE = 1;
        }
        return result;
	}
	private void AnalysisDefaultInfo() {
		if (this.BASE_ID_L == -1) {
			this.BASE_ID_L = Util.GetBaseTargetTrackIdx(rawData);
		}
		if (this.BASE_ID_L != -1) { 
	        BASE_ID_F = 99; // Base차량은는 99로 한다.
	        double SPEED = rawData.getSpeed();
	        double TARGET_Y = rawData.GetTrackDatas().get(BASE_ID_L).Y;
	        this.headway = (TARGET_Y+4.7)/(SPEED/3.6);
		}
	}
	private void AnalysisBaseCarSetInfo() {
		if (this.baseCarRoadInfo == null) { return;}
	    if (BASE_ID_L < 0) { return;}
         
        // 찾아진 선행 차량이 있을때 
		double BASE_SPEED = getSpeed(); 										// km 단위
		double TARGET_Y = rawData.GetTrackDatas().get(BASE_ID_L).Y;
		double TARGET_SPEED = rawData.getSpeed()/3.6 + rawData.GetTrackDatas().get(BASE_ID_L).RangeRate; // m/s 
//		System.out.print(rawData.getIndex() + "\t");
		BASE_AnalysisResult = AnalysisCarInfo(this.baseCarRoadInfo, BASE_SPEED, TARGET_SPEED, TARGET_Y);
	}
	
	private void AnalysisExtractCarSetInfo() {
		if (this.baseCarRoadInfo == null)
			return;
		extractCarSetList = GetExtractCarSetList();
		Iterator<ExtractCarSet> iter = extractCarSetList.iterator();
		while (iter .hasNext()) {
			ExtractCarSet carSetData = iter.next();
			TrackData backCarTrackData = rawData.GetTrackDatas().get(carSetData.backCarTrackIdx);
			Deg2UTM deg2UTM = new Deg2UTM(rawData.getLatitude(), rawData.getLongitude());;
			UTM utm = new UTM();
			utm.X = deg2UTM.Easting + backCarTrackData.X;
			utm.Y = deg2UTM.Northing + backCarTrackData.Y;
			carSetData.roadInfoData = roadDataMGR.GetRoadInfoFromTM(utm);
//			if (this.baseCarRoadInfo !=null) {
//				carSetData.roadInfoData = this.baseCarRoadInfo;
//			}
//			
            double BASE_SPEED = rawData.getSpeed() + rawData.GetTrackDatas().get(carSetData.backCarTrackIdx).RangeRate * 3.6;   	// km/h 로 바꿔준다.
            double TARGET_Y = rawData.GetTrackDatas().get(carSetData.frontCarTrackIdx).Y;
            double TARGET_SPEED = rawData.getSpeed()/3.6 + rawData.GetTrackDatas().get(carSetData.frontCarTrackIdx).RangeRate; 		// m/s 단위
            
            carSetData.analysisResult = AnalysisCarInfo(carSetData.roadInfoData, BASE_SPEED, TARGET_SPEED, TARGET_Y);
		}
	}
	private ArrayList<ExtractCarSet> GetExtractCarSetList() {
		// HashMap 		Key : lane	,	Value : ArrayList<TrackIdx>
		HashMap<Integer,ArrayList<Integer>> carSetMap = new HashMap<Integer,ArrayList<Integer>>();
		int trackIdx = 0;
		Iterator<TrackData> iter = rawData.GetTrackDatas().iterator();
		while (iter.hasNext()) {
			TrackData nowTrackData = iter.next();
			if (nowTrackData.isValid() == false) {
				trackIdx++;
				continue;
			}
			int lane = Util.GetLaneNumber(nowTrackData.X);
			if (lane == -1) {
				trackIdx++;
				continue;
			}
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
		Iterator<Integer> keyIter = carSetMap.keySet().iterator();
		ArrayList<ExtractCarSet> carSetList = new ArrayList<ExtractCarSet>();
		while(keyIter.hasNext()) {
			ArrayList<Integer> mapData = carSetMap.get(keyIter.next());
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
        if (BASE_AnalysisResult != null) {
	        TOT_AnalysisResult.SL += this.BASE_AnalysisResult.SL; 
	        TOT_AnalysisResult.SS += this.BASE_AnalysisResult.SS;
	        TOT_AnalysisResult.SDI += this.BASE_AnalysisResult.SDI;
	        TOT_AnalysisResult.TTC += this.BASE_AnalysisResult.TTC;
	        TOT_AnalysisResult.TOT_SAFE += this.BASE_AnalysisResult.TOT_SAFE;
	        if ((this.BASE_AnalysisResult.SL ==1 || this.BASE_AnalysisResult.SS ==1 ) && (this.BASE_AnalysisResult.SDI ==1 || this.BASE_AnalysisResult.TTC ==1 ))
	        	System.out.println("########");
	        // ExtractCarSet 처리;
	        TOT_CD += extractCarSetList.size();
	        Iterator<ExtractCarSet> extractIter = extractCarSetList.iterator();
	        while (extractIter.hasNext()) {
	        	ExtractCarSet extractCarSet = extractIter.next();
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
	    	double Y1 = rawData.GetTrackDatas().get(o1).Y;
	    	double Y2 = rawData.GetTrackDatas().get(o2).Y;
	        return Double.compare(Y1, Y2);
	    }
	 
	}
	public String GetDefaultInfoString() {
		String str = rawData.getIndex() + "," + rawData.getTime() + "," +  rawData.getWeek() + "," + rawData.getGpsTime() + "," 
				+ rawData.getLatitude() + "," + rawData.getLongitude() + "," + rawData.getDistance() + "," + rawData.getHeight() + "," + rawData.getNorthVel() + "," + rawData.getEastVel() 
				+ "," + rawData.getUpVel()  + "," + rawData.getRoll() + "," + rawData.getPitch() + "," + rawData.getAzimouth() + "," + rawData.getINSstatus()
				+ "," + rawData.getSpeed() + "," + this.getALIGN_RADIUS() + "," + this.getPROFILE_SLOPE() + "," + this.getCROSS_SLOPE_UP() + "," + this.getHEADWAY();
		return str;
	}

	
	public String GetTotalInfoString() {
		if (TOT_AnalysisResult == null)
			return "";
		if (TOT_AnalysisResult.SL >0 ){
			int k =1;
		}
		String str = this.TOT_CD + "," + TOT_AnalysisResult.SL  + "," + TOT_AnalysisResult.SS + "," + TOT_AnalysisResult.SDI + "," + TOT_AnalysisResult.TTC + "," + TOT_AnalysisResult.TOT_SAFE + "," + this.TOT_SECTION;
		return str;
	}
	public String GetBaseCarInfo() {
		String str;
		if (BASE_AnalysisResult == null) {
			str = BASE_ID_F + "," + BASE_ID_L + ",0,0,0";
		} else {
			str = BASE_ID_F + "," + BASE_ID_L + "," + this.BASE_AnalysisResult.SD_F + "," + this.BASE_AnalysisResult.SD_L + "," + this.BASE_AnalysisResult.TTC_T;
		}
		return str;
	}
	public String GetExtractCatInfo() {
		if (extractCarSetList == null) {
			return "";
		}
		Iterator<ExtractCarSet> iter = extractCarSetList.iterator();
		String str = new String();
		boolean isFirst = true;
		while (iter.hasNext()) {
			ExtractCarSet data = iter.next();
			str += (isFirst?"":",") + data.backCarTrackIdx + "," + data.frontCarTrackIdx + "," + data.analysisResult.SD_F + "," + data.analysisResult.SD_L + "," + data.analysisResult.TTC_T;
			isFirst=false;
		}
		return str.toString();
	}
}


