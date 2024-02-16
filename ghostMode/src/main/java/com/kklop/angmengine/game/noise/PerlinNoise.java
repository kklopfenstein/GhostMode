/*******************************************************************************
 * Copyright 2012-2014 Kevin Klopfenstein.
 *
 * This file is part of AnGmEngine.
 *
 * AnGmEngine is free software: you can redistribute it and/or modify
 * it under the terms of the GNU General Public License as published by
 * the Free Software Foundation, either version 3 of the License, or
 * (at your option) any later version.
 *
 * AnGmEngine is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 * GNU General Public License for more details.
 *
 * You should have received a copy of the GNU General Public License
 * along with AnGmEngine.  If not, see <http://www.gnu.org/licenses/>.
 *******************************************************************************/
package com.kklop.angmengine.game.noise;

import java.util.Random;

/**
 * Java port from: http://devmag.org.za/2009/04/25/perlin-noise/
 * @author hal9000
 *
 */
public class PerlinNoise {

	/**
	 * Generate white noise
	 * @param width
	 * @param height
	 * @return
	 */
	public float[][] generateWhiteNoise(int width, int height) {
		Random rand = new Random();
		float[][] noise = new float[width][height];
		
		for(int i=0; i<width; i++) {
			for (int j=0; j<height; j++) {
				noise[i][j] = (float) rand.nextFloat();
			}
		}
		
		return noise;
	}
	
	/**
	 * Generate smooth noise, base noise should come from generateWhiteNoise
	 * @param baseNoise
	 * @param octave
	 * @return
	 */
	public float[][] generateSmoothNoise(float[][] baseNoise, int octave) {
		int width = baseNoise.length;
		int height = baseNoise[0].length;
		
		float[][] smoothNoise = new float[width][height];
		
		int samplePeriod = Double.valueOf(Math.pow(2, octave)).intValue();
		float sampleFrequency = 1.0f / samplePeriod;
		
		for (int i=0; i < width; i++) {
			//calculate the horizontal sampling indices
		      int sample_i0 = (i / samplePeriod) * samplePeriod;
		      int sample_i1 = (sample_i0 + samplePeriod) % width; //wrap around
		      float horizontal_blend = (i - sample_i0) * sampleFrequency;
		      for (int j = 0; j < height; j++)
		      {
		         //calculate the vertical sampling indices
		         int sample_j0 = (j / samplePeriod) * samplePeriod;
		         int sample_j1 = (sample_j0 + samplePeriod) % height; //wrap around
		         float vertical_blend = (j - sample_j0) * sampleFrequency;
		 
		         //blend the top two corners
		         float top = Interpolate(baseNoise[sample_i0][sample_j0],
		            baseNoise[sample_i1][sample_j0], horizontal_blend);
		 
		         //blend the bottom two corners
		         float bottom = Interpolate(baseNoise[sample_i0][sample_j1],
		            baseNoise[sample_i1][sample_j1], horizontal_blend);
		 
		         //final blend
		         smoothNoise[i][j] = Interpolate(top, bottom, vertical_blend);
		      }
		}
		
		return smoothNoise;
	}
	
	/**
	 * Interpolate
	 * @param x0
	 * @param x1
	 * @param alpha
	 * @return
	 */
	private float Interpolate(float x0, float x1, float alpha)
	{
	   return x0 * (1 - alpha) + alpha * x1;
	}
	
	/**
	 * Generate perlin noise
	 * @param baseNoise
	 * @param octaveCount
	 * @return
	 */
	public float[][] generatePerlinNoise(float[][] baseNoise, int octaveCount)
	{
	   int width = baseNoise.length;
	   int height = baseNoise[0].length;
	 
	   float[][][] smoothNoise = new float[octaveCount][][]; //an array of 2D arrays containing
	 
	   float persistance = 0.5f;
	 
	   //generate smooth noise
	   for (int i = 0; i < octaveCount; i++)
	   {
	       smoothNoise[i] = generateSmoothNoise(baseNoise, i);
	   }
	 
	    float[][] perlinNoise = new float[width][height];
	    float amplitude = 1.0f;
	    float totalAmplitude = 0.0f;
	 
	    //blend noise together
	    for (int octave = octaveCount - 1; octave >= 0; octave--)
	    {
	       amplitude *= persistance;
	       totalAmplitude += amplitude;
	 
	       for (int i = 0; i < width; i++)
	       {
	          for (int j = 0; j < height; j++)
	          {
	             perlinNoise[i][j] += smoothNoise[octave][i][j] * amplitude;
	          }
	       }
	    }
	 
	   //normalisation
	   for (int i = 0; i < width; i++)
	   {
	      for (int j = 0; j < height; j++)
	      {
	         perlinNoise[i][j] /= totalAmplitude;
	      }
	   }
	 
	   return perlinNoise;
	}
	
	/*Define k sets of similar looking objects.
Create Perlin noise large enough to cover your grid. Each pixel of noise should correspond with one cell in the grid.
For every cell in the grid, find the corresponding pixel of noise, and use the following formula to decide from which set you should choose an object: i = floor (n / (1.0 / k)). You choose objects from set S(i), usually randomly.
*/	
}
