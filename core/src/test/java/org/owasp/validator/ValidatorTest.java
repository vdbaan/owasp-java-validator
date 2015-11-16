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
import org.owasp.validator.extension.EANValidator;
import org.owasp.validator.extension.EmailValidator;

import java.math.BigDecimal;
import java.math.BigInteger;
import java.util.*;

import static org.junit.Assert.fail;

/**
 * Created by steven on 17/09/15.
 */
public class ValidatorTest {

    @Test
    public void testValidator() {
        try {
            Validator validator = Validators.forName(null);
            fail("Exception should be thrown");
        } catch (Exception ve) {
        }
        try {
            Validator validator = Validators.forName("not here");
            fail("Exception should be thrown");
        } catch (UnsupportedContextException e) {
        } catch (Exception ve) {
            fail("Wrong exception");
        }
        try {
            Validator validator = Validators.forName(Validators.CREDITCARD);

        } catch (Exception ve) {
            fail("Exception should be thrown");
        }
    }

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
    public void testBaseNotEmptyValidator() throws ValidationException {
        Validator validator = new NotEmptyValidator();
        Set set = new HashSet();
        try {
            validator.validate(set);
            fail("Exception should be thrown");
        } catch (ValidationException ve) {
        }
        set.add(new Object());
        validator.validate(set);
        List list = new ArrayList();
        try {
            validator.validate(list);
            fail("Exception should be thrown");
        } catch (ValidationException ve) {
        }
        list.add(new Object());
        validator.validate(list);
    }

    @Test
    public void testBaseMaxValidator() throws ValidationException {
        Validator validator = new MaxValidator(10, false);
        short s = 1;
        int i = 1;
        long l = 1l;
        byte b = 1;
        double d = 1.0;
        float f = 1.0f;
        BigDecimal bd = new BigDecimal(1);
        BigInteger bi = new BigInteger("1");

        validator.validate(s);
        validator.validate(i);
        validator.validate(l);
        validator.validate(b);
        validator.validate(d);
        validator.validate(f);
        validator.validate(bd);
        validator.validate(bi);

        s = 10;
        i = 10;
        l = 10l;
        b = 10;
        d = 10.0;
        f = 10.0f;
        bd = new BigDecimal(10);
        bi = new BigInteger("10");
        try {
            validator.validate(s);
            fail("exception should be thrown");
        } catch (ValidationException ve) {
        }
        try {
            validator.validate(i);
            fail("exception should be thrown");
        } catch (ValidationException ve) {
        }
        try {
            validator.validate(l);
            fail("exception should be thrown");
        } catch (ValidationException ve) {
        }
        try {
            validator.validate(b);
            fail("exception should be thrown");
        } catch (ValidationException ve) {
        }
        try {
            validator.validate(d);
            fail("exception should be thrown");
        } catch (ValidationException ve) {
        }
        try {
            validator.validate(f);
            fail("exception should be thrown");
        } catch (ValidationException ve) {
        }
        try {
            validator.validate(bd);
            fail("exception should be thrown");
        } catch (ValidationException ve) {
        }
        try {
            validator.validate(bi);
            fail("exception should be thrown");
        } catch (ValidationException ve) {
        }

        validator = new MaxValidator(10, true);
        validator.validate(s);
        validator.validate(i);
        validator.validate(l);
        validator.validate(b);
        validator.validate(d);
        validator.validate(f);
        validator.validate(bd);
        validator.validate(bi);

        s = 100;
        i = 100;
        l = 100l;
        b = 100;
        d = 100.0;
        f = 100.0f;
        bd = new BigDecimal(100);
        bi = new BigInteger("100");
        try {
            validator.validate(s);
            fail("exception should be thrown");
        } catch (ValidationException ve) {
        }
        try {
            validator.validate(i);
            fail("exception should be thrown");
        } catch (ValidationException ve) {
        }
        try {
            validator.validate(l);
            fail("exception should be thrown");
        } catch (ValidationException ve) {
        }
        try {
            validator.validate(b);
            fail("exception should be thrown");
        } catch (ValidationException ve) {
        }
        try {
            validator.validate(d);
            fail("exception should be thrown");
        } catch (ValidationException ve) {
        }
        try {
            validator.validate(f);
            fail("exception should be thrown");
        } catch (ValidationException ve) {
        }
        try {
            validator.validate(bd);
            fail("exception should be thrown");
        } catch (ValidationException ve) {
        }
        try {
            validator.validate(bi);
            fail("exception should be thrown");
        } catch (ValidationException ve) {
        }
    }

