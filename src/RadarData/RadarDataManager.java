package RadarData;

import java.io.BufferedReader;
import java.io.FileReader;
import java.util.ArrayList;

import RadarData.Controller.RadarDataController;
import Utility.Util;

public class RadarDataManager {
	public enum TYPE {A, B};
	private ArrayList<RadarDataModel> datas = new ArrayList<RadarDataModel>();

	public ArrayList<RadarDataModel> getDatas() {
		return datas;
	}

	public void add(String path, RadarData.Controller.RadarDataController.TYPE controllerType) throws Exception {
		Read(path, controllerType);
	}

	private void Read(String path, RadarData.Controller.RadarDataController.TYPE controllerType) throws Exception {
		RadarDataController controller = RadarDataController.getController(controllerType);
		System.out.printf("####\t START \tRawDataReader.Read() " + path + "\n");
		BufferedReader in = new BufferedReader(new FileReader(path));
		String s;
		int VALID_MAX_SPEED = 150; // VALID_SPEED 이상인 값은 버린다.
		int VALID_MIN_SPEED = 30; // 이하 버
		int lineIdx = 0;
		int dupGPSTimeCnt = 0;
		int exceedSpeedCnt = 0;
		int parseErrorCnt = 0;
		int validCnt = 0;
		RadarDataModel preData = null;
		
		double oldGPSTime = 0.0;
		double oldSpeed = 0.0;
		
		while ((s = in.readLine()) != null) {
			lineIdx ++;
			if (lineIdx <= controller.getSkipLines()) {
				continue;
			}
			try {
				RadarDataModel newData = controller.parseLine(s);
//				// 이전것과 GPS시간이 같다면 이전것과 현재것을 합친다.
//				if (preData != null && newData.getGpsTime() == preData.getGpsTime()) {
//					preData.MergeSameGPSTime(newData);
//					dupGPSTimeCnt++;
//					continue;
//				}
				
				// 가속도값 계산하
				if (oldGPSTime != 0.0 && oldGPSTime != newData.getGpsTime()) {
					double accel = (newData.getSpeed() - oldSpeed) / 3.6 / (newData.getGpsTime() - oldGPSTime);
					newData.setCalACCEL(accel);					
				}
				oldSpeed = newData.getSpeed();
				oldGPSTime = newData.getGpsTime();
				// 속도가 150KM 이상인것은 버린다. 그리고 가속도 +-10m/s/s 이상인 것도 버린다.
				if (newData.getSpeed() >= VALID_MAX_SPEED || newData.getSpeed() <= VALID_MIN_SPEED
						|| newData.getCalACCEL() < -20 || newData.getCalACCEL() > 20) {
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
		System.out.printf("\t ReadLines : " + lineIdx + "\tSkipLines : " + controller.getSkipLines() + "\tValidData : " + validCnt + "\tParseError : " + parseErrorCnt + "\tDuplicated GPSTime : " + dupGPSTimeCnt + "\tExceedSpeed("+VALID_MAX_SPEED+") : " + exceedSpeedCnt + "\n");
		System.out.printf("####\t END \tRawDataReader.Read() - RawData Data Size : " + datas.size() + "\n");
	}
}
