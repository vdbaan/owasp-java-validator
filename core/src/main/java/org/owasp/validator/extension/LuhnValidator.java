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

package org.owasp.validator.extension;

import org.owasp.validator.ValidationException;
import org.owasp.validator.Validator;

/**
 * @author steven
 */
public class LuhnValidator extends Validator<String> {


    /**
     * Performs additional validation on the card nummber.
     * This implementation performs Luhn algorithm checking
     *
     * @param ccNum number to be validated
     * @return true if the ccNum passes the Luhn Algorithm
     */
    public void validate(String ccNum) throws ValidationException {

        StringBuilder digitsOnly = new StringBuilder();
        char c;
        for (int i = 0; i < ccNum.length(); i++) {
            c = ccNum.charAt(i);
            if (Character.isDigit(c)) {
                digitsOnly.append(c);
            }
        }

        int sum = 0;
        int digit = 0;
        int addend = 0;
        boolean timesTwo = false;

        for (int i = digitsOnly.length() - 1; i >= 0; i--) {
            // guaranteed to be an int
            digit = Integer.valueOf(digitsOnly.substring(i, i + 1));
            if (timesTwo) {
                addend = digit * 2;
                if (addend > 9) {
                    addend -= 9;
                }
            } else {
                addend = digit;
            }
            sum += addend;
            timesTwo = !timesTwo;
        }

        if (sum % 10 != 0) throw new ValidationException("Creditcard doesn't adhere to luhn algorithm");

    }
}