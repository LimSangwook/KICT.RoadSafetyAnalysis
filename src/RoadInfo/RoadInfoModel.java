package RoadInfo;

public class RoadInfoModel {
	public RoadInfoModel(int index, double seconds, double x, double y, double z, double ALIGN_RADIU, double PROFILE_SLOPE, double CROSS_SLOPE_UP) {
		this.setIndex(index);
		this.setSECONDS(seconds);
		this.X = x;
		this.Y = y;
		this.Z = z;
		this.ALIGN_RADIU = ALIGN_RADIU;
		this.PROFILE_SLOPE = PROFILE_SLOPE;
		this.CROSS_SLOPE_UP = CROSS_SLOPE_UP;
	}
	
	public double getSECONDS() {
		return SECONDS;
	}
	public void setSECONDS(double sECONDS) {
		SECONDS = sECONDS;
	}
	public double getX() { return X;};
	public double getY() { return Y;};
	public int getIndex() {
		return index;
	}

	public void setIndex(int index) {
		this.index = index;
	}

	private int index;
	private double SECONDS;
	private double X;
	private double Y;
	private double Z;
	private double ALIGN_RADIU;
	private double PROFILE_SLOPE;
	private double CROSS_SLOPE_UP;
	
	public void MergeSameGPSTime(RoadInfoModel newData) {
		// TODO Auto-generated method stub
		
	}

	public double getALIGN_RADIUS() {
		return ALIGN_RADIU;
	}

	public double getPROFILE_SLOPE() {
		return PROFILE_SLOPE;
	}

	public double getCROSS_SLOPE_UP() {
		return CROSS_SLOPE_UP;
	}
}
