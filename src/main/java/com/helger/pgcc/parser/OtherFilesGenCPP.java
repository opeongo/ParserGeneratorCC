/**
 * Copyright (c) 2006, Sun Microsystems, Inc.
 * All rights reserved.
 *
 * Redistribution and use in source and binary forms, with or without
 * modification, are permitted provided that the following conditions are met:
 *
 *     * Redistributions of source code must retain the above copyright notice,
 *       this list of conditions and the following disclaimer.
 *     * Redistributions in binary form must reproduce the above copyright
 *       notice, this list of conditions and the following disclaimer in the
 *       documentation and/or other materials provided with the distribution.
 *     * Neither the name of the Sun Microsystems, Inc. nor the names of its
 *       contributors may be used to endorse or promote products derived from
 *       this software without specific prior written permission.
 *
 * THIS SOFTWARE IS PROVIDED BY THE COPYRIGHT HOLDERS AND CONTRIBUTORS "AS IS"
 * AND ANY EXPRESS OR IMPLIED WARRANTIES, INCLUDING, BUT NOT LIMITED TO, THE
 * IMPLIED WARRANTIES OF MERCHANTABILITY AND FITNESS FOR A PARTICULAR PURPOSE
 * ARE DISCLAIMED. IN NO EVENT SHALL THE COPYRIGHT OWNER OR CONTRIBUTORS BE
 * LIABLE FOR ANY DIRECT, INDIRECT, INCIDENTAL, SPECIAL, EXEMPLARY, OR
 * CONSEQUENTIAL DAMAGES (INCLUDING, BUT NOT LIMITED TO, PROCUREMENT OF
 * SUBSTITUTE GOODS OR SERVICES; LOSS OF USE, DATA, OR PROFITS; OR BUSINESS
 * INTERRUPTION) HOWEVER CAUSED AND ON ANY THEORY OF LIABILITY, WHETHER IN
 * CONTRACT, STRICT LIABILITY, OR TORT (INCLUDING NEGLIGENCE OR OTHERWISE)
 * ARISING IN ANY WAY OUT OF THE USE OF THIS SOFTWARE, EVEN IF ADVISED OF
 * THE POSSIBILITY OF SUCH DAMAGE.
 *
 * Copyright 2011 Google Inc. All Rights Reserved.
 * Author: sreeni@google.com (Sreeni Viswanadha)
 *
 * Copyright 2017-2018 Philip Helger, pgcc@helger.com
 */
// Copyright 2012 Google Inc. All Rights Reserved.
// Author: sreeni@google.com (Sreeni Viswanadha)

/* Copyright (c) 2006, Sun Microsystems, Inc.
 * All rights reserved.
 *
 * Redistribution and use in source and binary forms, with or without
 * modification, are permitted provided that the following conditions are met:
 *
 *     * Redistributions of source code must retain the above copyright notice,
 *       this list of conditions and the following disclaimer.
 *     * Redistributions in binary form must reproduce the above copyright
 *       notice, this list of conditions and the following disclaimer in the
 *       documentation and/or other materials provided with the distribution.
 *     * Neither the name of the Sun Microsystems, Inc. nor the names of its
 *       contributors may be used to endorse or promote products derived from
 *       this software without specific prior written permission.
 *
 * THIS SOFTWARE IS PROVIDED BY THE COPYRIGHT HOLDERS AND CONTRIBUTORS "AS IS"
 * AND ANY EXPRESS OR IMPLIED WARRANTIES, INCLUDING, BUT NOT LIMITED TO, THE
 * IMPLIED WARRANTIES OF MERCHANTABILITY AND FITNESS FOR A PARTICULAR PURPOSE
 * ARE DISCLAIMED. IN NO EVENT SHALL THE COPYRIGHT OWNER OR CONTRIBUTORS BE
 * LIABLE FOR ANY DIRECT, INDIRECT, INCIDENTAL, SPECIAL, EXEMPLARY, OR
 * CONSEQUENTIAL DAMAGES (INCLUDING, BUT NOT LIMITED TO, PROCUREMENT OF
 * SUBSTITUTE GOODS OR SERVICES; LOSS OF USE, DATA, OR PROFITS; OR BUSINESS
 * INTERRUPTION) HOWEVER CAUSED AND ON ANY THEORY OF LIABILITY, WHETHER IN
 * CONTRACT, STRICT LIABILITY, OR TORT (INCLUDING NEGLIGENCE OR OTHERWISE)
 * ARISING IN ANY WAY OUT OF THE USE OF THIS SOFTWARE, EVEN IF ADVISED OF
 * THE POSSIBILITY OF SUCH DAMAGE.
 */
package com.helger.pgcc.parser;

import java.io.File;
import java.io.PrintWriter;
import java.io.Writer;
import java.util.ArrayList;
import java.util.List;

import com.helger.commons.io.file.FileHelper;
import com.helger.commons.string.StringHelper;

/**
 * Generates the Constants file.
 */
public class OtherFilesGenCPP extends JavaCCGlobals
{

  // Used by the CPP code generatror
  public static void printCharArray (final PrintWriter ostr, final String s)
  {
    ostr.print ("{");
    for (int i = 0; i < s.length (); i++)
    {
      ostr.print ("0x" + Integer.toHexString (s.charAt (i)) + ", ");
    }
    ostr.print ("0}");
  }

