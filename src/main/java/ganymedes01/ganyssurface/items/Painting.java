package ganymedes01.ganyssurface.items;

import ganymedes01.ganyssurface.GanysSurface;
import ganymedes01.ganyssurface.ModItems;
import ganymedes01.ganyssurface.core.utils.InventoryUtils;
import ganymedes01.ganyssurface.core.utils.Utils;
import ganymedes01.ganyssurface.lib.Strings;

import java.util.List;

import net.minecraft.client.renderer.texture.IIconRegister;
import net.minecraft.creativetab.CreativeTabs;
import net.minecraft.entity.Entity;
import net.minecraft.entity.EntityHanging;
import net.minecraft.entity.item.EntityPainting;
import net.minecraft.entity.item.EntityPainting.EnumArt;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.init.Items;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.util.Direction;
import net.minecraft.util.IIcon;
import net.minecraft.util.StatCollector;
import net.minecraft.world.World;
import cpw.mods.fml.relauncher.Side;
import cpw.mods.fml.relauncher.SideOnly;

/**
 * Gany's Surface
 *
 * @author ganymedes01
 *
 */

public class Painting extends Item {

	public Painting() {
		setMaxDamage(0);
		setHasSubtypes(true);
		setUnlocalizedName(Utils.getUnlocalizedName(Strings.PAINTING_NAME));
		setCreativeTab(GanysSurface.enablePaintings ? GanysSurface.surfaceTab : null);
	}

	@Override
	@SideOnly(Side.CLIENT)
	public void registerIcons(IIconRegister reg) {
	}

	@Override
	@SideOnly(Side.CLIENT)
	public IIcon getIconFromDamage(int meta) {
		return Items.painting.getIconFromDamage(0);
	}

	@Override
	@SideOnly(Side.CLIENT)
	@SuppressWarnings({ "rawtypes", "unchecked" })
	public void getSubItems(Item item, CreativeTabs tabs, List list) {
		for (int i = 0; i < EnumArt.values().length; i++)
			list.add(new ItemStack(item, 1, i));
	}

	@Override
	public String getItemStackDisplayName(ItemStack stack) {
		return StatCollector.translateToLocal("item.painting.name") + " (" + EnumArt.values()[getMeta(stack)].toString() + ")";
	}

	public static int getMeta(ItemStack stack) {
		return Math.max(Math.min(stack.getItemDamage(), EnumArt.values().length - 1), 0);
	}

	@Override
	public boolean onItemUse(ItemStack stack, EntityPlayer player, World world, int x, int y, int z, int side, float hitX, float hitY, float hitZ) {
		if (side == 0 || side == 1)
			return false;

		EntityHanging painting = createPainting(world, x, y, z, Direction.facingToDirection[side], getMeta(stack));
		if (painting != null)
			if (!player.canPlayerEdit(x, y, z, side, stack))
				return false;
			else {
				if (painting != null && painting.onValidSurface()) {
					if (!world.isRemote)
						world.spawnEntityInWorld(painting);
					stack.stackSize--;
				}

				return true;
			}

		return false;
	}

	private EntityPainting createPainting(World world, int x, int y, int z, int direction, int type) {
		EntityPainting painting = new EntityFixedPainting(world, x, y, z, direction);
		painting.art = EnumArt.values()[type];
		if (painting.onValidSurface()) {
			painting.setDirection(direction);
			return painting;
		}
		return null;
	}

	public class EntityFixedPainting extends EntityPainting {

		public EntityFixedPainting(World world, int x, int y, int z, int direction) {
			super(world, x, y, z, direction);
		}

		@Override
		public void onBroken(Entity attacker) {
			ItemStack stack = new ItemStack(ModItems.painting, 1, art.ordinal());
			if (attacker instanceof EntityPlayer) {
				EntityPlayer player = (EntityPlayer) attacker;
				if (!player.capabilities.isCreativeMode)
					InventoryUtils.addToPlayerInventory(player, stack, player.posX, player.posY, player.posZ);
			} else
				entityDropItem(stack, 0.0F);
		}
	}
}