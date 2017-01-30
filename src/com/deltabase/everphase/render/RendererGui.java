package com.deltabase.everphase.render;

import com.deltabase.everphase.gui.Gui;
import com.deltabase.everphase.main.Main;
import com.deltabase.everphase.model.ModelRaw;
import com.deltabase.everphase.modelloader.ResourceLoader;
import com.deltabase.everphase.resource.TextureGui;
import com.deltabase.everphase.shader.ShaderGui;
import com.deltabase.everphase.toolbox.Maths;
import org.joml.Matrix4f;
import org.joml.Vector2f;
import org.joml.Vector4f;
import org.lwjgl.opengl.GL11;
import org.lwjgl.opengl.GL13;
import org.lwjgl.opengl.GL20;
import org.lwjgl.opengl.GL30;

import java.util.ArrayList;
import java.util.List;

public class RendererGui {

    private final ModelRaw quad;
    private ShaderGui shader;
    private ResourceLoader loader;

    private List<TextureGui> textureUnits = new ArrayList<>();

    public RendererGui(ResourceLoader loader) {
        float[] positions = { -1, 1, -1, -1, 1, 1, 1, -1};  //The six indices of the quad
        quad = loader.loadToVAO(positions, 2);   //Create the quad out of the six vertices

        this.loader = loader;
        shader = new ShaderGui();                           //Create an instance of the GUI Shader
    }

    public void render(Gui gui) {
        shader.start();                                                     //Start the shader
        RendererMaster.disableCulling();
        GL30.glBindVertexArray(quad.getVaoID());                            //Bind the VAO of the quad to memory
        GL20.glEnableVertexAttribArray(0);                            //Enable the vertices buffer
        GL11.glEnable(GL11.GL_BLEND);                                       //Enable transparency
        GL11.glBlendFunc(GL11.GL_SRC_ALPHA, GL11.GL_ONE_MINUS_SRC_ALPHA);
        GL11.glDisable(GL11.GL_DEPTH_TEST);                                 //Disable the depth test so you can render multiple transparent GUIs behind each other

        Matrix4f matrix = Maths.createTransformationMatrix(new Vector2f(0.0F, 0.0F), new Vector2f(1.0F, 1.0F));  //...create a new transformation matrix...
        shader.loadTransformationMatrix(matrix);                        //...load it to the shader

        gui.render();


        GL11.glEnable(GL11.GL_DEPTH_TEST);                                  //Enable the depth test again
        GL11.glDisable(GL11.GL_BLEND);                                      //Disable transparency
        GL20.glDisableVertexAttribArray(0);                          //Disable the vertices buffer
        GL30.glBindVertexArray(0);                           //Unbind the VAO
        shader.loadSize(0.0F, 0.0F, 1.0F, 1.0F);
        RendererMaster.enableCulling();
        shader.stop();                                                      //Stop the shader

        textureUnits.clear();
    }

    public void drawTexture(float posX, float posY, float scale, Vector4f size, TextureGui texture) {
        GL13.glActiveTexture(GL13.GL_TEXTURE0);
        GL11.glBindTexture(GL11.GL_TEXTURE_2D, texture.getTextureID());
        shader.loadTransformationMatrix(Maths.createTransformationMatrix(new Vector2f((posX - (size.z() - size.x()) / Main.getWindowSize()[0]) *0.5F, (posY - (size.w() - size.y()) / Main.getWindowSize()[1]) *0.5F) , new Vector2f(scale, -scale * Main.getAspectRatio())));
        shader.loadSize(size.x / texture.getSize(), size.y / texture.getSize(), size.z / texture.getSize(), size.w / texture.getSize());
        GL11.glDrawArrays(GL11.GL_TRIANGLE_STRIP, 0, quad.getVertexCnt());
    }

    /*
     * Cleanup the shader
     */
    public void clean() {
        shader.clean();
    }
}
