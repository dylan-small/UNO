package com.dylan.game;

public class Card {
	public static enum Type{
		ZERO,
		ONE,
		TWO,
		THREE,
		FOUR,
		FIVE,
		SIX,
		SEVEN,
		EIGHT,
		NINE,
		SKIP,
		REVERSE,
		PLUS_TWO,
		WILD,
		WILD_PLUS_FOUR
	}
	public static enum Color{
		RED,
		BLUE,
		GREEN,
		YELLOW
	}
	private Type type;
	private Color color;
	
	/*
	 * Constructor
	 */
	public Card(Type type, Color color) {
		this.type = type;
		this.color = color;
	}
	
	
	/*
	 * Getter and Setter methods
	 */
	public Type getType() {
		return this.type;
	}
	public void setType(Type type) {
		this.type = type;
	}
	public Color getColor() {
		return color;
	}
	public void setColor(Color color) {
		this.color = color;
	}
	
	@Override
	public String toString() {
		return "["+this.getColor()+" " + this.getType()+"]";
	}
}
