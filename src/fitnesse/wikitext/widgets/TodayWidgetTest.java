package fitnesse.wikitext.widgets;

import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertTrue;

import java.text.DateFormat;
import java.util.Calendar;
import java.util.GregorianCalendar;
import java.util.Locale;
import java.util.regex.Pattern;

import org.junit.After;
import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;

public class TodayWidgetTest {
  private Calendar comparisonDate;
  private DateFormat comparisonFormat;
  
  @Before
  public void setup() {
    comparisonDate = new GregorianCalendar(1952, Calendar.DECEMBER, 5, 1, 13, 23); // make this a new object to be safe
    TodayWidget.todayForTest = new GregorianCalendar(1952, Calendar.DECEMBER, 5, 1, 13, 23);  //GDTH unix date!!!  Eleven == Dec
  }

  @After
  public void teardown() {
    TodayWidget.todayForTest = null;
    comparisonDate = null;
    comparisonFormat = null;
  }

  private boolean matches(String widget) {
    return Pattern.matches(TodayWidget.REGEXP, widget);
  }

  private void assertRenders(String widgetString, String result) throws Exception {
    TodayWidget widget = new TodayWidget(new MockWidgetRoot(), widgetString);
    Assert.assertEquals(result, widget.render());
  }

  @Test
  public void shouldMatch() throws Exception {
    assertTrue(matches("!today"));
    assertTrue(matches("!today -t"));
    assertTrue(matches("!today -xml"));
    assertTrue(matches("!today +3"));
    assertTrue(matches("!today -3"));
    assertTrue(matches("!today -3d"));
    assertTrue(matches("!today +2h"));
    assertTrue(matches("!today -7m"));
    assertTrue(matches("!today (MMM)"));
    assertTrue(matches("!today (MMM) +3"));
    assertTrue(matches("!today -lang=en"));
    assertTrue(matches("!today -lang=de (dd.MM.yyyy)"));
  }

  @Test
  public void shouldNotMatch() throws Exception {
    assertFalse(matches("!today -p"));
    assertFalse(matches("!today 33"));
    assertFalse(matches("!today x"));
    assertFalse(matches("!today -lang=xxx"));
    assertFalse(matches("!today -4k"));
  }

  @Test
  public void today() throws Exception {
    comparisonFormat = DateFormat.getDateInstance(DateFormat.MEDIUM);
    assertRenders("!today", comparisonFormat.format(comparisonDate.getTime()));
  }

  @Test
  public void withTime() throws Exception {
    comparisonFormat = DateFormat.getDateTimeInstance(DateFormat.MEDIUM, DateFormat.SHORT);
    assertRenders("!today -t", comparisonFormat.format(comparisonDate.getTime()));
  }

  @Test
  public void xml() throws Exception {
    assertRenders("!today -xml", "1952-12-05T01:13:23");
  }

  @Test
  public void subtractSevenMinutes() throws Exception {
    assertRenders("!today -lang=en -t -7m", "Dec 5, 1952 1:06 AM");
  }
  
  @Test
  public void subtractTwentyfiveHours() throws Exception {
    assertRenders("!today -lang=nl -t -25h", "4-dec-1952 0:13");
  }
  
  @Test
  public void addOneDay() throws Exception {
    assertRenders("!today -lang=de +1", "06.12.1952");
  }

  @Test
  public void subtractOneDay() throws Exception {
    comparisonDate.add(Calendar.DAY_OF_MONTH, -1);
    comparisonFormat = DateFormat.getDateInstance(DateFormat.MEDIUM);
    assertRenders("!today -1", comparisonFormat.format(comparisonDate.getTime()));
  }
  
  @Test
  public void addTwoDaysWithLetter() throws Exception {
    comparisonDate.add(Calendar.DAY_OF_MONTH, 2);
    comparisonFormat = DateFormat.getDateInstance(DateFormat.MEDIUM);
    assertRenders("!today +2d", comparisonFormat.format(comparisonDate.getTime()));
  }

  @Test
  public void subtractOneWeek() throws Exception {
    comparisonDate.add(Calendar.DAY_OF_MONTH, -7);
    comparisonFormat = DateFormat.getDateInstance(DateFormat.MEDIUM);
    assertRenders("!today -7", comparisonFormat.format(comparisonDate.getTime()));
  }

  @Test
  public void addOneYear() throws Exception {
    comparisonDate.add(Calendar.DAY_OF_MONTH, 365);
    comparisonFormat = DateFormat.getDateInstance(DateFormat.MEDIUM);
    assertRenders("!today +365", comparisonFormat.format(comparisonDate.getTime()));
  }
  
  @Test
  public void format() throws Exception {
    assertRenders("!today -lang=en (MMM)", "Dec");
  }

  @Test
  public void formatPlusOneDay() throws Exception {
    assertRenders("!today -lang=en (ddMMM) +1", "06Dec");
  }
  
  @Test
  public void formatSpecificLocale() throws Exception {
    assertRenders("!today -lang=de (MMM)", "Dez");
  }
  
  @Test
  public void testLocaleOverride() throws Exception {
    Locale currentLocale = Locale.getDefault();
    Locale.setDefault(Locale.CHINESE);
    assertRenders("!today -lang=en (ddMMM) +1", "06Dec");
    Locale.setDefault(currentLocale);
  }

}
