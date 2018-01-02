package com.helger.pgcc.parser.test;

import java.io.BufferedReader;
import java.io.FileInputStream;
import java.io.InputStreamReader;

import com.helger.pgcc.JavaCCTestCase;
import com.helger.pgcc.parser.CodeGenerator;
import com.helger.pgcc.parser.JavaCCGlobals;
import com.helger.pgcc.parser.JavaCCParser;
import com.helger.pgcc.parser.LexGenJava;
import com.helger.pgcc.parser.Main;
import com.helger.pgcc.parser.NfaState;
import com.helger.pgcc.parser.Options;
import com.helger.pgcc.parser.Semanticize;

/**
 * A sea anchor, to ensure that code is not inadvertently broken.
 *
 * @author timp
 * @since 16 Mar 2007
 */
public class NfaStateTest extends JavaCCTestCase
{

  String parserInput = getJJInputDirectory () + "JavaCC.jj";

  /**
   * @param name
   *        test case name
   */
  public NfaStateTest (final String name)
  {
    super (name);
  }

  /**
   * {@inheritDoc}
   *
   * @throws Exception
   * @see junit.framework.TestCase#setUp()
   */
  @Override
  protected void setUp () throws Exception
  {
    super.setUp ();
    Options.init ();
    Main.reInitAll ();
  }

  /**
   * {@inheritDoc}
   *
   * @throws Exception
   * @see junit.framework.TestCase#tearDown()
   */
  @Override
  protected void tearDown () throws Exception
  {
    super.tearDown ();
  }

  protected void setupState ()
  {
    try
    {
      final JavaCCParser parser = new JavaCCParser (new BufferedReader (new InputStreamReader (new FileInputStream (parserInput),
                                                                                               Options.getGrammarEncoding ())));
      parser.javacc_input ();
      JavaCCGlobals.s_fileName = JavaCCGlobals.s_origFileName = parserInput;
      JavaCCGlobals.s_jjtreeGenerated = JavaCCGlobals.isGeneratedBy ("JJTree", parserInput);
      JavaCCGlobals.s_toolNames = JavaCCGlobals.getToolNames (parserInput);
      Semanticize.start ();
      new LexGenJava ().start ();
    }
    catch (final Exception ex)
    {
      throw new RuntimeException (ex);
    }
  }

  /**
   * Test method for
   * {@link org.javacc.parser.NfaState# DumpStateSets(CodeGenerator)}.
   */
  public void testDumpStateSets ()
  {
    final CodeGenerator cg = new CodeGenerator ();
    NfaState.dumpStateSets (cg);
    final String result = cg.getGeneratedCode ().replaceAll ("\r", "");
    assertEquals ("static final int[] jjnextStates = {0\n};\n\n", result);
  }

  /**
   * Test method for
   * {@link org.javacc.parser.NfaState#DumpStateSets(CodeGenerator)}.
   */
  public void testDumpStateSetsInitialised ()
  {
    final CodeGenerator cg = new CodeGenerator ();
    setupState ();
    NfaState.dumpStateSets (cg);
    assertEquals ("static final int[] jjnextStates = {\n" +
                  "   34, 35, 12, 38, 39, 42, 43, 23, 24, 26, 14, 16, 49, 51, 6, 52, \n" +
                  "   59, 8, 9, 12, 23, 24, 28, 26, 34, 35, 12, 44, 45, 12, 53, 54, \n" +
                  "   60, 61, 62, 10, 11, 17, 18, 20, 25, 27, 29, 36, 37, 40, 41, 46, \n" +
                  "   47, 55, 56, 57, 58, 63, 64, \n" +
                  "};",
                  cg.getGeneratedCode ().replaceAll ("\r", "").trim ());
  }

  /**
   * Test method for
   * {@link org.javacc.parser.NfaState# DumpCharAndRangeMoves(CodeGenerator)}.
   */
  public void testDumpCharAndRangeMoves ()
  {
    final CodeGenerator cg = new CodeGenerator ();
    NfaState.dumpCharAndRangeMoves (cg);
    final String result = cg.getGeneratedCode ().replaceAll ("\r", "");
    assertEquals ("         int i2 = (curChar & 0xff) >> 6;\n" +
                  "         long l2 = 1L << (curChar & 077);\n" +
                  "         do\n" +
                  "         {\n" +
                  "            switch(jjstateSet[--i])\n" +
                  "            {\n" +
                  "               default : break;\n" +
                  "            }\n" +
                  "         } while(i != startsAt);\n\n",
                  result);
  }

