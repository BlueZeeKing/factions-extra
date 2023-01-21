package dev.blueish.factions.extra.mixin;

import com.mojang.brigadier.context.CommandContext;
import dev.blueish.factions.extra.FactionsExtraMod;
import io.icker.factions.command.CreateCommand;
import io.icker.factions.util.Message;
import io.icker.factions.api.persistents.Faction;
import net.minecraft.server.command.ServerCommandSource;
import net.minecraft.server.network.ServerPlayerEntity;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;

@Mixin(value = CreateCommand.class, remap = false)
public class CreateCommandMixin {
    @Inject(method = "run", at = @At(value = "INVOKE", target = "Lio/icker/factions/api/persistents/Faction;<init>(Ljava/lang/String;Ljava/lang/String;Ljava/lang/String;Lnet/minecraft/util/Formatting;ZI)V", remap = false), remap = false, cancellable = true)
    private void run(CommandContext<ServerCommandSource> context, CallbackInfoReturnable<Integer> cir) { // Kinda terrible
        ServerPlayerEntity player = context.getSource().getPlayer();

        if (FactionsExtraMod.CONFIG.MAX_FACTIONS > 0 && Faction.all().size() >= FactionsExtraMod.CONFIG.MAX_FACTIONS) {
            new Message("Too many factions already exist").fail().send(player, false);
            cir.setReturnValue(0);
            return;
        }

        if (!FactionsExtraMod.removeItems(player)) {
            new Message("Please add the required items to your inventory and try again").fail().send(player, false);
            cir.setReturnValue(0);
        }
    }
}
