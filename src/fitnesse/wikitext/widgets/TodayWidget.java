package fitnesse.wikitext.widgets;

import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.GregorianCalendar;
import java.util.Locale;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class TodayWidget extends ParentWidget {
  public static final String REGEXP = "!today(?: +(?:-t|-xml|-lang=\\w\\w|\\(.*\\)))*( +(([+-]\\d+)([dhm])?))?";
  public static final Pattern PATTERN = Pattern.compile("!today( +(?:(-t)|(-xml)|((-lang=)(\\w\\w))|\\((.*)\\)))*( +(([+-]\\d+)([dhm])?))?");
  private boolean withTime = false;
  private boolean xml = false;
  private SimpleDateFormat explicitDateFormat = null;
  private int timeDiff;
  private int timeField;
  private boolean lang;
  private String langCode;
  private SimpleDateFormat xmlDateFormat = new SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ss");

  public static Calendar todayForTest = null;

  public TodayWidget(ParentWidget parent, String text) throws Exception {
    super(parent);
    Matcher match = PATTERN.matcher(text);
    if (!match.find()) {
      System.err.println("TodayWidget: match was not found, text = '" + text + "'");
    } else {
      withTime = (match.group(2) != null);
      xml = (match.group(3) != null);
      lang = (match.group(4) != null);
      if (lang) {
        langCode = match.group(6);
      }

      String formatString = match.group(7);

      if (formatString != null) {
        explicitDateFormat = new SimpleDateFormat(formatString, getLocale());
      }

      if (match.group(9) != null) {
        String s = match.group(10);
        if (s != null) {
          if (s.startsWith("+")) {
            s = s.substring(1);
          }
          timeDiff = Integer.parseInt(s);
          String s1 = match.group(11);
          if (s1 == null || "d".equals(s1)) {
            timeField = Calendar.DAY_OF_MONTH;
          } else if ("h".equals(s1)) {
            timeField = Calendar.HOUR_OF_DAY;
          } else if ("m".equals(s1)) {
            timeField = Calendar.MINUTE;
          }
        }
      }
    }
  }
  
  private Locale getLocale() {
    return lang ? new Locale(langCode) : Locale.getDefault();
  }

  public String render() throws Exception {
    Calendar cal = todayForTest != null ? todayForTest : GregorianCalendar.getInstance();
    cal.add(timeField, timeDiff);

    Date date = cal.getTime();

    String result;
    if (withTime) {
      result = DateFormat.getDateTimeInstance(DateFormat.MEDIUM, DateFormat.SHORT, getLocale()).format(date);
    } else if (xml) {
      result = xmlDateFormat.format(date);
    } else {
      if (explicitDateFormat != null) {
        result = explicitDateFormat.format(date);
      } else {
        result = DateFormat.getDateInstance(DateFormat.MEDIUM, getLocale()).format(date);
      }
    }
    return result;
  }
}
