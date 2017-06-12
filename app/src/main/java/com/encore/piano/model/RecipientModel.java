package com.encore.piano.model;

public class RecipientModel extends BaseModel {

	private String Username;
	
	public String getName() {
		return Username;
	}
	public void setName(String name) {
		Username = name;
	}
	
	@Override
	public String toString()
	{
		return getName();
	}
	
	
	public enum RecipientModelEnum
	{
		Name("UserName");
		
		public String Value;
		
		private RecipientModelEnum(String v){
			Value = v;
		}
	}
}
