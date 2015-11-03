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

import org.junit.Test;

import java.io.IOException;

import static org.junit.Assert.fail;

/**
 * Created by steven on 03/11/15.
 */
public class ValidatorTest {

    @Test
    public void testGetValidCreditCard() throws ValidationException {
        System.out.println("getValidCreditCard");
        Validators.CREDIT_CARD_VALIDATOR.validate("4462 0000 0000 0003");
        Validators.CREDIT_CARD_VALIDATOR.validate("4012888888881881");
        try {
            Validators.CREDIT_CARD_VALIDATOR.validate("12349876000000081");
            fail("Exception should be thrown");
        } catch (ValidationException ve) {
        }
        try {
            Validators.CREDIT_CARD_VALIDATOR.validate("4417 1234 5678 9112");
            fail("Exception should be thrown");
        } catch (ValidationException ve) {
        }
    }

    @Test
    public void testGetValidDate() throws Exception {
        System.out.println("getValidDate");
        Validators.DATE_VALIDATOR.setDatePattern("MMM DD, YYYY");
        Validators.DATE_VALIDATOR.validate("June 23, 1967");
        try {
            Validators.DATE_VALIDATOR.validate("2015-12-2");
            fail("Exception should be thrown");
        } catch (ValidationException ve) {
        }
    }

    @Test
    public void testGetValidDirectoryPath() throws Exception {

    }

    @Test
    public void testGetValidDouble() throws Exception {

    }

    @Test
    public void testGetValidFileName() throws Exception {

    }
//
//    @Test
//    public void testGetValidInput() {
//
//    }
//
//    @Test
//    public void testGetValidInteger() {
//
//    }
//
//    @Test
//    public void testGetValidNumber() {
//
//    }
//
//    @Test
//    public void testGetValidRedirectLocation() {
//
//    }

    @Test
    public void testIsInvalidFilename() {

    }

    @Test
    public void testIsValidDate() {

    }

    @Test
    public void testIsValidDirectoryPath() throws IOException {

    }

    @Test
    public void testIsValidDouble() {

    }

    @Test
    public void testIsValidFileContent() {

    }

    @Test
    public void testIsValidFileName() {

    }

    @Test
    public void testIsValidFileUpload() throws IOException {

    }

    @Test
    public void testisValidInput() {

    }

    @Test
    public void testIsValidInteger() {

    }

    @Test
    public void testIsValidListItem() {

    }

    @Test
    public void testIsValidNumber() {

    }


}
