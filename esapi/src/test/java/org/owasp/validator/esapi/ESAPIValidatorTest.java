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


import org.junit.Test;
import org.owasp.esapi.ESAPI;
import org.owasp.esapi.Encoder;
import org.owasp.esapi.ValidationErrorList;
import org.owasp.esapi.ValidationRule;
import org.owasp.esapi.http.MockHttpServletRequest;
import org.owasp.esapi.http.MockHttpServletResponse;
import org.owasp.esapi.reference.DefaultEncoder;
import org.owasp.esapi.reference.validation.HTMLValidationRule;
import org.owasp.esapi.reference.validation.StringValidationRule;

import java.io.File;
import java.io.IOException;
import java.io.UnsupportedEncodingException;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.*;

import static org.junit.Assert.*;

/**
 * Created by steven on 17/09/15.
 */
public class ESAPIValidatorTest {

    private static final String PREFERRED_ENCODING = "UTF-8";

    @Test
    public void testGetRule() {

        ValidationRule rule = new StringValidationRule("rule");
        ESAPIValidator.getInstance().addRule(rule);
        assertEquals(rule, ESAPIValidator.getInstance().getRule("rule"));
        assertFalse(rule == ESAPIValidator.getInstance().getRule("ridiculous"));
    }

    @Test
    public void testGetValidCreditCard() {
        System.out.println("getValidCreditCard");
        ValidationErrorList errors = new ValidationErrorList();

        assertTrue(ESAPIValidator.getInstance().isValidCreditCard("cctest1", "1234 9876 0000 0008", false));
        assertTrue(ESAPIValidator.getInstance().isValidCreditCard("cctest2", "1234987600000008", false));
        assertFalse(ESAPIValidator.getInstance().isValidCreditCard("cctest3", "12349876000000081", false));
        assertFalse(ESAPIValidator.getInstance().isValidCreditCard("cctest4", "4417 1234 5678 9112", false));

        ESAPIValidator.getInstance().getValidCreditCard("cctest5", "1234 9876 0000 0008", false, errors);
        assertEquals(0, errors.size());
        ESAPIValidator.getInstance().getValidCreditCard("cctest6", "1234987600000008", false, errors);
        assertEquals(0, errors.size());
        ESAPIValidator.getInstance().getValidCreditCard("cctest7", "12349876000000081", false, errors);
        assertEquals(1, errors.size());
        ESAPIValidator.getInstance().getValidCreditCard("cctest8", "4417 1234 5678 9112", false, errors);
        assertEquals(2, errors.size());

        assertTrue(ESAPIValidator.getInstance().isValidCreditCard("cctest1", "1234 9876 0000 0008", false, errors));
        assertTrue(errors.size() == 2);
        assertTrue(ESAPIValidator.getInstance().isValidCreditCard("cctest2", "1234987600000008", false, errors));
        assertTrue(errors.size() == 2);
        assertFalse(ESAPIValidator.getInstance().isValidCreditCard("cctest3", "12349876000000081", false, errors));
        assertTrue(errors.size() == 3);
        assertFalse(ESAPIValidator.getInstance().isValidCreditCard("cctest4", "4417 1234 5678 9112", false, errors));
        assertTrue(errors.size() == 4);
    }

    @Test
    public void testGetValidDate() throws Exception {
        System.out.println("getValidDate");
        ValidationErrorList errors = new ValidationErrorList();
        assertTrue(ESAPIValidator.getInstance().getValidDate("datetest1", "June 23, 1967", DateFormat.getDateInstance(DateFormat.MEDIUM, Locale.US), false) != null);
        ESAPIValidator.getInstance().getValidDate("datetest2", "freakshow", DateFormat.getDateInstance(), false, errors);
        assertEquals(1, errors.size());

        // TODO: This test case fails due to an apparent bug in SimpleDateFormat
        // Note: This seems to be fixed in JDK 6. Will leave it commented out since
        //		 we only require JDK 5. -kww
        ESAPIValidator.getInstance().getValidDate("test", "June 32, 2008", DateFormat.getDateInstance(), false, errors);
        // assertEquals( 2, errors.size() );
    }

    @Test
    public void testGetValidDirectoryPath() throws Exception {
        System.out.println("getValidDirectoryPath");
        ValidationErrorList errors = new ValidationErrorList();
        // find a directory that exists
        File parent = new File("/");
        String current = (new File(".")).getCanonicalPath();
        String path = this.getClass().getResource("/.esapi").getFile();

        ESAPIValidator.getInstance().getValidDirectoryPath("dirtest1", path, parent, true, errors);
        assertEquals(0, errors.size());
        ESAPIValidator.getInstance().getValidDirectoryPath("dirtest2", null, parent, false, errors);
        assertEquals(1, errors.size());
        ESAPIValidator.getInstance().getValidDirectoryPath("dirtest3", "ridicul%00ous", parent, false, errors);
        assertEquals(2, errors.size());
    }

    @Test
    public void testGetValidDouble() throws Exception {
        System.out.println("getValidDouble");
        ValidationErrorList errors = new ValidationErrorList();
        ESAPIValidator.getInstance().getValidDouble("dtest1", "1.0", 0, 20, true, errors);
        assertEquals(0, errors.size());
        ESAPIValidator.getInstance().getValidDouble("dtest2", null, 0, 20, true, errors);
        assertEquals(0, errors.size());
        ESAPIValidator.getInstance().getValidDouble("dtest3", null, 0, 20, false, errors);
        assertEquals(1, errors.size());
        ESAPIValidator.getInstance().getValidDouble("dtest4", "ridiculous", 0, 20, true, errors);
        assertEquals(2, errors.size());
        ESAPIValidator.getInstance().getValidDouble("dtest5", "" + (Double.MAX_VALUE), 0, 20, true, errors);
        assertEquals(3, errors.size());
        ESAPIValidator.getInstance().getValidDouble("dtest6", "" + (Double.MAX_VALUE + .00001), 0, 20, true, errors);
        assertEquals(4, errors.size());
    }

//    @Test
//    public void testGetValidFileContent() {
//        System.out.println("getValidFileContent");
//        ValidationErrorList errors = new ValidationErrorList();
//        byte[] bytes = null;
//        try {
//            bytes = "12345".getBytes(PREFERRED_ENCODING);
//        } catch (UnsupportedEncodingException e) {
//            fail(PREFERRED_ENCODING + " not a supported encoding?!?!!");
//        }
//        ESAPIValidator.getInstance().getValidFileContent("test", bytes, 5, true, errors);
//        assertEquals(0, errors.size());
//        ESAPIValidator.getInstance().getValidFileContent("test", bytes, 4, true, errors);
//        assertEquals(1, errors.size());
//    }

    @Test
    public void testGetValidFileName() throws Exception {
        System.out.println("getValidFileName");
        ValidationErrorList errors = new ValidationErrorList();
        String testName = "aspe%20ct.jar";
        assertEquals("Percent encoding is not changed", testName, ESAPIValidator.getInstance().getValidFileName("test", testName, ESAPI.securityConfiguration().getAllowedFileExtensions(), false, errors));
    }

    @Test
    public void testGetValidInput() {
        System.out.println("getValidInput");
        ValidationErrorList errors = new ValidationErrorList();
        // ESAPIValidator.getInstance().getValidInput(String, String, String, int, boolean, ValidationErrorList)
    }

    @Test
    public void testGetValidInteger() {
        System.out.println("getValidInteger");
        ValidationErrorList errors = new ValidationErrorList();
        // ESAPIValidator.getInstance().getValidInteger(String, String, int, int, boolean, ValidationErrorList)
    }

    @Test
    public void testGetValidListItem() {
        System.out.println("getValidListItem");
        ValidationErrorList errors = new ValidationErrorList();
        // ESAPIValidator.getInstance().getValidListItem(String, String, List, ValidationErrorList)
    }

    @Test
    public void testGetValidNumber() {
        System.out.println("getValidNumber");
        ValidationErrorList errors = new ValidationErrorList();
        // ESAPIValidator.getInstance().getValidNumber(String, String, long, long, boolean, ValidationErrorList)
    }

    @Test
    public void testGetValidRedirectLocation() {
        System.out.println("getValidRedirectLocation");
        ValidationErrorList errors = new ValidationErrorList();
        // ESAPIValidator.getInstance().getValidRedirectLocation(String, String, boolean, ValidationErrorList)
    }

