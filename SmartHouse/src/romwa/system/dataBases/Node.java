package romwa.system.dataBases;

public class Node<T> {

	private T value;
	
	private Node<T> next;
	
	public Node(T value) {
		this.value = value;
		next = null;
	}
	
	public Node(T value, Node<T> next) {
		this.value = value;
		this.next = next;
	}
	
	public T getValue() {
		return value;
	}
	
	public Node<T> getNext() {
		return next;
	}
	
	public void setValue(T value) {
		this.value = value;
	}
	
	public void setNext(Node<T> next) {
		this.next = next;
	}
	
	public boolean hasNext() {
		return next != null;
	}
	
	public String toString() {
		return value+"-->"+next;
	}
}
