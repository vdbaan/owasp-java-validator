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

package org.owasp.validator.esapi;

import org.owasp.esapi.ValidationErrorList;
import org.owasp.esapi.ValidationRule;
import org.owasp.esapi.Validator;
import org.owasp.esapi.errors.IntrusionException;
import org.owasp.esapi.errors.ValidationException;
import org.owasp.esapi.reference.DefaultValidator;

import javax.servlet.http.HttpServletRequest;
import java.io.File;
import java.io.InputStream;
import java.text.DateFormat;
import java.util.Date;
import java.util.List;
import java.util.Set;

/**
 * Created by steven on 17/09/15.
 */
public final class ESAPIValidator {

    private ESAPIValidator() {
    }

    public static Validator getInstance() {
        return Impl.INSTANCE;
    }

    private enum Impl implements Validator {
        INSTANCE;
        private final Validator _referenceValidator = DefaultValidator.getInstance();

        public void addRule(ValidationRule validationRule) {
            _referenceValidator.addRule(validationRule);
        }

        public ValidationRule getRule(String s) {
            return _referenceValidator.getRule(s);
        }

        public boolean isValidInput(String s, String s1, String s2, int i, boolean b) throws IntrusionException {
            return _referenceValidator.isValidInput(s, s1, s2, i, b);
        }

        public boolean isValidInput(String s, String s1, String s2, int i, boolean b, ValidationErrorList validationErrorList) throws IntrusionException {
            return _referenceValidator.isValidInput(s, s1, s2, i, b, validationErrorList);
        }

        public boolean isValidInput(String s, String s1, String s2, int i, boolean b, boolean b1) throws IntrusionException {
            return _referenceValidator.isValidInput(s, s1, s2, i, b, b1);
        }

        public boolean isValidInput(String s, String s1, String s2, int i, boolean b, boolean b1, ValidationErrorList validationErrorList) throws IntrusionException {
            return _referenceValidator.isValidInput(s, s1, s2, i, b, b1, validationErrorList);
        }

        public String getValidInput(String s, String s1, String s2, int i, boolean b) throws ValidationException, IntrusionException {
            return _referenceValidator.getValidInput(s, s1, s2, i, b);
        }

        public String getValidInput(String s, String s1, String s2, int i, boolean b, boolean b1) throws ValidationException, IntrusionException {
            return _referenceValidator.getValidInput(s, s1, s2, i, b, b1);
        }

        public String getValidInput(String s, String s1, String s2, int i, boolean b, ValidationErrorList validationErrorList) throws IntrusionException {
            return _referenceValidator.getValidInput(s, s1, s2, i, b, validationErrorList);
        }

        public String getValidInput(String s, String s1, String s2, int i, boolean b, boolean b1, ValidationErrorList validationErrorList) throws IntrusionException {
            return _referenceValidator.getValidInput(s, s1, s2, i, b, b1, validationErrorList);
        }

        public boolean isValidDate(String s, String s1, DateFormat dateFormat, boolean b) throws IntrusionException {
            return _referenceValidator.isValidDate(s, s1, dateFormat, b);
        }

        public boolean isValidDate(String s, String s1, DateFormat dateFormat, boolean b, ValidationErrorList validationErrorList) throws IntrusionException {
            return _referenceValidator.isValidDate(s, s1, dateFormat, b, validationErrorList);
        }

        public Date getValidDate(String s, String s1, DateFormat dateFormat, boolean b) throws ValidationException, IntrusionException {
            return _referenceValidator.getValidDate(s, s1, dateFormat, b);
        }

        public Date getValidDate(String s, String s1, DateFormat dateFormat, boolean b, ValidationErrorList validationErrorList) throws IntrusionException {
            return _referenceValidator.getValidDate(s, s1, dateFormat, b, validationErrorList);
        }

        public boolean isValidSafeHTML(String s, String s1, int i, boolean b) throws IntrusionException {
            return _referenceValidator.isValidSafeHTML(s, s1, i, b);
        }

        public boolean isValidSafeHTML(String s, String s1, int i, boolean b, ValidationErrorList validationErrorList) throws IntrusionException {
            return _referenceValidator.isValidSafeHTML(s, s1, i, b, validationErrorList);
        }

