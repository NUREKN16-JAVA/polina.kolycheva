package itkn_16_7.kolycheva.usermanagement.web;

import itkn_16_7.kolycheva.usermanagement.User;

public class DetailsServletTest extends MockServletTestCase {

	 @Override
	    public void setUp() throws Exception {
	        super.setUp();
	        createServlet(DetailsServlet.class);
	    }

	    public void testBack() {
	        addRequestParameter("backButton", "Back");
	        User nullUser = (User) getWebMockObjectFactory().getMockSession().getAttribute("user");
	        assertNull("User must not exist in session scope", nullUser);
	    }
	}