    @Test
    public void testBaseMinValidator() throws ValidationException {
        Validator validator = new MinValidator(1, false);
        short s = 10;
        int i = 10;
        long l = 10l;
        byte b = 10;
        double d = 10.0;
        float f = 10.0f;
        BigDecimal bd = new BigDecimal(10);
        BigInteger bi = new BigInteger("10");

        validator.validate(s);
        validator.validate(i);
        validator.validate(l);
        validator.validate(b);
        validator.validate(d);
        validator.validate(f);
        validator.validate(bd);
        validator.validate(bi);

        s = 1;
        i = 1;
        l = 1l;
        b = 1;
        d = 1.0;
        f = 1.0f;
        bd = new BigDecimal(1);
        bi = new BigInteger("1");
        try {
            validator.validate(s);
            fail("exception should be thrown");
        } catch (ValidationException ve) {
        }
        try {
            validator.validate(i);
            fail("exception should be thrown");
        } catch (ValidationException ve) {
        }
        try {
            validator.validate(l);
            fail("exception should be thrown");
        } catch (ValidationException ve) {
        }
        try {
            validator.validate(b);
            fail("exception should be thrown");
        } catch (ValidationException ve) {
        }
        try {
            validator.validate(d);
            fail("exception should be thrown");
        } catch (ValidationException ve) {
        }
        try {
            validator.validate(f);
            fail("exception should be thrown");
        } catch (ValidationException ve) {
        }
        try {
            validator.validate(bd);
            fail("exception should be thrown");
        } catch (ValidationException ve) {
        }
        try {
            validator.validate(bi);
            fail("exception should be thrown");
        } catch (ValidationException ve) {
        }

        validator = new MinValidator(1, true);
        validator.validate(s);
        validator.validate(i);
        validator.validate(l);
        validator.validate(b);
        validator.validate(d);
        validator.validate(f);
        validator.validate(bd);
        validator.validate(bi);

        s = 0;
        i = 0;
        l = 0l;
        b = 0;
        d = 0.0;
        f = 0.0f;
        bd = new BigDecimal(0);
        bi = new BigInteger("0");
        try {
            validator.validate(s);
            fail("exception should be thrown");
        } catch (ValidationException ve) {
        }
        try {
            validator.validate(i);
            fail("exception should be thrown");
        } catch (ValidationException ve) {
        }
        try {
            validator.validate(l);
            fail("exception should be thrown");
        } catch (ValidationException ve) {
        }
        try {
            validator.validate(b);
            fail("exception should be thrown");
        } catch (ValidationException ve) {
        }
        try {
            validator.validate(d);
            fail("exception should be thrown");
        } catch (ValidationException ve) {
        }
        try {
            validator.validate(f);
            fail("exception should be thrown");
        } catch (ValidationException ve) {
        }
        try {
            validator.validate(bd);
            fail("exception should be thrown");
        } catch (ValidationException ve) {
        }
        try {
            validator.validate(bi);
            fail("exception should be thrown");
        } catch (ValidationException ve) {
        }
    }

    @Test
    public void testBaseSizeValidator() throws ValidationException {
        SizeValidator validator = new SizeValidator();
        List list = Collections.nCopies(10, "10");

        validator.validate(null);
        validator.validate(list);
        validator.setMin(2);
        validator.validate(list);
        validator.setMin(100);
        try {
            validator.validate(list);
        } catch (ValidationException ve) {
        }
        validator.setMin(2);
        validator.setMax(100);
        validator.validate(list);
        validator.setMax(5);
        try {
            validator.validate(list);
        } catch (ValidationException ve) {
        }
    }

    @Test
    public void testExceptionEmailValidator() throws ValidationException {
        Validator validator = new EmailValidator();
        String email = "here@example.com";
        validator.validate(email);
        email = "not.a.valid.email";

        try {
            validator.validate(email);
            fail("exception should be thrown");
        } catch (ValidationException ve) {
        }
        try {
            validator.validate(null);
            fail("exception should be thrown");
        } catch (ValidationException ve) {
        }
    }

    @Test
    public void testExtensionEANValidator() throws ValidationException {
        Validator validator = new EANValidator();
        String ean13g = "4006381333931";
        String ean8g = "73513537";
        String ean13f = "4006381333930";
        String ean8f = "73513536";

        validator.validate(ean13g);
        validator.validate(ean8g);

        try {
            validator.validate(ean13f);
            fail("exception should be thrown");
        } catch (ValidationException ve) {
        }
        try {
            validator.validate(ean8f);
            fail("exception should be thrown");
        } catch (ValidationException ve) {
        }
        try {
            validator.validate("");
            fail("exception should be thrown");
        } catch (ValidationException ve) {
        }
        try {
            validator.validate(null);
            fail("exception should be thrown");
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