    @Test
    public void testGetValidSafeHTML() throws Exception {
        System.out.println("getValidSafeHTML");
        ValidationErrorList errors = new ValidationErrorList();

        // new school test case setup
        HTMLValidationRule rule = new HTMLValidationRule("test");
        ESAPI.validator().addRule(rule);

        assertEquals("Test.", ESAPI.validator().getRule("test").getValid("test", "Test. <script>alert(document.cookie)</script>"));

        String test1 = "<b>Jeff</b>";
        String result1 = ESAPIValidator.getInstance().getValidSafeHTML("test", test1, 100, false, errors);
        assertEquals(test1, result1);

        String test2 = "<a href=\"http://www.aspectsecurity.com\">Aspect Security</a>";
        String result2 = ESAPIValidator.getInstance().getValidSafeHTML("test", test2, 100, false, errors);
        assertEquals(test2, result2);

        String test3 = "Test. <script>alert(document.cookie)</script>";
        assertEquals("Test.", rule.getSafe("test", test3));

        assertEquals("Test. &lt;<div>load=alert()</div>", rule.getSafe("test", "Test. <<div on<script></script>load=alert()"));
        assertEquals("Test. <div>b</div>", rule.getSafe("test", "Test. <div style={xss:expression(xss)}>b</div>"));
//        assertEquals("Test. alert(document.cookie)", rule.getSafe("test", "Test. <s%00cript>alert(document.cookie)</script>"));
//        assertEquals("Test. alert(document.cookie)", rule.getSafe("test", "Test. <s\tcript>alert(document.cookie)</script>"));
//        assertEquals("Test. alert(document.cookie)", rule.getSafe("test", "Test. <s\tcript>alert(document.cookie)</script>"));
        // TODO: ENHANCE waiting for a way to validate text headed for an attribute for scripts
        // This would be nice to catch, but just looks like text to AntiSamy
        // assertFalse(ESAPIValidator.getInstance().isValidSafeHTML("test", "\" onload=\"alert(document.cookie)\" "));
        // String result4 = ESAPIValidator.getInstance().getValidSafeHTML("test", test4);
        // assertEquals("", result4);
    }

    @Test
    public void testIsInvalidFilename() {
        System.out.println("testIsInvalidFilename");
        char invalidChars[] = "/\\:*?\"<>|".toCharArray();
        for (int i = 0; i < invalidChars.length; i++) {
            assertFalse(invalidChars[i] + " is an invalid character for a filename",
                    ESAPIValidator.getInstance().isValidFileName("test", "as" + invalidChars[i] + "pect.jar", false));
        }
        assertFalse("Files must have an extension", ESAPIValidator.getInstance().isValidFileName("test", "", false));
        assertFalse("Files must have a valid extension", ESAPIValidator.getInstance().isValidFileName("test.invalidExtension", "", false));
        assertFalse("Filennames cannot be the empty string", ESAPIValidator.getInstance().isValidFileName("test", "", false));
    }

    @Test
    public void testIsValidDate() {
        System.out.println("isValidDate");
        DateFormat format = SimpleDateFormat.getDateInstance(SimpleDateFormat.MEDIUM, Locale.US);
        assertTrue(ESAPIValidator.getInstance().isValidDate("datetest1", "September 11, 2001", format, true));
        assertFalse(ESAPIValidator.getInstance().isValidDate("datetest2", null, format, false));
        assertFalse(ESAPIValidator.getInstance().isValidDate("datetest3", "", format, false));

        ValidationErrorList errors = new ValidationErrorList();
        assertTrue(ESAPIValidator.getInstance().isValidDate("datetest1", "September 11, 2001", format, true, errors));
        assertTrue(errors.size() == 0);
        assertFalse(ESAPIValidator.getInstance().isValidDate("datetest2", null, format, false, errors));
        assertTrue(errors.size() == 1);
        assertFalse(ESAPIValidator.getInstance().isValidDate("datetest3", "", format, false, errors));
        assertTrue(errors.size() == 2);

    }

    @Test
    public void testIsValidDirectoryPath() throws IOException {
        System.out.println("isValidDirectoryPath");

        // get an encoder with a special list of codecs and make a validator out of it
        List list = new ArrayList();
        list.add("HTMLEntityCodec");
        Encoder encoder = new DefaultEncoder(list);
//        Validator instance = new DefaultValidator(encoder);

        boolean isWindows = (System.getProperty("os.name").indexOf("Windows") != -1) ? true : false;
        File parent = new File("/");

        ValidationErrorList errors = new ValidationErrorList();

        if (isWindows) {
            String sysRoot = new File(System.getenv("SystemRoot")).getCanonicalPath();
            // Windows paths that don't exist and thus should fail
            assertFalse(ESAPIValidator.getInstance().isValidDirectoryPath("test", "c:\\ridiculous", parent, false));
            assertFalse(ESAPIValidator.getInstance().isValidDirectoryPath("test", "c:\\jeff", parent, false));
            assertFalse(ESAPIValidator.getInstance().isValidDirectoryPath("test", "c:\\temp\\..\\etc", parent, false));

            // Windows paths
            assertTrue(ESAPIValidator.getInstance().isValidDirectoryPath("test", "C:\\", parent, false));                        // Windows root directory
            assertTrue(ESAPIValidator.getInstance().isValidDirectoryPath("test", sysRoot, parent, false));                  // Windows always exist directory
            assertFalse(ESAPIValidator.getInstance().isValidDirectoryPath("test", sysRoot + "\\System32\\cmd.exe", parent, false));      // Windows command shell

            // Unix specific paths should not pass
            assertFalse(ESAPIValidator.getInstance().isValidDirectoryPath("test", "/tmp", parent, false));      // Unix Temporary directory
            assertFalse(ESAPIValidator.getInstance().isValidDirectoryPath("test", "/bin/sh", parent, false));   // Unix Standard shell
            assertFalse(ESAPIValidator.getInstance().isValidDirectoryPath("test", "/etc/config", parent, false));

            // Unix specific paths that should not exist or work
            assertFalse(ESAPIValidator.getInstance().isValidDirectoryPath("test", "/etc/ridiculous", parent, false));
            assertFalse(ESAPIValidator.getInstance().isValidDirectoryPath("test", "/tmp/../etc", parent, false));

            assertFalse(ESAPIValidator.getInstance().isValidDirectoryPath("test1", "c:\\ridiculous", parent, false, errors));
            assertTrue(errors.size() == 1);
            assertFalse(ESAPIValidator.getInstance().isValidDirectoryPath("test2", "c:\\jeff", parent, false, errors));
            assertTrue(errors.size() == 2);
            assertFalse(ESAPIValidator.getInstance().isValidDirectoryPath("test3", "c:\\temp\\..\\etc", parent, false, errors));
            assertTrue(errors.size() == 3);

            // Windows paths
            assertTrue(ESAPIValidator.getInstance().isValidDirectoryPath("test4", "C:\\", parent, false, errors));                        // Windows root directory
            assertTrue(errors.size() == 3);
            assertTrue(ESAPIValidator.getInstance().isValidDirectoryPath("test5", sysRoot, parent, false, errors));                  // Windows always exist directory
            assertTrue(errors.size() == 3);
            assertFalse(ESAPIValidator.getInstance().isValidDirectoryPath("test6", sysRoot + "\\System32\\cmd.exe", parent, false, errors));      // Windows command shell
            assertTrue(errors.size() == 4);

            // Unix specific paths should not pass
            assertFalse(ESAPIValidator.getInstance().isValidDirectoryPath("test7", "/tmp", parent, false, errors));      // Unix Temporary directory
            assertTrue(errors.size() == 5);
            assertFalse(ESAPIValidator.getInstance().isValidDirectoryPath("test8", "/bin/sh", parent, false, errors));   // Unix Standard shell
            assertTrue(errors.size() == 6);
            assertFalse(ESAPIValidator.getInstance().isValidDirectoryPath("test9", "/etc/config", parent, false, errors));
            assertTrue(errors.size() == 7);

            // Unix specific paths that should not exist or work
            assertFalse(ESAPIValidator.getInstance().isValidDirectoryPath("test10", "/etc/ridiculous", parent, false, errors));
            assertTrue(errors.size() == 8);
            assertFalse(ESAPIValidator.getInstance().isValidDirectoryPath("test11", "/tmp/../etc", parent, false, errors));
            assertTrue(errors.size() == 9);

        } else {
            // Windows paths should fail
            assertFalse(ESAPIValidator.getInstance().isValidDirectoryPath("test", "c:\\ridiculous", parent, false));
            assertFalse(ESAPIValidator.getInstance().isValidDirectoryPath("test", "c:\\temp\\..\\etc", parent, false));

            // Standard Windows locations should fail
            assertFalse(ESAPIValidator.getInstance().isValidDirectoryPath("test", "c:\\", parent, false));                        // Windows root directory
            assertFalse(ESAPIValidator.getInstance().isValidDirectoryPath("test", "c:\\Windows\\temp", parent, false));               // Windows temporary directory
            assertFalse(ESAPIValidator.getInstance().isValidDirectoryPath("test", "c:\\Windows\\System32\\cmd.exe", parent, false));   // Windows command shell

            // Unix specific paths should pass
            assertTrue(ESAPIValidator.getInstance().isValidDirectoryPath("test", "/", parent, false));         // Root directory
            assertTrue(ESAPIValidator.getInstance().isValidDirectoryPath("test", "/bin", parent, false));      // Always exist directory

            // Unix specific paths that should not exist or work
            assertFalse(ESAPIValidator.getInstance().isValidDirectoryPath("test", "/bin/sh", parent, false));   // Standard shell, not dir
            assertFalse(ESAPIValidator.getInstance().isValidDirectoryPath("test", "/etc/ridiculous", parent, false));
            assertFalse(ESAPIValidator.getInstance().isValidDirectoryPath("test", "/tmp/../etc", parent, false));

            // Windows paths should fail
            assertFalse(ESAPIValidator.getInstance().isValidDirectoryPath("test1", "c:\\ridiculous", parent, false, errors));
            assertTrue(errors.size() == 1);
            assertFalse(ESAPIValidator.getInstance().isValidDirectoryPath("test2", "c:\\temp\\..\\etc", parent, false, errors));
            assertTrue(errors.size() == 2);

            // Standard Windows locations should fail
            assertFalse(ESAPIValidator.getInstance().isValidDirectoryPath("test3", "c:\\", parent, false, errors));                        // Windows root directory
            assertTrue(errors.size() == 3);
            assertFalse(ESAPIValidator.getInstance().isValidDirectoryPath("test4", "c:\\Windows\\temp", parent, false, errors));               // Windows temporary directory
            assertTrue(errors.size() == 4);
            assertFalse(ESAPIValidator.getInstance().isValidDirectoryPath("test5", "c:\\Windows\\System32\\cmd.exe", parent, false, errors));   // Windows command shell
            assertTrue(errors.size() == 5);

            // Unix specific paths should pass
            assertTrue(ESAPIValidator.getInstance().isValidDirectoryPath("test6", "/", parent, false, errors));         // Root directory
            assertTrue(errors.size() == 5);
            assertTrue(ESAPIValidator.getInstance().isValidDirectoryPath("test7", "/bin", parent, false, errors));      // Always exist directory
            assertTrue(errors.size() == 5);

            // Unix specific paths that should not exist or work
            assertFalse(ESAPIValidator.getInstance().isValidDirectoryPath("test8", "/bin/sh", parent, false, errors));   // Standard shell, not dir
            assertTrue(errors.size() == 6);
            assertFalse(ESAPIValidator.getInstance().isValidDirectoryPath("test9", "/etc/ridiculous", parent, false, errors));
            assertTrue(errors.size() == 7);
            assertFalse(ESAPIValidator.getInstance().isValidDirectoryPath("test10", "/tmp/../etc", parent, false, errors));
            assertTrue(errors.size() == 8);
        }
    }

