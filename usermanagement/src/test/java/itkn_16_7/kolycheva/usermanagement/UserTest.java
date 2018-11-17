package itkn_16_7.kolycheva.usermanagement;

import static org.junit.Assert.*;

import java.util.Calendar;
import java.util.GregorianCalendar;

import org.junit.Before;
import org.junit.Test;

public class UserTest {

	
		private static final Long ID = 1L;
		
		//Test 1: test values of User's name
		private static final String FIRST_NAME = "Polina";
		private static final String LAST_NAME = "Kolycheva";
		private static final String FULL_NAME_ETALONE = "Kolycheva, Polina";
		
		private static final int YEAR_OF_BIRTH = 1999;
		
		//Test 2: birthday has been passed in this year, month > month_of_birth
		private static final int AGE_ETALONE_1 = 19;
		private static int MONTH_OF_BIRTH_1 = 10;
		private static final int DAY_OF_BIRTH_1 = 18;
		
		//Test 3: birthday hasn't been passed in this year, month < month_of_birth
		private static final int AGE_ETALONE_2 = 18;
		private static int MONTH_OF_BIRTH_2 = 12;
		private static final int DAY_OF_BIRTH_2 = 18;
		
		//Test 4: birthday has been passed in this year, month == month_of_birth, day > day_of_birth
		private static final int AGE_ETALONE_3 = 19;
		private static int MONTH_OF_BIRTH_3 = 11;	
		private static int DAY_OF_BIRTH_3 = 10;
		
		//Test 5: birthday has been passed in this year, month == month_of_birth, day == day_of_birth
		private static final int AGE_ETALONE_4 = 19;
		private static int MONTH_OF_BIRTH_4 = 11;	
		private static int DAY_OF_BIRTH_4 = 13;
		
		//Test 6: birthday hasn't been passed in this year, month == month_of_birth, day < day_of_birth
		private static final int AGE_ETALONE_5 = 18;
		private static int MONTH_OF_BIRTH_5 = 11;	
		private static int DAY_OF_BIRTH_5 = 18;
		
		//Test 7: birthday is the 31 of December
	    private static final int AGE_ETALONE_6 = 18;
	    private static final int MONTH_OF_BIRTH_6 = 12;
	    private static final int DAY_OF_BIRTH_6 = 31;

	    //Test 8: bBirthday is the 1 of January
	    private static final int AGE_ETALONE_7 = 19;
	    private static final int MONTH_OF_BIRTH_7 = 1;
	    private static final int DAY_OF_BIRTH_7 = 1;
		
		private User user;
		private Calendar calendar;
		
		@Before
		public void setUp() throws Exception {
			user = new User();
			setTestDate();
		}
		

		
		/**
		 * Set the test values of the month and day depending on the current date.
		 */
		void setTestDate() {
			Calendar calendar = Calendar.getInstance();
			int month = calendar.get(Calendar.MONTH);
			int day = calendar.get(Calendar.DAY_OF_MONTH);
			
			Calendar tmpCalendar = new GregorianCalendar(YEAR_OF_BIRTH, month, DAY_OF_BIRTH_1);
			tmpCalendar.add(Calendar.MONTH, -1);
			MONTH_OF_BIRTH_1 = tmpCalendar.get(Calendar.MONTH);
			
			tmpCalendar = new GregorianCalendar(YEAR_OF_BIRTH, month, DAY_OF_BIRTH_2);
			tmpCalendar.add(Calendar.MONTH, 1);
			MONTH_OF_BIRTH_2 = tmpCalendar.get(Calendar.MONTH);
			
			tmpCalendar = new GregorianCalendar(YEAR_OF_BIRTH, month, day);
			tmpCalendar.add(Calendar.DAY_OF_MONTH, -1);
			MONTH_OF_BIRTH_3 = tmpCalendar.get(Calendar.MONTH);
			DAY_OF_BIRTH_3 = tmpCalendar.get(Calendar.DAY_OF_MONTH);
			
			tmpCalendar = new GregorianCalendar(YEAR_OF_BIRTH, month, day);
			MONTH_OF_BIRTH_4 = tmpCalendar.get(Calendar.MONTH);
			DAY_OF_BIRTH_4 = tmpCalendar.get(Calendar.DAY_OF_MONTH);
			
			tmpCalendar = new GregorianCalendar(YEAR_OF_BIRTH, month, day);
			tmpCalendar.add(Calendar.DAY_OF_MONTH, 1);
			MONTH_OF_BIRTH_5 = tmpCalendar.get(Calendar.MONTH);
			DAY_OF_BIRTH_5 = tmpCalendar.get(Calendar.DAY_OF_MONTH);
		}
		
