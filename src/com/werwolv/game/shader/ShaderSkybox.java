package com.werwolv.game.shader;

import com.werwolv.game.entity.EntityPlayer;
import com.werwolv.game.toolbox.Maths;
import org.joml.Matrix4f;
import org.joml.Vector3f;

public class ShaderSkybox extends Shader {

    private int loc_projectionMatrix;
    private int loc_viewMatrix;
    private int loc_fogColor;
    private int loc_cubeMap, loc_cubeMap2, loc_blendFactor;

    private static final float ROTATION_SPEED = 0.00F;

    private float rotation;

    public ShaderSkybox() {
        super("shaderSkybox", "shaderSkybox");
    }

    public void loadProjectionMatrix(Matrix4f matrix) {
        super.loadMatrix(loc_projectionMatrix, matrix);
    }

    public void loadViewMatrix(EntityPlayer player) {
        Matrix4f matrix = Maths.createViewMatrix(player);
        matrix.m30(0);
        matrix.m31(0);
        matrix.m32(0);

        rotation += ROTATION_SPEED;
        matrix.rotate((float) Math.toRadians(rotation), new Vector3f(0.0F, 1.0F, 0.0F));
        super.loadMatrix(loc_viewMatrix, matrix);
    }

    public void loadFogColor(float r, float g, float b) {
        super.loadVector(loc_fogColor, new Vector3f(r, g, b));
    }

    public void loadBlendFactor(float blend) {
        super.loadFloat(loc_blendFactor, blend);
    }

    public void connectTextureUnits() {
        super.loadInteger(loc_cubeMap, 0);
        super.loadInteger(loc_cubeMap2, 1);
    }

    @Override
    protected void getAllUniformLocations() {
        loc_projectionMatrix = super.getUniformLocation("projectionMatrix");
        loc_viewMatrix = super.getUniformLocation("viewMatrix");
        loc_fogColor = super.getUniformLocation("fogColor");
        loc_blendFactor = super.getUniformLocation("blendFactor");
        loc_cubeMap = super.getUniformLocation("cubeMap");
        loc_cubeMap2 = super.getUniformLocation("cubeMap2");
    }

    @Override
    protected void bindAttributes() {
        super.bindAttribute(0, "position");
    }
}