    @Test
    public void TestIsValidDirectoryPath() {
        // isValidDirectoryPath(String, String, boolean)
    }

    @Test
    public void testIsValidDouble() {
        // isValidDouble(String, String, double, double, boolean)
        ValidationErrorList errors = new ValidationErrorList();
        //testing negative range
        assertFalse(ESAPIValidator.getInstance().isValidDouble("test1", "-4", 1, 10, false, errors));
        assertTrue(errors.size() == 1);
        assertTrue(ESAPIValidator.getInstance().isValidDouble("test2", "-4", -10, 10, false, errors));
        assertTrue(errors.size() == 1);
        //testing null value
        assertTrue(ESAPIValidator.getInstance().isValidDouble("test3", null, -10, 10, true, errors));
        assertTrue(errors.size() == 1);
        assertFalse(ESAPIValidator.getInstance().isValidDouble("test4", null, -10, 10, false, errors));
        assertTrue(errors.size() == 2);
        //testing empty string
        assertTrue(ESAPIValidator.getInstance().isValidDouble("test5", "", -10, 10, true, errors));
        assertTrue(errors.size() == 2);
        assertFalse(ESAPIValidator.getInstance().isValidDouble("test6", "", -10, 10, false, errors));
        assertTrue(errors.size() == 3);
        //testing improper range
        assertFalse(ESAPIValidator.getInstance().isValidDouble("test7", "50.0", 10, -10, false, errors));
        assertTrue(errors.size() == 4);
        //testing non-integers
        assertTrue(ESAPIValidator.getInstance().isValidDouble("test8", "4.3214", -10, 10, true, errors));
        assertTrue(errors.size() == 4);
        assertTrue(ESAPIValidator.getInstance().isValidDouble("test9", "-1.65", -10, 10, true, errors));
        assertTrue(errors.size() == 4);
        //other testing
        assertTrue(ESAPIValidator.getInstance().isValidDouble("test10", "4", 1, 10, false, errors));
        assertTrue(errors.size() == 4);
        assertTrue(ESAPIValidator.getInstance().isValidDouble("test11", "400", 1, 10000, false, errors));
        assertTrue(errors.size() == 4);
        assertTrue(ESAPIValidator.getInstance().isValidDouble("test12", "400000000", 1, 400000000, false, errors));
        assertTrue(errors.size() == 4);
        assertFalse(ESAPIValidator.getInstance().isValidDouble("test13", "4000000000000", 1, 10000, false, errors));
        assertTrue(errors.size() == 5);
        assertFalse(ESAPIValidator.getInstance().isValidDouble("test14", "alsdkf", 10, 10000, false, errors));
        assertTrue(errors.size() == 6);
        assertFalse(ESAPIValidator.getInstance().isValidDouble("test15", "--10", 10, 10000, false, errors));
        assertTrue(errors.size() == 7);
        assertFalse(ESAPIValidator.getInstance().isValidDouble("test16", "14.1414234x", 10, 10000, false, errors));
        assertTrue(errors.size() == 8);
        assertFalse(ESAPIValidator.getInstance().isValidDouble("test17", "Infinity", 10, 10000, false, errors));
        assertTrue(errors.size() == 9);
        assertFalse(ESAPIValidator.getInstance().isValidDouble("test18", "-Infinity", 10, 10000, false, errors));
        assertTrue(errors.size() == 10);
        assertFalse(ESAPIValidator.getInstance().isValidDouble("test19", "NaN", 10, 10000, false, errors));
        assertTrue(errors.size() == 11);
        assertFalse(ESAPIValidator.getInstance().isValidDouble("test20", "-NaN", 10, 10000, false, errors));
        assertTrue(errors.size() == 12);
        assertFalse(ESAPIValidator.getInstance().isValidDouble("test21", "+NaN", 10, 10000, false, errors));
        assertTrue(errors.size() == 13);
        assertTrue(ESAPIValidator.getInstance().isValidDouble("test22", "1e-6", -999999999, 999999999, false, errors));
        assertTrue(errors.size() == 13);
        assertTrue(ESAPIValidator.getInstance().isValidDouble("test23", "-1e-6", -999999999, 999999999, false, errors));
        assertTrue(errors.size() == 13);
    }

    @Test
    public void testIsValidFileContent() {
        System.out.println("isValidFileContent");
        byte[] content = null;
        try {
            content = "This is some file content".getBytes(PREFERRED_ENCODING);
        } catch (UnsupportedEncodingException e) {
            fail(PREFERRED_ENCODING + " not a supported encoding?!?!!!");
        }
        assertTrue(ESAPIValidator.getInstance().isValidFileContent("test", content, 100, false));
    }

