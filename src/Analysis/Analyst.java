package Analysis;
import java.io.FileWriter;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Iterator;

import RawData.RawDataManager;
import RawData.RawDataModel;
import RoadInfo.RoadInfoManager;

public class Analyst {
	private RawDataManager rawDataMGR = null;
	private RoadInfoManager roadDataMGR = null;
	private ArrayList<AnalysisDataModel> analysisResult= null;
	
	class AnalysisInfo {
		AnalysisDataModel defaultInfo;
	}	
	
	public Analyst(RawDataManager rawDataManager, RoadInfoManager roadInfoManager) {
		rawDataMGR = rawDataManager;
		roadDataMGR = roadInfoManager;
	}
	public void DoAnalysis() {
		System.out.printf("####	 START \tAnalyst.DoAnalysis()\n");

		if (rawDataMGR == null || roadDataMGR == null) {
			System.out.printf("Error - Analyst.DoAnalysis() : rawDataMGR == null || roadDataMGR == null");
		}
		
		analysisResult = new ArrayList<AnalysisDataModel>();
		Iterator<RawDataModel> it = rawDataMGR.GetIeterator();
		while (it.hasNext()) {
			RawDataModel rawData = it.next();
			AnalysisDataModel analysisDataModel = new AnalysisDataModel(rawData, roadDataMGR);
			analysisResult.add(analysisDataModel);
		}
		System.out.printf("####	 END \tAnalyst.DoAnalysis()\n");
	}

	public void WriteCSV(String path) throws IOException {
		System.out.printf("####	 START \tAnalyst.WriteCSV()\n");
		FileWriter writer = new FileWriter(path, false);
		writer.write(GetCSVHeader());
		Iterator<AnalysisDataModel> iter = analysisResult.iterator();
		while(iter.hasNext()) {
			AnalysisDataModel data = iter.next();
			String str = data.GetDefaultInfoString() + "," + data.GetTotalInfoString() + "," + data.GetBaseCarInfo() + "," + data.GetExtractCatInfo() + "\n";
			writer.write(str);
		}
		writer.close();
		System.out.printf("####	 END \tAnalyst.WriteCSV()\n");
		// TODO Auto-generated method stub
		
	}
	private String GetCSVHeader() {
		String HeadLine = "\n\n\nIndex(distance),time(progress),week,GPSrealtime,Latitide,Longitide,Distance(m),Height,NorthVel,EastVel,UpVel,Roll,Pitch,Azimouth,INSstatus,차량속도,ALIGN_RADIUS,PROFILE_SLOPE,CROSS_SLOPE_UP,H(headway), 추종 개수, 제한속도 초과,안전속도 초과,SDI 위험 개수,TTC 초과(3초 기준) 개수,종합판단에 따른 위험판단 개수,근접IC,추종 후행,추종 선행,후행차량정지거리,선행차량정지거리,TTC 시간,추종 후행,추종 선행,후행차량정지거리,선행차량정지거리,TTC 시간,추종 후행,추종 선행,후행차량정지거리,선행차량정지거리,TTC 시간\n";
		
		// TODO Auto-generated method stub
		return HeadLine;
	}

	
}
