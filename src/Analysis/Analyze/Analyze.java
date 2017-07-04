package Analysis.Analyze;

import Analysis.AnalysisDataModel;
import Analysis.AnalysisResult;
import RoadInfo.RoadInfoManager;
import RoadInfo.RoadInfoModel;

public abstract class Analyze {
	private static int LIMIT_SPEED = 110;
	private static int LIMIT_TTC = 3;
	
	Analyze nextAnalyze;
	public void next(Analyze nextAnayze) {
		this.nextAnalyze = nextAnayze;
	}
	public void doAnalysis(AnalysisDataModel analysisDataModel, RoadInfoManager roadInfoMGR) {
		this.process(analysisDataModel, roadInfoMGR);
		if (nextAnalyze != null) {
			nextAnalyze.doAnalysis(analysisDataModel, roadInfoMGR);
		}
	}
	protected AnalysisResult AnalysisCarInfo(RoadInfoModel roadInfo, double BASE_SPEED, double TARGET_SPEED, double TARGET_Y ){
		// baseSpeed km/h 단위 
		// targetSpeed m/s 단위 
		AnalysisResult result = new AnalysisResult();
		if (roadInfo == null) { // 이건 없을수 있다.
			return result;
		}
		
		double ALIGN_RADIUS = roadInfo.getALIGN_RADIUS();
        double PROFILE_SLOPE = roadInfo.getPROFILE_SLOPE();
        double CROSS_SLOPE_UP = roadInfo.getCROSS_SLOPE_UP();
        double HEADWAY = (TARGET_Y + 4.7)/(BASE_SPEED);
        double safeSpeed =-1;
        
        if (ALIGN_RADIUS == 0.0) {// 직선부
            if (BASE_SPEED > LIMIT_SPEED) {
            	result.SS = 1;
            }
        } else { // 곡선부 
            safeSpeed = Math.pow(ALIGN_RADIUS*127*((Math.abs(CROSS_SLOPE_UP)/100)+0.11), 0.5);
            if (safeSpeed - BASE_SPEED <= 0 ) {
            	result.SL = 1;
            }
		}

        //후행차량 정지 거리;
        result.SD_F = ((BASE_SPEED/3.6)*2.5)+(Math.pow(BASE_SPEED/3.6,2))/(254*(0.11+((Math.abs(PROFILE_SLOPE))/100)));       	
        result.SD_L = (TARGET_SPEED+(BASE_SPEED/3.6))*HEADWAY+(Math.pow(TARGET_SPEED+(BASE_SPEED/3.6),2))/(254*(0.11+((Math.abs(ALIGN_RADIUS)/100)))+4.7);   //선행차량 정지 거리;	
        result.TTC_T = (TARGET_Y)/(TARGET_SPEED/3.6-(TARGET_SPEED+TARGET_SPEED/3.6));
        if (result.TTC_T == 0) {
        	result.TTC_T = -1;
        }
        
        // SDI, TTC : TTC_First
        if (result.SS == 0 && result.SL == 0) {
	        if (result.TTC_T > LIMIT_TTC)  {
	        	result.TTC_T = 1;
	        }
	        if (result.TTC == 0 && result.SD_L < result.SD_F) {
	        	result.SDI = 1;
	        }
        }        
        
        if (result.SS == 1 || result.SL == 1 || result.SDI == 1 || result.TTC ==1) {
        	result.TOT_SAFE = 1;
        }
        return result;
	}
	abstract protected void process(AnalysisDataModel analysisDataModel, RoadInfoManager roadInfoMGR);
}
