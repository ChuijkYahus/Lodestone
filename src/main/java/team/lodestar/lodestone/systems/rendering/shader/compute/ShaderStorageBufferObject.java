package team.lodestar.lodestone.systems.rendering.shader.compute;

import com.mojang.blaze3d.platform.GlStateManager;
import org.lwjgl.system.MemoryUtil;
import team.lodestar.lodestone.systems.rendering.IBufferObject;
import team.lodestar.lodestone.systems.rendering.LodestoneRenderSystem;

import java.nio.ByteBuffer;

import static org.lwjgl.opengl.GL43.*;

/**
 * Shader Storage Buffer Object
 */
public class ShaderStorageBufferObject implements IBufferObject {
    private int bufferId;
    private final Usage usage;
    private final int bindingIndex;
    private ByteBuffer byteBuffer;


    public ShaderStorageBufferObject(Usage usage, int bindingIndex) {
        this.usage = usage;
        this.bindingIndex = bindingIndex;
        LodestoneRenderSystem.glGenBuffers(i -> this.bufferId = i);
        LodestoneRenderSystem.glBindBuffer(GL_SHADER_STORAGE_BUFFER, this.bufferId);
        LodestoneRenderSystem.bindBufferBase(GL_SHADER_STORAGE_BUFFER, bindingIndex, this.bufferId);

        this.registerBufferObject();
    }

    public void upload(ByteBuffer byteBuffer) {
        int size = byteBuffer.remaining();
        if(byteBuffer.remaining() > glGetInteger(GL_MAX_SHADER_STORAGE_BLOCK_SIZE))
            throw new RuntimeException("Buffer size exceeds maximum shader storage block size");
        glBindBuffer(GL_SHADER_STORAGE_BUFFER, this.bufferId);
        glBufferData(GL_SHADER_STORAGE_BUFFER, byteBuffer, this.usage.getGlEnum());
        glBindBuffer(GL_SHADER_STORAGE_BUFFER, 0);
        this.byteBuffer = byteBuffer;
    }

    public static void unbind() {
        LodestoneRenderSystem.glBindBuffer(GL_SHADER_STORAGE_BUFFER, 0);
    }

    public int getId() {
        return this.bufferId;
    }

    public int getBindingIndex() {
        return this.bindingIndex;
    }

    public Usage getUsage() {
        return this.usage;
    }

    @Override
    public String toString() {
        return "ShaderStorageBufferObject{" +
                "bufferId=" + bufferId +
                ", usage=" + usage +
                ", bindingIndex=" + bindingIndex +
                '}';
    }

    @Override
    public void destroy() {
        if (this.bufferId != 0)
            glDeleteProgram(this.bufferId);

        if (this.byteBuffer != null)
            MemoryUtil.memFree(this.byteBuffer);
    }

    public enum Usage {
        STREAM_DRAW(GL_STREAM_DRAW),
        STREAM_READ(GL_STREAM_READ),
        STREAM_COPY(GL_STREAM_COPY),
        STATIC_DRAW(GL_STATIC_DRAW),
        STATIC_READ(GL_STATIC_READ),
        STATIC_COPY(GL_STATIC_COPY),
        DYNAMIC_DRAW(GL_DYNAMIC_DRAW),
        DYNAMIC_READ(GL_DYNAMIC_READ),
        DYNAMIC_COPY(GL_DYNAMIC_COPY);

        private final int glEnum;

        Usage(int glEnum) {
            this.glEnum = glEnum;
        }

        public int getGlEnum() {
            return glEnum;
        }
    }
}
