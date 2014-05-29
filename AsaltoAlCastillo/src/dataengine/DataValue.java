package dataengine;

public class DataValue implements DataNode {
	private String ident;
	private Datum value;
	private boolean isKeyValue;
	public DataValue(String ident, Datum value){
		this(ident, value, true);
	}
	public DataValue(String ident, Datum value, boolean isKeyValue){
		this.ident = ident;
		this.value = value;
		this.isKeyValue = isKeyValue;
	}
	public Datum getValue() {
		return value;
	}
	public String getType(){
		return value.getType();
	}
	public String toString(){
		return "<"+value.toString()+">";
	}
	public boolean isGroup() {
		return false;
	}
	public boolean isKeyValue() {
		return isKeyValue;
	}
	public boolean isFunctionValue() {
		return !isKeyValue;
	}
	public String getIdentifier() {
		return ident;
	}
	public DataGroup asGroup() {
		return null;
	}
	public DataValue asValue() {
		return this;
	}
}
