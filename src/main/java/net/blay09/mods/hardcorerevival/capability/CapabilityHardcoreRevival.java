package net.blay09.mods.hardcorerevival.capability;

import net.blay09.mods.hardcorerevival.HardcoreRevival;
import net.minecraft.nbt.INBTBase;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.util.EnumFacing;
import net.minecraft.util.ResourceLocation;
import net.minecraftforge.common.capabilities.Capability;
import net.minecraftforge.common.capabilities.CapabilityInject;
import net.minecraftforge.common.capabilities.CapabilityManager;

public class CapabilityHardcoreRevival {
    public static final ResourceLocation REGISTRY_NAME = new ResourceLocation(HardcoreRevival.MOD_ID, "hardcore_revival");
    @CapabilityInject(IHardcoreRevival.class)
    public static Capability<IHardcoreRevival> REVIVAL_CAPABILITY = null;

    public static void register() {
        CapabilityManager.INSTANCE.register(IHardcoreRevival.class, new Capability.IStorage<IHardcoreRevival>() {
            @Override
            public INBTBase writeNBT(Capability<IHardcoreRevival> capability, IHardcoreRevival instance, EnumFacing side) {
                NBTTagCompound tagCompound = new NBTTagCompound();
                tagCompound.setInt("DeathTime", instance.getDeathTime());
                return tagCompound;
            }

            @Override
            public void readNBT(Capability<IHardcoreRevival> capability, IHardcoreRevival instance, EnumFacing side, INBTBase base) {
                NBTTagCompound tagCompound = (NBTTagCompound) base;
                instance.setDeathTime(tagCompound.getInt("DeathTime"));
            }
        }, HardcoreRevivalImpl::new);
    }
}
