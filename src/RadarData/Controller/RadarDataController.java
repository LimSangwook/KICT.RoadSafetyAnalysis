package RadarData.Controller;

import RadarData.RadarDataModel;

abstract public class RadarDataController {
	public enum TYPE{A,B};
	public static RadarDataController getController(TYPE type) {
		RadarDataController context = null;
		switch(type) {
		case A:
			context = RadarDataControllerTypeA.getInstance();
			break;
		case B:
			context = RadarDataControllerTypeB.getInstance();
			break;
		}
		return context;
	}
	abstract public int getSkipLines();
	abstract public RadarDataModel parseLine(String s) throws Exception;
}
