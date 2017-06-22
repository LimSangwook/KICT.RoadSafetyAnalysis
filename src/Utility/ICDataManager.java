package Utility;

import java.util.ArrayList;

public class ICDataManager {
	private static ICDataManager instance = new ICDataManager();

	class ICData {
		public ICData(String ICName, double latitude, double longitude) {
			this.ICName = ICName;
			this.Latitude = latitude;
			this.Longitude = longitude;
		}
		String ICName;
		double Latitude;
		double Longitude;
	}
	
	ArrayList<ICData> ICDataList;
	public static ICDataManager getInstance() {
		return instance;
	}
	
	private ICDataManager() {
		LoadICData();
	}
	private void LoadICData() {
		ICDataList = new ArrayList<ICData>();
		ICDataList.add(new ICData("판교JCT", 37.4058,127.0945));
		ICDataList.add(new ICData("성남", 37.4273,127.1233));
		ICDataList.add(new ICData("송파", 37.4742,127.1277));
		ICDataList.add(new ICData("서하남", 37.5125,127.1517));
		ICDataList.add(new ICData("하남JCT", 37.5314,127.1962));
		ICDataList.add(new ICData("상일", 37.549,127.1793));
		ICDataList.add(new ICData("강일", 37.5731,127.1649));
		ICDataList.add(new ICData("토평", 37.5823,127.1576));
		ICDataList.add(new ICData("남양주", 37.6006,127.1542));
		ICDataList.add(new ICData("구리", 37.6154,127.1416));
		ICDataList.add(new ICData("일산", 37.6361,126.8045));
		ICDataList.add(new ICData("자유로JCT", 37.6174,126.7988));
		ICDataList.add(new ICData("김포", 37.5974,126.7756));
		ICDataList.add(new ICData("노오지JCT", 37.5703,126.7533));
		ICDataList.add(new ICData("계양", 37.5439,126.7489));
		ICDataList.add(new ICData("서운JCT", 37.5286,126.7523));
		ICDataList.add(new ICData("중동", 37.5285,126.7523));
		ICDataList.add(new ICData("송내", 37.4877,126.7472));
		ICDataList.add(new ICData("장수", 37.4666,126.7547));
		ICDataList.add(new ICData("시흥", 37.4558,126.7973));
		ICDataList.add(new ICData("안현JCT", 37.4374,126.8166));
		ICDataList.add(new ICData("도리JCT", 37.3964,126.848));
		ICDataList.add(new ICData("조남JCT", 37.3687,126.8691));
		ICDataList.add(new ICData("산본", 37.3758,126.9318));
		ICDataList.add(new ICData("평촌", 37.3805,126.9713));
		ICDataList.add(new ICData("학의JCT", 37.3874,127.0017));
		
	}

	public String GetSectionName(double latitude, double longitude) {
		double minDistance = -1;
		int minDistanceIdx = -1;
		int idx = 0;
		for (ICData nowICData : ICDataList) {
	        double distance = Util.distance(nowICData.Latitude, nowICData.Longitude, latitude, longitude);
			
	        if (minDistance == -1) {
	            minDistance = distance;
	            minDistanceIdx = idx;
	            idx ++;
	            continue;
	        }
	        if (minDistance > distance) {
	            minDistance = distance;
	            minDistanceIdx = idx;
	        }
	        idx ++;
		}
		
		int preICIdx = -1, nextICIdx = -1;
		double preDistanceOfNow, nextDistanceOfNow, preDistanceOfMidIC, nextDistanceOfMidIC;
		if (minDistanceIdx == 0) {
			preICIdx = ICDataList.size() - 1;
		} else {
			preICIdx = minDistanceIdx - 1;
		}
		
		if (minDistanceIdx == ICDataList.size() - 1) {
			nextICIdx = 0;
		} else {
			nextICIdx = minDistanceIdx + 1;
		}

		preDistanceOfNow = Util.distance(ICDataList.get(preICIdx).Latitude, ICDataList.get(preICIdx).Longitude, latitude, longitude);
		nextDistanceOfNow = Util.distance(ICDataList.get(nextICIdx).Latitude, ICDataList.get(nextICIdx).Longitude, latitude, longitude);
		preDistanceOfMidIC = Util.distance(ICDataList.get(preICIdx).Latitude, ICDataList.get(preICIdx).Longitude, ICDataList.get(minDistanceIdx).Latitude, ICDataList.get(minDistanceIdx).Longitude);
		nextDistanceOfMidIC = Util.distance(ICDataList.get(nextICIdx).Latitude, ICDataList.get(nextICIdx).Longitude, ICDataList.get(minDistanceIdx).Latitude, ICDataList.get(minDistanceIdx).Longitude);

		String nearICName = "";
		if (preDistanceOfMidIC > preDistanceOfNow) {
			nearICName = ICDataList.get(preICIdx).ICName + " ~ " + ICDataList.get(minDistanceIdx).ICName;;
		} else if (nextDistanceOfMidIC > nextDistanceOfNow) {
			nearICName = ICDataList.get(minDistanceIdx).ICName + " ~ " + ICDataList.get(nextICIdx).ICName;
		} else {
			double preM = Math.abs(preDistanceOfMidIC - preDistanceOfNow);
			double nextM = Math.abs(nextDistanceOfMidIC - nextDistanceOfNow);
			if (preM > nextM) {
				nearICName = ICDataList.get(minDistanceIdx).ICName + " ~ " + ICDataList.get(nextICIdx).ICName;
			} else {
				nearICName = ICDataList.get(preICIdx).ICName + " ~ " + ICDataList.get(minDistanceIdx).ICName;;
				
			}
		}
		 
		return nearICName;
	}
}