        public String getValidSafeHTML(String s, String s1, int i, boolean b) throws ValidationException, IntrusionException {
            return _referenceValidator.getValidSafeHTML(s, s1, i, b);
        }

        public String getValidSafeHTML(String s, String s1, int i, boolean b, ValidationErrorList validationErrorList) throws IntrusionException {
            return _referenceValidator.getValidSafeHTML(s, s1, i, b, validationErrorList);
        }

        public boolean isValidCreditCard(String s, String s1, boolean b) throws IntrusionException {
            return _referenceValidator.isValidCreditCard(s, s1, b);
        }

        public boolean isValidCreditCard(String s, String s1, boolean b, ValidationErrorList validationErrorList) throws IntrusionException {
            return _referenceValidator.isValidCreditCard(s, s1, b, validationErrorList);
        }

        public String getValidCreditCard(String s, String s1, boolean b) throws ValidationException, IntrusionException {
            return _referenceValidator.getValidCreditCard(s, s1, b);
        }

        public String getValidCreditCard(String s, String s1, boolean b, ValidationErrorList validationErrorList) throws IntrusionException {
            return _referenceValidator.getValidCreditCard(s, s1, b, validationErrorList);
        }

        public boolean isValidDirectoryPath(String s, String s1, File file, boolean b) throws IntrusionException {
            return _referenceValidator.isValidDirectoryPath(s, s1, file, b);
        }

        public boolean isValidDirectoryPath(String s, String s1, File file, boolean b, ValidationErrorList validationErrorList) throws IntrusionException {
            return _referenceValidator.isValidDirectoryPath(s, s1, file, b, validationErrorList);
        }

        public String getValidDirectoryPath(String s, String s1, File file, boolean b) throws ValidationException, IntrusionException {
            return _referenceValidator.getValidDirectoryPath(s, s1, file, b);
        }

        public String getValidDirectoryPath(String s, String s1, File file, boolean b, ValidationErrorList validationErrorList) throws IntrusionException {
            return _referenceValidator.getValidDirectoryPath(s, s1, file, b, validationErrorList);
        }

        public boolean isValidFileName(String s, String s1, boolean b) throws IntrusionException {
            return _referenceValidator.isValidFileName(s, s1, b);
        }

        public boolean isValidFileName(String s, String s1, boolean b, ValidationErrorList validationErrorList) throws IntrusionException {
            return _referenceValidator.isValidFileName(s, s1, b, validationErrorList);
        }

        public boolean isValidFileName(String s, String s1, List<String> list, boolean b) throws IntrusionException {
            return _referenceValidator.isValidFileName(s, s1, list, b);
        }

        public boolean isValidFileName(String s, String s1, List<String> list, boolean b, ValidationErrorList validationErrorList) throws IntrusionException {
            return _referenceValidator.isValidFileName(s, s1, list, b, validationErrorList);
        }

        public String getValidFileName(String s, String s1, List<String> list, boolean b) throws ValidationException, IntrusionException {
            return _referenceValidator.getValidFileName(s, s1, list, b);
        }

        public String getValidFileName(String s, String s1, List<String> list, boolean b, ValidationErrorList validationErrorList) throws IntrusionException {
            return _referenceValidator.getValidFileName(s, s1, list, b, validationErrorList);
        }

        public boolean isValidNumber(String s, String s1, long l, long l1, boolean b) throws IntrusionException {
            return _referenceValidator.isValidNumber(s, s1, l, l1, b);
        }

        public boolean isValidNumber(String s, String s1, long l, long l1, boolean b, ValidationErrorList validationErrorList) throws IntrusionException {
            return _referenceValidator.isValidNumber(s, s1, l, l1, b, validationErrorList);
        }

        public Double getValidNumber(String s, String s1, long l, long l1, boolean b) throws ValidationException, IntrusionException {
            return _referenceValidator.getValidNumber(s, s1, l, l1, b);
        }

        public Double getValidNumber(String s, String s1, long l, long l1, boolean b, ValidationErrorList validationErrorList) throws IntrusionException {
            return _referenceValidator.getValidNumber(s, s1, l, l1, b, validationErrorList);
        }

