package main_package;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.Set;

import game_package.GameType;

public class User {

	private int playerId;
	public String playerName;
	private String password;
	public int hoursPlayed;
	public OnlineStatus status;
	private ArrayList<Skin> skin;
	private Skin activeSkin;
	private Set<User> friendsList;
	public Set<Pair<GameType, Statistics>> statistics; // for every gametype, you have a statistic

	public User() {
		// TODO Auto-generated constructor stub
		this.statistics = new HashSet<Pair<GameType, Statistics>>();

		for (GameType gameType : GameType.values()) {

			this.statistics.add(new Pair(gameType, new Statistics()));
		}
	}

	public void changeName(String newName) {
		this.playerName = newName;
	}

	public Skin getActiveSkin() {
		return activeSkin;
	}

	public void addFriend(User user) {
		friendsList.add(user);
	}

	public void removeFriend(User user) {
		friendsList.remove(user);
	}

	public int getPlayerID() {
		return this.playerId;
	}
}