    @Test
    public void testIsValidFileName() {
        System.out.println("isValidFileName");
        assertTrue("Simple valid filename with a valid extension", ESAPIValidator.getInstance().isValidFileName("test", "aspect.jar", false));
        assertTrue("All valid filename characters are accepted", ESAPIValidator.getInstance().isValidFileName("test", "!@#$%^&{}[]()_+-=,.~'` abcdefghijklmnopqrstuvwxyzABCDEFGHIJKLMNOPQRSTUVWXYZ1234567890.jar", false));
        assertTrue("Legal filenames that decode to legal filenames are accepted", ESAPIValidator.getInstance().isValidFileName("test", "aspe%20ct.jar", false));

        ValidationErrorList errors = new ValidationErrorList();
        assertTrue("Simple valid filename with a valid extension", ESAPIValidator.getInstance().isValidFileName("test", "aspect.jar", false, errors));
        assertTrue("All valid filename characters are accepted", ESAPIValidator.getInstance().isValidFileName("test", "!@#$%^&{}[]()_+-=,.~'` abcdefghijklmnopqrstuvwxyzABCDEFGHIJKLMNOPQRSTUVWXYZ1234567890.jar", false, errors));
        assertTrue("Legal filenames that decode to legal filenames are accepted", ESAPIValidator.getInstance().isValidFileName("test", "aspe%20ct.jar", false, errors));
        assertTrue(errors.size() == 0);
    }

    @Test
    public void testIsValidFileUpload() throws IOException {
        System.out.println("isValidFileUpload");
        String filepath = new File(System.getProperty("user.dir")).getCanonicalPath();
        String filename = "aspect.jar";
        File parent = new File("/").getCanonicalFile();
        ValidationErrorList errors = new ValidationErrorList();
        byte[] content = null;
        try {
            content = "This is some file content".getBytes(PREFERRED_ENCODING);
        } catch (UnsupportedEncodingException e) {
            fail(PREFERRED_ENCODING + " not a supported encoding?!?!!!");
        }
        assertTrue(ESAPIValidator.getInstance().isValidFileUpload("test", filepath, filename, parent, content, 100, false));
        assertTrue(ESAPIValidator.getInstance().isValidFileUpload("test", filepath, filename, parent, content, 100, false, errors));
        assertTrue(errors.size() == 0);

        filepath = "/ridiculous";
        filename = "aspect.jar";
        try {
            content = "This is some file content".getBytes(PREFERRED_ENCODING);
        } catch (UnsupportedEncodingException e) {
            fail(PREFERRED_ENCODING + " not a supported encoding?!?!!!");
        }
        assertFalse(ESAPIValidator.getInstance().isValidFileUpload("test", filepath, filename, parent, content, 100, false));
        assertFalse(ESAPIValidator.getInstance().isValidFileUpload("test", filepath, filename, parent, content, 100, false, errors));
        assertTrue(errors.size() == 1);
    }

    @Test
    public void testIsValidHTTPRequestParameterSet() {
        //		isValidHTTPRequestParameterSet(String, Set, Set)
    }

    @Test
    public void testisValidInput() {
        System.out.println("isValidInput");
        assertTrue(ESAPIValidator.getInstance().isValidInput("test", "jeff.williams@aspectsecurity.com", "Email", 100, false));
        assertFalse(ESAPIValidator.getInstance().isValidInput("test", "jeff.williams@@aspectsecurity.com", "Email", 100, false));
        assertFalse(ESAPIValidator.getInstance().isValidInput("test", "jeff.williams@aspectsecurity", "Email", 100, false));
        assertTrue(ESAPIValidator.getInstance().isValidInput("test", "jeff.wil'liams@aspectsecurity.com", "Email", 100, false));
        assertTrue(ESAPIValidator.getInstance().isValidInput("test", "jeff.wil''liams@aspectsecurity.com", "Email", 100, false));
        assertTrue(ESAPIValidator.getInstance().isValidInput("test", "123.168.100.234", "IPAddress", 100, false));
        assertTrue(ESAPIValidator.getInstance().isValidInput("test", "192.168.1.234", "IPAddress", 100, false));
        assertFalse(ESAPIValidator.getInstance().isValidInput("test", "..168.1.234", "IPAddress", 100, false));
        assertFalse(ESAPIValidator.getInstance().isValidInput("test", "10.x.1.234", "IPAddress", 100, false));
        assertTrue(ESAPIValidator.getInstance().isValidInput("test", "http://www.aspectsecurity.com", "URL", 100, false));
        assertFalse(ESAPIValidator.getInstance().isValidInput("test", "http:///www.aspectsecurity.com", "URL", 100, false));
        assertFalse(ESAPIValidator.getInstance().isValidInput("test", "http://www.aspect security.com", "URL", 100, false));
        assertTrue(ESAPIValidator.getInstance().isValidInput("test", "078-05-1120", "SSN", 100, false));
        assertTrue(ESAPIValidator.getInstance().isValidInput("test", "078 05 1120", "SSN", 100, false));
        assertTrue(ESAPIValidator.getInstance().isValidInput("test", "078051120", "SSN", 100, false));
        assertFalse(ESAPIValidator.getInstance().isValidInput("test", "987-65-4320", "SSN", 100, false));
        assertFalse(ESAPIValidator.getInstance().isValidInput("test", "000-00-0000", "SSN", 100, false));
        assertFalse(ESAPIValidator.getInstance().isValidInput("test", "(555) 555-5555", "SSN", 100, false));
        assertFalse(ESAPIValidator.getInstance().isValidInput("test", "test", "SSN", 100, false));
        assertTrue(ESAPIValidator.getInstance().isValidInput("test", "jeffWILLIAMS123", "HTTPParameterValue", 100, false));
        assertTrue(ESAPIValidator.getInstance().isValidInput("test", "jeff .-/+=@_ WILLIAMS", "HTTPParameterValue", 100, false));
        // Removed per Issue 116 - The '*' character is valid as a parameter character
//        assertFalse(ESAPIValidator.getInstance().isValidInput("test", "jeff*WILLIAMS", "HTTPParameterValue", 100, false));
        assertFalse(ESAPIValidator.getInstance().isValidInput("test", "jeff^WILLIAMS", "HTTPParameterValue", 100, false));
        assertFalse(ESAPIValidator.getInstance().isValidInput("test", "jeff\\WILLIAMS", "HTTPParameterValue", 100, false));

        assertTrue(ESAPIValidator.getInstance().isValidInput("test", null, "Email", 100, true));
        assertFalse(ESAPIValidator.getInstance().isValidInput("test", null, "Email", 100, false));

        ValidationErrorList errors = new ValidationErrorList();

        assertTrue(ESAPIValidator.getInstance().isValidInput("test1", "jeff.williams@aspectsecurity.com", "Email", 100, false, errors));
        assertTrue(errors.size() == 0);
        assertFalse(ESAPIValidator.getInstance().isValidInput("test2", "jeff.williams@@aspectsecurity.com", "Email", 100, false, errors));
        assertTrue(errors.size() == 1);
        assertFalse(ESAPIValidator.getInstance().isValidInput("test3", "jeff.williams@aspectsecurity", "Email", 100, false, errors));
        assertTrue(errors.size() == 2);
        assertTrue(ESAPIValidator.getInstance().isValidInput("test4", "jeff.wil'liams@aspectsecurity.com", "Email", 100, false, errors));
        assertTrue(errors.size() == 2);
        assertTrue(ESAPIValidator.getInstance().isValidInput("test5", "jeff.wil''liams@aspectsecurity.com", "Email", 100, false, errors));
        assertTrue(errors.size() == 2);
        assertTrue(ESAPIValidator.getInstance().isValidInput("test6", "123.168.100.234", "IPAddress", 100, false, errors));
        assertTrue(errors.size() == 2);
        assertTrue(ESAPIValidator.getInstance().isValidInput("test7", "192.168.1.234", "IPAddress", 100, false, errors));
        assertTrue(errors.size() == 2);
        assertFalse(ESAPIValidator.getInstance().isValidInput("test8", "..168.1.234", "IPAddress", 100, false, errors));
        assertTrue(errors.size() == 3);
        assertFalse(ESAPIValidator.getInstance().isValidInput("test9", "10.x.1.234", "IPAddress", 100, false, errors));
        assertTrue(errors.size() == 4);
        assertTrue(ESAPIValidator.getInstance().isValidInput("test10", "http://www.aspectsecurity.com", "URL", 100, false, errors));
        assertTrue(errors.size() == 4);
        assertFalse(ESAPIValidator.getInstance().isValidInput("test11", "http:///www.aspectsecurity.com", "URL", 100, false, errors));
        assertTrue(errors.size() == 5);
        assertFalse(ESAPIValidator.getInstance().isValidInput("test12", "http://www.aspect security.com", "URL", 100, false, errors));
        assertTrue(errors.size() == 6);
        assertTrue(ESAPIValidator.getInstance().isValidInput("test13", "078-05-1120", "SSN", 100, false, errors));
        assertTrue(errors.size() == 6);
        assertTrue(ESAPIValidator.getInstance().isValidInput("test14", "078 05 1120", "SSN", 100, false, errors));
        assertTrue(errors.size() == 6);
        assertTrue(ESAPIValidator.getInstance().isValidInput("test15", "078051120", "SSN", 100, false, errors));
        assertTrue(errors.size() == 6);
        assertFalse(ESAPIValidator.getInstance().isValidInput("test16", "987-65-4320", "SSN", 100, false, errors));
        assertTrue(errors.size() == 7);
        assertFalse(ESAPIValidator.getInstance().isValidInput("test17", "000-00-0000", "SSN", 100, false, errors));
        assertTrue(errors.size() == 8);
        assertFalse(ESAPIValidator.getInstance().isValidInput("test18", "(555) 555-5555", "SSN", 100, false, errors));
        assertTrue(errors.size() == 9);
        assertFalse(ESAPIValidator.getInstance().isValidInput("test19", "test", "SSN", 100, false, errors));
        assertTrue(errors.size() == 10);
        assertTrue(ESAPIValidator.getInstance().isValidInput("test20", "jeffWILLIAMS123", "HTTPParameterValue", 100, false, errors));
        assertTrue(errors.size() == 10);
        assertTrue(ESAPIValidator.getInstance().isValidInput("test21", "jeff .-/+=@_ WILLIAMS", "HTTPParameterValue", 100, false, errors));
        assertTrue(errors.size() == 10);
        // Removed per Issue 116 - The '*' character is valid as a parameter character
//        assertFalse(ESAPIValidator.getInstance().isValidInput("test", "jeff*WILLIAMS", "HTTPParameterValue", 100, false));
        assertFalse(ESAPIValidator.getInstance().isValidInput("test22", "jeff^WILLIAMS", "HTTPParameterValue", 100, false, errors));
        assertTrue(errors.size() == 11);
        assertFalse(ESAPIValidator.getInstance().isValidInput("test23", "jeff\\WILLIAMS", "HTTPParameterValue", 100, false, errors));
        assertTrue(errors.size() == 12);

        assertTrue(ESAPIValidator.getInstance().isValidInput("test", null, "Email", 100, true, errors));
        assertFalse(ESAPIValidator.getInstance().isValidInput("test", null, "Email", 100, false, errors));
    }

