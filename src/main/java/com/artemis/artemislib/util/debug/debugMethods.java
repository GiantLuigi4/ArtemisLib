package com.artemis.artemislib.util.debug;

import java.util.UUID;

import com.artemis.artemislib.util.attributes.ArtemisLibAttributes;

import net.minecraft.client.Minecraft;
import net.minecraft.entity.Entity;
import net.minecraft.entity.EntityLiving;
import net.minecraft.entity.EntityLivingBase;
import net.minecraft.entity.ai.attributes.AttributeModifier;
import net.minecraft.entity.ai.attributes.IAttributeInstance;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.util.math.RayTraceResult.Type;
import net.minecraftforge.event.entity.living.LivingEvent.LivingUpdateEvent;
import net.minecraftforge.fml.common.eventhandler.SubscribeEvent;
import net.minecraftforge.fml.common.gameevent.TickEvent;
import net.minecraftforge.fml.common.gameevent.TickEvent.Phase;

//@EventBusSubscriber
public class debugMethods
{
	private static UUID uuidH = UUID.fromString("f269dd95-41c1-49b5-ab89-be40c5da69b0");
	private static UUID uuidW = UUID.fromString("0bc6b919-49e9-4f64-8702-20b220ea9d84");
	
	private static int target;
	
	@SubscribeEvent
	public void playerTick(TickEvent.PlayerTickEvent event)
	{
		final EntityPlayer player = event.player;
		final boolean client = player.world.isRemote;
		
		if(event.phase == Phase.END && client)
		{
			if(player.isSneaking() && player.ticksExisted%80==0)
			{
				if(Minecraft.getMinecraft().objectMouseOver != null && Minecraft.getMinecraft().objectMouseOver.typeOfHit == Type.ENTITY)
				{
					final int id = Minecraft.getMinecraft().objectMouseOver.entityHit.getEntityId();
					final EntityLiving entity = (EntityLiving) player.world.getEntityByID(id);

					setTarget(entity.getEntityId());
					System.out.println("Setting Target: Id= " + entity.getEntityId());
				}
			}
		}
	}
	
	@SubscribeEvent
	public void LivingUpdateEvent(LivingUpdateEvent event)
	{
		if(getTarget() != 0)
		{
			System.out.println("Target ID:" + getTarget() + " Found");
			final Entity target = event.getEntityLiving().world.getEntityByID(getTarget());
			
			if(target instanceof EntityLivingBase)
			{
				final EntityLivingBase entity = (EntityLivingBase) event.getEntityLiving().world.getEntityByID(getTarget());
				final IAttributeInstance entityHeight = entity.getAttributeMap().getAttributeInstance(ArtemisLibAttributes.ENTITY_HEIGHT);
				final IAttributeInstance entityWidth = entity.getAttributeMap().getAttributeInstance(ArtemisLibAttributes.ENTITY_WIDTH);
				final AttributeModifier heightModifier = entityHeight.getModifier(uuidH);
				final AttributeModifier widthModifier = entityWidth.getModifier(uuidW);
				
				if(heightModifier == null && widthModifier == null)
				{
					System.out.println("Adding Modifiers");
					entityHeight.applyModifier(constructHeightModifier());
					entityWidth.applyModifier(constructWidthModifier());
				}
				
				if(heightModifier != null && widthModifier != null)
				{
					System.out.println("Removing Modifiers");
					entityHeight.removeModifier(uuidH);
					entityWidth.removeModifier(uuidW);
				}
				
				setTarget(0);
			}
		}
	}
	
	public static AttributeModifier constructHeightModifier()
	{
		return new AttributeModifier(uuidH, "resize", -0.5f, 0);
	}
	public static AttributeModifier constructWidthModifier()
	{
		return new AttributeModifier(uuidW, "resize", -0.5f, 0);
	}
	
	public static int getTarget()
	{
		return target;
	}
	
	public static void setTarget(int target)
	{
		debugMethods.target = target;
	}
}
