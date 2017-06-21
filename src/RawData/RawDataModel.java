package RawData;

import java.util.ArrayList;

public class RawDataModel {
	private int index;
	private int time;
	private int week;
	private double gpsTime;
	private double latitude;
	private double longitude;
	private double height;
	private double northVel;
	private double eastVel;
	private double upVel;
	private double roll;
	private double pitch;
	private double azimouth;
	private double insStatus;
	private double speed;
	private double distance = -1;
	private ArrayList<TrackData> trackDatas;
	
	public class TrackData {
		public double X;			// 단위 : m		
		public double Y;			// 단위 : m
		public double RangeRate;	// 단위 : m/s
		public boolean isValid() {
			if (this.Y == 0 && this.X == 0 ) 
				return false;
			return true;
		}
	}
	public RawDataModel() {
		trackDatas = new ArrayList<TrackData>(64);
		for (int idx = 0 ; idx < 64 ; idx ++) {
			TrackData newTrackData = new TrackData();
			trackDatas.add(newTrackData);
		}
	}
	public void SetTrackData(int index, double X, double Y, double RangeRate){
		TrackData trackData = trackDatas.get(index);
		trackData.X = X;
		trackData.Y = Y;
		trackData.RangeRate = RangeRate;
	}
	
	public int getIndex() {
		return index;
	}

	public void setIndex(int index) {
		this.index = index;
	}

	public int getTime() {
		return time;
	}

	public void setTime(int time) {
		this.time = time;
	}

	public int getWeek() {
		return week;
	}

	public void setWeek(int week) {
		this.week = week;
	}

	public double getGpsTime() {
		return gpsTime;
	}

	public void setGpsTime(double gpsTime) {
		this.gpsTime = gpsTime;
	}

	public double getLatitude() {
		return latitude;
	}

	public void setLatitude(double latitude) {
		this.latitude = latitude;
	}

	public double getLongitude() {
		return longitude;
	}

	public void setLongitude(double longitude) {
		this.longitude = longitude;
	}

	public double getHeight() {
		return height;
	}

	public void setHeight(double height) {
		this.height = height;
	}

	public double getNorthVel() {
		return northVel;
	}

	public void setNorthVel(double northVel) {
		this.northVel = northVel;
	}

	public double getEastVel() {
		return eastVel;
	}

	public void setEastVel(double eastVel) {
		this.eastVel = eastVel;
	}

	public double getUpVel() {
		return upVel;
	}

	public void setUpVel(double upVel) {
		this.upVel = upVel;
	}

	public double getRoll() {
		return roll;
	}

	public void setRoll(double roll) {
		this.roll = roll;
	}

	public double getPitch() {
		return pitch;
	}

	public void setPitch(double pitch) {
		this.pitch = pitch;
	}

	public double getAzimouth() {
		return azimouth;
	}

	public void setAzimouth(double azimouth) {
		this.azimouth = azimouth;
	}

	public double getInsStatus() {
		return insStatus;
	}

	public void setInsStatus(double insStatus) {
		this.insStatus = insStatus;
	}

	public double getSpeed() {
		return speed;
	}

	public void setSpeed(double speed) {
		this.speed = speed;
	}

	public ArrayList<TrackData> getTrackDatas() {
		return trackDatas;
	}

	public double getDistance() {
		return distance;
	}
	
	public void MergeSameGPSTime(RawDataModel newData) {
		// TODO Auto-generated method stub
	}
		
	public void setDistance(double distance) {
		this.distance = distance;
	}



}
