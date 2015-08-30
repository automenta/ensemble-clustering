/**
 * Copyright (c) 2013 Oculus Info Inc.
 * http://www.oculusinfo.com/
 *
 * Released under the MIT License.
 *
 * Permission is hereby granted, free of charge, to any person obtaining a copy of
 * this software and associated documentation files (the "Software"), to deal in
 * the Software without restriction, including without limitation the rights to
 * use, copy, modify, merge, publish, distribute, sublicense, and/or sell copies
 * of the Software, and to permit persons to whom the Software is furnished to do
 * so, subject to the following conditions:

 * The above copyright notice and this permission notice shall be included in all
 * copies or substantial portions of the Software.

 * THE SOFTWARE IS PROVIDED "AS IS", WITHOUT WARRANTY OF ANY KIND, EXPRESS OR
 * IMPLIED, INCLUDING BUT NOT LIMITED TO THE WARRANTIES OF MERCHANTABILITY,
 * FITNESS FOR A PARTICULAR PURPOSE AND NONINFRINGEMENT. IN NO EVENT SHALL THE
 * AUTHORS OR COPYRIGHT HOLDERS BE LIABLE FOR ANY CLAIM, DAMAGES OR OTHER
 * LIABILITY, WHETHER IN AN ACTION OF CONTRACT, TORT OR OTHERWISE, ARISING FROM,
 * OUT OF OR IN CONNECTION WITH THE SOFTWARE OR THE USE OR OTHER DEALINGS IN THE
 * SOFTWARE.
 */
package com.oculusinfo.ml.unsupervised;

import com.oculusinfo.ml.DataSet;
import com.oculusinfo.ml.Instance;
import com.oculusinfo.ml.feature.numeric.NumericVectorFeature;
import com.oculusinfo.ml.feature.numeric.centroid.MeanNumericVectorCentroid;
import com.oculusinfo.ml.feature.numeric.distance.EuclideanDistance;
import com.oculusinfo.ml.unsupervised.cluster.Cluster;
import com.oculusinfo.ml.unsupervised.cluster.ClusterResult;
import com.oculusinfo.ml.unsupervised.cluster.kmeans.KMeans;

import javax.swing.*;
import java.awt.*;
import java.awt.geom.Ellipse2D;
import java.util.Random;

public class TestKMeans extends JFrame {

	/**
	 * 
	 */
	private static final long serialVersionUID = -9073044772934024066L;

	/**
	 * @param args
	 */
	public static void main(String[] args) {
		DataSet ds = new DataSet();
		
		Random rnd = new Random();
		
		// randomly generate a dataset of lat, lon points
		for (int i = 0; i < 100000; i ++) {
			Instance inst = new Instance();
			NumericVectorFeature v = new NumericVectorFeature("point");
		
			double x = rnd.nextDouble() * 400.0;
			double y = rnd.nextDouble() * 400.0;
			v.setValue( new double[] { x, y } );
		
			inst.addFeature(v);
			ds.add(inst);
		}
		
		KMeans clusterer = new KMeans(4, 10, false);
		clusterer.registerFeatureType(
				"point", 
				MeanNumericVectorCentroid.class, 
				new EuclideanDistance(1.0));
		
		final ClusterResult clusters = clusterer.doCluster(ds);
		
//		System.out.println(clusters);
		
		final Color[] colors = {Color.red, Color.blue, Color.green, Color.magenta, Color.yellow};
		
		TestKMeans t = new TestKMeans();
        t.add(new JComponent() {
			private static final long serialVersionUID = -2708612400626081243L;

			@Override
			public void paintComponent(Graphics g) {
                Graphics2D g2 = (Graphics2D) g;
                g2.setRenderingHint(RenderingHints.KEY_ANTIALIASING,
                                    RenderingHints.VALUE_ANTIALIAS_ON);
                
                int color = 0;
                for (Cluster cluster : clusters) {
                	for (Instance inst : cluster.getMembers()) {
                		g.setColor ( colors[color] );
                		NumericVectorFeature v = (NumericVectorFeature)inst.getFeature("point");
                		Ellipse2D l = new Ellipse2D.Double(v.getValue()[0], v.getValue()[1], 5, 5);
                		g2.draw(l);
                	}
                	color++;
                }
            }
        });

        t.setDefaultCloseOperation(EXIT_ON_CLOSE);
        t.setSize(400, 400);
        t.setVisible(true);
	}

}
