package com.codeforcommunity.dto.site;

import com.fasterxml.jackson.annotation.JsonFormat;
import java.sql.Date;

public class StewardshipActivity {
  private final int id;
  private final int userId;

  @JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "MM/dd/yyyy", timezone="America/New_York")
  private final Date date;

  private final Boolean watered;
  private final Boolean mulched;
  private final Boolean cleaned;
  private final Boolean weeded;

  public StewardshipActivity(
      int id,
      int userId,
      Date date,
      Boolean watered,
      Boolean mulched,
      Boolean cleaned,
      Boolean weeded) {
    this.id = id;
    this.userId = userId;
    this.date = date;
    this.watered = watered;
    this.mulched = mulched;
    this.cleaned = cleaned;
    this.weeded = weeded;
  }

  public int getId() {
    return id;
  }

  public int getUserId() {
    return userId;
  }

  public Date getDate() {
    return date;
  }

  public Boolean getWatered() {
    return watered;
  }

  public Boolean getMulched() {
    return mulched;
  }

  public Boolean getCleaned() {
    return cleaned;
  }

  public Boolean getWeeded() {
    return weeded;
  }
}
