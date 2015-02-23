package yamparser;

import static org.junit.Assert.*;

import java.util.ArrayList;
import java.util.List;

import messagepasser.Message;
import messagepasser.Rule;
import yamlparser.YamlParser;

public class YamlParserTest {

	@org.junit.Test
	public void testGetSendRules() throws Exception {
		
		YamlParser tester = new YamlParser("test/yamparser/config_test/config.yaml");
		List<Rule<Message>> parsedSendRules = tester.getSendRules();
		List<Rule<Message>> rules = new ArrayList<Rule<Message>>();
		
		Rule.Builder<Message> builder = new Rule.Builder<Message>("delay");
		rules.add(builder.from("Alice").to("Bob").kind("ACK").seqNum(2).build());
		
		builder = new Rule.Builder<Message>("delay");
		rules.add(builder.from("Alice").to("Charlie").seqNum(2).build());
		
		builder = new Rule.Builder<Message>("drop");
		rules.add(builder.from("Alice").to("Bob").kind("DROP").seqNum(4).build());
		
		assertEquals(parsedSendRules, rules);
		
	}
	
	@org.junit.Test
	public void testGetRecvRules() throws Exception {
		
		YamlParser tester = new YamlParser("test/yamparser/config_test/config.yaml");
		List<Rule<Message>> parsedSendRules = tester.getRecvRules();
		List<Rule<Message>> rules = new ArrayList<Rule<Message>>();
		
		Rule.Builder<Message> builder = new Rule.Builder<Message>("delay");
		rules.add(builder.from("Charlie").to("Dephnie").kind("ACK").seqNum(12).build());
		
		builder = new Rule.Builder<Message>("drop");
		rules.add(builder.from("Charlie").to("Alice").build());
		
		assertEquals(parsedSendRules, rules);
		
	}
	
}

//- action : delay     # 3rd message from Charlie that anyone
//src    : Charlie        # receives will be duplicated
//dest   : Dephnie
//kind   : ACK
//seqNum : 12
//- action : drop      # 3rd message from Charlie that anyone
//src    : Charlie        # receives will be duplicated
//dest   : Alice