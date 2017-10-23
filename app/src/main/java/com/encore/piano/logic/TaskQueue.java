package com.encore.piano.logic;

import java.util.LinkedList;
import java.util.Queue;

public class TaskQueue {
	
	public static Queue<EnumTask> queue = new LinkedList<EnumTask>();
	
	public static Queue<EnumTask> getQueue() {
		return queue;
	}

	public enum EnumTask {
		
		SendSignature("S"), 
		LoadConsignments("CL"),
		SyncConsignments("CS"),
		SyncMessages("M"),
		SyncMessagesLogoff("SML"),
		SendMessage("SM");
		
		private String taskName;
	 
		private EnumTask(String s) {
			taskName = s;
		}
	 
		public String getTaskName() {
			return taskName;
		}
	 
	}
}
