package com.alandy.game2048;

import android.content.Context;
import android.view.Gravity;
import android.widget.FrameLayout;
import android.widget.TextView;
/**
 * ============================================================
 *
 * 版 权 ： 小楫轻舟开发团队 版权所有 (c) 2015
 *
 * 作 者 : 冯方俊
 *
 * 版 本 ： 1.0
 *
 * 创建日期 ： 2015年6月21日 下午6:34:51
 *
 * 描 述 ：
 *	实现每一个小方格的形状、颜色等
 * 
 * 修订历史 ：
 *
 * ============================================================
 **/
public class Card extends FrameLayout {
	private int num = 0;
	private TextView label;
	public Card(Context context) {
		super(context);
				
		label = new TextView(getContext());
		label.setTextSize(32);
		label.setBackgroundColor(0x33ffffff);
		label.setGravity(Gravity.CENTER);
		
		//使方格自适应屏幕  
		LayoutParams lp = new LayoutParams(-1, -1);
		//使方格左，上的间距均为10像素
		lp.setMargins(10, 10, 0, 0);
		addView(label, lp);
		
		setNum(0);
	}
	
	
	
	public int getNum() {
		return num;
	}
	
	public void setNum(int num) {
		this.num = num;
		
		if (num<=0) {
			label.setText("");
		}else{
			label.setText(num+"");
		}
	}
	
	/**
	 * 比较方格的数字是否相同
	 * @param o
	 * @return
	 */
	public boolean equals(Card o) {
		return getNum()==o.getNum();
	}

	
}