		/**
	    * Tests User's full name.
	    * <p>
	    * Expected result: FULL_NAME_ETALONE
	    */
		@Test
		public void testGetFullName() {
			user.setFirstName(FIRST_NAME);
			user.setLastName(LAST_NAME);
			assertEquals(FULL_NAME_ETALONE, user.getFullName());
		}
		
		/**
	    * Tests User's age when birthday passed in this year.
	    * <p>
	    * expected result: ETALONE_AGE_1
	    */
		@Test
		public void testGetAge_birthdayPassed() {
			calendar = Calendar.getInstance();
			calendar.set(YEAR_OF_BIRTH, MONTH_OF_BIRTH_1, DAY_OF_BIRTH_1);
			
			user.setDateOfBirth(calendar.getTime());
			assertEquals(AGE_ETALONE_1, user.getAge());
		}
		
		/**
		* Tests User's age when birthday will be in this year.
		* <p>
		* expected result: ETALONE_AGE_2
		*/
		@Test
		public void testGetAge_birthdayWillBe() {
			calendar = Calendar.getInstance();
			calendar.set(YEAR_OF_BIRTH, MONTH_OF_BIRTH_2, DAY_OF_BIRTH_2);
				
			user.setDateOfBirth(calendar.getTime());
			assertEquals(AGE_ETALONE_2, user.getAge());
		}
		
		/**
		* Tests User's age when birthday has been passed in this year, month == month_of_birth, day > day_of_birth
		* <p>
		* expected result: ETALONE_AGE_3
		*/
		@Test
		public void testGetAge_birthdayPassedThisMonth() {
			calendar = Calendar.getInstance();
			calendar.set(YEAR_OF_BIRTH, MONTH_OF_BIRTH_3, DAY_OF_BIRTH_3);
				
			user.setDateOfBirth(calendar.getTime());
			assertEquals(AGE_ETALONE_3, user.getAge());
		}
		
		/**
		* Tests User's age when birthday has been passed in this year, month == month_of_birth, day == day_of_birth
		* <p>
		* expected result: ETALONE_AGE_4
		*/
		@Test
		public void testGetAge_birthdayToday() {
			calendar = Calendar.getInstance();
			calendar.set(YEAR_OF_BIRTH, MONTH_OF_BIRTH_4, DAY_OF_BIRTH_4);
				
			user.setDateOfBirth(calendar.getTime());
			assertEquals(AGE_ETALONE_4, user.getAge());
		}
		
		/**
		* Tests User's age when birthday hasn't been passed in this year, month == month_of_birth, day < day_of_birth
		* <p>
		* expected result: ETALONE_AGE_5
		*/
		@Test
		public void testGetAge_birthdayWillBePassedThisMonth() {
			calendar = Calendar.getInstance();
			calendar.set(YEAR_OF_BIRTH, MONTH_OF_BIRTH_5, DAY_OF_BIRTH_5);
				
			user.setDateOfBirth(calendar.getTime());
			assertEquals(AGE_ETALONE_5, user.getAge());
		}
		
		/**
		* Tests User's age when birthday is the 31 of December
		* <p>
		* expected result: ETALONE_AGE_6
		*/
		@Test
		public void testGetAge_birthday31December() {
			calendar = Calendar.getInstance();
			calendar.set(YEAR_OF_BIRTH, MONTH_OF_BIRTH_6, DAY_OF_BIRTH_6);
				
			user.setDateOfBirth(calendar.getTime());
			assertEquals(AGE_ETALONE_6, user.getAge());
		}
		
		/**
		* Tests User's age when birthday is the 1 of January
		* <p>
		* expected result: ETALONE_AGE_7
		*/
		@Test
		public void testGetAge_birthday1January() {
			calendar = Calendar.getInstance();
			calendar.set(YEAR_OF_BIRTH, MONTH_OF_BIRTH_7, DAY_OF_BIRTH_7);
				
			user.setDateOfBirth(calendar.getTime());
			assertEquals(AGE_ETALONE_7, user.getAge());
		}
}

	




