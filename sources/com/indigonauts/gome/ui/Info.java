package com.indigonauts.gome.ui;

import java.io.IOException;
import java.util.Enumeration;

import javax.microedition.lcdui.Command;
import javax.microedition.lcdui.CommandListener;
import javax.microedition.lcdui.Display;
import javax.microedition.lcdui.Displayable;
import javax.microedition.lcdui.Font;
import javax.microedition.lcdui.Form;
import javax.microedition.lcdui.Item;
import javax.microedition.lcdui.StringItem;

import com.indigonauts.gome.Gome;
import com.indigonauts.gome.MainCanvas;
import com.indigonauts.gome.common.StringVector;
import com.indigonauts.gome.common.Util;
import com.indigonauts.gome.i18n.I18N;
import com.indigonauts.gome.io.IOManager;
import com.indigonauts.gome.sgf.SgfModel;

public class Info extends Fetcher implements CommandListener, Showable {
  //#if DEBUG
  private static final org.apache.log4j.Logger log = org.apache.log4j.Logger.getLogger("Info");
  //#endif

  private Showable parent;
  private Form current = getKeys();
  private String urlToFetch;
  private String title;

  private static final Command RULES;
  private static final Command KEYS;
  private static final Command HELP;
  private static final Command ABOUT;
  private boolean inSubMenu = false;
  static {
    RULES = new Command(I18N.help.rules, Command.SCREEN, 1); //$NON-NLS-1$
    HELP = new Command(I18N.help.help, Command.SCREEN, 1); //$NON-NLS-1$
    KEYS = new Command(I18N.help.key, Command.SCREEN, 1); //$NON-NLS-1$
    ABOUT = new Command(I18N.about, Command.SCREEN, 9); //$NON-NLS-1$
  }

  /**
   * For external entities
   * @param parent
   * @param title
   * @param urlToFetch
   */
  public Info(Showable parent, String title, String urlToFetch) {
    this.title = title;
    this.urlToFetch = urlToFetch;
    this.parent = parent;
    inSubMenu = false;
    super.show(Gome.singleton.display);
    super.start();

  }

  public Info(Showable parent) {
    //#if DEBUG
    log.debug("help");
    //#endif

    this.parent = parent;
    this.current = getKeys();
    setUpCurrentHelp();

    //#if DEBUG
    log.debug("end if constructor");
    //#endif

  }

  public Info(MainCanvas mainCanvas, Command def) {
    this(mainCanvas);
    commandAction(def, null);
    inSubMenu = false; // so it jumps back directly
  }

  private void setUpCurrentExternal() {
    current.addCommand(MenuEngine.BACK);
    current.setCommandListener(this);
  }

  private void setUpCurrentHelp() {
    current.addCommand(KEYS);
    current.addCommand(HELP);
    current.addCommand(RULES);
    current.addCommand(MenuEngine.GAME_STATUS);
    current.addCommand(ABOUT);
    current.addCommand(MenuEngine.BACK);
    current.setCommandListener(this);
  }

  public void commandAction(Command command, Displayable disp) {
    if (command == MenuEngine.BACK) {
      if (inSubMenu) {
        current = getKeys();
        inSubMenu = false;
      } else {
        parent.show(Gome.singleton.display);
        parent = null; // some help for the garbage collector
        return;
      }
    } else if (command == RULES) {
      fetchRules();
      inSubMenu = true;
      super.show(Gome.singleton.display);
      super.start();
      return;
    } else if (command == KEYS) {
      current = getKeys();
      inSubMenu = false;
    } else if (command == HELP) {
      fetchHelp();
      inSubMenu = true;
      super.show(Gome.singleton.display);
      super.start();
      return;

    } else if (command == MenuEngine.GAME_STATUS) {
      current = getGameInfo();
      inSubMenu = true;
    } else if (command == ABOUT) {
      current = getAbout();
      inSubMenu = true;
    }

    setUpCurrentHelp();
    show(Gome.singleton.display);
  }

  public void show(Display destination) {

    destination.setCurrent(current);
  }

