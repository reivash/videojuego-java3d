package dataengine;

public class Datum {
	Object datum;
	String type;
	public Datum(Object datum, String type){
		this.datum = datum;
		this.type = type;
	}
	public Object getDatum(){
		return datum;
	}
	public String getType() {
		return type;
	}
	public String toString(){
		if(type.equals(DataNode.VECTOR_TYPE)){
			float[] res = (float[]) datum;
			return "[" + res[0] + ", " + res[1] + ", " + res[2] + "]";
		}
		return datum.toString();
	}
}
