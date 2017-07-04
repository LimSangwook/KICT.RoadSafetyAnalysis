package RoadInfo;

import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

import com.vividsolutions.jts.geom.Coordinate;
import com.vividsolutions.jts.geom.Envelope;
import com.vividsolutions.jts.index.strtree.STRtree;

import DataController.RawDataControllr;
import RawData.RawDataManager;
import RawData.RawDataModel;
import Utility.Util;

public class RoadInfoManager {
	private static int SKIP_LINE = 1; 
	
	ArrayList<RoadInfoModel> datas = new ArrayList<RoadInfoModel>();
	HashMap<Double, RoadInfoModel> GPSTimeMap = new HashMap<Double, RoadInfoModel>();
	STRtree spIndex = new STRtree();
	RawDataManager locationDataManager = new RawDataManager();
	
	public void add(String path) throws IOException {
		Read(path);
	}

	private void Read(String path) throws IOException {
		System.out.printf("####\t START \tRoadInfoManager.Read()\n");
		BufferedReader in = new BufferedReader(new FileReader(path));
		String s;
		int lineIdx = 0;
		int dupSecondsCnt = 0;
		int validCnt = 0;
		RoadInfoModel preData = null;
		while ((s = in.readLine()) != null) {
			lineIdx ++;
			if (lineIdx <= SKIP_LINE) {
				continue;
			}
			try {
				RoadInfoModel newData = Parse(s);
				
				// 이전것과 GPS시간이 같다면 이전것과 현재것을 합친다.
				if (preData != null && newData.getSECONDS() == preData.getSECONDS()) {
					//System.out.printf("Found samed gpsTime Data - RoadInfoManager.Read() (Index "+preData.getIndex() + " and " + newData.getIndex()+") : SECONDS : " + preData.getSECONDS() + "\n");
					preData.MergeSameGPSTime(newData);
					dupSecondsCnt++;
					continue;
				}				
				validCnt++;
				datas.add(newData);
				GPSTimeMap.put(newData.getSECONDS(), newData);
				preData = newData;
			} catch( Exception e) {
				System.out.printf("File Parse Error - RoadInfoManager.Parse() (Line "+lineIdx+") : " + s + "\n");
			}
		}
		in.close();
		System.out.printf("\t ReadLines : " + (lineIdx -SKIP_LINE ) + ", now ValidData : " + validCnt + " , Duplicated SECONDS Data : " + dupSecondsCnt + "\n");
		System.out.printf("####\t END \tRoadInfoManager.Read() - RoadInfo Data Size : " + datas.size() + "\n");
	}

	private RoadInfoModel Parse(String line) throws Exception {
		String[] token = line.split(",");
		if (token.length != 75) {
			throw new Exception();
		}
		int index = Integer.parseInt(token[0]);
		double seconds = Double.parseDouble(token[1]);
		double x = Double.parseDouble(token[3]);
		double y = Double.parseDouble(token[4]);
		double ALIGN_RADIUS = Double.parseDouble(token[38]);
		double PROFILE_SLOPE = Double.parseDouble(token[41]);
		double CROSS_SLOPE_UP = Double.parseDouble(token[46]);
		RoadInfoModel newData = new RoadInfoModel(index, seconds, x, y, ALIGN_RADIUS, PROFILE_SLOPE, CROSS_SLOPE_UP);
		return newData;
	}

	public RoadInfoModel GetRoadInfoFromGPSTime(double gpsTime) {
		return GPSTimeMap.get(gpsTime);
	}
	public RoadInfoModel GetRoadInfoFromGPSLocation(double lat, double lon) {
		Util.Deg2UTM deg2UTM = new Util.Deg2UTM(lat, lon);
		return GetRoadInfoFromUTM(deg2UTM.Easting, deg2UTM.Northing);
	}
	public RoadInfoModel GetRoadInfoFromUTM(double X, double Y) {
		for (int COVERAGE = 1; COVERAGE < 2 ; COVERAGE++ ) {
			Coordinate pt1 = new Coordinate(X - COVERAGE, Y - COVERAGE); //UTM to TM
			Coordinate pt2 = new Coordinate(X + COVERAGE, Y + COVERAGE); //UTM to TM
			Envelope searchEnv = new Envelope(pt1, pt2);
			@SuppressWarnings("unchecked")
			List<RoadInfoModel> list = spIndex.query(searchEnv);
			if (list.size() > 0) {
//				System.out.println("idx : " + list.get(0).getIndex() + "\tX : " + X + " \t Y : " + Y + "\tInfoX : " + list.get(0).getUTM_X() + "\tInfoY : " + list.get(0).getUTM_Y());
				return list.get(0);
			}
		}
		return null;
	}

	public void addLocationData(String string, RawDataControllr rdControllerA) throws Exception {
		locationDataManager.add(string, rdControllerA);
	}

	public void syncLocationData() {
		for (RawDataModel locationData : locationDataManager.getDatas()) {
			RoadInfoModel roadInfoData = GPSTimeMap.get(locationData.getGpsTime());
			if (roadInfoData != null) {
				roadInfoData.setLatitude(locationData.getLatitude());
				roadInfoData.setLongitude(locationData.getLongitude());
				Util.Deg2UTM deg2UTM = new Util.Deg2UTM(locationData.getLatitude(), locationData.getLongitude());
				roadInfoData.setUTM_X(deg2UTM.Easting);
				roadInfoData.setUTM_Y(deg2UTM.Northing);
				Coordinate pt = new Coordinate(deg2UTM.Easting, deg2UTM.Northing);
				spIndex.insert(new Envelope(pt), roadInfoData);
			}
		}
		// TODO Auto-generated method stub
		
	}


}
