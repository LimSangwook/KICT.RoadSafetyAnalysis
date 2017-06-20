package RawData;

import java.io.BufferedReader;
import java.io.FileReader;
import java.util.ArrayList;

import Utility.Util;

public class RawDataReaderA implements IRawDataReader {
	static private int SKIP_LINE = 17;

	@Override
	public ArrayList<RawDataModel> Read(String string) throws Exception {
		System.out.printf("####\t START \tRawDataReaderA.Read()\n");
		BufferedReader in = new BufferedReader(new FileReader(string));
		ArrayList<RawDataModel> dataModelList = new ArrayList<RawDataModel>();
		String s;
		int VALID_SPEED = 150; // VALID_SPEED 이상인 값은 버린다.
		int lineIdx = 0;
		int dupGPSTimeCnt = 0;
		int exceedSpeedCnt = 0;
		int parseErrorCnt = 0;
		RawDataModel preData = null;
		while ((s = in.readLine()) != null) {
			lineIdx ++;
			if (lineIdx <= SKIP_LINE) {
				continue;
			}
			try {
				RawDataModel newData = new RawDataModel(s);
				// 이전것과 GPS시간이 같다면 이전것과 현재것을 합친다.
				if (preData != null && newData.getGpsTime() == preData.getGpsTime()) {
					//System.out.printf("Found samed gpsTime Data - RawDataReaderA.Read() (Index "+preData.getIndex() + " and " + newData.getIndex()+") : GPSTime : " + preData.getGpsTime() + "\n");
					preData.MergeSameGPSTime(newData);
					dupGPSTimeCnt++;
					continue;
				}
				// 속도가 150KM 이상인것은 버린다.
				if (newData.getSpeed() >= VALID_SPEED) {
					exceedSpeedCnt++;
					continue;
				}
				if (preData != null) {
					double distance = Util.distance(newData.getLatitude(), newData.getLongitude(), preData.getLatitude(), preData.getLongitude(), "K") * 1000;
					newData.setDistance(distance);
				}
				dataModelList.add(newData);
				preData = newData;
			} catch( Exception e) {
				System.out.printf("File Parse Error - RawDataReaderA.Parse() (Line "+lineIdx+") : " + s + "\n");
				parseErrorCnt++;
			}
		}
		in.close();
		System.out.printf("\t ReadLines : " + lineIdx + "\tSkipLines : " + SKIP_LINE + "\tValidData : " + dataModelList.size() + "\tParseError : " + parseErrorCnt + "\tDuplicated GPSTime : " + dupGPSTimeCnt + "\tExceedSpeed("+VALID_SPEED+") : " + exceedSpeedCnt + "\n");
		System.out.printf("####\t END \tRawDataReaderA.Read()\n");
		return dataModelList;
	}
}
