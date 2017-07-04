package Analysis;

import Analysis.AnalysisResult;
import RoadInfo.RoadInfoModel;

public class ExtractCarSet {
	public int frontCarTrackIdx;
	public int backCarTrackIdx;
	public RoadInfoModel roadInfoData;
	public AnalysisResult analysisResult;
	public int getFrontCarTrackIdx() {			return frontCarTrackIdx;		}
	public int getBackCarTrackIdx() {			return backCarTrackIdx;		}
	public RoadInfoModel getRoadInfoData() {			return roadInfoData;		}
	public AnalysisResult getAnalysisResult() {			return analysisResult;		}
}