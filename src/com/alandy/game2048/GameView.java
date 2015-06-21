package com.alandy.game2048;

import java.util.ArrayList;
import java.util.List;

import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.graphics.Point;
import android.util.AttributeSet;
import android.view.MotionEvent;
import android.view.View;
import android.widget.GridLayout;
/**
 * ============================================================
 *
 * 版 权 ： 小楫轻舟开发团队 版权所有 (c) 2015
 *
 * 作 者 : 冯方俊
 *
 * 版 本 ： 1.0
 *
 * 创建日期 ： 2015年6月21日 下午6:33:52
 *
 * 描 述 ：
 *	主要是处理用户滑动时间、控制游戏界面等
 * 
 * 修订历史 ：
 *
 * ============================================================
 **/
public class GameView extends GridLayout {
	private Card[][] cardsMap = new Card[4][4];
	// 使用emptyPoints数组来存储没有数字的方格  
	private List<Point> emptyPoints = new ArrayList<Point>();

	public GameView(Context context, AttributeSet attrs, int defStyle) {
		super(context, attrs, defStyle);

		initGameView();
	}

	public GameView(Context context) {
		super(context);

		initGameView();
	}

	public GameView(Context context, AttributeSet attrs) {
		super(context, attrs);

		initGameView();
	}

	private void initGameView() {
		// 定义列数，即 4*4 的格子
		setColumnCount(4);
		setBackgroundColor(0xffbbada0);

		setOnTouchListener(new View.OnTouchListener() {

			private float startX, startY, offsetX, offsetY;

			@Override
			public boolean onTouch(View v, MotionEvent event) {

				switch (event.getAction()) {
				case MotionEvent.ACTION_DOWN:
					startX = event.getX();
					startY = event.getY();
					break;
				case MotionEvent.ACTION_UP:
					offsetX = event.getX() - startX;
					offsetY = event.getY() - startY;

					// 说明用户是水平方向滑动的
					if (Math.abs(offsetX) > Math.abs(offsetY)) {
						// 设置为“-5”是为了容错，因为数据存储精度原因 
						if (offsetX < -5) {
							swipeLeft();
						} else if (offsetX > 5) {
							swipeRight();
						}
						// 说明用户是竖直方向滑动的
					} else {
						if (offsetY < -5) {
							swipeUp();
						} else if (offsetY > 5) {
							swipeDown();
						}
					}

					break;
				}
				return true;
			}
		});
	}

	@Override
	protected void onSizeChanged(int w, int h, int oldw, int oldh) {
		// 跟随不同手机的屏幕大小，设置相应每个方格的大小 
		super.onSizeChanged(w, h, oldw, oldh);

		int cardWidth = (Math.min(w, h) - 10) / 4;
		 //向  GameView 界面添加方格
		addCards(cardWidth, cardWidth);

		startGame();
	}

	private void addCards(int cardWidth, int cardHeight) {

		Card c;

		for (int y = 0; y < 4; y++) {
			for (int x = 0; x < 4; x++) {
				c = new Card(getContext());
				// 先设置每个格子都是没有数字，即值均为 0 
				c.setNum(0);
				addView(c, cardWidth, cardHeight);

				cardsMap[x][y] = c;
			}
		}
	}

	private void startGame() {

		MainActivity.getMainActivity().clearScore();

		for (int y = 0; y < 4; y++) {
			for (int x = 0; x < 4; x++) {
				cardsMap[x][y].setNum(0);
			}
		}
		// 开始时先有两个方格有数字
		addRandomNum();
		addRandomNum();
	}

	/**
	 * 随机生成下一个数字出现的位置
	 */
	private void addRandomNum() {
		// 先清空游戏界面
		emptyPoints.clear();

		// 遍历cardsMap中所有元素，如果其值为
		// 0，即不需要显示数字的话，则加入emptyPoints中  
		for (int y = 0; y < 4; y++) {
			for (int x = 0; x < 4; x++) {
				if (cardsMap[x][y].getNum() <= 0) {
					emptyPoints.add(new Point(x, y));
				}
			}
		}

		// 随机生成下一个格子的位置，即只需要从emptyPoints中随机挑一个就OK了
		Point p = emptyPoints
				.remove((int) (Math.random() * emptyPoints.size()));
		// 使下一个格子出现 4 和2的概率比为 1 9（实际游戏中出现4的概率比较小，这里我们假设它为0.1）
		cardsMap[p.x][p.y].setNum(Math.random() > 0.1 ? 2 : 4);
	}

	/**
	 * 处理用户向左滑动的事件
	 */
	private void swipeLeft() {

		boolean merge = false;

		 // 每一个方格都比较一下自己右边的数，看能否结合
		for (int y = 0; y < 4; y++) {
			for (int x = 0; x < 4; x++) {

				for (int x1 = x + 1; x1 < 4; x1++) {
					if (cardsMap[x1][y].getNum() > 0) {

						if (cardsMap[x][y].getNum() <= 0) {
							cardsMap[x][y].setNum(cardsMap[x1][y].getNum());
							cardsMap[x1][y].setNum(0);

							x--;

							merge = true;
						} else if (cardsMap[x][y].equals(cardsMap[x1][y])) {
							cardsMap[x][y].setNum(cardsMap[x][y].getNum() * 2);
							cardsMap[x1][y].setNum(0);

							MainActivity.getMainActivity().addScore(
									cardsMap[x][y].getNum());
							merge = true;
						}

						break;
					}
				}
			}
		}

		if (merge) {
			addRandomNum();
			//每次移动都判断游戏是否结束
			checkComplete();
		}
	}

