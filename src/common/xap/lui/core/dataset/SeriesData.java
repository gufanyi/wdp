package xap.lui.core.dataset;

public class SeriesData {

	private MarkLineData[] markline_data = null;
	private MarkPointData[] markpoint_data = null;

	public MarkLineData[] getMarkline_data() {
		return markline_data;
	}

	public void setMarkline_data(MarkLineData[] markline_data) {
		this.markline_data = markline_data;
	}

	public MarkPointData[] getMarkpoint_data() {
		return markpoint_data;
	}

	public void setMarkpoint_data(MarkPointData[] markpoint_data) {
		this.markpoint_data = markpoint_data;
	}

}
