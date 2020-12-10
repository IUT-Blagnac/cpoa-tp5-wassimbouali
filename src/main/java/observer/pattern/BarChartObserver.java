package observer.pattern;

import java.awt.Color;
import java.awt.Dimension;
import java.awt.Graphics;
import java.util.Vector;

import javax.swing.JPanel;
import javax.swing.JSlider;

import observer.CourseRecord;
import observer.LayoutConstants;

/**
 * This class represents a bar chart view of a vector of data. Uses the Observer
 * pattern.
 */
@SuppressWarnings("serial")
public class BarChartObserver extends JPanel implements Observer {
	/**
	 * Creates a BarChartObserver object
	 * 
	 * @param data
	 *            a CourseData object to observe
	 */
	public BarChartObserver(CourseData data) {
		data.attach(this);
		this.courseData = data.getUpdate();
		this.setPreferredSize(new Dimension(2 * LayoutConstants.xOffset
				+ (LayoutConstants.barSpacing + LayoutConstants.barWidth)
				* this.courseData.size(), LayoutConstants.graphHeight + 2
				* LayoutConstants.yOffset));
		this.setBackground(Color.white);
	}

	public void paint(Graphics g) {
		super.paint(g);
		int radius = 100;
		int data[] = new int [courseData.size()];
		//first compute the total number of students
		double total = 0.0;
		for (int j = 0; j < data.length; j++) {
			data[j]= courseData.get(j).getNumOfStudents();
			total += data[j];
		}
		//if total == 0 nothing to draw
		if (total != 0) {
			double startAngle = 0.0;
			for (int i = 0; i < data.length; i++) {
				double ratio = (data[i] / total) * 360.0;
				//draw the arc
				g.setColor(LayoutConstants.courseColours[i%LayoutConstants.courseColours.length]);
				g.fillArc(LayoutConstants.xOffset, LayoutConstants.yOffset + 300, 2 * radius, 2 * radius, (int) startAngle, (int) ratio);
				startAngle += ratio;
				
				LayoutConstants.paintBarChartOutline(g, courseData.size());
				for (int i1 = 0; i1 < courseData.size(); i1++) {
					CourseRecord record = courseData.elementAt(i1);
					g.setColor(LayoutConstants.courseColours[i1]);
					g.fillRect(
							LayoutConstants.xOffset + (i1 + 1)
									* LayoutConstants.barSpacing + i1
									* LayoutConstants.barWidth, LayoutConstants.yOffset
									+ LayoutConstants.graphHeight
									- LayoutConstants.barHeight + 2
									* (LayoutConstants.maxValue - record.getNumOfStudents()),
							LayoutConstants.barWidth, 2 * record.getNumOfStudents());
					g.setColor(Color.red);
					g.drawString(record.getName(), 
							LayoutConstants.xOffset + (i1 + 1)
									* LayoutConstants.barSpacing + i1
									* LayoutConstants.barWidth, LayoutConstants.yOffset
									+ LayoutConstants.graphHeight + 20);
				
			}
		}
	}
}

	/**
	 * Informs this observer that the observed CourseData object has changed
	 * 
	 * @param o
	 *            the observed CourseData object that has changed
	 */
	public void update(Observable o) {
		CourseData data = (CourseData) o;
		this.courseData = data.getUpdate();

		this.setPreferredSize(new Dimension(2 * LayoutConstants.xOffset
				+ (LayoutConstants.barSpacing + LayoutConstants.barWidth)
				* this.courseData.size(), LayoutConstants.graphHeight + 2
				* LayoutConstants.yOffset));
		this.revalidate();
		this.repaint();
	}

	private Vector<CourseRecord> courseData;
}