  private Form getKeys() {
    MainCanvas canvas = Gome.singleton.mainCanvas;
    Form help = new Form(I18N.help.help);
    StringBuffer buf = new StringBuffer();
    buf.append(I18N.help.pointerReview1); //$NON-NLS-1$
    buf.append('\n');
    buf.append(I18N.help.pointerReview2); //$NON-NLS-1$
    buf.append('\n');
    buf.append(I18N.help.pointerReview3); //$NON-NLS-1$
    buf.append('\n');
    buf.append(I18N.help.pointerReview4); //$NON-NLS-1$
    buf.append(I18N.help.pointer); //$NON-NLS-1$
    buf.append('\n');
    buf.append(Util.getActionKeyName(canvas, MainCanvas.ACTION_COMMENT));
    buf.append(' ');
    buf.append(I18N.help.comment); //$NON-NLS-1$
    buf.append('\n');
    buf.append(Util.getActionKeyName(canvas, MainCanvas.ACTION_ZOOM));
    buf.append(' ');
    buf.append(I18N.help.zoom); //$NON-NLS-1$
    buf.append('\n');
    buf.append(Util.getActionKeyName(canvas, MainCanvas.ACTION_UNDO));
    buf.append(' ');
    buf.append(I18N.help.undo); //$NON-NLS-1$
    buf.append('\n');
    buf.append(Util.getActionKeyName(canvas, MainCanvas.ACTION_HINT));
    buf.append(' ');
    buf.append(I18N.help.hint); //$NON-NLS-1$
    buf.append('\n');
    buf.append(Util.safeGetKeyName(canvas, canvas.KEY_SCROLLUP));
    buf.append(' ');
    buf.append(I18N.help.scrollUp); //$NON-NLS-1$
    buf.append('\n');
    buf.append(Util.safeGetKeyName(canvas, canvas.KEY_SCROLLDOWN));
    buf.append(' ');
    buf.append(I18N.help.scrollDown); //$NON-NLS-1$
    buf.append('\n');
    buf.append(Util.safeGetKeyName(canvas, canvas.KEY_10NEXTMOVES));
    buf.append(' ');
    buf.append(I18N.help.next10Moves); //$NON-NLS-1$
    buf.append('\n');
    buf.append(Util.safeGetKeyName(canvas, canvas.KEY_10PREVMOVES));
    buf.append(' ');
    buf.append(I18N.help.prev10Moves); //$NON-NLS-1$
    buf.append('\n');
    buf.append(Util.safeGetKeyName(canvas, canvas.KEY_NEXTCORNER));
    buf.append(' ');
    buf.append(I18N.help.nextCorner); //$NON-NLS-1$
    buf.append('\n');
    buf.append(Util.safeGetKeyName(canvas, canvas.KEY_PREVCORNER));
    buf.append(' ');
    buf.append(I18N.help.prevCorner); //$NON-NLS-1$

    StringItem si = new StringItem("", buf.toString());
    si.setFont(MainCanvas.SMALL_FONT);
    help.append(si);
    return help;
  }

