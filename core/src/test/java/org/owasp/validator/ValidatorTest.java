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

import org.joda.time.DateTime;
import org.junit.Test;
import org.owasp.validator.base.*;

import java.util.Calendar;
import java.util.Date;
import java.util.GregorianCalendar;

import static org.junit.Assert.fail;

/**
 * Created by steven on 17/09/15.
 */
public class ValidatorTest {

    @Test
    public void testBaseFalseValidator() throws ValidationException {
        Validator validator = new FalseValidator();
        validator.validate(Boolean.FALSE);
        validator.validate(false);
        try {
            validator.validate(Boolean.TRUE);
            fail("Exception should be thrown");
        } catch (ValidationException ve) {
        }
        try {
            validator.validate(true);
            fail("Exception should be thrown");
        } catch (ValidationException ve) {
        }
    }

    @Test
    public void testBaseTrueValidator() throws ValidationException {
        Validator validator = new TrueValidator();
        validator.validate(Boolean.TRUE);
        validator.validate(true);
        try {
            validator.validate(Boolean.FALSE);
            fail("Exception should be thrown");
        } catch (ValidationException ve) {
        }
        try {
            validator.validate(false);
            fail("Exception should be thrown");
        } catch (ValidationException ve) {
        }
    }

    @Test
    public void testBaseNullValidator() throws ValidationException {
        Validator validator = new NullValidator();
        validator.validate(null);
        try {
            validator.validate("");
            fail("Exception should be thrown");
        } catch (ValidationException ve) {
        }
    }

    @Test
    public void testBaseNotNullValidator() throws ValidationException {
        Validator validator = new NotNullValidator();
        validator.validate("");
        validator.validate(new Object());
        try {
            validator.validate(null);
            fail("Exception should be thrown");
        } catch (ValidationException ve) {
        }
    }

    @Test
    public void testBasePastValidator() throws ValidationException {
        Calendar pastCalendar = new GregorianCalendar(1015, 0, 1);
        Date pastDate = pastCalendar.getTime();
        DateTime pastDateTime = new DateTime(pastDate);
        Calendar futureCalendar = new GregorianCalendar(3015, 0, 1);
        Date futureDate = futureCalendar.getTime();
        DateTime futureDateTime = new DateTime(futureDate);

        Validator validator = new PastValidator();
        validator.validate(pastCalendar);
        validator.validate(pastDate);
        validator.validate(pastDateTime);

        try {
            validator.validate(futureCalendar);
            fail("Exception should be thrown");
        } catch (ValidationException ve) {
        }

        try {
            validator.validate(futureDate);
            fail("Exception should be thrown");
        } catch (ValidationException ve) {
        }

        try {
            validator.validate(futureDateTime);
            fail("Exception should be thrown");
        } catch (ValidationException ve) {
        }

        try {
            validator.validate(null);
            fail("Exception should be thrown");
        } catch (ValidationException ve) {
        }
    }

    @Test
    public void testBaseFutureValidator() throws ValidationException {
        Calendar pastCalendar = new GregorianCalendar(1015, 0, 1);
        Date pastDate = pastCalendar.getTime();
        DateTime pastDateTime = new DateTime(pastDate);
        Calendar futureCalendar = new GregorianCalendar(3015, 0, 1);
        Date futureDate = futureCalendar.getTime();
        DateTime futureDateTime = new DateTime(futureDate);

        Validator validator = new FutureValidator();
        validator.validate(futureCalendar);
        validator.validate(futureDate);
        validator.validate(futureDateTime);

        try {
            validator.validate(pastCalendar);
            fail("Exception should be thrown");
        } catch (ValidationException ve) {
        }

        try {
            validator.validate(pastDate);
            fail("Exception should be thrown");
        } catch (ValidationException ve) {
        }

        try {
            validator.validate(pastDateTime);
            fail("Exception should be thrown");
        } catch (ValidationException ve) {
        }

        try {
            validator.validate(null);
            fail("Exception should be thrown");
        } catch (ValidationException ve) {
        }

    }

    @Test
    public void testNumber() throws ValidationException {
        NumberValidator validator = new NumberValidator();
        short s = 1;
        validator.validate(s);
        validator.validate(1.1);
        validator.validate(0x7ffffffffL);
    }

    @Test(expected = ValidationException.class)
    public void testInvalidCreditCard() throws ValidationException {
        System.out.println("test CC");
        Validators.CREDIT_CARD_VALIDATOR.validate("");
        fail("should not be here");
    }

    @Test
    public void testValidCreditCards() {
        System.out.println("test CC2");
        try {
            Validators.CREDIT_CARD_VALIDATOR.validate("4012888888881881"); //Visa
            Validators.CREDIT_CARD_VALIDATOR.validate("5404000000000001"); //Mastercard
            Validators.CREDIT_CARD_VALIDATOR.validate("4462 0000 0000 0003"); //Delta
        } catch (ValidationException e) {
            fail("should not fail");
        }
    }
}
