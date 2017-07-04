package DataController;

import java.util.Iterator;

import Analysis.AnalysisDataModel;
import Analysis.AnalysisResult;
import Analysis.ExtractCarSet;
import RawData.RawDataModel;

public class RawDataControllerA extends RawDataControllr {

	@Override
	public int getSkipLines() {
		return 17;
	}
	
	@Override
	public RawDataModel parseLine(String s) throws Exception {
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
		
		if (data.getLatitude() == 0 || data.getLongitude() == 0) {
			throw new Exception("InValid Lat Long Row");
		}

		// TrackData
		for (int idx = 0 ; idx < 64 ; idx ++) {
			double X = Double.parseDouble(token[15 + idx * 6]);
			double Y = Double.parseDouble(token[16 + idx * 6]);
			double RangeAccel = Double.parseDouble(token[19 + idx * 6]);
			data.SetTrackData(idx, X, Y, RangeAccel);
		}
		return data;
	}
	@Override
	public String getCSVFormatHeader() {
		String header = getHeaderOfDefault()
				+ ","+getHeaderOfTotalInfo()
				+ ","+getHeaderOfBaseCarSet()
				+ ","+getHeaderOfExtractCarSet();
		return header;
	}
	@Override
	public String getCSVFormatData(AnalysisDataModel data) {
		String csv = getCSVOfDefaultInfo(data) 
				+ "," + getCSVOfTotalInfo(data)
				+ "," + getCSVOfBaseCarSet(data)
				+ "," + getCSVOfExtractCarSet(data) + "\n";
		return csv;
	}

	private String getHeaderOfDefault() {
		return "Index(distance),time(progress),week,GPSrealtime,Latitide,Longitide,Distance(m),차량속도,ALIGN_RADIUS,PROFILE_SLOPE,CROSS_SLOPE_UP,H(headway)";
	}
	private String getCSVOfDefaultInfo(AnalysisDataModel data) {
		RawDataModel rawData = data.getRawData();
		return rawData.getIndex() + "," + rawData.getTime() 
				+ "," + rawData.getWeek() + "," + rawData.getGpsTime() 
				+ "," + rawData.getLatitude() + "," + rawData.getLongitude() 
				+ "," + rawData.getDistance() + "," + rawData.getSpeed() 
				+ "," + data.getALIGN_RADIUS() + "," + data.getPROFILE_SLOPE() 
				+ "," + data.getCROSS_SLOPE_UP() + "," + data.getHEADWAY();
	}


	private String getHeaderOfTotalInfo() {
		return "총 추종개수,총 제한속도 초과,총 안전속도 초과,총 SDI 위험 개수,총 TTC 초과(3초 기준) 개수,총 종합판단에 따른 위험판단 개수,근접IC";
	}
	private String getCSVOfTotalInfo(AnalysisDataModel data) {
		AnalysisResult TOT_AnalysisResult = data.getTOT_AnalysisResult();
		if (TOT_AnalysisResult != null) {
			return data.getTOT_CD() 
					+ "," + TOT_AnalysisResult.SL + "," + TOT_AnalysisResult.SS 
					+ "," + TOT_AnalysisResult.SDI+ "," + TOT_AnalysisResult.TTC 
					+ "," + TOT_AnalysisResult.TOT_SAFE + "," + data.getTOT_SECTION();
		} else {
			return data.getTOT_CD() + ",-1,-1,-1,-1,-1," + data.getTOT_SECTION();
		}
	}
	
	private String getHeaderOfBaseCarSet() {
		return "추종 후행,추종 선행,후행차량정지거리,선행차량정지거리,TTC 시간";
	}
	private String getCSVOfBaseCarSet(AnalysisDataModel data) {
		AnalysisResult baseResult = data.getBaseAnalysisResult();
		if (baseResult == null) {
			return "0,0,0,0,0";
		} else {
			return data.getBASE_ID_F() + "," + data.getBASE_ID_L() + "," + baseResult.SD_F
					+ "," + baseResult.SD_L + "," + baseResult.TTC_T;
		}
	}

	private String getHeaderOfExtractCarSet() {
		return "추종 후행,추종 선행,후행차량정지거리,선행차량정지거리,TTC 시간,추종 후행,추종 선행,후행차량정지거리,선행차량정지거리,TTC 시간\n";
	}
	private String getCSVOfExtractCarSet(AnalysisDataModel data) {
		if (data.getExtractCarSetList() == null) {
			return "";
		}
		StringBuilder str = new StringBuilder();
		boolean isFirst = true;
		Iterator<ExtractCarSet> iter = data.getExtractCarSetList().iterator();
		while (iter.hasNext()) {
			ExtractCarSet dataSet = iter.next();
			str.append(isFirst ? "" : ",").append(dataSet.getBackCarTrackIdx())
				.append("," + dataSet.getFrontCarTrackIdx())
				.append("," + dataSet.getAnalysisResult().SD_F)
				.append("," + dataSet.getAnalysisResult().SD_L)
				.append("," + dataSet.getAnalysisResult().TTC_T);
			isFirst=false;
		}
		return str.toString();
	}	
}
