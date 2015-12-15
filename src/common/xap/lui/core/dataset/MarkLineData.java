package xap.lui.core.dataset;

public class MarkLineData {

	private MarkLineDataType type = null;
	private String name = null;

	public MarkLineDataType getType() {
		return type;
	}

	public void setType(MarkLineDataType type) {
		this.type = type;
	}

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}
	
	public static void main(String args[]){
		System.out.println(MarkLineDataType.average);
	}

	public enum MarkLineDataType {
		max("max"), min("min"), average("average");
		private String value = null;

		MarkLineDataType(String value) {
			this.value = value;
		}

		public String toString() {
			return value;
		}

		public static MarkLineDataType getValue(String value) {
			MarkLineDataType[] taskStats = MarkLineDataType.class.getEnumConstants();
			for (int i = 0; i < taskStats.length; i++) {
				if (taskStats[i].toString().equalsIgnoreCase(value)) {
					return taskStats[i];
				}
			}
			return null;
		}
	}

}
