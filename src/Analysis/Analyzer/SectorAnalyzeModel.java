package Analysis.Analyzer;

public class SectorAnalyzeModel {

	// TTC 결과 정리를 위한 변수 
	public double totalSumBaseCarSpeed;
	public double totalSumTargetCarSpeed;
	public int totalSetCount;
	public double totalSumBetweenDistance;
	public double minBaseCarSpeed = 999.0;
	public double maxBaseCarSpeed = 0.0;
	public double minTargetCarSpeed = 999.0;
	public double maxTargetCarSpeed = 0.0;
	public double minBetweenDistanceCarSpeed = 999.0;
	public double maxBetweenDistanceCarSpeed = 0.0;
	
	// TTCe 결과 정리 변수
	public double totalSumTTCE;
	
	// 가중치 결과
	public double totalWeightWA;
	public double minWeightWA;
	public double maxWeightWA;
	public double totalWeightUniform;
	public double minWeightUniform;
	public double maxWeightUniform;
	public double totalWeightTW;
	public double minWeightTW;
	public double maxWeightTW;
	public double totalWeightGaussian;
	public double minWeightGaussian;
	public double maxWeightGaussian;
	
	// 가속도 -  유클리드 가중평균 
	public double FCRE_WA_UnderM6ValueSum;
	public int FCRE_WA_UnderM6Cnt;
	public double FCRE_WA_UnderM4ValueSum;
	public int FCRE_WA_UnderM4Cnt;
	public double FCRE_WA_UnderM2ValueSum;
	public int FCRE_WA_UnderM2Cnt;
	public double FCRE_WA_Under0ValueSum;
	public int FCRE_WA_Under0Cnt;
	public double FCRE_WA_Under2ValueSum;
	public int FCRE_WA_Under2Cnt;
	public double FCRE_WA_Under4ValueSum;
	public int FCRE_WA_Under4Cnt;
	public double FCRE_WA_Under6ValueSum;
	public int FCRE_WA_Under6Cnt;
	public double FCRE_WA_Over6ValueSum;
	public int FCRE_WA_Over6Cnt;
	public double FCRE_WA_SECTOR_TOT_ValueSum;

	// 가속도 -  UNIFORM 가중평균 
	public double FCRE_UNIFORM_UnderM6ValueSum;
	public int FCRE_UNIFORM_UnderM6Cnt;
	public double FCRE_UNIFORM_UnderM4ValueSum;
	public int FCRE_UNIFORM_UnderM4Cnt;
	public double FCRE_UNIFORM_UnderM2ValueSum;
	public int FCRE_UNIFORM_UnderM2Cnt;
	public double FCRE_UNIFORM_Under0ValueSum;
	public int FCRE_UNIFORM_Under0Cnt;
	public double FCRE_UNIFORM_Under2ValueSum;
	public int FCRE_UNIFORM_Under2Cnt;
	public double FCRE_UNIFORM_Under4ValueSum;
	public int FCRE_UNIFORM_Under4Cnt;
	public double FCRE_UNIFORM_Under6ValueSum;
	public int FCRE_UNIFORM_Under6Cnt;
	public double FCRE_UNIFORM_Over6ValueSum;
	public int FCRE_UNIFORM_Over6Cnt;
	public double FCRE_UNIFORM_SECTOR_TOT_ValueSum;
	
	// 가속도 -  TRIWEIGHT 가중평균 
	public double FCRE_TRIWEIGHT_UnderM6ValueSum;
	public int FCRE_TRIWEIGHT_UnderM6Cnt;
	public double FCRE_TRIWEIGHT_UnderM4ValueSum;
	public int FCRE_TRIWEIGHT_UnderM4Cnt;
	public double FCRE_TRIWEIGHT_UnderM2ValueSum;
	public int FCRE_TRIWEIGHT_UnderM2Cnt;
	public double FCRE_TRIWEIGHT_Under0ValueSum;
	public int FCRE_TRIWEIGHT_Under0Cnt;
	public double FCRE_TRIWEIGHT_Under2ValueSum;
	public int FCRE_TRIWEIGHT_Under2Cnt;
	public double FCRE_TRIWEIGHT_Under4ValueSum;
	public int FCRE_TRIWEIGHT_Under4Cnt;
	public double FCRE_TRIWEIGHT_Under6ValueSum;
	public int FCRE_TRIWEIGHT_Under6Cnt;
	public double FCRE_TRIWEIGHT_Over6ValueSum;
	public int FCRE_TRIWEIGHT_Over6Cnt;
	public double FCRE_TRIWEIGHT_SECTOR_TOT_ValueSum;
	
