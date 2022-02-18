package io.alwa.mods.myrtrees.common.blockentity.render;

import com.mojang.blaze3d.vertex.PoseStack;
import com.mojang.blaze3d.vertex.VertexConsumer;
import com.mojang.math.Matrix3f;
import com.mojang.math.Matrix4f;
import io.alwa.mods.myrtrees.common.MyrtreesConfig;
import io.alwa.mods.myrtrees.common.blockentity.WoodenBucketBlockEntity;
import net.minecraft.client.Minecraft;
import net.minecraft.client.renderer.MultiBufferSource;
import net.minecraft.client.renderer.RenderType;
import net.minecraft.client.renderer.blockentity.BlockEntityRenderer;
import net.minecraft.client.renderer.blockentity.BlockEntityRendererProvider;
import net.minecraft.client.renderer.texture.TextureAtlas;
import net.minecraft.client.renderer.texture.TextureAtlasSprite;
import net.minecraft.core.Direction;
import net.minecraft.resources.ResourceLocation;

import static net.minecraft.world.level.block.HorizontalDirectionalBlock.FACING;

public class WoodenBucketRenderer implements BlockEntityRenderer<WoodenBucketBlockEntity> {
    public WoodenBucketRenderer(BlockEntityRendererProvider.Context context) {
    }

    public static ResourceLocation LATEX_TEXTURE = new ResourceLocation("myrtrees:block/latex");

    @Override
    public void render(WoodenBucketBlockEntity bucket, float partialTicks, PoseStack poseStack, MultiBufferSource multiBufferSource, int i, int j) {
        int amount = bucket.latex;

        if (amount <= 0) {
            return;
        }

        float o0 = 4.00F / 16F;
        float o1 = 12.00F / 16F;

        float y0 = 6 / 16F;
        float y11 = 10.8F / 16F;

        float fluidLevel = y0 + ((y11 - y0) * amount / (float) MyrtreesConfig.BUCKET_CAPACITY);


        VertexConsumer buffer = multiBufferSource.getBuffer(RenderType.translucent());

        TextureAtlasSprite sprite = Minecraft.getInstance().getTextureAtlas(TextureAtlas.LOCATION_BLOCKS).apply(LATEX_TEXTURE);

        float u0 = sprite.getU0();
        float v0 = sprite.getV0();
        float u1 = sprite.getU1();
        float v1 = sprite.getV1();
        float uo = (u1 - u0) / 16F;
        float vo = (v1 - v0) / 16F;

        int color = 0xffffffff;
        int a = (color >> 24) & 0xFF;
        int r = (color >> 16) & 0xFF;
        int g = (color >> 8) & 0xFF;
        int b = color & 0xFF;


        Direction facing = bucket.getBlockState().getValue(FACING);

        float translation = 3.0f / 16f;

        poseStack.pushPose();
        if (facing.getAxis() == Direction.Axis.X) {
            poseStack.translate(facing.getAxisDirection() == Direction.AxisDirection.NEGATIVE ? translation : -translation, 0, 0);
        } else {
            poseStack.translate(0, 0, facing.getAxisDirection() == Direction.AxisDirection.NEGATIVE ? translation : -translation);
        }
        Matrix4f m = poseStack.last().pose();
        Matrix3f n = poseStack.last().normal();
        buffer.vertex(m, o0, fluidLevel, o0).color(r, g, b, a).uv(u0 + uo, v0 + vo).overlayCoords(j).uv2(i).normal(n, 0, 1, 0).endVertex();
        buffer.vertex(m, o0, fluidLevel, o1).color(r, g, b, a).uv(u1 - uo, v0 + vo).overlayCoords(j).uv2(i).normal(n, 0, 1, 0).endVertex();
        buffer.vertex(m, o1, fluidLevel, o1).color(r, g, b, a).uv(u1 - uo, v1 - vo).overlayCoords(j).uv2(i).normal(n, 0, 1, 0).endVertex();
        buffer.vertex(m, o1, fluidLevel, o0).color(r, g, b, a).uv(u0 + uo, v1 - vo).overlayCoords(j).uv2(i).normal(n, 0, 1, 0).endVertex();
        poseStack.popPose();
    }
}
