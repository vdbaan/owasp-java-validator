/*
 * Copyright (c) 2015.  Steven van der Baan
 * All rights reserved.
 *
 * Redistribution and use in source and binary forms, with or without
 * modification, are permitted provided that the following conditions
 * are met:
 *
 *     * Redistributions of source code must retain the above
 *       copyright notice, this list of conditions and the following
 *       disclaimer.
 *
 *     * Redistributions in binary form must reproduce the above
 *       copyright notice, this list of conditions and the following
 *       disclaimer in the documentation and/or other materials
 *       provided with the distribution.
 *
 *     * Neither the name of the OWASP nor the names of its
 *       contributors may be used to endorse or promote products
 *       derived from this software without specific prior written
 *       permission.
 *
 * THIS SOFTWARE IS PROVIDED BY THE COPYRIGHT HOLDERS AND CONTRIBUTORS
 * "AS IS" AND ANY EXPRESS OR IMPLIED WARRANTIES, INCLUDING, BUT NOT
 * LIMITED TO, THE IMPLIED WARRANTIES OF MERCHANTABILITY AND FITNESS
 * FOR A PARTICULAR PURPOSE ARE DISCLAIMED. IN NO EVENT SHALL THE
 * COPYRIGHT HOLDER OR CONTRIBUTORS BE LIABLE FOR ANY DIRECT,
 * INDIRECT, INCIDENTAL, SPECIAL, EXEMPLARY, OR CONSEQUENTIAL DAMAGES
 * (INCLUDING, BUT NOT LIMITED TO, PROCUREMENT OF SUBSTITUTE GOODS OR
 * SERVICES; LOSS OF USE, DATA, OR PROFITS; OR BUSINESS INTERRUPTION)
 * HOWEVER CAUSED AND ON ANY THEORY OF LIABILITY, WHETHER IN CONTRACT,
 * STRICT LIABILITY, OR TORT (INCLUDING NEGLIGENCE OR OTHERWISE)
 * ARISING IN ANY WAY OUT OF THE USE OF THIS SOFTWARE, EVEN IF ADVISED
 * OF THE POSSIBILITY OF SUCH DAMAGE.
 *
 */

package org.owasp.validator;

import org.owasp.validator.base.PatternValidator;
import org.owasp.validator.extension.LuhnValidator;

import java.util.HashMap;
import java.util.Map;

/**
 * Created by steven on 17/09/15.
 */
public final class Validators {
    private Validators() {
    }

    public static final String CREDITCARD = "creditcard";
    public static final String DATE = "date";

    /**
     * Map from validator name to validator singleton.
     */
    private static Map<String, Validator> VALIDATORS_MAP = new HashMap<String, Validator>(32);

    /**
     * Internal method to setup and map validator singletons.
     *
     * @param name      -- name of the validator (one of the constants above)
     * @param validator -- the validator singleton instance
     * @param <T>       the validator type
     * @return the validator argument.
     */
    private static <T extends Validator> T map(String name, T validator) {
        Validator old = VALIDATORS_MAP.put(name, validator);
        assert old == null;
        return validator;
    }

    static final ChainedValidators CREDIT_CARD_VALIDATOR = map(CREDITCARD,
            new ChainedValidators(new PatternValidator("^(?:4\\d{3}|5[1-5]\\d{2}|6011|3[47]\\d{2})([- ]?)\\d{4}\\1\\d{4}\\1\\d{4}$"), new LuhnValidator()));
    static final DateValidator DATE_VALIDATOR = map(DATE,new DateValidator());

    /**
     * Returns a new instance of an Validator for the specified context.
     * The returned instance is thread-safe.
     *
     * @param contextName the context name (one of the String constants defined in this class)
     * @return an validator for the specified context.
     * @throws NullPointerException        if {@code contextName} is null
     * @throws UnsupportedContextException if {@code contextName} is not
     *                                     recognized.
     */
    public static Validator forName(String contextName) throws NullPointerException,
            UnsupportedContextException {
        if (contextName == null) {
            throw new UnsupportedContextException("Context should not be null");
        }

        Validator encoder = VALIDATORS_MAP.get(contextName);

        if (encoder == null) {
            throw new UnsupportedContextException(contextName);
        }

        return encoder;
    }
}
