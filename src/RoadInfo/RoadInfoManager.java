package RoadInfo;

import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

import com.vividsolutions.jts.geom.Coordinate;
import com.vividsolutions.jts.geom.Envelope;
import com.vividsolutions.jts.index.ItemVisitor;
import com.vividsolutions.jts.index.quadtree.Quadtree;

import Utility.Util.UTM;

public class RoadInfoManager {
	private static int SKIP_LINE = 1; 
	ArrayList<RoadInfoModel> datas;
	HashMap<Double, RoadInfoModel> GPSTimeMap;
	Quadtree spIndex;
	public RoadInfoManager(String path) throws IOException {
		Read(path);
	}

	private void Read(String path) throws IOException {
		System.out.printf("####\t START \tRoadInfoManager.Read()\n");
		BufferedReader in = new BufferedReader(new FileReader(path));
		datas = new ArrayList<RoadInfoModel>();
		String s;
		int lineIdx = 0;
		int dupSecondsCnt = 0;
		RoadInfoModel preData = null;
		GPSTimeMap = new HashMap<Double, RoadInfoModel>();
		spIndex = new Quadtree();
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
				datas.add(newData);
				GPSTimeMap.put(newData.getSECONDS(), newData);
				Coordinate pt = new Coordinate(newData.getX(), newData.getY());
				spIndex.insert(new Envelope(pt), newData);
				
				preData = newData;
			} catch( Exception e) {
				System.out.printf("File Parse Error - RoadInfoManager.Parse() (Line "+lineIdx+") : " + s + "\n");
			}
		}
		in.close();
		System.out.printf("####\t END \tRoadInfoManager.Read() - ReadLines : " + (datas.size() + dupSecondsCnt) + ", ValidData : " + datas.size() + " , Duplicated SECONDS Data : " + dupSecondsCnt + "\n");
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
		double z = Double.parseDouble(token[5]);
		double ALIGN_RADIUS = Double.parseDouble(token[38]);
		double PROFILE_SLOPE = Double.parseDouble(token[41]);
		double CROSS_SLOPE_UP = Double.parseDouble(token[46]);
		RoadInfoModel newData = new RoadInfoModel(index, seconds, x, y, z, ALIGN_RADIUS, PROFILE_SLOPE, CROSS_SLOPE_UP);
		return newData;
	}


	public RoadInfoModel GetRoadInfoFromGPSTime(double gpsTime) {
		return GPSTimeMap.get(gpsTime);
	}

	public RoadInfoModel GetRoadInfoFromTM(UTM utm) {
		for (int COVERAGE = 1; COVERAGE < 5 ; COVERAGE++ ) {
			double X = utm.X - 122919.823;
			double Y = utm.Y - 3607851.602;
			
			Coordinate pt1 = new Coordinate(X - COVERAGE, Y - COVERAGE); //UTM to TM
			Coordinate pt2 = new Coordinate(X + COVERAGE, Y + COVERAGE); //UTM to TM
			Envelope searchEnv = new Envelope(pt1, pt2);
			List<RoadInfoModel> list = spIndex.query(searchEnv);
			if (list.size() > 0) {
				return list.get(0);
			}
		}
		return null;
	}
}
