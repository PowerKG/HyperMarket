package me.kg.fastuse;

import java.util.HashMap;
import java.util.UUID;

import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.EventPriority;
import org.bukkit.event.Listener;
import org.bukkit.event.player.AsyncPlayerChatEvent;
import org.bukkit.event.player.PlayerQuitEvent;
import org.bukkit.plugin.Plugin;

public class FastUse implements Listener
{
	private static FastUse instance;

	private static HashMap<UUID, IResultHandler> jamChatMap = new HashMap<>();

	private FastUse()
	{

	}

	public static void init(Plugin plugin)
	{
		instance = new FastUse();
		plugin.getServer().getPluginManager().registerEvents(instance, plugin);
	}

	private static boolean isJamming(JamType type, UUID uuid)
	{
		if (type == JamType.chat)
		{
			return jamChatMap.containsKey(uuid);
		}
		return false;
	}

	private static IResultHandler getJamming(JamType type, UUID uuid)
	{
		if (type == JamType.chat)
			return jamChatMap.get(uuid);
		return null;
	}

	private static void clear(JamType type, UUID uuid)
	{
		if (type == JamType.chat)
		{
			jamChatMap.remove(uuid);
		}
	}

	private static boolean isJamming(UUID uuid)
	{
		return jamChatMap.containsKey(uuid);
	}

	private static void clear(UUID uuid)
	{
		jamChatMap.remove(uuid);
	}

	public static void jamMsg(Player player, IResultHandler handler)
	{
		if (jamChatMap.containsKey(player.getUniqueId()))
		{
			respond(player.getUniqueId(), JamType.chat, new JamResult(null, JamResult.extrude));
		}
		jamChatMap.put(player.getUniqueId(), handler);
	}

	private static boolean respond(UUID uuid, JamType type, JamResult obj)
	{
		if (isJamming(type, uuid))
		{
			IResultHandler handler = getJamming(type, uuid);
			clear(type, uuid);
			if (handler != null)
				handler.handld(type, obj);
			else
				return false;
			return true;
		}
		return false;
	}

	private static boolean respond(UUID uuid, JamResult obj)
	{
		for (JamType type : JamType.values())
		{
			if (isJamming(type, uuid))
			{
				IResultHandler handler = getJamming(type, uuid);
				clear(type, uuid);
				if (handler != null)
					handler.handld(type, obj);
				else
					return false;
				return true;
			}
		}
		return false;
	}

	@EventHandler(priority = EventPriority.HIGHEST, ignoreCancelled = true)
	public void onPlayerQuit(PlayerQuitEvent event)
	{
		UUID pUUID = event.getPlayer().getUniqueId();
		respond(pUUID, new JamResult(null, JamResult.exit));
	}

	@EventHandler(priority = EventPriority.HIGHEST, ignoreCancelled = true)
	public void onChat(AsyncPlayerChatEvent event)
	{
		UUID pID = event.getPlayer().getUniqueId();
		if (isJamming(JamType.chat, pID))
		{
			event.setCancelled(true);
			String msg = event.getMessage();
			respond(pID, JamType.chat, new JamResult(msg, JamResult.successful));
		}
	}

}
