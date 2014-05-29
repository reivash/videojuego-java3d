package dataengine;

import java.util.List;
import java.util.ArrayList;
import java.util.Iterator;

public class DataGroup implements DataNode, Iterable<DataNode> {
	private String ident;
	private List<DataNode> elements;
	public DataGroup(String ident){
		this(ident, new ArrayList<DataNode>());
	}
	public DataGroup(String ident, List<DataNode> elements){
		this.ident = ident;
		this.elements = elements;
	}
	public boolean isGroup() {
		return true;
	}
	public boolean isKeyValue() {
		return false;
	}
	public boolean isFunctionValue() {
		return false;
	}
	public String getType() {
		return GROUP_TYPE;
	}
	public String getIdentifier() {
		return ident;
	}
	public Iterator<DataNode> iterator() {
		return elements.iterator();
	}
	public void addNode(DataNode node){
		elements.add(node);
	}
	public void removeNode(DataNode node){
		elements.remove(node);
	}
	public void clearAllNodes(){
		elements.clear();
	}
	public DataNode getNodeByIndex(int index){
		return elements.get(index);
	}
	public List<DataNode> getAllNodes(){
		return elements;
	}
	public String toString() {
		return "[" + ident + ", " + elements + "]";
	}
	public DataGroup asGroup() {
		return this;
	}
	public DataValue asValue() {
		return null;
	}
}
