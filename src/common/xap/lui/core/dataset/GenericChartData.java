package xap.lui.core.dataset;

import javax.xml.bind.annotation.XmlRootElement;

import xap.lui.core.comps.ViewElement;
@XmlRootElement(name="GenericChartData")
public class GenericChartData extends ViewElement {
	private static final long serialVersionUID = -1201468558451904558L;
	private String[] legend_data = null;
	private String[] xAxis_data = null;
	private String[] yAxis_data = null;
	private SeriesData[] series_data = null;
	public String[] getLegend_data() {
		return legend_data;
	}
	public void setLegend_data(String[] legend_data) {
		this.legend_data = legend_data;
	}
	public String[] getxAxis_data() {
		return xAxis_data;
	}
	public void setxAxis_data(String[] xAxis_data) {
		this.xAxis_data = xAxis_data;
	}
	public String[] getyAxis_data() {
		return yAxis_data;
	}
	public void setyAxis_data(String[] yAxis_data) {
		this.yAxis_data = yAxis_data;
	}
	public SeriesData[] getSeries_data() {
		return series_data;
	}
	public void setSeries_data(SeriesData[] series_data) {
		this.series_data = series_data;
	}
	
	

}
