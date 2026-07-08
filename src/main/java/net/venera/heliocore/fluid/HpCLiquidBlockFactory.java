package net.venera.heliocore.fluid;

import com.mojang.blaze3d.shaders.FogShape;
import com.mojang.blaze3d.systems.RenderSystem;
import net.minecraft.client.renderer.FogRenderer;
import net.venera.heliocore.HeliopauseCore;
import net.venera.heliocore.block.*;
import net.venera.heliocore.item.LiquidBucketItem;
import net.venera.heliocore.item.HpCItems;
import net.venera.heliocore.util.FluidHelper;
import net.minecraft.client.Camera;
import net.minecraft.client.multiplayer.ClientLevel;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.item.ItemEntity;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.Items;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.LiquidBlock;
import net.minecraft.world.level.material.Fluid;
import net.minecraft.world.level.material.MapColor;
import net.minecraft.world.level.material.PushReaction;
import net.minecraft.world.phys.Vec3;
import net.neoforged.neoforge.client.extensions.common.IClientFluidTypeExtensions;
import net.neoforged.neoforge.fluids.BaseFlowingFluid;
import net.neoforged.neoforge.fluids.FluidType;
import net.neoforged.neoforge.registries.DeferredBlock;
import net.neoforged.neoforge.registries.DeferredHolder;
import net.neoforged.neoforge.registries.DeferredItem;
import org.jetbrains.annotations.NotNull;
import org.joml.Vector3f;
import javax.annotation.Nonnull;
import java.util.function.Consumer;
import java.util.function.Supplier;

public class HpCLiquidBlockFactory {
    private static final ResourceLocation STILL_METAL = ResourceLocation.fromNamespaceAndPath(HeliopauseCore.MOD_ID,"block/molten_block_still");
    private static final ResourceLocation CRUDE_OIL_STILL = ResourceLocation.fromNamespaceAndPath(HeliopauseCore.MOD_ID, "block/crude_oil_still");
    private static final ResourceLocation CRUDE_OIL_FLOW = ResourceLocation.fromNamespaceAndPath(HeliopauseCore.MOD_ID, "block/crude_oil_flow");
    private static final ResourceLocation REFINED_FUEL_STILL = ResourceLocation.fromNamespaceAndPath(HeliopauseCore.MOD_ID, "block/refined_fuel_still");
    private static final ResourceLocation REFINED_FUEL_FLOW = ResourceLocation.fromNamespaceAndPath(HeliopauseCore.MOD_ID, "block/refined_fuel_flow");
	private static final ResourceLocation OXYGEN_STILL = ResourceLocation.fromNamespaceAndPath(HeliopauseCore.MOD_ID, "block/oxygen_liquid_still");

	private final String name;
	private final DeferredHolder<FluidType, FluidType> fluidType;
	private DeferredHolder<Fluid, BaseFlowingFluid> source;
	private DeferredHolder<Fluid, BaseFlowingFluid> flowing;
	private DeferredBlock<LiquidBlock> fluidblock;
	private DeferredItem<LiquidBucketItem> bucket;

	@Nonnull
	public String getName() {
		return name;
	}

	@Nonnull
	public DeferredHolder<FluidType, FluidType> getFluidType() {
		return fluidType;
	}

	@Nonnull
	public DeferredHolder<Fluid, BaseFlowingFluid> getSourceRegistry() {
		return source;
	}

	@Nonnull
	public BaseFlowingFluid getSource() {
		return source.get();
	}

	@Nonnull
	public DeferredHolder<Fluid, BaseFlowingFluid> getFlowing() {
		return flowing;
	}

	@Nonnull
	public LiquidBlock getFluidblock() {
		return fluidblock.get();
	}

	public DeferredBlock<LiquidBlock> getFluidBlockRegistry() {
		return fluidblock;
	}

	public DeferredItem<LiquidBucketItem> getBucketRegistry() {
		return bucket;
	}

	public Item getBucket() {
		return bucket.get();
	}

	public static BaseFlowingFluid.Properties createProperties(Supplier<FluidType> type, Supplier<BaseFlowingFluid> still, Supplier<BaseFlowingFluid> flowing,
	                                                           DeferredItem<LiquidBucketItem> bucket, Supplier<LiquidBlock> block) {
		return new BaseFlowingFluid.Properties(type, still, flowing)
				.bucket(bucket).block(block);
	}

