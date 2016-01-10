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

import static org.junit.Assert.fail;

import java.io.IOException;
import java.util.Locale;

import org.junit.Test;

/**
 * Created by steven on 03/11/15.
 */
public class ESAPIValidatorTest {

	@Test
	public void testIsValidCreditCard() throws ValidationException {
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
	public void testIsValidDate() throws Exception {
		System.out.println("getValidDate");
		Locale.setDefault(Locale.ENGLISH);
		Validators.DATE_VALIDATOR.validate("01-01-1970");
		Validators.DATE_VALIDATOR.setDatePattern("MMMM DD, YYYY");
		Validators.DATE_VALIDATOR.validate("June 23, 1967");
		try {
			Validators.DATE_VALIDATOR.validate("2015-12-2");
			fail("Exception should be thrown");
		} catch (ValidationException ve) {
		}
	}

	@Test
	public void testIsValidDirectoryPath() throws Exception {
		boolean isWindows = (System.getProperty("os.name").indexOf("Windows") != -1) ? true
				: false;
		if (isWindows) {
			try {
				Validators.FILE_VALIDATOR.validate("c:\\ridiculous");
				fail("Exception should be thrown");
			} catch (ValidationException ve) {
			}
			try {
				Validators.FILE_VALIDATOR.validate("c:\\temp\\..\\Windows");
				fail("Exception should be thrown");
			} catch (ValidationException ve) {
			}
			Validators.FILE_VALIDATOR.validate("c:\\");
			Validators.FILE_VALIDATOR.validate("c:\\Windows\\temp");
			Validators.FILE_VALIDATOR
					.validate("c:\\Windows\\System32\\cmd.exe");
			try {
				Validators.FILE_VALIDATOR.validate("/"); // Root directory
				fail("Exception should be thrown");
			} catch (ValidationException ve) {
			}
			try {
				Validators.FILE_VALIDATOR.validate("/bin"); // Always exist
															// directory
				fail("Exception should be thrown");
			} catch (ValidationException ve) {
			}
		} else {
			try {
				Validators.FILE_VALIDATOR.validate("c:\\ridiculous");
				fail("Exception should be thrown");
			} catch (ValidationException ve) {
			}
			try {
				Validators.FILE_VALIDATOR.validate("c:\\temp\\..\\etc");
				fail("Exception should be thrown");
			} catch (ValidationException ve) {
			}
			try {
				Validators.FILE_VALIDATOR.validate("c:\\");
				fail("Exception should be thrown");
			} catch (ValidationException ve) {
			}
			try {
				Validators.FILE_VALIDATOR.validate("c:\\Windows\\temp");
				fail("Exception should be thrown");
			} catch (ValidationException ve) {
			}
			try {
				Validators.FILE_VALIDATOR
						.validate("c:\\Windows\\System32\\cmd.exe");
				fail("Exception should be thrown");
			} catch (ValidationException ve) {
			}
			// Unix specific paths should pass
			Validators.FILE_VALIDATOR.validate("/"); // Root directory
			Validators.FILE_VALIDATOR.validate("/bin"); // Always exist
														// directory

			// Unix specific paths that should not exist or work
			try {
				Validators.FILE_VALIDATOR.validate("/bin/sh");
				fail("Exception should be thrown");
			} catch (ValidationException ve) {
			}
			try {
				Validators.FILE_VALIDATOR.validate("/etc/ridiculous");
				fail("Exception should be thrown");
			} catch (ValidationException ve) {
			}
			try {
				Validators.FILE_VALIDATOR.validate("/tmp/../etc");
				fail("Exception should be thrown");
			} catch (ValidationException ve) {
			}
		}
	}

	//
	// @Test
	// public void testGetValidInput() {
	//
	// }
	//
	// @Test
	// public void testGetValidInteger() {
	//
	// }
	//
	// @Test
	// public void testGetValidNumber() {
	//
	// }
	//
	// @Test
	// public void testGetValidRedirectLocation() {
	//
	// }

	@Test
	public void testIsValidDouble() throws ValidationException {
		Validators.NUMBER_VALIDATOR.setLocale("en-GB");
		Validators.NUMBER_VALIDATOR.validate(1.0);
		Validators.NUMBER_VALIDATOR.validate("1", "0", "10", false);

		try {
			Validators.NUMBER_VALIDATOR.validate("-4", "1", "10", false);
			fail("Exception should be thrown");
		} catch (ValidationException ve) {
		}

		Validators.NUMBER_VALIDATOR.validate("-4", "-10", "10", false);
		// Validators.NUMBER_VALIDATOR.validate(null, "-10", "10", true);
		// testing null value
		// assertTrue(ESAPIValidator.getInstance().isValidDouble("test3", null,
		// -10, 10, true, errors));
		// assertTrue(errors.size() == 1);
		// assertFalse(ESAPIValidator.getInstance().isValidDouble("test4", null,
		// -10, 10, false, errors));
		// assertTrue(errors.size() == 2);
		// testing empty string
		// Validators.NUMBER_VALIDATOR.validate("", -10, 10, true);
		// assertTrue(errors.size() == 2);
		// Validators.NUMBER_VALIDATOR.validate("", -10, 10, false);
		// assertTrue(errors.size() == 3);
		// testing improper range
		try {
			Validators.NUMBER_VALIDATOR.validate("50.0", "10", "-10", false);
			fail("Exception should be thrown");
		} catch (ValidationException ve) {
		}
		// assertTrue(errors.size() == 4);
		// testing non-integers
		Validators.NUMBER_VALIDATOR.validate("4.3214", -10, 10, true);
		// assertTrue(errors.size() == 4);
		Validators.NUMBER_VALIDATOR.validate("-1.65", -10, 10, true);
		// assertTrue(errors.size() == 4);
		// other testing
		Validators.NUMBER_VALIDATOR.validate("4", 1, 10, false);
		// assertTrue(errors.size() == 4);
		Validators.NUMBER_VALIDATOR.validate("400", 1, 10000, false);
		// assertTrue(errors.size() == 4);
		Validators.NUMBER_VALIDATOR.validate("400000000", 1, 400000000, false);
		// assertTrue(errors.size() == 4);
		try {
			Validators.NUMBER_VALIDATOR.validate("4000000000000", 1, 10000,
					false);
			fail("Exception should be thrown");
		} catch (ValidationException ve) {
		}
		try {
			// assertTrue(errors.size() == 5);
			Validators.NUMBER_VALIDATOR.validate("alsdkf", 10, 10000, false);
			fail("Exception should be thrown");
		} catch (ValidationException ve) {
		}
		try {
			// assertTrue(errors.size() == 6);
			Validators.NUMBER_VALIDATOR.validate("--10", 10, 10000, false);
			fail("Exception should be thrown");
		} catch (ValidationException ve) {
		}
		try {
			// assertTrue(errors.size() == 7);
			Validators.NUMBER_VALIDATOR.validate("14.1414234x", 10, 10000,
					false);
			fail("Exception should be thrown");
		} catch (ValidationException ve) {
		}
		try {
			// assertTrue(errors.size() == 8);
			Validators.NUMBER_VALIDATOR.validate("Infinity", 10, 10000, false);
			fail("Exception should be thrown");
		} catch (ValidationException ve) {
		}
		try {
			// assertTrue(errors.size() == 9);
			Validators.NUMBER_VALIDATOR.validate("-Infinity", 10, 10000, false);
			fail("Exception should be thrown");
		} catch (ValidationException ve) {
		}
		try {
			// assertTrue(errors.size() == 10);
			Validators.NUMBER_VALIDATOR.validate("NaN", 10, 10000, false);
			fail("Exception should be thrown");
		} catch (ValidationException ve) {
		}
		try {
			// assertTrue(errors.size() == 11);
			Validators.NUMBER_VALIDATOR.validate("-NaN", 10, 10000, false);
			fail("Exception should be thrown");
		} catch (ValidationException ve) {
		}
		try {
			// assertTrue(errors.size() == 12);
			Validators.NUMBER_VALIDATOR.validate("+NaN", 10, 10000, false);
			fail("Exception should be thrown");
		} catch (ValidationException ve) {
		}
		// assertTrue(errors.size() == 13);
		Validators.NUMBER_VALIDATOR.validate("1e-6", -999999999, 999999999,
				false);
		// assertTrue(errors.size() == 13);
		Validators.NUMBER_VALIDATOR.validate("-1e-6", -999999999, 999999999,
				false);
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
	public void testIsValidInteger() throws ValidationException {
		Validators.NUMBER_VALIDATOR.validate(1);
		Validators.NUMBER_VALIDATOR.validate("1", "0", "10", false);

		try {
			Validators.NUMBER_VALIDATOR.validate("-4", "1", "10", false);
			fail("Exception should be thrown");
		} catch (ValidationException ve) {
		}

		Validators.NUMBER_VALIDATOR.validate("-4", "-10", "10", false);
		// Validators.NUMBER_VALIDATOR.validate(null, "-10", "10", true);
		// testing null value
		// assertTrue(ESAPIValidator.getInstance().isValidDouble("test3", null,
		// -10, 10, true, errors));
		// assertTrue(errors.size() == 1);
		// assertFalse(ESAPIValidator.getInstance().isValidDouble("test4", null,
		// -10, 10, false, errors));
		// assertTrue(errors.size() == 2);
		// testing empty string
		// Validators.NUMBER_VALIDATOR.validate("", -10, 10, true);
		// assertTrue(errors.size() == 2);
		// Validators.NUMBER_VALIDATOR.validate("", -10, 10, false);
		// assertTrue(errors.size() == 3);
		// testing improper range
		try {
			Validators.NUMBER_VALIDATOR.validate("50.0", "10", "-10", false);
			fail("Exception should be thrown");
		} catch (ValidationException ve) {
		}
		// assertTrue(errors.size() == 4);
		// testing non-integers
		Validators.NUMBER_VALIDATOR.validate("4.3214", -10, 10, true);
		// assertTrue(errors.size() == 4);
		Validators.NUMBER_VALIDATOR.validate("-1.65", -10, 10, true);
		// assertTrue(errors.size() == 4);
		// other testing
		Validators.NUMBER_VALIDATOR.validate("4", 1, 10, false);
		// assertTrue(errors.size() == 4);
		Validators.NUMBER_VALIDATOR.validate("400", 1, 10000, false);
		// assertTrue(errors.size() == 4);
		Validators.NUMBER_VALIDATOR.validate("400000000", 1, 400000000, false);
		// assertTrue(errors.size() == 4);
		try {
			Validators.NUMBER_VALIDATOR.validate("4000000000000", 1, 10000,
					false);
			fail("Exception should be thrown");
		} catch (ValidationException ve) {
		}
		try {
			// assertTrue(errors.size() == 5);
			Validators.NUMBER_VALIDATOR.validate("alsdkf", 10, 10000, false);
			fail("Exception should be thrown");
		} catch (ValidationException ve) {
		}
		try {
			// assertTrue(errors.size() == 6);
			Validators.NUMBER_VALIDATOR.validate("--10", 10, 10000, false);
			fail("Exception should be thrown");
		} catch (ValidationException ve) {
		}
		try {
			// assertTrue(errors.size() == 7);
			Validators.NUMBER_VALIDATOR.validate("14.1414234x", 10, 10000,
					false);
			fail("Exception should be thrown");
		} catch (ValidationException ve) {
		}
		try {
			// assertTrue(errors.size() == 8);
			Validators.NUMBER_VALIDATOR.validate("Infinity", 10, 10000, false);
			fail("Exception should be thrown");
		} catch (ValidationException ve) {
		}
		try {
			// assertTrue(errors.size() == 9);
			Validators.NUMBER_VALIDATOR.validate("-Infinity", 10, 10000, false);
			fail("Exception should be thrown");
		} catch (ValidationException ve) {
		}
		try {
			// assertTrue(errors.size() == 10);
			Validators.NUMBER_VALIDATOR.validate("NaN", 10, 10000, false);
			fail("Exception should be thrown");
		} catch (ValidationException ve) {
		}
		try {
			// assertTrue(errors.size() == 11);
			Validators.NUMBER_VALIDATOR.validate("-NaN", 10, 10000, false);
			fail("Exception should be thrown");
		} catch (ValidationException ve) {
		}
		try {
			// assertTrue(errors.size() == 12);
			Validators.NUMBER_VALIDATOR.validate("+NaN", 10, 10000, false);
			fail("Exception should be thrown");
		} catch (ValidationException ve) {
		}
		// assertTrue(errors.size() == 13);
		Validators.NUMBER_VALIDATOR.validate("1e-6", -999999999, 999999999,
				false);
		// assertTrue(errors.size() == 13);
		Validators.NUMBER_VALIDATOR.validate("-1e-6", -999999999, 999999999,
				false);
	}

	@Test
	public void testIsValidListItem() {

	}

	@Test
	public void testIsValidNumber() throws ValidationException {
		Validators.NUMBER_VALIDATOR.validate(1);
		Validators.NUMBER_VALIDATOR.validate("1", "0", "10", false);

		try {
			Validators.NUMBER_VALIDATOR.validate("-4", "1", "10", false);
			fail("Exception should be thrown");
		} catch (ValidationException ve) {
		}

		Validators.NUMBER_VALIDATOR.validate("-4", "-10", "10", false);
		// Validators.NUMBER_VALIDATOR.validate(null, "-10", "10", true);
		// testing null value
		// assertTrue(ESAPIValidator.getInstance().isValidDouble("test3", null,
		// -10, 10, true, errors));
		// assertTrue(errors.size() == 1);
		// assertFalse(ESAPIValidator.getInstance().isValidDouble("test4", null,
		// -10, 10, false, errors));
		// assertTrue(errors.size() == 2);
		// testing empty string
		// Validators.NUMBER_VALIDATOR.validate("", -10, 10, true);
		// assertTrue(errors.size() == 2);
		// Validators.NUMBER_VALIDATOR.validate("", -10, 10, false);
		// assertTrue(errors.size() == 3);
		// testing improper range
		try {
			Validators.NUMBER_VALIDATOR.validate("50.0", "10", "-10", false);
			fail("Exception should be thrown");
		} catch (ValidationException ve) {
		}
		// assertTrue(errors.size() == 4);
		// testing non-integers
		Validators.NUMBER_VALIDATOR.validate("4.3214", -10, 10, true);
		// assertTrue(errors.size() == 4);
		Validators.NUMBER_VALIDATOR.validate("-1.65", -10, 10, true);
		// assertTrue(errors.size() == 4);
		// other testing
		Validators.NUMBER_VALIDATOR.validate("4", 1, 10, false);
		// assertTrue(errors.size() == 4);
		Validators.NUMBER_VALIDATOR.validate("400", 1, 10000, false);
		// assertTrue(errors.size() == 4);
		Validators.NUMBER_VALIDATOR.validate("400000000", 1, 400000000, false);
		// assertTrue(errors.size() == 4);
		try {
			Validators.NUMBER_VALIDATOR.validate("4000000000000", 1, 10000,
					false);
			fail("Exception should be thrown");
		} catch (ValidationException ve) {
		}
		try {
			// assertTrue(errors.size() == 5);
			Validators.NUMBER_VALIDATOR.validate("alsdkf", 10, 10000, false);
			fail("Exception should be thrown");
		} catch (ValidationException ve) {
		}
		try {
			// assertTrue(errors.size() == 6);
			Validators.NUMBER_VALIDATOR.validate("--10", 10, 10000, false);
			fail("Exception should be thrown");
		} catch (ValidationException ve) {
		}
		try {
			// assertTrue(errors.size() == 7);
			Validators.NUMBER_VALIDATOR.validate("14.1414234x", 10, 10000,
					false);
			fail("Exception should be thrown");
		} catch (ValidationException ve) {
		}
		try {
			// assertTrue(errors.size() == 8);
			Validators.NUMBER_VALIDATOR.validate("Infinity", 10, 10000, false);
			fail("Exception should be thrown");
		} catch (ValidationException ve) {
		}
		try {
			// assertTrue(errors.size() == 9);
			Validators.NUMBER_VALIDATOR.validate("-Infinity", 10, 10000, false);
			fail("Exception should be thrown");
		} catch (ValidationException ve) {
		}
		try {
			// assertTrue(errors.size() == 10);
			Validators.NUMBER_VALIDATOR.validate("NaN", 10, 10000, false);
			fail("Exception should be thrown");
		} catch (ValidationException ve) {
		}
		try {
			// assertTrue(errors.size() == 11);
			Validators.NUMBER_VALIDATOR.validate("-NaN", 10, 10000, false);
			fail("Exception should be thrown");
		} catch (ValidationException ve) {
		}
		try {
			// assertTrue(errors.size() == 12);
			Validators.NUMBER_VALIDATOR.validate("+NaN", 10, 10000, false);
			fail("Exception should be thrown");
		} catch (ValidationException ve) {
		}
		// assertTrue(errors.size() == 13);
		Validators.NUMBER_VALIDATOR.validate("1e-6", -999999999, 999999999,
				false);
		// assertTrue(errors.size() == 13);
		Validators.NUMBER_VALIDATOR.validate("-1e-6", -999999999, 999999999,
				false);
	}

}
