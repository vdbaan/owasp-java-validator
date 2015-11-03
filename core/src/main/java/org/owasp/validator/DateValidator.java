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

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;

/**
 * Created by steven on 17/09/15.
 */
public class DateValidator extends Validator<Date> {

    private Date _minDate = null;
    private Date _maxDate = null;
    private SimpleDateFormat dateParser;

    DateValidator() {
        this(null, null);
    }

    DateValidator(String datePattern) {
        this(null, null, datePattern);
    }

    DateValidator(Date minDate, Date maxDate) {
        this(minDate, maxDate, "DD-MM-YYYY");
    }

    DateValidator(Date minDate, Date maxDate, String datePattern) {
        _minDate = minDate;
        _maxDate = maxDate;
        dateParser = new SimpleDateFormat(datePattern);

    }

    public void validate(String date) throws ValidationException {
        try {
            validate(dateParser.parse(date));
        } catch (ParseException e) {
            throw new ValidationException(date + " is not a date");
        }
    }

    @Override
    public void validate(Date value) throws ValidationException {
        if (_minDate != null && value.before(_minDate)) throw new ValidationException("Date before min range");
        if (_maxDate != null && value.after(_maxDate)) throw new ValidationException("Date after max range");
    }

    public void setDatePattern(String pattern) {
        dateParser = new SimpleDateFormat(pattern);
    }
}
