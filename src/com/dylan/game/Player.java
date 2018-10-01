package com.dylan.game;



import java.util.List;

import com.dylan.game.Card.Type;

public class Player {
	private String name;
	private List<Card> cards;
	
	public Player(String name,List<Card> cards) {
		this.name = name;
		this.cards = cards;
	}
	public String getName() {
		return this.name;
	}
	public void setName(String name) {
		this.name = name;
	}
	public List<Card> getCards(){
		return this.cards;
	}
	
	public void printCards() {
		for(int i = 0;i<cards.size();i++) {
			if(!cards.get(i).getType().equals(Type.WILD) && !cards.get(i).getType().equals(Type.WILD_PLUS_FOUR)) {
				System.out.print("[" + cards.get(i).getColor() + " " + cards.get(i).getType() + "] ");
			}else {
				System.out.print("[" + cards.get(i).getType()+"] ");
			}
		}
		System.out.println("");
	}
}