    @Test
    public void testIsValidInteger() {
        System.out.println("isValidInteger");
        //testing negative range
        assertFalse(ESAPIValidator.getInstance().isValidInteger("test", "-4", 1, 10, false));
        assertTrue(ESAPIValidator.getInstance().isValidInteger("test", "-4", -10, 10, false));
        //testing null value
        assertTrue(ESAPIValidator.getInstance().isValidInteger("test", null, -10, 10, true));
        assertFalse(ESAPIValidator.getInstance().isValidInteger("test", null, -10, 10, false));
        //testing empty string
        assertTrue(ESAPIValidator.getInstance().isValidInteger("test", "", -10, 10, true));
        assertFalse(ESAPIValidator.getInstance().isValidInteger("test", "", -10, 10, false));
        //testing improper range
        assertFalse(ESAPIValidator.getInstance().isValidInteger("test", "50", 10, -10, false));
        //testing non-integers
        assertFalse(ESAPIValidator.getInstance().isValidInteger("test", "4.3214", -10, 10, true));
        assertFalse(ESAPIValidator.getInstance().isValidInteger("test", "-1.65", -10, 10, true));
        //other testing
        assertTrue(ESAPIValidator.getInstance().isValidInteger("test", "4", 1, 10, false));
        assertTrue(ESAPIValidator.getInstance().isValidInteger("test", "400", 1, 10000, false));
        assertTrue(ESAPIValidator.getInstance().isValidInteger("test", "400000000", 1, 400000000, false));
        assertFalse(ESAPIValidator.getInstance().isValidInteger("test", "4000000000000", 1, 10000, false));
        assertFalse(ESAPIValidator.getInstance().isValidInteger("test", "alsdkf", 10, 10000, false));
        assertFalse(ESAPIValidator.getInstance().isValidInteger("test", "--10", 10, 10000, false));
        assertFalse(ESAPIValidator.getInstance().isValidInteger("test", "14.1414234x", 10, 10000, false));
        assertFalse(ESAPIValidator.getInstance().isValidInteger("test", "Infinity", 10, 10000, false));
        assertFalse(ESAPIValidator.getInstance().isValidInteger("test", "-Infinity", 10, 10000, false));
        assertFalse(ESAPIValidator.getInstance().isValidInteger("test", "NaN", 10, 10000, false));
        assertFalse(ESAPIValidator.getInstance().isValidInteger("test", "-NaN", 10, 10000, false));
        assertFalse(ESAPIValidator.getInstance().isValidInteger("test", "+NaN", 10, 10000, false));
        assertFalse(ESAPIValidator.getInstance().isValidInteger("test", "1e-6", -999999999, 999999999, false));
        assertFalse(ESAPIValidator.getInstance().isValidInteger("test", "-1e-6", -999999999, 999999999, false));

        ValidationErrorList errors = new ValidationErrorList();
        //testing negative range
        assertFalse(ESAPIValidator.getInstance().isValidInteger("test1", "-4", 1, 10, false, errors));
        assertTrue(errors.size() == 1);
        assertTrue(ESAPIValidator.getInstance().isValidInteger("test2", "-4", -10, 10, false, errors));
        assertTrue(errors.size() == 1);
        //testing null value
        assertTrue(ESAPIValidator.getInstance().isValidInteger("test3", null, -10, 10, true, errors));
        assertTrue(errors.size() == 1);
        assertFalse(ESAPIValidator.getInstance().isValidInteger("test4", null, -10, 10, false, errors));
        assertTrue(errors.size() == 2);
        //testing empty string
        assertTrue(ESAPIValidator.getInstance().isValidInteger("test5", "", -10, 10, true, errors));
        assertTrue(errors.size() == 2);
        assertFalse(ESAPIValidator.getInstance().isValidInteger("test6", "", -10, 10, false, errors));
        assertTrue(errors.size() == 3);
        //testing improper range
        assertFalse(ESAPIValidator.getInstance().isValidInteger("test7", "50", 10, -10, false, errors));
        assertTrue(errors.size() == 4);
        //testing non-integers
        assertFalse(ESAPIValidator.getInstance().isValidInteger("test8", "4.3214", -10, 10, true, errors));
        assertTrue(errors.size() == 5);
        assertFalse(ESAPIValidator.getInstance().isValidInteger("test9", "-1.65", -10, 10, true, errors));
        assertTrue(errors.size() == 6);
        //other testing
        assertTrue(ESAPIValidator.getInstance().isValidInteger("test10", "4", 1, 10, false, errors));
        assertTrue(errors.size() == 6);
        assertTrue(ESAPIValidator.getInstance().isValidInteger("test11", "400", 1, 10000, false, errors));
        assertTrue(errors.size() == 6);
        assertTrue(ESAPIValidator.getInstance().isValidInteger("test12", "400000000", 1, 400000000, false, errors));
        assertTrue(errors.size() == 6);
        assertFalse(ESAPIValidator.getInstance().isValidInteger("test13", "4000000000000", 1, 10000, false, errors));
        assertTrue(errors.size() == 7);
        assertFalse(ESAPIValidator.getInstance().isValidInteger("test14", "alsdkf", 10, 10000, false, errors));
        assertTrue(errors.size() == 8);
        assertFalse(ESAPIValidator.getInstance().isValidInteger("test15", "--10", 10, 10000, false, errors));
        assertTrue(errors.size() == 9);
        assertFalse(ESAPIValidator.getInstance().isValidInteger("test16", "14.1414234x", 10, 10000, false, errors));
        assertTrue(errors.size() == 10);
        assertFalse(ESAPIValidator.getInstance().isValidInteger("test17", "Infinity", 10, 10000, false, errors));
        assertTrue(errors.size() == 11);
        assertFalse(ESAPIValidator.getInstance().isValidInteger("test18", "-Infinity", 10, 10000, false, errors));
        assertTrue(errors.size() == 12);
        assertFalse(ESAPIValidator.getInstance().isValidInteger("test19", "NaN", 10, 10000, false, errors));
        assertTrue(errors.size() == 13);
        assertFalse(ESAPIValidator.getInstance().isValidInteger("test20", "-NaN", 10, 10000, false, errors));
        assertTrue(errors.size() == 14);
        assertFalse(ESAPIValidator.getInstance().isValidInteger("test21", "+NaN", 10, 10000, false, errors));
        assertTrue(errors.size() == 15);
        assertFalse(ESAPIValidator.getInstance().isValidInteger("test22", "1e-6", -999999999, 999999999, false, errors));
        assertTrue(errors.size() == 16);
        assertFalse(ESAPIValidator.getInstance().isValidInteger("test23", "-1e-6", -999999999, 999999999, false, errors));
        assertTrue(errors.size() == 17);

    }

