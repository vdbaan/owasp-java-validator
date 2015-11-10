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

import java.math.BigDecimal;
import java.math.BigInteger;
import java.text.NumberFormat;
import java.text.ParseException;

/**
 * Created by steven on 17/09/15.
 */
public class NumberValidator extends Validator<Number> {


    public NumberValidator() {

    }


    @Override
    public void validate(Number number) throws ValidationException {
        if (number instanceof Byte) {
            validate((Byte) number, Byte.MIN_VALUE, Byte.MAX_VALUE, false);
        } else if (number instanceof Double) {
            validate((Double) number, Double.MIN_VALUE, Double.MAX_VALUE, false);
        } else if (number instanceof Float) {
            validate((Float) number, Float.MIN_VALUE, Float.MAX_VALUE, false);
        } else if (number instanceof Integer) {
            validate((Integer) number, Integer.MIN_VALUE, Integer.MAX_VALUE, false);
        } else if (number instanceof Long) {
            validate((Long) number, Long.MIN_VALUE, Long.MAX_VALUE, false);
        } else if (number instanceof Short) {
            validate((Short) number, Short.MIN_VALUE, Short.MAX_VALUE, false);
        }
    }

    public void validate(Number number, Number min, Number max, boolean inclusive) throws ValidationException {
        if (min == null && max == null) return;
        if (number instanceof Byte) {
            validate((Byte) number,min,max, inclusive);
        } else if (number instanceof Double) {
            validate((Double) number,min,max, inclusive);
        } else if (number instanceof Float) {
            validate((Float) number,min,max, inclusive);
        } else if (number instanceof Integer) {
            validate((Integer) number,min,max, inclusive);
        } else if (number instanceof Long) {
            validate((Long) number,min,max, inclusive);
        } else if (number instanceof Short) {
            validate((Short) number,min,max, inclusive);
        }
    }

    public void validate(String number, String min, String max, boolean inclusive) throws ValidationException {
        if(!number.matches("^[-+]?[0-9]*\\.?[0-9]+([eE][-+]?[0-9]+)?$"))
            throw new ValidationException("String is not a number");
        try {
            Number _min = null;
            Number _max = null;
            Number _num = NumberFormat.getInstance().parse(number);
            if (min != null) _min = NumberFormat.getInstance().parse(min);
            if (max != null) _max = NumberFormat.getInstance().parse(max);
            validate(_num, _min, _max, inclusive);
        } catch (ParseException e) {
            throw new ValidationException("String is not a number");
        }
    }

    public void validate(String number, Number min, Number max, boolean inclusive) throws ValidationException {
        if(!number.matches("^[-+]?[0-9]*\\.?[0-9]+([eE][-+]?[0-9]+)?$"))
            throw new ValidationException("String is not a number");
        try {
            Number _num = NumberFormat.getInstance().parse(number);
            validate(_num, min, max, inclusive);
        } catch (ParseException e) {
            throw new ValidationException("String is not a number");
        }
    }

    public void validate(Byte number, Number _min, Number _max, boolean inclusive) throws ValidationException {
        Byte min = (_min == null) ? Byte.MIN_VALUE : _min.byteValue();
        Byte max = (_max == null) ? Byte.MAX_VALUE : _max.byteValue();
        if (min.byteValue() > number) throw new ValidationException("value higher than max");
        if (inclusive&& min.byteValue() == number) throw new ValidationException("value higher than max");
        if (max.byteValue() < number) throw new ValidationException("value lower than max");
        if (inclusive&& max.byteValue() == number) throw new ValidationException("value lower than max");

    }


    public void validate(Double number, Number _min, Number _max, boolean inclusive) throws ValidationException {
        Double min = (_min == null) ? Double.MIN_VALUE : _min.doubleValue();
        Double max = (_max == null) ? Double.MAX_VALUE : _max.doubleValue();
        if (min.doubleValue() > number) throw new ValidationException("value higher than max");
        if (inclusive&& min.doubleValue() == number) throw new ValidationException("value higher than max");
        if (max.doubleValue() < number) throw new ValidationException("value lower than max");
        if (inclusive&& max.doubleValue() == number) throw new ValidationException("value lower than max");
    }


    public void validate(Float number, Number _min, Number _max, boolean inclusive) throws ValidationException {
        Float min = (_min == null) ? Float.MIN_VALUE : _min.floatValue();
        Float max = (_max == null) ? Float.MAX_VALUE : _max.floatValue();
        if (min.floatValue() > number) throw new ValidationException("value higher than max");
        if (inclusive&& min.floatValue() == number) throw new ValidationException("value higher than max");
        if (max.floatValue() < number) throw new ValidationException("value lower than max");
        if (inclusive&& max.floatValue() == number) throw new ValidationException("value lower than max");
    }


    public void validate(Integer number, Number _min, Number _max, boolean inclusive) throws ValidationException {
        Integer min = (_min == null) ? Integer.MIN_VALUE : _min.intValue();
        Integer max = (_max == null) ? Integer.MAX_VALUE : _max.intValue();
        if (min.intValue() > number) throw new ValidationException("value higher than max");
        if (inclusive&& min.intValue() == number) throw new ValidationException("value higher than max");
        if (max.intValue() < number) throw new ValidationException("value lower than max");
        if (inclusive&& max.intValue() == number) throw new ValidationException("value lower than max");
    }

    public void validate(Long number, Number _min, Number _max, boolean inclusive) throws ValidationException {
        Long min = (_min == null) ? Long.MIN_VALUE : _min.longValue();
        Long max = (_max == null) ? Long.MAX_VALUE : _max.longValue();
        if (min.longValue() > number) throw new ValidationException("value higher than max");
        if (inclusive&& min.longValue() == number) throw new ValidationException("value higher than max");
        if (max.longValue() < number) throw new ValidationException("value lower than max");
        if (inclusive&& max.longValue() == number) throw new ValidationException("value lower than max");
    }


    public void validate(Short number, Number _min, Number _max, boolean inclusive) throws ValidationException {
        Short min = (_min == null) ? Short.MIN_VALUE : _min.shortValue();
        Short max = (_min == null) ? Short.MAX_VALUE : _min.shortValue();
        if (min.shortValue() > number) throw new ValidationException("value higher than max");
        if (inclusive&& min.shortValue() == number) throw new ValidationException("value higher than max");
        if (max.shortValue() < number) throw new ValidationException("value lower than max");
        if (inclusive&& max.shortValue() == number) throw new ValidationException("value lower than max");
    }

    public void validate(BigDecimal number) throws ValidationException {
//        validate(number,
//                (_minValue == null) ? Byte.MIN_VALUE : _minValue,
//                (_minValue == null) ? Byte.MAX_VALUE : _maxValue
//        );
    }

//    void validate(BigDecimal number, Number _min, Number _max) throws ValidationException {
//        System.out.println("validating bd " + number.getClass().getCanonicalName());
//    }

    public void validate(BigInteger number) throws ValidationException {
//        validate(number,
//                (_minValue == null) ? Byte.MIN_VALUE : _minValue,
//                (_minValue == null) ? Byte.MAX_VALUE : _maxValue
//        );
    }

//    void validate(BigInteger number, Number _min, Number _max) throws ValidationException {
//        System.out.println("validating bi " + number.getClass().getCanonicalName());
//    }
}
