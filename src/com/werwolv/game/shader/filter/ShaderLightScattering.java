package com.werwolv.game.shader.filter;

import com.werwolv.game.shader.Shader;

public class ShaderLightScattering extends Shader {

    private int loc_colorBuffer;

    public ShaderLightScattering() {
        super("filter/filterLightScattering", "filter/filterLightScattering");
    }

    @Override
    protected void getAllUniformLocations() {
        this.loc_colorBuffer = super.getUniformLocation("colorBuffer");
    }

    @Override
    public void connectTextureUnits() {
        super.loadInteger(loc_colorBuffer, 0);
    }

    @Override
    protected void bindAttributes() {
        super.bindAttribute(0, "position");
    }
}
