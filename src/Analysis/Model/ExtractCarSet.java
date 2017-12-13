package Analysis.Model;

import RoadInfo.RoadInfoModel;

public class ExtractCarSet {
	public int frontCarTrackIdx;
	public int backCarTrackIdx;
	public RoadInfoModel roadInfoData;
	public AnalysisResultModel analysisResult;
	
	public int getFrontCarTrackIdx() {			return frontCarTrackIdx;		}
	public int getBackCarTrackIdx() {			return backCarTrackIdx;		}
	public RoadInfoModel getRoadInfoData() {			return roadInfoData;		}
	public AnalysisResultModel getAnalysisResult() {			return analysisResult;		}
}