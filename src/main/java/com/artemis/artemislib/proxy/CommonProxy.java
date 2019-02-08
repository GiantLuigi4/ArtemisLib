package com.artemis.artemislib.proxy;

import javax.annotation.Nullable;

import com.artemis.artemislib.compatibilities.Capabilities;
import com.artemis.artemislib.compatibilities.CapabilitiesHandler;
import com.artemis.artemislib.network.NetworkHandler;
import com.artemis.artemislib.util.AttachAttributes;
import com.artemis.artemislib.util.debug.debugMethods;

import net.minecraft.entity.Entity;
import net.minecraft.entity.EntityLivingBase;
import net.minecraft.item.Item;
import net.minecraftforge.common.MinecraftForge;
import net.minecraftforge.fml.common.event.FMLInitializationEvent;
import net.minecraftforge.fml.common.event.FMLPreInitializationEvent;
import net.minecraftforge.fml.common.network.simpleimpl.MessageContext;

public class CommonProxy
{

	public void preInit(FMLPreInitializationEvent event) {
		Capabilities.init();
		NetworkHandler.init();
	}

	public void init(FMLInitializationEvent event) {
		MinecraftForge.EVENT_BUS.register(new CapabilitiesHandler());
		MinecraftForge.EVENT_BUS.register(new AttachAttributes());
		MinecraftForge.EVENT_BUS.register(new debugMethods());
	}
	public void registerItemRenderer(Item item, int meta, String id)
	{

	}

	@Nullable
	public EntityLivingBase getEntityLivingBase(MessageContext context, int entityID)
	{
		if(context.side.isServer())
		{
			final Entity entity = context.getServerHandler().player.world.getEntityByID(entityID);
			return entity instanceof EntityLivingBase ? (EntityLivingBase) entity : null;
		}
		throw new WrongSideException("Tried to get the player from a client-side MessageContext on the dedicated server");
	}

	class WrongSideException extends RuntimeException
	{
		public WrongSideException(final String message)
		{
			super(message);
		}

		public WrongSideException(final String message, final Throwable cause)
		{
			super(message, cause);
		}
	}
}
