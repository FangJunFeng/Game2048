package com.alandy.game2048;

import android.app.Activity;
import android.os.Bundle;
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
 * 创建日期 ： 2015年6月21日 下午6:49:25
 *
 * 描 述 ：
 *	主函数
 * 
 * 修订历史 ：
 *
 * ============================================================
 **/
public class MainActivity extends Activity {

	private int score = 0;
	private TextView tvScore;

	private static MainActivity mainActivity = null;
	//使其他方法能调用 MainActivity中的方法，如  GameView 方法中 
//     MainActivity.getMainActivity() 
//            .addScore( 
//            cardsMap[x][y] 
//                    .getNum());
	public static MainActivity getMainActivity() {
		return mainActivity;
	}

	public MainActivity() {
		mainActivity = this;
	}

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_main);

		tvScore = (TextView) findViewById(R.id.tvScore);
	}

	//分数清零，用于游戏开始或重新开始时
	public void clearScore() {
		score = 0;
		showScore();
	}

	public void showScore() {
		tvScore.setText(score + "");
	}

	public void addScore(int s) {
		score += s;
		showScore();
	}

}
