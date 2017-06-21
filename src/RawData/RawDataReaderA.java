package RawData;

public class RawDataReaderA extends RawDataReader {
	static private int SKIP_LINE = 17;

	@Override
	protected int getSkipLines() {
		return RawDataReaderA.SKIP_LINE;
	}

	@Override
	protected RawDataModel parseLine(String s) throws Exception {
		RawDataModel data = new RawDataModel();
		String[] token = s.split(",");
		if (token.length != 399) {
			throw new Exception("InValid Row");
		}
		
		data.setIndex(Integer.parseInt(token[0]));
		data.setTime(Integer.parseInt(token[1]));
		data.setWeek(Integer.parseInt(token[2]));
		data.setGpsTime(Double.parseDouble(token[3]));
		data.setLatitude(Double.parseDouble(token[4]));
		data.setLongitude(Double.parseDouble(token[5]));
		data.setHeight(Double.parseDouble(token[6]));
		data.setNorthVel(Double.parseDouble(token[7]));
		data.setEastVel(Double.parseDouble(token[8]));
		data.setUpVel(Double.parseDouble(token[9]));
		data.setRoll(Double.parseDouble(token[10]));
		data.setPitch(Double.parseDouble(token[11]));
		data.setAzimouth(Double.parseDouble(token[12]));
		data.setInsStatus(Double.parseDouble(token[13]));
		data.setSpeed(Double.parseDouble(token[14]));

		// TrackData
		for (int idx = 0 ; idx < 64 ; idx ++) {
			double X = Double.parseDouble(token[15 + idx * 6]);
			double Y = Double.parseDouble(token[16 + idx * 6]);
			double RangeAccel = Double.parseDouble(token[19 + idx * 6]);
			data.SetTrackData(idx, X, Y, RangeAccel);
		}
		return data;
	}
}
