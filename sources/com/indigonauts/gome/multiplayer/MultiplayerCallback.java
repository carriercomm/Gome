//#condition BT || IGS
/*
 * (c) 2006 Indigonauts
 */
package com.indigonauts.gome.multiplayer;

public interface MultiplayerCallback {

  static final byte MESSAGE_ERROR_TYPE = 0x0;
  static final byte MESSAGE_CHAT_TYPE = 0x1;
  static final byte MESSAGE_KIBITZING_TYPE = 0x2;

  //#if IGS
  void loggedEvent();

  //#endif

  //#if BT
  void connectedBTEvent(MultiplayerConnector connector);

  //#endif

  //#if IGS
  void gameListEvent(Game[] games);

  void userListEvent();

  void observeEvent(Move[] moves);

  //#endif

  void moveEvent(Move move);

  void challenge(Challenge challenge);

  void message(byte type, String nick, String message);

  void startGame(Challenge challenge, char mode);

  void synOnlineTime(int whiteTime, int whiteByoStone, int blackTime, int blackByoStone);

  void timesUP(String name);

  void endGame();

  void restoreGameForCounting();

  void oppRemoveDeadStone(byte x, byte y);

  void gameIsDone(String player1, int score1, String player2, int score2);

  void oppSetHandicap(byte handicap);

  //#if IGS
  void setKomi(byte komi);

  void onlineSetKomi(byte komi);

  void oppWantToSetNewKomi(byte komi);

  //#endif

  void onlineResigned(String name);

  void onlineScore(int whiteScore, int blackScore);

  void winByValue(String name, int winByValue);

  void setSplashInfo(String message);
}