	public HpCLiquidBlockFactory(String name, Supplier<Block> blockSupplier, MapColor mapColor, Vector3f fogColor, boolean coldLiquid,
								 int luminosity, int viscosity) {
		this.name = name;
		fluidType = HpCFluids.FLUID_TYPES.register(name, () -> new FluidType(FluidHelper.createTypeProperties().viscosity(viscosity).temperature(coldLiquid ? 300 : 1000).lightLevel(luminosity)) {
			@Override
			public double motionScale(Entity entity) {
				return 0.0023333333333333335D;
			}

			@Override
			public void setItemMovement(ItemEntity entity) {
				Vec3 vec3 = entity.getDeltaMovement();
				entity.setDeltaMovement(vec3.x * (double) 0.95F, vec3.y + (double) (vec3.y < (double) 0.06F ? 5.0E-4F : 0.0F), vec3.z * (double) 0.95F);
			}

			@SuppressWarnings("removal")
            @Override
			public void initializeClient(Consumer<IClientFluidTypeExtensions> consumer) {
				consumer.accept(new IClientFluidTypeExtensions() {

					@Override
					public ResourceLocation getStillTexture() {
                        return switch (name) {
                            case "crude_oil" -> CRUDE_OIL_STILL;
                            case "refined_fuel" -> REFINED_FUEL_STILL;
							case "oxygen_liquid" -> OXYGEN_STILL;
                            default -> STILL_METAL;
                        };
                    }

					@Override
					public ResourceLocation getFlowingTexture() {
                        return switch (name) {
                            case "crude_oil" -> CRUDE_OIL_FLOW;
                            case "refined_fuel" -> REFINED_FUEL_FLOW;
                            default -> STILL_METAL;
                        };
                    }

					@Override
					public int getTintColor() {
						return 0xFFFFFF;
					}

					@Override
					public @NotNull Vector3f modifyFogColor(Camera camera, float partialTick, ClientLevel level, int renderDistance, float darkenWorldAmount, Vector3f fluidFogColor) {
						return fogColor;
					}

                    @Override
                    public void modifyFogRender(Camera camera, FogRenderer.FogMode mode, float renderDistance, float partialTick, float nearDistance, float farDistance, FogShape shape) {
                        IClientFluidTypeExtensions.super.modifyFogRender(camera, mode, renderDistance, partialTick, nearDistance, farDistance, shape);{
                            nearDistance = -0.7f;
                            farDistance = 2.0f;
                            shape = FogShape.SPHERE;
                            RenderSystem.setShaderFogStart(nearDistance);
                            RenderSystem.setShaderFogEnd(farDistance);
                            RenderSystem.setShaderFogShape(shape);
                        }
                    }
                });
			}
		});
		source = HpCFluids.FLUIDS.register(name, () -> new LiquidBlockFluid.Source(
				createProperties(fluidType, source, flowing, bucket, fluidblock))
		);
		flowing = HpCFluids.FLUIDS.register(name + "_flowing", () -> new LiquidBlockFluid.Flowing(
				createProperties(fluidType, source, flowing, bucket, fluidblock))
		);

        fluidblock = HpCBlocks.BLOCKS.register(name, () -> new LiquidBlockBlock(
					Block.Properties.of().mapColor(mapColor).pushReaction(PushReaction.DESTROY).liquid().noCollission().strength(100.0F).replaceable().randomTicks().noLootTable().lightLevel(state -> luminosity), source, blockSupplier));

		bucket = HpCItems.ITEMS.register(name + "_bucket", () -> new LiquidBucketItem(new Item.Properties().craftRemainder(Items.BUCKET).stacksTo(1), source));
	}

	public static class Builder {
		private String name;
		private Supplier<Block> blockSupplier;
		private MapColor mapColor = MapColor.WATER;
        private int viscosity = 1000;
        private Vector3f fogColor;
		private boolean hot = false;
		private int luminosity = 0;
        private double motionScale = 0.0023333333333333335D;

		public Builder(String name, Supplier<Block> blockSupplier, int color) {
			this.name = name;
			this.blockSupplier = blockSupplier;
		}

		public Builder mapColor(MapColor mapColor) {
			this.mapColor = mapColor;
			return this;
		}

		public Builder hot() {
			this.hot = true;
			return this;
		}

		public Builder luminosity(int luminosity) {
			this.luminosity = luminosity;
			return this;
		}

        public Builder setViscosity(int viscosity) {
            this.viscosity = viscosity;
            return this;
        }

        public Builder motionScale(double scale) {
            this.motionScale = scale;
            return this;
        }

        public Builder modifyFogColor(Vector3f fogColor) {
            this.fogColor = fogColor;
            return this;
        }

        public HpCLiquidBlockFactory build() {
			return new HpCLiquidBlockFactory(name, blockSupplier, mapColor, fogColor, hot, luminosity, viscosity);
		}
	}
}
