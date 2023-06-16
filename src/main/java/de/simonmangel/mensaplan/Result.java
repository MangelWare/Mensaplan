package de.simonmangel.mensaplan;

import java.util.List;

public class Result {
  
  private final String weekday;
  private final boolean isToday;
  private final RWTHMensa mensa;

  private final List<MenuEntry> menuEntries;
  private final List<ExtraEntry> extraEntries;

  public Result(String weekday, boolean isToday, RWTHMensa mensa, List<MenuEntry> menuEntries, List<ExtraEntry> extraEntries) {
    this.weekday = weekday;
    this.isToday = isToday;
    this.mensa = mensa;
    this.menuEntries = menuEntries;
    this.extraEntries = extraEntries;
  }

  public String getWeekday() {
    return weekday;
  }

  public boolean isToday() {
    return isToday;
  }

  public List<MenuEntry> getMenuEntries() {
    return menuEntries;
  }

  public List<ExtraEntry> getExtraEntries() {
    return extraEntries;
  }


  public RWTHMensa getMensa() {
    return mensa;
  }

  static class MenuEntry {
    private final String type;
    private final String meal;
    private final String price;
    
    public MenuEntry(String type, String meal, String price) {
      this.type = type;
      this.meal = meal;
      this.price = price;
    }

    public String getType() {
      return type;
    }

    public String getMeal() {
      return meal;
    }

    public String getPrice() {
      return price;
    }
    
  }

  static class ExtraEntry {
    private final String type;
    private final String meal;

    public ExtraEntry(String type, String meal) {
      this.type = type;
      this.meal = meal;
    }

    public String getType() {
      return type;
    }
    public String getMeal() {
      return meal;
    }
    
  }

}
