package Analysis.Analyzer;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.HashMap;

import Analysis.Model.AnalysisDataModel;
import Analysis.Model.ExtractCarSet;
import RadarData.RadarDataModel;
import RadarData.RadarDataModel.TrackData;
import RoadInfo.RoadInfoManager;
import Utility.Util;
import Utility.Util.Deg2UTM;

public class AnalyzeExtractCarSet extends AnalyzerBase {
	public static int MAX_COUNT_SET = 0;
	@Override
	protected void process(AnalysisDataModel analysisDataModel, RoadInfoManager roadInfoMGR) {
		if (analysisDataModel.getBaseCarRoadInfo() == null)
			return;
		RadarDataModel rawData = analysisDataModel.getRawData();
		ArrayList<ExtractCarSet> extractCarSetList = GetExtractCarSetList(analysisDataModel);
		analysisDataModel.setExtractCarSetList(extractCarSetList);
		for (ExtractCarSet carSetData : extractCarSetList) {
			TrackData backCarTrackData = rawData.getTrackDatas().get(carSetData.backCarTrackIdx);
			// 방위각이 있으면 정확히 체크한다.
			if (rawData.getAzimouth() > -360.0) {
				double AzimouthRadian = -Math.toRadians(rawData.getAzimouth());
				double x = backCarTrackData.X;
				double y = backCarTrackData.Y;
				double newX = (x) * Math.cos(AzimouthRadian) - (y) * Math.sin(AzimouthRadian);
				double newY = (x) * Math.sin(AzimouthRadian) + (y) * Math.cos(AzimouthRadian);
				Deg2UTM deg2UTM = new Deg2UTM(rawData.getLatitude(), rawData.getLongitude());
				carSetData.roadInfoData = roadInfoMGR.GetRoadInfoFromUTM(deg2UTM.Easting + newX, deg2UTM.Northing + newY);
			} else {
				carSetData.roadInfoData = analysisDataModel.getBaseCarRoadInfo();
			}
			
            double BASE_SPEED_km_p_h = rawData.getSpeed() + rawData.getTrackDatas().get(carSetData.backCarTrackIdx).RangeRate * 3.6;   // km/h 로 바꿔준다.
            double TARGET_Y = rawData.getTrackDatas().get(carSetData.frontCarTrackIdx).Y - rawData.getTrackDatas().get(carSetData.backCarTrackIdx).Y + CAR_LENGTH;
            double TARGET_SPEED_km_p_h = rawData.getSpeed() + rawData.getTrackDatas().get(carSetData.frontCarTrackIdx).RangeRate * 3.6;						// km/h 단위
            
            carSetData.analysisResult = AnalysisCarInfo(carSetData.roadInfoData, BASE_SPEED_km_p_h, TARGET_SPEED_km_p_h, TARGET_Y);
		    carSetData.analysisResult.RAW_BACK_SPPED_km_p_h = BASE_SPEED_km_p_h;
		    carSetData.analysisResult.RAW_FRONT_SPPED_km_p_h = TARGET_SPEED_km_p_h;
		    carSetData.analysisResult.RAW_BETWEEN_DISTANCE = TARGET_Y;
		}
		MAX_COUNT_SET = Math.max(extractCarSetList.size(), MAX_COUNT_SET);
	}
	
	private ArrayList<ExtractCarSet> GetExtractCarSetList(AnalysisDataModel analysisDataModel) {
		// HashMap 		Key : lane	,	Value : ArrayList<TrackIdx>
		HashMap<Integer,ArrayList<Integer>> carSetMap = new HashMap<Integer,ArrayList<Integer>>();
		
		RadarDataModel rawData = analysisDataModel.getRawData();
		for (int trackIdx = 0 ; trackIdx < rawData.getTrackDatas().size() ; trackIdx++) {
			TrackData nowTrackData = rawData.getTrackDatas().get(trackIdx);
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
				Descending descending = new Descending(rawData);
		        Collections.sort(mapData, descending);
		        for (int idx = 0; idx < mapData.size() - 1 ; idx ++) {
			        	ExtractCarSet newExtractCarSet = new ExtractCarSet();
			        	newExtractCarSet.backCarTrackIdx = mapData.get(idx);
			        	newExtractCarSet.frontCarTrackIdx = mapData.get(idx + 1);
			        	// 차량간 거리가 차량 길이보다 긴것인지 확인함.
			        	if (rawData.getTrackDatas().get(newExtractCarSet.frontCarTrackIdx).Y - rawData.getTrackDatas().get(newExtractCarSet.backCarTrackIdx).Y > CAR_LENGTH)
			        		carSetList.add(newExtractCarSet);
		        }
			}
		}
		
        return carSetList;
	}
	
	public class Descending implements Comparator<Integer> {
		RadarDataModel rawData ;
	    public Descending(RadarDataModel rawData) {
			this.rawData = rawData;
		}

		@Override
	    public int compare(Integer o1, Integer o2) {
	    	double Y1 = rawData.getTrackDatas().get(o1).Y;
	    	double Y2 = rawData.getTrackDatas().get(o2).Y;
	        return Double.compare(Y1, Y2);
	    }
	}
}
