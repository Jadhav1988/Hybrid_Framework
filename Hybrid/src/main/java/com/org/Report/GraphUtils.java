package com.org.Report;

import java.io.FileOutputStream;
import java.io.InputStream;
import java.io.OutputStream;
import java.net.URL;
import java.util.ArrayList;
import java.util.List;

import com.googlecode.charts4j.AxisLabels;
import com.googlecode.charts4j.AxisLabelsFactory;
import com.googlecode.charts4j.AxisStyle;
import com.googlecode.charts4j.AxisTextAlignment;
import com.googlecode.charts4j.BarChart;
import com.googlecode.charts4j.BarChartPlot;
import com.googlecode.charts4j.Color;
import com.googlecode.charts4j.Data;
import com.googlecode.charts4j.Fills;
import com.googlecode.charts4j.GCharts;
import com.googlecode.charts4j.LinearGradientFill;
import com.googlecode.charts4j.PieChart;
import com.googlecode.charts4j.Plots;
import com.googlecode.charts4j.Slice;

public class GraphUtils {

	/**
	 * draw pie graph
	 * 
	 * @param passTestCount
	 * @param failedTestCount
	 * @param skippedTestCount
	 * @param chatTitle
	 * @return
	 */
	public String drawPieChart(int passTestCount, int failedTestCount,
			int skippedTestCount, String chatTitle) {

		Slice s1 = Slice.newSlice(passTestCount, Color.FORESTGREEN, "Passed: "
				+ passTestCount);
		Slice s2 = Slice.newSlice(failedTestCount, Color.MAROON, "Failed: "
				+ failedTestCount);
		Slice s3 = Slice.newSlice(skippedTestCount, Color.GRAY, "Skipped: "
				+ skippedTestCount);

		PieChart pieChart = GCharts.newPieChart(s1, s2, s3);
		pieChart.setTitle(chatTitle, Color.ROYALBLUE, 15);
		pieChart.setSize(720, 360);
		pieChart.setThreeD(true);

		return pieChart.toURLString();
	}

	/**
	 * create bar graph
	 * 
	 * @param passTestCount
	 * @param failTestCount
	 * @param skipTestCount
	 * @param modules
	 * @param chartTitle
	 * @param totalTestInModules
	 * @return
	 */
	public String drawBarGraph(double[] passTestCount, double[] failTestCount,
			double[] skipTestCount, List<String> modules, String chartTitle,
			int[] totalTestInModules) {

		double passPercentage[] = new double[totalTestInModules.length];
		double failPercentage[] = new double[totalTestInModules.length];
		double skipPercentage[] = new double[totalTestInModules.length];

		for (int i = 0; i < passTestCount.length; i++) {
			passPercentage[i] = getPercentage(totalTestInModules[i],
					passTestCount[i]);
		}

		for (int i = 0; i < failTestCount.length; i++) {
			failPercentage[i] = getPercentage(totalTestInModules[i],
					failTestCount[i]);
		}

		for (int i = 0; i < skipTestCount.length; i++) {
			skipPercentage[i] = getPercentage(totalTestInModules[i],
					skipTestCount[i]);
		}

		BarChartPlot passTests = Plots.newBarChartPlot(
				Data.newData(passPercentage), Color.FORESTGREEN);
		BarChartPlot failTests = Plots.newBarChartPlot(
				Data.newData(failPercentage), Color.MAROON);
		BarChartPlot skippedTests = Plots.newBarChartPlot(
				Data.newData(skipPercentage), Color.GRAY);

		// Instantiating chart.
		BarChart chart = GCharts
				.newBarChart(passTests, failTests, skippedTests);

		// Defining axis info and styles
		AxisStyle axisStyle = AxisStyle.newAxisStyle(Color.BLACK, 15,
				AxisTextAlignment.CENTER);
		AxisLabels tests = AxisLabelsFactory.newAxisLabels("Test % ", 50.0);
		AxisLabels p = AxisLabelsFactory.newAxisLabels("Pass: "
				+ getValues(passTestCount), 50.0);
		AxisLabels f = AxisLabelsFactory.newAxisLabels("Fail: "
				+ getValues(failTestCount), 50.0);
		AxisLabels s = AxisLabelsFactory.newAxisLabels("Skipped: "
				+ getValues(skipTestCount), 50.0);

		tests.setAxisStyle(axisStyle);
		AxisLabels module = AxisLabelsFactory.newAxisLabels("Modules", 50.0);
		module.setAxisStyle(axisStyle);

		chart.addXAxisLabels(AxisLabelsFactory
				.newNumericRangeAxisLabels(0, 100));
		chart.addYAxisLabels(AxisLabelsFactory.newAxisLabels(modules));
		chart.addYAxisLabels(module);
		chart.addXAxisLabels(tests);

		chart.addXAxisLabels(p);
		chart.addXAxisLabels(f);
		chart.addXAxisLabels(s);
		chart.setSize(500, 600);
		chart.setBarWidth(BarChart.AUTO_RESIZE);
		chart.setSpaceWithinGroupsOfBars(5);
		chart.setDataStacked(true);
		chart.setHorizontal(true);
		chart.setTitle(chartTitle, Color.ROYALBLUE, 18);
		chart.setTransparency(100);
		chart.setGrid(100, 100, 30, 20);
		chart.setBackgroundFill(Fills.newSolidFill(Color.ALICEBLUE));
		LinearGradientFill fill = Fills.newLinearGradientFill(0,
				Color.LAVENDER, 100);
		fill.addColorAndOffset(Color.BLACK, 0);
		chart.setAreaFill(fill);

		return chart.toURLString();
	}

	/**
	 * get values from the list
	 * 
	 * @param values
	 * @return
	 */
	public List<Integer> getValues(double[] values) {
		List<Integer> number = new ArrayList<>();
		for (double value : values) {
			number.add((int) value);
		}
		return number;
	}

	/**
	 * get percentage
	 * 
	 * @param totalTestCase
	 * @param testCount
	 * @return
	 */
	public double getPercentage(double totalTestCase, double testCount) {
		return (testCount * 100) / totalTestCase;
	}

	public void saveImage(String imageUrl, String destinationFile) {
		try {
			URL url = new URL(imageUrl);
			InputStream is = url.openStream();
			OutputStream os = new FileOutputStream(destinationFile);

			byte[] b = new byte[2048];
			int length;

			while ((length = is.read(b)) != -1) {
				os.write(b, 0, length);
			}

			is.close();
			os.close();
		} catch (Exception e) {
			e.printStackTrace();
		}

	}

}