    @Test
    public void testIsValidListItem() {
        System.out.println("isValidListItem");
        List list = new ArrayList();
        list.add("one");
        list.add("two");
        assertTrue(ESAPIValidator.getInstance().isValidListItem("test", "one", list));
        assertFalse(ESAPIValidator.getInstance().isValidListItem("test", "three", list));

        ValidationErrorList errors = new ValidationErrorList();
        assertTrue(ESAPIValidator.getInstance().isValidListItem("test1", "one", list, errors));
        assertTrue(errors.size() == 0);
        assertFalse(ESAPIValidator.getInstance().isValidListItem("test2", "three", list, errors));
        assertTrue(errors.size() == 1);
    }

    @Test
    public void testIsValidNumber() {
        System.out.println("isValidNumber");
        //testing negative range
        assertFalse(ESAPIValidator.getInstance().isValidNumber("test", "-4", 1, 10, false));
        assertTrue(ESAPIValidator.getInstance().isValidNumber("test", "-4", -10, 10, false));
        //testing null value
        assertTrue(ESAPIValidator.getInstance().isValidNumber("test", null, -10, 10, true));
        assertFalse(ESAPIValidator.getInstance().isValidNumber("test", null, -10, 10, false));
        //testing empty string
        assertTrue(ESAPIValidator.getInstance().isValidNumber("test", "", -10, 10, true));
        assertFalse(ESAPIValidator.getInstance().isValidNumber("test", "", -10, 10, false));
        //testing improper range
        assertFalse(ESAPIValidator.getInstance().isValidNumber("test", "5", 10, -10, false));
        //testing non-integers
        assertTrue(ESAPIValidator.getInstance().isValidNumber("test", "4.3214", -10, 10, true));
        assertTrue(ESAPIValidator.getInstance().isValidNumber("test", "-1.65", -10, 10, true));
        //other testing
        assertTrue(ESAPIValidator.getInstance().isValidNumber("test", "4", 1, 10, false));
        assertTrue(ESAPIValidator.getInstance().isValidNumber("test", "400", 1, 10000, false));
        assertTrue(ESAPIValidator.getInstance().isValidNumber("test", "400000000", 1, 400000000, false));
        assertFalse(ESAPIValidator.getInstance().isValidNumber("test", "4000000000000", 1, 10000, false));
        assertFalse(ESAPIValidator.getInstance().isValidNumber("test", "alsdkf", 10, 10000, false));
        assertFalse(ESAPIValidator.getInstance().isValidNumber("test", "--10", 10, 10000, false));
        assertFalse(ESAPIValidator.getInstance().isValidNumber("test", "14.1414234x", 10, 10000, false));
        assertFalse(ESAPIValidator.getInstance().isValidNumber("test", "Infinity", 10, 10000, false));
        assertFalse(ESAPIValidator.getInstance().isValidNumber("test", "-Infinity", 10, 10000, false));
        assertFalse(ESAPIValidator.getInstance().isValidNumber("test", "NaN", 10, 10000, false));
        assertFalse(ESAPIValidator.getInstance().isValidNumber("test", "-NaN", 10, 10000, false));
        assertFalse(ESAPIValidator.getInstance().isValidNumber("test", "+NaN", 10, 10000, false));
        assertTrue(ESAPIValidator.getInstance().isValidNumber("test", "1e-6", -999999999, 999999999, false));
        assertTrue(ESAPIValidator.getInstance().isValidNumber("test", "-1e-6", -999999999, 999999999, false));

        ValidationErrorList errors = new ValidationErrorList();
        //testing negative range
        assertFalse(ESAPIValidator.getInstance().isValidNumber("test1", "-4", 1, 10, false, errors));
        assertTrue(errors.size() == 1);
        assertTrue(ESAPIValidator.getInstance().isValidNumber("test2", "-4", -10, 10, false, errors));
        assertTrue(errors.size() == 1);
        //testing null value
        assertTrue(ESAPIValidator.getInstance().isValidNumber("test3", null, -10, 10, true, errors));
        assertTrue(errors.size() == 1);
        assertFalse(ESAPIValidator.getInstance().isValidNumber("test4", null, -10, 10, false, errors));
        assertTrue(errors.size() == 2);
        //testing empty string
        assertTrue(ESAPIValidator.getInstance().isValidNumber("test5", "", -10, 10, true, errors));
        assertTrue(errors.size() == 2);
        assertFalse(ESAPIValidator.getInstance().isValidNumber("test6", "", -10, 10, false, errors));
        assertTrue(errors.size() == 3);
        //testing improper range
        assertFalse(ESAPIValidator.getInstance().isValidNumber("test7", "5", 10, -10, false, errors));
        assertTrue(errors.size() == 4);
        //testing non-integers
        assertTrue(ESAPIValidator.getInstance().isValidNumber("test8", "4.3214", -10, 10, true, errors));
        assertTrue(errors.size() == 4);
        assertTrue(ESAPIValidator.getInstance().isValidNumber("test9", "-1.65", -10, 10, true, errors));
        assertTrue(errors.size() == 4);
        //other testing
        assertTrue(ESAPIValidator.getInstance().isValidNumber("test10", "4", 1, 10, false, errors));
        assertTrue(errors.size() == 4);
        assertTrue(ESAPIValidator.getInstance().isValidNumber("test11", "400", 1, 10000, false, errors));
        assertTrue(errors.size() == 4);
        assertTrue(ESAPIValidator.getInstance().isValidNumber("test12", "400000000", 1, 400000000, false, errors));
        assertTrue(errors.size() == 4);
        assertFalse(ESAPIValidator.getInstance().isValidNumber("test13", "4000000000000", 1, 10000, false, errors));
        assertTrue(errors.size() == 5);
        assertFalse(ESAPIValidator.getInstance().isValidNumber("test14", "alsdkf", 10, 10000, false, errors));
        assertTrue(errors.size() == 6);
        assertFalse(ESAPIValidator.getInstance().isValidNumber("test15", "--10", 10, 10000, false, errors));
        assertTrue(errors.size() == 7);
        assertFalse(ESAPIValidator.getInstance().isValidNumber("test16", "14.1414234x", 10, 10000, false, errors));
        assertTrue(errors.size() == 8);
        assertFalse(ESAPIValidator.getInstance().isValidNumber("test17", "Infinity", 10, 10000, false, errors));
        assertTrue(errors.size() == 9);
        assertFalse(ESAPIValidator.getInstance().isValidNumber("test18", "-Infinity", 10, 10000, false, errors));
        assertTrue(errors.size() == 10);
        assertFalse(ESAPIValidator.getInstance().isValidNumber("test19", "NaN", 10, 10000, false, errors));
        assertTrue(errors.size() == 11);
        assertFalse(ESAPIValidator.getInstance().isValidNumber("test20", "-NaN", 10, 10000, false, errors));
        assertTrue(errors.size() == 12);
        assertFalse(ESAPIValidator.getInstance().isValidNumber("test21", "+NaN", 10, 10000, false, errors));
        assertTrue(errors.size() == 13);
        assertTrue(ESAPIValidator.getInstance().isValidNumber("test22", "1e-6", -999999999, 999999999, false, errors));
        assertTrue(errors.size() == 13);
        assertTrue(ESAPIValidator.getInstance().isValidNumber("test23", "-1e-6", -999999999, 999999999, false, errors));
        assertTrue(errors.size() == 13);
    }

