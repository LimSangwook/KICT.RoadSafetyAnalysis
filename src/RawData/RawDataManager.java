package RawData;

import java.util.ArrayList;
import java.util.Iterator;

public class RawDataManager {
	public enum TYPE {A, B};
	private ArrayList<RawDataModel> datas;
	
	public RawDataManager(String string, TYPE type) throws Exception {
		datas = GetRawDataReader(type).Read(string);
	}

	private IRawDataReader GetRawDataReader(TYPE type) {
		if (type == TYPE.A)
			return new RawDataReaderA();
		if (type == TYPE.B)
			return new RawDataReaderB();
		System.out.printf("### RawDataManager.GetRawDataReader(TYPE type) : 이련경우는 없어!");
		return null;
	}

	public Iterator<RawDataModel> GetIeterator() {
		return datas.iterator();
	}

}
