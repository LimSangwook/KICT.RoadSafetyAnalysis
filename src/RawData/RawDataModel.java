package RawData;

import java.util.ArrayList;
import Utility.Util;

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

	public int getIndex() 			{	return index;}
	public int getTime() 			{	return time;}
	public int getWeek() 			{	return week;}
	public double getGpsTime() 		{	return gpsTime;}
	public double getLatitude() 	{	return latitude;}
	public double getLongitude() 	{	return longitude;}
	public double getHeight() 		{	return height;}
	public double getNorthVel() 	{	return northVel;}
	public double getEastVel() 		{	return eastVel;}
	public double getUpVel() 		{	return upVel;}
	public double getRoll() 		{	return roll;}
	public double getPitch() 		{	return pitch;}
	public double getAzimouth() 	{	return azimouth;}
	public double getINSstatus() 	{	return insStatus;}
	public double getSpeed() 		{	return speed;}
	public double getDistance() 	{	return distance;}
	public ArrayList<TrackData> GetTrackDatas() {	return trackDatas;}

	public class TrackData {
		public double X;			// 단위 : m		
		public double Y;			// 단위 : m
		public double LatRata;		// 단위 : m/s
		public double RangeRate;	// 단위 : m/s
		public double RangeAccel;	// 단위 : m/s
		public double AMP;
		public boolean isValid() {
			if (this.Y == 0 && this.X == 0 ) 
				return false;
			return true;
		}
	}
	
	public RawDataModel(String s) throws Exception {
		String[] token = s.split(",");
		if (token.length != 399) {
			throw new Exception("InValid Row");
		}
		
		index = Integer.parseInt(token[0]);
		time = Integer.parseInt(token[1]);
		week = Integer.parseInt(token[2]);
		gpsTime = Double.parseDouble(token[3]);
		latitude = Double.parseDouble(token[4]);
		longitude = Double.parseDouble(token[5]);
		height = Double.parseDouble(token[6]);
		northVel = Double.parseDouble(token[7]);
		eastVel = Double.parseDouble(token[8]);
		upVel = Double.parseDouble(token[9]);
		roll = Double.parseDouble(token[10]);
		pitch = Double.parseDouble(token[11]);
		azimouth = Double.parseDouble(token[12]);
		insStatus = Double.parseDouble(token[13]);
		speed = Double.parseDouble(token[14]);

		// TrackData
		trackDatas = new ArrayList<TrackData>(64);
		for (int idx = 0 ; idx < 64 ; idx ++) {
			TrackData newTrackData = new TrackData();
			newTrackData.X = Double.parseDouble(token[15 + idx * 6]);
			newTrackData.Y = Double.parseDouble(token[16 + idx * 6]);
			newTrackData.LatRata = Double.parseDouble(token[17 + idx * 6]);
			newTrackData.RangeRate = Double.parseDouble(token[18 + idx * 6]);
			newTrackData.RangeAccel = Double.parseDouble(token[19 + idx * 6]);
			newTrackData.AMP = Double.parseDouble(token[20 + idx * 6]);
			trackDatas.add(newTrackData);
		}
	}

	public void MergeSameGPSTime(RawDataModel newData) {
		// TODO Auto-generated method stub
	}
		
	public void setDistance(double distance) {
		this.distance = distance;
	}



}