        public boolean isValidInteger(String s, String s1, int i, int i1, boolean b) throws IntrusionException {
            return _referenceValidator.isValidInteger(s, s1, i, i1, b);
        }

        public boolean isValidInteger(String s, String s1, int i, int i1, boolean b, ValidationErrorList validationErrorList) throws IntrusionException {
            return _referenceValidator.isValidInteger(s, s1, i, i1, b, validationErrorList);
        }

        public Integer getValidInteger(String s, String s1, int i, int i1, boolean b) throws ValidationException, IntrusionException {
            return _referenceValidator.getValidInteger(s, s1, i, i1, b);
        }

        public Integer getValidInteger(String s, String s1, int i, int i1, boolean b, ValidationErrorList validationErrorList) throws IntrusionException {
            return _referenceValidator.getValidInteger(s, s1, i, i1, b, validationErrorList);
        }

        public boolean isValidDouble(String s, String s1, double v, double v1, boolean b) throws IntrusionException {
            return _referenceValidator.isValidDouble(s, s1, v, v1, b);
        }

        public boolean isValidDouble(String s, String s1, double v, double v1, boolean b, ValidationErrorList validationErrorList) throws IntrusionException {
            return _referenceValidator.isValidDouble(s, s1, v, v1, b, validationErrorList);
        }

        public Double getValidDouble(String s, String s1, double v, double v1, boolean b) throws ValidationException, IntrusionException {
            return _referenceValidator.getValidDouble(s, s1, v, v1, b);
        }

        public Double getValidDouble(String s, String s1, double v, double v1, boolean b, ValidationErrorList validationErrorList) throws IntrusionException {
            return _referenceValidator.getValidDouble(s, s1, v, v1, b, validationErrorList);
        }

        public boolean isValidFileContent(String s, byte[] bytes, int i, boolean b) throws IntrusionException {
            return _referenceValidator.isValidFileContent(s, bytes, i, b);
        }

        public boolean isValidFileContent(String s, byte[] bytes, int i, boolean b, ValidationErrorList validationErrorList) throws IntrusionException {
            return _referenceValidator.isValidFileContent(s, bytes, i, b, validationErrorList);
        }

        public byte[] getValidFileContent(String s, byte[] bytes, int i, boolean b) throws ValidationException, IntrusionException {
            return _referenceValidator.getValidFileContent(s, bytes, i, b);
        }

        public byte[] getValidFileContent(String s, byte[] bytes, int i, boolean b, ValidationErrorList validationErrorList) throws IntrusionException {
            return _referenceValidator.getValidFileContent(s, bytes, i, b, validationErrorList);
        }

        public boolean isValidFileUpload(String s, String s1, String s2, File file, byte[] bytes, int i, boolean b) throws IntrusionException {
            return _referenceValidator.isValidFileUpload(s, s1, s2, file, bytes, i, b);
        }

        public boolean isValidFileUpload(String s, String s1, String s2, File file, byte[] bytes, int i, boolean b, ValidationErrorList validationErrorList) throws IntrusionException {
            return _referenceValidator.isValidFileUpload(s, s1, s2, file, bytes, i, b, validationErrorList);
        }

        public void assertValidFileUpload(String s, String s1, String s2, File file, byte[] bytes, int i, List<String> list, boolean b) throws ValidationException, IntrusionException {
            _referenceValidator.assertValidFileUpload(s, s1, s2, file, bytes, i, list, b);
        }

        public void assertValidFileUpload(String s, String s1, String s2, File file, byte[] bytes, int i, List<String> list, boolean b, ValidationErrorList validationErrorList) throws IntrusionException {
            _referenceValidator.assertValidFileUpload(s, s1, s2, file, bytes, i, list, b, validationErrorList);
        }

        public boolean isValidListItem(String s, String s1, List<String> list) throws IntrusionException {
            return _referenceValidator.isValidListItem(s, s1, list);
        }

        public boolean isValidListItem(String s, String s1, List<String> list, ValidationErrorList validationErrorList) throws IntrusionException {
            return _referenceValidator.isValidListItem(s, s1, list, validationErrorList);
        }

