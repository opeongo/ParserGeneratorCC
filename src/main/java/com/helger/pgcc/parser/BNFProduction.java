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
package com.helger.pgcc.parser;

import java.util.ArrayList;
import java.util.List;

import javax.annotation.Nonnull;

import com.helger.commons.annotation.ReturnsMutableObject;

/**
 * Describes BNF productions.
 */
public class BNFProduction extends NormalProduction
{
  /**
   * The declarations of this production.
   */
  private final List <Token> m_aDeclarationTokens = new ArrayList <> ();

  /**
   * This flag keeps track of whether or not return and throw statements have
   * been patched within this production's actions to include a preceding "if
   * (true)".
   */
  private boolean m_bJumpPatched = false;

  /**
   * @return the declaration_tokens
   */
  @Nonnull
  @ReturnsMutableObject
  public List <Token> getDeclarationTokens ()
  {
    return m_aDeclarationTokens;
  }

  /**
   * @param jumpPatched
   *        the jumpPatched to set
   */
  public void setJumpPatched (final boolean jumpPatched)
  {
    m_bJumpPatched = jumpPatched;
  }

  /**
   * @return the jumpPatched
   */
  public boolean isJumpPatched ()
  {
    return m_bJumpPatched;
  }
}
