package com.app.auscope;

import android.graphics.PointF;

public class PointD extends PointF 
{
	public double x = 0.0, y = 0.0;
	
	public PointD(double sX, double sY)
	{
		sX = x;
		sY = y;
	}
	
	public double getX()
	{
		return x;
	}
	public double getY()
	{
		return y;
	}
}