        public String getValidListItem(String s, String s1, List<String> list) throws ValidationException, IntrusionException {
            return _referenceValidator.getValidListItem(s, s1, list);
        }

        public String getValidListItem(String s, String s1, List<String> list, ValidationErrorList validationErrorList) throws IntrusionException {
            return _referenceValidator.getValidListItem(s, s1, list, validationErrorList);
        }

        public boolean isValidHTTPRequestParameterSet(String s, HttpServletRequest httpServletRequest, Set<String> set, Set<String> set1) throws IntrusionException {
            return _referenceValidator.isValidHTTPRequestParameterSet(s, httpServletRequest, set, set1);
        }

        public boolean isValidHTTPRequestParameterSet(String s, HttpServletRequest httpServletRequest, Set<String> set, Set<String> set1, ValidationErrorList validationErrorList) throws IntrusionException {
            return _referenceValidator.isValidHTTPRequestParameterSet(s, httpServletRequest, set, set1, validationErrorList);
        }

        public void assertValidHTTPRequestParameterSet(String s, HttpServletRequest httpServletRequest, Set<String> set, Set<String> set1) throws ValidationException, IntrusionException {
            _referenceValidator.assertValidHTTPRequestParameterSet(s, httpServletRequest, set, set1);
        }

        public void assertValidHTTPRequestParameterSet(String s, HttpServletRequest httpServletRequest, Set<String> set, Set<String> set1, ValidationErrorList validationErrorList) throws IntrusionException {
            _referenceValidator.assertValidHTTPRequestParameterSet(s, httpServletRequest, set, set1, validationErrorList);
        }

        public boolean isValidPrintable(String s, char[] chars, int i, boolean b) throws IntrusionException {
            return _referenceValidator.isValidPrintable(s, chars, i, b);
        }

        public boolean isValidPrintable(String s, char[] chars, int i, boolean b, ValidationErrorList validationErrorList) throws IntrusionException {
            return _referenceValidator.isValidPrintable(s, chars, i, b, validationErrorList);
        }

        public char[] getValidPrintable(String s, char[] chars, int i, boolean b) throws ValidationException {
            return _referenceValidator.getValidPrintable(s, chars, i, b);
        }

        public char[] getValidPrintable(String s, char[] chars, int i, boolean b, ValidationErrorList validationErrorList) throws IntrusionException {
            return _referenceValidator.getValidPrintable(s, chars, i, b, validationErrorList);
        }

        public boolean isValidPrintable(String s, String s1, int i, boolean b) throws IntrusionException {
            return _referenceValidator.isValidPrintable(s, s1, i, b);
        }

        public boolean isValidPrintable(String s, String s1, int i, boolean b, ValidationErrorList validationErrorList) throws IntrusionException {
            return _referenceValidator.isValidPrintable(s, s1, i, b, validationErrorList);
        }

        public String getValidPrintable(String s, String s1, int i, boolean b) throws ValidationException {
            return _referenceValidator.getValidPrintable(s, s1, i, b);
        }

        public String getValidPrintable(String s, String s1, int i, boolean b, ValidationErrorList validationErrorList) throws IntrusionException {
            return _referenceValidator.getValidPrintable(s, s1, i, b, validationErrorList);
        }

        public boolean isValidRedirectLocation(String s, String s1, boolean b) {
            return _referenceValidator.isValidRedirectLocation(s, s1, b);
        }

        public boolean isValidRedirectLocation(String s, String s1, boolean b, ValidationErrorList validationErrorList) {
            return _referenceValidator.isValidRedirectLocation(s, s1, b, validationErrorList);
        }

        public String getValidRedirectLocation(String s, String s1, boolean b) throws ValidationException, IntrusionException {
            return _referenceValidator.getValidRedirectLocation(s, s1, b);
        }

        public String getValidRedirectLocation(String s, String s1, boolean b, ValidationErrorList validationErrorList) throws IntrusionException {
            return _referenceValidator.getValidRedirectLocation(s, s1, b, validationErrorList);
        }

        public String safeReadLine(InputStream inputStream, int i) throws ValidationException {
            return _referenceValidator.safeReadLine(inputStream, i);

        }

    }
}
