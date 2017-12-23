package Analysis.Analyzer;

import java.util.ArrayList;

import Analysis.AnalysisManager.BaseTargetType;
import Analysis.Model.AnalysisDataModel;
import Analysis.Model.AnalysisResultModel;
import Analysis.Model.ExtractCarSet;
import RadarData.RadarDataModel;
import RadarData.RadarDataModel.TrackData;
import RoadInfo.RoadInfoManager;
import RoadInfo.RoadInfoModel;
import Utility.Util;

public class AnalyzeBasicCarSet extends AnalyzeBase {
	private BaseTargetType baseTargetType;
	public static int MAX_COUNT_SET = 0;

	public AnalyzeBasicCarSet(BaseTargetType baseTargetType) {
		this.baseTargetType = baseTargetType;
	}

	@Override
	protected void process(AnalysisDataModel analysisDataModel, RoadInfoManager roadInfoMGR) {
		RadarDataModel rawData = analysisDataModel.getRawData();
		RoadInfoModel baseCarRoadInfo = roadInfoMGR.GetRoadInfoFromGPSTime(rawData.getGpsTime());
		if (baseCarRoadInfo == null) {
			baseCarRoadInfo = roadInfoMGR.GetRoadInfoFromGPSLocation(rawData.getLatitude(), rawData.getLongitude());
		}
		analysisDataModel.setBaseCarRoadInfo(baseCarRoadInfo);
		
		if (baseCarRoadInfo == null) { return;}
		
		ArrayList<ExtractCarSet> baseCarMultiSetList = getBaseCarSetList(analysisDataModel, baseCarRoadInfo);
		setHeadway(analysisDataModel);
		ArrayList<ExtractCarSet> removeSet = new ArrayList<ExtractCarSet>();
		for (ExtractCarSet carSetData : baseCarMultiSetList) {
			double BASE_SPEED_km_p_h = rawData.getSpeed(); 										// km 단위
		    double TARGET_Y = rawData.getTrackDatas().get(carSetData.frontCarTrackIdx).Y;
			double TARGET_SPEED_km_p_h = BASE_SPEED_km_p_h + rawData.getTrackDatas().get(carSetData.frontCarTrackIdx).RangeRate * 3.6; // 상대속 m/s
		    AnalysisResultModel analysisResult = AnalysisCarInfo(baseCarRoadInfo, BASE_SPEED_km_p_h, TARGET_SPEED_km_p_h, TARGET_Y);
		    // Data Cleansing!
		    if (BASE_SPEED_km_p_h < 50 || BASE_SPEED_km_p_h > 150
		    		|| analysisResult.TTC_T <= 0 
		    		|| TARGET_SPEED_km_p_h < 30 || TARGET_SPEED_km_p_h > 200) {
		    		removeSet.add(carSetData);
		    }
		    
		    carSetData.analysisResult = analysisResult;
		    carSetData.analysisResult.RAW_BACK_ACCEL_m_p_ss = rawData.getCalACCEL();
		    carSetData.analysisResult.RAW_FRONT_ACCEL_m_p_ss = rawData.getTrackDatas().get(carSetData.frontCarTrackIdx).RangeAccel;
		    carSetData.analysisResult.RAW_BACK_SPPED_km_p_h = BASE_SPEED_km_p_h;
		    carSetData.analysisResult.RAW_FRONT_SPPED_km_p_h = TARGET_SPEED_km_p_h;
		    carSetData.analysisResult.RAW_BETWEEN_DISTANCE = TARGET_Y;
		}
		for (ExtractCarSet carSetData : removeSet) {
			baseCarMultiSetList.remove(carSetData);
		}
		analysisDataModel.setBaseCarSetList(baseCarMultiSetList);
		MAX_COUNT_SET = Math.max(baseCarMultiSetList.size(), MAX_COUNT_SET);
	}

	private void setHeadway(AnalysisDataModel analysisDataModel) {
		RadarDataModel rawData = analysisDataModel.getRawData();
		int idx = Util.getBaseTargetTrackIdx(rawData);
		if (idx != -1) {
			double BASE_SPEED_km_p_h = rawData.getSpeed(); 							// km/h 단위
		    double TARGET_Y = rawData.getTrackDatas().get(idx).Y;
		    if (BASE_SPEED_km_p_h != 0) {
		    		analysisDataModel.setHeadway((TARGET_Y+4.7)/(BASE_SPEED_km_p_h/3.6));	//m/s단
		    }
		}
	}

	private ArrayList<ExtractCarSet> getBaseCarSetList(AnalysisDataModel data, RoadInfoModel baseCarRoadInfo) {
		ArrayList<ExtractCarSet> resultSetList = new ArrayList<ExtractCarSet>();
		RadarDataModel rawData = data.getRawData();
		if (baseTargetType == BaseTargetType.SINGLE_BASE_TARGET) {
			int idx = Util.getBaseTargetTrackIdx(rawData);
			if (idx != -1) {
				ExtractCarSet carSet = new ExtractCarSet();
				carSet.backCarTrackIdx = 99;
				carSet.frontCarTrackIdx = idx;
				carSet.roadInfoData = baseCarRoadInfo;
				resultSetList.add(carSet);
			}
			return resultSetList;
		}
		
		for(int idx = 0 ; idx < rawData.getTrackDatas().size() ; idx ++) {
			TrackData nowTrackData = rawData.getTrackDatas().get(idx);
			if (Util.getRelativeLaneNumber(nowTrackData.X) != 0) {
				continue;
			}
			if (nowTrackData.isValid() == false) {
				continue;
			}
			
//			double SPEED_km_p_h = rawData.getSpeed() + rawData.getTrackDatas().get(idx).RangeRate * 3.6;
//			if (SPEED_km_p_h < 30 || SPEED_km_p_h > 200) 
//				continue;
			
			ExtractCarSet carSet = new ExtractCarSet();
			carSet.backCarTrackIdx = 99;
			carSet.frontCarTrackIdx = idx;
			carSet.roadInfoData = baseCarRoadInfo;
			resultSetList.add(carSet);
		}
		return resultSetList;
	}
}