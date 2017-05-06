package net.powerkg.mailbox;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.Comparator;
import java.util.HashMap;

import org.bukkit.Bukkit;
import org.bukkit.Material;
import org.bukkit.entity.Player;
import org.bukkit.event.inventory.InventoryAction;
import org.bukkit.event.inventory.InventoryClickEvent;
import org.bukkit.event.inventory.InventoryCloseEvent;
import org.bukkit.event.inventory.InventoryOpenEvent;
import org.bukkit.inventory.ItemStack;

import me.kg.easygui.EasyGui;
import net.powerkg.mailbox.mail.IMail;
import net.powerkg.market.file.MarketConfig;
import net.powerkg.utils.Tools;

public class GuiMailBox extends EasyGui
{
	private static ItemStack next/*上一页*/
			, sp/*分割*/
			, pre;/*下一页*/

	private Comparator<IMail> comparator = new Sorter();
	private ArrayList<IMail> orMails = new ArrayList<>();

	private MailBox box;
	private static HashMap<Integer, IMail> mailMap = new HashMap<>();

	private int pageNow = 1;
	private int pageMax = 1;

	private boolean hasSlider = false;

	static
	{
		next = new ItemStack(Material.STAINED_GLASS_PANE, 1, (short) 5);
		Tools.setItemStack(next, "§2" + MarketConfig.translate("Page Up"), null);
		sp = new ItemStack(Material.STAINED_GLASS_PANE, 1, (short) 15);
		Tools.setItemStack(sp, "§7-", null);
		pre = new ItemStack(Material.STAINED_GLASS_PANE, 1, (short) 5);
		Tools.setItemStack(pre, "§2" + MarketConfig.translate("Page Down"), null);
	}

	public GuiMailBox(Player p)
	{
		super(p);

		inv = Bukkit.createInventory(null, 36, "§9" + p.getName() + MarketConfig.translate("sMailbox"));
		box = MailBoxHandler.getMailBox(p);

		orMails.addAll(box.getMails());

		freashMailboxContent();
	}

	private void freashMailboxContent()
	{
		inv.clear();
		ArrayList<IMail> mails = new ArrayList<>();
		mails.addAll(box.getMails());
		Collections.sort(mails, comparator);

		pageMax = mails.size() / 27 + (mails.size() % 27 == 0 ? 0 : 1);

		if (mails.size() > 36)
		{
			hasSlider = true;
			refreashBar();

			mailMap.clear();

			for (int i = 0; i < (pageNow < pageMax ? 27 : mails.size() % 27); i++)
			{
				IMail mail = mails.get((pageNow - 1) * 27 + i);
				inv.setItem(i, mail.getDisplay());
				mailMap.put(i, mail);
			}
		} else
		{
			hasSlider = false;
			for (int i = 0; i < mails.size(); i++)
			{
				IMail mail = mails.get(i);
				inv.setItem(i, mail.getDisplay());
				mailMap.put(i, mail);
			}
		}
	}

	private void refreashBar()
	{
		ItemStack fnext = next.clone(), fpre = pre.clone(), fsp = sp.clone();
		Tools.setItemStack(fnext, null, Arrays.asList("§9" + MarketConfig.translate("Now Page Is") + " §6§l[" + pageNow + " §6/ §l" + pageMax + "]"));
		Tools.setItemStack(fpre, null, Arrays.asList("§9" + MarketConfig.translate("Now Page Is") + " §6§l[" + pageNow + " §6/ §l" + pageMax + "]"));
		Tools.setItemStack(fsp, "§9" + MarketConfig.translate("Now Page Is") + " §6§l[" + pageNow + " §6/ §l" + pageMax + "]", null);

		if (pageNow == pageMax)
			inv.setItem(35, fsp);
		else
			inv.setItem(35, fnext);
		if (pageNow == 1)
			inv.setItem(27, fsp);
		else
			inv.setItem(27, fpre);

		for (int i = 0; i < 7; i++)
		{
			inv.setItem(28 + i, fsp);
		}
	}

	private void tryToSave()
	{
		if (orMails.size() == box.getMails().size())
		{
			ArrayList<IMail> mails = box.getMails();
			boolean i = false;
			for (IMail mail : orMails)
			{
				if (!mails.contains(mail))
				{
					i = true;
					break;
				}
			}
			if (!i)
				return;
		}
		box.save();
	}

	@Override
	public void onVerifiedEvent(InventoryClickEvent event)
	{
		event.setCancelled(true);
		int slot = event.getSlot();
		boolean isRightClick = false;

		if (event.getAction().equals(InventoryAction.PICKUP_HALF))
		{
			isRightClick = true;
		}

		if (hasSlider)
			if (slot == 27 || slot == 35)
			{
				int newPage = pageNow;

				if (slot == 27)
				{
					if (newPage - 1 <= 0)
						newPage = pageMax;
					--newPage;
				}
				if (slot == 35)
				{
					if (newPage + 1 > pageMax)
						newPage = 1;
					++newPage;
				}

				if (newPage != pageNow)
				{
					pageNow = newPage;
					freashMailboxContent();
				}
				return;
			}

		if (mailMap.containsKey(slot))
		{
			if (mailMap.get(slot).click((Player) event.getWhoClicked(), isRightClick))
			{
				event.setCancelled(true);
			} else
			{
				freashMailboxContent();
			}
		}
	}

	@Override
	public void onClose(InventoryCloseEvent event)
	{
		tryToSave();
	}

	@Override
	public void onForceClose()
	{
		tryToSave();
	};

	@Override
	public void onOpen(InventoryOpenEvent event)
	{

	}

}

class Sorter implements Comparator<IMail>
{

	@Override
	public int compare(IMail o1, IMail o2)
	{
		boolean o1p = o1.getMark().equalsIgnoreCase("MailPost"), o2p = o2.getMark().equalsIgnoreCase("MailPost");
		return (o1p && !o2p ? -1 : !o1p && o2p ? 1 : 0);
	}

}
