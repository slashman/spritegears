package net.slashware.util;

public interface PriorityEnqueable {
	public int getCost();
	public void reduceCost(int value);
}
