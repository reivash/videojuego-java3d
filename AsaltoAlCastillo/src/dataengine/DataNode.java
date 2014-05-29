package dataengine;

public interface DataNode {
	/*
	 * Set of valid types.
	 * 
	 * Empty should never be a thing, but for if.
	 */
	public static final String DEFAULT_TYPE = "EMPTY";
	public static final String GROUP_TYPE = "GROUP_DATANODE";
	public static final String NUMBER_TYPE = "NUMBER_DATANODE";
	public static final String VECTOR_TYPE = "VECTOR_DATANODE";
	public static final String STRING_TYPE = "STRING_DATANODE";
	public static final String BOOLEAN_TYPE = "BOOLEAN_DATANODE";
	boolean isGroup();
	boolean isKeyValue();
	boolean isFunctionValue();
	public String getType();
	public String getIdentifier();
	public DataGroup asGroup();
	public DataValue asValue();
}
