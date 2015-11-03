/**
 * OWASP Enterprise Security API (ESAPI)
 * <p/>
 * This file is part of the Open Web Application Security Project (OWASP)
 * Enterprise Security API (ESAPI) project. For details, please see
 * <a href="http://www.owasp.org/index.php/ESAPI">http://www.owasp.org/index.php/ESAPI</a>.
 * <p/>
 * Copyright (c) 2007 - The OWASP Foundation
 * <p/>
 * The ESAPI is published by OWASP under the BSD license. You should read and accept the
 * LICENSE before you use, modify, and/or redistribute this software.
 *
 * @author Jeff Williams <a href="http://www.aspectsecurity.com">Aspect Security</a>
 * @created 2007
 */
package org.owasp.esapi.http;

import javax.servlet.RequestDispatcher;
import javax.servlet.ServletException;
import javax.servlet.ServletRequest;
import javax.servlet.ServletResponse;
import java.io.IOException;

/**
 *
 * @author jwilliams
 */
public class MockRequestDispatcher implements RequestDispatcher {

    /**
     *
     * @param request
     * @param response
     * @throws javax.servlet.ServletException
     * @throws java.io.IOException
     */
    public void forward(ServletRequest request, ServletResponse response) throws ServletException, IOException {
        System.out.println("Forwarding");
    }

    /**
     *
     * @param request
     * @param response
     * @throws javax.servlet.ServletException
     * @throws java.io.IOException
     */
    public void include(ServletRequest request, ServletResponse response) throws ServletException, IOException {
        System.out.println("Including");
    }
}


