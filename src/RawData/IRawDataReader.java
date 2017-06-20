package RawData;

import java.util.ArrayList;

public interface IRawDataReader {

	ArrayList<RawDataModel> Read(String string) throws Exception;

}
