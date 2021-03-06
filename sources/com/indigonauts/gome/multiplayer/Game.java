//#condition BT || IGS
/*
 * (c) 2006 Indigonauts
 */
package com.indigonauts.gome.multiplayer;

import java.io.DataInputStream;
import java.io.IOException;

public class Game {
  public int nb;

  public String white;

  public String whiteRank;

  public String black;

  public String blackRank;

  public int move;

  public byte size;

  public byte komi;

  public int nbobs;

  public byte handi;

  private Game() {
    // empty private constructor
  }

  public Game(int nb, String white, String whiteRank, String black, String blackRank, int move, byte size, byte handi, byte komi, int nbobs) {
    this.nb = nb;
    this.white = white;
    this.whiteRank = whiteRank;
    this.black = black;
    this.blackRank = blackRank;
    this.move = move;
    this.size = size;
    this.handi = handi;
    this.komi = komi;
    this.nbobs = nbobs;
  }

  public static Game unmarshal(DataInputStream in) throws IOException {
    Game newOne = new Game();
    newOne.nb = in.readInt();
    newOne.white = in.readUTF();
    newOne.whiteRank = in.readUTF();
    newOne.black = in.readUTF();
    newOne.blackRank = in.readUTF();
    newOne.move = in.readInt();
    newOne.size = in.readByte();
    newOne.komi = in.readByte();
    newOne.nbobs = in.readInt();
    newOne.handi = in.readByte();
    return newOne;
  }

  public String toString() {
    return "#" + nb + " " + white + " [" + whiteRank + "] " + black + " [" + blackRank + "]" + " H" + handi + " M" + move;
  }

}