	/**
	 * 处理用户向右滑动的事件  
	 */
	private void swipeRight() {

		boolean merge = false;

		for (int y = 0; y < 4; y++) {
			for (int x = 3; x >= 0; x--) {

				for (int x1 = x - 1; x1 >= 0; x1--) {
					if (cardsMap[x1][y].getNum() > 0) {

						if (cardsMap[x][y].getNum() <= 0) {
							cardsMap[x][y].setNum(cardsMap[x1][y].getNum());
							cardsMap[x1][y].setNum(0);

							x++;
							merge = true;
						} else if (cardsMap[x][y].equals(cardsMap[x1][y])) {
							cardsMap[x][y].setNum(cardsMap[x][y].getNum() * 2);
							cardsMap[x1][y].setNum(0);
							MainActivity.getMainActivity().addScore(
									cardsMap[x][y].getNum());
							merge = true;
						}

						break;
					}
				}
			}
		}

		//如果有合并，则再随机添加一个有数字的格子，并检查游戏是否结束
		if (merge) {
			addRandomNum();
			checkComplete();
		}
	}

	// 处理用户向上滑动的事件
	private void swipeUp() {

		boolean merge = false;

		for (int x = 0; x < 4; x++) {
			for (int y = 0; y < 4; y++) {

				for (int y1 = y + 1; y1 < 4; y1++) {
					if (cardsMap[x][y1].getNum() > 0) {

						if (cardsMap[x][y].getNum() <= 0) {
							cardsMap[x][y].setNum(cardsMap[x][y1].getNum());
							cardsMap[x][y1].setNum(0);

							y--;

							merge = true;
						} else if (cardsMap[x][y].equals(cardsMap[x][y1])) {
							cardsMap[x][y].setNum(cardsMap[x][y].getNum() * 2);
							cardsMap[x][y1].setNum(0);
							MainActivity.getMainActivity().addScore(
									cardsMap[x][y].getNum());
							merge = true;
						}

						break;

					}
				}
			}
		}
		//如果有合并，则再随机添加一个有数字的格子，并检查游戏是否结束
		if (merge) {
			addRandomNum();
			checkComplete();
		}
	}

	/**
	 * 处理用户向下滑动的事件
	 */
	private void swipeDown() {

		boolean merge = false;

		for (int x = 0; x < 4; x++) {
			for (int y = 3; y >= 0; y--) {

				for (int y1 = y - 1; y1 >= 0; y1--) {
					if (cardsMap[x][y1].getNum() > 0) {

						if (cardsMap[x][y].getNum() <= 0) {
							cardsMap[x][y].setNum(cardsMap[x][y1].getNum());
							cardsMap[x][y1].setNum(0);

							y++;
							merge = true;
						} else if (cardsMap[x][y].equals(cardsMap[x][y1])) {
							cardsMap[x][y].setNum(cardsMap[x][y].getNum() * 2);
							cardsMap[x][y1].setNum(0);
							MainActivity.getMainActivity().addScore(
									cardsMap[x][y].getNum());
							merge = true;
						}

						break;
					}
				}
			}
		}
		//如果有合并，则再随机添加一个有数字的格子，并检查游戏是否结束
		if (merge) {
			addRandomNum();
			checkComplete();
		}
	}

	/**
	 * 判断游戏是否结束
	 */
	private void checkComplete() {

		boolean complete = true;
		// 定义一个ALL标签，是下面双重循环中的break能跳出该双重循环
		ALL: for (int y = 0; y < 4; y++) {
			for (int x = 0; x < 4; x++) {
				// 如果还有空格子 或  
                // 任意一个格子的上下左右可以合并，即存在相同的数的时候，则游戏继续
				if (cardsMap[x][y].getNum() == 0
						|| (x > 0 && cardsMap[x][y].equals(cardsMap[x - 1][y]))
						|| (x < 3 && cardsMap[x][y].equals(cardsMap[x + 1][y]))
						|| (y > 0 && cardsMap[x][y].equals(cardsMap[x][y - 1]))
						|| (y < 3 && cardsMap[x][y].equals(cardsMap[x][y + 1]))) {

					complete = false;
					 // 跳出该双重循环 
					break ALL;
				}
			}
		}
		 //游戏结束的时候跳出一个dialog
		if (complete) {
			new AlertDialog.Builder(getContext())
					.setTitle("你好")
					.setMessage("游戏结束")
					.setPositiveButton("重来",
							new DialogInterface.OnClickListener() {

								@Override
								public void onClick(DialogInterface dialog,
										int which) {
									startGame();
								}
							}).show();
		}

	}

}