  static public void start () throws MetaParseException
  {
    if (JavaCCErrors.getErrorCount () != 0)
      throw new MetaParseException ();

    FilesCpp.gen_JavaCCDefs ();
    FilesCpp.gen_CharStream ();
    FilesCpp.gen_Token (); // TODO(theov): issued twice??
    FilesCpp.gen_TokenManager ();
    FilesCpp.gen_TokenMgrError ();
    FilesCpp.gen_ParseException ();
    FilesCpp.gen_ErrorHandler ();

    final Writer w = FileHelper.getBufferedWriter (new File (Options.getOutputDirectory (), s_cu_name + "Constants.h"),
                                                   Options.getOutputEncoding ());
    if (w == null)
    {
      JavaCCErrors.semantic_error ("Could not open file " + s_cu_name + "Constants.h for writing.");
      return;
    }

    try (PrintWriter s_ostr = new PrintWriter (w))
    {
      final List <String> tn = new ArrayList <> (s_toolNames);
      tn.add (s_toolName);
      s_ostr.println ("/* " + getIdString (tn, s_cu_name + "Constants.java") + " */");

      if (s_cu_to_insertion_point_1.size () != 0 &&
          s_cu_to_insertion_point_1.get (0).kind == JavaCCParserConstants.PACKAGE)
      {
        for (int i = 1; i < s_cu_to_insertion_point_1.size (); i++)
        {
          if (s_cu_to_insertion_point_1.get (i).kind == JavaCCParserConstants.SEMICOLON)
          {
            Token t = s_cu_to_insertion_point_1.get (0);
            printTokenSetup (t);
            for (int j = 0; j <= i; j++)
            {
              t = s_cu_to_insertion_point_1.get (j);
              printToken (t, s_ostr);
            }
            printTrailingComments (t);
            s_ostr.println ();
            s_ostr.println ();
            break;
          }
        }
      }
      s_ostr.println ("");
      s_ostr.println ("/**");
      s_ostr.println (" * Token literal values and constants.");
      s_ostr.println (" * Generated by " + OtherFilesGenCPP.class.getName () + "#start()");
      s_ostr.println (" */");

      final String define = (s_cu_name + "Constants_h").toUpperCase ();
      s_ostr.println ("#ifndef " + define);
      s_ostr.println ("#define " + define);
      s_ostr.println ("#include \"JavaCC.h\"");
      s_ostr.println ("");
      if (Options.stringValue (Options.USEROPTION__CPP_NAMESPACE).length () > 0)
      {
        s_ostr.println ("namespace " + Options.stringValue ("NAMESPACE_OPEN"));
      }

      RegularExpression re;
      final String constPrefix = "const";
      s_ostr.println ("  /** End of File. */");
      s_ostr.println (constPrefix + "  int _EOF = 0;");
      for (final java.util.Iterator <RegularExpression> it = s_ordered_named_tokens.iterator (); it.hasNext ();)
      {
        re = it.next ();
        s_ostr.println ("  /** RegularExpression Id. */");
        s_ostr.println (constPrefix + "  int " + re.m_label + " = " + re.m_ordinal + ";");
      }
      s_ostr.println ("");

      if (!Options.getUserTokenManager () && Options.getBuildTokenManager ())
      {
        for (int i = 0; i < LexGenJava.lexStateName.length; i++)
        {
          s_ostr.println ("  /** Lexical state. */");
          s_ostr.println (constPrefix + "  int " + LexGenJava.lexStateName[i] + " = " + i + ";");
        }
        s_ostr.println ("");
      }
      s_ostr.println ("  /** Literal token values. */");
      int cnt = 0;
      s_ostr.println ("  static const JJChar tokenImage_arr_" + cnt + "[] = ");
      printCharArray (s_ostr, "<EOF>");
      s_ostr.println (";");

      for (final TokenProduction tp : s_rexprlist)
      {
        final List <RegExprSpec> respecs = tp.respecs;
        for (final RegExprSpec res : respecs)
        {
          re = res.rexp;
          s_ostr.println ("  static const JJChar tokenImage_arr_" + ++cnt + "[] = ");
          if (re instanceof RStringLiteral)
          {
            printCharArray (s_ostr, "\"" + ((RStringLiteral) re).m_image + "\"");
          }
          else
            if (StringHelper.hasText (re.m_label))
            {
              printCharArray (s_ostr, "\"<" + re.m_label + ">\"");
            }
            else
            {
              if (re.tpContext.kind == TokenProduction.TOKEN)
              {
                JavaCCErrors.warning (re, "Consider giving this non-string token a label for better error reporting.");
              }
              printCharArray (s_ostr, "\"<token of kind " + re.m_ordinal + ">\"");
            }
          s_ostr.println (";");
        }
      }

      s_ostr.println ("  static const JJChar* const tokenImage[] = {");
      for (int i = 0; i <= cnt; i++)
      {
        s_ostr.println ("tokenImage_arr_" + i + ", ");
      }
      s_ostr.println ("  };");
      s_ostr.println ("");
      if (Options.stringValue (Options.USEROPTION__CPP_NAMESPACE).length () > 0)
      {
        s_ostr.println (Options.stringValue ("NAMESPACE_CLOSE"));
      }
      s_ostr.println ("#endif");
    }
  }

  public static void reInit ()
  {
    // empty
  }
}
