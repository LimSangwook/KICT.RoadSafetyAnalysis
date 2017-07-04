package RoadInfo;

public class RoadInfoModel {
	private int index;
	private double SECONDS;
	private double X;
	private double Y;
	private double ALIGN_RADIU;
	private double PROFILE_SLOPE;
	private double CROSS_SLOPE_UP;
	
	private double latitude;
	private double longitude;
	
	private double UTM_X;
	private double UTM_Y;

	public void setLatitude(double latitude) {		this.latitude = latitude;	}
	public void setLongitude(double longitude) {		this.longitude = longitude;	}
	public void setUTM_X(double uTM_X) {		UTM_X = uTM_X;	}
	public void setUTM_Y(double uTM_Y) {		UTM_Y = uTM_Y;	}
	public double getLatitude() {		return latitude;	}
	public double getLongitude() {		return longitude;	}
	public double getUTM_X() {		return UTM_X;	}
	public double getUTM_Y() {		return UTM_Y;	}

	public RoadInfoModel(int index, double seconds, double x, double y, double ALIGN_RADIU, double PROFILE_SLOPE, double CROSS_SLOPE_UP) {
		this.index = index;
		this.SECONDS = seconds;
		this.X = x;
		this.Y = y;
		this.ALIGN_RADIU = ALIGN_RADIU;
		this.PROFILE_SLOPE = PROFILE_SLOPE;
		this.CROSS_SLOPE_UP = CROSS_SLOPE_UP;
	}
	
	public int getIndex() 				{	return index;	}
	public double getSECONDS() 			{	return SECONDS;	}
	public double getX() 				{	return X;};
	public double getY() 				{ 	return Y;};
	public double getALIGN_RADIUS() 	{	return ALIGN_RADIU;	}
	public double getPROFILE_SLOPE() 	{	return PROFILE_SLOPE;	}
	public double getCROSS_SLOPE_UP() 	{	return CROSS_SLOPE_UP;	}

	public void MergeSameGPSTime(RoadInfoModel newData) {
		// TODO Auto-generated method stub
		
	}

}
