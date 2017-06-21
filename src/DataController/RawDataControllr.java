package DataController;

import Analysis.AnalysisDataModel;
import RawData.RawDataModel;

abstract public class RawDataControllr {
	public enum TYPE{A,B};
	public static RawDataControllr Create(TYPE type) {
		RawDataControllr context = null;
		switch(type) {
		case A:
			context = new RawDataControllerA();
			break;
		case B:
			context = new RawDataControllerB();
			break;
		}
		return context;
	}
	abstract public int getSkipLines();
	abstract public RawDataModel parseLine(String s) throws Exception;
	abstract public String getCSVFormatHeader();
	abstract public String getCSVFormatData(AnalysisDataModel data);
}