  /**
   * Test method for
   * {@link org.javacc.parser.NfaState#DumpCharAndRangeMoves(CodeGenerator)}.
   */
  public void testDumpCharAndRangeMovesInitialised ()
  {
    final CodeGenerator cg = new CodeGenerator ();
    setupState ();
    NfaState.dumpCharAndRangeMoves (cg);
    assertEquals (("         int hiByte = (curChar >> 8);\n" +
                   "         int i1 = hiByte >> 6;\n" +
                   "         long l1 = 1L << (hiByte & 077);\n" +
                   "         int i2 = (curChar & 0xff) >> 6;\n" +
                   "         long l2 = 1L << (curChar & 077);\n" +
                   "         do\n" +
                   "         {\n" +
                   "            switch(jjstateSet[--i])\n" +
                   "            {\n" +
                   "               default : if (i1 == 0 || l1 == 0 || i2 == 0 ||  l2 == 0) break; else break;\n" +
                   "            }\n" +
                   "         } while(i != startsAt);\n").trim (),
                  cg.getGeneratedCode ().replaceAll ("\r", "").trim ());
  }

  /**
   * Test method for
   * {@link org.javacc.parser.NfaState#DumpNonAsciiMoveMethods(CodeGenerator)}.
   */
  public void testDumpNonAsciiMoveMethods ()
  {
    final CodeGenerator cg = new CodeGenerator ();
    NfaState.dumpNonAsciiMoveMethods (cg);
    final String result = cg.getGeneratedCode ();
    assertEquals ("", result.trim ());
  }

