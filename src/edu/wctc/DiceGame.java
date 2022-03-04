package edu.wctc;

import java.util.*;
import java.util.function.ToIntFunction;
import java.util.stream.Collectors;
import java.util.stream.Stream;

public class DiceGame {
    private final List<Player> players = new ArrayList<Player>();
    private final List<Die> Dice = new ArrayList<Die>();
    private final int maxRolls;
    private Player currentPlayer;


    public DiceGame(int countPlayers, int countDice, int maxRolls) {
        // creating objects of player and die and adding them to the list
        for (int i = 1; i <= countPlayers; i++) {
            Player player = new Player();
            players.add(player);
        }

        for (int i = 1; i <= countDice; i++) {
            Die dice = new Die(6);
            Dice.add(dice);
        }

        this.maxRolls = maxRolls;

        if (players.size() < 2) {
            throw new IllegalArgumentException();
        }
    }

    private boolean allDiceHeld() {
        return Dice.stream()
                .allMatch(die -> die.isBeingHeld() == true);

    }

    public boolean autoHold(int faceValue) {
        //code
        List<Die> diceWithFaceValue = Dice.stream()
                .filter(die -> die.getFaceValue() == faceValue)
                .collect(Collectors.toList());

        if (diceWithFaceValue.stream().count() != 0) {
            if (diceWithFaceValue.stream()
                    .filter(die -> die.isBeingHeld() == true)
                    .findFirst()
                    .isPresent()
            ){
                return true;
            }
            else {
                diceWithFaceValue.stream()
                        .filter(die -> die.isBeingHeld() == true)
                        .findFirst()
                        .ifPresent(die -> die.holdDie());
                return true;
            }
        }
        else {
            return false;
        }
    }

    public int getCurrentPlayerNumber() {
        //Returns the player number of the current player.
        return currentPlayer.getPlayerNumber();
    }
    //code

    public int getCurrentPlayerScore() {
        //code
        return currentPlayer.getScore();
    }

    public boolean currentPlayerCanRoll() {
        //Returns true if the current player has any rolls remaining and if not all dice are held.
        if (currentPlayer.getRollsUsed() < maxRolls) {
            return true;
        }
        else {
            for(Die dice : Dice){
                dice.holdDie();
            }
        }
        return true;
    }

    public String getDiceResults(){
        //Resets a string composed by concatenating each Die's toString.
        return Dice.stream()
                .map(die -> die.toString())
                .collect(Collectors.joining());

        //Hints: map, Collectors.joining
    }

    public String getFinalWinner(){
        //Finds the player with the most wins and returns its toString.
        //
        //Hints: Collections.max, Comparator.comparingInt
        return players.stream()
                .max(Comparator.comparingInt(Player::getWins))
                .toString();

    }

    public String getGameResults(){
        //Sorts the player list field by score, highest to lowest.
        //
        //Awards each player that earned the highest score a win and all others a loss.
        //
        //Returns a string composed by concatenating each Player's toString.
        //
        //Hints: Comparator.comparingInt, reversed
        //
        //More hints: forEach
        //
        //Final hints: map, Collectors.joining
        List<Player> playersrevsort = players.stream()
                .sorted(Comparator.comparingInt(Player::getScore).reversed())
                .collect(Collectors.toList());

        long highestscore = players.stream()
                        .mapToInt(Player::getScore)
                                .max()
                                        .orElse(0);

        playersrevsort.stream()
                .filter(player -> player.getScore() == highestscore)
                .forEach(Player::addWin);

        playersrevsort.stream()
                .filter(player -> player.getScore() != highestscore)
                .forEach(Player::addLoss);

        return players.stream()
                .map(player -> toString())
                .collect(Collectors.joining());
    }

    private boolean isHoldingDie(int faceValue){
        //Returns true if there is any held die with a matching face value, false otherwise.
        //
        //Hints: filter, findFirst, isPresent
        if (Dice.stream()
                .filter(die -> die.getFaceValue() == faceValue)
                .filter(die -> die.isBeingHeld() == true)
                .findFirst()
                .isPresent()){
            return true;
        }
        else {
            return false;
        }
    }

    public boolean nextPlayer(){
        //If there are more players in the list after the current player,
        // updates currentPlayer to be the next player and returns true. Otherwise, returns false.
        for(Player play: players){
            if (currentPlayer.equals(play)){
                if (players.indexOf(play) != players.size()){
                    currentPlayer = players.get(players.indexOf(play) + 1);
                    return true;
                }
                else {
                    return false;
                }
            }
        }
        return false;
    }

    public void playerHold(char dieNum){
        //Finds the die with the given die number (NOT the face value) and holds it.
        //
        //Hints: filter, findFirst, isPre

        Dice.stream()
                .filter(die -> die.getDieNum() == dieNum)
                .findFirst().ifPresent(die -> die.holdDie());
    }

    public void resetDice(){
        //
        //Resets each die.
        //
        //Hint: forEach

        Dice.stream()
                .forEach(die -> die.resetDie());
    }

    public void resetPlayers(){

        players.stream()
                .forEach(player -> player.resetPlayer());
    }

    public void rollDice(){
        //Logs the roll for the current player, then rolls each die.
        //
        //Hint: forEach
        currentPlayer.roll();
        Dice.stream()
                .forEach(die -> die.rollDie());
    }
    public  void scoreCurrentPlayer(){
        // code
    }
    public void startNewGame(){
        // code
        //Assigns the first player in the list as the current player.
        // (The list will still be sorted by score from the previous round, so winner will end up going first.)
        //
        //Resets all players.
        currentPlayer = players.get(players.size() - 1);
        players.stream()
                .forEach(Player::resetPlayer);
    }


}