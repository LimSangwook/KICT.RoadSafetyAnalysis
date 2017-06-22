package RawData;

import java.io.BufferedReader;
import java.io.FileReader;
import java.util.ArrayList;

import DataController.RawDataControllr;
import Utility.Util;

public class RawDataManager {
	public enum TYPE {A, B};
	private ArrayList<RawDataModel> datas = new ArrayList<RawDataModel>();

	public ArrayList<RawDataModel> getDatas() {
		return datas;
	}

	public void Add(String path, RawDataControllr dataTypeController) throws Exception {
		Read(path, dataTypeController);

	}

	private void Read(String path, RawDataControllr dataTypeController) throws Exception {
		System.out.printf("####\t START \tRawDataReader.Read() " + path + "\n");
		BufferedReader in = new BufferedReader(new FileReader(path));
		String s;
		int VALID_SPEED = 150; // VALID_SPEED 이상인 값은 버린다.
		int lineIdx = 0;
		int dupGPSTimeCnt = 0;
		int exceedSpeedCnt = 0;
		int parseErrorCnt = 0;
		int validCnt = 0;
		RawDataModel preData = null;
		while ((s = in.readLine()) != null) {
			lineIdx ++;
			if (lineIdx <= dataTypeController.getSkipLines()) {
				continue;
			}
			try {
				RawDataModel newData = dataTypeController.parseLine(s);
//				// 이전것과 GPS시간이 같다면 이전것과 현재것을 합친다.
//				if (preData != null && newData.getGpsTime() == preData.getGpsTime()) {
//					preData.MergeSameGPSTime(newData);
//					dupGPSTimeCnt++;
//					continue;
//				}
				// 속도가 150KM 이상인것은 버린다.
				if (newData.getSpeed() >= VALID_SPEED) {
					exceedSpeedCnt++;
					continue;
				}
				if (preData != null) {
					double distance = Util.distance(newData.getLatitude(), newData.getLongitude(), preData.getLatitude(), preData.getLongitude());
					newData.setDistance(distance);
				}
				validCnt++;
				datas.add(newData);
				preData = newData;
			} catch( Exception e) {
				System.out.printf("File Parse Error - RawDataReaderA.Parse() (Line "+lineIdx+") : " + s + "\n");
				parseErrorCnt++;
			}
		}
		in.close();
		System.out.printf("\t ReadLines : " + lineIdx + "\tSkipLines : " + dataTypeController.getSkipLines() + "\tValidData : " + validCnt + "\tParseError : " + parseErrorCnt + "\tDuplicated GPSTime : " + dupGPSTimeCnt + "\tExceedSpeed("+VALID_SPEED+") : " + exceedSpeedCnt + "\n");
		System.out.printf("####\t END \tRawDataReader.Read() - RawData Data Size : " + datas.size() + "\n");
	}
}
