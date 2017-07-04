package Analysis;

public class AnalysisResult {
	public int 	SL = 0;			// 직선도로 제한속도 안전판단  
	public int 	SS = 0;			// 곡선도로 안전속도 안전판단
	public double 	SD_F = 0.0;		// 추종후행 정지거리 
	public double 	SD_L = 0.0;		// 추종선행 정지거리
	public int 	SDI = 0;		// 선후차량 정지거리 안전판
	public double 	TTC_T = 0.0;	// 추돌까지 시간
	public int 	TTC = 0;		// 선후차량 정지시간
	public int 	TOT_SAFE = 0;	// 종합판단.
	
}