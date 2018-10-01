package com.dylan.main;




import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.List;
import java.util.Random;
import java.util.Scanner;
import java.util.Stack;

import com.dylan.game.Card;
import com.dylan.game.Card.Color;
import com.dylan.game.Card.Type;
import com.dylan.game.Player;


public class Engine {
	private Stack<Card> deck;
	private Stack<Card> discard;
	private Player[] players;
	private boolean debug = false;
	private Scanner scanner;
	public static void main(String[] args) {
		new Engine().start();
	}
	public void start() {
		//Create a new temporary deck as an array and shuffle it
		Card[] temp_deck = new Card[108];
		int type_index = 0;
		for(int i = 0;i<temp_deck.length/4;i++) {
			for(int color_index = 0;color_index<Color.values().length;color_index++) {
				temp_deck[4*i + color_index] = new Card(Type.values()[type_index],Color.values()[color_index]);
				if(debug) {
					System.out.println("Created new Card with " + temp_deck[4*i + color_index].getType() + " and " + temp_deck[4*i + color_index].getColor());
				}	
			}
			type_index++;
			type_index = type_index > Type.values().length - 1 ? 1 : type_index;
		}
		Collections.shuffle(Arrays.asList(temp_deck));
		//turn our temporary deck into a stack to simulate a pile of cards
		deck = new Stack<Card>();
		discard = new Stack<Card>();
		deck.addAll(Arrays.asList(temp_deck));
		
		//start game and deal out the cards to the players
		scanner = new Scanner(System.in);
		System.out.println("Welcome to Uno!\n\n"
				+ "Menu:\n"
				+ "1 : Play Game\n"
				+ "2 : Instructions\n"
				+ "3 : Credits\n");
		int option = -1;
		while(option != 1) {
			try {
				option = scanner.nextInt();
				if(option == 2) {
					displayInstructions();
				}else if(option == 3) {
					displayCredits();
				}else if(option == 1){
					break;
				}else {
					System.out.println("Please re-enter a number 1-3");
				}
			}catch(Exception e) {
				System.out.println("Please re-enter a number 1-3");
				scanner.nextLine();
			}
		}
		System.out.println("How many players are playing? (3 - 10)\n");
		option = -1;
		while(option < 3 || option > 10) {
			try {
				option = scanner.nextInt();
				scanner.nextLine();
				if(option >= 3 && option <= 10) {
					break;
				}
				System.out.println("Please enter a correct number 3-10\n");
			}catch(Exception e) {
				System.out.println("Please enter a correct number 3-10\n");
				scanner.nextLine();
			}
		}
		players = new Player[option];
		for(int i = 0;i<players.length;i++) {
			System.out.println("What is your name, Player " + (i+1)+"?\n");
			String name = scanner.nextLine();
			for(int j = i-1;j>-1;j--) {
				if(name.equals(players[j].getName())) {
					System.out.println("Please re-enter a name that has not already been taken.");
					name = scanner.nextLine();
				}
			}
			players[i] = new Player(name,dealToPlayer());
		}
		//clear screen
		clear();
		//pick starting card, start game loop and pick random player to go first
		Card first = discard.push(grabCard());
		//while the first card we picked is a special card, re-pick because it can't
		while(first.getType().ordinal() > 9) {
			first = discard.push(grabCard());
		}
		//choose random player to start
		int player_index = new Random().nextInt(players.length);
		boolean skip = false, reverse = false, plus_two = false, wild_plus_four = false;
		int toAdd = 1;
		while(gameShouldGoOn()) {
			Card current = discard.peek();
			//make a player move
			System.out.println("It's your turn, " + players[player_index].getName()+".\n\n"
					+ "Menu:\n"
					+ "1 - " + players[player_index].getCards().size()+" : Play a card\n"
					+ "-1 : Draw a card from the deck\n\nYour cards: ");
			if(players[player_index].getCards().size() == 1){
				System.out.println("\nUNO!!!\n");
			}
			players[player_index].printCards();
			System.out.println("\n\nCurrent card: \n" + current);
			System.out.println("\n");
			option = Integer.MIN_VALUE;
			//use a nested while loop to check for errors in the input
			while(option != -1 || option < 1 || option > players[player_index].getCards().size()) {
				skip = false; plus_two = false; wild_plus_four = false; //need to be re-initialized at the beginning of every round
				try{
					option = scanner.nextInt();
					scanner.nextLine();
					if(option == -1) {
						players[player_index].getCards().add(grabCard());
						break;
					}
					//Plays the card
					else if(option >=1 && option <= players[player_index].getCards().size()) {
						if(canPlayCard(players[player_index].getCards().get(option-1),current)) {
							//check to see if its a special card
							if(players[player_index].getCards().get(option-1).getType() == Type.REVERSE) {
								reverse = !reverse;
								useCard(option, player_index);
							}else if(players[player_index].getCards().get(option-1).getType() == Type.SKIP) {
								skip = true;
								useCard(option, player_index);
							}else if(players[player_index].getCards().get(option-1).getType() == Type.PLUS_TWO) {
								plus_two = true;
								useCard(option, player_index);
							}else if(players[player_index].getCards().get(option-1).getType() == Type.WILD) {
								System.out.println("What color would you like to play? (RED, GREEN, YELLOW, BLUE)");
								String input = "";
								while(!input.equalsIgnoreCase("RED") && !input.equalsIgnoreCase("YELLOW") 
										&& !input.equalsIgnoreCase("GREEN") && !input.equalsIgnoreCase("BLUE")) {
									input = scanner.nextLine();
									if(input.equalsIgnoreCase("RED")) {
										players[player_index].getCards().get(option-1).setColor(Color.RED);
									}else if(input.equalsIgnoreCase("YELLOW")) {
										players[player_index].getCards().get(option-1).setColor(Color.YELLOW);
									}else if(input.equalsIgnoreCase("GREEN")) {
										players[player_index].getCards().get(option-1).setColor(Color.GREEN);
									}else if(input.equalsIgnoreCase("BLUE")) {
										players[player_index].getCards().get(option-1).setColor(Color.BLUE);
									}else {
										System.out.println("Invalid input. Please enter a Color");
									}
								}
								useCard(option, player_index);
							}else if(players[player_index].getCards().get(option-1).getType() == Type.WILD_PLUS_FOUR){
								wild_plus_four = true;
								System.out.println("What color would you like to play? (RED, GREEN, YELLOW, BLUE)");
								String input = "";
								while(!input.equalsIgnoreCase("RED") && !input.equalsIgnoreCase("YELLOW") 
										&& !input.equalsIgnoreCase("GREEN") && !input.equalsIgnoreCase("BLUE")) {
									input = scanner.nextLine();
									if(input.equalsIgnoreCase("RED")) {
										players[player_index].getCards().get(option-1).setColor(Color.RED);
									}else if(input.equalsIgnoreCase("YELLOW")) {
										players[player_index].getCards().get(option-1).setColor(Color.YELLOW);
									}else if(input.equalsIgnoreCase("GREEN")) {
										players[player_index].getCards().get(option-1).setColor(Color.GREEN);
									}else if(input.equalsIgnoreCase("BLUE")) {
										players[player_index].getCards().get(option-1).setColor(Color.BLUE);
									}else {
										System.out.println("Invalid input. Please enter a Color");
									}
								}
								useCard(option, player_index);
							}else {
								useCard(option, player_index);
							}
							break;
						}else {
							System.out.println("Unable to play card, try again");
						}
					}else {
						System.out.println("Please enter a valid number.");
					}
				}catch(Exception e) {
					System.out.println("Please enter a valid input");
					scanner.nextLine();
				}
			}
			//configures the amount of players to jump
			if(reverse && !skip) {
				toAdd = -1;
			}else if(reverse && skip) {
				toAdd = -2;
			}else if(!reverse && !skip) {
				toAdd = 1;
			}else {
				toAdd =2;
			}
			player_index+= toAdd;
			
			//takes into account IndexOutOfBounds Exception
			if(player_index >= players.length) {
				int difference = player_index + toAdd - players.length -1;
				player_index = difference;
			}else if(player_index < 0) {
				player_index = players.length + player_index;
			}
			//Adds cards for the +2 and (dreaded) +4
			if(wild_plus_four) {
				for(int i = 0;i<4;i++)
					players[player_index].getCards().add(grabCard());
			}if(plus_two) {
				for(int i = 0;i<2;i++)
					players[player_index].getCards().add(grabCard());
			}
			//make sure deck size isn't 0, if so, swap with the discard pile
			if(deck.size() == 0) {
				deck = discard;
				discard.empty();
			}
			current = discard.peek();
			clear();
		}
		
	}
	/**
	 * Prints instructions
	 */
	private void displayInstructions() {
		System.out.println("Instructions for UNO can be found on this site:\nhttps://www.unorules.com/page/3/");
		System.out.println("\n"
				+ "Menu:\n"
				+ "1 : Play Game\n"
				+ "3 : Credits\n");
	}
	/**
	 * Prints credits
	 */
	private void displayCredits() {
		System.out.println("MIT LICENSE Copyright 2018 Dylan T Small\n" + 
				"\n" + 
				"Permission is hereby granted, free of charge, to any person obtaining a copy of this software and associated documentation files "
				+ "\n(the \"Software\"), to deal in the Software without restriction, including without limitation the rights to use, copy, modify, merge, "
				+ "\npublish, distribute, sublicense, and/or sell copies of the Software, and to permit persons to whom the Software is furnished to do so, "
				+ "\nsubject to the following conditions:\n"
				+ "\nThe above copyright notice and this permission notice shall be included in all copies or substantial portions of the Software.\n"
				+ "\nTHE SOFTWARE IS PROVIDED \"AS IS\", WITHOUT WARRANTY OF ANY KIND, EXPRESS OR IMPLIED, INCLUDING BUT NOT LIMITED TO THE WARRANTIES OF MERCHANTABILITY, "
				+ "\nFITNESS FOR A PARTICULAR PURPOSE AND NONINFRINGEMENT. IN NO EVENT SHALL THE AUTHORS OR COPYRIGHT HOLDERS BE LIABLE FOR ANY CLAIM, DAMAGES OR OTHER "
				+ "\nLIABILITY, WHETHER IN AN ACTION OF CONTRACT, TORT OR OTHERWISE, ARISING FROM, OUT OF OR IN CONNECTION WITH THE SOFTWARE OR THE USE OR OTHER DEALINGS "
				+ "\nIN THE SOFTWARE.");
		System.out.println("\n"
				+ "Menu:\n"
				+ "1 : Play Game\n"
				+ "2 : Instructions\n");
	}
	/**
	 * Uses a simple for loop to pop 7 cards off the top of a shuffled deck to the player
	 * @return
	 */
	private List<Card> dealToPlayer() {
		List<Card> cards = new ArrayList<Card>();
		for(int i = 0;i<7;i++) {
			cards.add(deck.pop());
		}
		return cards;
	}
	/**
	 * pops off from deck and returns (usually to place on hand or discard pile)
	 * @return
	 */
	private Card grabCard() {
		return deck.pop();
	}
	private boolean gameShouldGoOn() {
		for(Player player : players) {
			if(player.getCards().size() == 0) {
				System.out.println(player.getName() +" wins the game!");
				return false;
			}
		}
		return true;
	}
	/**
	 * designates whether a player can play a certain card or not
	 * @param trying
	 * @param current
	 * @return
	 */
	private boolean canPlayCard(Card trying,Card current) {
		if(trying.getType() == Type.WILD || trying.getType() == Type.WILD_PLUS_FOUR) {
			return true;
		}else if(trying.getType() == current.getType()) {
			return true;
		}else if(trying.getColor() == current.getColor()) {
			return true;
		}else {
			return false;
		}
	}
	/**
	 * clears the console so other players can't cheat
	 */
	private void clear() {
		for(int i = 0;i< 40;i++) {
			System.out.println();
		}
	}
	/**
	 * uses a user selected option to place a players card onto the discard pile, where it will
	 * become next round's current card
	 * @param option
	 * @param player_index
	 */
	private void useCard(int option, int player_index) {
		discard.push(players[player_index].getCards().get(option-1));
		players[player_index].getCards().remove(option-1);
	}
}