    @Test
    public void testIsValidParameterSet() {
        System.out.println("isValidParameterSet");
        Set requiredNames = new HashSet();
        requiredNames.add("p1");
        requiredNames.add("p2");
        requiredNames.add("p3");
        Set optionalNames = new HashSet();
        optionalNames.add("p4");
        optionalNames.add("p5");
        optionalNames.add("p6");
        MockHttpServletRequest request = new MockHttpServletRequest();
        MockHttpServletResponse response = new MockHttpServletResponse();
        request.addParameter("p1", "value");
        request.addParameter("p2", "value");
        request.addParameter("p3", "value");
        ESAPI.httpUtilities().setCurrentHTTP(request, response);
        ValidationErrorList errors = new ValidationErrorList();
        assertTrue(ESAPIValidator.getInstance().isValidHTTPRequestParameterSet("HTTPParameters", request, requiredNames, optionalNames));
        assertTrue(ESAPIValidator.getInstance().isValidHTTPRequestParameterSet("HTTPParameters", request, requiredNames, optionalNames, errors));
        assertTrue(errors.size() == 0);
        request.addParameter("p4", "value");
        request.addParameter("p5", "value");
        request.addParameter("p6", "value");
        assertTrue(ESAPIValidator.getInstance().isValidHTTPRequestParameterSet("HTTPParameters", request, requiredNames, optionalNames));
        assertTrue(ESAPIValidator.getInstance().isValidHTTPRequestParameterSet("HTTPParameters", request, requiredNames, optionalNames, errors));
        assertTrue(errors.size() == 0);
        request.removeParameter("p1");
        assertFalse(ESAPIValidator.getInstance().isValidHTTPRequestParameterSet("HTTPParameters", request, requiredNames, optionalNames));
        assertFalse(ESAPIValidator.getInstance().isValidHTTPRequestParameterSet("HTTPParameters", request, requiredNames, optionalNames, errors));
        assertTrue(errors.size() == 1);
    }

    @Test
    public void testIsValidPrintable() {
        System.out.println("isValidPrintable");
        assertTrue(ESAPIValidator.getInstance().isValidPrintable("name", "abcDEF", 100, false));
        assertTrue(ESAPIValidator.getInstance().isValidPrintable("name", "!@#R()*$;><()", 100, false));
        char[] chars = {0x60, (char) 0xFF, 0x10, 0x25};
        assertFalse(ESAPIValidator.getInstance().isValidPrintable("name", chars, 100, false));
        assertFalse(ESAPIValidator.getInstance().isValidPrintable("name", "%08", 100, false));

        ValidationErrorList errors = new ValidationErrorList();
        assertTrue(ESAPIValidator.getInstance().isValidPrintable("name1", "abcDEF", 100, false, errors));
        assertTrue(errors.size() == 0);
        assertTrue(ESAPIValidator.getInstance().isValidPrintable("name2", "!@#R()*$;><()", 100, false, errors));
        assertTrue(errors.size() == 0);
        assertFalse(ESAPIValidator.getInstance().isValidPrintable("name3", chars, 100, false, errors));
        assertTrue(errors.size() == 1);
        assertFalse(ESAPIValidator.getInstance().isValidPrintable("name4", "%08", 100, false, errors));
        assertTrue(errors.size() == 2);

    }

    @Test
    public void testIsValidRedirectLocation() {
        //		isValidRedirectLocation(String, String, boolean)
    }

