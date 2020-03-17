package romwa.system.dataBases;

public class Stack<T> {

	private Node<T> top;
	
	public Stack() {
		top = null;
	}
	
	public Stack<T> stack() {
		top = null;
		return this;
	}
	
	public void push(T x) {
		if(top == null) top = new Node<T>(x, top);
		else top = new Node<T>(x, top);
	}
	
	public T pop() {
		T value = top.getValue();
		top = top.getNext();
		return value;
	}
	
	public T top() {
		return top.getValue();
	}
	
	public boolean isEmpty() {
		if(top == null) return true;
		return false;
	}
	
	public String toString() {
		return top.toString();
	}
}
