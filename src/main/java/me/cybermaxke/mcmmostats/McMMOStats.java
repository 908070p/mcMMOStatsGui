/**
 * 
 * This software is part of the mcMMOStatsGui
 * 
 * mcMMOStatsGui is free software: you can redistribute it and/or modify
 * it under the terms of the GNU General Public License as published by
 * the Free Software Foundation, either version 3 of the License, or 
 * any later version.
 *  
 * mcMMOStatsGui is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE. See the
 * GNU General Public License for more details.
 *
 * You should have received a copy of the GNU General Public License
 * along with mcMMOStatsGui. If not, see <http://www.gnu.org/licenses/>.
 * 
 */
package me.cybermaxke.mcmmostats;

import java.util.HashMap;
import java.util.Map;

import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerJoinEvent;
import org.bukkit.event.player.PlayerQuitEvent;
import org.bukkit.plugin.java.JavaPlugin;

import com.gmail.nossr50.datatypes.skills.SkillType;
import com.gmail.nossr50.events.experience.McMMOPlayerLevelUpEvent;
import com.gmail.nossr50.events.experience.McMMOPlayerXpGainEvent;

public class McMMOStats extends JavaPlugin implements Listener {
	private static Map<SkillType, String> skillNames = new HashMap<SkillType, String>();
	private static Map<String, StatsGui> statsGui = new HashMap<String, StatsGui>();
	private static McMMOStats instance;

	@Override
	public void onEnable() {
		instance = this;

		this.getServer().getPluginManager().registerEvents(this, this);
		new GuiCommand(this);

		skillNames.put(SkillType.ACROBATICS, "Acrobatics");
		skillNames.put(SkillType.ARCHERY, "Archery");
		skillNames.put(SkillType.AXES, "Axes");
		skillNames.put(SkillType.EXCAVATION, "Exavation");
		skillNames.put(SkillType.FISHING, "Fishing");
		skillNames.put(SkillType.HERBALISM, "Herbalism");
		skillNames.put(SkillType.MINING, "Mining");
		skillNames.put(SkillType.REPAIR, "Repairing");
		skillNames.put(SkillType.SWORDS, "Swords");
		skillNames.put(SkillType.TAMING, "Taming");
		skillNames.put(SkillType.UNARMED, "Unarmed");
		skillNames.put(SkillType.WOODCUTTING, "Woodcutting");
	}

	@Override
	public void onDisable() {

	}

	public static McMMOStats getInstance() {
		return instance;
	}

	public static String getSkillName(SkillType type) {
		return skillNames.containsKey(type) ? skillNames.get(type) : null;
	}

	public static StatsGui getGui(Player player) {
		return statsGui.get(player.getName());
	}

	@EventHandler
	public void onPlayerJoin(PlayerJoinEvent e) {
		Player p = e.getPlayer();
		statsGui.put(p.getName(), new StatsGui(p));
	}

	@EventHandler
	public void onPlayerQuit(PlayerQuitEvent e) {
		Player p = e.getPlayer();
		statsGui.get(p.getName()).removeUpdater();
		statsGui.remove(p.getName());
	}

	@EventHandler
	public void onMcMMOPlayerLevelUp(McMMOPlayerLevelUpEvent e) {
		getGui(e.getPlayer()).sendSkillStats(e.getSkill());
	}

	@EventHandler
	public void onMcMMOPlayerXpGain(McMMOPlayerXpGainEvent e) {
		getGui(e.getPlayer()).sendSkillStats(e.getSkill());
	}
}