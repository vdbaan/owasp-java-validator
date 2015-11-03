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

package org.owasp.validator.base;

import org.owasp.validator.UnsupportedContextException;
import org.owasp.validator.ValidationException;
import org.owasp.validator.Validator;

import java.math.BigDecimal;
import java.math.BigInteger;

/**
 * Implementations of min validator against Byte, Double, Float, Integer, Long, Short, BigInteger and BigDecimal.
 *
 * @author steven
 */
public class MinValidator extends Validator<Number> {

    private Number _min;
    private boolean _inclusive = false;

    /**
     * @param min       the minimal value to compare against
     * @param inclusive if the test inclusive of the minimal value
     */
    MinValidator(Number min, boolean inclusive) {
        _min = min;
        _inclusive = inclusive;
    }

    /**
     * does a byte compare
     * @param number
     * @throws ValidationException
     */
    protected void validate(Byte number) throws ValidationException {
        if (number.compareTo((Byte) _min) == -1) throw new ValidationException("value lower than min");
        if (!_inclusive && number.compareTo((Byte) _min) == 0) throw new ValidationException("value lower than min");
    }

    /**
     * does a double compare
     * @param number
     * @throws ValidationException
     */
    public void validate(Double number) throws ValidationException {
        if (number.compareTo((Double) _min) == -1) throw new ValidationException("value lower than min");
        if (!_inclusive && number.compareTo((Double) _min) == 0) throw new ValidationException("value lower than min");
    }

    /**
     * does a float compare
     * @param number
     * @throws ValidationException
     */
    public void validate(Float number) throws ValidationException {
        if (number.compareTo((Float) _min) == -1) throw new ValidationException("value lower than min");
        if (!_inclusive && number.compareTo((Float) _min) == 0) throw new ValidationException("value lower than min");
    }

    /**
     * does an integer compare
     * @param number
     * @throws ValidationException
     */
    public void validate(Integer number) throws ValidationException {
        if (number.compareTo((Integer) _min) == -1) throw new ValidationException("value lower than min");
        if (!_inclusive && number.compareTo((Integer) _min) == 0) throw new ValidationException("value lower than min");
    }

    /**
     * does a long compare
     * @param number
     * @throws ValidationException
     */
    public void validate(Long number) throws ValidationException {
        if (number.compareTo((Long) _min) == -1) throw new ValidationException("value lower than min");
        if (!_inclusive && number.compareTo((Long) _min) == 0) throw new ValidationException("value lower than min");
    }

    /**
     * does a short compare
     * @param number
     * @throws ValidationException
     */
    public void validate(Short number) throws ValidationException {
        if (number.compareTo((Short) _min) == -1) throw new ValidationException("value lower than min");
        if (!_inclusive && number.compareTo((Short) _min) == 0) throw new ValidationException("value lower than min");
    }

    /**
     * does a BigDecimal compare
     * @param number
     * @throws ValidationException
     */
    public void validate(BigDecimal number) throws ValidationException {
        if (number.compareTo((BigDecimal) _min) == -1)
            throw new ValidationException("value lower than min");
        if (!_inclusive && number.compareTo((BigDecimal) _min) == 0)
            throw new ValidationException("value lower than min");
    }

    /**
     * does a BigInteger compare
     * @param number
     * @throws ValidationException
     */
    public void validate(BigInteger number) throws ValidationException {
        if (number.compareTo((BigInteger) _min) == -1)
            throw new ValidationException("value lower than min");
        if (!_inclusive && number.compareTo((BigInteger) _min) == 0)
            throw new ValidationException("value lower than min");
    }

    @Override
    public void validate(Number value) throws ValidationException {
        throw new UnsupportedContextException("Can't compare, unknown type ");
    }
}