  /**
   * Test method for
   * {@link org.javacc.parser.NfaState#DumpNonAsciiMoveMethods(CodeGenerator)}.
   */
  public void testDumpNonAsciiMoveMethodsInitialised ()
  {
    final CodeGenerator cg = new CodeGenerator ();
    setupState ();
    NfaState.dumpNonAsciiMoveMethods (cg);
    assertEquals ("private static final boolean jjCanMove_0(int hiByte, int i1, int i2, long l1, long l2)\n" +
                  "{\n" +
                  "   switch(hiByte)\n" +
                  "   {\n" +
                  "      case 0:\n" +
                  "         return ((jjbitVec2[i2] & l2) != 0L);\n" +
                  "      default :\n" +
                  "         if ((jjbitVec0[i1] & l1) != 0L)\n" +
                  "            return true;\n" +
                  "         return false;\n" +
                  "   }\n" +
                  "}\n" +
                  "private static final boolean jjCanMove_1(int hiByte, int i1, int i2, long l1, long l2)\n" +
                  "{\n" +
                  "   switch(hiByte)\n" +
                  "   {\n" +
                  "      case 0:\n" +
                  "         return ((jjbitVec4[i2] & l2) != 0L);\n" +
                  "      case 2:\n" +
                  "         return ((jjbitVec5[i2] & l2) != 0L);\n" +
                  "      case 3:\n" +
                  "         return ((jjbitVec6[i2] & l2) != 0L);\n" +
                  "      case 4:\n" +
                  "         return ((jjbitVec7[i2] & l2) != 0L);\n" +
                  "      case 5:\n" +
                  "         return ((jjbitVec8[i2] & l2) != 0L);\n" +
                  "      case 6:\n" +
                  "         return ((jjbitVec9[i2] & l2) != 0L);\n" +
                  "      case 7:\n" +
                  "         return ((jjbitVec10[i2] & l2) != 0L);\n" +
                  "      case 9:\n" +
                  "         return ((jjbitVec11[i2] & l2) != 0L);\n" +
                  "      case 10:\n" +
                  "         return ((jjbitVec12[i2] & l2) != 0L);\n" +
                  "      case 11:\n" +
                  "         return ((jjbitVec13[i2] & l2) != 0L);\n" +
                  "      case 12:\n" +
                  "         return ((jjbitVec14[i2] & l2) != 0L);\n" +
                  "      case 13:\n" +
                  "         return ((jjbitVec15[i2] & l2) != 0L);\n" +
                  "      case 14:\n" +
                  "         return ((jjbitVec16[i2] & l2) != 0L);\n" +
                  "      case 15:\n" +
                  "         return ((jjbitVec17[i2] & l2) != 0L);\n" +
                  "      case 16:\n" +
                  "         return ((jjbitVec18[i2] & l2) != 0L);\n" +
                  "      case 17:\n" +
                  "         return ((jjbitVec19[i2] & l2) != 0L);\n" +
                  "      case 18:\n" +
                  "         return ((jjbitVec20[i2] & l2) != 0L);\n" +
                  "      case 19:\n" +
                  "         return ((jjbitVec21[i2] & l2) != 0L);\n" +
                  "      case 20:\n" +
                  "         return ((jjbitVec0[i2] & l2) != 0L);\n" +
                  "      case 22:\n" +
                  "         return ((jjbitVec22[i2] & l2) != 0L);\n" +
                  "      case 23:\n" +
                  "         return ((jjbitVec23[i2] & l2) != 0L);\n" +
                  "      case 24:\n" +
                  "         return ((jjbitVec24[i2] & l2) != 0L);\n" +
                  "      case 30:\n" +
                  "         return ((jjbitVec25[i2] & l2) != 0L);\n" +
                  "      case 31:\n" +
                  "         return ((jjbitVec26[i2] & l2) != 0L);\n" +
                  "      case 32:\n" +
                  "         return ((jjbitVec27[i2] & l2) != 0L);\n" +
                  "      case 33:\n" +
                  "         return ((jjbitVec28[i2] & l2) != 0L);\n" +
                  "      case 48:\n" +
                  "         return ((jjbitVec29[i2] & l2) != 0L);\n" +
                  "      case 49:\n" +
                  "         return ((jjbitVec30[i2] & l2) != 0L);\n" +
                  "      case 77:\n" +
                  "         return ((jjbitVec31[i2] & l2) != 0L);\n" +
                  "      case 159:\n" +
                  "         return ((jjbitVec32[i2] & l2) != 0L);\n" +
                  "      case 164:\n" +
                  "         return ((jjbitVec33[i2] & l2) != 0L);\n" +
                  "      case 215:\n" +
                  "         return ((jjbitVec34[i2] & l2) != 0L);\n" +
                  "      case 250:\n" +
                  "         return ((jjbitVec35[i2] & l2) != 0L);\n" +
                  "      case 251:\n" +
                  "         return ((jjbitVec36[i2] & l2) != 0L);\n" +
                  "      case 253:\n" +
                  "         return ((jjbitVec37[i2] & l2) != 0L);\n" +
                  "      case 254:\n" +
                  "         return ((jjbitVec38[i2] & l2) != 0L);\n" +
                  "      case 255:\n" +
                  "         return ((jjbitVec39[i2] & l2) != 0L);\n" +
                  "      default :\n" +
                  "         if ((jjbitVec3[i1] & l1) != 0L)\n" +
                  "            return true;\n" +
                  "         return false;\n" +
                  "   }\n" +
                  "}\n" +
                  "private static final boolean jjCanMove_2(int hiByte, int i1, int i2, long l1, long l2)\n" +
                  "{\n" +
                  "   switch(hiByte)\n" +
                  "   {\n" +
                  "      case 0:\n" +
                  "         return ((jjbitVec40[i2] & l2) != 0L);\n" +
                  "      case 2:\n" +
                  "         return ((jjbitVec5[i2] & l2) != 0L);\n" +
                  "      case 3:\n" +
                  "         return ((jjbitVec41[i2] & l2) != 0L);\n" +
                  "      case 4:\n" +
                  "         return ((jjbitVec42[i2] & l2) != 0L);\n" +
                  "      case 5:\n" +
                  "         return ((jjbitVec43[i2] & l2) != 0L);\n" +
                  "      case 6:\n" +
                  "         return ((jjbitVec44[i2] & l2) != 0L);\n" +
                  "      case 7:\n" +
                  "         return ((jjbitVec45[i2] & l2) != 0L);\n" +
                  "      case 9:\n" +
                  "         return ((jjbitVec46[i2] & l2) != 0L);\n" +
                  "      case 10:\n" +
                  "         return ((jjbitVec47[i2] & l2) != 0L);\n" +
                  "      case 11:\n" +
                  "         return ((jjbitVec48[i2] & l2) != 0L);\n" +
                  "      case 12:\n" +
                  "         return ((jjbitVec49[i2] & l2) != 0L);\n" +
                  "      case 13:\n" +
                  "         return ((jjbitVec50[i2] & l2) != 0L);\n" +
                  "      case 14:\n" +
                  "         return ((jjbitVec51[i2] & l2) != 0L);\n" +
                  "      case 15:\n" +
                  "         return ((jjbitVec52[i2] & l2) != 0L);\n" +
                  "      case 16:\n" +
                  "         return ((jjbitVec53[i2] & l2) != 0L);\n" +
                  "      case 17:\n" +
                  "         return ((jjbitVec19[i2] & l2) != 0L);\n" +
                  "      case 18:\n" +
                  "         return ((jjbitVec20[i2] & l2) != 0L);\n" +
                  "      case 19:\n" +
                  "         return ((jjbitVec54[i2] & l2) != 0L);\n" +
                  "      case 20:\n" +
                  "         return ((jjbitVec0[i2] & l2) != 0L);\n" +
                  "      case 22:\n" +
                  "         return ((jjbitVec22[i2] & l2) != 0L);\n" +
                  "      case 23:\n" +
                  "         return ((jjbitVec55[i2] & l2) != 0L);\n" +
                  "      case 24:\n" +
                  "         return ((jjbitVec56[i2] & l2) != 0L);\n" +
                  "      case 30:\n" +
                  "         return ((jjbitVec25[i2] & l2) != 0L);\n" +
                  "      case 31:\n" +
                  "         return ((jjbitVec26[i2] & l2) != 0L);\n" +
                  "      case 32:\n" +
                  "         return ((jjbitVec57[i2] & l2) != 0L);\n" +
                  "      case 33:\n" +
                  "         return ((jjbitVec28[i2] & l2) != 0L);\n" +
                  "      case 48:\n" +
                  "         return ((jjbitVec58[i2] & l2) != 0L);\n" +
                  "      case 49:\n" +
                  "         return ((jjbitVec30[i2] & l2) != 0L);\n" +
                  "      case 77:\n" +
                  "         return ((jjbitVec31[i2] & l2) != 0L);\n" +
                  "      case 159:\n" +
                  "         return ((jjbitVec32[i2] & l2) != 0L);\n" +
                  "      case 164:\n" +
                  "         return ((jjbitVec33[i2] & l2) != 0L);\n" +
                  "      case 215:\n" +
                  "         return ((jjbitVec34[i2] & l2) != 0L);\n" +
                  "      case 250:\n" +
                  "         return ((jjbitVec35[i2] & l2) != 0L);\n" +
                  "      case 251:\n" +
                  "         return ((jjbitVec59[i2] & l2) != 0L);\n" +
                  "      case 253:\n" +
                  "         return ((jjbitVec37[i2] & l2) != 0L);\n" +
                  "      case 254:\n" +
                  "         return ((jjbitVec60[i2] & l2) != 0L);\n" +
                  "      case 255:\n" +
                  "         return ((jjbitVec61[i2] & l2) != 0L);\n" +
                  "      default :\n" +
                  "         if ((jjbitVec3[i1] & l1) != 0L)\n" +
                  "            return true;\n" +
                  "         return false;\n" +
                  "   }\n" +
                  "}",
                  cg.getGeneratedCode ().replaceAll ("\r", "").trim ());
  }