  private Form getGameInfo() {
    Form form = new Form(I18N.game.info);
    GameController gc = Gome.singleton.gameController;
    SgfModel model = gc.getSgfModel();

    StringBuffer info = new StringBuffer();
    info.append(I18N.game.captured); //$NON-NLS-1$
    info.append(gc.getBoard().getNbCapturedBlack());
    info.append('/');
    info.append(gc.getBoard().getNbCapturedWhite());
    info.append('\n');
    if (model.getName() != null) {
      info.append(I18N.game.name); //$NON-NLS-1$
      info.append(model.getName());
      info.append('\n');
    }
    if (model.getEvent() != null) {
      info.append(I18N.game.event); //$NON-NLS-1$
      info.append(model.getEvent());
      info.append('\n');
    }
    if (model.getRound() != null) {
      info.append(I18N.game.round); //$NON-NLS-1$
      info.append(model.getRound());
      info.append('\n');
    }
    if (model.getDate() != null) {
      info.append(" "); //$NON-NLS-1$
      info.append(model.getDate());
    }
    if (model.getBlackPlayer() != null && model.getWhitePlayer() != null) {
      info.append('\n');
      info.append(model.getWhitePlayer());
      if (model.getWhiteRank() != null) {
        info.append(I18N.game.whiteShort); //$NON-NLS-1$
        info.append('[');
        info.append(model.getWhiteRank());
        info.append(']');
      }
      info.append(' ');
      info.append(I18N.game.versus); //$NON-NLS-1$
      info.append(' ');
      info.append(model.getBlackPlayer());
      if (model.getBlackRank() != null) {
        info.append(I18N.game.blackShort); //$NON-NLS-1$
        info.append('[');
        info.append(model.getBlackRank());
        info.append(']');
      }
      info.append('\n');
    }
    if (model.getBlackTeam() != null & model.getWhiteTeam() != null) {
      info.append(model.getWhiteTeam());
      info.append(' ');
      info.append(I18N.game.versus); //$NON-NLS-1$
      info.append(' ');
      info.append(model.getBlackTeam());
      info.append('\n');
    }
    if (model.getKomi() != null) {
      info.append(I18N.game.komi); //$NON-NLS-1$
      info.append(model.getKomi());
      info.append('\n');
    }
    if (model.getResult() != null) {
      info.append(I18N.game.result); //$NON-NLS-1$
      info.append(model.getResult());
      info.append('\n');
    }
    if (model.getOpening() != null) {
      info.append(I18N.game.opening); //$NON-NLS-1$
      info.append(model.getOpening());
      info.append('\n');
    }
    if (model.getPlace() != null) {
      info.append(I18N.game.place); //$NON-NLS-1$
      info.append(model.getPlace());
      info.append('\n');
    }
    if (model.getContext() != null) {
      info.append(I18N.game.context); //$NON-NLS-1$
      info.append(model.getContext());
      info.append('\n');
    }
    if (model.getScribe() != null) {
      info.append(I18N.game.scribe); //$NON-NLS-1$
      info.append(model.getScribe());
      info.append('\n');
    }
    if (model.getSource() != null) {
      info.append(I18N.game.source); //$NON-NLS-1$
      info.append(model.getSource());
      info.append('\n');
    }
    if (model.getApplication() != null) {
      info.append(I18N.game.application); //$NON-NLS-1$
      info.append(model.getApplication());
      info.append('\n');
    }
    if (model.getCopyright() != null) {
      info.append(I18N.game.copyright); //$NON-NLS-1$
      info.append(model.getCopyright());
      info.append('\n');
    }
    StringItem si = new StringItem("", info.toString());
    si.setFont(MainCanvas.SMALL_FONT);
    form.append(si);
    return form;
  }

  private Form getAbout() {
    Form form = new Form(I18N.about);
    StringBuffer buf = new StringBuffer("GOME v");
    buf.append(Gome.VERSION);
    buf.append("\n\n");
    buf.append("(c) 2005-2007 Indigonauts");
    buf.append("\n\n");
    StringItem url = new StringItem("", "http://www.indigonauts.com/gome", Item.HYPERLINK);
    url.setFont(MainCanvas.SMALL_FONT);
    form.append(buf.toString());
    form.append(url);

    return form;

  }

  private static final Font TITLE_FONT = Font.getFont(Font.FACE_PROPORTIONAL, Font.STYLE_BOLD, Font.SIZE_MEDIUM);
  private static final Font UNDERLINED_FONT = Font.getFont(Font.FACE_PROPORTIONAL, Font.STYLE_UNDERLINED, Font.SIZE_MEDIUM);

  public static Form formatHelp(String name, String url) {
    Form form = new Form(name);
    try {
      byte[] file = IOManager.singleton.loadFile(url, null);
      StringVector list = new StringVector(new String(file), '\n');
      Enumeration all = list.elements();

      while (all.hasMoreElements()) {
        String element = (String) all.nextElement();
        if (element.startsWith("(;")) {
          form.append(Util.generatePosition(element));
        } else if (element.startsWith("*")) {
          StringItem si = new StringItem("", element.substring(1));
          si.setFont(TITLE_FONT);
          form.append(si);
        } else if (element.startsWith("_")) {
          StringItem si = new StringItem("", element.substring(1));
          si.setFont(UNDERLINED_FONT);
          form.append(si);
        } else {
          StringItem si = new StringItem("", element);
          si.setFont(MainCanvas.SMALL_FONT);
          form.append(si);
        }
        form.append("\n");
      }
    } catch (IOException ioe) {// TODO: error handling
    }
    return form;
  }

  private void fetchHelp() {
    title = I18N.help.help;
    urlToFetch = "jar:/com/indigonauts/gome/i18n/help/general_US.hlp";
  }

  private void fetchRules() {
    title = I18N.help.rules;
    urlToFetch = "http://www.indigonauts.com/gome/library/help/rules_" + Gome.LOCALE + ".hlp";
  }

  protected void download() throws IOException {
    current = formatHelp(title, urlToFetch);
    setUpCurrentExternal();
  }

  protected void downloadFailed(Exception reason) {
    parent.show(Gome.singleton.display);
  }

  protected void downloadFinished() {
    show(Gome.singleton.display);

  }

}
