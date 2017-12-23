package Analysis.Analyzer;

import Analysis.Model.AnalysisDataModel;
import Analysis.Model.AnalysisResultModel;
import RoadInfo.RoadInfoManager;
import RoadInfo.RoadInfoModel;

public abstract class AnalyzeBase {
	private static int LIMIT_SPEED = 110;
	private static int LIMIT_TTC = 3;
	protected static double CAR_LENGTH = 4.7;
	AnalyzeBase nextAnalyze;
	public AnalyzeBase next(AnalyzeBase nextAnayze) {
		this.nextAnalyze = nextAnayze;
		return nextAnayze;
	}
	public void doAnalysis(AnalysisDataModel analysisDataModel, RoadInfoManager roadInfoMGR) {
		this.process(analysisDataModel, roadInfoMGR);
		if (nextAnalyze != null) {
			nextAnalyze.doAnalysis(analysisDataModel, roadInfoMGR);
		}
	}
	protected AnalysisResultModel AnalysisCarInfo(RoadInfoModel roadInfo, double BASE_SPEED_km_p_h, double TARGET_SPEED_km_p_h, double TARGET_Y ){
		// BASE_SPEED_km_p_h 	km/h 단위 
		// TARGET_SPEED_km_p_h 	km/h 단위
		double BASE_SPEED_m_p_s = BASE_SPEED_km_p_h / 3.6;		// m/s 단
		double TARGET_SPEED_m_p_s = TARGET_SPEED_km_p_h / 3.6;	// m/s
		AnalysisResultModel result = new AnalysisResultModel();
		if (roadInfo == null) { // 이건 없을수 있다.
			return result;
		}
		
		double ALIGN_RADIUS = roadInfo.getALIGN_RADIUS();
        double PROFILE_SLOPE = roadInfo.getPROFILE_SLOPE();
        double CROSS_SLOPE_UP = roadInfo.getCROSS_SLOPE_UP();
        double HEADWAY = (TARGET_Y)/(BASE_SPEED_m_p_s);	
        double safeSpeed =-1;
        
        if (ALIGN_RADIUS == 0.0) {// 직선부
            if (BASE_SPEED_km_p_h > LIMIT_SPEED) {
            		result.SS = 1;
            }
        } else { // 곡선부 
            safeSpeed = Math.pow(ALIGN_RADIUS*127*((Math.abs(CROSS_SLOPE_UP)/100)+0.11), 0.5);
            if (safeSpeed - BASE_SPEED_km_p_h <= 0 ) {
            		result.SL = 1;
            }
		}

        // 후행차량 정지 거리;
        result.SD_F = ((BASE_SPEED_m_p_s)*2.5)+(Math.pow(BASE_SPEED_m_p_s,2))/(254*(0.11+((Math.abs(PROFILE_SLOPE))/100)));       	
        // 선행차량 정지 거리;
        result.SD_L = (TARGET_SPEED_m_p_s+BASE_SPEED_m_p_s)*HEADWAY+(Math.pow(TARGET_SPEED_m_p_s+BASE_SPEED_m_p_s,2))/(254*(0.11+((Math.abs(ALIGN_RADIUS)/100)))+CAR_LENGTH);
        // TTC Time : 충돌 예상시간 ex) 3 : 3초뒤 충돌 예
        result.TTC_T = (TARGET_Y)/(BASE_SPEED_m_p_s-TARGET_SPEED_m_p_s);
        if (result.TTC_T <= 0) {
        		result.TTC_T = -1;
        }
        if (result.TTC_T >=300) {
        		result.TTC_T = 300;
        }
        
        // SDI, TTC : TTC_First
        if (result.SS == 0 && result.SL == 0) {
	        if (result.TTC_T > LIMIT_TTC)  {
	        		result.TTC = 1;
	        }
	        if (result.TTC == 0 && result.SD_L < result.SD_F) {
	        		result.SDI = 1;
	        }
        }
        
        if (result.SS == 1 || result.SL == 1 || result.SDI == 1 || result.TTC ==1) {
        		result.TOT_SAFE = 1;
        }
        
        // TTC 가중치 구하기
        if (result.TTC_T > 0) {
        		setTTCWeights(result, TARGET_Y);
        }
        
        return result;
	}
	
	private void setTTCWeights(AnalysisResultModel result, double distance) {
		result.TTC_EXP = getTTC_EXP(result.TTC_T);
		result.TTC_Uniform_Kernel = getTTC_Uniform_Kernel(result.TTC_T, distance);
		result.TTC_Triweight_Kernel = getTTC_Triweight_Kernel(result.TTC_T, distance);
		result.TTC_Gaussian_Kernel = getTTC_Gaussian_Kernel(result.TTC_T, distance);
	}
	
	private double getTTC_EXP(double TTC_T) {
		return Math.exp(-TTC_T);
	}
	
	private double getTTC_Uniform_Kernel(double TTC_T, double DISTANCE) {
		return 1.0/2.0;
	}
	
	private double getTTC_Triweight_Kernel(double TTC_T, double DISTANCE) {
		double weight = 35.0/32.0*Math.pow((1-Math.pow(DISTANCE,2.0)),3.0);
		return weight;
	}
	
	private double getTTC_Gaussian_Kernel(double TTC_T, double DISTANCE) {
		double weight = (1.0/(Math.sqrt(2.0 * Math.PI)))*Math.pow(Math.E,(-1.0/2.0)*Math.pow(DISTANCE, 2.0));
		System.out.println(DISTANCE + " \t\t" + weight);
		return weight;
	}

	abstract protected void process(AnalysisDataModel analysisDataModel, RoadInfoManager roadInfoMGR);
}
