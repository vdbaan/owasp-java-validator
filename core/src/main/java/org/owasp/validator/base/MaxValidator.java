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

import org.owasp.validator.ValidationException;
import org.owasp.validator.Validator;

import java.math.BigDecimal;
import java.math.BigInteger;

/**
 * Implementations of max validator against Byte, Double, Float, Integer, Long, Short, BigInteger and BigDecimal.
 *
 * @author steven
 */
public class MaxValidator extends Validator<Number> {

    private Number _max = null;
    private boolean _inclusive = false;

    public MaxValidator() {
    }

    /**
     * @param max       the maximal value to compare against
     * @param inclusive if the test inclusive of the maximal value
     */
    public MaxValidator(Number max, boolean inclusive) {
        super();
        _max = max;
        _inclusive = inclusive;
    }

    @Override
    public void validate(Number number) throws ValidationException {
        if (number instanceof Byte) {
            validate((Byte) number);
        } else if (number instanceof Double) {
            validate((Double) number);
        } else if (number instanceof Float) {
            validate((Float) number);
        } else if (number instanceof Integer) {
            validate((Integer) number);
        } else if (number instanceof Long) {
            validate((Long) number);
        } else if (number instanceof Short) {
            validate((Short) number);
        } else if (number instanceof BigDecimal) {
            validate((BigDecimal) number);
        } else if (number instanceof BigInteger) {
            validate((BigInteger) number);
        }
    }

    /**
     * does a byte compare
     *
     * @param number the byte to validate
     * @throws ValidationException is thrown when the byte is higher than the set maximum
     */
    public void validate(Byte number) throws ValidationException {
        if (((_max == null) ? Byte.MAX_VALUE : _max.byteValue()) < number)
            throw new ValidationException("value higher than max");
        if (!_inclusive && _max.byteValue() == number) throw new ValidationException("value higher than max");
    }

    /**
     * does a double compare
     *
     * @param number the double to validate
     * @throws ValidationException is thrown when the double is higher than the set maximum
     */
    public void validate(Double number) throws ValidationException {
        if (((_max == null) ? Double.MAX_VALUE : _max.doubleValue()) < number)
            throw new ValidationException("value higher than max");
        if (!_inclusive && _max.doubleValue() == number) throw new ValidationException("value higher than max");
    }

    /**
     * does a float compare
     *
     * @param number the float to validate
     * @throws ValidationException is thrown when the float is higher than the set maximum
     */
    public void validate(Float number) throws ValidationException {
        if (((_max == null) ? Float.MAX_VALUE : _max.floatValue()) < number)
            throw new ValidationException("value higher than max");
        if (!_inclusive && _max.floatValue() == number) throw new ValidationException("value higher than max");
    }

    /**
     * does an integer compare
     *
     * @param number the integer to validate
     * @throws ValidationException is thrown when the integer is higher than the set maximum
     */
    public void validate(Integer number) throws ValidationException {
        if (((_max == null) ? Integer.MAX_VALUE : _max.intValue()) < number)
            throw new ValidationException("value higher than max");
        if (!_inclusive && _max.intValue() == number) throw new ValidationException("value higher than max");
    }

    /**
     * does a long compare
     *
     * @param number the long to validate
     * @throws ValidationException is thrown when the long is higher than the set maximum
     */
    public void validate(Long number) throws ValidationException {
        if (((_max == null) ? Long.MAX_VALUE : _max.longValue()) < number)
            throw new ValidationException("value higher than max");
        if (!_inclusive && _max.longValue() == number) throw new ValidationException("value higher than max");
    }

    /**
     * does a short compare
     *
     * @param number the short to validate
     * @throws ValidationException is thrown when the short is higher than the set maximum
     */
    public void validate(Short number) throws ValidationException {
        if (((_max == null) ? Short.MAX_VALUE : _max.shortValue()) < number)
            throw new ValidationException("value higher than max");
        if (!_inclusive && _max.shortValue() == number) throw new ValidationException("value higher than max");
    }

    /**
     * does a BigDecimal compare
     *
     * @param number the BigDecimal to validate
     * @throws ValidationException is thrown when the BigDecimal is higher than the set maximum
     */
    public void validate(BigDecimal number) throws ValidationException {
        if (_max == null) return;
        BigDecimal max = new BigDecimal(_max.toString());

        if (number.compareTo(max) == 1)
            throw new ValidationException("value higher than max");
        if (!_inclusive && number.compareTo(max) == 0)
            throw new ValidationException("value higher than max");
    }

    /**
     * does a BigInteger compare
     *
     * @param number the BigInteger to validate
     * @throws ValidationException is thrown when the BigInteger is higher than the set maximum
     */
    public void validate(BigInteger number) throws ValidationException {
        if (_max == null) return;
        BigInteger max = new BigInteger(_max.toString());
        if (number.compareTo(max) == 1)
            throw new ValidationException("value higher than max");
        if (!_inclusive && number.compareTo(max) == 0)
            throw new ValidationException("value higher than max");
    }

}
