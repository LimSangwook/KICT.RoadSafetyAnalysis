package Analysis.Model;

public class AnalysisResultModel {
	public double RAW_BACK_SPPED_km_p_h = 0.0;
	public double RAW_FRONT_SPPED_km_p_h = 0.0;
	public double RAW_BETWEEN_DISTANCE = 0.0;
	public double RAW_ACCEL = 0.0;
	
	
	public int 		SL = 0;			// 직선도로 제한속도 기준 안전판단  
	public int 		SS = 0;			// 곡선도로 안전속도 기준 안전판단
	public double 	SD_F = 0.0;		// 추종후행 정지거리 
	public double 	SD_L = 0.0;		// 추종선행 정지거리
	public int 		SDI = 0;			// 선후차량 정지거리 기준 안전판단 
	public double 	TTC_T = 0.0;		// 추돌까지 시간, 추돌하지 않으면 -1
	public int 		TTC = 0;			// 선후차량 정지시간 기준 안전판단
	public int 		TOT_SAFE = 0;	// 종합판단.

	// 2017.12.01 추가 ->
	// 산출된 TTC_Time을 Exponential Decay Function에 적용하여 0~1의 범위로 도출함 : EXP(-TTC_T) , TTC_T=-1 -> 0
	public double TTC_EXP = 0.0; 			

	// 함수별 가중치 값
	public double TTC_Uniform_Kernel = 0.0;		// Uniform Kernel Function(거리에 상관없이 일정)
	public double TTC_Triweight_Kernel = 0.0;	// Triweight Kernel Function(거리에 따른 가중치)
	public double TTC_Gaussian_Kernel = 0.0;		// Gaussian Kernel Function(정규분포로 가정)
	
	// TTC가 주체차량에 영향을 미치는 정도를 거리(d)에 따른 가중치로 계산하여 주체차량에 미치는 영향을 분석함
	public double TTC_Weighted_Average = 0.0; 	// 가중평균을 이용한 TTC 영향력 분석
	// 새로운 교통안전 지표(Forward Collision Risk Index)도출 
	public double FCRI_TTC_EXP = 0.0;
	public double FCRI_TTC_Uniform_Kernel = 0.0;
	public double FCRI_TTC_Triweight_Kernel = 0.0;
	public double FCRI_TTC_Gaussian_Kernel = 0.0;
	public double FCRI_TTC_Weighted_Average = 0.0;
	
	// <- 2017.12.01 추가
}
