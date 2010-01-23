package net.slashware.util;

import java.util.ArrayList;
import java.util.List;

public class Node <T,K> {
	private T object;
	private List<K> rays;
	
	public Node (T t){
		object  = t;
		rays = new ArrayList<K>();
	}

	public T getObject() {
		return object;
	}

	public void setObject(T object) {
		this.object = object;
	}

	public List<K> getRays() {
		return rays;
	}

	public void setRays(List<K> rays) {
		this.rays = rays;
	}
	
	public void addRay(K ray) {
		this.rays.add(ray);
	}
}
