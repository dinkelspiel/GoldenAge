package dev.keii.goldenage.betaprotect.listeners;

import dev.keii.goldenage.GoldenAge;
import dev.keii.goldenage.betaprotect.BetaProtect;
import dev.keii.goldenage.betaprotect.dao.BlockTransactionDao;
import dev.keii.goldenage.betaprotect.models.BlockTransaction;
import dev.keii.goldenage.betaprotect.models.TransactionAction;
import dev.keii.goldenage.betaprotect.models.TransactionActor;
import dev.keii.goldenage.dao.UserDao;
import dev.keii.goldenage.dao.UserNameDao;
import dev.keii.goldenage.dao.WorldDao;
import dev.keii.goldenage.models.User;
import dev.keii.goldenage.models.UserName;
import dev.keii.goldenage.models.World;
import dev.keii.goldenage.utils.DateUtility;
import org.bukkit.Material;
import org.bukkit.block.Block;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.block.Action;
import org.bukkit.event.player.PlayerInteractEvent;

import java.time.LocalDateTime;
import java.time.ZoneOffset;
import java.util.Arrays;
import java.util.List;

public class PlayerInteractListener implements Listener {
    private final GoldenAge plugin;
    private final BetaProtect betaProtect;

    public PlayerInteractListener(BetaProtect betaProtect) {
        this.plugin = betaProtect.getPlugin();
        this.betaProtect = betaProtect;
    }

    @EventHandler
    public void onPlayerInteract(PlayerInteractEvent event) throws Exception {
        Player player = event.getPlayer();
        UserDao userDao = new UserDao(plugin.getDatabaseUtility());
        User user = userDao.getUserByUuid(player.getUniqueId());
        if (user == null) {
            throw new Exception("Player does not exist");
        }

        BlockTransactionDao blockTransactionDao = new BlockTransactionDao(plugin.getDatabaseUtility());
        Block block = event.getClickedBlock();

        if (event.getAction() == Action.RIGHT_CLICK_BLOCK) {

            if (Arrays.asList(Material.CHEST, Material.DISPENSER, Material.FURNACE, Material.BURNING_FURNACE, Material.STONE_BUTTON, Material.LEVER, Material.TRAP_DOOR, Material.WOODEN_DOOR).contains(block.getType())) {
                WorldDao worldDao = new WorldDao(plugin.getDatabaseUtility());
                World world = worldDao.getWorldByUuid(block.getWorld().getUID());
                if (world == null) {
                    throw new Exception("World does not exist");
                }

                BlockTransaction blockTransaction = new BlockTransaction(TransactionActor.Player, TransactionAction.Interact, user, block, world, LocalDateTime.now(ZoneOffset.UTC));
                blockTransactionDao.insertBlockTransaction(blockTransaction);
            }
        } else if (event.getAction() == Action.LEFT_CLICK_BLOCK) {
            if (this.betaProtect.usersInInteractMode.contains(user.getId())) {
                List<BlockTransaction> transactions = blockTransactionDao.getBlockTransactionsAtLocation(block.getX(), block.getY(), block.getZ(), 10, 0);

                UserNameDao userNameDao = new UserNameDao(plugin.getDatabaseUtility());


                String title = plugin.getConfig().getBetaProtect().getCommands().getBetaProtect().getInspect().getBlockTitle();
                title = title.replace("%x", String.valueOf(block.getX()));
                title = title.replace("%y", String.valueOf(block.getY()));
                title = title.replace("%z", String.valueOf(block.getZ()));

                player.sendMessage(title);
                for (BlockTransaction blockTransaction : transactions) {

                    String entry = plugin.getConfig().getBetaProtect().getCommands().getBetaProtect().getInspect().getBlockEntry();
                    entry = entry.replace("%d", DateUtility.getHumanReadableTimeSpan(blockTransaction.getCreatedAt().toEpochSecond(ZoneOffset.UTC), true));

                    switch (blockTransaction.getActor()) {
                        case Player:
                            UserName userName = userNameDao.getUserNameByUser(blockTransaction.getUser());
                            entry = entry.replace("%a", userName.getName());
                            break;
                        case Creeper:
                            entry = entry.replace("%a", "CREEPER");
                            break;
                        case Fire:
                            entry = entry.replace("%a", "FIRE");
                            break;
                        case Fireball:
                            entry = entry.replace("%a", "FIREBALL");
                            break;
                        case PrimedTNT:
                            entry = entry.replace("%a", "TNT");
                            break;
                        case Unknown:
                            entry = entry.replace("%a", "UNKNOWN");
                            break;
                        default:
                            break;

                    }

                    switch (blockTransaction.getAction()) {
                        case Add:
                            entry = entry.replace("%r", "placed");
                            break;
                        case Interact:
                            entry = entry.replace("%r", "interacted with");
                            break;
                        case Remove:
                            entry = entry.replace("%r", "removed");
                            break;
                        default:
                            break;

                    }

                    StringBuilder id = new StringBuilder();
                    id.append(blockTransaction.getBlockId());
                    if (blockTransaction.getBlockData() != 0) {
                        id.append(":");
                        id.append(blockTransaction.getBlockData());
                    }
                    entry = entry.replace("%i", id.toString());
                    entry = entry.replace("%b", Material.getMaterial(blockTransaction.getBlockId()).toString());

                    player.sendMessage(entry);
                }
            }
        }
    }
}
