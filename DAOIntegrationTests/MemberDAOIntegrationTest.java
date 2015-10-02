import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertTrue;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import library.daos.MemberHelper;
import library.daos.MemberMapDAO;
import library.entities.Member;
import library.interfaces.daos.IMemberDAO;
import library.interfaces.daos.IMemberHelper;
import library.interfaces.entities.IMember;

import org.junit.After;
import org.junit.Before;
import org.junit.Test;


public class MemberDAOIntegrationTest {
	
	private Map<Integer, IMember> memberMap;
	private IMemberHelper helper;
	private IMember borrower;
	private String fName;
	private String lName;
	private String contact;
	private String email;
	private int memberID;
	private MemberMapDAO memberDAO;

	@Before
	public void setUp() throws Exception {
		
		fName = "Sid";
		lName = "Bernardson";
		contact = "0401234567";
		email = "sbernadson@email.com";
		memberID = 77;
		borrower = new Member(fName, lName, contact, email, memberID);
		memberMap = new HashMap<Integer, IMember>();
		memberMap.put(memberID, borrower);
		helper = new MemberHelper();
		memberDAO = new MemberMapDAO(helper, memberMap);

	}

	@After
	public void tearDown() throws Exception {
		memberMap = null;
		helper = null;
		memberDAO = null;
		borrower = null;
	}

	@Test
	public void testMemberMapDAO() {
		memberDAO = new MemberMapDAO(helper);
		assertNotNull(helper);
		assertTrue(memberDAO instanceof IMemberDAO);
	}
	
	@Test (expected=IllegalArgumentException.class)
	public void testMemberMapDAONull() {
		memberDAO = new MemberMapDAO(null);
	}
	
	@Test
	public void testMemberMapDAOWithMap() {
		memberDAO = new MemberMapDAO(helper, memberMap);
		assertTrue(memberDAO instanceof IMemberDAO);
	}
	
	@Test (expected=IllegalArgumentException.class)
	public void testMemberMapDAOWithMapNull() {
		memberDAO = new MemberMapDAO(helper, null);
	}
	
	@Test
	public void testAddMember() {
		IMember borrower = memberDAO.addMember(fName, lName, contact, email);
		assertTrue(borrower instanceof IMember);
	}
	
	@Test (expected=IllegalArgumentException.class)
	public void testAddMemberBadParamFName() {
		memberDAO.addMember(null, lName, contact, email);
	}
	
	@Test (expected=IllegalArgumentException.class)
	public void testAddMemberBadParamLName() {
		memberDAO.addMember(fName, null, contact, email);
	}
	
	@Test (expected=IllegalArgumentException.class)
	public void testAddMemberBadParamContact() {
		memberDAO.addMember(fName, lName, null, email);
	}
	
	@Test (expected=IllegalArgumentException.class)
	public void testAddMemberBadParamEmail() {
		memberDAO.addMember(fName, lName, contact, null);
	}
	
	@Test 
	public void testGetMemberByID() {
		assertTrue(memberDAO.getMemberByID(memberID) == borrower);
	}
	
	@Test 
	public void testGetMemberByIDFalse() {
		int falseMemberID = 2;
		assertTrue(memberDAO.getMemberByID(falseMemberID) == null);
	}
	
	@Test
	public void testListMembers() {
		assertTrue(memberDAO.listMembers().contains(borrower));
	}
	
	@Test
	public void testListMembersFail() {
		IMember borrower2 = helper.makeMember("Tom", "Vallance", "0401098765", "tvallance@email.com", 199);
		assertFalse(memberDAO.listMembers().contains(borrower2));
	}
	
	@Test
	public void testFindMembersByLastName() {
		assertTrue(memberDAO.findMembersByLastName(lName).get(0) == borrower);
	}
	
	@Test (expected=IllegalArgumentException.class)
	public void testFindMembersByLastNameNull() {
		memberDAO.findMembersByLastName(null);
	}
	
	@Test
	public void testFindMembersByLastNameFalse() {
		String falseLName = "JacobsDatter";
		List<IMember> list = new ArrayList<IMember>(memberDAO.findMembersByLastName(falseLName));
		assertTrue(list.isEmpty());
	}
	
	@Test
	public void testFindMembersByEmailAddress() {
		assertTrue(memberDAO.findMembersByEmailAddress(email).get(0) == borrower);
	}
	
	@Test (expected=IllegalArgumentException.class)
	public void testFindMembersByEmailAddressNull() {
		memberDAO.findMembersByEmailAddress(null);
	}
	
	@Test
	public void testFindMembersByEmailAddressFalse() {
		String falseEmail = "ajacobsdatter@email.com";
		List<IMember> list = new ArrayList<IMember>(memberDAO.findMembersByEmailAddress(falseEmail));
		assertTrue(list.isEmpty());
	}
	
	@Test
	public void testFindMembersByNames() {
		List<IMember> list = new ArrayList<IMember>(memberDAO.findMembersByNames(fName, lName));
		assertTrue(list.contains(borrower));
	}
	
	@Test (expected=IllegalArgumentException.class)
	public void testFindMembersByNamesNullFirstName() {
		memberDAO.findMembersByNames(null, lName);
	}
	
	@Test (expected=IllegalArgumentException.class)
	public void testFindMembersByNamesNullLastName() {
		memberDAO.findMembersByNames(fName, null);
	}
	
	@Test
	public void testFindMembersByNamesFirstNameOnly() {
		String falseLName = "JacobsDatter";
		List<IMember> list = new ArrayList<IMember>(memberDAO.findMembersByNames(fName, falseLName));
		assertTrue(list.isEmpty());
	}
	
	@Test
	public void testFindMembersByNamesLastNameOnly() {
		String falseFName = "Ann";
		List<IMember> list = new ArrayList<IMember>(memberDAO.findMembersByNames(falseFName, lName));
		assertTrue(list.isEmpty());
	}
	
}
