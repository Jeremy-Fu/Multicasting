package messagepasser;

import static org.junit.Assert.*;

import org.junit.Before;


public class RuleTest {
	
//	private Rule tester;
//	
//	@Before
//	public void setup() {
//		
//	}
	
	
	@org.junit.Test
	public void illegalArgumentTest() {
		String ErrMsg = "The action[HELLO] is not supported";
		try {
			Rule.Builder<Message> builder = new Rule.Builder<Message>("hello");
		} catch (IllegalArgumentException e) {
			assertTrue("Illegal action", e.getMessage().equals(ErrMsg));
		}
		
	}
	
	@org.junit.Test
	public void getActionTest() {
		Rule.Builder<Message> builder = new Rule.Builder<Message>("delay");
		builder.from("alice").to("bob").kind("delay-kind").seqNum(2);
		
		Rule<Message> tester = builder.build();
		assertEquals(tester.getAction(), Rule.DELAY);
	}
	
	@org.junit.Test
	public void equalsTest() {
		Rule.Builder<Message> builder = new Rule.Builder<Message>("delay");
		builder.from("alice").to("bob").kind("delay-kind").seqNum(2);
		Rule<Message> tester1 = builder.build();
		
		Rule.Builder<Message> builder2 = new Rule.Builder<Message>("delay");
		builder2.from("alice").to("bob").kind("delay-kind").seqNum(2);
		Rule<Message> tester2 = builder2.build();
		assertTrue("same fileds", tester1.equals(tester2));
		
		Rule.Builder<Message> builder3 = new Rule.Builder<Message>("drop");
		builder3.from("alice").to("bob").kind("delay-kind").seqNum(2);
		Rule<Message> tester3 = builder3.build();
		assertFalse("different action filed", tester1.equals(tester3));
		
		Rule.Builder<Message> builder4 = new Rule.Builder<Message>("delay");
		builder4.from("alice1").to("charlie").kind("delay-kind").seqNum(2);
		Rule<Message> tester4 = builder4.build();
		assertFalse("different srouce filed", tester1.equals(tester4));
		
		Rule.Builder<Message> builder5 = new Rule.Builder<Message>("delay");
		builder5.from("alice").to("bob2").kind("delay-kind").seqNum(2);
		Rule<Message> tester5 = builder5.build();
		assertFalse("different filed", tester1.equals(tester5));
		
	}
	
	@org.junit.Test
	public void isSatisfiedTest() {
		Rule.Builder<Message> builder = new Rule.Builder<Message>("delay");
		builder.from("alice").to("bob").kind("delay-kind").seqNum(2);
		Rule<Message> tester1 = builder.build();
		Message message = new Message("bob", "delay-kind", null, false);
		message.set_source("alice");
		message.set_seqNum(2);
		assertTrue("Totally satisfied", tester1.isSatisfied(message));
		
		message.set_source("bob");
		assertFalse("source is not satisfied", tester1.isSatisfied(message));
		
		
		Rule.Builder<Message> builder2 = new Rule.Builder<Message>("delay");
		builder2.from("alice").kind("delay-kind").seqNum(2);
		Rule<Message> tester2 = builder2.build();
		
		Message message2 = new Message("charlie", "delay-kind", null, false);
		message2.set_source("alice");
		message2.set_seqNum(2);
		assertTrue("dest is wildcard", tester2.isSatisfied(message2));
		
		message2.set_seqNum(3);
		assertFalse("dest is wildcard, but seq is wrong", tester2.isSatisfied(message2));
		
		Rule.Builder<Message> builder3 = new Rule.Builder<Message>("delay");
		builder3.from("alice").to("bob").kind("delay-kind");
		Rule<Message> tester3 = builder3.build();
		
		Message message3 = new Message("bob", "delay-kind", null, false);
		message3.set_source("alice");
		message3.set_seqNum(5);
		
		assertTrue("seqNum is wildcard", tester3.isSatisfied(message3));
	}
}
