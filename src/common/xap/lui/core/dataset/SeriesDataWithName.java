package xap.lui.core.dataset;

import com.alibaba.fastjson.JSON;

public class SeriesDataWithName extends SeriesData {

	private ChartDataWithName[] series_data = null;

	public ChartDataWithName[] getSeries_data() {
		return series_data;
	}

	public void setSeries_data(ChartDataWithName[] series_data) {
		this.series_data = series_data;
	}

	public void setSeries_data(String jsonStr) {
		ChartDataWithName[] result = JSON.parseObject(jsonStr, ChartDataWithName[].class);
		this.series_data = result;
	}

}