	// 가속도 -  GAUSSIAN 가중평균 
	public double FCRE_GAUSSIAN_UnderM6ValueSum;
	public int FCRE_GAUSSIAN_UnderM6Cnt;
	public double FCRE_GAUSSIAN_UnderM4ValueSum;
	public int FCRE_GAUSSIAN_UnderM4Cnt;
	public double FCRE_GAUSSIAN_UnderM2ValueSum;
	public int FCRE_GAUSSIAN_UnderM2Cnt;
	public double FCRE_GAUSSIAN_Under0ValueSum;
	public int FCRE_GAUSSIAN_Under0Cnt;
	public double FCRE_GAUSSIAN_Under2ValueSum;
	public int FCRE_GAUSSIAN_Under2Cnt;
	public double FCRE_GAUSSIAN_Under4ValueSum;
	public int FCRE_GAUSSIAN_Under4Cnt;
	public double FCRE_GAUSSIAN_Under6ValueSum;
	public int FCRE_GAUSSIAN_Under6Cnt;
	public double FCRE_GAUSSIAN_Over6ValueSum;
	public int FCRE_GAUSSIAN_Over6Cnt;
	public double FCRE_GAUSSIAN_SECTOR_TOT_ValueSum;
	
	public void addFCRE(SectorAnalyzeModel model) {
		// 가속도 -  유클리드 가중평균 
		this.FCRE_WA_UnderM6ValueSum += model.FCRE_WA_UnderM6ValueSum;
		this.FCRE_WA_UnderM6Cnt += model.FCRE_WA_UnderM6Cnt;
		this.FCRE_WA_UnderM4ValueSum += model.FCRE_WA_UnderM4ValueSum;
		this.FCRE_WA_UnderM4Cnt += model.FCRE_WA_UnderM4Cnt;
		this.FCRE_WA_UnderM2ValueSum += model.FCRE_WA_UnderM2ValueSum;
		this.FCRE_WA_UnderM2Cnt += model.FCRE_WA_UnderM2Cnt;
		this.FCRE_WA_Under0ValueSum += model.FCRE_WA_Under0ValueSum;
		this.FCRE_WA_Under0Cnt += model.FCRE_WA_Under0Cnt;
		this.FCRE_WA_Under2ValueSum += model.FCRE_WA_Under2ValueSum;
		this.FCRE_WA_Under2Cnt += model.FCRE_WA_Under2Cnt;
		this.FCRE_WA_Under4ValueSum += model.FCRE_WA_Under4ValueSum;
		this.FCRE_WA_Under4Cnt += model.FCRE_WA_Under4Cnt;
		this.FCRE_WA_Under6ValueSum += model.FCRE_WA_Under6ValueSum;
		this.FCRE_WA_Under6Cnt += model.FCRE_WA_Under6Cnt;
		this.FCRE_WA_Over6ValueSum += model.FCRE_WA_Over6ValueSum;
		this.FCRE_WA_Over6Cnt += model.FCRE_WA_Over6Cnt;
		this.FCRE_WA_SECTOR_TOT_ValueSum += model.FCRE_WA_SECTOR_TOT_ValueSum;

		// 가속도 -  UNIFORM 가중평균 
		this.FCRE_UNIFORM_UnderM6ValueSum += model.FCRE_UNIFORM_UnderM6ValueSum;
		this.FCRE_UNIFORM_UnderM6Cnt += model.FCRE_UNIFORM_UnderM6Cnt;
		this.FCRE_UNIFORM_UnderM4ValueSum += model.FCRE_UNIFORM_UnderM4ValueSum;
		this.FCRE_UNIFORM_UnderM4Cnt += model.FCRE_UNIFORM_UnderM4Cnt;
		this.FCRE_UNIFORM_UnderM2ValueSum += model.FCRE_UNIFORM_UnderM2ValueSum;
		this.FCRE_UNIFORM_UnderM2Cnt += model.FCRE_UNIFORM_UnderM2Cnt;
		this.FCRE_UNIFORM_Under0ValueSum += model.FCRE_UNIFORM_Under0ValueSum;
		this.FCRE_UNIFORM_Under0Cnt += model.FCRE_UNIFORM_Under0Cnt;
		this.FCRE_UNIFORM_Under2ValueSum += model.FCRE_UNIFORM_Under2ValueSum;
		this.FCRE_UNIFORM_Under2Cnt += model.FCRE_UNIFORM_Under2Cnt;
		this.FCRE_UNIFORM_Under4ValueSum += model.FCRE_UNIFORM_Under4ValueSum;
		this.FCRE_UNIFORM_Under4Cnt += model.FCRE_UNIFORM_Under4Cnt;
		this.FCRE_UNIFORM_Under6ValueSum += model.FCRE_UNIFORM_Under6ValueSum;
		this.FCRE_UNIFORM_Under6Cnt += model.FCRE_UNIFORM_Under6Cnt;
		this.FCRE_UNIFORM_Over6ValueSum += model.FCRE_UNIFORM_Over6ValueSum;
		this.FCRE_UNIFORM_Over6Cnt += model.FCRE_UNIFORM_Over6Cnt;
		this.FCRE_UNIFORM_SECTOR_TOT_ValueSum += model.FCRE_UNIFORM_SECTOR_TOT_ValueSum;
			
			// 가속도 -  TRIWEIGHT 가중평균 
		this.FCRE_TRIWEIGHT_UnderM6ValueSum += model.FCRE_TRIWEIGHT_UnderM6ValueSum;
		this.FCRE_TRIWEIGHT_UnderM6Cnt += model.FCRE_TRIWEIGHT_UnderM6Cnt;
		this.FCRE_TRIWEIGHT_UnderM4ValueSum += model.FCRE_TRIWEIGHT_UnderM4ValueSum;
		this.FCRE_TRIWEIGHT_UnderM4Cnt += model.FCRE_TRIWEIGHT_UnderM4Cnt;
		this.FCRE_TRIWEIGHT_UnderM2ValueSum += model.FCRE_TRIWEIGHT_UnderM2ValueSum;
		this.FCRE_TRIWEIGHT_UnderM2Cnt += model.FCRE_TRIWEIGHT_UnderM2Cnt;
		this.FCRE_TRIWEIGHT_Under0ValueSum += model.FCRE_TRIWEIGHT_Under0ValueSum;
		this.FCRE_TRIWEIGHT_Under0Cnt += model.FCRE_TRIWEIGHT_Under0Cnt;
		this.FCRE_TRIWEIGHT_Under2ValueSum += model.FCRE_TRIWEIGHT_Under2ValueSum;
		this.FCRE_TRIWEIGHT_Under2Cnt += model.FCRE_TRIWEIGHT_Under2Cnt;
		this.FCRE_TRIWEIGHT_Under4ValueSum += model.FCRE_TRIWEIGHT_Under4ValueSum;
		this.FCRE_TRIWEIGHT_Under4Cnt += model.FCRE_TRIWEIGHT_Under4Cnt;
		this.FCRE_TRIWEIGHT_Under6ValueSum += model.FCRE_TRIWEIGHT_Under6ValueSum;
		this.FCRE_TRIWEIGHT_Under6Cnt += model.FCRE_TRIWEIGHT_Under6Cnt;
		this.FCRE_TRIWEIGHT_Over6ValueSum += model.FCRE_TRIWEIGHT_Over6ValueSum;
		this.FCRE_TRIWEIGHT_Over6Cnt += model.FCRE_TRIWEIGHT_Over6Cnt;
		this.FCRE_TRIWEIGHT_SECTOR_TOT_ValueSum += model.FCRE_TRIWEIGHT_SECTOR_TOT_ValueSum;
			
			// 가속도 -  GAUSSIAN 가중평균 
		this.FCRE_GAUSSIAN_UnderM6ValueSum += model.FCRE_GAUSSIAN_UnderM6ValueSum;
		this.FCRE_GAUSSIAN_UnderM6Cnt += model.FCRE_GAUSSIAN_UnderM6Cnt;
		this.FCRE_GAUSSIAN_UnderM4ValueSum += model.FCRE_GAUSSIAN_UnderM4ValueSum;
		this.FCRE_GAUSSIAN_UnderM4Cnt += model.FCRE_GAUSSIAN_UnderM4Cnt;
		this.FCRE_GAUSSIAN_UnderM2ValueSum += model.FCRE_GAUSSIAN_UnderM2ValueSum;
		this.FCRE_GAUSSIAN_UnderM2Cnt += model.FCRE_GAUSSIAN_UnderM2Cnt;
		this.FCRE_GAUSSIAN_Under0ValueSum += model.FCRE_GAUSSIAN_Under0ValueSum;
		this.FCRE_GAUSSIAN_Under0Cnt += model.FCRE_GAUSSIAN_Under0Cnt;
		this.FCRE_GAUSSIAN_Under2ValueSum += model.FCRE_GAUSSIAN_Under2ValueSum;
		this.FCRE_GAUSSIAN_Under2Cnt += model.FCRE_GAUSSIAN_Under2Cnt;
		this.FCRE_GAUSSIAN_Under4ValueSum += model.FCRE_GAUSSIAN_Under4ValueSum;
		this.FCRE_GAUSSIAN_Under4Cnt += model.FCRE_GAUSSIAN_Under4Cnt;
		this.FCRE_GAUSSIAN_Under6ValueSum += model.FCRE_GAUSSIAN_Under6ValueSum;
		this.FCRE_GAUSSIAN_Under6Cnt += model.FCRE_GAUSSIAN_Under6Cnt;
		this.FCRE_GAUSSIAN_Over6ValueSum += model.FCRE_GAUSSIAN_Over6ValueSum;
		this.FCRE_GAUSSIAN_Over6Cnt += model.FCRE_GAUSSIAN_Over6Cnt;
		this.FCRE_GAUSSIAN_SECTOR_TOT_ValueSum += model.FCRE_GAUSSIAN_SECTOR_TOT_ValueSum;
	}
}