  /**
   * Test method for
   * {@link org.javacc.parser.NfaState#DumpMoveNfa(CodeGenerator)}.
   */
  public void testDumpMoveNfa ()
  {
    final CodeGenerator cg = new CodeGenerator ();
    try
    {
      NfaState.dumpMoveNfa (cg);
      fail ("Should have bombed");
    }
    catch (ArrayIndexOutOfBoundsException e)
    {
      e = null;
    }
    final String result = cg.getGeneratedCode ();
    assertEquals ("", result.trim ());

    if (false)
    {

      assertEquals ("static private final void jjCheckNAdd(int state)\n" +
                    "{\n" +
                    "   if (jjrounds[state] != jjround)\n" +
                    "   {\n" +
                    "      jjstateSet[jjnewStateCnt++] = state;\n" +
                    "      jjrounds[state] = jjround;\n" +
                    "   }\n" +
                    "}\n" +
                    "static private final void jjAddStates(int start, int end)\n" +
                    "{\n" +
                    "   do {\n" +
                    "      jjstateSet[jjnewStateCnt++] = jjnextStates[start];\n" +
                    "   } while (start++ != end);\n" +
                    "}\n" +
                    "static private final void jjCheckNAddTwoStates(int state1, int state2)\n" +
                    "{\n" +
                    "   jjCheckNAdd(state1);\n" +
                    "   jjCheckNAdd(state2);\n" +
                    "}\n" +
                    "static private final void jjCheckNAddStates(int start, int end)\n" +
                    "{\n" +
                    "   do {\n" +
                    "      jjCheckNAdd(jjnextStates[start]);\n" +
                    "   } while (start++ != end);\n" +
                    "}\n" +
                    "static private final void jjCheckNAddStates(int start)\n" +
                    "{\n" +
                    "   jjCheckNAdd(jjnextStates[start]);\n" +
                    "   jjCheckNAdd(jjnextStates[start + 1]);\n" +
                    "}\n" +
                    "",
                    cg.getGeneratedCode ().trim ());
    }
  }

