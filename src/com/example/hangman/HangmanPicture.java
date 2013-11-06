package com.example.hangman;

import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.Paint.Style;
import android.graphics.RectF;
import android.view.View;

/**
 * 
 * @author student5
 *
 */
public class HangmanPicture extends View {

	public HangmanPicture(Context context) {
		super(context);
		// TODO Auto-generated constructor stub
	}

	private final Paint p = new Paint();
	@Override
	protected void onDraw(Canvas canvas) {
		int width = canvas.getWidth();
		int height = canvas.getHeight();
		p.setStrokeWidth(2);
		p.setStyle(Style.STROKE);
		p.setTextSize(20);
		switch (leveli) {
		case 9:
			p.setColor(Color.RED);
			canvas.drawText("Game over", width*0.1F, height*0.5F, p);
			p.setColor(Color.BLACK);
		case 8:
			canvas.drawLine(width * 0.5F, height * 0.8F, width * 0.75F, height * 1F, p);  // oikea jalka
		case 7:
			canvas.drawLine(width * 0.5F, height * 0.8F, width * 0.25F, height * 1F, p);  // vasen jalka
		case 6:
			canvas.drawLine(width * 0.5F, height * 0.5F, width * 0.5F, height * 0.8F, p); // vartalo
		case 5:
			canvas.drawLine(width * 0.5F, height * 0.5F, width * 0.75F, height * 0.7F, p);  // oikea käsi
		case 4:
			canvas.drawLine(width * 0.5F, height * 0.5F, width * 0.25F, height * 0.7F, p);  // vasen käsi
		case 3:
			canvas.drawLine(width*0.5F, height*0.4F, width*0.5F, height*0.5F, p);  // kaula
		case 2:
			canvas.drawOval(new RectF(width*0.4F, height*0.2F, width*0.6F, height*0.4F), p);  // pää
		case 1:
			canvas.drawLine(width*0.5F, height*0.2F, width*0.5F, 0, p);  // köysi
		case 0:
			p.setStrokeWidth(10);  // hirsipuu
			canvas.drawLine(0, 0, 0, height, p);
			canvas.drawLine(0, 0, width*0.5F, 0, p);
			canvas.drawLine(0, height*0.3F, width*0.3F, 0, p);
		}
		super.onDraw(canvas);
	}
	
	private int leveli = 0;
	/**
	 * 
	 * @param level Valid values 0 - 9. 0 = no wrong guesses, 9 = game over
	 */
	public void setLevel(int level) {
		leveli = level;
		invalidate();
		
	}
}
