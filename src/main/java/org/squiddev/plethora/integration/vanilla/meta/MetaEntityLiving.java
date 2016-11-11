package org.squiddev.plethora.integration.vanilla.meta;

import com.google.common.collect.Maps;
import dan200.computercraft.api.lua.ILuaObject;
import net.minecraft.entity.EntityLivingBase;
import net.minecraft.potion.PotionEffect;
import net.minecraft.util.ResourceLocation;
import org.squiddev.plethora.api.PlethoraAPI;
import org.squiddev.plethora.api.meta.BasicMetaProvider;
import org.squiddev.plethora.api.meta.IMetaProvider;
import org.squiddev.plethora.api.method.CostHelpers;
import org.squiddev.plethora.api.reference.Reference;

import javax.annotation.Nonnull;
import java.util.Collection;
import java.util.Collections;
import java.util.Map;

import static org.squiddev.plethora.api.reference.Reference.entity;
import static org.squiddev.plethora.api.reference.Reference.id;

/**
 * A basic provider for living entities
 */
@IMetaProvider.Inject(EntityLivingBase.class)
public class MetaEntityLiving extends BasicMetaProvider<EntityLivingBase> {
	@Nonnull
	@Override
	public Map<Object, Object> getMeta(@Nonnull EntityLivingBase target) {
		Map<Object, Object> map = Maps.newHashMap();

		{
			Map<String, Object> armor = Maps.newHashMap();
			armor.put("boots", wrap(target, target.getEquipmentInSlot(1)));
			armor.put("leggings", wrap(target, target.getEquipmentInSlot(2)));
			armor.put("chestplate", wrap(target, target.getEquipmentInSlot(3)));
			armor.put("helmet", wrap(target, target.getEquipmentInSlot(4)));
			map.put("armor", armor);
		}

		map.put("heldItem", wrap(target, target.getEquipmentInSlot(0)));

		{
			Map<Object, String> potionEffects = Maps.newHashMap();
			@SuppressWarnings("unchecked")
			Collection<PotionEffect> effects = target.getActivePotionEffects();

			int count = 1;
			for (PotionEffect effect : effects) {
				potionEffects.put(count++, effect.getEffectName());
			}
			map.put("potionEffects", potionEffects);
		}

		map.put("health", target.getHealth());
		map.put("maxHealth", target.getMaxHealth());
		map.put("isAirborne", target.isAirBorne);
		map.put("isBurning", target.isBurning());
		map.put("isAlive", target.isEntityAlive());
		map.put("isInWater", target.isInWater());
		map.put("isOnLadder", target.isOnLadder());
		map.put("isSleeping", target.isPlayerSleeping());
		map.put("isRiding", target.isRiding());
		map.put("isSneaking", target.isSneaking());
		map.put("isSprinting", target.isSprinting());
		map.put("isWet", target.isWet());
		map.put("isChild", target.isChild());
		map.put("isDead", target.isDead);

		return map;
	}

	private static <T> ILuaObject wrap(EntityLivingBase entity, T object) {
		if (object == null) return null;

		return PlethoraAPI.instance().methodRegistry().makeContext(
			id(object),
			CostHelpers.getCostHandler(entity),
			Reference.id(Collections.<ResourceLocation>emptySet()),
			entity(entity)
		).getObject();
	}
}