  /**
   * Test method for
   * {@link org.javacc.parser.NfaState#DumpMoveNfa(CodeGenerator)}.
   */
  public void testDumpMoveNfaInitialised ()
  {
    final CodeGenerator cg = new CodeGenerator ();
    setupState ();
    NfaState.dumpMoveNfa (cg);
    assertEquals ("private int jjMoveNfa_3(int startState, int curPos)\n" + "{\n" + "   return curPos;\n" + "}",
                  cg.getGeneratedCode ().replaceAll ("\r", "").trim ());
  }

  /**
   * Test method for
   * {@link org.javacc.parser.NfaState#DumpStatesForState(CodeGenerator)}.
   */
  public void testDumpStatesForState ()
  {
    final CodeGenerator cg = new CodeGenerator ();
    NfaState.dumpStatesForStateJava (cg);
    final String result = cg.getGeneratedCode ().replaceAll ("\r", "");
    assertEquals ("protected static final int[][][] statesForState = null;", result.trim ());
  }

  /**
   * Test method for
   * {@link org.javacc.parser.NfaState#DumpStatesForState(CodeGenerator)}.
   */
  public void testDumpStatesForStateInitialised ()
  {
    final CodeGenerator cg = new CodeGenerator ();
    setupState ();
    NfaState.dumpStatesForStateJava (cg);
    final String result = cg.getGeneratedCode ().replaceAll ("\r", "");
    assertEquals ("protected static final int[][][] statesForState = {\n" +
                  " {\n" +
                  "   { 0 },\n" +
                  "   { 1 },\n" +
                  "   { 2 },\n" +
                  "   { 3, 4, 7, 13, 22, 31, 33, 48, },\n" +
                  "   { 3, 4, 7, 13, 22, 31, 33, 48, },\n" +
                  "   { 5 },\n" +
                  "   { 6 },\n" +
                  "   { 3, 4, 7, 13, 22, 31, 33, 48, },\n" +
                  "   { 8 },\n" +
                  "   { 9 },\n" +
                  "   { 10 },\n" +
                  "   { 11 },\n" +
                  "   { 12 },\n" +
                  "   { 3, 4, 7, 13, 22, 31, 33, 48, },\n" +
                  "   { 14 },\n" +
                  "   { 15 },\n" +
                  "   { 16 },\n" +
                  "   { 17 },\n" +
                  "   { 18 },\n" +
                  "   { 19 },\n" +
                  "   { 20 },\n" +
                  "   { 21 },\n" +
                  "   { 3, 4, 7, 13, 22, 31, 33, 48, },\n" +
                  "   { 23 },\n" +
                  "   { 24 },\n" +
                  "   { 25 },\n" +
                  "   { 26 },\n" +
                  "   { 27 },\n" +
                  "   { 28 },\n" +
                  "   { 29 },\n" +
                  "   { 30 },\n" +
                  "   { 3, 4, 7, 13, 22, 31, 33, 48, },\n" +
                  "   { 32 },\n" +
                  "   { 3, 4, 7, 13, 22, 31, 33, 48, },\n" +
                  "   { 34 },\n" +
                  "   { 35 },\n" +
                  "   { 36 },\n" +
                  "   { 37 },\n" +
                  "   { 38 },\n" +
                  "   { 39 },\n" +
                  "   { 40 },\n" +
                  "   { 41 },\n" +
                  "   { 42 },\n" +
                  "   { 43 },\n" +
                  "   { 44 },\n" +
                  "   { 45 },\n" +
                  "   { 46 },\n" +
                  "   { 47 },\n" +
                  "   { 3, 4, 7, 13, 22, 31, 33, 48, },\n" +
                  "   { 49 },\n" +
                  "   { 50 },\n" +
                  "   { 51 },\n" +
                  "   { 52 },\n" +
                  "   { 53 },\n" +
                  "   { 54 },\n" +
                  "   { 55 },\n" +
                  "   { 56 },\n" +
                  "   { 57 },\n" +
                  "   { 58 },\n" +
                  "   { 59 },\n" +
                  "   { 60 },\n" +
                  "   { 61 },\n" +
                  "   { 62 },\n" +
                  "   { 63 },\n" +
                  "   { 64 },\n" +
                  "},\n" +
                  " {},\n" +
                  " {\n" +
                  "   { 0, 2, },\n" +
                  "   { 1 },\n" +
                  "   { 0, 2, },\n" +
                  "},\n" +
                  " {},\n" +
                  " {},\n" +
                  "\n" +
                  "};",
                  result.trim ());
  }

