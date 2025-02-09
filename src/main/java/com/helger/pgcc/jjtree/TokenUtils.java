/*
 * Copyright 2017-2023 Philip Helger, pgcc@helger.com
 *
 * Copyright 2011 Google Inc. All Rights Reserved.
 * Author: sreeni@google.com (Sreeni Viswanadha)
 *
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
 */
package com.helger.pgcc.jjtree;

import javax.annotation.Nonnull;

import com.helger.pgcc.parser.JavaCCErrors;

/**
 * Utilities for manipulating Tokens.
 */
public final class TokenUtils
{
  private TokenUtils ()
  {}

  static void print (final Token t, final JJTreeIO io, final String in, final String out)
  {
    Token tt = t.specialToken;
    if (tt != null)
    {
      while (tt.specialToken != null)
        tt = tt.specialToken;
      while (tt != null)
      {
        io.print (addUnicodeEscapes (tt.image));
        tt = tt.next;
      }
    }
    String i = t.image;
    if (in != null && i.equals (in))
    {
      i = out;
    }
    io.print (addUnicodeEscapes (i));
  }

  static void print (final Token t, final JJTreeIO io)
  {
    print (t, io, null, null);
  }

  static String addUnicodeEscapes (final String str)
  {
    final StringBuilder ret = new StringBuilder (str.length ());
    for (final char ch : str.toCharArray ())
    {
      if ((ch < 0x20 || ch > 0x7e) && ch != '\t' && ch != '\n' && ch != '\r' && ch != '\f')
      {
        final String s = "0000" + Integer.toString (ch, 16);
        ret.append ("\\u").append (s.substring (s.length () - 4, s.length ()));
      }
      else
      {
        ret.append (ch);
      }
    }
    return ret.toString ();
  }

  static boolean hasTokens (@Nonnull final JJTreeNode n)
  {
    if (n.getLastToken ().next == n.getFirstToken ())
      return false;
    return true;
  }

  static String remove_escapes_and_quotes (final Token t, final String str)
  {
    String retval = "";
    int index = 1;
    while (index < str.length () - 1)
    {
      if (str.charAt (index) != '\\')
      {
        retval += str.charAt (index);
        index++;
        continue;
      }
      index++;
      char ch = str.charAt (index);
      if (ch == 'b')
      {
        retval += '\b';
        index++;
        continue;
      }
      if (ch == 't')
      {
        retval += '\t';
        index++;
        continue;
      }
      if (ch == 'n')
      {
        retval += '\n';
        index++;
        continue;
      }
      if (ch == 'f')
      {
        retval += '\f';
        index++;
        continue;
      }
      if (ch == 'r')
      {
        retval += '\r';
        index++;
        continue;
      }
      if (ch == '"')
      {
        retval += '\"';
        index++;
        continue;
      }
      if (ch == '\'')
      {
        retval += '\'';
        index++;
        continue;
      }
      if (ch == '\\')
      {
        retval += '\\';
        index++;
        continue;
      }
      if (ch >= '0' && ch <= '7')
      {
        int ordinal = (ch) - ('0');
        index++;
        char ch1 = str.charAt (index);
        if (ch1 >= '0' && ch1 <= '7')
        {
          ordinal = ordinal * 8 + (ch1) - ('0');
          index++;
          ch1 = str.charAt (index);
          if (ch <= '3' && ch1 >= '0' && ch1 <= '7')
          {
            ordinal = ordinal * 8 + (ch1) - ('0');
            index++;
          }
        }
        retval += (char) ordinal;
        continue;
      }
      if (ch == 'u')
      {
        index++;
        ch = str.charAt (index);
        if (_isHexchar (ch))
        {
          int ordinal = _getHexVal (ch);
          index++;
          ch = str.charAt (index);
          if (_isHexchar (ch))
          {
            ordinal = ordinal * 16 + _getHexVal (ch);
            index++;
            ch = str.charAt (index);
            if (_isHexchar (ch))
            {
              ordinal = ordinal * 16 + _getHexVal (ch);
              index++;
              ch = str.charAt (index);
              if (_isHexchar (ch))
              {
                ordinal = ordinal * 16 + _getHexVal (ch);
                index++;
                continue;
              }
            }
          }
        }
        JavaCCErrors.parse_error (t,
                                  "Encountered non-hex character '" +
                                     ch +
                                     "' at position " +
                                     index +
                                     " of string - Unicode escape must have 4 hex digits after it.");
        return retval;
      }
      JavaCCErrors.parse_error (t, "Illegal escape sequence '\\" + ch + "' at position " + index + " of string.");
      return retval;
    }
    return retval;
  }

  private static boolean _isHexchar (final char ch)
  {
    if (ch >= '0' && ch <= '9')
      return true;
    if (ch >= 'A' && ch <= 'F')
      return true;
    if (ch >= 'a' && ch <= 'f')
      return true;
    return false;
  }

  private static int _getHexVal (final char ch)
  {
    if (ch >= '0' && ch <= '9')
      return ch - '0';
    if (ch >= 'A' && ch <= 'F')
      return ch - 'A' + 10;
    return ch - 'a' + 10;
  }
}
