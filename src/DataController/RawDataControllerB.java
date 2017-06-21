package DataController;

import Analysis.AnalysisDataModel;
import RawData.RawDataModel;

public class RawDataControllerB extends RawDataControllr {

	@Override
	public int getSkipLines() {
		// TODO Auto-generated method stub
		return 7;
	}

	@Override
	public RawDataModel parseLine(String s) throws Exception {
		RawDataModel data = new RawDataModel();
		String[] token = s.split(",");
		if (token.length != 908) {
			throw new Exception("InValid Row");
		}
		
		data.setIndex(Integer.parseInt(token[0]));
		data.setGpsTime(Double.parseDouble(token[2]));
		data.setLatitude(Double.parseDouble(token[3]));
		data.setLongitude(Double.parseDouble(token[4]));
		data.setSpeed(Double.parseDouble(token[6]) * 3.6); // m/s -> km/h

		if (data.getLatitude() == 0 || data.getLongitude() == 0) {
			throw new Exception("InValid Lat Long Row");
		}	
		
		// TrackData
		for (int idx = 0 ; idx < 64 ; idx ++) {
			double X = Double.parseDouble(token[12 + idx * 6]);
			double Y = Double.parseDouble(token[13 + idx * 6]);
			double RangeAccel = Double.parseDouble(token[15 + idx * 7]);
			data.SetTrackData(idx, X, Y, RangeAccel);
		}
		return data;
	}

	@Override
	public String getCSVFormatHeader() {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public String getCSVFormatData(AnalysisDataModel data) {
		// TODO Auto-generated method stub
		return null;
	}

}
