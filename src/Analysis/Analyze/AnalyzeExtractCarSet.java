package Analysis.Analyze;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.HashMap;

import Analysis.AnalysisDataModel;
import Analysis.ExtractCarSet;
import RawData.RawDataModel;
import RawData.RawDataModel.TrackData;
import RoadInfo.RoadInfoManager;
import Utility.Util;
import Utility.Util.Deg2UTM;

public class AnalyzeExtractCarSet extends Analyze {

	@Override
	protected void process(AnalysisDataModel analysisDataModel, RoadInfoManager roadInfoMGR) {
		if (analysisDataModel.getBaseCarRoadInfo() == null)
			return;
		RawDataModel rawData = analysisDataModel.getRawData();
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
			
            double BASE_SPEED = rawData.getSpeed() + rawData.getTrackDatas().get(carSetData.backCarTrackIdx).RangeRate * 3.6;   	// km/h 로 바꿔준다.
            double TARGET_Y = rawData.getTrackDatas().get(carSetData.frontCarTrackIdx).Y - rawData.getTrackDatas().get(carSetData.backCarTrackIdx).Y;
            double TARGET_SPEED = rawData.getTrackDatas().get(carSetData.frontCarTrackIdx).RangeRate; 		// m/s 단위
            
            carSetData.analysisResult = AnalysisCarInfo(carSetData.roadInfoData, BASE_SPEED, TARGET_SPEED, TARGET_Y);
		}
	}
	private ArrayList<ExtractCarSet> GetExtractCarSetList(AnalysisDataModel analysisDataModel) {
		// HashMap 		Key : lane	,	Value : ArrayList<TrackIdx>
		HashMap<Integer,ArrayList<Integer>> carSetMap = new HashMap<Integer,ArrayList<Integer>>();
		
		RawDataModel rawData = analysisDataModel.getRawData();
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
				Descending descending = new Descending(rawData);
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
	public class Descending implements Comparator<Integer> {
		RawDataModel rawData ;
	    public Descending(RawDataModel rawData) {
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
