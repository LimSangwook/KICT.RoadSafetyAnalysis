package Analysis.Analyze;

import Analysis.AnalysisDataModel;
import Analysis.AnalysisResult;
import RawData.RawDataModel;
import RoadInfo.RoadInfoManager;
import RoadInfo.RoadInfoModel;
import Utility.Util;

public class AnalyzeBasicCarSet extends Analyze {
	@Override
	protected void process(AnalysisDataModel data, RoadInfoManager roadInfoMGR) {
		RawDataModel rawData = data.getRawData();
		RoadInfoModel baseCarRoadInfo = roadInfoMGR.GetRoadInfoFromGPSTime(rawData.getGpsTime());
		if (baseCarRoadInfo == null) {
			baseCarRoadInfo = roadInfoMGR.GetRoadInfoFromGPSLocation(rawData.getLatitude(), rawData.getLongitude());
		}
		data.setBaseCarRoadInfo(baseCarRoadInfo);
		
		if (baseCarRoadInfo == null) { return;}
		
		data.setBASE_ID_L(Util.getBaseTargetTrackIdx(rawData));
	    if (data.getBASE_ID_L() < 0) { return;}
	    
        // 찾아진 선행 차량이 있을때 
		data.setBASE_ID_F(99); // Base차량은는 99로 한다.
		double BASE_SPEED = rawData.getSpeed(); 										// km 단위
	    double TARGET_Y = rawData.getTrackDatas().get(data.getBASE_ID_L()).Y;
		double TARGET_SPEED = rawData.getTrackDatas().get(data.getBASE_ID_L()).RangeRate; // 상대속 m/s
	    if (BASE_SPEED != 0) {
	    	data.setHeadway((TARGET_Y+4.7)/(BASE_SPEED/3.6));
	    }
//		System.out.print(rawData.getIndex() + "\t");
	    AnalysisResult baseAnalysisResult = AnalysisCarInfo(baseCarRoadInfo, BASE_SPEED, TARGET_SPEED, TARGET_Y);
	    data.setBaseAnalysisResult(baseAnalysisResult);
	    
	}

}
