package com.werwolv.game.render.postProcessing;

import com.werwolv.game.main.Main;
import com.werwolv.game.shader.filter.ShaderBright;

public class FilterBright extends Filter<ShaderBright> {

	public FilterBright(float value){
		super(new ShaderBright(), Main.getWindowSize()[0], Main.getWindowSize()[1]);

		getShader().start();
		getShader().loadValue(value);
		getShader().stop();
	}

	@Override
	public void render(int colorTexture, int colorTexture2) {
		super.render(colorTexture, colorTexture2);
	}
}
