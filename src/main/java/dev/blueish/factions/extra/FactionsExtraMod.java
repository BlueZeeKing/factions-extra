package dev.blueish.factions.extra;

import net.fabricmc.api.ModInitializer;
import net.minecraft.inventory.Inventories;
import net.minecraft.inventory.SimpleInventory;
import net.minecraft.item.ItemStack;
import net.minecraft.registry.Registries;
import net.minecraft.server.network.ServerPlayerEntity;
import net.minecraft.util.Identifier;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import java.util.stream.Stream;

public class FactionsExtraMod implements ModInitializer {
	public static final Logger LOGGER = LoggerFactory.getLogger("factions-money");
	public static Config CONFIG = Config.load();

	@Override
	public void onInitialize() {

	}

	public static boolean removeItems(ServerPlayerEntity player) {
		if (hasItems(player)) {
			for (ItemStack itemStack : getItems().toList()) {
				if (player.getInventory().remove((itemStack1 -> itemStack1.isOf(itemStack.getItem())), itemStack.getCount(), new SimpleInventory()) < itemStack.getCount()) {
					FactionsExtraMod.LOGGER.error(String.format("Cannot remove items properly for player %s", player.getName().getString()));
					return false;
				}
			}

			return true;
		}

		return false;
	}

	public static boolean hasItems(ServerPlayerEntity player) {
		return getItems().allMatch((itemStack) -> Inventories.remove(player.getInventory(), (itemStack1 -> itemStack1.isOf(itemStack.getItem())), itemStack.getCount(), true) >= itemStack.getCount());
	}

	public static Stream<ItemStack> getItems() {
		return CONFIG.CREATE_COST.entrySet().stream().map((entry) -> new ItemStack(Registries.ITEM.get(new Identifier(entry.getKey())), entry.getValue()));
	}
}
