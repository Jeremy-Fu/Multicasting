package messagepasser;

public class Rule<T extends Message> {
	
	public static final String DROP = "DROP";
	public static final String DELAY = "DELAY";
	public static final String DUPLICATE = "DUPLICATE";
	
	private String action;
	private String src;
	private String dest;
	private String kind;
	private int seqNum;
	
	private Rule(Builder<T> ruleBuilder) {
		
		action = ruleBuilder.action;
		src = ruleBuilder.src;
		dest = ruleBuilder.dest;
		kind = ruleBuilder.kind;
		seqNum = ruleBuilder.seqNum;
	}
	
	public String getAction() {
		return action;
	}
	
	@Override 
	public boolean equals(Object obj) {
		if (obj == null) 
			return false;
		if (!(obj instanceof Rule))
            return false;
        if (obj == this)
            return true;
       
        @SuppressWarnings("unchecked")
		Rule<T> objRule = (Rule<T>)obj;
        String key1 = this.action    + this.src    + this.dest    + this.kind    + this.seqNum;
        String key2 = objRule.action + objRule.src + objRule.dest + objRule.kind + objRule.seqNum;
        return key1.equals(key2);
        
	}
	
	
	public boolean isSatisfied(T message) {
		
		if (src != null && !message.getSource().equals(src)) {
			return false;
		}
		
		if (dest != null && !message.getDest().equals(dest)) {
			return false;
		}
		
		if (kind != null && !message.getKind().equals(kind)) {
			return false;
		}
		
		if (seqNum != -1 && !(message.getNum() == seqNum)) {
			return false;
		}
		
		return true;
	}
	
	public static class Builder<K extends Message> {
		
		private String action;
		private String src;
		private String dest;
		private String kind;
		private int seqNum = -1;
		
		public Builder(String action) {
			action = action.trim().toUpperCase();
			if (action.equals(DROP)) {
				this.action = DROP;
			} else if (action.equals(DELAY)) {
				this.action = DELAY;
			} else if (action.equals(DUPLICATE)) {
				this.action = DUPLICATE;
			} else  {
				throw new IllegalArgumentException("The action[" + action +
						"] is not supported");
			}
		}
		
		public Builder<K> from(String src) {
			this.src = src;
			return this;
		}
		
		public Builder<K> to(String dest) {
			this.dest = dest;
			return this;
		}
		
		public Builder<K> kind(String kind) {
			this.kind = kind;
			return this;
		}
		
		public Builder<K> seqNum(int seqNum) {
			this.seqNum = seqNum;
			return this;
		}
		
		public Rule<K> build() {
			return new Rule<K>(this);
		}
		
	}
	
	
}