  /**
   * Test method for
   * {@link org.javacc.parser.NfaState#DumpStatesForKind(CodeGenerator)}.
   */
  public void testDumpStatesForKind ()
  {
    final CodeGenerator cg = new CodeGenerator ();
    NfaState.dumpStatesForKind (cg);
    final String result = cg.getGeneratedCode ().replaceAll ("\r", "");
    assertEquals ("protected static final int[][][] statesForState = null;\n" +
                  "protected static final int[][] kindForState = null;",
                  result.trim ());
  }

  /**
   * Test method for
   * {@link org.javacc.parser.NfaState#DumpStatesForKind(CodeGenerator)}.
   */
  public void testDumpStatesForKindInitialised ()
  {
    final CodeGenerator cg = new CodeGenerator ();
    setupState ();
    NfaState.dumpStatesForKind (cg);
    final String result = cg.getGeneratedCode ().replaceAll ("\r", "");
    assertEquals ("protected static final int[][][] statesForState = {\n" +
                  " {\n" +
                  "   { 0 },\n" +
                  "   { 1 },\n" +
                  "   { 2 },\n" +
                  "   { 3, 4, 7, 13, 22, 31, 33, 48, },\n" +
                  "   { 3, 4, 7, 13, 22, 31, 33, 48, },\n" +
                  "   { 5 },\n" +
                  "   { 6 },\n" +
                  "   { 3, 4, 7, 13, 22, 31, 33, 48, },\n" +
                  "   { 8 },\n" +
                  "   { 9 },\n" +
                  "   { 10 },\n" +
                  "   { 11 },\n" +
                  "   { 12 },\n" +
                  "   { 3, 4, 7, 13, 22, 31, 33, 48, },\n" +
                  "   { 14 },\n" +
                  "   { 15 },\n" +
                  "   { 16 },\n" +
                  "   { 17 },\n" +
                  "   { 18 },\n" +
                  "   { 19 },\n" +
                  "   { 20 },\n" +
                  "   { 21 },\n" +
                  "   { 3, 4, 7, 13, 22, 31, 33, 48, },\n" +
                  "   { 23 },\n" +
                  "   { 24 },\n" +
                  "   { 25 },\n" +
                  "   { 26 },\n" +
                  "   { 27 },\n" +
                  "   { 28 },\n" +
                  "   { 29 },\n" +
                  "   { 30 },\n" +
                  "   { 3, 4, 7, 13, 22, 31, 33, 48, },\n" +
                  "   { 32 },\n" +
                  "   { 3, 4, 7, 13, 22, 31, 33, 48, },\n" +
                  "   { 34 },\n" +
                  "   { 35 },\n" +
                  "   { 36 },\n" +
                  "   { 37 },\n" +
                  "   { 38 },\n" +
                  "   { 39 },\n" +
                  "   { 40 },\n" +
                  "   { 41 },\n" +
                  "   { 42 },\n" +
                  "   { 43 },\n" +
                  "   { 44 },\n" +
                  "   { 45 },\n" +
                  "   { 46 },\n" +
                  "   { 47 },\n" +
                  "   { 3, 4, 7, 13, 22, 31, 33, 48, },\n" +
                  "   { 49 },\n" +
                  "   { 50 },\n" +
                  "   { 51 },\n" +
                  "   { 52 },\n" +
                  "   { 53 },\n" +
                  "   { 54 },\n" +
                  "   { 55 },\n" +
                  "   { 56 },\n" +
                  "   { 57 },\n" +
                  "   { 58 },\n" +
                  "   { 59 },\n" +
                  "   { 60 },\n" +
                  "   { 61 },\n" +
                  "   { 62 },\n" +
                  "   { 63 },\n" +
                  "   { 64 },\n" +
                  "},\n" +
                  " {},\n" +
                  " {\n" +
                  "   { 0, 2, },\n" +
                  "   { 1 },\n" +
                  "   { 0, 2, },\n" +
                  "},\n" +
                  " {},\n" +
                  " {},\n" +
                  "\n" +
                  "};\n" +
                  "protected static final int[][] kindForState = {\n" +
                  "{ \n" +
                  "  27, \n" +
                  "  27, \n" +
                  "  27, \n" +
                  "  27, \n" +
                  "  89, \n" +
                  "  89, \n" +
                  "  89, \n" +
                  "  93, \n" +
                  "  93, \n" +
                  "  93, \n" +
                  "  93, \n" +
                  "  93, \n" +
                  "  93, \n" +
                  "  98, \n" +
                  "  98, \n" +
                  "  98, \n" +
                  "  98, \n" +
                  "  98, \n" +
                  "  98, \n" +
                  "  98, \n" +
                  "  98, \n" +
                  "  98, \n" +
                  "  99, \n" +
                  "  99, \n" +
                  "  99, \n" +
                  "  99, \n" +
                  "  99, \n" +
                  "  99, \n" +
                  "  99, \n" +
                  "  99, \n" +
                  "  99, \n" +
                  "  150, \n" +
                  "  150, \n" +
                  "  93, \n" +
                  "  93, \n" +
                  "  93, \n" +
                  "  93, \n" +
                  "  93, \n" +
                  "  93, \n" +
                  "  93, \n" +
                  "  93, \n" +
                  "  93, \n" +
                  "  93, \n" +
                  "  93, \n" +
                  "  93, \n" +
                  "  93, \n" +
                  "  93, \n" +
                  "  93, \n" +
                  "  93, \n" +
                  "  89, \n" +
                  "  89, \n" +
                  "  89, \n" +
                  "  93, \n" +
                  "  93, \n" +
                  "  93, \n" +
                  "  93, \n" +
                  "  93, \n" +
                  "  93, \n" +
                  "  93, \n" +
                  "  93, \n" +
                  "  93, \n" +
                  "  93, \n" +
                  "  93, \n" +
                  "  93, \n" +
                  "  93, },\n" +
                  "{}\n" +
                  ",\n" +
                  "{ \n" +
                  "  30, \n" +
                  "  30, \n" +
                  "  30, },\n" +
                  "{}\n" +
                  ",\n" +
                  "{}\n\n" +
                  "};",
                  result.trim ());
  }

  /**
   * Test method for {@link org.javacc.parser.NfaState#reInit()}.
   */
  public void testReInit1 ()
  {}

}
