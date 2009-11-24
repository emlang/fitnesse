// Copyright (C) 2003-2009 by Object Mentor, Inc. All rights reserved.
// Released under the terms of the CPL Common Public License version 1.0.
package fitnesse.slimTables;

import fitnesse.responders.run.slimResponder.MockSlimTestContext;
import static fitnesse.slimTables.SlimTable.Disgracer.disgraceClassName;
import static fitnesse.slimTables.SlimTable.Disgracer.disgraceMethodName;
import static fitnesse.slimTables.SlimTable.approximatelyEqual;
import static org.junit.Assert.*;
import org.junit.Test;

import java.util.Map;

public class SlimTableTest {
  @Test
  public void gracefulClassNames() throws Exception {
    assertEquals("MyClass", disgraceClassName("my class"));
    assertEquals("myclass", disgraceClassName("myclass"));
    assertEquals("x.y", disgraceClassName("x.y"));
    assertEquals("x_y", disgraceClassName("x_y"));
    assertEquals("MeAndMrs_jones", disgraceClassName("me and mrs_jones"));
    assertEquals("PageCreator", disgraceClassName("Page creator."));
  }

  @Test
  public void gracefulMethodNames() throws Exception {
    assertEquals("myMethodName", disgraceMethodName("my method name"));
    assertEquals("myMethodName", disgraceMethodName("myMethodName"));
    assertEquals("my_method_name", disgraceMethodName("my_method_name"));
    assertEquals("getStringArgs", disgraceMethodName("getStringArgs"));
    assertEquals("setMyVariableName", disgraceMethodName("set myVariableName"));
  }

  @Test
  public void trulyEqual() throws Exception {
    assertTrue(approximatelyEqual("3.0", "3.0"));
  }

  @Test
  public void veryUnequal() throws Exception {
    assertFalse(approximatelyEqual("5", "3"));
  }

  @Test
  public void isWithinPrecision() throws Exception {
    assertTrue(approximatelyEqual("3", "2.5"));
  }

  @Test
  public void justTooBig() throws Exception {
    assertFalse(approximatelyEqual("3.000", "3.0005"));
  }

  @Test
  public void justTooSmall() throws Exception {
    assertFalse(approximatelyEqual("3.0000", "2.999949"));
  }

  @Test
  public void justSmallEnough() throws Exception {
    assertTrue(approximatelyEqual("-3.00", "-2.995"));
  }

  @Test
  public void justBigEnough() throws Exception {
    assertTrue(approximatelyEqual("-3.000000", "-3.000000499"));
  }

  @Test
  public void classicRoundUp() throws Exception {
    assertTrue(approximatelyEqual("3.05", "3.049"));
  }

  @Test
  public void replaceSymbolsShouldReplaceSimpleSymbol() throws Exception {
    SlimTable table = new MockTable();
    table.setSymbol("x", "a");
    assertEquals("this is a", table.replaceSymbols("this is $x"));
  }

  @Test
  public void replaceSymbolsShouldReplaceMoreThanOneSymbol() throws Exception {
    SlimTable table = new MockTable();
    table.setSymbol("x", "a");
    table.setSymbol("y", "b");
    assertEquals("this is a and b", table.replaceSymbols("this is $x and $y"));
  }

  @Test
  public void replaceSymbolsShouldConcatenate() throws Exception {
    SlimTable table = new MockTable();
    table.setSymbol("x", "a");
    table.setSymbol("y", "b");
    assertEquals("this is ab", table.replaceSymbols("this is $x$y"));
  }

  @Test
  public void replaceSymbolsShouldReplaceSameSymbolMoreThanOnce() throws Exception {
    SlimTable table = new MockTable();
    table.setSymbol("x", "a");
    assertEquals("this is a and a again", table.replaceSymbols("this is $x and $x again"));
  }

  @Test
  public void replaceSymbolsShouldMatchFullSymbolName() throws Exception {
    SlimTable table = new MockTable();
    table.setSymbol("V", "v");
    table.setSymbol("VX", "x");
    String actual = table.replaceSymbols("$V $VX");
    assertEquals("v x", actual);
  }

  @Test
  public void replaceSymbolsFullExpansion_ShouldReplaceSimpleSymbol() throws Exception {
    SlimTable table = new MockTable();
    table.setSymbol("x", "a");
    assertEquals("this is $x->[a]", table.replaceSymbolsWithFullExpansion("this is $x"));
  }

  @Test
  public void replaceSymbolsFullExpansion_ShouldReplaceMoreThanOneSymbol() throws Exception {
    SlimTable table = new MockTable();
    table.setSymbol("x", "a");
    table.setSymbol("y", "b");
    assertEquals("this is $x->[a] and $y->[b]", table.replaceSymbolsWithFullExpansion("this is $x and $y"));
  }

  @Test
  public void replaceSymbolsFullExpansion_ShouldReplaceSameSymbolMoreThanOnce() throws Exception {
    SlimTable table = new MockTable();
    table.setSymbol("x", "a");
    assertEquals("this is $x->[a] and $x->[a] again", table.replaceSymbolsWithFullExpansion("this is $x and $x again"));
  }

  @Test
  public void replaceSymbolsFullExpansion_ShouldMatchFullSymbolName() throws Exception {
    SlimTable table = new MockTable();
    table.setSymbol("V", "v");
    table.setSymbol("VX", "x");
    String actual = table.replaceSymbolsWithFullExpansion("$V $VX");
    assertEquals("$V->[v] $VX->[x]", actual);
  }


  private static class MockTable extends SlimTable {
    public MockTable() {
      super(new MockSlimTestContext());
    }

    protected String getTableType() {
      return null;
    }

    public void appendInstructions() {
    }

    public void evaluateReturnValues(Map<String, Object> returnValues) throws Exception {
    }
  }
}
