package com.werwolv.game.render.postProcessing;

import com.werwolv.game.main.Main;
import com.werwolv.game.shader.filter.ShaderLightScattering;
import org.lwjgl.opengl.GL11;
import org.lwjgl.opengl.GL13;

public class FilterLightScattering extends Filter<ShaderLightScattering> {

    public FilterLightScattering() {
        super(new ShaderLightScattering(), Main.getWindowSize()[0], Main.getWindowSize()[1]);
    }

    @Override
    public void render(int colorTexture, int colorTexture2) {
        super.render(colorTexture, colorTexture2);
        getShader().start();
        GL13.glActiveTexture(GL13.GL_TEXTURE0);
        GL11.glBindTexture(GL11.GL_TEXTURE_2D, colorTexture);
        getRenderer().renderQuad();
        getShader().stop();
    }
}