    @Test
    public void testIsValidSafeHTML() {
        System.out.println("isValidSafeHTML");

        assertTrue(ESAPIValidator.getInstance().isValidSafeHTML("test", "<b>Jeff</b>", 100, false));
        assertTrue(ESAPIValidator.getInstance().isValidSafeHTML("test", "<a href=\"http://www.aspectsecurity.com\">Aspect Security</a>", 100, false));
        assertTrue(ESAPIValidator.getInstance().isValidSafeHTML("test", "Test. <script>alert(document.cookie)</script>", 100, false));
        assertTrue(ESAPIValidator.getInstance().isValidSafeHTML("test", "Test. <div style={xss:expression(xss)}>", 100, false));
        assertTrue(ESAPIValidator.getInstance().isValidSafeHTML("test", "Test. <s%00cript>alert(document.cookie)</script>", 100, false));
        assertTrue(ESAPIValidator.getInstance().isValidSafeHTML("test", "Test. <s\tcript>alert(document.cookie)</script>", 100, false));
        assertTrue(ESAPIValidator.getInstance().isValidSafeHTML("test", "Test. <s\r\n\0cript>alert(document.cookie)</script>", 100, false));

        // TODO: waiting for a way to validate text headed for an attribute for scripts
        // This would be nice to catch, but just looks like text to AntiSamy
        // assertFalse(ESAPIValidator.getInstance().isValidSafeHTML("test", "\" onload=\"alert(document.cookie)\" "));
        ValidationErrorList errors = new ValidationErrorList();
        assertTrue(ESAPIValidator.getInstance().isValidSafeHTML("test1", "<b>Jeff</b>", 100, false, errors));
        assertTrue(ESAPIValidator.getInstance().isValidSafeHTML("test2", "<a href=\"http://www.aspectsecurity.com\">Aspect Security</a>", 100, false, errors));
        assertTrue(ESAPIValidator.getInstance().isValidSafeHTML("test3", "Test. <script>alert(document.cookie)</script>", 100, false, errors));
        assertTrue(ESAPIValidator.getInstance().isValidSafeHTML("test4", "Test. <div style={xss:expression(xss)}>", 100, false, errors));
        assertTrue(ESAPIValidator.getInstance().isValidSafeHTML("test5", "Test. <s%00cript>alert(document.cookie)</script>", 100, false, errors));
        assertTrue(ESAPIValidator.getInstance().isValidSafeHTML("test6", "Test. <s\tcript>alert(document.cookie)</script>", 100, false, errors));
        assertTrue(ESAPIValidator.getInstance().isValidSafeHTML("test7", "Test. <s\r\n\0cript>alert(document.cookie)</script>", 100, false, errors));
        assertTrue(errors.size() == 0);

    }

//    @Test
//    public void testSafeReadLine() {
//        System.out.println("safeReadLine");
//
//        byte[] bytes = null;
//        try {
//            bytes = "testString".getBytes(PREFERRED_ENCODING);
//        } catch (UnsupportedEncodingException e1) {
//            fail(PREFERRED_ENCODING + " not a supported encoding?!?!!!");
//        }
//        ByteArrayInputStream s = new ByteArrayInputStream(bytes);
//        try {
//            ESAPIValidator.getInstance().safeReadLine(s, -1);
//            fail();
//        } catch (ValidationException e) {
//            // Expected
//        }
//        s.reset();
//        try {
//            ESAPIValidator.getInstance().safeReadLine(s, 4);
//            fail();
//        } catch (ValidationException e) {
//            // Expected
//        }
//        s.reset();
//        try {
//            String u = ESAPIValidator.getInstance().safeReadLine(s, 20);
//            assertEquals("testString", u);
//        } catch (ValidationException e) {
//            fail();
//        }
//
//        // This sub-test attempts to validate that BufferedReader.readLine() and safeReadLine() are similar in operation
//        // for the nominal case
//        try {
//            s.reset();
//            InputStreamReader isr = new InputStreamReader(s);
//            BufferedReader br = new BufferedReader(isr);
//            String u = br.readLine();
//            s.reset();
//            String v = ESAPIValidator.getInstance().safeReadLine(s, 20);
//            assertEquals(u, v);
//        } catch (IOException e) {
//            fail();
//        } catch (ValidationException e) {
//            fail();
//        }
//    }

//    @Test
//    public void testIssue82_SafeString_Bad_Regex() {
//        try {
//            ESAPIValidator.getInstance().getValidInput("address", "55 main st. pasadena ak", "SafeString", 512, false);
//        } catch (ValidationException e) {
//            fail(e.getLogMessage());
//        }
//    }

//    @Test
//    public void testGetParameterMap() {
////testing Validator.HTTPParameterName and Validator.HTTPParameterValue
//        MockHttpServletRequest request = new MockHttpServletRequest();
//        SecurityWrapperRequest safeRequest = new SecurityWrapperRequest(request);
////an example of a parameter from displaytag, should pass
//        request.addParameter("d-49653-p", "pass");
//        request.addParameter("<img ", "fail");
//        request.addParameter(generateStringOfLength(32), "pass");
//        request.addParameter(generateStringOfLength(33), "fail");
//        assertEquals(safeRequest.getParameterMap().size(), 2);
//        assertNull(safeRequest.getParameterMap().get("<img"));
//        assertNull(safeRequest.getParameterMap().get(generateStringOfLength(33)));
//    }

//    @Test
//    public void testGetParameterNames() {
////testing Validator.HTTPParameterName
//        MockHttpServletRequest request = new MockHttpServletRequest();
//        SecurityWrapperRequest safeRequest = new SecurityWrapperRequest(request);
////an example of a parameter from displaytag, should pass
//        request.addParameter("d-49653-p", "pass");
//        request.addParameter("<img ", "fail");
//        request.addParameter(generateStringOfLength(32), "pass");
//        request.addParameter(generateStringOfLength(33), "fail");
//        assertEquals(Collections.list(safeRequest.getParameterNames()).size(), 2);
//    }

//    @Test
//    public void testGetParameter() {
////testing Validator.HTTPParameterValue
//        MockHttpServletRequest request = new MockHttpServletRequest();
//        SecurityWrapperRequest safeRequest = new SecurityWrapperRequest(request);
//        request.addParameter("p1", "Alice");
//        request.addParameter("p2", "bob@alice.com");//mail-address from a submit-form
//        request.addParameter("p3", ESAPI.authenticator().generateStrongPassword());
//        request.addParameter("p4", new String(EncoderConstants.CHAR_PASSWORD_SPECIALS));
//        //TODO - I think this should fair request.addParameter("p5", "�������?����"); //some special characters from european languages;
//        request.addParameter("f1", "<SCRIPT SRC=http://ha.ckers.org/xss.js></SCRIPT>");
//        request.addParameter("f2", "<IMG SRC=&#106;&#97;&#118;&#97;&#115;&#99;&#114;&#105;&#112;&#116;&#58;&#97;&#108;&#101;&#114;&#116;&#40;&#39;&#88;&#83;&#83;&#39;&#41;>");
//        request.addParameter("f3", "<IMG SRC=&#106;&#97;&#118;&#97;&#115;&#99;&#114;&#105;&#112;&#116;&#58;&#97;&#108;&#101;&#114;&#116;&#40;&#39;&#88;&#83;&#83;&#39;&#41;>");
//        for (int i = 1; i <= 4; i++) {
//            assertTrue(safeRequest.getParameter("p" + i).equals(request.getParameter("p" + i)));
//        }
//        for (int i = 1; i <= 2; i++) {
//            boolean testResult = false;
//            try {
//                testResult = safeRequest.getParameter("f" + i).equals(request.getParameter("f" + i));
//            } catch (NullPointerException npe) {
//                //the test is this block SHOULD fail. a NPE is an acceptable failure state
//                testResult = false; //redundant, just being descriptive here
//            }
//            assertFalse(testResult);
//        }
//        assertNull(safeRequest.getParameter("e1"));
//
//        //This is revealing problems with Jeff's original SafeRequest
//        //mishandling of the AllowNull parameter. I'm adding a new Google code
//        //bug to track this.
//        //
//        //assertNotNull(safeRequest.getParameter("e1", false));
//    }

//    @Test
//    public void testGetCookies() {
////testing Validator.HTTPCookieName and Validator.HTTPCookieValue
//        MockHttpServletRequest request = new MockHttpServletRequest();
//        SecurityWrapperRequest safeRequest = new SecurityWrapperRequest(request);
////should support a base64-encode value
//        request.setCookie("p1", "34=VJhjv7jiDu7tsdLrQQ2KcUwpfWUM2_mBae6UA8ttk4wBHdxxQ-1IBxyCOn3LWE08SDhpnBcJ7N5Vze48F2t8a1R_hXt7PX1BvgTM0pn-T4JkqGTm_tlmV4RmU3GT-dgn");
//        request.setCookie("f1", "<A HREF=\"http://66.102.7.147/\">XSS</A>");
//        request.setCookie("load-balancing", "pass");
//        request.setCookie("'bypass", "fail");
//        Cookie[] cookies = safeRequest.getCookies();
//        assertEquals(cookies[0].getValue(), request.getCookies()[0].getValue());
//        assertEquals(cookies[1].getName(), request.getCookies()[2].getName());
//        assertTrue(cookies.length == 2);
//    }

//    @Test
//    public void testGetHeader() {
////testing Validator.HTTPHeaderValue
//        MockHttpServletRequest request = new MockHttpServletRequest();
//        SecurityWrapperRequest safeRequest = new SecurityWrapperRequest(request);
//        request.addHeader("p1", "login");
//        request.addHeader("f1", "<A HREF=\"http://0x42.0x0000066.0x7.0x93/\">XSS</A>");
//        request.addHeader("p2", generateStringOfLength(150));
//        request.addHeader("f2", generateStringOfLength(151));
//        assertEquals(safeRequest.getHeader("p1"), request.getHeader("p1"));
//        assertEquals(safeRequest.getHeader("p2"), request.getHeader("p2"));
//        assertFalse(safeRequest.getHeader("f1").equals(request.getHeader("f1")));
//        assertFalse(safeRequest.getHeader("f2").equals(request.getHeader("f2")));
//        assertNull(safeRequest.getHeader("p3"));
//    }

//    @Test
//    public void testGetHeaderNames() {
////testing Validator.HTTPHeaderName
//        MockHttpServletRequest request = new MockHttpServletRequest();
//        SecurityWrapperRequest safeRequest = new SecurityWrapperRequest(request);
//        request.addHeader("d-49653-p", "pass");
//        request.addHeader("<img ", "fail");
//        request.addHeader(generateStringOfLength(32), "pass");
//        request.addHeader(generateStringOfLength(33), "fail");
//        assertEquals(Collections.list(safeRequest.getHeaderNames()).size(), 2);
//    }

//    @Test
//    public void testGetQueryString() {
////testing Validator.HTTPQueryString
//        MockHttpServletRequest request = new MockHttpServletRequest();
//        SecurityWrapperRequest safeRequest = new SecurityWrapperRequest(request);
//        request.setQueryString("mail=bob@alice.com&passwd=" + new String(EncoderConstants.CHAR_PASSWORD_SPECIALS));// TODO, fix this + "&special=�����");
//        assertEquals(safeRequest.getQueryString(), request.getQueryString());
//        request.setQueryString("mail=<IMG SRC=\"jav\tascript:alert('XSS');\">");
//        assertFalse(safeRequest.getQueryString().equals(request.getQueryString()));
//        request.setQueryString("mail=bob@alice.com-passwd=johny");
//        assertTrue(safeRequest.getQueryString().equals(request.getQueryString()));
//        request.setQueryString("mail=bob@alice.com-passwd=johny&special"); //= is missing!
//        assertFalse(safeRequest.getQueryString().equals(request.getQueryString()));
//    }

//    @Test
//    public void testGetRequestURI() {
////testing Validator.HTTPURI
//        MockHttpServletRequest request = new MockHttpServletRequest();
//        SecurityWrapperRequest safeRequest = new SecurityWrapperRequest(request);
//        try {
//            request.setRequestURI("/app/page.jsp");
//        } catch (UnsupportedEncodingException ignored) {
//        }
//        assertEquals(safeRequest.getRequestURI(), request.getRequestURI());
//    }

    private String generateStringOfLength(int length) {
        StringBuilder longString = new StringBuilder();
        for (int i = 0; i < length; i++) {
            longString.append("a");
        }
        return longString.toString();
    }

    @Test
    public void testGetContextPath() {
        // Root Context Path ("")
        assertTrue(ESAPI.validator().isValidInput("HTTPContextPath", "", "HTTPContextPath", 512, true));
        // Deployed Context Path ("/context")
        assertTrue(ESAPI.validator().isValidInput("HTTPContextPath", "/context", "HTTPContextPath", 512, true));
        // Fail-case - URL Splitting
        assertFalse(ESAPI.validator().isValidInput("HTTPContextPath", "/\\nGET http://evil.com", "HTTPContextPath", 512, true));
    }
}
